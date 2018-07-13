package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.sistemaist.dao.AtorDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Ator;

public class AtorCRUD extends CRUDGenerico<Ator, Long> implements AtorDAO, Serializable{

	private static final long serialVersionUID = 1L;

	public Ator buscarAtorPorLoginESenha(String login, String senha){
		
		Criteria criteria = ((Session)getEntityManager().getDelegate()).createCriteria(getClassePersistente());
		criteria.add(Restrictions.eq("email", login));
		criteria.add(Restrictions.eq("senha", senha));
		
		Ator ator = (Ator) criteria.uniqueResult();
	
		return ator;
	}

	public String buscarEmailPorLogin(String login) throws ExcecaoGenerica{

		StringBuffer hql = new StringBuffer("select a.email FROM Ator a where a.email = :login ");
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setString("login", login);
		return (String) query.uniqueResult();
	}
	
	public boolean verificarExistenciaLogin(String login, Long id) throws ExcecaoGenerica{

		StringBuffer hql = new StringBuffer("select count(a.id) FROM Ator a where a.email = :login ");
		if(null != id){
			hql.append(" and a.id != :id");
		}
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		
		query.setString("login", login);
		if(null != id){
			query.setLong("id", id);
		}
		return ( (Long) query.uniqueResult() > 0);
	}

	@Override
	public boolean validarAtorPorLoginECodigo(String login, String codigo) {
		StringBuffer hql = new StringBuffer("select count(a.id) FROM Ator a where a.email = :login and a.codigo = :codigo and a.dataValidadeCodigo > :agora");
		
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setString("login", login);
		query.setString("codigo", codigo);
		query.setCalendar("agora", Calendar.getInstance());
		
		return ( (Long) query.uniqueResult() > 0);
	}

	@Override
	public boolean validarAtorPorLoginESenha(String login, String senhaAtual) {
		StringBuffer hql = new StringBuffer("select count(a.id) FROM Ator a where a.email = :login and a.senha = :senhaAtual");
		
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setString("login", login);
		query.setString("senhaAtual", senhaAtual);

		return ( (Long) query.uniqueResult() > 0);
	}

	@Override
	public Ator buscarAtorPorLogin(String login) throws ExcecaoGenerica {
		Criteria criteria = ((Session)getEntityManager().getDelegate()).createCriteria(getClassePersistente());
		criteria.add(Restrictions.eq("email", login));
		
		Ator ator = (Ator) criteria.uniqueResult();
		return ator;
	}
	
	@Override
	public void salvarCodigoEDataValidadeCodigo(String login, String codigo, Calendar dataExpiracaoSenha) throws ExcecaoGenerica {
		StringBuffer hql = new StringBuffer("update Ator a set a.codigo = :codigo, a.dataValidadeCodigo = :dataExpiracaoSenha where a.email = :login");
		
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setString("codigo", codigo);
		query.setCalendar("dataExpiracaoSenha", dataExpiracaoSenha);
		query.setString("login", login);

		query.executeUpdate();
	}
}
