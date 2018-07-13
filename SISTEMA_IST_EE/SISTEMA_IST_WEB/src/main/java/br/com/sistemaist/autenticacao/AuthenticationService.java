package br.com.sistemaist.autenticacao;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService implements Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	public boolean login(String username, String password) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			Autenticacao autenticacao = (Autenticacao) authenticationManager.authenticate(token);
			if (autenticacao.isAuthenticated()) {
			    SecurityContextHolder.getContext().setAuthentication(autenticacao);
			    return true;
			}
		}catch (AuthenticationException e) {}
		return false;
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		invalidateSession();
	}

	public User getUsuarioLogado() {
	    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private void invalidateSession() {
	    FacesContext fc = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
	    session.invalidate();
	}

}

