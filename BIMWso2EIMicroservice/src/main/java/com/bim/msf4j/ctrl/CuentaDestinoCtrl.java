package com.bim.msf4j.ctrl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("cuentas-destino")
public class CuentaDestinoCtrl {

	private static final Logger logger = Logger.getLogger(CuentaDestinoCtrl.class);

	private CuentaDestinoServicio cuentaDestinoServicio;
	
	public CuentaDestinoCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.cuentaDestinoServicio = new CuentaDestinoServicio();		
		logger.info("CTRL: Terminando metodo init...");
	}
	
	@Path("/BIM")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listadoCuentasDestinoBIM(@QueryParam("status") String status, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando listadoCuentasDestinoBIM metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info(principalResultadoObjecto);
		
		if(status == null || status.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.49");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosCuentaDestinoBIMConsultar = new JsonObject();
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", "");
		datosCuentaDestinoBIMConsultar.addProperty("FechaSis", fechaSis);

		if(status.equals("ACTIVO"))
			datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", "L2");
		
		if(status.equals("PENDIENTE"))
			datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", "L3");
		
		JsonObject cuentaDestinoBIMConsultarRestultado = this.cuentaDestinoServicio.cuentaDestinoBIMConsultar(datosCuentaDestinoBIMConsultar);
		logger.info("- cuentaDestinoBIMConsultarRestultado: " + cuentaDestinoBIMConsultarRestultado);
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIMConsultarRestultado, "cuentaDestinoBIM");
		logger.info("- cuentaDestinoBIM: " + cuentaDestinoBIM);

		JsonObject cuentasDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIM, "cuentasDestinoBIM");
		logger.info("- cuentasDestinoBIM: " + cuentasDestinoBIM);

		JsonObject resultado = new JsonObject();
		resultado.add("cuentasDestinoBIM", cuentasDestinoBIM);
		
		logger.info("CTRL: Terminando listadoCuentasDestinoBIM metodo...");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
}
