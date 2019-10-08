package com.bim.msf4j.commons;

import java.util.List;

import org.apache.log4j.Logger;


public class Utilerias {

	private static final Logger logger = Logger.getLogger(Utilerias.class);
	
	public static List<Object> paginado(List<Object> datos, int pagina, int porPagina) {
		logger.info("Inicio paginado");
		int numDatos = (pagina - 1) * porPagina;
		logger.info("Termino paginado");
		return datos.subList(numDatos, numDatos + porPagina);
	}
}
