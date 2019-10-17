package com.bim.msf4j.ctrl;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.wso2.msf4j.Microservice;

import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.utils.HttpClientUtils;
import com.google.gson.Gson;

@Path("/ping")
public class PingCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(PingCtrl.class);

    @Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPing(@QueryParam("email") String email) {
    	logger.info("CTRL: Starting post method");    	
    	MessageProxyDTO messageProxyDTO = new MessageProxyDTO();
    	HashMap<String, String> queryParams = new HashMap<>();
		messageProxyDTO.setQueryParams(new Gson().toJson(queryParams));
    	RequestDTO requestDTO = new RequestDTO();
    	requestDTO.setMessage(messageProxyDTO);
    	requestDTO.setUrl("http://DESKTOP-B042GEE:8280/services/PingProxy");
    	String responseBody = HttpClientUtils.postPerform(requestDTO);
    	logger.info("CTRL: Ending post method");
    	return Response
    			.status(Response.Status.OK)
    			.header("Content-Type", MediaType.APPLICATION_JSON)
    			.entity(responseBody)
    			.build();
    }

}
