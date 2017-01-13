package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.grupoatrium.modelo.Autor;
import com.grupoatrium.modelo.Libro;
import com.grupoatrium.negocio.GestionLibreria;

/*****************************************************************************
 * Nombre del Servlet : ServletAutor													
 * Autor: Miguel Rosa														
 * Fecha: 11 - Enero - 2017												
 * Versión: 1.0																
 * Descripción: Servlet que recoge los objetos XMLHttpRequest creados y
 * enviados por el código en el archivo buscarAutor.js y que devuelve al
 * cliente con el código html correspondiente según la clave de dicho objeto.
 * MEJORA EN UNA PRÓXIMA VERSION: Este servlet genera todo el código html
 * que se tiene que incrustar en la página web, si la página en origen 
 * tuviera una buena estructura html, este servlet sólo tendría que
 * incluir los datos necesarios y se reduciría considerablemente la cantidad
 * de información a transportar.
 * TESTEADO y FUNCIONA EN FIREFOX 50.1.0
 * TESTEADO y FUNCIONA EN Microsoft Edge 38.14393.0.0
 * TESTEADO Y FUNCIONA EN CHROME 55.0.2883.87 m (64-bit)									
 *****************************************************************************/

@WebServlet("/ServletAutor")
public class ServletAutor extends HttpServlet {    

	/*************************************************************************
	 * CONSTRUCTORES
	 *************************************************************************/
    public ServletAutor() {
        super();
    }
    
	/*************************************************************************
	 * MÉTODOS
	 *************************************************************************/
    
	/* Nombre: processRequest()
	 * Argumentos: HttpServletRequest, HttpServletResponse
	 * Devuelve: void
	 * Excepciones que lanza: ServletException, IOException
	 * 
	 * Descripción: Éste método se encarga de crear el código html en función
	 * de la clave que lleve el objeto XMLHttpRequest remitido por la página
	 * web.
	 * Al cargar la página web buscarAutor.jsp se necesita el nombre de todos
	 * los autores en la BDD para generar el código HTML del combo. En este
	 * caso el objeto XMLHttpRequest lleva la clave "inicio". Además,
	 * aprovechando que aparecerá el nombre del primer autor de la BDD
	 * también se incluyen el resto de datos necesarios para
	 * la creación completa de la página para este autor.
	 * Al seleccionarse un nuevo autor del combo, se crea un nuevo objeto
	 * XMLHttpRequest que lleva como clave el nombre del autor seleccionado.
	 * Con este dato se realiza una búsqueda en la BDD y se devuelve el código
	 * html con los datos necesarios para generar la página.
	 */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	// Se indica que el tipo de respuesta que se devolverá será código HTML
    	// en formato UTF-8
    	response.setContentType("text/html;charset=UTF-8");
    	
    	// Se almacena la clave de la respuesta en un objeto BufferedReader   	
    	BufferedReader bf = request.getReader();
    	// Se convierte el objeto BufferedReader a String
    	String str = bf.readLine();
    	
    	// Variable auxiliar para el conteo de autores en la BDD
    	int n = 0;
    	
    	// Variables de tipo String auxiliares que almacenarán el código html
    	String combo = "";
    	String tablaAutor = "";
    	String tablaLibros = "";
    	String respuesta = "";

    	// Se crea un objeto de tipo GestionLibreria (fachada) para realizar
    	// las gestiones contra la BDD 
		GestionLibreria commBDD = new GestionLibreria();
		// Ejecutamos el método consutarAutores() de la clase GestionLibreria
		// para optener una colección de todos los objetos Autores (Hibernate
		// se encarga de la conversión registro / objeto). Almacenamos los
		// objetos Autor devueltos por la consulta en una variable de tipo List
		List<Autor> autores = commBDD.consultarAutores();
		
