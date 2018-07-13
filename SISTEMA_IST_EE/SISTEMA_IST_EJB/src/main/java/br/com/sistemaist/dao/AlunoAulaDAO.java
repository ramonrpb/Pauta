package br.com.sistemaist.dao;

import java.util.List;

import br.com.sistemaist.daofabrica.dao.DAOGenerico;
import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.vo.MesVO;

public interface AlunoAulaDAO extends DAOGenerico<AlunoAula, Long>{

	public List<AlunoAula> buscarPorIdAula(Long id);

	public  List<AlunoAula> buscarPorIdTurmaMes(Long idTurma, MesVO mesVO);

	public Long buscarTotalDeAulasPorTurma(Long idTurma, MesVO mesVO);

	public Long buscarTotalDeAulasPorTurma(Long idTurma);
	
	public List<AlunoAula> buscarPorIdTurma(Long idTurma);

	public List<AlunoAula> buscarPorIdTurmaIdAluno(Long id, Long idAluno);

	public Long contarAulaPorAlunoETurma(Long idAluno, Long idTurma) throws ExcecaoGenerica;

}
