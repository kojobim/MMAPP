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
	private static String AvisoPrivacidadActualizacionOpTransaccio;
	private static String AvisoPrivacidadActualizacionOpUsuario;
	private static String AvisoPrivacidadActualizacionOpSucOrigen;
	private static String AvisoPrivacidadActualizacionOpSucDestino;
    private static String AvisoPrivacidadActualizacionOpModulo;

	public AvisoPrivacidadServicio() {
		super();

		AvisoPrivacidadServicio = properties.getProperty("data_service.aviso_privacidad_servicio");
        AvisoPrivacidadActualizacionOp = properties.getProperty("op.aviso_privacidad_actualizacion");

        AvisoPrivacidadActualizacionOpTransaccio = properties.getProperty("op.aviso_privacidad_actualizacion.transaccio");
		AvisoPrivacidadActualizacionOpUsuario = properties.getProperty("op.aviso_privacidad_actualizacion.usuario");
		AvisoPrivacidadActualizacionOpSucOrigen = properties.getProperty("op.aviso_privacidad_actualizacion.suc_origen");
		AvisoPrivacidadActualizacionOpSucDestino = properties.getProperty("op.aviso_privacidad_actualizacion.suc_destino");
        AvisoPrivacidadActualizacionOpModulo = properties.getProperty("op.aviso_privacidad_actualizacion.modulo");
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
    
}