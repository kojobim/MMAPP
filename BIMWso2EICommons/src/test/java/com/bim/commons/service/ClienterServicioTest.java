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
public class ClienterServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(ClienterServicioTest.class);
	
	private static ClienteServicio clienteServicio;
	
	@BeforeClass
	public static void innit() {
		logger.info("init...");
		clienteServicio = new ClienteServicio();
	}
	
	@Test
	public void clienteConsultarTestDeberiaConsular() {
		String fechaSis = Utilerias.getFechaSis();
		JsonObject datosClienteConsultar = new JsonObject();
		datosClienteConsultar.addProperty("Cli_Numero", "00193500");
		datosClienteConsultar.addProperty("Tip_Consul", "C1");
		datosClienteConsultar.addProperty("Transaccio", "CCL");
		datosClienteConsultar.addProperty("Usuario", "000100");
		datosClienteConsultar.addProperty("FechaSis", fechaSis);
		datosClienteConsultar.addProperty("SucOrigen", "001");
		datosClienteConsultar.addProperty("SucDestino", "001");
		datosClienteConsultar.addProperty("Modulo", "NB");
		
		clienteServicio.clienteConsultar(datosClienteConsultar);
	}
}
