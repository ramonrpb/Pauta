package br.com.sistemaist.validator;


import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.service.AulaService;
import br.com.sistemaist.util.Constante;
import br.com.sistemaist.util.Utilitario;

@FacesValidator(value="aulaTurmaDataValidator")
public class AulaTurmaDataValidator implements Validator{

	private AulaService aulaService;
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object valor) throws ValidatorException {

		try {
			Long codigo = (Long) uiComponent.getAttributes().get("codigo");
			Long turma = (Long) uiComponent.getAttributes().get("idTurma");
			
			AnoLetivo anoLetivo = (AnoLetivo) uiComponent.getAttributes().get("anoLetivo");
			
			aulaService = (AulaService) new InitialContext().lookup("java:global/SISTEMA_IST_EAR/SISTEMA_IST_EJB/AulaBean");
			
			if(null != anoLetivo.getDataInicio() && anoLetivo.getDataInicio().after((Date) valor)){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data inválida, a data deve ser lançada a partir do dia "+Utilitario.formatarData(anoLetivo.getDataInicio(), Constante.FORMATO_DATA), "");
				throw new ValidatorException(fm);
			}
			if(null != anoLetivo.getDataFim() && anoLetivo.getDataFim().before((Date) valor)){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data inválida, a data deve ser lançada até dia "+Utilitario.formatarData(anoLetivo.getDataFim(), Constante.FORMATO_DATA), "");
				throw new ValidatorException(fm);
			}
			
			boolean validou;
			
			if(null == codigo){
				//novo
				validou = aulaService.validarTurmaData(null, turma, (Date) valor);
			}else{
				//existente, alteracao
				validou = aulaService.validarTurmaData(codigo, turma, (Date) valor);
			}
				
			if(! validou){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Já existe uma aula cadastrada neste dia", "");
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
