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
		datosTransfarenciaSPEI.addProperty("Trs_Usuari","000149");
		datosTransfarenciaSPEI.addProperty("Trs_Client","00195171");
		datosTransfarenciaSPEI.addProperty("Trs_CueOri","001951710012");
		datosTransfarenciaSPEI.addProperty("Trs_CueDes","012180029483065593");
		datosTransfarenciaSPEI.addProperty("Trs_Monto", 3);
		datosTransfarenciaSPEI.addProperty("Trs_Descri","PRUEBA SPEI");
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
		 * String mock = "{\"REQUEST_STATUS\":\"SUCCESSFUL\"}";
		 * JsonObject resultado = Utilerias.fromJsonObject(mock);
		 */
		JsonObject resultado = speiServicio.transaferenciaSPEICreacion(datosTransfarenciaSPEI);
		logger.info("- resultado " + resultado);
		
		assertTrue(resultado.has("REQUEST_STATUS"));
		assertTrue(resultado.get("REQUEST_STATUS").getAsString().equals("SUCCESSFUL"));
		
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
		JsonObject resultado = speiServicio.transaferenciaSPEIProcesar(datosTransferenciaSPEI);
		logger.info("- resultado " + resultado);
		
		assertTrue("El resultado no tienen la propiedad transaccionSPEI", resultado.has("transaccionSPEI"));
		assertTrue("La propiedad transaccionSPEI no es un JsonObject", resultado.get("transaccionSPEI").isJsonObject());
		
		JsonObject transaccionSPEI = resultado.get("transaccionSPEI").getAsJsonObject();
		
		assertTrue("El objeto transaccionSPEI no tiene la propiedad Err_Codigo", transaccionSPEI.has("Err_Codigo"));
		assertTrue("El objeto transaccionSPEI no tiene la propiedad Err_Mensaj", transaccionSPEI.has("Err_Mensaj"));
		
		String errCodigo = transaccionSPEI.get("Err_Codigo").getAsString();
		
		assertTrue("La operacion no fue exitosa se obtuvo el error " + errCodigo, errCodigo.equals("000000"));
		logger.info("TEST: Finalizando transaferenciaSPEIProcesarTestDeberiaSerExitoso metodo...");
	}
}
