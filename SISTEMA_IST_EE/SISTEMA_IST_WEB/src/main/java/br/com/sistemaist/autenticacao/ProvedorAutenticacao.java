package br.com.sistemaist.autenticacao;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.managedbean.SessaoManagedBean;
import br.com.sistemaist.service.ProvedorAutenticacaoService;
import br.com.sistemaist.util.EncriptarMD5;

public class ProvedorAutenticacao implements AuthenticationProvider{
	
	private Logger log = LoggerFactory.getLogger(getClass());

//	@EJB
	private ProvedorAutenticacaoService provedorAutenticacaoService;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		log.info("*******************Autenticação********************");
		
		Ator ator = null;
		try {
			provedorAutenticacaoService = (ProvedorAutenticacaoService) 
					new InitialContext().lookup("java:global/SISTEMA_IST_EAR/SISTEMA_IST_EJB/ProvedorAutenticacaoBean");
			
			ator = provedorAutenticacaoService.buscarAtorPorLoginESenha(authentication.getName(), EncriptarMD5.encriptar(authentication.getCredentials().toString()));
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
	
		if(ator == null){
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}
		
		Autenticacao autenticacao = null;// = new Autenticacao("ROLE_USER", authentication);
		
		if(PerfilEnum.AD.equals(ator.getPerfil())){
			autenticacao = new Autenticacao("ROLE_ADMIN", authentication);
		}else if(PerfilEnum.PR.equals(ator.getPerfil())){
			autenticacao = new Autenticacao("ROLE_PROFESSOR", authentication);
		} else if(PerfilEnum.AL.equals(ator.getPerfil())){
			autenticacao = new Autenticacao("ROLE_ALUNO", authentication);
		} else {
			autenticacao = new Autenticacao("ROLE_USER", authentication);
		}
		
		autenticacao.setAtor(ator);
		autenticacao.setAuthenticated(true);
		
		SessaoManagedBean sessaoManagedBean = new SessaoManagedBean();
		sessaoManagedBean.setAtor(ator);
		
		return autenticacao;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
}
