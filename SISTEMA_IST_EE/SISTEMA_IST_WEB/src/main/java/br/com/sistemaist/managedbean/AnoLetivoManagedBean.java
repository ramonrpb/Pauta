package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.util.Util;
    
@ManagedBean(name="anoLetivoMB")
@ViewScoped
public class AnoLetivoManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private AnoLetivo anoLetivo = new AnoLetivo();
	
	private List<AnoLetivo> listaAnoLetivo = new ArrayList<AnoLetivo>();
	
	private List<Integer> listaAno = new ArrayList<Integer>();
	private List<Integer> listaSemestre = new ArrayList<Integer>();
	
	@EJB
	private AnoLetivoService anoLetivoService;
	
	@PostConstruct
	public void carregarTela() {

		anoLetivo = new AnoLetivo();

		Calendar cal = Calendar.getInstance();
		int ano = cal.get(Calendar.YEAR);
		ano += 1;
		
		for(int i = 2014; i <= ano; i++){
			listaAno.add(i);
		}
		listaSemestre.add(1);
		listaSemestre.add(2);
		
		try {
			listaAnoLetivo = anoLetivoService.buscarAnosLetivos();
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
	}

	public void novoAnoLetivo(){
		anoLetivo = new AnoLetivo();
	}
	
	
	/**
	 * Responsavel por salvar o ano letivo
	 * @return
	 */
    public void salvarAnoLetivo(){
    	try {
    		anoLetivoService.salvar(anoLetivo);
			setListaAnoLetivo(anoLetivoService.buscarAnosLetivos());
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
   		Util.montaMensagemFlashRedirect("Cadastro realizado com sucesso !","Sucesso!");
   		anoLetivo = new AnoLetivo();
    }
    
    /**
     * Metodo responsavel pela edicao do ano letivo
     * @return
     */
	public void editarAnoLetivo(){
		try {
    		anoLetivoService.alterar(anoLetivo);
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
    	Util.montaMensagemFlashRedirect("Alteração realizada com sucesso !","Sucesso!");
    	anoLetivo = new AnoLetivo();
    }

	/**
     * Metodo responsavel pela edicao do ano letivo
     * @return
     */
	public void excluirAnoLetivo(){
		try {
    		anoLetivoService.excluir(anoLetivo);
    		setListaAnoLetivo(anoLetivoService.buscarAnosLetivos());
		} catch (ExcecaoGenerica e) {
			log.error("Erro inesperado. "+e.getMessage());
	    	Util.montaMensagemFlashRedirectErro("Ocorreu um erro inesperado, tente mais tarde !","Erro!");
	    	return;
		} catch(EJBException e){
			log.error("Ano letivo já associado a outros itens.");
	    	Util.montaMensagemFlashRedirectErro("Não é possível excluir o ano letivo "+anoLetivo.getNomeFormatado()+". Há históricos associados a ele!","Erro!");
	    	return;
		}
		
    	Util.montaMensagemFlashRedirect("Exclusão realizada com sucesso !","Sucesso!");
    }

	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}
	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public List<AnoLetivo> getListaAnoLetivo() {
		return listaAnoLetivo;
	}
	public void setListaAnoLetivo(List<AnoLetivo> listaAnoLetivo) {
		this.listaAnoLetivo = listaAnoLetivo;
	}

	public List<Integer> getListaAno() {
		return listaAno;
	}
	public void setListaAno(List<Integer> listaAno) {
		this.listaAno = listaAno;
	}

	public List<Integer> getListaSemestre() {
		return listaSemestre;
	}
	public void setListaSemestre(List<Integer> listaSemestre) {
		this.listaSemestre = listaSemestre;
	}
	
}  