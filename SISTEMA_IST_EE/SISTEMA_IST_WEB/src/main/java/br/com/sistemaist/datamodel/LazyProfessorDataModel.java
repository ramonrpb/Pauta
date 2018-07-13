package br.com.sistemaist.datamodel;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.service.ProfessorService;
import br.com.sistemaist.vo.PaginaVO;

public class LazyProfessorDataModel extends LazyDataModel<Professor> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Professor> professores;
	
	private PaginaVO paginaVO;
	
	private ProfessorService professorService;
	
	private Context context;
	
	public LazyProfessorDataModel(PaginaVO paginaVO){
		this.paginaVO = paginaVO;
		
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			professorService = (ProfessorService) context.lookup("java:app/SISTEMA_IST_EJB/ProfessorBean");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Professor> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		paginaVO.setPosicao(first);
		paginaVO.setQuantidade(pageSize);
		
		try {
			professores = professorService.listarPorPagina(paginaVO);
		} catch (ExcecaoGenerica e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return professores;
	}  
	
	
	public PaginaVO getPaginaVO() {
		return paginaVO;
	}

	public void setPaginaVO(PaginaVO paginaVO) {
		this.paginaVO = paginaVO;
	}
}
