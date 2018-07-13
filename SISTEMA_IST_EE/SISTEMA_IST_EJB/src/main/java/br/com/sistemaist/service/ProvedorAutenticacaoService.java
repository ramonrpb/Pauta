package br.com.sistemaist.service;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Ator;

@Local
public interface ProvedorAutenticacaoService{
	
	public Ator buscarAtorPorLoginESenha(String login, String senha) throws ExcecaoGenerica;

}
