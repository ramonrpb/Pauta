package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;

import br.com.sistemaist.dao.TurnoDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.entidades.Turno;

public class TurnoCRUD extends CRUDGenerico<Turno, Long> implements TurnoDAO, Serializable{

	private static final long serialVersionUID = 1L;

}
