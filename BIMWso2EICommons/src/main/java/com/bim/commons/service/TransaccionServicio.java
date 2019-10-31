package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre transacción
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class TransaccionServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(TransaccionServicio.class);

	private static String TransaccionServicio;
	private static String FolioTransaccionGenerarOp;
	private static String FolioTransaccionGenerarOpSucOrigen;

	public TransaccionServicio() {
		super();

		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");

		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		FolioTransaccionGenerarOpSucOrigen = properties.getProperty("op.folio_transaccion_generar.suc_origen");
	}
	/**
	 * Método para generar folio de transacción
	 * ProcedureName: SYINITRANSA (Num_Transa: String, SucOrigen: String)
	 * @return 
	 * <pre>
	 * {
	 * 	transaccion: {
	 * 		Fol_Transa: String
	 * 	}
	 * }
	 * </pre>
	 */
	public JsonObject folioTransaccionGenerar() {
		logger.info("COMMONS: Comenzando folioTransaccionGenerar... ");
		JsonObject datosFolioTransaccion = new JsonObject();
		datosFolioTransaccion.addProperty("Num_Transa", "");
		datosFolioTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		JsonObject folioTransaccionGenerarResultadoObjeto =  Utilerias
				.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, 
						datosFolioTransaccion);
		logger.info("COMMONS: Finalizando folioTransaccionGenerar... ");
		return folioTransaccionGenerarResultadoObjeto;
	}//Cierre del método
}
