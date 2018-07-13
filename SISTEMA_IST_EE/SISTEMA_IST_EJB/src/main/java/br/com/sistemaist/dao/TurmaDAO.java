package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.entidades.Turno;
import br.com.sistemaist.vo.PaginaVO;

public interface TurmaDAO extends DAOGenerico<Turma, Long>{

	public List<Turma> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica;

	public List<Turma> listarTurmaPorIdProfessor(long idProfessor);

	public List<Turma> buscarTurmasPorAnoLetivo(Long anoLetivoOrigem) throws ExcecaoGenerica;

	public List<Turma> listarTurmaPorIdProfessorEAnoLetivo(Long idProfessor, Long idAnoLetivo);

	public List<Turma> listarTurmaPorIdAnoLetivo(Long idAnoLetivo);

	public List<Turma> listarPorPaginaEAtor(PaginaVO paginaVO, Ator ator) throws ExcecaoGenerica;

	public int contarTurmasProfessor(Ator ator, AnoLetivo anoLetivo, Periodo periodo, Disciplina disciplina, Turno turno) throws ExcecaoGenerica;

	public Turma buscarTurmaPorIdComAvaliacao(Long idTurma) throws ExcecaoGenerica;

	public int contarTurmas(AnoLetivo anoLetivo, Periodo periodo, Disciplina disciplina, Professor ator, Turno turno) throws ExcecaoGenerica;

//	public List<Turma> listarTurmaPorIdAlunoEAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica;

}
