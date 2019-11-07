package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre saldos
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class SaldoServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SaldoServicio.class);

	protected static String SaldoServicio;
	private static String SaldosClienteConsultarOp;
	private static String SaldosClienteConsultarOpTransaccio;
	private static String SaldosClienteConsultarOpUsuario;
	private static String SaldosClienteConsultarOpSucOrigen;
	private static String SaldosClienteConsultarOpSucDestino;
	private static String SaldosClienteConsultarOpModulo;
	
	public SaldoServicio() {
		super();

		SaldoServicio = properties.getProperty("data_service.saldo_servicio");
		
		SaldosClienteConsultarOp = properties.getProperty("saldo_servicio.op.saldos_cliente_consultar");

		SaldosClienteConsultarOpModulo = properties.getProperty("op.saldos_cliente_consultar.modulo");
		SaldosClienteConsultarOpSucDestino = properties.getProperty("op.saldos_cliente_consultar.suc_destino");
		SaldosClienteConsultarOpSucOrigen = properties.getProperty("op.saldos_cliente_consultar.suc_origen");
		SaldosClienteConsultarOpTransaccio = properties.getProperty("op.saldos_cliente_consultar.transaccio");
		SaldosClienteConsultarOpUsuario = properties.getProperty("op.saldos_cliente_consultar.usuario");
	}

	/**
	 * Método de consulta de saldos
	 * ProcedureName: NBSALCLICON
	 * @param datosSaldosClienteConsultar
	 * <pre> 
	 * {
	 * 	Cue_Client: String,
	 *	Usu_Numero: String,
	 *	Bit_DireIP: String,
	 *	Cue_Numero?: String,
	 * 	NumTransac?: String,
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
	 *	cuenta: {
	 *		saldos: [
	 *			{
	 *				Sal_Cuenta: String,
	 *				Sal_CLABE:String,
	 *				Sal_Dispon: Double,
	 *				Mon_Descri: String,
	 *				Tip_Descri: String,
	 *				Sal_SBC2: Double,
	 *				Sal_SBC1: Double,
	 *				Sal_PI: Double,
	 *				Sal_Bloque: Double,
	 *				Sal_BloqLF: Double,
	 *				Sal_Pagare: Integer,
	 *				Sal_Mesa: Integer,
	 *				Sal_Fondos: Integer,
	 *				Sal_Cedes: Integer,
	 *				Cor_Alias: String,
	 *				Sal_Saldo: Double
	 *			}
	 *		]
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject saldosClienteConsultar(JsonObject datosSaldosClienteConsultar) {
		logger.info("COMMONS: Comenzando saldosClienteConsultar metodo... ");
		if(!datosSaldosClienteConsultar.has("Cue_Numero"))
			datosSaldosClienteConsultar.addProperty("Cue_Numero", "");
		if(!datosSaldosClienteConsultar.has("NumTransac"))
			datosSaldosClienteConsultar.addProperty("NumTransac", "");
		datosSaldosClienteConsultar.addProperty("Transaccio", SaldosClienteConsultarOpTransaccio);
		datosSaldosClienteConsultar.addProperty("Usuario", SaldosClienteConsultarOpUsuario);
		datosSaldosClienteConsultar.addProperty("SucOrigen", SaldosClienteConsultarOpSucOrigen);
		datosSaldosClienteConsultar.addProperty("SucDestino", SaldosClienteConsultarOpSucDestino);
		datosSaldosClienteConsultar.addProperty("Modulo", SaldosClienteConsultarOpModulo);
		JsonObject saldosClienteConsultarResultadoObjecto = Utilerias.performOperacion(SaldoServicio, SaldosClienteConsultarOp, datosSaldosClienteConsultar);
		logger.info("COMMONS: Finalizando saldosClienteConsultar metodo... ");
		return saldosClienteConsultarResultadoObjecto;
	}//Cierre del método
	
}
