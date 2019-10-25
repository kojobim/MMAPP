package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/cuentas")
public class CuentasCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CuentasCtrl.class);
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
	private static String TransaccionServicio;
	private static String FolioTransaccionGenerarOp;
	private static String FolioTransaccionGenerarOpSucOrigen;
	private static String BitacoraServicio;
	private static String BitacoraCreacionOp;
	private static String BitacoraCreacionOpBitTipOpe;
	private static String BitacoraCreacionOpTransaccio;
	private static String BitacoraCreacionOpUsuario;
	private static String BitacoraCreacionOpSucOrigen;
	private static String BitacoraCreacionOpSucDestino;
	private static String BitacoraCreacionOpModulo;
	private static String MovimientosServicio;
	private static String MovimientosListadoOp;
	private static String MovimientosListadoOpTipConsul;
	private static String MovimientosListadoOpTransaccio;
	private static String MovimientosListadoOpUsuario;
	private static String MovimientosListadoOpSucOrigen;
	private static String MovimientosListadoOpSucDestino;
	private static String MovimientosListadoOpModulo;
	
	
	public CuentasCtrl() {
		super();
		
		SaldoServicio = properties.getProperty("data_service.saldo_servicio");
		ClienteServicio = properties.getProperty("data_service.cliente_servicio");
		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		MovimientosServicio = properties.getProperty("data_service.movimientos_servicio");
				
		
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

		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
		BitacoraCreacionOpBitTipOpe = properties.getProperty("op.bitacora_creacion.bit_tipope");
		BitacoraCreacionOpModulo = properties.getProperty("op.bitacora_creacion.transaccio");
		BitacoraCreacionOpSucDestino = properties.getProperty("op.bitacora_creacion.usuario");
		BitacoraCreacionOpSucOrigen = properties.getProperty("op.bitacora_creacion.suc_origen");
		BitacoraCreacionOpTransaccio = properties.getProperty("op.bitacora_creacion.suc_destino");
		BitacoraCreacionOpUsuario = properties.getProperty("op.bitacora_creacion.modulo");

		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		FolioTransaccionGenerarOpSucOrigen = properties.getProperty("op.folio_transaccion_generar.suc_origen");
		
		MovimientosListadoOp = properties.getProperty("movimientos_servicio.op.movimientos_listado");
		MovimientosListadoOpTipConsul = properties.getProperty("op.movimientos_listado.tip_consul");
		MovimientosListadoOpModulo = properties.getProperty("op.movimientos_listado.modulo");
		MovimientosListadoOpSucDestino = properties.getProperty("op.movimientos_listado.suc_destino");
		MovimientosListadoOpSucOrigen = properties.getProperty("op.movimientos_listado.suc_origen");
		MovimientosListadoOpTransaccio = properties.getProperty("op.movimientos_listado.transaccio");
		MovimientosListadoOpUsuario = properties.getProperty("op.movimientos_listado.usuario");
	}
	
	@Path("/")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasListado(@Context Request solicitud) {
		logger.info("CTRL: Empezando cuentasListado metodo");
		
		logger.info("Authorization " + solicitud.getHeader("Authorization"));
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.getPrincipal(bearerToken);
		
		logger.info(">>>>>>>>>principalResultadoObjecto: " + principalResultadoObjecto);
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		
		String fechaSis = Utilerias.getFechaSis();
		
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
		
		JsonObject saldosClienteConsultarResultadoObjecto = Utilerias.performOperacion(SaldoServicio, SaldosClienteConsultarOp, datosSaldosClienteConsultar);
		
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
		
		JsonObject clienteConsultarResultadoObjecto = Utilerias.performOperacion(ClienteServicio, ClienteConsultarOp, datosClienteConsultar);
		
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
	
	@Path("/{salCuenta}/movimientos")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response movimientosListado(@PathParam("salCuenta") String salCuenta, 
			@QueryParam("fecIni") String fecIni, @QueryParam("fecFin") String fecFin,
			@Context Request solicitud) {
		logger.info("CTRL: Empezando movimientosListado metodo");
		logger.info("- salCuenta " + salCuenta );
		logger.info("- fecIni " + fecIni);
		logger.info("- fecFin " + fecFin);
		
		logger.info("Authorization " + solicitud.getHeader("Authorization"));
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.getPrincipal(bearerToken); 
		
		String usuNumero = principalResultadoObjecto.has("usuNumero") ? principalResultadoObjecto.get("usuNumero").getAsString() : null;
		
		logger.info(">>>>>>>>>principalResultadoObjecto: " + principalResultadoObjecto);
		logger.info(">>>>>X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		String fechaSis = Utilerias.getFechaSis();
		
		logger.info(">>>>>fechaSis " + fechaSis);
		
		JsonObject datosFolioTransaccion = new JsonObject();
		datosFolioTransaccion.addProperty("Num_Transa", "");
		datosFolioTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		JsonObject folioTransaccionGenerarResultadoObjeto =  Utilerias
				.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, 
						datosFolioTransaccion);
		
		logger.info("- folioTransaccionGenerarResultadoObjeto " + folioTransaccionGenerarResultadoObjeto); 
		
		JsonObject transaccion = folioTransaccionGenerarResultadoObjeto.has("transaccion") ? folioTransaccionGenerarResultadoObjeto.get("transaccion").getAsJsonObject() : null;
		String folTransa = transaccion.has("Fol_Transa") ? transaccion.get("Fol_Transa").getAsString() : null;
		
		
		JsonObject datosMovimientos = new JsonObject();
		datosMovimientos.addProperty("Cue_Numero", salCuenta);
		datosMovimientos.addProperty("Fec_Inicia", fecIni);
		datosMovimientos.addProperty("Fec_Final", fecFin);
		datosMovimientos.addProperty("FechaSis", fechaSis);
		datosMovimientos.addProperty("Mov_Natura", "");
		datosMovimientos.addProperty("Mov_PalCla", "");
		datosMovimientos.addProperty("Mov_MonIni", 0);
		datosMovimientos.addProperty("Mov_MonFin", 0);
		datosMovimientos.addProperty("Mov_Clasif", "");
		datosMovimientos.addProperty("Tip_Consul", MovimientosListadoOpTipConsul);
		datosMovimientos.addProperty("NumTransac", "");
		datosMovimientos.addProperty("Transaccio", MovimientosListadoOpTransaccio);
		datosMovimientos.addProperty("Usuario", MovimientosListadoOpUsuario);
		datosMovimientos.addProperty("SucOrigen", MovimientosListadoOpSucOrigen);
		datosMovimientos.addProperty("SucDestino", MovimientosListadoOpSucDestino);
		datosMovimientos.addProperty("Modulo", MovimientosListadoOpModulo);
		
		JsonObject movimientosListadoResultadoObjeto = Utilerias
				.performOperacion(MovimientosServicio, MovimientosListadoOp, 
						datosMovimientos);
		
		logger.info("- movimientosListadoResultadoObjeto " + movimientosListadoResultadoObjeto );
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", salCuenta);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", "0");
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);
		
		JsonObject bitacoraCreacionResultadoObjeto = Utilerias
				.performOperacion(BitacoraServicio, BitacoraCreacionOp, 
						datosBitacora);
		
		logger.info("- bitacoraCreacionResultadoObjeto " + bitacoraCreacionResultadoObjeto);
		
		JsonArray movimientosResultado = new JsonArray();
		
		JsonObject response = new JsonObject();
		response.add("movimientos", movimientosResultado);

		logger.info("- response " + response);
		logger.info("CTRL: Terminando movimientosListado metodo");
		return Response
				.ok(response.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
}
