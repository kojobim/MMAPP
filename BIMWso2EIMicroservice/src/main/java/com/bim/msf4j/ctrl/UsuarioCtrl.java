package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.service.AvisoPrivacidadServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/usuario")
public class UsuarioCtrl extends BimBaseCtrl {

	private static Logger logger = Logger.getLogger(UsuarioCtrl.class);
	
	private CuentaDestinoServicio cuentaDestinoServicio;
	private TransaccionServicio transaccionServicio;
	private AvisoPrivacidadServicio avisoPrivacidadServicio;
	
	public UsuarioCtrl() {
		super();
	
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.avisoPrivacidadServicio = new AvisoPrivacidadServicio();
	}

	@Path("/cuentas-destino-nacionales")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasDestinoNacionalesListado(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando cuentasDestinoNacionalesListado metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuAdm = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuUsuAdm");
		logger.info("- usuAdm " + usuAdm);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNumero");
		logger.info("- usuNumero " + usuNumero);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		logger.info("- fechaSis " + fechaSis);
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuAdm);
		datosCuentaDestinoSPEI.addProperty("Cds_Usuari", usuNumero);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		JsonObject cuentasDestinoSPEIRespuesta = this.cuentaDestinoServicio.cuentaDestinoSPEIConsultar(datosCuentaDestinoSPEI );
		logger.info("- cuentasDestinoSPEIRespuesta " + cuentasDestinoSPEIRespuesta);
		
		JsonObject cuentasDestinoObjeto = Utilerias.obtenerJsonObjectPropiedad(cuentasDestinoSPEIRespuesta, "cuentasDestino");
		JsonArray cuentaDestinoArray = Utilerias.obtenerJsonArrayPropiedad(cuentasDestinoObjeto, "cuentaDestino");
		
		JsonArray cuentasDestinoResultado = new JsonArray();
		
		for(JsonElement cuentaDestinoElemento : cuentaDestinoArray) {
			JsonObject cuentaDestinoElementoObjeto = (JsonObject) cuentaDestinoElemento;
			JsonObject cuentaDestinoResultado = new JsonObject();
			cuentaDestinoResultado.addProperty("cdsCLABE", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_CLABE"));
			cuentaDestinoResultado.addProperty("cdsConsec", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_Consec"));
			cuentaDestinoResultado.addProperty("cdsDesCue", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_DesCue"));
			cuentasDestinoResultado.add(cuentaDestinoResultado);
		}
		
		JsonObject cuentasDestino = new JsonObject();
		cuentasDestino.add("cuentasDestino", cuentasDestinoResultado);
		logger.info("- cuentasDestino " + cuentasDestino);
		logger.info("CTRL: Finalizando cuentasDestinoNacionalesListado metodo");
		return Response.ok(cuentasDestino.toString(),MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("/aviso-privacidad")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response verificarAvisoPrivacidad(@Context Request solicitud) {
		logger.info("CTRL: Comenzando verificarAvisoPrivacidad metodo...");
		
		logger.info("Authorization " + solicitud.getHeader("Authorization"));

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principal " + principalResultadoObjeto);

		JsonObject folioTransaccionGenerarResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarResultadoObjeto,"transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(transaccion,"Fol_Transa");
		logger.info("- folTransa " + folTransa);

		String fechaSis = Utilerias.obtenerFechaSis();
		
		String usuNumero = principalResultadoObjeto.get("usuNumero").getAsString();
		
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Usu_Numero", usuNumero);
		datosAvisoPrivacidad.addProperty("NumTransac", folTransa);
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		
		JsonObject avisoPrivacidadVerificarObjeto = this.avisoPrivacidadServicio.avisoPrivacidadVerificar(datosAvisoPrivacidad);
		logger.info("- avisoPrivacidadVerificarObjeto " + avisoPrivacidadVerificarObjeto);
		
		JsonObject avisoPrivacidad = Utilerias.obtenerJsonObjectPropiedad(avisoPrivacidadVerificarObjeto, "avisoPrivacidad");
		
		Boolean usuAceAvi = Boolean.parseBoolean(Utilerias.obtenerStringPropiedad(avisoPrivacidad,"Usu_AceAvi"));
		logger.info("- usuAceAvi " + usuAceAvi);

		JsonObject avisoPrivacidadResultadoObjeto = new JsonObject();
		avisoPrivacidadResultadoObjeto.addProperty("cpAvisoAceptado", usuAceAvi);
		
		JsonObject avisoPrivacidadAceptado = new JsonObject();
		avisoPrivacidadAceptado.add("avisoPrivacidadAceptado", avisoPrivacidadResultadoObjeto);
		logger.info("CTRL: Finalizando verificarAvisoPrivacidad metodo");
		return Response
				.ok(avisoPrivacidadAceptado.toString(), MediaType.APPLICATION_JSON)
				.build();	
	}
	
}
