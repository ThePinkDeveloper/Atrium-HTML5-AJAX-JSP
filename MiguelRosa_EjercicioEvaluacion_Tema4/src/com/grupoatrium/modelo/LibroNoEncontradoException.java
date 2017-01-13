package com.grupoatrium.modelo;

import java.util.Scanner;

import javax.swing.JOptionPane;

/*****************************************************************************************
 * Nombre de Clase : LibroNoEncontradoException											 *
 * Autor: Miguel Rosa																	 *
 * Fecha: 11 - Noviembre - 2016															 *
 * Versión: 1.0																			 *
 * Descripción: 																		 *
 * Esta Excepción se lanza cuando alguno de estos métodos:								 *
 *		eliminarLibro()																	 *
 *		consultarISBN()																	 *
 *		consultarTitulo()																 *
 *		modificarPrecio()																 *
 * declarados en la interfaz ItfzLibrosDao y definidos en la Clase LibrosDAO es invocado *
 * y no se encuentra ningún registro en la base de datos que coincida con los parámetros *
 * aportados por el usuario y, en consecuencia, se informa al mismo de que no se ha		 *
 * encontrado ningún registro con los datos aportados.									 * 														 	*
 *****************************************************************************************/

public class LibroNoEncontradoException extends Exception {
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 *
	 *************************************************************************************/
	
	private static boolean modoGrafico = true;
	
	/*************************************************************************************
	 * CONSTRUCTORES																	 *
	 *************************************************************************************/
	
	public LibroNoEncontradoException(String e) {
		if (modoGrafico) {
			mensajeAlert(e);
		} else {
			mensajeConsola(e);
		}
		
	}
	
	/*************************************************************************************
	 * GETTERS Y SETTERS																 *
	 *************************************************************************************/
	
	public static boolean isModoGrafico() {
		return modoGrafico;
	}

	public static void setModoGrafico(boolean modoGrafico) {
		LibroNoEncontradoException.modoGrafico = modoGrafico;
	}

	/*************************************************************************************
	 * METODOS																 *
	 *************************************************************************************/
	public void mensajeConsola(String contenido) {
		// Se muestra un mensaje por consola indicando que no hay ningún libro con el
		// dato proporcionado.
		System.out.println();
		System.out.println("NO SE ENCUENTRA UN LIBRO CON ESE " + contenido + " EN NUESTRA BASE DE DATOS. Pulse INTRO para continuar.");
		System.out.println();
		
	}
	
	public void mensajeAlert(String contenido) {
		// Se muestra un Alert indicando que no hay ningún libro con el
		// dato proporcionado.
		JOptionPane.showMessageDialog(null, "NO SE ENCUENTRA UN LIBRO CON ESE " + contenido + " EN NUESTRA BASE DE DATOS.");
	}

}
