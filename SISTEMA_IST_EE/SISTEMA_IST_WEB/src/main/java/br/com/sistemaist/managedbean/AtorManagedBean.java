package br.com.sistemaist.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.autenticacao.AuthenticationService;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.service.AtorService;
import br.com.sistemaist.util.EncriptarMD5;
import br.com.sistemaist.util.Util;
    
@ManagedBean(name="atorMB")
@ViewScoped
public class AtorManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;
	private Ator ator = new Ator();
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@EJB
	private AtorService atorBean;
	@ManagedProperty(value = "#{authenticationService}")
	private AuthenticationService authenticationService;
	
	
	private String sufixoEmail;
	
	private Integer quantidadeDigitos;
	
	@PostConstruct
	public void carregarTela() {
		ator = Util.pegarAtor();
		if(ator == null){
			ator = new Ator();
		}
	}

	/**
	 * Responsavel por salvar o ator
	 * @return
	 */
    public String salvaraAtor(){
    	String senha;
    	try {
    		//guarda a senha nao encriptada para logar no sistema(authentication encriptara a senha)
    		senha = ator.getSenha();
    		
    		//salva o usuario com a senha encriptada
    		ator.setSenha(EncriptarMD5.encriptar(ator.getSenha()));
			atorBean.inserirAtor(ator);
		} catch (ExcecaoGenerica e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "Erro na persistencia");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return null;
		}
    	
    	//Autentica o usuario (coloca o usuario que acabou de criar logado no sistema)
		Boolean logado = authenticationService.login(ator.getEmail(), senha);
   		if (!logado) {
   			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Login ou Senha inválidos");  
	        FacesContext.getCurrentInstance().addMessage("mensagemFalhaLogin", msg);
	        return null;
   	    }

   		montaMensagemFlashRedirect("Cadastro realizado com sucesso !","Sucesso!");
		return "home";
    }
    
    /**
     * Metodo respons�vel pela edi��o do ator
     * @return
     */
	public String editarAtor(){
    	
    	try {
			atorBean.alterarAtor(ator);
		} catch (ExcecaoGenerica e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "Erro na persistencia");  
	        FacesContext.getCurrentInstance().addMessage("mensagemFalhaLogin", msg);
	        return null;
		}
		
    	montaMensagemFlashRedirect("Alteração realizada com sucesso !","Sucesso!");
    	
    	return "home";
    }

    /**
     * Metodo responsavel por enviar um email ao login que esqueceu a senha
     * @return
     */
    public String esqueciMinhaSenhaNovoCodigo(){
    	
		try {
			boolean existe = atorBean.verificarExistenciaLogin(ator.getEmail(), null);
			if( ! existe){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login inv�lido", "Login inv�lido");  
		        FacesContext.getCurrentInstance().addMessage("mensagemFalhaLogin", msg);
		        return null;
			}
		} catch (ExcecaoGenerica e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	montaMensagemFlashRedirect("O e-mail foi enviado com sucesso !","Sucesso!");
    	return "alterarSenha";
    }
    
    /**
     * Adiciona a mensagem de INFO para ser exibida, quando trocamos de tela
     * @param mensagem
     * @param mensagem2
     */
    private void montaMensagemFlashRedirect(String mensagem, String mensagem2) {

    	// MENSAGEM REDIRECIONADA PARA SER EXIBIDA EM OUTRA PAGINA
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setRedirect(true);
		flash.setKeepMessages(true);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO , mensagem, mensagem2);
		facesContext.addMessage("mensagem", message);
	}
    
    public void mudarSufixoEmails() {
    	try {
			atorBean.mudarSufixoEmails(sufixoEmail);
			Util.montaMensagemFlashRedirect("E-mails / Login alterados com sucesso!", "SUCESSO");
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemErroSemFlashRedirect("Não foi possível alterar e-mail", "ERRO");
		}
    }
    
    public void acrescentarZerosAFrente() {
    	try {
			atorBean.acrescentarZerosAFrente(quantidadeDigitos);
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemErroSemFlashRedirect("Não foi possível alterar as matrículas", "ERRO");
		}
    	
    	Util.montaMensagemFlashRedirect("Matrículas atualizadas com sucesso!", "Sucesso");
    }
    
	public void setAuthenticationService(AuthenticationService authenticationService) {
	    this.authenticationService = authenticationService;
	}
    
	public Ator getAtor() {
		return ator;
	}
	public void setAtor(Ator ator) {
		this.ator = ator;
	}

	public String getSufixoEmail() {
		return sufixoEmail;
	}

	public void setSufixoEmail(String sufixoEmail) {
		this.sufixoEmail = sufixoEmail;
	}

	public Integer getQuantidadeDigitos() {
		return quantidadeDigitos;
	}

	public void setQuantidadeDigitos(Integer quantidadeDigitos) {
		this.quantidadeDigitos = quantidadeDigitos;
	}
}  