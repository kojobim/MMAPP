package com.bim.commons.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.exceptions.BadRequestException;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.bim.commons.service", "com.bim.commons.utils"})
public class SoapServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(SoapServicioTest.class);
	
	private static SoapServicio soapServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		soapServicio = new SoapServicio();
	}
	
	@Test(expected = BadRequestException.class)
	public void movimientosEnvioCorreoTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando movimientosEnvioCorreoTestDeberiaSerExitoso metodo...");

		String anio = "2019";
		String cliente = null;//"04099972";
		String mes = "02";

		/*
         * Test
         */
		soapServicio.movimientosEnvioCorreo(anio, mes, cliente);
		
		logger.info("TEST: Finalizando movimientosEnvioCorreoTestDeberiaSerExitoso metodo...");
	}

}
