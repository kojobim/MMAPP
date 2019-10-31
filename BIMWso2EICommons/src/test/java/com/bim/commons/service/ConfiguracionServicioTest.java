package com.bim.commons.service;

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
public class ConfiguracionServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(ConfiguracionServicioTest.class);
	
	private static ConfiguracionServicio configuracionServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		configuracionServicio = new ConfiguracionServicio();
	}
	
	@Test
	public void horariosConsultarTestDeberiaConsultar() {
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
		datoshorariosConsultar.addProperty("SucOrigen", "");
		datoshorariosConsultar.addProperty("SucDestino", "");
		datoshorariosConsultar.addProperty("Modulo", "");
	
		configuracionServicio.horariosConsultar(datoshorariosConsultar);
		
	}
	
}
