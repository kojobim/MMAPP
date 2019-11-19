package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.TransferenciasBIMServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/transferencias-bim")
public class TransferenciasBIMCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(TransferenciasBIMCtrl.class);

	private TransaccionServicio transaccionServicio;
	private TransferenciasBIMServicio transferenciasBIMServicio;
	private BitacoraServicio bitacoraServicio;
	
	public TransferenciasBIMCtrl() {
		super();
		
		logger.info("CTRL: Comenzando metodo init...");
		this.transaccionServicio = new TransaccionServicio();
		this.transferenciasBIMServicio = new TransferenciasBIMServicio();
		this.bitacoraServicio = new BitacoraServicio();
	}


	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaBIMCrear(JsonObject datosTransferenciaBIM,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaBIMCrear metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principal = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principal " + principal);
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTrasaccionResultado " +  folioTransaccionResultado);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoTransferenciaBIMActivacion = new JsonObject();
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Cdb_UsuAdm", "");
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("NumTransac", "");
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("FechaSis", fechaSis);
		this.transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion );
		
		
		JsonObject datosTransferenciaBIMCreacion = new JsonObject();
		datosTransferenciaBIMCreacion.addProperty("Trb_Client", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_CueOri", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_CueDes", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_Monto", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_MonOri", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_MonDes", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_Descri", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_UsuCap", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_FecAut", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_TipTra", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_Frecue", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_FePrEn", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_DurFec", "");
		datosTransferenciaBIMCreacion.addProperty("Trb_EmaBen", "");
		datosTransferenciaBIMCreacion.addProperty("NumTransac", "");
		datosTransferenciaBIMCreacion.addProperty("FechaSis", fechaSis);
		this.transferenciasBIMServicio.transferenciaBIMCreacion(datosTransferenciaBIMCreacion );
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", "");
		datosBitacora.addProperty("Bit_Fecha", "");
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", "");
		datosBitacora.addProperty("Bit_PriRef", "");
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", "");
		datosBitacora.addProperty("NumTransac", "");
		datosBitacora.addProperty("FechaSis", fechaSis);
		this.bitacoraServicio.creacionBitacora(datosBitacora);
		
		
		logger.info("CTRL: Terminando transferenciaBIMCrear metodo");
		return Response
				.ok(null, MediaType.APPLICATION_JSON)
				.build();
	}
	
}
