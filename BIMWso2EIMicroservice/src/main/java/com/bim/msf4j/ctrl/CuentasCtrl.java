package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.service.SoapService;
import com.bim.commons.service.TokenService;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/cuentas")
public class CuentasCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CuentasCtrl.class);
	private SoapService soapService;
	private TokenService tokenService;
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
	private static String FolioTransaccionGenerarOp;
	private static String FolioTransaccionGenerarOpSucOrigen;
	private static String BitacoraCreacionOp;
	private static String BitacoraCreacionOpBitTipOpe;
	private static String BitacoraCreacionOpEnvioCorreoMovimientosBitTipOpe;
	private static String BitacoraCreacionOpTransaccio;
	private static String BitacoraCreacionOpUsuario;
	private static String BitacoraCreacionOpSucOrigen;
	private static String BitacoraCreacionOpSucDestino;
	private static String BitacoraCreacionOpModulo;
	private static String MovimientosListadoOp;
	private static String MovimientosListadoOpTipConsul;
	private static String MovimientosListadoOpTransaccio;
	private static String MovimientosListadoOpUsuario;
	private static String MovimientosListadoOpSucOrigen;
	private static String MovimientosListadoOpSucDestino;
	private static String MovimientosListadoOpModulo;
	
	
	public CuentasCtrl() {
		super();
		
		this.soapService = new SoapService();
		this.tokenService = new TokenService();
		
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
		BitacoraCreacionOpEnvioCorreoMovimientosBitTipOpe = properties.getProperty("op.bitacora_creacion.envio_correo_movimientos.bit_tipope");
		
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
		
		JsonObject cuenta = Utilerias.getJsonObjectProperty(saldosClienteConsultarResultadoObjecto, "cuenta");
		
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
			String salCuenta = Utilerias.getStringProperty(saldo, "Sal_Cuenta");
			String salCLAVE = Utilerias.getStringProperty(saldo, "Sal_CLABE");
			String tipDescri = Utilerias.getStringProperty(saldo, "Tip_Descri");
			String monDescri = Utilerias.getStringProperty(saldo, "Mon_Descri");
			Double salDispon = Utilerias.getDoubleProperty(saldo, "Sal_Dispon");
			
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
		
		JsonObject cliente = Utilerias.getJsonObjectProperty(clienteConsultarResultadoObjecto, "cliente");
		
		String cliComple = Utilerias.getStringProperty(cliente, "Cli_Numero");
		String cliRFC = Utilerias.getStringProperty(cliente, "Cli_RFC").trim();
		String cliTelefo = Utilerias.getStringProperty(cliente,"Cli_Telefo");
		String cliCalle = Utilerias.getStringProperty(cliente,"Cli_Calle");
		String cliCalNum = Utilerias.getStringProperty(cliente,"Cli_CalNum");
		String cliCodPos = Utilerias.getStringProperty(cliente,"Cli_CodPos");
		String cliColoni = Utilerias.getStringProperty(cliente,"Cli_Coloni");
		String locNombre = Utilerias.getStringProperty(cliente,"Loc_Nombre");
		String entNombre = Utilerias.getStringProperty(cliente,"Ent_Nombre");
		String paiNombre = Utilerias.getStringProperty(cliente,"Pai_Nombre");
		
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
		
		logger.info("- datosFolioTransaccion " + datosFolioTransaccion);
		
		JsonObject folioTransaccionGenerarResultadoObjeto =  Utilerias
				.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, 
						datosFolioTransaccion);
		
		logger.info("- folioTransaccionGenerarResultadoObjeto " + folioTransaccionGenerarResultadoObjeto); 
		
		JsonObject transaccion = Utilerias.getJsonObjectProperty(folioTransaccionGenerarResultadoObjeto,"transaccion");
		String folTransa = Utilerias.getStringProperty(transaccion,"Fol_Transa");
		
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
		
		logger.info("- movimientosListadoResultadoObjeto " + movimientosListadoResultadoObjeto);
		
		JsonObject cuenta = Utilerias.getJsonObjectProperty(movimientosListadoResultadoObjeto , "cuenta");
		
		JsonArray movimientosResultado = new JsonArray();
		
		JsonArray movimientos = new JsonArray();
		
		if(cuenta.has("movimientos") && cuenta.get("movimientos").isJsonObject()) {
			JsonObject movimiento = Utilerias.getJsonObjectProperty(cuenta, "movimientos");
			if(movimiento != null)
				movimientos.add(movimiento);
		}
		if(cuenta.has("movimientos") && cuenta.get("movimientos").isJsonArray()) {
			movimientos = Utilerias.getJsonArrayProperty(cuenta, "movimientos");
		}
		
		logger.info("- movimientos " + movimientos);
		
		for(int i = 0; i < movimientos.size(); i++) {
			JsonObject movimiento = (JsonObject) movimientos.get(i);
			String movDescri = Utilerias.getStringProperty(movimiento, "Mov_Descri").trim();
			String fechaVal = Utilerias.getStringProperty(movimiento, "Fecha_Val");
			Float movCantid = Utilerias.getFloatProperty(movimiento, "Mov_Cantid");
			Integer movNatura = Utilerias.getIntProperty(movimiento, "Mov_Natura");
			
			Float movCantidValor = (float) Utilerias.redondear(movCantid, 2); 
			JsonObject movimientoResultado = new JsonObject();
			movimientoResultado.addProperty("movDescrip", movDescri);
			movimientoResultado.addProperty("fechaVal", fechaVal);
			movimientoResultado.addProperty("movCantid", movCantidValor);
			movimientoResultado.addProperty("movNatura", movNatura);
			movimientosResultado.add(movimientoResultado);
		}
		
		
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
		
		
		JsonObject response = new JsonObject();
		response.add("movimientos", movimientosResultado);

		logger.info("- response " + response);
		logger.info("CTRL: Terminando movimientosListado metodo");
		return Response
				.ok(response.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/envio-movimientos")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void movimientosRegistro(JsonObject datosEnvioCorreoMovimientos, @Context Request solicitud) {
		logger.info("CTRL: Empezando movimientosRegistro metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		
		JsonObject principal = Utilerias.getPrincipal(bearerToken);
		
		logger.info("- principal " + principal);
	
		JsonObject datosfolioTransaccion = new JsonObject();
		datosfolioTransaccion.addProperty("Num_Transa", "");
		datosfolioTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		JsonObject folioTransaccionResultado = Utilerias.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, datosfolioTransaccion);
		logger.info("- folioTransaccionResultado " + folioTransaccionResultado );
		
		JsonObject transaccion = Utilerias.getJsonObjectProperty(folioTransaccionResultado, "transaccion");
		
		JsonObject datosCorreoMovimientos = Utilerias.getJsonObjectProperty(datosEnvioCorreoMovimientos, "enviaCorreoMovimientos");
		logger.info("- datosCorreoMovimientos " + datosCorreoMovimientos);
		
		String cpRSAToken = Utilerias.getStringProperty(datosCorreoMovimientos, "cpRSAToken");
		logger.info("- claveRSA " + cpRSAToken);

		String folTok = Utilerias.getStringProperty(principal, "usuFolTok");
		logger.info("- folTok " + folTok);

		String bitUsuari = Utilerias.getStringProperty(principal, "usuNumero");
		logger.info("- bitUsuari " + bitUsuari);

		String numTransac = Utilerias.getStringProperty(transaccion, "Fol_Transa");
		
		this.tokenService.validarTokenOperacion(folTok, cpRSAToken, bitUsuari, numTransac);

		String fechaSis = Utilerias.getFechaSis();
		String bitPriRef = Utilerias.getStringProperty(principal, "usuClient");
		String bitDireIp = solicitud.getHeader("X_Forwarded_For");
		
		JsonObject datosBitacoraCreacion = new JsonObject();
		datosBitacoraCreacion.addProperty("Bit_Usuari", bitUsuari);
		datosBitacoraCreacion.addProperty("Bit_Fecha", fechaSis);
		datosBitacoraCreacion.addProperty("Bit_PriRef", bitPriRef);
		datosBitacoraCreacion.addProperty("Bit_DireIP", bitDireIp);
		datosBitacoraCreacion.addProperty("Num_Transac", numTransac);
		datosBitacoraCreacion.addProperty("Bit_NumTra", "");
		datosBitacoraCreacion.addProperty("Bit_CueOri", "");
		datosBitacoraCreacion.addProperty("Bit_CueDes", "");
		datosBitacoraCreacion.addProperty("Bit_Monto", 0);
		datosBitacoraCreacion.addProperty("Bit_SegRef", "");
		datosBitacoraCreacion.addProperty("Bit_TipOpe", BitacoraCreacionOpEnvioCorreoMovimientosBitTipOpe);
		datosBitacoraCreacion.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacoraCreacion.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacoraCreacion.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacoraCreacion.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacoraCreacion.addProperty("Modulo", BitacoraCreacionOpModulo);
		
		Utilerias.performOperacion(BitacoraServicio, BitacoraCreacionOp, datosBitacoraCreacion);
		
		String anio = Utilerias.getStringProperty(datosCorreoMovimientos, "cpAnio");
		String mes = Utilerias.getStringProperty(datosCorreoMovimientos, "cpMes");
		String cliNumero = Utilerias.getStringProperty(datosCorreoMovimientos, "cliNumero");
		
		logger.info("- anio " + anio);
		logger.info("- mes " + mes);
		logger.info("- cliNumero " + cliNumero);
		
		this.soapService.movimientosEnvioCorreo(anio, mes, cliNumero);
		logger.info("CTRL: Terminando movimientosRegistro metodo");
		
		
	}
}
