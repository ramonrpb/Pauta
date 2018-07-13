package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.datamodel.LazyTurmaDataModel;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.AlunoTurma;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Disciplina;
import br.com.sistemaist.entidades.Periodo;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.entidades.Turno;
import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.service.AlunoService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.util.Util;
import br.com.sistemaist.vo.CombosTurmaVO;
import br.com.sistemaist.vo.PaginaVO;
    
@ManagedBean(name="turmaMB")
@ViewScoped
public class TurmaManagedBean implements Serializable {  
    
	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(getClass());

	
	@EJB
	private TurmaService turmaService;
	
	@EJB
	private AlunoService alunoService;
	
	private CombosTurmaVO comboTurmaVO = new CombosTurmaVO();
	
	private Turma turma;
	
	private Aluno aluno;
	
	private Periodo periodo = new Periodo();
	
	private Disciplina disciplina = new Disciplina();
	
	private Professor professor = new Professor();
	
	private Turno turno = new Turno();
	
	private AnoLetivo anoLetivo = new AnoLetivo();
	
	private List<AlunoTurma> alunoTurmaExcluido = new ArrayList<AlunoTurma>();

	private PaginaVO paginaVO = new PaginaVO();
	private LazyDataModel<Turma> lazyTurmas;
	
	private Long anoLetivoOrigem;
	private Long anoLetivoDestino;
	
	public List<Professor> completarProfessor(String query) {
        List<Professor> results = new ArrayList<Professor>();
        for(Professor p: comboTurmaVO.getListaProfessor()) {
            
			if(p.getNomeEMatricula().contains(query)) {
				results.add(p);
			}
        }
         
        return results;
    }
	
	
	
	public List<Disciplina> populaDisciplina() {
		List<Disciplina> lista = new ArrayList<Disciplina>();
		if(periodo.getId() == null ) {
			return lista;
		}
		
		for(Disciplina d: comboTurmaVO.getListaDisciplina()) {
			if(periodo.getId().equals(d.getPeriodo().getId())) {
				lista.add(d);
			}
		}
		return lista;
	}
	
	@PostConstruct
	public void carregarTela() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idTurma = request.getParameter("idTurma");
		
