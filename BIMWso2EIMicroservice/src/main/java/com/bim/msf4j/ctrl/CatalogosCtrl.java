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

import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/catalogos")
public class CatalogosCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CatalogosCtrl.class);

	private CuentaDestinoServicio cuentaDestinoServicio;
	
	public CatalogosCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		logger.info("CTRL: Finalizando metodo init...");		
	}
	
	@Path("/bancos")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response catalogoInstitucionesConsultar(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando catalogoInstitucionesConsultar metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principalResultadoObjecto " + principalResultadoObjecto);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosCatalogoInstituciones = new JsonObject();
		datosCatalogoInstituciones.addProperty("FechaSis", fechaSis);
		
		JsonObject catalogoInstitucionesResultadoObjeto = this.cuentaDestinoServicio.catalogoInstitucionesConsultar(datosCatalogoInstituciones);
		logger.info("- catalogoInstitucionesResultadoObjeto: " + catalogoInstitucionesResultadoObjeto);

		JsonObject catalogoInstitucionesObjeto = Utilerias.obtenerJsonObjectPropiedad(catalogoInstitucionesResultadoObjeto, "instituciones");
		JsonArray catalogoInstitucionArray = Utilerias.obtenerJsonArrayPropiedad(catalogoInstitucionesObjeto, "institucion");
		
		JsonArray catalogoInstitucionArrayResultado = new JsonArray();
		for(JsonElement institucionElemento : catalogoInstitucionArray) {
			JsonObject institucionObjeto = (JsonObject) institucionElemento;
			JsonObject institucionObjetoResultado = new JsonObject();
			if(institucionObjeto.has("Ins_Clave")) {
				String insClave = Utilerias.obtenerStringPropiedad(institucionObjeto, "Ins_Clave") == null 
						? null 
						: Utilerias.obtenerStringPropiedad(institucionObjeto, "Ins_Clave").trim();
				institucionObjetoResultado.addProperty("insClave", Integer.parseInt(insClave));
				institucionObjetoResultado.add("insDescri", institucionObjeto.get("Ins_Descri"));
			}
			catalogoInstitucionArrayResultado.add(institucionObjetoResultado);
		}

		JsonObject catalogoInstitucionesResultado = new JsonObject();
		catalogoInstitucionesResultado.add("bancos", catalogoInstitucionArrayResultado);
		logger.info("CTRL: Terminando catalogoInstitucionesConsultar metodo...");
		return Response
				.ok(catalogoInstitucionesResultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
}
