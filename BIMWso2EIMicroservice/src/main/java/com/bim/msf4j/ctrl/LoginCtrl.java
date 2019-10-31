package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/login")
public class LoginCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(LoginCtrl.class);
	private static String FolioTransaccionGenerarOpSucOrigen;
	private static String UsuarioConsultarOpTipConsul;
	private static String UsuarioConsultarOpTransaccio;
	private static String UsuarioConsultarOpUsuario;
	private static String UsuarioConsultarOpSucOrigen;
	private static String UsuarioConsultarOpSucDestino;
	private static String UsuarioConsultarOpModulo;
	private static String ConfiguracionBancoDetalleOpTipConsul;
	private static String ConfiguracionBancoDetalleOpTransaccio;
	private static String ConfiguracionBancoDetalleOpUsuario;
	private static String ConfiguracionBancoDetalleOpSucOrigen;
	private static String ConfiguracionBancoDetalleOpSucDestino;
	private static String ConfiguracionBancoDetalleOpModulo;
	private static String UsuarioActualizacionTipActual;
	private static String UsuarioActualizacionTransaccio;
	private static String UsuarioActualizacionUsuario;
	private static String UsuarioActualizacionSucOrigen;
	private static String UsuarioActualizacionSucDestion;
	private static String UsuarioActualizacionModulo;
	private static String TokenVerificarUsuario;
	private static String TokenVerificarTransaccio;
	private static String TokenVerificarSucOrigen;
	private static String TokenVerificarSucDestino;
	private static String TokenVerificarModulo;
	private static String BitacoraCreacionOpBitTipOpe;
	private static String BitacoraCreacionOpTransaccio;
	private static String BitacoraCreacionOpUsuario;
	private static String BitacoraCreacionOpSucOrigen;
	private static String BitacoraCreacionOpSucDestino;
	private static String BitacoraCreacionOpModulo;
	private static String UsuarioConsultarOp;
	private static String UsuarioActualizacionOp;
	private static String ConfiguracionBancoDetalleOp;
	private static String TokenVerificarOp;
	private static String FolioTransaccionGenerarOp;
	private static String BitacoraCreacionOp;
	
	public LoginCtrl() {
		super();
		
		FolioTransaccionGenerarOpSucOrigen = properties.getProperty("op.folio_transaccion_generar.suc_origen");
		
		UsuarioConsultarOpTipConsul = properties.getProperty("op.usuario_consultar.tip_consul");
		UsuarioConsultarOpTransaccio = properties.getProperty("op.usuario_consultar.transaccio");
		UsuarioConsultarOpUsuario = properties.getProperty("op.usuario_consultar.usuario");
		UsuarioConsultarOpSucOrigen = properties.getProperty("op.usuario_consultar.suc_origen");
		UsuarioConsultarOpSucDestino = properties.getProperty("op.usuario_consultar.suc_destino");
		UsuarioConsultarOpModulo = properties.getProperty("op.usuario_consultar.modulo");

		ConfiguracionBancoDetalleOpTipConsul= properties.getProperty("op.configuracion_banco_detalle.tip_consul");
		ConfiguracionBancoDetalleOpTransaccio = properties.getProperty("op.configuracion_banco_detalle.transaccio");
		ConfiguracionBancoDetalleOpUsuario = properties.getProperty("op.configuracion_banco_detalle.usuario");
		ConfiguracionBancoDetalleOpSucOrigen = properties.getProperty("op.configuracion_banco_detalle.suc_origen");
		ConfiguracionBancoDetalleOpSucDestino = properties.getProperty("op.configuracion_banco_detalle.suc_destino");
		ConfiguracionBancoDetalleOpModulo = properties.getProperty("op.configuracion_banco_detalle.modulo");
		
		UsuarioActualizacionTipActual = properties.getProperty("op.usuario_actualizacion.tip_actual");
		UsuarioActualizacionTransaccio = properties.getProperty("op.usuario_actualizacion.transaccio");
		UsuarioActualizacionUsuario = properties.getProperty("op.usuario_actualizacion.usuario");
		UsuarioActualizacionSucOrigen = properties.getProperty("op.usuario_actualizacion.suc_origen");
		UsuarioActualizacionSucDestion = properties.getProperty("op.usuario_actualizacion.suc_destino");
		UsuarioActualizacionModulo = properties.getProperty("op.usuario_actualizacion.modulo");

		TokenVerificarUsuario = properties.getProperty("op.token_verificar.usuario");
		TokenVerificarTransaccio = properties.getProperty("op.token_verificar.transaccio");
		TokenVerificarSucOrigen = properties.getProperty("op.token_verificar.suc_origen");
		TokenVerificarSucDestino = properties.getProperty("op.token_verificar.suc_destino");
		TokenVerificarModulo = properties.getProperty("op.token_verificar.modulo");

		BitacoraCreacionOpBitTipOpe = properties.getProperty("op.bitacora_creacion.bit_tipope");
		BitacoraCreacionOpModulo = properties.getProperty("op.bitacora_creacion.transaccio");
		BitacoraCreacionOpSucDestino = properties.getProperty("op.bitacora_creacion.usuario");
		BitacoraCreacionOpSucOrigen = properties.getProperty("op.bitacora_creacion.suc_origen");
		BitacoraCreacionOpTransaccio = properties.getProperty("op.bitacora_creacion.suc_destino");
		BitacoraCreacionOpUsuario = properties.getProperty("op.bitacora_creacion.modulo");
		
		UsuarioConsultarOp = properties.getProperty("usuario_servicio.op.usuario_consultar");
		UsuarioActualizacionOp = properties.getProperty("usuario_servicio.op.usuario_actualizacion");
		ConfiguracionBancoDetalleOp = properties.getProperty("configuracion_servicio.op.configuracion_banco_detalle");
		TokenVerificarOp = properties.getProperty("token_servicio.op.token_verificar");
		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
	}
	
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(JsonObject datosUsuario, @Context Request solicitud) {
		logger.info("CTRL: Comenzando login metodo");
		logger.info("datosUsuario " + datosUsuario);
		
		String usuClave = Utilerias.getStringProperty(datosUsuario, "Usu_Clave");
		 
		if(usuClave == null || usuClave.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.4");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String usuPasswo = Utilerias.getStringProperty(datosUsuario, "Usu_Passwo"); 
		
		if(usuPasswo == null || usuPasswo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.5");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.getFechaSis();
		
		JsonObject datosFolioTransaccion = new JsonObject();
		datosFolioTransaccion.addProperty("Num_Transa", "");
		datosFolioTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = Utilerias.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, datosFolioTransaccion);
		
		String folTransa = Utilerias.getStringProperty(folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject(), "Fol_Transa");
		
		datosUsuario.addProperty("Usu_Passwo", "");
		datosUsuario.addProperty("Tip_Consul", UsuarioConsultarOpTipConsul);
		datosUsuario.addProperty("Transaccio", UsuarioConsultarOpTransaccio);
		datosUsuario.addProperty("Usuario", UsuarioConsultarOpUsuario);
		datosUsuario.addProperty("SucOrigen", UsuarioConsultarOpSucOrigen);
		datosUsuario.addProperty("SucDestino", UsuarioConsultarOpSucDestino);
		datosUsuario.addProperty("Modulo", UsuarioConsultarOpModulo);
		datosUsuario.addProperty("Usu_Numero", "");
		datosUsuario.addProperty("Usu_UsuAdm", "");
		datosUsuario.addProperty("Usu_Client", "");
		datosUsuario.addProperty("Usu_FolNip", 0);
		datosUsuario.addProperty("Usu_FolTok", "");
		datosUsuario.addProperty("Usu_Status", "");
		datosUsuario.addProperty("Usu_CuCaCo", "");
		datosUsuario.addProperty("Usu_SucMod", "");
		datosUsuario.addProperty("NumTransac", "");	
		datosUsuario.addProperty("FechaSis",fechaSis);	
		
		logger.info("datosUsuario" + datosUsuario);
		JsonObject usuarioConsultarOpResultadoObjeto = Utilerias.performOperacion(UsuarioServicio, UsuarioConsultarOp, datosUsuario);
		logger.info("usuarioConsultarOpResultadoObject" + usuarioConsultarOpResultadoObjeto);

		datosUsuario.addProperty("Usu_Passwo", usuPasswo);
		JsonObject usuario = Utilerias.getJsonObjectProperty(usuarioConsultarOpResultadoObjeto, "usuario");
		
		if(usuario == null || usuario.isJsonPrimitive()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		logger.info("- usuario " + usuario);
		
		String usuNumero = Utilerias.getStringProperty(usuario, "Usu_Numero");
		String usuStatus = Utilerias.getStringProperty(usuario, "Usu_Status");
		
		if(usuNumero == null || (usuStatus == null || usuStatus.isEmpty())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.11");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("F")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.12");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String tokStatus = Utilerias.getStringProperty(usuario, "Tok_Status");
		
		if(tokStatus == null || tokStatus.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(tokStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.13");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		Integer usuCoAcNe = Utilerias.getIntProperty(usuario, "Usu_CoAcNe"); 
		Integer usuCoDeNe = Utilerias.getIntProperty(usuario, "Usu_CoDeNe");
		Integer usuCoPaNe = Utilerias.getIntProperty(usuario, "Usu_CoPaNe");
		
		if(usuCoAcNe == null || usuCoDeNe == null || usuCoPaNe == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
			
		if((usuCoAcNe.intValue() > 3 || usuStatus.equals("B")) && usuCoDeNe.intValue() < 3 && usuCoPaNe < 1) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.14");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuCoDeNe.intValue() > 3 && usuStatus.equals("B")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.15");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuCoPaNe.intValue() > 1 && usuStatus.equals("B")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.15");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String usuStaSes = Utilerias.getStringProperty(usuario, "Usu_StaSes");

		if(usuStaSes == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(!usuStaSes.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.16");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("D")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.17");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("C")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.18");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(!usuStatus.equals("A")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.19");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonObject datosConfiguracion = new JsonObject();
		datosConfiguracion.addProperty("Tip_Consul", ConfiguracionBancoDetalleOpTipConsul);
		datosConfiguracion.addProperty("NumTransac", "");
		datosConfiguracion.addProperty("Transaccio", ConfiguracionBancoDetalleOpTransaccio);
		datosConfiguracion.addProperty("Usuario", ConfiguracionBancoDetalleOpUsuario);
		datosConfiguracion.addProperty("FechaSis", fechaSis);
		datosConfiguracion.addProperty("SucOrigen", ConfiguracionBancoDetalleOpSucOrigen);
		datosConfiguracion.addProperty("SucDestino", ConfiguracionBancoDetalleOpSucDestino);
		datosConfiguracion.addProperty("Modulo", ConfiguracionBancoDetalleOpModulo);
		
		JsonObject configuracionBancoDetalleOpResultadoObjecto = Utilerias.performOperacion(ConfiguracionServicio, ConfiguracionBancoDetalleOp, datosConfiguracion);
		logger.info("configuracionBancoDetalleOpResultadoObjecto" + configuracionBancoDetalleOpResultadoObjecto);
		
		JsonObject configuracionesBanco = Utilerias.getJsonObjectProperty(configuracionBancoDetalleOpResultadoObjecto, "configuracionesBanco");
							
		JsonObject configuracionBanco = configuracionesBanco.has("configuracionBanco") ? 
				configuracionesBanco.get("configuracionBanco").isJsonObject() 
					? configuracionesBanco.get("configuracionBanco").getAsJsonObject()
						: configuracionesBanco.get("configuracionBanco").getAsJsonArray().get(0).getAsJsonObject()
				: null; 
						
		String parAcceso = Utilerias.getStringProperty(configuracionBanco, "Par_Acceso");
		
		if(parAcceso.equals("N")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String usuPasCif = Racal.cifraPassword_HSM(datosUsuario.get("Usu_Passwo").getAsString());
		logger.info("contrasenaCifrada " + usuPasCif);
		
		if(usuPasCif.length() == 7 && usuPasCif.equals("autoriz")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(usuPasCif.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		logger.info(">>>>>>>>>>>>>usuPasCif: " + usuPasCif);
		logger.info(">>>>>>>>>>>>>usuPasswo: " + usuario.get("Usu_Passwo"));
		
		if(!usuPasCif.equals(usuario.get("Usu_Passwo").getAsString().trim())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.20");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		usuarioConsultarOpResultadoObjeto = Utilerias.performOperacion(UsuarioServicio, UsuarioConsultarOp, datosUsuario);
		
		JsonObject datosUsuarioActualizacion = new JsonObject();
		datosUsuarioActualizacion.addProperty("Usu_Numero", "");
		datosUsuarioActualizacion.addProperty("Usu_Clave", datosUsuario.get("Usu_Clave").getAsString());
		datosUsuarioActualizacion.addProperty("Usu_Passwo", "");
		datosUsuarioActualizacion.addProperty("Usu_Status", "");
		datosUsuarioActualizacion.addProperty("Usu_Email", "");
		datosUsuarioActualizacion.addProperty("Usu_UsuAdm", "");
		datosUsuarioActualizacion.addProperty("Usu_FolTok", "");
		datosUsuarioActualizacion.addProperty("Usu_Client", "");
		datosUsuarioActualizacion.addProperty("Usu_SucMod", "");
		datosUsuarioActualizacion.addProperty("Usu_FolNip", "0");
		datosUsuarioActualizacion.addProperty("Usu_Nombre", "");
		datosUsuarioActualizacion.addProperty("Usuario", UsuarioActualizacionUsuario);
		datosUsuarioActualizacion.addProperty("Tip_Actual", UsuarioActualizacionTipActual);
		datosUsuarioActualizacion.addProperty("NumTransac", folTransa);
		datosUsuarioActualizacion.addProperty("Transaccio", UsuarioActualizacionTransaccio);
		datosUsuarioActualizacion.addProperty("FechaSis", fechaSis);
		datosUsuarioActualizacion.addProperty("SucOrigen", UsuarioActualizacionSucOrigen);
		datosUsuarioActualizacion.addProperty("SucDestino", UsuarioActualizacionSucDestion);
		datosUsuarioActualizacion.addProperty("Modulo", UsuarioActualizacionModulo);
		
		JsonObject usuarioActualizacionOpResultadoObjecto = Utilerias.performOperacion(UsuarioServicio, UsuarioActualizacionOp, datosUsuarioActualizacion);
		logger.info("usuarioActualizacionOpResultadoObjecto" + usuarioActualizacionOpResultadoObjecto);
		
		String tovSerie = Utilerias.getStringProperty(usuarioConsultarOpResultadoObjeto.get("usuario").getAsJsonObject(), "Usu_FolTok");
		
		JsonObject datosTokenVerificar = new JsonObject();
		datosTokenVerificar.addProperty("Tov_Serie", tovSerie);
		datosTokenVerificar.addProperty("NumTransac", "");
		datosTokenVerificar.addProperty("Transaccio", TokenVerificarTransaccio);
		datosTokenVerificar.addProperty("Usuario", TokenVerificarUsuario);
		datosTokenVerificar.addProperty("FechaSis", fechaSis);
		datosTokenVerificar.addProperty("SucOrigen", TokenVerificarSucOrigen);
		datosTokenVerificar.addProperty("SucDestino", TokenVerificarSucDestino);
		datosTokenVerificar.addProperty("Modulo", TokenVerificarModulo);
		
		JsonObject tokenVerificarOpResultadoObjecto = Utilerias.performOperacion(TokenServicio, TokenVerificarOp, datosTokenVerificar);
		logger.info("tokenVerificarOpResultadoObjecto" + tokenVerificarOpResultadoObjecto);
		
		JsonObject tokenVerificar = Utilerias.getJsonObjectProperty(tokenVerificarOpResultadoObjecto, "tokenVerificar");
		
		if(tokenVerificar != null && tokenVerificar.has("Tov_FecVen")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.21");
			bimMessageDTO.addMergeVariable("fecha", tokenVerificar.get("Tov_FecVen").getAsString());
			throw new ConflictException(bimMessageDTO.toString());
		}

		usuarioActualizacionOpResultadoObjecto = Utilerias.performOperacion(UsuarioServicio, UsuarioActualizacionOp, datosUsuarioActualizacion);
		logger.info("usuarioActualizacionOpResultadoObjecto" + usuarioActualizacionOpResultadoObjecto);
		
		String bitUsuari = usuarioConsultarOpResultadoObjeto.get("usuario").getAsJsonObject().get("Usu_Numero").getAsString();

		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		
		String bitDireIP = solicitud.getHeader("User-Agent") == null ? solicitud.getHeader("User-Agent") : "";
		String bitPriRef = solicitud.getHeader("X-Forwarded-For") == null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		JsonObject datosBitacoraCreacion = new JsonObject();
		datosBitacoraCreacion.addProperty("Bit_NumTra", "");
		datosBitacoraCreacion.addProperty("Bit_SegRef", "");
		datosBitacoraCreacion.addProperty("Bit_CueOri", "");
		datosBitacoraCreacion.addProperty("Bit_CueDes", "");
		datosBitacoraCreacion.addProperty("Bit_Monto", "0");
		datosBitacoraCreacion.addProperty("Bit_Usuari", bitUsuari);
		datosBitacoraCreacion.addProperty("Bit_Fecha", fechaSis);
		datosBitacoraCreacion.addProperty("Bit_PriRef", bitPriRef);
		datosBitacoraCreacion.addProperty("Bit_DireIP", bitDireIP);
		datosBitacoraCreacion.addProperty("NumTransac", folTransa);
		datosBitacoraCreacion.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacoraCreacion.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacoraCreacion.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacoraCreacion.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacoraCreacion.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacoraCreacion.addProperty("Modulo", BitacoraCreacionOpModulo);		
		
		Utilerias.performOperacion(BitacoraServicio, BitacoraCreacionOp, datosBitacoraCreacion);
		
		String usuClient = Utilerias.getStringProperty(usuario, "Usu_Client");
		String usuNombre = Utilerias.getStringProperty(usuario, "Usu_Nombre");
		String usuEmail = Utilerias.getStringProperty(usuario, "Usu_Email");
		String usuUsuAdm = Utilerias.getStringProperty(usuario, "Usu_UsuAdm");
		String usuFolTok = Utilerias.getStringProperty(usuario, "Usu_FolTok");
		
		JsonObject usuarioResultado = new JsonObject();
		usuarioResultado.addProperty("usuClave ", usuClave);
		usuarioResultado.addProperty("usuNumero ", usuNumero);
		usuarioResultado.addProperty("usuClient ", usuClient);
		usuarioResultado.addProperty("usuNombre ", usuNombre);
		usuarioResultado.addProperty("usuEmail ", usuEmail);
		usuarioResultado.addProperty("usuUsuAdm ", usuUsuAdm);
		usuarioResultado.addProperty("usuFolTok ", usuFolTok);
		logger.info("CTRL: Terminando login metodo");
		return Response.ok(usuarioResultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
}
