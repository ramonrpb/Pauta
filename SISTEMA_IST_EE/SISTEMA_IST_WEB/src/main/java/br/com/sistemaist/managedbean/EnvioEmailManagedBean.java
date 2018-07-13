package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.email.CorreioEnvio;
import br.com.sistemaist.email.CorreioVO;
import br.com.sistemaist.email.EnvioEmailException;
import br.com.sistemaist.email.MontaMensagemEmail;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.EnvioEmailVO;
import br.com.sistemaist.util.Util;

@ViewScoped
@ManagedBean(name="envioEmailMB")
public class EnvioEmailManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private AnoLetivo anoLetivo = new AnoLetivo();
	
	@EJB
	AnoLetivoService anoLetivoService;
	
	private List<Turma> listaTurmas;
	
	@EJB
	TurmaService turmaService;
	
	private Turma turma = new Turma();
	
	private List<AnoLetivo> listaAnoLetivo = new ArrayList<AnoLetivo>();
	
	private List<EnvioEmailVO> listaEnvioEmail;
	
	private String textoEmail;
	
	private String assuntoEmail;
	
	private Ator ator;
	
	private Boolean enviarParaTodos;
	
	@PostConstruct
	public void carregarTela() {
		if(null == listaTurmas){
			listaTurmas = new ArrayList<Turma>();
		}
		
		try {
			Ator ator = Util.pegarAtor();
			if(null != ator && ator.getPerfil().equals(PerfilEnum.AD)){
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertosAteHoje();
			}else{
				listaAnoLetivo = anoLetivoService.buscarAnosLetivosAbertos();
			}
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemFlashRedirectErro("Um erro inesperado aconteceu, tente mais tarde!", "ERRO");
		}
		if(null == listaAnoLetivo){
			listaAnoLetivo = new ArrayList<AnoLetivo>();
		}
	
	}
	
	public void filtrarTurmasPorAnoLetivo(){
		//buscar turmas do professor
		ator = Util.pegarAtor();
		try {
			if(null != anoLetivo.getId()){
				anoLetivo = anoLetivoService.buscarAnoLetivoPorId(anoLetivo.getId());
			}
			
			listaTurmas = turmaService.listarTurmaPorIdProfessorEAnoLetivo(ator.getId(), anoLetivo.getId());
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
		
		if(null == listaTurmas || listaTurmas.isEmpty()){
			listaTurmas = new ArrayList<Turma>();
			if(null != anoLetivo.getId()){
				Util.montaMensagemFlashRedirectErro("Não há turmas cadastradas para este ano letivo.", "Erro");
			}
		}
	}
	
	public Ator getAtor() {
		return ator;
	}

	public void setAtor(Ator ator) {
		this.ator = ator;
	}

	public void buscarTurma() {
		try {
			turma = turmaService.buscarTurmaPorId(turma.getId());
			listaEnvioEmail = new ArrayList<EnvioEmailVO>();
			for(AlunoTurma at: turma.getAlunoTurma()) {
				EnvioEmailVO emailVO = new EnvioEmailVO();
				emailVO.setAluno(at.getAluno());
				listaEnvioEmail.add(emailVO);
			}
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemErroSemFlashRedirect("Não foi possível recuperar a turma", "ERRO");
		}
	}
	
	public String enviarEmail() {
		CorreioEnvio correioEnvio = new CorreioEnvio();
		List<Aluno> alunos = new ArrayList<Aluno>();
		for(EnvioEmailVO e : listaEnvioEmail) {
			if(e.getEnviarEmail()) {
				alunos.add(e.getAluno());
			}
		}
		try {
			CorreioVO correioVO = MontaMensagemEmail.montarMensagemParaAlunos(alunos, textoEmail, assuntoEmail, ator);
			correioEnvio.enviarEMail(correioVO);
			Util.montaMensagemFlashRedirect("E-mail enviado com sucesso!", "Sucesso");
		} catch (ExcecaoGenerica e1) {
			log.error(e1.getMessage(), e1);
			Util.montaMensagemFlashRedirectErro("Não foi possível enviar o e-mail", "ERRO");
		} catch (EnvioEmailException e1) {
			log.error(e1.getMessage(), e1);
			Util.montaMensagemFlashRedirectErro("Não foi possível enviar o e-mail", "ERRO");
		}
		
		return "envioEmail";
	}
	
	public void marcarTodos() {
		for(EnvioEmailVO e : listaEnvioEmail) {
			if(enviarParaTodos) {
				e.setEnviarEmail(true);
			} else {
				e.setEnviarEmail(false);
			}
		}
	}


	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}


	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}


	public List<Turma> getListaTurmas() {
		return listaTurmas;
	}


	public void setListaTurmas(List<Turma> listaTurmas) {
		this.listaTurmas = listaTurmas;
	}


	public List<AnoLetivo> getListaAnoLetivo() {
		return listaAnoLetivo;
	}


	public void setListaAnoLetivo(List<AnoLetivo> listaAnoLetivo) {
		this.listaAnoLetivo = listaAnoLetivo;
	}


	public Turma getTurma() {
		return turma;
	}


	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<EnvioEmailVO> getListaEnvioEmail() {
		return listaEnvioEmail;
	}

	public void setListaEnvioEmail(List<EnvioEmailVO> listaEnvioEmail) {
		this.listaEnvioEmail = listaEnvioEmail;
	}

	public String getTextoEmail() {
		return textoEmail;
	}

	public void setTextoEmail(String textoEmail) {
		this.textoEmail = textoEmail;
	}

	public String getAssuntoEmail() {
		return assuntoEmail;
	}

	public void setAssuntoEmail(String assuntoEmail) {
		this.assuntoEmail = assuntoEmail;
	}

	public Boolean getEnviarParaTodos() {
		return enviarParaTodos;
	}

	public void setEnviarParaTodos(Boolean enviarParaTodos) {
		this.enviarParaTodos = enviarParaTodos;
	}
	
	

}
