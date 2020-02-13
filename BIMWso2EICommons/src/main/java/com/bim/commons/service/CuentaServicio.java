package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class CuentaServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicio.class);
	
	private static String CuentaServicio;
	private static String CuentaOrigenConsultarOp;
	private static String CuentaOrigenConsultarC1Op;
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
		CuentaOrigenConsultarC1Op = properties.getProperty("cuenta_servicio.op.cuenta_origen_consultar_c1");
		
		CuentaOrigenConsultarOpTipConsul = properties.getProperty("op.cuenta_origen_consultar.tip_consul");
		CuentaOrigenConsultarOpTransaccio = properties.getProperty("op.cuenta_origen_consultar.transaccio");
		CuentaOrigenConsultarOpUsuario = properties.getProperty("op.cuenta_origen_consultar.usuario");
		CuentaOrigenConsultarOpSucOrigen = properties.getProperty("op.cuenta_origen_consultar.suc_origen");
		CuentaOrigenConsultarOpSucDestino = properties.getProperty("op.cuenta_origen_consultar.suc_destino");
		CuentaOrigenConsultarOpModulo = properties.getProperty("op.cuenta_origen_consultar.modulo");
	}

	/**
     * Método para obtener las cuentas origen de un usuario 
     * ProcedureName: NBCUEORICON
     * @param datosCuentaOrigenConsultar
     * <pre>
     * {
     * 	Cor_Usuari: String,
     * 	Cor_Cuenta?: String,
     * 	Tip_Consul?: String
     *  NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
	 * <pre>
	 * { 
	 * 	cuentas: {
	 * 		cuenta: [
	 * 			{
	 * 				Cor_Cuenta: String,
	 * 				Cor_Produc: String,
	 * 				Cor_DeCuOr: String,
	 * 				Cue_Moneda: String,
	 * 				Cor_Tipo: String,
	 * 				Cue_Dispon: Double,
	 * 				Cor_TipCli: String,
	 * 				Cor_NoCuOr: String,
	 * 				Cor_Alias: String,
	 * 				Cue_NumFor: String,
	 * 				Tip_Descri: String
	 * 			}
	 * 		]
	 * 	}
	 * }
	 * </pre>
	 * or
	 * <pre>
	 * { 
	 * 	cuenta: {
	 * 		Cor_Cuenta: String,
	 * 		Cor_Status: String,
	 * 		Cor_Alias: String,
	 * 		Cor_MoLiDi: String,
	 * 		Cor_MonDia: String,
	 * 		Cue_Moneda: String,
	 * 		Cli_Tipo: String
	 * 	}
	 * }
	 * </pre>
	 */
	public JsonObject cuentaOrigenConsultar(JsonObject datosCuentaOrigenConsultar) {
		logger.info("COMMONS: Comenzando cuentaOrigenConsultar metodo... ");
		if(!datosCuentaOrigenConsultar.has("Cor_Cuenta"))
			datosCuentaOrigenConsultar.addProperty("Cor_Cuenta", "");
		if(!datosCuentaOrigenConsultar.has("Cor_Moneda"))
			datosCuentaOrigenConsultar.addProperty("Cor_Moneda", "");
		if(!datosCuentaOrigenConsultar.has("Cor_CliUsu"))
			datosCuentaOrigenConsultar.addProperty("Cor_CliUsu", "");
		if(!datosCuentaOrigenConsultar.has("Usu_SucMod"))
			datosCuentaOrigenConsultar.addProperty("Usu_SucMod", "");
		if(!datosCuentaOrigenConsultar.has("NumTransac"))
			datosCuentaOrigenConsultar.addProperty("NumTransac", "");
		if(!datosCuentaOrigenConsultar.has("Tip_Consul"))
			datosCuentaOrigenConsultar.addProperty("Tip_Consul", CuentaOrigenConsultarOpTipConsul);
		datosCuentaOrigenConsultar.addProperty("Transaccio", CuentaOrigenConsultarOpTransaccio);
		datosCuentaOrigenConsultar.addProperty("Usuario", CuentaOrigenConsultarOpUsuario);
		datosCuentaOrigenConsultar.addProperty("SucOrigen", CuentaOrigenConsultarOpSucOrigen);
		datosCuentaOrigenConsultar.addProperty("SucDestino", CuentaOrigenConsultarOpSucDestino);
		datosCuentaOrigenConsultar.addProperty("Modulo", CuentaOrigenConsultarOpModulo);
		
		if(datosCuentaOrigenConsultar.get("Tip_Consul").getAsString().equals("C1"))
			CuentaOrigenConsultarOp = CuentaOrigenConsultarC1Op;
		
		JsonObject cuentaOrigenConsultarOpResultadoObjeto = Utilerias.performOperacion(CuentaServicio, CuentaOrigenConsultarOp, datosCuentaOrigenConsultar);
		logger.info("cuentaOrigenConsultarOpResultadoObjeto" + cuentaOrigenConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando cuentaOrigenConsultar metodo... ");
		return cuentaOrigenConsultarOpResultadoObjeto;
	}
}