		if(idTurma != null) {
			Long idTurmaLong = Long.valueOf(idTurma);
			try {
				turma = turmaService.buscarTurmaPorId(idTurmaLong);
				periodo = turma.getDisciplina().getPeriodo();
			} catch (ExcecaoGenerica e) {
				Util.montaMensagemFlashRedirectErro("Não foi possível recuperar as turmas", "Erro");
			}
		} else {
			if(turma == null) {
				turma = new Turma();
				turma.setDisciplina(new Disciplina());
				turma.setAnoLetivo(new AnoLetivo());
				turma.setTurno(new Turno());
				turma.setProfessor(new Professor());
			}
		}
		
		
		try {
			setComboTurmaVO(turmaService.buscarDadosTurma());
		} catch (ExcecaoGenerica e) {
			log.error("Erro ao carregar os dados da turma");
			Util.montaMensagemFlashRedirectErro("Erro ao carregar os dados da turma", "Erro");
		}
	}

	public void carregarTurmasCadastrados() {
		int quantidadeTurmas = 0;
		try {
			if(Util.pegarAtor().getPerfil().equals(PerfilEnum.AD)){
				quantidadeTurmas = turmaService.contarTurmasFiltradas(anoLetivo, periodo, disciplina, professor, turno);
			}else{
				quantidadeTurmas = turmaService.contarTurmasProfessor(Util.pegarAtor(), anoLetivo, periodo,  disciplina, turno);
			}
			
			paginaVO.setAnoLetivo(anoLetivo);
			paginaVO.setPeriodo(periodo);
			paginaVO.setDisciplina(disciplina);
			paginaVO.setProfessor(professor);
			paginaVO.setTurno(turno);
			
			lazyTurmas = new LazyTurmaDataModel(paginaVO);
			lazyTurmas.setPageSize(10);
			lazyTurmas.setRowCount(quantidadeTurmas);
			
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível carregar as turmas", e);
		}
		
	}
	
	public void adicionarAluno() {
		if(!existeAlunoAssociado()) {
		
			if(turma.getAlunoTurma() == null) {
				turma.setAlunoTurma(new ArrayList<AlunoTurma>());
			}
			if (aluno != null && aluno.getId() != null) {
				turma.getAlunoTurma().add(new AlunoTurma(aluno, turma));
				aluno = null;
			}
		}
	}



	private Boolean existeAlunoAssociado() {
		for(AlunoTurma at : turma.getAlunoTurma()) {
			if(at.getAluno().getId().equals(aluno.getId())) {
				Util.montaMensagemFlashRedirectErro("Aluno já cadastrado na turma", "Erro");
				return true;
			}
			
		}
		return false;
	}
	
	public String salvar() {
		
		try {
			if(turma.getId() != null) {
				turmaService.deletarAssociacaoAlunoTurma(alunoTurmaExcluido);
			}
			turmaService.salvar(turma);
			Util.montaMensagemFlashRedirect("Turma salva no sistema com sucesso", "Transação efetuada");
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível salvar a turma", e);
			Util.montaMensagemFlashRedirectErro("Não foi possível salvar a turma", "Erro");
		}
		
		return "listagemTurmas";
	}
	
	public String excluir() {
		
		try {
			turmaService.excluir(turma.getId());
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível excluir a turma de id: "+ turma.getId());
			Util.montaMensagemFlashRedirectErro("Não foi possível excluir a turma", "Erro");
		} catch (EJBException e) {
			log.error("Turma já associada a outros itens."+e.getMessage());
	    	Util.montaMensagemFlashRedirectErro("Não é possível excluir a turma "+turma.getNome()+". Há históricos associados a ela!","Erro!");
	    	return "listagemTurmas";
		}
		
		Util.montaMensagemFlashRedirect("Turma excluída com sucesso!", "Transação efetuada");
		return "listagemTurmas";
	}
	
	public String gerarTurmasPorAnoLetivo() {
		
		try {
			turmaService.gerarTurmasPorAnoLetivo(anoLetivoOrigem, anoLetivoDestino);
		} catch (ExcecaoGenerica e) {
			log.error(e.getMessage(), e);
			Util.montaMensagemFlashRedirectErro("Não foi possível gerar as turmas", "ERRO");
		}
		
		Util.montaMensagemFlashRedirect("Turmas geradas com sucesso!", "Transação feita com sucesso");
		return "listagemTurmas";
	}
	
	public Long getAnoLetivoOrigem() {
		return anoLetivoOrigem;
	}

	public void setAnoLetivoOrigem(Long anoLetivoOrigem) {
		this.anoLetivoOrigem = anoLetivoOrigem;
	}

	public Long getAnoLetivoDestino() {
		return anoLetivoDestino;
	}

	public void setAnoLetivoDestino(Long anoLetivoDestino) {
		this.anoLetivoDestino = anoLetivoDestino;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public CombosTurmaVO getComboTurmaVO() {
		return comboTurmaVO;
	}

	public void setComboTurmaVO(CombosTurmaVO comboTurmaVO) {
		this.comboTurmaVO = comboTurmaVO;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	
	public LazyDataModel<Turma> getLazyTurmas() {
		return lazyTurmas;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<AlunoTurma> getAlunoTurmaExcluido() {
		return alunoTurmaExcluido;
	}

	public void setAlunoTurmaExcluido(List<AlunoTurma> alunoTurmaExcluido) {
		this.alunoTurmaExcluido = alunoTurmaExcluido;
	}

	public AnoLetivo getAnoLetivo() {
		return anoLetivo;
	}

	public void setAnoLetivo(AnoLetivo anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public PaginaVO getPaginaVO() {
		return paginaVO;
	}

	public void setPaginaVO(PaginaVO paginaVO) {
		this.paginaVO = paginaVO;
	}



	public Disciplina getDisciplina() {
		return disciplina;
	}



	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}



	public Professor getProfessor() {
		return professor;
	}



	public void setProfessor(Professor professor) {
		this.professor = professor;
	}



	public Turno getTurno() {
		return turno;
	}



	public void setTurno(Turno turno) {
		this.turno = turno;
	}
}  