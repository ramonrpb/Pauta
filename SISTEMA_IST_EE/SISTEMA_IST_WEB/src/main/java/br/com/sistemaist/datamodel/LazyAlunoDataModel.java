package br.com.sistemaist.datamodel;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.service.AlunoService;
import br.com.sistemaist.vo.PaginaVO;

public class LazyAlunoDataModel extends LazyDataModel<Aluno> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Aluno> alunos;
	
	private PaginaVO paginaVO;
	
	private AlunoService alunoService;
	
	private Context context;
	
	
	public LazyAlunoDataModel(PaginaVO paginaVO){
		this.paginaVO = paginaVO;
		
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			alunoService = (AlunoService) context.lookup("java:app/SISTEMA_IST_EJB/AlunoBean");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Aluno> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		
		paginaVO.setPosicao(first);
		paginaVO.setQuantidade(pageSize);
		
		try {
			alunos = alunoService.listarPorPagina(paginaVO);
			setRowCount(alunoService.contarAlunos(paginaVO.getFiltro()));
		} catch (ExcecaoGenerica e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alunos;
	}  
	
	
	public PaginaVO getPaginaVO() {
		return paginaVO;
	}

	public void setPaginaVO(PaginaVO paginaVO) {
		this.paginaVO = paginaVO;
	}


}
