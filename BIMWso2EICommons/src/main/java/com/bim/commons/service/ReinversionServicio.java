package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class ReinversionServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

	private static String ReinversionServicio;
	private static String FechaHabilConsultarOp;
	private static String FechaHabilConsultarOpFinSem;
	private static String FechaHabilConsultarOpTransaccio;
	private static String FechaHabilConsultarOpUsuari;
	private static String FechaHabilConsultarOpSucOrigen;
	private static String FechaHabilConsultarOpSucDestino;
	private static String FechaHabilConsultarOpModulo;
	
	public JsonObject fechaHabilConsultar(JsonObject datosFechaHabil) {
		datosFechaHabil.addProperty("FinSem", FechaHabilConsultarOpFinSem);
		datosFechaHabil.addProperty("Transaccio", FechaHabilConsultarOpTransaccio);
		datosFechaHabil.addProperty("Usuario", FechaHabilConsultarOpUsuari);
		datosFechaHabil.addProperty("SucOrigen", FechaHabilConsultarOpSucOrigen);
		datosFechaHabil.addProperty("SucDestino", FechaHabilConsultarOpSucDestino);
		datosFechaHabil.addProperty("Modulo", FechaHabilConsultarOpModulo);
		logger.info("datosFechaHabil" + datosFechaHabil);
		JsonObject fechaHabilConsultarOpResultadoObjeto = Utilerias.performOperacion(ReinversionServicio, FechaHabilConsultarOp, datosFechaHabil);
		return fechaHabilConsultarOpResultadoObjeto;
	}
}
