package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class TasaServicio extends BaseService {
	
	private static String TasaServicio;
	private static String TasaClienteConsultarOp;
	private static String TasaClienteConsultarOpInvMoneda;
	private static String TasaClienteConsultarOpTasa;
	private static String TasaClienteConsultarOpTransaccio;
	private static String TasaClienteConsultarOpUsuari;
	private static String TasaClienteConsultarOpSucOrigen;
	private static String TasaClienteConsultarOpSucDestino;
	private static String TasaClienteConsultarOpModulo;
	private static String TasaMonedaConsultarOp;
	private static String TasaMonedaConsultarOpMonNumero;
	private static String TasaMonedaConsultarOpTransaccio;
	private static String TasaMonedaConsultarOpUsuari;
	private static String TasaMonedaConsultarOpSucOrigen;
	private static String TasaMonedaConsultarOpSucDestino;
	private static String TasaMonedaConsultarOpModulo;
	private static String TasaGATConsultaCalcularOp;
	private static String TasaGATConsultaCalcualrOpInvGAT;
	private static String TasaGATConsultaCalcularOpMonComisi;
	private static String TasaGATConsultaCalcularOpTransaccio;
	private static String TasaGATConsultaCalcularOpUsuari;
	private static String TasaGATConsultaCalcularOpSucOrigen;
	private static String TasaGATConsultaCalcularOpSucDestino;
	private static String TasaGATConsultaCalcularOpModulo;
	private static String TasaGATRealConsultaCalcularOp;
	private static String TasaGATReaConsultaCalcualrOpInvGATRea;
	private static String TasaGATReaConsultaCalcularOpTransaccio;
	private static String TasaGATReaConsultaCalcularOpUsuari;
	private static String TasaGATReaConsultaCalcularOpSucOrigen;
	private static String TasaGATReaConsultaCalcularOpSucDestino;
	private static String TasaGATReaConsultaCalcularOpModulo;
	
	
	
	public TasaServicio() {
		super();

		TasaServicio= properties.getProperty("data_service.tasa_servicio");
		
		TasaClienteConsultarOp = properties.getProperty("tasa_servicio.op.tasa_cliente_consultar");
		TasaMonedaConsultarOp = properties.getProperty("tasa_servicio.op.tasa_moneda_consultar");
		TasaGATConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_consulta_calcular");
		TasaGATRealConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_rea_consulta_calcular");
		
		TasaClienteConsultarOpTasa  = properties.getProperty("op.tasa_cliente_consultar.tasa");
		TasaClienteConsultarOpTransaccio  = properties.getProperty("op.tasa_cliente_consultar.transaccio");
		TasaClienteConsultarOpUsuari = properties.getProperty("op.tasa_cliente_consultar.usuario");
		TasaClienteConsultarOpSucOrigen = properties.getProperty("op.tasa_cliente_consultar.suc_origen");
		TasaClienteConsultarOpSucDestino = properties.getProperty("op.tasa_cliente_consultar.suc_destino");
		TasaClienteConsultarOpModulo = properties.getProperty("op.tasa_cliente_consultar.modulo");		
		TasaClienteConsultarOpInvMoneda = properties.getProperty("op.tasa_cliente_consultar.inv_moneda");
		
		TasaMonedaConsultarOpTransaccio  = properties.getProperty("op.tasa_moneda_consultar.transaccio");
		TasaMonedaConsultarOpUsuari = properties.getProperty("op.tasa_moneda_consultar.usuario");
		TasaMonedaConsultarOpSucOrigen = properties.getProperty("op.tasa_moneda_consultar.suc_origen");
		TasaMonedaConsultarOpSucDestino = properties.getProperty("op.tasa_moneda_consultar.suc_destino");
		TasaMonedaConsultarOpModulo = properties.getProperty("op.tasa_moneda_consultar.modulo");		
		TasaMonedaConsultarOpMonNumero = properties.getProperty("op.tasa_moneda_consultar.mon_numero");
		
		TasaGATConsultaCalcularOpMonComisi  = properties.getProperty("op.tasa_gat_consulta_calcular.mon_comisi");
		TasaGATConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_consulta_calcular.transaccio");
		TasaGATConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_consulta_calcular.usuario");
		TasaGATConsultaCalcularOpSucOrigen = properties.getProperty("op.tasa_gat_consulta_calcular.suc_origen");
		TasaGATConsultaCalcularOpSucDestino = properties.getProperty("op.tasa_gat_consulta_calcular.suc_destino");
		TasaGATConsultaCalcularOpModulo = properties.getProperty("op.tasa_gat_consulta_calcular.modulo");	
		TasaGATConsultaCalcualrOpInvGAT = properties.getProperty("op.tasa_gat_consulta_calcular.inv_gat");
		
		TasaGATReaConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_rea_consulta_calcular.transaccio");
		TasaGATReaConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_rea_consulta_calcular.usuario");
		TasaGATReaConsultaCalcularOpSucOrigen = properties.getProperty("op.tasa_gat_rea_consulta_calcular.suc_origen");
		TasaGATReaConsultaCalcularOpSucDestino = properties.getProperty("op.tasa_gat_rea_consulta_calcular.suc_destino");
		TasaGATReaConsultaCalcularOpModulo = properties.getProperty("op.tasa_gat_rea_consulta_calcular.modulo");	
		TasaGATReaConsultaCalcualrOpInvGATRea = properties.getProperty("op.tasa_gat_rea_consulta_calcular.inv_gat_rea");
	}

	public JsonObject tasaClienteConsultar(JsonObject datosTasaCliente) {
		datosTasaCliente.addProperty("Inv_Moneda", TasaClienteConsultarOpInvMoneda);
		datosTasaCliente.addProperty("Inv_FecVen", "");
		datosTasaCliente.addProperty("Ine_Numero", "");
		datosTasaCliente.addProperty("Tasa", Integer.parseInt(TasaClienteConsultarOpTasa));		
		datosTasaCliente.addProperty("Inv_GruTas", "");
		datosTasaCliente.addProperty("Inv_NuPoGr", "");
		datosTasaCliente.addProperty("Transaccio", TasaClienteConsultarOpTransaccio);
		datosTasaCliente.addProperty("Usuario", TasaClienteConsultarOpUsuari);
		datosTasaCliente.addProperty("SucOrigen", TasaClienteConsultarOpSucOrigen);
		datosTasaCliente.addProperty("SucDestino", TasaClienteConsultarOpSucDestino);
		datosTasaCliente.addProperty("Modulo", TasaClienteConsultarOpModulo);

		JsonObject tasaClienteConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaClienteConsultarOp, datosTasaCliente);
		return tasaClienteConsultarOpResultadoObjeto;
	}
	
	public JsonObject tasaMonedaConsultar(JsonObject datosMoneda) {
		datosMoneda.addProperty("Mon_Descri", "");
		datosMoneda.addProperty("Mon_Fecha", "");
		datosMoneda.addProperty("Tip_Consul", "");
		datosMoneda.addProperty("Mon_Numero", TasaMonedaConsultarOpMonNumero);
		datosMoneda.addProperty("Transaccio", TasaMonedaConsultarOpTransaccio);
		datosMoneda.addProperty("Usuario", TasaMonedaConsultarOpUsuari);
		datosMoneda.addProperty("SucOrigen", TasaMonedaConsultarOpSucOrigen);
		datosMoneda.addProperty("SucDestino", TasaMonedaConsultarOpSucDestino);
		datosMoneda.addProperty("Modulo", TasaMonedaConsultarOpModulo);
		JsonObject tasaMonedaConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaMonedaConsultarOp, datosMoneda);
		return tasaMonedaConsultarOpResultadoObjeto;
	}
	
	public JsonObject tasaGATConsultaCalcular(JsonObject datosGAT) {
		datosGAT.addProperty("Cal_Opcion", "");
		datosGAT.addProperty("Inv_GAT", Integer.parseInt(TasaGATConsultaCalcualrOpInvGAT));
		datosGAT.addProperty("Mon_Comisi", Integer.parseInt(TasaGATConsultaCalcularOpMonComisi));
		datosGAT.addProperty("Transaccio", TasaGATConsultaCalcularOpTransaccio);
		datosGAT.addProperty("Usuario", TasaGATConsultaCalcularOpUsuari);
		datosGAT.addProperty("SucOrigen", TasaGATConsultaCalcularOpSucOrigen);
		datosGAT.addProperty("SucDestino", TasaGATConsultaCalcularOpSucDestino);
		datosGAT.addProperty("Modulo", TasaGATConsultaCalcularOpModulo);
		JsonObject tasaGATConsultaCalcularOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaGATConsultaCalcularOp, datosGAT);
		return tasaGATConsultaCalcularOpResultadoObjeto;
	}
	
	public JsonObject tasaGATRealConsultaCalcular(JsonObject datosGATRea) {
		datosGATRea.addProperty("Inv_GATRea", Integer.parseInt(TasaGATReaConsultaCalcualrOpInvGATRea));
		datosGATRea.addProperty("Transaccio", TasaGATReaConsultaCalcularOpTransaccio);
		datosGATRea.addProperty("Usuario", TasaGATReaConsultaCalcularOpUsuari);
		datosGATRea.addProperty("SucOrigen", TasaGATReaConsultaCalcularOpSucOrigen);
		datosGATRea.addProperty("SucDestino", TasaGATReaConsultaCalcularOpSucDestino);
		datosGATRea.addProperty("Modulo", TasaGATReaConsultaCalcularOpModulo);
		JsonObject tasaGATRealConsultaCalcularOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, TasaGATRealConsultaCalcularOp, datosGATRea);
		return tasaGATRealConsultaCalcularOpResultadoObjeto;
	}
}
