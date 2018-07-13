package br.com.sistemaist.daofabrica.fabrica;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;


public class DAOFabricaImpl implements DAOFabrica{
	
	protected EntityManager entityManager;
	
	private static final String PACOTE_ENTIDADES = "entidades";
	private static final String PACOTE_CRUD = "dao.implementacao";
	private static final String SUFIXO_CRUD = "CRUD";
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public DAOFabricaImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public static String getNomeDAO(Class<?> entidade){
		
		 String nome = entidade.getName().replace(PACOTE_ENTIDADES, PACOTE_CRUD)+SUFIXO_CRUD;

		 return nome;
	}

	
	public CRUDGenerico<?, ?> getDAO(Class<?> entidade) throws ExcecaoGenerica {
		
		br.com.sistemaist.daofabrica.crud.CRUDGenerico<?,?> crud = null;
		
		String nome = getNomeDAO(entidade);
		
		try{
			
			crud = (CRUDGenerico<?,?>) Class.forName(nome).newInstance();
			crud.setEntityManager(getEntityManager());
		} catch (InstantiationException e) {
			   log.info(e.getMessage());
	           throw new ExcecaoGenerica(e.getMessage(), e);
	       } catch (IllegalAccessException e) {
	    	   log.info(e.getMessage());
	           throw new ExcecaoGenerica(e.getMessage(), e);
	       } catch (ClassNotFoundException e) {
	    	   log.info(e.getMessage());
	           throw new ExcecaoGenerica(e.getMessage(), e);
	       }

		return crud;
	}


	public EntityManager getEntityManager(){
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	



}
