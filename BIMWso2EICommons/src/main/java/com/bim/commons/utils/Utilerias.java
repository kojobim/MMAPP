package com.bim.commons.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.apache.log4j.Logger;
// import org.joda.time.format.DateTimeFormat;
// import org.joda.time.format.DateTimeFormatter;

public class Utilerias {

	private static final Logger logger = Logger.getLogger(Utilerias.class);
	// private static DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@SuppressWarnings("unchecked")
	public static JsonArray paginado(JsonArray datos, int page, int per_page) {
		logger.info("Inicio paginado");
		ArrayList<JsonElement> list = (ArrayList<JsonElement>)new Gson().fromJson(datos.toString(), ArrayList.class);
		if(per_page > list.size())
			return datos; 
		int numDatos = (page - 1) * per_page;
		List<JsonElement> listResult = list.subList(numDatos, numDatos + per_page);
		logger.info("Termino paginado");
		return new Gson().fromJson(new Gson().toJson(listResult), JsonArray.class);
	}
	   
	public static Boolean calcularVencimiento(Date fechaVen) {
		logger.info("COMMONS: Iniciando calcularVencimiento...");

		if(fechaVen == null)
				return false;
		
		Date fechaActual = new Date();
		
		if(fechaVen.compareTo(fechaActual) == 0)
			return true;

		return false;
		// String fechaAct = dtfOut.print(DateTime.now());
		// logger.info("Fecha actual: " + fechaAct);
		// String fechaConv = convertirFechaAFormatoSimple(fecha);
		// String invFecVen = dtfOut.print(DateTime.parse(fechaConv));
		// logger.info("Fecha de vencimiento: " + invFecVen);
		
		// int numDias = Days.daysBetween(DateTime.parse(fechaAct), DateTime.parse(invFecVen)).getDays();
		// logger.info("COMMONS: Finalizando calcularVencimiento...");
		// if(numDias == 0)
    	// 	return true;
		
		// return false;
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
