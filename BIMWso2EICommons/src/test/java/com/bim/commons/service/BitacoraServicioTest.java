package com.bim.commons.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", "000149");
		datosBitacora.addProperty("Bit_Fecha", "2019-05-29 14:24:46");
		datosBitacora.addProperty("Bit_TipOpe", "085");
		datosBitacora.addProperty("Bit_CueOri", "001951710012");
		datosBitacora.addProperty("Bit_CueDes", "00194160015");
		datosBitacora.addProperty("Bit_Monto", 1);
		datosBitacora.addProperty("Bit_PriRef", "Cuenta: 001944160015 BIM");
		datosBitacora.addProperty("Bit_SegRef", "$1.00 Pesos");
		datosBitacora.addProperty("Bit_DireIP", "127.0.0.1");
		datosBitacora.addProperty("NumTransac", "42246896");
		datosBitacora.addProperty("Transaccio", "HOR");
		datosBitacora.addProperty("Usuario", "000100");
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", "001");
		datosBitacora.addProperty("SucDestino", "001");
		datosBitacora.addProperty("Modulo", "NB");
		
		/**
		 *	Mockup Test
		 *	String json = "{\"REQUEST_STATUS\":\"SUCCESSFUL\"}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 *	Test
		 */
		JsonObject resultado = bitacoraServicio.creacionBitacora(datosBitacora );	
		logger.info("- resultado " + resultado);
		assertTrue("No viene la propiedad REQUEST_STATUS", resultado.has("REQUEST_STATUS"));
		
		String statusSolicitud = Utilerias.obtenerStringPropiedad(resultado, "REQUEST_STATUS");
		logger.info("- requestStatus " + statusSolicitud);
		
		assertNotNull(statusSolicitud);
		assertEquals("SUCCESSFUL", statusSolicitud.toString());

		logger.info("TEST: Terminando creacionBitacoraShouldCreated metodo");
	}
}
