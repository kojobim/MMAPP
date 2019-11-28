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

public class CambiarPasswordServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(CambiarPasswordServicio.class);

	// servicio
	private static String CambiarPasswordServicio;
	
	// operaciones
	private static String CuentasEspecialesConsultarOp;
	
	
	// propiedades
	private static String CuentasEspecialesConsultarOpTipConsul;
	private static String CuentasEspecialesConsultarOpTransaccio;
	private static String CuentasEspecialesConsultarOpUsuario;
	private static String CuentasEspecialesConsultarOpSucOrigen;
	private static String CuentasEspecialesConsultarOpSucDestino;
	private static String CuentasEspecialesConsultarOpModulo;
	
	static { // inicializando variables estaticas
		
		CambiarPasswordServicio = properties.getProperty("data_service.cambiar_password_servicio");
		
		// inicializacion de variables para cuentas especiales
		CuentasEspecialesConsultarOp = properties.getProperty("cambiar_password_servicio.op.cambiar_password_cuentas_especiales_consultar");
		CuentasEspecialesConsultarOpTipConsul = properties.getProperty("op.cambiar_password_cuentas_especiales_consultar.tip_consul");
		CuentasEspecialesConsultarOpTransaccio = properties.getProperty("op.cambiar_password_cuentas_especiales_consultar.transaccio");
		CuentasEspecialesConsultarOpUsuario = properties.getProperty("op.cambiar_password_cuentas_especiales_consultar.usuario");
		CuentasEspecialesConsultarOpSucOrigen = properties.getProperty("op.cambiar_password_cuentas_especiales_consultar.suc_origen");
		CuentasEspecialesConsultarOpSucDestino = properties.getProperty("op.cambiar_password_cuentas_especiales_consultar.suc_destino");
		CuentasEspecialesConsultarOpModulo = properties.getProperty("op.cambiar_password_cuentas_especiales_consultar.modulo");
		
	}
	
	public CambiarPasswordServicio() {
		super();
	}
	
	
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
    	"cuentasEspeciales": {
	        "Usu_Numero": String,
	        "Usu_UsuAdm": String,
	        "Usu_Client": String,
	        "Usu_CuCaCo": String,
	        "Usu_Nombre": String,
	        "Usu_Clave":  String,
	        "Usu_Passwo": String,
	        "Usu_Seguro": String,
	        "Usu_Tipo" :  String,
	        "Usu_Origen": String,
	        "Usu_TipSer": String,
	        "Usu_Status": String,
	        "Usu_FolNip": Integer,
	        "Usu_FolTok": String,
	        "Usu_StaTok": String,
	        "Usu_Email":  String,
	        "Usu_FeUlAc": String,
	        "Usu_FeAcPa": String,
	        "Usu_FecAlt": String,
	        "Usu_FecCan": String,
	        "Usu_MotCan": String,
	        "Usu_CoAcNe": Integer,
	        "Usu_StaSes": String,
	        "Usu_CoDeNe": Integer,
	        "Usu_CoPaNe": Integer
    	}
	 * }
     * </pre>
     */
	public JsonObject cuentasEspecialesConsulta( JsonObject datosCuentasEspeciales) {
		logger.info("COMMONS: Comenzando cuentasEspecialesConsulta...");
		// validacion de parametros opcionales
    	if(!datosCuentasEspeciales.has("Ces_Client")) {
            datosCuentasEspeciales.addProperty("Ces_Client", "");    		
    	}
        if(!datosCuentasEspeciales.has("Ces_Tarjet")) {
            datosCuentasEspeciales.addProperty("Ces_Tarjet", "");        	
        }
        if(!datosCuentasEspeciales.has("Ces_Usuari")) {
            datosCuentasEspeciales.addProperty("Ces_Usuari", "");
        }
        if(!datosCuentasEspeciales.has("Ser_Numero")) {
    		datosCuentasEspeciales.addProperty("Ser_Numero", "");        	
        }
    	if(!datosCuentasEspeciales.has("NumTransac")) {
    		datosCuentasEspeciales.addProperty("NumTransac", "");    		
    	}

    	
		// agregando propiedades hard-code
		datosCuentasEspeciales.addProperty("Tip_Consul", CuentasEspecialesConsultarOpTipConsul);
		datosCuentasEspeciales.addProperty("Transaccio",CuentasEspecialesConsultarOpTransaccio);
		datosCuentasEspeciales.addProperty("Usuario",CuentasEspecialesConsultarOpUsuario);
		datosCuentasEspeciales.addProperty("Suc_Origen",CuentasEspecialesConsultarOpSucOrigen);
		datosCuentasEspeciales.addProperty("Suc_Destino",CuentasEspecialesConsultarOpSucDestino);
		datosCuentasEspeciales.addProperty("Modulo",CuentasEspecialesConsultarOpModulo);
		
		JsonObject result = Utilerias.performOperacion(CambiarPasswordServicio,CuentasEspecialesConsultarOp,datosCuentasEspeciales);
		logger.info("COMMONS: Finalizando cuentasEspecialesConsulta...");
		return result;
		
	}
}
