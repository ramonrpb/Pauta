package br.com.sistemaist.service;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAvaliacao;

@Local
public interface AlunoAvaliacaoService {

	List<AlunoAvaliacao> carregarAlunoAvaliacaoPorTurma(Long idTurma) throws ExcecaoGenerica;

	void salvarListaAlunoAvaliacao(List<AlunoAvaliacao> listaAlunoAvaliacao) throws ExcecaoGenerica;

	public HashMap<Long, List<AlunoAvaliacao>> carregarAvaliacoesPorAlunoAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica;

	void removerAvaliacoes(List<AlunoAvaliacao> listaAlunoAvaliacaoRemover) throws ExcecaoGenerica;

}
