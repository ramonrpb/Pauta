package br.com.sistemaist.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="aluno")
@PrimaryKeyJoinColumn(name = "id")
public class Aluno extends Ator implements Serializable, Comparable<Aluno>{

	private static final long serialVersionUID = -1L;
	
	@Column(name="matricula", nullable=false)
	private String matricula;
	
	@Transient
	private String nomeEMatricula;
	
	@Transient
	private Integer qtdPresenca;
	
	@Transient
	private Double totalFrequencia;
	
	@Transient
	private Integer totalFalta;
	
	@Transient
	private Integer totalPresenca;
	
	@Transient
	private String media;
	
	public Integer getTotalPresenca() {
		return totalPresenca;
	}
	public void setTotalPresenca(Integer totalPresenca) {
		this.totalPresenca = totalPresenca;
	}
	public Integer getTotalFalta() {
		return totalFalta;
	}
	public void setTotalFalta(Integer totalFalta) {
		this.totalFalta = totalFalta;
	}
	public Double getTotalFrequencia() {
		return totalFrequencia;
	}
	public void setTotalFrequencia(Double totalFrequencia) {
		this.totalFrequencia = totalFrequencia;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNomeEMatricula() {
		if(matricula != null && getNome() != null){
			return new StringBuilder().append(matricula).append(" - ").append(getNome()).toString();
		} else {
			return null;
		}
	}
	public void setNomeEMatricula(String nomeEMatricula) {
		this.nomeEMatricula = nomeEMatricula;
	}
	
	public Integer getQtdPresenca() {
		return qtdPresenca;
	}
	public void setQtdPresenca(Integer qtdPresenca) {
		this.qtdPresenca = qtdPresenca;
	}
	
	
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public void adicionarRemoverPresenca(Boolean presenca) {
		if(presenca){
			this.qtdPresenca += 1;
		}else{
			this.qtdPresenca += 1;
		}
	}
	
	@Override
	public int compareTo(Aluno o) {
		if(this.matricula.equals(o.getMatricula())) {
			return 0;
		} else {
			return 1;
		}
		
	}
	
}
