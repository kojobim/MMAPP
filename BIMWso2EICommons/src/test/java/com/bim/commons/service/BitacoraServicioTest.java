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
public class BitacoraServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(BitacoraServicioTest.class);
	
	private static BitacoraServicio bitacoraServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		bitacoraServicio = new BitacoraServicio();
	}
	
	@Test
	public void creacionBitacoraDeberiaCrear() {
		logger.info("TEST: Empezando creacionBitacoraShouldCreated metodo");
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", "001844");
		datosBitacora.addProperty("Bit_Fecha", "2019-09-24 13:38:13");
		datosBitacora.addProperty("Bit_TipOpe", "265");
		datosBitacora.addProperty("Bit_PriRef", "001935000013-$509,000.00");
		datosBitacora.addProperty("Bit_SegRef", "1-Renovar capital con interés");
		datosBitacora.addProperty("Bit_DireIP", "127.0.0.1");
		datosBitacora.addProperty("NumTransac", "49646240");
		datosBitacora.addProperty("FechaSis", "2019-09-24 13:38:13");
		JsonObject resultado =  bitacoraServicio.creacionBitacora(datosBitacora );

		logger.info("- resultado " + resultado);
		assertTrue(resultado.has("REQUEST_STATUS"));
		
		String statusSolicitud = Utilerias.obtenerStringPropiedad(resultado, "REQUEST_STATUS");
		logger.info("- requestStatus " + statusSolicitud);
		
		assertNotNull(statusSolicitud);
		assertEquals("SUCCESSFUL", statusSolicitud.toString());

		logger.info("TEST: Terminando creacionBitacoraShouldCreated metodo");
	}
}
