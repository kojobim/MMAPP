package com.bim.msf4j.ctrl;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.service.AvisoPrivacidadServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/")
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
	
	@Path("/aviso-privacidad")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerAvisoPrivacidad(@QueryParam("formato") String formato, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerAvisoPrivacidad metodo...");
		
		if(AvisoPrivacidadFormatosEnum.validarFormato(formato) == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.44");
			bimMessageDTO.addMergeVariable("formato", formato);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		
		JsonObject avisoPrivacidadConsultarResultado = avisoPrivacidadServicio.avisoPrivacidadConsultar(datosAvisoPrivacidad);
		JsonObject avisoPrivacidadConsultarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(avisoPrivacidadConsultarResultado, "avisoPrivacidad");
		String textAviso = Utilerias.obtenerStringPropiedad(avisoPrivacidadConsultarResultadoObjeto, "Text_Aviso");
		textAviso = textAviso.replace("\r\n", "");
		
		JsonObject resultado = new JsonObject();
		JsonObject avisoPrivacidad = new JsonObject();
		avisoPrivacidad.addProperty("cpFormato", formato);
		avisoPrivacidad.addProperty("cpAvisoPrivacidad", textAviso);
		resultado.add("avisoPrivacidad", avisoPrivacidad);
		
		logger.info("CTRL: Terminando obtenerAvisoPrivacidad metodo");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/usuario/aviso-privacidad")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	public Response aceptarAvisoPrivacidad(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando aceptarAvisoPrivacidad metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		
		JsonObject folioTransaccionGenerarResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarResultadoObjeto" + folioTransaccionGenerarResultadoObjeto);

		String FolioTransaccionGenerarFolTransa = folioTransaccionGenerarResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Usu_Numero", usuNumero);
		datosAvisoPrivacidad.addProperty("Usu_Client", usuClient);
		datosAvisoPrivacidad.addProperty("Usu_FecAce", fechaSis);
		datosAvisoPrivacidad.addProperty("Usu_FecAct", fechaSis);
		datosAvisoPrivacidad.addProperty("NumTransac", FolioTransaccionGenerarFolTransa);
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		
		JsonObject avisoPrivacidadActualizacionResultado = avisoPrivacidadServicio.avisoPrivacidadActualizacion(datosAvisoPrivacidad);
		JsonObject avisoPrivacidadActualizacionResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(avisoPrivacidadActualizacionResultado, "avisoPrivacidad");
		String errCodigo = Utilerias.obtenerStringPropiedad(avisoPrivacidadActualizacionResultadoObjeto, "Err_Codigo");
		
		if(!"000000".equals(errCodigo)) {
			String errMensaj = Utilerias.obtenerStringPropiedad(avisoPrivacidadActualizacionResultadoObjeto, "Err_Mensaj");			
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		logger.info("CTRL: Terminando aceptarAvisoPrivacidad metodo...");
		return Response.noContent()
				.build();
	}
	
}
