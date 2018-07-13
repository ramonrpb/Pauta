package br.com.sistemaist.autenticacao;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import br.com.sistemaist.entidades.Ator;

public class Autenticacao implements Authentication{
private static final long serialVersionUID = -448422158890048086L;
	
	private boolean authenticated = false;
	private GrantedAuthority grantedAuthority;
	private Authentication authentication;
	private Ator ator;
	
	public Autenticacao(){
		
	}
	
	public Autenticacao(String role, Authentication authentication){
		this.grantedAuthority = new GrantedAuthorityImpl(role);
		this.authentication = authentication;
	}
	
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(grantedAuthority);
		return authorities;
	}

	public Object getCredentials() {
		return authentication.getCredentials();
	}

	public Object getDetails() {
		return authentication.getDetails();
	}

	public Object getPrincipal() {
		return authentication.getPrincipal();
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.authenticated = isAuthenticated;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public GrantedAuthority getGrantedAuthority() {
		return grantedAuthority;
	}

	public void setGrantedAuthority(GrantedAuthority grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}

	public Ator getAtor() {
		return ator;
	}

	public void setAtor(Ator ator) {
		this.ator = ator;
	}	
	
}
