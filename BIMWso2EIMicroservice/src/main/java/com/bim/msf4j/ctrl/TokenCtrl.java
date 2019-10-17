package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.wso2.msf4j.Microservice;
import com.bim.commons.utils.Racal;

@Path("/seguridad/token")
public class TokenCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(TokenCtrl.class);
	
	@Path("/validarToken")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validarToken(@QueryParam("token") String token) {
		logger.info("CTRL: Empezando metodo validarToken");
		String validaToken = Racal.validaToken(token);
		
		if(validaToken.isEmpty())
			return Response
					.status(Response.Status.CONFLICT)
					.build();
					
		logger.info("CTRL: Terminando metodo validarToken");
		return Response
				.status(Response.Status.OK)
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.build();
	}
}
