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
 * Versión: 1.0																			 *
 * Descripción: Se escriben unas breves instrucciones por consola para que el			 *
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
 * Si la opción introducida por el usuario no corresponde a ninguna de las				 *
 * anteriores se repetirá el mismo mensaje de selección hasta que la opción 			 *
 * introducida coincida.																 *
 * 																						 *
 * En función de la selección del usuario, se lanzará el método correspondiente			 *
 * así como nuevas instrucciones por consola solicitando los datos que fueran			 *
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
	 * Descripción: Se detecta durante la realización del programa que las
	 * instrucciones de este método se repiten considerablemente.
	 * Se crea este método para simplificar y clarificar el código. El String que
	 * se pasa como parámetro se muestra al usuario para informarle de algún evento 
	 * o solicitarle información.
	 */
	
	public String informacionYEspera(String str) {
		
		// Declaramos una variable String que almacenará el dato del usuario
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
	 * Descripción: Este método se encarga de la solicitud al usuario de los datos
	 * necesarios para crear un objeto de la clase Libro.
	 */
	
	public Libro crearLibroUsuario() {
		
		// Se declaran las variables que almacenarán los datos asignando valores por defecto.
		
		String temporal = "";			// Usamos esta variable para almacenar temporalmente los
										// datos introducidos por el usuario que requieran ser
										// convertidos a otro tipo, como en el caso de las
										// variables publicacion y precio.
		
		boolean error = false; 			// Esta es la variable que controla la salida de los 
										// bucles do/while para la conversión de datos.
		
		Set<Autor> autores = null; 		// Nombre del autor
		String descripcion = ""; 		// Sinopsis del Libro
		Editorial editorial = null;		// Editorial que lo ha publicado
		String isbn = "";				// Código ISBN
		double precio = 0.0;			// Precio en euros
		int publicacion = 0;			// Año de publicación
		String titulo = "";				// Título del libro
		
		// Se solicitan los datos al usuario.
		
		// titulo
		titulo = informacionYEspera("INTRODUZCA EL TÍTULO DEL LIBRO");
		
		// autores
		autores = crearAutores();
		
		// editorial
		editorial = crearEditorial();
		
		// isbn
		isbn = informacionYEspera("INTRODUZCA EL ISBN DEL LIBRO");
		
		// publicacion. Al tratarse publicacion de una variable de tipo int hay que convertir
		// el dato del usuario de String a Integer, como este proceso puede lanzar una
		// excepción, la capturamos y solicitamos al usuario que repita el proceso hasta que
		// que el dato introducido sea correcto.
			
		do {
			error = false;
			try {
				temporal = informacionYEspera("INTRODUZCA EL AÑO DE PUBLICACION DEL LIBRO");
				publicacion = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO AÑO. Introduzca un AÑO correcto.");
				error = true;
			}
		} while (error);
		
		// precio. Al tratarse precio de una variable de tipo double hay que convertir
		// el dato del usuario de String a Double, como este proceso puede lanzar una
		// excepción, la capturamos y solicitamos al usuario que repita el proceso hasta que
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
	 * Descripción: Este método se encarga de la solicitud al usuario de los datos
	 * necesarios para crear un conjunto (Set) de la clase Autor para el libro que
	 * se está creando.
	 */
	
	public Set<Autor> crearAutores() {
		boolean error;
		String temporal;
		Set<Autor> autores = new HashSet<Autor>();
		
		
		int numeroAutores = 0;
		do {
			error = false;
			try {
				temporal = informacionYEspera("¿CUANTOS AUTORES HAN INTERVENIDO EN EL LIBRO?\n");
				numeroAutores = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO NÚMERO ENTERO. Introduzca un NÚMERO correcto.");
				error = true;
			}
		} while (error);
		
		for (int n = 0; n < numeroAutores; n++) {
			String nombre = informacionYEspera("¿Nombre del AUTOR " + (n + 1) + "?\n");
			String nacionalidad = informacionYEspera("¿Nacionalidad del AUTOR " + (n + 1) + "?\n");
			String comentarios = informacionYEspera("¿Algún comentario relevante del AUTOR " + (n + 1) + "?\n");
			Autor autor = new Autor(nombre, nacionalidad, comentarios);
			autores.add(autor);
		}
		return autores;
		
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/* Nombre: crearEditorial()
	 * Argumentos: Ninguno
	 * Devuelve: Editorial
	 * Descripción: Este método se encarga de la solicitud al usuario de los datos
	 * necesarios para crear un objeto de la clase Editorial para el libro que
	 * se está creando.
	 */
	
	public Editorial crearEditorial() {
		boolean error;
		String temporal;
		
		String nombre = informacionYEspera("¿NOMBRE DE LA EDITORIAL?\n");
		
		String nif = informacionYEspera("¿NIF DE LA EDITORIAL?\n");
		
		String nombreCalle = informacionYEspera("¿NOMBRE DE LA CALLE?\n");

		int numeroCalle = 0;

		
		do {
			error = false;
			try {
				temporal = informacionYEspera("¿NÚMERO?\n");
				numeroCalle = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO NÚMERO ENTERO. Introduzca un NÚMERO correcto.");
				error = true;
			}
		} while (error);
		
		String poblacion = informacionYEspera("¿POBLACIÓN?\n");
		
		int cp = 0;
		
		do {
			error = false;
			try {
				temporal = informacionYEspera("¿CÓDIGO POSTAL?\n");
				cp = Integer.parseInt(temporal);
			} catch (NumberFormatException e) {
				System.out.println("EL DATO INTRODUCIDO NO SE RECONOCE COMO NÚMERO ENTERO. Introduzca un NÚMERO correcto.");
				error = true;
			}
		} while (error);
		
		String provincia = informacionYEspera("¿PROVINCIA?\n");
		
		Direccion direccion = new Direccion(nombreCalle, numeroCalle, poblacion, cp, provincia);
		
		return new Editorial(nombre, nif, direccion);
	}
	
	
	
	/*************************************************************************************
	 * METODOS SOBREESCRITOS INTERFAZ ItfzGestionLibreria								 *
	 *************************************************************************************/
	
	/* Nombre: altaLibro()
	 * Argumentos: Libro
	 * Devuelve: Boolean
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * altaLibro() devolviendo su resultado e independizando la clase GestionLibreria de
	 * la conexión a la base de datos
	 * El parámetro que recibe este método es el mismo que se pasa al método homónimo de
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * eliminarLibro() devolviendo su resultado e independizando la clase GestionLibreria de
	 * la conexión a la base de datos.
	 * El parámetro que recibe este método es el mismo que se pasa al método homónimo de
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarTodos() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexión a la base de datos.
	 * El parámetro que recibe este método es el mismo que se pasa al método homónimo de
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarISBN() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexión a la base de datos.
	 * El parámetro que recibe este método es el mismo que se pasa al método homónimo de
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarTitulo() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexión a la base de datos.
	 * El parámetro que recibe este método es el mismo que se pasa al método homónimo de
	 * la clase LibrosDAO. 
	 */

	@Override
	public List<Libro> consultarTitulo(String titulo) {
		
		List<Libro> libros;
		LibrosDAO conexionBD = new LibrosDAO();
		
		try {
			libros = conexionBD.consultarTitulo(titulo);
		} catch (LibroNoEncontradoException e) {
			e.mensajeConsola("TÍTULO");
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * modificarPrecio() devolviendo su resultado e independizando la clase GestionLibreria 
	 * de la conexión a la base de datos.
	 * Los parámetros que recibe este método son los mismos que se pasan al método homónimo
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarAutores() devolviendo su resultado e independizando la clase 
	 * GestionLibreria de la conexión a la base de datos.
	 * El parámetro que recibe este método son los mismos que se pasan al método homónimo
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarLibrosPorAutor() devolviendo su resultado e independizando la 
	 * clase GestionLibreria de la conexión a la base de datos.
	 * El parámetro que recibe este método son los mismos que se pasan al método 
	 * homónimo de la clase LibrosDAO. 
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarEditoriales() devolviendo su resultado e independizando la clase 
	 * GestionLibreria de la conexión a la base de datos.
	 * El parámetro que recibe este método son los mismos que se pasan al método homónimo
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
	 * Descripción: Crea una instancia de la clase LibrosDAO y lanza su método
	 * consultarLibrosPorEditorial() devolviendo su resultado e independizando la 
	 * clase GestionLibreria de la conexión a la base de datos.
	 * El parámetro que recibe este método son los mismos que se pasan al método 
	 * homónimo de la clase LibrosDAO. 
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
 * Descripción: Ya que el ISBN es un número complejo que en ocasiones viene divido
 * por diferentes bloques numéricos separados por guiones o espacios, este método
 * elimina los guines y espacios quedándose con un único bloque numérico.
 * Este método está pensado únicamente para propósitos de búsqueda y comparación,
 * por lo que los datos introducidos en la Base de Datos deben intentar pasarse 
 * con el formato original de guiones o espacios.
 * Además, todos aquellos caracteres que no sean numéricos serán eliminados. 
 */

 /*

public String formateadoISBN(String str) {
	// Todos los caracteres numéricos
	char[] comparacion = {'0','1', '2','3','4','5','6','7','8','9'};
	// La cadena pasada como argumento se pasa a un array de Character
	char[] array = str.toCharArray();
	// Se crea un ArrayList vacío para ir almacenando caracteres
	List<Character> lista = new ArrayList<Character>();
	
	// Se recorre la cadena de ISBN carácter a carácter, en caso de que sea
	// un número, éste se añade al Arraylist y se continúa con el siguiente
	// carácter.
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
