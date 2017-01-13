package com.grupoatrium.persistencia;

import java.util.List;

import com.grupoatrium.modelo.Autor;
import com.grupoatrium.modelo.Editorial;
import com.grupoatrium.modelo.Libro;
import com.grupoatrium.modelo.LibroNoEncontradoException;

public interface ItfzLibrosDao {

	public boolean altaLibro(Libro libro);
	public boolean eliminarLibro(int id) throws LibroNoEncontradoException;
	public List<Libro> consultarTodos();
	public Libro consultarISBN(String isbn)throws LibroNoEncontradoException;
	public List<Libro> consultarTitulo(String titulo)throws LibroNoEncontradoException;
	public boolean modificarPrecio(String isbn, double precio)throws LibroNoEncontradoException;
	public List<Autor> consultarAutores();
	public List<Libro> consultarLibrosPorAutor(Autor autor);
	public List<Editorial> consultarEditoriales();
	public List<Libro> consultarLibrosPorEditorial(Editorial editorial);
	
}
