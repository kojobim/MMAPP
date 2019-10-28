package com.bim.msf4j.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Racal;
import com.google.gson.Gson;
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
	private static String DataServiceHost;
	private static String UsuarioServicio;
	private static String ConfiguracionServicio;
	private static String TokenServicio;
	private static String TransaccionServicio;
	private static String BitacoraServicio;
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
		
		DataServiceHost = properties.getProperty("data_service.host");
		
		UsuarioServicio = properties.getProperty("data_service.usuario_servicio");
		ConfiguracionServicio = properties.getProperty("data_service.configuracion_servicio");
		TokenServicio = properties.getProperty("data_service.token_servicio");
		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		
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
		
		String usuClave = datosUsuario.has("Usu_Clave") ? datosUsuario.get("Usu_Clave").getAsString() : null;
		 
		if(usuClave == null || usuClave.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.4");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String usuPasswo = datosUsuario.has("Usu_Passwo") ? datosUsuario.get("Usu_Passwo").getAsString() : null; 
		
		if(usuPasswo == null || usuPasswo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.5");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		JsonObject datosFolioTransaccion = new JsonObject();
		datosFolioTransaccion.addProperty("Num_Transa", "");
		datosFolioTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		JsonObject folioTransaccionGenerarOp = new JsonObject();
		folioTransaccionGenerarOp.add("folioTransaccionGenerarOp", datosFolioTransaccion);
		
		StringBuilder folioTransaccionGenerarOpUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TransaccionServicio)
				.append("/")
				.append(FolioTransaccionGenerarOp);
		
		RequestDTO folioTransaccionGenerarOpSolicitud = new RequestDTO();
		folioTransaccionGenerarOpSolicitud.setUrl(folioTransaccionGenerarOpUrl.toString());
		MessageProxyDTO folioTransaccionGenerarOpMensaje = new MessageProxyDTO();
		folioTransaccionGenerarOpMensaje.setBody(folioTransaccionGenerarOp.toString());
		folioTransaccionGenerarOpSolicitud.setMessage(folioTransaccionGenerarOpMensaje);
		
		logger.info("folioTransaccionGenerarOpSolicitud " + folioTransaccionGenerarOpSolicitud.toString());
		String folioTransaccionGenerarOpResultado = HttpClientUtils.postPerform(folioTransaccionGenerarOpSolicitud);
		logger.info("folioTransaccionGenerarOpResultado" + folioTransaccionGenerarOpResultado);
		JsonObject folioTransaccionGenerarOpResultadoObjeto = new Gson().fromJson(folioTransaccionGenerarOpResultado, JsonObject.class);
		
		String folTransa = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		
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
		StringBuilder usuarioConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(UsuarioServicio)
				.append("/")
				.append(UsuarioConsultarOp);
		JsonObject usuarioConsultarOp = new JsonObject();
		usuarioConsultarOp.add("usuarioConsultarOp", datosUsuario);
		logger.info("usuarioConsultarOp" + usuarioConsultarOp);

		RequestDTO usuarioConsultarOpSolicitud = new RequestDTO();
		usuarioConsultarOpSolicitud.setUrl(usuarioConsultarUrl.toString());
		MessageProxyDTO usuarioConsultarOpMensaje = new MessageProxyDTO();
		usuarioConsultarOpMensaje.setBody(usuarioConsultarOp.toString());
		usuarioConsultarOpSolicitud.setMessage(usuarioConsultarOpMensaje);
		
		String usuarioConsultarOpResultado = HttpClientUtils.postPerform(usuarioConsultarOpSolicitud);
		logger.info("usuarioConsultarOpResultado " + usuarioConsultarOpResultado);
		JsonObject usuarioConsultarOpResultadoObjeto = new Gson().fromJson(usuarioConsultarOpResultado, JsonObject.class);
		logger.info("usuarioConsultarOpResultadoObject" + usuarioConsultarOpResultadoObjeto);

		datosUsuario.addProperty("Usu_Passwo", usuPasswo);
		JsonObject usuario = usuarioConsultarOpResultadoObjeto.has("usuario") ? usuarioConsultarOpResultadoObjeto.get("usuario").getAsJsonObject() : null;
		
		if(usuario == null || usuario.isJsonPrimitive()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
			
		String usuNumero = usuario.has("Usu_Numero") ? usuario.get("Usu_Numero").getAsString() : null;
		String usuStatus = usuario.has("Usu_Status") ? usuario.get("Usu_Status").getAsString() : null;
		
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
		
		String tokStatus = usuario.has("Tok_Status") ? usuario.get("Tok_Status").getAsString() : null;
		
		if(tokStatus == null || tokStatus.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(tokStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.13");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		Integer usuCoAcNe = usuario.has("Usu_CoAcNe") ? usuario.get("Usu_CoAcNe").getAsInt() : null; 
		Integer usuCoDeNe = usuario.has("Usu_CoDeNe") ? usuario.get("Usu_CoDeNe").getAsInt() : null;
		Integer usuCoPaNe = usuario.has("Usu_CoPaNe") ? usuario.get("Usu_CoPaNe").getAsInt() : null;
		
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
		
		String usuStaSes = usuario.has("Usu_StaSes") ? usuario.get("Usu_StaSes").getAsString() : null;

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
		
		JsonObject configuracionBancoDetalleOp = new JsonObject();
		configuracionBancoDetalleOp.add("configuracionBancoDetalleOp", datosConfiguracion);
		
		RequestDTO configuracionBancoDetalleSolicitud = new RequestDTO();
		StringBuilder configuracionBancoDetalleOpUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(ConfiguracionServicio)
				.append("/")
				.append(ConfiguracionBancoDetalleOp);
		configuracionBancoDetalleSolicitud.setUrl(configuracionBancoDetalleOpUrl.toString());
		configuracionBancoDetalleSolicitud.setMessage(new MessageProxyDTO());
		configuracionBancoDetalleSolicitud.getMessage().setBody(configuracionBancoDetalleOp.toString());
		
		String configuracionBancoDetalleOpResultado = HttpClientUtils.postPerform(configuracionBancoDetalleSolicitud);
		logger.info("configuracionBancoDetalleOpResultado " + configuracionBancoDetalleOpResultado);
		JsonObject configuracionBancoDetalleOpResultadoObjecto = new Gson().fromJson(configuracionBancoDetalleOpResultado, JsonObject.class);
		logger.info("configuracionBancoDetalleOpResultadoObjecto" + configuracionBancoDetalleOpResultadoObjecto);
		
		JsonObject configuracionesBanco = configuracionBancoDetalleOpResultadoObjecto.has("configuracionesBanco") 
				? configuracionBancoDetalleOpResultadoObjecto.get("configuracionesBanco").isJsonObject() 
					? configuracionBancoDetalleOpResultadoObjecto.get("configuracionesBanco").getAsJsonObject()
					: configuracionBancoDetalleOpResultadoObjecto.get("configuracionesBanco").getAsJsonArray().get(0).getAsJsonObject()
				: null;
							
							
		JsonObject configuracionBanco = configuracionesBanco.has("configuracionBanco") ? configuracionesBanco.get("configuracionBanco").getAsJsonObject() : null; 
		String parAcceso = configuracionBanco.has("Par_Acceso") ? configuracionBanco.get("Par_Acceso").getAsString() : null;
		
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
			
		logger.info("newVersion" + usuarioConsultarOpSolicitud.toString());
		usuarioConsultarOpResultado = HttpClientUtils.postPerform(usuarioConsultarOpSolicitud);
		usuarioConsultarOpResultadoObjeto = new Gson().fromJson(usuarioConsultarOpResultado, JsonObject.class);
		
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
		
		JsonObject usuarioActualizacionOp = new JsonObject();
		usuarioActualizacionOp.add("usuarioActualizacionOp", datosUsuarioActualizacion);
		
		StringBuilder usuarioActualizacionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(UsuarioServicio)
				.append("/")
				.append(UsuarioActualizacionOp);
		
		RequestDTO usuarioActualizacionSolicitud = new RequestDTO();
		usuarioActualizacionSolicitud.setUrl(usuarioActualizacionUrl.toString());
		MessageProxyDTO usuarioActualizacionMensaje = new MessageProxyDTO();
		usuarioActualizacionMensaje.setBody(usuarioActualizacionOp.toString());
		usuarioActualizacionSolicitud.setMessage(usuarioActualizacionMensaje);
		
		String usuarioActualizacionOpResultado = HttpClientUtils.postPerform(usuarioActualizacionSolicitud);
		logger.info("usuarioActualizacionOpResultado " + usuarioActualizacionOpResultado);
		JsonObject usuarioActualizacionOpResultadoObjecto = new Gson().fromJson(usuarioActualizacionOpResultado, JsonObject.class);
		logger.info("usuarioActualizacionOpResultadoObjecto" + usuarioActualizacionOpResultadoObjecto);
		
		String tovSerie = usuarioConsultarOpResultadoObjeto.get("usuario").getAsJsonObject().get("Usu_FolTok").getAsString();
		
		JsonObject datosTokenVerificar = new JsonObject();
			datosTokenVerificar.addProperty("Tov_Serie", tovSerie);
			datosTokenVerificar.addProperty("NumTransac", "");
			datosTokenVerificar.addProperty("Transaccio", TokenVerificarTransaccio);
			datosTokenVerificar.addProperty("Usuario", TokenVerificarUsuario);
			datosTokenVerificar.addProperty("FechaSis", fechaSis);
			datosTokenVerificar.addProperty("SucOrigen", TokenVerificarSucOrigen);
			datosTokenVerificar.addProperty("SucDestino", TokenVerificarSucDestino);
			datosTokenVerificar.addProperty("Modulo", TokenVerificarModulo);
		
		JsonObject tokenVerificarOp = new JsonObject();
		tokenVerificarOp.add("tokenVerificarOp", datosTokenVerificar);
		
		StringBuilder tokenVerificarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TokenServicio)
				.append("/")
				.append(TokenVerificarOp);
		
		RequestDTO tokenVerificarSolicitud = new RequestDTO();
		tokenVerificarSolicitud.setUrl(tokenVerificarUrl.toString());
		MessageProxyDTO tokenVerificiarMensaje = new MessageProxyDTO();
		tokenVerificiarMensaje.setBody(tokenVerificarOp.toString());
		tokenVerificarSolicitud.setMessage(tokenVerificiarMensaje);
		
		String tokenVerificarOpResultado = HttpClientUtils.postPerform(tokenVerificarSolicitud);
		logger.info("tokenVerificarOpResultado " + tokenVerificarOpResultado);
		JsonObject tokenVerificarOpResultadoObjecto = new Gson().fromJson(tokenVerificarOpResultado, JsonObject.class);
		logger.info("tokenVerificarOpResultadoObjecto" + tokenVerificarOpResultadoObjecto);
		
		JsonObject tokenVerificar = tokenVerificarOpResultadoObjecto.has("tokenVerificar") 
				&& tokenVerificarOpResultadoObjecto.get("tokenVerificar").isJsonObject() 
				? tokenVerificarOpResultadoObjecto.get("tokenVerificar").getAsJsonObject()
						: null;
		
		if(tokenVerificar != null && tokenVerificar.has("Tov_FecVen")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.21");
			bimMessageDTO.addMergeVariable("fecha", tokenVerificar.get("Tov_FecVen").getAsString());
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		logger.info("usuarioActualizacionOp " + usuarioActualizacionOp.toString());
		usuarioActualizacionSolicitud = new RequestDTO();
		usuarioActualizacionSolicitud.setUrl(usuarioActualizacionUrl.toString());
		usuarioActualizacionMensaje = new MessageProxyDTO();
		usuarioActualizacionMensaje.setBody(usuarioActualizacionOp.toString());
		usuarioActualizacionSolicitud.setMessage(usuarioActualizacionMensaje);
		
		usuarioActualizacionOpResultado = HttpClientUtils.postPerform(usuarioActualizacionSolicitud);
		logger.info("usuarioActualizacionOpResultado " + usuarioActualizacionOpResultado);
		usuarioActualizacionOpResultadoObjecto = new Gson().fromJson(usuarioActualizacionOpResultado, JsonObject.class);
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
			
		JsonObject bitacoraCreacionOp = new JsonObject();
		bitacoraCreacionOp.add("bitacoraCreacionOp", datosBitacoraCreacion);
		
		StringBuilder bitacoraCreacionOpUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(BitacoraServicio)
				.append("/")
				.append(BitacoraCreacionOp);
		RequestDTO bitacoraCreacionOpSolicitud = new RequestDTO();
		MessageProxyDTO bitacoraCreacionOpMensaje = new MessageProxyDTO();
		bitacoraCreacionOpMensaje.setBody(bitacoraCreacionOp.toString());
		bitacoraCreacionOpSolicitud.setUrl(bitacoraCreacionOpUrl.toString());
		bitacoraCreacionOpSolicitud.setMessage(bitacoraCreacionOpMensaje);
		
		HttpClientUtils.postPerform(bitacoraCreacionOpSolicitud);
		
		String usuClient = usuario.has("Usu_Client") ? usuario.get("Usu_Client").getAsString() : null;
		String usuNombre = usuario.has("Usu_Nombre") ? usuario.get("Usu_Nombre").getAsString() : null;
		String usuEmail = usuario.has("Usu_Email") ? usuario.get("Usu_Email").getAsString() : null;
		String usuUsuAdm = usuario.has("Usu_UsuAdm") ? usuario.get("Usu_UsuAdm").getAsString() : null;
		
		JsonObject usuarioResultado = new JsonObject();
		usuarioResultado.addProperty("usuClave ", usuClave);
		usuarioResultado.addProperty("usuNumero ", usuNumero);
		usuarioResultado.addProperty("usuClient ", usuClient);
		usuarioResultado.addProperty("usuNombre ", usuNombre);
		usuarioResultado.addProperty("usuEmail ", usuEmail);
		usuarioResultado.addProperty("usuUsuAdm ", usuUsuAdm);
		logger.info("CTRL: Terminando login metodo");
		return Response.ok(usuarioResultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
}
