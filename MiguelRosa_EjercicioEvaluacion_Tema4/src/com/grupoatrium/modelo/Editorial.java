package com.grupoatrium.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Editorial implements Serializable {
	
	/*************************************************************************************
	 * ATRIBUTOS																		 
	 *************************************************************************************/
	
	private Integer IDEditorial;
	
	private String nombre;
	
	private Direccion direccion;
	
	private String nif;
	
	private Set<Libro> libros = new HashSet<Libro>();
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 
	 *************************************************************************************/
	
	public Editorial() {}
	
	public Editorial (String nombre, String nif, Direccion direccion) {
		this.nombre = nombre;
		this.nif = nif;
		this.direccion = direccion;
	}
	
	/*************************************************************************************
	 * METODOS																			 
	 *************************************************************************************/
	
	// Método de sincronización de la colección libros
	
	public void addLibro(Libro l) {
		libros.add(l);
		l.setEditorial(this);
	}
	
	/*************************************************************************************
	 * GETTERS Y SETTERS																 
	 *************************************************************************************/

	public Integer getIDEditorial() {
		return IDEditorial;
	}

	public void setIDEditorial(Integer iDEditorial) {
		IDEditorial = iDEditorial;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public Set<Libro> getLibros() {
		return libros;
	}

	public void setLibros(Set<Libro> libros) {
		this.libros = libros;
	}
	
	/*************************************************************************************
	 * TOSTRING															 
	 *************************************************************************************/

	@Override
	public String toString() {
		return "Editorial [nombre=" + nombre + ", direccion=" + direccion + ", nif=" + nif + ", libros=" + libros + "]";
	}
	
	

}
