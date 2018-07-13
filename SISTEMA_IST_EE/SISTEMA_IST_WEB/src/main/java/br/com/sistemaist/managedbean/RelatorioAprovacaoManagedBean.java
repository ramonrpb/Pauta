package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.entidades.AlunoAvaliacao;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Aula;
import br.com.sistemaist.entidades.Avaliacao;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AlunoAvaliacaoService;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.service.AulaService;
import br.com.sistemaist.service.AvaliacaoService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Constante;
import br.com.sistemaist.util.Util;

@ViewScoped
@ManagedBean(name = "relatorioAprovacaoMB")
public class RelatorioAprovacaoManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	@EJB
	private AnoLetivoService anoLetivoService;
	@EJB
	AulaService aulaService;
	@EJB
	private TurmaService turmaService;

	@EJB
	private AvaliacaoService avaliacaoService;

	@EJB
	private AlunoAvaliacaoService alunoAvaliacaoService;

	
	private List<Turma> listaTurmas;
	private List<AnoLetivo> listaAnoLetivo;
	private AnoLetivo anoLetivo;
	
	private List<AlunoAvaliacao> listaAlunoAvaliacao;
	
	private TreeMap<Aluno, Map<Long, Avaliacao>> avaliacoes;
	private Turma turma = new Turma();
	private Long idTurma;

	private Map<Aluno, Map<Avaliacao, AlunoAvaliacao>> listaAlunoAvaliacaoMap = new HashMap<Aluno, Map<Avaliacao,AlunoAvaliacao>>();

	@PostConstruct
	public void carregarTela() {
		
		log.info("*****************************Locale: " +Locale.getDefault());
		if(null == listaTurmas){
			listaTurmas = new ArrayList<Turma>();
		}
		
		try {
			Ator ator = Util.pegarAtor();
//			if(null != ator && ator.getPerfil().equals(PerfilEnum.AD)){
//				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertosAteHoje();
//			}else{
				listaAnoLetivo = anoLetivoService.buscarAnosLetivos();
//			}
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemFlashRedirectErro("Um erro inesperado aconteceu, tente mais tarde!", "ERRO");
		}
		if(null == listaAnoLetivo){
			listaAnoLetivo = new ArrayList<AnoLetivo>();
		}
		anoLetivo = new AnoLetivo();
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idTurma = request.getParameter("idTurma");
		
		if(idTurma != null) {
			
			this.idTurma = Long.valueOf(idTurma);
			carregarAlunoAvaliacao();
			anoLetivo = getTurma().getAnoLetivo();
			filtrarTurmasPorAnoLetivo();
			
		}
		
	}
	
	public void filtrarTurmasPorAnoLetivo(){
		//buscar turmas do professor
		Ator ator = Util.pegarAtor();
		try {
			if(ator.getPerfil().equals(PerfilEnum.AD)){
				listaTurmas = turmaService.listarTurmaPorIdAnoLetivo(anoLetivo.getId());
			}else{
				listaTurmas = turmaService.listarTurmaPorIdProfessorEAnoLetivo(ator.getId(), anoLetivo.getId());
			}
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
		
		if(null == listaTurmas || listaTurmas.isEmpty()){
			listaTurmas = new ArrayList<Turma>();
			if(null != anoLetivo.getId()){
				Util.montaMensagemFlashRedirectErro("Não há turmas cadastradas para este ano letivo.", "Erro");
			}
		}
		
	}
	
	public void carregarAlunoAvaliacao() {
		
		try {
			turma = turmaService.buscarTurmaPorIdComAvaliacao(idTurma);
			if (avaliacoes == null) {
				avaliacoes = new TreeMap<Aluno, Map<Long, Avaliacao>>();
			}
			listaAlunoAvaliacao = alunoAvaliacaoService.carregarAlunoAvaliacaoPorTurma(turma.getId());
			if(turma.getListaAvaliacao() == null || turma.getListaAvaliacao().isEmpty()) {
				turma.setListaAvaliacao(new ArrayList<Avaliacao>());
				
				adicionarAvaliacoesPadrao();
				adicionarPF();
			}
			for (AlunoTurma at : turma.getAlunoTurma()) {
				Map<Long, Avaliacao> listaAvaliacoes = new HashMap<Long, Avaliacao>();
				Map<Avaliacao, AlunoAvaliacao> listaAvaliacaoPorAluno = new HashMap<Avaliacao, AlunoAvaliacao>();
				for (Avaliacao a : turma.getListaAvaliacao()) {
					listaAvaliacoes.put(a.getId(), a);
					AlunoAvaliacao aa = retornarAlunoAvaliacao(at, a);
					if (aa == null) {
						AlunoAvaliacao alunoAv = new AlunoAvaliacao(
								at.getAluno(), a, null);
						listaAvaliacaoPorAluno.put(a, alunoAv);
					} else {
						listaAvaliacaoPorAluno.put(a, aa);
					}

				}

				avaliacoes.put(at.getAluno(), listaAvaliacoes);

				listaAlunoAvaliacaoMap.put(at.getAluno(),
						listaAvaliacaoPorAluno);
			}

			for (Aluno a : avaliacoes.keySet()) {
				a.setMedia(calcularMedia(a));
			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
	
		filtrarListaAlunoAulaPorTurma();
		
	}
	
	private void adicionarAvaliacoesPadrao() throws ExcecaoGenerica {
		
		Avaliacao av1 = new Avaliacao("AV1", 5, turma);
		//avaliacaoService.salvar(av1);
		Avaliacao av2 = new Avaliacao("AV2", 5, turma);
		//avaliacaoService.salvar(av2);
		//Atualiza a turma após salvar as avaliações
		turma.getListaAvaliacao().add(av1);
		turma.getListaAvaliacao().add(av2);
	
	}

	private void adicionarPF() {
		
		Boolean existePF = false;
		Avaliacao provaFinal = null;
		for(Avaliacao a : turma.getListaAvaliacao()) {
			if(Constante.NOME_PROVA_FINAL.equals(a.getNome())) {
				existePF = true;
				provaFinal = a;
				break;
			}
		}
		if(!existePF) {
			Avaliacao av = new Avaliacao();
			av.setNome(Constante.NOME_PROVA_FINAL);
			av.setPeso(10);
			av.setTurma(turma);
			turma.getListaAvaliacao().add(av);
		} else {
			//para ordenar e ficar a prova final por último
			turma.getListaAvaliacao().remove(provaFinal);
			turma.getListaAvaliacao().add(provaFinal);
		}
	}

	private AlunoAvaliacao retornarAlunoAvaliacao(AlunoTurma at, Avaliacao a) {
		if (listaAlunoAvaliacao != null && listaAlunoAvaliacao.size() != 0) {
			
			for(AlunoAvaliacao aa: listaAlunoAvaliacao) {
				if(aa.getAvaliacao().getId().equals(a.getId()) && aa.getAluno().getId().equals(at.getAluno().getId())) {
					return aa;
				}
			}
			
		}
		return null;
	}
	
	public String salvar() {
		
		List<AlunoAvaliacao> listaAlunoAvaliacao = new ArrayList<AlunoAvaliacao>();
		List<AlunoAvaliacao> listaAlunoAvaliacaoRemover = new ArrayList<AlunoAvaliacao>();
		Collection<Map<Avaliacao, AlunoAvaliacao>> avaliacoes = listaAlunoAvaliacaoMap.values();
		ArrayList<Map<Avaliacao, AlunoAvaliacao>> listaAvaliacaoAlunoAvaliacao = new ArrayList<Map<Avaliacao,AlunoAvaliacao>>();
		listaAvaliacaoAlunoAvaliacao.addAll(avaliacoes);
		for(Map<Avaliacao, AlunoAvaliacao> aaa : listaAvaliacaoAlunoAvaliacao) {
			for(Avaliacao a: aaa.keySet()) {
				if(a.getId() == null) {
					try {
						avaliacaoService.salvar(a);
					} catch (ExcecaoGenerica e) {
						log.error(e.getMessage(), e);
					}
				}
				if(aaa.get(a).getNota() != null) {
					listaAlunoAvaliacao.add(aaa.get(a));
				} else {
					listaAlunoAvaliacaoRemover.add(aaa.get(a));
				}
			}
		}
		
		try {
			alunoAvaliacaoService.salvarListaAlunoAvaliacao(listaAlunoAvaliacao);
			alunoAvaliacaoService.removerAvaliacoes(listaAlunoAvaliacaoRemover);
			Util.montaMensagemFlashRedirect("Notas lançadas com sucesso!", "Sucesso");
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemFlashRedirect("Não foi possível salvar o lançamento das notas", "Erro");
		}
		
		return "cadastroNotas";
	}
	
	public String calcularMedia(Aluno aluno) {
		Double media = Double.valueOf(0);
		Double mediaPF = Double.valueOf(0);
		Boolean existePF = false;
		for(AlunoAvaliacao aa : listaAlunoAvaliacaoMap.get(aluno).values()) {
			if(!Constante.NOME_PROVA_FINAL.equals(aa.getAvaliacao().getNome())) {
				if(aa.getNota() != null) {
					media = media + (aa.getNota() * aa.getAvaliacao().getPeso()) / 10;
				}
			} else {
				if(aa.getNota() != null) {
					existePF = true;
					mediaPF = aa.getNota();
				}
			}
		}
		
		if(existePF) {
			
			String mediaCalculada = Double.valueOf((media + mediaPF) / 2).toString();
			aluno.setMedia(mediaCalculada);
			return mediaCalculada;
		} else {
			aluno.setMedia(media.toString());
			return media.toString();
		}
	}
	
	public void filtrarListaAlunoAulaPorTurma() {

		try {
			List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();

			Aula aula = new Aula();
			aula.setTurma(turma);

			if (null != aula.getTurma().getId()) {

				aula = aulaService.carregarFrequenciaPorPeriodo(aula);
				listaAlunoAula = aula.getListaAlunoAula();

				if (null == listaAlunoAula || listaAlunoAula.isEmpty()) {
					Long idGuardado = aula.getTurma().getId();
					aula.setTurma(new Turma());
					aula.getTurma().setId(idGuardado);
				} else {

					for (AlunoTurma at : turma.getAlunoTurma()) {
						for (AlunoAula aa : aula.getListaAlunoAula()) {
							if (at.getAluno().getId()
									.equals(aa.getAluno().getId())) {
								at.getAluno().setQtdPresenca(
										aa.getAluno().getQtdPresenca());
								at.getAluno().setTotalPresenca(
										aa.getAluno().getTotalPresenca());
								at.getAluno().setTotalFalta(
										aa.getAluno().getTotalFalta());
								at.getAluno().setTotalFrequencia(
										aa.getAluno().getTotalFrequencia());
							}
						}
					}

				}

			} else {
				aula.setTurma(new Turma());
				aula.getTurma().setAnoLetivo(new AnoLetivo());
			}

		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro(
					"Erro ao filtrar a lista de alunos por turma", "Erro");
		}

	}


	public List<Turma> getListaTurmas() {
		return listaTurmas;
	}

	public void setListaTurmas(List<Turma> listaTurmas) {
		this.listaTurmas = listaTurmas;
	}

	public List<AnoLetivo> getListaAnoLetivo() {
		return listaAnoLetivo;
	}

	public void setListaAnoLetivo(List<AnoLetivo> listaAnoLetivo) {
		this.listaAnoLetivo = listaAnoLetivo;
	}

	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}

	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public TreeMap<Aluno, Map<Long, Avaliacao>> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(TreeMap<Aluno, Map<Long, Avaliacao>> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Long getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(Long idTurma) {
		this.idTurma = idTurma;
	}

	public Map<Aluno, Map<Avaliacao, AlunoAvaliacao>> getListaAlunoAvaliacaoMap() {
		return listaAlunoAvaliacaoMap;
	}

	public void setListaAlunoAvaliacaoMap(
			Map<Aluno, Map<Avaliacao, AlunoAvaliacao>> listaAlunoAvaliacaoMap) {
		this.listaAlunoAvaliacaoMap = listaAlunoAvaliacaoMap;
	}

}
