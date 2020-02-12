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
		
		/**
		 *	Mockup Test
		 *	String json = "{\"inversiones\":{\"inversion\":[{\"Inv_Numero\":\"String\",\"Inv_Cuenta\":\"String\",\"Inv_ForTas\":\"String\",\"Inv_FecIni\":\"String\",\"Inv_FecVen\":\"String\",\"Inv_Cantid\":\"Double\",\"Amo_Tasa\":\"Double\",\"Amo_ISR\":\"Double\",\"Amo_FecIni\":\"String\",\"Amo_FecVen\":\"String\",\"Amo_Numero\":\"String\",\"Imp_Intere\":\"Double\",\"Imp_ISR\":\"Double\",\"Plazo\":\"Integer\",\"Pla_Intere\":\"Integer\",\"Imp_Total\":\"Double\",\"Fot_Descri\":\"String\",\"Inv_Tipo\":\"String\",\"Inv_Gat\":\"Double\",\"Inv_GatRea\":\"Double\",\"Inv_IntBru\":\"Double\",\"Inv_IntNet\":\"Double\",\"Inv_ISRTot\":\"Double\",\"Inv_Total\":\"Double\",\"Inv_Esquema\":\"String\"}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */

		/**
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
		
		/**
		 *	Mockup Test
		 *	String json = "{\"inversiones\":{\"inversion\":[{\"Inv_Numero\":\"String\",\"Inv_FecIni\":\"Date\",\"Inv_FecVen\":\"Date\",\"Inv_Cantid\":\"Double\",\"Inv_Tasa\":\"Double\",\"Inv_Cuenta\":\"String\",\"Inv_ISR\":\"Double\",\"Inv_TBruta\":\"Double\",\"Adi_InsLiq\":\"String\",\"Mon_Descri\":\"String\",\"Inv_Plazo\":\"Integer\",\"Inv_GAT\":\"Double\",\"Inv_GATRea\":\"Double\",\"Gar_ComFon\":\"String\",\"Imp_ISR\":\"Double\",\"Imp_Intere\":\"Double\",\"Inv_Total\":\"Double\"}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
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
	
	@Test
	public void inversionesImportesDeInversionFinalizadaActualizarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando inversionesImportesDeInversionFinalizadaActualizarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosInversionFinalizada = new JsonObject();
		datosInversionFinalizada.addProperty("Inv_Numero", "001935000030001");
		datosInversionFinalizada.addProperty("Inv_Deposi", 509000);
		datosInversionFinalizada.addProperty("Inv_rFecIn", "2019-09-24 00:00:00");
		datosInversionFinalizada.addProperty("Inv_rFecVe", "2019-09-25 00:00:00");
		datosInversionFinalizada.addProperty("Inv_rCanti", 509000);
		datosInversionFinalizada.addProperty("Inv_rTasa", 3.61);
		datosInversionFinalizada.addProperty("Inv_rAutor", "000100");
		datosInversionFinalizada.addProperty("Inv_rISR", 1.04);
		datosInversionFinalizada.addProperty("Inv_rCuent", "001935000013");
		datosInversionFinalizada.addProperty("Inv_rTBrut", 4.65);
		datosInversionFinalizada.addProperty("NumTransac", "49646239");
		datosInversionFinalizada.addProperty("Transaccio", "IAQ");
		datosInversionFinalizada.addProperty("Usuario", "000100");
		datosInversionFinalizada.addProperty("FechaSis", fechaSis);
		datosInversionFinalizada.addProperty("SucOrigen", "001");
		datosInversionFinalizada.addProperty("SucDestino", "001");
		datosInversionFinalizada.addProperty("Modulo", "NB");
		
		/**
		 * Mock
		 * String json = "{\"importesDeInversionFinalizada\":{\"Err_Codigo\":\"String\",\"Err_Mensaj\":\"String\",\"Err_Variab\":\"String\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = inversionesServicio.inversionesImportesDeInversionFinalizadaActualizar(datosInversionFinalizada);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad importesDeInversionFinalizada", resultado.has("importesDeInversionFinalizada"));
		assertTrue("La propiedad importesDeInversionFinalizada no es un JsonObject", resultado.get("importesDeInversionFinalizada").isJsonObject());
		
		JsonObject importesDeInversionFinalizada = Utilerias.obtenerJsonObjectPropiedad(resultado, "importesDeInversionFinalizada");
		
		if(!resultado.get("importesDeInversionFinalizada").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en importesDeInversionFinalizada", importesDeInversionFinalizada.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en importesDeInversionFinalizada", importesDeInversionFinalizada.has("Err_Mensaj"));
		} else
			assertNotNull("la propiedad importesDeInversionFinalizada es nula", importesDeInversionFinalizada);
		
		logger.info("TEST: Finalizando inversionesImportesDeInversionFinalizadaActualizarTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void inversionesAltaTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando inversionesAltaTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosInversionesAlta = new JsonObject();
		datosInversionesAlta.addProperty("Inv_FecIni", "2019-09-24 00:00:00");
		datosInversionesAlta.addProperty("Inv_FecVen", "2019-09-25 00:00:00");
		datosInversionesAlta.addProperty("Inv_Cantid", 6000);
		datosInversionesAlta.addProperty("Inv_Tasa", 2.45);
		datosInversionesAlta.addProperty("Inv_Autori", "000100");
		datosInversionesAlta.addProperty("Inv_Moneda", "01");
		datosInversionesAlta.addProperty("Inv_ISR", 1.45);
		datosInversionesAlta.addProperty("Inv_Cuenta", "001953830040");
		datosInversionesAlta.addProperty("Inv_TipImp", "V");
		datosInversionesAlta.addProperty("Inv_InvAnt", "");
		datosInversionesAlta.addProperty("Inv_TBruta", 3.9);
		datosInversionesAlta.addProperty("Inv_CveSeg", "");
		datosInversionesAlta.addProperty("Inv_Origen", "IN");
		datosInversionesAlta.addProperty("Inv_ClaInv", "001");
		datosInversionesAlta.addProperty("Inv_MonRef", 0);
		datosInversionesAlta.addProperty("Inv_CanPer", 1);
		datosInversionesAlta.addProperty("Inv_CanPer", "");
		datosInversionesAlta.addProperty("I_Numero", "");
		datosInversionesAlta.addProperty("NumTransac", "57838271");
		datosInversionesAlta.addProperty("Transaccio", "AII");
		datosInversionesAlta.addProperty("Usuario", "000100");
		datosInversionesAlta.addProperty("FechaSis", fechaSis);
		datosInversionesAlta.addProperty("SucOrigen", "001");
		datosInversionesAlta.addProperty("SucDestino", "001");
		datosInversionesAlta.addProperty("Modulo", "NB");
		
		/**
		 * Mock
		 * String json = "{\"altaDeInversion\":{\"Err_Codigo\":\"String\",\"Err_Mensaj\":\"String\",\"Err_Variab\":\"String\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = inversionesServicio.inversionesAlta(datosInversionesAlta);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad importesDeInversionFinalizada", resultado.has("importesDeInversionFinalizada"));
		assertTrue("La propiedad importesDeInversionFinalizada no es un JsonObject", resultado.get("importesDeInversionFinalizada").isJsonObject());
		
		JsonObject importesDeInversionFinalizada = Utilerias.obtenerJsonObjectPropiedad(resultado, "importesDeInversionFinalizada");
		
		if(!resultado.get("importesDeInversionFinalizada").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en importesDeInversionFinalizada", importesDeInversionFinalizada.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en importesDeInversionFinalizada", importesDeInversionFinalizada.has("Err_Mensaj"));
		} else
			assertNotNull("la propiedad importesDeInversionFinalizada es nula", importesDeInversionFinalizada);
		
		logger.info("TEST: Finalizando inversionesAltaTestDeberiaSerExitoso metodo...");
	}
}
