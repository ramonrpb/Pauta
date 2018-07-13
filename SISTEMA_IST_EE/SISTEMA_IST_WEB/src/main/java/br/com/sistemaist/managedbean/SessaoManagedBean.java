package br.com.sistemaist.managedbean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Aula;
import br.com.sistemaist.util.Util;
    
@ManagedBean(name="sessaoMB")
@SessionScoped
public class SessaoManagedBean implements Serializable {  
      
	private static final long serialVersionUID = 1L;
	private Ator ator;
	
	private Aula aula;

	public Ator getAtor() {
		if(null == ator){
    		ator = Util.pegarAtor();
    	}
		return ator;
	}
	public void setAtor(Ator ator) {
		this.ator = ator;
	}
	
	public Aula getAula() {
		return aula;
	}
	public void setAula(Aula aula) {
		this.aula = aula;
	}
}  