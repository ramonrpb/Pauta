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
@Table(name="aluno_turma")
public class AlunoTurma implements Serializable{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorAlunoTurma", strategy="increment")
	@GeneratedValue(generator="generatorAlunoTurma")
	@Column(name="id", nullable=false)
	private Long id;
	@ManyToOne
	@JoinColumn(name="id_aluno", nullable=false)
	private Aluno aluno;
	@ManyToOne
	@JoinColumn(name="id_turma", nullable=false)
	private Turma turma;
	@Transient
	private Double totalFrequencia;
	@Transient
	private Integer totalFalta;
	@Transient
	private Integer totalPresenca;
	
	public AlunoTurma() {
		super();
	}
	
	public AlunoTurma(Aluno aluno, Turma turma) {
		super();
		this.aluno = aluno;
		this.turma = turma;
	}
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

	public Turma getTurma() {
		return turma;
	}
	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Double getTotalFrequencia() {
		return totalFrequencia;
	}

	public void setTotalFrequencia(Double totalFrequencia) {
		this.totalFrequencia = totalFrequencia;
	}

	public Integer getTotalFalta() {
		return totalFalta;
	}

	public void setTotalFalta(Integer totalFalta) {
		this.totalFalta = totalFalta;
	}

	public Integer getTotalPresenca() {
		return totalPresenca;
	}

	public void setTotalPresenca(Integer totalPresenca) {
		this.totalPresenca = totalPresenca;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
		result = prime * result + ((turma == null) ? 0 : turma.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlunoTurma other = (AlunoTurma) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		if (turma == null) {
			if (other.turma != null)
				return false;
		} else if (!turma.equals(other.turma))
			return false;
		return true;
	}

}
