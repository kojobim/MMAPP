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
public class ConfiguracionServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(ConfiguracionServicioTest.class);
	
	private static ConfiguracionServicio configuracionServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		configuracionServicio = new ConfiguracionServicio();
	}
	
	@Test
	public void horariosConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Empezando horariosConsultarTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datoshorariosConsultar = new JsonObject();
		datoshorariosConsultar.addProperty("Tip_Consul", "C6");
		datoshorariosConsultar.addProperty("Tip_Transf", "I");
		datoshorariosConsultar.addProperty("Err_Codigo", "");
		datoshorariosConsultar.addProperty("Msj_Error", "");
		datoshorariosConsultar.addProperty("NumTransac", "395417");
		datoshorariosConsultar.addProperty("Transaccio", "A04");
		datoshorariosConsultar.addProperty("Usuario", "000100");
		datoshorariosConsultar.addProperty("FechaSis", fechaSis);
		datoshorariosConsultar.addProperty("SucOrigen", "001");
		datoshorariosConsultar.addProperty("SucDestino", "001");
		datoshorariosConsultar.addProperty("Modulo", "NB");
	
		JsonObject resultado = configuracionServicio.horariosConsultar(datoshorariosConsultar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad horariosInversion", resultado.has("horariosInversion"));
		assertTrue("La propiedad horariosInversion no es un JsonObject", resultado.get("horariosInversion").isJsonObject());
		
		JsonObject horariosInversion = Utilerias.obtenerJsonObjectPropiedad(resultado, "horariosInversion");
		
		assertNotNull("horariosInversion es nulo", horariosInversion);
		
		assertTrue("La propiedad EsHorariID no se encuentra en horariosInversion", horariosInversion.has("EsHorariID"));
		assertTrue("La propiedad Hor_Numero no se encuentra en horariosInversion", horariosInversion.has("Hor_Numero"));

		logger.info("TEST: Terminando horariosConsultarTestDeberiaSerExitoso metodo");
	}
	
	@Test
	public void configuracionBancoConsultarDetalleTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando configuracionBancoConsultarDetalleTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosConfiguracionDetalle = new JsonObject();
		datosConfiguracionDetalle.addProperty("Tip_Consul", "C1");
		datosConfiguracionDetalle.addProperty("NumTransac", "");
		datosConfiguracionDetalle.addProperty("Transaccio", "HNK");
		datosConfiguracionDetalle.addProperty("Usuario", "000100");
		datosConfiguracionDetalle.addProperty("FechaSis", fechaSis);
		datosConfiguracionDetalle.addProperty("SucOrigen", "001");
		datosConfiguracionDetalle.addProperty("SucDestino", "001");
		datosConfiguracionDetalle.addProperty("Modulo", "NB");

		JsonObject resultado = configuracionServicio.configuracionBancoConsultarDetalle(datosConfiguracionDetalle);
		logger.info("- resultado " + resultado);
		
		
		assertTrue("No viene la propiedad configuracionesBanco", resultado.has("configuracionesBanco"));
		assertTrue("La propiedad configuracionesBanco no es un JsonObject", resultado.get("configuracionesBanco").isJsonObject());

		JsonObject configuracionesBanco = Utilerias.obtenerJsonObjectPropiedad(resultado, "configuracionesBanco");
		
		
		assertTrue("No viene la propiedad configuracionBanco", configuracionesBanco.has("configuracionBanco"));
		assertTrue("La propiedad configuracionBanco no es un JsonArray o un JsonObject", configuracionesBanco.get("configuracionBanco").isJsonArray() || configuracionesBanco.get("configuracionBanco").isJsonObject());
		
		JsonObject configuracionBancoElemento = null;
		if(configuracionesBanco.get("configuracionBanco").isJsonArray()) {
			JsonArray configuracionBanco = Utilerias.obtenerJsonArrayPropiedad(configuracionesBanco,"configuracionBanco");
			
			assertTrue("La propiedad configuracionBanco no tiene elementos", configuracionBanco.size() > 0);
			assertTrue("El elemento de configuracionBanco no es un JsonObject", configuracionBanco.get(0).isJsonObject());
			
			configuracionBancoElemento = configuracionBanco.get(0).getAsJsonObject();
		}
		else
			configuracionBancoElemento = configuracionesBanco.get("configuracionBanco").getAsJsonObject();
		
		assertNotNull("El elemento configuracionBancoElemento es nulo", configuracionBancoElemento);
		
		assertTrue("La propiedad Par_FecAct no se encuentra en configuracionBanco", configuracionBancoElemento.has("Par_FecAct"));
		assertTrue("La propiedad Par_Acceso no se encuentra en configuracionBanco", configuracionBancoElemento.has("Par_Acceso"));
		
		logger.info("TEST: Finalizando configuracionBancoConsultarDetalleTestDeberiaSerExitoso metodo");
	}
}
