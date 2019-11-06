package com.bim.commons.service;

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
public class ClienteServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(ClienteServicioTest.class);
	
	private static ClienteServicio clienteServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		clienteServicio = new ClienteServicio();
	}
	
	@Test
	public void clienteConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Empezando clienteConsultarTestDeberiaSerExitoso metodo");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosClienteConsultar = new JsonObject();
		datosClienteConsultar.addProperty("Cli_Numero", "00193500");
		datosClienteConsultar.addProperty("Tip_Consul", "C1");
		datosClienteConsultar.addProperty("Transaccio", "CCL");
		datosClienteConsultar.addProperty("Usuario", "000100");
		datosClienteConsultar.addProperty("FechaSis", fechaSis);
		datosClienteConsultar.addProperty("SucOrigen", "001");
		datosClienteConsultar.addProperty("SucDestino", "001");
		datosClienteConsultar.addProperty("Modulo", "NB");
		
	 	/* 
		 *	Mockup Test
		 *	String json = "{\"cliente\":{\"Cli_Numero\":\"String\",\"Cli_Sucurs\":\"String\",\"Cli_Tipo\":\"String\",\"Cli_Sector\":\"String\",\"Cli_Activi\":\"String\",\"Cli_ActINE\":\"String\",\"Tit_Numero\":\"String\",\"Cli_Titulo\":\"String\",\"Cli_Nombre\":\"String\",\"Cli_ApePat\":\"String\",\"Cli_ApeMat\":\"String\",\"Cli_RazSoc\":\"String\",\"Cli_Comple\":\"String\",\"Cli_ComOrd\":\"String\",\"Cli_RFC\":\"String\",\"Cli_CURP\":\"String\",\"Cli_Calle\":\"String\",\"Cli_CalNum\":\"String\",\"Cli_NumExt\":\"String\",\"Cli_Coloni\":\"String\",\"Cli_Entida\":\"String\",\"Cli_Locali\":\"String\",\"Cli_Pais\":\"String\",\"Cli_CodPos\":\"String\",\"Cli_ApaPos\":\"String\",\"Cli_Consec\":\"Integer\",\"Cli_CobISR\":\"String\",\"Cli_Promot\":\"String\",\"Cli_ProAsi\":\"String\",\"Cli_ProOri\":\"String\",\"Cli_Telefo\":\"String\",\"Cli_Fax\":\"String\",\"Cli_Status\":\"String\",\"Cli_ClaCar\":\"String\",\"Cli_Clasif\":\"String\",\"Cli_Contac\":\"String\",\"Cli_ConCon\":\"Integer\",\"Cli_CobIVA\":\"String\",\"Cli_DesAct\":\"String\",\"Cli_Nacion\":\"String\",\"Cli_GruCor\":\"String\",\"Cli_NomCor\":\"String\",\"Cli_PaiRes\":\"String\",\"Cli_TasISR\":\"Decimal\",\"Cli_ActEmp\":\"String\",\"Cli_Grupo\":\"String\",\"Cli_Corpor\":\"String\",\"Cli_InsChe\":\"String\",\"Cli_ClaEco\":\"String\",\"Cli_Fecha\":\"Date\",\"FechaSis\":\"Date\",\"Cda_Tamano\":\"String\",\"Cda_TiDeRe\":\"Integer\",\"Cda_Usuari\":\"String\",\"Cda_DesEsp\":\"String\",\"Cda_NumEmp\":\"Integer\",\"Cli_SucAti\":\"String\",\"Adi_Email\":\"String\",\"Loc_Nombre\":\"String\",\"Ent_Nombre\":\"String\",\"Pai_Nombre\":\"String\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = clienteServicio.clienteConsultar(datosClienteConsultar);
		logger.info("- resultado " + resultado );

		assertTrue("No viene la propiedad cliente" ,resultado.has("cliente"));
		
		JsonObject cliente  = Utilerias.obtenerJsonObjectPropiedad(resultado, "cliente");
		
		assertTrue(cliente != null);
		
		assertTrue(cliente.has("Cli_Numero"));
		logger.info("TEST: Terminando clienteConsultarTestDeberiaSerExitoso metodo");
	}
}
