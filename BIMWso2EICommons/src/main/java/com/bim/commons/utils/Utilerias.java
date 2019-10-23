package com.bim.commons.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.apache.log4j.Logger;

public class Utilerias {

	private static final Logger logger = Logger.getLogger(Utilerias.class);
	
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

	public static JsonObject calculaTasa(JsonObject datos) {
		/*
			Inv_Plazo = Se obtiene de la inversión a renovar en la propiedad 'Inv_Plazo'.
			Inv_Cantid = Se obtiene de la inversión a renovar en la propiedad 'Inv_Cantid'.
			TasInv = Se obtiene al consultar el SP CLTAGRCACON.
			Par_DiBaIn = Se obtiene al consultar el SP SOPARAMSCON.
			Par_ISR = Cli_TasISR Se obtiene al consultar el SP CLCLIENTCON.
			Cli_CobISR = Se obtiene de consultar al SP CLCLIENTCON.
		*/
		int invPlazo = datos.get("Inv_Plazo").getAsInt();
		double invCantid = datos.get("Inv_Cantid").getAsDouble();
		double invTBruta = datos.get("TasInv").getAsDouble();
		double parDiBaIn = datos.get("Par_DiBaIn").getAsDouble();
		double parISR = datos.get("Par_ISR").getAsDouble();
		String cliCobISR = datos.get("Cli_CobISR").getAsString();

		double invCanBru = invCantid * invTBruta * invPlazo / (parDiBaIn * 100);

		double tasISR = 0;
		double tasNet = 0;
		double canNet = 0;

		if("S".equals(cliCobISR))
			tasISR = parISR / 10;

		tasNet = invTBruta - tasISR;
		canNet = invCantid * tasNet * invPlazo / (parDiBaIn * 100);

		double invCanISR = invCantid * tasISR * invPlazo / (parDiBaIn * 100);
		double invCanTot = invCantid + canNet;

		JsonObject resultado = new JsonObject();
		resultado.addProperty("Inv_Capita", invCantid);
		resultado.addProperty("Inv_CanBru", invCanBru);
		resultado.addProperty("Inv_ISR", redondear(tasISR, 2));
		resultado.addProperty("Inv_CanISR", invCanISR);
		resultado.addProperty("Inv_Tasa", redondear(tasNet, 2));
		resultado.addProperty("Inv_CanNet", canNet);
		resultado.addProperty("Inv_CanTot", invCanTot);

		return resultado;
	}

}
