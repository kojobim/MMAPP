package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre movimientos
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class MovimientosServicio extends BaseService {

	private static String MovimientosServicio;
	private static String MovimientosListadoOp;
	private static String MovimientosListadoOpTipConsul;
	private static String MovimientosListadoOpTransaccio;
	private static String MovimientosListadoOpUsuario;
	private static String MovimientosListadoOpSucOrigen;
	private static String MovimientosListadoOpSucDestino;
	private static String MovimientosListadoOpModulo;
	
	public MovimientosServicio() {
		super();
		
		MovimientosServicio = properties.getProperty("data_service.movimientos_servicio");
		
		MovimientosListadoOp = properties.getProperty("movimientos_servicio.op.movimientos_listado");

		MovimientosListadoOpTipConsul = properties.getProperty("op.movimientos_listado.tip_consul");
		MovimientosListadoOpModulo = properties.getProperty("op.movimientos_listado.modulo");
		MovimientosListadoOpSucDestino = properties.getProperty("op.movimientos_listado.suc_destino");
		MovimientosListadoOpSucOrigen = properties.getProperty("op.movimientos_listado.suc_origen");
		MovimientosListadoOpTransaccio = properties.getProperty("op.movimientos_listado.transaccio");
		MovimientosListadoOpUsuario = properties.getProperty("op.movimientos_listado.usuario");
	}
	
	/**
	 * Método de listado de movimientos
	 * ProcedureName: NBMOVCHECON
	 * @param datosMovimientosListado
	 * <pre>
	 * {
	 *	Cue_Numero: String,
	 *	Fec_Inicia: String,
	 *	Fec_Final: String,
	 *	Mov_Natura?: String,
	 *	Mov_PalCla?: String,
	 *	Mov_MonIni: Integer,
	 *	Mov_MonFin: Integer,
	 *	Mov_Clasif?: String,
	 *	Tip_Consul: String,
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
	 *     cuenta: {
	 *         movimientos: [
	 *             {
	 *                 Mov_Cuenta: String,
	 *                 Mov_Numero: String,
	 *                 Mov_Consec: String,
	 *                 Mov_Natura: String,
	 *                 Fecha_Val: String,
	 *                 Mov_Descri: String,
	 *                 Mov_Refere: String,
	 *                 Mov_Cantid: Double,
	 *                 Usuario: String,
	 *                 Transaccio: String,
	 *                 Mov_Saldo: Double,
	 *                 Mov_Tipo: String,
	 *                 Mov_Clasif: String,
	 *                 Mov_DesCla: String
	 *             }
	 *         ]
	 *     }
	 * }
	 * </pre>
	 */
	public JsonObject movimientosListado(JsonObject datosMovimientosListado) {
		if(!datosMovimientosListado.has("Mov_Natura"))
			datosMovimientosListado.addProperty("Mov_Natura", "");
		if(!datosMovimientosListado.has("Mov_PalCla"))
			datosMovimientosListado.addProperty("Mov_PalCla", "");
		datosMovimientosListado.addProperty("Mov_MonIni", 0);
		datosMovimientosListado.addProperty("Mov_MonFin", 0);
		if(!datosMovimientosListado.has("Mov_Clasif"))
			datosMovimientosListado.addProperty("Mov_Clasif", "");
		datosMovimientosListado.addProperty("Tip_Consul", MovimientosListadoOpTipConsul);
		if(!datosMovimientosListado.has("NumTransac"))
			datosMovimientosListado.addProperty("NumTransac", "");
		datosMovimientosListado.addProperty("Transaccio", MovimientosListadoOpTransaccio);
		datosMovimientosListado.addProperty("Usuario", MovimientosListadoOpUsuario);
		datosMovimientosListado.addProperty("SucOrigen", MovimientosListadoOpSucOrigen);
		datosMovimientosListado.addProperty("SucDestino", MovimientosListadoOpSucDestino);
		datosMovimientosListado.addProperty("Modulo", MovimientosListadoOpModulo);
		JsonObject movimientosListadoResultadoObjeto = Utilerias
				.performOperacion(MovimientosServicio, MovimientosListadoOp, 
						datosMovimientosListado);
		return movimientosListadoResultadoObjeto;
	}//Cierre del método
}
