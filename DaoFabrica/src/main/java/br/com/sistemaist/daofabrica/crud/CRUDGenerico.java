package br.com.sistemaist.daofabrica.crud;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.Normalizer;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;


@SuppressWarnings("unchecked")
public abstract class CRUDGenerico<T, ID extends Serializable> implements DAOGenerico<T, ID> {

	protected Logger logger = LoggerFactory.getLogger(CRUDGenerico.class);
	
	private EntityManager entityManager;
	
	private Class<T> classePersistente;

	public void destacar(T entity)throws ExcecaoGenerica{
		((Session) getEntityManager().getDelegate()).evict(entity);
	}
	
	/**
	 * No construtor foi usado Reflection para fazer o casting correto para o tipo
	 * que realmente foi instanciado.
	 * 
	 */
	public CRUDGenerico() {
		this.classePersistente = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	

	/**
	 * 
	 * Obtem a implementacao dessa classe, ou seja, a instancia da sub-classe
	 * 
	 * @return
	 */
	public Class<T> getClassePersistente() {
		return classePersistente;
	}

	/**
	 * 
	 * Define o valor da classe persistente.
	 * 
	 * @param classePersistente
	 */
	public void setClassePersistente(Class<T> classePersistente) {
		this.classePersistente = classePersistente;
	}

	/**
	 * 
	 * Criacao de uma entidade.
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T salvar(T entity)throws ExcecaoGenerica {
		
		logger.info("::::Acionou o metodo salvar em GenericCRUD::::");
		
		try {
			
			((Session) getEntityManager().getDelegate()).save(entity);

		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}

		return entity;
	}

	/**
	 * 
	 * Criacao de uma entidade no caso de nao existir ou alteracao no caso de
	 * existir.
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T salvarOuAlterar(T entity)throws ExcecaoGenerica{
		
		logger.info("::::Acionou o metodo salvarOuAlterar em GenericCRUD::::");
		
		try {

			((Session) getEntityManager().getDelegate()).saveOrUpdate(entity);

		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}

		return entity;
	}

	/**
	 * Exclus�o de uma entidade.
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public void excluir(T entity)throws ExcecaoGenerica {
		
		logger.info("::::Acionou o metodo excluir em GenericCRUD::::");
		
		try {
			
			((Session) getEntityManager().getDelegate()).delete(entity);

		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
	}

	/**
	 * Retorna uma lista completa da Entidade.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<T> listar()throws ExcecaoGenerica {
		
		logger.info("::::Acionou o metodo listar em GenericCRUD::::");
		
		List<T> lista = null;
		
		try {
			
			lista = buscarPorCriteria();
			
		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		
		
		return lista;
	}

	/**
	 * Retorna uma lista de objetos de acordo com o conjunto de criterios passados .
	 * 
	 * @param criterions
	 * @return
	 * @throws Exception
	 */
	public List<T> buscarPorCriteria(Criterion... criterions) throws ExcecaoGenerica{
		
		logger.info("::::Acionou o metodo buscarPorCriteria em GenericCRUD::::");

		Criteria criteria = null;

		try {
			
			criteria = ((Session) getEntityManager().getDelegate()).createCriteria(getClassePersistente());
			
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}

		return criteria.list();
	}

	/**
	 * Obtem uma entidade pelo seu ID.
	 * Usando o metodo find(); do EntityManager
	 * que retorna um objeto completo e o torna gerenciavel enquanto a sessao estiver aberta.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T buscarPorId(ID id) throws ExcecaoGenerica{	
		
		logger.info("::::Acionou o metodo buscarPorId em GenericCRUD::::");
		
		T entidade = null;
		
		try {
			
			entidade = getEntityManager().find(getClassePersistente(), id);
			
		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		
  	
		return entidade;
  	
	}

	/**
	 * Alteracao de uma entidade.
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T alterar(T entidade) throws ExcecaoGenerica{
		
		logger.info("::::Acionou o metodo alterar em GenericCRUD::::");
		
		try {
			
			((Session) getEntityManager().getDelegate()).update(entidade);

		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		return entidade;
	}

	/**
	 * Mesclagem (merge) de uma entidade.
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T mesclar(T entidade) throws ExcecaoGenerica{
		
		logger.info("::::Acionou o metodo mesclar em GenericCRUD::::");
		
		try {
			
			return getEntityManager().merge(entidade);

		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
	}
	
	public Long retornarMaxId() throws ExcecaoGenerica {
		Session sessao = (Session) getEntityManager().getDelegate();
		Criteria criteria = sessao.createCriteria(getClassePersistente());
		
		criteria.setProjection(Projections.projectionList().add(Projections.max("id")));
		
		Long idMaximo = (Long) criteria.uniqueResult();
		if(idMaximo == null){
			idMaximo = new Long(1);
		}
		
		return idMaximo;
	}
	
	/**
	 * Retorna uma lista de objetos de acordo com o conjunto de crit�rios passados .
	 * 
	 * @param criterions
	 * @return
	 * @throws Exception
	 */
	public List<T> buscarPorCriteria(String ordemAscCampo, boolean ordemAsc, Criterion... criterions) throws ExcecaoGenerica{
		
		logger.info("::::Acionou o m�todo buscarPorCriteria em GenericCRUD::::");

		Criteria criteria = null;
		Session session = null;
		try {
			criteria = ((Session) getEntityManager().getDelegate()).createCriteria(getClassePersistente());

			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
			if(ordemAsc){
				if(ordemAscCampo.contains(",")){
					criteria.addOrder(Order.asc(ordemAscCampo.split(",")[0].trim()));
					criteria.addOrder(Order.asc(ordemAscCampo.split(",")[1].trim()));
				}else{
					criteria.addOrder(Order.asc(ordemAscCampo));
				}
			}else{
				if(ordemAscCampo.contains(",")){
					criteria.addOrder(Order.desc(ordemAscCampo.split(",")[0].trim()));
					criteria.addOrder(Order.desc(ordemAscCampo.split(",")[1].trim()));
				}else{
					criteria.addOrder(Order.desc(ordemAscCampo));
				}
			}
			return criteria.list();
			

		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		} finally {
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
	/**
	 * Retorna uma lista completa da Entidade ordenados pelo campo ordemAscCampo
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<T> listarAscOuDesc(String ordemAscCampo, boolean buscarAsc)throws ExcecaoGenerica {
		
		logger.info("::::Acionou o m�todo listarAscOuDesc em GenericCRUD::::");
		
		List<T> lista = null;
		
		try {
			
			lista = buscarPorCriteria(ordemAscCampo, buscarAsc);
			
		} catch (HibernateException e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		
		return lista;
	}
	
	public Statistics getEstatisticas(){
		
		return ((Session) getEntityManager().getDelegate()).getSessionFactory().getStatistics();
		
	}
	
	public Query criarQuery(String query){
		
		return ((Session) getEntityManager().getDelegate()).createQuery(query);
		
	}
	
	public Criteria criarCriteria(){
		
		return ((Session) getEntityManager().getDelegate()).createCriteria(getClassePersistente());
		
	}
	
	public int contar() throws ExcecaoGenerica {
		Long quantidade = 0l;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select count (*) from "+getClassePersistente().getName());
			
			javax.persistence.Query query = getEntityManager().createQuery(queryString.toString());
			
			quantidade = (Long) query.getSingleResult();
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		return quantidade.intValue();
		
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	protected String translate(String campo) {
		return new StringBuilder("translate(").append(campo).append(",'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç'").append(",'AAAAAAAAEEEEIIOOOOOOUUUUCC')").toString();
	}
	
	protected static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	
	

}
