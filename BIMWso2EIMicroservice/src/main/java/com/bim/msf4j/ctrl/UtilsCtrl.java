package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPElementFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.apache.log4j.Logger;

import com.bim.commons.dto.SOAPRequestDTO;
import com.bim.commons.utils.SOAPClientUtils;
import com.google.gson.JsonObject;

@Path("/utils")
public class UtilsCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(UtilsCtrl.class);

	private static String SoapEndpoint;
	private static String SoapAction;
	private static String Namespace;
	private static String NamespaceUrl;
	private static String ParentName;
	
	
	public UtilsCtrl() {
		super();
		
		SoapEndpoint = properties.getProperty("soap.saldos.endpoint");
		SoapAction = properties.getProperty("soap.saldos.action");
		Namespace = properties.getProperty("soap.saldos.datos_client.namespace");
		NamespaceUrl = properties.getProperty("soap.saldos.datos_client.namespace.url");
		ParentName = properties.getProperty("soap.saldos.datos_client.parent_name");
		
	}
	
	@Path("/cuentas/enviar-correo-movimientos")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String consultaSaldos(JsonObject datosCorreo) {
		logger.info("CTRL: Comenzando consultaSaldos metodo");
		
		String anio = datosCorreo.get("anio").getAsString();
		String mes = datosCorreo.get("mes").getAsString();
		String cliente = datosCorreo.get("cliente").getAsString();
		
		//agregar validacions de datosCorreo
		
		SOAPRequestDTO soapRequestDTO = new SOAPRequestDTO();
		soapRequestDTO.setSoapEnpoint(SoapEndpoint);
		soapRequestDTO.setSoapAction(SoapAction);
		soapRequestDTO.setNamespace(Namespace);
		soapRequestDTO.setNamespaceUrl(NamespaceUrl);
		soapRequestDTO.setSoapRequestPrefix("s");
		
		SOAPElement soapElementDatosCliente = null;
		try {
			soapElementDatosCliente = SOAPFactory.newInstance().createElement("DatosCliente","", "http://tempuri.org/");
			SOAPElement soapElementMCorreoRequest = soapElementDatosCliente.addChildElement("MCorreoRequest");
			soapElementMCorreoRequest.addNamespaceDeclaration("d4p1", "http://schemas.datacontract.org/2004/07/WcfSendMail.Modelo");
			soapElementMCorreoRequest.addNamespaceDeclaration("i", "http://www.w3.org/2001/XMLSchema-instance");
			SOAPElement soapElementMCorreoRequestAnio = soapElementMCorreoRequest.addChildElement("Anio", "d4p1");
			soapElementMCorreoRequestAnio.addTextNode(anio);
			SOAPElement soapElementMCorreoRequestMes= soapElementMCorreoRequest.addChildElement("Mes", "d4p1");
			soapElementMCorreoRequestMes.addTextNode(mes);
			SOAPElement soapElementMCorreoRequestCliente = soapElementMCorreoRequest.addChildElement("Cliente", "d4p1");
			soapElementMCorreoRequestCliente.addTextNode(cliente);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		
		soapRequestDTO.setSoapElementBody(soapElementDatosCliente);
		
		SOAPClientUtils.callSoapWebService(soapRequestDTO);
		logger.info("CTRL: Finalizando consultaSaldos metodo");
		return "SUCCESS";
	}
	 
}
