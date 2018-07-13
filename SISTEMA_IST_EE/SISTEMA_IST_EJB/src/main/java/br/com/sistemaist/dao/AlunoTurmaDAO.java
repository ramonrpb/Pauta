package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.entidades.AlunoTurma;

public interface AlunoTurmaDAO extends DAOGenerico<AlunoTurma, Long>{

	public List<AlunoTurma> buscarListaPorIdTurma(long idTurma);

	public List<AlunoTurma> listarTurmaPorIdAlunoEAnoLetivo(Long idAluno,
			Long idAnoLetivo);

}
