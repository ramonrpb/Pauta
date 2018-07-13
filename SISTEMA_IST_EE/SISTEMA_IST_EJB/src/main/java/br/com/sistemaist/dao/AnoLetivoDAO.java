package br.com.sistemaist.dao;

import java.util.Date;
import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;

public interface AnoLetivoDAO extends DAOGenerico<AnoLetivo, Long>{

	boolean validarAkAnoLetivo(Integer ano, Integer semestre, Long id) throws ExcecaoGenerica;
	List<AnoLetivo> buscarAnosLetivosAbertos(Date date) throws ExcecaoGenerica;
	List<AnoLetivo> buscarAnosLetivosAbertosAteHoje(Date date) throws ExcecaoGenerica;

}
