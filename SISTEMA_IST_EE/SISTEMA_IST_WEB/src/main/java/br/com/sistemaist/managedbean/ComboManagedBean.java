package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.Aluno;
import br.com.sistemaist.entidades.Professor;
import br.com.sistemaist.service.AlunoService;
import br.com.sistemaist.service.ProfessorService;
import br.com.sistemaist.service.TurmaService;
import br.com.sistemaist.vo.CombosTurmaVO;

@ManagedBean(name="comboMB")
@ViewScoped
public class ComboManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private TurmaService turmaService;
	
	@EJB
	private AlunoService alunoService;
	
	@EJB
	private ProfessorService professorService;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private CombosTurmaVO comboTurmaVO = new CombosTurmaVO();
	
	private List<Aluno> listaAluno;
	
	private List<Professor> listaProfessor;

	public CombosTurmaVO getComboTurmaVO() {
		try {
			comboTurmaVO = turmaService.buscarDadosTurma();
		} catch (ExcecaoGenerica e) {
			log.error("Não foi possível carregar a combo turmas");
		}
		return comboTurmaVO;
	}
	
	public List<Aluno> completarAluno(String query) {
        
		List<Aluno> results = new ArrayList<Aluno>();
        for(Aluno a: getListaAluno()) {
            
			if(a.getNomeEMatricula().contains(query)) {
				results.add(a);
			}
        }
         
        return results;
    }

	public void setComboTurmaVO(CombosTurmaVO comboTurmaVO) {
		this.comboTurmaVO = comboTurmaVO;
	}

	
	
	public List<Aluno> getListaAluno() {
		if(listaAluno == null) {
			try {
				listaAluno = alunoService.carregarAlunosCadastrados();
				return listaAluno;
			} catch (ExcecaoGenerica e) {
				log.error("Erro ao recuperar a lista de Alunos cadastrados");
				return null;
			}
		} else {
			return listaAluno;
		}
		
	}

	public void setListaAluno(List<Aluno> listaAluno) {
		this.listaAluno = listaAluno;
	}

	public List<Professor> getListaProfessor() {
		if(listaProfessor == null) {
			try {
				listaProfessor = professorService.carregarProfessoresCadastrados();
				return listaProfessor;
			} catch (ExcecaoGenerica e) {
				log.error("Erro ao recuperar a lista de professores cadastrados");
				return null;
			}
		} else {
			return listaProfessor;
		}
	}

	public void setListaProfessor(List<Professor> listaProfessor) {
		this.listaProfessor = listaProfessor;
	}

}
