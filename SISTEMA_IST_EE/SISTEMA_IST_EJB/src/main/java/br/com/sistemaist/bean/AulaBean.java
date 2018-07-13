package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import br.com.sistemaist.dao.AlunoAulaDAO;
import br.com.sistemaist.dao.AlunoTurmaDAO;
import br.com.sistemaist.dao.AulaDAO;
import br.com.sistemaist.dao.TurmaDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.Aula;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.service.AulaService;
import br.com.sistemaist.util.Utilitario;
import br.com.sistemaist.vo.FrequenciaVO;
import br.com.sistemaist.vo.MesVO;

@Stateless
public class AulaBean implements AulaService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica(){
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public AulaDAO getAulaDAO() throws ExcecaoGenerica{
		return (AulaDAO) daoFabrica.getDAO(Aula.class);
	}
	public AlunoAulaDAO getAlunoAulaDAO() throws ExcecaoGenerica{
		return (AlunoAulaDAO) daoFabrica.getDAO(AlunoAula.class);
	}
	
	public TurmaDAO getTurmaDAO() throws ExcecaoGenerica{
		return (TurmaDAO) daoFabrica.getDAO(Turma.class);
	}
	
	public AlunoTurmaDAO getAlunoTurmaDAO() throws ExcecaoGenerica{
		return (AlunoTurmaDAO) daoFabrica.getDAO(AlunoTurma.class);
	}
	
	@Override
	public void salvar(Aula aula, List<AlunoAula> listaAlunoAula) throws ExcecaoGenerica{
		try{
			montarAulaComLIstaAlunoAula(aula, listaAlunoAula);
			getAulaDAO().salvar(aula);
		}catch(PersistenceException e){
			throw new ExcecaoGenerica(e.getMessage(), e);
		}
	}
	
	@Override
	public void alterar(Aula aula, List<AlunoAula> listaAlunoAula) throws ExcecaoGenerica {
		try{
			montarAulaComLIstaAlunoAula(aula, listaAlunoAula);
			aula = getAulaDAO().alterar(aula);
		}catch(PersistenceException e){
			throw new ExcecaoGenerica(e.getMessage(), e);
		}
	}

	private void montarAulaComLIstaAlunoAula(Aula aula, List<AlunoAula> listaAlunoAula) {
		if(null != listaAlunoAula){
			aula.setListaAlunoAula(new ArrayList<AlunoAula>());
			for(AlunoAula alunoAula : listaAlunoAula){
				alunoAula.setAula(aula);
				aula.getListaAlunoAula().add(alunoAula);
			}
		}
	}
	
	@Override
	public void excluir(Aula aula) throws ExcecaoGenerica {
		 getAulaDAO().excluir(getAulaDAO().buscarPorId(aula.getId()));
	}
	
	@Override
	public List<Aula> buscarAulas() throws ExcecaoGenerica {
		return getAulaDAO().listar();
	}

	@Override
	public List<Aula> buscarAulasPorIdTurma(Long id) throws ExcecaoGenerica {
		return getAulaDAO().buscarAulasPorIdTurma(id);
	}

	@Override
	public Aula buscarAulaPorId(Long idAula) throws ExcecaoGenerica {
		return getAulaDAO().buscarPorId(idAula);
	}

	@Override
	public List<AlunoAula> buscarListaAlunoAulaPorIdAula(Long id) throws ExcecaoGenerica {
		return getAlunoAulaDAO().buscarPorIdAula(id);
	}

	@Override
	public boolean validarTurmaData(Long id, Long idTurma, Date data) throws ExcecaoGenerica {
		return getAulaDAO().validarTurmaData(id, idTurma, data);
	}

