package com.bim.msf4j.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.utils.HttpClientUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("/cuentas")
public class CuentasCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CuentasCtrl.class);
	private static String DataServer;
	private static String IdentityServer;
	private static String SaldoServicio;
	private static String ClienteServicio;
	private static String SaldosClienteConsultarOp;
	private static String SaldosClienteConsultarOpTransaccio;
	private static String SaldosClienteConsultarOpUsuario;
	private static String SaldosClienteConsultarOpSucOrigen;
	private static String SaldosClienteConsultarOpSucDestino;
	private static String SaldosClienteConsultarOpModulo;
	private static String ClienteConsultarOp;
	private static String ClienteConsultarOpTipConsul;
	private static String ClienteConsultarOpTransaccio;
	private static String ClienteConsultarOpUsuario;
	private static String ClienteConsultarOpSucOrigen;
	private static String ClienteConsultarOpSucDestino;
	private static String ClienteConsultarOpModulo;
	
	public CuentasCtrl() {
		super();
		DataServer = properties.getProperty("data_service.host");
		
		IdentityServer = properties.getProperty("identiy_server.host");
		
		SaldoServicio = properties.getProperty("data_service.saldo_servicio");
		ClienteServicio = properties.getProperty("data_service.cliente_servicio");
		
		SaldosClienteConsultarOp = properties.getProperty("saldo_servicio.op.saldos_cliente_consultar");
		SaldosClienteConsultarOpModulo = properties.getProperty("op.saldos_cliente_consular.modulo");
		SaldosClienteConsultarOpSucDestino = properties.getProperty("op.saldos_cliente_consular.suc_destino");
		SaldosClienteConsultarOpSucOrigen = properties.getProperty("op.saldos_cliente_consular.suc_origen");
		SaldosClienteConsultarOpTransaccio = properties.getProperty("op.saldos_cliente_consular.transaccio");
		SaldosClienteConsultarOpUsuario = properties.getProperty("op.saldos_cliente_consular.usuario");
		
		ClienteConsultarOp = properties.getProperty("cliente_servicio.op.cliente_consultar");
		ClienteConsultarOpModulo = properties.getProperty("op.cliente_consultar.modulo");
		ClienteConsultarOpSucDestino = properties.getProperty("op.cliente_consultar.suc_destino");
		ClienteConsultarOpSucOrigen = properties.getProperty("op.cliente_consultar.suc_origen");
		ClienteConsultarOpTipConsul = properties.getProperty("op.cliente_consultar.tip_consul");
		ClienteConsultarOpTransaccio = properties.getProperty("op.cliente_consultar.transaccio");
		ClienteConsultarOpUsuario = properties.getProperty("op.cliente_consultar.usuario");
		
	}
	
	@Path("/")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasListado(@Context Request solicitud) {
		logger.info("CTRL: Empezando cuentasListado metodo");
		
		logger.info("Authorization " + solicitud.getHeader("Authorization"));
		
		RequestDTO principalSolicitud = new RequestDTO();
		principalSolicitud.setUrl(IdentityServer);
		principalSolicitud.addHeader("Authorization", solicitud.getHeader("Authorization"));
		
		String principalResultado = HttpClientUtils.getPerform(principalSolicitud);
		JsonObject principalResultadoObjecto = new Gson().fromJson(principalResultado, JsonObject.class);
		
		logger.info(">>>>>>>>>principalResultadoObjecto: " + principalResultadoObjecto);
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		String cueClient = "";
		String usuNumero = "";
		String bitDireIp = solicitud.getHeader("X-Forwarded-For");
		
		JsonObject datosSaldosClienteConsultar = new JsonObject();
		datosSaldosClienteConsultar.addProperty("Cue_Client", cueClient);
		datosSaldosClienteConsultar.addProperty("Usu_Numero", usuNumero);
		datosSaldosClienteConsultar.addProperty("Bit_DireIP", bitDireIp);
		datosSaldosClienteConsultar.addProperty("FechaSis", fechaSis);
		datosSaldosClienteConsultar.addProperty("Cue_Numero", "");
		datosSaldosClienteConsultar.addProperty("NumTransac", "");
		datosSaldosClienteConsultar.addProperty("Transaccio", SaldosClienteConsultarOpTransaccio);
		datosSaldosClienteConsultar.addProperty("Usuario", SaldosClienteConsultarOpUsuario);
		datosSaldosClienteConsultar.addProperty("SucOrigen", SaldosClienteConsultarOpSucOrigen);
		datosSaldosClienteConsultar.addProperty("SucDestino", SaldosClienteConsultarOpSucDestino);
		datosSaldosClienteConsultar.addProperty("Modulo", SaldosClienteConsultarOpModulo);
		
		JsonObject saldosClienteConsultarOp = new JsonObject();
		saldosClienteConsultarOp.add("saldosClienteConsultarOp", datosSaldosClienteConsultar);
		
		StringBuilder saldosClienteConsultarOpUrl = new StringBuilder()
				.append(DataServer)
				.append("/")
				.append(SaldoServicio)
				.append("/")
				.append(SaldosClienteConsultarOp);
		
		RequestDTO saldosClienteConsultarSolicitud = new RequestDTO();
		saldosClienteConsultarSolicitud.setUrl(saldosClienteConsultarOpUrl.toString());
		MessageProxyDTO saldosClienteConsultarMensaje = new MessageProxyDTO();
		saldosClienteConsultarMensaje.setBody(saldosClienteConsultarOp.toString());
		saldosClienteConsultarSolicitud.setMessage(saldosClienteConsultarMensaje);
		
		String saldosClienteConsultarResultado = HttpClientUtils.postPerform(saldosClienteConsultarSolicitud);
		JsonObject saldosClienteConsultarResultadoObjecto = new Gson().fromJson(saldosClienteConsultarResultado, JsonObject.class);
		
		logger.info(">>>>>>>>>saldosClienteConsultarResultadoObjecto " + saldosClienteConsultarResultadoObjecto);
		
		String cliNumero = "";
		
		JsonObject datosClienteConsultar = new JsonObject();
		datosClienteConsultar.addProperty("Cli_Numero", cliNumero);
		datosClienteConsultar.addProperty("FechaSis", fechaSis);
		datosClienteConsultar.addProperty("Cli_Sucurs", "");
		datosClienteConsultar.addProperty("Cli_Nombre", "");
		datosClienteConsultar.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosClienteConsultar.addProperty("NumTransac", "");
		datosClienteConsultar.addProperty("Transaccio", ClienteConsultarOpTransaccio);
		datosClienteConsultar.addProperty("Usuario", ClienteConsultarOpUsuario);
		datosClienteConsultar.addProperty("SucOrigen", ClienteConsultarOpSucOrigen);
		datosClienteConsultar.addProperty("SucDestino", ClienteConsultarOpSucDestino);
		datosClienteConsultar.addProperty("Modulo", ClienteConsultarOpModulo);
		
		JsonObject clienteConsultarOp = new JsonObject();
		clienteConsultarOp.add("clienteConsultarOp", datosClienteConsultar);
		
		StringBuilder clienteConsultarUrl = new StringBuilder()
				.append(DataServer)
				.append("/")
				.append(ClienteServicio)
				.append("/")
				.append(ClienteConsultarOp);
		
		RequestDTO clienteConsultarSolicitud = new RequestDTO();
		clienteConsultarSolicitud.setUrl(clienteConsultarUrl.toString());
		MessageProxyDTO clienteConsultarMensaje = new MessageProxyDTO();
		clienteConsultarMensaje.setBody(clienteConsultarOp.toString());
		clienteConsultarSolicitud.setMessage(clienteConsultarMensaje);
		
		String clienteConsultarResultado = HttpClientUtils.postPerform(clienteConsultarSolicitud);
		JsonObject clienteConsultarResultadoObjecto = new Gson().fromJson(clienteConsultarResultado, JsonObject.class);
		
		logger.info("clienteConsultarResultadoObjecto " + clienteConsultarResultadoObjecto);
		
		
		return null;
	}
	
}
