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
	
		/*
		 * Mockup Test
		 * String json = "{\"horariosInversion\":[{\"EsHorariID\":\"Integer\",\"Hor_Numero\":\"Integer\",\"Hor_TipMod\":\"String\",\"Hor_TipHor\":\"String\",\"Hor_HorIni\":\"Date\",\"Hor_HorFin\":\"Date\",\"Hor_DiaHab\":\"String\"}]}";
		 * JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
         */
		
		/*
		 * Test
		 */
		JsonObject resultado = configuracionServicio.horariosConsultar(datoshorariosConsultar);
		logger.info("- resultado " + resultado);
		
		assertTrue("No viene la propiedad horariosInversion", resultado.has("horariosInversion"));
		assertTrue("La propiedad horariosInversion no es un JsonArray o un JsonObject", resultado.get("horariosInversion").isJsonArray() || resultado.get("horariosInversion").isJsonObject());
		
		JsonObject horariosInversionElemento = null;
		if(resultado.get("horariosInversion").isJsonArray()) {
			JsonArray horariosInversion = Utilerias.obtenerJsonArrayPropiedad(resultado, "horariosInversion");
			
			assertTrue("La propiedad horariosInversion no tiene elementos", horariosInversion.size() > 0);
			assertTrue("El elemento de horariosInversion no es un JsonObject", horariosInversion.get(0).isJsonObject());
			
			horariosInversionElemento = horariosInversion.get(0).getAsJsonObject();
			
			assertTrue("La propiedad EsHorariID no se encuentra en horariosInversion", horariosInversionElemento.has("EsHorariID"));
			assertTrue("La propiedad Hor_Numero no se encuentra en horariosInversion", horariosInversionElemento.has("Hor_Numero"));
		}
		else		
			assertNotNull("El elemento horariosInversion es nulo", horariosInversionElemento);

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

		/*
         *	Mockup Test
         *	String json = "{\"configuracionesBanco\":{\"configuracionBanco\":[{\"Par_FecAct\":\"Date\",\"Par_FeOpEs\":\"Date\",\"Par_Acceso\":\"String\",\"Par_HoInSP\":\"Date\",\"Par_FeDiNa\":\"Date\",\"Par_HoCaPI\":\"Date\",\"Par_MiCuDe\":\"Integer\",\"Par_RECAMo\":\"String\",\"Par_RECAFi\":\"String\",\"Par_RECAFA\":\"String\"}]}}";
         *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
         */
		
		/*
         *	Test
         */
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
			
			assertTrue("La propiedad Par_FecAct no se encuentra en configuracionBanco", configuracionBancoElemento.has("Par_FecAct"));
			assertTrue("La propiedad Par_Acceso no se encuentra en configuracionBanco", configuracionBancoElemento.has("Par_Acceso"));
		}
		else
			assertNotNull("El elemento configuracionBancoElemento es nulo", configuracionBancoElemento);
		
		
		logger.info("TEST: Finalizando configuracionBancoConsultarDetalleTestDeberiaSerExitoso metodo");
	}
	
	@Test
	public void informacionSucursalObtenerTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando informacionSucursalObtenerTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", "001");
		datosSucursal.addProperty("Tip_Consul", "");
		datosSucursal.addProperty("NumTransac", "");
		datosSucursal.addProperty("Transaccio", "CPE");
		datosSucursal.addProperty("Usuario", "000100");
		datosSucursal.addProperty("FechaSis", fechaSis);
		datosSucursal.addProperty("SucOrigen", "001");
		datosSucursal.addProperty("SucDestino", "001");
		datosSucursal.addProperty("Modulo", "NB");

		/*
		 *	Mockup Test
		 *	String json = "{\"informacionSucursal\":{\"Par_Sucurs\":\"String\",\"Par_CheCaj\":\"Integer\",\"Par_IVA\":\"Double\",\"Par_ISR\":\"Double\",\"Par_DiBaIn\":\"Integer\",\"Par_DiBaCr\":\"Integer\",\"Par_DiBaCh\":\"Integer\",\"Par_ChLey1\":\"String\",\"Par_ChLey2\":\"String\",\"Par_ChLey3\":\"String\",\"Par_CheCer\":\"String\",\"Par_DiaRem\":\"Integer\",\"Par_LimAut\":\"Double\",\"Par_TranBR\":\"String\",\"Par_CliInd\":\"Integer\",\"Par_BanFol\":\"Integer\",\"Par_FecAct\":\"Date\",\"Par_CoCoIn\":\"Double\",\"Par_CoReme\":\"Double\",\"Par_OpeBan\":\"Integer\",\"Par_ConPap\":\"Integer\",\"Par_MonCom\":\"String\",\"Par_LinSob\":\"String\",\"Par_EnvSPE\":\"Integer\",\"Par_Banco\":\"String\",\"Par_ComRem\":\"String\",\"Par_IvaRem\":\"String\",\"Par_RemExt\":\"String\",\"Par_IVAREX\":\"String\",\"Par_GiBaEx\":\"String\",\"Par_GiBaNa\":\"String\",\"Par_IVAGBN\":\"String\",\"Par_IVAGBE\":\"String\",\"Par_ComSPE\":\"String\",\"Par_IVASPE\":\"String\",\"Par_NumTes\":\"String\",\"Par_IncISR\":\"String\",\"Par_InsISR\":\"String\",\"Par_SPEUA\":\"String\",\"Par_SIAC\":\"String\",\"Par_CobInm\":\"String\",\"Par_FecRem\":\"Date\",\"Par_IntBan\":\"String\",\"Par_Compan\":\"String\",\"Tip_Fecha\":\"String\",\"Suc_UltDia\":\"String\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/*
		 *	Test
		 */
		JsonObject resultado = configuracionServicio.informacionSucursalObtener(datosSucursal);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad informacionSucursal", resultado.has("informacionSucursal"));
		assertTrue("La propiedad informacionSucursal no es un JsonObject", resultado.get("informacionSucursal").isJsonObject());
		
		JsonObject informacionSucursal = Utilerias.obtenerJsonObjectPropiedad(resultado, "informacionSucursal");
		
		if(!resultado.get("informacionSucursal").isJsonNull()) {
			assertTrue("La propiedad Par_Sucurs no se encuentra en informacionSucursal", informacionSucursal.has("Par_Sucurs"));
			assertTrue("La propiedad Par_ISR no se encuentra en informacionSucursal", informacionSucursal.has("Par_ISR"));
		}else 
			assertNotNull("informacionSucursal es nulo", informacionSucursal);
		
		logger.info("TEST: Finalizando informacionSucursalObtenerTestDeberiaSerExitoso metodo...");
	}
}
