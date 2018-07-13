package br.com.sistemaist.vo;

import java.io.Serializable;
import java.util.List;

import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;

public class CombosDisciplinaVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Disciplina> listaDisciplina;
	private List<Periodo> periodos;
	
	public List<Disciplina> getListaDisciplina() {
		return listaDisciplina;
	}
	public void setListaDisciplina(List<Disciplina> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}
	public List<Periodo> getPeriodos() {
		return periodos;
	}
	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
	}

	
	
}
