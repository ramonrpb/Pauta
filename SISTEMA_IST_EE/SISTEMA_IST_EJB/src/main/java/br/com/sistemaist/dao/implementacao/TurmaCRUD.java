package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import br.com.sistemaist.dao.TurmaDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.entidades.Turno;
import br.com.sistemaist.vo.PaginaVO;

public class TurmaCRUD extends CRUDGenerico<Turma, Long> implements TurmaDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<Turma> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica {
		List<Turma> lista = null;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("from Turma t where 1 = 1");
			
			if(null != paginaVO.getAnoLetivo().getId()){
				queryString.append(" and t.anoLetivo.id = :idAnoLetivo");
			} if(null != paginaVO.getPeriodo().getId()){
				queryString.append(" and t.disciplina.periodo.id = :idPeriodo");
			} if (null != paginaVO.getDisciplina().getId()) {
				queryString.append(" and t.disciplina.id = :idDisciplina");
			} if (null != paginaVO.getProfessor().getId()) {
				queryString.append(" and t.professor.id = :idProfessor");
			} if (null != paginaVO.getTurno().getId()) {
				queryString.append(" and t.turno.id = :idTurno");
			}
			
			queryString.append(" order by t.disciplina.nome");
			
			Query query = getEntityManager().createQuery(queryString.toString());
			if(null != paginaVO.getAnoLetivo().getId()){
				query.setParameter("idAnoLetivo", paginaVO.getAnoLetivo().getId());
			}
			if(null != paginaVO.getPeriodo().getId()){
				query.setParameter("idPeriodo", paginaVO.getPeriodo().getId());
			} if (null != paginaVO.getDisciplina().getId()) {
				query.setParameter("idDisciplina", paginaVO.getDisciplina().getId());
			} if (null != paginaVO.getProfessor().getId()) {
				query.setParameter("idProfessor", paginaVO.getProfessor().getId());
			} if (null != paginaVO.getTurno().getId()) {
				query.setParameter("idTurno", paginaVO.getTurno().getId());
			}
			
			query.setFirstResult(paginaVO.getPosicao());
			query.setMaxResults(paginaVO.getQuantidade());

