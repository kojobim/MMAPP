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

import com.bim.msf4j.commons.HttpClientUtils;
import com.bim.msf4j.commons.Utilerias;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Path("/paginado")
public class PaginadoCtrl {

	private static final Logger logger = Logger.getLogger(PaginadoCtrl.class);
	
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> paginado(@Context final Request solicitud, @QueryParam("pagina") int pagina, @QueryParam("porPagina") int porPagina) {
		logger.info("CTRL: Comenzando paginado metodo");
		String mensaje = HttpClientUtils.getStringContent(solicitud);		
		Type tipoColeccion = new TypeToken<Collection<Object>>(){}.getType();
		Collection<Object> datos = new Gson().fromJson(mensaje, tipoColeccion);		
		List<Object> datosPaginados = Utilerias.paginado((List<Object>)datos, pagina, porPagina);
		logger.info("CTRL: Termino paginado metodo");
		return datosPaginados;
	}
}
