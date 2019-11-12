package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.service.AvisoPrivacidadServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/usuario")
public class UsuarioCtrl extends BimBaseCtrl {

	private static Logger logger = Logger.getLogger(UsuarioCtrl.class);
	private CuentaDestinoServicio cuentaDestinoServicio;
	private AvisoPrivacidadServicio avisoPrivacidadServicio;
	private TransaccionServicio transaccionServicio;
	
	public UsuarioCtrl() {
		super();
		
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.avisoPrivacidadServicio = new AvisoPrivacidadServicio();
		this.transaccionServicio = new TransaccionServicio();
	}

	@Path("/cuentas-destino-nacionales")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasDestinoNacionalesListado(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando cuentasDestinoNacionalesListado metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuAdm = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuUsuAdm");
		logger.info("- usuAdm " + usuAdm);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNumero");
		logger.info("- usuNumero " + usuNumero);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		logger.info("- fechaSis " + fechaSis);
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuAdm);
		datosCuentaDestinoSPEI.addProperty("Cds_Usuari", usuNumero);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		JsonObject cuentasDestinoSPEIRespuesta = this.cuentaDestinoServicio.cuentaDestinoSPEIConsultar(datosCuentaDestinoSPEI );
		logger.info("- cuentasDestinoSPEIRespuesta " + cuentasDestinoSPEIRespuesta);
		
		JsonObject cuentasDestinoObjeto = Utilerias.obtenerJsonObjectPropiedad(cuentasDestinoSPEIRespuesta, "cuentasDestino");
		JsonArray cuentaDestinoArray = Utilerias.obtenerJsonArrayPropiedad(cuentasDestinoObjeto, "cuentaDestino");
		
		JsonArray cuentasDestinoResultado = new JsonArray();
		
		for(JsonElement cuentaDestinoElemento : cuentaDestinoArray) {
			JsonObject cuentaDestinoElementoObjeto = (JsonObject) cuentaDestinoElemento;
			JsonObject cuentaDestinoResultado = new JsonObject();
			cuentaDestinoResultado.addProperty("cdsCLABE", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_CLABE"));
			cuentaDestinoResultado.addProperty("cdsConsec", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_Consec"));
			cuentaDestinoResultado.addProperty("cdsDesCue", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_DesCue"));
			cuentasDestinoResultado.add(cuentaDestinoResultado);
		}
		
		JsonObject cuentasDestino = new JsonObject();
		cuentasDestino.add("cuentasDestino", cuentasDestinoResultado);
		logger.info("- cuentasDestino " + cuentasDestino);
		logger.info("CTRL: Finalizando cuentasDestinoNacionalesListado metodo");
		return Response.ok(cuentasDestino.toString(),MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("/aviso-privacidad")
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
