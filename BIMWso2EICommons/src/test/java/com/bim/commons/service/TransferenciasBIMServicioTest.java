package com.bim.commons.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class TransferenciasBIMServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(TransferenciasBIMServicioTest.class);
	
	private static TransferenciasBIMServicio transferenciasBIMServicio;
	

	@BeforeClass
	public static void init() {
		logger.info("init...");
		transferenciasBIMServicio = new TransferenciasBIMServicio();
	}
	
	@Test
	public void cuentasOrigenConsultarTestDeberiaSerExitoso() {
		logger.info("TEST:  Comenzando cuentasOrigenConsultarTestDeberiaSerExitoso metodo");
		
		JsonObject datosCuentasOrigenConsultar = new JsonObject();
		datosCuentasOrigenConsultar.addProperty("Cor_Usuari", "000014");
		datosCuentasOrigenConsultar.addProperty("FechaSis", "2019-05-29 14:23:41");
		
		/**
		 *	Mockup Test
		 * 	String json= "{\"cuentasOrigen\":{\"cuentaOrigen\":[{\"Cor_Cuenta\":\"001953830023\",\"Cor_Produc\":\"\",\"Cor_CueFor\":\"001-95383-002-3 CUENTA EJE\",\"Cor_Alias\":\"CUENTA EJE\",\"Cli_ComOrd\":\"Nombre Ordenado\",\"Cue_Dispon\":\"95995.1300\",\"Cue_Moneda\":\"01\",\"Cue_Tipo\":\"02\",\"Cor_Tipo\":\"1\",\"Tip_Linea\":\"\",\"Cor_CheEmp\":\"N\"},{\"Cor_Cuenta\":\"001953830040\",\"Cor_Produc\":\"\",\"Cor_CueFor\":\"001-95383-004-0 AHORRO CHARLIE\",\"Cor_Alias\":\"AHORRO CHARLIE\",\"Cli_ComOrd\":\"Nombre Ordenado\",\"Cue_Dispon\":\"0.0000\",\"Cue_Moneda\":\"01\",\"Cue_Tipo\":\"02\",\"Cor_Tipo\":\"1\",\"Tip_Linea\":\"\",\"Cor_CheEmp\":\"N\"}]}}";
		 * 	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 * Test
		 */
		JsonObject resultado = transferenciasBIMServicio.cuentasOrigenConsultar(datosCuentasOrigenConsultar);
		logger.info("- resultado " + resultado);
		
		assertTrue(resultado.has("cuentasOrigen"));
		assertTrue(resultado.get("cuentasOrigen").isJsonObject());
		
		JsonObject cuentasOrigen = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentasOrigen");
		
		assertTrue(cuentasOrigen.has("cuentaOrigen"));
		assertTrue(cuentasOrigen.get("cuentaOrigen").isJsonArray());
		
		JsonArray cuentaOrigen = cuentasOrigen.get("cuentaOrigen").getAsJsonArray();
		
		assertTrue(cuentaOrigen.size() > 0);
		assertTrue(cuentaOrigen.get(0).isJsonObject());
		
		JsonObject cuentaOrigenItem = cuentaOrigen.get(0).getAsJsonObject();
		
		assertTrue(cuentaOrigenItem.has("Cor_Cuenta"));
		assertTrue(cuentaOrigenItem.has("Cor_CueFor"));
		assertTrue(cuentaOrigenItem.has("Cue_Tipo"));
		
		
		logger.info("TEST:  Terminando cuentasOrigenConsultarTestDeberiaSerExitoso metodo");
	}
	
	@Test
	public void cuentaDestinoTransferenciaBIMActivacionTestDeberiaSerExitoso() {
		logger.info("TEST:  Empezando cuentaDestinoTransferenciaBIMActivacionTestDeberiaSerExitoso metodo");
		
		JsonObject datosCuentaDestinoTransferenciaBIMActivacion = new JsonObject();
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Cdb_UsuAdm", "000149");
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("NumTransac", "42246895");
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("FechaSis", "2019-05-29 14:23:41");
		
		JsonObject resultado = transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion );
		logger.info("- resultado " + resultado);
		
		assertTrue(resultado.has("cuentaDestinoTransferenciaBIM"));
		assertTrue(resultado.get("cuentaDestinoTransferenciaBIM").isJsonObject());
		
		JsonObject cuentaDestinoTransferenciaBIM = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentaDestinoTransferenciaBIM");
		
		assertTrue(cuentaDestinoTransferenciaBIM.has("Err_Codigo"));
		assertTrue(cuentaDestinoTransferenciaBIM.get("Err_Codigo").isJsonPrimitive());
		
		String errCodigo = Utilerias.obtenerStringPropiedad(cuentaDestinoTransferenciaBIM, "Err_Codigo");
		
		assertTrue("000000".equals(errCodigo));
		
		
		logger.info("TEST:  Terminando cuentaDestinoTransferenciaBIMActivacionTestDeberiaSerExitoso metodo");
	}
}
