package br.com.sistemaist.dao.implementacao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import br.com.sistemaist.dao.ProfessorDAO;
import br.com.sistemaist.daofabrica.crud.CRUDGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.vo.PaginaVO;

public class ProfessorCRUD extends CRUDGenerico<Professor, Long> implements ProfessorDAO, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public List<Professor> listarPorPagina(PaginaVO paginaVO) throws ExcecaoGenerica {
		List<Professor> lista = null;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("from Professor p ");
			if(null != paginaVO.getFiltro() && !"".equals(paginaVO.getFiltro())) {
				queryString.append(" where lower(p.nome) like lower(:filtro) or cast(p.matricula as string) like :filtro");
			} 
			queryString.append(" order by p.nome");
			
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
	public boolean validarAkMatricula(Long id, String matricula) throws ExcecaoGenerica {
		
		StringBuffer hql = new StringBuffer("select count(p.id) FROM Professor p where p.matricula = :matricula");
		
		if(null != id){
			hql.append(" and p.id != :id");
		}
		
		Query query = getEntityManager().createQuery(hql.toString());
		
		query.setParameter("matricula", matricula);
		if(null != id){
			query.setParameter("id", id);
		}
		
		return ( (Long) query.getSingleResult() == 0);
	}

	@Override
	public int contar(String filtro) throws ExcecaoGenerica {
		Long quantidade = 0l;
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select count (*) from Professor p ");
			if(null != filtro && !"".equals(filtro)) {
				queryString.append("where lower(p.nome) like lower(:filtro) or cast(p.matricula as string) like :filtro");
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
