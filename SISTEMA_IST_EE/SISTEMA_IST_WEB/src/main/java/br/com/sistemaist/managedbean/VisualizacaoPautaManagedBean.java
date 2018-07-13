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
import br.com.sistemaist.util.Constante;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.util.Utilitario;
import br.com.sistemaist.vo.MesVO;
    
@ManagedBean(name="visualizacaoPautaMB")
@ViewScoped
public class VisualizacaoPautaManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Aula aula = new Aula();
	private AnoLetivo anoLetivo = new AnoLetivo();
	
	private List<Turma> listaTurmas;
	private List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();
	private List<Aula> listaAulas = new ArrayList<Aula>();
	private List<AnoLetivo> listaAnoLetivo = new ArrayList<AnoLetivo>();
	private List<MesVO> listaMeses = new ArrayList<MesVO>();
	
	private TreeMap<String, List<AlunoAula>> listaPresenca = new TreeMap<String, List<AlunoAula>>();
	
	private MesVO mesVO = new MesVO();
	
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
			String idAula = request.getParameter("idAula");
			
			if(null != idAula){
				aula = aulaService.buscarAulaPorId(Long.parseLong(idAula));
				listaAlunoAula = aulaService.buscarListaAlunoAulaPorIdAula(Long.parseLong(idAula));
				anoLetivo = aula.getTurma().getAnoLetivo();
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
		aula = new Aula();
		aula.setTurma(new Turma());
		listaAulas = new ArrayList<Aula>();
		listaAlunoAula = new ArrayList<AlunoAula>();
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
			listaAlunoAula = new ArrayList<AlunoAula>();
			
			if(null != aula.getTurma().getId()){
				for(MesVO mes : listaMeses){
					if(mes.getId() == mesVO.getId()){
						mesVO = new MesVO(mes.getId(), mes.getNome());
						break;
					}
				}
				aula = aulaService.carregarPauta(aula, mesVO);
				listaAlunoAula = aula.getListaAlunoAula();
				
				if(null == listaAlunoAula || listaAlunoAula.isEmpty()){
					Long idGuardado = aula.getTurma().getId();
					aula.setTurma(new Turma());
					aula.getTurma().setId(idGuardado);
				}
				
				listaPresenca = new TreeMap<String, List<AlunoAula>>();
				for(AlunoAula alunoAula : listaAlunoAula){
					alunoAula.getAula().setDataFormatada(Utilitario.formatarData(alunoAula.getAula().getData(), Constante.FORMATO_DIA));
					
					if(listaPresenca.containsKey(alunoAula.getAula().getDataFormatada())){
						listaPresenca.get(alunoAula.getAula().getDataFormatada()).add(alunoAula);
					}else{
						listaPresenca.put(alunoAula.getAula().getDataFormatada(), new ArrayList<AlunoAula>());
						listaPresenca.get(alunoAula.getAula().getDataFormatada()).add(alunoAula);
					}
				}
				
				
			}else{
				aula.setTurma(new Turma());
				aula.getTurma().setAnoLetivo(new AnoLetivo());
			}
			
//			for(AnoLetivo ano : listaAnoLetivo){
//				if(anoLetivo.getId().equals(ano.getId())){
//					listaMeses = Util.carregarMesesPorSemestre(ano.getSemestre());
//					break;
//				}
//			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
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
	
	public Aula getAula() {
		return aula;
	}
	public void setAula(Aula aula) {
		this.aula = aula;
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
	
}  