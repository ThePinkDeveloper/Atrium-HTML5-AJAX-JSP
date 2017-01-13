/*****************************************************************************
 * VARIABLES															 
 *****************************************************************************/

// Variable que almacenará el objeto XMLHttpRequest
var req = null;

// Constantes de estado
var READY_STATE_UNINITIALIZED = 0;
var READY_STATE_LOADING = 1;
var READY_STATE_LOADED = 2;
var READY_STATE_INTERACTIVE = 3;
var READY_STATE_COMPLETE = 4;

/*****************************************************************************
 * FUNCIONES															 
 *****************************************************************************/

/* Nombre: llamadaANombreAutores()
 * Descripción: Función que se encarga de crear el objeto XMLHttpRequest y 
 * lanzarlo contra el servlet ServletAutor como POST con la clave "inicio" y
 * queda a la espera de los datos de respuesta del servlet. 
 */

function llamadaANombreAutores() {
	
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
		req.open("POST", "ServletAutor", true);
		req.send("inicio");
	}
}

/* Nombre: llamadaADatosAutores()
 * Descripción: Función que se encarga de crear el objeto XMLHttpRequest cada vez
 * que se selecciona un autor del combo de autores y lanzarlo contra el servlet 
 * ServletAutor como POST con el nombre de clave que coincide con el nombre del
 * autor y queda a la espera de los datos de respuesta del servlet
 */

function llamadaADatosAutores() {
	
	// Se crea una referencia al objeto con id "combo" y se almacena en la
	// variable seleccion.
	var seleccion = document.getElementById("combo");
	
	// Se crea el tipo de objeto XMLHttpRequest en función del navegador que 
	// se esté usando y se almacena en la variable "req".
	if (window.XMLHttpRequest){
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}

	// Cuando se crea el objeto XMLHttpRequest se envía un POST con el nombre de
	// clave coincidente con el nombre del autor seleccionado para ser resuelto 
	// por el servlet "ServletAutor"
	if (req){
		// Cada vez que haya un cambio en el estado del objeto XMLHttpRequest
		// se llama a la función funcionAsincronaRecepcionDatosAutores
		req.onreadystatechange = funcionAsincronaRecepcionDatosAutores;
		
		req.open("POST", "ServletAutor", true);
		req.send(seleccion.value);
	}
}

/* Nombre: funcionAsincronaRecepcionNombre()
 * Descripción: Función que se encarga de recoger los datos enviados por el
 * servlet ServletAutor como respuesta al objeto XMLHttpRequest con nombre de
 * clave "inicio" e incrustar dicha respuesta dentro del DIV con id "main" que
 * existe en el BODY de la página web. 
 */

function funcionAsincronaRecepcionNombre(){
	
	// Se recoge el estado del objeto XMLHttpRequest almacenado en la variable
	// req y se almacena en la variable ready
	var ready = req.readyState;
	// La variable data almacenará el código html remitido por el servlet
	var data = null;

	// Cuando se haya recibido toda la información del objeto XMLHttpRequest...
	if (ready == READY_STATE_COMPLETE){
		// ... se almacena la respuesta del servlet en la variable data ...
		data = req.responseText;
		// ... y se incrusta en el DIV con id "main"
		document.getElementById("main").innerHTML = data;

	}
}

/* Nombre: funcionAsincronaRecepcionDatosAutor()
 * Descripción: Función que se encarga de recoger los datos enviados por el
 * servlet ServletAutor como respuesta al objeto XMLHttpRequest con nombre de
 * clave igual al nombre del autor seleccionado e incrustar dicha respuesta 
 * dentro de la TABLE con id "tablaAutor"
 */

function funcionAsincronaRecepcionDatosAutores(){

	// Se recoge el estado del objeto XMLHttpRequest almacenado en la variable
	// req y se almacena en la variable ready
	var ready = req.readyState;
	// La variable data almacenará el código html remitido por el servlet
	var data = null;

	// Cuando se haya recibido toda la información del objeto XMLHttpRequest...
	if (ready == READY_STATE_COMPLETE){
		// ... se almacena la respuesta del servlet en la variable data ...
		data = req.responseText;
		// ... y se incrusta en el DIV con id "datos"
		document.getElementById("datos").innerHTML = data;
	}
}

// En cuando carga la página "buscarAutor.jsp" se llama al método
// llamadaANombreAutores().
// IMPORTANTE!!
// En JavaScript las funciones deben estar definidas antes de poder
// usarlas.

window.onload = function() {
	llamadaANombreAutores();
}