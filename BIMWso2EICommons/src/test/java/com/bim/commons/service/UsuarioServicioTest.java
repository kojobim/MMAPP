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
	public void usuarioConsultarDeberiaSerExitoso() {
		logger.info("TEST: Comenzando usuarioConsultarDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosUsuarioConsultar = new JsonObject();
		datosUsuarioConsultar.addProperty("Usu_Numero", "");
		datosUsuarioConsultar.addProperty("Usu_UsuAdm", "");
		datosUsuarioConsultar.addProperty("Usu_Clave", "u0019350001");
		datosUsuarioConsultar.addProperty("Usu_Passwo", "");
		datosUsuarioConsultar.addProperty("Usu_Client", "");
		datosUsuarioConsultar.addProperty("Usu_FolNip", "0");
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
		
		JsonObject resultado = usuarioServicio.usuarioConsultar(datosUsuarioConsultar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad usuario", resultado.has("usuario"));
		assertTrue("La propiedad usuario no es un JsonObject", resultado.get("usuario").isJsonObject());
		
		JsonObject usuario = Utilerias.obtenerJsonObjectPropiedad(resultado, "usuario");
		
		assertNotNull("usuario es nulo", usuario);
		
		assertTrue("La propiedad Usu_Numero no se encuentra en usuario", usuario.has("Usu_Numero"));
		assertTrue("La propiedad Usu_Clave no se encuentra en usuario", usuario.has("Usu_Clave"));

		logger.info("TEST: Finalizando usuarioConsultarDeberiaSerExitoso metodo");
	}
	
	@Test
	public void usuarioActualizarDeberiaSerExitoso() {
		logger.info("TEST: Comenzando usuarioActualizarDeberiaSerExitoso metodo");
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
		datosUsuarioActualizar.addProperty("Tip_Actual", "C1");
		datosUsuarioActualizar.addProperty("NumTransac", "255927");
		datosUsuarioActualizar.addProperty("Transaccio", "HKC");
		datosUsuarioActualizar.addProperty("Usuario", "000100");
		datosUsuarioActualizar.addProperty("FechaSis", fechaSis);
		datosUsuarioActualizar.addProperty("SucOrigen", "001");
		datosUsuarioActualizar.addProperty("SucDestino", "001");
		datosUsuarioActualizar.addProperty("Modulo", "NB");
		
		JsonObject resultado = usuarioServicio.usuarioActualizar(datosUsuarioActualizar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad usuario", resultado.has("usuario"));
		assertTrue("La propiedad usuario no es un JsonObject", resultado.get("usuario").isJsonObject());
		
		JsonObject usuario = Utilerias.obtenerJsonObjectPropiedad(resultado, "usuario");
		
		assertNotNull("usuario es nulo", usuario);
		
		assertTrue("La propiedad Usu_CoAcNe no se encuentra en usuario", usuario.has("Usu_CoAcNe"));

		logger.info("TEST: Finalizando usuarioActualizarDeberiaSerExitoso metodo");
	}
}
