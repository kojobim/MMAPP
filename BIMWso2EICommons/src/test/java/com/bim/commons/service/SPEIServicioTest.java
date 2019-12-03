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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.bim.commons.service", "com.bim.commons.utils"})
public class SPEIServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(SPEIServicioTest.class);
	
	private static SPEIServicio speiServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		speiServicio = new SPEIServicio();
	}
	
	@Test
	public void horariosSPEIConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando horariosSPEIConsultarTestDeberiaSerExitoso metodo...");
		
		JsonObject datosHorariosSPEI = new JsonObject();
		datosHorariosSPEI.addProperty("Hor_HorIni", "1900-01-01 00:00:00");
		datosHorariosSPEI.addProperty("Hor_HorFin", "1900-01-01 00:00:00");
		datosHorariosSPEI.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		
		/**
		 * String mock = "{\"horarioSPEI\":{\"Hor_HorIni\":\"1900-01-01T02:23:24.000-06:36\",\"Hor_HorFin\":\"1900-01-01T10:53:24.000-06:36\"}}";
		 * JsonObject resultado = Utilerias.fromJsonObject(mock);
		 */
		
		JsonObject resultado = speiServicio.horariosSPEIConsultar(datosHorariosSPEI );
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad horarioSPEI", resultado.has("horarioSPEI"));
		assertTrue("La propiedad horarioSPEI no es un JsonObject", resultado.get("horarioSPEI").isJsonObject());
		
		JsonObject horarioSPEI = Utilerias.obtenerJsonObjectPropiedad(resultado, "horarioSPEI");
		
		assertTrue("No viene la propiedad Hor_HorIni", horarioSPEI.has("Hor_HorIni"));
		assertTrue("No viene la propiedad Hor_HorFin", horarioSPEI.has("Hor_HorFin"));

		logger.info("TEST: Finalizando horariosSPEIConsultarTestDeberiaSerExitoso metodo...");
	}

	@Test
	public void transaferenciaSPEICreacionTestDeberiasSerExitoso() {
		logger.info("TEST: Comenzando transaferenciaSPEICreacionTestDeberiasSerExitoso metodo...");
		JsonObject datosTransfarenciaSPEI = new JsonObject();
		datosTransfarenciaSPEI.addProperty("Trs_Usuari","001844");
		datosTransfarenciaSPEI.addProperty("Trs_Client","00193500");
		datosTransfarenciaSPEI.addProperty("Trs_CueOri","001951710012");
		datosTransfarenciaSPEI.addProperty("Trs_CueDes","012180029483065593");
		datosTransfarenciaSPEI.addProperty("Trs_Monto", 3.0);
		datosTransfarenciaSPEI.addProperty("Trs_Descri","PRUEBA SPEI POSTMAN");
		datosTransfarenciaSPEI.addProperty("Trs_PriRef","Nombre Usuario BE");
		datosTransfarenciaSPEI.addProperty("Trs_Tipo","I");
		datosTransfarenciaSPEI.addProperty("Trs_UsuCap","000149");
		datosTransfarenciaSPEI.addProperty("Trs_TipTra","I");
		datosTransfarenciaSPEI.addProperty("Trs_Frecue","U");
		datosTransfarenciaSPEI.addProperty("Trs_FePrEn","1900-01-01 00:00:00");
		datosTransfarenciaSPEI.addProperty("Trs_DurFec","1900-01-01 00:00:00");
		datosTransfarenciaSPEI.addProperty("FechaSis","2019-05-29 17:25:59");
		datosTransfarenciaSPEI.addProperty("NumTransac","42246953");
		
		/**
		 * String mock = "{\"transferenciaSPEI\":{\"Err_Codigo\":\"000001\",\"Err_Mensaj\":\"La cuenta no se encuentra en su catalogo de cuentas origen\"}}";
		 * JsonObject resultado = Utilerias.fromJsonObject(mock);
		 */
		JsonObject resultado = speiServicio.transferenciaSPEICreacion(datosTransfarenciaSPEI);
		logger.info("- resultado " + resultado);
		
		assertTrue(resultado.has("transferenciaSPEI"));
		assertTrue(resultado.get("transferenciaSPEI").isJsonObject());
		
		JsonObject transferenciaSPEI = Utilerias.obtenerJsonObjectPropiedad(resultado, "transferenciaSPEI");
		
		assertTrue(transferenciaSPEI.has("Err_Codigo"));
		
		String errCodigo = transferenciaSPEI.get("Err_Codigo").getAsString();
		assertTrue("000000".equals(errCodigo));
		
		
		logger.info("TEST: Finalizando transaferenciaSPEICreacionTestDeberiasSerExitoso metodo...");
	}
	
	@Test
	public void transaferenciaSPEIProcesarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando transaferenciaSPEIProcesarTestDeberiaSerExitoso metodo...");
		JsonObject datosTransferenciaSPEI = new JsonObject();
		datosTransferenciaSPEI.addProperty("Trs_UsuAdm", "000149");
		datosTransferenciaSPEI.addProperty("Trs_Usuari", "000149");
		datosTransferenciaSPEI.addProperty("Trs_UsuCli", "00195171");
		datosTransferenciaSPEI.addProperty("Trs_Consec", "0000105506");
		datosTransferenciaSPEI.addProperty("Trs_CueOri", "001951710012");
		datosTransferenciaSPEI.addProperty("Trs_TiCuBe", "40");
		datosTransferenciaSPEI.addProperty("Trs_CueBen", "012180029483065593");
		datosTransferenciaSPEI.addProperty("Trs_Monto", 3.0000);
		datosTransferenciaSPEI.addProperty("Trs_ConPag", "PRUEBA SPEI");
		datosTransferenciaSPEI.addProperty("Trs_Banco", "40012");
		datosTransferenciaSPEI.addProperty("Trs_SegRef", "Nombre Usuario BE");
		datosTransferenciaSPEI.addProperty("Trs_CoCuDe", "0000001815");
		datosTransferenciaSPEI.addProperty("Trs_TCPDir", "127.0.0.1");
		datosTransferenciaSPEI.addProperty("Trs_TipPag", "01");
		datosTransferenciaSPEI.addProperty("Trs_TipTra", "I");
		datosTransferenciaSPEI.addProperty("Trs_ValFir", "S");
		datosTransferenciaSPEI.addProperty("Ban_Descri", "BBVA BANCOMER");
		datosTransferenciaSPEI.addProperty("NumTransac", "42246958");
		datosTransferenciaSPEI.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		JsonObject resultado = speiServicio.transferenciaSPEIProcesar(datosTransferenciaSPEI);
		logger.info("- resultado " + resultado);
		
		assertTrue("El resultado no tienen la propiedad transferenciaSPEI", resultado.has("transferenciaSPEI"));
		assertTrue("La propiedad transferenciaSPEI no es un JsonObject", resultado.get("transferenciaSPEI").isJsonObject());
		
		JsonObject transferenciaSPEI = resultado.get("transferenciaSPEI").getAsJsonObject();
		
		assertTrue("El objeto transferenciaSPEI no tiene la propiedad Err_Codigo", transferenciaSPEI.has("Err_Codigo"));
		assertTrue("El objeto transferenciaSPEI no tiene la propiedad Err_Mensaj", transferenciaSPEI.has("Err_Mensaj"));
		
		String errCodigo = transferenciaSPEI.get("Err_Codigo").getAsString();
		
		assertTrue("La operacion no fue exitosa se obtuvo el error " + errCodigo, errCodigo.equals("000000"));
		logger.info("TEST: Finalizando transaferenciaSPEIProcesarTestDeberiaSerExitoso metodo...");
	}
	
	@Test
	public void transaferenciaSPEIConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando transaferenciaSPEIConsultarTestDeberiaSerExitoso metodo...");
		JsonObject datosTransferenciaSPEI = new JsonObject();
		datosTransferenciaSPEI.addProperty("Trn_UsuAdm", "000014");
		datosTransferenciaSPEI.addProperty("Trn_Usuari", "000014");
		datosTransferenciaSPEI.addProperty("Trn_Status", "A");
		datosTransferenciaSPEI.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		JsonObject resultado = speiServicio.transferenciaSPEIConsultar(datosTransferenciaSPEI );
		logger.info("- resultado " + resultado);
		
		assertTrue("El resultado no tiene la propiedad transferenciasSPEI", resultado.has("transferenciasSPEI"));
		assertTrue("La propiedad transferenciasSPEI no es un JsonObject", resultado.get("transferenciasSPEI").isJsonObject());
		
		JsonObject transferenciasSPEI = resultado.get("transferenciasSPEI").getAsJsonObject();
		
		assertTrue("El objeto transferenciasSPEI no tiene la propiedad transferenciaSPEI", transferenciasSPEI.has("transferenciaSPEI"));
		assertTrue("La propiedad transferenciaSPEI no es un JsonArray", transferenciasSPEI.get("transferenciaSPEI").isJsonArray());
		
		JsonArray transferenciaSPEI = transferenciasSPEI.get("transferenciaSPEI").getAsJsonArray();
		
		assertTrue("El arreglo transferenciaSPEI no tiene elementos",transferenciaSPEI.size() > 0);
		assertTrue("El arreglo transferenciaSPEI no tiene elementos del tipo JsonObject", transferenciaSPEI.get(0).isJsonObject());

		
		JsonObject transferenciaSPEIItem = transferenciaSPEI.get(0).getAsJsonObject(); 
		
		assertTrue("El elemento no tiene la propiedad Trn_Client", transferenciaSPEIItem.has("Trn_Client"));
		assertTrue("El elemento no tiene la propiedad Trn_CueOri",transferenciaSPEIItem.has("Trn_CueOri"));
		assertTrue("El elemento no tiene la propiedad Trn_CueDes",transferenciaSPEIItem.has("Trn_CueDes"));
		assertTrue("El elemento no tiene la propiedad Trn_Monto",transferenciaSPEIItem.has("Trn_Monto"));
		assertTrue("El elemento no tiene la propiedad Trn_Banco",transferenciaSPEIItem.has("Trn_Banco"));
		
		logger.info("TEST: Finalizando transaferenciaSPEIConsultarTestDeberiaSerExitoso metodo...");
	}
}
