package br.com.sistemaist.managedbean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import br.com.sistemaist.util.Constante;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

@ManagedBean(name="mapMB")
@NoneScoped
public class MapMangedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private MapModel draggableModel = new DefaultMapModel();
	private MapModel miniMapModel = new DefaultMapModel();
	
	private Marker marker;

	private Double latitude;
	private Double longitude;
	private String cidade;
	private String estado;
	private String endereco;
	
	private Double latitudePesquisa;
	private Double longitudePesquisa;
	
	//Dados do Mapa
	private Integer zoom = Constante.ZOOM_MAP_DEFAULT;
	private Double latitudeCentroMap = Constante.LATITUDE_DEFAULT;
	private Double longitudeCentroMap = Constante.LONGITUDE_DEFAULT;
	
	//*******************************
	//pointSelect
	public void adicionarMarcadorPorClique(PointSelectEvent event){
		LatLng coordenada = event.getLatLng();
		adicionarMarcador(coordenada);
		latitudePesquisa = event.getLatLng().getLat();
		longitudePesquisa = event.getLatLng().getLng();
	}
	
	//pelo botão pesquisa Latitude e Longitude
	public void adicionarMarcadorPorLatLon(){
		LatLng coordenada = new LatLng(latitudePesquisa, longitudePesquisa);
		adicionarMarcador(coordenada);
	}

	private void adicionarMarcador(LatLng coordenada) {
		marker = new Marker(coordenada);
		
		draggableModel.getMarkers().clear();
		draggableModel.addOverlay(new Marker(coordenada, "Marcador"));
		
		for(Marker marker : draggableModel.getMarkers()) {
			marker.setDraggable(true);
		}
	}
	
	//markerDrag
	public void movimentarMarcador(MarkerDragEvent event) {
		setMarker(event.getMarker());
		latitudePesquisa = event.getMarker().getLatlng().getLat();
		longitudePesquisa = event.getMarker().getLatlng().getLng();
	}
	
	//stateChange
	public void trocaEstadoMapa(StateChangeEvent event){
		zoom = event.getZoomLevel();
		latitudeCentroMap = event.getCenter().getLat();
		longitudeCentroMap = event.getCenter().getLng();
	}
	
	public void cidadeEstado(LatLng coordenada){

		estado = null;
		cidade = null;
		endereco = null; 
		
//		System.getProperties().put("http.proxyHost", "192.168.79.10");
//		System.getProperties().put("http.proxyPort", "3128");
		
		com.google.code.geocoder.model.LatLng latLong = new com.google.code.geocoder.model.LatLng(new BigDecimal(coordenada.getLat()), new BigDecimal(coordenada.getLng())); 
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().getGeocoderRequest();
		geocoderRequest.setLocation(latLong);
	
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		
		if(null != geocoderResponse && null != geocoderResponse.getResults() && geocoderResponse.getResults().size() > 0){
			
			GeocoderResult geoResult = geocoderResponse.getResults().get(0);

			for(GeocoderAddressComponent gAC : geoResult.getAddressComponents()){
				
				if(null != gAC.getTypes().toString() && gAC.getTypes().toString().equals("[street_number]")){ //Endereço
						
				}else if(null != gAC.getTypes().toString() && gAC.getTypes().toString().equals("[route]")){ //Endereço
					setEndereco(gAC.getLongName());
				}else if(null != gAC.getTypes().toString() && gAC.getTypes().toString().equals("[sublocality, political]")){ //Bairro
					
				}else if(null != gAC.getTypes().toString() && 
						(gAC.getTypes().toString().equals("[locality, political]")) || gAC.getTypes().toString().equals("[administrative_area_level_2, political]")){ // Cidade
					cidade = gAC.getLongName();
				}else if(null != gAC.getTypes().toString() && gAC.getTypes().toString().equals("[administrative_area_level_1, political]")){ // Estado
					estado = gAC.getShortName();
				}else if(null != gAC.getTypes().toString() && gAC.getTypes().toString().equals("[country, political]")){ // Pais
					
				}else if(null != gAC.getTypes().toString() && gAC.getTypes().toString().equals("[postal_code]")){ // CEP
					
				}
			}
			
		}
		
	}
	
	/**
	 * Confirmar marcador, no caso de modal
	 */
	public void confirmarMarcador(){
		if(null != marker){
			latitude = marker.getLatlng().getLat();
			longitude = marker.getLatlng().getLng();
		}else{
			latitude = null;
			longitude = null;
		}
		
		miniMapModel = draggableModel;
		for(Marker marker : miniMapModel.getMarkers()) {
			marker.setDraggable(false);
		}
		
		cidadeEstado(marker.getLatlng());
	}
	
	/**
	 * Limpa todos os marcadores e reseta o map, fica como abriu inicialmente
	 */
	public void limparMarcador(){
		draggableModel = new DefaultMapModel();
		zoom = Constante.ZOOM_MAP_DEFAULT;
		latitudeCentroMap = Constante.LATITUDE_DEFAULT;
		longitudeCentroMap = Constante.LONGITUDE_DEFAULT;
		marker = null;
		latitudePesquisa = null;
		longitudePesquisa = null;
	}
	
	/**
	 * Coloca o mapa com o último marcador confirmado
	 */
	public void cancelarMarcador(){
		draggableModel = new DefaultMapModel();
		
		if(latitude != null && longitude != null){
			LatLng coordenada = new LatLng(latitude, longitude);
			marker = new Marker(coordenada);
			draggableModel.addOverlay(new Marker(coordenada, "Marcador"));
			
			for(Marker marker : draggableModel.getMarkers()) {
				marker.setDraggable(true);
			}
		}
		zoom = Constante.ZOOM_MAP_DEFAULT;
		if(null != marker){
			latitudePesquisa = marker.getLatlng().getLat();
			longitudePesquisa = marker.getLatlng().getLng();
		}
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public MapModel getDraggableModel() {
		return draggableModel;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public Double getLatitudeCentroMap() {
		return latitudeCentroMap;
	}

	public void setLatitudeCentroMap(Double latitudeCentroMap) {
		this.latitudeCentroMap = latitudeCentroMap;
	}

	public Double getLongitudeCentroMap() {
		return longitudeCentroMap;
	}

	public void setLongitudeCentroMap(Double longitudeCentroMap) {
		this.longitudeCentroMap = longitudeCentroMap;
	}

	public Double getLatitudePesquisa() {
		return latitudePesquisa;
	}

	public void setLatitudePesquisa(Double latitudePesquisa) {
		this.latitudePesquisa = latitudePesquisa;
	}

	public Double getLongitudePesquisa() {
		return longitudePesquisa;
	}

	public void setLongitudePesquisa(Double longitudePesquisa) {
		this.longitudePesquisa = longitudePesquisa;
	}

	public MapModel getMiniMapModel() {
		return miniMapModel;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	
}
					