	@Override
	public Aula carregarPauta(Aula aula, MesVO mesVO) throws ExcecaoGenerica {
		
		List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();
		listaAlunoAula = getAlunoAulaDAO().buscarPorIdTurmaMes(aula.getTurma().getId(), mesVO);
		
		aula.setListaAlunoAula(listaAlunoAula);
		
		Turma turma = getTurmaDAO().buscarPorId(aula.getTurma().getId());
		turma.getAlunoTurma().size();
		if(turma != null){
			aula.setTurma(turma);
		}
		
		
		HashMap<String, Integer> listaFrequencia = new HashMap<String, Integer>();
		HashMap<String, Integer> listaFaltas = new HashMap<String, Integer>();
		Long totalDeAulas = getAlunoAulaDAO().buscarTotalDeAulasPorTurma(turma.getId(), mesVO);
		aula.setTotalAulas(totalDeAulas);
		Integer quantidadeAulas = 0;
		Integer quantidadeFaltas = 0;
		for(AlunoAula aa : listaAlunoAula){
			
			if(listaFrequencia.containsKey(aa.getAluno().getMatricula())){
				if(aa.isPresenca()){
					quantidadeAulas = listaFrequencia.get(aa.getAluno().getMatricula()) + 1;
					listaFrequencia.put(aa.getAluno().getMatricula(), quantidadeAulas);
				}
			}else{
				if(aa.isPresenca()){
					listaFrequencia.put(aa.getAluno().getMatricula(), 1);
				}else{
					listaFrequencia.put(aa.getAluno().getMatricula(), 0);
				}
			}
			
			if(listaFaltas.containsKey(aa.getAluno().getMatricula())){
				if(!aa.isPresenca()){
					quantidadeFaltas = listaFaltas.get(aa.getAluno().getMatricula()) + 1;
					listaFaltas.put(aa.getAluno().getMatricula(), quantidadeFaltas);
				}
			}else{
				if(!aa.isPresenca()){
					listaFaltas.put(aa.getAluno().getMatricula(), 1);
				}
			}
		}
		if(null != totalDeAulas){
			for(AlunoTurma at : aula.getTurma().getAlunoTurma()){
				Long quantidadeTotalAulaAluno = getAlunoAulaDAO().contarAulaPorAlunoETurma(at.getAluno().getId(), at.getTurma().getId());
				Integer frequenciaAluno = listaFrequencia.get(at.getAluno().getMatricula());
				if(frequenciaAluno == null) {
					frequenciaAluno = 0;
				}
				at.getAluno().setTotalFrequencia(Utilitario.calcularFrequencia(quantidadeTotalAulaAluno, frequenciaAluno));
				if(listaFaltas.get(at.getAluno().getMatricula()) == null){
					at.getAluno().setTotalFalta(0);
				}else{
					at.getAluno().setTotalFalta(listaFaltas.get(at.getAluno().getMatricula()));
				}
			}
		}
		return aula;
	}

	@Override
	public Aula carregarFrequenciaPorPeriodo(Aula aula) throws ExcecaoGenerica {
		List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();
		listaAlunoAula = getAlunoAulaDAO().buscarPorIdTurma(aula.getTurma().getId());
		
		aula.setListaAlunoAula(listaAlunoAula);
		
		Turma turma = getTurmaDAO().buscarPorId(aula.getTurma().getId());
		turma.getAlunoTurma().size();
		if(turma != null){
			aula.setTurma(turma);
		}
		
		
		HashMap<String, Integer> listaFrequencia = new HashMap<String, Integer>();
		HashMap<String, Integer> listaFaltas = new HashMap<String, Integer>();
		Long totalDeAulas = getAlunoAulaDAO().buscarTotalDeAulasPorTurma(turma.getId());
		aula.setTotalAulas(totalDeAulas);
		Integer quantidadeAulas = 0;
		Integer quantidadeFaltas = 0;
		
		
		for(AlunoAula aa : listaAlunoAula){
			
			if(listaFrequencia.containsKey(aa.getAluno().getMatricula())){
				if(aa.isPresenca()){
					quantidadeAulas = listaFrequencia.get(aa.getAluno().getMatricula()) + 1;
					listaFrequencia.put(aa.getAluno().getMatricula(), quantidadeAulas);
				}
			}else{
				if(aa.isPresenca()){
					listaFrequencia.put(aa.getAluno().getMatricula(), 1);
				}
			}
			
			if(listaFaltas.containsKey(aa.getAluno().getMatricula())){
				if(!aa.isPresenca()){
					quantidadeFaltas = listaFaltas.get(aa.getAluno().getMatricula()) + 1;
					listaFaltas.put(aa.getAluno().getMatricula(), quantidadeFaltas);
				}
			}else{
				if(!aa.isPresenca()){
					listaFaltas.put(aa.getAluno().getMatricula(), 1);
				}
			}
			
		}
		
		for(AlunoTurma at : aula.getTurma().getAlunoTurma()){
			Long quantidadeTotalAulaAluno = getAlunoAulaDAO().contarAulaPorAlunoETurma(at.getAluno().getId(), at.getTurma().getId());
			listaFrequencia.get(at.getAluno().getMatricula());
			Integer frequenciaAluno = listaFrequencia.get(at.getAluno().getMatricula());
			if(frequenciaAluno == null) {
				frequenciaAluno = 0;
			}
			at.getAluno().setTotalFrequencia(Utilitario.calcularFrequencia(quantidadeTotalAulaAluno, frequenciaAluno));
			at.getAluno().setTotalPresenca(frequenciaAluno);
			if(listaFaltas.get(at.getAluno().getMatricula()) == null){
				at.getAluno().setTotalFalta(0);
			}else{
				at.getAluno().setTotalFalta(listaFaltas.get(at.getAluno().getMatricula()));
			}
		}
		return aula;
	}

