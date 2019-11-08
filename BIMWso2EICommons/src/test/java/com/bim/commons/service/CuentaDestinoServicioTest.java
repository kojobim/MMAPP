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
	
	@Test
	public void cuentaDestinoBIMActualizacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoBIMActualizacionTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoBIMActualizacion = new JsonObject();
		datosCuentaDestinoBIMActualizacion.addProperty("Cdb_UsuAdm", "000149");
		datosCuentaDestinoBIMActualizacion.addProperty("Cdb_Cuenta", "001951620013");
		datosCuentaDestinoBIMActualizacion.addProperty("Cdb_Random", "1F7EA45940C6943AB1652941B28B9F96");
		datosCuentaDestinoBIMActualizacion.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"000000\",\"Err_Mensaj\":\"Cuentas Actualizadas\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoBIMActualizacion(datosCuentaDestinoBIMActualizacion);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentaDestino", resultado.has("cuentaDestino"));
		assertTrue("La propiedad cuentaDestino no es un JsonObject", resultado.get("cuentaDestino").isJsonObject());
		
		JsonObject cuentaDestino = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestino");
		
		if(!resultado.get("cuentaDestino").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en cuentaDestino", cuentaDestino.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en cuentaDestino", cuentaDestino.has("Err_Mensaj"));
		} else
			assertNotNull("la propiedad cuentaDestino es nula", cuentaDestino);
		
		logger.info("TEST: Finalizando cuentaDestinoBIMActualizacionTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void cuentaDestinoSPEIActualizacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoSPEIActualizacionTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoSPEIActualizacion = new JsonObject();
		datosCuentaDestinoSPEIActualizacion.addProperty("Cds_UsuAdm", "000149");
		datosCuentaDestinoSPEIActualizacion.addProperty("Cds_CLABE", "012180001457899904");
		datosCuentaDestinoSPEIActualizacion.addProperty("Cds_Randoms", "0308C7D06B148A94B1652941B28B9F96");
		datosCuentaDestinoSPEIActualizacion.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"000000\",\"Err_Mensaj\":\"Cuentas Actualizadas\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoSPEIActualizacion(datosCuentaDestinoSPEIActualizacion);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentaDestino", resultado.has("cuentaDestino"));
		assertTrue("La propiedad cuentaDestino no es un JsonObject", resultado.get("cuentaDestino").isJsonObject());
		
		JsonObject cuentaDestino = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestino");
		
		if(!resultado.get("cuentaDestino").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en cuentaDestino", cuentaDestino.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en cuentaDestino", cuentaDestino.has("Err_Mensaj"));
		} else
			assertNotNull("la propiedad cuentaDestino es nula", cuentaDestino);
		
		logger.info("TEST: Finalizando cuentaDestinoSPEIActualizacionTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void cuentasEspecialesConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentasEspecialesConsultarTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentasEspecialesConsultar = new JsonObject();
		datosCuentasEspecialesConsultar.addProperty("Ces_Cuenta", "001951620013");
		datosCuentasEspecialesConsultar.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * String json = "{\"cuentasEspeciales\":{\"Cue_Numero\":\"001951620013\",\"Cue_Moneda\":\"01\",\"Cue_Status\":\"A\","
		 *		+ "\"Cue_Saldo\":\"20011.0900\",\"Cue_Tipo\":\"02\",\"Cli_ComOrd\":\"Nombre Ordenado\",\"Cli_Status\":\"S\","
		 *		+ "\"Cli_RFC\":\"GASB760625TF2\",\"Cue_MonDep\":\"14372.2600\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentasEspecialesConsultar(datosCuentasEspecialesConsultar);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentasEspeciales", resultado.has("cuentasEspeciales"));
		assertTrue("La propiedad cuentasEspeciales no es un JsonObject", resultado.get("cuentasEspeciales").isJsonObject());
		
		JsonObject cuentasEspeciales = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentasEspeciales");
		
		if(!resultado.get("cuentasEspeciales").isJsonNull()) {
			assertTrue("La propiedad Cue_Numero no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cue_Numero"));
			assertTrue("La propiedad Cue_Moneda no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cue_Moneda"));
			assertTrue("La propiedad Cue_Status no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cue_Status"));
			assertTrue("La propiedad Cue_Saldo no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cue_Saldo"));
			assertTrue("La propiedad Cue_Tipo no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cue_Tipo"));
			assertTrue("La propiedad Cli_ComOrd no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cli_ComOrd"));
			assertTrue("La propiedad Cli_Status no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cli_Status"));
			assertTrue("La propiedad Cli_RFC no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cli_RFC"));
			assertTrue("La propiedad Cue_MonDep no se encuentra en cuentasEspeciales", cuentasEspeciales.has("Cue_MonDep"));
		} else
			assertNotNull("la propiedad cuentasEspeciales es nula", cuentasEspeciales);
		
		logger.info("TEST: Finalizando cuentasEspecialesConsultarTestDeberiaSerExitoso metodo...");
	}

}
