package br.com.sistemaist.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.util.Constante;
import br.com.sistemaist.util.MensagensConstantes;
import br.com.sistemaist.util.Utilitario;

public class MontaMensagemEmail {

	/**
	 * Monta o objeto de correioVO, para enviar email ao usuario
	 * 
	 * @param formularioVO
	 * @return
	 * @throws ExcecaoGenerica
	 */
	public static CorreioVO montarMensagemParaUsuario(String emailPara, String login, Calendar dataExpiracaoSenha, String codigo) throws ExcecaoGenerica{
		
		CorreioVO correioVO = new CorreioVO();
		Properties properties = new Properties();
	    try {
			properties.load(MontaMensagemEmail.class.getResourceAsStream(ConstantesDeProperties.CORREIO_PROPERTIES));
			
			correioVO.setEmailDe(properties.getProperty(ConstantesDeProperties.MENSAGEM_DE));
			//Adiciona o destinatario da mensagem
			correioVO.setEmailPara(emailPara);
			
			correioVO.setUsuarioAutorizacao(properties.getProperty(ConstantesDeProperties.SMTP_AUTORIZACAO_USER));
			correioVO.setSenhaAutorizacao(properties.getProperty(ConstantesDeProperties.SMTP_AUTORIZACAO_SENHA)); 
	        
			//Adiciona o titulo da mensagem
			correioVO.setTituloEmail(properties.getProperty(ConstantesDeProperties.MENSAGEM_TITULO));
			
			montarCorpoDoEmail(login, dataExpiracaoSenha, codigo, correioVO, properties);
			
			//Monta o final do corpo da mensagem
			correioVO.addMensagens(properties.getProperty(ConstantesDeProperties.MENSAGEM_ATENCIOSAMENTE)+"\n");
			correioVO.addMensagens(properties.getProperty(ConstantesDeProperties.MENSAGEM_ATENCIOSAMENTE_COMPLEMENTO));
		} catch (IOException e) {
			throw new ExcecaoGenerica(MensagensConstantes.ERRO_RECUPERACAO_ARQUIVO_LOG, e);
		}
	    
	    return correioVO;
	}
	
	
	private static void montarCorpoDoEmail(String login, Calendar dataExpiracaoSenha, String codigo, CorreioVO correioVO, Properties properties) {
		//Monta o corpo da mensagem
		correioVO.addMensagens(properties.getProperty(ConstantesDeProperties.MENSAGEM_INTRODUCAO));
		
		String corpoEmail = properties.getProperty(ConstantesDeProperties.MENSAGEM_INICIO);
		corpoEmail = corpoEmail.replace(ConstantesDeProperties.PARAMETRO_0, Utilitario.formatarData(dataExpiracaoSenha.getTime(), Constante.FORMATO_DATA_HORA));
		correioVO.addMensagens(corpoEmail);
		
		corpoEmail = properties.getProperty(ConstantesDeProperties.MENSAGEM_MENSAGEM);
		corpoEmail = corpoEmail.replace(ConstantesDeProperties.PARAMETRO_0, codigo);
		corpoEmail = corpoEmail.replace(ConstantesDeProperties.PARAMETRO_1, login);
		correioVO.addMensagens(corpoEmail);
		
	}
	
	/**
	 * Monta o objeto de correioVO, para enviar email para os alunos
	 * @param ator 
	 * @param assuntoEmail 
	 * 
	 * @param formularioVO
	 * @return
	 * @throws ExcecaoGenerica
	 */
	public static CorreioVO montarMensagemParaAlunos(List<Aluno> alunos, String mensagem, String assuntoEmail, Ator ator) throws ExcecaoGenerica{
		
		CorreioVO correioVO = new CorreioVO();
		Properties properties = new Properties();
	    try {
			properties.load(MontaMensagemEmail.class.getResourceAsStream(ConstantesDeProperties.CORREIO_PROPERTIES));
			
			correioVO.setEmailDe(ator.getEmail());
			//Adiciona o destinatario da mensagem
			for(Aluno a : alunos) {
				correioVO.getEmailsPara().add(a.getEmail());
			}
			
			correioVO.setUsuarioAutorizacao(properties.getProperty(ConstantesDeProperties.SMTP_AUTORIZACAO_USER));
			correioVO.setSenhaAutorizacao(properties.getProperty(ConstantesDeProperties.SMTP_AUTORIZACAO_SENHA)); 
	        
			//Adiciona o titulo da mensagem
			correioVO.setTituloEmail(assuntoEmail);
			correioVO.setMensagens(new ArrayList<String>());
			correioVO.getMensagens().add(mensagem);
			//Monta o final do corpo da mensagem
			correioVO.addMensagens(properties.getProperty(ConstantesDeProperties.MENSAGEM_ATENCIOSAMENTE)+"\n");
			correioVO.addMensagens(ator.getNome() + " - " + ator.getEmail());
		} catch (IOException e) {
			throw new ExcecaoGenerica(MensagensConstantes.ERRO_RECUPERACAO_ARQUIVO_LOG, e);
		}
	    
	    return correioVO;
	}
}
