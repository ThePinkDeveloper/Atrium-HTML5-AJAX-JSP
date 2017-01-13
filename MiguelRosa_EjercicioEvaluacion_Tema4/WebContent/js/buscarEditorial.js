/*****************************************************************************
 * VARIABLES															 
 *****************************************************************************/

// Variable que almacenará el objeto XMLHttpRequest
var req = null;

//Constantes de estado
var READY_STATE_UNINITIALIZED = 0;
var READY_STATE_LOADING = 1;
var READY_STATE_LOADED = 2;
var READY_STATE_INTERACTIVE = 3;
var READY_STATE_COMPLETE = 4;

/*****************************************************************************
 * FUNCIONES															 
 *****************************************************************************/

/* Nombre: llamadaANombreEditoriales()
 * Descripción: Función que se encarga de crear el objeto XMLHttpRequest y 
 * lanzarlo contra el servlet ServletEditorial como POST con la clave "inicio" y
 * queda a la espera de los datos de respuesta del servlet. 
 */

function llamadaANombreEditoriales() {
	
	// Se crea el tipo de objeto XMLHttpRequest en función del navegador que 
	// se esté usando y se almacena en la variable "req"
	if (window.XMLHttpRequest){
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}

	// Cuando se crea el objeto XMLHttpRequest se envía un POST con la clave
	// "inicio" para ser resuelto por el servlet "ServletAutor"
	if (req){
		req.onreadystatechange = funcionAsincronaRecepcionNombre;
		req.open("POST", "ServletEditorial", true);
		req.send("inicio");
	}
}

/* Nombre: llamadaADatosEditoriales()
 * Descripción: Función que se encarga de crear el objeto XMLHttpRequest cada vez
 * que se selecciona una editorial del combo de editoriales y lanzarlo contra el 
 * servlet ServletEditorial como POST con el nombre de clave que coincide con el 
 * nombre de la editorial seleccionada y queda a la espera de los datos de 
 * respuesta del servlet.
 */

function llamadaADatosEditoriales() {
	
	// Se crea una referencia al objeto con id "combo" y se almacena en la
	// variable seleccion.
	var seleccion = document.getElementById("combo");
	
	// Se crea el tipo de objeto XMLHttpRequest en función del navegador que 
	// se esté usando y se almacena en la variable "req"
	if (window.XMLHttpRequest){
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}

	// Cuando se crea el objeto XMLHttpRequest se envía un POST con el nombre de
	// clave coincidente con el nombre de la editorial seleccionada para ser 
	// resuelto por el servlet "ServletEditorial"
	if (req){
		// Cada vez que haya un cambio en el estado del objeto XMLHttpRequest
		// se llama a la función funcionAsincronaRecepcionDatosEditoriales
		req.onreadystatechange = funcionAsincronaRecepcionDatosEditoriales;
		
		req.open("POST", "ServletEditorial", true);
		req.send(seleccion.value);
	}
}

/* Nombre: funcionAsincronaRecepcionNombre()
 * Descripción: Función que se encarga de recoger los datos enviados por el
 * servlet ServletEditorial como respuesta al objeto XMLHttpRequest con nombre
 * de clave "inicio" e incrustar dicha respuesta dentro del DIV con id "main" 
 * que existe en el BODY de la página web.
 * Por petición de la práctica el código de respuesta viene en html y xml.
 * El servlet ha incluido un separador "#####" para partir el código html del
 * xml.
 * IMPORTANTE: El código xml como tal no se puede leer directamente. Hay que
 * convertirlo a un objeto XML antes de poder extraer los datos
 * IMPORTANTE: El código remitido por el servlet viene formato en UTF-8, sin
 * embargo, el código html que se genera en este script NO y por tanto los
 * caracteres especiales (vocales tildadas, por ejemplo) deben escribirse
 * con su correspondiente código hexadecimal (&#x00f3; = ó, por ejemplo).
 */

