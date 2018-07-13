package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Avaliacao;

@Local
public interface AvaliacaoService {

	public void salvar(List<Avaliacao> listaAvaliacao) throws ExcecaoGenerica;

	public void excluirAvaliacoes(List<Long> idAvaliacoesExcluidas) throws ExcecaoGenerica;

	Avaliacao salvar(Avaliacao a) throws ExcecaoGenerica;

	List<Avaliacao> carregarAvaliacoesPorTurmaSemPF(Long idTurma)
			throws ExcecaoGenerica;
}
