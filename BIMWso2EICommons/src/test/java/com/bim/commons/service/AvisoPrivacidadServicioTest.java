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
public class AvisoPrivacidadServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(AvisoPrivacidadServicioTest.class);
	
	private static AvisoPrivacidadServicio avisoPrivacidadServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		avisoPrivacidadServicio = new AvisoPrivacidadServicio();
	}
	
	@Test
	public void avisoPrivacidadActualizacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando avisoPrivacidadActualizacionTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Usu_Numero", "000002");
		datosAvisoPrivacidad.addProperty("Usu_Client", "00195421");
		datosAvisoPrivacidad.addProperty("Usu_AceAvi", 1);
		datosAvisoPrivacidad.addProperty("Usu_FecAce", "2019-10-14 09:40:44");
		datosAvisoPrivacidad.addProperty("Usu_FecAct", "2019-10-14 09:40:44");
		datosAvisoPrivacidad.addProperty("NumTransac", "999999");
		datosAvisoPrivacidad.addProperty("Transaccio", "ABE");
		datosAvisoPrivacidad.addProperty("Usuario", "000100");
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		datosAvisoPrivacidad.addProperty("SucOrigen", "001");
		datosAvisoPrivacidad.addProperty("SucDestino", "001");
		datosAvisoPrivacidad.addProperty("Modulo", "BM");
		
		/*
		 *	Mockup Test
		 *	String json = "{\"avisoPrivacidad\":{\"Err_Codigo\":\"String\",\"Err_Mensaj\":\"String\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = avisoPrivacidadServicio.avisoPrivacidadActualizacion(datosAvisoPrivacidad);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad avisoPrivacidad", resultado.has("avisoPrivacidad"));
		assertTrue("La propiedad avisoPrivacidad no es un JsonObject", resultado.get("avisoPrivacidad").isJsonObject());
		
		JsonObject avisoPrivacidad = Utilerias.obtenerJsonObjectPropiedad(resultado, "avisoPrivacidad");
		
		if(!resultado.get("avisoPrivacidad").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en avisoPrivacidad", avisoPrivacidad.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en avisoPrivacidad", avisoPrivacidad.has("Err_Mensaj"));
		}else 
			assertNotNull("avisoPrivacidad es nulo", avisoPrivacidad);
		
		logger.info("TEST: Finalizando avisoPrivacidadActualizacionTestDeberiaSerExitoso metodo...");
	}

}
 