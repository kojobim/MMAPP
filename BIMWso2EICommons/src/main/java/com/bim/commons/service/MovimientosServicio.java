package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

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
	}
}
