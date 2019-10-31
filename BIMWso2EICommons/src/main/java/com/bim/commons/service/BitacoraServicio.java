package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre la bitácora
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class BitacoraServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(BitacoraServicio.class);
 
	private static String BitacoraServicio;
	private static String BitacoraCreacionOp;
	private static String BitacoraCreacionOpBitTipOpe;
	private static String BitacoraCreacionOpTransaccio;
	private static String BitacoraCreacionOpUsuario;
	private static String BitacoraCreacionOpSucOrigen;
	private static String BitacoraCreacionOpSucDestino;
	private static String BitacoraCreacionOpModulo;
	
	public BitacoraServicio() {
		super();

		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		
		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
		BitacoraCreacionOpBitTipOpe = properties.getProperty("op.bitacora_creacion.bit_tipope");
		BitacoraCreacionOpModulo = properties.getProperty("op.bitacora_creacion.transaccio");
		BitacoraCreacionOpSucDestino = properties.getProperty("op.bitacora_creacion.usuario");
		BitacoraCreacionOpSucOrigen = properties.getProperty("op.bitacora_creacion.suc_origen");
		BitacoraCreacionOpTransaccio = properties.getProperty("op.bitacora_creacion.suc_destino");
		BitacoraCreacionOpUsuario = properties.getProperty("op.bitacora_creacion.modulo");
	}
	
	
	/**
	 * Método de creación de bitácora
	 * ProcedureName: NBBITACOALT
	 * @param datosBitacora
	 * <pre> 
	 * { 
	 *	Bit_Usuari: String,	 
	 *	Bit_Fecha:  String,
	 *	Bit_NumTra?: String,
	 *	Bit_TipOpe: String,
	 *	Bit_CueOri?: String,
	 *	Bit_CueDes?: String,
	 *	Bit_Monto: Integer,
	 *	Bit_PriRef: String,
	 *	Bit_SegRef?: String,
	 *	Bit_DireIP: String,
	 *	NumTransac: String,
	 *	Transaccio: String,
	 *	Usuario: String,
	 *	FechaSis: String,
	 *	SucOrigen: String,
	 *	SucDestino: String,
	 *	Modulo: String
	 * }
	 * </pre>
	 * @return 
	 * { }
	 */
	public JsonObject creacionBitacora(JsonObject datosBitacora) {
		logger.info("COMMONS: Comenzando creacionBitacora...");
		if(!datosBitacora.has("Bit_NumTra"))
			datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		if(!datosBitacora.has("Bit_CueOri"))
			datosBitacora.addProperty("Bit_CueOri", "");
		if(!datosBitacora.has("Bit_CueDes"))
			datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", 0);
		if(!datosBitacora.has("Bit_SegRef"))
			datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);
		JsonObject result = Utilerias
				.performOperacion(BitacoraServicio, BitacoraCreacionOp, 
						datosBitacora);
		logger.info("COMMONS: Finalizando creacionBitacora...");
		return result;
	}//Cierre del método
	
}