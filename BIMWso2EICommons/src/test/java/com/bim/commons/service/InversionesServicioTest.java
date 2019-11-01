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
public class InversionesServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(InversionesServicioTest.class);
	
	private static InversionesServicio inversionesServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		inversionesServicio = new InversionesServicio();
	}
	
	@Test
	public void inversionesObtenerTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando inversionesObtenerTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject inversionesObtener = new JsonObject();
		inversionesObtener.addProperty("Inv_Client", "00193500");
		inversionesObtener.addProperty("Inv_Moneda", "01");
		inversionesObtener.addProperty("NumTransac", "");
		inversionesObtener.addProperty("Transaccio", "HMZ");
		inversionesObtener.addProperty("Usuario", "000100");
		inversionesObtener.addProperty("FechaSis", fechaSis);
		inversionesObtener.addProperty("SucOrigen", "001");
		inversionesObtener.addProperty("SucDestino", "001");
		inversionesObtener.addProperty("Modulo", "NB");
		
		JsonObject resultado = inversionesServicio.inversionesObtener(inversionesObtener);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad inversiones", resultado.has("inversiones"));
		assertTrue("La propiedad inversiones no es un JsonObject", resultado.get("inversiones").isJsonObject());
		
		JsonObject inversiones = Utilerias.obtenerJsonObjectPropiedad(resultado, "inversion");
		
		assertTrue("No viene la propiedad inversion", inversiones.has("inversion"));
		assertTrue("La propiedad inversiones no es un JsonArray o un JsonObject", inversiones.get("inversion").isJsonArray() || inversiones.get("inversion").isJsonObject());
		
		JsonObject inversionElemento = null;
		if(inversiones.get("inversion").isJsonArray()) {
			JsonArray inversion = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");
			
			assertTrue("La propiedad inversion no tiene elementos", inversion.size() > 0);
			assertTrue("El elemento de inversion no es un JsonObject", inversion.get(0).isJsonObject());
			
			inversionElemento = inversion.get(0).getAsJsonObject();
		}
		else
			inversionElemento = inversiones.get("inversion").getAsJsonObject();
		
		assertNotNull("El elemento inversion es nulo", inversionElemento);

		assertTrue("La propiedad Inv_Numero no se encuentra en inversion", inversionElemento.has("Inv_Numero"));
		assertTrue("La propiedad Inv_Cantid no se encuentra en inversion", inversionElemento.has("Inv_Cantid"));
		
		logger.info("TEST: Finalizando inversionesObtenerTestDeberiaSerExitoso metodo");
	}
	
	@Test
	public void inversionesPagareNumeroUsuarioObtenerTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando inversionesPagareNumeroUsuarioObtenerTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosInversionesPagare = new JsonObject();
		datosInversionesPagare.addProperty("Inv_Numero", "001935000030002");
		datosInversionesPagare.addProperty("Inv_Usuari", "");
		datosInversionesPagare.addProperty("Tip_Consul", "C1");
		datosInversionesPagare.addProperty("NumTransac", "");
		datosInversionesPagare.addProperty("Transaccio", "HWE");
		datosInversionesPagare.addProperty("Usuario", "000100");
		datosInversionesPagare.addProperty("FechaSis", fechaSis);
		datosInversionesPagare.addProperty("SucOrigen", "001");
		datosInversionesPagare.addProperty("SucDestino", "001");
		datosInversionesPagare.addProperty("Modulo", "NB");
		
		JsonObject resultado = inversionesServicio.inversionesPagareNumeroUsuarioObtener(datosInversionesPagare);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad inversiones", resultado.has("inversiones"));
		assertTrue("La propiedad inversiones no es un JsonObject", resultado.get("inversiones").isJsonObject());
		
		JsonObject inversiones = Utilerias.obtenerJsonObjectPropiedad(resultado, "inversion");
		
		assertTrue("No viene la propiedad inversion", inversiones.has("inversion"));
		assertTrue("La propiedad inversiones no es un JsonArray", inversiones.get("inversion").isJsonArray());
		
		JsonObject inversionElemento = null;
		if(inversiones.get("inversion").isJsonArray()) {
			JsonArray inversion = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");
			
			assertTrue("La propiedad inversion no tiene elementos", inversion.size() > 0);
			assertTrue("El elemento de inversion no es un JsonObject", inversion.get(0).isJsonObject());
			
			inversionElemento = inversion.get(0).getAsJsonObject();
		}
		
		assertNotNull("El elemento inversion es nulo", inversionElemento);

		assertTrue("La propiedad Inv_Numero no se encuentra en inversion", inversionElemento.has("Inv_Numero"));
		assertTrue("La propiedad Inv_Cantid no se encuentra en inversion", inversionElemento.has("Inv_Cantid"));
		
		logger.info("TEST: Finalizando inversionesPagareNumeroUsuarioObtenerTestDeberiaSerExitoso metodo...");	
	}
}
