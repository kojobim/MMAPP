package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Clase para realizar operaciones que requieran token
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 */
public class TokenService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

	/**
	 * Método que se encarga de validar el token de operación
	 * @param tokFolio String
	 * @param cpRSAToken String
	 * @param tokUsuari String
	 * @return
	 * String
	 */
    public String validarTokenOperacion(String tokFolio, String cpRSAToken, String tokUsuari, String numTransac) {
		logger.info("COMMONS: Iniciando validarTokenTransaccion metodo...");
		
		String TokenServicio = properties.getProperty("data_service.token_servicio");
		String IntentosActualizacionOp = properties.getProperty("token_servicio.op.intentos_actualizacion");
		String IntentosActualizacionOpTransaccio = properties.getProperty("op.intentos_actualizacion.transaccio");
		String IntentosActualizacionOpUsuario = properties.getProperty("op.intentos_actualizacion.usuario");
		String IntentosActualizacionOpSucOrigen = properties.getProperty("op.intentos_actualizacion.suc_origen");
		String IntentosActualizacionOpSucDestino = properties.getProperty("op.intentos_actualizacion.suc_destino");
		String IntentosActualizacionOpModulo = properties.getProperty("op.intentos_actualizacion.modulo");

		String fechaSis = Utilerias.obtenerFechaSis();
		String clave = "0" + tokFolio + cpRSAToken;
		String validaToken = Racal.validaTokenOpera(clave);
		String tipActual = !"00".equals(validaToken) ? "2" : "1";
		String usuStatus = !"00".equals(validaToken) ? "C" : "A";

		JsonObject datosToken = new JsonObject();
		datosToken.addProperty("Tok_Folio", tokFolio);
		datosToken.addProperty("Tok_UsuAdm", "");
		datosToken.addProperty("Tok_Usuari", tokUsuari);
		datosToken.addProperty("Tok_ComCan", "");
		datosToken.addProperty("Tip_Actual", tipActual);
		datosToken.addProperty("NumTransac", numTransac);
		datosToken.addProperty("Transaccio", IntentosActualizacionOpTransaccio);
		datosToken.addProperty("Usuario", IntentosActualizacionOpUsuario);
		datosToken.addProperty("FechaSis", fechaSis);
		datosToken.addProperty("SucOrigen", IntentosActualizacionOpSucOrigen);
		datosToken.addProperty("SucDestino", IntentosActualizacionOpSucDestino);
		datosToken.addProperty("Modulo", IntentosActualizacionOpModulo);

		JsonObject intentosActualizacionOpResultadoObjecto = Utilerias.performOperacion(TokenServicio, IntentosActualizacionOp, datosToken);
		logger.info("intentosActualizacionOpResultadoObjecto: " + intentosActualizacionOpResultadoObjecto);
		JsonObject intentosActualizacion = Utilerias.obtenerJsonObjectPropiedad(intentosActualizacionOpResultadoObjecto, "intentosActualizacion");
		
		if(intentosActualizacion != null && intentosActualizacion.has("Usu_Status"))
			usuStatus = intentosActualizacion.get("Usu_Status").getAsString();

		logger.info("COMMONS: Terminando validarTokenTransaccion metodo...");
		return usuStatus;
	}//Cierre del método

}