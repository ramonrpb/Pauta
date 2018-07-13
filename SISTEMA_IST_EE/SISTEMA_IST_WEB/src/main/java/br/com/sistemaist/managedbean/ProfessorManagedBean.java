package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.datamodel.LazyAlunoDataModel;
import br.com.sistemaist.datamodel.LazyProfessorDataModel;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.ProfessorService;
import br.com.sistemaist.util.EncriptarMD5;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.util.UtilValidacoes;
import br.com.sistemaist.vo.PaginaVO;

@ManagedBean(name="professorMB")
@ViewScoped
public class ProfessorManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Professor professor;
	
	private List<Professor> professores;
	
	@EJB
	private ProfessorService professorService;
	
	

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Object idProfessor;
	
	private PaginaVO paginaVO = new PaginaVO();
	
	private LazyDataModel<Professor> lazyProfessores;

	@PostConstruct
	public void carregarFormulario() {
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		idProfessor = request.getParameter("idProfessor");
	
		if(idProfessor != null) {
			carregarProfessor(Long.valueOf(idProfessor.toString()));
		} else {
			professor = new Professor();
		}
	}
	
	public String salvarProfessor() {
		try {
			if(null == professor.getId()){
				professor.setSenha(EncriptarMD5.encriptar(professor.getSenha()));
			}
			professorService.cadastrarProfessor(professor);
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível cadastrar o professor: "+professor.getNome(), e);
		}
		
		Util.montaMensagemFlashRedirect("Professor cadastrado com sucesso", "Sucesso");
		if(Util.pegarAtor().getPerfil().equals(PerfilEnum.AD)){
			return "listagemProfessores";
		}
		return "home"; 
	}
	
	public void carregarProfessoresCadastrados() {
		int quantidadeProfessores = 0;
		try {
			quantidadeProfessores = professorService.contarProfessores(paginaVO.getFiltro());
			lazyProfessores = new LazyProfessorDataModel(paginaVO);
			lazyProfessores.setPageSize(10);
			lazyProfessores.setRowCount(quantidadeProfessores);
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível carregar os professores", e);
		}
		
	}
	
	
	public String carregarProfessor(Long id) {
		try {
			professor = professorService.carregarProfessorPorId(id);
			
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível carregar o professor", e);
		}
		return "formularioProfessor";
	}
	
	public void excluirProfessor() {
		try {
			professorService.excluirProfessorPorId(professor.getId());
			Util.montaMensagemFlashRedirect("Professor excluído com sucesso!", "Sucesso");
			professor = null;
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível excluir o professor selecionado", e);
			Util.montaMensagemFlashRedirect("Não foi possível excluir o professor selecionado", "Falha");
		} catch (EJBException e) {
			log.error("Professor já associada a outros itens."+e.getMessage());
	    	Util.montaMensagemFlashRedirectErro("Não é possível excluir o professor "+professor.getNome()+". Há históricos associados a ele!","Erro!");
	    	return;
		}
		
		
	}
	
	public Professor getProfessor() {
		
		return professor;
	}

	public void setProfessor(Professor professor) {
		
		this.professor = professor;
	}

	public List<Professor> getProfessores() {
		return professores;
	}

	public void setProfessores(List<Professor> professores) {
		this.professores = professores;
	}

	public Object getIdProfessor() {
		return idProfessor;
	}

	public void setIdProfessor(Object idProfessor) {
		this.idProfessor = idProfessor;
	}

	public LazyDataModel<Professor> getLazyProfessores() {
		return lazyProfessores;
	}

	public void setLazyProfessores(LazyDataModel<Professor> lazyProfessores) {
		this.lazyProfessores = lazyProfessores;
	}

	public PaginaVO getPaginaVO() {
		return paginaVO;
	}

	public void setPaginaVO(PaginaVO paginaVO) {
		this.paginaVO = paginaVO;
	}
	
	

}
