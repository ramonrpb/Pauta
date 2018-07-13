package br.com.sistemaist.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="aluno_aula")
public class AlunoAula implements Serializable{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorAlunoAula", strategy="increment")
	@GeneratedValue(generator="generatorAlunoAula")
	@Column(name="id", nullable=false)
	private Long id;
	@ManyToOne
	@JoinColumn(name="id_aluno", nullable=false)
	private Aluno aluno;
	@ManyToOne
	@JoinColumn(name="id_aula", nullable=false)
	private Aula aula;
	@Column(name="presenca", nullable=false)
	private boolean presenca;
	@Transient
	private Integer tempo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public Aula getAula() {
		return aula;
	}
	public void setAula(Aula aula) {
		this.aula = aula;
	}
	
	public boolean isPresenca() {
		return presenca;
	}
	public void setPresenca(boolean presenca) {
		this.presenca = presenca;
	}
	public Integer getTempo() {
		return tempo;
	}
	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}
	
}
