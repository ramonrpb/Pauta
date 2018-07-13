package br.com.sistemaist.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="aula")
public class Aula implements Serializable{

	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorAula", strategy="increment")
	@GeneratedValue(generator="generatorAula")
	@Column(name="id", nullable=false)
	private Long id;
	@Column(name="dia", nullable=false)
	private Date data;
	@Column(name="quantidade", nullable=false)
	private Integer quantidade = 1;
	@ManyToOne
	@JoinColumn(name="id_turma", nullable=false)
	private Turma turma;
	
	@OneToMany(mappedBy="aula", cascade={CascadeType.ALL})
    @org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<AlunoAula> listaAlunoAula;
	
	@Transient
	private String dataFormatada;
	
	@Transient
	private Long totalAulas;
	
	public Long getTotalAulas() {
		return totalAulas;
	}
	public void setTotalAulas(Long totalAulas) {
		this.totalAulas = totalAulas;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<AlunoAula> getListaAlunoAula() {
		return listaAlunoAula;
	}
	public void setListaAlunoAula(List<AlunoAula> listaAlunoAula) {
		this.listaAlunoAula = listaAlunoAula;
	}
	public Turma getTurma() {
		return turma;
	}
	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	public String getDataFormatada() {
		return dataFormatada;
	}
	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}
	
}
