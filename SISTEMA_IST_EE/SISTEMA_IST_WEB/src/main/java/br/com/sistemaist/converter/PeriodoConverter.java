package br.com.sistemaist.converter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.managedbean.ComboManagedBean;

@FacesConverter(value="periodoConverter")
@ManagedBean
public class PeriodoConverter implements Serializable, Converter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{comboMB}")
	private ComboManagedBean comboManagedBean;
	
	public PeriodoConverter() {
		
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if(arg2 == null) {
			return null;
		}
		Long id = Long.valueOf(arg2);
		for(Periodo p : comboManagedBean.getComboTurmaVO().getListaPeriodo()) {
			if(id.equals(p.getId())) {
				return p;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 != null) {
			return ((Periodo) arg2).getNome();
		} else {
			return null;
		}
		 
	}

	public ComboManagedBean getComboManagedBean() {
		return comboManagedBean;
	}

	public void setComboManagedBean(ComboManagedBean comboManagedBean) {
		this.comboManagedBean = comboManagedBean;
	}
	
	

}
