package br.com.sistemaist.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.sistemaist.daofabrica.excecoes.ExcecaoGenerica;
import br.com.sistemaist.entidades.AlunoAula;
import br.com.sistemaist.entidades.AnoLetivo;
import br.com.sistemaist.entidades.Aula;
import br.com.sistemaist.vo.FrequenciaVO;
import br.com.sistemaist.vo.MesVO;

@Local
public interface AulaService {

	public void salvar(Aula aula, List<AlunoAula> listaAlunoAula) throws ExcecaoGenerica;
	public void alterar(Aula aula, List<AlunoAula> listaAlunoAula) throws ExcecaoGenerica;
	public List<Aula> buscarAulas() throws ExcecaoGenerica;
	public void excluir(Aula aula) throws ExcecaoGenerica;
	public List<Aula> buscarAulasPorIdTurma(Long id) throws ExcecaoGenerica;
	public Aula buscarAulaPorId(Long idAula) throws ExcecaoGenerica;
	public List<AlunoAula> buscarListaAlunoAulaPorIdAula(Long id) throws ExcecaoGenerica;
	public boolean validarTurmaData(Long id, Long idTurma, Date data) throws ExcecaoGenerica;
	public Aula carregarPauta(Aula aula, MesVO mesVO) throws ExcecaoGenerica;
	public Aula carregarFrequenciaPorPeriodo(Aula aula) throws ExcecaoGenerica;
	public FrequenciaVO carregarFrequenciaDoAlunoPorPeriodo(Long id, Long id2) throws ExcecaoGenerica;

}
