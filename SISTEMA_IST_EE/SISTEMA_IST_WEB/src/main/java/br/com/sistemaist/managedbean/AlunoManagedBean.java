package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.autenticacao.AuthenticationService;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.datamodel.LazyAlunoDataModel;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AlunoService;
import br.com.sistemaist.util.EncriptarMD5;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.vo.PaginaVO;
    
@ManagedBean(name="alunoMB")
@ViewScoped
public class AlunoManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;
	//private Ator ator = new Ator();

	private Aluno aluno = new Aluno();
	
	private String filtroNomeMatricula;
	
	@EJB
	private AlunoService alunoService;
	@ManagedProperty(value = "#{authenticationService}")
	private AuthenticationService authenticationService;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private List<Aluno> alunos = new ArrayList<Aluno>();
	
	
	private PaginaVO paginaVO = new PaginaVO();
	

	private LazyDataModel<Aluno> lazyAluno;
	
	public LazyDataModel<Aluno> getLazyAluno(){
		return lazyAluno;
	}
	
	@PostConstruct
	public void carregarFormulario() {
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Object idAluno = request.getParameter("idAluno");
		if(idAluno != null) {
			carregarAluno(Long.valueOf(idAluno.toString()));
		} else {
			aluno = new Aluno();
		}
	}

	/**
	 * Responsavel por salvar o ator
	 * @return
	 */
    public String salvarAluno(){
    	
    	try {
    		//Monta senha encriptada
    		if(null == aluno.getId()){
    			aluno.setSenha(EncriptarMD5.encriptar(aluno.getSenha()));
    		}
			alunoService.cadastrarAluno(aluno);
		} catch (ExcecaoGenerica e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		Util.montaMensagemFlashRedirect("Cadastro realizado com sucesso!","Sucesso!");
		if(Util.pegarAtor().getPerfil().equals(PerfilEnum.AD)){
			return "listagemAlunos";
		}
		return "home"; 
    }
   
    public String carregarAluno(Long id) {
		try {
			aluno = alunoService.carregarAlunoPorId(id);
		} catch (ExcecaoGenerica e) {
			log.error("N�o foi poss�vel carregar o aluno", e);
		}
		return "formularioAluno";
	}
	
	public void excluirAluno() {
		try {
			alunoService.excluirAlunoPorId(aluno.getId());
			Util.montaMensagemFlashRedirect("Aluno excluído com sucesso!", "Sucesso");
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível excluir o Aluno selecionado!", e);
			Util.montaMensagemFlashRedirect("Não foi possível excluir o Aluno selecionado!", "Falha");
		} catch (EJBException e) {
			log.error("Anluno já associada a outros itens."+e.getMessage());
	    	Util.montaMensagemFlashRedirectErro("Não é possível excluir o aluno "+aluno.getNome()+". Há históricos associados a ele!","Erro!");
	    	return;
		}
		
	}

	public void carregarAlunosCadastrados() {
		lazyAluno = new LazyAlunoDataModel(paginaVO);
		lazyAluno.setPageSize(10);
	}
	
	
    public void setAuthenticationService(AuthenticationService authenticationService) {
	    this.authenticationService = authenticationService;
	}
    
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public String getFiltroNomeMatricula() {
		return filtroNomeMatricula;
	}

	public void setFiltroNomeMatricula(String filtroNomeMatricula) {
		this.filtroNomeMatricula = filtroNomeMatricula;
	}
	
	public PaginaVO getPaginaVO() {
		return paginaVO;
	}

	public void setPaginaVO(PaginaVO paginaVO) {
		this.paginaVO = paginaVO;
	}
    
}  