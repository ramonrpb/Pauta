package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import br.com.sistemaist.dao.AlunoDAO;
import br.com.sistemaist.dao.AtorDAO;
import br.com.sistemaist.dao.ProfessorDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.service.AtorService;
import br.com.sistemaist.util.Utilitario;

@Stateless
public class AtorBean implements AtorService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica() {
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public AtorDAO getAtorDAO() throws ExcecaoGenerica{
		return (AtorDAO) daoFabrica.getDAO(Ator.class);
	}
	
	public ProfessorDAO getProfessorDAO() throws ExcecaoGenerica{
		return (ProfessorDAO) daoFabrica.getDAO(Professor.class);
	}
	
	public AlunoDAO getAlunoDAO() throws ExcecaoGenerica{
		return (AlunoDAO) daoFabrica.getDAO(Aluno.class);
	}
	
	public Ator inserirAtor(Ator ator) throws ExcecaoGenerica{
		ator = getAtorDAO().salvar(ator);
		try{
			em.flush();
		}catch(PersistenceException e){
			String erro = e.getMessage().split("ERRO:")[1];
			throw new ExcecaoGenerica(erro);
		}
		return ator;
	}

	public Ator alterarAtor(Ator ator) throws ExcecaoGenerica {
		ator = getAtorDAO().alterar(ator);
		return ator;
	}
	
	public String buscarEmailPorLogin(String login) throws ExcecaoGenerica {
		return getAtorDAO().buscarEmailPorLogin(login);
	}
	
	public boolean verificarExistenciaLogin(String login, Long id) throws ExcecaoGenerica {
		return getAtorDAO().verificarExistenciaLogin(login, id);
	}

	@Override
	public Boolean validarCodigo(String login, String codigo) throws ExcecaoGenerica {
		return getAtorDAO().validarAtorPorLoginECodigo(login, codigo);
	}

	@Override
	public Boolean validarLogin(String login, String senhaAtual) throws ExcecaoGenerica {
		return getAtorDAO().validarAtorPorLoginESenha(login, senhaAtual);
	}

	@Override
	public void alterarSenhaAtor(String login, String senha) throws ExcecaoGenerica {
		Ator ator = getAtorDAO().buscarAtorPorLogin(login);
		ator.setSenha(senha);
		getAtorDAO().alterar(ator);
	}
	
	@Override
	public void salvarCodigoEDataExpiracaoCodigo(String login, String codigo, Calendar dataExpiracaoSenha) throws ExcecaoGenerica {

		getAtorDAO().salvarCodigoEDataValidadeCodigo(login, codigo, dataExpiracaoSenha);
		
	}

	@Override
	public void mudarSufixoEmails(String sufixoEmail) throws ExcecaoGenerica {
		List<Ator> atores = getAtorDAO().listar();
		for(Ator a : atores) {
			if(a.getEmail().contains("@")) {
				String email[] = a.getEmail().split("@");
				String prefixoEmail = email[0];
				a.setEmail(prefixoEmail.concat(sufixoEmail));
				getAtorDAO().salvarOuAlterar(a);
			}
			
		}
		
	}

	@Override
	public void acrescentarZerosAFrente(Integer quantidadeDigitos) throws ExcecaoGenerica {
		List<Ator> atores = getAtorDAO().listar();
		for(Ator a : atores) {
			if(a instanceof Professor) {
				Professor p = (Professor) a;
				p.setMatricula(Utilitario.acrescentarZeroAFrente(quantidadeDigitos, p.getMatricula()));
				getProfessorDAO().salvarOuAlterar(p);
			} else if(a instanceof Aluno){
				Aluno al = (Aluno) a;
				al.setMatricula(Utilitario.acrescentarZeroAFrente(quantidadeDigitos, al.getMatricula()));
				getAlunoDAO().salvarOuAlterar(al);
				
			}
		}
	}
}
