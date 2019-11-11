package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;
import com.bim.commons.service.AvisoPrivacidadServicio;

@Path("/")
public class AvisoPrivacidadCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(AvisoPrivacidadCtrl.class);

	private AvisoPrivacidadServicio avisoPrivacidadServicio;
	
	public AvisoPrivacidadCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.avisoPrivacidadServicio = new AvisoPrivacidadServicio();		
		logger.info("CTRL: Terminando metodo init...");
	}
	
	@Path("/aviso-privacidad")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerAvisoPrivacidad(@QueryParam("formato") String formato, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerAvisoPrivacidad metodo...");
		
		logger.info("CTRL: Terminando obtenerAvisoPrivacidad metodo");
		return Response.ok(MediaType.APPLICATION_JSON)
				.build();
	}
	
}
