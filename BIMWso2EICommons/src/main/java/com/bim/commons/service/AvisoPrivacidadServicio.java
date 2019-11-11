package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre aviso de privacidad
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class AvisoPrivacidadServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(AvisoPrivacidadServicio.class);
 
    private static String AvisoPrivacidadServicio;
    
	private static String AvisoPrivacidadActualizacionOp;
	private static String AvisoPrivacidadConsultarOp;
	private static String AvisoPrivacidadActualizacionOpTransaccio;
	private static String AvisoPrivacidadActualizacionOpUsuario;
	private static String AvisoPrivacidadActualizacionOpSucOrigen;
	private static String AvisoPrivacidadActualizacionOpSucDestino;
    private static String AvisoPrivacidadActualizacionOpModulo;
    
    private static String AvisoPrivacidadConsultarOpConAviso;
    private static String AvisoPrivacidadConsultarOpTransaccio;
	private static String AvisoPrivacidadConsultarOpUsuario;
	private static String AvisoPrivacidadConsultarOpSucOrigen;
	private static String AvisoPrivacidadConsultarOpSucDestino;
    private static String AvisoPrivacidadConsultarOpModulo;

	public AvisoPrivacidadServicio() {
		super();

		AvisoPrivacidadServicio = properties.getProperty("data_service.aviso_privacidad_servicio");
        AvisoPrivacidadActualizacionOp = properties.getProperty("aviso_privacidad_servicio.op.aviso_privacidad_actualizacion");
        AvisoPrivacidadConsultarOp = properties.getProperty("aviso_privacidad_servicio.op.aviso_privacidad_consultar");

        AvisoPrivacidadActualizacionOpTransaccio = properties.getProperty("op.aviso_privacidad_actualizacion.transaccio");
		AvisoPrivacidadActualizacionOpUsuario = properties.getProperty("op.aviso_privacidad_actualizacion.usuario");
		AvisoPrivacidadActualizacionOpSucOrigen = properties.getProperty("op.aviso_privacidad_actualizacion.suc_origen");
		AvisoPrivacidadActualizacionOpSucDestino = properties.getProperty("op.aviso_privacidad_actualizacion.suc_destino");
        AvisoPrivacidadActualizacionOpModulo = properties.getProperty("op.aviso_privacidad_actualizacion.modulo");
        
        AvisoPrivacidadConsultarOpConAviso = properties.getProperty("op.aviso_privacidad_consultar.con_aviso");
        AvisoPrivacidadConsultarOpTransaccio = properties.getProperty("op.aviso_privacidad_consultar.transaccio");
		AvisoPrivacidadConsultarOpUsuario = properties.getProperty("op.aviso_privacidad_consultar.usuario");
		AvisoPrivacidadConsultarOpSucOrigen = properties.getProperty("op.aviso_privacidad_consultar.suc_origen");
		AvisoPrivacidadConsultarOpSucDestino = properties.getProperty("op.aviso_privacidad_consultar.suc_destino");
        AvisoPrivacidadConsultarOpModulo = properties.getProperty("op.aviso_privacidad_consultar.modulo");
    }

	
    /**
     * Método para actualizar el de aviso de privacidad
     * ProcedureName: NBENRMOVALT
     * @param datosAvisoPrivacidad
     * <pre>
     * {
     *	Usu_Numero: String,
     *	Usu_Client: String,
     *	Usu_AceAvi: String,
     *	Usu_FecAce: String,
     *	Usu_FecAct: String,
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
     *	avisoPrivacidad: {
     *		Err_Codigo: String, 
     *		Err_Mensaj: String
     *	}
     * }
     * </pre>
     */
	public JsonObject avisoPrivacidadActualizacion(JsonObject datosAvisoPrivacidad) {
		logger.info("COMMONS: Comenzando avisoPrivacidadActualizacion...");
		datosAvisoPrivacidad.addProperty("Transaccio", AvisoPrivacidadActualizacionOpTransaccio);
        datosAvisoPrivacidad.addProperty("Usuario", AvisoPrivacidadActualizacionOpUsuario);
        datosAvisoPrivacidad.addProperty("SucOrigen", AvisoPrivacidadActualizacionOpSucOrigen);
        datosAvisoPrivacidad.addProperty("SucDestino", AvisoPrivacidadActualizacionOpSucDestino);
        datosAvisoPrivacidad.addProperty("Modulo", AvisoPrivacidadActualizacionOpModulo);
		JsonObject result = Utilerias.performOperacion(AvisoPrivacidadServicio, AvisoPrivacidadActualizacionOp, datosAvisoPrivacidad);
		logger.info("COMMONS: Finalizando avisoPrivacidadActualizacion...");
		return result;
    }//Cierre del método
	
	/**
     * Método para consultar el de aviso de privacidad
     * ProcedureName: NBAVISOSCON
     * @param datosAvisoPrivacidad
     * <pre>
     * {
     *	Con_Aviso: String,
     *	NumTransac?: String,
     *	Transaccio: String,
     *	Usuario: String,
     *	FechaSis: String,
     *	SucOrigen: String,
     *	SucDestino: String,
     *	Modulo: String
     * }
     * @return
     * <pre>
     * {
     *	avisoPrivacidad: {
     *		Text_Aviso: String
     *	}
     * }
     * </pre>
     */
	public JsonObject avisoPrivacidadConsultar(JsonObject datosAvisoPrivacidad) {
		logger.info("COMMONS: Comenzando avisoPrivacidadConsultar...");
		datosAvisoPrivacidad.addProperty("Con_Aviso", AvisoPrivacidadConsultarOpConAviso);
		if(!datosAvisoPrivacidad.has("NumTransac"))
			datosAvisoPrivacidad.addProperty("NumTransac", "");
		datosAvisoPrivacidad.addProperty("Transaccio", AvisoPrivacidadConsultarOpTransaccio);
        datosAvisoPrivacidad.addProperty("Usuario", AvisoPrivacidadConsultarOpUsuario);
        datosAvisoPrivacidad.addProperty("SucOrigen", AvisoPrivacidadConsultarOpSucOrigen);
        datosAvisoPrivacidad.addProperty("SucDestino", AvisoPrivacidadConsultarOpSucDestino);
        datosAvisoPrivacidad.addProperty("Modulo", AvisoPrivacidadConsultarOpModulo);
		JsonObject result = Utilerias.performOperacion(AvisoPrivacidadServicio, AvisoPrivacidadConsultarOp, datosAvisoPrivacidad);
		logger.info("COMMONS: Finalizando avisoPrivacidadConsultar...");
		return result;
    }//Cierre del método
    
}