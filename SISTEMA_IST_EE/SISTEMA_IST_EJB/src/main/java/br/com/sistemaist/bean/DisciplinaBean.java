package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import br.com.sistemaist.dao.DisciplinaDAO;
import br.com.sistemaist.dao.PeriodoDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.service.DisciplinaService;
import br.com.sistemaist.vo.CombosDisciplinaVO;

@Stateless
public class DisciplinaBean implements DisciplinaService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica(){
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public DisciplinaDAO getDisciplinaDAO() throws ExcecaoGenerica{
		return (DisciplinaDAO) daoFabrica.getDAO(Disciplina.class);
	}
	
	public PeriodoDAO getPeriodoDAO() throws ExcecaoGenerica{
		return (PeriodoDAO) daoFabrica.getDAO(Periodo.class);
	}
	
	@Override
	public void salvar(Disciplina disciplina) throws ExcecaoGenerica{
		try{
			getDisciplinaDAO().salvar(disciplina);
		}catch(PersistenceException e){
			throw new ExcecaoGenerica(e.getMessage(), e);
		}
	}
	
	@Override
	public void alterar(Disciplina disciplina) throws ExcecaoGenerica {
		try{
			disciplina = getDisciplinaDAO().alterar(disciplina);
		}catch(PersistenceException e){
			throw new ExcecaoGenerica(e.getMessage(), e);
		}
	}
	
	@Override
	public void excluir(Disciplina disciplina) throws ExcecaoGenerica {
		getDisciplinaDAO().excluir(getDisciplinaDAO().buscarPorId(disciplina.getId()));
	}
	
	@Override
	public List<Disciplina> buscarDisciplinas() throws ExcecaoGenerica {
		return getDisciplinaDAO().listarAscOuDesc("nome", true);
	}

	@Override
	public boolean validarAkDisciplina(String valor) throws ExcecaoGenerica {
		return getDisciplinaDAO().validarAkDisciplina(valor);
	}

	@Override
	public CombosDisciplinaVO buscarDadosIniciaisTela() throws ExcecaoGenerica {
		
		CombosDisciplinaVO combosDisciplinaVO = new CombosDisciplinaVO();
		combosDisciplinaVO.setListaDisciplina(getDisciplinaDAO().listarAscOuDesc("nome", true));
		combosDisciplinaVO.setPeriodos(getPeriodoDAO().listarAscOuDesc("id", true));

		return combosDisciplinaVO;
	}

}
