package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre el token
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class TokenServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(TokenServicio.class);

	private static String TokenServicio;
	
	private static String TokenVerificarOp;
	private static String IntentosActualizacionOp;

	private static String TokenVerificarOpUsuario;
	private static String TokenVerificarOpTransaccio;
	private static String TokenVerificarOpSucOrigen;
	private static String TokenVerificarOpSucDestino;
	private static String TokenVerificarOpModulo;

	private static String IntentosActualizacionOpTransaccio;
	private static String IntentosActualizacionOpUsuario;
	private static String IntentosActualizacionOpSucOrigen;
	private static String IntentosActualizacionOpSucDestino;
	private static String IntentosActualizacionOpModulo;
	
	public TokenServicio() {
		super();

		TokenServicio = properties.getProperty("data_service.token_servicio");
		
		TokenVerificarOp = properties.getProperty("token_servicio.op.token_verificar");
		TokenVerificarOpUsuario = properties.getProperty("op.token_verificar.usuario");
		TokenVerificarOpTransaccio = properties.getProperty("op.token_verificar.transaccio");
		TokenVerificarOpSucOrigen = properties.getProperty("op.token_verificar.suc_origen");
		TokenVerificarOpSucDestino = properties.getProperty("op.token_verificar.suc_destino");
		TokenVerificarOpModulo = properties.getProperty("op.token_verificar.modulo");
		
		TokenServicio = properties.getProperty("data_service.token_servicio");
		IntentosActualizacionOp = properties.getProperty("token_servicio.op.intentos_actualizacion");
		IntentosActualizacionOpTransaccio = properties.getProperty("op.intentos_actualizacion.transaccio");
		IntentosActualizacionOpUsuario = properties.getProperty("op.intentos_actualizacion.usuario");
		IntentosActualizacionOpSucOrigen = properties.getProperty("op.intentos_actualizacion.suc_origen");
		IntentosActualizacionOpSucDestino = properties.getProperty("op.intentos_actualizacion.suc_destino");
		IntentosActualizacionOpModulo = properties.getProperty("op.intentos_actualizacion.modulo");
	}
	
	/**
	 * Método para verificar el token
	 * ProcedureName: NBTOKVENCON
	 * @param datosTokenVerificar
	 * <pre> 
	 * {
	 * 	Tov_Serie: String,
	 * 	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return 
	 * <pre>
	 * { 
	 *	tokenVerificar: {
	 *		Tov_FecVen: Date
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject tokenVerificar(JsonObject datosTokenVerificar) {
		logger.info("COMMONS: Comenzando tokenVerificar metodo... ");
		if(!datosTokenVerificar.has("NumTransac"))
			datosTokenVerificar.addProperty("NumTransac", "");
		datosTokenVerificar.addProperty("Transaccio", TokenVerificarOpTransaccio);
		datosTokenVerificar.addProperty("Usuario", TokenVerificarOpUsuario);
		datosTokenVerificar.addProperty("SucOrigen", TokenVerificarOpSucOrigen);
		datosTokenVerificar.addProperty("SucDestino", TokenVerificarOpSucDestino);
		datosTokenVerificar.addProperty("Modulo", TokenVerificarOpModulo);
		JsonObject tokenVerificarOpResultadoObjecto = Utilerias.performOperacion(TokenServicio, TokenVerificarOp, datosTokenVerificar);
		logger.info("COMMONS: Finalizando tokenVerificar metodo... ");
		return tokenVerificarOpResultadoObjecto;
	}//Cierre del método

	/**
	 * Método que se encarga de validar el token de operación
	 * ProcedureName: NBTOKENACT
	 * @param tokFolio String
	 * @param cpRSAToken String
	 * @param tokUsuari String
	 * @param numTransac
	 * @param scriptName
	 * @return
	 * String
	 */
    public String validarTokenOperacion(String tokFolio, String cpRSAToken, String tokUsuari, String numTransac, String scriptName) {
		logger.info("COMMONS: Comenzando validarTokenOperacion metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		String clave = tokFolio + cpRSAToken;
		String validaToken = Racal.validaTokenOpera(clave, scriptName);
		String tipActual = !"00".equals(validaToken) ? "2" : "1";
		String usuStatus = !"00".equals(validaToken) ? "C" : "A";

		JsonObject datosToken = new JsonObject();
		datosToken.addProperty("Tok_Folio", tokFolio);
		if(!datosToken.has("Tok_UsuAdm"))
			datosToken.addProperty("Tok_UsuAdm", "");
		datosToken.addProperty("Tok_Usuari", tokUsuari);
		if(!datosToken.has("Tok_ComCan"))
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
		
		Utilerias.verificarError(intentosActualizacionOpResultadoObjecto);		
		JsonObject intentosActualizacion = Utilerias.obtenerJsonObjectPropiedad(intentosActualizacionOpResultadoObjecto, "intentosActualizacion");
		
		if(intentosActualizacion != null && intentosActualizacion.has("Usu_Status"))
			usuStatus = intentosActualizacion.get("Usu_Status").getAsString();

		logger.info("COMMONS: Finalizando validarTokenOperacion metodo...");
		return usuStatus;
	}//Cierre del método
}
