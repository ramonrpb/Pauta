package br.com.sistemaist.vo;

import java.io.Serializable;

import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;

public class FiltroVO implements Serializable{

	private static final long serialVersionUID = -8386661503353533217L;
	
	private AnoLetivo anoLetivo;
	private Periodo periodo;
	private Disciplina disciplina;
	private Professor professor;
	
	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}
	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
}
