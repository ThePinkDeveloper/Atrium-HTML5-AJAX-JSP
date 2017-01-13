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
 * Versi�n: 1.0																
 * Descripci�n: Servlet que recoge los objetos XMLHttpRequest creados y
 * enviados por el c�digo en el archivo buscareDITORIAL.js y que devuelve al
 * cliente con el c�digo html correspondiente seg�n la clave de dicho objeto.
 * MEJORA EN UNA PR�XIMA VERSION: Este servlet genera todo el c�digo html
 * que se tiene que incrustar en la p�gina web, si la p�gina en origen 
 * tuviera una buena estructura html, este servlet s�lo tendr�a que
 * incluir los datos necesarios y se reducir�a considerablemente la cantidad
 * de informaci�n a transportar.
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
	 * M�TODOS
	 *************************************************************************/
    
	/* Nombre: processRequest()
	 * Argumentos: HttpServletRequest, HttpServletResponse
	 * Devuelve: void
	 * Excepciones que lanza: ServletException, IOException
	 * 
	 * Descripci�n: �ste m�todo se encarga de crear el c�digo html en funci�n
	 * de la clave que lleve el objeto XMLHttpRequest remitido por la p�gina
	 * web.
	 * Al cargar la p�gina web buscarEditorial.jsp se necesita el nombre de 
	 * todas las editoriales en la BDD para generar el c�digo HTML del combo. 
	 * En este caso el objeto XMLHttpRequest lleva la clave "inicio". Adem�s,
	 * aprovechando que aparecer� el nombre de la primera editorial de la 
	 * BDD tambi�n se incluyen el resto de datos necesarios para
	 * la creaci�n completa de la p�gina para esta editorial.
	 * Al seleccionarse una nueva editorial del combo, se crea un nuevo objeto
	 * XMLHttpRequest que lleva como clave el nombre de la editorial 
	 * seleccionada.
	 * Con este dato se realiza una b�squeda en la BDD y se devuelve el c�digo
	 * html con los datos necesarios para generar la p�gina.
	 * A petici�n de la pr�ctica hay datos que ir�n en formato XML y que
	 * deber�n ser interpretados y formateados por el correspondiente c�digo
	 * javaScript
	 */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// Se indica que el tipo de respuesta que se devolver� ser� c�digo HTML
    	// en formato UTF-8
    	response.setContentType("text/html;charset=UTF-8");
    	
    	// Se almacena la clave de la respuesta en un objeto BufferedReader  
    	BufferedReader bf = request.getReader();
    	// Se convierte el objeto BufferedReader a String
    	String str = bf.readLine();
    	
    	// Variable auxiliar para el conteo de autores en la BDD
    	int n = 0;
    	
    	// Variables de tipo String auxiliares que almacenar�n el c�digo html
    	String combo = "";
    	String datosXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
    	String separador = "#####"; // Separador
    	String respuesta = "";

    	// Se crea un objeto de tipo GestionLibreria (fachada) para realizar
    	// las gestiones contra la BDD 
		GestionLibreria commBDD = new GestionLibreria();
		// Ejecutamos el m�todo consutarEditoriales() de la clase 
		// GestionLibreria para optener una colecci�n de todos los objetos 
		// Editorial (Hibernate se encarga de la conversi�n registro / objeto).
		// Almacenamos los objetos Editorial devueltos por la consulta 
		// en una variable de tipo List
		List<Editorial> editoriales = commBDD.consultarEditoriales();
		
    	// Si la clave del objeto XMLHttpRerquest es igual a "inicio" hay que
		// crear el c�digo html para crear el combo y aprovechamos para
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
    			// A�adimos el nombre de la editorial al combo
    			combo = combo + "<option value = \"" + nombre + "\">" + nombre + "</option>";
    			
    			// Si adem�s es el primer nombre del List, consultamos el resto
    			// de datos de esta editorial para generar la web 
    			if (n == 0) {
    				// Utilizamos el objeto de la clase GestionLibreria creado
    				// anteriormente para obtener tambi�n los libros publicados
    				// por esta editorial gracias a su m�tedo
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
    				
    				// Generamos el c�digo XML
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
    		
    		// Creamos un �nico String sumando el c�digo html del combo, el
    		// separador y el c�digo xml.
        	respuesta = combo + separador + datosXML;
        	
    		// Lo a�adimos al objeto HttpServletResponse
        	response.getWriter().write(respuesta);
 
    	// Si la clave del objeto XMLHttpRerquest es igual al nombre de una
    	// editorial no es necesario crear el combo y s�lo hay que obtener los
    	// datos necesarios de esa editorial.
    	} else {
    		
    		// Recorremos la colecci�n de editoriales de la base de datos 
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
    				
    				// Creamos una colecci�n de objetos Libros publicados por
    				// por la editorial.
    				List<Libro> libros = commBDD.consultarLibrosPorEditorial(e);
    				
    				// Generamos el c�digo xml para las tablas con los datos
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
				
    	    		// Una vez hemos encontrado la coincidencia en la colecci�n
    	    		// ya no es necesario seguir recorriendo la colecci�n.
					break;
    			}
    		}	
    		
    		// Como ya se ha tenido que haber generado el c�digo html de la
    		// p�gina web, en este caso s�lo enviamos c�digo xml
    		response.setContentType("text/xml;charset=UTF-8");
    		// A�adimos el c�digo XML generado al objeto HttpServletResponse
    		System.out.println(datosXML);
    		response.getWriter().write(datosXML);
    	}    	
    }
    
	/* Nombre: doGet()
	 * Argumentos: HttpServletRequest, HttpServletResponse
	 * Devuelve: void
	 * Descripci�n: M�todo que se encarga de lanzar el m�todo processRequest
	 * cuando el objeto XMLHttpRequest viene como GET
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);	
		} catch (Exception e) {
			System.out.println("Se ha producido un error al gestionar la petici�n GET");
			e.printStackTrace();
		}
	}

	/* Nombre: doPost()
	 * Argumentos: HttpServletRequest, HttpServletResponse
	 * Devuelve: void
	 * Descripci�n: M�todo que se encarga de lanzar el m�todo processRequest
	 * cuando el objeto XMLHttpRequest viene como POST
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);	
		} catch (Exception e) {
			System.out.println("Se ha producido un error al gestionar la petici�n POST");
			e.printStackTrace();
		}
	}
	

}
