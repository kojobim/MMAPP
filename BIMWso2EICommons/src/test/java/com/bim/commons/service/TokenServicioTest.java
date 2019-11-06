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
public class TokenServicioTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenServicioTest.class);
	
	private static TokenServicio tokenServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		tokenServicio = new TokenServicio();
	}
	
	@Test
	public void tokenVerificarDeberiaSerExitoso() {
		logger.info("TEST: Comenzando tokenVerificarDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosTokenVerificar = new JsonObject();
		datosTokenVerificar.addProperty("Tov_Serie", "0416218853");
		datosTokenVerificar.addProperty("NumTransac", "");
		datosTokenVerificar.addProperty("Transaccio", "JBD");
		datosTokenVerificar.addProperty("Usuario", "000031");
		datosTokenVerificar.addProperty("FechaSis", fechaSis);
		datosTokenVerificar.addProperty("SucOrigen", "799");
		datosTokenVerificar.addProperty("SucDestino", "799");
		datosTokenVerificar.addProperty("Modulo", "NB");
		
		/*
		 *	Mockup Test
		 *	String json = "{\"tokenVerificar\":{\"Tov_FecVen\":\"Date\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = tokenServicio.tokenVerificar(datosTokenVerificar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad tokenVerificar", resultado.has("tokenVerificar"));
		assertTrue("La propiedad tokenVerificar no es un JsonObject", resultado.get("tokenVerificar").isJsonObject());
		
		JsonObject tokenVerificar = Utilerias.obtenerJsonObjectPropiedad(resultado, "tokenVerificar");
		
		if(!resultado.get("tokenVerificar").isJsonNull()) {
			assertNotNull("tokenVerificar es nulo", tokenVerificar);
		}else
			assertTrue("La propiedad Tov_FecVen no se encuentra en tokenVerificar", tokenVerificar.has("Tov_FecVen"));

		logger.info("TEST: Finalizando tokenVerificarDeberiaSerExitoso metodo");
	}
}
