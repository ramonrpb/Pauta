package br.com.sistemaist.email;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * VO que contem os dados necessários para montar o email
 *
 */
public class CorreioVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String emailDe;
	private String emailPara;
	private String tituloEmail;
	private List<String> mensagens = new ArrayList<String>();
	private String usuarioAutorizacao;
	private String senhaAutorizacao;
	private List<File> listaArquivos;
	private List<String> emailsPara = new ArrayList<String>();
	
	public String getEmailDe() {
		return emailDe;
	}
	public void setEmailDe(String emailDe) {
		this.emailDe = emailDe;
	}

	public String getEmailPara() {
		return emailPara;
	}
	public void setEmailPara(String emailPara) {
		this.emailPara = emailPara;
	}

	public String getTituloEmail() {
		return tituloEmail;
	}
	public void setTituloEmail(String tituloEmail) {
		this.tituloEmail = tituloEmail;
	}

	public List<String> getMensagens() {
		return mensagens;
	}
	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}
	public void addMensagens(String mensagem) {
		this.mensagens.add(mensagem);
	}
	public String getUsuarioAutorizacao() {
		return usuarioAutorizacao;
	}
	public void setUsuarioAutorizacao(String usuarioAutorizacao) {
		this.usuarioAutorizacao = usuarioAutorizacao;
	}
	public String getSenhaAutorizacao() {
		return senhaAutorizacao;
	}
	
	public void setSenhaAutorizacao(String senhaAutorizacao) {
		this.senhaAutorizacao = senhaAutorizacao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<File> getListaArquivos() {
		if(null == listaArquivos){
			listaArquivos = new ArrayList<File>();
		}
		return listaArquivos;
	}
	public void setListaArquivos(List<File> listaArquivos) {
		this.listaArquivos = listaArquivos;
	}
	public List<String> getEmailsPara() {
		return emailsPara;
	}
	public void setEmailsPara(List<String> emailsPara) {
		this.emailsPara = emailsPara;
	}
}

