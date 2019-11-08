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
		
		/*
		 *	Mockup Test
		 *	String json = "{\"inversiones\":{\"inversion\":[{\"Inv_Numero\":\"String\",\"Inv_Cuenta\":\"String\",\"Inv_ForTas\":\"String\",\"Inv_FecIni\":\"String\",\"Inv_FecVen\":\"String\",\"Inv_Cantid\":\"Double\",\"Amo_Tasa\":\"Double\",\"Amo_ISR\":\"Double\",\"Amo_FecIni\":\"String\",\"Amo_FecVen\":\"String\",\"Amo_Numero\":\"String\",\"Imp_Intere\":\"Double\",\"Imp_ISR\":\"Double\",\"Plazo\":\"Integer\",\"Pla_Intere\":\"Integer\",\"Imp_Total\":\"Double\",\"Fot_Descri\":\"String\",\"Inv_Tipo\":\"String\",\"Inv_Gat\":\"Double\",\"Inv_GatRea\":\"Double\",\"Inv_IntBru\":\"Double\",\"Inv_IntNet\":\"Double\",\"Inv_ISRTot\":\"Double\",\"Inv_Total\":\"Double\",\"Inv_Esquema\":\"String\"}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */

		/*
		 * Test
		 */
		JsonObject resultado = inversionesServicio.inversionesObtener(inversionesObtener);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad inversiones", resultado.has("inversiones"));
		assertTrue("La propiedad inversiones no es un JsonObject", resultado.get("inversiones").isJsonObject());
		
		JsonObject inversiones = Utilerias.obtenerJsonObjectPropiedad(resultado, "inversiones");
		
		assertTrue("No viene la propiedad inversion", inversiones.has("inversion"));
		assertTrue("La propiedad inversiones no es un JsonArray o un JsonObject", inversiones.get("inversion").isJsonArray() || inversiones.get("inversion").isJsonObject());
		
		JsonObject inversionElemento = null;
		if(inversiones.get("inversion").isJsonArray()) {
			JsonArray inversion = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");
			
			assertTrue("La propiedad inversion no tiene elementos", inversion.size() > 0);
			assertTrue("El elemento de inversion no es un JsonObject", inversion.get(0).isJsonObject());
			
			inversionElemento = inversion.get(0).getAsJsonObject();
			
			assertTrue("La propiedad Inv_Numero no se encuentra en inversion", inversionElemento.has("Inv_Numero"));
			assertTrue("La propiedad Inv_Cantid no se encuentra en inversion", inversionElemento.has("Inv_Cantid"));
		}
		else
			inversionElemento = inversiones.get("inversion").getAsJsonObject();
		
		assertNotNull("El elemento inversion es nulo", inversionElemento);
		
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
		
		/*
		 *	Mockup Test
		 *	String json = "{\"inversiones\":{\"inversion\":[{\"Inv_Numero\":\"String\",\"Inv_FecIni\":\"Date\",\"Inv_FecVen\":\"Date\",\"Inv_Cantid\":\"Double\",\"Inv_Tasa\":\"Double\",\"Inv_Cuenta\":\"String\",\"Inv_ISR\":\"Double\",\"Inv_TBruta\":\"Double\",\"Adi_InsLiq\":\"String\",\"Mon_Descri\":\"String\",\"Inv_Plazo\":\"Integer\",\"Inv_GAT\":\"Double\",\"Inv_GATRea\":\"Double\",\"Gar_ComFon\":\"String\",\"Imp_ISR\":\"Double\",\"Imp_Intere\":\"Double\",\"Inv_Total\":\"Double\"}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 * Test
		 */
		JsonObject resultado = inversionesServicio.inversionesPagareNumeroUsuarioObtener(datosInversionesPagare);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad inversiones", resultado.has("inversiones"));
		assertTrue("La propiedad inversiones no es un JsonObject", resultado.get("inversiones").isJsonObject());
				
		JsonObject inversiones = Utilerias.obtenerJsonObjectPropiedad(resultado, "inversiones");

		if(inversiones.has("inversion")) {
			assertTrue("La propiedad inversion no es un JsonArray", inversiones.get("inversion").isJsonArray());
			
			JsonObject inversionElemento = null;
			if(inversiones.get("inversion").isJsonArray()) {
				JsonArray inversion = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");
				
				assertTrue("La propiedad inversion no tiene elementos", inversion.size() > 0);
				assertTrue("El elemento de inversion no es un JsonObject", inversion.get(0).isJsonObject());
				
				inversionElemento = inversion.get(0).getAsJsonObject();
				
				assertTrue("La propiedad Inv_Numero no se encuentra en inversion", inversionElemento.has("Inv_Numero"));
				assertTrue("La propiedad Inv_Cantid no se encuentra en inversion", inversionElemento.has("Inv_Cantid"));			
			}else 
				assertNotNull("El elemento inversion es nulo", inversionElemento);			
		}
		logger.info("TEST: Finalizando inversionesPagareNumeroUsuarioObtenerTestDeberiaSerExitoso metodo...");	
	}
}
