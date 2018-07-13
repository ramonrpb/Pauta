package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.vo.PaginaVO;

public interface ProfessorDAO extends DAOGenerico<Professor, Long>{

	List<Professor> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica;

	boolean validarAkMatricula(Long id, String matricula) throws ExcecaoGenerica;

	int contar(String filtro) throws ExcecaoGenerica;

}
