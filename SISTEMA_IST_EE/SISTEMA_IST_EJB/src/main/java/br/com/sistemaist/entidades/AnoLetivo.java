package br.com.sistemaist.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ano_letivo")
public class AnoLetivo implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="generatorAnoLetivo", strategy="increment")
	@GeneratedValue(generator="generatorAnoLetivo")
	@Column(name="id")
	private Long id;
	@Column(name="ano")
	private Integer ano; 
	@Column(name="semestre")
	private Integer semestre;
	@Column(name="data_inicio")
	private Date dataInicio;
	@Column(name="data_fim")
	private Date dataFim;	
	
	@Transient
	private String nomeFormatado;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getSemestre() {
		return semestre;
	}
	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}

	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getNomeFormatado() {
		return new StringBuilder().append(ano).append("/").append(semestre).toString();
	}
	public void setNomeFormatado(String nomeFormatado) {
		this.nomeFormatado = nomeFormatado;
	}
}
