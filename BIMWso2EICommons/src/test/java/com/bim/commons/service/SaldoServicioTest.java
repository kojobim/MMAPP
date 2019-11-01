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
public class SaldoServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(SaldoServicioTest.class);
	
	private static SaldoServicio saldoServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		saldoServicio = new SaldoServicio();
	}
	
	@Test
	public void saldosClienteConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando saldosClienteConsultarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosSaldosClienteConsultar = new JsonObject();
		datosSaldosClienteConsultar.addProperty("Cue_Client", "00195383");
		datosSaldosClienteConsultar.addProperty("Usu_Numero", "000014");
		datosSaldosClienteConsultar.addProperty("Bit_DireIP", "127.0.0.1");
		datosSaldosClienteConsultar.addProperty("Cue_Numero", "");
		datosSaldosClienteConsultar.addProperty("NumTransac", "");
		datosSaldosClienteConsultar.addProperty("Transaccio", "HMU");
		datosSaldosClienteConsultar.addProperty("Usuario", "000100");
		datosSaldosClienteConsultar.addProperty("FechaSis", fechaSis);
		datosSaldosClienteConsultar.addProperty("SucOrigen", "001");
		datosSaldosClienteConsultar.addProperty("SucDestino", "001");
		datosSaldosClienteConsultar.addProperty("Modulo", "NB");
		
		JsonObject resultado = saldoServicio.saldosClienteConsultar(datosSaldosClienteConsultar);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuenta", resultado.has("cuenta"));
		assertTrue("La propiedad cuenta no es un JsonObject", resultado.get("cuenta").isJsonObject());
		
		JsonObject cuenta = Utilerias.obtenerJsonObjectPropiedad(resultado, "saldos");
		
		assertTrue("No viene la propiedad saldos", cuenta.has("saldos"));
		assertTrue("La propiedad saldos no es un JsonArray", cuenta.get("saldos").isJsonArray());
		
		JsonObject saldosElemento = null;
		if(cuenta.get("saldos").isJsonArray()) {
			JsonArray saldos = Utilerias.obtenerJsonArrayPropiedad(cuenta, "saldos");
			
			assertTrue("La propiedad saldos no tiene elementos", saldos.size() > 0);
			assertTrue("El elemento de saldos no es un JsonObject", saldos.get(0).isJsonObject());
			
			saldosElemento = saldos.get(0).getAsJsonObject();
		}
		
		assertNotNull("El elemento saldosElemento es nulo", saldosElemento);

		assertTrue("La propiedad Sal_Cuenta no se encuentra en movimientos", saldosElemento.has("Sal_Cuenta"));
		assertTrue("La propiedad Mon_Descri no se encuentra en movimientos", saldosElemento.has("Mon_Descri"));
		
		logger.info("TEST: Finalizando saldosClienteConsultarTestDeberiaSerExitoso metodo...");
	}

}
