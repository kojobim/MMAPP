package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class SPEIServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicio.class);
	
	private static String SPEIServicio;
	private static String HorariosSPEIConsultarOp;
	private static String HorariosSPEIConsultarOpTipConsul;
	private static String HorariosSPEIConsultarOpTransaccio;
	private static String HorariosSPEIConsultarOpUsuario;
	private static String HorariosSPEIConsultarOpSucOrigen;
	private static String HorariosSPEIConsultarOpSucDestino;
	private static String HorariosSPEIConsultarOpModulo;
	private static String TransaferenciaSPEICreacionOp;
	private static String TransaferenciaSPEICreacionOpTransaccio;
	private static String TransaferenciaSPEICreacionOpUsuario;
	private static String TransaferenciaSPEICreacionOpSucOrigen;
	private static String TransaferenciaSPEICreacionOpSucDestino;
	private static String TransaferenciaSPEICreacionOpModulo;
	private static String TransaferenciaSPEIProcesarOp;
	private static String TransaferenciaSPEIProcesarOpTransaccio;
	private static String TransaferenciaSPEIProcesarOpUsuario;
	private static String TransaferenciaSPEIProcesarOpSucOrigen;
	private static String TransaferenciaSPEIProcesarOpSucDestino;
	private static String TransaferenciaSPEIProcesarOpModulo;
	
	public SPEIServicio() {
		super();
		
		SPEIServicio = properties.getProperty("data_service.spei_servicio");
		
		HorariosSPEIConsultarOp = properties.getProperty("spei_servicio.op.horarios_spei_consultar");
		TransaferenciaSPEICreacionOp = properties.getProperty("spei_servicio.op.transferencias_spei_creacion");
		TransaferenciaSPEIProcesarOp = properties.getProperty("spei_servicio.op.transferencias_spei_procesar");
		
		HorariosSPEIConsultarOpTipConsul = properties.getProperty("op.horarios_spei_consultar.tip_consul");
		HorariosSPEIConsultarOpTransaccio = properties.getProperty("op.horarios_spei_consultar.transaccio");
		HorariosSPEIConsultarOpUsuario = properties.getProperty("op.horarios_spei_consultar.usuario");
		HorariosSPEIConsultarOpSucOrigen = properties.getProperty("op.horarios_spei_consultar.suc_origen");
		HorariosSPEIConsultarOpSucDestino = properties.getProperty("op.horarios_spei_consultar.suc_destino");
		HorariosSPEIConsultarOpModulo = properties.getProperty("op.horarios_spei_consultar.modulo");
				 
		TransaferenciaSPEICreacionOpTransaccio = properties.getProperty("op.transferencias_spei_creacion.transaccio");
		TransaferenciaSPEICreacionOpUsuario = properties.getProperty("op.transferencias_spei_creacion.usuario");
		TransaferenciaSPEICreacionOpSucOrigen = properties.getProperty("op.transferencias_spei_creacion.suc_origen");
		TransaferenciaSPEICreacionOpSucDestino = properties.getProperty("op.transferencias_spei_creacion.suc_destino");
		TransaferenciaSPEICreacionOpModulo = properties.getProperty("op.transferencias_spei_creacion.modulo");
		
		TransaferenciaSPEIProcesarOpTransaccio = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransaferenciaSPEIProcesarOpUsuario = properties.getProperty("op.transferencias_spei_procesar.usuario");
		TransaferenciaSPEIProcesarOpSucOrigen = properties.getProperty("op.transferencias_spei_procesar.suc_origen");
		TransaferenciaSPEIProcesarOpSucDestino = properties.getProperty("op.transferencias_spei_procesar.suc_destino");
		TransaferenciaSPEIProcesarOpModulo = properties.getProperty("op.transferencias_spei_procesar.modulo");
	}

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

	public JsonObject transaferenciaSPEICreacion(JsonObject datosTransfarenciaSPEI) {
		logger.info("COMMONS: Comenzando transaferenciaSPEICreacion metodo... ");
		datosTransfarenciaSPEI.addProperty("Trs_SegRef", "");
		datosTransfarenciaSPEI.addProperty("Trs_RFC", "");
		datosTransfarenciaSPEI.addProperty("Trs_Email", "");
		datosTransfarenciaSPEI.addProperty("Trs_TipDur", "");
		datosTransfarenciaSPEI.addProperty("Trs_IVA", 0);
		datosTransfarenciaSPEI.addProperty("Trs_DurTra", 0);
		datosTransfarenciaSPEI.addProperty("Trs_DiAnEm", 0);
		datosTransfarenciaSPEI.addProperty("Transaccio", TransaferenciaSPEICreacionOpTransaccio);
		datosTransfarenciaSPEI.addProperty("Usuario", TransaferenciaSPEICreacionOpUsuario);
		datosTransfarenciaSPEI.addProperty("SucOrigen", TransaferenciaSPEICreacionOpSucOrigen);
		datosTransfarenciaSPEI.addProperty("SucDestino", TransaferenciaSPEICreacionOpSucDestino);
		datosTransfarenciaSPEI.addProperty("Modulo", TransaferenciaSPEICreacionOpModulo);
		JsonObject transaferenciaSPEICreacionOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, TransaferenciaSPEICreacionOp, datosTransfarenciaSPEI);
		logger.info("transaferenciaSPEICreacionOpResultadoObjeto" + transaferenciaSPEICreacionOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transaferenciaSPEICreacion metodo... ");
		return transaferenciaSPEICreacionOpResultadoObjeto;
	}
	
	public JsonObject transaferenciaSPEIProcesar(JsonObject datosTransferenciaSPEI) {
		datosTransferenciaSPEI.addProperty("Trs_OrdPag", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_IVA", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_Comisi", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_IVAPro", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_RFC", "");
		datosTransferenciaSPEI.addProperty("Trs_RFCPro", "");
		datosTransferenciaSPEI.addProperty("Trs_CuBeAd", "");
		datosTransferenciaSPEI.addProperty("Trs_TiCuBA", "");
		datosTransferenciaSPEI.addProperty("Trs_NoBeAd", "");
		datosTransferenciaSPEI.addProperty("Trs_ConPaU", "");
		datosTransferenciaSPEI.addProperty("Trs_DaBeAd", "");
		datosTransferenciaSPEI.addProperty("Trs_DireIP", "");
		datosTransferenciaSPEI.addProperty("Transaccio", TransaferenciaSPEIProcesarOpTransaccio);
		datosTransferenciaSPEI.addProperty("Usuario", TransaferenciaSPEIProcesarOpUsuario);
		datosTransferenciaSPEI.addProperty("SucOrigen", TransaferenciaSPEIProcesarOpSucOrigen);
		datosTransferenciaSPEI.addProperty("SucDestino", TransaferenciaSPEIProcesarOpSucDestino);
		datosTransferenciaSPEI.addProperty("Modulo", TransaferenciaSPEIProcesarOpModulo);
		JsonObject transaferenciaSPEICreacionOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, TransaferenciaSPEIProcesarOp, datosTransferenciaSPEI);
		logger.info("transaferenciaSPEICreacionOpResultadoObjeto" + transaferenciaSPEICreacionOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transaferenciaSPEICreacion metodo... ");
		return transaferenciaSPEICreacionOpResultadoObjeto;
	}
}
