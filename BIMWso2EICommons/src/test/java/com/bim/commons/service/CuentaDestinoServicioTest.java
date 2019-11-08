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
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", "000149");
		datosCuentaDestinoSPEI.addProperty("NumTransac", "42623902");
		datosCuentaDestinoSPEI.addProperty("Transaccio", "HRB");
		datosCuentaDestinoSPEI.addProperty("Usuario", "000100");
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoSPEI.addProperty("SucOrigen", "001");
		datosCuentaDestinoSPEI.addProperty("SucDestino", "001");
		datosCuentaDestinoSPEI.addProperty("Modulo", "NB");
		
		/*
		 *	Mockup Test
		 *	String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"String\",\"Err_Mensaj\":\"String\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEI);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuentaDestino", resultado.has("cuentaDestino"));
		assertTrue("La propiedad cuenta no es un JsonObject", resultado.get("cuentaDestino").isJsonObject());
		
		JsonObject cuentaDestino = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestino");
		
		if(!resultado.get("cuentaDestino").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en cuentaDestino", cuentaDestino.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en cuentaDestino", cuentaDestino.has("Err_Mensaj"));
		}else 
			assertNotNull("cuentaDestino es nulo", cuentaDestino);
		
		logger.info("TEST: Finalizando cuentaDestinoSPEIActivacionTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void cuentaDestinoSPEIConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoSPEIConsultarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_Client", "");
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", "000149");
		datosCuentaDestinoSPEI.addProperty("Cds_Usuari", "000149");
		datosCuentaDestinoSPEI.addProperty("Cds_Consec", "");
		datosCuentaDestinoSPEI.addProperty("Cds_CLABE", "");
		datosCuentaDestinoSPEI.addProperty("Tip_Consul", "L1");
		datosCuentaDestinoSPEI.addProperty("NumTransac", "");
		datosCuentaDestinoSPEI.addProperty("Transaccio", "HOY");
		datosCuentaDestinoSPEI.addProperty("Usuario", "000100");
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoSPEI.addProperty("SucOrigen", "001");
		datosCuentaDestinoSPEI.addProperty("SucDestino", "001");
		datosCuentaDestinoSPEI.addProperty("Modulo", "NB");

		/*
		 *	Mockup Test
		 *	String json = "{\"cuentasDestino\":{\"cuentaDestino\":[{\"Cds_UsuAdm\":\"String\",\"Cds_Consec\":\"String\",\"Cds_CLABE\":\"String\",\"Cds_Banco\":\"String\",\"Ins_Descri\":\"String\",\"Cds_CliUsu\":\"String\",\"Cds_Status\":\"String\",\"Cds_Alias\":\"String\",\"Cds_RFCBen\":\"String\",\"Cds_EmaBen\":\"String\",\"Cds_Inicia\":\"String\",\"Cds_FecAlt\":\"String\",\"Cds_FecCan\":\"String\",\"Cds_DesAdi\":\"String\",\"Cds_DesCue\":\"String\",\"Cds_FeAlFo\":\"String\"}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoSPEIConsultar(datosCuentaDestinoSPEI);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuentasDestino", resultado.has("cuentasDestino"));
		assertTrue("La propiedad cuentasDestino no es un JsonObject", resultado.get("cuentasDestino").isJsonObject());
		
		JsonObject cuentasDestino = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentasDestino");
		
		assertTrue("No viene la propiedad cuentaDestino", cuentasDestino.has("cuentaDestino"));
		assertTrue("La propiedad cuentaDestino no es un JsonArray", cuentasDestino.get("cuentaDestino").isJsonArray());

		JsonObject cuentaDestinoElemento = null;
		if(cuentasDestino.get("cuentaDestino").isJsonArray()) {
			JsonArray cuentaDestino = Utilerias.obtenerJsonArrayPropiedad(cuentasDestino, "cuentaDestino");
			
			assertTrue("La propiedad cuentaDestino no tiene elementos", cuentaDestino.size() > 0);
			assertTrue("El elemento de cuentaDestino no es un JsonObject", cuentaDestino.get(0).isJsonObject());
			
			cuentaDestinoElemento = cuentaDestino.get(0).getAsJsonObject();
			
			assertTrue("La propiedad Cds_CLABE no se encuentra en cuentaDestino", cuentaDestinoElemento.has("Cds_CLABE"));
			assertTrue("La propiedad Ins_Descri no se encuentra en cuentaDestino", cuentaDestinoElemento.has("Ins_Descri"));
		}
		else
			assertNotNull("El elemento cuentaDestino es nulo", cuentaDestinoElemento);
		
		logger.info("TEST: Finalizando cuentaDestinoSPEIConsultarTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void catalogoInstitucionesConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando catalogoInstitucionesConsultarTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCatalogoInstituciones = new JsonObject();
		datosCatalogoInstituciones.addProperty("Ins_Clave", "");
		datosCatalogoInstituciones.addProperty("Ins_Descri", "");
		datosCatalogoInstituciones.addProperty("Tip_Consul", "L3");
		datosCatalogoInstituciones.addProperty("NumTransac", "");
		datosCatalogoInstituciones.addProperty("Transaccio", "PIT");
		datosCatalogoInstituciones.addProperty("Usuario", "000100");
		datosCatalogoInstituciones.addProperty("FechaSis", fechaSis);
		datosCatalogoInstituciones.addProperty("SucOrigen", "001");
		datosCatalogoInstituciones.addProperty("SucDestino", "001");
		datosCatalogoInstituciones.addProperty("Modulo", "NB");

		/*
		 *	Mockup Test
		 *	String json = "{\"instituciones\":{\"institucion\":[{\"Ins_Clave\":\"String\",\"Ins_Descri\":\"String\"}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = cuentaDestinoServicio.catalogoInstitucionesConsultar(datosCatalogoInstituciones);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad instituciones", resultado.has("instituciones"));
		assertTrue("La propiedad instituciones no es un JsonObject", resultado.get("instituciones").isJsonObject());
		
		JsonObject instituciones = Utilerias.obtenerJsonObjectPropiedad(resultado, "instituciones");
		
		assertTrue("No viene la propiedad institucion", instituciones.has("institucion"));
		assertTrue("La propiedad institucion no es un JsonArray", instituciones.get("institucion").isJsonArray());

		JsonObject institucionElemento = null;
		if(instituciones.get("institucion").isJsonArray()) {
			JsonArray institucion = Utilerias.obtenerJsonArrayPropiedad(instituciones, "institucion");
			
			assertTrue("La propiedad institucion no tiene elementos", institucion.size() > 0);
			assertTrue("El elemento de institucion no es un JsonObject", institucion.get(0).isJsonObject());
			
			institucionElemento = institucion.get(0).getAsJsonObject();
			
			assertTrue("La propiedad Ins_Clave no se encuentra en institucion", institucionElemento.has("Ins_Clave"));
			assertTrue("La propiedad Ins_Descri no se encuentra en institucion", institucionElemento.has("Ins_Descri"));
		}
		else
			assertNotNull("El elemento cuentaDestino es nulo", institucionElemento);
		
		logger.info("TEST: Finalizando catalogoInstitucionesConsultarTestDeberiaSerExitoso metodo...");
	}

}
