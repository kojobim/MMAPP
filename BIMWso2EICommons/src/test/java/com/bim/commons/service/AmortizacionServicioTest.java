package com.bim.commons.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
public class AmortizacionServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(AmortizacionServicioTest.class);
	
	private static AmortizacionServicio amortizacionServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		amortizacionServicio = new AmortizacionServicio();
	}
	
	@Test
	public void amortizacionGenerarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando amortizacionGenerarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosAmortizacionGenerar = new JsonObject();
		datosAmortizacionGenerar.addProperty("Ced_Cantid", 6000);
		datosAmortizacionGenerar.addProperty("Ced_Plazo", 180);
		datosAmortizacionGenerar.addProperty("Ced_FecIni", "2020-02-04 00:00:00");
		datosAmortizacionGenerar.addProperty("Ced_FecVen", "2020-08-03 00:00:00");
		datosAmortizacionGenerar.addProperty("Ced_DiaPag", 1);
		datosAmortizacionGenerar.addProperty("Ced_DiPaFe", "2020-03-03 00:00:00");
		datosAmortizacionGenerar.addProperty("Ced_TasBru", 0);
		datosAmortizacionGenerar.addProperty("Ced_TasNet", 0);
		datosAmortizacionGenerar.addProperty("Ced_Produc", "07");
		datosAmortizacionGenerar.addProperty("Transaccio", "A01");
		datosAmortizacionGenerar.addProperty("Usuario", "000100");
		datosAmortizacionGenerar.addProperty("FechaSis", fechaSis);
		datosAmortizacionGenerar.addProperty("SucOrigen", "001");
		datosAmortizacionGenerar.addProperty("SucDestino", "001");
		datosAmortizacionGenerar.addProperty("Modulo", "NB");
		
		JsonObject resultado = amortizacionServicio.amortizacionGenerar(datosAmortizacionGenerar);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad amortizaciones", resultado.has("amortizaciones"));
		assertTrue("La propiedad amortizaciones no es un JsonObject", resultado.get("amortizaciones").isJsonObject());
		
		JsonObject amortizaciones = Utilerias.obtenerJsonObjectPropiedad(resultado, "amortizaciones");
		
		if(!amortizaciones.isJsonNull()) {
			assertTrue("No viene  la propiedad amortizacion", amortizaciones.has("amortizacion"));
			assertTrue("La propiedad amortizacion no es un JsonArray", amortizaciones.get("amortizacion").isJsonArray());
			
			JsonObject amortizacion = Utilerias.obtenerJsonArrayPropiedad(amortizaciones, "amortizacion").get(0).getAsJsonObject();
			
			assertTrue("La propiedad Numero no se encuentra en amortizacion", amortizacion.has("Numero"));
			assertTrue("La propiedad Amo_Numero no se encuentra en amortizacion", amortizacion.has("Amo_Numero"));
			assertTrue("La propiedad Amo_FecIni no se encuentra en amortizacion", amortizacion.has("Amo_FecIni"));
			assertTrue("La propiedad Amo_FecVen no se encuentra en amortizacion", amortizacion.has("Amo_FecVen"));
			assertTrue("La propiedad Amo_Cantid no se encuentra en amortizacion", amortizacion.has("Amo_Cantid"));
			assertTrue("La propiedad Amo_IntBru no se encuentra en amortizacion", amortizacion.has("Amo_IntBru"));
			assertTrue("La propiedad Amo_IntNet no se encuentra en amortizacion", amortizacion.has("Amo_IntNet"));
			assertTrue("La propiedad Amo_ISR no se encuentra en amortizacion", amortizacion.has("Amo_ISR"));
			assertTrue("La propiedad Amo_TasBru no se encuentra en amortizacion", amortizacion.has("Amo_TasBru"));
			assertTrue("La propiedad Amo_TasNet no se encuentra en amortizacion", amortizacion.has("Amo_TasNet"));
			assertTrue("La propiedad Amo_TasISR no se encuentra en amortizacion", amortizacion.has("Amo_TasISR"));
		}else {
			assertNotNull("la propiedad amortizaciones es nula", amortizaciones);
		}
		
		logger.info("TEST: Finalizando amortizacionGenerarTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void amortizacionAltaTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando amortizacionAltaTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosAltaGenerar = new JsonObject();
		datosAltaGenerar.addProperty("Amo_Invers", "001953830066004");
		datosAltaGenerar.addProperty("Amo_Numero", "005");
		datosAltaGenerar.addProperty("Amo_FecIni", "2020-03-31 00:00:00");
		datosAltaGenerar.addProperty("Amo_FecVen", "2020-04-28 00:00:00");
		datosAltaGenerar.addProperty("Amo_Cantid", 0.0000);
		datosAltaGenerar.addProperty("Amo_Tasa", 6.04);
		datosAltaGenerar.addProperty("Amo_TasBru", 7.49);
		datosAltaGenerar.addProperty("Amo_ISR", 14.5);
		datosAltaGenerar.addProperty("NumTransac", "58138428");
		datosAltaGenerar.addProperty("Transaccio", "AMZ");
		datosAltaGenerar.addProperty("Usuario", "000100");
		datosAltaGenerar.addProperty("FechaSis", fechaSis);
		datosAltaGenerar.addProperty("SucOrigen", "001");
		datosAltaGenerar.addProperty("SucDestino", "001");
		datosAltaGenerar.addProperty("Modulo", "NB");
		
		JsonObject resultado = amortizacionServicio.amortizacionAlta(datosAltaGenerar);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad amortizacion", resultado.has("amortizacion"));
		assertTrue("La propiedad amortizacion no es un JsonObject", resultado.get("amortizacion").isJsonObject());
		
		JsonObject amortizacion = Utilerias.obtenerJsonObjectPropiedad(resultado, "amortizacion");
		
		if(!amortizacion.isJsonNull()) {
			assertFalse("Se halló la propiedad Err_Codigo en el objeto amortizacion", amortizacion.has("Err_Codigo"));
			assertFalse("Se halló la propiedad Err_Mensaj en el objeto amortizacion", amortizacion.has("Err_Mensaj"));
		}else {
			assertNotNull("la propiedad amortizacion es nula", amortizacion);
		}
		
		logger.info("TEST: Finalizando amortizacionAltaTestDeberiaSerExitoso metodo...");
	}

}
 