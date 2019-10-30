package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class TransaccionServicio extends BaseService {

	private static String TransaccionServicio;
	
	private static String FolioTransaccionGenerarOp;
	private static String FolioTransaccionGenerarOpSucOrigen;

	public TransaccionServicio() {
		super();

		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");

		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		FolioTransaccionGenerarOpSucOrigen = properties.getProperty("op.folio_transaccion_generar.suc_origen");
	}
	
	public JsonObject folioTransaccionGenerar() {
		JsonObject datosFolioTransaccion = new JsonObject();
		datosFolioTransaccion.addProperty("Num_Transa", "");
		datosFolioTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		JsonObject folioTransaccionGenerarResultadoObjeto =  Utilerias
				.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, 
						datosFolioTransaccion);
		return folioTransaccionGenerarResultadoObjeto;
	}
}
