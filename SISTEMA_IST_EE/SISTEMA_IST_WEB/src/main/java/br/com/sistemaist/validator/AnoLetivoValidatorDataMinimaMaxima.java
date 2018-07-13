package br.com.sistemaist.validator;


import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.sistemaist.util.Constante;
import br.com.sistemaist.util.Utilitario;

/**
 * recebe os atributos dataMinima e dataMaxima e valida a data recebida entre estes valores
 * @author Felipe
 */
@FacesValidator(value="anoLetivoValidatorDataMinimaMaxima")
public class AnoLetivoValidatorDataMinimaMaxima implements Validator{

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object valor) throws ValidatorException {

		UIInput inputDataInicio = (UIInput) facesContext.getViewRoot().findComponent("form:dataInicio");

		if(null == (Date)valor){
			return;
		}
		
		if(null != inputDataInicio && ( null != inputDataInicio.getValue() || null != inputDataInicio.getSubmittedValue())){
			
			Date dataMinima = null;
			if(null != inputDataInicio.getSubmittedValue()){
				dataMinima = Utilitario.formatarStringParaData(inputDataInicio.getSubmittedValue().toString().trim(), Constante.FORMATO_DATA);
			}else if(null != inputDataInicio.getValue()){
				dataMinima = (Date) inputDataInicio.getValue();
			}
			if(null != dataMinima && dataMinima.after((Date) valor)){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data inválida, a data deve ser lançada a partir do dia "+Utilitario.formatarData(dataMinima, Constante.FORMATO_DATA), "");
				throw new ValidatorException(fm);
			}
		}
		
		UIInput inputDataFim = (UIInput) facesContext.getViewRoot().findComponent("form:dataFim");

		if(null != inputDataFim && ( null != inputDataFim.getValue() || null != inputDataFim.getSubmittedValue())){
			Date dataMaxima = null;			
			if(null != inputDataFim.getSubmittedValue()){
				dataMaxima = Utilitario.formatarStringParaData(inputDataFim.getSubmittedValue().toString().trim(), Constante.FORMATO_DATA);
			}else if(null != inputDataFim.getValue()){
				dataMaxima = (Date) inputDataFim.getValue();
			}
			
			if(null != dataMaxima && dataMaxima.before((Date) valor)){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data inválida, a data deve ser lançada até dia "+Utilitario.formatarData(dataMaxima, Constante.FORMATO_DATA), "");
				throw new ValidatorException(fm);
			}
		}
		
	}
}
