package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class SPEIServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicio.class);
	
	private static String SPEIServicio;
	private static String HorariosSPEIConsultarOp;
	private static String HorariosSPEIConsultarOpHorHorIni;
	private static String HorariosSPEIConsultarOpHorHorFin;
	private static String HorariosSPEIConsultarOpTipConsul;
	private static String HorariosSPEIConsultarOpTransaccio;
	private static String HorariosSPEIConsultarOpUsuario;
	private static String HorariosSPEIConsultarOpSucOrigen;
	private static String HorariosSPEIConsultarOpSucDestino;
	private static String HorariosSPEIConsultarOpModulo;
	private static String TransferenciaSPEICreacionOp;
	private static String TransferenciaSPEICreacionOpTransaccio;
	private static String TransferenciaSPEICreacionOpUsuario;
	private static String TransferenciaSPEICreacionOpSucOrigen;
	private static String TransferenciaSPEICreacionOpSucDestino;
	private static String TransferenciaSPEICreacionOpModulo;
	private static String TransferenciaSPEIProcesarOp;
	private static String TransferenciaSPEIProcesarOpTrsCuBe;
	private static String TransferenciaSPEIProcesarOpTrsTipPag;
	private static String TransferenciaSPEIProcesarOpTrsTipTra;
	private static String TransferenciaSPEIProcesarOpTrsValFir;
	private static String TransferenciaSPEIProcesarOpTransaccio;
	private static String TransferenciaSPEIProcesarOpUsuario;
	private static String TransferenciaSPEIProcesarOpSucOrigen;
	private static String TransferenciaSPEIProcesarOpSucDestino;
	private static String TransferenciaSPEIProcesarOpModulo;
	private static String TransferenciaSPEIConsultarOp;
	private static String TransferenciaSPEIConsultarOpTrnStatus;
	private static String TransferenciaSPEIConsultarOpTipConsul;
	private static String TransferenciaSPEIConsultarOpTransaccio;
	private static String TransferenciaSPEIConsultarOpUsuario;
	private static String TransferenciaSPEIConsultarOpSucOrigen;
	private static String TransferenciaSPEIConsultarOpSucDestino;
	private static String TransferenciaSPEIConsultarOpModulo;
	
	public SPEIServicio() {
		super();
		
		SPEIServicio = properties.getProperty("data_service.spei_servicio");
		
		HorariosSPEIConsultarOp = properties.getProperty("spei_servicio.op.horarios_spei_consultar");
		TransferenciaSPEICreacionOp = properties.getProperty("spei_servicio.op.transferencias_spei_creacion");
		TransferenciaSPEIProcesarOp = properties.getProperty("spei_servicio.op.transferencias_spei_procesar");
		TransferenciaSPEIConsultarOp = properties.getProperty("spei_servicio.op.transferencias_spei_consultar");
		
		HorariosSPEIConsultarOpTipConsul = properties.getProperty("op.horarios_spei_consultar.tip_consul");
		HorariosSPEIConsultarOpTransaccio = properties.getProperty("op.horarios_spei_consultar.transaccio");
		HorariosSPEIConsultarOpUsuario = properties.getProperty("op.horarios_spei_consultar.usuario");
		HorariosSPEIConsultarOpSucOrigen = properties.getProperty("op.horarios_spei_consultar.suc_origen");
		HorariosSPEIConsultarOpSucDestino = properties.getProperty("op.horarios_spei_consultar.suc_destino");
		HorariosSPEIConsultarOpModulo = properties.getProperty("op.horarios_spei_consultar.modulo");
		HorariosSPEIConsultarOpHorHorIni = properties.getProperty("op.horarios_spei_consultar.hor_hor_ini");
		HorariosSPEIConsultarOpHorHorFin = properties.getProperty("op.horarios_spei_consultar.hor_hor_fin");
		
				 
		TransferenciaSPEICreacionOpTransaccio = properties.getProperty("op.transferencias_spei_creacion.transaccio");
		TransferenciaSPEICreacionOpUsuario = properties.getProperty("op.transferencias_spei_creacion.usuario");
		TransferenciaSPEICreacionOpSucOrigen = properties.getProperty("op.transferencias_spei_creacion.suc_origen");
		TransferenciaSPEICreacionOpSucDestino = properties.getProperty("op.transferencias_spei_creacion.suc_destino");
		TransferenciaSPEICreacionOpModulo = properties.getProperty("op.transferencias_spei_creacion.modulo");
		
		TransferenciaSPEIProcesarOpTrsCuBe = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransferenciaSPEIProcesarOpTrsTipPag = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransferenciaSPEIProcesarOpTrsTipTra = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransferenciaSPEIProcesarOpTrsValFir = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransferenciaSPEIProcesarOpTransaccio = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransferenciaSPEIProcesarOpUsuario = properties.getProperty("op.transferencias_spei_procesar.usuario");
		TransferenciaSPEIProcesarOpSucOrigen = properties.getProperty("op.transferencias_spei_procesar.suc_origen");
		TransferenciaSPEIProcesarOpSucDestino = properties.getProperty("op.transferencias_spei_procesar.suc_destino");
		TransferenciaSPEIProcesarOpModulo = properties.getProperty("op.transferencias_spei_procesar.modulo");
		
		TransferenciaSPEIConsultarOpTrnStatus = properties.getProperty("op.transferencias_spei_consultar.trn_status");
		TransferenciaSPEIConsultarOpTipConsul = properties.getProperty("op.transferencias_spei_consultar.tip_consul");
		TransferenciaSPEIConsultarOpTransaccio = properties.getProperty("op.transferencias_spei_consultar.transaccio");
		TransferenciaSPEIConsultarOpUsuario = properties.getProperty("op.transferencias_spei_consultar.usuario");
		TransferenciaSPEIConsultarOpSucOrigen = properties.getProperty("op.transferencias_spei_consultar.suc_origen");
		TransferenciaSPEIConsultarOpSucDestino = properties.getProperty("op.transferencias_spei_consultar.suc_destino");
		TransferenciaSPEIConsultarOpModulo = properties.getProperty("op.transferencias_spei_consultar.modulo");
	}

	/**
	 * Método para consultar los horarios de disponibilidad para transferencias SPEI
	 * ProcedureName: SPHORARICON
	 * @param datosHorariosSPEI
	 * @return
	 */
	public JsonObject horariosSPEIConsultar(JsonObject datosHorariosSPEI) {
		logger.info("COMMONS: Empezando horariosSPEIConsultar metodo... ");
		if(!datosHorariosSPEI.has("Msj_Error"))
			datosHorariosSPEI.addProperty("Msj_Error", "");
		if(!datosHorariosSPEI.has("Hor_MonIni"))
			datosHorariosSPEI.addProperty("Hor_MonIni", 0);
		if(!datosHorariosSPEI.has("Hor_MonFin"))
			datosHorariosSPEI.addProperty("Hor_MonFin", 0);
		if(!datosHorariosSPEI.has("NumTransac"))
			datosHorariosSPEI.addProperty("NumTransac", "");

		datosHorariosSPEI.addProperty("Hor_HorIni", HorariosSPEIConsultarOpHorHorIni);
		datosHorariosSPEI.addProperty("Hor_HorFin", HorariosSPEIConsultarOpHorHorFin);
		datosHorariosSPEI.addProperty("Tip_Consul", HorariosSPEIConsultarOpTipConsul);
		datosHorariosSPEI.addProperty("Transaccio", HorariosSPEIConsultarOpTransaccio);
		datosHorariosSPEI.addProperty("Usuario", HorariosSPEIConsultarOpUsuario);
		datosHorariosSPEI.addProperty("SucOrigen", HorariosSPEIConsultarOpSucOrigen);
		datosHorariosSPEI.addProperty("SucDestino", HorariosSPEIConsultarOpSucDestino);
		datosHorariosSPEI.addProperty("Modulo", HorariosSPEIConsultarOpModulo);
		JsonObject horariosSPEIConsultarOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, HorariosSPEIConsultarOp, datosHorariosSPEI);
		logger.info("horariosSPEIConsultarOpResultadoObjeto" + horariosSPEIConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando horariosSPEIConsultar metodo... ");
		return horariosSPEIConsultarOpResultadoObjeto;
	}

	/**
	 * Método para crear transferencias SPEI
	 * ProcedureName: NBTRASPEALT
	 * @param datosTransfarenciaSPEI
	 * <pre>
	 * {
	 *  Trs_Usuari: String,
	 *  Trs_Client: String,
	 *  Trs_CueOri: String,
	 *  Trs_CueDes: String,
	 *  Trs_Monto: Double,
	 *  Trs_Descri: String,
	 *  Trs_PriRef: String,
	 *  Trs_SegRef: String,
	 *  Trs_RFC: String,
	 *  Trs_IVA: Double,
	 *  Trs_Email: String,
	 *  Trs_Tipo: String,
	 *  Trs_UsuCap: String,
	 *  Trs_TipTra: String,
	 *  Trs_Frecue: String,
	 *  Trs_FePrEn: String,
	 *  Trs_TipDur: String,
	 *  Trs_DurFec: String,
	 *  Trs_DurTra: Numeric,
	 *  Trs_DiAnEm: Numeric,
	 *  NumTransac: String,
	 *  FechaSis: String
	 * }
	 * </pre>
	 * @return
	 */
	public JsonObject transferenciaSPEICreacion(JsonObject datosTransfarenciaSPEI) {
		logger.info("COMMONS: Comenzando transferenciaSPEICreacion metodo... ");
		if(!datosTransfarenciaSPEI.has("Trs_SegRef"))
			datosTransfarenciaSPEI.addProperty("Trs_SegRef", "");
		if(!datosTransfarenciaSPEI.has("Trs_RFC"))
			datosTransfarenciaSPEI.addProperty("Trs_RFC", "");
		if(!datosTransfarenciaSPEI.has("Trs_Email"))
			datosTransfarenciaSPEI.addProperty("Trs_Email", "");
		if(!datosTransfarenciaSPEI.has("Trs_TipDur"))
			datosTransfarenciaSPEI.addProperty("Trs_TipDur", "");
		if(!datosTransfarenciaSPEI.has("Trs_IVA"))
			datosTransfarenciaSPEI.addProperty("Trs_IVA", 0);
		datosTransfarenciaSPEI.addProperty("Trs_DurTra", 0);
		datosTransfarenciaSPEI.addProperty("Trs_DiAnEm", 0);
		datosTransfarenciaSPEI.addProperty("Transaccio", TransferenciaSPEICreacionOpTransaccio);
		datosTransfarenciaSPEI.addProperty("Usuario", TransferenciaSPEICreacionOpUsuario);
		datosTransfarenciaSPEI.addProperty("SucOrigen", TransferenciaSPEICreacionOpSucOrigen);
		datosTransfarenciaSPEI.addProperty("SucDestino", TransferenciaSPEICreacionOpSucDestino);
		datosTransfarenciaSPEI.addProperty("Modulo", TransferenciaSPEICreacionOpModulo);
		JsonObject transferenciaSPEICreacionOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, TransferenciaSPEICreacionOp, datosTransfarenciaSPEI);
		logger.info("transferenciaSPEICreacionOpResultadoObjeto" + transferenciaSPEICreacionOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaSPEICreacion metodo... ");
		return transferenciaSPEICreacionOpResultadoObjeto;
	}
	
	/**
	 * Método para procesar la transferencia SPEI
	 * ProcedureName: NBTRASPEPRO
	 * @param datosTransferenciaSPEI
	 * @return
	 */
	public JsonObject transferenciaSPEIProcesar(JsonObject datosTransferenciaSPEI) {
		logger.info("COMMONS: Comenzando transferenciaSPEIProcesar metodo... ");
		datosTransferenciaSPEI.addProperty("Trs_OrdPag", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_IVA", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_Comisi", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_IVAPro", 0.0000);
		if(!datosTransferenciaSPEI.has("Trs_RFC"))
			datosTransferenciaSPEI.addProperty("Trs_RFC", "");
		datosTransferenciaSPEI.addProperty("Trs_RFCPro", "");
		datosTransferenciaSPEI.addProperty("Trs_CuBeAd", "");
		datosTransferenciaSPEI.addProperty("Trs_TiCuBA", "");
		datosTransferenciaSPEI.addProperty("Trs_NoBeAd", "");
		datosTransferenciaSPEI.addProperty("Trs_ConPaU", "");
		if(!datosTransferenciaSPEI.has("Trs_DaBeAd"))
			datosTransferenciaSPEI.addProperty("Trs_DaBeAd", "");
		datosTransferenciaSPEI.addProperty("Trs_DireIP", "");
		datosTransferenciaSPEI.addProperty("Trs_TiCuBe", TransferenciaSPEIProcesarOpTrsCuBe);
		datosTransferenciaSPEI.addProperty("Trs_TipPag", TransferenciaSPEIProcesarOpTrsTipPag);
		datosTransferenciaSPEI.addProperty("Trs_TipTra", TransferenciaSPEIProcesarOpTrsTipTra);
		datosTransferenciaSPEI.addProperty("Trs_ValFir", TransferenciaSPEIProcesarOpTrsValFir);
		datosTransferenciaSPEI.addProperty("Transaccio", TransferenciaSPEIProcesarOpTransaccio);
		datosTransferenciaSPEI.addProperty("Usuario", TransferenciaSPEIProcesarOpUsuario);
		datosTransferenciaSPEI.addProperty("SucOrigen", TransferenciaSPEIProcesarOpSucOrigen);
		datosTransferenciaSPEI.addProperty("SucDestino", TransferenciaSPEIProcesarOpSucDestino);
		datosTransferenciaSPEI.addProperty("Modulo", TransferenciaSPEIProcesarOpModulo);
		JsonObject transferenciaSPEIProcesarOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, TransferenciaSPEIProcesarOp, datosTransferenciaSPEI);
		logger.info("transferenciaSPEIProcesarOpResultadoObjeto" + transferenciaSPEIProcesarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaSPEIProcesar metodo... ");
		return transferenciaSPEIProcesarOpResultadoObjeto;
	}
	
	/**
	 * Método para consultar transferencias SPEI
	 * ProcedureName: NBTRANACCON
	 * @param datosTransferenciaSPEI
	 * @return
	 */
	public JsonObject transferenciaSPEIConsultar(JsonObject datosTransferenciaSPEI) {
		logger.info("COMMONS: Comenzando transferenciaSPEIConsultar metodo... ");
		datosTransferenciaSPEI.addProperty("Trn_Client", "");
		datosTransferenciaSPEI.addProperty("Trn_Consec", "");
		datosTransferenciaSPEI.addProperty("Trn_Transf", "");
		datosTransferenciaSPEI.addProperty("NumTransac", "");
		datosTransferenciaSPEI.addProperty("Trn_Status", TransferenciaSPEIConsultarOpTrnStatus);
		datosTransferenciaSPEI.addProperty("Tip_Consul", TransferenciaSPEIConsultarOpTipConsul);
		datosTransferenciaSPEI.addProperty("Transaccio", TransferenciaSPEIConsultarOpTransaccio);
		datosTransferenciaSPEI.addProperty("Usuario", TransferenciaSPEIConsultarOpUsuario);
		datosTransferenciaSPEI.addProperty("SucOrigen", TransferenciaSPEIConsultarOpSucOrigen);
		datosTransferenciaSPEI.addProperty("SucDestino", TransferenciaSPEIConsultarOpSucDestino);
		datosTransferenciaSPEI.addProperty("Modulo", TransferenciaSPEIConsultarOpModulo);
		JsonObject transferenciaSPEIConsultarOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, TransferenciaSPEIConsultarOp, datosTransferenciaSPEI);
		logger.info("transferenciaSPEIConsultarOpResultadoObjeto" + transferenciaSPEIConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaSPEIConsultar metodo... ");
		return transferenciaSPEIConsultarOpResultadoObjeto;
	}

}
