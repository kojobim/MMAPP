package com.bim.msf4j.ctrl;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Path("/paginado")
public class PaginadoCtrl {

	private static final Logger logger = Logger.getLogger(PaginadoCtrl.class);
	
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonArray paginado(@Context final Request solicitud, @QueryParam("page") int page, @QueryParam("per_page") int per_page) {
		logger.info("CTRL: Comenzando paginado metodo");
		String mensaje = HttpClientUtils.getStringContent(solicitud);
		Type tipoColeccion = new TypeToken<Collection<JsonObject>>(){}.getType();
		Collection<JsonElement> datos = new Gson().fromJson(mensaje, tipoColeccion);
		List<JsonElement> datosLista = (List<JsonElement>)datos;
		JsonArray datosPaginados = Utilerias.paginado(new Gson().fromJson(datosLista.toString(), JsonArray.class), page, per_page);
		logger.info("CTRL: Termino paginado metodo");
		return datosPaginados;
	}
}
