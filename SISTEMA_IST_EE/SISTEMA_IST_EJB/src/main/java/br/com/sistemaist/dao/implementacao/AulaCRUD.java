package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import br.com.sistemaist.dao.AulaDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.entidades.Aula;

public class AulaCRUD extends CRUDGenerico<Aula, Long> implements AulaDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<Aula> buscarAulasPorIdTurma(Long id) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from Aula au where au.turma.id = :id order by au.data desc");

		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("id", id);
		
		return query.getResultList();
	}

	@Override
	public boolean validarTurmaData(Long id, Long idTurma, Date data) {
		
		StringBuffer hql = new StringBuffer("select count(a.id) FROM Aula a where a.turma.id = :idTurma and a.data = :data");
		
		if(null != id){
			hql.append(" and a.id != :id");
		}
		
		Query query = getEntityManager().createQuery(hql.toString());
		
		query.setParameter("idTurma", idTurma);
		query.setParameter("data", data);
		if(null != id){
			query.setParameter("id", id);
		}
		
		return ( (Long) query.getSingleResult() == 0);
	}

}
