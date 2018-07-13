package br.com.sistemaist.converter;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesConverter(value="doubleConverter")
public class DoubleConverter implements Converter {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		if(value != null) {
			value = value.replace(",", ".");
			return Double.valueOf(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		
		try {
			if(value != null) {
				DecimalFormat decimal = new DecimalFormat("#,###.00");
				return decimal.format(Double.valueOf(value.toString()));
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return null;
	}

}
