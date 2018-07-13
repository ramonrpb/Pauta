package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Avaliacao;

public interface AvaliacaoDAO extends DAOGenerico<Avaliacao, Long>{

	List<Avaliacao> carregarAvaliacoesPorTurma(Long idTurma) throws ExcecaoGenerica;

	void excluirAvaliacoes(List<Long> idAvaliacoesExcluidas) throws ExcecaoGenerica;

	List<Avaliacao> carregarAvaliacoesPorTurmaSemPF(Long idTurma)
			throws ExcecaoGenerica;

}
