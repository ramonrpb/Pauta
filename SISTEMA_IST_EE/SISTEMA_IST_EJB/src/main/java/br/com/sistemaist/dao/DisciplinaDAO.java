package br.com.sistemaist.dao;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Disciplina;

public interface DisciplinaDAO extends DAOGenerico<Disciplina, Long>{

	boolean validarAkDisciplina(String valor) throws ExcecaoGenerica;

}
