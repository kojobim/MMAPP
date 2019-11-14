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

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.ConfiguracionServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/login")
public class LoginCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(LoginCtrl.class);
	private BitacoraServicio bitacoraServicio;
	private ConfiguracionServicio configuracionServicio;
	private TransaccionServicio transaccionServicio;
	private TokenServicio tokenServicio;
	private UsuarioServicio usuarioServicio;
	
	public LoginCtrl() {
		super();
		
		this.bitacoraServicio = new BitacoraServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.tokenServicio = new TokenServicio();
		this.usuarioServicio = new UsuarioServicio();
		
	}
	
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(JsonObject datosUsuario, @Context Request solicitud) {
		logger.info("CTRL: Comenzando login metodo");
		logger.info("datosUsuario " + datosUsuario);
		
		String usuClave = Utilerias.obtenerStringPropiedad(datosUsuario, "Usu_Clave");
		 
		if(usuClave == null || usuClave.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.4");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String usuPasswo = Utilerias.obtenerStringPropiedad(datosUsuario, "Usu_Passwo"); 
		
		if(usuPasswo == null || usuPasswo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.5");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		
		String folTransa = Utilerias.obtenerStringPropiedad(folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject(), "Fol_Transa");
		
		datosUsuario.addProperty("FechaSis",fechaSis);	
		
		logger.info("datosUsuario" + datosUsuario);
		JsonObject usuarioConsultarOpResultadoObjeto = this.usuarioServicio.usuarioConsultar(datosUsuario);
		logger.info("usuarioConsultarOpResultadoObject" + usuarioConsultarOpResultadoObjeto);

		datosUsuario.addProperty("Usu_Passwo", usuPasswo);
		JsonObject usuario = Utilerias.obtenerJsonObjectPropiedad(usuarioConsultarOpResultadoObjeto, "usuario");
		
		if(usuario == null || usuario.isJsonPrimitive()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
			
		String usuNumero = Utilerias.obtenerStringPropiedad(usuario, "Usu_Numero");
		String usuStatus = Utilerias.obtenerStringPropiedad(usuario, "Usu_Status");
		
		if(usuNumero == null || (usuStatus == null || usuStatus.isEmpty())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.11");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("F")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.12");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String tokStatus = Utilerias.obtenerStringPropiedad(usuario, "Tok_Status");
		
		if(tokStatus == null || tokStatus.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(tokStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.13");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		Integer usuCoAcNe = Utilerias.obtenerIntPropiedad(usuario, "Usu_CoAcNe"); 
		Integer usuCoDeNe = Utilerias.obtenerIntPropiedad(usuario, "Usu_CoDeNe");
		Integer usuCoPaNe = Utilerias.obtenerIntPropiedad(usuario, "Usu_CoPaNe");
		
		if(usuCoAcNe == null || usuCoDeNe == null || usuCoPaNe == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
			
		if((usuCoAcNe.intValue() > 3 || usuStatus.equals("B")) && usuCoDeNe.intValue() < 3 && usuCoPaNe < 1) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.14");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuCoDeNe.intValue() > 3 && usuStatus.equals("B")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.15");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuCoPaNe.intValue() > 1 && usuStatus.equals("B")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.15");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String usuStaSes = Utilerias.obtenerStringPropiedad(usuario, "Usu_StaSes");

		if(usuStaSes == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(!usuStaSes.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.16");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("D")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.17");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("C")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.18");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(!usuStatus.equals("A")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.19");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonObject datosConfiguracion = new JsonObject();
		datosConfiguracion.addProperty("FechaSis", fechaSis);
		
		JsonObject configuracionBancoDetalleOpResultadoObjecto = this.configuracionServicio.configuracionBancoConsultarDetalle(datosConfiguracion); 
		logger.info("configuracionBancoDetalleOpResultadoObjecto" + configuracionBancoDetalleOpResultadoObjecto);
		
		JsonObject configuracionesBanco = Utilerias.obtenerJsonObjectPropiedad(configuracionBancoDetalleOpResultadoObjecto, "configuracionesBanco");
							
		JsonObject configuracionBanco = configuracionesBanco.has("configuracionBanco") ? 
				configuracionesBanco.get("configuracionBanco").isJsonObject() 
					? configuracionesBanco.get("configuracionBanco").getAsJsonObject()
						: configuracionesBanco.get("configuracionBanco").getAsJsonArray().get(0).getAsJsonObject()
				: null; 
						
		String parAcceso = Utilerias.obtenerStringPropiedad(configuracionBanco, "Par_Acceso");
		
		if(parAcceso.equals("N")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String usuPasCif = Racal.cifraPassword_HSM(datosUsuario.get("Usu_Passwo").getAsString());
		logger.info("contrasenaCifrada " + usuPasCif);
		
		if(usuPasCif.length() == 7 && usuPasCif.equals("autoriz")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(usuPasCif.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		logger.info(">>>>>>>>>>>>>usuPasCif: " + usuPasCif);
		logger.info(">>>>>>>>>>>>>usuPasswo: " + usuario.get("Usu_Passwo"));
		
		if(!usuPasCif.equals(usuario.get("Usu_Passwo").getAsString().trim())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.20");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		usuarioConsultarOpResultadoObjeto = this.usuarioServicio.usuarioConsultar(datosUsuario);
		
		JsonObject datosUsuarioActualizacion = new JsonObject();
		datosUsuarioActualizacion.addProperty("Usu_Clave", datosUsuario.get("Usu_Clave").getAsString());
		datosUsuarioActualizacion.addProperty("NumTransac", folTransa);
		datosUsuarioActualizacion.addProperty("FechaSis", fechaSis);
		
		JsonObject usuarioActualizacionOpResultadoObjecto = this.usuarioServicio.usuarioActualizar(datosUsuarioActualizacion);
		logger.info("usuarioActualizacionOpResultadoObjecto" + usuarioActualizacionOpResultadoObjecto);
		
		String tovSerie = Utilerias.obtenerStringPropiedad(usuarioConsultarOpResultadoObjeto.get("usuario").getAsJsonObject(), "Usu_FolTok");
		
		JsonObject datosTokenVerificar = new JsonObject();
		datosTokenVerificar.addProperty("Tov_Serie", tovSerie);
		datosTokenVerificar.addProperty("FechaSis", fechaSis);
		
		JsonObject tokenVerificarOpResultadoObjecto = this.tokenServicio.tokenVerificar(datosTokenVerificar);
		logger.info("tokenVerificarOpResultadoObjecto" + tokenVerificarOpResultadoObjecto);
		
		JsonObject tokenVerificar = Utilerias.obtenerJsonObjectPropiedad(tokenVerificarOpResultadoObjecto, "tokenVerificar");
		
		if(tokenVerificar != null && tokenVerificar.has("Tov_FecVen")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.21");
			bimMessageDTO.addMergeVariable("fecha", tokenVerificar.get("Tov_FecVen").getAsString());
			throw new ConflictException(bimMessageDTO.toString());
		}

		usuarioActualizacionOpResultadoObjecto = this.usuarioServicio.usuarioActualizar(datosUsuarioActualizacion);
		logger.info("usuarioActualizacionOpResultadoObjecto" + usuarioActualizacionOpResultadoObjecto);
		
		String bitUsuari = usuarioConsultarOpResultadoObjeto.get("usuario").getAsJsonObject().get("Usu_Numero").getAsString();

		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		
		String bitDireIP = solicitud.getHeader("User-Agent") == null ? solicitud.getHeader("User-Agent") : "";
		String bitPriRef = solicitud.getHeader("X-Forwarded-For") == null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		JsonObject datosBitacoraCreacion = new JsonObject();
		datosBitacoraCreacion.addProperty("Bit_Usuari", bitUsuari);
		datosBitacoraCreacion.addProperty("Bit_Fecha", fechaSis);
		datosBitacoraCreacion.addProperty("Bit_PriRef", bitPriRef);
		datosBitacoraCreacion.addProperty("Bit_DireIP", bitDireIP);
		datosBitacoraCreacion.addProperty("NumTransac", folTransa);		
		

		this.bitacoraServicio.creacionBitacora(datosBitacoraCreacion);
		
		String usuClient = Utilerias.obtenerStringPropiedad(usuario, "Usu_Client");
		String usuNombre = Utilerias.obtenerStringPropiedad(usuario, "Usu_Nombre");
		String usuEmail = Utilerias.obtenerStringPropiedad(usuario, "Usu_Email");
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(usuario, "Usu_UsuAdm");
		
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 * String usuFolTok = principalResultadoObjecto.get("usuFolTok").getAsString();
		 */
		String usuFolTok = "0416218850";
		
		JsonObject usuarioResultado = new JsonObject();
		usuarioResultado.addProperty("usuClave", usuClave);
		usuarioResultado.addProperty("usuNumero", usuNumero);
		usuarioResultado.addProperty("usuClient", usuClient);
		usuarioResultado.addProperty("usuNombre", usuNombre);
		usuarioResultado.addProperty("usuEmail", usuEmail);
		usuarioResultado.addProperty("usuUsuAdm", usuUsuAdm);
		usuarioResultado.addProperty("usuFolTok", usuFolTok);
		logger.info("CTRL: Terminando login metodo");
		return Response.ok(usuarioResultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
}
