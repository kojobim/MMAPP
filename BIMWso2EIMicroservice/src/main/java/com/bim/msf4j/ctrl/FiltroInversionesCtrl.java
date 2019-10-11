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
		JsonArray inversiones = objInversiones.getAsJsonArray("inversion");
		logger.info(">>>> for inversiones" + inversiones);
		JsonObject itemAI = null;
		JsonArray arrayInverP = new JsonArray();
		JsonArray arrayInverV = new JsonArray();
		JsonArray arrayInverC = new JsonArray();
		JsonArray arrayInverF = new JsonArray();
		
		int cpTotalInvP = 0, cpTotalInvV = 0, cpTotalInvF = 0, cpTotalInvC = 0;
		int cpTotalInvCantidP = 0, cpTotalInvCantidV = 0, cpTotalInvCantidC = 0, cpTotalInvCantidF = 0;
		int cantidad;
		
		for(int i = 0; i<inversiones.size(); i++) {
			logger.info(">>>> for inversiones" + i);
			itemAI = (JsonObject) inversiones.get(i);
			if(itemAI.has("fotDescri")) {
				itemAI.get("fotDescri").getAsString();
			} else {
				itemAI.addProperty("fotDescri", "PAGARE");
			}
			
			cantidad = itemAI.get("invCantid").getAsInt();
									
			switch (itemAI.get("fotDescri").getAsString()) {
			case "FIJA":
				logger.info("*********FIJA");
				cpTotalInvCantidF = cpTotalInvCantidF + cantidad;			
				arrayInverF.add(itemAI);
				cpTotalInvF ++;
				break;
			case "VALOR":
				logger.info("*********VALOR");
				cpTotalInvCantidV = cpTotalInvCantidV + cantidad;
				arrayInverV.add(itemAI);
				cpTotalInvV ++;
				break;
			case "CEDE_RI":
				logger.info("*********CEDE_RI");
				cpTotalInvCantidC = cpTotalInvCantidC + cantidad;
				arrayInverC.add(itemAI);
				cpTotalInvC ++;
				break;
			case "PAGARE":
				logger.info("*********PAGARE");
				cpTotalInvCantidP = cpTotalInvCantidP + cantidad;
				arrayInverP.add(itemAI);
				cpTotalInvP ++;
				break;
			default:
				break;
			}
		}
		
		JsonObject categoriaFija = new JsonObject();
		categoriaFija.addProperty("categoria", "FIJA");
		categoriaFija.addProperty("cpTotalInvCantid", cpTotalInvCantidF);
		categoriaFija.addProperty("cpTotalInv", cpTotalInvF);
		categoriaFija.add("inversiones", arrayInverF);

		JsonObject categoriaValor = new JsonObject();
		categoriaValor.addProperty("categoria", "VALOR");
		categoriaValor.addProperty("cpTotalInvCantid", cpTotalInvCantidV);
		categoriaValor.addProperty("cpTotalInv", cpTotalInvV);
		categoriaValor.add("inversiones", arrayInverV);
		
		JsonObject categoriaCede = new JsonObject();
		categoriaCede.addProperty("categoria", "CEDE_RI");
		categoriaCede.addProperty("cpTotalInvCantid", cpTotalInvCantidC);
		categoriaCede.addProperty("cpTotalInv", cpTotalInvC);
		categoriaCede.add("inversiones", arrayInverC);		

		JsonObject categoriaPagare = new JsonObject();
		categoriaPagare.addProperty("categoria", "PAGARE");
		categoriaPagare.addProperty("cpTotalInvCantid", cpTotalInvCantidP);
		categoriaPagare.addProperty("cpTotalInv", cpTotalInvP);
		categoriaPagare.add("inversiones", arrayInverP);
		
		JsonArray arrayCategorias = new JsonArray();
		arrayCategorias.add(categoriaFija);
		arrayCategorias.add(categoriaValor);
		arrayCategorias.add(categoriaPagare);
		arrayCategorias.add(categoriaCede);
		
		JsonObject objetoCategorias = new JsonObject();
		objetoCategorias.add("categorias", arrayCategorias);

		logger.info("CTRL: Termino filtroInversiones metodo");
		return objetoCategorias;
	}
}
