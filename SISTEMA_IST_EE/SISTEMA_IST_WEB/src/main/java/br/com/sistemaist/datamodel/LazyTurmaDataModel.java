package br.com.sistemaist.datamodel;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.vo.PaginaVO;

public class LazyTurmaDataModel extends LazyDataModel<Turma> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Turma> turmas;
	
	private PaginaVO paginaVO;
	
	private TurmaService turmaService;
	
	private Context context;
	
	public LazyTurmaDataModel(PaginaVO paginaVO){
		this.paginaVO = paginaVO;
		
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			turmaService = (TurmaService) context.lookup("java:app/SISTEMA_IST_EJB/TurmaBean");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Turma> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		paginaVO.setPosicao(first);
		paginaVO.setQuantidade(pageSize);
		
		try {
			if(Util.pegarAtor().getPerfil().equals(PerfilEnum.AD)){
				turmas = turmaService.listarPorPagina(paginaVO);
			}else{
				turmas = turmaService.listarPorPaginaEAtor(paginaVO, Util.pegarAtor());
			}
		} catch (ExcecaoGenerica e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return turmas;
	}  
	
	
	public PaginaVO getPaginaVO() {
		return paginaVO;
	}

	public void setPaginaVO(PaginaVO paginaVO) {
		this.paginaVO = paginaVO;
	}
}
