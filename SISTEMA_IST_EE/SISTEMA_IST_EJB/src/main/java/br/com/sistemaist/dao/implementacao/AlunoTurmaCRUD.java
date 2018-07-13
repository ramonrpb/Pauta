package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import br.com.sistemaist.dao.AlunoTurmaDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.entidades.AlunoTurma;

public class AlunoTurmaCRUD extends CRUDGenerico<AlunoTurma, Long> implements AlunoTurmaDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<AlunoTurma> buscarListaPorIdTurma(long idTurma) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from AlunoTurma at where at.turma.id = :idTurma order by translate(at.aluno.nome,"+"'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç'"+",'AAAAAAAAEEEEIIOOOOOOUUUUCC')");

		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idTurma", idTurma);
		
		return query.getResultList();
	}


	@Override
	public List<AlunoTurma> listarTurmaPorIdAlunoEAnoLetivo(Long idAluno, Long idAnoLetivo) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from AlunoTurma at where at.aluno.id = :idAluno and at.turma.anoLetivo.id = :idAnoLetivo");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("idAluno", idAluno);
		query.setParameter("idAnoLetivo", idAnoLetivo);
		
		return query.getResultList();
	}
}