	@Override
	public FrequenciaVO carregarFrequenciaDoAlunoPorPeriodo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica {
		FrequenciaVO frequenciaVO = new FrequenciaVO();
		List<AlunoTurma> listaAlunoTurma = getAlunoTurmaDAO().listarTurmaPorIdAlunoEAnoLetivo(idAluno, idAnoLetivo);
		
		Aula aula = new Aula();
		HashMap<Long, Integer> listaFrequenciaAux = new HashMap<Long, Integer>();
		HashMap<Long, Integer> listaFaltasAux = new HashMap<Long, Integer>();

		HashMap<String, Integer> listaFrequencia;
		HashMap<String, Integer> listaFaltas;
		
		for(AlunoTurma at : listaAlunoTurma){
			listaFrequencia = new HashMap<String, Integer>();
			listaFaltas = new HashMap<String, Integer>();
			at.getTurma().getAlunoTurma().size();
			if(at.getTurma() != null){
				aula.setTurma(at.getTurma());
			}
			List<AlunoAula> listaAlunoAula = new ArrayList<AlunoAula>();
			listaAlunoAula = getAlunoAulaDAO().buscarPorIdTurmaIdAluno(aula.getTurma().getId(), idAluno);
			
			aula.setListaAlunoAula(listaAlunoAula);
			
			
			
			Long totalDeAulas = getAlunoAulaDAO().buscarTotalDeAulasPorTurma(at.getTurma().getId());
			aula.setTotalAulas(totalDeAulas);
			Integer quantidadeAulas = 0;
			Integer quantidadeFaltas = 0;
			for(AlunoAula aa : listaAlunoAula){
				if(listaFrequencia.containsKey(aa.getAluno().getMatricula())){
					if(aa.isPresenca()){
						quantidadeAulas = listaFrequencia.get(aa.getAluno().getMatricula()) + 1;
						listaFrequencia.put(aa.getAluno().getMatricula(), quantidadeAulas);
					}
				}else{
					if(aa.isPresenca()){
						listaFrequencia.put(aa.getAluno().getMatricula(), 1);
					}
				}
				
				if(listaFaltas.containsKey(aa.getAluno().getMatricula())){
					if(!aa.isPresenca()){
						quantidadeFaltas = listaFaltas.get(aa.getAluno().getMatricula()) + 1;
						listaFaltas.put(aa.getAluno().getMatricula(), quantidadeFaltas);
					}
				}else{
					if(!aa.isPresenca()){
						listaFaltas.put(aa.getAluno().getMatricula(), 1);
					}
				}
			}
			listaFrequenciaAux.put(at.getId(), listaFrequencia.get(at.getAluno().getMatricula()));
			listaFaltasAux.put(at.getId(), listaFaltas.get(at.getAluno().getMatricula()));
			
		}
		
		for(AlunoTurma alunoTurma : listaAlunoTurma){
			if(alunoTurma.getAluno().getId().equals(idAluno)){
				Long quantidadeTotalAulaAluno = getAlunoAulaDAO().contarAulaPorAlunoETurma(alunoTurma.getAluno().getId(), alunoTurma.getTurma().getId());
				//Long totalDeAulas = getAlunoAulaDAO().buscarTotalDeAulasPorTurma(alunoTurma.getTurma().getId());
//				Integer frequenciaAluno = listaFrequenciaAux.get(alunoTurma.getAluno().getMatricula());
				Integer frequenciaAluno = listaFrequenciaAux.get(alunoTurma.getId());
				if(frequenciaAluno == null) {
					frequenciaAluno = 0;
				}
				if(listaFrequenciaAux.get(alunoTurma.getId()) == null){
					alunoTurma.setTotalFrequencia(0.0);
				}else{
					alunoTurma.setTotalFrequencia(Utilitario.calcularFrequencia(quantidadeTotalAulaAluno, frequenciaAluno));
				}
				if(listaFrequenciaAux.get(alunoTurma.getId()) == null){
					alunoTurma.setTotalPresenca(0);
				}else{
					alunoTurma.setTotalPresenca(listaFrequenciaAux.get(alunoTurma.getId()));
				}
				if(listaFaltasAux.get(alunoTurma.getId()) == null){
					alunoTurma.setTotalFalta(0);
				}else{
					alunoTurma.setTotalFalta(listaFaltasAux.get(alunoTurma.getId()));
				}
			}
		}
		
		frequenciaVO.setAula(aula);
		frequenciaVO.setListaAlunoTurma(listaAlunoTurma);
		return frequenciaVO;
	}
}
