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
public class CuentaDestinoServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(CuentaDestinoServicioTest.class);
	
	private static CuentaDestinoServicio cuentaDestinoServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		cuentaDestinoServicio = new CuentaDestinoServicio();
	}
	
	@Test
	public void cuentaDestinoSPEIActivacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoSPEIActivacionTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoSPEIActivacion = new JsonObject();
		datosCuentaDestinoSPEIActivacion.addProperty("Cds_UsuAdm", "000149");
		datosCuentaDestinoSPEIActivacion.addProperty("NumTransac", "42623902");
		datosCuentaDestinoSPEIActivacion.addProperty("Transaccio", "HRB");
		datosCuentaDestinoSPEIActivacion.addProperty("Usuario", "000100");
		datosCuentaDestinoSPEIActivacion.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoSPEIActivacion.addProperty("SucOrigen", "001");
		datosCuentaDestinoSPEIActivacion.addProperty("SucDestino", "001");
		datosCuentaDestinoSPEIActivacion.addProperty("Modulo", "NB");
		
		/*
		 *	Mockup Test
		 *	String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"000000\",\"Err_Mensaj\":\"Cuentas Actualizadas\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEIActivacion);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuentaDestino", resultado.has("cuentaDestino"));
		assertTrue("La propiedad cuenta no es un JsonObject", resultado.get("cuentaDestino").isJsonObject());
		
		JsonObject cuentaDestino = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestino");
		
		if(!resultado.get("cuentaDestino").isJsonNull()) {
			logger.info(" ----Entra al if", !resultado.get("cuentaDestino").isJsonNull());
			assertTrue("La propiedad Err_Codigo no se encuentra en cuentaDestino", cuentaDestino.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en cuentaDestino", cuentaDestino.has("Err_Mensaj"));
			
		}else 
			assertNotNull("cuentaDestino es nulo", cuentaDestino);
		
		logger.info("TEST: Finalizando cuentaDestinoSPEIActivacionTestDeberiaSerExitoso metodo...");
	}

}
