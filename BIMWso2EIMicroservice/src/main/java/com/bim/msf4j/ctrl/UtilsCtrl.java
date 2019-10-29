package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.utils.SOAPClientUtils;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/utils")
public class UtilsCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(UtilsCtrl.class);

	private static String SoapEndpoint;
	private static String SoapAction;
	private static String NamespaceWcf;
	private static String PrefixWcf;
	private static String NamespaceTem;
	private static String PrefixTem;
	private static String NamespaceSoapenv;
	private static String PrefixSoapenv;
	
	public UtilsCtrl() {
		super();
		
		SoapEndpoint = properties.getProperty("soap.movimientos.endpoint");
		SoapAction = properties.getProperty("soap.movimientos.action");
		NamespaceWcf = properties.getProperty("soap.movimientos.namespace.wcf");
		PrefixWcf = properties.getProperty("soap.movimientos.namespace.wcf.prefix");
		NamespaceTem = properties.getProperty("soap.movimientos.namespace.tem");
		PrefixTem = properties.getProperty("soap.movimientos.namespace.tem.prefix");
		NamespaceSoapenv = properties.getProperty("soap.movimientos.namespace.soapenv");
		PrefixSoapenv = properties.getProperty("soap.movimientos.namespace.soapenv.prefix");
		
	}
	
	@Path("/cuentas/enviar-correo-movimientos")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void movimientosEnvioCorreo(JsonObject datosCorreo) throws SOAPException {
		logger.info("CTRL: Comenzando movimientosEnvioCorreo metodo");
		
		String anio = datosCorreo.get("anio").getAsString();
		String mes = datosCorreo.get("mes").getAsString();
		String cliente = datosCorreo.get("cliente").getAsString();
		
        if(anio == null || anio.isEmpty()) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.23");
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(mes == null || mes.isEmpty()) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.24");
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(cliente == null || cliente.isEmpty()) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.25");
            throw new BadRequestException(bimMessageDTO.toString());
        }
                
        if(!Utilerias.isNumber(anio)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.26");
            bimMessageDTO.addMergeVariable("anio", anio);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(!Utilerias.isNumber(mes)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.27");
            bimMessageDTO.addMergeVariable("mes", mes);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(!Utilerias.isNumber(cliente)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
            bimMessageDTO.addMergeVariable("cliente", cliente);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
		SOAPMessage soapRequest = null;
		MessageFactory messageFactory = MessageFactory.newInstance();
		soapRequest = messageFactory.createMessage();
		
		MimeHeaders headers =  soapRequest.getMimeHeaders();
		headers.addHeader("SOAPAction", SoapAction);
		
		SOAPPart soapPart = soapRequest.getSOAPPart();
		
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
		soapEnvelope.addNamespaceDeclaration(PrefixSoapenv, NamespaceSoapenv);
		soapEnvelope.addNamespaceDeclaration(PrefixTem, NamespaceTem);
		soapEnvelope.addNamespaceDeclaration(PrefixWcf, NamespaceWcf);
		soapEnvelope.setPrefix(PrefixSoapenv);
	
        if(soapEnvelope.getHeader() != null) 
            soapEnvelope.getHeader().detachNode();
        
        SOAPHeader soapHeader = soapEnvelope.addHeader();
        soapHeader.setPrefix(PrefixSoapenv);
		
		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.setPrefix(PrefixSoapenv);
       
		QName datosClienteName = new QName(NamespaceTem, "DatosCliente", PrefixTem);
		SOAPElement soapElementDatosCliente = soapBody.addBodyElement(datosClienteName);
		
		QName datosMCorreoRequestName = new QName(NamespaceTem, "MCorreoRequest", PrefixTem);
		SOAPElement soapElementMCorreoRequest = soapElementDatosCliente.addChildElement(datosMCorreoRequestName);
		
		QName anioName = new QName(NamespaceWcf, "Anio", PrefixWcf);
		SOAPElement soapElementAnio = soapElementMCorreoRequest.addChildElement(anioName);
		soapElementAnio.addTextNode(anio);

		QName clienteName = new QName(NamespaceWcf, "Cliente", PrefixWcf);
		SOAPElement soapElementCliente = soapElementMCorreoRequest.addChildElement(clienteName);
		soapElementCliente.addTextNode(cliente);
		
		QName mesName = new QName(NamespaceWcf, "Mes", PrefixWcf);
		SOAPElement soapElementMes = soapElementMCorreoRequest.addChildElement(mesName);
		soapElementMes.addTextNode(mes);
		
		SOAPClientUtils.callSoapWebService(soapRequest, SoapEndpoint);
		
		logger.info("CTRL: Finalizando movimientosEnvioCorreo metodo");
	}
	 
}
