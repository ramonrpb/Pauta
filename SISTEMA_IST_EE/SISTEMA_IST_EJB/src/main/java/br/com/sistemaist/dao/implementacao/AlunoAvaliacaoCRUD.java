package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.sistemaist.dao.AlunoAvaliacaoDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAvaliacao;

public class AlunoAvaliacaoCRUD extends CRUDGenerico<AlunoAvaliacao, Long> implements AlunoAvaliacaoDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<AlunoAvaliacao> buscarPorTurma(Long idTurma) throws ExcecaoGenerica {
		try {
			Criteria criteria = criarCriteria();
			criteria.createAlias("aluno", "aluno");
			criteria.createAlias("avaliacao", "avaliacao");
			criteria.createAlias("avaliacao.turma", "avaliacao.turma");
			criteria.add(Restrictions.eq("avaliacao.turma.id", idTurma));
			criteria.addOrder(Order.asc("aluno.nome"));
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
	}

	@Override
	public List<AlunoAvaliacao> carregarAvaliacoesPorAlunoAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica {
		try {
			Criteria criteria = criarCriteria();
			criteria.createAlias("aluno", "aluno");
			criteria.add(Restrictions.eq("aluno.id", idAluno));
			criteria.createAlias("avaliacao", "avaliacao");
			criteria.createAlias("avaliacao.turma", "turma");
			criteria.createAlias("turma.anoLetivo", "anoLetivo");
			criteria.add(Restrictions.eq("anoLetivo.id", idAnoLetivo));
			criteria.addOrder(Order.asc("aluno.nome"));
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			throw new ExcecaoGenerica(e);
		}
	}
}
