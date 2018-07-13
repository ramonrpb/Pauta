package br.com.sistemaist.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.sistemaist.autenticacao.AuthenticationService;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.util.Util;
    
@ManagedBean(name="loginMB")
@RequestScoped
public class LoginManagedBean implements Serializable {  
      
	private static final long serialVersionUID = 1L;
	private Ator ator; 
	private String username = "@faeterj-petropolis.edu.br";
	private String password;
	
	@ManagedProperty(value = "#{authenticationService}")
	private AuthenticationService authenticationService;
      
    @PostConstruct
    public void iniciar(){
        ator = Util.pegarAtor();
    }
    
    public String login(){
    	
    	if(null != username && ! username.contains("@") && !username.equals("admin")){
    		username += "@faeterj-petropolis.edu.br";
    	}
    	
   		Boolean logado = authenticationService.login(username, password);
   		if (!logado) {
   			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login ou Senha inválidos.", "Login ou Senha inválidos.");  
	        FacesContext.getCurrentInstance().addMessage("messages", msg);
   	      return null;
   	    }

   	    return "home";
    }
    
    public String logout() {
    	SecurityContextHolder.getContext().setAuthentication(null);
    	invalidateSession();
    	return "paginaInicial";
    }
    
    private void invalidateSession() {
    	FacesContext fc = FacesContext.getCurrentInstance ();
    	HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
    	session.invalidate();
	}
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
}  