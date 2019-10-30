package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

public class UsuarioServicio extends BaseService {

	private static String UsuarioServicio;
	private static String UsuarioConsultarOp;
	private static String UsuarioConsultarOpTipConsul;
	private static String UsuarioConsultarOpTransaccio;
	private static String UsuarioConsultarOpUsuario;
	private static String UsuarioConsultarOpSucOrigen;
	private static String UsuarioConsultarOpSucDestino;
	private static String UsuarioConsultarOpModulo;
	private static String UsuarioActualizacionOp;
	private static String UsuarioActualizacionOpTipActual;
	private static String UsuarioActualizacionOpTransaccio;
	private static String UsuarioActualizacionOpUsuario;
	private static String UsuarioActualizacionOpSucOrigen;
	private static String UsuarioActualizacionOpSucDestion;
	private static String UsuarioActualizacionOpModulo;
	
	public UsuarioServicio() {
		super();

		UsuarioServicio = properties.getProperty("data_service.usuario_servicio");

		UsuarioConsultarOp = properties.getProperty("usuario_servicio.op.usuario_consultar");
		UsuarioConsultarOpTipConsul = properties.getProperty("op.usuario_consultar.tip_consul");
		UsuarioConsultarOpTransaccio = properties.getProperty("op.usuario_consultar.transaccio");
		UsuarioConsultarOpUsuario = properties.getProperty("op.usuario_consultar.usuario");
		UsuarioConsultarOpSucOrigen = properties.getProperty("op.usuario_consultar.suc_origen");
		UsuarioConsultarOpSucDestino = properties.getProperty("op.usuario_consultar.suc_destino");
		UsuarioConsultarOpModulo = properties.getProperty("op.usuario_consultar.modulo");
		UsuarioActualizacionOp = properties.getProperty("usuario_servicio.op.usuario_actualizacion");
		UsuarioActualizacionOpTipActual = properties.getProperty("op.usuario_actualizacion.tip_actual");
		UsuarioActualizacionOpTransaccio = properties.getProperty("op.usuario_actualizacion.transaccio");
		UsuarioActualizacionOpUsuario = properties.getProperty("op.usuario_actualizacion.usuario");
		UsuarioActualizacionOpSucOrigen = properties.getProperty("op.usuario_actualizacion.suc_origen");
		UsuarioActualizacionOpSucDestion = properties.getProperty("op.usuario_actualizacion.suc_destino");
		UsuarioActualizacionOpModulo = properties.getProperty("op.usuario_actualizacion.modulo");
	}
	
	public JsonObject usuarioConsultar(JsonObject datosUsuarioConsultar) {
		datosUsuarioConsultar.addProperty("Usu_Passwo", "");
		datosUsuarioConsultar.addProperty("Tip_Consul", UsuarioConsultarOpTipConsul);
		datosUsuarioConsultar.addProperty("Transaccio", UsuarioConsultarOpTransaccio);
		datosUsuarioConsultar.addProperty("Usuario", UsuarioConsultarOpUsuario);
		datosUsuarioConsultar.addProperty("SucOrigen", UsuarioConsultarOpSucOrigen);
		datosUsuarioConsultar.addProperty("SucDestino", UsuarioConsultarOpSucDestino);
		datosUsuarioConsultar.addProperty("Modulo", UsuarioConsultarOpModulo);
		if(!datosUsuarioConsultar.has("Usu_Numero"))
			datosUsuarioConsultar.addProperty("Usu_Numero", "");
		if(!datosUsuarioConsultar.has("Usu_UsuAdm"))
			datosUsuarioConsultar.addProperty("Usu_UsuAdm", "");
		if(!datosUsuarioConsultar.has("Usu_Client"))
			datosUsuarioConsultar.addProperty("Usu_Client", "");
		datosUsuarioConsultar.addProperty("Usu_FolNip", 0);
		if(!datosUsuarioConsultar.has("Usu_FolTok"))
			datosUsuarioConsultar.addProperty("Usu_FolTok", "");
		if(!datosUsuarioConsultar.has("Usu_Status"))
			datosUsuarioConsultar.addProperty("Usu_Status", "");
		if(!datosUsuarioConsultar.has("Usu_CuCaCo"))
			datosUsuarioConsultar.addProperty("Usu_CuCaCo", "");
		if(!datosUsuarioConsultar.has("Usu_SucMod"))
			datosUsuarioConsultar.addProperty("Usu_SucMod", "");
		if(!datosUsuarioConsultar.has("NumTransac"))
			datosUsuarioConsultar.addProperty("NumTransac", "");	
		JsonObject usuarioConsultarOpResultadoObjeto = Utilerias.performOperacion(UsuarioServicio, UsuarioConsultarOp, datosUsuarioConsultar);
		return usuarioConsultarOpResultadoObjeto;
	}
	
	public JsonObject usuarioActualizar(JsonObject datosUsuarioActualizar) {
		if(!datosUsuarioActualizar.has("Usu_Numero"))
			datosUsuarioActualizar.addProperty("Usu_Numero", "");
		if(!datosUsuarioActualizar.has("Usu_Passwo"))
			datosUsuarioActualizar.addProperty("Usu_Passwo", "");
		if(!datosUsuarioActualizar.has("Usu_Status"))
			datosUsuarioActualizar.addProperty("Usu_Status", "");
		if(!datosUsuarioActualizar.has("Usu_Email"))
			datosUsuarioActualizar.addProperty("Usu_Email", "");
		if(!datosUsuarioActualizar.has("Usu_UsuAdm"))
			datosUsuarioActualizar.addProperty("Usu_UsuAdm", "");
		if(!datosUsuarioActualizar.has("Usu_FolTok"))
			datosUsuarioActualizar.addProperty("Usu_FolTok", "");
		if(!datosUsuarioActualizar.has("Usu_Client"))
			datosUsuarioActualizar.addProperty("Usu_Client", "");
		if(!datosUsuarioActualizar.has("Usu_SucMod"))
			datosUsuarioActualizar.addProperty("Usu_SucMod", "");
		datosUsuarioActualizar.addProperty("Usu_FolNip", "0");
		if(!datosUsuarioActualizar.has("Usu_Nombre"))
			datosUsuarioActualizar.addProperty("Usu_Nombre", "");
		datosUsuarioActualizar.addProperty("Usuario", UsuarioActualizacionOpUsuario);
		datosUsuarioActualizar.addProperty("Tip_Actual", UsuarioActualizacionOpTipActual);
		datosUsuarioActualizar.addProperty("Transaccio", UsuarioActualizacionOpTransaccio);
		datosUsuarioActualizar.addProperty("SucOrigen", UsuarioActualizacionOpSucOrigen);
		datosUsuarioActualizar.addProperty("SucDestino", UsuarioActualizacionOpSucDestion);
		datosUsuarioActualizar.addProperty("Modulo", UsuarioActualizacionOpModulo);
		JsonObject usuarioActualizacionOpResultadoObjecto = Utilerias.performOperacion(UsuarioServicio, UsuarioActualizacionOp, datosUsuarioActualizar);
		return usuarioActualizacionOpResultadoObjecto;
	}
	

}
