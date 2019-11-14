package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class CuentaServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicio.class);
	
	private static String CuentaServicio;
	private static String CuentaOrigenConsultarOp;
	private static String CuentaOrigenConsultarOpTipConsul;
	private static String CuentaOrigenConsultarOpTransaccio;
	private static String CuentaOrigenConsultarOpUsuario;
	private static String CuentaOrigenConsultarOpSucOrigen;
	private static String CuentaOrigenConsultarOpSucDestino;
	private static String CuentaOrigenConsultarOpModulo;
	
	public CuentaServicio() {
		super();
		
		CuentaServicio = properties.getProperty("data_service.cuenta_servicio");
		
		CuentaOrigenConsultarOp = properties.getProperty("cuenta_servicio.op.cuenta_origen_consultar");
		
		CuentaOrigenConsultarOpTipConsul = properties.getProperty("op.cuenta_origen_consultar.tip_consul");
		CuentaOrigenConsultarOpTransaccio = properties.getProperty("op.cuenta_origen_consultar.transaccio");
		CuentaOrigenConsultarOpUsuario = properties.getProperty("op.cuenta_origen_consultar.usuario");
		CuentaOrigenConsultarOpSucOrigen = properties.getProperty("op.cuenta_origen_consultar.suc_origen");
		CuentaOrigenConsultarOpSucDestino = properties.getProperty("op.cuenta_origen_consultar.suc_destino");
		CuentaOrigenConsultarOpModulo = properties.getProperty("op.cuenta_origen_consultar.modulo");
	}

	public JsonObject cuentaOrigenConsultar(JsonObject datosCuentaOrigenConsultar) {
		logger.info("COMMONS: Comenzando cuentaOrigenConsultar metodo... ");
		datosCuentaOrigenConsultar.addProperty("Cor_Cuenta", "");
		datosCuentaOrigenConsultar.addProperty("Cor_Moneda", "");
		datosCuentaOrigenConsultar.addProperty("Cor_CliUsu", "");
		datosCuentaOrigenConsultar.addProperty("Usu_SucMod", "");
		datosCuentaOrigenConsultar.addProperty("NumTransac", "");
		datosCuentaOrigenConsultar.addProperty("Tip_Consul", CuentaOrigenConsultarOpTipConsul);
		datosCuentaOrigenConsultar.addProperty("Transaccio", CuentaOrigenConsultarOpTransaccio);
		datosCuentaOrigenConsultar.addProperty("Usuario", CuentaOrigenConsultarOpUsuario);
		datosCuentaOrigenConsultar.addProperty("SucOrigen", CuentaOrigenConsultarOpSucOrigen);
		datosCuentaOrigenConsultar.addProperty("SucDestino", CuentaOrigenConsultarOpSucDestino);
		datosCuentaOrigenConsultar.addProperty("Modulo", CuentaOrigenConsultarOpModulo);
		JsonObject cuentaOrigenConsultarOpResultadoObjeto = Utilerias.performOperacion(CuentaServicio, CuentaOrigenConsultarOp, datosCuentaOrigenConsultar);
		logger.info("cuentaOrigenConsultarOpResultadoObjeto" + cuentaOrigenConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando cuentaOrigenConsultar metodo... ");
		return cuentaOrigenConsultarOpResultadoObjeto;
	}
}
