package com.bim.commons.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.bim.commons.dto.SOAPRequestDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	
	private static void createSoapEnvelope(SOAPMessage soapMessage ,SOAPRequestDTO soapRequestDTO) throws SOAPException {
		System.out.println("COMMONS: Empezando metodo createSoapEnvelope...");
		System.out.println("soapMessage: " + soapMessage.toString());
		System.out.println("soapRequestDTO " + soapRequestDTO.toString());
		SOAPPart  soapPart =  soapMessage.getSOAPPart();
		
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		soapEnvelope.setPrefix("s");
		System.out.println("namespace " + soapRequestDTO.getNamespace());
		System.out.println("namespaceURL " + soapRequestDTO.getNamespaceUrl());
		soapEnvelope.addNamespaceDeclaration(soapRequestDTO.getNamespace(),soapRequestDTO.getNamespaceUrl());
		
		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.setPrefix("s");
		System.out.println("soapBody " + soapBody.toString());
		
		EnvelopeDTO envelopeDTO = new EnvelopeDTO();
		envelopeDTO.setElement(soapRequestDTO.getData());
		envelopeDTO.setNamespace(soapRequestDTO.getNamespace());
		envelopeDTO.setNamespaceUrl(soapRequestDTO.getNamespaceUrl());
		envelopeDTO.setParentName(soapRequestDTO.getParentName());
		envelopeDTO.setSoapBody(soapBody);
		createSoapBody(envelopeDTO);
		System.out.println("sopaBody Completed " + soapBody.toString());
		System.out.println("COMMONS: Terminando metodo createSoapEnvelope...");
	}
	
	private static SOAPElement createSoapBody(EnvelopeDTO envelopeDTO) throws SOAPException {
		System.out.println("COMMONS: Empezando metodo createSoapBody...");
		System.out.println("parentName " + envelopeDTO.getParentName());
		System.out.println("namespace " + envelopeDTO.getNamespaceUrl());
		System.out.println("namespaceUrl " + envelopeDTO.getNamespaceUrl());
		System.out.println("element " + envelopeDTO.getElement());

		SOAPBody soapBody = envelopeDTO.getSoapBody();
		convertSoapElement(soapBody, null, envelopeDTO.getElement());
		System.out.println("COMMONS: Terminando metodo createSoapBody...");
		return soapBody;
	}
	
	private static void convertSoapElement(SOAPBody soapBody,SOAPElement soapParentElement, JsonElement element) throws SOAPException {
		System.out.println("COMMONS: Empezando metodo convertSoapElement...");
		System.out.println("soapParentElement " + soapParentElement);

		if(element.isJsonObject()) {
			System.out.println("elementObject " + element);
			JsonObject attribute = (JsonObject) element;
			System.out.println("attribute " + attribute);
			for(Entry<String, JsonElement> property: attribute.entrySet()) {
				System.out.println("property " + property);
				SOAPElement soapProperty = null;
				if(soapBody != null && soapParentElement == null)
					soapProperty = soapBody.addChildElement(property.getKey());
				else
					soapProperty = soapParentElement.addChildElement(property.getKey());
				
				if(property.getValue().isJsonPrimitive()) 
					soapProperty.addTextNode(property.getValue().getAsString());	
	
				if(property.getValue().isJsonObject())
					convertSoapElement(null, soapProperty, property.getValue());
				
			}
		}

		if(element.isJsonPrimitive()) {
			System.out.println("elemenPrimitive " + element);
			soapParentElement.addTextNode(element.getAsString());
		}
		System.out.println("AcumulateSoapBody " +  soapBody);
		System.out.println("COMMONS: Terminando metodo convertSoapElement...");
	}
	
	public static void callSoapWebService(SOAPRequestDTO soapRequestDTO) {
		System.out.println("COMMONS: Comenzando callSoapWebService metodo");
		System.out.println("soapRequestDTO " +  soapRequestDTO);
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapResponse = soapConnection.call(createSoapRequest(soapRequestDTO.getSoapAction(), soapRequestDTO), soapRequestDTO.getSoapEnpoint());
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
	
	private static SOAPMessage createSoapRequest(String soapAction, SOAPRequestDTO soapRequestDTO) throws SOAPException, IOException {
		System.out.println("COMMONS: Comenzando createSoapRequest metodo");
		System.out.println("soapAction " + soapAction);
		System.out.println("soapRequestDTO " + soapRequestDTO);
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		createSoapEnvelope(soapMessage, soapRequestDTO);
		
		System.out.println("Request SOAP Message-Body");
		soapMessage.writeTo(System.out);
		System.out.println("\n");
		
		soapMessage.getMimeHeaders()
			.addHeader("SOAPAction", soapAction);
		
		soapMessage.saveChanges();
		System.out.println("Request SOAP Message-Completed");
		soapMessage.writeTo(System.out);
		System.out.println("\n");
		System.out.println("COMMONS: Terminando createSoapRequest metodo");
		return soapMessage;
	}
	
}

class EnvelopeDTO {
	private SOAPBody soapBody;
	private String parentName;
	private String namespace;
	private String namespaceUrl;
	private String childNamespace;
	private String childNamespacePrefix;
	private JsonElement element;

	public SOAPBody getSoapBody() {
		return soapBody;
	}

	public void setSoapBody(SOAPBody soapBody) {
		this.soapBody = soapBody;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespaceUrl() {
		return namespaceUrl;
	}

	public void setNamespaceUrl(String namespaceUrl) {
		this.namespaceUrl = namespaceUrl;
	}

	public JsonElement getElement() {
		return element;
	}

	public void setElement(JsonElement element) {
		this.element = element;
	}

	public String getChildNamespace() {
		return childNamespace;
	}

	public void setChildNamespace(String childNamespace) {
		this.childNamespace = childNamespace;
	}

	public String getChildNamespacePrefix() {
		return childNamespacePrefix;
	}

	public void setChildNamespacePrefix(String childNamespacePrefix) {
		this.childNamespacePrefix = childNamespacePrefix;
	}
}