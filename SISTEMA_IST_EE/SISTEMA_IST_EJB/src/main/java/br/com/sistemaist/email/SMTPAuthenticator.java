package br.com.sistemaist.email;

import java.io.Serializable;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private PasswordAuthentication passwordAuthentication;
	
	public SMTPAuthenticator(String username, String password) {
		super();
		
		passwordAuthentication = new PasswordAuthentication(username, password);
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		
		return this.passwordAuthentication;
	}	
}
