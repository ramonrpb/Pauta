package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Aula;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.service.AulaService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.vo.MesVO;
    
@ManagedBean(name="relatorioPautaMB")
@ViewScoped
public class RelatorioPautaManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private AnoLetivo anoLetivo = new AnoLetivo();
	
	private List<Turma> listaTurmas;
	private List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();
	private List<Aula> listaAulas = new ArrayList<Aula>();
	private List<AnoLetivo> listaAnoLetivo = new ArrayList<AnoLetivo>();
	private List<MesVO> listaMeses = new ArrayList<MesVO>();
	
//	private HashMap<String, List<AlunoAula>> listaPresenca = new HashMap<String, List<AlunoAula>>();
	private TreeMap<String, List<AlunoAula>> listaPresenca = new TreeMap<String, List<AlunoAula>>();
	
	private MesVO mesVO = new MesVO();
	
	private Turma turma = new Turma();
	
	private List<Integer> listaQuantidadeAula = new ArrayList<Integer>(); 
	
	private List<Integer> listaQuantidadeAulaTotal = new ArrayList<Integer>();
	
	private Integer quantidadeAula;
	
	@EJB
	AulaService aulaService;
	@EJB
	TurmaService turmaService;
	@EJB
	AnoLetivoService anoLetivoService;
	
	@PostConstruct
	public void carregarTela() {
		
		try {
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			turma = new Turma();
			turma.setAnoLetivo(new AnoLetivo());
			String idTurma = request.getParameter("idTurma");
			if(null != idTurma && ! "".equals(idTurma.trim())){
				turma.setId(Long.parseLong(idTurma));
				filtrarListaAulaPorTurma();
				if(null != turma){
					anoLetivo = turma.getAnoLetivo();
				}
			}
			
			Ator ator = Util.pegarAtor();
			if(ator.getPerfil().equals(PerfilEnum.AD)){
				listaTurmas = turmaService.listarTurmaPorIdAnoLetivo(anoLetivo.getId());
			}else{
				listaTurmas = turmaService.listarTurmaPorIdProfessorEAnoLetivo(ator.getId(), anoLetivo.getId());
			}
			
			if(null == listaTurmas){
				listaTurmas = new ArrayList<Turma>();
			}
			
			if(null != ator && ator.getPerfil().equals(PerfilEnum.AD)){
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertosAteHoje();
			}else{
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertos();
			}
			
			if(null == listaAnoLetivo){
				listaAnoLetivo = new ArrayList<AnoLetivo>();
			}
			
			quantidadeAula = 1;
			listaQuantidadeAulaTotal.clear();
			for(Integer i = 1; i <= 30; i++){
				listaQuantidadeAulaTotal.add(i);
			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao carregar os dados da aula");
			Util.montaMensagemFlashRedirectErro("Erro ao carregar os dados da aula", "Erro");
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
		turma = new Turma();
	}
	
	public void filtrarListaAlunoAulaPorTurma(){
		AnoLetivo anoLetivoAux;
		for(AnoLetivo ano : listaAnoLetivo){
			if(anoLetivo.getId().equals(ano.getId())){
				try {
					anoLetivoAux = anoLetivoService.buscarAnoLetivoPorId(ano.getId());
					if(anoLetivoAux.getDataFim() == null){
						listaMeses = Util.carregarMesesPorSemestre(null, null);
					}else{
						Calendar dataInicial = Calendar.getInstance();
						dataInicial.setTime(anoLetivoAux.getDataInicio());
						Calendar dataFinal = Calendar.getInstance();
						dataFinal.setTime(anoLetivoAux.getDataFim());
						listaMeses = Util.carregarMesesPorSemestre(dataInicial.get(Calendar.MONTH)+1, dataFinal.get(Calendar.MONTH)+1);
					}
					break;
				} catch (ExcecaoGenerica e) {
					log.error("Erro ao montar lista de meses");
					Util.montaMensagemFlashRedirectErro("Erro ao montar lista de meses", "Erro");
				}
				
			}
		}
	}
	
	public void carregarPauta(){
		
		try {
			if(null != turma.getId()){
				
				turma = turmaService.buscarTurmaPorId(turma.getId());
				
				for(MesVO mes : listaMeses){
					if(mes.getId() == mesVO.getId()){
						mesVO = new MesVO(mes.getId(), mes.getNome());
						break;
					}
				}
				listaQuantidadeAula = new ArrayList<Integer>(listaQuantidadeAulaTotal);
			}else{
				turma = new Turma();
				turma.setAnoLetivo(new AnoLetivo());
			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
		
	}

	public void atualizaPauta(){
		System.out.println("Quantidade aula: "+quantidadeAula);
		listaQuantidadeAula.clear();
		for(Integer i = 1; i <= quantidadeAula; i++){
			listaQuantidadeAula.add(i);
		}
	}
	
	public void filtrarListaAulaPorTurma(){
		try {
			listaAulas = new ArrayList<Aula>();
			if(null != turma && null != turma.getId()){
				turma = turmaService.buscarTurmaPorId(turma.getId());
				if(null == turma){
					return;
				}
				listaAulas = aulaService.buscarAulasPorIdTurma(turma.getId());
			}else{
				turma = new Turma();
			}
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar as aulas pela turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar as aulas pela turma", "Erro");
		}
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

	public List<MesVO> getListaMeses() {
		return listaMeses;
	}

	public void setListaMeses(List<MesVO> listaMeses) {
		this.listaMeses = listaMeses;
	}

	public MesVO getMesVO() {
		return mesVO;
	}

	public void setMesVO(MesVO mesVO) {
		this.mesVO = mesVO;
	}

	public TreeMap<String, List<AlunoAula>> getListaPresenca() {
		return listaPresenca;
	}

	public void setListaPresenca(TreeMap<String, List<AlunoAula>> listaPresenca) {
		this.listaPresenca = listaPresenca;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Integer> getListaQuantidadeAula() {
		return listaQuantidadeAula;
	}

	public void setListaQuantidadeAula(List<Integer> listaQuantidadeAula) {
		this.listaQuantidadeAula = listaQuantidadeAula;
	}

	public Integer getQuantidadeAula() {
		return quantidadeAula;
	}

	public void setQuantidadeAula(Integer quantidadeAula) {
		this.quantidadeAula = quantidadeAula;
	}

	public List<Integer> getListaQuantidadeAulaTotal() {
		return listaQuantidadeAulaTotal;
	}

	public void setListaQuantidadeAulaTotal(List<Integer> listaQuantidadeAulaTotal) {
		this.listaQuantidadeAulaTotal = listaQuantidadeAulaTotal;
	}
	
}  