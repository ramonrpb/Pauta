package br.com.sistemaist.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="aluno_avaliacao")
public class AlunoAvaliacao implements Serializable, Comparable<AlunoAvaliacao>{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorAlunoAvaliacao", strategy="increment")
	@GeneratedValue(generator="generatorAlunoAvaliacao")
	@Column(name="id", nullable=false)
	private Long id;
	@ManyToOne
	@JoinColumn(name="id_aluno", nullable=false)
	private Aluno aluno;
	@ManyToOne
	@JoinColumn(name="id_avaliacao", nullable=false)
	private Avaliacao avaliacao;
	@Column(name="nota", nullable=false)
	private Double nota;
	
	public AlunoAvaliacao() {
		super();
	}
	
	
	public AlunoAvaliacao(Aluno aluno, Avaliacao avaliacao, Double nota) {
		super();
		this.aluno = aluno;
		this.avaliacao = avaliacao;
		this.nota = nota;
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
	
	public Avaliacao getAvaliacao() {
		return avaliacao;
	}
	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}
	
	public Double getNota() {
		return nota;
	}
	public void setNota(Double nota) {
		this.nota = nota;
	}
	
	@Override
	public int compareTo(AlunoAvaliacao o) {
		if(id != null) {
			if(this.id.equals(o.getId())) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if(o.equals(this)) {
				return 0;
			} else {
				return 1;
			}
				
		}
		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
		result = prime * result
				+ ((avaliacao == null) ? 0 : avaliacao.hashCode());
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
		AlunoAvaliacao other = (AlunoAvaliacao) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		if (avaliacao == null) {
			if (other.avaliacao != null)
				return false;
		} else if (!avaliacao.equals(other.avaliacao))
			return false;
		return true;
	}
}
