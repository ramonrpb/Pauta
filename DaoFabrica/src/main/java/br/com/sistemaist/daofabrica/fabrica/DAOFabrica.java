package br.com.sistemaist.daofabrica.fabrica;

import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;

public interface DAOFabrica {

	/**
	 * Metodo que retorna a implementacao da classe DAO para uma determinada entidade persistente.
	 * Para que possa ser utilizado, a necessario que a nomenclatura dos pacotes e nomes das classes das
	 * entidades e das classes de implementacao da camada DAO sigam o seguinte padrao.
	 * @Exemplo  
	 * pacote da entidade: br.com.goldencross.entidades.controleacesso
	 * pacote da implementacao DAO: br.com.goldencross.projetoejb.DAO.implementacao.controleacesso
	 * nome da entidade: Entidade
	 * nome da implementacao DAO: EntidadeCRUD
	 * @Importante Todas as implementacoes de DAO`s devem extender GenericCRUD e implementar sua 
	 * interface que extende GenericDAO 
	 * @Importante quando infocar este metodo deve-se fazer o cast do retorno para a interface desejada
	 * @Exemplo EntidadeDAO entidadeDAO = (EntidadeDAO) daoFactory.getDAO(Entidade.class);
	 * @param Class entidade
	 * @return instancia da implementacao DAO referente a entidade
	 * @throws ExcecaoGenerica
	 */
	CRUDGenerico<?, ?> getDAO(Class<?> entidade) throws ExcecaoGenerica;
}
