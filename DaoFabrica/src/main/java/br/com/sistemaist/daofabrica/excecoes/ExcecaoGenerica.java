package br.com.sistemaist.daofabrica.excecoes;

public class ExcecaoGenerica extends Exception {

	private static final long serialVersionUID = 1L;

	public ExcecaoGenerica() {
		
	}

	public ExcecaoGenerica(String message) {
		super(message);
	}

	public ExcecaoGenerica(Throwable cause) {
		super(cause);
	}

	public ExcecaoGenerica(String message, Throwable cause) {
		super(message, cause);
	}

}
