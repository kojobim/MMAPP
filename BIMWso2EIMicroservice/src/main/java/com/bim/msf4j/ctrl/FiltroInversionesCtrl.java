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
import com.bim.msf4j.commons.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/filtroInversiones")
public class FiltroInversionesCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(FiltroInversionesCtrl.class);
	private Utilerias utilerias;
	
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
		JsonArray inversiones = objInversiones.getAsJsonArray("inversion");
		logger.info(">>>> for inversiones" + inversiones);
		JsonObject itemAI = null;
		JsonObject inver = new JsonObject();
		JsonArray arrayInver = new JsonArray();
		
		int cpTotalInvP = 0, cpTotalInvV = 0, cpTotalInvF = 0, cpTotalInvC = 0;
		int cpTotalInvCantidP = 0, cpTotalInvCantidV = 0, cpTotalInvCantidC = 0, cpTotalInvCantidF = 0;
		int cantidad, invCantidad;
		String invNumero, invFecVen, categoria;
		
		for(int i = 0; i<inversiones.size(); i++) {
			logger.info(">>>> for inversiones" + i);
			itemAI = (JsonObject) inversiones.get(i);
			if(itemAI.has("fotDescri")) {
				itemAI.get("fotDescri").getAsString();
			} else {
				itemAI.addProperty("fotDescri", "PAGARE");
			}		
			categoria = itemAI.get("fotDescri").getAsString();
			cantidad = itemAI.get("invCantid").getAsInt();
			invCantidad = itemAI.get("invCantid").getAsInt();
			invNumero = itemAI.get("invNumero").getAsString();
			invFecVen = itemAI.get("invFecVen").getAsString();
			
			inver.addProperty("invCantid", invCantidad);
			inver.addProperty("invNumero", invNumero);
			inver.addProperty("invFecVen", invFecVen);
			
			Boolean inversionVencida = utilerias.calcularVencimiento(invFecVen);
			if(inversionVencida) {
				inversiones.remove(i);
			}
			logger.info("new inversiones" + inversiones);
				
			inver.addProperty("cpRenInv", true);
			
			switch (itemAI.get("fotDescri").getAsString()) {
			case "FIJA":
				logger.info("*********FIJA");
				cpTotalInvCantidF = cpTotalInvCantidF + cantidad;
				cpTotalInvF ++;
				break;
			case "VALOR":
				logger.info("*********VALOR");
				cpTotalInvCantidV = cpTotalInvCantidV + cantidad;
				cpTotalInvV ++;
				break;
			case "CEDE_RI":
				logger.info("*********CEDE_RI");
				cpTotalInvCantidC = cpTotalInvCantidC + cantidad;
				cpTotalInvC ++;
				break;
			case "PAGARE":
				logger.info("*********PAGARE");
				cpTotalInvCantidP = cpTotalInvCantidP + cantidad;
				cpTotalInvP ++;
				break;
			default:
				break;
			}
		}
		
		JsonObject objetoCategorias = new JsonObject();
		JsonArray arrayCategorias = new JsonArray();
		
		// CATEGORIAS
		JsonObject categoriaPagare = new JsonObject();
		JsonObject categoriaFija = new JsonObject();
		JsonObject categoriaValor = new JsonObject();
		JsonObject categoriaCede = new JsonObject();
		
		categoriaFija.addProperty("categoria", "FIJA");
		categoriaFija.addProperty("totalinvCantid", cpTotalInvCantidF);
		categoriaFija.addProperty("totalInv", cpTotalInvF);
		categoriaFija.add("inversiones", arrayInver);

		categoriaValor.addProperty("categoria", "VALOR");
		categoriaValor.addProperty("totalinvCantid", cpTotalInvCantidV);
		categoriaValor.addProperty("totalInv", cpTotalInvV);
		categoriaValor.add("inversiones", arrayInver);
		
		categoriaCede.addProperty("categoria", "CEDE_RI");
		categoriaCede.addProperty("totalinvCantid", cpTotalInvCantidC);
		categoriaCede.addProperty("totalInv", cpTotalInvC);
		categoriaCede.add("inversiones", arrayInver);		

		categoriaPagare.addProperty("categoria", "PAGARE");
		categoriaPagare.addProperty("cpTotalInvCantid", cpTotalInvCantidP);
		categoriaPagare.addProperty("totalInv", cpTotalInvP);
		categoriaPagare.add("inversiones", arrayInver);
		
		arrayCategorias.add(categoriaFija);
		arrayCategorias.add(categoriaValor);
		arrayCategorias.add(categoriaPagare);
		arrayCategorias.add(categoriaCede);
		objetoCategorias.add("categorias", arrayCategorias);

		logger.info("CTRL: Termino filtroInversiones metodo");
		return objetoCategorias;
	}
}
