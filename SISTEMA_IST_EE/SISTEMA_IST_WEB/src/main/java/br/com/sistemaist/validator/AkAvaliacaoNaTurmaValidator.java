package br.com.sistemaist.validator;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.sistemaist.entidades.Avaliacao;

@FacesValidator(value="akAvaliacaoNaTurmaValidator")
public class AkAvaliacaoNaTurmaValidator implements Validator{

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object valor)
			throws ValidatorException {
		
		if (valor != null) {
			String nome = valor.toString();
			List<Avaliacao> listaAvaliacao = (List<Avaliacao>) uiComponent.getAttributes().get("listaAvaliacao");
			for (Avaliacao a : listaAvaliacao) {
				if (nome.equalsIgnoreCase(a.getNome())) {
					FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nome de avaliação já adicionado", "");
					throw new ValidatorException(fm);
				}
			}
		}
		
	}
	
	

}
