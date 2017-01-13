package com.grupoatrium.modelo;

import java.io.Serializable;

public class Direccion implements Serializable{
	
	/*************************************************************************************
	 * ATRIBUTOS																		 
	 *************************************************************************************/
	
	private Integer IDDireccion;
	
	private String calle;
	
	private Integer numero;
	
	private String poblacion;
	
	private Integer cp;
	
	private String provincia;
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 
	 *************************************************************************************/
	
	public Direccion() {}
	
	public Direccion(String calle, int numero, String poblacion, int cp, String provincia) {
		this.calle = calle;
		this.numero = numero;
		this.poblacion = poblacion;
		this.cp = cp;
		this.provincia = provincia;
	}
	
	/*************************************************************************************
	 * METODOS																			 
	 *************************************************************************************/
	
	/*************************************************************************************
	 * GETTERS Y SETTERS																 
	 *************************************************************************************/
	
	public Integer getIDDireccion() {
		return IDDireccion;
	}

	public void setIDDireccion(Integer iDDireccion) {
		IDDireccion = iDDireccion;
	}
	
	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public Integer getCp() {
		return cp;
	}

	public void setCp(Integer cp) {
		this.cp = cp;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/*************************************************************************************
	 * TOSTRING															 
	 *************************************************************************************/

	@Override
	public String toString() {
		return "Direccion [IDDireccion=" + IDDireccion + ", calle=" + calle + ", numero=" + numero + ", poblacion="
				+ poblacion + ", cp=" + cp + ", provincia=" + provincia + "]";
	}
	
}
