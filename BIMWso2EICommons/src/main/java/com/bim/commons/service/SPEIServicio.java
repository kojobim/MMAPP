package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.dao.TransferenciasNacionalesDAO;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre las transferencias nacionales
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class SPEIServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicio.class);
	
	private TransferenciasNacionalesDAO transferenciasNacionalesDAO;
	
	//servicio
	private static String SPEIServicio;
	
	//operaciones
	private static String HorariosSPEIConsultarOp;
	private static String TransferenciaSPEICreacionOp;
	private static String TransferenciaSPEIProcesarOp;
	private static String TransferenciaSPEIConsultarOp;

	//propiedades para consultar horarios
	private static String HorariosSPEIConsultarOpHorHorIni;
	private static String HorariosSPEIConsultarOpHorHorFin;
	private static String HorariosSPEIConsultarOpTipConsul;
	private static String HorariosSPEIConsultarOpTransaccio;
	private static String HorariosSPEIConsultarOpUsuario;
	private static String HorariosSPEIConsultarOpSucOrigen;
	private static String HorariosSPEIConsultarOpSucDestino;
	private static String HorariosSPEIConsultarOpModulo;
	
	//propiedades para creacion de transferencia nacional
	private static String TransferenciaSPEICreacionOpTransaccio;
	private static String TransferenciaSPEICreacionOpUsuario;
	private static String TransferenciaSPEICreacionOpSucOrigen;
	private static String TransferenciaSPEICreacionOpSucDestino;
	private static String TransferenciaSPEICreacionOpModulo;
	
	//propiedades para procesar tranferencia nacional
	private static String TransferenciaSPEIProcesarOpTrsCuBe;
	private static String TransferenciaSPEIProcesarOpTrsTipPag;
	private static String TransferenciaSPEIProcesarOpTrsTipTra;
	private static String TransferenciaSPEIProcesarOpTrsValFir;
	private static String TransferenciaSPEIProcesarOpTransaccio;
	private static String TransferenciaSPEIProcesarOpUsuario;
	private static String TransferenciaSPEIProcesarOpSucOrigen;
	private static String TransferenciaSPEIProcesarOpSucDestino;
	private static String TransferenciaSPEIProcesarOpModulo;
	
	//propiedades consultar transferencia nacional 
	private static String TransferenciaSPEIConsultarOpTrnStatus;
	private static String TransferenciaSPEIConsultarOpTipConsul;
	private static String TransferenciaSPEIConsultarOpTransaccio;
	private static String TransferenciaSPEIConsultarOpUsuario;
	private static String TransferenciaSPEIConsultarOpSucOrigen;
	private static String TransferenciaSPEIConsultarOpSucDestino;
	private static String TransferenciaSPEIConsultarOpModulo;
	
	static { // inicializando variables estaticas
		
		SPEIServicio = properties.getProperty("data_service.spei_servicio");
		
		// inicializacion de variables para consultar horarios
		HorariosSPEIConsultarOp = properties.getProperty("spei_servicio.op.horarios_spei_consultar");
		HorariosSPEIConsultarOpTipConsul = properties.getProperty("op.horarios_spei_consultar.tip_consul");
		HorariosSPEIConsultarOpTransaccio = properties.getProperty("op.horarios_spei_consultar.transaccio");
		HorariosSPEIConsultarOpUsuario = properties.getProperty("op.horarios_spei_consultar.usuario");
		HorariosSPEIConsultarOpSucOrigen = properties.getProperty("op.horarios_spei_consultar.suc_origen");
		HorariosSPEIConsultarOpSucDestino = properties.getProperty("op.horarios_spei_consultar.suc_destino");
		HorariosSPEIConsultarOpModulo = properties.getProperty("op.horarios_spei_consultar.modulo");
		HorariosSPEIConsultarOpHorHorIni = properties.getProperty("op.horarios_spei_consultar.hor_hor_ini");
		HorariosSPEIConsultarOpHorHorFin = properties.getProperty("op.horarios_spei_consultar.hor_hor_fin");
		
		// inicializacion de variables para crear transferencia nacional
		TransferenciaSPEICreacionOp = properties.getProperty("spei_servicio.op.transferencias_spei_creacion");
		TransferenciaSPEICreacionOpTransaccio = properties.getProperty("op.transferencias_spei_creacion.transaccio");
		TransferenciaSPEICreacionOpUsuario = properties.getProperty("op.transferencias_spei_creacion.usuario");
		TransferenciaSPEICreacionOpSucOrigen = properties.getProperty("op.transferencias_spei_creacion.suc_origen");
		TransferenciaSPEICreacionOpSucDestino = properties.getProperty("op.transferencias_spei_creacion.suc_destino");
		TransferenciaSPEICreacionOpModulo = properties.getProperty("op.transferencias_spei_creacion.modulo");
		
		// inicializacion de variables para procesar tranferencia nacional
		TransferenciaSPEIProcesarOp = properties.getProperty("spei_servicio.op.transferencias_spei_procesar");
		TransferenciaSPEIProcesarOpTrsCuBe = properties.getProperty("op.transferencias_spei_procesar.trs_ti_cu_be");
		TransferenciaSPEIProcesarOpTrsTipPag = properties.getProperty("op.transferencias_spei_procesar.trs_tip_pag");
		TransferenciaSPEIProcesarOpTrsTipTra = properties.getProperty("op.transferencias_spei_procesar.trs_tip_tra");
		TransferenciaSPEIProcesarOpTrsValFir = properties.getProperty("op.transferencias_spei_procesar.trs_val_fir");
		TransferenciaSPEIProcesarOpTransaccio = properties.getProperty("op.transferencias_spei_procesar.transaccio");
		TransferenciaSPEIProcesarOpUsuario = properties.getProperty("op.transferencias_spei_procesar.usuario");
		TransferenciaSPEIProcesarOpSucOrigen = properties.getProperty("op.transferencias_spei_procesar.suc_origen");
		TransferenciaSPEIProcesarOpSucDestino = properties.getProperty("op.transferencias_spei_procesar.suc_destino");
		TransferenciaSPEIProcesarOpModulo = properties.getProperty("op.transferencias_spei_procesar.modulo");
		
		// inicializacion de variables para consultar tranferencia nacional
		TransferenciaSPEIConsultarOp = properties.getProperty("spei_servicio.op.transferencias_spei_consultar");
		TransferenciaSPEIConsultarOpTrnStatus = properties.getProperty("op.transferencias_spei_consultar.trn_status");
		TransferenciaSPEIConsultarOpTipConsul = properties.getProperty("op.transferencias_spei_consultar.tip_consul");
		TransferenciaSPEIConsultarOpTransaccio = properties.getProperty("op.transferencias_spei_consultar.transaccio");
		TransferenciaSPEIConsultarOpUsuario = properties.getProperty("op.transferencias_spei_consultar.usuario");
		TransferenciaSPEIConsultarOpSucOrigen = properties.getProperty("op.transferencias_spei_consultar.suc_origen");
		TransferenciaSPEIConsultarOpSucDestino = properties.getProperty("op.transferencias_spei_consultar.suc_destino");
		TransferenciaSPEIConsultarOpModulo = properties.getProperty("op.transferencias_spei_consultar.modulo");
	
	}
	
	public SPEIServicio() {
		super();
		this.transferenciasNacionalesDAO = new TransferenciasNacionalesDAO();
	}

	/**
	 * Método para consultar los horarios de disponibilidad para transferencias SPEI
	 * ProcedureName: SPHORARICON
	 * @param datosHorariosSPEI
	 * <pre>
	 * {
	 *	Msj_Error?: String,
	 *	NumTransac?: String,
	 *	FechaSis: : String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *  horarioSPEI:
	 *  	{
	 *  	Hor_HorIni: String,
	 *  	Hor_HorFin: String
	 *  }
	 * }
	 * </pre>
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
	 * <pre>
	 * {
	 * 	transaccionSPEI: {
	 *		Err_Codigo: String,
	 *		Err_Mensaj: String,
	 *		Trs_Consec: String
	 *	}
	 * }
	 * </pre>
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
		if(!datosTransfarenciaSPEI.has("Trs_DurTra"))
			datosTransfarenciaSPEI.addProperty("Trs_DurTra", 0);
		if(!datosTransfarenciaSPEI.has("Trs_DiAnEm"))
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
	 * <pre>
	 * {
	 * 	Trs_UsuAdm: String,
	 * 	Trs_Usuari: String,
	 * 	Trs_UsuCli: String,
	 * 	Trs_Consec: String,
	 * 	Trs_CueOri: String,
	 * 	Trs_TiCuBe: String,
	 * 	Trs_CueBen: String,
	 * 	Trs_Monto: Double,
	 * 	Trs_IVA: Double,
	 * 	Trs_Comisi: Double,
	 * 	Trs_ConPag: String,
	 * 	Trs_RFCPro: String,
	 * 	Trs_IVAPro: Double,
	 * 	Trs_Banco: String,
	 * 	Trs_SegRef: String,
	 * 	Trs_CoCuDe: String,
	 * 	Trs_TCPDir: String,
	 * 	Trs_TipPag: String,
	 * 	Trs_CuBeAd: String,				
	 * 	Trs_TiCuBA: String,
	 * 	Trs_NoBeAd: String,
	 * 	Trs_ConPaU: String,
	 * 	Trs_DaBeAd: String,
	 * 	Trs_DireIP: String,
	 * 	Trs_TipTra: String,
	 * 	Trs_ValFir: String,
	 * 	Ban_Descri: String,
	 * 	NumTransac: String,
	 * 	FechaSis: String
	 * }
	 * </pre>
	 * @param datosTransferenciaSPEI
 	 * @return
	 * <pre>
	 * {
	 * 	transaccionSPEI: {
	 *		Err_Codigo: String,
	 *		Err_Mensaj: String,
	 *		NumTransac: String,
	 *		Numero: String,
	 *		Trs_FecAut: String,
	 *		Trs_FecCar: String,
	 *		Trs_FecApl: String,
	 *		Trs_Proces: String,
	 *		Trs_ClaRas: String,
	 *		Trs_Firma: String,
	 *		Trs_Progra: String,
	 *		Trs_TipSPE: String
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject transferenciaSPEIProcesar(JsonObject datosTransferenciaSPEI) {
		logger.info("COMMONS: Comenzando transferenciaSPEIProcesar metodo... ");
		if(!datosTransferenciaSPEI.has("Trs_OrdPag"))
			datosTransferenciaSPEI.addProperty("Trs_OrdPag", "");
		if(!datosTransferenciaSPEI.has("Trs_IVA"))
			datosTransferenciaSPEI.addProperty("Trs_IVA", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_Comisi", 0.0000);
		datosTransferenciaSPEI.addProperty("Trs_IVAPro", 0.0000);
		if(!datosTransferenciaSPEI.has("Trs_RFC"))
			datosTransferenciaSPEI.addProperty("Trs_RFC", "");
		if(!datosTransferenciaSPEI.has("Trs_RFCPro"))
			datosTransferenciaSPEI.addProperty("Trs_RFCPro", "");
		if(!datosTransferenciaSPEI.has("Trs_CuBeAd"))
			datosTransferenciaSPEI.addProperty("Trs_CuBeAd", "");
		if(!datosTransferenciaSPEI.has("Trs_TiCuBA"))
			datosTransferenciaSPEI.addProperty("Trs_TiCuBA", "");
		if(!datosTransferenciaSPEI.has("Trs_NoBeAd"))
			datosTransferenciaSPEI.addProperty("Trs_NoBeAd", "");
		if(!datosTransferenciaSPEI.has("Trs_ConPaU"))
			datosTransferenciaSPEI.addProperty("Trs_ConPaU", "");
		if(!datosTransferenciaSPEI.has("Trs_DaBeAd"))
			datosTransferenciaSPEI.addProperty("Trs_DaBeAd", "");
		if(!datosTransferenciaSPEI.has("Trs_DireIP"))
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
	 * <pre>
	 * {
	 *	Trn_UsuAdm: String,
	 *	Trn_Usuari: String,
	 *	Trn_Client: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 * 	transaccionesSPEI: {
	 * 		Trn_Client: String,
	 * 		Trn_Consec: String,
	 * 		Trn_CueOri: String,
	 * 		Trn_CueDes: String,
	 * 		Trn_Monto: Duble,
	 * 		Trn_Banco: String,
	 * 		Trn_Status: String,
	 * 		Trn_Descri: String,
	 * 		Trn_RFC: String,
	 * 		Trn_IVA: Duble,
	 * 		Trn_Tipo: String,
	 * 		Trn_UsuCap: String,
	 * 		Trn_TipTra: String,
	 * 		Trn_Frecue: String,
	 * 		Trn_FePrEn: String,
	 * 		Trn_TipDur: String,
	 * 		Trn_DurFec: String,
	 * 		Trn_DurTra: Integer,
	 * 		Trn_DiAnEm: Integer,
	 * 		Trn_FecCap: String,
	 * 		Cor_Alias: String,
	 * 		Cde_Alias: String,
	 * 		Cde_Consec: String,
	 * 		Cde_EmaBen: String,
	 * 		Cli_ComOrd: String,
	 * 		Trn_Transf: String,
	 * 		Trn_SigSec: Integer,
	 * 		Trn_Secuen: Integer,
	 * 		Trn_UsCaNo: String,
	 * 		Trn_UsAuNo: String,
	 * 		Trn_BanDes: String,
	 * 		Trn_MonTot: Double,
	 * 		Trn_EmaBen: String,
	 * 		Trn_DeCuOr: String,
	 * 		Trn_DeCuDe: String,
	 * 		Trn_DesAdi: String
	 * 	}
	 * }
	 * </pre>
	 */
	public JsonObject transferenciaSPEIConsultar(JsonObject datosTransferenciaSPEI) {
		logger.info("COMMONS: Comenzando transferenciaSPEIConsultar metodo... ");
		if(!datosTransferenciaSPEI.has("Trn_Client"))
			datosTransferenciaSPEI.addProperty("Trn_Client", "");
		if(!datosTransferenciaSPEI.has("Trn_Consec"))
			datosTransferenciaSPEI.addProperty("Trn_Consec", "");
		if(!datosTransferenciaSPEI.has("Trn_Transf"))
			datosTransferenciaSPEI.addProperty("Trn_Transf", "");
		if(!datosTransferenciaSPEI.has("NumTransac"))
			datosTransferenciaSPEI.addProperty("NumTransac", "");
		datosTransferenciaSPEI.addProperty("Trn_Status", TransferenciaSPEIConsultarOpTrnStatus);
		datosTransferenciaSPEI.addProperty("Tip_Consul", TransferenciaSPEIConsultarOpTipConsul);
		datosTransferenciaSPEI.addProperty("Transaccio", TransferenciaSPEIConsultarOpTransaccio);
		datosTransferenciaSPEI.addProperty("Usuario", TransferenciaSPEIConsultarOpUsuario);
		datosTransferenciaSPEI.addProperty("SucOrigen", TransferenciaSPEIConsultarOpSucOrigen);
		datosTransferenciaSPEI.addProperty("SucDestino", TransferenciaSPEIConsultarOpSucDestino);
		datosTransferenciaSPEI.addProperty("Modulo", TransferenciaSPEIConsultarOpModulo);
		JsonObject transferenciaSPEIConsultarOpResultadoObjeto = Utilerias.performOperacion(SPEIServicio, TransferenciaSPEIConsultarOp, datosTransferenciaSPEI);
		logger.info("COMMONS: Finalizando transferenciaSPEIConsultar metodo... ");
		return transferenciaSPEIConsultarOpResultadoObjeto;
	}

	public JsonObject transferenciaSPEIConsultarResultSets(JsonObject datosTransferenciaSPEI) {
		logger.info("COMMONS: Comenzando transferenciaSPEIConsultarResultSets metodo... ");
		if(!datosTransferenciaSPEI.has("Trn_Client"))
			datosTransferenciaSPEI.addProperty("Trn_Client", "");
		if(!datosTransferenciaSPEI.has("Trn_Consec"))
			datosTransferenciaSPEI.addProperty("Trn_Consec", "");
		if(!datosTransferenciaSPEI.has("Trn_Transf"))
			datosTransferenciaSPEI.addProperty("Trn_Transf", "");
		if(!datosTransferenciaSPEI.has("NumTransac"))
			datosTransferenciaSPEI.addProperty("NumTransac", "");
		datosTransferenciaSPEI.addProperty("Trn_Status", TransferenciaSPEIConsultarOpTrnStatus);
		datosTransferenciaSPEI.addProperty("Tip_Consul", TransferenciaSPEIConsultarOpTipConsul);
		datosTransferenciaSPEI.addProperty("Transaccio", TransferenciaSPEIConsultarOpTransaccio);
		datosTransferenciaSPEI.addProperty("Usuario", TransferenciaSPEIConsultarOpUsuario);
		datosTransferenciaSPEI.addProperty("SucOrigen", TransferenciaSPEIConsultarOpSucOrigen);
		datosTransferenciaSPEI.addProperty("SucDestino", TransferenciaSPEIConsultarOpSucDestino);
		datosTransferenciaSPEI.addProperty("Modulo", TransferenciaSPEIConsultarOpModulo);
		JsonObject transferenciaSPEIConsultarOpResultadoObjeto = transferenciasNacionalesDAO.transferenciasNacionalesConsultar(datosTransferenciaSPEI);
		logger.info("transferenciaSPEIConsultarOpResultadoObjeto" + transferenciaSPEIConsultarOpResultadoObjeto);
		logger.info("COMMONS: Finalizando transferenciaSPEIConsultarResultSets metodo... ");
		return transferenciaSPEIConsultarOpResultadoObjeto;
	}
	
}
