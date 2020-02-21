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
	private static String InversionesProcesoLiquidacionGenerarOpInvrAutor;
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

	private static String InversionesAltaOp;
	private static String InversionesAltaOpTransaccio;
	private static String InversionesAltaOpUsuario;
	private static String InversionesAltaOpSucOrigen;
	private static String InversionesAltaOpSucDestino;
	private static String InversionesAltaOpModulo;

	private static String InversionesCedePlazosConsultarL1Op;
	private static String InversionesCedePlazosConsultarC4Op;
	private static String InversionesCedePlazosConsultarOpTipConsulL1;
	private static String InversionesCedePlazosConsultarOpTransaccio;
	private static String InversionesCedePlazosConsultarOpUsuario;
	private static String InversionesCedePlazosConsultarOpSucOrigen;
	private static String InversionesCedePlazosConsultarOpSucDestino;
	private static String InversionesCedePlazosConsultarOpModulo;
	
	private static String InversionesCedeDiasDePagoConsultarOp;		
	private static String InversionesCedeDiasDePagoConsultarOpTransaccio;
	private static String InversionesCedeDiasDePagoConsultarOpUsuario;
	private static String InversionesCedeDiasDePagoConsultarOpSucOrigen;
	private static String InversionesCedeDiasDePagoConsultarOpSucDestino;
	private static String InversionesCedeDiasDePagoConsultarOpModulo;
	
	private static String InversionesAltaPagarOp;
	private static String InversionesAltaPagarOptransaccio;
	private static String InversionesAltaPagarOpUsuario;
	private static String InversionesAltaPagarOpSucOrigen;
	private static String InversionesAltaPagarOpSucDestino;
	private static String InversionesAltaPagarOpModulo;
	
	private static String InversionesPagareInformacionGuardarOp;
	private static String InversionesPagareInformacionGuardarOpTransaccio;
	private static String InversionesPagareInformacionGuardarOpUsuario;
	private static String InversionesPagareInformacionGuardarOpSucOrigen;
	private static String InversionesPagareInformacionGuardarOpSucDestino;
	private static String InversionesPagareInformacionGuardarOpModulo;
	
	private static String InversionesCedeAltaOp;
	private static String InversionesCedeAltaOpInvMoneda;
	private static String InversionesCedeAltaOpTransaccio;
	private static String InversionesCedeAltaOpUsuario;
	private static String InversionesCedeAltaOpSucOrigen;
	private static String InversionesCedeAltaOpSucDestino;
	private static String InversionesCedeAltaOpModulo;
	
	private static String InversionesCedePagarOp;
	private static String InversionesCedePagarOpTransaccio;
	private static String InversionesCedePagarOpUsuario;
	private static String InversionesCedePagarOpSucOrigen;
	private static String InversionesCedePagarOpSucDestino;
	private static String InversionesCedePagarOpModulo;
	
	public InversionesServicio() {
		super();

		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");

		InversionesObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_obtener");
		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_numero_usuario_obtener");
		InversionesImportesDeInversionFinalizadaActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_importes_de_inversion_finalizada_actualizar");
		InversionesStatusActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_status_actualizar");
		InversionesProcesoLiquidacionGenerarOp = properties.getProperty("inversiones_servicio.op.inversiones_proceso_liquidacion_generar");
		InversionesContraEstadoCuentaActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_contra_estado_cuenta_actualizar");
		InversionesAltaOp = properties.getProperty("inversiones_servicio.op.inversiones_alta");
		InversionesCedeDiasDePagoConsultarOp = properties.getProperty("inversiones_servicio.op.inversiones_cede_dias_de_pago_consultar");
		InversionesCedePlazosConsultarL1Op = properties.getProperty("inversiones_servicio.op.inversiones_cede_plazos_consultar_l1");
		InversionesCedePlazosConsultarC4Op = properties.getProperty("inversiones_servicio.op.inversiones_cede_plazos_consultar_c4");
		InversionesAltaPagarOp = properties.getProperty("inversiones_servicio.op.inversiones_alta_pagar");
		InversionesPagareInformacionGuardarOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_informacion_guardar");
		InversionesCedeAltaOp = properties.getProperty("inversiones_servicio.op.inversiones_cede_alta");
		InversionesCedePagarOp = properties.getProperty("inversiones_servicio.op.inversiones_cede_pagar");

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

		InversionesImportesDeInversionFinalizadaActualizarOpInvrAutor = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.inv_rautor");
		InversionesImportesDeInversionFinalizadaActualizarOpTransaccio = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.transaccio");
		InversionesImportesDeInversionFinalizadaActualizarOpUsuari = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.usuario");
		InversionesImportesDeInversionFinalizadaActualizarOpSucOrigen = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.suc_origen");
		InversionesImportesDeInversionFinalizadaActualizarOpSucDestino = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.suc_destino");
		InversionesImportesDeInversionFinalizadaActualizarOpModulo = properties.getProperty("op.inversiones_importes_de_inversion_finalizada_actualizar.modulo");		
		
		InversionesStatusActualizarOpAdiInsLiq = properties.getProperty("op.inversiones_status_actualizar.adi_insLiq");
		InversionesStatusActualizarOpAdiMoReGr = properties.getProperty("op.inversiones_status_actualizar.adi_moregr");
		InversionesStatusActualizarOpTransaccio = properties.getProperty("op.inversiones_status_actualizar.transaccio");
		InversionesStatusActualizarOpUsuari = properties.getProperty("op.inversiones_status_actualizar.usuario");
		InversionesStatusActualizarOpSucOrigen = properties.getProperty("op.inversiones_status_actualizar.suc_origen");
		InversionesStatusActualizarOpSucDestino = properties.getProperty("op.inversiones_status_actualizar.suc_destino");
		InversionesStatusActualizarOpModulo = properties.getProperty("op.inversiones_status_actualizar.modulo");		
		
		InversionesProcesoLiquidacionGenerarOpInvrAutor = properties.getProperty("op.inversiones_proceso_liquidacion_generar.inv_rautor");
		InversionesProcesoLiquidacionGenerarOpInvMonRef = properties.getProperty("op.inversiones_proceso_liquidacion_generar.inv_monref");
		InversionesProcesoLiquidacionGenerarOpTransaccio = properties.getProperty("op.inversiones_proceso_liquidacion_generar.transaccio");
		InversionesProcesoLiquidacionGenerarOpUsuari = properties.getProperty("op.inversiones_proceso_liquidacion_generar.usuario");
		InversionesProcesoLiquidacionGenerarOpSucOrigen = properties.getProperty("op.inversiones_proceso_liquidacion_generar.suc_origen");
		InversionesProcesoLiquidacionGenerarOpSucDestino = properties.getProperty("op.inversiones_proceso_liquidacion_generar.suc_destino");
		InversionesProcesoLiquidacionGenerarOpModulo = properties.getProperty("op.inversiones_proceso_liquidacion_generar.modulo");		
		
		InversionesContraEstadoCuentaActualizarOpCorMoLiDi = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.cor_molidi");
		InversionesContraEstadoCuentaActualizarOpTipActual = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.tip_actual");
		InversionesContraEstadoCuentaActualizarOpTransaccio = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.transaccio");
		InversionesContraEstadoCuentaActualizarOpUsuari = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.usuario");
		InversionesContraEstadoCuentaActualizarOpSucOrigen = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.suc_origen");
		InversionesContraEstadoCuentaActualizarOpSucDestino = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.suc_destino");		
		InversionesContraEstadoCuentaActualizarOpModulo = properties.getProperty("op.inversiones_contra_estado_cuenta_actualizar.modulo");
		
		InversionesAltaOpTransaccio = properties.getProperty("op.inversiones_alta.transaccio");
		InversionesAltaOpUsuario = properties.getProperty("op.inversiones_alta.usuario");
		InversionesAltaOpSucOrigen = properties.getProperty("op.inversiones_alta.suc_origen");
		InversionesAltaOpSucDestino = properties.getProperty("op.inversiones_alta.suc_destino");
		InversionesAltaOpModulo = properties.getProperty("op.inversiones_alta.modulo");
		
		InversionesCedeDiasDePagoConsultarOpTransaccio = properties.getProperty("op.inversiones_cede_dias_de_pago_consultar.transaccio");
		InversionesCedeDiasDePagoConsultarOpUsuario = properties.getProperty("op.inversiones_cede_dias_de_pago_consultar.usuario");
		InversionesCedeDiasDePagoConsultarOpSucOrigen = properties.getProperty("op.inversiones_cede_dias_de_pago_consultar.suc_origen");
		InversionesCedeDiasDePagoConsultarOpSucDestino = properties.getProperty("op.inversiones_cede_dias_de_pago_consultar.suc_destino");
		InversionesCedeDiasDePagoConsultarOpModulo = properties.getProperty("op.inversiones_cede_dias_de_pago_consultar.modulo");
		
		InversionesCedePlazosConsultarOpTipConsulL1 = properties.getProperty("op.inversiones_cede_plazos_consultar.tip_consul_l1");
		InversionesCedePlazosConsultarOpTransaccio = properties.getProperty("op.inversiones_cede_plazos_consultar.transaccio");
		InversionesCedePlazosConsultarOpUsuario = properties.getProperty("op.inversiones_cede_plazos_consultar.usuario");
		InversionesCedePlazosConsultarOpSucOrigen = properties.getProperty("op.inversiones_cede_plazos_consultar.suc_origen");
		InversionesCedePlazosConsultarOpSucDestino = properties.getProperty("op.inversiones_cede_plazos_consultar.suc_destino");
		InversionesCedePlazosConsultarOpModulo = properties.getProperty("op.inversiones_cede_plazos_consultar.modulo");	
		
		InversionesAltaPagarOptransaccio = properties.getProperty("op.inversiones_alta_pagar.transaccio");
		InversionesAltaPagarOpUsuario = properties.getProperty("op.inversiones_alta_pagar.usuario");
		InversionesAltaPagarOpSucOrigen = properties.getProperty("op.inversiones_alta_pagar.suc_origen");
		InversionesAltaPagarOpSucDestino = properties.getProperty("op.inversiones_alta_pagar.suc_destino");
		InversionesAltaPagarOpModulo = properties.getProperty("op.inversiones_alta_pagar.modulo");
		
		InversionesPagareInformacionGuardarOpTransaccio = properties.getProperty("op.inversiones_pagare_informacion_guardar.transaccio");
		InversionesPagareInformacionGuardarOpUsuario = properties.getProperty("op.inversiones_pagare_informacion_guardar.usuario");
		InversionesPagareInformacionGuardarOpSucOrigen = properties.getProperty("op.inversiones_pagare_informacion_guardar.suc_origen");
		InversionesPagareInformacionGuardarOpSucDestino = properties.getProperty("op.inversiones_pagare_informacion_guardar.suc_destino");
		InversionesPagareInformacionGuardarOpModulo = properties.getProperty("op.inversiones_pagare_informacion_guardar.modulo");
		
		InversionesCedeAltaOpInvMoneda = properties.getProperty("op.inversiones_cede_alta.inv_moneda");
		InversionesCedeAltaOpTransaccio = properties.getProperty("op.inversiones_cede_alta.transaccio");
		InversionesCedeAltaOpUsuario = properties.getProperty("op.inversiones_cede_alta.usuario");
		InversionesCedeAltaOpSucOrigen = properties.getProperty("op.inversiones_cede_alta.suc_origen");
		InversionesCedeAltaOpSucDestino = properties.getProperty("op.inversiones_cede_alta.suc_destino");
		InversionesCedeAltaOpModulo = properties.getProperty("op.inversiones_cede_alta.modulo");
		
		InversionesCedePagarOpTransaccio = properties.getProperty("op.inversiones_cede_pagar.transaccio");
		InversionesCedePagarOpUsuario = properties.getProperty("op.inversiones_cede_pagar.usuario");
		InversionesCedePagarOpSucOrigen = properties.getProperty("op.inversiones_cede_pagar.suc_origen");
		InversionesCedePagarOpSucDestino = properties.getProperty("op.inversiones_cede_pagar.suc_destino");
		InversionesCedePagarOpModulo = properties.getProperty("op.inversiones_cede_pagar.modulo");
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
		datosProcesoLiquidacion.addProperty("Inv_rAutor", InversionesProcesoLiquidacionGenerarOpInvrAutor);
		datosProcesoLiquidacion.addProperty("Inv_MonRef", Double.parseDouble(InversionesProcesoLiquidacionGenerarOpInvMonRef));
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
		datosIversionVsEstadoCuenta.addProperty("Cor_MoLiDi", Double.parseDouble(InversionesContraEstadoCuentaActualizarOpCorMoLiDi));
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
	
	/**
	 * Método para consultar los plazos para nueva inversión CEDE VALOR
	 * ProcedureName: CEPLAZOSCON
	 * @param datosInversionesCedePlazos
	 * <pre>
	 * {
	 *	Pla_Moneda: String,
	 *	Pla_Produc: String,
	 *	Tip_Consul?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 * 	plazos: {
	 * 		plazo: [
	 * 			{
	 * 				Pla_Numero: String,
	 * 				Pla_Moneda: String,
	 * 				Pla_Plazo: Integer,
	 * 				Pla_Produc: String,
	 * 				Pla_Descri: String
	 * 			}
	 * 		]
	 * 	}
	 * }
	 * </pre>
	 * or
	 * <pre>
	 * {
	 * 	plazo: {
	 * 		Pla_Numero: String,
	 * 		Pla_Moneda: String,
	 * 		Pla_Plazo: Integer,
	 * 		Pla_Produc: String,
	 * 		Pro_Descri: String,
	 * 		Pla_Descri: String,
	 * 		Pla_Dias: Integer
	 * 	}
	 * }
	 * </pre>
	 */
	public JsonObject inversionesCedePlazosConsultar(JsonObject datosInversionesCedePlazos) {
		logger.info("COMMONS: Comenzando inversionesCedePlazosConsultar metodo... ");
		if(!datosInversionesCedePlazos.has("Pla_Numero"))
			datosInversionesCedePlazos.addProperty("Pla_Numero", "");
		if(!datosInversionesCedePlazos.has("Pla_Moneda"))
			datosInversionesCedePlazos.addProperty("Pla_Moneda", "");
		if(!datosInversionesCedePlazos.has("Fec_Inicio"))
			datosInversionesCedePlazos.addProperty("Fec_Inicio", "");
		if(!datosInversionesCedePlazos.has("Fec_Final"))
			datosInversionesCedePlazos.addProperty("Fec_Final", "");
		if(!datosInversionesCedePlazos.has("NumTransac"))
			datosInversionesCedePlazos.addProperty("NumTransac", "");
		if(!datosInversionesCedePlazos.has("Tip_Consul") || datosInversionesCedePlazos.get("Tip_Consul").getAsString().isEmpty())
			datosInversionesCedePlazos.addProperty("Tip_Consul", InversionesCedePlazosConsultarOpTipConsulL1);
		datosInversionesCedePlazos.addProperty("Transaccio", InversionesCedePlazosConsultarOpTransaccio);
		datosInversionesCedePlazos.addProperty("Usuario", InversionesCedePlazosConsultarOpUsuario);
		datosInversionesCedePlazos.addProperty("SucOrigen", InversionesCedePlazosConsultarOpSucOrigen);
		datosInversionesCedePlazos.addProperty("SucDestino", InversionesCedePlazosConsultarOpSucDestino);
		datosInversionesCedePlazos.addProperty("Modulo", InversionesCedePlazosConsultarOpModulo);
		
		String inversionesCedePlazosConsultarOp;
		
		if (datosInversionesCedePlazos.get("Tip_Consul").getAsString().equals("C4")) {
			inversionesCedePlazosConsultarOp = InversionesCedePlazosConsultarC4Op;
		} else {
			inversionesCedePlazosConsultarOp = InversionesCedePlazosConsultarL1Op;
		}
		
		JsonObject inversionesCedePlazosConsultarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesCedePlazosConsultarOp, datosInversionesCedePlazos);
		logger.info("COMMONS: Finalizando inversionesCedePlazosConsultar metodo... ");
		return inversionesCedePlazosConsultarOpResultadoObjeto;
	}//Cierre del método

	/**
	* Método para consultar el listado de días de pago para nueva inversión CEDE
	* ProcedureName: CEDIAPAGCON
	* @param datosInversionesCedeDiasDePago
	* <pre>
	* {
	*	FechaSis: String
	* }
	* </pre>
	* @return
	* <pre>
	* {
	*     diasDePago: {
	*         diaDePago: [
	*             {
	*                 DiaP_Id: Integer,
	*                 DiaP_Desc: String
	*             }
	*         ]
	*     }
	* }
	* </pre>
	*/
	public JsonObject inversionesCedeDiasDePagoConsultar(JsonObject datosInversionesCedeDiasDePago) {
		logger.info("COMMONS: Comenzando inversionesCedeDiasDePagoConsultar metodo... ");
		if(!datosInversionesCedeDiasDePago.has("NumTransac"))
			datosInversionesCedeDiasDePago.addProperty("NumTransac", "");
		datosInversionesCedeDiasDePago.addProperty("Transaccio", InversionesCedeDiasDePagoConsultarOpTransaccio);
		datosInversionesCedeDiasDePago.addProperty("Usuario", InversionesCedeDiasDePagoConsultarOpUsuario);
		datosInversionesCedeDiasDePago.addProperty("SucOrigen", InversionesCedeDiasDePagoConsultarOpSucOrigen);
		datosInversionesCedeDiasDePago.addProperty("SucDestino", InversionesCedeDiasDePagoConsultarOpSucDestino);
		datosInversionesCedeDiasDePago.addProperty("Modulo", InversionesCedeDiasDePagoConsultarOpModulo);
		JsonObject inversionesCedeDiasDePagoConsultarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesCedeDiasDePagoConsultarOp, datosInversionesCedeDiasDePago);
		logger.info("COMMONS: Finalizando inversionesCedeDiasDePagoConsultar metodo... ");
		return inversionesCedeDiasDePagoConsultarOpResultadoObjeto;
	}//Cierre del método

	/**
	 * Método para dar de alta una inversión 
	 * ProcedureName: ININVERSALT
	 * @param datosInversionesAlta
	 * <pre>
	 * {
	 *	Inv_FecIni String,
     *  Inv_FecVen String,
     *  Inv_Cantid Integer,
     *  Inv_Tasa Double,
     *  Inv_Autori String,
     *  Inv_Moneda String,
     *  Inv_ISR Double,
     *  Inv_Cuenta String,
     *  Inv_TipImp String,
     *  Inv_InvAnt String,
     *  Inv_TBruta Double,
     *  Inv_CveSeg String,
     *  Inv_Origen String,
     *  Inv_ClaInv String,
     *  Inv_MonRef integer,
     *  Inv_CanPer Double,
     *  Inv_CanPer String,
     *  I_Numero String,
	 * 	NumTransac?: String,	 
	 * </pre>
	 * @return
	 *  alta: {
	 *         Err_Codigo: String,
	 *         Err_Mensaj: String,
	 *         Err_Variab: String,
	 *         Inv_Numero: String
	 *   }
	 * }
	 * </pre>
	 */
	public JsonObject inversionesAlta(JsonObject datosInversionesAlta) {
		logger.info("COMMONS: Comenzando inversionesAlta metodo... ");
		if(!datosInversionesAlta.has("NumTransac"))
			datosInversionesAlta.addProperty("NumTransac", "");
		datosInversionesAlta.addProperty("Transaccio", InversionesAltaOpTransaccio);
		datosInversionesAlta.addProperty("Usuario", InversionesAltaOpUsuario);
		datosInversionesAlta.addProperty("SucOrigen", InversionesAltaOpSucOrigen);
		datosInversionesAlta.addProperty("SucDestino", InversionesAltaOpSucDestino);
		datosInversionesAlta.addProperty("Modulo", InversionesAltaOpModulo);
		JsonObject inversionesPagareAltadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesAltaOp, datosInversionesAlta);
		logger.info("COMMONS: Finalizando inversionesAlta metodo... ");
		return inversionesPagareAltadoObjeto;
	}//Cierre del método
	
	/**
	* Método para guardar informacion adicional de nueva inversion Pagare
	* ProcedureName: INADICIOALT
	* @param inversionesPagareInformacionGuardar
	* <pre>
	* {
	*	Adi_Invers: String,
	*	Adi_InsLiq: String,
	*	Adi_MoReGr: Numeric,
	* }
	* </pre>
	* @return
	* <pre>
	* {
	 *     procesoLiquidacionGenerar: {
	 *         Err_Codigo: String,
	 *         Err_Mensaj: String
	 *     }
	 * }
	* </pre>
	*/
	public JsonObject inversionesPagareInformacionGuardar(JsonObject datosInversionesPagareInformacionGuardar) {
		logger.info("COMMONS: Comenzando inversionesPagareInformacionGuardar metodo... ");
		if(!datosInversionesPagareInformacionGuardar.has("NumTransac"))
			datosInversionesPagareInformacionGuardar.addProperty("NumTransac", "");
		datosInversionesPagareInformacionGuardar.addProperty("Transaccio", InversionesPagareInformacionGuardarOpTransaccio);
		datosInversionesPagareInformacionGuardar.addProperty("Usuario", InversionesPagareInformacionGuardarOpUsuario);
		datosInversionesPagareInformacionGuardar.addProperty("SucOrigen", InversionesPagareInformacionGuardarOpSucOrigen);
		datosInversionesPagareInformacionGuardar.addProperty("SucDestino", InversionesPagareInformacionGuardarOpSucDestino);
		datosInversionesPagareInformacionGuardar.addProperty("Modulo", InversionesPagareInformacionGuardarOpModulo);
		JsonObject inversionesPagareInformacionGuardarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesPagareInformacionGuardarOp, datosInversionesPagareInformacionGuardar);
		logger.info("COMMONS: Finalizando inversionesPagareInformacionGuardar metodo... ");
		return inversionesPagareInformacionGuardarOpResultadoObjeto;
	}//Cierre del método

	/**
	* Método que realizá el cargo de la inversion a la cuenta PAGARE
	* ProcedureName: ININVERSCAR
	* @param datosInversionesAltaPagar
	* <pre>
	* {
	* 	Inv_Numero: String,
    *   Inv_FecIni: String,
    *   Inv_Cantid: Numeric,
    *   Inv_Status: String,
    *   Inv_Cuenta: String,
    *   Fecha: String,
    *   NumTransac: String
	* }
	* </pre>
	* @return
	* <pre>	
	*  {
	*     cargo: {
	*         Err_Codigo: String,
	*         Err_Mensaj: String,
	*         Err_Variab: String
	*     }
	* }	
	* </pre>
	*/
	public JsonObject inversionesAltaPagar(JsonObject datosInversionesAltaPagar) {
		logger.info("COMMONS: Comenzando datosInversionesAltaPagar metodo... ");		
		datosInversionesAltaPagar.addProperty("Transaccio", InversionesAltaPagarOptransaccio);
		datosInversionesAltaPagar.addProperty("Usuario", InversionesAltaPagarOpUsuario);
		datosInversionesAltaPagar.addProperty("SucOrigen", InversionesAltaPagarOpSucOrigen);
		datosInversionesAltaPagar.addProperty("SucDestino", InversionesAltaPagarOpSucDestino);
		datosInversionesAltaPagar.addProperty("Modulo", InversionesAltaPagarOpModulo);
		JsonObject InversionesAltaPagarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesAltaPagarOp, datosInversionesAltaPagar);
		logger.info("COMMONS: Finalizando datosInversionesAltaPagar metodo... ");
		return InversionesAltaPagarOpResultadoObjeto;
	}//Cierre del método
	
	/**
	* Método para dar de alta una nueva inversión de tipo CEDE
	* ProcedureName: CEINVERSALT
	* @param datosInversionesCedeAlta
	* <pre>
	* {
	* 	Inv_Cuenta: String,
    *   Inv_Plazo: String,
    *   Inv_OriInv: String,
    *   Inv_FecVen: String,
    *   Inv_Cantid: Double,
    *   Inv_Tasa: Double,
    *   Inv_ForTas: String,
    *   Inv_Produc: Double,
    *   Inv_PorBas: Double,
    *   Inv_Puntos: Double,
    *   Inv_TBruta: Double,
    *   NumTransac: String,
    *   FechaSis: String
	* }
	* </pre>
	* @return
	* <pre>	
	*  {
	*     inversion: {
	*         Err_Codigo: String,
	*         Err_Mensaj: String,
	*         Inv_Numero: String,
	*         Err_Variab: String
	*     }
	* }	
	* </pre>
	*/
	public JsonObject inversionesCedeAlta(JsonObject datosInversionesCedeAlta) {
		if(!datosInversionesCedeAlta.has("Inv_Numero"))
			datosInversionesCedeAlta.addProperty("Inv_Numero", "");
		if(!datosInversionesCedeAlta.has("Inv_Autori"))
			datosInversionesCedeAlta.addProperty("Inv_Autori", "");
		if(!datosInversionesCedeAlta.has("Con_Origen"))
			datosInversionesCedeAlta.addProperty("Con_Origen", "");
		if(!datosInversionesCedeAlta.has("Inv_TasBas"))
			datosInversionesCedeAlta.addProperty("Inv_TasBas", "");
		logger.info("COMMONS: Comenzando inversionesCedeAlta metodo... ");
		datosInversionesCedeAlta.addProperty("Inv_Moneda", InversionesCedeAltaOpInvMoneda);
		datosInversionesCedeAlta.addProperty("Transaccio", InversionesCedeAltaOpTransaccio);
		datosInversionesCedeAlta.addProperty("Usuario", InversionesCedeAltaOpUsuario);
		datosInversionesCedeAlta.addProperty("SucOrigen", InversionesCedeAltaOpSucOrigen);
		datosInversionesCedeAlta.addProperty("SucDestino", InversionesCedeAltaOpSucDestino);
		datosInversionesCedeAlta.addProperty("Modulo", InversionesCedeAltaOpModulo);
		JsonObject InversionesAltaPagarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesCedeAltaOp, datosInversionesCedeAlta);
		logger.info("COMMONS: Finalizando inversionesCedeAlta metodo... ");
		return InversionesAltaPagarOpResultadoObjeto;
	}//Cierre del método
	
	/**
	* Método para realizar el cargo de una nueva inversión de tipo CEDE
	* ProcedureName: CECARCUEPRO
	* @param datosInversionesCedePagar
	* <pre>
	* {
	* 	Inv_Numero: String,
    *   NumTransac: String,
    *   FechaSis: String
	* }
	* </pre>
	* @return
	* <pre>	
	*  {
	*     cargo: {
	*         Err_Codigo: String,
	*         Err_Mensaj: String,
	*         Err_Variab?: String
	*     }
	* }	
	* </pre>
	*/
	public JsonObject inversionesCedePagar(JsonObject datosInversionesCedePagar) {
		logger.info("COMMONS: Comenzando inversionesCedePagar metodo... ");
		datosInversionesCedePagar.addProperty("Transaccio", InversionesCedePagarOpTransaccio);
		datosInversionesCedePagar.addProperty("Usuario", InversionesCedePagarOpUsuario);
		datosInversionesCedePagar.addProperty("SucOrigen", InversionesCedePagarOpSucOrigen);
		datosInversionesCedePagar.addProperty("SucDestino", InversionesCedePagarOpSucDestino);
		datosInversionesCedePagar.addProperty("Modulo", InversionesCedePagarOpModulo);
		JsonObject InversionesCedePagarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, InversionesCedePagarOp, datosInversionesCedePagar);
		logger.info("COMMONS: Finalizando inversionesCedePagar metodo... ");
		return InversionesCedePagarOpResultadoObjeto;
	}//Cierre del método

}