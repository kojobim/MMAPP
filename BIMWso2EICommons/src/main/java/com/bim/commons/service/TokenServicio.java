package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

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
	
	public JsonObject tokenVerificar(JsonObject datosTokenVerificar) {
		datosTokenVerificar.addProperty("NumTransac", "");
		datosTokenVerificar.addProperty("Transaccio", TokenVerificarOpTransaccio);
		datosTokenVerificar.addProperty("Usuario", TokenVerificarOpUsuario);
		datosTokenVerificar.addProperty("SucOrigen", TokenVerificarOpSucOrigen);
		datosTokenVerificar.addProperty("SucDestino", TokenVerificarOpSucDestino);
		datosTokenVerificar.addProperty("Modulo", TokenVerificarOpModulo);
		JsonObject tokenVerificarOpResultadoObjecto = Utilerias.performOperacion(TokenServicio, TokenVerificarOp, datosTokenVerificar);
		return tokenVerificarOpResultadoObjecto;
	}

}
