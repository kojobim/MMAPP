package com.bim.msf4j.ctrl;

import java.text.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

import com.bim.msf4j.commons.HttpClientUtils;
import com.bim.msf4j.commons.Utilerias;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/inversiones")
public class InversionesCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(InversionesCtrl.class);
	private DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Path("/detalle")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response detalleInversion(@Context final Request request) throws ParseException {
    	logger.info("CTRL: Empezando detalleInversion method...");
    	String body = HttpClientUtils.getStringContent(request);
    	logger.info("vo: " + body);
    	
    	JsonObject jsonObj = new Gson().fromJson(body, JsonObject.class);
    	JsonObject inversion = jsonObj.get("inversion").getAsJsonObject();
    	String invFecIni = inversion.get("invFecIni").getAsString();
    	String invFecVen = inversion.get("invFecVen").getAsString();
    	double intBru = inversion.get("intBru").getAsDouble();
    	
    	intBru = Utilerias.redondear(intBru, 2);
    	invFecIni = Utilerias.convertirFechaAFormatoSimple(invFecIni);
    	invFecVen = Utilerias.convertirFechaAFormatoSimple(invFecVen);
    	DateTime dtFecIni = DateTime.parse(invFecIni);
    	DateTime dtFecVen = DateTime.parse(invFecVen);
		inversion.addProperty("cpRenInv", Utilerias.calcularVencimiento(invFecVen));
    	
    	inversion.addProperty("intBru", intBru);
    	inversion.addProperty("invFecIni", dtfOut.print(dtFecIni));
    	inversion.addProperty("invFecVen", dtfOut.print(dtFecVen));
    	
    	logger.info("CTRL: Terminando detalleInversion method...");
    	return Response
    			.status(Response.Status.OK)
    			.header("Content-Type", MediaType.APPLICATION_JSON)
    			.entity(jsonObj)
    			.build();
    }

}
