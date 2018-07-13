package br.com.sistemaist.managedbean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.service.AtorService;
    
@ManagedBean(name="validatorMB")
public class ValidatorManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;
	
	@EJB
	private AtorService atorBean;
    
    public void validaExistenciaLogin(FacesContext facesContext, UIComponent uiComponent, Object value){
    	
   		boolean loginExiste = false;
		try {
			Long id = (Long) uiComponent.getAttributes().get("idAtor");
			loginExiste = atorBean.verificarExistenciaLogin( (String) value, id);
		} catch (ExcecaoGenerica e) {
			e.printStackTrace();
		}
   	    
   		if(loginExiste){
   			//Não aceito
   			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Este login já existe.","Login já existe."));
   		}
    }

}