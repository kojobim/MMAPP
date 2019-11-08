package com.bim.commons.service;

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
public class SPEIServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicioTest.class);
	
	private static SPEIServicio speiServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		speiServicio = new SPEIServicio();
	}
	
	@Test
	public void horariosSPEIConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando horariosSPEIConsultarTestDeberiaSerExitoso metodo...");
		
		JsonObject datosHorariosSPEI = new JsonObject();
		datosHorariosSPEI.addProperty("Hor_HorIni", "1900-01-01 00:00:00");
		datosHorariosSPEI.addProperty("Hor_HorFin", "1900-01-01 00:00:00");
		datosHorariosSPEI.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		
		/**
		 * String mock = "{\"horarioSPEI\":{\"Hor_HorIni\":\"1900-01-01T02:23:24.000-06:36\",\"Hor_HorFin\":\"1900-01-01T10:53:24.000-06:36\"}}";
		 * JsonObject resultado = Utilerias.fromJsonObject(mock);
		 */
		
		JsonObject resultado = speiServicio.horariosSPEIConsultar(datosHorariosSPEI );
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad horarioSPEI", resultado.has("horarioSPEI"));
		assertTrue("La propiedad horarioSPEI no es un JsonObject", resultado.get("horarioSPEI").isJsonObject());
		
		JsonObject horarioSPEI = Utilerias.obtenerJsonObjectPropiedad(resultado, "horarioSPEI");
		
		assertTrue("No viene la propiedad Hor_HorIni", horarioSPEI.has("Hor_HorIni"));
		assertTrue("No viene la propiedad Hor_HorFin", horarioSPEI.has("Hor_HorFin"));
		
		logger.info("TEST: Finalizando horariosSPEIConsultarTestDeberiaSerExitoso metodo...");
		
	}
	
}
