package br.com.sistemaist.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.Aula;

public class FrequenciaVO implements Serializable{

	private static final long serialVersionUID = 2903927461186373243L;

	private List<AlunoTurma> listaAlunoTurma = new ArrayList<AlunoTurma>();
	
	private Aula aula;

	public List<AlunoTurma> getListaAlunoTurma() {
		return listaAlunoTurma;
	}
	public void setListaAlunoTurma(List<AlunoTurma> listaAlunoTurma) {
		this.listaAlunoTurma = listaAlunoTurma;
	}

	public Aula getAula() {
		return aula;
	}
	public void setAula(Aula aula) {
		this.aula = aula;
	}
	
}
