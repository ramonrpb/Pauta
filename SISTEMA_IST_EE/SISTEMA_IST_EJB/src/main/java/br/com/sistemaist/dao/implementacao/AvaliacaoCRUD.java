package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import br.com.sistemaist.dao.AvaliacaoDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Avaliacao;
import br.com.sistemaist.util.Constante;

public class AvaliacaoCRUD extends CRUDGenerico<Avaliacao, Long> implements AvaliacaoDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<Avaliacao> carregarAvaliacoesPorTurma(Long idTurma) throws ExcecaoGenerica {
		try {
			Criteria criteria = criarCriteria();
			criteria.createAlias("turma", "turma");
			criteria.add(Restrictions.eq("turma.id", idTurma));
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
	}
	
	@Override
	public List<Avaliacao> carregarAvaliacoesPorTurmaSemPF(Long idTurma) throws ExcecaoGenerica {
		try {
			Criteria criteria = criarCriteria();
			criteria.createAlias("turma", "turma");
			criteria.add(Restrictions.eq("turma.id", idTurma));
			criteria.add(Restrictions.ne("nome", Constante.NOME_PROVA_FINAL));
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
	}

	@Override
	public void excluirAvaliacoes(List<Long> idAvaliacoesExcluidas) throws ExcecaoGenerica {
		try {
			StringBuilder hql = new StringBuilder("delete from Avaliacao where id in (:idAvaliacoesExcluidas)");
			Query query = criarQuery(hql.toString());
			query.setParameterList("idAvaliacoesExcluidas", idAvaliacoesExcluidas);
			Integer retorno = query.executeUpdate();
			logger.info(retorno.toString());
			query.getNamedParameters();
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
		
	}

}