    	// Si la clave del objeto XMLHttpRerquest es igual a "inicio" hay que
		// crear el código html para crear el combo y aprovechamos para
		// obtener los datos del primer autor de la lista
    	if (str.equals("inicio")) {

    		// Un combo se crea con las etiquetas
    		// <select>
    		//		<option> </option>
    		// </select>
    		combo += "<div id = \"divcombo\"><select id = \"combo\" name = \"Autores\" onchange=javascript:llamadaADatosAutores();>";
    		
    		// Recorremos todo el List autores, para obtener el nombre de todos
    		// los autores en la base de datos
    		for (Autor a: autores) {
    			String nombre = a.getNombre();
    			// Añadimos el nombre del autor al combo
    			combo = combo + "<option value = \"" + nombre + "\">" + nombre + "</option>";
    			
    			// Si además es el primer nombre del List, consultamos el resto
    			// de datos de este autor para generar la web 
    			if (n == 0) {
    				// Utilizamos el objeto de la clase GestionLibreria creado
    				// anteriormente para obtener también los libros escritos
    				// por este autor gracias a su métedo
    				// consultarLibrosPorAutor(Autor). Almacenamos los objetos
    				// Libro en una variable List de nombre libros
    				List<Libro> libros = commBDD.consultarLibrosPorAutor(a);
    				
    				// Almacenamos la nacionalidad y el comentario de este
    				// autor
    				String nacionalidad = a.getNacionalidad();
    				String comentarios = a.getComentarios();
    				
    				// Generamos el código html para crear la tabla con los
    				// datos obtenidos de este primer autor dde la lista
    				// Una tabla se genera con el código html:
    				// <table>
    				// 		<tr> 			<- Esto es una fila
    				//				<td> 	<- Cada columna de la fila
    				//				<td>	<- Cada columna de la fila
    				//		</tr>
    				// </table>
    				// Incrustamos la tabla dentro de un DIV para poder
    				// darle formato con CSS más fácilmente.
    				
    				tablaAutor += "<div id = \"datosAutor\">";
    				tablaAutor += "<table id = \"tablaAutor\" border>";
    	    		tablaAutor += "<caption>Datos del autor</caption>";
    	    		tablaAutor += "<tbody>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nombre" + "</td>";
    	    		tablaAutor += "<td>" + nombre + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nacionalidad" + "</td>";
    	    		tablaAutor += "<td>" + nacionalidad + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Comentarios" + "</td>";
    	    		tablaAutor += "<td>" + comentarios + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "</tbody></table></div>";
    	    		
    	    		// Procedemos de igual manera para general el código html
    	    		// de los libros escritos por el primer autor de la
    	    		// colección. Igualmente, incrustamos la tabla dentro de 
    	    		// un DIV para poder darle formato con CSS más fácilmente.
    	    		tablaLibros += "<div id = \"datosLibros\">";
    				tablaLibros += "<table id = \"tablaLibros\" border>";
    				tablaLibros += "<caption>Títulos escritos por este autor</caption>";
    				tablaLibros += "<tbody>";
	    			for (Libro l: libros) {
	    				tablaLibros += "<tr><td>" + l.getTitulo() + "</td></tr>";
	    			}
    				tablaLibros += "</tbody></table></div>";
    			}
    			// Una vez obtenidos todos necesarios del primer autor de la
    			// coleccion, incrementamos el valor de referencia para que no
    			// se sobreescriban los datos obtenidos con el resto de autores
    			n++;
    		}
    		
    		// Cuando ya hemos obtenido el nombre de todos los autores de la
    		// BDD podemos cerrar el combo
    		combo += "</select></div>";
    		
    		// Creamos un único String sumando todo el código html generado.
    		// Enmarcamos los datos de los autores dentro de un DIV para no
    		// tener que volver a cargar el combo en cada selección
    		respuesta = combo + "<div id = \"datos\">" + tablaAutor + tablaLibros + "</div>";
			
    		// Lo añadimos al objeto HttpServletResponse
    		response.getWriter().write(respuesta);
    	
    	// Si la clave del objeto XMLHttpRerquest es igual al nombre de un
    	// autor no es necesario crear el combo y sólo hay que obtener los
    	// datos necesarios de ese autor.
    	} else {
    		// Recorremos la colección de autores de la base de datos buscando
    		// la coincidencia entre el valor de la clave del objeto
    		// XMLHttpRequest y el nombre del autor 
    		for (Autor a: autores) {
    			// Cuando se produce la coincidencia obtenemos sus datos
    			if (str.equals(a.getNombre())) {
    				
    				// Guardamos los datos del autor en Strings
    				String nombre = a.getNombre();
    				String nacionalidad = a.getNacionalidad();
    				String comentarios = a.getComentarios();
    				
    				// Creamos una colección de objetos Libros escritos
    				// por el autor
    				List<Libro> libros = commBDD.consultarLibrosPorAutor(a);
    				
    				// Generamos el código html para la tabla con los datos
    				// del autor
    				tablaAutor = "";
    				tablaAutor += "<div id = \"datosAutor\">";
    				tablaAutor += "<table id = \"tablaAutor\" border>";
    	    		tablaAutor += "<caption>Datos del autor</caption>";
    	    		tablaAutor += "<tbody>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nombre" + "</td>";
    	    		tablaAutor += "<td>" + nombre + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nacionalidad" + "</td>";
    	    		tablaAutor += "<td>" + nacionalidad + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Comentarios" + "</td>";
    	    		tablaAutor += "<td>" + comentarios + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "</tbody></table></div>";
    	    		
    	    		// Generamos el código html para la tabla con los títulos
    	    		// de los libros escritos por el autor
    	    		tablaLibros = "";
    	    		if (libros != null) {
        	    		tablaLibros += "<div id = \"datosLibros\">";
        				tablaLibros += "<table id = \"tablaLibros\" border>";
        				tablaLibros += "<caption>Títulos escritos por este autor</caption>";
        				tablaLibros += "<tbody>";
		    				for (Libro l: libros) {
		    					tablaLibros += "<tr><td>" + l.getTitulo() + "</td></tr>";
		    				}
		    			tablaLibros += "</tbody></table></div>";
    				}
    	    		
    	    		// Una vez hemos encontrado la coincidencia en la colección
    	    		// ya no es necesario seguir recorriendo la colección.
    	    		break;
    			}
    		}

    		// Creamos un único String sumando todo el código html generado.
    		respuesta = tablaAutor + tablaLibros;

    		// Lo añadimos al objeto HttpServletResponse
    		response.getWriter().write(respuesta);
    	}
	}
    
	/* Nombre: doGet()
	 * Argumentos: HttpServletRequest, HttpServletResponse
	 * Devuelve: void
	 * Descripción: Método que se encarga de lanzar el método processRequest
	 * cuando el objeto XMLHttpRequest viene como GET
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);	
		} catch (Exception e) {
			System.out.println("Se ha producido un error al gestionar la petición GET");
			e.printStackTrace();
		}
	}

	/* Nombre: doPost()
	 * Argumentos: HttpServletRequest, HttpServletResponse
	 * Devuelve: void
	 * Descripción: Método que se encarga de lanzar el método processRequest
	 * cuando el objeto XMLHttpRequest viene como POST
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);	
		} catch (Exception e) {
			System.out.println("Se ha producido un error al gestionar la petición POST");
			e.printStackTrace();
		}
	}
	

}
