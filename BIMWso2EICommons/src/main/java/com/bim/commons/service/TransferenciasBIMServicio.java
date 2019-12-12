package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.dao.ResultSetDAO;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class TransferenciasBIMServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(TransferenciasBIMServicio.class);
	
	private ResultSetDAO resultSetDAO;
	
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
	private static String TransferenciaBIMProcesarOpTrbTipTra;
	private static String TransferenciaBIMProcesarOpTrbValFir;
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
	private static String TransferenciaBIMConsultarOp;
	private static String TransferenciaBIMConsultarOpTipConsul;
	private static String TransferenciaBIMConsultarOpTransaccio;
	private static String TransferenciaBIMConsultarOpUsuario;
	private static String TransferenciaBIMConsultarOpSucOrigen;
	private static String TransferenciaBIMConsultarOpSucDestino;
	private static String TransferenciaBIMConsultarOpModulo;
	private static String TransferenciaBIMFirmasConsultarOp;
	private static String TransferenciaBIMFirmasConsultarOpTipConsul;
	private static String TransferenciaBIMFirmasConsultarOpTransaccio;
	private static String TransferenciaBIMFirmasConsultarOpUsuario;
	private static String TransferenciaBIMFirmasConsultarOpSucOrigen;
	private static String TransferenciaBIMFirmasConsultarOpSucDestino;
	private static String TransferenciaBIMFirmasConsultarOpModulo;

	public TransferenciasBIMServicio() {
		super();
		
		this.resultSetDAO = new ResultSetDAO();
		
		TransferenciasBIMServicio = properties.getProperty("data_service.transferencias_bim_servicio");
		
		CuentasOrigenConsultarOp = properties.getProperty("transferencias_bim_servicio.op.cuentas_origen_consultar");
		CuentaDestinoTransferenciaBIMActivacionOp = properties.getProperty("transferencias_bim_servicio.op.cuentas_destino_transferencia_bim_activacion");
		TransferenciaBIMProcesarOp = properties.getProperty("transferencias_bim_servicio.op.transferencia_bim_procesar");
		TransferenciaBIMCreacionOp = properties.getProperty("transferencias_bim_servicio.op.transferencia_bim_creacion");
		TransferenciaBIMConsultarOp = properties.getProperty("transferencias_bim_servicio.op.transferencia_bim_consultar");
		TransferenciaBIMFirmasConsultarOp = properties.getProperty("transferencias_bim_servicio.op.transferencia_bim_firmas_consultar");
		
		CuentasOrigenConsultarOpTipConsul = properties.getProperty("op.cuentas_origen_consultar.tip_consul");
		CuentasOrigenConsultarOpTransaccio = properties.getProperty("op.cuentas_origen_consultar.transaccio");
		CuentasOrigenConsultarOpUsuario = properties.getProperty("op.cuentas_origen_consultar.usuario");
		CuentasOrigenConsultarOpSucOrigen = properties.getProperty("op.cuentas_origen_consultar.suc_origen");
		CuentasOrigenConsultarOpSucDestino = properties.getProperty("op.cuentas_origen_consultar.suc_destino");
		CuentasOrigenConsultarOpModulo = properties.getProperty("op.cuentas_origen_consultar.modulo");
		
		CuentaDestinoTransferenciaBIMActivacionOpTransaccio = properties.getProperty("op.cuentas_destino_transferencia_bim_activacion.transaccio");
		CuentaDestinoTransferenciaBIMActivacionOpUsuario = properties.getProperty("op.cuentas_destino_transferencia_bim_activacion.usuario");
		CuentaDestinoTransferenciaBIMActivacionOpSucOrigen = properties.getProperty("op.cuentas_destino_transferencia_bim_activacion.suc_origen");
		CuentaDestinoTransferenciaBIMActivacionOpSucDestino = properties.getProperty("op.cuentas_destino_transferencia_bim_activacion.suc_destino");
		CuentaDestinoTransferenciaBIMActivacionOpModulo = properties.getProperty("op.cuentas_destino_transferencia_bim_activacion.modulo");

		TransferenciaBIMProcesarOpTrbTipTra = properties.getProperty("op.transferencia_bim_procesar.trb_tip_tra");
		TransferenciaBIMProcesarOpTrbValFir = properties.getProperty("op.transferencia_bim_procesar.trb_val_fir");
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
		
		TransferenciaBIMConsultarOpTipConsul = properties.getProperty("op.transferencia_bim_consultar.tip_consul");
		TransferenciaBIMConsultarOpTransaccio = properties.getProperty("op.transferencia_bim_consultar.transaccio");
		TransferenciaBIMConsultarOpUsuario = properties.getProperty("op.transferencia_bim_consultar.usuario");
		TransferenciaBIMConsultarOpSucOrigen = properties.getProperty("op.transferencia_bim_consultar.suc_origen");
		TransferenciaBIMConsultarOpSucDestino = properties.getProperty("op.transferencia_bim_consultar.suc_destino");
		TransferenciaBIMConsultarOpModulo = properties.getProperty("op.transferencia_bim_consultar.modulo");

		TransferenciaBIMFirmasConsultarOpTipConsul = properties.getProperty("op.transferencia_bim_firmas_consultar.tip_consul");
		TransferenciaBIMFirmasConsultarOpTransaccio = properties.getProperty("op.transferencia_bim_firmas_consultar.transaccio");
		TransferenciaBIMFirmasConsultarOpUsuario = properties.getProperty("op.transferencia_bim_firmas_consultar.usuario");
		TransferenciaBIMFirmasConsultarOpSucOrigen = properties.getProperty("op.transferencia_bim_firmas_consultar.suc_origen");
		TransferenciaBIMFirmasConsultarOpSucDestino = properties.getProperty("op.transferencia_bim_firmas_consultar.suc_destino");
		TransferenciaBIMFirmasConsultarOpModulo = properties.getProperty("op.transferencia_bim_firmas_consultar.modulo");
		
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
		if(!datosTransferenciaBIMProcesar.has("Trb_RFC") || datosTransferenciaBIMProcesar.get("Trb_RFC") == null)
			datosTransferenciaBIMProcesar.addProperty("Trb_RFC", "");
		if(!datosTransferenciaBIMProcesar.has("Trb_IVA") || datosTransferenciaBIMProcesar.get("Trb_IVA") == null)
			datosTransferenciaBIMProcesar.addProperty("Trb_IVA", 0.000);
		datosTransferenciaBIMProcesar.addProperty("Trb_DireIP", "");
		datosTransferenciaBIMProcesar.addProperty("Trb_TipTra", TransferenciaBIMProcesarOpTrbTipTra);
		datosTransferenciaBIMProcesar.addProperty("Trb_ValFir", TransferenciaBIMProcesarOpTrbValFir);
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
	
	public JsonObject transferenciaBIMProcesarResultSets(JsonObject datosTransferenciaBIMProcesar) {
		logger.info("COMMONS: Comenzando transferenciaBIMProcesarResultSets metodo... ");
		if(!datosTransferenciaBIMProcesar.has("Trb_RFC") || datosTransferenciaBIMProcesar.get("Trb_RFC") == null)
			datosTransferenciaBIMProcesar.addProperty("Trb_RFC", "");
		if(!datosTransferenciaBIMProcesar.has("Trb_IVA") || datosTransferenciaBIMProcesar.get("Trb_IVA") == null)
			datosTransferenciaBIMProcesar.addProperty("Trb_IVA", 0.000);
		datosTransferenciaBIMProcesar.addProperty("Trb_DireIP", "");
		datosTransferenciaBIMProcesar.addProperty("Trb_ValFir", TransferenciaBIMProcesarOpTrbValFir);
		datosTransferenciaBIMProcesar.addProperty("Transaccio", TransferenciaBIMProcesarOpTransaccio);
		datosTransferenciaBIMProcesar.addProperty("Usuario", TransferenciaBIMProcesarOpUsuario);
		datosTransferenciaBIMProcesar.addProperty("SucOrigen", TransferenciaBIMProcesarOpSucOrigen);
		datosTransferenciaBIMProcesar.addProperty("SucDestino", TransferenciaBIMProcesarOpSucDestino);
		datosTransferenciaBIMProcesar.addProperty("Modulo", TransferenciaBIMProcesarOpModulo);
		JsonObject datosTransferenciaBIMProcesarOpResultadoObjeto = resultSetDAO.resultSet(datosTransferenciaBIMProcesar, "NBTRABANPRO", "transferenciasBIM", "transferenciaBIM");
		logger.info("datosTransferenciaBIMProcesarOpResultadoObjeto" + datosTransferenciaBIMProcesarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaBIMProcesarResultSets metodo... ");
		return datosTransferenciaBIMProcesarOpResultadoObjeto;
	}

	public JsonObject transferenciaBIMCreacion(JsonObject datosTransferenciaBIMCreacion) {
		logger.info("COMMONS: Comenzando transferenciaBIMCreacion metodo... ");
		if(!datosTransferenciaBIMCreacion.has("Trb_RFC") || datosTransferenciaBIMCreacion.get("Trb_RFC") == null)
			datosTransferenciaBIMCreacion.addProperty("Trb_RFC", "");
		if(!datosTransferenciaBIMCreacion.has("Trb_IVA") || datosTransferenciaBIMCreacion.get("Trb_IVA") == null)
			datosTransferenciaBIMCreacion.addProperty("Trb_IVA", 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_UsuAut", "");
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
	
	public JsonObject transferenciasBIMConsultar(JsonObject datosTransferenciaBIMConsultar) {
		logger.info("COMMONS: Comenzando transferenciasBIMConsultar metodo... ");
		datosTransferenciaBIMConsultar.addProperty("Trb_Client", "");
		if(!datosTransferenciaBIMConsultar.has("Trb_Status"))
			datosTransferenciaBIMConsultar.addProperty("Trb_Status", "");
		datosTransferenciaBIMConsultar.addProperty("Trb_TipTra", "");
		datosTransferenciaBIMConsultar.addProperty("NumTransac", "");
		datosTransferenciaBIMConsultar.addProperty("Trb_Consec", "");
		if(!datosTransferenciaBIMConsultar.has("Tip_Consul"))
			datosTransferenciaBIMConsultar.addProperty("Tip_Consul", TransferenciaBIMConsultarOpTipConsul);
		datosTransferenciaBIMConsultar.addProperty("Transaccio", TransferenciaBIMConsultarOpTransaccio);
		datosTransferenciaBIMConsultar.addProperty("Usuario", TransferenciaBIMConsultarOpUsuario);
		datosTransferenciaBIMConsultar.addProperty("SucOrigen", TransferenciaBIMConsultarOpSucOrigen);
		datosTransferenciaBIMConsultar.addProperty("SucDestino", TransferenciaBIMConsultarOpSucDestino);
		datosTransferenciaBIMConsultar.addProperty("Modulo", TransferenciaBIMConsultarOpModulo);
		JsonObject transferenciaBIMConsultarResultadoObjeto = Utilerias.performOperacion(TransferenciasBIMServicio, TransferenciaBIMConsultarOp, datosTransferenciaBIMConsultar);
		logger.info("transferenciaBIMConsultarResultadoObjeto" + transferenciaBIMConsultarResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaBIMCreacion metodo... ");
		return transferenciaBIMConsultarResultadoObjeto;
	}

	public JsonObject transferenciasBIMConsultarResultSets(JsonObject datosTransferenciaBIMConsultar) {
		logger.info("COMMONS: Comenzando transferenciasBIMConsultarResultSets metodo... ");
		datosTransferenciaBIMConsultar.addProperty("Trb_Client", "");
		if(!datosTransferenciaBIMConsultar.has("Trb_Status"))
			datosTransferenciaBIMConsultar.addProperty("Trb_Status", "");
		datosTransferenciaBIMConsultar.addProperty("Trb_TipTra", "");
		datosTransferenciaBIMConsultar.addProperty("NumTransac", "");
		datosTransferenciaBIMConsultar.addProperty("Trb_Consec", "");
		if(!datosTransferenciaBIMConsultar.has("Tip_Consul"))
			datosTransferenciaBIMConsultar.addProperty("Tip_Consul", TransferenciaBIMConsultarOpTipConsul);
		datosTransferenciaBIMConsultar.addProperty("Transaccio", TransferenciaBIMConsultarOpTransaccio);
		datosTransferenciaBIMConsultar.addProperty("Usuario", TransferenciaBIMConsultarOpUsuario);
		datosTransferenciaBIMConsultar.addProperty("SucOrigen", TransferenciaBIMConsultarOpSucOrigen);
		datosTransferenciaBIMConsultar.addProperty("SucDestino", TransferenciaBIMConsultarOpSucDestino);
		datosTransferenciaBIMConsultar.addProperty("Modulo", TransferenciaBIMConsultarOpModulo);		
		JsonObject transferenciaBIMConsultarResultadoObjeto = resultSetDAO.resultSet(datosTransferenciaBIMConsultar, "NBTRABANCON", "transferenciasBIM", "transferenciaBIM");
		logger.info("transferenciaBIMConsultarResultadoObjeto" + transferenciaBIMConsultarResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciasBIMConsultarResultSets metodo... ");
		return transferenciaBIMConsultarResultadoObjeto;
	}
	
	public JsonObject transferenciaBIMFirmasConsultar(JsonObject datosTransferenciaBIMFirmasConsultar) {
		logger.info("COMMONS: Comenzando transferenciaBIMFirmasConsultar metodo... ");
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Valida", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Usuari", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Client", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("NumTransac", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("Tip_Consul", TransferenciaBIMFirmasConsultarOpTipConsul);
		datosTransferenciaBIMFirmasConsultar.addProperty("Transaccio", TransferenciaBIMFirmasConsultarOpTransaccio);
		datosTransferenciaBIMFirmasConsultar.addProperty("Usuario", TransferenciaBIMFirmasConsultarOpUsuario);
		datosTransferenciaBIMFirmasConsultar.addProperty("SucOrigen", TransferenciaBIMFirmasConsultarOpSucOrigen);
		datosTransferenciaBIMFirmasConsultar.addProperty("SucDestino", TransferenciaBIMFirmasConsultarOpSucDestino);
		datosTransferenciaBIMFirmasConsultar.addProperty("Modulo", TransferenciaBIMFirmasConsultarOpModulo);
		JsonObject datosTransferenciaBIMFirmasConsultarResultadoObjeto = Utilerias.performOperacion(TransferenciasBIMServicio, TransferenciaBIMFirmasConsultarOp, datosTransferenciaBIMFirmasConsultar);
		logger.info("datosTransferenciaBIMFirmasConsultarResultadoObjeto" + datosTransferenciaBIMFirmasConsultarResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaBIMFirmasConsultar metodo... ");
		return datosTransferenciaBIMFirmasConsultarResultadoObjeto;
	}
	
	public JsonObject transferenciaBIMFirmasConsultarResultSets(JsonObject datosTransferenciaBIMFirmasConsultar) {
		logger.info("COMMONS: Comenzando transferenciaBIMFirmasConsultarResultSets metodo... ");
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Valida", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Usuari", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Client", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("NumTransac", "");
		datosTransferenciaBIMFirmasConsultar.addProperty("Tip_Consul", TransferenciaBIMFirmasConsultarOpTipConsul);
		datosTransferenciaBIMFirmasConsultar.addProperty("Transaccio", TransferenciaBIMFirmasConsultarOpTransaccio);
		datosTransferenciaBIMFirmasConsultar.addProperty("Usuario", TransferenciaBIMFirmasConsultarOpUsuario);
		datosTransferenciaBIMFirmasConsultar.addProperty("SucOrigen", TransferenciaBIMFirmasConsultarOpSucOrigen);
		datosTransferenciaBIMFirmasConsultar.addProperty("SucDestino", TransferenciaBIMFirmasConsultarOpSucDestino);
		datosTransferenciaBIMFirmasConsultar.addProperty("Modulo", TransferenciaBIMFirmasConsultarOpModulo);
		JsonObject datosTransferenciaBIMFirmasConsultarResultadoObjeto = resultSetDAO.resultSet(datosTransferenciaBIMFirmasConsultar, "NBFITRBRCON", "transferenciasBIMFirmas", "transferenciaBIMFirmas");
		logger.info("datosTransferenciaBIMFirmasConsultarResultadoObjeto" + datosTransferenciaBIMFirmasConsultarResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaBIMFirmasConsultarResultSets metodo... ");
		return datosTransferenciaBIMFirmasConsultarResultadoObjeto;
	}
}
