package com.grupoatrium.negocio;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Transaction;

import com.grupoatrium.modelo.Autor;
import com.grupoatrium.modelo.Direccion;
import com.grupoatrium.modelo.Editorial;
import com.grupoatrium.modelo.Libro;
import com.grupoatrium.modelo.LibroNoEncontradoException;
import com.grupoatrium.persistencia.LibrosDAO;

/*****************************************************************************************
 * Nombre de Clase : GestionLibreria													 *
 * Autor: Miguel Rosa																	 *
 * Fecha: 07 - Noviembre - 2016															 *
 * Versi�n: 1.0																			 *
 * Descripci�n: Se escriben unas breves instrucciones por consola para que el			 *
 * usuario seleccione:																	 *
 * 																						 *
 * 		1 Dar de ALTA un libro en la Base de Datos.										 *
 *		2 ELIMINAR un libro de la Base de Datos.										 *
 *		3 Consultar TODOS los libros de la Base de Datos (por defecto).					 *
 *		4 Consultar libro por ISBN.														 *
 *		5 Consultar libro por TITULO.													 *
 *		6 Modificar PRECIO de un libro.													 *
 *		7 SALIR																			 *
 * 																						 *
 * Si la opci�n introducida por el usuario no corresponde a ninguna de las				 *
 * anteriores se repetir� el mismo mensaje de selecci�n hasta que la opci�n 			 *
 * introducida coincida.																 *
 * 																						 *
 * En funci�n de la selecci�n del usuario, se lanzar� el m�todo correspondiente			 *
 * as� como nuevas instrucciones por consola solicitando los datos que fueran			 *
 * necesarios.																			 *
 *****************************************************************************************/

public class GestionLibreria implements ItfzGestionLibreria{
	
	/*************************************************************************************
	 * ATRIBUTOS																		 *
	 *************************************************************************************/
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 *
	 *************************************************************************************/
	public GestionLibreria() {
			
	}
	
	/*************************************************************************************
	 * METODOS																			 *
	 *************************************************************************************/
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: informacionYEspera()
	 * Argumentos: String
	 * Devuelve: void
	 * Descripci�n: Se detecta durante la realizaci�n del programa que las
	 * instrucciones de este m�todo se repiten considerablemente.
	 * Se crea este m�todo para simplificar y clarificar el c�digo. El String que
	 * se pasa como par�metro se muestra al usuario para informarle de alg�n evento 
	 * o solicitarle informaci�n.
	 */
	
