package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Avaliacao;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.service.AvaliacaoService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Util;

@ViewScoped
@ManagedBean(name="avaliacaoMB")
public class AvaliacaoManagedBean implements Serializable {

	private static final String ACAO_INCLUIR = "INCLUIR";
	private static final String ACAO_EDITAR = "EDITAR";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	@EJB
	private AnoLetivoService anoLetivoService;
	
	@EJB
	private TurmaService turmaService;
	
	@EJB
	private AvaliacaoService avaliacaoService;
	
	private List<Turma> listaTurmas;
	private List<AnoLetivo> listaAnoLetivo;
	private AnoLetivo anoLetivo;
	private Avaliacao avaliacao = new Avaliacao();
	private List<Avaliacao> listaAvaliacao;
	private Integer totalPeso = 0;
	private Integer minimoPeso = 0;
	
	private List<Long> idAvaliacoesExcluidas = new ArrayList<Long>();
	
	private String acaoAvaliacao = "";
	
	private Integer maximoPeso;
	

	@PostConstruct
	public void carregarTela() {
		if(null == listaTurmas){
			listaTurmas = new ArrayList<Turma>();
		}
		
		try {
			Ator ator = Util.pegarAtor();
			if(null != ator && ator.getPerfil().equals(PerfilEnum.AD)){
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertosAteHoje();
			}else{
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertos();
			}
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemFlashRedirectErro("Um erro inesperado aconteceu, tente mais tarde!", "ERRO");
		}
		if(null == listaAnoLetivo){
			listaAnoLetivo = new ArrayList<AnoLetivo>();
		}
		
		if(listaAvaliacao == null) {
			listaAvaliacao = new ArrayList<Avaliacao>();
		}
		
		avaliacao = new Avaliacao();
		avaliacao.setTurma(new Turma());
		avaliacao.getTurma().setAnoLetivo(new AnoLetivo());
		anoLetivo = new AnoLetivo();
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idTurma = request.getParameter("idTurma");
		
		if(idTurma != null) {
			
			try {
				avaliacao.setTurma(turmaService.buscarTurmaPorIdComAvaliacao(Long.valueOf(idTurma)));
			} catch (NumberFormatException e) {
				log.error(e.getMessage(), e);
				Util.montaMensagemErroSemFlashRedirect("Não foi possível carregar as avaliações desta turma", "Erro");
			} catch (ExcecaoGenerica e) {
				log.error(e.getMessage(), e);
				Util.montaMensagemErroSemFlashRedirect("Não foi possível carregar as avaliações desta turma", "Erro");
			}
			listaAvaliacao = avaliacao.getTurma().getListaAvaliacao();
			anoLetivo = avaliacao.getTurma().getAnoLetivo();
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
	
	public void carregarAvaliacoes() {
		try {
			listaAvaliacao = avaliacaoService.carregarAvaliacoesPorTurmaSemPF(avaliacao.getTurma().getId());
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemFlashRedirectErro("Não foi possível carregar as avaliações desta turma", "ERRO");
		}
	}
	
	public void salvarAvaliacao() {
		totalPeso = 0;
		
		if(ACAO_EDITAR.equals(acaoAvaliacao)) {
			listaAvaliacao.remove(avaliacao);
			listaAvaliacao.add(avaliacao);
			Util.montaMensagemFlashRedirect("Avaliação editada com sucesso, ainda não salva.", "Sucesso");
		} else {
			listaAvaliacao.add(avaliacao);
		}
		for(Avaliacao a: listaAvaliacao) {
			totalPeso += a.getPeso();
		}
		if(ACAO_INCLUIR.equals(acaoAvaliacao)) {
			recriarObjetoAvaliacao();
			Util.montaMensagemSemFlashRedirect("Avaliação adicionada com sucesso, ainda não salva.", "Sucesso");
		}
		
	}

	private Integer buscarIndexAvaliacaoPorNome() {
		
		Integer i = 0;
		for(Avaliacao a : listaAvaliacao) {
			
			if(avaliacao.getNome().equalsIgnoreCase(a.getNome())) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public void recriarObjetoAvaliacao() {
		Long idTurma = avaliacao.getTurma().getId();
		Long idAnoLetivo = avaliacao.getTurma().getAnoLetivo().getId();
		avaliacao = new Avaliacao();
		avaliacao.setTurma(new Turma());
		avaliacao.getTurma().setId(idTurma);
		avaliacao.getTurma().setAnoLetivo(new AnoLetivo());
		avaliacao.getTurma().getAnoLetivo().setId(idAnoLetivo);
	}
	
	public void excluirAvaliacao() {
		listaAvaliacao.remove(avaliacao);
		if(avaliacao.getId() != null) {
			idAvaliacoesExcluidas.add(avaliacao.getId());
		}
		Util.montaMensagemFlashRedirect("Avaliação excluida com sucesso", "Sucesso");
		getMaximoPeso();
		recriarObjetoAvaliacao();
	}
	
	public void novaAvaliacao() {
		acaoAvaliacao = ACAO_INCLUIR;
		recriarObjetoAvaliacao();
	}
	
	public String salvar() {
		
		try {
			if(idAvaliacoesExcluidas.size() > 0) {
				avaliacaoService.excluirAvaliacoes(idAvaliacoesExcluidas);
			}
			avaliacaoService.salvar(listaAvaliacao);
		} catch (ExcecaoGenerica e) {
			Util.montaMensagemFlashRedirectErro("Erro", "Não foi possível salvar as avaliações");
		}
		
		Util.montaMensagemSemFlashRedirect("Lista de avaliações cadastradas com sucesso", "Sucesso");
		
		return null;//permanece na mesma página, pois ao trocar perde a msg, pois não é redirect
	}

	public AnoLetivoService getAnoLetivoService() {
		return anoLetivoService;
	}

	public void setAnoLetivoService(AnoLetivoService anoLetivoService) {
		this.anoLetivoService = anoLetivoService;
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

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
		getMaximoPeso();
	}

	public List<Avaliacao> getListaAvaliacao() {
		return listaAvaliacao;
	}

	public void setListaAvaliacao(List<Avaliacao> listaAvaliacao) {
		this.listaAvaliacao = listaAvaliacao;
	}

	public Integer getTotalPeso() {
		totalPeso = 0;
		for(Avaliacao a: listaAvaliacao) {
			totalPeso += a.getPeso();
		}
		return totalPeso;
	}

	public void setTotalPeso(Integer totalPeso) {
		this.totalPeso = totalPeso;
	}

	public Integer getMinimoPeso() {
		if(totalPeso < 10) {
			return 1;
		} else {
			return 0;
		}
	}

	public void setMinimoPeso(Integer minimoPeso) {
		this.minimoPeso = minimoPeso;
	}

	public String getAcaoAvaliacao() {
		return acaoAvaliacao;
	}

	public void setAcaoAvaliacao(String acaoAvaliacao) {
		this.acaoAvaliacao = acaoAvaliacao;
	}

	public List<Long> getIdAvaliacoesExcluidas() {
		return idAvaliacoesExcluidas;
	}

	public void setIdAvaliacoesExcluidas(List<Long> idAvaliacoesExcluidas) {
		this.idAvaliacoesExcluidas = idAvaliacoesExcluidas;
	}

	public Integer getMaximoPeso() {
		maximoPeso = 0;
		if(avaliacao.getPeso() != null) {
			maximoPeso = 10 - (totalPeso - avaliacao.getPeso());
		} else {
			maximoPeso = 10 - totalPeso;
		}
		log.info(maximoPeso.toString());
		return maximoPeso;
	}

	public void setMaximoPeso(Integer maximoPeso) {
		this.maximoPeso = maximoPeso;
	}

}
