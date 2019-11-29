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

	//servicio
	private static String UsuarioServicio;
	
	//operaciones
	private static String UsuarioConsultarOp;
	private static String UsuarioActualizacionOp;
	private static String UsuarioPerfilRiesgoConsultarOp;
	private static String UsuarioParametrosActualizacionOp;
	
	//propiedades usuario consultar
	private static String UsuarioConsultarOpTipConsul;
	private static String UsuarioConsultarOpTransaccio;
	private static String UsuarioConsultarOpUsuario;
	private static String UsuarioConsultarOpSucOrigen;
	private static String UsuarioConsultarOpSucDestino;
	private static String UsuarioConsultarOpModulo;
	
	//propiedades usuario actualizacion
	private static String UsuarioActualizacionOpTransaccio;
	private static String UsuarioActualizacionOpUsuario;
	private static String UsuarioActualizacionOpSucOrigen;
	private static String UsuarioActualizacionOpSucDestion;
	private static String UsuarioActualizacionOpModulo;
	
	//propiedades usuario consultar perfil riesgo
	private static String UsuarioPerfilRiesgoConsultarOpTipConsul;
	private static String UsuarioPerfilRiesgoConsultarOpTransaccio;
	private static String UsuarioPerfilRiesgoConsultarOpUsuari;
	private static String UsuarioPerfilRiesgoConsultarOpSucOrigen;
	private static String UsuarioPerfilRiesgoConsultarOpSucDestino;
	private static String UsuarioPerfilRiesgoConsultarOpModulo;

	//propiedades usuario actualizacion parametros
	private static String UsuarioParametrosActualizacionOpTransaccio;
	private static String UsuarioParametrosActualizacionOpUsuario;
	private static String UsuarioParametrosActualizacionOpSucOrigen;
	private static String UsuarioParametrosActualizacionOpSucDestino;
	private static String UsuarioParametrosActualizacionOpModulo;
	
	static { // inicializando variables estaticas
		
		UsuarioServicio = properties.getProperty("data_service.usuario_servicio");
		
		// inicializacion de variables para consultar usuario
		UsuarioConsultarOp = properties.getProperty("usuario_servicio.op.usuario_consultar");
		UsuarioConsultarOpTipConsul = properties.getProperty("op.usuario_consultar.tip_consul");
		UsuarioConsultarOpTransaccio = properties.getProperty("op.usuario_consultar.transaccio");
		UsuarioConsultarOpUsuario = properties.getProperty("op.usuario_consultar.usuario");
		UsuarioConsultarOpSucOrigen = properties.getProperty("op.usuario_consultar.suc_origen");
		UsuarioConsultarOpSucDestino = properties.getProperty("op.usuario_consultar.suc_destino");
		UsuarioConsultarOpModulo = properties.getProperty("op.usuario_consultar.modulo");
		
		// inicializacion de variables para actualizacion de usuario
		UsuarioActualizacionOp = properties.getProperty("usuario_servicio.op.usuario_actualizacion");
		UsuarioActualizacionOpTransaccio = properties.getProperty("op.usuario_actualizacion.transaccio");
		UsuarioActualizacionOpUsuario = properties.getProperty("op.usuario_actualizacion.usuario");
		UsuarioActualizacionOpSucOrigen = properties.getProperty("op.usuario_actualizacion.suc_origen");
		UsuarioActualizacionOpSucDestion = properties.getProperty("op.usuario_actualizacion.suc_destino");
		UsuarioActualizacionOpModulo = properties.getProperty("op.usuario_actualizacion.modulo");

		// inicializacion de variables para consultar perfil riesgo de usuario
		UsuarioPerfilRiesgoConsultarOp = properties.getProperty("usuario_servicio.op.usuario_perfil_riesgo_consultar");
		UsuarioPerfilRiesgoConsultarOpTipConsul = properties.getProperty("op.usuario_perfil_riesgo_consultar.tip_consul");
		UsuarioPerfilRiesgoConsultarOpTransaccio = properties.getProperty("op.usuario_perfil_riesgo_consultar.transaccio");
		UsuarioPerfilRiesgoConsultarOpUsuari = properties.getProperty("op.usuario_perfil_riesgo_consultar.usuario");
		UsuarioPerfilRiesgoConsultarOpSucOrigen = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_origen");
		UsuarioPerfilRiesgoConsultarOpSucDestino = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_destino");
		UsuarioPerfilRiesgoConsultarOpModulo = properties.getProperty("op.usuario_perfil_riesgo_consultar.modulo");

		// inicializacion de variables para actualizar parametros del usuario
		UsuarioParametrosActualizacionOp = properties.getProperty("usuario_servicio.op.usuario_parametros_actualizacion");
		UsuarioParametrosActualizacionOpTransaccio = properties.getProperty("op.usuario_parametros_actualizacion.transaccio");
		UsuarioParametrosActualizacionOpUsuario = properties.getProperty("op.usuario_parametros_actualizacion.usuario");
		UsuarioParametrosActualizacionOpSucOrigen = properties.getProperty("op.usuario_parametros_actualizacion.suc_origen");
		UsuarioParametrosActualizacionOpSucDestino = properties.getProperty("op.usuario_parametros_actualizacion.suc_destino");
		UsuarioParametrosActualizacionOpModulo = properties.getProperty("op.usuario_parametros_actualizacion.modulo");
	}
	
	public UsuarioServicio() {
		super();
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
		if(!datosUsuarioActualizar.has("Usu_Clave"))
			datosUsuarioActualizar.addProperty("Usu_Clave", "");
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
	
	/**
	 * Método para actualizar parametros del usuario
	 * ProcedureName: NBPARUSUACT
	 * @param parametrosUsuario
	 * <pre>
	 * {
	 * 	Pau_Usuari: String,
	 * 	Pau_OpMeHa?: String,
	 * 	Pau_ImaSeg?: String,
	 * 	Pau_FraSeg?: String,
	 * 	Pau_PanIni?: String,
	 * 	Pau_PrPrSe?: String,
	 * 	Pau_PrPrPe?: String,
	 * 	Pau_PrReSe?: String,
	 * 	Pau_SePrSe?: String,
	 * 	Pau_SePrPe?: String,
	 * 	Pau_SeReSe?: String,
	 * 	Pau_TipAcc?: String,
	 * 	Pau_ConCue?: String,
	 * 	Pau_AltCue?: String,
	 * 	Pau_CarCue?: String,
	 * 	Pau_Solici?: String,
	 * 	Pau_Autori?: String,
	 * 	Pau_NivFir?: String,
	 * 	Pau_UlPrSe?: String,
	 * 	Pau_OrCuOr?: String,
	 * 	Pau_OrCuDe?: String,
	 * 	Pau_ArMeHa?: String
	 * 	Tip_Actual: String,
	 * 	NumTransac: String,
	 * 	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 */
	public JsonObject usuarioParametrosActualizacion(JsonObject parametrosUsuario) {
		logger.info("COMMONS: Comenzando usuarioParametrosActualizacion metodo... ");
		if(!parametrosUsuario.has("Pau_OpMeHa"))
			parametrosUsuario.addProperty("Pau_OpMeHa", "");
		if(!parametrosUsuario.has("Pau_ImaSeg"))
			parametrosUsuario.addProperty("Pau_ImaSeg", "");
		if(!parametrosUsuario.has("Pau_FraSeg"))
			parametrosUsuario.addProperty("Pau_FraSeg", "");
		if(!parametrosUsuario.has("Pau_PanIni"))
			parametrosUsuario.addProperty("Pau_PanIni", "");
		if(!parametrosUsuario.has("Pau_PrPrSe"))
			parametrosUsuario.addProperty("Pau_PrPrSe", "");
		if(!parametrosUsuario.has("Pau_PrPrPe"))
			parametrosUsuario.addProperty("Pau_PrPrPe", "");
		if(!parametrosUsuario.has("Pau_PrReSe"))
			parametrosUsuario.addProperty("Pau_PrReSe", "");
		if(!parametrosUsuario.has("Pau_SePrSe"))
			parametrosUsuario.addProperty("Pau_SePrSe", "");
		if(!parametrosUsuario.has("Pau_SePrPe"))
			parametrosUsuario.addProperty("Pau_SePrPe", "");
		if(!parametrosUsuario.has("Pau_SeReSe"))
			parametrosUsuario.addProperty("Pau_SeReSe", "");
		if(!parametrosUsuario.has("Pau_TipAcc"))
			parametrosUsuario.addProperty("Pau_TipAcc", "");
		if(!parametrosUsuario.has("Pau_ConCue"))
			parametrosUsuario.addProperty("Pau_ConCue", "");
		if(!parametrosUsuario.has("Pau_AltCue"))
			parametrosUsuario.addProperty("Pau_AltCue", "");
		if(!parametrosUsuario.has("Pau_CarCue"))
			parametrosUsuario.addProperty("Pau_CarCue", "");
		if(!parametrosUsuario.has("Pau_Solici"))
			parametrosUsuario.addProperty("Pau_Solici", "");
		if(!parametrosUsuario.has("Pau_Autori"))
			parametrosUsuario.addProperty("Pau_Autori", "");
		if(!parametrosUsuario.has("Pau_NivFir"))
			parametrosUsuario.addProperty("Pau_NivFir", "");
		if(!parametrosUsuario.has("Pau_UlPrSe"))
			parametrosUsuario.addProperty("Pau_UlPrSe", "");
		if(!parametrosUsuario.has("Pau_OrCuOr"))
			parametrosUsuario.addProperty("Pau_OrCuOr", "");
		if(!parametrosUsuario.has("Pau_OrCuDe"))
			parametrosUsuario.addProperty("Pau_OrCuDe", "");
		if(!parametrosUsuario.has("Pau_ArMeHa"))
			parametrosUsuario.addProperty("Pau_ArMeHa", "");
		if(!parametrosUsuario.has("Tip_Actual"))
			parametrosUsuario.addProperty("Tip_Actual", "");
		
        // agregando propiedades hard-code
        parametrosUsuario.addProperty("Transaccio", UsuarioParametrosActualizacionOpTransaccio);
        parametrosUsuario.addProperty("Usuario", UsuarioParametrosActualizacionOpUsuario);
        parametrosUsuario.addProperty("SucOrigen", UsuarioParametrosActualizacionOpSucOrigen);
        parametrosUsuario.addProperty("SucDestino", UsuarioParametrosActualizacionOpSucDestino);
        parametrosUsuario.addProperty("Modulo", UsuarioParametrosActualizacionOpModulo);
        
		JsonObject result = Utilerias.performOperacion(UsuarioServicio, UsuarioParametrosActualizacionOp, parametrosUsuario);
		logger.info("COMMONS: Finalizando usuarioParametrosActualizacion metodo... ");
		return result;
	}//Cierre del método
}
