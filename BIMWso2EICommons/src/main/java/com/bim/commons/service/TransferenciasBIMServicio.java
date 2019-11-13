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
	private static String CuentaDestinoTransferenciaBIMActivacionOp;
	private static String CuentaDestinoTransferenciaBIMActivacionOpTransaccio;
	private static String CuentaDestinoTransferenciaBIMActivacionOpUsuario;
	private static String CuentaDestinoTransferenciaBIMActivacionOpSucOrigen;
	private static String CuentaDestinoTransferenciaBIMActivacionOpSucDestino;
	private static String CuentaDestinoTransferenciaBIMActivacionOpModulo;
	private static String TransferenciaBIMProcesarOp;
	private static String TransferenciaBIMProcesarOpTransaccio;
	private static String TransferenciaBIMProcesarOpUsuario;
	private static String TransferenciaBIMProcesarOpSucOrigen;
	private static String TransferenciaBIMProcesarOpSucDestino;
	private static String TransferenciaBIMProcesarOpModulo;
	private static String TransferenciaBIMCreacionOp;
	private static String TransferenciaBIMCreacionOpTransaccio;
	private static String TransferenciaBIMCreacionOpUsuario;
	private static String TransferenciaBIMCreacionOpSucOrigen;
	private static String TransferenciaBIMCreacionOpSucDestino;
	private static String TransferenciaBIMCreacionOpModulo;
	
	public TransferenciasBIMServicio() {
		super();
		
		TransferenciasBIMServicio = properties.getProperty("data_service.transferencias_bim_servicio");
		
		CuentasOrigenConsultarOp = properties.getProperty("transferencias_bim_servicio.op.cuentas_origen_consultar");
		CuentaDestinoTransferenciaBIMActivacionOp = properties.getProperty("transferencias_bim_servicio.op.cuentas_destino_transferencia_bim_activacion");
		TransferenciaBIMProcesarOp = properties.getProperty("transferencias_bim_servicio.op.transferencia_bim_procesar");
		TransferenciaBIMCreacionOp = properties.getProperty("transferencias_bim_servicio.op.transferencia_bim_creacion");
		
		CuentasOrigenConsultarOpTipConsul = properties.getProperty("op.cuentas_origen_consultar.tip_consul");
		CuentasOrigenConsultarOpTransaccio = properties.getProperty("op.cuentas_origen_consultar.transaccio");
		CuentasOrigenConsultarOpUsuario = properties.getProperty("op.cuentas_origen_consultar.usuario");
		CuentasOrigenConsultarOpSucOrigen = properties.getProperty("op.cuentas_origen_consultar.suc_origen");
		CuentasOrigenConsultarOpSucDestino = properties.getProperty("op.cuentas_origen_consultar.suc_destino");
		CuentasOrigenConsultarOpModulo = properties.getProperty("op.cuentas_origen_consultar.modulo");
		
		CuentaDestinoTransferenciaBIMActivacionOpTransaccio = properties.getProperty("op.cuentas_origen_consultar.transaccio");
		CuentaDestinoTransferenciaBIMActivacionOpUsuario = properties.getProperty("op.cuentas_origen_consultar.usuario");
		CuentaDestinoTransferenciaBIMActivacionOpSucOrigen = properties.getProperty("op.cuentas_origen_consultar.suc_origen");
		CuentaDestinoTransferenciaBIMActivacionOpSucDestino = properties.getProperty("op.cuentas_origen_consultar.suc_destino");
		CuentaDestinoTransferenciaBIMActivacionOpModulo = properties.getProperty("op.cuentas_origen_consultar.modulo");
		
		TransferenciaBIMProcesarOpTransaccio = properties.getProperty("op.transferencia_bim_procesar.transaccio");
		TransferenciaBIMProcesarOpUsuario = properties.getProperty("op.transferencia_bim_procesar.usuario");
		TransferenciaBIMProcesarOpSucOrigen = properties.getProperty("op.transferencia_bim_procesar.suc_origen");
		TransferenciaBIMProcesarOpSucDestino = properties.getProperty("op.transferencia_bim_procesar.suc_destino");
		TransferenciaBIMProcesarOpModulo = properties.getProperty("op.transferencia_bim_procesar.modulo");

		TransferenciaBIMCreacionOpTransaccio = properties.getProperty("op.transferencia_bim_creacion.transaccio");
		TransferenciaBIMCreacionOpUsuario = properties.getProperty("op.transferencia_bim_creacion.usuario");
		TransferenciaBIMCreacionOpSucOrigen = properties.getProperty("op.transferencia_bim_creacion.suc_origen");
		TransferenciaBIMCreacionOpSucDestino = properties.getProperty("op.transferencia_bim_creacion.suc_destino");
		TransferenciaBIMCreacionOpModulo = properties.getProperty("op.transferencia_bim_creacion.modulo");
		
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
	
	public JsonObject cuentaDestinoTransferenciaBIMActivacion(JsonObject datosCuentaDestinoTransferenciaBIMActivacion) {
		logger.info("COMMONS: Comenzando cuentaDestinoTransferenciaBIMActivacion metodo... ");
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Transaccio", CuentaDestinoTransferenciaBIMActivacionOpTransaccio);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Usuario", CuentaDestinoTransferenciaBIMActivacionOpUsuario);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("SucOrigen", CuentaDestinoTransferenciaBIMActivacionOpSucOrigen);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("SucDestino", CuentaDestinoTransferenciaBIMActivacionOpSucDestino);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Modulo", CuentaDestinoTransferenciaBIMActivacionOpModulo);
		JsonObject datosCuentaDestinoTransferenciaBIMActivacionOpResultadoObjeto = Utilerias.performOperacion(TransferenciasBIMServicio, CuentaDestinoTransferenciaBIMActivacionOp, datosCuentaDestinoTransferenciaBIMActivacion);
		logger.info("datosCuentasOrigenConsultarOpResultadoObjeto" + datosCuentaDestinoTransferenciaBIMActivacionOpResultadoObjeto);
		logger.info("COMMONS: Finalizando cuentaDestinoTransferenciaBIMActivacion metodo... ");
		return datosCuentaDestinoTransferenciaBIMActivacionOpResultadoObjeto;
	}
	
	public JsonObject transferenciaBIMProcesar(JsonObject datosTransferenciaBIMProcesar) {
		logger.info("COMMONS: Comenzando transferenciaBIMProcesar metodo... ");
		datosTransferenciaBIMProcesar.addProperty("Trb_RFC", "");
		datosTransferenciaBIMProcesar.addProperty("Trb_IVA", 0.000);
		datosTransferenciaBIMProcesar.addProperty("Trb_DireIP", "");
		datosTransferenciaBIMProcesar.addProperty("Transaccio", TransferenciaBIMProcesarOpTransaccio);
		datosTransferenciaBIMProcesar.addProperty("Usuario", TransferenciaBIMProcesarOpUsuario);
		datosTransferenciaBIMProcesar.addProperty("SucOrigen", TransferenciaBIMProcesarOpSucOrigen);
		datosTransferenciaBIMProcesar.addProperty("SucDestino", TransferenciaBIMProcesarOpSucDestino);
		datosTransferenciaBIMProcesar.addProperty("Modulo", TransferenciaBIMProcesarOpModulo);
		JsonObject datosTransferenciaBIMProcesarOpResultadoObjeto = Utilerias.performOperacion(TransferenciasBIMServicio, TransferenciaBIMProcesarOp, datosTransferenciaBIMProcesar);
		logger.info("datosTransferenciaBIMProcesarOpResultadoObjeto" + datosTransferenciaBIMProcesarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaBIMProcesar metodo... ");
		return datosTransferenciaBIMProcesarOpResultadoObjeto;
	}

	public JsonObject transferenciaBIMCreacion(JsonObject datosTransferenciaBIMCreacion) {
		logger.info("COMMONS: Comenzando transferenciaBIMCreacion metodo... ");
		datosTransferenciaBIMCreacion.addProperty("Trb_RFC", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_IVA", 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_UsuAut", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_TipDur", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_DurTra", 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_DiAnEm", 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_CLABE", "");
		datosTransferenciaBIMCreacion.addProperty("Transaccio", TransferenciaBIMCreacionOpTransaccio);
		datosTransferenciaBIMCreacion.addProperty("Usuario", TransferenciaBIMCreacionOpUsuario);
		datosTransferenciaBIMCreacion.addProperty("SucOrigen", TransferenciaBIMCreacionOpSucOrigen);
		datosTransferenciaBIMCreacion.addProperty("SucDestino", TransferenciaBIMCreacionOpSucDestino);
		datosTransferenciaBIMCreacion.addProperty("Modulo", TransferenciaBIMCreacionOpModulo);
		JsonObject transferenciaBIMCreacionOpResultadoObjeto = Utilerias.performOperacion(TransferenciasBIMServicio, TransferenciaBIMCreacionOp, datosTransferenciaBIMCreacion);
		logger.info("transferenciaBIMCreacionOpResultadoObjeto" + transferenciaBIMCreacionOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaBIMCreacion metodo... ");
		return transferenciaBIMCreacionOpResultadoObjeto;
	}
}
