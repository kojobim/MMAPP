package com.bim.msf4j.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Microservice;

import com.bim.commons.dto.SOAPRequestDTO;
import com.bim.commons.utils.SOAPClientUtils;
import com.google.gson.JsonObject;

@Path("/saldos")
public class SaldosCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(SaldosCtrl.class);
	private static Properties properties;
	private static String SoapEndpoint;
	private static String SoapAction;
	private static String Namespace;
	private static String NamespaceUrl;
	private static String ParentName;
	
	
	public SaldosCtrl() {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		
		SoapEndpoint = properties.getProperty("soap.saldos.endpoint");
		SoapAction = properties.getProperty("soap.saldos.action");
		Namespace = properties.getProperty("soap.saldos.datos_client.namespace");
		NamespaceUrl = properties.getProperty("soap.saldos.datos_client.namespace.url");
		ParentName = properties.getProperty("soap.saldos.datos_client.parent_name");
		
	}
	
	@Path("/")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String consultaSaldos(@QueryParam("anio") String anio, 
			@QueryParam("mes") String mes, @QueryParam("cliente") String cliente) {
		logger.info("CTRL: Comenzando consultaSaldos metodo");
		JsonObject datos = new JsonObject();
		
		JsonObject datosCliente = new JsonObject();
		JsonObject datosCorreo = new JsonObject();
		datosCorreo.addProperty("Anio", anio);
		datosCorreo.addProperty("Cliente", cliente);
		datosCorreo.addProperty("Mes", mes);

		datosCliente.add("MCorreoRequest", datosCorreo);
		datos.add("DatosCliente", datosCliente);
		SOAPRequestDTO soapRequestDTO = new SOAPRequestDTO();
		soapRequestDTO.setSoapEnpoint(SoapEndpoint);
		soapRequestDTO.setSoapAction(SoapAction);
		soapRequestDTO.setNamespace(Namespace);
		soapRequestDTO.setNamespaceUrl(NamespaceUrl);
		soapRequestDTO.setParentName(ParentName);
		soapRequestDTO.setData(datos);
		
		SOAPClientUtils.callSoapWebService(soapRequestDTO);
		logger.info("CTRL: Finalizando consultaSaldos metodo");
		return "SUCCESS";
	}
	 
}
