package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.SelectItem;

import br.com.sistemaist.enumerator.PerfilEnum;
import br.com.sistemaist.util.Constante;
import br.com.sistemaist.util.Utilitario;

@ManagedBean(name="utilMB")
@NoneScoped
public class UtilManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private final Double latitudeCentroMap = Constante.LATITUDE_DEFAULT;
	private final Double longitudeCentroMap = Constante.LONGITUDE_DEFAULT;
	
	private PerfilEnum perfilAD = PerfilEnum.AD;
	private PerfilEnum perfilPR = PerfilEnum.PR;
	private PerfilEnum perfilAL = PerfilEnum.AL;
	
	private final SelectItem selectItemSelecione = new SelectItem(null,"--Selecione--");
	
	private Double mediaAprovacao = 6.0;
	private String nomeProvaFinal = Constante.NOME_PROVA_FINAL;
	
	private Double frequenciaAprovacao = 75.0;
		
	public Date hoje(){
		return new Date();
	}
	
	public String formataDataHoje(){
		SimpleDateFormat df = new SimpleDateFormat(Constante.FORMATO_DATA);
		return df.format(new Date());
	}
	
	public String formataData(Date data){
		if(data != null){
			SimpleDateFormat df = new SimpleDateFormat(Constante.FORMATO_DATA);
			return df.format(data);
		}
		return null;
	}
	
	/**
	 * Se nula não encerrou
	 * @param data
	 * @return
	 */
	public boolean dataMenorIgualHoje(Date data){
		if(null != data){
			return data.after(Utilitario.zerarHorario(new Date()));
		}
		return true;
	}
	
	public Double getLatitudeCentroMap(){
		return latitudeCentroMap;
	}
	public Double getLongitudeCentroMap(){
		return longitudeCentroMap;
	}
	public SelectItem getSelectItemSelecione(){
		return selectItemSelecione;
	}

	public PerfilEnum getPerfilAD() {
		return perfilAD;
	}

	public void setPerfilAD(PerfilEnum perfilAD) {
		this.perfilAD = perfilAD;
	}

	public PerfilEnum getPerfilPR() {
		return perfilPR;
	}

	public void setPerfilPR(PerfilEnum perfilPR) {
		this.perfilPR = perfilPR;
	}

	public PerfilEnum getPerfilAL() {
		return perfilAL;
	}

	public void setPerfilAL(PerfilEnum perfilAL) {
		this.perfilAL = perfilAL;
	}

	public String getNomeProvaFinal() {
		return nomeProvaFinal;
	}

	public void setNomeProvaFinal(String nomeProvaFinal) {
		this.nomeProvaFinal = nomeProvaFinal;
	}

	public Double getMediaAprovacao() {
		return mediaAprovacao;
	}

	public void setMediaAprovacao(Double mediaAprovacao) {
		this.mediaAprovacao = mediaAprovacao;
	}

	public Double getFrequenciaAprovacao() {
		return frequenciaAprovacao;
	}

	public void setFrequenciaAprovacao(Double frequenciaAprovacao) {
		this.frequenciaAprovacao = frequenciaAprovacao;
	}

	

}
