package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAvaliacao;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Ator;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AlunoAvaliacaoService;
import br.com.sistemaist.service.AnoLetivoService;
import br.com.sistemaist.service.AvaliacaoService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Util;
    
@ManagedBean(name="visualizacaoNotaAlunoMB")
@ViewScoped
public class VisualizacaoNotasAlunoManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private AnoLetivo anoLetivo = new AnoLetivo();
	private List<AnoLetivo> listaAnoLetivo = new ArrayList<AnoLetivo>();
	
	private List<Disciplina> listaDisciplinas = new ArrayList<Disciplina>();
	
	private HashMap<Long, List<AlunoAvaliacao>> listaDisciplinaAlunoAvaliacao = new HashMap<Long, List<AlunoAvaliacao>>();
	
	private Ator ator;
	
	@EJB
	AvaliacaoService avaliacaoService;
	@EJB
	AlunoAvaliacaoService alunoAvaliacaoService;
	@EJB
	TurmaService turmaService;
	@EJB
	AnoLetivoService anoLetivoService;
	
	@PostConstruct
	public void carregarTela() {
		
		try {
			ator = Util.pegarAtor();
			if(null != ator && ator.getPerfil().equals(PerfilEnum.AL)){
				listaAnoLetivo = anoLetivoService.buscarAnosLetivos();
			}
			
			if(null == listaAnoLetivo){
				listaAnoLetivo = new ArrayList<AnoLetivo>();
			}
			
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao carregar os dados da aula");
			Util.montaMensagemFlashRedirectErro("Erro ao carregar os dados da aula", "Erro");
		}
	}
	
	public void filtrarTurmasPorAnoLetivo(){
		//buscar turmas do aluno no ano letivo selecionado
		try {
			if(ator.getPerfil().equals(PerfilEnum.AL) && null != anoLetivo.getId()){
				List<Turma> listaTurmas = turmaService.listarTurmaPorIdAlunoEAnoLetivo(ator.getId(), anoLetivo.getId());
				listaDisciplinaAlunoAvaliacao = alunoAvaliacaoService.carregarAvaliacoesPorAlunoAnoLetivo(ator.getId(), anoLetivo.getId());
				
				listaDisciplinas = new ArrayList<Disciplina>();
				for(Turma t : listaTurmas){
					listaDisciplinas.add(t.getDisciplina());
				}
				
				if(listaDisciplinas.isEmpty()){
					Util.montaMensagemFlashRedirectErro("O aluno não está cadastrado em nenhuma disciplina neste ano letivo.", "Erro");
				}
			}
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao filtrar a lista de alunos por turma");
			Util.montaMensagemFlashRedirectErro("Erro ao filtrar a lista de alunos por turma", "Erro");
		}
	}

	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}

	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public List<AnoLetivo> getListaAnoLetivo() {
		return listaAnoLetivo;
	}

	public void setListaAnoLetivo(List<AnoLetivo> listaAnoLetivo) {
		this.listaAnoLetivo = listaAnoLetivo;
	}

	public HashMap<Long, List<AlunoAvaliacao>> getListaDisciplinaAlunoAvaliacao() {
		return listaDisciplinaAlunoAvaliacao;
	}

	public void setListaDisciplinaAlunoAvaliacao(HashMap<Long, List<AlunoAvaliacao>> listaDisciplinaAlunoAvaliacao) {
		this.listaDisciplinaAlunoAvaliacao = listaDisciplinaAlunoAvaliacao;
	}

	public List<Disciplina> getListaDisciplinas() {
		return listaDisciplinas;
	}

	public void setListaDisciplinas(List<Disciplina> listaDisciplinas) {
		this.listaDisciplinas = listaDisciplinas;
	}

}  