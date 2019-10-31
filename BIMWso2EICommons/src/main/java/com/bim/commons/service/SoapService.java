package com.bim.commons.service;

import java.util.Calendar;
import java.util.Date;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.enums.MesesEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.utils.SOAPClientUtils;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class SoapService extends BaseService {
	
	private static final Logger logger = LoggerFactory.getLogger(SoapService.class);

	private static String SoapEndpoint;
	private static String SoapAction;
	private static String NamespaceWcf;
	private static String PrefixWcf;
	private static String NamespaceTem;
	private static String PrefixTem;
	private static String NamespaceSoapenv;
	private static String PrefixSoapenv;
	
	public SoapService() {
		super();
		
		SoapEndpoint = properties.getProperty("soap.movimientos_envio_correo.endpoint");
		SoapAction = properties.getProperty("soap.movimientos_envio_correo.action");
		NamespaceWcf = properties.getProperty("soap.movimientos_envio_correo.namespace.wcf");
		PrefixWcf = properties.getProperty("soap.movimientos_envio_correo.namespace.wcf.prefix");
		NamespaceTem = properties.getProperty("soap.movimientos_envio_correo.namespace.tem");
		PrefixTem = properties.getProperty("soap.movimientos_envio_correo.namespace.tem.prefix");
		NamespaceSoapenv = properties.getProperty("soap.movimientos_envio_correo.namespace.soapenv");
		PrefixSoapenv = properties.getProperty("soap.movimientos_envio_correo.namespace.soapenv.prefix");
		
	}
	
	/**
	 * Método para envío de correo de movimientos
	 * @param anio: String
	 * @param cliente: String
	 * @param mes: String
	 * @return 
	 * { }
	 */
	public void movimientosEnvioCorreo(String anio, String mes, String cliente) throws SOAPException {
		logger.info("SERVICE: Comenzando movimientosEnvioCorreo metodo");
		
        if(anio == null || anio.isEmpty()) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.32");
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(mes == null || mes.isEmpty()) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.33");
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(cliente == null || cliente.isEmpty()) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.34");
            throw new BadRequestException(bimMessageDTO.toString());
        }
                
        if(!Utilerias.validaNumero(anio)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.35");
            bimMessageDTO.addMergeVariable("anio", anio);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        logger.info("- current year " + currentYear);
        if(Integer.parseInt(anio) > currentYear) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.39");
            bimMessageDTO.addMergeVariable("anio", anio);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(!Utilerias.validaNumero(mes)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.36");
            bimMessageDTO.addMergeVariable("mes", mes);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        if(MesesEnum.validaMes(mes) == null) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.40");
            bimMessageDTO.addMergeVariable("mes", mes);
            throw new BadRequestException(bimMessageDTO.toString());        	

        }
        
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        if(Integer.parseInt(mes) > currentMonth) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.41");
            bimMessageDTO.addMergeVariable("mes", mes);
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
        
        if(!Utilerias.validaNumero(cliente)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.37");
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
		
		JsonObject jsonObject = SOAPClientUtils.callSoapWebService(soapRequest, SoapEndpoint);
		JsonObject datosClienteResponse = Utilerias.getJsonObjectProperty(jsonObject, "DatosClienteResponse");
		JsonObject datosClienteResult = Utilerias.getJsonObjectProperty(datosClienteResponse, "DatosClienteResult");
		JsonObject mCorreoRespons = Utilerias.getJsonObjectProperty(datosClienteResult, "MCorreoRespons");
		Integer codigoError = Utilerias.getIntProperty(mCorreoRespons, "codigoError");
		
		if(codigoError.intValue() == 0) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.38");
            throw new ConflictException(bimMessageDTO.toString());
        }
		
		logger.info("SERVICE: Finalizando movimientosEnvioCorreo metodo");
	}//Cierre del método
}
