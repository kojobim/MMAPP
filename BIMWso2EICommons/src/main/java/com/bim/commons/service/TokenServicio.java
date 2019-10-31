package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre el token
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class TokenServicio extends BaseService {

	private static String TokenServicio;
	private static String TokenVerificarOp;
	private static String TokenVerificarOpUsuario;
	private static String TokenVerificarOpTransaccio;
	private static String TokenVerificarOpSucOrigen;
	private static String TokenVerificarOpSucDestino;
	private static String TokenVerificarOpModulo;
	
	public TokenServicio() {
		super();

		TokenServicio = properties.getProperty("data_service.token_servicio");
		
		TokenVerificarOp = properties.getProperty("token_servicio.op.token_verificar");
		TokenVerificarOpUsuario = properties.getProperty("op.token_verificar.usuario");
		TokenVerificarOpTransaccio = properties.getProperty("op.token_verificar.transaccio");
		TokenVerificarOpSucOrigen = properties.getProperty("op.token_verificar.suc_origen");
		TokenVerificarOpSucDestino = properties.getProperty("op.token_verificar.suc_destino");
		TokenVerificarOpModulo = properties.getProperty("op.token_verificar.modulo");
	}
	
	/**
	 * Método para verificar el token
	 * ProcedureName: NBTOKVENCON
	 * @param datosTokenVerificar
	 * <pre> 
	 * {
	 * 	Tov_Serie: String,
	 * 	NumTransac?: String,
	 *	Transaccio: String, 
	 *	Usuario: String,
	 *	FechaSis: String, 
	 *	SucOrigen: String, 
	 *	SucDestino: String, 
	 *	Modulo: String 
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
		if(!datosTokenVerificar.has("NumTransac"))
			datosTokenVerificar.addProperty("NumTransac", "");
		datosTokenVerificar.addProperty("Transaccio", TokenVerificarOpTransaccio);
		datosTokenVerificar.addProperty("Usuario", TokenVerificarOpUsuario);
		datosTokenVerificar.addProperty("SucOrigen", TokenVerificarOpSucOrigen);
		datosTokenVerificar.addProperty("SucDestino", TokenVerificarOpSucDestino);
		datosTokenVerificar.addProperty("Modulo", TokenVerificarOpModulo);
		JsonObject tokenVerificarOpResultadoObjecto = Utilerias.performOperacion(TokenServicio, TokenVerificarOp, datosTokenVerificar);
		return tokenVerificarOpResultadoObjecto;
	}//Cierre del método

}
