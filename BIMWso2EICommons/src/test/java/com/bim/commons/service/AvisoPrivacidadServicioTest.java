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
import com.google.gson.JsonObject;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.bim.commons.service", "com.bim.commons.utils"})
public class AvisoPrivacidadServicioTest {

	private static final Logger logger = LoggerFactory.getLogger(AvisoPrivacidadServicioTest.class);
	
	private static AvisoPrivacidadServicio avisoPrivacidadServicio;
	
	@BeforeClass
	public static void init() {
		logger.info("init...");
		avisoPrivacidadServicio = new AvisoPrivacidadServicio();
	}
	
	@Test
	public void avisoPrivacidadActualizacionTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando avisoPrivacidadActualizacionTestDeberiaSerExitoso metodo...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Usu_Numero", "000002");
		datosAvisoPrivacidad.addProperty("Usu_Client", "00195421");
		datosAvisoPrivacidad.addProperty("Usu_AceAvi", 1);
		datosAvisoPrivacidad.addProperty("Usu_FecAce", "2019-10-14 09:40:44");
		datosAvisoPrivacidad.addProperty("Usu_FecAct", "2019-10-14 09:40:44");
		datosAvisoPrivacidad.addProperty("NumTransac", "999999");
		datosAvisoPrivacidad.addProperty("Transaccio", "ABE");
		datosAvisoPrivacidad.addProperty("Usuario", "000100");
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		datosAvisoPrivacidad.addProperty("SucOrigen", "001");
		datosAvisoPrivacidad.addProperty("SucDestino", "001");
		datosAvisoPrivacidad.addProperty("Modulo", "BM");
		
		/**
		 *	Mockup Test
		 *	String json = "{\"avisoPrivacidad\":{\"Err_Codigo\":\"String\",\"Err_Mensaj\":\"String\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 *	Test
		 */
		JsonObject resultado = avisoPrivacidadServicio.avisoPrivacidadActualizacion(datosAvisoPrivacidad);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad avisoPrivacidad", resultado.has("avisoPrivacidad"));
		assertTrue("La propiedad avisoPrivacidad no es un JsonObject", resultado.get("avisoPrivacidad").isJsonObject());
		
		JsonObject avisoPrivacidad = Utilerias.obtenerJsonObjectPropiedad(resultado, "avisoPrivacidad");
		
		if(!resultado.get("avisoPrivacidad").isJsonNull()) {
			assertTrue("La propiedad Err_Codigo no se encuentra en avisoPrivacidad", avisoPrivacidad.has("Err_Codigo"));
			assertTrue("La propiedad Err_Mensaj no se encuentra en avisoPrivacidad", avisoPrivacidad.has("Err_Mensaj"));
		}else 
			assertNotNull("la propiedad avisoPrivacidad es nula", avisoPrivacidad);
		
