package com.grupoatrium.persistencia;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.grupoatrium.modelo.Autor;
import com.grupoatrium.modelo.Editorial;
import com.grupoatrium.modelo.Libro;
import com.grupoatrium.modelo.LibroNoEncontradoException;


/*****************************************************************************************
 * Nombre de Clase : LibrosDAO															 *
 * Autor: Miguel Rosa																	 *
 * Fecha: 09 - Noviembre - 2016															 *
 * Versión: 1.0																			 *
 * Descripción: Esta clase se encarga de la capa de conexión con la base de datos.		 *
 * Los datos de conexión se definen directamente en la sección ATRIBUTOS. 				 *
 * Esta clase está definida únicamente para la conexión a una base de datos MySQL. 		 *
 *****************************************************************************************/

public class LibrosDAO implements ItfzLibrosDao{
	
	/*************************************************************************************
	 * ATRIBUTOS																		 *
	 *************************************************************************************/
		
	// Creamos la SessionFactory
	private SessionFactory sf = new Configuration().configure().buildSessionFactory();
			
	// De la SessionFactory obtenemos la Session 
	private Session sesion = sf.openSession();
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 *
	 *************************************************************************************/
	
	public LibrosDAO() {
				
	}

	/*************************************************************************************
	 * GETTERS Y SETTERS															
	 *************************************************************************************/
	
	public SessionFactory getSf() {
		return sf;
	}

	public void setSf(SessionFactory sf) {
		this.sf = sf;
	}

	public Session getSesion() {
		return sesion;
	}

	public void setSesion(Session sesion) {
		this.sesion = sesion;
	}

	/*************************************************************************************
	 * METODOS 																			 *
	 *************************************************************************************/
	
	/* Nombre: mensajeError()
	 * Argumentos: String
	 * Devuelve: void
	 * Descripción: Se detecta durante la realización del programa que las
	 * instrucciones de este método se repiten considerablemente.
	 * Se crea este método para simplificar y clarificar el código. El String que
	 * se pasa como parámetro se muestra al usuario para informarle de que se
	 * ha producido un error.
	 */
	
	public String mensajeError(String str, Exception e) {
		
		// Se declara un String que almacenará los datos introducidos por el
		// usuario
		String cadena = null;
		
		// Imprimimos el registro de error
		e.printStackTrace();
		
		System.out.println();
		System.out.println(str);
			
		// Devolvemos el dato introducido por el usuario
		return cadena;	
	}
	
	/*************************************************************************************
	 * METODOS SOBREESCRITOS INTERFAZ ItfzLibrosDao										 *
	 *************************************************************************************/
	
	/* Nombre: altaLibro()
	 * Argumentos: Libro
	 * Devuelve: Boolean
	 * Descripción: Este método se encarga de gestionar todos los procesos necesarios
	 * para dar de alta un registro en la base de datos.
	 */

