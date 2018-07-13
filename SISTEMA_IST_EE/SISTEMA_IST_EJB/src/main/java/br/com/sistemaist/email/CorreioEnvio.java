package br.com.sistemaist.email;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.util.MensagensConstantes;

/**
 * Classe responsavel pelo envio de e-mail.
 * 
 *
 */
public class CorreioEnvio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	protected Session session;

	/**
	 * Monta o email atraves dos dados passador no objeto correioVO e envia o email
	 * @param correioVO
	 * @throws ExcecaoGenerica 
	 */
	public void enviarEMail(CorreioVO correioVO) throws EnvioEmailException {
		
	    try {
	    	autenticarSMTP(correioVO);

	        Transport transport = session.getTransport();

	        MimeMessage message = new MimeMessage(session);
	        
	        StringBuilder mensagem = new StringBuilder();
	        for (String itemMensagem : correioVO.getMensagens()) {
				mensagem.append(itemMensagem);
				mensagem.append(ConstantesDeProperties.PULA_LINHA);
			}
	        
	        message.setSubject(correioVO.getTituloEmail(), ConstantesDeProperties.UTF8);
	        
	        message.setFrom(new InternetAddress(correioVO.getEmailDe()));
	        message.setSender(new InternetAddress(correioVO.getEmailDe()));
	      
	        if(correioVO.getEmailPara() != null) {
    		   message.addRecipient(Message.RecipientType.TO, new InternetAddress(correioVO.getEmailPara()));
	        }
	       if(correioVO.getEmailsPara().size() > 0) {
	    	   for(String email: correioVO.getEmailsPara()) {
	    		   message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
	    	   }
	       }

	        Multipart mp = new MimeMultipart();
	        
	        //Adiciona o texto do email
	        MimeBodyPart mbp = new MimeBodyPart();
	        mbp.setText(mensagem.toString(), ConstantesDeProperties.UTF8);
	        mp.addBodyPart(mbp);
	        
	       //enviando anexo  
	        if(null != correioVO.getListaArquivos() && !correioVO.getListaArquivos().isEmpty()){
	        	DataSource fds = null;
	        	
	        	for(File arquivo : correioVO.getListaArquivos()){
		        	mbp = new MimeBodyPart();
		            fds = new FileDataSource(arquivo);  
		            mbp.setDisposition(Part.ATTACHMENT);  
		            mbp.setDataHandler(new DataHandler(fds));  
		            mbp.setFileName(fds.getName());
		            mp.addBodyPart(mbp);
	        	}
	        }
	        
	        //adiciona 
	        message.setContent(mp, ConstantesDeProperties.UTF8);
	        
	        transport.connect();
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	        
	    } catch (MessagingException me) {
	    	log.error(me.getMessage(), me);
	        throw new EnvioEmailException(MensagensConstantes.ERRO_ENVIO_EMAIL, me);
	    }	
	}
	
	/**
	 * Cria a session a partir do properties "/correio.properties" para o autenticador SMTP
	 * @param messageProperties
	 * @throws ExcecaoGenerica
	 */
	public void autenticarSMTP(CorreioVO messageProperties) throws EnvioEmailException {

		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream(ConstantesDeProperties.CORREIO_PROPERTIES));
			
			//Faz a autenticacao do servidor de envio de email
		    this.session = Session.getInstance(properties, new SMTPAuthenticator(messageProperties.getUsuarioAutorizacao(), messageProperties.getSenhaAutorizacao()));

		} catch (IOException e) {
			throw new EnvioEmailException(MensagensConstantes.ERRO_RECUPERACAO_ARQUIVO_LOG, e);
		}
	}
	
}
