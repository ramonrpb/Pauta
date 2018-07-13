package br.com.sistemaist.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.sistemaist.enumerator.PerfilEnum;

@Entity
@Table(name="professor")
@PrimaryKeyJoinColumn(name = "id")
public class Professor extends Ator implements Serializable{

	
	public Professor() {
		setPerfil(PerfilEnum.PR);
	}
	
	private static final long serialVersionUID = -1L;
	
	@Column(name="matricula", nullable=false)
	private String matricula;
	
	@Transient
	private String nomeEMatricula;
	
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNomeEMatricula() {
		if(matricula != null) {
			return matricula.toString().concat(" - ").concat(getNome());
		} else {
			return getNome();
		}
		
	}
	public void setNomeEMatricula(String nomeEMatricula) {
		this.nomeEMatricula = nomeEMatricula;
	}
	
}
