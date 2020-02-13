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
public class CuentaServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(CuentaServicioTest.class);
	
	private static CuentaServicio cuentaServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		cuentaServicio = new CuentaServicio();
	}
	
	@Test
	public void cuentaOrigenConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando cuentaOrigenConsultarTestDeberiaSerExitoso metodo...");
		/*
		 * CONSULTA LC
		 */
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaOrigenConsultar = new JsonObject();
		datosCuentaOrigenConsultar.addProperty("Cor_Usuari", "000014");
		datosCuentaOrigenConsultar.addProperty("Cor_Cuenta", "");
		datosCuentaOrigenConsultar.addProperty("Cor_Moneda", "");
		datosCuentaOrigenConsultar.addProperty("Cor_CliUsu", "");
		datosCuentaOrigenConsultar.addProperty("Usu_SucMod", "");
		datosCuentaOrigenConsultar.addProperty("Tip_Consul", "LC");
		datosCuentaOrigenConsultar.addProperty("NumTransac", "");
		datosCuentaOrigenConsultar.addProperty("Transaccio", "HKT");
		datosCuentaOrigenConsultar.addProperty("Usuario", "000100");
		datosCuentaOrigenConsultar.addProperty("FechaSis", fechaSis);
		datosCuentaOrigenConsultar.addProperty("SucOrigen", "001");
		datosCuentaOrigenConsultar.addProperty("SucDestino", "001");
		datosCuentaOrigenConsultar.addProperty("Modulo", "NB");
		
		JsonObject resultado = cuentaServicio.cuentaOrigenConsultar(datosCuentaOrigenConsultar);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuentas", resultado.has("cuentas"));
		assertTrue("La propiedad cuentas no es un JsonObject", resultado.get("cuentas").isJsonObject());
		
		JsonObject cuentasObjeto = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuentas");
		
		assertTrue("No viene la propiedad cuenta", cuentasObjeto.has("cuenta"));
		assertTrue("La propiedad cuenta no es un JsonArray", cuentasObjeto.get("cuenta").isJsonArray());
		
		JsonArray cuentasArray = Utilerias.obtenerJsonArrayPropiedad(cuentasObjeto, "cuenta");
		
		if(!cuentasArray.isJsonNull()) {			
			assertTrue("La propiedad cuenta no tiene elementos", cuentasArray.size() > 0);
			assertTrue("El elemento de cuenta no es un JsonObject", cuentasArray.get(0).isJsonObject());
			
			JsonObject cuentasElemento = cuentasArray.get(0).getAsJsonObject();
			
			assertTrue("La propiedad Cor_Cuenta no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_Cuenta"));
			assertTrue("La propiedad Cor_Produc no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_Produc"));
			assertTrue("La propiedad Cor_DeCuOr no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_DeCuOr"));
			assertTrue("La propiedad Cue_Moneda no se encuentra en el elemeto cuenta", cuentasElemento.has("Cue_Moneda"));
			assertTrue("La propiedad Cor_Tipo no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_Tipo"));
			assertTrue("La propiedad Cue_Dispon no se encuentra en el elemeto cuenta", cuentasElemento.has("Cue_Dispon"));
			assertTrue("La propiedad Cor_TipCli no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_TipCli"));
			assertTrue("La propiedad Cor_NoCuOr no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_NoCuOr"));
			assertTrue("La propiedad Cor_Alias no se encuentra en el elemeto cuenta", cuentasElemento.has("Cor_Alias"));
			assertTrue("La propiedad Cue_NumFor no se encuentra en el elemeto cuenta", cuentasElemento.has("Cue_NumFor"));
			assertTrue("La propiedad Tip_Descri no se encuentra en el elemeto cuenta", cuentasElemento.has("Tip_Descri"));
		} else {
			assertNotNull("El elemento cuenta es nulo", cuentasArray);
		}
		
		/*
		 * CONSULTA C1
		 */
		datosCuentaOrigenConsultar.addProperty("Cor_Cuenta", "001953830040");
		datosCuentaOrigenConsultar.addProperty("Tip_Consul", "C1");
		
		resultado = cuentaServicio.cuentaOrigenConsultar(datosCuentaOrigenConsultar);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad cuenta", resultado.has("cuenta"));
		assertTrue("La propiedad cuenta no es un JsonObject", resultado.get("cuenta").isJsonObject());
		
		JsonObject cuenta = Utilerias.obtenerJsonObjectPropiedad(resultado, "cuenta");
		
		if(!cuenta.isJsonNull()) {
			assertTrue("La propiedad Cor_Cuenta no se encuentra en cuentaDestino", cuenta.has("Cor_Cuenta"));
			assertTrue("La propiedad Cor_Status no se encuentra en cuentaDestino", cuenta.has("Cor_Status"));
			assertTrue("La propiedad Cor_Alias no se encuentra en cuentaDestino", cuenta.has("Cor_Alias"));
			assertTrue("La propiedad Cor_MoLiDi no se encuentra en cuentaDestino", cuenta.has("Cor_MoLiDi"));
			assertTrue("La propiedad Cor_MonDia no se encuentra en cuentaDestino", cuenta.has("Cor_MonDia"));
			assertTrue("La propiedad Cue_Moneda no se encuentra en cuentaDestino", cuenta.has("Cue_Moneda"));
			assertTrue("La propiedad Cli_Tipo no se encuentra en cuentaDestino", cuenta.has("Cli_Tipo"));
		} else {
			assertNotNull("El elemento cuenta es nulo", cuenta);
		}
		
		logger.info("TEST: Finalizando cuentaDestinoSPEIConsultarTestDeberiaSerExitoso metodo...");
	}
}