			lista = query.getResultList();
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}

		return lista;
	}

	@Override
	public List<Turma> listarTurmaPorIdProfessor(long idProfessor) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from Turma t where t.professor.id = :idProfessor");

		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idProfessor", idProfessor);
		
		return query.getResultList();
	}

	@Override
	public List<Turma> buscarTurmasPorAnoLetivo(Long anoLetivoOrigem) throws ExcecaoGenerica {
		try {
			Criteria criteria = criarCriteria();
			criteria.createAlias("anoLetivo", "anoLetivo");
			criteria.add(Restrictions.eq("anoLetivo.id", anoLetivoOrigem));
			return (List<Turma>)criteria.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
	}
	
	@Override
	public List<Turma> listarTurmaPorIdProfessorEAnoLetivo(Long idProfessor, Long idAnoLetivo) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from Turma t where t.professor.id = :idProfessor and t.anoLetivo.id = :idAnoLetivo order by t.disciplina.nome");

		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idProfessor", idProfessor);
		query.setParameter("idAnoLetivo", idAnoLetivo);
		
		return query.getResultList();
	}

	@Override
	public List<Turma> listarTurmaPorIdAnoLetivo(Long idAnoLetivo) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from Turma t where t.anoLetivo.id = :idAnoLetivo order by t.disciplina.nome");

		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idAnoLetivo", idAnoLetivo);
		
		return query.getResultList();
	}

	@Override
	public List<Turma> listarPorPaginaEAtor(PaginaVO paginaVO, Ator ator) throws ExcecaoGenerica {
		List<Turma> lista = null;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("from Turma t where t.professor.id = :idProfessor ");
			if(null != paginaVO.getAnoLetivo().getId()){
				queryString.append(" and t.anoLetivo.id = :idAnoLetivo ");
			}
			if(null != paginaVO.getPeriodo().getId()){
				queryString.append(" and t.disciplina.periodo.id = :idPeriodo");
			} if (null != paginaVO.getDisciplina().getId()) {
				queryString.append(" and t.disciplina.id = :idDisciplina");
			} if (null != paginaVO.getTurno().getId()) {
				queryString.append(" and t.turno.id = :idTurno");
			} 
			
			queryString.append(" order by t.disciplina.nome");
			
			Query query = getEntityManager().createQuery(queryString.toString());
			if(null != paginaVO.getAnoLetivo().getId()){
				query.setParameter("idAnoLetivo", paginaVO.getAnoLetivo().getId());
			}
			if(null != paginaVO.getPeriodo().getId()){
				query.setParameter("idPeriodo", paginaVO.getPeriodo().getId());
			}
			
			if (null != paginaVO.getDisciplina().getId()) {
				query.setParameter("idDisciplina", paginaVO.getDisciplina().getId());
			} if (null != paginaVO.getTurno().getId()) {
				query.setParameter("idTurno", paginaVO.getTurno().getId());
			}
			query.setParameter("idProfessor", ator.getId());
			
			lista = query.getResultList();
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}

		return lista;
	}

	

	@Override
	public Turma buscarTurmaPorIdComAvaliacao(Long idTurma) throws ExcecaoGenerica {
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("from Turma t left join fetch t.listaAvaliacao as a ")
			.append("where t.id = :idTurma order by a.nome asc");
			Query query = getEntityManager().createQuery(queryString.toString());
			query.setParameter("idTurma", idTurma);
			return (Turma) query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
	}

	@Override
	public int contarTurmas(AnoLetivo anoLetivo, Periodo periodo,
			Disciplina disciplina, Professor ator, Turno turno) throws ExcecaoGenerica {
		StringBuilder queryString = new StringBuilder();
		queryString.append("select count(t.id) from Turma t where 1 = 1");
		
		if(null != anoLetivo.getId()){
			queryString.append(" and t.anoLetivo.id = :idAnoLetivo");
		} if(null != periodo.getId()){
			queryString.append(" and t.disciplina.periodo.id = :idPeriodo");
		}
		if(null != disciplina.getId()){
			queryString.append(" and t.disciplina.id = :idDisciplina");
		}
		if(null != ator.getId()){
			queryString.append(" and t.professor.id = :idProfessor");
		}
		if(null != turno.getId()){
			queryString.append(" and t.turno.id = :idTurno");
		}
		
		Query query = getEntityManager().createQuery(queryString.toString());
		if(null != anoLetivo.getId()){
			query.setParameter("idAnoLetivo", anoLetivo.getId());
		}
		if(null != periodo.getId()){
			query.setParameter("idPeriodo", periodo.getId());
		}
		
		if(null != disciplina.getId()){
			query.setParameter("idDisciplina", disciplina.getId());
		}
		if(null != ator.getId()){
			query.setParameter("idProfessor", ator.getId());
		}
		if(null != turno.getId()){
			query.setParameter("idTurno", turno.getId());
		}
		
		return Integer.parseInt(query.getSingleResult().toString());
	}
	
	@Override
	public int contarTurmasProfessor(Ator ator, AnoLetivo anoLetivo,
			Periodo periodo, Disciplina disciplina, Turno turno) throws ExcecaoGenerica {
		StringBuilder queryString = new StringBuilder();
		queryString.append("select count(t.id) from Turma t where t.professor.id = :idProfessor ");

		if(null != anoLetivo.getId()){
			queryString.append(" and t.anoLetivo.id = :idAnoLetivo");
		} if(null != periodo.getId()){
			queryString.append(" and t.disciplina.periodo.id = :idPeriodo");
		}
		if(null != disciplina.getId()){
			queryString.append(" and t.disciplina.id = :idDisciplina");
		}
		if(null != ator.getId()){
			queryString.append(" and t.professor.id = :idProfessor");
		}
		if(null != turno.getId()){
			queryString.append(" and t.turno.id = :idTurno");
		}
		
		Query query = getEntityManager().createQuery(queryString.toString());
		if(null != anoLetivo.getId()){
			query.setParameter("idAnoLetivo", anoLetivo.getId());
		}
		if(null != periodo.getId()){
			query.setParameter("idPeriodo", periodo.getId());
		}
		
		if(null != disciplina.getId()){
			query.setParameter("idDisciplina", disciplina.getId());
		}
		if(null != ator.getId()){
			query.setParameter("idProfessor", ator.getId());
		}
		if(null != turno.getId()){
			query.setParameter("idTurno", turno.getId());
		}
		
		return Integer.parseInt(query.getSingleResult().toString());
	}

	

//	@Override
//	public List<Turma> listarTurmaPorIdAlunoEAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica {
//		StringBuilder queryString = new StringBuilder();
//		queryString.append("from AlunoTurma at where at.aluno.id = :idAluno and at.turma.anoLetivo.id = :idAnoLetivo");
//
//		Query query = getEntityManager().createQuery(queryString.toString());
//		query.setParameter("idAluno", idAluno);
//		query.setParameter("idAnoLetivo", idAnoLetivo);
//		
//		return query.getResultList();
//	}

}
