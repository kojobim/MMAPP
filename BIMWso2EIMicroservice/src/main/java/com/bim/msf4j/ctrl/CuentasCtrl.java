package com.bim.msf4j.ctrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.Request;

import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.ClienteServicio;
import com.bim.commons.service.MovimientosServicio;
import com.bim.commons.service.SaldoServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.SoapServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/cuentas")
public class CuentasCtrl extends BimBaseCtrl {

	private static final Logger logger = LoggerFactory.getLogger(CuentasCtrl.class);
	private BitacoraServicio bitacoraServicio;
	private ClienteServicio clienteServicio;
	private TransaccionServicio transaccionServicio;
	private MovimientosServicio movimientosServicio;
	private SaldoServicio saldoServicio;
	private SoapServicio soapService;
	private TokenServicio tokenServicio;
	
	private static String ClienteConsultarOpTipConsul;
	private static String CuentasListadoBitacoraCreacionOpBitTipOpe;
	private static String MovimientosListadoBitacoraCreacionOpBitTipOpe;
	private static String MovimientosRegistroBitacoraCreacionOpBitTipOpe;
	
	public CuentasCtrl() {
		super();
		
		this.bitacoraServicio = new BitacoraServicio();
		this.clienteServicio = new ClienteServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.movimientosServicio = new MovimientosServicio();
		this.saldoServicio = new SaldoServicio();
		this.soapService = new SoapServicio();
		this.tokenServicio = new TokenServicio();

				
		ClienteConsultarOpTipConsul = properties.getProperty("op.cliente_consultar.tip_consul.c9");
		CuentasListadoBitacoraCreacionOpBitTipOpe = properties.getProperty("op.cuentas_listado.bitacora_creacion.bit_tip_ope");
		MovimientosListadoBitacoraCreacionOpBitTipOpe = properties.getProperty("op.movimientos_listado.bitacora_creacion.bit_tip_ope");
		MovimientosRegistroBitacoraCreacionOpBitTipOpe = properties.getProperty("op.movimientos_registro.bitacora_creacion.bit_tip_ope");

	}
	
