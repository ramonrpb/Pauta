package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.email.CorreioEnvio;
import br.com.sistemaist.email.CorreioVO;
import br.com.sistemaist.email.EnvioEmailException;
import br.com.sistemaist.email.MontaMensagemEmail;
import br.com.sistemaist.service.AtorService;
import br.com.sistemaist.util.EncriptarMD5;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.util.Utilitario;
    
@ManagedBean(name="gerenciarSenhaMB")
@ViewScoped
public class GerenciarSenhaManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;
	
	private String login = "@faeterj-petropolis.edu.br";
	
	//Utilizado no alterar senha, contem o valor da senha antes de trocar pela nova
	private String senhaAtual;
	//Senha nova, substituira que a atiga
	private String senhaNova;
	//Utilizado como codigo repassado por e-mail no caso de esqueci minha senha
	private String codigo;
	//Utilizado para exibir o campo de codigo, quando o usuario esqueceu a senha
	private boolean digitarCodigo = false;
	
	@EJB
	private AtorService atorBean;
	
	/**
	 * Carrega os dados iniciais da tela
	 */
	@PostConstruct
	public void carregarTela() {
		
		//Recupera os valores caso venha do fluxo de esqueci minha senha, para pré-preencher os valores.
		codigo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigo");
		String loginURL = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("login");
		
		if(null != loginURL){
			login = loginURL;
		}
		
		if(codigo != null){
			digitarCodigo = true;
		}else{
			//alteracao de senha
			if(null != Util.pegarAtor()){
				login = Util.pegarAtor().getEmail();
			}
			digitarCodigo = false;
		}
	}
	
	/**
	 * Metodo responsavel pela alteracao de senha do ator
	 * @return
	 */
    public String alterarSenha(){
    	
    	try{
	    	if(codigo == null){
	    		//Alteração de senha normal
		    	Boolean logado = atorBean.validarLogin(login, EncriptarMD5.encriptar(senhaAtual));
		   		if (!logado) {
		   			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha atual não confere", "Senha atual não confere");  
			        FacesContext.getCurrentInstance().addMessage("mensagemFalhaLogin", msg);
			        return null;
		   	    }
	    	}else{
	    		//alteração de senha, esqueci minha senha
	    		Boolean validado = atorBean.validarCodigo(login, codigo);
		   		if (!validado) {
		   			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código não confere ou já expirado", "Código não confere ou já expirado");  
			        FacesContext.getCurrentInstance().addMessage("mensagemFalhaLogin", msg);
			        return null;
		   	    }
	    	}
    	
			atorBean.alterarSenhaAtor(login, EncriptarMD5.encriptar(senhaNova));
		} catch (ExcecaoGenerica e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		
   		montaMensagemFlashRedirect("Senha alterada com sucesso !","Sucesso!");
   		
    	return "home";
    }
    
    /**
     * Metodo responsavel por enviar um email ao login que esqueceu a senha com o codigo gerado
     * @return
     */
    public void gerarCodigoEnviarEmail(){
    	
    	try {
    		
    		if(null != login && ! login.contains("@") && !login.equals("admin") ){
    			login += "@faeterj-petropolis.edu.br";
        	}
    		
			String emailPara = atorBean.buscarEmailPorLogin(login);
			
			if(null == emailPara){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login inválido ou não existe e-mail associado a este login", "Login inválido");  
		        FacesContext.getCurrentInstance().addMessage("mensagemFalhaLogin", msg);
		        return;
			}
		
			String codigo = Utilitario.gerarSenha(8);//TODO colocar parametro em properties numeroDigitos
			Calendar dataExpiracaoSenha = Utilitario.gerarDataExpiracaoSenha();
			
			atorBean.salvarCodigoEDataExpiracaoCodigo(login, codigo, dataExpiracaoSenha);
			
			CorreioEnvio correioEnvio = new CorreioEnvio();
			//Email para o usuario
			CorreioVO correioVO = MontaMensagemEmail.montarMensagemParaUsuario(emailPara, login, dataExpiracaoSenha, codigo);
			correioEnvio.enviarEMail(correioVO);
			
			montaMensagemFlashRedirect("O e-mail foi enviado com sucesso !","Sucesso!");
			
			digitarCodigo = true;
		}catch (ExcecaoGenerica e) {
			e.getMessage();
		} catch (EnvioEmailException e1) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR , "Ocorreu um erro ao enviar o email", e1.getMessage());
			FacesContext.getCurrentInstance().addMessage("mensagem", message);
		}
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

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}
	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		return senhaNova;
	}
	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public boolean isDigitarCodigo() {
		return digitarCodigo;
	}

	public void setDigitarCodigo(boolean digitarCodigo) {
		this.digitarCodigo = digitarCodigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}  