package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.vo.PaginaVO;

@Local
public interface AlunoService{

	public Aluno carregarAlunoPorId(Long id) throws ExcecaoGenerica;

	public void excluirAlunoPorId(Long id) throws ExcecaoGenerica;

	public List<Aluno> carregarAlunosCadastrados() throws ExcecaoGenerica;

	public Aluno cadastrarAluno(Aluno aluno) throws ExcecaoGenerica;

	public List<Aluno> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica;

	public int contarAlunos() throws ExcecaoGenerica;

	public List<Aluno> listar() throws ExcecaoGenerica;

	boolean validarAkMatricula(Long id, String valor) throws ExcecaoGenerica;

	public int contarAlunos(String filtro) throws ExcecaoGenerica;
	
	
}
