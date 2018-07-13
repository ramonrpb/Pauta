package br.com.sistemaist.util;

import java.io.Serializable;

public class UtilValidacoes implements Serializable{

	private static final long serialVersionUID = 1L;

	public static StringBuffer validaNuloOuVazio(Long campo, String mensagemErro, StringBuffer mensagensErro){
		if(null == campo || "".equals(campo)){
			mensagensErro = adicionaMensagem(mensagensErro, mensagemErro);
		}
		return mensagensErro;
	}
	
	public static StringBuffer validaNuloOuVazio(String campo, String mensagemErro, StringBuffer mensagensErro){
		if(null == campo || "".equals(campo)){
			mensagensErro = adicionaMensagem(mensagensErro, mensagemErro);
		}
		return mensagensErro;
	}
	
	public static StringBuffer adicionaMensagem(StringBuffer stringBuffer, String mensagem){
		
		if(null == stringBuffer){
			stringBuffer = new StringBuffer(mensagem);
		}else{
			stringBuffer.append("\n").append(mensagem);
		}
		
		return stringBuffer;
	}

}
