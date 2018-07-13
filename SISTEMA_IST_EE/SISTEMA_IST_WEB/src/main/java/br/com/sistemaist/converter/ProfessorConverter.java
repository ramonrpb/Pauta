package br.com.sistemaist.converter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.managedbean.ComboManagedBean;
import br.com.sistemaist.util.Util;

@FacesConverter(value="professorConverter")
public class ProfessorConverter implements Serializable, Converter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ComboManagedBean comboManagedBean;
	
	private Logger log = LoggerFactory.getLogger(getClass());

	public ProfessorConverter() {
		comboManagedBean = Util.recuperarManagedBean("comboMB");
	}
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 != null) {
			Long id = Long.valueOf(arg2);
			for(Professor p: comboManagedBean.getListaProfessor()) {
				if(p.getId().equals(id)) {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 != null && ((Professor) arg2).getId() != null) {
			return String.valueOf(((Professor) arg2).getId());
		} else {
			return null;
		}
		 
	}

}
