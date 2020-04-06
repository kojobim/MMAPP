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
	private static String CuentaOrigenConsultarOpTipConsulLC;
	private static String CuentaOrigenConsultarOpTransaccio;
	private static String CuentaOrigenConsultarOpUsuario;
	private static String CuentaOrigenConsultarOpSucOrigen;
	private static String CuentaOrigenConsultarOpSucDestino;
	private static String CuentaOrigenConsultarOpModulo;
	
	private static String CuentaDatosConsultarOp;
	private static String CuentaDatosConsultarTipConsul;
	private static String CuentaDatosConsultarTransaccio;
	private static String CuentaDatosConsultarUsuario;
	private static String CuentaDatosConsultarSucOrigen;
	private static String CuentaDatosConsultarSucDestino;
	private static String CuentaDatosConsultarOpModulo;
	
	
	public CuentaServicio() {
		super();
		
		CuentaServicio = properties.getProperty("data_service.cuenta_servicio");
		
		
		CuentaOrigenConsultarOp = properties.getProperty("cuenta_servicio.op.cuenta_origen_consultar");
		CuentaOrigenConsultarC1Op = properties.getProperty("cuenta_servicio.op.cuenta_origen_consultar_c1");
		
		CuentaDatosConsultarOp = properties.getProperty("cuenta_datos_servicio.op.cuenta_datos_consultar");
		
		
		CuentaOrigenConsultarOpTipConsulLC = properties.getProperty("op.cuenta_origen_consultar.tip_consul_lc");
		CuentaOrigenConsultarOpTransaccio = properties.getProperty("op.cuenta_origen_consultar.transaccio");
		CuentaOrigenConsultarOpUsuario = properties.getProperty("op.cuenta_origen_consultar.usuario");
		CuentaOrigenConsultarOpSucOrigen = properties.getProperty("op.cuenta_origen_consultar.suc_origen");
		CuentaOrigenConsultarOpSucDestino = properties.getProperty("op.cuenta_origen_consultar.suc_destino");
		CuentaOrigenConsultarOpModulo = properties.getProperty("op.cuenta_origen_consultar.modulo");
		
		CuentaDatosConsultarTransaccio = properties.getProperty("op.cuenta_datos_consultar.transaccio");
		CuentaDatosConsultarUsuario = properties.getProperty("op.cuenta_datos_consultar.usuario");
		CuentaDatosConsultarSucOrigen = properties.getProperty("op.cuenta_datos_consultar.suc_origen");
		CuentaDatosConsultarSucDestino = properties.getProperty("op.cuenta_datos_consultar.suc_destino");
		CuentaDatosConsultarOpModulo = properties.getProperty("op.cuenta_datos_consultar.modulo");
		CuentaDatosConsultarTipConsul = properties.getProperty("op.cuenta_datos_consultar.tip_consul");
	}
	
	/**
     * Método para obtener las cuentas origen de un usuario 
     * ProcedureName: CHCUENTACON
     * @param cuentaDatosConsultar
     * <pre>
     * {
     * 	Cue_Numero: String,
     * 	Cue_Client: String
     * 	Tip_Consul: String
     *  NumTransac: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
	 * <pre>
	 * { 
	 * 	cuenta: {
	 * 		Cue_Numero: String,
	 * 		Cue_Moneda: String,
	 * 		Mon_Descri: String,
	 * 		Mon_Abrevi: String,
	 * 		Tip_Descri: String,
	 * 		Cue_Dispon: Double,
	 * 		Cue_Clabe: String,
	 * 		Cue_Tipo: String,
	 * 		Cue_Reteni: Double	
	 * 	}
	 * }
	 */
	
	
	public JsonObject cuentaDatosConsultar(JsonObject cuentaDatosConsultar) {
		logger.info("COMMONS: Comenzando cuentaDatosConsultar metodo... ");
		
		if(!cuentaDatosConsultar.has("NumTransac")) {
			cuentaDatosConsultar.addProperty("NumTransac", "");
		} 
		
		cuentaDatosConsultar.addProperty("Tip_Consul", CuentaDatosConsultarTipConsul);
		cuentaDatosConsultar.addProperty("Transaccio", CuentaDatosConsultarTransaccio);
		cuentaDatosConsultar.addProperty("Usuario", CuentaDatosConsultarUsuario);
		cuentaDatosConsultar.addProperty("SucOrigen", CuentaDatosConsultarSucOrigen);
		cuentaDatosConsultar.addProperty("SucDestino", CuentaDatosConsultarSucDestino);
		cuentaDatosConsultar.addProperty("Modulo", CuentaDatosConsultarOpModulo);
		
		JsonObject cuentaDatosConsultarOpResultadoObjeto = Utilerias.performOperacion(CuentaServicio, CuentaDatosConsultarOp, cuentaDatosConsultar);
		logger.debug("cuentaDatosConsultarOpResultadoObjeto" + cuentaDatosConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando cuentaDatosConsultar metodo... ");
		
		return cuentaDatosConsultarOpResultadoObjeto;
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
		if(!datosCuentaOrigenConsultar.has("Tip_Consul") || datosCuentaOrigenConsultar.get("Tip_Consul").getAsString().isEmpty())
			datosCuentaOrigenConsultar.addProperty("Tip_Consul", CuentaOrigenConsultarOpTipConsulLC);
		datosCuentaOrigenConsultar.addProperty("Transaccio", CuentaOrigenConsultarOpTransaccio);
		datosCuentaOrigenConsultar.addProperty("Usuario", CuentaOrigenConsultarOpUsuario);
		datosCuentaOrigenConsultar.addProperty("SucOrigen", CuentaOrigenConsultarOpSucOrigen);
		datosCuentaOrigenConsultar.addProperty("SucDestino", CuentaOrigenConsultarOpSucDestino);
		datosCuentaOrigenConsultar.addProperty("Modulo", CuentaOrigenConsultarOpModulo);
		
		String cuentaOrigenConsultarOperacion = "";
		
		if(datosCuentaOrigenConsultar.get("Tip_Consul").getAsString().equals("C1"))
			cuentaOrigenConsultarOperacion = CuentaOrigenConsultarC1Op;
		else
			cuentaOrigenConsultarOperacion = CuentaOrigenConsultarOp;
		
		JsonObject cuentaOrigenConsultarOpResultadoObjeto = Utilerias.performOperacion(CuentaServicio, cuentaOrigenConsultarOperacion, datosCuentaOrigenConsultar);
		logger.info("cuentaOrigenConsultarOpResultadoObjeto" + cuentaOrigenConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando cuentaOrigenConsultar metodo... ");
		return cuentaOrigenConsultarOpResultadoObjeto;
	}
}
