package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import br.com.sistemaist.dao.AlunoAulaDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.vo.MesVO;

public class AlunoAulaCRUD extends CRUDGenerico<AlunoAula, Long> implements AlunoAulaDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<AlunoAula> buscarPorIdAula(Long id) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from AlunoAula aa where aa.aula.id = :id");

		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("id", id);
		
		return query.getResultList();
	}

	@Override
	public List<AlunoAula> buscarPorIdTurmaMes(Long idTurma, MesVO mesVO) {
		StringBuilder queryString = new StringBuilder();
		//queryString.append("from AlunoAula aa where aa.aula.turma.id = :idTurma and (SELECT DATE_PART('MONTH', aula.data) AS mes from Aula aula) = :mes");
		queryString.append("from AlunoAula aa where aa.aula.turma.id = :idTurma and (SELECT MONTH(aula.data) AS mes from Aula aula where aa.aula.id = aula.id) = :mes order by aa.aula.data");
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idTurma", idTurma);
		query.setParameter("mes", mesVO.getId());
		
		return query.getResultList();
	}

	@Override
	public Long buscarTotalDeAulasPorTurma(Long idTurma, MesVO mesVO) {
		
		StringBuilder queryString = new StringBuilder();
		queryString.append("select SUM (a.quantidade) from Aula a where a.turma.id = :idTurma and (SELECT MONTH(aula.data) AS mes from Aula aula where a.id = aula.id) = :mes");
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idTurma", idTurma);
		query.setParameter("mes", mesVO.getId());
		
		return (Long) query.getSingleResult();
	}

	@Override
	public Long buscarTotalDeAulasPorTurma(Long idTurma) {
		
		StringBuilder queryString = new StringBuilder();
		queryString.append("select SUM (a.quantidade) from Aula a where a.turma.id = :idTurma");
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idTurma", idTurma);
		
		return (Long) query.getSingleResult();
	}
	@Override
	public List<AlunoAula> buscarPorIdTurma(Long idTurma) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from AlunoAula aa where aa.aula.turma.id = :idTurma");
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idTurma", idTurma);
		
		return query.getResultList();
	}

	@Override
	public List<AlunoAula> buscarPorIdTurmaIdAluno(Long idTurma, Long idAluno) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from AlunoAula aa where aa.aula.turma.id = :idTurma and aa.aluno.id = :idAluno");
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idTurma", idTurma);
		query.setParameter("idAluno", idAluno);
		
		return query.getResultList();
	}

	@Override
	public Long contarAulaPorAlunoETurma(Long idAluno, Long idTurma) throws ExcecaoGenerica {
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select count(aa.id) from AlunoAula aa where aa.aula.turma.id = :idTurma and aa.aluno.id = :idAluno");
			Query query = getEntityManager().createQuery(queryString.toString());
			query.setParameter("idTurma", idTurma);
			query.setParameter("idAluno", idAluno);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			throw new ExcecaoGenerica("Erro ao buscar total de aulas por Aluno", e);
		}
	}

}
