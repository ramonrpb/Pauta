package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;

@Local
public interface AnoLetivoService {

	public void salvar(AnoLetivo anoLetivo) throws ExcecaoGenerica;
	public void alterar(AnoLetivo anoLetivo) throws ExcecaoGenerica;
	public void excluir(AnoLetivo anoLetivo) throws ExcecaoGenerica;
	public boolean validarAkAnoLetivo(Integer ano, Integer semestre, Long id) throws ExcecaoGenerica;
	public AnoLetivo buscarAnoLetivoPorId(Long id) throws ExcecaoGenerica;
	public List<AnoLetivo> buscarAnosLetivos() throws ExcecaoGenerica;
	public List<AnoLetivo> buscarAnosLetivosAbertosAteHoje() throws ExcecaoGenerica;
	public List<AnoLetivo> buscarAnosLetivosAbertos() throws ExcecaoGenerica;

}
