package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.service.DisciplinaService;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.vo.CombosDisciplinaVO;
    
@ManagedBean(name="disciplinaMB")
@ViewScoped
public class DisciplinaManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Disciplina disciplina = new Disciplina();
	
	private List<Disciplina> listaDisciplina = new ArrayList<Disciplina>();
	
	private List<Periodo> listaPeriodo = new ArrayList<Periodo>();
	
	private Integer qtdDados = 10;
	
	private String filtroCodigo;
	
	private Long filtroPeriodo;
	
	@EJB
	private DisciplinaService disciplinaService;
	
	private CombosDisciplinaVO combosDisciplinaVO;
	
	@PostConstruct
	public void carregarTela() {

		disciplina = new Disciplina();
		disciplina.setPeriodo(new Periodo());

		try {
			
			combosDisciplinaVO = disciplinaService.buscarDadosIniciaisTela();
			setListaDisciplina(combosDisciplinaVO.getListaDisciplina());
			setListaPeriodo(combosDisciplinaVO.getPeriodos());
			
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
	}
	
	public void filtrarDisciplina() {
		setListaDisciplina(new ArrayList<Disciplina>());
		if(null != filtroCodigo || null != filtroPeriodo) {
			for(Disciplina d : combosDisciplinaVO.getListaDisciplina()) {
				Boolean adicionar= false;
				if(filtroCodigo != null) {
					if(d.getCodigo().contains(filtroCodigo)){
						adicionar = true;
					} else {
						continue;
					}
				} 
				if(filtroPeriodo != null) {
					if(d.getPeriodo().getId().equals(filtroPeriodo)) {
						adicionar = true;
					} else {
						continue;
					}
				}
				
				if(adicionar) {
					getListaDisciplina().add(d);
				}
			}
		} else {
			setListaDisciplina(combosDisciplinaVO.getListaDisciplina());
		}
	}

	public void novaDisciplina(){
		disciplina = new Disciplina();
		disciplina.setPeriodo(new Periodo());
	}
	
	
	/**
	 * Responsavel por salvar o disciplina
	 * @return
	 */
    public void salvarDisciplina(){
    	try {
    		disciplinaService.salvar(disciplina);
			setListaDisciplina(disciplinaService.buscarDisciplinas());
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
   		Util.montaMensagemFlashRedirect("Cadastro realizado com sucesso !","Sucesso!");
   		disciplina = new Disciplina();
		disciplina.setPeriodo(new Periodo());
    }
    
    /**
     * Metodo responsavel pela edicao da disciplina
     * @return
     */
	public void editarDisciplina(){
		try {
    		disciplinaService.alterar(disciplina);
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
    	Util.montaMensagemFlashRedirect("Alteração realizada com sucesso !","Sucesso!");
    	disciplina = new Disciplina();
		disciplina.setPeriodo(new Periodo());
    }

	/**
     * Metodo responsavel pela edicao da disciplina
     * @return
     */
	public void excluirDisciplina(){
		try {
    		disciplinaService.excluir(disciplina);
    		setListaDisciplina(disciplinaService.buscarDisciplinas());
		} catch (ExcecaoGenerica e) {
			log.error("Erro inesperado. "+e.getMessage());
	    	Util.montaMensagemFlashRedirectErro("Ocorreu um erro inesperado, tente mais tarde !","Erro!");
	    	return;
		} catch (EJBException e) {
			log.error("Disciplina já associada a outros itens."+e.getMessage());
	    	Util.montaMensagemFlashRedirectErro("Não é possível excluir a disciplina "+disciplina.getNome()+". Há históricos associados a ela!","Erro!");
	    	return;
		}
    	Util.montaMensagemFlashRedirect("Exclusão realizada com sucesso !","Sucesso!");
    }

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public List<Disciplina> getListaDisciplina() {
		return listaDisciplina;
	}

	public void setListaDisciplina(List<Disciplina> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}

	public List<Periodo> getListaPeriodo() {
		return listaPeriodo;
	}

	public void setListaPeriodo(List<Periodo> listaPeriodo) {
		this.listaPeriodo = listaPeriodo;
	}

	public Integer getQtdDados() {
		return qtdDados;
	}

	public void setQtdDados(Integer qtdDados) {
		this.qtdDados = qtdDados;
	}

	
	public String getFiltroCodigo() {
		return filtroCodigo;
	}

	public void setFiltroCodigo(String filtroCodigo) {
		this.filtroCodigo = filtroCodigo;
	}

	public Long getFiltroPeriodo() {
		return filtroPeriodo;
	}

	public void setFiltroPeriodo(Long filtroPeriodo) {
		this.filtroPeriodo = filtroPeriodo;
	}

	public CombosDisciplinaVO getCombosDisciplinaVO() {
		return combosDisciplinaVO;
	}

	public void setCombosDisciplinaVO(CombosDisciplinaVO combosDisciplinaVO) {
		this.combosDisciplinaVO = combosDisciplinaVO;
	}
	
}  