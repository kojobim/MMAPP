package com.bim.commons.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class BitacoraServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(BitacoraServiceTest.class);
	
	private static BitacoraServicio bitacoraServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		bitacoraServicio = new BitacoraServicio();
	}
	
	@Test
	public void creacionBitacoraShouldCreated() {
		logger.info("TEST: Starting creacionBitacoraShouldCreated method");
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", "001844");
		datosBitacora.addProperty("Bit_Fecha", "2019-09-24 13:38:13");
		datosBitacora.addProperty("Bit_TipOpe", "265");
		datosBitacora.addProperty("Bit_PriRef", "001935000013-$509,000.00");
		datosBitacora.addProperty("Bit_SegRef", "1-Renovar capital con interés");
		datosBitacora.addProperty("Bit_DireIP", "127.0.0.1");
		datosBitacora.addProperty("NumTransac", "49646240");
		datosBitacora.addProperty("FechaSis", "2019-09-24 13:38:13");
		JsonObject result =  bitacoraServicio.creacionBitacora(datosBitacora );

		logger.info("- result " + result);
		assertTrue(result.has("REQUEST_STATUS"));
		
		String requestStatus = Utilerias.getStringProperty(result, "REQUEST_STATUS");
		logger.info("- requestStatus " + requestStatus);
		
		assertNotNull(requestStatus);
		assertEquals("SUCCESSFUL", requestStatus.toString());
		
		logger.info("TEST: Starting creacionBitacoraShouldCreated method");
	}
}
