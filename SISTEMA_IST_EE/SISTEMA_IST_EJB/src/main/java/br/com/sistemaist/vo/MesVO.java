package br.com.sistemaist.vo;

import java.io.Serializable;

public class MesVO implements Serializable{

	private static final long serialVersionUID = -7514559157019912741L;

	private Integer id;
	
	private String nome;

	public MesVO(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	public MesVO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
