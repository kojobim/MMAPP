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
import com.bim.commons.enums.AvisoPrivacidadFormatosEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.service.AvisoPrivacidadServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/aviso-privacidad")
public class AvisoPrivacidadCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(AvisoPrivacidadCtrl.class);

	private AvisoPrivacidadServicio avisoPrivacidadServicio;
	private TransaccionServicio transaccionServicio;
	
	public AvisoPrivacidadCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.avisoPrivacidadServicio = new AvisoPrivacidadServicio();
		this.transaccionServicio = new TransaccionServicio();
		logger.info("CTRL: Terminando metodo init...");
	}
	
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerAvisoPrivacidad(@QueryParam("formato") String formato, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerAvisoPrivacidad metodo...");
		
		if(AvisoPrivacidadFormatosEnum.validarFormato(formato) == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.44");
			bimMessageDTO.addMergeVariable("formato", formato);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		JsonObject folioTransaccionGenerarResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarResultadoObjeto,"transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(transaccion,"Fol_Transa");
		logger.info("- folTransa " + folTransa);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		datosAvisoPrivacidad.addProperty("NumTransac", folTransa);
		
		JsonObject avisoPrivacidadConsultarResultado = avisoPrivacidadServicio.avisoPrivacidadConsultar(datosAvisoPrivacidad);
		JsonObject avisoPrivacidadConsultarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(avisoPrivacidadConsultarResultado, "avisoPrivacidad");
		String textAviso = Utilerias.obtenerStringPropiedad(avisoPrivacidadConsultarResultadoObjeto, "Text_Aviso");
		
		JsonObject resultado = new JsonObject();
		JsonObject avisoPrivacidad = new JsonObject();
		avisoPrivacidad.addProperty("cpFormato", formato);
		avisoPrivacidad.addProperty("cpAvisoPrivacidad", textAviso.replace("\r\n", ""));
		resultado.add("avisoPrivacidad", avisoPrivacidad);
		
		logger.info("CTRL: Terminando obtenerAvisoPrivacidad metodo...");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

}