	@Path("/")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasListado(@Context Request solicitud) {
		logger.info("CTRL: Empezando cuentasListado metodo");
		
		logger.info("Authorization " + solicitud.getHeader("Authorization"));
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		logger.info(">>>>>>>>>principalResultadoObjecto: " + principalResultadoObjecto);
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		String cueClient = principalResultadoObjecto.get("usuClient").getAsString();
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String bitDireIp = solicitud.getHeader("X-Forwarded-For") != null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		JsonObject folioTransaccionGenerarResultadoObjeto =  this.transaccionServicio.folioTransaccionGenerar();
		
		logger.info("- folioTransaccionGenerarResultadoObjeto " + folioTransaccionGenerarResultadoObjeto); 
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarResultadoObjeto,"transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(transaccion,"Fol_Transa");
		
		JsonObject datosSaldosClienteConsultar = new JsonObject();
		datosSaldosClienteConsultar.addProperty("Cue_Client", cueClient);
		datosSaldosClienteConsultar.addProperty("Usu_Numero", usuNumero);
		datosSaldosClienteConsultar.addProperty("Bit_DireIP", bitDireIp );
		datosSaldosClienteConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject saldosClienteConsultarResultadoObjecto = this.saldoServicio.saldosClienteConsultar(datosSaldosClienteConsultar);
		
		logger.info(">>>>>>>>>saldosClienteConsultarResultadoObjecto " + saldosClienteConsultarResultadoObjecto);
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", cueClient);
		datosBitacora.addProperty("Bit_DireIP", bitDireIp);
		datosBitacora.addProperty("Bit_TipOpe", CuentasListadoBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);
		
		JsonObject bitacoraCreacionResultadoObjeto = bitacoraServicio.creacionBitacora(datosBitacora);
		
		logger.info("- bitacoraCreacionResultadoObjeto " + bitacoraCreacionResultadoObjeto);
		
		JsonObject cuenta = Utilerias.obtenerJsonObjectPropiedad(saldosClienteConsultarResultadoObjecto, "cuenta");
		
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
			String salCuenta = Utilerias.obtenerStringPropiedad(saldo, "Sal_Cuenta");
			String salCLAVE = Utilerias.obtenerStringPropiedad(saldo, "Sal_CLABE");
			String tipDescri = Utilerias.obtenerStringPropiedad(saldo, "Tip_Descri");
			String monDescri = Utilerias.obtenerStringPropiedad(saldo, "Mon_Descri");
			Double salDispon = Utilerias.obtenerDoublePropiedad(saldo, "Sal_Dispon");
			
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
		datosClienteConsultar.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		
		JsonObject clienteConsultarResultadoObjecto = this.clienteServicio.clienteConsultar(datosClienteConsultar);
		
		logger.info("clienteConsultarResultadoObjecto " + clienteConsultarResultadoObjecto);
		
		JsonObject cliente = Utilerias.obtenerJsonObjectPropiedad(clienteConsultarResultadoObjecto, "cliente");
		
		String cliComple = Utilerias.obtenerStringPropiedad(cliente, "Cli_Numero");
		String cliRFC = Utilerias.obtenerStringPropiedad(cliente, "Cli_RFC").trim();
		String cliTelefo = Utilerias.obtenerStringPropiedad(cliente,"Cli_Telefo");
		String cliCalle = Utilerias.obtenerStringPropiedad(cliente,"Cli_Calle");
		String cliCalNum = Utilerias.obtenerStringPropiedad(cliente,"Cli_CalNum");
		String cliCodPos = Utilerias.obtenerStringPropiedad(cliente,"Cli_CodPos");
		String cliColoni = Utilerias.obtenerStringPropiedad(cliente,"Cli_Coloni");
		String locNombre = Utilerias.obtenerStringPropiedad(cliente,"Loc_Nombre");
		String entNombre = Utilerias.obtenerStringPropiedad(cliente,"Ent_Nombre");
		String paiNombre = Utilerias.obtenerStringPropiedad(cliente,"Pai_Nombre");
		
		String cpCliDir = Utilerias.concatenar(cliCalle, cliCalNum, cliCodPos, cliColoni, locNombre, entNombre, paiNombre);
		
		JsonObject datosCuentasCliente = new JsonObject();
		datosCuentasCliente.addProperty("cliNumero", cliNumero);
		datosCuentasCliente.addProperty("cliComple", cliComple);
		datosCuentasCliente.addProperty("cliRFC", cliRFC);
		datosCuentasCliente.addProperty("cliTelefo", cliTelefo);
		datosCuentasCliente.addProperty("cpCliDir", cpCliDir);
		datosCuentasCliente.add("cuentas", saldosResult);

		JsonObject cuentasCliente = new JsonObject();
		cuentasCliente.add("cuentasCliente", datosCuentasCliente);
		logger.info("CTRL: Terminando cuentasListado metodo");
		return Response
				.ok(cuentasCliente.toString(), MediaType.APPLICATION_JSON)
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
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken); 
		
		String usuNumero = principalResultadoObjecto.has("usuNumero") ? principalResultadoObjecto.get("usuNumero").getAsString() : null;
		
