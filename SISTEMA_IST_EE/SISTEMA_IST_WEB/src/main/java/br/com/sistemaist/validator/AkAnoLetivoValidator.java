package br.com.sistemaist.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.service.AnoLetivoService;

@FacesValidator(value="akAnoLetivoValidator")
public class AkAnoLetivoValidator implements Validator{

	private AnoLetivoService anoLetivoService;
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object valor) throws ValidatorException {

		try {
			//Não será validado pois é o codigo corrente
			
			Long id = (Long) uiComponent.getAttributes().get("idAnoLetivo");
			
			UIInput inputAno = (UIInput) facesContext.getViewRoot().findComponent("form:ano");

			if(null != inputAno && ( null != inputAno.getValue() || null != inputAno.getSubmittedValue())){
				
				Integer ano = null;
				if(null != inputAno.getValue()){
					ano = Integer.parseInt(inputAno.getValue().toString().trim());
				}else if(null != inputAno.getSubmittedValue()){
					ano = Integer.parseInt(inputAno.getSubmittedValue().toString().trim());
				}
				
				Integer semestre = (Integer)valor;
				
				anoLetivoService = (AnoLetivoService) new InitialContext().lookup("java:global/SISTEMA_IST_EAR/SISTEMA_IST_EJB/AnoLetivoBean");
				
				boolean validou = anoLetivoService.validarAkAnoLetivo(ano, semestre, id);
				
				if(! validou){
					FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Combinação já adicionado", "");
					throw new ValidatorException(fm);
				}
			}
		} catch (ExcecaoGenerica e) {
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na consulta do banco de dados", "");
			throw new ValidatorException(fm);
		} catch (NamingException e) {
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao montar o acesso ao banco de dados ", "");
			throw new ValidatorException(fm);
		}
		
	}
}
