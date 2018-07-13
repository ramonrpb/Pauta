package br.com.sistemaist.util;

import java.io.Serializable;

import br.com.sistemaist.entidades.Aluno;

public class EnvioEmailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Aluno aluno;
	private Boolean enviarEmail;
	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	public Boolean getEnviarEmail() {
		return enviarEmail;
	}
	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}
	
}
