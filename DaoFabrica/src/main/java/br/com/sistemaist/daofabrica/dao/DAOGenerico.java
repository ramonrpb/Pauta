package br.com.sistemaist.daofabrica.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;


public interface DAOGenerico<T, ID extends Serializable> {
	
	 EntityManager getEntityManager();

	 void setEntityManager(EntityManager entityManager);

	 List<T> buscarPorCriteria(Criterion... criterions) throws ExcecaoGenerica ;

	 void excluir(T entity) throws ExcecaoGenerica ;

	 List<T> listar() throws ExcecaoGenerica ;

	 T buscarPorId(ID id)throws ExcecaoGenerica ;

	 T salvar(T entity)throws ExcecaoGenerica ;

	 T alterar(T entity)throws ExcecaoGenerica ;

	 T mesclar(T entity)throws ExcecaoGenerica ;

	 T salvarOuAlterar(T entity)throws ExcecaoGenerica ;
	
	 void destacar(T entity) throws ExcecaoGenerica ;
	
	 Long retornarMaxId() throws ExcecaoGenerica;
	 
	 List<T> listarAscOuDesc(String ordemAscCampo, boolean buscarAsc)throws ExcecaoGenerica;
	 
	 public int contar() throws ExcecaoGenerica;
}
