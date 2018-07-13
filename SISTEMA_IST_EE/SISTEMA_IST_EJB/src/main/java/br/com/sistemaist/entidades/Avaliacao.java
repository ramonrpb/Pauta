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
@Table(name="avaliacao")
public class Avaliacao implements Serializable, Comparable<Avaliacao>{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorAvaliacao", strategy="increment")
	@GeneratedValue(generator="generatorAvaliacao")
	@Column(name="id", nullable=false)
	private Long id;
	@Column(name="nome", nullable=false)
	private String nome;
	@Column(name="peso", nullable=false)
	private Integer peso;
	@ManyToOne
	@JoinColumn(name="id_turma", nullable=false)
	private Turma turma;
	
	public Avaliacao() {
		super();
	}
	
	public Avaliacao(String nome, Integer peso, Turma turma) {
		super();
		this.nome = nome;
		this.peso = peso;
		this.turma = turma;
	}
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

	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public Turma getTurma() {
		return turma;
	}
	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	@Override
	public int compareTo(Avaliacao o) {
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
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Avaliacao other = (Avaliacao) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (turma == null) {
			if (other.turma != null)
				return false;
		} else if (!turma.equals(other.turma))
			return false;
		return true;
	}
	
	
}
