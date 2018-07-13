package br.com.sistemaist.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.entidades.Turno;

public class CombosTurmaVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<AnoLetivo> listaAnoLetivo;
	private List<Aluno> listaAluno;
	private List<Disciplina> listaDisciplina;
	private List<Periodo> listaPeriodo;
	private List<Professor> listaProfessor;
	private List<Turno> listaTurno;
	private List<Turma> listaTurma;
	
	public List<Aluno> getListaAluno() {
		return listaAluno;
	}
	public void setListaAluno(List<Aluno> listaAluno) {
		this.listaAluno = listaAluno;
	}
	
	public List<Disciplina> getListaDisciplina() {
		return listaDisciplina;
	}
	public void setListaDisciplina(List<Disciplina> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}

	public List<Periodo> getListaPeriodo() {
		return listaPeriodo;
	}
	public void setListaPeriodo(List<Periodo> listaPeriodo) {
		this.listaPeriodo = listaPeriodo;
	}

	public List<Professor> getListaProfessor() {
		return listaProfessor;
	}
	public void setListaProfessor(List<Professor> listaProfessor) {
		this.listaProfessor = listaProfessor;
	}

	public List<Turno> getListaTurno() {
		return listaTurno;
	}
	public void setListaTurno(List<Turno> listaTurno) {
		this.listaTurno = listaTurno;
	}

	public List<Turma> getListaTurma() {
		return listaTurma;
	}
	public void setListaTurma(List<Turma> listaTurma) {
		this.listaTurma = listaTurma;
	}
	public List<AnoLetivo> getListaAnoLetivo() {
		return listaAnoLetivo;
	}
	public void setListaAnoLetivo(List<AnoLetivo> listaAnoLetivo) {
		this.listaAnoLetivo = listaAnoLetivo;
	}
}
