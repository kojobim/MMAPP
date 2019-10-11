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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
		JsonObject objInversiones = new Gson().fromJson(mensaje, JsonObject.class);
		JsonArray inversiones = objInversiones.getAsJsonArray("inversion");
		logger.info(">>>> for inversiones" + inversiones);
		
		int cpTotalInvCantidP = 0, cpTotalInvCantidV = 0, cpTotalInvCantidC = 0, cpTotalInvCantidF = 0;
		

		JsonObject categoriaFija = new JsonObject();
		categoriaFija.addProperty("categoria", "FIJA");
		categoriaFija.add("inversiones", new JsonArray());

		JsonObject categoriaValor = new JsonObject();
		categoriaValor.addProperty("categoria", "VALOR");
		categoriaValor.add("inversiones", new JsonArray());
		
		JsonObject categoriaCede = new JsonObject();
		categoriaCede.addProperty("categoria", "CEDE_RI");
		categoriaCede.add("inversiones", new JsonArray());

		JsonObject categoriaPagare = new JsonObject();
		categoriaPagare.addProperty("categoria", "PAGARE");
		categoriaPagare.add("inversiones", new JsonArray());
		
		for(JsonElement element : inversiones) {
			JsonObject objetoItem = new JsonObject();
			JsonObject itemAI  = (JsonObject) element;
			
			if(itemAI.has("invTipo") && itemAI.get("invTipo").getAsString().equals("P"))
				continue;
			
			if(!itemAI.has("fotDescri")) 
				itemAI.addProperty("fotDescri", "PAGARE");

			int cantidad = itemAI.get("invCantid").getAsInt();
			
						
			String invNumero = itemAI.get("invNumero").getAsString();
			logger.info("invNumero: " + invNumero);
			int invCantid = itemAI.get("invCantid").getAsInt();
			logger.info("item2: " + invCantid);
			String invFecVen = itemAI.get("invFecVen").getAsString();
			logger.info("item3: " + invFecVen);
			
			Boolean cpRenInv = Utilerias.calcularVencimiento(invFecVen);

			objetoItem.addProperty("invNumero", invNumero);
			objetoItem.addProperty("invCantid", invCantid);
			objetoItem.addProperty("invFecVen", invFecVen);
			objetoItem.addProperty("cpRenInv", cpRenInv);

			logger.info("***** objetoItems" + objetoItem);
			switch (itemAI.get("fotDescri").getAsString()) {
				case "FIJA":
					logger.info("*********FIJA");
					cpTotalInvCantidF += cantidad;
					categoriaFija.get("inversiones").getAsJsonArray().add(objetoItem);
					break;
				case "VALOR":
					logger.info("*********VALOR");
					cpTotalInvCantidV += cantidad;
					categoriaValor.get("inversiones").getAsJsonArray().add(objetoItem);
					logger.info("***** objetoItems" + objetoItem);
					break;
				case "CEDE_RI":
					logger.info("*********CEDE_RI");
					cpTotalInvCantidC += cantidad;
					categoriaCede.get("inversiones").getAsJsonArray().add(objetoItem);
					break;
				case "PAGARE":
					logger.info("*********PAGARE");
					cpTotalInvCantidP += cantidad;
					categoriaPagare.get("inversiones").getAsJsonArray().add(objetoItem);
					break;
				default:
					break;
			}
		}

		int cpTotalInvP = categoriaPagare.get("inversiones").getAsJsonArray().size(); 
		int cpTotalInvV = categoriaValor.get("inversiones").getAsJsonArray().size(); 
		int cpTotalInvF = categoriaFija.get("inversiones").getAsJsonArray().size();
		int cpTotalInvC = categoriaCede.get("inversiones").getAsJsonArray().size();
		
		categoriaFija.addProperty("cpTotalInvCantid", cpTotalInvCantidF);
		categoriaFija.addProperty("cpTotalInv", cpTotalInvF);
		categoriaValor.addProperty("cpTotalInvCantid", cpTotalInvCantidV);
		categoriaValor.addProperty("cpTotalInv", cpTotalInvV);
		categoriaCede.addProperty("cpTotalInvCantid", cpTotalInvCantidC);
		categoriaCede.addProperty("cpTotalInv", cpTotalInvC);
		categoriaPagare.addProperty("cpTotalInvCantid", cpTotalInvCantidP);
		categoriaPagare.addProperty("cpTotalInv", cpTotalInvP);
		
		JsonArray arrayCategorias = new JsonArray();
		arrayCategorias.add(categoriaFija);
		arrayCategorias.add(categoriaValor);
		arrayCategorias.add(categoriaPagare);
		arrayCategorias.add(categoriaCede);
		
		JsonObject objetoCategorias = new JsonObject();
		objetoCategorias.add("categorias", arrayCategorias);
		
		logger.info("&&&&&&&&CATEGORIAS&&&&&&&&&&&  " + objetoCategorias);
		logger.info("CTRL: Termino filtroInversiones metodo");
		return objetoCategorias;
	}
}
