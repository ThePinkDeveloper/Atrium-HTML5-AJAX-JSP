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
import com.grupoatrium.modelo.Editorial;
import com.grupoatrium.modelo.Libro;
import com.grupoatrium.negocio.GestionLibreria;

/*****************************************************************************
 * Nombre del Servlet : ServletAutor													
 * Autor: Miguel Rosa														
 * Fecha: 11 - Enero - 2017												
 * Versión: 1.0																
 * Descripción: Servlet que recoge los objetos XMLHttpRequest creados y
 * enviados por el código en el archivo buscareDITORIAL.js y que devuelve al
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

@WebServlet("/ServletEditorial")
public class ServletEditorial extends HttpServlet {
    
	/*************************************************************************
	 * CONSTRUCTORES
	 *************************************************************************/

    public ServletEditorial() {
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
	 * Al cargar la página web buscarEditorial.jsp se necesita el nombre de 
	 * todas las editoriales en la BDD para generar el código HTML del combo. 
	 * En este caso el objeto XMLHttpRequest lleva la clave "inicio". Además,
	 * aprovechando que aparecerá el nombre de la primera editorial de la 
	 * BDD también se incluyen el resto de datos necesarios para
	 * la creación completa de la página para esta editorial.
	 * Al seleccionarse una nueva editorial del combo, se crea un nuevo objeto
	 * XMLHttpRequest que lleva como clave el nombre de la editorial 
	 * seleccionada.
	 * Con este dato se realiza una búsqueda en la BDD y se devuelve el código
	 * html con los datos necesarios para generar la página.
	 * A petición de la práctica hay datos que irán en formato XML y que
	 * deberán ser interpretados y formateados por el correspondiente código
	 * javaScript
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
    	String datosXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
    	String separador = "#####"; // Separador
    	String respuesta = "";

    	// Se crea un objeto de tipo GestionLibreria (fachada) para realizar
    	// las gestiones contra la BDD 
		GestionLibreria commBDD = new GestionLibreria();
		// Ejecutamos el método consutarEditoriales() de la clase 
		// GestionLibreria para optener una colección de todos los objetos 
		// Editorial (Hibernate se encarga de la conversión registro / objeto).
		// Almacenamos los objetos Editorial devueltos por la consulta 
		// en una variable de tipo List
		List<Editorial> editoriales = commBDD.consultarEditoriales();
		
    	// Si la clave del objeto XMLHttpRerquest es igual a "inicio" hay que
		// crear el código html para crear el combo y aprovechamos para
		// obtener los datos del primer autor de la lista
    	if (str.equals("inicio")) {

    		// Un combo se crea con las etiquetas
    		// <select>
    		//		<option> </option>
    		// </select>
    		combo = combo + "<div id = \"divcombo\"><select id = \"combo\" name = \"Editoriales\" onchange=javascript:llamadaADatosEditoriales();>";
    		
    		// Recorremos todo el List editoriales, para obtener el nombre de 
    		// todas las editoriales en la base de datos
    		for (Editorial e: editoriales) {
    			String nombre = e.getNombre();
    			// Añadimos el nombre de la editorial al combo
    			combo = combo + "<option value = \"" + nombre + "\">" + nombre + "</option>";
    			
    			// Si además es el primer nombre del List, consultamos el resto
    			// de datos de esta editorial para generar la web 
    			if (n == 0) {
    				// Utilizamos el objeto de la clase GestionLibreria creado
    				// anteriormente para obtener también los libros publicados
    				// por esta editorial gracias a su métedo
    				// consultarLibrosPorEditorial(Editorial). Almacenamos los 
    				// objetos Libro en una variable List de nombre libros
    				List<Libro> libros = commBDD.consultarLibrosPorEditorial(e);
    				
    				// Almacenamos los datos de la editorial
    				String nif = e.getNif();
    				String dirNombreCalle = e.getDireccion().getCalle();
    				String dirNumero = e.getDireccion().getNumero().toString();
    				String dirPoblacion = e.getDireccion().getPoblacion();
    				String dirProvincia = e.getDireccion().getProvincia();
    				String dirCP = e.getDireccion().getCp().toString();
    				
    				// Generamos el código XML
    				datosXML += "<editorial>" + "\n";
    					datosXML += "<nombre>" + nombre + "</nombre>" + "\n";
    					datosXML += "<nif>" + nif + "</nif>" + "\n";
    					datosXML += "<direccion>" + "\n";
    						datosXML += "<nombreCalle>" + dirNombreCalle + "</nombreCalle>" + "\n";
    						datosXML += "<numeroCalle>" + dirNumero + "</numeroCalle>" + "\n";
    						datosXML += "<poblacion>" + dirPoblacion + "</poblacion>" + "\n";
    						datosXML += "<provincia>" + dirProvincia + "</provincia>" + "\n";
    						datosXML += "<cp>" + dirCP + "</cp>" + "\n";
    					datosXML += "</direccion>" + "\n";
    					datosXML += "<libros>" + "\n";
						for (Libro l : libros) {
							datosXML += "<libro>" + l.getTitulo() + "</libro>" + "\n";
						}
						datosXML += "</libros>" + "\n";
					datosXML += "</editorial>" + "\n";
    			}
    			
    			// Una vez obtenidos todos necesarios de la primera editorial
    			// de la coleccion, incrementamos el valor de referencia para 
    			// que no se sobreescriban los datos obtenidos con el resto 
    			// de editoriales
    			n++;
    		}
    		
    		// Cuando ya hemos obtenido el nombre de todos los autores de la
    		// BDD podemos cerrar el combo
    		combo = combo + "</select></div>";
    		
    		// Creamos un único String sumando el código html del combo, el
    		// separador y el código xml.
        	respuesta = combo + separador + datosXML;
        	
    		// Lo añadimos al objeto HttpServletResponse
        	response.getWriter().write(respuesta);
 
    	// Si la clave del objeto XMLHttpRerquest es igual al nombre de una
    	// editorial no es necesario crear el combo y sólo hay que obtener los
    	// datos necesarios de esa editorial.
    	} else {
    		
    		// Recorremos la colección de editoriales de la base de datos 
    		// buscando la coincidencia entre el valor de la clave del objeto
    		// XMLHttpRequest y el nombre de la editorial
    		for (Editorial e: editoriales) {
    			
    			// Cuando se produce la coincidencia obtenemos sus datos
    			if (str.equals(e.getNombre())) {
    				
    				// Guardamos los datos de la editorial en Strings
    				String nombre = e.getNombre();
    				String nif = e.getNif();
    				String dirNombreCalle = e.getDireccion().getCalle();
    				String dirNumero = e.getDireccion().getNumero().toString();
    				String dirPoblacion = e.getDireccion().getPoblacion();
    				String dirProvincia = e.getDireccion().getProvincia();
    				String dirCP = e.getDireccion().getCp().toString();
    				
    				// Creamos una colección de objetos Libros publicados por
    				// por la editorial.
    				List<Libro> libros = commBDD.consultarLibrosPorEditorial(e);
    				
    				// Generamos el código xml para las tablas con los datos
    				// de la editorial
    				datosXML += "<editorial>" + "\n";
					datosXML += "<nombre>" + nombre + "</nombre>" + "\n";
					datosXML += "<nif>" + nif + "</nif>" + "\n";
					datosXML += "<direccion>" + "\n";
						datosXML += "<nombreCalle>" + dirNombreCalle + "</nombreCalle>" + "\n";
						datosXML += "<numeroCalle>" + dirNumero + "</numeroCalle>" + "\n";
						datosXML += "<poblacion>" + dirPoblacion + "</poblacion>" + "\n";
						datosXML += "<provincia>" + dirProvincia + "</provincia>" + "\n";
						datosXML += "<cp>" + dirCP + "</cp>" + "\n";
					datosXML += "</direccion>" + "\n";
					datosXML += "<libros>" + "\n";
					for (Libro l : libros) {
						datosXML += "<libro>" + l.getTitulo() + "</libro>" + "\n";
					}
					datosXML += "</libros>" + "\n";
					datosXML += "</editorial>" + "\n";
				
    	    		// Una vez hemos encontrado la coincidencia en la colección
    	    		// ya no es necesario seguir recorriendo la colección.
					break;
    			}
    		}	
    		
    		// Como ya se ha tenido que haber generado el código html de la
    		// página web, en este caso sólo enviamos código xml
    		response.setContentType("text/xml;charset=UTF-8");
    		// Añadimos el código XML generado al objeto HttpServletResponse
    		System.out.println(datosXML);
    		response.getWriter().write(datosXML);
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
