package com.bim.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.log4j.Logger;

public class Filtrado {

	private static final Logger logger = Logger.getLogger(Filtrado.class);
	
    public static JsonObject filtroInversiones(JsonArray inversionArray, Integer page, Integer per_page, String filter_by) {
		logger.info("COMMONS: Comenzando filtroInversiones metodo");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		double cpTotalInvCantidP = 0, cpTotalInvCantidV = 0, cpTotalInvCantidC = 0, cpTotalInvCantidF = 0;
		
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
		
		JsonArray categoriaFijaArray = new JsonArray();
		JsonArray categoriaValorArray = new JsonArray();
		JsonArray categoriaPagareArray = new JsonArray();
		JsonArray categoriaCedeArray = new JsonArray();
		
		for(JsonElement inversion : inversionArray) {
			JsonObject elementosObjeto = new JsonObject();
			JsonObject elemento  = (JsonObject) inversion;
			
			if(elemento.has("Inv_Tipo") && elemento.get("Inv_Tipo").getAsString().equals("P"))
				continue;
			
			if(!elemento.has("Fot_Descri")) 
				elemento.addProperty("Fot_Descri", "PAGARE");
						
			String invNumero = elemento.get("Inv_Numero").getAsString();
			double invCantid = elemento.get("Inv_Cantid").getAsDouble();
			if(!elemento.has("Inv_FecVen") || elemento.get("Inv_FecVen").isJsonNull())
				continue;
			
			Boolean cpRenInv = elementosObjeto.has("cpRenInv") ? elementosObjeto.get("cpRenInv").getAsBoolean() : false;
			if(filter_by != null && !filter_by.isEmpty() && filter_by.equals("PROXIMOS_VENCIMIENTOS") && !cpRenInv)
				continue;
			
			elementosObjeto.addProperty("invNumero", invNumero);
			elementosObjeto.addProperty("invCantid", invCantid);
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String invFecVen = null;
			if(elemento.has("Inv_FecVen"))
				invFecVen = elemento.get("Inv_FecVen").getAsString();
			Date fecha1 = new Date();
			if(invFecVen != null && !invFecVen.contains("Proximo Vencimiento ")) {
				try {
					fecha1 = format.parse(invFecVen);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				elementosObjeto.addProperty("Inv_FecVen", simpleDateFormat.format(fecha1));
			}
			elementosObjeto.addProperty("cpRenInv", cpRenInv);
			
			switch (elemento.get("Fot_Descri").getAsString()) {
				case "FIJA":
					cpTotalInvCantidF += invCantid;
					categoriaFijaArray.add(elementosObjeto);
					break;
				case "VALOR":
					cpTotalInvCantidV += invCantid;
					categoriaValorArray.add(elementosObjeto);
					break;
				case "CEDE_RI":
					cpTotalInvCantidC += invCantid;
					categoriaCedeArray.add(elementosObjeto);
					break;
				case "PAGARE":
					cpTotalInvCantidP += invCantid;
					categoriaPagareArray.add(elementosObjeto);
					break;
				default:
					break;
			}
		}

		int cpTotalInvP = categoriaPagareArray.size(); 
		int cpTotalInvV = categoriaValorArray.size(); 
		int cpTotalInvF = categoriaFijaArray.size();
		int cpTotalInvC = categoriaCedeArray.size();
		
		categoriaFijaArray = Utilerias.paginado(categoriaFijaArray, page.intValue(), per_page.intValue());
		categoriaFija.add("inversiones", categoriaFijaArray);
		categoriaValorArray = Utilerias.paginado(categoriaValorArray, page.intValue(), per_page.intValue());
		categoriaValor.add("inversiones", categoriaValorArray);
		categoriaPagareArray = Utilerias.paginado(categoriaPagareArray, page.intValue(), per_page.intValue());
		categoriaPagare.add("inversiones", categoriaPagareArray);
		categoriaCedeArray = Utilerias.paginado(categoriaCedeArray, page.intValue(), per_page.intValue());
		categoriaCede.add("inversiones", categoriaCedeArray);
		
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
		
		logger.info("COMMONS: Termino filtroInversiones metodo");
		return objetoCategorias;
    }

}
