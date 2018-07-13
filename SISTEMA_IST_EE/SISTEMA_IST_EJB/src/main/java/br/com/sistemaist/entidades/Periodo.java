package br.com.sistemaist.entidades;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="periodo")
public class Periodo implements Serializable{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorPeriodo", strategy="increment")
	@GeneratedValue(generator="generatorPeriodo")
	@Column(name="id", nullable=false)
	private Long id;
	@Column(name="nome", nullable=false)
	private String nome;
	@Transient
	private Calendar dataInicio;
	@Transient
	private Calendar dataFim;
	
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
	public Calendar getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Calendar getDataFim() {
		return dataFim;
	}
	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
	}
	
}
