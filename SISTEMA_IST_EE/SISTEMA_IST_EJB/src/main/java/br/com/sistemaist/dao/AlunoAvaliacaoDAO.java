package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAvaliacao;

public interface AlunoAvaliacaoDAO extends DAOGenerico<AlunoAvaliacao, Long>{

	List<AlunoAvaliacao> buscarPorTurma(Long idTurma) throws ExcecaoGenerica;

	List<AlunoAvaliacao> carregarAvaliacoesPorAlunoAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica;
}
