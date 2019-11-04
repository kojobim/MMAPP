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
import com.google.gson.JsonObject;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.bim.commons.service", "com.bim.commons.utils"})
public class TransaccionServicioTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TransaccionServicioTest.class);
	
	private static TransaccionServicio transaccionServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		transaccionServicio = new TransaccionServicio();
	}
	
	@Test
	public void folioTransaccionGenerarDeberiaSerExitoso() {
		logger.info("TEST: Comenzando folioTransaccionGenerarDeberiaSerExitoso metodo");
	
		/*
		 *	Mockup Test
		 * 	String json = "{\"transaccion\":{\"Fol_Transa\":\"String\"}}";
		 * 	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 * Test
		 */
		JsonObject resultado = transaccionServicio.folioTransaccionGenerar();
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad transaccion", resultado.has("transaccion"));
		assertTrue("La propiedad transaccion no es un JsonObject", resultado.get("transaccion").isJsonObject());
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(resultado, "transaccion");
		
		assertNotNull("transaccion es nulo", transaccion);
		
		assertTrue("La propiedad Fol_Transa no se encuentra en transaccion", transaccion.has("Fol_Transa"));

		logger.info("TEST: Finalizando folioTransaccionGenerarDeberiaSerExitoso metodo");
	}
}
