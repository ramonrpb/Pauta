package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.sistemaist.dao.AnoLetivoDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;

public class AnoLetivoCRUD extends CRUDGenerico<AnoLetivo, Long> implements AnoLetivoDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public boolean validarAkAnoLetivo(Integer ano, Integer semestre, Long id) throws ExcecaoGenerica {
		
		StringBuffer hql = new StringBuffer("select count(al.id) FROM AnoLetivo al where al.ano = :ano and al.semestre = :semestre ");
		if(null != id){
			hql.append(" and al.id != :id");
		}
		
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setInteger("ano", ano);
		query.setInteger("semestre", semestre);
		if(null != id){
			query.setLong("id", id);
		}
		return ( (Long) query.uniqueResult() == 0);
	}

	@Override
	public List<AnoLetivo> buscarAnosLetivosAbertos(Date date) throws ExcecaoGenerica {
	    
		StringBuffer hql = new StringBuffer("FROM AnoLetivo al where ( al.dataInicio = null or al.dataInicio <= :date) and ( al.dataFim = null or al.dataFim >= :date ) order by al.ano, al.semestre ");
		
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setDate("date", date);
		
		return query.list();
	}

	@Override
	public List<AnoLetivo> buscarAnosLetivosAbertosAteHoje(Date date) throws ExcecaoGenerica {
	    
		StringBuffer hql = new StringBuffer("FROM AnoLetivo al where ( al.dataInicio = null or al.dataInicio <= :date) order by al.ano, al.semestre ");
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setDate("date",  date);
		
		return query.list();	
	}

}
