package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class InversionesServicio extends BaseService {

	private static String InversionesServicio;
	private static String InversionesObtenerOp;
	private static String InversionesObtenerOpInvMoneda;
	private static String InversionesObtenerOpTransaccio;
	private static String InversionesObtenerOpUsuario;
	private static String InversionesObtenerOpSucOrigen;
	private static String InversionesObtenerOpSucDestino;
	private static String InversionesObtenerOpModulo;
	private static String InversionesPagareNumeroUsuarioObtenerOp;
	private static String InversionesPagareNumeroUsuarioObtenerOpTipConsul;
	private static String InversionesPagareNumeroUsuarioObtenerOpTransaccio;
	private static String InversionesPagareNumeroUsuarioObtenerOpUsuario;
	private static String InversionesPagareNumeroUsuarioObtenerOpSucOrigen;
	private static String InversionesPagareNumeroUsuarioObtenerOpSucDestino;
	private static String InversionesPagareNumeroUsuarioObtenerOpModulo;
	
	public InversionesServicio() {
		super();

		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");

		InversionesObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_obtener");

		InversionesObtenerOpInvMoneda = properties.getProperty("op.inversiones_obtener.inv_moneda");
		InversionesObtenerOpTransaccio = properties.getProperty("op.inversiones_obtener.transaccio");
		InversionesObtenerOpUsuario = properties.getProperty("op.inversiones_obtener.usuario");
		InversionesObtenerOpSucOrigen = properties.getProperty("op.inversiones_obtener.suc_origen");
		InversionesObtenerOpSucDestino = properties.getProperty("op.inversiones_obtener.suc_destino");
		InversionesObtenerOpModulo = properties.getProperty("op.inversiones_obtener.modulo");

		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_numero_usuario_obtener");

		InversionesPagareNumeroUsuarioObtenerOpTipConsul = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.tip_consul");
		InversionesPagareNumeroUsuarioObtenerOpTransaccio = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.transaccio");
		InversionesPagareNumeroUsuarioObtenerOpUsuario = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.usuario");
		InversionesPagareNumeroUsuarioObtenerOpSucOrigen = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_origen");
		InversionesPagareNumeroUsuarioObtenerOpSucDestino = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_destino");
		InversionesPagareNumeroUsuarioObtenerOpModulo = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.modulo");

	}

	public JsonObject inversionesObtener(JsonObject datosInversionesObtener) {
		datosInversionesObtener.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
		datosInversionesObtener.addProperty("NumTransac", "");
		datosInversionesObtener.addProperty("Transaccio", InversionesObtenerOpTransaccio);
		datosInversionesObtener.addProperty("Usuario", InversionesObtenerOpUsuario);
		datosInversionesObtener.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
		datosInversionesObtener.addProperty("SucDestino", InversionesObtenerOpSucDestino);
		datosInversionesObtener.addProperty("Modulo", InversionesObtenerOpModulo);
		JsonObject inversionesObtenerOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesObtenerOp, datosInversionesObtener);
		return inversionesObtenerOpResultadoObjeto;
	}
	
	public JsonObject inversionesPagareNumeroUsuarioObtener(JsonObject datosInversionesPagareNumeroUsuarioObtener) {
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Inv_Numero", "");
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = Utilerias.performOperacion(InversionesServicio, InversionesPagareNumeroUsuarioObtenerOp, datosInversionesPagareNumeroUsuarioObtener);
		return inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto;
	}
}