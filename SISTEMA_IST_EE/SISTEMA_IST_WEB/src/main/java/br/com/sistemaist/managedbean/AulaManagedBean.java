package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Aula;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AlunoService;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.service.AulaService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Util;
    
@ManagedBean(name="aulaMB")
@ViewScoped
public class AulaManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Aula aula = new Aula();
	private AnoLetivo anoLetivo = new AnoLetivo();
	
	private List<Turma> listaTurmas;
	private List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();
	private List<Aula> listaAulas = new ArrayList<Aula>();
	private List<AnoLetivo> listaAnoLetivo = new ArrayList<AnoLetivo>();
	
	private Periodo periodo;
	
	//Usado para mapear idAluno e seus tempos de presença
	private Map<Long, String[]> listaAlunoAulasPresenca = new HashMap<Long, String[]>();
	//U
	private Map<Long, Boolean> listaAlunoTodasAulas = new HashMap<Long, Boolean>();
	private List<String> listaPresenca;
	
	@EJB
	AulaService aulaService;
	@EJB
	AlunoService alunoService;
	@EJB
	TurmaService turmaService;
	@EJB
	AnoLetivoService anoLetivoService;
	
	@PostConstruct
	public void carregarTela() {
		
		try {
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String idAula = request.getParameter("idAula");
			
			if(null != idAula){
				aula = aulaService.buscarAulaPorId(Long.parseLong(idAula));
				listaAlunoAula = aulaService.buscarListaAlunoAulaPorIdAula(Long.parseLong(idAula));
				anoLetivo = aula.getTurma().getAnoLetivo();
				
				listaPresenca = new ArrayList<String>();
				for(int i=0; i < aula.getQuantidade(); i++){
					listaPresenca.add(i+1+"");
				}
				
				listaAlunoAulasPresenca = new HashMap<Long, String[]>();
				listaAlunoTodasAulas = new HashMap<Long, Boolean>();
				
				List<AlunoAula> alunoAulaList = new ArrayList<AlunoAula>();
				for(AlunoAula alunoAu : listaAlunoAula){
					if(listaAlunoAulasPresenca.containsKey(alunoAu.getAluno().getId())){
						String[] str = listaAlunoAulasPresenca.get(alunoAu.getAluno().getId());
						int proximaPosicao = 0;
						for(String stg : str){
							if(null != stg){
								proximaPosicao += 1;
							}
						}
						if(alunoAu.isPresenca()){
							str[proximaPosicao] = proximaPosicao+1+"";
						}else{
							str[proximaPosicao] = "0";
						}
						listaAlunoAulasPresenca.put(alunoAu.getAluno().getId(), str);
						
					}else{
						String[] str = new String[aula.getQuantidade()];
						if(alunoAu.isPresenca()){
							str[0] = "1";
						}else{
							str[0] = "0";
						}
						listaAlunoAulasPresenca.put(alunoAu.getAluno().getId(), str);
						alunoAulaList.add(alunoAu);
						
						listaAlunoTodasAulas.put(alunoAu.getAluno().getId(), false);
					}
					
					verificarPreenchimentoTotal(alunoAu.getAluno().getId());
				}
				
				listaAlunoAula = alunoAulaList;
				
			}else{
				aula.setTurma(new Turma());
				aula.getTurma().setAnoLetivo(new AnoLetivo());
				String idTurma = request.getParameter("idTurma");
				if(null != idTurma && ! "".equals(idTurma.trim())){
					aula.getTurma().setId(Long.parseLong(idTurma));
					filtrarListaAulaPorTurma();
					if(null != aula.getTurma()){
						anoLetivo = aula.getTurma().getAnoLetivo();
					}
				}
			}
			Ator ator = Util.pegarAtor();
			if(ator.getPerfil().equals(PerfilEnum.AD)){
				listaTurmas = turmaService.listarTurmaPorIdAnoLetivo(anoLetivo.getId());
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertosAteHoje();
			}else{
				listaTurmas = turmaService.listarTurmaPorIdProfessorEAnoLetivo(ator.getId(), anoLetivo.getId());
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertos();
			}
			
			if(null == listaTurmas){
				listaTurmas = new ArrayList<Turma>();
			}
			if(null == listaAnoLetivo){
				listaAnoLetivo = new ArrayList<AnoLetivo>();
			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao carregar os dados da aula");
			Util.montaMensagemFlashRedirectErro("Erro ao carregar os dados da aula", "Erro");
		}
	}
	
	private void verificarPreenchimentoTotal(Long idAluno) {
		String[] strs = listaAlunoAulasPresenca.get(idAluno);
		List<String> items = Arrays.asList(strs);
		strs = new String[aula.getQuantidade()];
		
		for(String item : items){
			if(null != item && item.equals("0")){
				listaAlunoTodasAulas.put(idAluno, false);
				return;
			}
		}
		
		listaAlunoTodasAulas.put(idAluno, true);
	}

	public void filtrarTurmasPorAnoLetivo(){
		//buscar turmas do professor
		Ator ator = Util.pegarAtor();
		try {
			if(null != anoLetivo.getId()){
				anoLetivo = anoLetivoService.buscarAnoLetivoPorId(anoLetivo.getId());
			}
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
		aula = new Aula();
		aula.setTurma(new Turma());
		listaAulas = new ArrayList<Aula>();
		listaAlunoAula = new ArrayList<AlunoAula>();
	}
	
	public void filtrarListaAlunoAulaPorTurma(){
		
		try {
			listaAlunoAula = new ArrayList<AlunoAula>();
			
			if(null != aula.getTurma().getId()){
				aula.setTurma(turmaService.buscarTurmaPorId(aula.getTurma().getId()));
				
				if(null != aula.getTurma().getAlunoTurma()){
					
					listaPresenca = new ArrayList<String>();
					for(int i=0; i < aula.getQuantidade(); i++){
						listaPresenca.add(i+1+"");
					}
					
					listaAlunoAulasPresenca = new HashMap<Long, String[]>();
					
					for(AlunoTurma alunoTurma : aula.getTurma().getAlunoTurma()){
						AlunoAula alunoAula = new AlunoAula();
						alunoAula.setAluno(alunoTurma.getAluno());
						listaAlunoAula.add(alunoAula);
						
						//setando presença default
						String[] aulas = new String[aula.getQuantidade()];
						for(int i = 0; i < aulas.length; i++){
							aulas[i] = i+1+"";
						}
						listaAlunoAulasPresenca.put(alunoAula.getAluno().getId(), aulas);

						//setando todas
						listaAlunoTodasAulas.put(alunoAula.getAluno().getId(), true);
					}
				}
			}else{
				aula.setTurma(new Turma());
				aula.getTurma().setAnoLetivo(new AnoLetivo());
			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
	}
	
	public void filtrarListaRelatorioPorTurma(){
		filtrarListaAulaPorTurma();
		filtrarListaAlunoAulaPorTurma();
	}

	public void salvar(){
		try {
			
			List<AlunoAula> listaAlunoAulas = new ArrayList<AlunoAula>();
			AlunoAula alunoAul = new AlunoAula();
			for(AlunoAula al : listaAlunoAula){
				
				String[] aulas = listaAlunoAulasPresenca.get(al.getAluno().getId());
				
				for(int i=0; i< aula.getQuantidade(); i++){
					alunoAul = new AlunoAula();
					alunoAul.setAluno(al.getAluno());
					
					for(String str : aulas){
						if(str.equals(i+1+"")){
							alunoAul.setPresenca(true);
							alunoAul.setTempo(i);
						}
					}
					listaAlunoAulas.add(alunoAul);					
				}

			}
			
			aulaService.salvar(aula, listaAlunoAulas);
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao salvar");
			Util.montaMensagemFlashRedirectErro("Erro ao salvar", "Erro");
			return;
		}
		
		Util.montaMensagemFlashRedirect("Cadastro realizado com sucesso !","Sucesso!");
		aula = new Aula();
		aula.setTurma(new Turma());
		listaAlunoAula = new ArrayList<AlunoAula>();
	}

	private List<AlunoAula> montaListaDeAula(List<AlunoAula> listaAula) {
		List<AlunoAula> listaAlunoAulas = new ArrayList<AlunoAula>();
		AlunoAula alunoAul = new AlunoAula();
			
		Map<Long, Integer> numeroAula = new HashMap<Long, Integer>();
		for(AlunoAula al : listaAula){

			String[] aulas = listaAlunoAulasPresenca.get(al.getAluno().getId());
			if(null != aulas){
				alunoAul = new AlunoAula();
				alunoAul.setAluno(al.getAluno());
				alunoAul.setId(al.getId());
				
				Integer inteiro = numeroAula.get(al.getAluno().getId());
				if(null == inteiro){
					inteiro = 1;
				}
				alunoAul.setTempo(inteiro);
				for(String str : aulas){
					if(str.equals(alunoAul.getTempo()+"")){
						alunoAul.setPresenca(true);
						break;
					}
				}
				numeroAula.put(al.getAluno().getId(), inteiro+1);
			}
			listaAlunoAulas.add(alunoAul);

		}
		return listaAlunoAulas;
	}

	public String editar(){
		try {
			List<AlunoAula> listaAlunoAulas = montaListaDeAula(aulaService.buscarListaAlunoAulaPorIdAula(aula.getId()));
			aulaService.alterar(aula, listaAlunoAulas);
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao editar");
			Util.montaMensagemFlashRedirectErro("Erro ao editar", "Erro");
		}
		
		Util.montaMensagemFlashRedirect("Alterado realizado com sucesso !","Sucesso!");
		
		return "listagemAula";
	}
	
	public void filtrarListaAulaPorTurma(){
		try {
			listaAulas = new ArrayList<Aula>();
			if(null != aula.getTurma() && null != aula.getTurma().getId()){
				aula.setTurma(turmaService.buscarTurmaPorId(aula.getTurma().getId()));
				if(null == aula.getTurma()){
					return;
				}
				listaAulas = aulaService.buscarAulasPorIdTurma(aula.getTurma().getId());
			}else{
				aula.setTurma(new Turma());
			}
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar as aulas pela turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar as aulas pela turma", "Erro");
		}
	}
	
	public void excluir(){
		try {
			aulaService.excluir(aula);
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao excluir");
			Util.montaMensagemFlashRedirectErro("Erro ao excluir", "Erro");
			return;
		}
		filtrarListaAulaPorTurma();
		Util.montaMensagemFlashRedirect("Exclusão realizada com sucesso !","Sucesso!");
	}
	
	public void verificarCheckTotal(Long idAluno){
		String[] strs = listaAlunoAulasPresenca.get(idAluno);
		
		List<String> items = Arrays.asList(strs);
		
		strs = new String[aula.getQuantidade()];
		if(items.size() == aula.getQuantidade()){
			listaAlunoTodasAulas.put(idAluno, true);
		}else{
			listaAlunoTodasAulas.put(idAluno, false);
		}
	}

	public void preencheDesmarcaValoresCheck(Long idAluno){
		
		Boolean bool = listaAlunoTodasAulas.get(idAluno);
		
		String[] strs = new String[aula.getQuantidade()];
		
		if(null != bool && bool){
			for(int i = 0; i < aula.getQuantidade(); i++){
				strs[i] = i+1+"";
			}
		}
		listaAlunoAulasPresenca.put(idAluno, strs);
		
	}
	
	public Aula getAula() {
		return aula;
	}
	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public List<AlunoAula> getListaAlunoAula() {
		return listaAlunoAula;
	}
	public void setListaAlunoAula(List<AlunoAula> listaAlunoAula) {
		this.listaAlunoAula = listaAlunoAula;
	}

	public List<Turma> getListaTurmas() {
		return listaTurmas;
	}
	public void setListaTurmas(List<Turma> listaTurmas) {
		this.listaTurmas = listaTurmas;
	}

	public List<Aula> getListaAulas() {
		return listaAulas;
	}
	public void setListaAulas(List<Aula> listaAulas) {
		this.listaAulas = listaAulas;
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

	public Map<Long, String[]> getListaAlunoAulasPresenca() {
		return listaAlunoAulasPresenca;
	}

	public void setListaAlunoAulasPresenca(Map<Long, String[]> listaAlunoAulasPresenca) {
		this.listaAlunoAulasPresenca = listaAlunoAulasPresenca;
	}

	public List<String> getListaPresenca() {
		return listaPresenca;
	}

	public void setListaPresenca(List<String> listaPresenca) {
		this.listaPresenca = listaPresenca;
	}

	public Map<Long, Boolean> getListaAlunoTodasAulas() {
		return listaAlunoTodasAulas;
	}

	public void setListaAlunoTodasAulas(Map<Long, Boolean> listaAlunoTodasAulas) {
		this.listaAlunoTodasAulas = listaAlunoTodasAulas;
	}
	
}  