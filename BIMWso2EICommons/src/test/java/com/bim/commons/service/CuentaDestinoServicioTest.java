package com.bim.commons.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertTrue("La propiedad cuentaDestino no es un JsonObject", resultado.get("cuentaDestino").isJsonObject());
		
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

	@Test
	public void cuentaDestinoBIMCreacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoBIMCreacionTestDeberiaSerExitoso método...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoBIMCreacion = new JsonObject();
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_UsuAdm", "000149");
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_Cuenta", "001951620013");
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_CliUsu", "00195171");
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_Alias", "ALIAS");
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_RFCBen", "RFC");
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_EmaBen", "y.duran@bim.mx");
		datosCuentaDestinoBIMCreacion.addProperty("Cdb_Random", "F30BCD373A70CCFBB1652941B28B9F96");
		datosCuentaDestinoBIMCreacion.addProperty("NumTransac", "42551186");
		datosCuentaDestinoBIMCreacion.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * String json = "{\"cuentaDestinoBIM\":{\"Err_Codigo\":\"000001\",\"Err_Mensaj\":\"El código de Autenticación no pudo ser validado\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoBIMCreacion(datosCuentaDestinoBIMCreacion);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentaDestinoBIM", resultado.has("cuentaDestinoBIM"));
		assertTrue("La propiedad cuentaDestinoBIM no es un JsonObject", resultado.get("cuentaDestinoBIM").isJsonObject());
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestinoBIM");
		
		if(!resultado.get("cuentaDestinoBIM").isJsonNull()) {
			assertTrue("La propiedad Cue_Numero no se encuentra en cuentaDestinoBIM", cuentaDestinoBIM.has("Err_Codigo"));
			assertTrue("La propiedad Cue_Moneda no se encuentra en cuentaDestinoBIM", cuentaDestinoBIM.has("Err_Mensaj"));
		} else
			assertNotNull("la propiedad cuentaDestinoBIM es nula", cuentaDestinoBIM);
		
		logger.info("TEST: Finalizando cuentaDestinoBIMCreacionTestDeberiaSerExitoso método...");
	}
	
	@Test
	public void cuentaDestinoProcesarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoProcesarTestDeberiaSerExitoso método...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoProcesar = new JsonObject();
		datosCuentaDestinoProcesar.addProperty("Cud_UsuAdm", "000149");
		datosCuentaDestinoProcesar.addProperty("Cud_CLABE", "012180001457899904");
		datosCuentaDestinoProcesar.addProperty("Cud_Banco", "40012");
		datosCuentaDestinoProcesar.addProperty("NumTransac", "42623901");
		datosCuentaDestinoProcesar.addProperty("FechaSis", fechaSis);
		
		/**
		 *	Mockup Test
		 *	String json = "{\"REQUEST_STATUS\":\"SUCCESSFUL\"}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */

		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoProcesar(datosCuentaDestinoProcesar);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad REQUEST_STATUS", resultado.has("REQUEST_STATUS"));
		
		String statusSolicitud = Utilerias.obtenerStringPropiedad(resultado, "REQUEST_STATUS");
		logger.info("- requestStatus " + statusSolicitud);
		
		assertNotNull(statusSolicitud);
		assertEquals("SUCCESSFUL", statusSolicitud.toString());
		
		logger.info("TEST: Finalizando cuentaDestinoProcesarTestDeberiaSerExitoso método...");
	}

	@Test
	public void cuentaDestinoBIMConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoBIMConsultarTestDeberiaSerExitoso método...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoBIMConsultar = new JsonObject();
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Client", "");
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", "000149");
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Usuari", "000149");
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Cuenta", "");
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Status", "");
		datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", "L2");
		datosCuentaDestinoBIMConsultar.addProperty("NumTransac", "");
		datosCuentaDestinoBIMConsultar.addProperty("Transaccio", "HNI");
		datosCuentaDestinoBIMConsultar.addProperty("FechaSis", fechaSis);
		
		/**
		 *	Mockup Test
		 *	String json = "{\"cuentaDestinoBIM\":{\"cuentasDestinoBIM\":[{\"Cdb_Consec\":\"000750\",\"Cdb_UsuAdm\":\"001844\",\"Cdb_Cuenta\":\"001000010011\",\"Cdb_CliUsu\":\"00193500\",\"Cdb_Alias\":\"CUENTA PRUEBA MARCOS\",\"Cdb_RFCBen\":\"COCM870323PLN\",\"Cdb_EmaBen\":\"MCONTRERASCRUZ@HOTMAIL.COM\",\"Cdb_FecAlt\":\"2019-11-08T07:08:00.000-06:00\",\"Cdb_FecCan\":\"1899-12-31T17:23:24.000-06:36\",\"Cdb_FecAct\":\"1899-12-31T17:23:24.000-06:36\",\"Cdb_MinRes\":\-63036779\,\"Cli_ComOrd\":\"Nombre Ordenado\",\"Pat_Codigo\":\"   \",\"Pat_Consec\":\0\}
		 																 {\"Cdb_Consec\":\"000751\",\"Cdb_UsuAdm\":\"001844\",\"Cdb_Cuenta\":\"001000010011\",\"Cdb_CliUsu\":\"00193500\",\"Cdb_Alias\":\"PRUEBA 3\",\"Cdb_RFCBen\":\"ABCDE12345667\",\"Cdb_EmaBen\":\"contrerasc.marcos@gmail.com\",\"Cdb_FecAlt\":\"2019-11-08T07:08:00.000-06:00\",\"Cdb_FecCan\":\"1899-12-31T17:23:24.000-06:36\",\"Cdb_FecAct\":\"1899-12-31T17:23:24.000-06:36\",\"Cdb_MinRes\":\-63036779\,\"Cli_ComOrd\":\"Nombre Ordenado\",\"Pat_Codigo\":\"   \",\"Pat_Consec\":\0\}]}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */

		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoBIMConsultar(datosCuentaDestinoBIMConsultar);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentaDestinoBIM", resultado.has("cuentaDestinoBIM"));
		assertTrue("La propiedad cuentaDestinoBIM no es un JsonObject", resultado.get("cuentaDestinoBIM").isJsonObject());
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestinoBIM");

		assertTrue("No viene la propiedad cuentasDestinoBIM", cuentaDestinoBIM.has("cuentasDestinoBIM"));
		assertTrue("La propiedad cuentasDestinoBIM no es un JsonObject", cuentaDestinoBIM.get("cuentasDestinoBIM").isJsonArray());
		
		JsonObject cuentasDestinoBIMElemento = null;
		
		if(cuentaDestinoBIM.get("cuentasDestinoBIM").isJsonArray()) {
			JsonArray cuentasDestinoBIM = Utilerias.obtenerJsonArrayPropiedad(cuentaDestinoBIM, "cuentasDestinoBIM");

			assertTrue("La propiedad cuentasDestinoBIM no tiene elementos", cuentasDestinoBIM.size() > 0);
			assertTrue("El elemento de cuentasDestinoBIM no es un JsonObject", cuentasDestinoBIM.get(0).isJsonObject());

			cuentasDestinoBIMElemento = cuentasDestinoBIM.get(0).getAsJsonObject();

			assertTrue("La propiedad Cdb_Consec no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_Consec"));
			assertTrue("La propiedad Cdb_UsuAdm no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_UsuAdm"));
			assertTrue("La propiedad Cdb_Cuenta no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_Cuenta"));
			assertTrue("La propiedad Cdb_CliUsu no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_CliUsu"));
			assertTrue("La propiedad Cdb_Alias no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_Alias"));
			assertTrue("La propiedad Cdb_RFCBen no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_RFCBen"));
			assertTrue("La propiedad Cdb_EmaBen no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_EmaBen"));
			assertTrue("La propiedad Cdb_FecAlt no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_FecAlt"));
			assertTrue("La propiedad Cdb_FecCan no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_FecCan"));
			
			if(datosCuentaDestinoBIMConsultar.get("Transaccio").getAsString().equals("L3"))
				assertTrue("La propiedad Cdb_FecAct no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_FecAct"));

			if(datosCuentaDestinoBIMConsultar.get("Transaccio").getAsString().equals("L3"))
				assertTrue("La propiedad Cdb_MinRes no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cdb_MinRes"));

			if(datosCuentaDestinoBIMConsultar.get("Transaccio").getAsString().equals("L3"))
				assertTrue("La propiedad Cli_ComOrd no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Cli_ComOrd"));

			if(datosCuentaDestinoBIMConsultar.get("Transaccio").getAsString().equals("L3"))
				assertTrue("La propiedad Pat_Codigo no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Pat_Codigo"));

			if(datosCuentaDestinoBIMConsultar.get("Transaccio").getAsString().equals("L3"))
				assertTrue("La propiedad Pat_Consec no se encuentra en cuentasDestinoBIM", cuentasDestinoBIMElemento.has("Pat_Consec"));
		}else
			assertNotNull("la propiedad cuentasDestinoBIMElemento es nula", cuentasDestinoBIMElemento);
		
		
		logger.info("TEST: Finalizando cuentasDestinoBIMConsultarTestDeberiaSerExitoso método...");
	}

	@Test
	public void cuentaDestinoSPEICreacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaDestinoSPEICreacionTestDeberiaSerExitoso método...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", "000149");
		datosCuentaDestinoSPEI.addProperty("Cds_CLABE", "012180001457899904");
		datosCuentaDestinoSPEI.addProperty("Cds_Banco", "40012");
		datosCuentaDestinoSPEI.addProperty("Cds_CliUsu", "00195171");
		datosCuentaDestinoSPEI.addProperty("Cds_Alias", "ALIAS");
		datosCuentaDestinoSPEI.addProperty("Cds_RFCBen", "RFC");
		datosCuentaDestinoSPEI.addProperty("Cds_EmaBen", "y.duran@bim.mx");
		datosCuentaDestinoSPEI.addProperty("Cds_DesAdi", "REFERENCIA");
		datosCuentaDestinoSPEI.addProperty("Cds_Random", "0308C7D06B148A94B1652941B28B9F96");
		datosCuentaDestinoSPEI.addProperty("NumTransac", "42623901");
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		/**
		 * Mock
		 * 
		 * String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"000002\",\"Err_Mensaj\":\"La cuenta SPEI ya existe para este usuario\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 * 
		 * cuando cambias Cds_CLABE por 012180001457899994
		 * String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"000004\",\"Err_Mensaj\":\"Error en el dígito verificador de la cuenta destino\",\"Err_Variab\":\"Cdp_Usuari\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 * 
		 * cuando cambias Cds_CLABE por 012180001457899994 y Cds_Banco por 40092
		 * String json = "{\"cuentaDestino\":{\"Err_Codigo\":\"000003\",\"Err_Mensaj\":\"La Cuenta no coincide con el banco receptor\",\"Err_Foco\":\"Cds_UsuAdm\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		
		JsonObject resultado = cuentaDestinoServicio.cuentaDestinoSPEICreacion(datosCuentaDestinoSPEI);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentaDestino", resultado.has("cuentaDestino"));
		assertTrue("La propiedad cuentaDestino no es un JsonObject", resultado.get("cuentaDestino").isJsonObject());
		
		JsonObject cuentaDestino = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestino");
		
		if(!resultado.get("cuentaDestino").isJsonNull()) {
			assertTrue("La propiedad Cue_Numero no se encuentra en cuentaDestino", cuentaDestino.has("Err_Codigo"));
			assertTrue("La propiedad Cue_Moneda no se encuentra en cuentaDestino", cuentaDestino.has("Err_Mensaj"));
		} else
			assertNotNull("la propiedad cuentaDestino es nula", cuentaDestino);
		
		logger.info("TEST: Finalizando cuentaDestinoSPEICreacionTestDeberiaSerExitoso método...");
	}
}