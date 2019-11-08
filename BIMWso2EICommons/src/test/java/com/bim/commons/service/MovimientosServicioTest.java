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
public class MovimientosServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(MovimientosServicioTest.class);
	
	private static MovimientosServicio movimientosServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		movimientosServicio = new MovimientosServicio();
	}
	
	@Test
	public void movimientosListadoTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando movimientosListadoTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosMovimientosListado = new JsonObject();
		datosMovimientosListado.addProperty("Cue_Numero", "001953830023");
		datosMovimientosListado.addProperty("Fec_Inicia", "2019-10-01 00:00:00");
		datosMovimientosListado.addProperty("Fec_Final", "2019-10-31 00:00:00");
		datosMovimientosListado.addProperty("Mov_Natura", "");
		datosMovimientosListado.addProperty("Mov_PalCla", "");
		datosMovimientosListado.addProperty("Mov_MonIni", "0");
		datosMovimientosListado.addProperty("Mov_MonFin", "0");
		datosMovimientosListado.addProperty("Mov_Clasif", "");
		datosMovimientosListado.addProperty("Tip_Consul", "C1");
		datosMovimientosListado.addProperty("NumTransac", "");
		datosMovimientosListado.addProperty("Transaccio", "HNV");
		datosMovimientosListado.addProperty("Usuario", "000100");
		datosMovimientosListado.addProperty("FechaSis", fechaSis);
		datosMovimientosListado.addProperty("SucOrigen", "001");
		datosMovimientosListado.addProperty("SucDestino", "001");
		datosMovimientosListado.addProperty("Modulo", "NB");
		
		/* 
		 *	Mockup Test
		 * 	String json = "{\"cuenta\":{\"movimientos\":[{\"Mov_Cuenta\":\"String\",\"Mov_Numero\":\"String\",\"Mov_Consec\":\"String\",\"Mov_Natura\":\"String\",\"Fecha_Val\":\"String\",\"Mov_Descri\":\"String\",\"Mov_Refere\":\"String\",\"Mov_Cantid\":\"Double\",\"Usuario\":\"String\",\"Transaccio\":\"String\",\"Mov_Saldo\":\"Double\",\"Mov_Tipo\":\"String\",\"Mov_Clasif\":\"String\",\"Mov_DesCla\":\"String\"}]}}";
		 * 	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
         * Test
         */
		JsonObject resultado = movimientosServicio.movimientosListado(datosMovimientosListado);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuenta", resultado.has("cuenta"));
		assertTrue("La propiedad cuenta no es un JsonObject", resultado.get("cuenta").isJsonObject());
		
		JsonObject cuenta = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuenta");
		
		assertTrue("No viene la propiedad movimientos", cuenta.has("movimientos"));
		assertTrue("La propiedad movimientos no es un JsonArray", cuenta.get("movimientos").isJsonArray());
		
		JsonObject movimientosElemento = null;
		if(cuenta.get("movimientos").isJsonArray()) {
			JsonArray movimientos = Utilerias.obtenerJsonArrayPropiedad(cuenta, "movimientos");
			
			assertTrue("La propiedad movimientos no tiene elementos", movimientos.size() > 0);
			assertTrue("El elemento de movimientos no es un JsonObject", movimientos.get(0).isJsonObject());
			
			movimientosElemento = movimientos.get(0).getAsJsonObject();
			
			assertTrue("La propiedad Mov_Cuenta no se encuentra en movimientos", movimientosElemento.has("Mov_Cuenta"));
			assertTrue("La propiedad Mov_Descri no se encuentra en movimientos", movimientosElemento.has("Mov_Descri"));
		}else
			assertNotNull("El elemento movimientosElemento es nulo", movimientosElemento);
		
		logger.info("TEST: Finalizando movimientosListadoTestDeberiaSerExitoso metodo...");
	}

}
