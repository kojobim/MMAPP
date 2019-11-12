package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre el usuario
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class UsuarioServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

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
	private static String UsuarioPerfilRiesgoConsultarOp;
	private static String UsuarioPerfilRiesgoConsultarOpTipConsul;
	private static String UsuarioPerfilRiesgoConsultarOpTransaccio;
	private static String UsuarioPerfilRiesgoConsultarOpUsuari;
	private static String UsuarioPerfilRiesgoConsultarOpSucOrigen;
	private static String UsuarioPerfilRiesgoConsultarOpSucDestino;
	private static String UsuarioPerfilRiesgoConsultarOpModulo;
	
	public UsuarioServicio() {
		super();

		UsuarioServicio = properties.getProperty("data_service.usuario_servicio");

		UsuarioConsultarOp = properties.getProperty("usuario_servicio.op.usuario_consultar");
		UsuarioActualizacionOp = properties.getProperty("usuario_servicio.op.usuario_actualizacion");
		UsuarioPerfilRiesgoConsultarOp = properties.getProperty("usuario_servicio.op.usuario_perfil_riesgo_consultar");
		
		UsuarioConsultarOpTipConsul = properties.getProperty("op.usuario_consultar.tip_consul");
		UsuarioConsultarOpTransaccio = properties.getProperty("op.usuario_consultar.transaccio");
		UsuarioConsultarOpUsuario = properties.getProperty("op.usuario_consultar.usuario");
		UsuarioConsultarOpSucOrigen = properties.getProperty("op.usuario_consultar.suc_origen");
		UsuarioConsultarOpSucDestino = properties.getProperty("op.usuario_consultar.suc_destino");
		UsuarioConsultarOpModulo = properties.getProperty("op.usuario_consultar.modulo");

		UsuarioActualizacionOpTipActual = properties.getProperty("op.usuario_actualizacion.tip_actual");
		UsuarioActualizacionOpTransaccio = properties.getProperty("op.usuario_actualizacion.transaccio");
		UsuarioActualizacionOpUsuario = properties.getProperty("op.usuario_actualizacion.usuario");
		UsuarioActualizacionOpSucOrigen = properties.getProperty("op.usuario_actualizacion.suc_origen");
		UsuarioActualizacionOpSucDestion = properties.getProperty("op.usuario_actualizacion.suc_destino");
		UsuarioActualizacionOpModulo = properties.getProperty("op.usuario_actualizacion.modulo");

		UsuarioPerfilRiesgoConsultarOpTipConsul = properties.getProperty("op.usuario_perfil_riesgo_consultar.tip_consul");
		UsuarioPerfilRiesgoConsultarOpTransaccio = properties.getProperty("op.usuario_perfil_riesgo_consultar.transaccio");
		UsuarioPerfilRiesgoConsultarOpUsuari = properties.getProperty("op.usuario_perfil_riesgo_consultar.usuario");
		UsuarioPerfilRiesgoConsultarOpSucOrigen = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_origen");
		UsuarioPerfilRiesgoConsultarOpSucDestino = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_destino");
		UsuarioPerfilRiesgoConsultarOpModulo = properties.getProperty("op.usuario_perfil_riesgo_consultar.modulo");
	}
	
	/**
	 * Método para consultar usuario
	 * ProcedureName: NBUSUARICON
	 * @param datosUsuarioConsultar
	 * <pre> 
	 * {
	 * 	Usu_Numero?: String,
	 * 	Usu_UsuAdm?: String,
	 * 	Usu_Clave: String,
	 * 	Usu_Passwo?: String,
	 * 	Usu_Client?: String,
	 * 	Usu_FolTok?: String,
	 * 	Usu_Status?: String,
	 * 	Usu_CuCaCo?: String,
	 * 	Usu_SucMod?: String,
	 * 	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre> 
	 * {
	 *	usuario: {
	 *		Usu_Numero: String,
	 *		Usu_UsuAdm: String,
	 *		Usu_Client: String,
	 *		Usu_CuCaCo: String,
	 *		Usu_Nombre: String,
	 *		Usu_Clave: String,
	 *		Usu_Passwo: String, 
	 *		Usu_Seguro: String,
	 *		Usu_Tipo: String, 
	 *		Usu_Origen: String,
	 *		Usu_TipSer: String, 
	 *		Usu_Status: String, 
	 *		Usu_FolNip: Integer,
	 *		Usu_FolTok: String, 
	 *		Usu_StaTok: String, 
	 *		Usu_Email: String, 
	 *		Usu_FeUlAc: String,
	 *		Usu_FeAcPa: String,
	 *		Usu_FecAlt: String,
	 *		Usu_FecCan: String,
	 *		Usu_MotCan: String, 
	 *		Usu_CoAcNe: Integer,
	 *		Usu_ToAcNe: Integer,
	 *		Usu_StaSes: String, 
	 *		Usu_CoDeNe: Integer,
	 *		Usu_CoPaNe: Integer,
	 *		Pau_Usuari: String,
	 *		Pau_OpMeHa: String,
	 *		Pau_ImaSeg: String,
	 *		Pau_FraSeg: String,
	 *		Pau_PanIni: String,
	 *		Pau_PrPrSe: String,
	 *		Pau_PrPrPe: String,
	 *		Pau_PrReSe: String,
	 *		Pau_SePrSe: String,
	 *		Pau_SePrPe: String,
	 *		Pau_SeReSe: String,
	 *		Pau_ArMeHa: String,
	 *		Pau_TipAcc: String,
	 *		Pau_ConCue: String,
	 *		Pau_AltCue: String,
	 *		Pau_CarCue: String,
	 *		Pau_Solici: String,
	 *		Pau_Autori: String,
	 *		Pau_NivFir: String,
	 *		Pau_UlPrSe: String,
	 *		Tok_Status: String,
	 *		Usu_EmaAdm: String,
	 *		Usu_DesSta: String
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject usuarioConsultar(JsonObject datosUsuarioConsultar) {
		logger.info("COMMONS: Comenzando usuarioConsultar metodo... ");
		if(!datosUsuarioConsultar.has("Usu_Passwo"))
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
		logger.info("COMMONS: Finalizando usuarioConsultar metodo... ");
		return usuarioConsultarOpResultadoObjeto;
	}//Cierre del método
	
	/**
	 * Método para actualizar el usuario
	 * ProcedureName: NBUSUARIACT
	 * @param datosUsuarioActualizar
	 * <pre> 
	 * {
	 * 	Usu_Numero?: String,
	 * 	Usu_Clave: String,
	 * 	Usu_Passwo?: String,
	 *	Usu_Status?: String,
	 *	Usu_Email?: String,
	 * 	Usu_UsuAdm?: String,
	 * 	Usu_FolTok?: String,
	 *	Usu_Client?: String,
	 *	Usu_SucMod?: String,
	 * 	Usu_Nombre?: String,
	 * 	NumTransac: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 * 	usuario: {
	 * 		Usu_CoAcNe: Integer
	 * 	}
	 * }
	 * </pre>
	 */
	public JsonObject usuarioActualizar(JsonObject datosUsuarioActualizar) {
		logger.info("COMMONS: Comenzando usuarioActualizar metodo... ");
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
		datosUsuarioActualizar.addProperty("Usu_FolNip", 0);
		if(!datosUsuarioActualizar.has("Usu_Nombre"))
			datosUsuarioActualizar.addProperty("Usu_Nombre", "");
		datosUsuarioActualizar.addProperty("Usuario", UsuarioActualizacionOpUsuario);
		datosUsuarioActualizar.addProperty("Tip_Actual", UsuarioActualizacionOpTipActual);
		datosUsuarioActualizar.addProperty("Transaccio", UsuarioActualizacionOpTransaccio);
		datosUsuarioActualizar.addProperty("SucOrigen", UsuarioActualizacionOpSucOrigen);
		datosUsuarioActualizar.addProperty("SucDestino", UsuarioActualizacionOpSucDestion);
		datosUsuarioActualizar.addProperty("Modulo", UsuarioActualizacionOpModulo);
		JsonObject usuarioActualizacionOpResultadoObjecto = Utilerias.performOperacion(UsuarioServicio, UsuarioActualizacionOp, datosUsuarioActualizar);
		logger.info("COMMONS: Finalizando usuarioActualizar metodo... ");
		return usuarioActualizacionOpResultadoObjecto;
	}//Cierre del método
	
	
	/**
	 * Método para consultar el perfil de riesgo del usuario
	 * ProcedureName: CLAPLCUECON
	 * @param datosPerfilRiesgo
	 * <pre>
	 * {
	 *	Apl_Client: String,
	 *	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *	perfilRiesgo: {
	 *		Apl_PerRie: Integer,
	 *		Apl_ConOpe: Integer,
	 *		Per_TitCli: String,
	 *		Par_OpExPe: Integer
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject usuarioPerfilRiesgoConsultar(JsonObject datosPerfilRiesgo) {
		logger.info("COMMONS: Comenzando usuarioPerfilRiesgoConsultar metodo... ");
		
		logger.info("- datosPerfilRiesgo " + datosPerfilRiesgo);
		
		datosPerfilRiesgo.addProperty("Apl_Cuesti", 0);		
        datosPerfilRiesgo.addProperty("Tip_Consul", UsuarioPerfilRiesgoConsultarOpTipConsul);
        if(!datosPerfilRiesgo.has("NumTransac"))
        	datosPerfilRiesgo.addProperty("NumTransac", "");
		datosPerfilRiesgo.addProperty("Transaccio", UsuarioPerfilRiesgoConsultarOpTransaccio);
		datosPerfilRiesgo.addProperty("Usuario", UsuarioPerfilRiesgoConsultarOpUsuari);
		datosPerfilRiesgo.addProperty("SucOrigen", UsuarioPerfilRiesgoConsultarOpSucOrigen);
		datosPerfilRiesgo.addProperty("SucDestino", UsuarioPerfilRiesgoConsultarOpSucDestino);
		datosPerfilRiesgo.addProperty("Modulo", UsuarioPerfilRiesgoConsultarOpModulo);

		JsonObject usuarioPerfilRiesgoOpResultadoObjecto = Utilerias.performOperacion(UsuarioServicio, UsuarioPerfilRiesgoConsultarOp, datosPerfilRiesgo);
		
		logger.info("COMMONS: Finalizando usuarioPerfilRiesgoConsultar metodo... ");
		return usuarioPerfilRiesgoOpResultadoObjecto;
	}//Cierre del método
}
