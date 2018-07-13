package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import br.com.sistemaist.dao.AlunoDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.vo.PaginaVO;

public class AlunoCRUD extends CRUDGenerico<Aluno, Long> implements AlunoDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<Aluno> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica {
		List<Aluno> lista = null;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("from Aluno a ");
			if(null != paginaVO.getFiltro() && !"".equals(paginaVO.getFiltro())) {
				queryString.append(" where a.nome like :filtro or cast(a.matricula as string) like :filtro");
			} 
			queryString.append(" order by a.nome");
			Query query = getEntityManager().createQuery(queryString.toString());
			if(null != paginaVO.getFiltro() && !"".equals(paginaVO.getFiltro())) {
				query.setParameter("filtro", "%".concat(paginaVO.getFiltro()).concat("%"));
			}
			
			query.setFirstResult(paginaVO.getPosicao());
			query.setMaxResults(paginaVO.getQuantidade());
			
			lista = query.getResultList();
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		} 
		
		return lista;
	}

	@Override
	public int contarAlunos() throws ExcecaoGenerica {
		Long quantidade = 0l;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select count (*) from Aluno ");
			
			Query query = getEntityManager().createQuery(queryString.toString());
			
			quantidade = (Long) query.getSingleResult();
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		return quantidade.intValue();
	}
	
	@Override
	public boolean validarAkMatricula(Long id, String matricula) throws ExcecaoGenerica {
		
		StringBuffer hql = new StringBuffer("select count(a.id) FROM Aluno a where a.matricula = :matricula");
		
		if(null != id){
			hql.append(" and a.id != :id");
		}
		
		Query query = getEntityManager().createQuery(hql.toString());
		
		query.setParameter("matricula", matricula);
		if(null != id){
			query.setParameter("id", id);
		}
		
		return ( (Long) query.getSingleResult() == 0);
	}

	@Override
	public int contarAlunos(String filtro) throws ExcecaoGenerica {
		Long quantidade = 0l;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select count (*) from Aluno a ");
			if(null != filtro && !"".equals(filtro)) {
				queryString.append("where a.nome like :filtro or cast(a.matricula as string) like :filtro");
			}
			Query query = getEntityManager().createQuery(queryString.toString());
			if(null != filtro && !"".equals(filtro)) {
				query.setParameter("filtro", "%".concat(filtro).concat("%"));
			}
			quantidade = (Long) query.getSingleResult();
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ExcecaoGenerica(e);
		}
		return quantidade.intValue();
	}

}
