package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.vo.CombosDisciplinaVO;

@Local
public interface DisciplinaService{

	public void salvar(Disciplina disciplina) throws ExcecaoGenerica;
	public void alterar(Disciplina disciplina) throws ExcecaoGenerica;
	public List<Disciplina> buscarDisciplinas() throws ExcecaoGenerica;
	public void excluir(Disciplina disciplina) throws ExcecaoGenerica;
	public boolean validarAkDisciplina(String valor) throws ExcecaoGenerica;
	
	public CombosDisciplinaVO buscarDadosIniciaisTela() throws ExcecaoGenerica;

}