	public String informacionYEspera(String str) {
		
		// Declaramos una variable String que almacenar� el dato del usuario
		String cadena;
		
		// Se informa al usuario o se le solicita un dato.
		System.out.println();
		System.out.println(str);
		
		// Se devuelven los datos escritos por el usuario.
		Scanner sc = new Scanner(System.in);
		cadena = sc.nextLine();
		
		// Devolvemos el dato del usuario
		return cadena;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: crearLibroUsuario()
	 * Argumentos: Ninguno
	 * Devuelve: Libro
	 * Descripci�n: Este m�todo se encarga de la solicitud al usuario de los datos
	 * necesarios para crear un objeto de la clase Libro.
	 */
	
	public Libro crearLibroUsuario() {
		
		// Se declaran las variables que almacenar�n los datos asignando valores por defecto.
		
		String temporal = "";			// Usamos esta variable para almacenar temporalmente los
										// datos introducidos por el usuario que requieran ser
										// convertidos a otro tipo, como en el caso de las
										// variables publicacion y precio.
		
		boolean error = false; 			// Esta es la variable que controla la salida de los 
										// bucles do/while para la conversi�n de datos.
		
		Set<Autor> autores = null; 		// Nombre del autor
		String descripcion = ""; 		// Sinopsis del Libro
		Editorial editorial = null;		// Editorial que lo ha publicado
		String isbn = "";				// C�digo ISBN
		double precio = 0.0;			// Precio en euros
		int publicacion = 0;			// A�o de publicaci�n
		String titulo = "";				// T�tulo del libro
		
		// Se solicitan los datos al usuario.
		
		// titulo
		titulo = informacionYEspera("INTRODUZCA EL T�TULO DEL LIBRO");
		
		// autores
		autores = crearAutores();
		
		// editorial
		editorial = crearEditorial();
		
		// isbn
		isbn = informacionYEspera("INTRODUZCA EL ISBN DEL LIBRO");
		
		// publicacion. Al tratarse publicacion de una variable de tipo int hay que convertir
		// el dato del usuario de String a Integer, como este proceso puede lanzar una
		// excepci�n, la capturamos y solicitamos al usuario que repita el proceso hasta que
		// que el dato introducido sea correcto.
			
		do {
			error = false;
			try {
				temporal = informacionYEspera("INTRODUZCA EL A�O DE PUBLICACION DEL LIBRO");
				publicacion = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO A�O. Introduzca un A�O correcto.");
				error = true;
			}
		} while (error);
		
		// precio. Al tratarse precio de una variable de tipo double hay que convertir
		// el dato del usuario de String a Double, como este proceso puede lanzar una
		// excepci�n, la capturamos y solicitamos al usuario que repita el proceso hasta que
		// que el dato introducido sea correcto.
		
		do {
			error = false;
			try {
				temporal = informacionYEspera("INTRODUZCA EL PRECIO DEL LIBRO");
				precio = Double.parseDouble(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO PRECIO. Introduzca un PRECIO correcto.");
				error = true;
			}
		} while (error);
	
		// descripcion		
		descripcion = informacionYEspera("INTRODUZCA UNA DESCRIPCION DEL LIBRO");
		
		Libro miLibro = new Libro(titulo, autores, editorial, isbn, publicacion, precio, descripcion);
		
		return miLibro;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: crearAutores()
	 * Argumentos: Ninguno
	 * Devuelve: Set<Autor>
	 * Descripci�n: Este m�todo se encarga de la solicitud al usuario de los datos
	 * necesarios para crear un conjunto (Set) de la clase Autor para el libro que
	 * se est� creando.
	 */
	
	public Set<Autor> crearAutores() {
		boolean error;
		String temporal;
		Set<Autor> autores = new HashSet<Autor>();
		
		
		int numeroAutores = 0;
		do {
			error = false;
			try {
				temporal = informacionYEspera("�CUANTOS AUTORES HAN INTERVENIDO EN EL LIBRO?\n");
				numeroAutores = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO N�MERO ENTERO. Introduzca un N�MERO correcto.");
				error = true;
			}
		} while (error);
		
		for (int n = 0; n < numeroAutores; n++) {
			String nombre = informacionYEspera("�Nombre del AUTOR " + (n + 1) + "?\n");
			String nacionalidad = informacionYEspera("�Nacionalidad del AUTOR " + (n + 1) + "?\n");
			String comentarios = informacionYEspera("�Alg�n comentario relevante del AUTOR " + (n + 1) + "?\n");
			Autor autor = new Autor(nombre, nacionalidad, comentarios);
			autores.add(autor);
		}
		return autores;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: crearEditorial()
	 * Argumentos: Ninguno
	 * Devuelve: Editorial
	 * Descripci�n: Este m�todo se encarga de la solicitud al usuario de los datos
	 * necesarios para crear un objeto de la clase Editorial para el libro que
	 * se est� creando.
	 */
	
	public Editorial crearEditorial() {
		boolean error;
		String temporal;
		
		String nombre = informacionYEspera("�NOMBRE DE LA EDITORIAL?\n");
		
		String nif = informacionYEspera("�NIF DE LA EDITORIAL?\n");
		
		String nombreCalle = informacionYEspera("�NOMBRE DE LA CALLE?\n");

		int numeroCalle = 0;

		
		do {
			error = false;
			try {
				temporal = informacionYEspera("�N�MERO?\n");
				numeroCalle = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO N�MERO ENTERO. Introduzca un N�MERO correcto.");
				error = true;
			}
		} while (error);
		
		String poblacion = informacionYEspera("�POBLACI�N?\n");
		
		int cp = 0;
		
		do {
			error = false;
			try {
				temporal = informacionYEspera("�C�DIGO POSTAL?\n");
				cp = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO N�MERO ENTERO. Introduzca un N�MERO correcto.");
				error = true;
			}
		} while (error);
		
		String provincia = informacionYEspera("�PROVINCIA?\n");
		
		Direccion direccion = new Direccion(nombreCalle, numeroCalle, poblacion, cp, provincia);
		
		return new Editorial(nombre, nif, direccion);
	}
	
	
	
	/*************************************************************************************
	 * METODOS SOBREESCRITOS INTERFAZ ItfzGestionLibreria								 *
	 *************************************************************************************/
	
	/* Nombre: altaLibro()
	 * Argumentos: Libro
	 * Devuelve: Boolean
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * altaLibro() devolviendo su resultado e independizando la clase GestionLibreria de
	 * la conexi�n a la base de datos
	 * El par�metro que recibe este m�todo es el mismo que se pasa al m�todo hom�nimo de
	 * la clase LibrosDAO.
	 */
	
	@Override
	public boolean altaLibro(Libro libro) {
		boolean salida;
		LibrosDAO conexionBD = new LibrosDAO();
		
		Transaction tx = conexionBD.getSesion().getTransaction();
		tx.begin();
		salida = conexionBD.altaLibro(libro);
		tx.commit();
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return salida;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: eliminarLibro()
	 * Argumentos: Integer
	 * Devuelve: Boolean
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * eliminarLibro() devolviendo su resultado e independizando la clase GestionLibreria de
	 * la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo es el mismo que se pasa al m�todo hom�nimo de
	 * la clase LibrosDAO. 
	 */

	@Override
	public boolean eliminarLibro(int id) {
		boolean salida;
		LibrosDAO conexionBD = new LibrosDAO();
		
		try {
			Transaction tx = conexionBD.getSesion().getTransaction();
			tx.begin();
			salida = conexionBD.eliminarLibro(id);
			tx.commit();	
		} catch (LibroNoEncontradoException e) {
			e.mensajeConsola("ID");
			salida = false;
		}
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return salida;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarTodos()
	 * Argumentos: Ninguno
	 * Devuelve: List
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarTodos() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo es el mismo que se pasa al m�todo hom�nimo de
	 * la clase LibrosDAO. 
	 */

	@Override
	public List<Libro> consultarTodos() {
		
		List<Libro> libros;
		LibrosDAO conexionBD = new LibrosDAO();
		
		libros = conexionBD.consultarTodos();
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return libros;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarISBN()
	 * Argumentos: String
	 * Devuelve: Libro
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarISBN() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo es el mismo que se pasa al m�todo hom�nimo de
	 * la clase LibrosDAO. 
	 */

	@Override
	public Libro consultarISBN(String isbn) {
		
		Libro libro;
		LibrosDAO conexionBD = new LibrosDAO();
		
		try {
			libro = conexionBD.consultarISBN(isbn);
		} catch (LibroNoEncontradoException e) {
			e.mensajeConsola("ISBN");
			libro = null;
		}
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return libro;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarTitulo()
	 * Argumentos: String
	 * Devuelve: List
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarTitulo() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo es el mismo que se pasa al m�todo hom�nimo de
	 * la clase LibrosDAO. 
	 */

	@Override
	public List<Libro> consultarTitulo(String titulo) {
		
		List<Libro> libros;
		LibrosDAO conexionBD = new LibrosDAO();
		
		try {
			libros = conexionBD.consultarTitulo(titulo);
		} catch (LibroNoEncontradoException e) {
			e.mensajeConsola("T�TULO");
			libros = null;
		}
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return libros;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: modificarPrecio()
	 * Argumentos: String, Double
	 * Devuelve: Boolean
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * modificarPrecio() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexi�n a la base de datos.
	 * Los par�metros que recibe este m�todo son los mismos que se pasan al m�todo hom�nimo
	 * de la clase LibrosDAO. 
	 */

	@Override
	public boolean modificarPrecio(String isbn, double precio) {
		
		boolean salida;
		LibrosDAO conexionBD = new LibrosDAO();
		
		try {
			Transaction tx = conexionBD.getSesion().getTransaction();
			tx.begin();
			salida = conexionBD.modificarPrecio(isbn, precio);
			tx.commit();
		} catch (LibroNoEncontradoException e) {
			e.mensajeConsola("ISBN");
			salida = false;
		}
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return salida;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarAutores()
	 * Argumentos: Ninguno
	 * Devuelve: List<Autor>
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarAutores() devolviendo su resultado e independizando la clase 
	 * GestionLibreria de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo son los mismos que se pasan al m�todo hom�nimo
	 * de la clase LibrosDAO. 
	 */
	
	@Override
	public List<Autor> consultarAutores() {
		
		List<Autor> autores;
		LibrosDAO conexionBD = new LibrosDAO();
		
		autores = conexionBD.consultarAutores();
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return autores;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarLibrosPorAutor()
	 * Argumentos: Autor
	 * Devuelve: List<Libro>
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarLibrosPorAutor() devolviendo su resultado e independizando la 
	 * clase GestionLibreria de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo son los mismos que se pasan al m�todo 
	 * hom�nimo de la clase LibrosDAO. 
	 */
	
	@Override
	public List<Libro> consultarLibrosPorAutor(Autor autor) {
		
		List<Libro> libros;
		LibrosDAO conexionBD = new LibrosDAO();
		
		libros = conexionBD.consultarLibrosPorAutor(autor);
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return libros;
	}

	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarEditoriales()
	 * Argumentos: Ninguno
	 * Devuelve: List<Editorial>
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarEditoriales() devolviendo su resultado e independizando la clase 
	 * GestionLibreria de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo son los mismos que se pasan al m�todo hom�nimo
	 * de la clase LibrosDAO. 
	 */
	
	@Override
	public List<Editorial> consultarEditoriales() {
		
		List<Editorial> editoriales;
		LibrosDAO conexionBD = new LibrosDAO();
		
		editoriales = conexionBD.consultarEditoriales();
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return editoriales;
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: consultarLibrosPorEditorial()
	 * Argumentos: Autor
	 * Devuelve: List<Libro>
	 * Descripci�n: Crea una instancia de la clase LibrosDAO y lanza su m�todo
	 * consultarLibrosPorEditorial() devolviendo su resultado e independizando la 
	 * clase GestionLibreria de la conexi�n a la base de datos.
	 * El par�metro que recibe este m�todo son los mismos que se pasan al m�todo 
	 * hom�nimo de la clase LibrosDAO. 
	 */
	
	@Override
	public List<Libro> consultarLibrosPorEditorial(Editorial editorial) {
		
		List<Libro> libros;
		LibrosDAO conexionBD = new LibrosDAO();
		
		libros = conexionBD.consultarLibrosPorEditorial(editorial);
		
		conexionBD.getSesion().close();
		conexionBD.getSf().close();
		
		return libros;
	}
}

/*************************************************************************************
 * METODOS INNECESARIOS																 *
 *************************************************************************************/

/* Nombre: formateadoISBN()
 * Argumentos: String
 * Devuelve: String
 * Descripci�n: Ya que el ISBN es un n�mero complejo que en ocasiones viene divido
 * por diferentes bloques num�ricos separados por guiones o espacios, este m�todo
 * elimina los guines y espacios qued�ndose con un �nico bloque num�rico.
 * Este m�todo est� pensado �nicamente para prop�sitos de b�squeda y comparaci�n,
 * por lo que los datos introducidos en la Base de Datos deben intentar pasarse 
 * con el formato original de guiones o espacios.
 * Adem�s, todos aquellos caracteres que no sean num�ricos ser�n eliminados. 
 */

 /*

public String formateadoISBN(String str) {
	// Todos los caracteres num�ricos
	char[] comparacion = {'0','1', '2','3','4','5','6','7','8','9'};
	// La cadena pasada como argumento se pasa a un array de Character
	char[] array = str.toCharArray();
	// Se crea un ArrayList vac�o para ir almacenando caracteres
	List<Character> lista = new ArrayList<Character>();
	
	// Se recorre la cadena de ISBN car�cter a car�cter, en caso de que sea
	// un n�mero, �ste se a�ade al Arraylist y se contin�a con el siguiente
	// car�cter.
	for (int i = 0; i < array.length; i++) {
		for(int j = 0; j < comparacion.length; j++) {
			if (array[i] == comparacion[j]) {
				lista.add(array[i]);
				break;
			}
		}
	}
	// Se devuelve el contenido de la lista de Character como un String
	return lista.toString(); 
}

 */
