package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;

import br.com.sistemaist.dao.PeriodoDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.entidades.Periodo;

public class PeriodoCRUD extends CRUDGenerico<Periodo, Long> implements PeriodoDAO, Serializable{

	private static final long serialVersionUID = 1L;


}
