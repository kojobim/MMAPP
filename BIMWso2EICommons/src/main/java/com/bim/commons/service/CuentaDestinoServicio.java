package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre cuenta destino
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class CuentaDestinoServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(CuentaDestinoServicio.class);
 
    private static String CuentaDestinoServicio;
    
	private static String CuentaDestinoSPEIActivacionOp;
    private static String CuentaDestinoSPEIConsultarOp;
    private static String CatalogoInstitucionesConsultarOp;
    private static String CuentaDestinoBIMActualizacionOp;
    private static String CuentaDestinoSPEIActualizacionOp;
    private static String CuentasEspecialesConsultarOp;
    private static String CuentaDestinoBIMCreacionOp;
    private static String CuentaDestinoProcesarOp;
    private static String CuentaDestinoBIMConsultarOp;
    private static String CuentaDestinoBIMConsultarL1Op;

	private static String CuentaDestinoSPEIActivacionOpTransaccio;
	private static String CuentaDestinoSPEIActivacionOpUsuario;
	private static String CuentaDestinoSPEIActivacionOpSucOrigen;
	private static String CuentaDestinoSPEIActivacionOpSucDestino;
    private static String CuentaDestinoSPEIActivacionOpModulo;
    private static String CuentaDestinoSPEIConsultarOpTipConsul;
    private static String CuentaDestinoSPEIConsultarOpTransaccio;
    private static String CuentaDestinoSPEIConsultarOpUsuario;
    private static String CuentaDestinoSPEIConsultarOpSucOrigen;
    private static String CuentaDestinoSPEIConsultarOpSucDestino;
    private static String CuentaDestinoSPEIConsultarOpModulo;
    private static String CatalogoInstitucionesConsultarOpTipConsul;
    private static String CatalogoInstitucionesConsultarOpTransaccio;
    private static String CatalogoInstitucionesConsultarOpUsuario;
    private static String CatalogoInstitucionesConsultarOpSucOrigen;
    private static String CatalogoInstitucionesConsultarOpSucDestino;
    private static String CatalogoInstitucionesConsultarOpModulo;
    private static String CuentaDestinoBIMActualizacionOpTipActual;
    private static String CuentaDestinoBIMActualizacionOpTransaccio;
    private static String CuentaDestinoBIMActualizacionOpUsuario;
    private static String CuentaDestinoBIMActualizacionOpSucOrigen;
    private static String CuentaDestinoBIMActualizacionOpSucDestino;
    private static String CuentaDestinoBIMActualizacionOpModulo;
    private static String CuentaDestinoSPEIActualizacionOpTipActual;
    private static String CuentaDestinoSPEIActualizacionOpTransaccio;
    private static String CuentaDestinoSPEIActualizacionOpUsuario;
    private static String CuentaDestinoSPEIActualizacionOpSucOrigen;
    private static String CuentaDestinoSPEIActualizacionOpSucDestino;
    private static String CuentaDestinoSPEIActualizacionOpModulo;    
    private static String CuentaDestinoBIMCreacionOpTransaccio;
    private static String CuentaDestinoBIMCreacionOpUsuario;
    private static String CuentaDestinoBIMCreacionOpSucOrigen;
    private static String CuentaDestinoBIMCreacionOpSucDestino;
    private static String CuentaDestinoBIMCreacionOpModulo;
    private static String CuentaDestinoBIMConsultarOpTransaccio;
    private static String CuentaDestinoBIMConsultarOpUsuario;
    private static String CuentaDestinoBIMConsultarOpSucOrigen;
    private static String CuentaDestinoBIMConsultarOpSucDestino;
    private static String CuentaDestinoBIMConsultarOpModulo;
    private static String CuentaDestinoProcesarOpTransaccio;
    private static String CuentaDestinoProcesarOpUsuario;
    private static String CuentaDestinoProcesarOpSucOrigen;
    private static String CuentaDestinoProcesarOpSucDestino;
    private static String CuentaDestinoProcesarOpModulo;
    private static String CuentasEspecialesConsultarOpTipConsul;
    private static String CuentasEspecialesConsultarOpTransaccio;
    private static String CuentasEspecialesConsultarOpUsuario;
    private static String CuentasEspecialesConsultarOpSucOrigen;
    private static String CuentasEspecialesConsultarOpSucDestino;
    private static String CuentasEspecialesConsultarOpModulo;
	public CuentaDestinoServicio() {
		super();

        CuentaDestinoServicio = properties.getProperty("data_service.cuenta_destino_servicio");

        CuentaDestinoSPEIActivacionOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_spei_activacion");
        CuentaDestinoSPEIConsultarOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_spei_consultar");
        CatalogoInstitucionesConsultarOp = properties.getProperty("cuenta_destino_servicio.op.catalogo_instituciones_consultar");
        CuentaDestinoBIMActualizacionOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_bim_actualizacion");
        CuentaDestinoSPEIActualizacionOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_spei_actualizacion");
        CuentasEspecialesConsultarOp = properties.getProperty("cuenta_destino_servicio.op.cuentas_especiales_consultar");
        CuentaDestinoBIMCreacionOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_bim_creacion");
        CuentaDestinoProcesarOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_procesar");
        CuentaDestinoBIMConsultarOp = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_bim_consultar");
        CuentaDestinoBIMConsultarL1Op = properties.getProperty("cuenta_destino_servicio.op.cuenta_destino_bim_consultar_l1");
        		
        CuentaDestinoSPEIActivacionOpTransaccio = properties.getProperty("op.cuenta_destino_spei_activacion.transaccio");
		CuentaDestinoSPEIActivacionOpUsuario = properties.getProperty("op.cuenta_destino_spei_activacion.usuario");
		CuentaDestinoSPEIActivacionOpSucOrigen = properties.getProperty("op.cuenta_destino_spei_activacion.suc_origen");
		CuentaDestinoSPEIActivacionOpSucDestino = properties.getProperty("op.cuenta_destino_spei_activacion.suc_destino");
        CuentaDestinoSPEIActivacionOpModulo = properties.getProperty("op.cuenta_destino_spei_activacion.modulo");
        
        CuentaDestinoSPEIConsultarOpTipConsul = properties.getProperty("op.cuenta_destino_spei_consultar.tip_consul");
        CuentaDestinoSPEIConsultarOpTransaccio = properties.getProperty("op.cuenta_destino_spei_consultar.transaccio");
        CuentaDestinoSPEIConsultarOpUsuario = properties.getProperty("op.cuenta_destino_spei_consultar.usuario");
        CuentaDestinoSPEIConsultarOpSucOrigen = properties.getProperty("op.cuenta_destino_spei_consultar.suc_origen");
        CuentaDestinoSPEIConsultarOpSucDestino = properties.getProperty("op.cuenta_destino_spei_consultar.suc_destino");
        CuentaDestinoSPEIConsultarOpModulo = properties.getProperty("op.cuenta_destino_spei_consultar.modulo");
    
        CatalogoInstitucionesConsultarOpTipConsul = properties.getProperty("op.catalogo_instituciones_consultar.tip_consul");
        CatalogoInstitucionesConsultarOpTransaccio = properties.getProperty("op.catalogo_instituciones_consultar.transaccio");
        CatalogoInstitucionesConsultarOpUsuario = properties.getProperty("op.catalogo_instituciones_consultar.usuario");
        CatalogoInstitucionesConsultarOpSucOrigen = properties.getProperty("op.catalogo_instituciones_consultar.suc_origen");
        CatalogoInstitucionesConsultarOpSucDestino = properties.getProperty("op.catalogo_instituciones_consultar.suc_destino");
        CatalogoInstitucionesConsultarOpModulo = properties.getProperty("op.catalogo_instituciones_consultar.modulo");


        CuentaDestinoBIMActualizacionOpTipActual = properties.getProperty("op.cuenta_destino_bim_actualizacion.tip_actual");
        CuentaDestinoBIMActualizacionOpTransaccio = properties.getProperty("op.cuenta_destino_bim_actualizacion.transaccio");
        CuentaDestinoBIMActualizacionOpUsuario = properties.getProperty("op.cuenta_destino_bim_actualizacion.usuario");
        CuentaDestinoBIMActualizacionOpSucOrigen = properties.getProperty("op.cuenta_destino_bim_actualizacion.suc_origen");
        CuentaDestinoBIMActualizacionOpSucDestino = properties.getProperty("op.cuenta_destino_bim_actualizacion.suc_destino");
        CuentaDestinoBIMActualizacionOpModulo = properties.getProperty("op.cuenta_destino_bim_actualizacion.modulo");
        
        CuentaDestinoBIMCreacionOpTransaccio = properties.getProperty("op.cuenta_destino_bim_creacion.transaccio");
        CuentaDestinoBIMCreacionOpUsuario = properties.getProperty("op.cuenta_destino_bim_creacion.usuario");
        CuentaDestinoBIMCreacionOpSucOrigen = properties.getProperty("op.cuenta_destino_bim_creacion.suc_origen");
        CuentaDestinoBIMCreacionOpSucDestino = properties.getProperty("op.cuenta_destino_bim_creacion.suc_destino");
        CuentaDestinoBIMCreacionOpModulo = properties.getProperty("op.cuenta_destino_bim_creacion.modulo");
        
        CuentaDestinoBIMConsultarOpTransaccio = properties.getProperty("op.cuenta_destino_bim_consultar.transaccio");
        CuentaDestinoBIMConsultarOpUsuario = properties.getProperty("op.cuenta_destino_bim_consultar.usuario");
        CuentaDestinoBIMConsultarOpSucOrigen = properties.getProperty("op.cuenta_destino_bim_consultar.suc_origen");
        CuentaDestinoBIMConsultarOpSucDestino = properties.getProperty("op.cuenta_destino_bim_consultar.suc_destino");
        CuentaDestinoBIMConsultarOpModulo = properties.getProperty("op.cuenta_destino_bim_consultar.modulo");
        
        CuentaDestinoProcesarOpTransaccio = properties.getProperty("op.cuenta_destino_procesar.transaccio");
        CuentaDestinoProcesarOpUsuario = properties.getProperty("op.cuenta_destino_procesar.usuario");
        CuentaDestinoProcesarOpSucOrigen = properties.getProperty("op.cuenta_destino_procesar.suc_origen");
        CuentaDestinoProcesarOpSucDestino = properties.getProperty("op.cuenta_destino_procesar.suc_destino");
        CuentaDestinoProcesarOpModulo = properties.getProperty("op.cuenta_destino_procesar.modulo");

        CuentaDestinoSPEIActualizacionOpTipActual = properties.getProperty("op.cuenta_destino_spei_actualizacion.tip_actual");
        CuentaDestinoSPEIActualizacionOpTransaccio = properties.getProperty("op.cuenta_destino_spei_actualizacion.transaccio");
        CuentaDestinoSPEIActualizacionOpUsuario = properties.getProperty("op.cuenta_destino_spei_actualizacion.usuario");
        CuentaDestinoSPEIActualizacionOpSucOrigen = properties.getProperty("op.cuenta_destino_spei_actualizacion.suc_origen");
        CuentaDestinoSPEIActualizacionOpSucDestino = properties.getProperty("op.cuenta_destino_spei_actualizacion.suc_destino");
        CuentaDestinoSPEIActualizacionOpModulo = properties.getProperty("op.cuenta_destino_spei_actualizacion.modulo");

        CuentasEspecialesConsultarOpTipConsul = properties.getProperty("op.cuentas_especiales_consultar.tip_consul");
        CuentasEspecialesConsultarOpTransaccio = properties.getProperty("op.cuentas_especiales_consultar.transaccio");
        CuentasEspecialesConsultarOpUsuario = properties.getProperty("op.cuentas_especiales_consultar.usuario");
        CuentasEspecialesConsultarOpSucOrigen = properties.getProperty("op.cuentas_especiales_consultar.suc_origen");
        CuentasEspecialesConsultarOpSucDestino = properties.getProperty("op.cuentas_especiales_consultar.suc_destino");
        CuentasEspecialesConsultarOpModulo = properties.getProperty("op.cuentas_especiales_consultar.modulo");
        
        CuentaDestinoBIMCreacionOpTransaccio = properties.getProperty("op.cuenta_destino_bim_creacion.transaccio");
        CuentaDestinoBIMCreacionOpUsuario = properties.getProperty("op.cuenta_destino_bim_creacion.usuario");
        CuentaDestinoBIMCreacionOpSucOrigen = properties.getProperty("op.cuenta_destino_bim_creacion.suc_origen");
        CuentaDestinoBIMCreacionOpSucDestino = properties.getProperty("op.cuenta_destino_bim_creacion.suc_destino");
        CuentaDestinoBIMCreacionOpModulo = properties.getProperty("op.cuenta_destino_bim_creacion.modulo");
    }
	
	/**
     * Método de activación de cuentas destino para transferencias SPEI
     * ProcedureName: NBCUDESPPRO
     * @param datosCuentaDestinoSPEI
     * <pre>
     * {
     *	Cds_UsuAdm: String,
     *	NumTransac: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *	cuentaDestino: {
     *		Err_Codigo: String, 
     *		Err_Mensaj: String
     *	}
     * }
     * </pre>
     */
	public JsonObject cuentaDestinoSPEIActivacion(JsonObject datosCuentaDestinoSPEI) {
		logger.info("COMMONS: Comenzando cuentaDestinoSPEIActivacion...");
		datosCuentaDestinoSPEI.addProperty("Transaccio", CuentaDestinoSPEIActivacionOpTransaccio);
        datosCuentaDestinoSPEI.addProperty("Usuario", CuentaDestinoSPEIActivacionOpUsuario);
        datosCuentaDestinoSPEI.addProperty("SucOrigen", CuentaDestinoSPEIActivacionOpSucOrigen);
        datosCuentaDestinoSPEI.addProperty("SucDestino", CuentaDestinoSPEIActivacionOpSucDestino);
        datosCuentaDestinoSPEI.addProperty("Modulo", CuentaDestinoSPEIActivacionOpModulo);
		JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoSPEIActivacionOp, datosCuentaDestinoSPEI);
		logger.info("COMMONS: Finalizando cuentaDestinoSPEIActivacion...");
		return result;
	}//Cierre del método
    

    /**
     * Método para consulta de cuentas destino para transferencias SPEI
     * ProcedureName: NBCUDESPCON
     * @param datosCuentaDestinoSPEI
     * <pre>
     * {
     *	Cds_Client?: String,
     *	Cds_UsuAdm: String,
     *	Cds_Usuari: String,
     *	Cds_Consec?: String,
     *	Cds_CLABE?: String,
     *	NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     *  {
     *	cuentasDestino: {
     *		cuentaDestino: [
     *			{
     *				Cds_UsuAdm: String, 
     *				Cds_Consec: String,
     *				Cds_CLABE: String, 
     *				Cds_Banco: String,
     *				Ins_Descri: String, 
     *				Cds_CliUsu: String,
     *				Cds_Status: String, 
     *				Cds_Alias: String,
     * 				Cds_RFCBen: String,
     *				Cds_EmaBen: String,
     *				Cds_Inicia: String, 
     *				Cds_FecAlt: String,
     *				Cds_FecCan: String, 
     *				Cds_DesAdi: String,
     *				Cds_DesCue: String,
     *				Cds_FeAlFo: String
     *			}
     *		]
     *	}
     * }
     * </pre>
     */
	public JsonObject cuentaDestinoSPEIConsultar(JsonObject datosCuentaDestinoSPEI) {
        logger.info("COMMONS: Comenzando cuentaDestinoSPEIConsultar...");
        if(!datosCuentaDestinoSPEI.has("Cds_Client"))
            datosCuentaDestinoSPEI.addProperty("Cds_Client", "");
        if(!datosCuentaDestinoSPEI.has("Cds_Consec"))
            datosCuentaDestinoSPEI.addProperty("Cds_Consec", "");
        if(!datosCuentaDestinoSPEI.has("Cds_CLABE"))
            datosCuentaDestinoSPEI.addProperty("Cds_CLABE", "");
        datosCuentaDestinoSPEI.addProperty("Tip_Consul", CuentaDestinoSPEIConsultarOpTipConsul);
        if(!datosCuentaDestinoSPEI.has("NumTransac"))
            datosCuentaDestinoSPEI.addProperty("NumTransac", "");
		datosCuentaDestinoSPEI.addProperty("Transaccio", CuentaDestinoSPEIConsultarOpTransaccio);
        datosCuentaDestinoSPEI.addProperty("Usuario", CuentaDestinoSPEIConsultarOpUsuario);
        datosCuentaDestinoSPEI.addProperty("SucOrigen", CuentaDestinoSPEIConsultarOpSucOrigen);
        datosCuentaDestinoSPEI.addProperty("SucDestino", CuentaDestinoSPEIConsultarOpSucDestino);
        datosCuentaDestinoSPEI.addProperty("Modulo", CuentaDestinoSPEIConsultarOpModulo);
		JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoSPEIConsultarOp, datosCuentaDestinoSPEI);
		logger.info("COMMONS: Finalizando cuentaDestinoSPEIConsultar...");
		return result;
	}//Cierre del método

	
    /**
     * Método para consultar el catalogo de instituciones
     * ProcedureName: SPINSTITCON
     * @param datosCatalogoInstituciones
     * <pre>
     * {
     *	Ins_Clave?: String,
     *	Ins_Descri?: String,
     *	NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *	instituciones: {
     *		institucion: [
     *			{
     *				Ins_Clave: String, 
     *				Ins_Descri: String
     *			}
     *		]
     *	}
     * }
     * </pre>
     */
    public JsonObject catalogoInstitucionesConsultar(JsonObject datosCatalogoInstituciones) {
        logger.info("COMMONS: Comenzando catalogoInstitucionesConsultar... ");
        if(!datosCatalogoInstituciones.has("Ins_Clave"))
            datosCatalogoInstituciones.addProperty("Ins_Clave", "");
        if(!datosCatalogoInstituciones.has("Ins_Descri"))
            datosCatalogoInstituciones.addProperty("Ins_Descri", "");
        datosCatalogoInstituciones.addProperty("Tip_Consul", CatalogoInstitucionesConsultarOpTipConsul);
        if(!datosCatalogoInstituciones.has("NumTransac"))
            datosCatalogoInstituciones.addProperty("NumTransac", "");
        datosCatalogoInstituciones.addProperty("Transaccio", CatalogoInstitucionesConsultarOpTransaccio);
        datosCatalogoInstituciones.addProperty("Usuario", CatalogoInstitucionesConsultarOpUsuario);
        datosCatalogoInstituciones.addProperty("SucOrigen", CatalogoInstitucionesConsultarOpSucOrigen);
        datosCatalogoInstituciones.addProperty("SucDestino", CatalogoInstitucionesConsultarOpSucDestino);
        datosCatalogoInstituciones.addProperty("Modulo", CatalogoInstitucionesConsultarOpModulo);
        JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CatalogoInstitucionesConsultarOp, datosCatalogoInstituciones);
		logger.info("COMMONS: Finalizando catalogoInstitucionesConsultar...");
		return result;
    }//Cierre del método
    
    /**
     * Método para actualizar una cuenta destino BIM
     * ProcedureName: NBCUDEBRACT
     * @param datosCuentaDestinoBIM
     * <pre>
     * {
     *  Cdb_Consec?: String,
     *  Cdb_UsuAdm: String,
     *  Cdb_Cuenta: String,
     *  Cdb_Random: String,
     *  NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *  cuentaDestino: {
     *      Err_Codigo: String,
     *      Err_Mensaj: String
     *  }
     * }
     * </pre>
     */
    public JsonObject cuentaDestinoBIMActualizacion(JsonObject datosCuentaDestinoBIM) {
        logger.info("COMMONS: Comenzando cuentaDestinoBIMActualizacion...");
        if(!datosCuentaDestinoBIM.has("Cdb_Consec"))
            datosCuentaDestinoBIM.addProperty("Cdb_Consec", "");
        if(!datosCuentaDestinoBIM.has("NumTransac"))
            datosCuentaDestinoBIM.addProperty("NumTransac", "");
        datosCuentaDestinoBIM.addProperty("Tip_Actual", CuentaDestinoBIMActualizacionOpTipActual);
        datosCuentaDestinoBIM.addProperty("Transaccio", CuentaDestinoBIMActualizacionOpTransaccio);
        datosCuentaDestinoBIM.addProperty("Usuario", CuentaDestinoBIMActualizacionOpUsuario);
        datosCuentaDestinoBIM.addProperty("SucOrigen", CuentaDestinoBIMActualizacionOpSucOrigen);
        datosCuentaDestinoBIM.addProperty("SucDestino", CuentaDestinoBIMActualizacionOpSucDestino);
        datosCuentaDestinoBIM.addProperty("Modulo", CuentaDestinoBIMActualizacionOpModulo);
        logger.info("CuentaDestinoServicio >>> " + CuentaDestinoServicio);
        logger.info("CuentaDestinoBIMActualizacionOp >>> " + CuentaDestinoBIMActualizacionOp);
        logger.info("datosCuentaDestinoBIM >>> " + datosCuentaDestinoBIM);
        JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoBIMActualizacionOp, datosCuentaDestinoBIM);
        logger.info("COMMONS: Finalizando cuentaDestinoBIMActualizacion...");
        return result;
    }//Cierre del método
    
    /**
     * Método para actualizar una cuenta destino SPEI
     * ProcedureName: NBCUDESPACT
     * @param datosCuentaDestinoSPEI
     * <pre>
     * {
     * 	Cds_UsuAdm: String,
     * 	Cds_Consec?: String,
     * 	Cds_CLABE: String,
     * 	Cds_Randoms: String,
     *  Tip_Actual: String,
     *  NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *  cuentaDestino: {
     *      Err_Codigo: String,
     *      Err_Mensaj: String
     *  }
     * }
     * </pre>
     */
    public JsonObject cuentaDestinoSPEIActualizacion(JsonObject datosCuentaDestinoSPEI) {
    	logger.info("COMMONS: Comenzando cuentaDestinoSPEIActualizacion...");
    	if(!datosCuentaDestinoSPEI.has("Cds_Consec"))
    		datosCuentaDestinoSPEI.addProperty("Cds_Consec", "");
    	if(!datosCuentaDestinoSPEI.has("NumTransac"))
    		datosCuentaDestinoSPEI.addProperty("NumTransac", "");
    	datosCuentaDestinoSPEI.addProperty("Tip_Actual", CuentaDestinoSPEIActualizacionOpTipActual);
    	datosCuentaDestinoSPEI.addProperty("Transaccio", CuentaDestinoSPEIActualizacionOpTransaccio);
    	datosCuentaDestinoSPEI.addProperty("Usuario", CuentaDestinoSPEIActualizacionOpUsuario);
    	datosCuentaDestinoSPEI.addProperty("SucOrigen", CuentaDestinoSPEIActualizacionOpSucOrigen);
    	datosCuentaDestinoSPEI.addProperty("SucDestino", CuentaDestinoSPEIActualizacionOpSucDestino);
    	datosCuentaDestinoSPEI.addProperty("Modulo", CuentaDestinoSPEIActualizacionOpModulo);
    	JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoSPEIActualizacionOp, datosCuentaDestinoSPEI);
    	logger.info("COMMONS: Finalizando cuentaDestinoSPEIActualizacion...");
    	return result;
    }//Cierre del método

    /**
     * Método para consulta de cuentas especiales
     * ProcedureName: NBCONESPCON
     * @param datosCuentasEspeciales
     * <pre>
     * {
     * 	Ces_Client?: String,
     * 	Ces_Cuenta: String,
     * 	Ces_Tarjet?: String,
     * 	Ces_Usuari?: String,
     *  Ser_Numero?: String,
     *  NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *  cuentasEspeciales: {
     *      Cue_Numero: String,
     *      Cue_Moneda: String,
     *      Cue_Status: String,
     *      Cue_Saldo: Double,
     *      Cue_Tipo: String,
     *      Cli_ComOrd: String,
     *      Cli_Status: String,
     *      Cli_RFC: String,
     *      Cue_MonDep: Double
     *  }
     * }
     * </pre>
     */
    public JsonObject cuentasEspecialesConsultar(JsonObject datosCuentasEspeciales) {
    	logger.info("COMMONS: Comenzando cuentasEspecialesConsultar...");
    	if(!datosCuentasEspeciales.has("Ces_Client"))
            datosCuentasEspeciales.addProperty("Ces_Client", "");
        if(!datosCuentasEspeciales.has("Ces_Tarjet"))
            datosCuentasEspeciales.addProperty("Ces_Tarjet", "");
        if(!datosCuentasEspeciales.has("Ces_Usuari"))
            datosCuentasEspeciales.addProperty("Ces_Usuari", "");
        if(!datosCuentasEspeciales.has("Ser_Numero"))
    		datosCuentasEspeciales.addProperty("Ser_Numero", "");
    	if(!datosCuentasEspeciales.has("NumTransac"))
    		datosCuentasEspeciales.addProperty("NumTransac", "");
    	datosCuentasEspeciales.addProperty("Tip_Consul", CuentasEspecialesConsultarOpTipConsul);
    	datosCuentasEspeciales.addProperty("Transaccio", CuentasEspecialesConsultarOpTransaccio);
    	datosCuentasEspeciales.addProperty("Usuario", CuentasEspecialesConsultarOpUsuario);
    	datosCuentasEspeciales.addProperty("SucOrigen", CuentasEspecialesConsultarOpSucOrigen);
    	datosCuentasEspeciales.addProperty("SucDestino", CuentasEspecialesConsultarOpSucDestino);
    	datosCuentasEspeciales.addProperty("Modulo", CuentasEspecialesConsultarOpModulo);
    	JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentasEspecialesConsultarOp, datosCuentasEspeciales);
    	logger.info("COMMONS: Finalizando cuentasEspecialesConsultar...");
    	return result;
    }//Cierre del método

    /**
     * Método para creación de cuentas destino BIM
     * ProcedureName: NBCUDEBRALT
     * @param datosCuentaDestinoBIM
     * <pre>
     * {
     * 	Cdb_UsuAdm: String,
     * 	Cdb_Cuenta: String,
     * 	Cdb_CliUsu: String,
     * 	Cdb_Alias: String,
     *  Cdb_RFCBen: String,
     *  Cdb_EmaBen: String,
     *  NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *  cuentaDestinoBIM: {
     *      Err_Codigo: String,
     *      Err_Mensaj: String
     *  }
     * }
     * </pre>
     */
    public JsonObject cuentaDestinoBIMCreacion(JsonObject datosCuentaDestinoBIM) {
    	logger.info("COMMONS: Comenzando cuentaDestinoBIMCreacion...");
    	if(!datosCuentaDestinoBIM.has("Cdb_UsuAdm"))
            datosCuentaDestinoBIM.addProperty("Cdb_UsuAdm", "");
        if(!datosCuentaDestinoBIM.has("Cdb_Cuenta"))
            datosCuentaDestinoBIM.addProperty("Cdb_Cuenta", "");
        if(!datosCuentaDestinoBIM.has("Cdb_CliUsu"))
            datosCuentaDestinoBIM.addProperty("Cdb_CliUsu", "");
        if(!datosCuentaDestinoBIM.has("Cdb_Alias"))
    		datosCuentaDestinoBIM.addProperty("Cdb_Alias", "");
    	if(!datosCuentaDestinoBIM.has("Cdb_RFCBen"))
    		datosCuentaDestinoBIM.addProperty("Cdb_RFCBen", "");
        if(!datosCuentaDestinoBIM.has("Cdb_EmaBen"))
            datosCuentaDestinoBIM.addProperty("Cdb_EmaBen", "");
        if(!datosCuentaDestinoBIM.has("Cdb_Alias"))
            datosCuentaDestinoBIM.addProperty("Cdb_Alias", "");
        if(!datosCuentaDestinoBIM.has("NumTransac"))
            datosCuentaDestinoBIM.addProperty("NumTransac", "");
        datosCuentaDestinoBIM.addProperty("Transaccio", CuentaDestinoBIMCreacionOpTransaccio);
    	datosCuentaDestinoBIM.addProperty("Usuario", CuentaDestinoBIMCreacionOpUsuario);
    	datosCuentaDestinoBIM.addProperty("SucOrigen", CuentaDestinoBIMCreacionOpSucOrigen);
    	datosCuentaDestinoBIM.addProperty("SucDestino", CuentaDestinoBIMCreacionOpSucDestino);
    	datosCuentaDestinoBIM.addProperty("Modulo", CuentaDestinoBIMCreacionOpModulo);
    	JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoBIMCreacionOp, datosCuentaDestinoBIM);
    	logger.info("COMMONS: Finalizando cuentaDestinoBIMCreacion...");
    	return result;
    }//Cierre del método
    
    /**
     * Método para procesar cuentas destino 
     * ProcedureName: NBCUEDESPRO
     * @param datosCuentaDestinoProcesar
     * <pre>
     * {
     * 	Cud_UsuAdm: String,
     * 	Cud_CLABE: String,
     * 	Cud_Banco: String,
     * 	Tip_Proces: String,
     *  NumTransac?: String,
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
    public JsonObject cuentaDestinoProcesar(JsonObject datosCuentaDestinoProcesar) {
    	logger.info("COMMONS: Comenzando cuentaDestinoBIMCreacion...");
    	if(!datosCuentaDestinoProcesar.has("Cud_UsuAdm"))
            datosCuentaDestinoProcesar.addProperty("Cud_UsuAdm", "");
        if(!datosCuentaDestinoProcesar.has("Cud_CLABE"))
            datosCuentaDestinoProcesar.addProperty("Cud_CLABE", "");
        if(!datosCuentaDestinoProcesar.has("Cud_Banco"))
            datosCuentaDestinoProcesar.addProperty("Cud_Banco", "");
        if(!datosCuentaDestinoProcesar.has("Tip_Proces"))
    		datosCuentaDestinoProcesar.addProperty("Tip_Proces", "");
        if(!datosCuentaDestinoProcesar.has("NumTransac"))
            datosCuentaDestinoProcesar.addProperty("NumTransac", "");
        datosCuentaDestinoProcesar.addProperty("Transaccio", CuentaDestinoProcesarOpTransaccio);
        datosCuentaDestinoProcesar.addProperty("Usuario", CuentaDestinoProcesarOpUsuario);
    	datosCuentaDestinoProcesar.addProperty("SucOrigen", CuentaDestinoProcesarOpSucOrigen);
    	datosCuentaDestinoProcesar.addProperty("SucDestino", CuentaDestinoProcesarOpSucDestino);
    	datosCuentaDestinoProcesar.addProperty("Modulo", CuentaDestinoProcesarOpModulo);
    	JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoProcesarOp, datosCuentaDestinoProcesar);
    	logger.info("COMMONS: Finalizando cuentaDestinoBIMCreacion...");
    	return result;
    }//Cierre del método

    /**
     * Método para consultar cuentas destino BIM
     * ProcedureName: NBCUDEBRCON
     * @param datosCuentaDestinoBIMConsultar
     * <pre>
     * {
     * 	Cdb_Client: String,
     * 	Cdb_UsuAdm: String,
     * 	Cdb_Usuari: String,
     * 	Cdb_Cuenta: String,
     * 	Cdb_Status: String,
     *  NumTransac?: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * 
     */
    public JsonObject cuentaDestinoBIMConsultar(JsonObject datosCuentaDestinoBIMConsultar) {
    	logger.info("COMMONS: Comenzando cuentaDestinoBIMConsultar metodo...");
    	if(!datosCuentaDestinoBIMConsultar.has("Cdb_Client"))
            datosCuentaDestinoBIMConsultar.addProperty("Cdb_Client", "");
        if(!datosCuentaDestinoBIMConsultar.has("Cdb_UsuAdm"))
            datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", "");
        if(!datosCuentaDestinoBIMConsultar.has("Cdb_Usuari"))
            datosCuentaDestinoBIMConsultar.addProperty("Cdb_Usuari", "");
        if(!datosCuentaDestinoBIMConsultar.has("Cdb_Cuenta"))
    		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Cuenta", "");
        if(!datosCuentaDestinoBIMConsultar.has("Cdb_Status"))
            datosCuentaDestinoBIMConsultar.addProperty("Cdb_Status", "");
        if(!datosCuentaDestinoBIMConsultar.has("Tip_Consul"))
            datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", "");
        if(!datosCuentaDestinoBIMConsultar.has("NumTransac"))
            datosCuentaDestinoBIMConsultar.addProperty("NumTransac", "");
        if(!datosCuentaDestinoBIMConsultar.has("Transaccio"))
        	datosCuentaDestinoBIMConsultar.addProperty("Transaccio", CuentaDestinoBIMConsultarOpTransaccio);
        datosCuentaDestinoBIMConsultar.addProperty("Usuario", CuentaDestinoBIMConsultarOpUsuario);
    	datosCuentaDestinoBIMConsultar.addProperty("SucOrigen", CuentaDestinoBIMConsultarOpSucOrigen);
    	datosCuentaDestinoBIMConsultar.addProperty("SucDestino", CuentaDestinoBIMConsultarOpSucDestino);
    	datosCuentaDestinoBIMConsultar.addProperty("Modulo", CuentaDestinoBIMConsultarOpModulo);

    	logger.info("- datosCuentaDestinoBIMConsultar: " + datosCuentaDestinoBIMConsultar);
    	
    	if(datosCuentaDestinoBIMConsultar.get("Tip_Consul").getAsString().equals("L1"))
    		CuentaDestinoBIMConsultarOp = CuentaDestinoBIMConsultarL1Op;
    		
    	JsonObject result = Utilerias.performOperacion(CuentaDestinoServicio, CuentaDestinoBIMConsultarOp, datosCuentaDestinoBIMConsultar);
    	logger.info("COMMONS: Finalizando cuentaDestinoBIMConsultar metodo...");
    	return result;
    }//Cierre del método
}