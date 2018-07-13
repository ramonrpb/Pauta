package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sistemaist.dao.AlunoDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AlunoService;
import br.com.sistemaist.vo.PaginaVO;

@Stateless
public class AlunoBean implements AlunoService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	public AlunoDAO getAlunoDAO(EntityManager em) throws ExcecaoGenerica{
		daoFabrica = new DAOFabricaImpl(em);
		return (AlunoDAO) daoFabrica.getDAO(Aluno.class);
	}
	
	@Override
	public Aluno cadastrarAluno(Aluno aluno) throws ExcecaoGenerica {
		aluno.setPerfil(PerfilEnum.AL);
		return getAlunoDAO(em).salvarOuAlterar(aluno);
	}
	
	@Override
	public Aluno carregarAlunoPorId(Long id) throws ExcecaoGenerica {
		return getAlunoDAO(em).buscarPorId(id);
	}

	@Override
	public void excluirAlunoPorId(Long id) throws ExcecaoGenerica {
		getAlunoDAO(em).excluir(getAlunoDAO(em).buscarPorId(id));
		
	}

	@Override
	public List<Aluno> carregarAlunosCadastrados() throws ExcecaoGenerica {
		return getAlunoDAO(em).listarAscOuDesc("nome", true);
	}

	@Override
	public List<Aluno> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica {
		return getAlunoDAO(em).listarPorPagina(paginaVO);
	}

	@Override
	public int contarAlunos() throws ExcecaoGenerica {
		return getAlunoDAO(em).contarAlunos();
	}

	@Override
	public List<Aluno> listar() throws ExcecaoGenerica {
		return getAlunoDAO(em).listar();
	}

	@Override
	public boolean validarAkMatricula(Long id, String matricula) throws ExcecaoGenerica {
		return getAlunoDAO(em).validarAkMatricula(id, matricula);
	}

	@Override
	public int contarAlunos(String filtro) throws ExcecaoGenerica {
		return getAlunoDAO(em).contarAlunos(filtro);
	}

}
