package com.bim.msf4j.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Racal;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("/login")
public class LoginCtrl {
	
	private static final Logger logger = Logger.getLogger(LoginCtrl.class);
	private static Properties properties;	
	private static String UsuarioConsultar_OpTip_Consul;
	private static String UsuarioConsultarOp_Transaccio;
	private static String UsuarioConsultarOp_Usuario;
	private static String UsuarioConsultarOp_SucOrigen;
	private static String UsuarioConsultarOp_SucDestino;
	private static String UsuarioConsultarOp_Modulo;
	private static String ConfiguracionBancoDetalleOpTransaccio;
	private static String ConfiguracionBancoDetalleOpSucOrigen;
	private static String ConfiguracionBancoDetalleOpSucDestino;
	private static String ConfiguracionBancoDetalleOpModulo;
	private static String UsuarioActualizacionTipActual;
	private static String UsuarioActualizacionNumTransac;
	private static String UsuarioActualizacionTransaccio;
	private static String UsuarioActualizacionSucOrigen;
	private static String UsuarioActualizacionSucDestion;
	private static String UsuarioActualizacionModulo;
	private static String TokenVerificarTransaccio;
	private static String TokenVerificarSucOrigen;
	private static String TokenVerificarSucDestino;
	private static String TokenVerificarModulo;
	private static String DataServiceHost;
	private static String UsuarioServicio;
	private static String ConfiguracionServicio;
	private static String TokenServicio;
	private static String UsuarioConsultarOp;
	private static String UsuarioActualizacionOp;
	private static String ConfiguracionBancoDetalleOp;
	private static String TokenVerificarOp;
	
	
	@PostConstruct
	public void init() {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		UsuarioConsultar_OpTip_Consul = properties.getProperty("op.usuario_consultar.tip_consul");
		UsuarioConsultarOp_Transaccio = properties.getProperty("op.usuario_consultar.transaccio");
		UsuarioConsultarOp_Usuario = properties.getProperty("op.usuario_consultar.usuario");
		UsuarioConsultarOp_SucOrigen = properties.getProperty("op.usuario_consultar.suc_origen");
		UsuarioConsultarOp_SucDestino = properties.getProperty("op.usuario_consultar.suc_destino");
		UsuarioConsultarOp_Modulo = properties.getProperty("op.usuario_consultar.modulo");
		
		ConfiguracionBancoDetalleOpTransaccio = properties.getProperty("op.configuracion_banco_detalle.transaccio");
		ConfiguracionBancoDetalleOpSucOrigen = properties.getProperty("op.configuracion_banco_detalle.suc_origen");
		ConfiguracionBancoDetalleOpSucDestino = properties.getProperty("op.configuracion_banco_detalle.suc_destino");
		ConfiguracionBancoDetalleOpModulo = properties.getProperty("op.configuracion_banco_detalle.modulo");
		
		UsuarioActualizacionTipActual = properties.getProperty("op.usuario_actualizacion.tip_actual");
		UsuarioActualizacionNumTransac = properties.getProperty("op.usuario_actualizacion.num_transac");
		UsuarioActualizacionTransaccio = properties.getProperty("op.usuario_actualizacion.transaccio");
		UsuarioActualizacionSucOrigen = properties.getProperty("op.usuario_actualizacion.suc_origen");
		UsuarioActualizacionSucDestion = properties.getProperty("op.usuario_actualizacion.suc_destino");
		UsuarioActualizacionModulo = properties.getProperty("op.usuario_actualizacion.modulo");
		
		TokenVerificarTransaccio = properties.getProperty("op.token_verificar.transaccio");
		TokenVerificarSucOrigen = properties.getProperty("op.token_verificar.suc_origen");
		TokenVerificarSucDestino = properties.getProperty("op.token_verificar.suc_destino");
		TokenVerificarModulo = properties.getProperty("op.token_verificar.modulo");
		
		DataServiceHost = properties.getProperty("data_service.host");
		
		UsuarioServicio = properties.getProperty("data_service.usuario_service");
		ConfiguracionServicio = properties.getProperty("data_service.configuracion_servicio");
		TokenServicio = properties.getProperty("data_service.token_servicio");
		
		UsuarioConsultarOp = properties.getProperty("usuario_service.op.usuario_consultar");
		UsuarioActualizacionOp = properties.getProperty("usuario_service.op.usuario_actualizacion");
		ConfiguracionBancoDetalleOp = properties.getProperty("data_service.op.configuracion_banco_detalle");
		TokenVerificarOp = properties.getProperty("data_service.op.configuracion_banco_detalle");
	}
	
	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void login(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando login metodo");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		String mensaje = HttpClientUtils.getStringContent(solicitud);
		JsonObject datosUsuario = new Gson().fromJson(mensaje, JsonObject.class);
		datosUsuario.addProperty("Tip_Consul", UsuarioConsultar_OpTip_Consul);
		datosUsuario.addProperty("Transaccio", UsuarioConsultarOp_Transaccio);
		datosUsuario.addProperty("Usuario", UsuarioConsultarOp_Usuario);
		datosUsuario.addProperty("SucOrigen", UsuarioConsultarOp_SucOrigen);
		datosUsuario.addProperty("SucDestino", UsuarioConsultarOp_SucDestino);
		datosUsuario.addProperty("Modulo", UsuarioConsultarOp_Modulo);
		datosUsuario.addProperty("Usu_Numero", "");
		datosUsuario.addProperty("Usu_UsuAdm", "");
		datosUsuario.addProperty("Usu_Client", "");
		datosUsuario.addProperty("Usu_FolNip", 0);
		datosUsuario.addProperty("Usu_FolTok", "");
		datosUsuario.addProperty("Usu_Status", "");
		datosUsuario.addProperty("Usu_CuCaCo", "");
		datosUsuario.addProperty("Usu_SucMod", "");
		datosUsuario.addProperty("NumTransac", "");	
		datosUsuario.addProperty("FechaSis",fechaSis );	
		
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

		RequestDTO usuarioConsultarOpsolicitud = new RequestDTO();
		usuarioConsultarOpsolicitud.setUrl(usuarioConsultarUrl.toString());
		MessageProxyDTO usuarioConsultarOpMessaje = new MessageProxyDTO();
		usuarioConsultarOpMessaje.setBody(usuarioConsultarOp.toString());
		usuarioConsultarOpsolicitud.setMessage(usuarioConsultarOpMessaje);
		
		String usuarioConsultarOpResultado = HttpClientUtils.postPerform(usuarioConsultarOpsolicitud);
		JsonObject usuarioConsultarOpResultadoObjecto = new Gson().fromJson(usuarioConsultarOpResultado, JsonObject.class);
		logger.info("usuarioConsultarOpResultadoObject" + usuarioConsultarOpResultadoObjecto);
		
		String usuario = usuarioConsultarOpResultadoObjecto.get("usuario").getAsJsonObject().get("Usu_Numero").getAsString();
		logger.info("usuarioValue" + usuario);
		JsonObject datosConfiguracion = new JsonObject();
		datosConfiguracion.addProperty("Tip_Consul", UsuarioConsultar_OpTip_Consul);
		datosConfiguracion.addProperty("NumTransac", "");
		datosConfiguracion.addProperty("Transaccio", ConfiguracionBancoDetalleOpTransaccio);
		datosConfiguracion.addProperty("Usuario", usuario);
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
		JsonObject configuracionBancoDetalleOpResultadoObjecto = new Gson().fromJson(configuracionBancoDetalleOpResultado, JsonObject.class);
		logger.info("configuracionBancoDetalleOpResultadoObjecto" + configuracionBancoDetalleOpResultadoObjecto);
		
		String contrasenaCifrada = Racal.cifraPassword_HSM(datosUsuario.get("Usu_Passwo").getAsString());
		logger.info("contrasenaCifrada " + contrasenaCifrada);
		
		datosUsuario.addProperty("Usuario", usuario);
		usuarioConsultarOpsolicitud.getMessage().setBody(datosUsuario.toString());
		
		logger.info("newVersion" + usuarioConsultarOpsolicitud.toString());
		usuarioConsultarOpResultado = HttpClientUtils.postPerform(usuarioConsultarOpsolicitud);
		usuarioConsultarOpResultadoObjecto = new Gson().fromJson(usuarioConsultarOpResultado, JsonObject.class);
		
		JsonObject datosUsuarioActualizacion = new JsonObject();
		datosUsuarioActualizacion.addProperty("Usu_Clave", datosUsuario.get("Usu_Clave").getAsString());
		datosUsuarioActualizacion.addProperty("Usuario", usuario);
		datosUsuarioActualizacion.addProperty("Tip_Actual", UsuarioActualizacionTipActual);
		datosUsuarioActualizacion.addProperty("NumTransac", UsuarioActualizacionNumTransac);
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
		usuarioActualizacionMensaje.setBody(UsuarioActualizacionOp.toString());
		usuarioActualizacionSolicitud.setMessage(usuarioActualizacionMensaje);
		
		String usuarioActualizacionOpResultado = HttpClientUtils.postPerform(usuarioConsultarOpsolicitud);
		JsonObject usuarioActualizacionOpResultadoObjecto = new Gson().fromJson(usuarioActualizacionOpResultado, JsonObject.class);
		logger.info("usuarioActualizacionOpResultadoObjecto" + usuarioActualizacionOpResultadoObjecto);
		
		String tovSerie = usuarioConsultarOpResultadoObjecto.get("usuario").getAsJsonObject().get("Tov_Serie").getAsString();
		JsonObject datosTokenVerificar = new JsonObject();
			datosTokenVerificar.addProperty("Usuario", usuario);
			datosTokenVerificar.addProperty("Tov_Serie", tovSerie);
			datosTokenVerificar.addProperty("Transaccio", TokenVerificarTransaccio);
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
		JsonObject tokenVerificarOpResultadoObjecto = new Gson().fromJson(tokenVerificarOpResultado, JsonObject.class);
		logger.info("tokenVerificarOpResultadoObjecto" + tokenVerificarOpResultadoObjecto);
			
		logger.info("CTRL: Terminando login metodo");
	}
}
