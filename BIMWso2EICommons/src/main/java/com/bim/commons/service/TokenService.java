package com.bim.commons.service;

import java.util.Properties;
import org.apache.log4j.Logger;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.HttpClientUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Clase para realizar operaciones que requieran token
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 */
public class TokenService {

    private static final Logger logger = Logger.getLogger(TokenService.class);
    private static Properties properties;
    
    static {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * Método que se encarga de validar el token de operación
	 * @param tokFolio String
	 * @param cpRSAToken String
	 * @param tokUsuari String
	 * @return
	 * String
	 */
    public static String validarTokenOperacion(String tokFolio, String cpRSAToken, String tokUsuari) {
		logger.info("COMMONS: Iniciando validarTokenTransaccion metodo...");

		String DataServiceHost = properties.getProperty("data_service.host");
		String TokenServicio = properties.getProperty("data_service.token_servicio");
		String IntentosActualizacionOp = properties.getProperty("token_servicio.op.intentos_actualizacion");
		String IntentosActualizacionOpTransaccio = properties.getProperty("op.intentos_actualizacion.transaccio");
		String IntentosActualizacionOpUsuario = properties.getProperty("op.intentos_actualizacion.usuario");
		String IntentosActualizacionOpSucOrigen = properties.getProperty("op.intentos_actualizacion.suc_origen");
		String IntentosActualizacionOpSucDestino = properties.getProperty("op.intentos_actualizacion.suc_destino");
		String IntentosActualizacionOpModulo = properties.getProperty("op.intentos_actualizacion.modulo");

		String clave = "0" + tokFolio + cpRSAToken;
		String validaToken = Racal.validaTokenOpera(clave);
		String tipActual = "01".equals(validaToken) ? "2" : "1";
		String usuStatus = "01".equals(validaToken) ? "C" : "A";

		JsonObject datosToken = new JsonObject();
		datosToken.addProperty("Tok_Folio", tokFolio);
		datosToken.addProperty("Tok_UsuAdm", "");
		datosToken.addProperty("Tok_Usuari", tokUsuari);
		datosToken.addProperty("Tok_ComCan", "");
		datosToken.addProperty("Tip_Actual", tipActual);
		datosToken.addProperty("NumTransac", "");
		datosToken.addProperty("Transaccio", IntentosActualizacionOpTransaccio);
		datosToken.addProperty("Usuario", IntentosActualizacionOpUsuario);
		datosToken.addProperty("FechaSis", "");
		datosToken.addProperty("SucOrigen", IntentosActualizacionOpSucOrigen);
		datosToken.addProperty("SucDestino", IntentosActualizacionOpSucDestino);
		datosToken.addProperty("Modulo", IntentosActualizacionOpModulo);
		
		StringBuilder intentosActualizacionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TokenServicio)
				.append("/")
				.append(IntentosActualizacionOp);

		JsonObject intentosActualizacionOp = new JsonObject();
		intentosActualizacionOp.add("intentosActualizacionOp", datosToken);

		RequestDTO intentosActualizacionOpSolicitud = new RequestDTO();
		intentosActualizacionOpSolicitud.setUrl(intentosActualizacionUrl.toString());
		MessageProxyDTO intentosActualizacionOpMensaje = new MessageProxyDTO();
		intentosActualizacionOpMensaje.setBody(intentosActualizacionOp.toString());
		intentosActualizacionOpSolicitud.setMessage(intentosActualizacionOpMensaje);

		String intentosActualizacionOpResultado = HttpClientUtils.postPerform(intentosActualizacionOpSolicitud);
		JsonObject intentosActualizacionOpResultadoObjecto = new Gson().fromJson(intentosActualizacionOpResultado, JsonObject.class);
		JsonObject intentosActualizacion = intentosActualizacionOpResultadoObjecto.has("intentosActualizacion")
			? intentosActualizacionOpResultadoObjecto.get("intentosActualizacion").getAsJsonObject() : new JsonObject();
		
		if(intentosActualizacion.has("Usu_Status"))
			usuStatus = intentosActualizacion.get("Usu_Status").getAsString();

		return usuStatus;
	}
	//Cierre del método

}