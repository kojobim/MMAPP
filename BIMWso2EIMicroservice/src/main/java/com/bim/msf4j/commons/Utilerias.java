package com.bim.msf4j.commons;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Utilerias {
	
	private static final Logger logger = Logger.getLogger(Utilerias.class);
	private static DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd");

	public static List<Object> paginado(List<Object> datos, int pagina, int porPagina) {
		logger.info("Inicio paginado");
		int numDatos = (pagina - 1) * porPagina;
		logger.info("Termino paginado");
		return datos.subList(numDatos, numDatos + porPagina);
	}

	public static Boolean calcularVencimiento(String fecha) {
		logger.info("COMMONS: Iniciando calcularVencimiento...");
		
		String fechaAct = dtfOut.print(DateTime.now());
		logger.info("Fecha actual: " + fechaAct);
		String fechaConv = convertirFechaAFormatoSimple(fecha);
		String invFecVen = dtfOut.print(DateTime.parse(fechaConv));
		logger.info("Fecha de vencimiento: " + invFecVen);
		
		int numDias = Days.daysBetween(DateTime.parse(fechaAct), DateTime.parse(invFecVen)).getDays();
		logger.info("COMMONS: Finalizando calcularVencimiento...");
		if(numDias == 1)
    		return true;
		
		return false;
	}
	
	public static double redondear(double cantidad, int decimales) {
		logger.info("COMMONS: Iniciando redondear...");
	    double escala = Math.pow(10, decimales);
	    logger.info("COMMONS: Finalizando redondear...");
    	return Math.round(cantidad * escala) / escala;
	}
	
	public static String convertirFechaAFormatoSimple(String fecha) {
		logger.info("COMMONS: Iniciando convertirFecha...");
		String fechaConv = fecha.trim();
		if(fechaConv.length() == 10 && fechaConv.contains("/")) {
			fechaConv = fechaConv.substring(6)
				+ "-" + fechaConv.substring(3, 5)
				+ "-" + fechaConv.substring(0, 2);
		}
		
		logger.info("COMMONS: Finalizando convertirFecha...");
		return fechaConv;
	}
}
