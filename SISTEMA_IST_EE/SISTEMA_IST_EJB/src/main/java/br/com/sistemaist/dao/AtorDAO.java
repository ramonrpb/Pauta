package br.com.sistemaist.dao;

import java.util.Calendar;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Ator;

public interface AtorDAO extends DAOGenerico<Ator, Long>{

	public Ator buscarAtorPorLoginESenha(String login, String senha);

	public String buscarEmailPorLogin(String login) throws ExcecaoGenerica;
	
	public boolean verificarExistenciaLogin(String login, Long id) throws ExcecaoGenerica;

	public boolean validarAtorPorLoginECodigo(String login, String codigo) throws ExcecaoGenerica;

	public boolean validarAtorPorLoginESenha(String login, String senhaAtual) throws ExcecaoGenerica;

	public Ator buscarAtorPorLogin(String login) throws ExcecaoGenerica;

	void salvarCodigoEDataValidadeCodigo(String login, String codigo, Calendar dataExpiracaoSenha) throws ExcecaoGenerica;

//	public void alterarSenhaPorLogin(String login, String senha) throws ExcecaoGenerica;

}
