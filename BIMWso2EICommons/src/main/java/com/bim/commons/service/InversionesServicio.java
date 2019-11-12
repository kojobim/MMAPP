package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre las inversiones
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class InversionesServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(InversionesServicio.class);

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
	private static String InversionesImportesDeInversionFinalizadaActualizarOp;
	private static String InversionesImportesDeInversionFinalizadaActualizarOpInvrAutor;		
	private static String InversionesImportesDeInversionFinalizadaActualizarOpTransaccio;
	private static String InversionesImportesDeInversionFinalizadaActualizarOpUsuari;
	private static String InversionesImportesDeInversionFinalizadaActualizarOpSucOrigen;
	private static String InversionesImportesDeInversionFinalizadaActualizarOpSucDestino;
	private static String InversionesImportesDeInversionFinalizadaActualizarOpModulo;
	private static String InversionesStatusActualizarOp;
	private static String InversionesStatusActualizarOpAdiInsLiq;
	private static String InversionesStatusActualizarOpAdiMoReGr;
	private static String InversionesStatusActualizarOpTransaccio;
	private static String InversionesStatusActualizarOpUsuari;
	private static String InversionesStatusActualizarOpSucOrigen;
	private static String InversionesStatusActualizarOpSucDestino;
	private static String InversionesStatusActualizarOpModulo;
	private static String InversionesProcesoLiquidacionGenerarOp;
	private static String InversionesProcesoLiquidacionGenerarOpInvMonRef;
	private static String InversionesProcesoLiquidacionGenerarOpTransaccio;
	private static String InversionesProcesoLiquidacionGenerarOpUsuari;
	private static String InversionesProcesoLiquidacionGenerarOpSucOrigen;
	private static String InversionesProcesoLiquidacionGenerarOpSucDestino;
	private static String InversionesProcesoLiquidacionGenerarOpModulo;
	private static String InversionesContraEstadoCuentaActualizarOp;
	private static String InversionesContraEstadoCuentaActualizarOpCorMoLiDi;
	private static String InversionesContraEstadoCuentaActualizarOpTipActual;		
	private static String InversionesContraEstadoCuentaActualizarOpTransaccio;
	private static String InversionesContraEstadoCuentaActualizarOpUsuari;
	private static String InversionesContraEstadoCuentaActualizarOpSucOrigen;
	private static String InversionesContraEstadoCuentaActualizarOpSucDestino;
	private static String InversionesContraEstadoCuentaActualizarOpModulo;
	
	
	public InversionesServicio() {
		super();

		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");

		InversionesObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_obtener");
		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_numero_usuario_obtener");
		InversionesImportesDeInversionFinalizadaActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_importes_de_inversion_finalizada_actualizar");

		InversionesObtenerOpInvMoneda = properties.getProperty("op.inversiones_obtener.inv_moneda");
		InversionesObtenerOpTransaccio = properties.getProperty("op.inversiones_obtener.transaccio");
		InversionesObtenerOpUsuario = properties.getProperty("op.inversiones_obtener.usuario");
		InversionesObtenerOpSucOrigen = properties.getProperty("op.inversiones_obtener.suc_origen");
		InversionesObtenerOpSucDestino = properties.getProperty("op.inversiones_obtener.suc_destino");
		InversionesObtenerOpModulo = properties.getProperty("op.inversiones_obtener.modulo");

		InversionesPagareNumeroUsuarioObtenerOpTipConsul = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.tip_consul");
		InversionesPagareNumeroUsuarioObtenerOpTransaccio = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.transaccio");
		InversionesPagareNumeroUsuarioObtenerOpUsuario = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.usuario");
		InversionesPagareNumeroUsuarioObtenerOpSucOrigen = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_origen");
		InversionesPagareNumeroUsuarioObtenerOpSucDestino = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_destino");
		InversionesPagareNumeroUsuarioObtenerOpModulo = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.modulo");

		InversionesImportesDeInversionFinalizadaActualizarOpTransaccio  = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.transaccio");
		InversionesImportesDeInversionFinalizadaActualizarOpUsuari = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.usuario");
		InversionesImportesDeInversionFinalizadaActualizarOpSucOrigen = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.suc_origen");
		InversionesImportesDeInversionFinalizadaActualizarOpSucDestino = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.suc_destino");
		InversionesImportesDeInversionFinalizadaActualizarOpModulo = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.modulo");

	}

	/**
	 * Método para obtener datos de las inversiones
	 * ProcedureName: NBSALCEDCON
	 * @param datosInversionesObtener
	 * <pre>
	 * {
	 * 	Inv_Client: String,
	 * 	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *	inversiones: {
	 *		inversion: [
	 *			{
	 *				Inv_Numero: String,
	 *				Inv_Cuenta: String,
	 *				Inv_ForTas: String,
	 *				Inv_FecIni: String,
	 *				Inv_FecVen: String,
	 *				Inv_Cantid: Double,
	 *				Amo_Tasa: Double,
	 *				Amo_ISR: Double,
	 *				Amo_FecIni: String,
	 *				Amo_FecVen: String,
	 *				Amo_Numero: String,
	 *				Inv_Cantid: Double,
	 *				Imp_Intere: Double,
	 *				Imp_ISR: Double,
	 *				Plazo: Integer,
	 *				Pla_Intere: Integer,
	 *				Imp_Total: Double,
	 *				Fot_Descri: String,
	 *				Inv_Tipo: String,
	 *				Inv_Gat: Double,
	 *				Inv_GatRea: Double,
	 *				Inv_IntBru: Double,
	 *				Inv_IntNet: Double,
	 *				Inv_ISRTot: Double,
	 *				Inv_Total: Double,
	 *				Inv_Esquema: String
	 *			}
	 *		]
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject inversionesObtener(JsonObject datosInversionesObtener) {
		logger.info("COMMONS: Comenzando inversionesObtener metodo... ");
		datosInversionesObtener.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
		if(!datosInversionesObtener.has("NumTransac"))
			datosInversionesObtener.addProperty("NumTransac", "");
		datosInversionesObtener.addProperty("Transaccio", InversionesObtenerOpTransaccio);
		datosInversionesObtener.addProperty("Usuario", InversionesObtenerOpUsuario);
		datosInversionesObtener.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
		datosInversionesObtener.addProperty("SucDestino", InversionesObtenerOpSucDestino);
		datosInversionesObtener.addProperty("Modulo", InversionesObtenerOpModulo);
		JsonObject inversionesObtenerOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesObtenerOp, datosInversionesObtener);
		logger.info("COMMONS: Finalizando inversionesObtener metodo... ");
		return inversionesObtenerOpResultadoObjeto;
	}//Cierre del método
	
	
	/**
	 * Método para obtener datos de inversiones pagaré
	 * ProcedureName: NBINVERSCON
	 * @param datosInversionesPagareNumeroUsuarioObtener
	 * <pre>
	 * {
	 *	Inv_Numero: String,
	 *	Inv_Usuari?: String,
	 * 	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *	inversiones: {
	 *		inversion: [
	 *			{
	 *				Inv_Numero: String,
	 *				Inv_FecIni: Date,
	 *				Inv_FecVen: Date,
	 *				Inv_Cantid: Double,
	 *				Inv_Tasa: Double,
	 *				Inv_Cuenta: String,
	 *				Inv_ISR: Double,
	 *				Inv_TBruta: Double,
	 *				Adi_InsLiq: String,
	 *				Mon_Descri: String,
	 *				Inv_Plazo: Integer,
	 *				Inv_GAT: Double,
	 *				Inv_GATRea: Double,
	 *				Gar_ComFon: String,
	 *				Imp_ISR: Double,
	 *				Imp_Intere: Double,
	 *				Inv_Total: Double		
	 *			}
	 *		]
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject inversionesPagareNumeroUsuarioObtener(JsonObject datosInversionesPagareNumeroUsuarioObtener) {
		logger.info("COMMONS: Comenzando inversionesPagareNumeroUsuarioObtener metodo... ");
		if(!datosInversionesPagareNumeroUsuarioObtener.has("Inv_Numero"))
			datosInversionesPagareNumeroUsuarioObtener.addProperty("Inv_Numero", "");
		if(!datosInversionesPagareNumeroUsuarioObtener.has("Inv_Usuari"))
			datosInversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", "");
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
		if(!datosInversionesPagareNumeroUsuarioObtener.has("NumTransac"))
			datosInversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", "");
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = Utilerias.performOperacion(InversionesServicio, InversionesPagareNumeroUsuarioObtenerOp, datosInversionesPagareNumeroUsuarioObtener);
		logger.info("COMMONS: Finalizando inversionesPagareNumeroUsuarioObtener metodo... ");
		return inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto;
	}//Cierre del método
	
	
	/**
	 * Método para actualizar los importes de la inversión finalizada
	 * ProcedureName: INREINVEALT
	 * @param datosInversionFinalizada
	 * <pre>
	 * {
	 *	Inv_Numero: String,
	 *	Inv_Deposi: Numeric,
	 *	Inv_rFecIn: String,
	 *	Inv_rFecVe: String,
	 *	Inv_rCanti: String,
	 *	Inv_rTasa: Numeric
	 *	Inv_rISR: Numeric,
	 *	Inv_rCuent: String,
	 *	Inv_rTBrut: Numeric,
	 *	NumTransac: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 * 	importesDeInversionFinalizada: {
	 * 		Err_Codigo: String,
	 * 		Err_Mensaj: String,
	 * 		Err_Variab: String
	 * }
	 * </pre>
	 */
	public JsonObject inversionesImportesDeInversionFinalizadaActualizar(JsonObject datosInversionFinalizada) {
		logger.info("COMMONS: Comenzando inversionesImportesDeInversionFinalizadaActualizar metodo... ");
		datosInversionFinalizada.addProperty("Inv_rAutor", InversionesImportesDeInversionFinalizadaActualizarOpInvrAutor);		
		datosInversionFinalizada.addProperty("Transaccio", InversionesImportesDeInversionFinalizadaActualizarOpTransaccio);
		datosInversionFinalizada.addProperty("Usuario", InversionesImportesDeInversionFinalizadaActualizarOpUsuari);
		datosInversionFinalizada.addProperty("SucOrigen", InversionesImportesDeInversionFinalizadaActualizarOpSucOrigen);
		datosInversionFinalizada.addProperty("SucDestino", InversionesImportesDeInversionFinalizadaActualizarOpSucDestino);
		datosInversionFinalizada.addProperty("Modulo", InversionesImportesDeInversionFinalizadaActualizarOpModulo);
		JsonObject inversionesImportesDeInversionFinalizadaActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesImportesDeInversionFinalizadaActualizarOp, datosInversionFinalizada);
		logger.info("COMMONS: Finalizando inversionesImportesDeInversionFinalizadaActualizar metodo... ");
		return inversionesImportesDeInversionFinalizadaActualizarOpResultadoObjeto;
	}//Cierre del método


	/**
	 * Método para actualizar el status de una inversión
	 * ProcedureName: INADICIOMOD
	 * @param datosStatusActualizar
	 * <pre>
	 * {
	 *	Adi_Invers: String,
	 *	NumTransac: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *     inversiones: {
	 *         inversion: [
	 *             {
	 *                 Err_Codigo: String,
	 *                 Err_Mensaj: String
	 *             }
	 *         ]
	 *     }
	 * }
	 * </pre>
	 */
	public JsonObject inversionesStatusActualizar(JsonObject datosStatusActualizar) {
		logger.info("COMMONS: Comenzando inversionesStatusActualizar metodo... ");
		datosStatusActualizar.addProperty("Adi_InsLiq", InversionesStatusActualizarOpAdiInsLiq);
		datosStatusActualizar.addProperty("Adi_MoReGr", Integer.parseInt(InversionesStatusActualizarOpAdiMoReGr));
		datosStatusActualizar.addProperty("Transaccio", InversionesStatusActualizarOpTransaccio);
		datosStatusActualizar.addProperty("Usuario", InversionesStatusActualizarOpUsuari);
		datosStatusActualizar.addProperty("SucOrigen", InversionesStatusActualizarOpSucOrigen);
		datosStatusActualizar.addProperty("SucDestino", InversionesStatusActualizarOpSucDestino);
		datosStatusActualizar.addProperty("Modulo", InversionesStatusActualizarOpModulo);
		JsonObject inversionesStatusActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesStatusActualizarOp, datosStatusActualizar);
		logger.info("COMMONS: Finalizando inversionesStatusActualizar metodo... ");
		return inversionesStatusActualizarOpResultadoObjeto;
	}//Cierre del método


	/**
	 * Método para generar el proceso de liquidación de inversiones
	 * ProcedureName: INREINVEGEN
	 * @param datosProcesoLiquidacion
	 * <pre>
	 * {
	 *	Inv_Numero: String,
	 *	Inv_rFecIn: String,
	 *	Inv_rFecVe: String,
	 *	Inv_rCanti: Numeric,
	 *	Inv_rTasa: Numeric,
	 *	Inv_rISR: Numeric,
	 *	Inv_rCuent: String,
	 *	Dias_Base: Integer,
	 *	Inv_Fecha: String,
	 *	Inv_rTBrut: Numeric,
	 *	NumTransac: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *     procesoLiquidacionGenerar: {
	 *         Err_Codigo: String,
	 *         Err_Mensaj: String,
	 *         Inv_Nueva: String
	 *     }
	 * }
	 * </pre>
	 */
	public JsonObject inversionesProcesoLiquidacionGenerar(JsonObject datosProcesoLiquidacion) {
		logger.info("COMMONS: Comenzando inversionesProcesoLiquidacionGenerar metodo... ");
		datosProcesoLiquidacion.addProperty("Inv_rAutor", InversionesImportesDeInversionFinalizadaActualizarOpUsuari);
		datosProcesoLiquidacion.addProperty("Inv_MonRef", InversionesProcesoLiquidacionGenerarOpInvMonRef);
		datosProcesoLiquidacion.addProperty("Transaccio", InversionesProcesoLiquidacionGenerarOpTransaccio);
		datosProcesoLiquidacion.addProperty("Usuario", InversionesProcesoLiquidacionGenerarOpUsuari);
		datosProcesoLiquidacion.addProperty("SucOrigen", InversionesProcesoLiquidacionGenerarOpSucOrigen);
		datosProcesoLiquidacion.addProperty("SucDestino", InversionesProcesoLiquidacionGenerarOpSucDestino);
		datosProcesoLiquidacion.addProperty("Modulo", InversionesProcesoLiquidacionGenerarOpModulo);
		JsonObject inversionesProcesoLiquidacionGenerarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesProcesoLiquidacionGenerarOp, datosProcesoLiquidacion);
		logger.info("COMMONS: Finalizando inversionesProcesoLiquidacionGenerar metodo... ");
		return inversionesProcesoLiquidacionGenerarOpResultadoObjeto;
	}//Cierre del método


	/**
	 * Método para actualizar el estado de la cuenta contra la inversión realizada 
	 * ProcedureName: NBCUEORIACT
	 * @param datosIversionVsEstadoCuenta
	 * <pre>
	 * {
	 *	Cor_Usuari: String,
	 *	Cor_Cuenta: String,
	 *	Cor_Status: String,
	 *	Cor_MonDia: Numeric,
	 *	Cor_CliUsu: String,
	 *	Cor_Alias: String,
	 *	NumTransac: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * { 
	 * 	REQUEST_STATUS: String
	 * }
	 * </pre>
	 */
	public JsonObject inversionesContraEstadoCuentaActualizar(JsonObject datosIversionVsEstadoCuenta) {
		logger.info("COMMONS: Comenzando inversionesContraEstadoCuentaActualizar metodo... ");
		if(!datosIversionVsEstadoCuenta.has("Cor_Status"))
			datosIversionVsEstadoCuenta.addProperty("Cor_Status", "");
		if(!datosIversionVsEstadoCuenta.has("Cor_CliUsu"))
			datosIversionVsEstadoCuenta.addProperty("Cor_CliUsu", "");
		if(!datosIversionVsEstadoCuenta.has("Cor_Alias"))
			datosIversionVsEstadoCuenta.addProperty("Cor_Alias", "");
		datosIversionVsEstadoCuenta.addProperty("Cor_MoLiDi", InversionesContraEstadoCuentaActualizarOpCorMoLiDi);
		datosIversionVsEstadoCuenta.addProperty("Tip_Actual", InversionesContraEstadoCuentaActualizarOpTipActual);		
		datosIversionVsEstadoCuenta.addProperty("Transaccio", InversionesContraEstadoCuentaActualizarOpTransaccio);
		datosIversionVsEstadoCuenta.addProperty("Usuario", InversionesContraEstadoCuentaActualizarOpUsuari);
		datosIversionVsEstadoCuenta.addProperty("SucOrigen", InversionesContraEstadoCuentaActualizarOpSucOrigen);
		datosIversionVsEstadoCuenta.addProperty("SucDestino", InversionesContraEstadoCuentaActualizarOpSucDestino);
		datosIversionVsEstadoCuenta.addProperty("Modulo", InversionesContraEstadoCuentaActualizarOpModulo);
		JsonObject inversionesContraEstadoCuentaActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesContraEstadoCuentaActualizarOp, datosIversionVsEstadoCuenta);
		logger.info("COMMONS: Finalizando inversionesContraEstadoCuentaActualizar metodo... ");
		return inversionesContraEstadoCuentaActualizarOpResultadoObjeto;
	}//Cierre del método
}