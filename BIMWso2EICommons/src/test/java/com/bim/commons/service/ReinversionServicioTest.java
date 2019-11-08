package com.bim.commons.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.bim.commons.service", "com.bim.commons.utils"})
public class ReinversionServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(ReinversionServicioTest.class);
	
	private static ReinversionServicio reinversionServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		reinversionServicio = new ReinversionServicio();
	}
	
	@Test
	public void fechaHabilConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando fechaHabilConsultarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", "2019-09-25 00:00:00");
		datosFechaHabil.addProperty("NumDia", 2);
		datosFechaHabil.addProperty("NumTransac", "");
		datosFechaHabil.addProperty("FechaSis", fechaSis);
		
		/*
		 *	Mockup Test
		 *	String json = "{\"fechaHabil\":{\"Fecha\":\"2019-09-27 00:00:00\",\"Dias\":2}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = reinversionServicio.fechaHabilConsultar(datosFechaHabil);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad fechaHabil", resultado.has("fechaHabil"));
		assertTrue("La propiedad fechaHabil no es un JsonObject", resultado.get("fechaHabil").isJsonObject());
		
		JsonObject fechaHabil = Utilerias.obtenerJsonObjectPropiedad(resultado, "fechaHabil");
		
		if(!resultado.get("fechaHabil").isJsonNull()) {
			assertTrue("La propiedad Fecha no se encuentra en fechaHabil", fechaHabil.has("Fecha"));
			assertTrue("La propiedad Dias no se encuentra en fechaHabil", fechaHabil.has("Dias"));
		}else 
			assertNotNull("La propiedad fechaHabil es nula", fechaHabil);
		
		logger.info("TEST: Finalizando fechaHabilConsultarTestDeberiaSerExitoso metodo...");
	}

}