function funcionAsincronaRecepcionNombre(){
	
	// Se recoge el estado del objeto XMLHttpRequest almacenado en la variable
	// req y se almacena en la variable ready
	var ready = req.readyState;
	// La variable data almacenará el código html remitido por el servlet
	var data = null;

	// Cuando se haya recibido toda la información del objeto XMLHttpRequest...
	if (ready == READY_STATE_COMPLETE){
		// ... se almacena la respuesta del servlet en la variable data.
		data = req.responseText;
		
		// Debido a que la respuesta del servidor contine html y xml, se
		// intercala el separador "#####" para diferenciar uno de otro. Con
		// la función split partimos la respuesta en 2 separaqmos html de
		// xml
		var array = data.split("#####");
		var datosHtml = array[0]; // datos html
		var datosXml = array[1];  // datos xml

		// Convertimos la cadena de texto en formato xml en un objeto xml
		// para poder extraer su contenido
		var parser = new DOMParser();
		xmlDoc = parser.parseFromString(datosXml,"text/xml");
		
		// Extramos los datos del objeto XML
		var nombreEditorial = xmlDoc.getElementsByTagName("nombre")[0].childNodes[0].nodeValue;
		var nifEditorial = xmlDoc.getElementsByTagName("nif")[0].childNodes[0].nodeValue;
		var nombreCalleEditorial = xmlDoc.getElementsByTagName("nombreCalle")[0].childNodes[0].nodeValue;
		var numeroCalleEditorial = xmlDoc.getElementsByTagName("numeroCalle")[0].childNodes[0].nodeValue;
		var poblacionEditorial = xmlDoc.getElementsByTagName("poblacion")[0].childNodes[0].nodeValue;
		var cpEditorial = xmlDoc.getElementsByTagName("cp")[0].childNodes[0].nodeValue;
		var provinciaEditorial = xmlDoc.getElementsByTagName("provincia")[0].childNodes[0].nodeValue;
		var xmlLibros = xmlDoc.getElementsByTagName("libros")[0].getElementsByTagName("libro");
		var libros = [];
		for (var i = 0; i < xmlLibros.length; i++) {
			libros.push(xmlLibros[i].childNodes[0].nodeValue);
		}
		
		// Generamos el código html con los datos obtenidos del código xml
		// Primero los datos de la Editorial
		var tablaDatosEditorial = "<div id = 'datosEditorial'>";
		tablaDatosEditorial += "<table id = 'tablaEditorial' border>";
		tablaDatosEditorial += "<caption>Datos de la Editorial</caption>";
		tablaDatosEditorial += "<tbody>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>Nombre</td>";
		tablaDatosEditorial += "<td>" + nombreEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>NIF</td>";
		tablaDatosEditorial += "<td>" + nifEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>Direcci&#x00f3;n</td>";
		tablaDatosEditorial += "<td>" + nombreCalleEditorial + ", " + numeroCalleEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
	
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>Poblaci&#x00f3;n</td>";
		tablaDatosEditorial += "<td>" + poblacionEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>C&#x00f3;digo Postal</td>";
		tablaDatosEditorial += "<td>" + cpEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";	
		tablaDatosEditorial += "<td>Provincia</td>";
		tablaDatosEditorial += "<td>" + provinciaEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		tablaDatosEditorial += "</tbody>";
		tablaDatosEditorial += "</table>";
		tablaDatosEditorial += "</div>";
		
		// Segundo, los libros publicados por la editorial
		var tablaLibrosEditorial = "<div id = 'datosLibros'>";
		tablaLibrosEditorial += "<table id = 'tablaLibros' border>";
		tablaLibrosEditorial += "<caption>Libros publicados por la Editorial</caption>";
		tablaLibrosEditorial += "<tbody>";
		for (var i = 0; i < libros.length; i++) {
			tablaLibrosEditorial += "<tr><td>" + libros[i] + "</tr></td>";
		}
		tablaLibrosEditorial += "</tbody>";
		tablaLibrosEditorial += "</table>";
		tablaLibrosEditorial += "</div>";
		
		// Añadimos todo el código html generado al combo ya recibido como
		// respuesta en el objeto XMLHttpRequest.
		datosHtml += "<div id = 'datos'>" + tablaDatosEditorial + tablaLibrosEditorial + "</div>";
		
		// Lo incrustamos en el DIV con id "main" que teníamos preparado
		// en la página web.
		document.getElementById("main").innerHTML = datosHtml;

	}
}

/* Nombre: funcionAsincronaRecepcionDatosEditoriales()
 * Descripción: Función que se encarga de recoger los datos enviados por el
 * servlet ServletEditorial como respuesta al objeto XMLHttpRequest con nombre de
 * clave igual al nombre de la editorial seleccionada e incrustar dicha respuesta 
 * dentro de la página web.
 */

