package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.entidades.Turno;
import br.com.sistemaist.vo.CombosTurmaVO;
import br.com.sistemaist.vo.PaginaVO;

@Local
public interface TurmaService {

	public CombosTurmaVO buscarDadosTurma() throws ExcecaoGenerica;

	public List<Turma> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica;

	public int contarTurmasFiltradas(AnoLetivo anoLetivo, Periodo periodo, Disciplina disciplina, Professor professor, Turno turno) throws ExcecaoGenerica;
	
	public int contarTurmasProfessor(Ator ator, AnoLetivo anoLetivo, Periodo periodo, Disciplina disciplina, Turno turno) throws ExcecaoGenerica;
	
	public List<Turma> listarTurmaPorIdProfessor(long idProfessor) throws ExcecaoGenerica;

	public void salvar(Turma turma) throws ExcecaoGenerica;

	Turma buscarTurmaPorId(Long idTurmaLong) throws ExcecaoGenerica;

	void excluir(Long idTurma) throws ExcecaoGenerica;

	void deletarAssociacaoAlunoTurma(List<AlunoTurma> alunoTurmaExcluido) throws ExcecaoGenerica;

	void gerarTurmasPorAnoLetivo(Long anoLetivoOrigem, Long anoLetivoDestino) throws ExcecaoGenerica;

	public List<Turma> listarTurmaPorIdProfessorEAnoLetivo(Long id,	Long idAnoLetivo) throws ExcecaoGenerica;
	public List<Turma> listarTurmaPorIdAnoLetivo(Long id) throws ExcecaoGenerica;


	public List<Turma> listarPorPaginaEAtor(PaginaVO paginaVO, Ator pegarAtor) throws ExcecaoGenerica;

	public Turma buscarTurmaPorIdComAvaliacao(Long idTurma) throws ExcecaoGenerica;

	public List<Turma> listarTurmaPorIdAlunoEAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica;
}
