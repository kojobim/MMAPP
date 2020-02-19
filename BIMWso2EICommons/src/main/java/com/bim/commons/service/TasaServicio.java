package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre la tasa
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class TasaServicio extends BaseService {
	
	private static final Logger logger = LoggerFactory.getLogger(TasaServicio.class);
	
	private static String TasaServicio;
	
	private static String TasaClienteConsultarOp;
	private static String TasaClienteConsultarOpInvMoneda;
	private static String TasaClienteConsultarOpTasa;
	private static String TasaClienteConsultarOpTransaccio;
	private static String TasaClienteConsultarOpUsuari;
	private static String TasaClienteConsultarOpSucOrigen;
	private static String TasaClienteConsultarOpSucDestino;
	private static String TasaClienteConsultarOpModulo;
	private static String TasaMonedaConsultarOp;
	private static String TasaMonedaConsultarOpMonNumero;
	private static String TasaMonedaConsultarOpTransaccio;
	private static String TasaMonedaConsultarOpUsuari;
	private static String TasaMonedaConsultarOpSucOrigen;
	private static String TasaMonedaConsultarOpSucDestino;
	private static String TasaMonedaConsultarOpModulo;
	private static String TasaGATConsultaCalcularOp;
	private static String TasaGATConsultaCalcualrOpInvGAT;
	private static String TasaGATConsultaCalcularOpMonComisi;
	private static String TasaGATConsultaCalcularOpTransaccio;
	private static String TasaGATConsultaCalcularOpUsuari;
	private static String TasaGATConsultaCalcularOpSucOrigen;
	private static String TasaGATConsultaCalcularOpSucDestino;
	private static String TasaGATConsultaCalcularOpModulo;
	private static String TasaGATRealConsultaCalcularOp;
	private static String TasaGATReaConsultaCalcualrOpInvGATRea;
	private static String TasaGATReaConsultaCalcularOpTransaccio;
	private static String TasaGATReaConsultaCalcularOpUsuari;
	private static String TasaGATReaConsultaCalcularOpSucOrigen;
	private static String TasaGATReaConsultaCalcularOpSucDestino;
	private static String TasaGATReaConsultaCalcularOpModulo;
	private static String TasaInversionesCedeConsultarOp;
	private static String TasaInversionesCedeConsultarOpTasPorBas;
	private static String TasaInversionesCedeConsultarOpTasPuntos;
	private static String TasaInversionesCedeConsultarOpTasa;
	private static String TasaInversionesCedeConsultarOpTipConsul;
	private static String TasaInversionesCedeConsultarOpTransaccio;
	private static String TasaInversionesCedeConsultarOpUsuario;
	private static String TasaInversionesCedeConsultarOpSucOrigen;
	private static String TasaInversionesCedeConsultarOpSucDestino;
	private static String TasaInversionesCedeConsultarOpModulo;
	
	public TasaServicio() {
		super();

		TasaServicio= properties.getProperty("data_service.tasa_servicio");
		
		TasaClienteConsultarOp = properties.getProperty("tasa_servicio.op.tasa_cliente_consultar");
		TasaMonedaConsultarOp = properties.getProperty("tasa_servicio.op.tasa_moneda_consultar");
		TasaGATConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_consulta_calcular");
		TasaGATRealConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_rea_consulta_calcular");
		TasaInversionesCedeConsultarOp = properties.getProperty("tasa_servicio.op.tasa_inversiones_cede_consultar");
		
		TasaClienteConsultarOpTasa  = properties.getProperty("op.tasa_cliente_consultar.tasa");
		TasaClienteConsultarOpTransaccio  = properties.getProperty("op.tasa_cliente_consultar.transaccio");
		TasaClienteConsultarOpUsuari = properties.getProperty("op.tasa_cliente_consultar.usuario");
		TasaClienteConsultarOpSucOrigen = properties.getProperty("op.tasa_cliente_consultar.suc_origen");
		TasaClienteConsultarOpSucDestino = properties.getProperty("op.tasa_cliente_consultar.suc_destino");
		TasaClienteConsultarOpModulo = properties.getProperty("op.tasa_cliente_consultar.modulo");		
		TasaClienteConsultarOpInvMoneda = properties.getProperty("op.tasa_cliente_consultar.inv_moneda");
		
		TasaMonedaConsultarOpTransaccio  = properties.getProperty("op.tasa_moneda_consultar.transaccio");
		TasaMonedaConsultarOpUsuari = properties.getProperty("op.tasa_moneda_consultar.usuario");
		TasaMonedaConsultarOpSucOrigen = properties.getProperty("op.tasa_moneda_consultar.suc_origen");
		TasaMonedaConsultarOpSucDestino = properties.getProperty("op.tasa_moneda_consultar.suc_destino");
		TasaMonedaConsultarOpModulo = properties.getProperty("op.tasa_moneda_consultar.modulo");		
		TasaMonedaConsultarOpMonNumero = properties.getProperty("op.tasa_moneda_consultar.mon_numero");
		
		TasaGATConsultaCalcularOpMonComisi  = properties.getProperty("op.tasa_gat_consulta_calcular.mon_comisi");
		TasaGATConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_consulta_calcular.transaccio");
		TasaGATConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_consulta_calcular.usuario");
		TasaGATConsultaCalcularOpSucOrigen = properties.getProperty("op.tasa_gat_consulta_calcular.suc_origen");
		TasaGATConsultaCalcularOpSucDestino = properties.getProperty("op.tasa_gat_consulta_calcular.suc_destino");
		TasaGATConsultaCalcularOpModulo = properties.getProperty("op.tasa_gat_consulta_calcular.modulo");	
		TasaGATConsultaCalcualrOpInvGAT = properties.getProperty("op.tasa_gat_consulta_calcular.inv_gat");
		
		TasaGATReaConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_rea_consulta_calcular.transaccio");
		TasaGATReaConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_rea_consulta_calcular.usuario");
		TasaGATReaConsultaCalcularOpSucOrigen = properties.getProperty("op.tasa_gat_rea_consulta_calcular.suc_origen");
		TasaGATReaConsultaCalcularOpSucDestino = properties.getProperty("op.tasa_gat_rea_consulta_calcular.suc_destino");
		TasaGATReaConsultaCalcularOpModulo = properties.getProperty("op.tasa_gat_rea_consulta_calcular.modulo");	
		TasaGATReaConsultaCalcualrOpInvGATRea = properties.getProperty("op.tasa_gat_rea_consulta_calcular.inv_gat_rea");
		
		TasaInversionesCedeConsultarOpTasPorBas = properties.getProperty("op.tasa_inversiones_cede_consultar.tas_por_bas");
		TasaInversionesCedeConsultarOpTasPuntos = properties.getProperty("op.tasa_inversiones_cede_consultar.tas_puntos");
		TasaInversionesCedeConsultarOpTasa = properties.getProperty("op.tasa_inversiones_cede_consultar.tasa");
		TasaInversionesCedeConsultarOpTipConsul = properties.getProperty("op.tasa_inversiones_cede_consultar.tip_consul");
		TasaInversionesCedeConsultarOpTransaccio = properties.getProperty("op.tasa_inversiones_cede_consultar.transaccio");
		TasaInversionesCedeConsultarOpUsuario = properties.getProperty("op.tasa_inversiones_cede_consultar.usuario");
		TasaInversionesCedeConsultarOpSucOrigen = properties.getProperty("op.tasa_inversiones_cede_consultar.suc_origen");
		TasaInversionesCedeConsultarOpSucDestino = properties.getProperty("op.tasa_inversiones_cede_consultar.suc_destino");
		TasaInversionesCedeConsultarOpModulo = properties.getProperty("op.tasa_inversiones_cede_consultar.modulo");
	}

	/**
	 * Método para consultar la tasa del cliente
	 * ProcedureName: CLTAGRCACON
	 * @param datosTasaCliente
	 * <pre>
	 * {
	 *	Cli_Numero: String,
	 *	Inv_Cantid: Numeric,
	 *	Cli_Tipo: String,
	 *	Plazo: Integer,
	 *	Inv_FecVen: String,
	 *	Ine_Numero?: String,
	 *	Inv_GruTas?: String,
	 *	Inv_NuPoGr?: String,
	 *	NumTransac?: String,
	 *	FechaSis: String
	 * } 
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *     tasaCliente: {
	 *         TasInv: Decimal,
	 *         Inv_GruTas: String,
	 *         Inv_NuPoGr: String
	 *     }
	 * }
	 * </pre>
	 */
	public JsonObject tasaClienteConsultar(JsonObject datosTasaCliente) {
		logger.info("COMMONS: Comenzando tasaClienteConsultar metodo...");
		datosTasaCliente.addProperty("Inv_Moneda", TasaClienteConsultarOpInvMoneda);
		if(!datosTasaCliente.has("Ine_Numero"))
			datosTasaCliente.addProperty("Ine_Numero", "");
		datosTasaCliente.addProperty("Tasa", Integer.parseInt(TasaClienteConsultarOpTasa));		
		if(!datosTasaCliente.has("Inv_GruTas"))
			datosTasaCliente.addProperty("Inv_GruTas", "");
		if(!datosTasaCliente.has("Inv_NuPoGr"))
			datosTasaCliente.addProperty("Inv_NuPoGr", "");
		if(!datosTasaCliente.has("NumTransac"))
			datosTasaCliente.addProperty("NumTransac", "");
		datosTasaCliente.addProperty("Transaccio", TasaClienteConsultarOpTransaccio);
		datosTasaCliente.addProperty("Usuario", TasaClienteConsultarOpUsuari);
		datosTasaCliente.addProperty("SucOrigen", TasaClienteConsultarOpSucOrigen);
		datosTasaCliente.addProperty("SucDestino", TasaClienteConsultarOpSucDestino);
		datosTasaCliente.addProperty("Modulo", TasaClienteConsultarOpModulo);
		JsonObject tasaClienteConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaClienteConsultarOp, datosTasaCliente);
		logger.info("COMMONS: Finalizando tasaClienteConsultar metodo...");
		return tasaClienteConsultarOpResultadoObjeto;
	}//Cierre del método
	
	
	/**
	 * Método para consultar la tasa de la moneda
	 * ProcedureName: SOMONEDACON
	 * @param datosMoneda
	 * <pre>
	 * {
	 *	Mon_Descri: String,
	 *	Mon_Fecha: String,
	 *	Tip_Consul: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *	tasaMoneda: {
	 *		Mon_Numero: String,
	 *		Mon_Descri: String,
	 *		Mon_Simbol: String,
	 *		Mon_Fecha: Date,
	 *		Mon_EfeCom: Double,
	 *		Mon_EfeVen: Double,
	 *		Mon_DocCom: Double,
	 *		Mon_DocVen: Double,
	 *		Mon_FixCom: Double,
	 *		Mon_FixVen: Double,
	 *		Mon_Abrevi: String,
	 *		Mon_DesCor: String,
	 *		Mon_CtaEfe: String,
	 *		Mon_CtaBM: String,
	 *		Mon_CtaSBC: String,
	 *		Mon_CtaRem: String,
	 *		Mon_CieCom: Double,
	 *		Mon_CieVen: Double,
	 *		Mon_SpoCom: Double,
	 *		Mon_SpoVen: Double,
	 *		Mon_EqBaMa: String,
	 *		Mon_OpeCam: String,
	 *		Mon_CieDia: Double,
	 *		Mon_FixVal: Double,
	 *		Mon_DesLeg: String,
	 *		Mon_Tipo: String,
	 *		Mon_ForMet: String
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject tasaMonedaConsultar(JsonObject datosMoneda) {
		logger.info("COMMONS: Comenzando tasaMonedaConsultar metodo...");
		if(!datosMoneda.has("Mon_Descri"))
			datosMoneda.addProperty("Mon_Descri", "");
		if(!datosMoneda.has("Mon_Fecha"))
			datosMoneda.addProperty("Mon_Fecha", "");
		if(!datosMoneda.has("Tip_Consul"))
			datosMoneda.addProperty("Tip_Consul", "");
		datosMoneda.addProperty("Mon_Numero", TasaMonedaConsultarOpMonNumero);
		datosMoneda.addProperty("Transaccio", TasaMonedaConsultarOpTransaccio);
		datosMoneda.addProperty("Usuario", TasaMonedaConsultarOpUsuari);
		datosMoneda.addProperty("SucOrigen", TasaMonedaConsultarOpSucOrigen);
		datosMoneda.addProperty("SucDestino", TasaMonedaConsultarOpSucDestino);
		datosMoneda.addProperty("Modulo", TasaMonedaConsultarOpModulo);
		JsonObject tasaMonedaConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaMonedaConsultarOp, datosMoneda);
		logger.info("COMMONS: Finalizando tasaMonedaConsultar metodo...");
		return tasaMonedaConsultarOpResultadoObjeto;
	}//Cierre del método
	
	
	/**
	 * Método para consulta de calculo de tasa GAT
	 * ProcedureName: INCALGATPRO
	 * @param datosGAT
	 * <pre>
	 * {
	 *   Inv_Dias: Integer,
	 *   Inv_TasInt: Double,
	 *   Cal_Opcion?: String,
	 *   Cue_MonInv: Numeric,
	 *   NumTransac?: String,
	 *   FechaSis: String
	 * } 
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *     tasaGAT: {
	 *         Err_Codigo: String,
	 *         Err_Mensaj: String,
	 *         Inv_GAT: Double
	 *     }
	 * }
	 * </pre>
	 */
	public JsonObject tasaGATConsultaCalcular(JsonObject datosGAT) {
		logger.info("COMMONS: Comenzando tasaGATConsultaCalcular metodo...");
		if(!datosGAT.has("Cal_Opcion"))
			datosGAT.addProperty("Cal_Opcion", "");
		datosGAT.addProperty("Inv_GAT", Integer.parseInt(TasaGATConsultaCalcualrOpInvGAT));
		datosGAT.addProperty("Mon_Comisi", Integer.parseInt(TasaGATConsultaCalcularOpMonComisi));
		if(!datosGAT.has("NumTransac"))
			datosGAT.addProperty("NumTransac", "");
		datosGAT.addProperty("Transaccio", TasaGATConsultaCalcularOpTransaccio);
		datosGAT.addProperty("Usuario", TasaGATConsultaCalcularOpUsuari);
		datosGAT.addProperty("SucOrigen", TasaGATConsultaCalcularOpSucOrigen);
		datosGAT.addProperty("SucDestino", TasaGATConsultaCalcularOpSucDestino);
		datosGAT.addProperty("Modulo", TasaGATConsultaCalcularOpModulo);
		JsonObject tasaGATConsultaCalcularOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaGATConsultaCalcularOp, datosGAT);
		logger.info("COMMONS: Finalizando tasaGATConsultaCalcular metodo...");
		return tasaGATConsultaCalcularOpResultadoObjeto;
	}//Cierre del método
	
	
	/**
	 * Método para consulta de calculo de tasa GAT real
	 * ProcedureName: INCALINFPRO
	 * @param datosGATReal
	 * <pre>
	 * {
	 *	Inv_GAT: Double,
	 *	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *     tasaGATReal: {
	 *         Err_Codigo: String,
	 *         Err_Mensaj: String,
	 *         Inv_GATRea: Double
	 *     }
	 * }
	 * </pre>
	 */
	public JsonObject tasaGATRealConsultaCalcular(JsonObject datosGATReal) {
		logger.info("COMMONS: Comenzando tasaGATRealConsultaCalcular metodo...");
		datosGATReal.addProperty("Inv_GATRea", Integer.parseInt(TasaGATReaConsultaCalcualrOpInvGATRea));
		if(!datosGATReal.has("NumTransac"))
			datosGATReal.addProperty("NumTransac", "");
		datosGATReal.addProperty("Transaccio", TasaGATReaConsultaCalcularOpTransaccio);
		datosGATReal.addProperty("Usuario", TasaGATReaConsultaCalcularOpUsuari);
		datosGATReal.addProperty("SucOrigen", TasaGATReaConsultaCalcularOpSucOrigen);
		datosGATReal.addProperty("SucDestino", TasaGATReaConsultaCalcularOpSucDestino);
		datosGATReal.addProperty("Modulo", TasaGATReaConsultaCalcularOpModulo);
		JsonObject tasaGATRealConsultaCalcularOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaGATRealConsultaCalcularOp, datosGATReal);
		logger.info("COMMONS: Finalizando tasaGATRealConsultaCalcular metodo...");
		return tasaGATRealConsultaCalcularOpResultadoObjeto;
	}//Cierre del método
	
	/**
	* Método para consultar la tasa de una inversión en base a un plazo y cantidad invertida
	* ProcedureName: CETASASCON
	* @param datosTasaInversionesCede
	* <pre>
	* {
	* 	Tas_Plazo: String,
	* 	Tas_Cantid: String,
	* 	Tas_Formul: String,
	* 	Tas_Fecha: String,
	* 	Cli_Numero: String,
	*	FechaSis: String
	* }
	* </pre>
	* @return
	* <pre>
	* {
	* 	tasa: {
	* 		Tas_Tasa: Double,
	* 		Tas_PorBas: Double,
	* 		Tas_Puntos: Double,
	* 		Tas_Esquema: String,
	* 		Tas_VarRea: Double
	* 	}
	* }
	* </pre>
	*/
	public JsonObject tasaInversionesCedeConsultar(JsonObject datosTasaInversionesCede) {
		logger.info("COMMONS: Comenzando tasaInversionesCedeConsultar metodo... ");
		if(!datosTasaInversionesCede.has("Tas_Monto"))
			datosTasaInversionesCede.addProperty("Tas_Monto", "");
		if(!datosTasaInversionesCede.has("Tas_TasBas"))
			datosTasaInversionesCede.addProperty("Tas_TasBas", "");
		if(!datosTasaInversionesCede.has("Tas_TipPer"))
			datosTasaInversionesCede.addProperty("Tas_TipPer", "");
		if(!datosTasaInversionesCede.has("NumTransac"))
			datosTasaInversionesCede.addProperty("NumTransac", "");
		datosTasaInversionesCede.addProperty("Tas_PorBas", Double.parseDouble(TasaInversionesCedeConsultarOpTasPorBas));
		datosTasaInversionesCede.addProperty("Tas_Puntos", Double.parseDouble(TasaInversionesCedeConsultarOpTasPuntos));
		datosTasaInversionesCede.addProperty("Tasa", Double.parseDouble(TasaInversionesCedeConsultarOpTasa));
		datosTasaInversionesCede.addProperty("Tip_Consul", TasaInversionesCedeConsultarOpTipConsul);
		datosTasaInversionesCede.addProperty("Transaccio", TasaInversionesCedeConsultarOpTransaccio);
		datosTasaInversionesCede.addProperty("Usuario", TasaInversionesCedeConsultarOpUsuario);
		datosTasaInversionesCede.addProperty("SucOrigen", TasaInversionesCedeConsultarOpSucOrigen);
		datosTasaInversionesCede.addProperty("SucDestino", TasaInversionesCedeConsultarOpSucDestino);
		datosTasaInversionesCede.addProperty("Modulo", TasaInversionesCedeConsultarOpModulo);
		JsonObject tasaInversionesCedeConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaInversionesCedeConsultarOp, datosTasaInversionesCede);
		logger.info("COMMONS: Finalizando tasaInversionesCedeConsultar metodo... ");
		return tasaInversionesCedeConsultarOpResultadoObjeto;
	}//Cierre del método
}
