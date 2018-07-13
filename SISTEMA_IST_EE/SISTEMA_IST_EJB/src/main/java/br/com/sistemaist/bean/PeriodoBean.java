package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sistemaist.dao.PeriodoDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.service.PeriodoService;

@Stateless
public class PeriodoBean implements PeriodoService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica(){
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public PeriodoDAO getPeriodoDAO() throws ExcecaoGenerica{
		return (PeriodoDAO) daoFabrica.getDAO(Periodo.class);
	}
	
	@Override
	public List<Periodo> buscarPeriodos() throws ExcecaoGenerica {
		return getPeriodoDAO().listar();
	}

}
