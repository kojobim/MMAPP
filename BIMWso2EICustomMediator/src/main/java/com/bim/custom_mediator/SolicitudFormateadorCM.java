package com.bim.custom_mediator;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.axis2.AxisFault;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.aspects.flow.statistics.data.artifact.ArtifactHolder;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SolicitudFormateadorCM  implements Mediator{
	
	public boolean mediate(MessageContext contexto) {
		System.out.print("SolicitudFormateadorCM: Starting mediate method");
		org.apache.axis2.context.MessageContext axisContexto = ((Axis2MessageContext) contexto)
                .getAxis2MessageContext();
		String cadenaSolicitudXML = contexto.getEnvelope().getBody().getFirstElement().getFirstElement().toString();
		System.out.println("&&&&&&cadenaSolicitudXML " + cadenaSolicitudXML + "\n");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Message.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Message mensaje = (Message) unmarshaller.unmarshal(new StringReader(cadenaSolicitudXML));
			System.out.println("&&&&&&Solicitud " + mensaje + "\n");
			
			JsonObject solicitudHttp = new JsonObject();

			byte[] b = mensaje.getPath().getBytes(StandardCharsets.UTF_8);
			contexto.setProperty("PATH",new String(b, StandardCharsets.UTF_8).replace("?", " ").split(" ")[0]);
			JsonObject queryParamars = this.getQueryParams(mensaje.getPath());
			solicitudHttp.add("queryParams", queryParamars);
			JsonObject pathParameters = this.getPathVariables(mensaje.getPath());
			solicitudHttp.add("pathVariables", pathParameters);
			JsonObject body = this.getBody(mensaje.getBody());
			solicitudHttp.add("body", body);
			JsonObject event = new JsonObject();
			event.add("solicitudHttp", solicitudHttp);
			try {
				JsonUtil.getNewJsonPayload(axisContexto, solicitudHttp.toString(), true, true);
			} catch (AxisFault e) {
				e.printStackTrace();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return true;
	}

	private JsonObject getQueryParams(String path) {
		System.out.println(">>>>>>>>>>getQueryParams<<<<<<<<<<<<<<<");
		byte[] b = path.getBytes(StandardCharsets.UTF_8);
		path = new String(b, StandardCharsets.UTF_8);
		System.out.println(">>>>>>>>>>PATH " +  path + "\n");
		JsonObject queryParams = new JsonObject();
		System.out.println("contain ? " + path.contains("?"));
		if(!path.contains("?"))
			return queryParams;
		
		System.out.println("index 1 " + path.replace("?", " ").split(" ")[1]);
		if(path.replace("?", " ").split(" ")[1] == null) {
			return queryParams;
		}
		String[] params = null;
		System.out.println("contains & " + path.contains("&"));
		if(path.contains("&"))
			 params = path.replace("?", " ").split(" ")[1].replace("&"," ").split(" ");
		else
			 params = path.split("?");
		
		System.out.println("params " + params);
		
		for(String param : params) {
			System.out.println("param " + param);
			String[] keyValue = param.replace("=", " ").split(" ");
			System.out.println("keyValue " + keyValue.toString());
			queryParams.addProperty(keyValue[0], keyValue[1]);
		}
		System.out.println("queryParams " + queryParams);
		System.out.println(">>>>>>>>>>getQueryParams<<<<<<<<<<<<<<<");
		return queryParams;
	}
	
	private JsonObject getPathVariables(String path) {
		System.out.println(">>>>>>>>>>getPathVariables<<<<<<<<<<<<<<<");
		byte[] b = path.getBytes(StandardCharsets.UTF_8);
		path = new String(b, StandardCharsets.UTF_8);
		System.out.println(">>>>>>>>>>PATH " +  path + "\n");
		JsonObject pathVariables = new JsonObject();
		String[] params = null;
		if(path.contains("?"))
			params = path.replace("?", " ").split(" ")[0].replace("/", " ").split(" ");
		else
			params = path.replace("/", " ").split(" ");
		
		Integer i = 0;
		for(String param : params) {
			if(param.isEmpty())
				continue;
			i++;
			pathVariables.addProperty("param"+i.toString(), param);
		}
		System.out.println(">>>>>>>>>>getPathVariables<<<<<<<<<<<<<<<");
		return pathVariables;
	}
	
	private JsonObject getBody(String bodyXML) {
		System.out.println(">>>>>>>>>>getBody<<<<<<<<<<<<<<<");
		bodyXML = "<?xml version=\"1.0\" ?>"+bodyXML;
		byte[] b = bodyXML.getBytes(StandardCharsets.UTF_8);
		bodyXML = new String(b, StandardCharsets.UTF_8);
		System.out.println(">>>>>>>>>>BodyXML " +  bodyXML + "\n");
		
		JSONObject xmlJSON = XML.toJSONObject(bodyXML);
		String jsonString = xmlJSON.toString(4);
		JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
		
		System.out.println(">>>>>>>>>>getBody<<<<<<<<<<<<<<<");
		return jsonObject;	
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDescription(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public int getTraceState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContentAware() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTraceState(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public String getMediatorName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMediatorPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContentAltering() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reportCloseStatistics(MessageContext arg0, Integer arg1) {
		// TODO Auto-generated method stub
		
	}

	public Integer reportOpenStatistics(MessageContext arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setComponentStatisticsId(ArtifactHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setMediatorPosition(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setShortDescription(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