	@Override
	public boolean altaLibro(Libro libro) {
		
		//Direccion direccion = libro.getEditorial().getDireccion();
		//Editorial editorial = libro.getEditorial();
		
		// Variable de retorno
		boolean salida = true;
		
		try {
			
			// Persistimos el objeto Libro
			sesion.save(libro);

			
		// Si ocurre un error en el proceso capturamos la Excepción 
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el registro
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR INTRODUCIR LOS DATOS EN LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha realizado el registro del libro en la base de datos devolvemos
			// false.
			salida = false;
			
		}
		
		return salida;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: eliminarLibro()
	 * Argumentos: Integer
	 * Devuelve: Boolean
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para eliminar de la base de datos un registro, con el id que coincida con el 
	 * Integer que se le pasa como parámetro al método.
	 * En caso de que el Integer que se pasa por parámetro no coincida con el ID de ningún
	 * registro en la base de datos se lanzará la excepción LibroNoEncontradoException.
	 */

	@Override
	public boolean eliminarLibro(int id) throws LibroNoEncontradoException {
		
		// Variable de retorno
		boolean salida = true;

		try {
			
			// Comprobamos que el id que pasamos como argumento existe en la BDD
			Query miQuery = sesion.createQuery("SELECT l FROM Libro l WHERE l.IDLibro = " + id);
			List<Libro> milista = miQuery.list();
			
			// Si el tamaño del List devuelto es 0 lanzamos la excepcion 
			// LibroNoEncontradoException
			if (milista.size() == 0) {
				throw new LibroNoEncontradoException("ID");
			} 
			// si no, borramos el libro
			else {
				Query otraQuery = sesion.createQuery("DELETE Libro l WHERE l.IDLibro = " + id);
				otraQuery.executeUpdate();
			}
			
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR ELIMINAR EL REGISTRO DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha eliminado el registro del libro en la base de datos devolvemos
			// false.
			salida = false;
		}
		
		return salida;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarTodos()
	 * Argumentos: Ninguno
	 * Devuelve: List
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para recoger de la base de datos, todos los datos, de todos los registros.
	 */

	@Override
	public List<Libro> consultarTodos() {
		
		// Creamos una colección ArrayList
		List<Libro> misLibros = new ArrayList<Libro>();
		
		try {
			
			// Creamos un Query con la consulta
			Query miQuery = sesion.createQuery("SELECT l FROM Libro l");
			
			// Creamos una lista a partir del Query anterior
			misLibros = miQuery.list();
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos y el ArrayList estará vacío
			// devolvemos null
			misLibros = null;

		}	
		
		return misLibros;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarISBN()
	 * Argumentos: String
	 * Devuelve: Libro
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para filtar de la base de datos, un registro con el isbn que coincida con el String 
	 * que se le pasa como parámetro al método, y recoger sus datos.
	 * En caso de que el String no coincida con el ISBN de ningún registro en la base de 
	 * datos se lanzará la excepción LibroNoEncontradoException.
	 */

	@Override
	public Libro consultarISBN(String isbn) throws LibroNoEncontradoException {
		
		// Se declara la variable libro que devuelve la función
		Libro libro;
		
		try {
			
			// Comprobamos que el isbn que pasamos como argumento existe en la BDD
			Query miQuery = sesion.createQuery("SELECT l FROM Libro l WHERE l.isbn = '" + isbn + "'");
			List<Libro> miLista = miQuery.list();
			
			// Si el tamaño del List devuelto es 0 lanzamos la excepcion 
			// LibroNoEncontradoException
			if (miLista.size() == 0) {
				throw new LibroNoEncontradoException("ISBN");
			} 
			// si no, devolvemos el objetio Libro devuelto
			else {
				libro = miLista.get(0);
			}
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos no se ha podido crear la
			// instancia de la clase Libro, devolvemos null
			libro = null;

		}	

		return libro;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarTitulo()
	 * Argumentos: String
	 * Devuelve: List
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para filtar de la base de datos los registros que contengan en su título el String
	 * que se le pasa como parámetro al método y recoger todos sus datos.
	 * En caso de que el String no forme parte del TITULO de ninguno de los registros 
	 * se lanzará la excepción LibroNoEncontradoException.
	 */

	@Override
	public List<Libro> consultarTitulo(String titulo) throws LibroNoEncontradoException {

		// Creamos la colección ArrayList que devuelve la función
		List<Libro> misLibros = new ArrayList<Libro>();
		
		try {
			
			// Creamos un Query con la consulta
			Query miQuery = sesion.createQuery("FROM Libro l WHERE l.titulo LIKE '%" + titulo + "%'");
			
			// Creamos una lista a partir del Query anterior
			misLibros = miQuery.list();
			
			// Si el tamaño del List devuelto es 0 lanzamos la excepcion 
			// LibroNoEncontradoException
			if (misLibros.size() == 0) {
				throw new LibroNoEncontradoException("TÍTULO");
			} 
			
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos no se ha podido crear el
			// List de objetos Libro, devolvemos null
			misLibros = null;			

		}	
		
		return misLibros;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: modificarPrecio()
	 * Argumentos: String, Double
	 * Devuelve: Boolean
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para filtar de la base de datos el registro cuyo ISBN coincida con el String que
	 * se pasa como parámetro y modificar el precio que aparece en el registro
	 * por el Double que se pasa como parámetro.
	 * En caso de que el String no coincida con el ISBN de ningún registro en la base de 
	 * datos se lanzará la excepción LibroNoEncontradoException.
	 */

	@Override
	public boolean modificarPrecio(String isbn, double precio) throws LibroNoEncontradoException {

		// Variable de retorno
		boolean salida = true;
		
		try {
			
			// Definimos la variable Libro
			Libro libro;
			
			// Comprobamos que el isbn se encuentra en la BDD
			Query miQuery = sesion.createQuery("FROM Libro l WHERE l.isbn = '" + isbn + "'");
			List<Libro> miLibro = miQuery.list();
			
			// Si el tamaño del List devuelto es 0 lanzamos la excepcion
			// LibroNoEncontradoException
			if (miLibro.size() == 0) {
				throw new LibroNoEncontradoException("ISBN");
			}
			// Si no, recuperamos el libro devuelto
			else {
				libro = miLibro.get(0);
			}
			
			// Cambiamos el valor del atributo precio al nuevo precio
			libro.setPrecio(precio);
			
			// Actualizamos los atributos de libro en la BDD
			sesion.update(libro);
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR MODIFICAR LOS DATOS EN LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha realizado el registro del libro en la base de datos devolvemos
			// false.
			salida = false;
			
		}
		
		return salida;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarAutores()
	 * Argumentos: Ninguno
	 * Devuelve: List
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para recoger de la base de datos, los datos de todos los registros de
	 * autores.
	 */

	public List<Autor> consultarAutores() {
		
		// Creamos una colección ArrayList
		List<Autor> misAutores;
		
		try {
			
			// Creamos un Query con la consulta
			Query miQuery = sesion.createQuery("SELECT a FROM Autor a");
			
			// Creamos una lista a partir del Query anterior
			misAutores = miQuery.list();
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos y el ArrayList estará vacío
			// devolvemos null
			misAutores = null;

		}	
		
		return misAutores;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarLibrosPorAutor()
	 * Argumentos: Ninguno
	 * Devuelve: List
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para recoger de la base de datos los libros escritos por el objeto de la
	 * clase Autor que se pasa por parámetro
	 */

	public List<Libro> consultarLibrosPorAutor(Autor autor) {
		
		// Creamos una colección ArrayList de tipo Libro 
		List<Libro> libros;
		
		try {
			
			// Creamos un Query con la consulta
			Query miQuery = sesion.createQuery("FROM Libro l WHERE :autor MEMBER OF l.autores");
			miQuery.setParameter("autor", autor);
			
			// Creamos una lista a partir del Query anterior
			libros = miQuery.list();
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos y el ArrayList estará vacío
			// devolvemos null
			libros = null;

		}	
		// Si todo sale bien devolvemos el ArrayList de Libros
		return libros;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarEditoriales()
	 * Argumentos: Ninguno
	 * Devuelve: List
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para recoger de la base de datos, los datos de todos los registros de
	 * editoriales.
	 */

	public List<Editorial> consultarEditoriales() {
		
		// Creamos una colección ArrayList
		List<Editorial> misEditoriales;
		
		try {
			
			// Creamos un Query con la consulta
			Query miQuery = sesion.createQuery("SELECT e FROM Editorial e");
			
			// Creamos una lista a partir del Query anterior
			misEditoriales = miQuery.list();
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos y el ArrayList estará vacío
			// devolvemos null
			misEditoriales = null;

		}	
		
		return misEditoriales;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarLibrosPorEditorial()
	 * Argumentos: Editorial
	 * Devuelve: List
	 * Descripción: Este método se encarga de gestionar los procesos necesarios
	 * para recoger de la base de datos los libros pertenecientes al objeto 
	 * de la clase Editorial que se pasa por parámetro
	 */
	
	public List<Libro> consultarLibrosPorEditorial(Editorial editorial) {
		
		// Creamos una colección ArrayList de tipo Libro 
		List<Libro> libros;
		
		try {
			
			// Creamos un Query con la consulta
			Query miQuery = sesion.createQuery("FROM Libro l WHERE :editorial MEMBER OF l.editorial");
			miQuery.setParameter("editorial", editorial);
			
			// Creamos una lista a partir del Query anterior
			libros = miQuery.list();
		
		// Si ocurre un error en el proceso capturamos la Excepción
		} catch (Exception e) {
			
			// Informamos al usuario de que se ha producido un error durante el proceso
			mensajeError("\nSE HA PRODUCIDO UN ERROR AL INTENTAR LEER LOS REGISTROS DE LA BASE DE DATOS.\nPulse INTRO para CONTINUAR.", e);
			
			// Como no se ha podido leer la base de datos y el ArrayList estará vacío
			// devolvemos null
			libros = null;

		}	
		// Si todo sale bien devolvemos el ArrayList de Libros
		return libros;
		
	}
}
