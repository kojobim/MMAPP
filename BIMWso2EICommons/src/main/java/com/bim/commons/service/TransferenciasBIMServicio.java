package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class TransferenciasBIMServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(TransferenciasBIMServicio.class);
	private static String TransferenciasBIMServicio;
	private static String CuentasOrigenConsultarOp;
	private static String CuentasOrigenConsultarOpTipConsul;
	private static String CuentasOrigenConsultarOpTransaccio;
	private static String CuentasOrigenConsultarOpUsuario;
	private static String CuentasOrigenConsultarOpSucOrigen;
	private static String CuentasOrigenConsultarOpSucDestino;
	private static String CuentasOrigenConsultarOpModulo;
	
	public TransferenciasBIMServicio() {
		super();
		
		TransferenciasBIMServicio = properties.getProperty("data_service.transferencias_bim_servicio");
		
		CuentasOrigenConsultarOp = properties.getProperty("transferencias_bim_servicio.op.cuentas_origen_consultar");
		
		CuentasOrigenConsultarOpTipConsul = properties.getProperty("op.cuentas_origen_consultar.tip_consul");
		CuentasOrigenConsultarOpTransaccio = properties.getProperty("op.cuentas_origen_consultar.transaccio");
		CuentasOrigenConsultarOpUsuario = properties.getProperty("op.cuentas_origen_consultar.usuario");
		CuentasOrigenConsultarOpSucOrigen = properties.getProperty("op.cuentas_origen_consultar.suc_origen");
		CuentasOrigenConsultarOpSucDestino = properties.getProperty("op.cuentas_origen_consultar.suc_destino");
		CuentasOrigenConsultarOpModulo = properties.getProperty("op.cuentas_origen_consultar.modulo");
		
	}
	
	public JsonObject cuentasOrigenConsultar(JsonObject datosCuentasOrigenConsultar) {
		datosCuentasOrigenConsultar.addProperty("Cor_Cuenta", "");
		datosCuentasOrigenConsultar.addProperty("Cor_Moneda", "");
		datosCuentasOrigenConsultar.addProperty("Cor_CliUsu", "");
		datosCuentasOrigenConsultar.addProperty("Usu_SucMod", "");
		datosCuentasOrigenConsultar.addProperty("NumTransac", "");
		datosCuentasOrigenConsultar.addProperty("Tip_Consul", CuentasOrigenConsultarOpTipConsul);
		datosCuentasOrigenConsultar.addProperty("Transaccio", CuentasOrigenConsultarOpTransaccio);
		datosCuentasOrigenConsultar.addProperty("Usuario", CuentasOrigenConsultarOpUsuario);
		datosCuentasOrigenConsultar.addProperty("SucOrigen", CuentasOrigenConsultarOpSucOrigen);
		datosCuentasOrigenConsultar.addProperty("SucDestino", CuentasOrigenConsultarOpSucDestino);
		datosCuentasOrigenConsultar.addProperty("Modulo", CuentasOrigenConsultarOpModulo);
		JsonObject datosCuentasOrigenConsultarOpResultadoObjeto = Utilerias.performOperacion(TransferenciasBIMServicio, CuentasOrigenConsultarOp, datosCuentasOrigenConsultar);
		logger.info("datosCuentasOrigenConsultarOpResultadoObjeto" + datosCuentasOrigenConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando datosCuentasOrigenConsultar metodo... ");
		return datosCuentasOrigenConsultarOpResultadoObjeto;
	}
}
