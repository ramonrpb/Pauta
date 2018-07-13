package br.com.sistemaist.dao;

import java.util.Date;
import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.entidades.Aula;

public interface AulaDAO extends DAOGenerico<Aula, Long>{

	List<Aula> buscarAulasPorIdTurma(Long id);
	boolean validarTurmaData(Long id, Long idTurma, Date data);

}
