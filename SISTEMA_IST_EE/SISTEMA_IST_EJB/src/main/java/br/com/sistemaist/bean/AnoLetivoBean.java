package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import br.com.sistemaist.dao.AnoLetivoDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.util.Utilitario;

@Stateless
public class AnoLetivoBean implements AnoLetivoService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica(){
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public AnoLetivoDAO getAnoLetivoDAO() throws ExcecaoGenerica{
		return (AnoLetivoDAO) daoFabrica.getDAO(AnoLetivo.class);
	}
	
	@Override
	public void salvar(AnoLetivo anoLetivo) throws ExcecaoGenerica{
		try{
			getAnoLetivoDAO().salvar(anoLetivo);
		}catch(PersistenceException e){
			throw new ExcecaoGenerica(e.getMessage(), e);
		}
	}
	
	@Override
	public void alterar(AnoLetivo anoLetivo) throws ExcecaoGenerica {
		try{
			anoLetivo = getAnoLetivoDAO().alterar(anoLetivo);
		}catch(PersistenceException e){
			throw new ExcecaoGenerica(e.getMessage(), e);
		}
	}
	
	@Override
	public void excluir(AnoLetivo anoLetivo) throws ExcecaoGenerica {
		getAnoLetivoDAO().excluir(getAnoLetivoDAO().buscarPorId(anoLetivo.getId()));
	}
	
	@Override
	public List<AnoLetivo> buscarAnosLetivos() throws ExcecaoGenerica {
		return getAnoLetivoDAO().listarAscOuDesc("ano, semestre", false);
	}
	
	@Override
	public List<AnoLetivo> buscarAnosLetivosAbertosAteHoje() throws ExcecaoGenerica {
		return getAnoLetivoDAO().buscarAnosLetivosAbertosAteHoje(Utilitario.zerarHorario(new Date()));
	}

	@Override
	public boolean validarAkAnoLetivo(Integer ano, Integer semestre, Long id) throws ExcecaoGenerica {
		return getAnoLetivoDAO().validarAkAnoLetivo(ano, semestre, id);
	}

	@Override
	public AnoLetivo buscarAnoLetivoPorId(Long id) throws ExcecaoGenerica {
		return getAnoLetivoDAO().buscarPorId(id);
	}

	@Override
	public List<AnoLetivo> buscarAnosLetivosAbertos() throws ExcecaoGenerica {
		return getAnoLetivoDAO().buscarAnosLetivosAbertos(Utilitario.zerarHorario(new Date()));
	}


}