		logger.info(">>>>>>>>>principalResultadoObjecto: " + principalResultadoObjecto);
		logger.info(">>>>>X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		logger.info(">>>>>fechaSis " + fechaSis);
		
		JsonObject folioTransaccionGenerarResultadoObjeto =  this.transaccionServicio.folioTransaccionGenerar();
		
		logger.info("- folioTransaccionGenerarResultadoObjeto " + folioTransaccionGenerarResultadoObjeto); 
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarResultadoObjeto,"transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(transaccion,"Fol_Transa");
		
		JsonObject datosMovimientos = new JsonObject();
		datosMovimientos.addProperty("Cue_Numero", salCuenta);
		datosMovimientos.addProperty("Fec_Inicia", fecIni);
		datosMovimientos.addProperty("Fec_Final", fecFin);
		datosMovimientos.addProperty("FechaSis", fechaSis);
		
		JsonObject movimientosListadoResultadoObjeto = this.movimientosServicio.movimientosListado(datosMovimientos);
		
		logger.info("- movimientosListadoResultadoObjeto " + movimientosListadoResultadoObjeto);
		
		JsonObject cuenta = Utilerias.obtenerJsonObjectPropiedad(movimientosListadoResultadoObjeto , "cuenta");
		
		JsonArray movimientosResultado = new JsonArray();
		
		JsonArray movimientos = new JsonArray();
		
		if(cuenta.has("movimientos") && cuenta.get("movimientos").isJsonObject()) {
			JsonObject movimiento = Utilerias.obtenerJsonObjectPropiedad(cuenta, "movimientos");
			if(movimiento != null)
				movimientos.add(movimiento);
		}
		if(cuenta.has("movimientos") && cuenta.get("movimientos").isJsonArray()) {
			movimientos = Utilerias.obtenerJsonArrayPropiedad(cuenta, "movimientos");
		}
		
		logger.info("- movimientos " + movimientos);
		
		for(int i = 0; i < movimientos.size(); i++) {
			JsonObject movimiento = (JsonObject) movimientos.get(i);
			String movDescri = Utilerias.obtenerStringPropiedad(movimiento, "Mov_Descri").trim();
			String fechaVal = Utilerias.obtenerStringPropiedad(movimiento, "Fecha_Val");
			Float movCantid = Utilerias.obtenerFloatPropiedad(movimiento, "Mov_Cantid");
			Integer movNatura = Utilerias.obtenerIntPropiedad(movimiento, "Mov_Natura");
			
			logger.info("- fechaVal " + fechaVal);
			
			if (fechaVal != null && !fechaVal.isEmpty()) {
				SimpleDateFormat sdfOrigen = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat sdfDestino = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date fecha = sdfOrigen.parse(fechaVal);
					fechaVal = sdfDestino.format(fecha);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
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
		datosBitacora.addProperty("Bit_TipOpe", MovimientosListadoBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);
		
		JsonObject bitacoraCreacionResultadoObjeto = bitacoraServicio.creacionBitacora(datosBitacora);
		
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
		
		JsonObject principal = Utilerias.obtenerPrincipal(bearerToken);	
		logger.info("- principal " + principal);
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTransaccionResultado " + folioTransaccionResultado );
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion");
		
		JsonObject datosCorreoMovimientos = Utilerias.obtenerJsonObjectPropiedad(datosEnvioCorreoMovimientos, "enviaCorreoMovimientos");
		logger.info("- datosCorreoMovimientos " + datosCorreoMovimientos);
		
		String cpRSAToken = Utilerias.obtenerStringPropiedad(datosCorreoMovimientos, "cpRSAToken");
		logger.info("- claveRSA " + cpRSAToken);

		/**
		 * Se pone la vairable folTok en duro para fines de prueba
		 * normalemente se extraeria del principal con la utileria
		 * Utilerias.getStringProperty(principal, "usuFolTok");
		 */
		String folTok = "0416218854";
		logger.info("- folTok " + folTok);

		String bitUsuari = Utilerias.obtenerStringPropiedad(principal, "usuNumero");
		logger.info("- bitUsuari " + bitUsuari);

		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");

		StringBuilder scriptName = new StringBuilder()
				.append(CuentasCtrl.class.getName())
				.append(".movimientosRegistro");
		
		this.tokenServicio.validarTokenOperacion(folTok, cpRSAToken, bitUsuari, numTransac, scriptName.toString());

		String fechaSis = Utilerias.obtenerFechaSis();
		String bitPriRef = Utilerias.obtenerStringPropiedad(principal, "usuClient");
		String bitDireIp = solicitud.getHeader("X-Forwarded-For") != null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		JsonObject datosBitacoraCreacion = new JsonObject();
		datosBitacoraCreacion.addProperty("Bit_Usuari", bitUsuari);
		datosBitacoraCreacion.addProperty("Bit_Fecha", fechaSis);
		datosBitacoraCreacion.addProperty("Bit_PriRef", bitPriRef);
		datosBitacoraCreacion.addProperty("Bit_DireIP", bitDireIp);
		datosBitacoraCreacion.addProperty("Bit_TipOpe", MovimientosRegistroBitacoraCreacionOpBitTipOpe);
		datosBitacoraCreacion.addProperty("NumTransac", numTransac);
		datosBitacoraCreacion.addProperty("FechaSis", fechaSis);
		
		this.bitacoraServicio.creacionBitacora(datosBitacoraCreacion);
		
		String anio = Utilerias.obtenerStringPropiedad(datosCorreoMovimientos, "cpAnio");
		String mes = Utilerias.obtenerStringPropiedad(datosCorreoMovimientos, "cpMes");
		String cliNumero = Utilerias.obtenerStringPropiedad(datosCorreoMovimientos, "cliNumero");
		
		logger.info("- anio " + anio);
		logger.info("- mes " + mes);
		logger.info("- cliNumero " + cliNumero);
		
		this.soapService.movimientosEnvioCorreo(anio, mes, cliNumero);
		logger.info("CTRL: Terminando movimientosRegistro metodo");
		
		
	}
}
