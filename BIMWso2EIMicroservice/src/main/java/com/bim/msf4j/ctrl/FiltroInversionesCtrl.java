package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

import com.bim.msf4j.commons.HttpClientUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/filtroInversiones")
public class FiltroInversionesCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(FiltroInversionesCtrl.class);

	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject filtroInversiones(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando filtroInversiones metodo");
		String mensaje = HttpClientUtils.getStringContent(solicitud);
		logger.info(">>>> mensaje" + mensaje);
		JsonParser jsonParser = new JsonParser();
		JsonObject objInversiones = (JsonObject) jsonParser.parse(mensaje);
		logger.info(">>>> objInversiones" + objInversiones);

		JsonArray inversiones = objInversiones.getAsJsonArray("inversion");
		logger.info(">>>> for inversiones" + inversiones);
		JsonObject itemAI = null;
		for(int i = 0; i<inversiones.size(); i++) {
			logger.info(">>>> for inversiones" + i);
			itemAI = (JsonObject) inversiones.get(i);
			if(itemAI.has("fotDescri")) {
				itemAI.get("fotDescri").getAsString();
			} else {
				itemAI.addProperty("fotDescri", "PAGARE");
			}		
		}
		
		JsonObject objetoCategorias = new JsonObject();
		logger.info(">>>> objetoCategorias 1 >>>>>>>>>>> " + objetoCategorias);
		JsonArray categorias = objetoCategorias.getAsJsonArray("categorias");
		objetoCategorias.add("categorias", categorias);
		logger.info(">>>> objetoCategorias 2 >>>>>>>>>>> " + objetoCategorias);
		objetoCategorias.addProperty("totalInv", inversiones.size());
		objetoCategorias.addProperty("totalinvCantid", inversiones.size());
		logger.info(">>>> objetoCategorias 3 >>>>>>>>>>> " + objetoCategorias);
		objetoCategorias.add("inversiones", objInversiones);
		logger.info(">>>> objetoCategorias 4 >>>>>>>>>>> " + objetoCategorias);
		
		logger.info("CTRL: Termino filtroInversiones metodo");
		return objetoCategorias;
	}
}
