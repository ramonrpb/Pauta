package br.com.sistemaist.bean;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sistemaist.dao.AtorDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.service.ProvedorAutenticacaoService;

@Stateless
public class ProvedorAutenticacaoBean implements ProvedorAutenticacaoService, Serializable{
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	public AtorDAO getAtorDAO(EntityManager em) throws ExcecaoGenerica{
		daoFabrica = new DAOFabricaImpl(em);
		return (AtorDAO) daoFabrica.getDAO(Ator.class);
	}

	@Override
	public Ator buscarAtorPorLoginESenha(String login, String senha) throws ExcecaoGenerica {
		return getAtorDAO(em).buscarAtorPorLoginESenha(login, senha);
	}
	
}
