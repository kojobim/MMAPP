package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Racal;
import com.bim.msf4j.vo.HSMVO;
import com.google.gson.Gson;

@Path("/hsm")
public class HSMCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(HSMCtrl.class);
	
	@Path("/cifraPasswordHSM")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cifraPasswordHSM(@Context final Request request) {
		logger.info("CTRL: Empezando cifraPasswordHSM metodo");
		String body = HttpClientUtils.getStringContent(request);
		logger.info("vo: " + body);
		HSMVO hsmVO = new Gson().fromJson(body, HSMVO.class);
		String contrasenaEncriptada = Racal.cifraPassword_HSM(hsmVO.getContrasena());
		hsmVO.setContrasenaEncriptada(contrasenaEncriptada);
		hsmVO.setContrasena(null);
		logger.info("CTRL: Terminando cifraPasswordHSM metodo");
		return Response
				.status(Response.Status.OK)
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.entity(hsmVO)
				.build();
	}
}