function funcionAsincronaRecepcionDatosEditoriales(){
	
	// Se recoge el estado del objeto XMLHttpRequest almacenado en la variable
	// req y se almacena en la variable ready
	var ready = req.readyState;

	// Cuando se haya recibido toda la información del objeto XMLHttpRequest...
	if (ready == READY_STATE_COMPLETE){
		// ... se almacena la respuesta del servlet en la variable xmlDoc ...
		var xmlDoc = req.responseXML;
		// como ya viene formateada en XML xmlDoc ya e sun objeto XML y no hay
		// que convertirlo. Así que podemos extraer los datos.
		var nombreEditorial = xmlDoc.getElementsByTagName("nombre")[0].childNodes[0].nodeValue;
		var nifEditorial = xmlDoc.getElementsByTagName("nif")[0].childNodes[0].nodeValue;
		var nombreCalleEditorial = xmlDoc.getElementsByTagName("nombreCalle")[0].childNodes[0].nodeValue;
		var numeroCalleEditorial = xmlDoc.getElementsByTagName("numeroCalle")[0].childNodes[0].nodeValue;
		var poblacionEditorial = xmlDoc.getElementsByTagName("poblacion")[0].childNodes[0].nodeValue;
		var cpEditorial = xmlDoc.getElementsByTagName("cp")[0].childNodes[0].nodeValue;
		var provinciaEditorial = xmlDoc.getElementsByTagName("provincia")[0].childNodes[0].nodeValue;
		var xmlLibros = xmlDoc.getElementsByTagName("libros")[0].getElementsByTagName("libro");
		var libros = [];
		for (var i = 0; i < xmlLibros.length; i++) {
			libros.push(xmlLibros[i].childNodes[0].nodeValue);
		}
		
		// Generamos el código html con los datos obtenidos del código xml
		// Primero los datos de la Editorial
		var tablaDatosEditorial = "<div id = 'datosEditorial'>";
		tablaDatosEditorial += "<table id = 'tablaEditorial' border>";
		tablaDatosEditorial += "<caption>Datos de la Editorial</caption>";
		tablaDatosEditorial += "<tbody>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>Nombre</td>";
		tablaDatosEditorial += "<td>" + nombreEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>NIF</td>";
		tablaDatosEditorial += "<td>" + nifEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>Direcci&#x00f3;n</td>";
		tablaDatosEditorial += "<td>" + nombreCalleEditorial + ", " + numeroCalleEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
	
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>Poblaci&#x00f3;n</td>";
		tablaDatosEditorial += "<td>" + poblacionEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";
		tablaDatosEditorial += "<td>C&#x00f3;digo Postal</td>";
		tablaDatosEditorial += "<td>" + cpEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		
		tablaDatosEditorial += "<tr>";	
		tablaDatosEditorial += "<td>Provincia</td>";
		tablaDatosEditorial += "<td>" + provinciaEditorial + "</td>";
		tablaDatosEditorial += "</tr>";
		tablaDatosEditorial += "</tbody>";
		tablaDatosEditorial += "</table>";
		tablaDatosEditorial += "</div>";
		
		// Segundo, los libros publicados por la editorial
		var tablaLibrosEditorial = "<div id = 'datosLibros'>";
		tablaLibrosEditorial += "<table id = 'tablaLibros' border>";
		tablaLibrosEditorial += "<caption>Libros publicados por la Editorial</caption>";
		tablaLibrosEditorial += "<tbody>";
		for (var i = 0; i < libros.length; i++) {
			tablaLibrosEditorial += "<tr><td>" + libros[i] + "</tr></td>";
		}
		tablaLibrosEditorial += "</tbody>";
		tablaLibrosEditorial += "</table>";
		tablaLibrosEditorial += "</div>";
		
		// Generamos un único archivo con todo el código html generado
		var datosHtml = tablaDatosEditorial + tablaLibrosEditorial;
		
		// Lo incrustamos en el DIV con id "datos" que teníamos preparado
		// en la página web.
		document.getElementById("datos").innerHTML = datosHtml;
		
	}
}

//En cuando carga la página "buscarEditorial.jsp" se llama al método
//llamadaANombreEditorial().
//IMPORTANTE!!
//En JavaScript las funciones deben estar definidas antes de poder
//usarlas.

window.onload = function() {
	llamadaANombreEditoriales();
}



