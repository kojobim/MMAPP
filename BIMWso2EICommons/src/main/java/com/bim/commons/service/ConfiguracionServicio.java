package com.bim.commons.service;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre la configuración
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class ConfiguracionServicio extends BaseService {

	private static String ConfiguracionServicio;
	private static String HorarioInversionOp;
	private static String HorarioInversionOpTipConsul;
	private static String HorarioInversionOpTipTransf;
	private static String HorarioInversionOpTransaccio;
	private static String HorarioInversionOpUsuario;
	private static String HorarioInversionOpSucOrigen;
	private static String HorarioInversionOpSucDestino;
	private static String HorarioInversionOpModulo;
	private static String ConfiguracionBancoDetalleOp;
	private static String ConfiguracionBancoDetalleOpTipConsul;
	private static String ConfiguracionBancoDetalleOpTransaccio;
	private static String ConfiguracionBancoDetalleOpUsuario;
	private static String ConfiguracionBancoDetalleOpSucOrigen;
	private static String ConfiguracionBancoDetalleOpSucDestino;
	private static String ConfiguracionBancoDetalleOpModulo;
	
	public ConfiguracionServicio() {
		super();
		
		ConfiguracionServicio = properties.getProperty("data_service.configuracion_servicio");
		
		HorarioInversionOp = properties.getProperty("configuracion_servicio.op.horario_inversion");
		HorarioInversionOpTipConsul = properties.getProperty("op.horario_inversion.tip_consul");
		HorarioInversionOpTipTransf = properties.getProperty("op.horario_inversion.tip_transf");
		HorarioInversionOpTransaccio = properties.getProperty("op.horario_inversion.transaccio");
		HorarioInversionOpUsuario = properties.getProperty("op.horario_inversion.usuario");
		HorarioInversionOpSucOrigen = properties.getProperty("op.horario_inversion.suc_origen");
		HorarioInversionOpSucDestino = properties.getProperty("op.horario_inversion.suc_destino");
		HorarioInversionOpModulo = properties.getProperty("op.horario_inversion.modulo");
		
		ConfiguracionBancoDetalleOp = properties.getProperty("configuracion_servicio.op.configuracion_banco_detalle");
		ConfiguracionBancoDetalleOpTipConsul= properties.getProperty("op.configuracion_banco_detalle.tip_consul");
		ConfiguracionBancoDetalleOpTransaccio = properties.getProperty("op.configuracion_banco_detalle.transaccio");
		ConfiguracionBancoDetalleOpUsuario = properties.getProperty("op.configuracion_banco_detalle.usuario");
		ConfiguracionBancoDetalleOpSucOrigen = properties.getProperty("op.configuracion_banco_detalle.suc_origen");
		ConfiguracionBancoDetalleOpSucDestino = properties.getProperty("op.configuracion_banco_detalle.suc_destino");
		ConfiguracionBancoDetalleOpModulo = properties.getProperty("op.configuracion_banco_detalle.modulo");
	}
	
	/**
	 * Método para consulta de horarios
	 * ProcedureName: ESHORARICON
	 * @param datosHorario
	 * <pre>
	 * {
	 *	Tip_Consul: String,
	 *	Tip_Transf: String,
	 *	Err_Codigo?: String, 
	 *	Msj_Error?: String, 
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
	 *	horariosInversion: {
	 *		EsHorariID: Integer,
	 *		Hor_Numero: Integer,
	 *		Hor_TipMod: String,
	 *		Hor_TipHor: String,
	 *		Hor_HorIni: Date,
	 *		Hor_HorFin: Date,
	 *		Hor_DiaHab: String
	 *	}
	 * }
	 * </pre>
	 */
	
	public JsonObject horariosConsultar(JsonObject datosHorario) {
		datosHorario.addProperty("Tip_Consul", HorarioInversionOpTipConsul);
		datosHorario.addProperty("Tip_Transf", HorarioInversionOpTipTransf);
		if(!datosHorario.has("Err_Codigo"))
			datosHorario.addProperty("Err_Codigo", "");
		if(!datosHorario.has("Msj_Error"))
			datosHorario.addProperty("Msj_Error", "");
		datosHorario.addProperty("Transaccio", HorarioInversionOpTransaccio);
		datosHorario.addProperty("Usuario", HorarioInversionOpUsuario);
		datosHorario.addProperty("SucOrigen", HorarioInversionOpSucOrigen);
		datosHorario.addProperty("SucDestino", HorarioInversionOpSucDestino);
		datosHorario.addProperty("Modulo", HorarioInversionOpModulo);
		JsonObject horarioInversionOpResultadoObjecto = Utilerias.performOperacion(ConfiguracionServicio, HorarioInversionOp, datosHorario);
		return horarioInversionOpResultadoObjecto;
	}//Cierre del método
	
	
	/**
	 * Método para consultar detalle de configuración de banco
	 * ProcedureName: NBPARAMECON
	 * @param datosConfiguracion
	 * <pre>
	 * {
	 *	Tip_Consul: String,
	 * 	NumTransac: String,
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
	 * 	configuracionesBanco: {
	 * 		configuracionBanco: [
	 * 			{
	 * 				Par_FecAct: Date,
	 * 				Par_FeOpEs: Date,
	 * 				Par_Acceso: String,
	 * 				Par_HoInSP: Date,
	 * 				Par_FeDiNa: Date,
	 * 				Par_HoCaPI: Date,
	 * 				Par_MiCuDe: Integer,
	 * 				Par_RECAMo: String,
	 * 				Par_RECAFi: String,
	 * 				Par_RECAFA: String
	 * 			}
	 * 		]
	 * 	}
	 * }
	 *</pre>
	 */
	public JsonObject configuracionBancoConsultarDetalle(JsonObject datosConfiguracion) {
		datosConfiguracion.addProperty("Tip_Consul", ConfiguracionBancoDetalleOpTipConsul);
		if(!datosConfiguracion.has("NumTransac"))
			datosConfiguracion.addProperty("NumTransac", "");
		datosConfiguracion.addProperty("Transaccio", ConfiguracionBancoDetalleOpTransaccio);
		datosConfiguracion.addProperty("Usuario", ConfiguracionBancoDetalleOpUsuario);
		datosConfiguracion.addProperty("SucOrigen", ConfiguracionBancoDetalleOpSucOrigen);
		datosConfiguracion.addProperty("SucDestino", ConfiguracionBancoDetalleOpSucDestino);
		datosConfiguracion.addProperty("Modulo", ConfiguracionBancoDetalleOpModulo);
		JsonObject configuracionBancoConsultarDetalleResultado = Utilerias.performOperacion(ConfiguracionServicio, ConfiguracionBancoDetalleOp, datosConfiguracion);
		return configuracionBancoConsultarDetalleResultado;
	}//Cierre del método
	
}
