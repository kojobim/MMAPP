package com.bim.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SOAPClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SOAPClientUtils.class);
	private static Properties properties;
	private static String Charset;
	
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
		
		Charset = properties.getProperty("charset");
	}
	
	public static JsonObject callSoapWebService(SOAPMessage soapRequest, String endpoint) {
		logger.info("COMMONS: Comenzando callSoapWebService metodo");
		try {
            soapRequest.writeTo(System.out);
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapResponse = soapConnection.call(soapRequest, endpoint);
            soapResponse.writeTo(System.out);

            soapConnection.close();
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            soapResponse.writeTo(stream);
            String message = new String(stream.toByteArray(), Charset);
            
            JSONObject data = XML.toJSONObject(message);
            
            JsonObject responseData = new Gson().fromJson(data.toString(), JsonObject.class);
            
            removeNamesapceAndPrefix(responseData);
            JsonObject result = formatResult(responseData);

            JsonObject envelope = Utilerias.obtenerJsonObjectPropiedad(result, "Envelope");
            
            JsonObject body = Utilerias.obtenerJsonObjectPropiedad(envelope, "Body");
            		
            logger.info("- body" + body);
            logger.info("COMMONS: Finalizando callSoapWebService metodo");
            return body;
		} catch (Exception e) {
			logger.info("Error al enviar el SOAP Request al Servidor");
			e.printStackTrace();
		}
		return null;
	}
	
	private static void removeNamesapceAndPrefix(JsonObject parentObject) {
		logger.info("COMMONS: Comenzando removeNamesapceAndPrefix metodo");
        Set<String> removedkeys = new HashSet<>();
		for(Entry<String, JsonElement> entry : parentObject.entrySet()) {
			if(entry.getKey().contains("xmlns")
					&& entry.getValue().isJsonPrimitive()) { 
				removedkeys.add(entry.getKey());
				continue;
			}
				
			if(parentObject.has(entry.getKey()) 
					&& !parentObject.get(entry.getKey()).isJsonNull() 
					&& parentObject.get(entry.getKey()).isJsonObject()) { 
				removeNamesapceAndPrefix(parentObject.get(entry.getKey()).getAsJsonObject());
			}
		}
		for(String key: removedkeys) {
			parentObject.remove(key);
		}
		logger.info("COMMONS: Finalizando removeNamesapceAndPrefix metodo");
	}
	
	private static JsonObject formatResult(JsonObject responseObject) {
		logger.info("COMMONS: Comenzando formatResult metodo");
		JsonObject parentObject = new JsonObject();
		for(Entry<String, JsonElement> entry : responseObject.entrySet()) {
			String key;
			if(entry.getKey().contains(":")) 
				 key = entry.getKey().split(":")[1];
			else
				key = entry.getKey();
			
			if(entry.getValue().isJsonPrimitive())
				parentObject.addProperty(key, entry.getValue().getAsString());
			else 
				parentObject.add(key, formatResult((JsonObject)entry.getValue()));
		}
		logger.info("COMMONS: Finalizando formatResult metodo");
		return parentObject;
	}
}
