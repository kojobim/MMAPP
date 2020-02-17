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
public class TasaServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(TasaServicioTest.class);
	
	private static TasaServicio tasaServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		tasaServicio = new TasaServicio();
	}
	
	@Test
	public void tasaClienteConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando tasaClienteConsultarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosTasaCliente = new JsonObject();
		datosTasaCliente.addProperty("Cli_Numero", "00193500");
		datosTasaCliente.addProperty("Inv_Cantid", 509000);
		datosTasaCliente.addProperty("Cli_Tipo", "2");
		datosTasaCliente.addProperty("Plazo", 1);
		datosTasaCliente.addProperty("Inv_FecVen", "2019-09-25 00:00:00");
		datosTasaCliente.addProperty("NumTransac", "42623902");
		datosTasaCliente.addProperty("FechaSis", fechaSis);
		
		/*
		 *	Mock
		 *	String json = "{\"tasaCliente\":{\"TasInv\":\"4.4000\",\"Inv_GruTas\":\"TG\",\"Inv_NuPoGr\":\"\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = tasaServicio.tasaClienteConsultar(datosTasaCliente);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad tasaCliente", resultado.has("tasaCliente"));
		assertTrue("La propiedad tasaCliente no es un JsonObject", resultado.get("tasaCliente").isJsonObject());
		
		JsonObject tasaCliente = Utilerias.obtenerJsonObjectPropiedad(resultado, "tasaCliente");
		
		if(!resultado.get("tasaCliente").isJsonNull()) {
			assertTrue("La propiedad TasInv no se encuentra en tasaCliente", tasaCliente.has("TasInv"));
			assertTrue("La propiedad Inv_GruTas no se encuentra en tasaCliente", tasaCliente.has("Inv_GruTas"));
			assertTrue("La propiedad Inv_NuPoGr no se encuentra en tasaCliente", tasaCliente.has("Inv_NuPoGr"));
		}else 
			assertNotNull("la propiedad tasaCliente es nula", tasaCliente);
		
		logger.info("TEST: Finalizando tasaClienteConsultarTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void tasaMonedaConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando tasaMonedaConsultarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("NumTransac", "");
		datosMoneda.addProperty("FechaSis", fechaSis);

		/*
		 *	Mockup Test
		 *	String json = "{\"tasaMoneda\":{\"Mon_Numero\":\"99\",\"Mon_Descri\":\"UDIS\",\"Mon_Simbol\":\"UDI\",\"Mon_Fecha\":\"2017-05-08T19:00:00.000-05:00\","
		 *		+ "\"Mon_EfeCom\":5.743398,\"Mon_EfeVen\":5.743398,\"Mon_DocCom\":5.743398,\"Mon_DocVen\":5.743398,\"Mon_FixCom\":5.743398,\"Mon_FixVen\":5.743398,"
		 *		+ "\"Mon_Abrevi\":\"UDI\",\"Mon_DesCor\":\"UDIS\",\"Mon_CtaEfe\":\"\",\"Mon_CtaBM\":\"\",\"Mon_CtaSBC\":\"\",\"Mon_CtaRem\":\"\",\"Mon_CieCom\":5.743398,"
		 *		+ "\"Mon_CieVen\":5.743398,\"Mon_SpoCom\":5.743398,\"Mon_SpoVen\":5.743398,\"Mon_EqBaMa\":\" \",\"Mon_OpeCam\":\"N\",\"Mon_CieDia\":0.0,\"Mon_FixVal\":4.682447,"
		 *		+ "\"Mon_DesLeg\":\"UDI\",\"Mon_Tipo\":\"M\",\"Mon_ForMet\":\"  \"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = tasaServicio.tasaMonedaConsultar(datosMoneda);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad tasaMoneda", resultado.has("tasaMoneda"));
		assertTrue("La propiedad tasaMoneda no es un JsonObject", resultado.get("tasaMoneda").isJsonObject());
		
		JsonObject tasaMoneda = Utilerias.obtenerJsonObjectPropiedad(resultado, "tasaMoneda");
		
		if(!resultado.get("tasaMoneda").isJsonNull()) {			
			assertTrue("La propiedad Mon_Numero no se encuentra en tasaMoneda", tasaMoneda.has("Mon_Numero"));
			assertTrue("La propiedad Mon_Descri no se encuentra en tasaMoneda", tasaMoneda.has("Mon_Descri"));
			assertTrue("La propiedad Mon_Simbol no se encuentra en tasaMoneda", tasaMoneda.has("Mon_Simbol"));
			assertTrue("La propiedad Mon_Fecha no se encuentra en tasaMoneda", tasaMoneda.has("Mon_Fecha"));
			assertTrue("La propiedad Mon_EfeCom no se encuentra en tasaMoneda", tasaMoneda.has("Mon_EfeCom"));
			assertTrue("La propiedad Mon_EfeVen no se encuentra en tasaMoneda", tasaMoneda.has("Mon_EfeVen"));
			assertTrue("La propiedad Mon_DocCom no se encuentra en tasaMoneda", tasaMoneda.has("Mon_DocCom"));
			assertTrue("La propiedad Mon_DocVen no se encuentra en tasaMoneda", tasaMoneda.has("Mon_DocVen"));
			assertTrue("La propiedad Mon_FixCom no se encuentra en tasaMoneda", tasaMoneda.has("Mon_FixCom"));
			assertTrue("La propiedad Mon_FixVen no se encuentra en tasaMoneda", tasaMoneda.has("Mon_FixVen"));
			assertTrue("La propiedad Mon_Abrevi no se encuentra en tasaMoneda", tasaMoneda.has("Mon_Abrevi"));
			assertTrue("La propiedad Mon_DesCor no se encuentra en tasaMoneda", tasaMoneda.has("Mon_DesCor"));
			assertTrue("La propiedad Mon_CtaEfe no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CtaEfe"));
			assertTrue("La propiedad Mon_CtaBM no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CtaBM"));
			assertTrue("La propiedad Mon_CtaSBC no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CtaSBC"));
			assertTrue("La propiedad Mon_CtaRem no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CtaRem"));			
			assertTrue("La propiedad Mon_CieCom no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CieCom"));
			assertTrue("La propiedad Mon_CieVen no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CieVen"));
			assertTrue("La propiedad Mon_SpoCom no se encuentra en tasaMoneda", tasaMoneda.has("Mon_SpoCom"));
			assertTrue("La propiedad Mon_SpoVen no se encuentra en tasaMoneda", tasaMoneda.has("Mon_SpoVen"));
			assertTrue("La propiedad Mon_EqBaMa no se encuentra en tasaMoneda", tasaMoneda.has("Mon_EqBaMa"));
			assertTrue("La propiedad Mon_OpeCam no se encuentra en tasaMoneda", tasaMoneda.has("Mon_OpeCam"));
			assertTrue("La propiedad Mon_CieDia no se encuentra en tasaMoneda", tasaMoneda.has("Mon_CieDia"));
			assertTrue("La propiedad Mon_FixVal no se encuentra en tasaMoneda", tasaMoneda.has("Mon_FixVal"));
			assertTrue("La propiedad Mon_DesLeg no se encuentra en tasaMoneda", tasaMoneda.has("Mon_DesLeg"));
			assertTrue("La propiedad Mon_Tipo no se encuentra en tasaMoneda", tasaMoneda.has("Mon_Tipo"));
			assertTrue("La propiedad Mon_ForMet no se encuentra en tasaMoneda", tasaMoneda.has("Mon_ForMet"));
		}
		else
			assertNotNull("La propiedad tasaMoneda es nula", tasaMoneda);
		
		logger.info("TEST: Finalizando tasaMonedaConsultarTestDeberiaSerExitoso metodo...");
	}

	@Test
	public void tasaGATConsultaCalcularTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando tasaGATConsultaCalcularTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosGAT = new JsonObject();
		datosGAT.addProperty("Inv_Dias", 1);
		datosGAT.addProperty("Inv_TasInt", 4.65);
		datosGAT.addProperty("Cue_MonInv", 509000);
		datosGAT.addProperty("NumTransac", "");
		datosGAT.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * String json = "{\"tasaGAT\":{\"Err_Codigo\":\"000000\",\"Err_Mensaj\":\"GAT Generado\",\"Inv_GAT\":4.76}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = tasaServicio.tasaGATConsultaCalcular(datosGAT);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad tasaGAT", resultado.has("tasaGAT"));
		assertTrue("La propiedad tasaGAT no es un JsonObject", resultado.get("tasaGAT").isJsonObject());
		
		JsonObject tasaGAT = Utilerias.obtenerJsonObjectPropiedad(resultado, "tasaGAT");
		
		if(!resultado.get("tasaGAT").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en tasaGAT", tasaGAT.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en tasaGAT", tasaGAT.has("Err_Mensaj"));
			assertTrue("La propiedad Inv_GAT no se encuentra en tasaGAT", tasaGAT.has("Inv_GAT"));
		} else
			assertNotNull("la propiedad tasaGAT es nula", tasaGAT);
		
		logger.info("TEST: Finalizando tasaGATConsultaCalcularTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void tasaGATRealConsultaCalcularTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando tasaGATRealConsultaCalcularTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosGATReal = new JsonObject();
		datosGATReal.addProperty("Inv_GAT", 4.76);
		datosGATReal.addProperty("NumTransac", "");
		datosGATReal.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * String json = "{\"tasaGATReal\":{\"Err_Codigo\":\"000000\",\"Err_Mensaj\":\"GAT Real Generado\",\"Inv_GATRea\":1.13}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = tasaServicio.tasaGATRealConsultaCalcular(datosGATReal);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad tasaGATReal", resultado.has("tasaGATReal"));
		assertTrue("La propiedad tasaGATReal no es un JsonObject", resultado.get("tasaGATReal").isJsonObject());
		
		JsonObject tasaGATReal = Utilerias.obtenerJsonObjectPropiedad(resultado, "tasaGATReal");
		
		if(!resultado.get("tasaGATReal").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en tasaGATReal", tasaGATReal.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en tasaGATReal", tasaGATReal.has("Err_Mensaj"));
			assertTrue("La propiedad Inv_GATRea no se encuentra en tasaGATReal", tasaGATReal.has("Inv_GATRea"));
		} else
			assertNotNull("la propiedad tasaGATReal es nula", tasaGATReal);
		
		logger.info("TEST: Finalizando tasaGATRealConsultaCalcularTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void tasaGATCedeConsultaCalcularTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando tasaGATCedeConsultaCalcularTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosGATCede = new JsonObject();
		datosGATCede.addProperty("Inv_Dias", 180);
		datosGATCede.addProperty("Inv_TasInt", 7.49);
		datosGATCede.addProperty("Cue_MonInv", 6000);
		datosGATCede.addProperty("FechaSis", fechaSis);
		
		JsonObject resultado = tasaServicio.tasaGATCedeConsultaCalcular(datosGATCede);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad tasaGATCede", resultado.has("tasaGATCede"));
		assertTrue("La propiedad tasaGATCede no es un JsonObject", resultado.get("tasaGATCede").isJsonObject());
		
		JsonObject tasaGATCede = Utilerias.obtenerJsonObjectPropiedad(resultado, "tasaGATCede");
		
		if(!tasaGATCede.isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en tasaGATCede", tasaGATCede.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en tasaGATCede", tasaGATCede.has("Err_Mensaj"));
			assertTrue("La propiedad Inv_GAT no se encuentra en tasaGATCede", tasaGATCede.has("Inv_GAT"));
		} else
			assertNotNull("la propiedad tasaGATCede es nula", tasaGATCede);
		
		logger.info("TEST: Finalizando tasaGATCedeConsultaCalcularTestDeberiaSerExitoso metodo...");
	}

}
