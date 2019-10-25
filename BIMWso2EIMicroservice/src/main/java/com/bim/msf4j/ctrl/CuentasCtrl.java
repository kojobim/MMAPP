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

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
		
		IdentityServer = properties.getProperty("identity_server.host");
		
		
		SaldoServicio = properties.getProperty("data_service.saldo_servicio");
		ClienteServicio = properties.getProperty("data_service.cliente_servicio");
		
		SaldosClienteConsultarOp = properties.getProperty("saldo_servicio.op.saldos_cliente_consultar");
		SaldosClienteConsultarOpModulo = properties.getProperty("op.saldos_cliente_consultar.modulo");
		SaldosClienteConsultarOpSucDestino = properties.getProperty("op.saldos_cliente_consultar.suc_destino");
		SaldosClienteConsultarOpSucOrigen = properties.getProperty("op.saldos_cliente_consultar.suc_origen");
		SaldosClienteConsultarOpTransaccio = properties.getProperty("op.saldos_cliente_consultar.transaccio");
		SaldosClienteConsultarOpUsuario = properties.getProperty("op.saldos_cliente_consultar.usuario");
		
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
		principalSolicitud.setIsHttps(true);
		principalSolicitud.addHeader("Authorization", solicitud.getHeader("Authorization"));
		principalSolicitud.addHeader("Content-Type", "application/json");
		principalSolicitud.addHeader("Accept", "application/json");
		
		String principalResultado = HttpClientUtils.getPerform(principalSolicitud);
		JsonObject principalResultadoObjecto = new Gson().fromJson(principalResultado, JsonObject.class);
		
		if(principalResultadoObjecto.has("error")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.29");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}
		
		logger.info(">>>>>>>>>principalResultadoObjecto: " + principalResultadoObjecto);
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		String cueClient = principalResultadoObjecto.get("usuClient").getAsString();
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
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
		
		JsonObject cuenta = saldosClienteConsultarResultadoObjecto.has("cuenta") ? saldosClienteConsultarResultadoObjecto.get("cuenta").getAsJsonObject() : null;
		
		JsonArray saldos = new JsonArray();
		
		if(cuenta.has("saldos") && cuenta.get("saldos").isJsonArray()) {
			saldos = cuenta.get("saldos").getAsJsonArray();
		}
		
		if(cuenta.has("saldos") && cuenta.get("saldos").isJsonObject()) {
			saldos = new JsonArray();
			saldos.add(cuenta.get("saldos").getAsJsonObject());
		}
		
		JsonArray saldosResult = new JsonArray();
		
		for(int i = 0; i < saldos.size(); i++) {
			JsonObject saldo = (JsonObject) saldos.get(i);
			String salCuenta = saldo.has("Sal_Cuenta") ? saldo.get("Sal_Cuenta").getAsString() : null;
			String salCLAVE = saldo.has("Sal_CLABE") ? saldo.get("Sal_CLABE").getAsString() : null;
			String tipDescri = saldo.has("Tip_Descri") ? saldo.get("Tip_Descri").getAsString() : null;
			String monDescri = saldo.has("Mon_Descri") ? saldo.get("Mon_Descri").getAsString() : null;
			Double salDispon = saldo.has("Sal_Dispon") ? Double.parseDouble(saldo.get("Sal_Dispon").getAsString()) : null;
			
			Double salDisponValue = Utilerias.redondear(salDispon.doubleValue(), 2); 
			JsonObject saldoResult = new JsonObject();
			saldoResult.addProperty("salCuenta", salCuenta);
			saldoResult.addProperty("salCLAVE", salCLAVE);
			saldoResult.addProperty("tipDescri", tipDescri);
			saldoResult.addProperty("monDescri", monDescri);
			saldoResult.addProperty("salDispon", salDisponValue);
			
			saldosResult.add(saldoResult);
		}
		
		String cliNumero = principalResultadoObjecto.get("usuClient").getAsString();
		
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
		
		JsonObject cliente = clienteConsultarResultadoObjecto.has("cliente") ? clienteConsultarResultadoObjecto.get("cliente").getAsJsonObject() : null;
		
		String cliComple = cliente.has("Cli_Numero") ? cliente.get("Cli_Numero").getAsString() : null;
		String cliRFC = cliente.has("Cli_RFC") ? cliente.get("Cli_RFC").getAsString().trim() : null;
		String cliTelefo = cliente.has("Cli_Telefo") ? cliente.get("Cli_Telefo").getAsString() : null;
		String cliCalle = cliente.has("Cli_Calle") ? cliente.get("Cli_Calle").getAsString() : null;
		String cliCalNum = cliente.has("Cli_CalNum") ? cliente.get("Cli_CalNum").getAsString() : null;
		String cliCodPos = cliente.has("Cli_CodPos") ? "Codigo Postal " + cliente.get("Cli_CodPos").getAsString() : null;
		String cliColoni = cliente.has("Cli_Coloni") ? cliente.get("Cli_Coloni").getAsString() : null;
		String locNombre = cliente.has("Loc_Nombre") ? cliente.get("Loc_Nombre").getAsString() : null;
		String entNombre = cliente.has("Ent_Nombre") ? cliente.get("Ent_Nombre").getAsString() : null;
		String paiNombre = cliente.has("Pai_Nombre") ? cliente.get("Pai_Nombre").getAsString() : null;
		
		String cpCliDir = Utilerias.concat(cliCalle, cliCalNum, cliCodPos, cliColoni, locNombre, entNombre, paiNombre);
		
		JsonObject datosCuentasCliente = new JsonObject();
		datosCuentasCliente.addProperty("cliNumero", cliNumero);
		datosCuentasCliente.addProperty("cliComple", cliComple);
		datosCuentasCliente.addProperty("cliRFC", cliRFC);
		datosCuentasCliente.addProperty("cliTelefo", cliTelefo);
		datosCuentasCliente.addProperty("cpCliDir", cpCliDir);
		datosCuentasCliente.add("cuentas", saldosResult);

		logger.info("CTRL: Terminando cuentasListado metodo");
		return Response
				.ok(datosCuentasCliente.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
}
