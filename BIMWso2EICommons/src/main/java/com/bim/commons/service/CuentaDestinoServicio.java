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
    
	private static String CuentaDestinoSPEIActualizacionOp;
	private static String CuentaDestinoSPEIActualizacionOpTransaccio;
	private static String CuentaDestinoSPEIActualizacionOpUsuario;
	private static String CuentaDestinoSPEIActualizacionOpSucOrigen;
	private static String CuentaDestinoSPEIActualizacionOpSucDestino;
    private static String CuentaDestinoSPEIActualizacionOpModulo;
    
    private static String CuentaDestinoSPEIConsultarOp;
    private static String CuentaDestinoSPEIConsultarOpTipConsul;
    private static String CuentaDestinoSPEIConsultarOpTransaccio;
    private static String CuentaDestinoSPEIConsultarOpUsuario;
    private static String CuentaDestinoSPEIConsultarOpSucOrigen;
    private static String CuentaDestinoSPEIConsultarOpSucDestino;
    private static String CuentaDestinoSPEIConsultarOpModulo;

    private static String CatalogoInstitucionesConsultarOp;
    private static String CatalogoInstitucionesConsultarOpTipConsul;
    private static String CatalogoInstitucionesConsultarOpTransaccio;
    private static String CatalogoInstitucionesConsultarOpUsuario;
    private static String CatalogoInstitucionesConsultarOpSucOrigen;
    private static String CatalogoInstitucionesConsultarOpSucDestino;
    private static String CatalogoInstitucionesConsultarOpModulo;

	public CuentaDestinoServicio() {
		super();

        CuentaDestinoServicio = properties.getProperty("data_service.cuenta_destino_servicio");
        
        CuentaDestinoSPEIActualizacionOp = properties.getProperty("op.cuenta_destino_spei_actualizacion");
        CuentaDestinoSPEIConsultarOp = properties.getProperty("op.cuenta_destino_spei_consultar");
        CatalogoInstitucionesConsultarOp = properties.getProperty("op.catalogo_instituciones_consultar");

        CuentaDestinoSPEIActualizacionOpTransaccio = properties.getProperty("op.cuenta_destino_spei_actualizacion.transaccio");
		CuentaDestinoSPEIActualizacionOpUsuario = properties.getProperty("op.cuenta_destino_spei_actualizacion.usuario");
		CuentaDestinoSPEIActualizacionOpSucOrigen = properties.getProperty("op.cuenta_destino_spei_actualizacion.suc_origen");
		CuentaDestinoSPEIActualizacionOpSucDestino = properties.getProperty("op.cuenta_destino_spei_actualizacion.suc_destino");
        CuentaDestinoSPEIActualizacionOpModulo = properties.getProperty("op.cuenta_destino_spei_actualizacion.modulo");
        
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
    }
	
	/**
     * Método de actualización de cuentas destino para transferencias SPEI
     * ProcedureName: NBCUDESPPRO
     * @param datosCuentaDestinoSPEI
     * <pre>
     * {
     *	Cds_UsuAdm: String,
     *	NumTransac: String,
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
     *	cuentaDestino: {
     *		Err_Codigo: String, 
     *		Err_Mensaj: String
     *	}
     * }
     * </pre>
     */
	public JsonObject cuentaDestinoSPEIActualizacion(JsonObject datosCuentaDestinoSPEI) {
		logger.info("COMMONS: Comenzando cuentaDestinoSPEIActualizacion...");
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
     * Método para consulta de cuentas destino para transferencias SPEI
     * ProcedureName: NBCUDESPCON
     * @param datoscuentaDestinoTransferenciasSPEI
     * <pre>
     * {
     *	Cds_Client?: String,
     *	Cds_UsuAdm: String,
     *	Cds_Usuari: String,
     *	Cds_Consec?: String,
     *	Cds_CLABE?: String,
     *	Tip_Consul: String,
     *	NumTransac?: String,
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
     *	Tip_Consul: String,
     *	NumTransac?: String,
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
}