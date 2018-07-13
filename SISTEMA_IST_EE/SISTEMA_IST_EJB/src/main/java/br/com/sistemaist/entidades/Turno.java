package br.com.sistemaist.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="turno")
public class Turno implements Serializable{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorTurno", strategy="increment")
	@GeneratedValue(generator="generatorTurno")
	@Column(name="id", nullable=false)
	private Long id;
	@Column(name="nome")
	private String nome;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
