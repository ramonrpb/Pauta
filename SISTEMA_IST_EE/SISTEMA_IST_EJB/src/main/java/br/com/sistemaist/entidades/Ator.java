package br.com.sistemaist.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.sistemaist.enumerator.PerfilEnum;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="ator")
public class Ator implements Serializable{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorAtor", strategy="increment")
	@GeneratedValue(generator="generatorAtor")
	@Column(name="id", nullable=false)
	private Long id;
	@Column(name="email", nullable=false)
	private String email;
	@Column(name="senha", nullable=false)
	private String senha;
	@Column(name="nome", nullable=false)
	private String nome;
	@Column(name="perfil", nullable=false, length=2)
	@Enumerated(EnumType.STRING)
	private PerfilEnum perfil;
	@Column(name="codigo_alteracao_senha", nullable=false)
	private String codigo;
	@Column(name="data_validade_codigo", nullable=false)
	private Date dataValidadeCodigo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public PerfilEnum getPerfil() {
		return perfil;
	}
	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Date getDataValidadeCodigo() {
		return dataValidadeCodigo;
	}
	public void setDataValidadeCodigo(Date dataValidadeCodigo) {
		this.dataValidadeCodigo = dataValidadeCodigo;
	}
}
