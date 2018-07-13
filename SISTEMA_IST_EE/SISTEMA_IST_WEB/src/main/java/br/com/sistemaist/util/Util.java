package br.com.sistemaist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.sistemaist.autenticacao.Autenticacao;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.vo.MesVO;

public class Util {

	public static Ator pegarAtor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
        if(authentication instanceof Autenticacao){
        	Autenticacao autenticacao = (Autenticacao) authentication;
        	return autenticacao.getAtor();
        }
        return null;
	}
	
	 /**
     * Adiciona a mensagem de INFO para ser exibida, quando nao trocamos de tela
     * @param mensagem
     * @param titulo
    */
	public static void montaMensagemSemFlashRedirect(String mensagem, String titulo) {
			
		// MENSAGEM REDIRECIONADA PARA SER EXIBIDA EM OUTRA PAGINA
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO , mensagem, titulo);
		facesContext.addMessage("mensagem", message);
	}
	 
	 /**
     * Adiciona a mensagem de ERROR para ser exibida, quando nao trocamos de tela
     * @param mensagem
     * @param titulo
     */
	 public static void montaMensagemErroSemFlashRedirect(String mensagem, String titulo) {
	
		// MENSAGEM PARA SER EXIBIDA NA MESMA PAGINA
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR , mensagem, titulo);
		facesContext.addMessage("mensagem", message);
	}
	 
	/**
     * Adiciona a mensagem de INFO para ser exibida, quando trocamos de tela
     * @param mensagem
     * @param titulo
     */
	 public static void montaMensagemFlashRedirect(String mensagem, String titulo) {
	
		// MENSAGEM REDIRECIONADA PARA SER EXIBIDA EM OUTRA PAGINA
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
//		flash.setRedirect(true);
		flash.setKeepMessages(true);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO , mensagem, titulo);
		facesContext.addMessage("mensagem", message);
	}
	 
 /**
     * Adiciona a mensagem de ERROR para ser exibida, quando trocamos de tela
     * @param mensagem
     * @param titulo
     */
	 public static void montaMensagemFlashRedirectErro(String mensagem, String titulo) {
	
		// MENSAGEM REDIRECIONADA PARA SER EXIBIDA EM OUTRA PAGINA
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
//			flash.setRedirect(true);
		flash.setKeepMessages(true);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR , mensagem, titulo);
		facesContext.addMessage("mensagem", message);
	}
	 
	public static <T> T recuperarManagedBean(String beanName) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		  T bean = (T) context.
		    getELContext().getELResolver().getValue(context.getELContext(), null, beanName);
		 return bean;
	}
	
	public static List<MesVO> carregarMesesPorSemestre(Integer mesInicial, Integer mesFinal){
		TreeMap<Integer, MesVO> lista = new TreeMap<Integer, MesVO>();
		List<MesVO> meses = new ArrayList<MesVO>();
		MesVO mesVO;
		if(mesFinal == null){
			mesVO = new MesVO();
			mesVO.setId(1);
			mesVO.setNome("Janeiro");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(2);
			mesVO.setNome("Fevereiro");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(3);
			mesVO.setNome("Março");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(4);
			mesVO.setNome("Abril");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(5);
			mesVO.setNome("Maio");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(6);
			mesVO.setNome("Junho");
			meses.add(mesVO);
			mesVO = new MesVO();
			mesVO.setId(7);
			mesVO.setNome("Julho");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(8);
			mesVO.setNome("Agosto");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(9);
			mesVO.setNome("Setembro");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(10);
			mesVO.setNome("Outubro");
			meses.add(mesVO);
			
			mesVO = new MesVO();
			mesVO.setId(11);
			mesVO.setNome("Novembro");
			meses.add(mesVO);

			mesVO = new MesVO();
			mesVO.setId(12);
			mesVO.setNome("Dezembro");
			meses.add(mesVO);
		}else{
			switch (mesFinal) {
			case 12:
				mesVO = new MesVO();
				mesVO.setId(12);
				mesVO.setNome("Dezembro");
				lista.put(mesVO.getId(), mesVO);
			case 11:
				mesVO = new MesVO();
				mesVO.setId(11);
				mesVO.setNome("Novembro");
				lista.put(mesVO.getId(), mesVO);
			case 10:
				mesVO = new MesVO();
				mesVO.setId(10);
				mesVO.setNome("Outubro");
				lista.put(mesVO.getId(), mesVO);
			case 9:
				mesVO = new MesVO();
				mesVO.setId(9);
				mesVO.setNome("Setembro");
				lista.put(mesVO.getId(), mesVO);
			case 8:
				mesVO = new MesVO();
				mesVO.setId(8);
				mesVO.setNome("Agosto");
				lista.put(mesVO.getId(), mesVO);
			case 7:
				mesVO = new MesVO();
				mesVO.setId(7);
				mesVO.setNome("Julho");
				lista.put(mesVO.getId(), mesVO);
			case 6:
				mesVO = new MesVO();
				mesVO.setId(6);
				mesVO.setNome("Junho");
				lista.put(mesVO.getId(), mesVO);
			case 5:
				mesVO = new MesVO();
				mesVO.setId(5);
				mesVO.setNome("Maio");
				lista.put(mesVO.getId(), mesVO);
			case 4:
				mesVO = new MesVO();
				mesVO.setId(4);
				mesVO.setNome("Abril");
				lista.put(mesVO.getId(), mesVO);
			case 3:
				mesVO = new MesVO();
				mesVO.setId(3);
				mesVO.setNome("Março");
				lista.put(mesVO.getId(), mesVO);
			case 2:
				mesVO = new MesVO();
				mesVO.setId(2);
				mesVO.setNome("Fevereiro");
				lista.put(mesVO.getId(), mesVO);
			case 1:
				mesVO = new MesVO();
				mesVO.setId(1);
				mesVO.setNome("Janeiro");
				lista.put(mesVO.getId(), mesVO);
			}
			for(MesVO mes : lista.values()){
				if(mes.getId() >= mesInicial){
					meses.add(mes);				
				}
			}
		}
		
		return meses;
	}
}
