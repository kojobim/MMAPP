package com.bim.commons.utils;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SOAPClientUtils {
	
	public static JsonObject callSoapWebService(SOAPMessage soapRequest, String endpoint) {
		System.out.println("COMMONS: Comenzando callSoapWebService metodo");
		try {
            System.out.println("SOAP Request: ");
            soapRequest.writeTo(System.out);
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapResponse = soapConnection.call(soapRequest, endpoint);
            System.out.println("\nSOAP Response: \n");
            soapResponse.writeTo(System.out);

            soapConnection.close();
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            soapResponse.writeTo(stream);
            String message = new String(stream.toByteArray(), "utf-8");
            System.out.println("@@@ message " + message);
            
            JSONObject data = XML.toJSONObject(message);
            System.out.println("@@@ data " + data);
            
            JsonObject responseData = new Gson().fromJson(data.toString(), JsonObject.class);
            System.out.println("responseData " + responseData.toString());
            
            removeNamesapceAndPrefix(responseData);
            
            System.out.println("responseData " + responseData);
            JsonObject result = formatResult(responseData);

            System.out.println("result " + result);
            System.out.println("COMMONS: Finalizando callSoapWebService metodo");
            return result;
		} catch (Exception e) {
			System.err.println("\nError al enviar el SOAP Request al Servidor\n");
			e.printStackTrace();
		}
		return null;
	}
	
	private static void removeNamesapceAndPrefix(JsonObject parentObject) {
        System.out.println("COMMONS: Comenzando removeNamesapceAndPrefix metodo");
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
        System.out.println("COMMONS: Comenzando removeNamesapceAndPrefix metodo");
	}
	
	private static JsonObject formatResult(JsonObject responseObject) {
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
		return parentObject;
	}
}
