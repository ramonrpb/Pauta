package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.dao.AlunoAulaDAO;
import br.com.sistemaist.dao.AlunoDAO;
import br.com.sistemaist.dao.AlunoTurmaDAO;
import br.com.sistemaist.dao.AnoLetivoDAO;
import br.com.sistemaist.dao.DisciplinaDAO;
import br.com.sistemaist.dao.PeriodoDAO;
import br.com.sistemaist.dao.ProfessorDAO;
import br.com.sistemaist.dao.TurmaDAO;
import br.com.sistemaist.dao.TurnoDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.entidades.Turno;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.vo.CombosTurmaVO;
import br.com.sistemaist.vo.PaginaVO;

@Stateless
public class TurmaBean implements TurmaService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	
	
	@PostConstruct
	public void instanciarDaoFabrica(){
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public AlunoDAO getAlunoDAO() throws ExcecaoGenerica{
		return (AlunoDAO) daoFabrica.getDAO(Aluno.class);
	}
	
	public AlunoTurmaDAO getAlunoTurmaDAO() throws ExcecaoGenerica{
		return (AlunoTurmaDAO) daoFabrica.getDAO(AlunoTurma.class);
	}
	
	public AnoLetivoDAO getAnoLetivoDAO() throws ExcecaoGenerica{
		return (AnoLetivoDAO) daoFabrica.getDAO(AnoLetivo.class);
	}
	
	public DisciplinaDAO getDisciplinaDAO() throws ExcecaoGenerica{
		return (DisciplinaDAO) daoFabrica.getDAO(Disciplina.class);
	}
	
	public PeriodoDAO getPeriodoDAO() throws ExcecaoGenerica{
		return (PeriodoDAO) daoFabrica.getDAO(Periodo.class);
	}
	
	public ProfessorDAO getProfessorDAO() throws ExcecaoGenerica{
		return (ProfessorDAO) daoFabrica.getDAO(Professor.class);
	}
	
	public TurmaDAO getTurmaDAO() throws ExcecaoGenerica{
		return (TurmaDAO) daoFabrica.getDAO(Turma.class);
	}
	
	public TurnoDAO getTurnoDAO() throws ExcecaoGenerica{
		return (TurnoDAO) daoFabrica.getDAO(Turno.class);
	}
	
	public AlunoAulaDAO getAlunoAulaDAO() throws ExcecaoGenerica{
		return (AlunoAulaDAO) daoFabrica.getDAO(AlunoAula.class);
	}
	
	@Override
	public CombosTurmaVO buscarDadosTurma() throws ExcecaoGenerica {
		
		CombosTurmaVO comboTurmaVO = new CombosTurmaVO();
		comboTurmaVO.setListaAnoLetivo(getAnoLetivoDAO().listar());
		comboTurmaVO.setListaAluno(getAlunoDAO().listarAscOuDesc("nome", true));
		comboTurmaVO.setListaDisciplina(getDisciplinaDAO().listarAscOuDesc("nome", true));
		comboTurmaVO.setListaPeriodo(getPeriodoDAO().listar());
		comboTurmaVO.setListaProfessor(getProfessorDAO().listarAscOuDesc("nome", true));
//		comboTurmaVO.setListaTurma(getTurmaDAO().listar());
		comboTurmaVO.setListaTurno(getTurnoDAO().listar());
		
		return comboTurmaVO;
	}


	@Override
	public List<Turma> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica {
		return getTurmaDAO().listarPorPagina(paginaVO);
	}


	@Override
	public List<Turma> listarTurmaPorIdProfessor(long idProfessor) throws ExcecaoGenerica{
		return getTurmaDAO().listarTurmaPorIdProfessor(idProfessor);
	}


	@Override
	public Turma buscarTurmaPorId(Long idTurma) throws ExcecaoGenerica {
		Turma turma = getTurmaDAO().buscarPorId(idTurma);
		if(turma == null){
			return null;
		}
		turma.setAlunoTurma(getAlunoTurmaDAO().buscarListaPorIdTurma(idTurma));
		return turma;
	}


	@Override
	public void salvar(Turma turma) throws ExcecaoGenerica {
		Turma turmaPersistida = null;
		if(turma.getId() != null) {
			
			for(AlunoTurma at: turma.getAlunoTurma()) {
				if(at.getId() == null) {
					getAlunoTurmaDAO().salvarOuAlterar(at);
				}
			}
			turmaPersistida = getTurmaDAO().mesclar(turma);
			
		} else {
			List<AlunoTurma> listaAlunoTurma = new ArrayList<AlunoTurma>();
			listaAlunoTurma.addAll(turma.getAlunoTurma());
			turma.setAlunoTurma(new ArrayList<AlunoTurma>());
			turmaPersistida = getTurmaDAO().salvar(turma);
			for(AlunoTurma at: listaAlunoTurma) {
				at.setTurma(turmaPersistida);
				getAlunoTurmaDAO().salvarOuAlterar(at);
			}
		}
		
		
		
		log.info(turmaPersistida.getNome());
	}
	
	@Override
	public void deletarAssociacaoAlunoTurma(List<AlunoTurma> alunoTurmaExcluido) throws ExcecaoGenerica {
		for(AlunoTurma atE : alunoTurmaExcluido) {
			if(atE.getId() != null) {
				AlunoTurma alunoTurmaParaExcluir = getAlunoTurmaDAO().buscarPorId(atE.getId());
				getAlunoTurmaDAO().excluir(alunoTurmaParaExcluir);
			}
		}	
	}
	
	@Override
	public void excluir(Long idTurma) throws ExcecaoGenerica {
		Turma turma = buscarTurmaPorId(idTurma);
		getTurmaDAO().excluir(turma);
	}

	@Override
	public void gerarTurmasPorAnoLetivo(Long anoLetivoOrigem,
			Long anoLetivoDestino) throws ExcecaoGenerica {
		List<Turma> turmasOrigem = getTurmaDAO().buscarTurmasPorAnoLetivo(anoLetivoOrigem);
		AnoLetivo anoLetivoDestinoObj = getAnoLetivoDAO().buscarPorId(anoLetivoDestino);
		for(Turma t : turmasOrigem) {
			Turma turmaDestino = new Turma(t.getProfessor(), t.getDisciplina(), t.getTurno(), anoLetivoDestinoObj, t.getNome(), t.getHorario());
			getTurmaDAO().salvarOuAlterar(turmaDestino);
		}
		
	}
	
	@Override
	public List<Turma> listarTurmaPorIdProfessorEAnoLetivo(Long idProfessor, Long idAnoLetivo) throws ExcecaoGenerica {
		return getTurmaDAO().listarTurmaPorIdProfessorEAnoLetivo(idProfessor, idAnoLetivo);
	}

	@Override
	public List<Turma> listarTurmaPorIdAnoLetivo(Long idAnoLetivo) throws ExcecaoGenerica {
		return getTurmaDAO().listarTurmaPorIdAnoLetivo(idAnoLetivo);
	}

	@Override
	public List<Turma> listarPorPaginaEAtor(PaginaVO paginaVO, Ator ator) throws ExcecaoGenerica {
		return getTurmaDAO().listarPorPaginaEAtor(paginaVO, ator);
	}

	@Override
	public Turma buscarTurmaPorIdComAvaliacao(Long idTurma) throws ExcecaoGenerica {
		Turma turma = getTurmaDAO().buscarTurmaPorIdComAvaliacao(idTurma);
		turma.setAlunoTurma(getAlunoTurmaDAO().buscarListaPorIdTurma(idTurma));
		return turma;
	}

	@Override
	public List<Turma> listarTurmaPorIdAlunoEAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica {
		
		List<AlunoTurma> at = getAlunoTurmaDAO().listarTurmaPorIdAlunoEAnoLetivo(idAluno, idAnoLetivo);
		
		List<Turma> t = new ArrayList<Turma>();
		
		for(AlunoTurma a : at){
			t.add(a.getTurma());
		}
		
		return t;
	}

	@Override
	public int contarTurmasFiltradas(AnoLetivo anoLetivo, Periodo periodo,
			Disciplina disciplina, Professor professor, Turno turno)
			throws ExcecaoGenerica {
		return getTurmaDAO().contarTurmas(anoLetivo, periodo, disciplina, professor, turno);
	}

	@Override
	public int contarTurmasProfessor(Ator ator, AnoLetivo anoLetivo,
			Periodo periodo, Disciplina disciplina, Turno turno)
			throws ExcecaoGenerica {
		return getTurmaDAO().contarTurmasProfessor(ator, anoLetivo, periodo, disciplina, turno);
	}

}
