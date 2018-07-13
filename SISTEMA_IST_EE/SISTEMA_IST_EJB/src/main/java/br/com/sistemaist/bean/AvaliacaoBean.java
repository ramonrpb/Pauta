package br.com.sistemaist.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sistemaist.dao.AvaliacaoDAO;
import br.com.sistemaist.dao.TurmaDAO;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabrica;
import br.com.sistemaist.daofabrica.fabrica.DAOFabricaImpl;
import br.com.sistemaist.entidades.Avaliacao;
import br.com.sistemaist.entidades.Turma;
import br.com.sistemaist.service.AvaliacaoService;

@Stateless
public class AvaliacaoBean implements AvaliacaoService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="sistema_ist")
	private EntityManager em;
	
	private DAOFabrica daoFabrica;
	
	@PostConstruct
	public void instanciarDaoFabrica() {
		daoFabrica = new DAOFabricaImpl(em);
	}
	
	public AvaliacaoDAO getAvaliacaoDAO() throws ExcecaoGenerica {
		return (AvaliacaoDAO) daoFabrica.getDAO(Avaliacao.class);
	}
	
	public TurmaDAO getTurmaDAO() throws ExcecaoGenerica {
		return (TurmaDAO) daoFabrica.getDAO(Turma.class);
	}

	@Override
	public void salvar(List<Avaliacao> listaAvaliacao) throws ExcecaoGenerica {
		for(Avaliacao a: listaAvaliacao) {
			Turma turma = getTurmaDAO().buscarPorId(a.getTurma().getId());
			if(a.getId() != null) {
				getAvaliacaoDAO().mesclar(a);
			} else {
				Avaliacao avaliacaoParaPersistir = new Avaliacao(a.getNome(), a.getPeso(), turma);
				getAvaliacaoDAO().salvarOuAlterar(avaliacaoParaPersistir);
			}
		}
	}
	
	@Override
	public Avaliacao salvar(Avaliacao a) throws ExcecaoGenerica {
		return getAvaliacaoDAO().salvar(a);
	}

	@Override
	public List<Avaliacao> carregarAvaliacoesPorTurmaSemPF(Long idTurma) throws ExcecaoGenerica {
		return getAvaliacaoDAO().carregarAvaliacoesPorTurmaSemPF(idTurma);
	}

	@Override
	public void excluirAvaliacoes(List<Long> idAvaliacoesExcluidas) throws ExcecaoGenerica {
		getAvaliacaoDAO().excluirAvaliacoes(idAvaliacoesExcluidas);
	}

}
