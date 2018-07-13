package br.com.sistemaist.service;

import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Periodo;

@Local
public interface PeriodoService{

	public List<Periodo> buscarPeriodos() throws ExcecaoGenerica;

}
