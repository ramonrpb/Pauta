package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.sistemaist.dao.DisciplinaDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Disciplina;

public class DisciplinaCRUD extends CRUDGenerico<Disciplina, Long> implements DisciplinaDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public boolean validarAkDisciplina(String codigo) throws ExcecaoGenerica {
		
		StringBuffer hql = new StringBuffer("select count(d.id) FROM Disciplina d where d.codigo = :codigo");
		
		Query query = ((Session)getEntityManager().getDelegate()).createQuery(hql.toString());
		query.setString("codigo", codigo);
		
		return ( (Long) query.uniqueResult() == 0);
	}

}
