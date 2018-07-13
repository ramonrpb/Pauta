package br.com.sistemaist.email;

public class EnvioEmailException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public EnvioEmailException() {
	}

	public EnvioEmailException(String message) {
		super(message);
	}

	public EnvioEmailException(Throwable cause) {
		super(cause);
	}

	public EnvioEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}
