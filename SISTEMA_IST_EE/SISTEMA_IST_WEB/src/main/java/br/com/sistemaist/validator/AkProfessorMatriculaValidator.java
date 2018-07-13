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
import br.com.sistemaist.service.ProfessorService;

@FacesValidator(value="akProfessorMatriculaValidator")
public class AkProfessorMatriculaValidator implements Validator{

	private ProfessorService professorService;
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object valor) throws ValidatorException {

		try {
			Long codigo = (Long) uiComponent.getAttributes().get("codigo");
			
			professorService = (ProfessorService) new InitialContext().lookup("java:global/SISTEMA_IST_EAR/SISTEMA_IST_EJB/ProfessorBean");
			
			boolean validou;
			
			if(null == codigo){
				//novo
				validou = professorService.validarAkMatricula(null, (String) valor);
			}else{
				//existente, alteracao
				validou = professorService.validarAkMatricula(codigo, (String) valor);
			}
				
			if(! validou){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Já existe um professor cadastrado com esta matricula", "");
				throw new ValidatorException(fm);
			}
		} catch (ExcecaoGenerica e) {
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na consulta do banco de dados", "");
			throw new ValidatorException(fm);
		} catch (NamingException e) {
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao montar o acesso ao banco de dados", "");
			throw new ValidatorException(fm);
		}
		
	}
}
