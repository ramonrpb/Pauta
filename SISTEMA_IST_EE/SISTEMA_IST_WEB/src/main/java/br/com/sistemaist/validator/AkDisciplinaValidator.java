package br.com.sistemaist.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.service.DisciplinaService;

@FacesValidator(value="akDisciplinaValidator")
public class AkDisciplinaValidator implements Validator{

	private DisciplinaService disciplinaService;
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object valor) throws ValidatorException {

		try {
			//Não será validado pois é o codigo corrente
			
			String codigo = (String) uiComponent.getAttributes().get("codigo");
			
			if(codigo == null || ! codigo.equals(((String)valor))){
				disciplinaService = (DisciplinaService) new InitialContext().lookup("java:global/SISTEMA_IST_EAR/SISTEMA_IST_EJB/DisciplinaBean");
				
				boolean validou = disciplinaService.validarAkDisciplina((String) valor);
				
				if(! validou){
					FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código já adicionado", "");
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
