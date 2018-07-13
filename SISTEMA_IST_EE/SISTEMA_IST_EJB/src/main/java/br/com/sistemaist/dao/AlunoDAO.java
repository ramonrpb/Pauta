package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.vo.PaginaVO;

public interface AlunoDAO extends DAOGenerico<Aluno, Long>{

	public List<Aluno> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica;

	public int contarAlunos() throws ExcecaoGenerica;

	boolean validarAkMatricula(Long id, String matricula) throws ExcecaoGenerica;

	public int contarAlunos(String filtro) throws ExcecaoGenerica;

}
