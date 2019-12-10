package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/logout")
public class LogoutCtrl extends BimBaseCtrl  {
	
	private static final Logger logger = Logger.getLogger(LogoutCtrl.class);
	
	private TransaccionServicio transaccionServicio;
	private UsuarioServicio usuarioServicio;
	private BitacoraServicio bitacoraServicio;
	private static String UsuarioActualizacionTipActual;
	private static String LogoutBitacoraCreacionOpBitTipOpe;
	
	public LogoutCtrl() {
		super();
		
		this.transaccionServicio = new TransaccionServicio();
		this.usuarioServicio = new UsuarioServicio();
		this.bitacoraServicio = new BitacoraServicio();
		
		UsuarioActualizacionTipActual = properties.getProperty("op.usuario_actualizacion.tip_actual.t");
		LogoutBitacoraCreacionOpBitTipOpe = properties.getProperty("op.logout.bitacora_creacion.bit_tip_ope");
	}
	
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@QueryParam("token") String token, @Context Request solicitud) {
		logger.info("CTRL: Comenzando logout metodo...");
		
		StringBuilder bearerToken = new StringBuilder()
				.append("Bearer ")
				.append(token);
		
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken.toString());
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNumero");
		String bitDireIP = solicitud.getHeader("User-Agent") != null ? solicitud.getHeader("User-Agent") : "";
		String bitPriRef = solicitud.getHeader("X-Forwarded-For") != null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		JsonObject folioTransaccionGenerarResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		String folTransa = Utilerias.obtenerStringPropiedad(folioTransaccionGenerarResultadoObjeto.get("transaccion").getAsJsonObject(), "Fol_Transa");
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosUsuarioActualizar = new JsonObject();
		datosUsuarioActualizar.addProperty("Usu_Numero", usuNumero != null ? usuNumero : "");
		datosUsuarioActualizar.addProperty("Tip_Actual", UsuarioActualizacionTipActual);
		datosUsuarioActualizar.addProperty("NumTransac", folTransa != null ? folTransa : "");
		datosUsuarioActualizar.addProperty("FechaSis", fechaSis != null ? fechaSis : "");
		
		JsonObject usuarioActualizarResultadoObjeto = this.usuarioServicio.usuarioActualizar(datosUsuarioActualizar);
		Utilerias.verificarError(usuarioActualizarResultadoObjeto);
		logger.info("- usuarioActualizarResultadoObjeto: " + usuarioActualizarResultadoObjeto);
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("Bit_TipOpe", LogoutBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);

		this.bitacoraServicio.creacionBitacora(datosBitacora);
		
		logger.info("CTRL: Terminando logout metodo...");
		return Response.ok(null, MediaType.APPLICATION_JSON)
				.build();
	}

}
