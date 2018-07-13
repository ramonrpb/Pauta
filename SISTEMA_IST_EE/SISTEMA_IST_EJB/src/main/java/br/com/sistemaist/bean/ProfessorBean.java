package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sistemaist.dao.ProfessorDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.service.ProfessorService;
import br.com.sistemaist.vo.PaginaVO;

@Stateless
public class ProfessorBean implements ProfessorService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica() {
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public ProfessorDAO getProfessorDAO() throws ExcecaoGenerica{
		
		return (ProfessorDAO) daoFabrica.getDAO(Professor.class);
	}

	@Override
	public Professor cadastrarProfessor(Professor professor) throws ExcecaoGenerica {
		return getProfessorDAO().salvarOuAlterar(professor);
	}

	@Override
	public List<Professor> carregarProfessoresCadastrados() throws ExcecaoGenerica {
		return getProfessorDAO().listar();
	}

	@Override
	public Professor carregarProfessorPorId(Long id) throws ExcecaoGenerica {
		return getProfessorDAO().buscarPorId(id); 
	}

	@Override
	public void excluirProfessorPorId(Long id) throws ExcecaoGenerica {
		getProfessorDAO().excluir(getProfessorDAO().buscarPorId(id));		
	}

	@Override
	public List<Professor> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica {
		return getProfessorDAO().listarPorPagina(paginaVO);
	}

	@Override
	public int contarProfessores(String filtro) throws ExcecaoGenerica {
		return getProfessorDAO().contar(filtro);
	}

	@Override
	public boolean validarAkMatricula(Long codigo, String valor) throws ExcecaoGenerica {

		return getProfessorDAO().validarAkMatricula(codigo, valor);
	}
	
}
