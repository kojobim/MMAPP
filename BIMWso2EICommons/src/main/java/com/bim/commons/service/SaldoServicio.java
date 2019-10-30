package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class SaldoServicio extends BaseService {

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

	public JsonObject saldosClienteConsultar(JsonObject datosSaldosClienteConsultar) {
		datosSaldosClienteConsultar.addProperty("Cue_Numero", "");
		datosSaldosClienteConsultar.addProperty("NumTransac", "");
		datosSaldosClienteConsultar.addProperty("Transaccio", SaldosClienteConsultarOpTransaccio);
		datosSaldosClienteConsultar.addProperty("Usuario", SaldosClienteConsultarOpUsuario);
		datosSaldosClienteConsultar.addProperty("SucOrigen", SaldosClienteConsultarOpSucOrigen);
		datosSaldosClienteConsultar.addProperty("SucDestino", SaldosClienteConsultarOpSucDestino);
		datosSaldosClienteConsultar.addProperty("Modulo", SaldosClienteConsultarOpModulo);
		JsonObject saldosClienteConsultarResultadoObjecto = Utilerias.performOperacion(SaldoServicio, SaldosClienteConsultarOp, datosSaldosClienteConsultar);
		return saldosClienteConsultarResultadoObjecto;
	}
	
}
