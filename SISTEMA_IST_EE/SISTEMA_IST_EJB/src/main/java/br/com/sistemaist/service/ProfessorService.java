package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.vo.PaginaVO;

@Local
public interface ProfessorService {
	
	Professor cadastrarProfessor(Professor professor) throws ExcecaoGenerica;

	List<Professor> carregarProfessoresCadastrados() throws ExcecaoGenerica;

	Professor carregarProfessorPorId(Long id) throws ExcecaoGenerica;

	void excluirProfessorPorId(Long id) throws ExcecaoGenerica;

	List<Professor> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica;

	int contarProfessores(String string) throws ExcecaoGenerica;

	boolean validarAkMatricula(Long codigo, String valor) throws ExcecaoGenerica;

}
