package br.com.sistemaist.entidades;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="turma")
public class Turma implements Serializable{

	public Turma() {
		super();
	}
	public Turma(Professor professor, Disciplina disciplina, Turno turno,
			AnoLetivo anoLetivo, String nome, String horario) {
		super();
		this.professor = professor;
		this.disciplina = disciplina;
		this.turno = turno;
		this.anoLetivo = anoLetivo;
		this.nome = nome;
		this.horario = horario;
	}
	private static final long serialVersionUID = -1L;
	
	@Id
	@GenericGenerator(name="generatorTurma", strategy="increment")
	@GeneratedValue(generator="generatorTurma")
	@Column(name="id", nullable=false)
	private Long id;
	@ManyToOne
	@JoinColumn(name="id_professor", referencedColumnName="id")
	private Professor professor;
	@ManyToOne
	@JoinColumn(name="id_disciplina", referencedColumnName="id")
	private Disciplina disciplina;
	@ManyToOne
	@JoinColumn(name="id_turno", referencedColumnName="id")
	private Turno turno;
	
	@ManyToOne
	@JoinColumn(name="id_ano_letivo", referencedColumnName="id")
	private AnoLetivo anoLetivo;
	
	@OneToMany(mappedBy="turma", cascade={CascadeType.ALL})
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<AlunoTurma> alunoTurma = new ArrayList<AlunoTurma>();
	
	@OneToMany(mappedBy="turma")
	private List<Avaliacao> listaAvaliacao;
	
	public List<Avaliacao> getListaAvaliacao() {
		return listaAvaliacao;
	}
	public void setListaAvaliacao(List<Avaliacao> listaAvaliacao) {
		this.listaAvaliacao = listaAvaliacao;
	}
	@Column(name="nome")
	private String nome;
	
	@Column(name="horario")
	private String horario;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	public Disciplina getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
	
	public Turno getTurno() {
		return turno;
	}
	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	
	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}
	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public List<AlunoTurma> getAlunoTurma() {
		Collections.sort(alunoTurma, new Comparator<AlunoTurma>() {
			Collator cot = Collator.getInstance(new Locale("pt", "BR"));
			@Override
			public int compare(AlunoTurma o1, AlunoTurma o2) {
				return cot.compare(o1.getAluno().getNome(), o2.getAluno().getNome());
			}
		});
		return alunoTurma;
	}
	public void setAlunoTurma(List<AlunoTurma> alunoTurma) {
		this.alunoTurma.clear();
	    this.alunoTurma.addAll(alunoTurma);
	    
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alunoTurma == null) ? 0 : alunoTurma.hashCode());
		result = prime * result
				+ ((anoLetivo == null) ? 0 : anoLetivo.hashCode());
		result = prime * result
				+ ((disciplina == null) ? 0 : disciplina.hashCode());
		result = prime * result + ((horario == null) ? 0 : horario.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((professor == null) ? 0 : professor.hashCode());
		result = prime * result + ((turno == null) ? 0 : turno.hashCode());
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
		Turma other = (Turma) obj;
		if (alunoTurma == null) {
			if (other.alunoTurma != null)
				return false;
		} else if (!alunoTurma.equals(other.alunoTurma))
			return false;
		if (anoLetivo == null) {
			if (other.anoLetivo != null)
				return false;
		} else if (!anoLetivo.equals(other.anoLetivo))
			return false;
		if (disciplina == null) {
			if (other.disciplina != null)
				return false;
		} else if (!disciplina.equals(other.disciplina))
			return false;
		if (horario == null) {
			if (other.horario != null)
				return false;
		} else if (!horario.equals(other.horario))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (professor == null) {
			if (other.professor != null)
				return false;
		} else if (!professor.equals(other.professor))
			return false;
		if (turno == null) {
			if (other.turno != null)
				return false;
		} else if (!turno.equals(other.turno))
			return false;
		return true;
	}
	
	
}
