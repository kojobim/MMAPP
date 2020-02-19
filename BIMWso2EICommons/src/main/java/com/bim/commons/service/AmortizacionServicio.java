package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre las tablas de amortizacion
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class AmortizacionServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(AmortizacionServicio.class);
 
    private static String AmortizacionServicio;
    
    private static String AmortizacionGenerarOp;

	private static String AmortizacionGenerarOpTransaccio;
	private static String AmortizacionGenerarOpUsuario;
	private static String AmortizacionGenerarOpSucOrigen;
	private static String AmortizacionGenerarOpSucDestino;
    private static String AmortizacionGenerarOpModulo;
    
	public AmortizacionServicio() {
		super();

		AmortizacionServicio = properties.getProperty("data_service.amortizacion_servicio");

		AmortizacionGenerarOp = properties.getProperty("amortizacion_servicio.op.amortizacion_generar");
    
		AmortizacionGenerarOpTransaccio = properties.getProperty("op.amortizacion_generar.transaccio");
		AmortizacionGenerarOpUsuario = properties.getProperty("op.amortizacion_generar.usuario");
		AmortizacionGenerarOpSucOrigen = properties.getProperty("op.amortizacion_generar.suc_origen");
		AmortizacionGenerarOpSucDestino = properties.getProperty("op.amortizacion_generar.suc_destino");
		AmortizacionGenerarOpModulo = properties.getProperty("op.amortizacion_generar.modulo");
    }
	
	/**
     * Método para generar la tabla de amortizaciones de nueva inversión CEDE
     * ProcedureName: CEAMOCALPRO
     * @param datosAmortizacionGenerar
     * <pre>
     * {
     *	Ced_Cantid: Double,
     *	Ced_Plazo: Integer,
     *	Ced_FecIni: String,
     *	Ced_FecVen: String,
     *	Ced_DiaPag: Integer,
     *	Ced_DiPaFe: String,
     *	Ced_TasBru: Double,
     *	Ced_TasNet: Double,
     *	Ced_Produc: String,
     *	FechaSis: String
     * }
     * </pre>
     * @return
     * <pre>
     * {
     *	amortizaciones: {
     *		amortizacion: [
     *			{
     *				Numero: Intener,
     *				Amo_Numero: String,
     *				Amo_FecIni: String,
     *				Amo_FecVen: String,
     *				Amo_Cantid: Double,
     *				Amo_IntBru: Double,
     *				Amo_IntNet: Double,
     *				Amo_ISR: Double,
     *				Amo_TasBru: Double,
     *				Amo_TasNet: Double,
     *				Amo_TasISR: Double
     *			}
     *		]
     *	}
     * }
     * </pre>
     */
	public JsonObject amortizacionGenerar(JsonObject datosAmortizacionGenerar) {
		logger.info("COMMONS: Comenzando amortizacionGenerar...");
		if(!datosAmortizacionGenerar.has("NumTransac"))
			datosAmortizacionGenerar.addProperty("NumTransac", "");
		datosAmortizacionGenerar.addProperty("Transaccio", AmortizacionGenerarOpTransaccio);
		datosAmortizacionGenerar.addProperty("Usuario", AmortizacionGenerarOpUsuario);
		datosAmortizacionGenerar.addProperty("SucOrigen", AmortizacionGenerarOpSucOrigen);
		datosAmortizacionGenerar.addProperty("SucDestino", AmortizacionGenerarOpSucDestino);
		datosAmortizacionGenerar.addProperty("Modulo", AmortizacionGenerarOpModulo);
		JsonObject amortizacionGenerarOpResultadoObjeto = Utilerias.performOperacion(AmortizacionServicio, AmortizacionGenerarOp, datosAmortizacionGenerar);
		logger.info("COMMONS: Finalizando amortizacionGenerar...");
		return amortizacionGenerarOpResultadoObjeto;
    }//Cierre del método
    
}