package com.bim.commons.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.bim.commons.dto.SOAPRequestDTO;

public class SOAPClientUtils {

	private static Properties properties;
	
	static {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private static void createSoapEnvelope(SOAPMessage soapMessage, SOAPRequestDTO soapRequestDTO) throws SOAPException {
		System.out.println("COMMONS: Empezando metodo createSoapEnvelope...");
		SOAPPart  soapPart =  soapMessage.getSOAPPart();
		
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		soapEnvelope.addNamespaceDeclaration("s", "http://schemas.xmlsoap.org/soap/envelope/");
		soapEnvelope.setPrefix("s");

		if(soapEnvelope.getHeader() != null) 
			soapEnvelope.getHeader().detachNode();
		
		SOAPHeader soapHeader = soapEnvelope.addHeader();
		soapHeader.setPrefix("s");
		soapHeader.addNamespaceDeclaration("s","http://schemas.xmlsoap.org/soap/envelope/");
		SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(new QName("http://schemas.microsoft.com/ws/2005/05/addressing/none", "Action"));
		soapHeaderElement.addTextNode("http://tempuri.org/IService1/DatosCliente");
		soapHeaderElement.addAttribute(SOAPFactory.newInstance().createName("s:mustUnderstand"), "1");

		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.setPrefix(soapRequestDTO.getSoapRequestPrefix());
		System.out.println("soapBody " + soapBody.toString());
		soapBody.addChildElement(soapRequestDTO.getSoapElementBody());
		
		System.out.println("sopaBody Completed " + soapBody.toString());
		System.out.println("COMMONS: Terminando metodo createSoapEnvelope...");
	}
	
	public static void callSoapWebService(SOAPRequestDTO soapRequestDTO) {
		System.out.println("COMMONS: Comenzando callSoapWebService metodo");
		System.out.println("soapRequestDTO - Namespace " +  soapRequestDTO.getNamespace());
		System.out.println("soapRequestDTO - NamespaceUrl " +  soapRequestDTO.getNamespaceUrl());
		System.out.println("soapRequestDTO - Action " +  soapRequestDTO.getSoapAction());
		System.out.println("soapRequestDTO - Enpoint " +  soapRequestDTO.getSoapEnpoint());
		System.out.println("soapRequestDTO - RequestPrefix " +  soapRequestDTO.getSoapRequestPrefix());
		
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapResponse = soapConnection.call(createSoapRequest(soapRequestDTO), soapRequestDTO.getSoapEnpoint());
			System.out.println("soapResponse  " + soapResponse );
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println("soapResponse " +  soapResponse);
            System.out.println();

            soapConnection.close();
            System.out.println("COMMONS: Finalizando callSoapWebService metodo");
		} catch (Exception e) {
			System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
	}
	
	private static SOAPMessage createSoapRequest(SOAPRequestDTO soapRequestDTO) throws SOAPException, IOException {
		System.out.println("COMMONS: Comenzando createSoapRequest metodo");
		System.out.println("soapRequestDTO - Namespace " +  soapRequestDTO.getNamespace());
		System.out.println("soapRequestDTO - NamespaceUrl " +  soapRequestDTO.getNamespaceUrl());
		System.out.println("soapRequestDTO - Action " +  soapRequestDTO.getSoapAction());
		System.out.println("soapRequestDTO - Enpoint " +  soapRequestDTO.getSoapEnpoint());
		System.out.println("soapRequestDTO - RequestPrefix " +  soapRequestDTO.getSoapRequestPrefix());
		
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		createSoapEnvelope(soapMessage, soapRequestDTO);
		
		System.out.println("Request SOAP Message-Body");
		soapMessage.writeTo(System.out);
		System.out.println("\n");
		
		soapMessage.saveChanges();
		System.out.println("Request SOAP Message-Completed");
		soapMessage.writeTo(System.out);
		System.out.println("\n");
		System.out.println("COMMONS: Terminando createSoapRequest metodo");
		return soapMessage;
	}
	
}
