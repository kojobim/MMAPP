package com.bim.commons.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class InversionesServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(InversionesServicioTest.class);
	
	private static InversionesServicio inversionesServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		inversionesServicio = new InversionesServicio();
	}
	
	@Test
	public void inversionesObtenerTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando inversionesObtenerTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject inversionesObtener = new JsonObject();
		inversionesObtener.addProperty("Inv_Client", "00193500");
		inversionesObtener.addProperty("Inv_Moneda", "01");
		inversionesObtener.addProperty("NumTransac", "");
		inversionesObtener.addProperty("Transaccio", "HMZ");
		inversionesObtener.addProperty("Usuario", "000100");
		inversionesObtener.addProperty("FechaSis", fechaSis);
		inversionesObtener.addProperty("SucOrigen", "001");
		inversionesObtener.addProperty("SucDestino", "001");
		inversionesObtener.addProperty("Modulo", "NB");
		
		JsonObject resultado = inversionesServicio.inversionesObtener(inversionesObtener);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad inversiones", resultado.has("inversiones"));
		assertTrue("La propiedad inversiones no es un JsonObject", resultado.get("inversiones").isJsonObject());
		
		
		logger.info("TEST: Finalizando inversionesObtenerTestDeberiaSerExitoso metodo");
	}
}
