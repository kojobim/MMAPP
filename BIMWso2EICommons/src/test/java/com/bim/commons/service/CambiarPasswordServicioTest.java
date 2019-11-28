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
public class CambiarPasswordServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(CambiarPasswordServicioTest.class);
	
	private static CambiarPasswordServicio cambiarPasswordServicio = null;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		cambiarPasswordServicio = new CambiarPasswordServicio();
	}
	
	@Test
	public void cuentasEspecialesConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentasEspecialesConsultarTestDeberiaSerExitoso metodo...");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentasEspecialesConsultar = new JsonObject();
		datosCuentasEspecialesConsultar.addProperty("FechaSis", fechaSis);
		datosCuentasEspecialesConsultar.addProperty("Ces_Usuari","000014");
		
		/**
		 * Test
		 */
		
		JsonObject resultado = cambiarPasswordServicio.cuentasEspecialesConsulta(datosCuentasEspecialesConsultar);
		logger.info("resultado: " + resultado);
		
		assertTrue("No viene la propiedad cuentasEspeciales", resultado.has("cuentasEspeciales"));
		assertTrue("La propiedad cuentasEspeciales no es un JsonObject", resultado.get("cuentasEspeciales").isJsonObject());
		
		JsonObject cuentasEspeciales = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentasEspeciales");
		
		if(!resultado.get("cuentasEspeciales").isJsonNull()) {

			assertTrue("La propiedad Usu_Numero se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Numero"));
			assertTrue("La propiedad Usu_UsuAdm se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_UsuAdm"));
			assertTrue("La propiedad Usu_Client se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Client"));
			assertTrue("La propiedad Usu_CuCaCo se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_CuCaCo"));
			assertTrue("La propiedad Usu_Nombre se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Nombre"));
			assertTrue("La propiedad Usu_Clave  se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Clave"));
			assertTrue("La propiedad Usu_Passwo se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Passwo"));
			assertTrue("La propiedad Usu_Seguro se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Seguro"));
			assertTrue("La propiedad Usu_Tipo se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Tipo"));
			assertTrue("La propiedad Usu_Origen se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Origen"));
			assertTrue("La propiedad Usu_TipSer se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_TipSer"));
			assertTrue("La propiedad Usu_Status se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Status"));
			assertTrue("La propiedad Usu_FolNip se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_FolNip"));
			assertTrue("La propiedad Usu_FolTok se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_FolTok"));
			assertTrue("La propiedad Usu_StaTok se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_StaTok"));
			assertTrue("La propiedad Usu_Email se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_Email"));
			assertTrue("La propiedad Usu_FeUlAc se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_FeUlAc"));
			assertTrue("La propiedad Usu_FeAcPa se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_FeAcPa"));
			assertTrue("La propiedad Usu_FecAlt se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_FecAlt"));
			assertTrue("La propiedad Usu_FecCan se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_FecCan"));
			assertTrue("La propiedad Usu_MotCan se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_MotCan"));
			assertTrue("La propiedad Usu_CoAcNe se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_CoAcNe"));
			assertTrue("La propiedad Usu_StaSes se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_StaSes"));
			assertTrue("La propiedad Usu_CoDeNe se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_CoDeNe"));
			assertTrue("La propiedad Usu_CoPaNe se encuentra en cuentasEspeciales", cuentasEspeciales.has("Usu_CoPaNe"));
			
		} else
			assertNotNull("la propiedad cuentasEspeciales es nula", cuentasEspeciales);
		
		logger.info("TEST: Finalizando cuentasEspecialesConsultarTestDeberiaSerExitoso metodo...");
	}
	
}
