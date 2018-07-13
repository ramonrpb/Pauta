package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sistemaist.dao.AlunoAvaliacaoDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.AlunoAvaliacao;
import br.com.sistemaist.service.AlunoAvaliacaoService;

@Stateless
public class AlunoAvaliacaoBean implements Serializable, AlunoAvaliacaoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica(){
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	
	public AlunoAvaliacaoDAO getAlunoAvaliacaoDAO() throws ExcecaoGenerica {
		return (AlunoAvaliacaoDAO) daoFabrica.getDAO(AlunoAvaliacao.class);
	}
	
	@Override
	public List<AlunoAvaliacao> carregarAlunoAvaliacaoPorTurma(Long idTurma) throws ExcecaoGenerica {
		return getAlunoAvaliacaoDAO().buscarPorTurma(idTurma);
	}


	@Override
	public void salvarListaAlunoAvaliacao(List<AlunoAvaliacao> listaAlunoAvaliacao) throws ExcecaoGenerica {
		
		for(AlunoAvaliacao aa: listaAlunoAvaliacao) {
			if(aa.getId() != null) {
				getAlunoAvaliacaoDAO().mesclar(aa);
			} else {
				getAlunoAvaliacaoDAO().salvarOuAlterar(aa);
			}
		}
		
	}

	@Override
	public HashMap<Long, List<AlunoAvaliacao>> carregarAvaliacoesPorAlunoAnoLetivo(Long idAluno, Long idAnoLetivo) throws ExcecaoGenerica {
		List<AlunoAvaliacao> avaliacoes = getAlunoAvaliacaoDAO().carregarAvaliacoesPorAlunoAnoLetivo(idAluno, idAnoLetivo);
		
		HashMap<Long, List<AlunoAvaliacao>> lista = new HashMap<Long, List<AlunoAvaliacao>>();
		
		for(AlunoAvaliacao aa : avaliacoes){
			if(lista.containsKey(aa.getAvaliacao().getTurma().getDisciplina().getId())){
				List<AlunoAvaliacao> alunoAv = lista.get(aa.getAvaliacao().getTurma().getDisciplina().getId());
				alunoAv.add(aa);
				lista.put(aa.getAvaliacao().getTurma().getDisciplina().getId(), alunoAv);
			}else{
				List<AlunoAvaliacao> alunoAv = new ArrayList<AlunoAvaliacao>();
				alunoAv.add(aa);
				lista.put(aa.getAvaliacao().getTurma().getDisciplina().getId(), alunoAv);
			}
			
		}
		
		return lista;
	}


	@Override
	public void removerAvaliacoes(List<AlunoAvaliacao> listaAlunoAvaliacaoRemover) throws ExcecaoGenerica {
		for(AlunoAvaliacao aa: listaAlunoAvaliacaoRemover) {
			if(aa.getId() != null) {
				getAlunoAvaliacaoDAO().excluir(getAlunoAvaliacaoDAO().buscarPorId(aa.getId()));
			}
		}
		
	}
	
}