		logger.info("TEST: Finalizando avisoPrivacidadActualizacionTestDeberiaSerExitoso metodo...");
	}

	@Test
	public void avisoPrivacidadConsultarTestDeberiaSerExitoso() {
		logger.info("TEST: Comenzando avisoPrivacidadConsultarTestDeberiaSerExitoso método...");
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Con_Aviso", "1");
		datosAvisoPrivacidad.addProperty("NumTransac", "999999");
		datosAvisoPrivacidad.addProperty("Transaccio", "ABE");
		datosAvisoPrivacidad.addProperty("Usuario", "000100");
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		datosAvisoPrivacidad.addProperty("SucOrigen", "001");
		datosAvisoPrivacidad.addProperty("SucDestino", "001");
		datosAvisoPrivacidad.addProperty("Modulo", "Nb");
		
		/**
		 *	Mockup Test
		 *	String json = "{\"avisoPrivacidad\":{\"Text_Aviso\":\"<div>
    <br />
        <br />
        <br />
        <br />  <div><h4><b>&nbsp&nbsp&nbsp&nbsp A.Identidad y domicilio del Responsable</b></h4> </div>
                <div>
                    <p style="text-align: justify; margin-left: 50px; margin-right: 30px; border: transparent 20px solid;">
                        En virtud de lo dispuesto por la Ley Federal de Protecci&oacute;n de Datos Personales en Posesi&oacute;n
                        de los Particulares (en adelante, la &ldquo;LFPD&rdquo;) y resto de disposiciones aplicables,<b>Banco
                       Inmobiliario Mexicano, Sociedad An&oacute;nima, Instituci&oacute;n de Banca M&uacute;ltiple </b> (en adelante
                        e indistintamente <b>&ldquo;BIM&rdquo;</b> o el &ldquo;Responsable&rdquo;), con domicilio en Calle R&iacute;o Elba N&uacute;mero 20,
                        tercer piso, Colonia Cuauht&eacute;moc, Ciudad de M&eacute;xico, C&oacute;digo Postal 06500; le informa de
                        manera expresa:
                    </p>
                
                </div>
    <div><h4><b>&nbsp&nbsp&nbsp&nbsp B. Finalidades del tratamiento</b></h4> </div>
             
    <div>
        <p style="text-align: justify; margin-left: 50px; margin-right: 30px; border: transparent 20px solid;">
            <b>a) Finalidades originarias y necesarias</b>
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            1. Realizar an&aacute;lisis de perfiles de prospectos y de clientes para determinar el grado de
               riesgo, antes y durante la contrataci&oacute;n de servicios y adquisici&oacute;n de productos
               financieros del Responsable.
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            2. Gesti&oacute;n, control, seguimiento y administraci&oacute;n de los servicios contratados y de los
               productos financieros adquiridos.
        </p>
        <p style="text-align: justify;margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            3. Gesti&oacute;n, control, administraci&oacute;n y actualizaci&oacute;n de informaci&oacute;n de nuestros clientes:
               personas f&iacute;sicas o representantes legales y contactos designados por personas
               morales.
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            4. Gesti&oacute;n, control y administraci&oacute;n de las comunicaciones entre el Responsable y sus
            clientes.
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            5. Cobro judicial o extrajudicial de obligaciones adquiridas por nuestros clientes frente al
               Responsable.
        </p>
        <p style="text-align: justify;margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            6. Proporcionar servicios generales y/o personalizados de atenci&oacute;n a clientes, para
            asegurar y monitorizar la calidad de los servicios y la satisfacci&oacute;n de nuestros clientes.
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            7. Cumplimiento de obligaciones y requerimientos legales, derivados de la normativa
               sectorial sobre protecci&oacute;n al consumidor, protecci&oacute;n de los usuarios de servicios
               financieros, confidencialidad, honorabilidad, transparencia y sobre prevenci&oacute;n de
               actividades il&iacute;citas, mediante la implementaci&oacute;n de procedimientos administrativos y
               t&eacute;cnicos establecidos por el Responsable para tales efectos.
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            8. En su caso, cumplimiento de obligaciones y requerimientos judiciales o administrativos
              debidamente fundados y motivados.
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            9. Estad&iacute;stica y registro hist&oacute;rico de nuestros clientes y prospectos.
        </p>
         <p style="text-align: justify; margin-left: 50px; margin-right: 30px; border: transparent 20px solid;">
            <b>b) Finalidades adicionales</b>
        </p>
        <p style="text-align: justify; margin-top:-50px; margin-left: 70px; margin-right: 30px; border: transparent 20px solid;">
            1. Env&iacute;o de comunicaciones comerciales y publicitarias sobre ofertas y lanzamiento de
               nuevos servicios o productos financieros del Responsable, as&iacute; como invitaciones para
               eventos organizados por el Responsable.
        </p>

    </div>
    <div><h4><b>&nbsp&nbsp&nbsp&nbsp C. Mecanismos para conocer el Aviso de Privacidad:</b></h4> </div>
             
                <div>
                    <p style="text-align: justify; margin-left: 50px; margin-right: 30px; border: transparent 20px solid;">
                        Para mayor informaci&oacute;n acerca del tratamiento de sus datos personales y de los derechos
                        que puede hacer valer, puede acceder al Aviso de Privacidad Integral correspondiente a
                        trav&eacute;s del sitio web  <a href="https://www.bim.mx/Portal/?id_category=1">www.bim.mx</a> secci&oacute;n                
                                               &ldquo;Avisos de Privacidad&rdquo;, o solicitarlo en el correo
                        electr&oacute;nico <a href="mailto:datospersonales@bim.mx">datospersonales@bim.mx</a>
                    </p>
                
                </div>
    </div>\"}}";
		 *	JsonObject resultado = new Gson().fromJson(json, JsonObject.class);
		 */
		
		/**
		 *	Test
		 */
		JsonObject resultado = avisoPrivacidadServicio.avisoPrivacidadConsultar(datosAvisoPrivacidad);
		logger.info("- resultado: "+ resultado);
		
		assertTrue("No viene la propiedad avisoPrivacidad", resultado.has("avisoPrivacidad"));
		assertTrue("La propiedad avisoPrivacidad no es un JsonObject", resultado.get("avisoPrivacidad").isJsonObject());
		
		JsonObject avisoPrivacidad = Utilerias.obtenerJsonObjectPropiedad(resultado, "avisoPrivacidad");
		
		if(!resultado.get("avisoPrivacidad").isJsonNull()) {
			assertTrue("La propiedad Text_Aviso no se encuentra en avisoPrivacidad", avisoPrivacidad.has("Text_Aviso"));
		}else 
			assertNotNull("la propiedad avisoPrivacidad es nula", avisoPrivacidad);
		
		logger.info("TEST: Finalizando avisoPrivacidadConsultarTestDeberiaSerExitoso metodo...");
	}

}
 