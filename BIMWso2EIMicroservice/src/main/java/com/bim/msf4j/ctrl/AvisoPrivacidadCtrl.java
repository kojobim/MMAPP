package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.service.AvisoPrivacidadServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/")
public class AvisoPrivacidadCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(AvisoPrivacidadCtrl.class);

	private AvisoPrivacidadServicio avisoPrivacidadServicio;
	private TransaccionServicio transaccionServicio;
	
	public AvisoPrivacidadCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.avisoPrivacidadServicio = new AvisoPrivacidadServicio();
		this.transaccionServicio = new TransaccionServicio();
		logger.info("CTRL: Terminando metodo init...");
	}
	
	@Path("/aviso-privacidad")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerAvisoPrivacidad(@QueryParam("formato") String formato, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerAvisoPrivacidad metodo...");
		
		logger.info("CTRL: Terminando obtenerAvisoPrivacidad metodo");
		return Response.ok(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("usuario/aviso-privacidad")
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
