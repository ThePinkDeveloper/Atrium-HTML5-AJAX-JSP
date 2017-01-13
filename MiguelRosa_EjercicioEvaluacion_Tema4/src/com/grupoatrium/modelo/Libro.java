package com.grupoatrium.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Libro implements Serializable {
	
	/*************************************************************************************
	 * ATRIBUTOS																		 *
	 *************************************************************************************/
	
	private Integer IDLibro;
	
	private String titulo;
	
	private Set<Autor> autores = new HashSet<Autor>();
	
	private Editorial editorial;
	
	private String isbn;
	
	private Integer publicacion;
	
	private Double precio;
	
	private String descripcion;
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 *
	 *************************************************************************************/
	
	public Libro() {}
	
	public Libro(String titulo, Set<Autor> autores, Editorial editorial, String isbn, int publicacion, double precio, String descripcion) {
		this.titulo = titulo;
		this.autores = autores;
		this.editorial = editorial;
		this.isbn = isbn;
		this.publicacion = publicacion;
		this.precio = precio;
		this.descripcion = descripcion;
	}
	
	/*************************************************************************************
	 * METODOS																			 
	 *************************************************************************************/
	
	// Método de sincronización de la colección autores
	
	public void addAutor(Autor a) {
		autores.add(a);
		a.getLibros().add(this);
	}
	
	public void addEditorial(Editorial e) {
		this.setEditorial(e);
		e.getLibros().add(this);
	}
	
	
	/*************************************************************************************
	 * GETTERS Y SETTERS																 
	 *************************************************************************************/

	public Integer getIDLibro() {
		return IDLibro;
	}

	public void setIDLibro(Integer iD) {
		IDLibro = iD;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Set<Autor> getAutores() {
		return autores;
	}

	public void setAutores(Set<Autor> autores) {
		this.autores = autores;
	}

	public Editorial getEditorial() {
		return editorial;
	}

	public void setEditorial(Editorial editorial) {
		this.editorial = editorial;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getPublicacion() {
		return publicacion;
	}

	public void setPublicacion(Integer publicacion) {
		this.publicacion = publicacion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/*************************************************************************************
	 * TOSTRING															 
	 *************************************************************************************/

	@Override
	public String toString() {
		return "Libro [ID=" + IDLibro + ", titulo=" + titulo + ", autores=" + autores + ", editorial=" + editorial
				+ ", isbn=" + isbn + ", publicacion=" + publicacion + ", precio=" + precio + ", descripcion="
				+ descripcion + "]";
	}
	

}
