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
public class UsuarioServicioTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicioTest.class);
	
	private static UsuarioServicio usuarioServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		usuarioServicio = new UsuarioServicio();
	}
	
	@Test
	public void usuarioConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando usuarioConsultarTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosUsuarioConsultar = new JsonObject();
		datosUsuarioConsultar.addProperty("Usu_Numero", "");
		datosUsuarioConsultar.addProperty("Usu_UsuAdm", "");
		datosUsuarioConsultar.addProperty("Usu_Clave", "u0019350001");
		datosUsuarioConsultar.addProperty("Usu_Passwo", "");
		datosUsuarioConsultar.addProperty("Usu_Client", "");
		datosUsuarioConsultar.addProperty("Usu_FolNip", 0);
		datosUsuarioConsultar.addProperty("Usu_FolTok", "");
		datosUsuarioConsultar.addProperty("Usu_Status", "");
		datosUsuarioConsultar.addProperty("Usu_CuCaCo", "");
		datosUsuarioConsultar.addProperty("Usu_SucMod", "");
		datosUsuarioConsultar.addProperty("Tip_Consul", "C1");
		datosUsuarioConsultar.addProperty("NumTransac", "");
		datosUsuarioConsultar.addProperty("Transaccio", "HKB");
		datosUsuarioConsultar.addProperty("Usuario", "");
		datosUsuarioConsultar.addProperty("FechaSis", fechaSis);
		datosUsuarioConsultar.addProperty("SucOrigen", "001");
		datosUsuarioConsultar.addProperty("SucDestino", "001");
		datosUsuarioConsultar.addProperty("Modulo", "NB");
		
		/*
		 * Mockup Test
		 * String json = "{\"usuario\":{\"Usu_Numero\":\"String\",\"Usu_UsuAdm\":\"String\",\"Usu_Client\":\"String\",\"Usu_CuCaCo\":\"String\",\"Usu_Nombre\":\"String\",\"Usu_Clave\":\"String\",\"Usu_Passwo\":\"String\",\"Usu_Seguro\":\"String\",\"Usu_Tipo\":\"String\",\"Usu_Origen\":\"String\",\"Usu_TipSer\":\"String\",\"Usu_Status\":\"String\",\"Usu_FolNip\":\"Integer\",\"Usu_FolTok\":\"String\",\"Usu_StaTok\":\"String\",\"Usu_Email\":\"String\",\"Usu_FeUlAc\":\"String\",\"Usu_FeAcPa\":\"String\",\"Usu_FecAlt\":\"String\",\"Usu_FecCan\":\"String\",\"Usu_MotCan\":\"String\",\"Usu_CoAcNe\":\"Integer\",\"Usu_ToAcNe\":\"Integer\",\"Usu_StaSes\":\"String\",\"Usu_CoDeNe\":\"Integer\",\"Usu_CoPaNe\":\"Integer\",\"Pau_Usuari\":\"String\",\"Pau_OpMeHa\":\"String\",\"Pau_ImaSeg\":\"String\",\"Pau_FraSeg\":\"String\",\"Pau_PanIni\":\"String\",\"Pau_PrPrSe\":\"String\",\"Pau_PrPrPe\":\"String\",\"Pau_PrReSe\":\"String\",\"Pau_SePrSe\":\"String\",\"Pau_SePrPe\":\"String\",\"Pau_SeReSe\":\"String\",\"Pau_ArMeHa\":\"String\",\"Pau_TipAcc\":\"String\",\"Pau_ConCue\":\"String\",\"Pau_AltCue\":\"String\",\"Pau_CarCue\":\"String\",\"Pau_Solici\":\"String\",\"Pau_Autori\":\"String\",\"Pau_NivFir\":\"String\",\"Pau_UlPrSe\":\"String\",\"Tok_Status\":\"String\",\"Usu_EmaAdm\":\"String\",\"Usu_DesSta\":\"String\"}}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 * Test
		 */
		JsonObject resultado = usuarioServicio.usuarioConsultar(datosUsuarioConsultar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad usuario", resultado.has("usuario"));
		assertTrue("La propiedad usuario no es un JsonObject", resultado.get("usuario").isJsonObject());
		
		JsonObject usuario = Utilerias.obtenerJsonObjectPropiedad(resultado, "usuario");
		
		assertNotNull("usuario es nulo", usuario);
		
		assertTrue("La propiedad Usu_Numero no se encuentra en usuario", usuario.has("Usu_Numero"));
		assertTrue("La propiedad Usu_Clave no se encuentra en usuario", usuario.has("Usu_Clave"));

		logger.info("TEST: Finalizando usuarioConsultarTestDeberiaSerExitoso metodo");
	}
	
	@Test
	public void usuarioActualizarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando usuarioActualizarTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosUsuarioActualizar = new JsonObject();
		datosUsuarioActualizar.addProperty("Usu_Numero", "000010");
		datosUsuarioActualizar.addProperty("Usu_Clave", "");
		datosUsuarioActualizar.addProperty("Usu_Passwo", "");
		datosUsuarioActualizar.addProperty("Usu_Status", "");
		datosUsuarioActualizar.addProperty("Usu_Email", "");
		datosUsuarioActualizar.addProperty("Usu_UsuAdm", "");
		datosUsuarioActualizar.addProperty("Usu_FolTok", "");
		datosUsuarioActualizar.addProperty("Usu_Client", "");
		datosUsuarioActualizar.addProperty("Usu_SucMod", "");
		datosUsuarioActualizar.addProperty("Usu_FolNip", 0);
		datosUsuarioActualizar.addProperty("Usu_Nombre", "");
		datosUsuarioActualizar.addProperty("Tip_Actual", "A");
		datosUsuarioActualizar.addProperty("NumTransac", "255927");
		datosUsuarioActualizar.addProperty("Transaccio", "HKC");
		datosUsuarioActualizar.addProperty("Usuario", "000100");
		datosUsuarioActualizar.addProperty("FechaSis", fechaSis);
		datosUsuarioActualizar.addProperty("SucOrigen", "001");
		datosUsuarioActualizar.addProperty("SucDestino", "001");
		datosUsuarioActualizar.addProperty("Modulo", "NB");
		
		/*
		 *	Mockup Test
		 * 	String json = "{\"usuario\":{\"Usu_CoAcNe\":\"Integer\"}}";
		 * 	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */

		/*
		 * Test
		 */
		JsonObject resultado = usuarioServicio.usuarioActualizar(datosUsuarioActualizar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad usuario", resultado.has("usuario"));
		assertTrue("La propiedad usuario no es un JsonObject", resultado.get("usuario").isJsonObject());
		
		JsonObject usuario = Utilerias.obtenerJsonObjectPropiedad(resultado, "usuario");
		
		if(!resultado.get("usuario").isJsonNull()) {
			assertTrue("La propiedad Usu_CoAcNe no se encuentra en usuario", usuario.has("Usu_CoAcNe"));
		}else
			assertNotNull("la propiedad  usuario es nula", usuario);

		logger.info("TEST: Finalizando usuarioActualizarTestDeberiaSerExitoso metodo");
	}
	
	@Test
	public void usuarioPerfilRiesgoConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando usuarioPerfilRiesgoConsultarTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosPerfilRiesgo = new JsonObject();
		datosPerfilRiesgo.addProperty("Apl_Client", "00193500");
		datosPerfilRiesgo.addProperty("Apl_Cuesti", 0);
		datosPerfilRiesgo.addProperty("FechaSis", fechaSis);
		
		/**
		 *	Mockup Test
		 *	String json = "{\"perfilRiesgo\":{\"Apl_PerRie\":\"Integer\",\"Apl_ConOpe\":\"Integer\",\"Per_TitCli\":\"String\",\"Par_OpExPe\":\"Integer\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */

		/**
		 * Test
		 */
		JsonObject resultado = usuarioServicio.usuarioPerfilRiesgoConsultar(datosPerfilRiesgo);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad perfilRiesgo", resultado.has("perfilRiesgo"));
		assertTrue("La propiedad perfilRiesgo no es un JsonObject", resultado.get("perfilRiesgo").isJsonObject());
		
		JsonObject perfilRiesgo = Utilerias.obtenerJsonObjectPropiedad(resultado, "perfilRiesgo");
		
		if(!resultado.get("perfilRiesgo").isJsonNull()) {
			assertTrue("La propiedad Apl_PerRie no se encuentra en perfilRiesgo", perfilRiesgo.has("Apl_PerRie"));
			assertTrue("La propiedad Apl_ConOpe no se encuentra en perfilRiesgo", perfilRiesgo.has("Apl_ConOpe"));
		}else
			assertNotNull("la propiedad perfilRiesgo es nula", perfilRiesgo);

		logger.info("TEST: Finalizando usuarioPerfilRiesgoConsultarTestDeberiaSerExitoso metodo");
	}
}
