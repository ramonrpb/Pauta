package br.com.sistemaist.email;

import java.io.Serializable;

public class ConstantesDeProperties implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//PARA RECUPERAR DO PROPERTIES
	/* Constants utilizadas para montagem da mensagem a ser enviada por e-mail ao usu�rio*/
	public static final String CORREIO_PROPERTIES = "/correio.properties";
	public static final String SMTP_AUTORIZACAO_USER = "smtp.auth.user";
	public static final String SMTP_AUTORIZACAO_SENHA = "smtp.auth.passwd";
	public static final String MENSAGEM_DE = "mensagem.de";

	public static final String MENSAGEM_TITULO = "mensagem.titulo";
	public static final String MENSAGEM_INTRODUCAO = "mensagem.introducao";
	public static final String MENSAGEM_INICIO = "mensagem.inicio";
	public static final String MENSAGEM_MENSAGEM = "mensagem.mensagem";
	public static final String MENSAGEM_ATENCIOSAMENTE = "mensagem.email.atenciosamente";
	public static final String MENSAGEM_ATENCIOSAMENTE_COMPLEMENTO = "mensagem.email.atenciosamente.complemento";
	
	//Estes valores ser�o substituidos na constru��o das mensagens
	public static final String PARAMETRO_0 = "{0}";
	public static final String PARAMETRO_1 = "{1}";

	public static final String PULA_LINHA = "\n";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String UTF8 = "UTF-8";
	
}
