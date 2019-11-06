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

	/**
	 * Método para obtener datos de las inversiones
	 * ProcedureName: NBSALCEDCON
	 * @param datosInversionesObtener
	 * <pre>
	 * {
	 * 	Inv_Client: String,
	 * 	Inv_Moneda: String,
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
		logger.info("COMMONS: Comenzando inversionesObtener... ");
		datosInversionesObtener.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
		if(!datosInversionesObtener.has("NumTransac"))
			datosInversionesObtener.addProperty("NumTransac", "");
		datosInversionesObtener.addProperty("Transaccio", InversionesObtenerOpTransaccio);
		datosInversionesObtener.addProperty("Usuario", InversionesObtenerOpUsuario);
		datosInversionesObtener.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
		datosInversionesObtener.addProperty("SucDestino", InversionesObtenerOpSucDestino);
		datosInversionesObtener.addProperty("Modulo", InversionesObtenerOpModulo);
		JsonObject inversionesObtenerOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesObtenerOp, datosInversionesObtener);
		logger.info("COMMONS: Finalizando inversionesObtener... ");
		return inversionesObtenerOpResultadoObjeto;
	}//Cierre del método
	
	
	/**
	 * Método para obtener datos de inversiones pagaré
	 * ProcedureName: NBINVERSCON
	 * @param datosInversionesPagareNumeroUsuarioObtener
	 * <pre>
	 * {
	 *	Inv_Numero?: String,
	 *	Inv_Usuari: String,
	 *	Tip_Consul: String,
	 * 	NumTransac: String,
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
		logger.info("COMMONS: Comenzando inversionesPagareNumeroUsuarioObtener... ");
		if(!datosInversionesPagareNumeroUsuarioObtener.has("Inv_Numero"))
			datosInversionesPagareNumeroUsuarioObtener.addProperty("Inv_Numero", "");
		if(!datosInversionesPagareNumeroUsuarioObtener.has("Inv_Usuari"))
			datosInversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", "");
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
		datosInversionesPagareNumeroUsuarioObtener.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = Utilerias.performOperacion(InversionesServicio, InversionesPagareNumeroUsuarioObtenerOp, datosInversionesPagareNumeroUsuarioObtener);
		logger.info("COMMONS: Finalizando inversionesPagareNumeroUsuarioObtener... ");
		return inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto;
	}//Cierre del método
	
	public JsonObject inversionesImportesDeInversionFinalizadaActualizar(JsonObject datosInversionFinalizada) {
		datosInversionFinalizada.addProperty("Inv_rAutor", InversionesImportesDeInversionFinalizadaActualizarOpInvrAutor);		
		datosInversionFinalizada.addProperty("Transaccio", InversionesImportesDeInversionFinalizadaActualizarOpTransaccio);
		datosInversionFinalizada.addProperty("Usuario", InversionesImportesDeInversionFinalizadaActualizarOpUsuari);
		datosInversionFinalizada.addProperty("SucOrigen", InversionesImportesDeInversionFinalizadaActualizarOpSucOrigen);
		datosInversionFinalizada.addProperty("SucDestino", InversionesImportesDeInversionFinalizadaActualizarOpSucDestino);
		datosInversionFinalizada.addProperty("Modulo", InversionesImportesDeInversionFinalizadaActualizarOpModulo);
		JsonObject inversionesImportesDeInversionFinalizadaActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesImportesDeInversionFinalizadaActualizarOp, datosInversionFinalizada);
		return inversionesImportesDeInversionFinalizadaActualizarOpResultadoObjeto;
		
	}
	
	public JsonObject inversionesStatusActualizar(JsonObject datosStatusActualizar) {
		datosStatusActualizar.addProperty("Adi_InsLiq", InversionesStatusActualizarOpAdiInsLiq);
		datosStatusActualizar.addProperty("Adi_MoReGr", Integer.parseInt(InversionesStatusActualizarOpAdiMoReGr));
		datosStatusActualizar.addProperty("Transaccio", InversionesStatusActualizarOpTransaccio);
		datosStatusActualizar.addProperty("Usuario", InversionesStatusActualizarOpUsuari);
		datosStatusActualizar.addProperty("SucOrigen", InversionesStatusActualizarOpSucOrigen);
		datosStatusActualizar.addProperty("SucDestino", InversionesStatusActualizarOpSucDestino);
		datosStatusActualizar.addProperty("Modulo", InversionesStatusActualizarOpModulo);

		JsonObject inversionesStatusActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesStatusActualizarOp, datosStatusActualizar);
		return inversionesStatusActualizarOpResultadoObjeto;
	}
	
	public JsonObject inversionesProcesoLiquidacionGenerar(JsonObject datosProcesoLiquidacion) {
		datosProcesoLiquidacion.addProperty("Inv_rAutor", InversionesImportesDeInversionFinalizadaActualizarOpUsuari);
		datosProcesoLiquidacion.addProperty("Inv_MonRef", InversionesProcesoLiquidacionGenerarOpInvMonRef);
		datosProcesoLiquidacion.addProperty("Transaccio", InversionesProcesoLiquidacionGenerarOpTransaccio);
		datosProcesoLiquidacion.addProperty("Usuario", InversionesProcesoLiquidacionGenerarOpUsuari);
		datosProcesoLiquidacion.addProperty("SucOrigen", InversionesProcesoLiquidacionGenerarOpSucOrigen);
		datosProcesoLiquidacion.addProperty("SucDestino", InversionesProcesoLiquidacionGenerarOpSucDestino);
		datosProcesoLiquidacion.addProperty("Modulo", InversionesProcesoLiquidacionGenerarOpModulo);
		JsonObject inversionesProcesoLiquidacionGenerarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesProcesoLiquidacionGenerarOp, datosProcesoLiquidacion);
		return inversionesProcesoLiquidacionGenerarOpResultadoObjeto;
	}
	
	public JsonObject inversionesContraEstadoCuentaActualizarOp(JsonObject datosIversionVsEstadoCuenta) {
		datosIversionVsEstadoCuenta.addProperty("Cor_Status", "");
		datosIversionVsEstadoCuenta.addProperty("Cor_CliUsu", "");
		datosIversionVsEstadoCuenta.addProperty("Cor_Alias", "");
		datosIversionVsEstadoCuenta.addProperty("Cor_MoLiDi", InversionesContraEstadoCuentaActualizarOpCorMoLiDi);
		datosIversionVsEstadoCuenta.addProperty("Tip_Actual", InversionesContraEstadoCuentaActualizarOpTipActual);		
		datosIversionVsEstadoCuenta.addProperty("Transaccio", InversionesContraEstadoCuentaActualizarOpTransaccio);
		datosIversionVsEstadoCuenta.addProperty("Usuario", InversionesContraEstadoCuentaActualizarOpUsuari);
		datosIversionVsEstadoCuenta.addProperty("SucOrigen", InversionesContraEstadoCuentaActualizarOpSucOrigen);
		datosIversionVsEstadoCuenta.addProperty("SucDestino", InversionesContraEstadoCuentaActualizarOpSucDestino);
		datosIversionVsEstadoCuenta.addProperty("Modulo", InversionesContraEstadoCuentaActualizarOpModulo);
		JsonObject inversionesContraEstadoCuentaActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesContraEstadoCuentaActualizarOp, datosIversionVsEstadoCuenta);
		return inversionesContraEstadoCuentaActualizarOpResultadoObjeto;
	}
}