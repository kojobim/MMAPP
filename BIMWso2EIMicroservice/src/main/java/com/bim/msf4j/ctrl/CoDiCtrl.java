package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/codi")
public class CoDiCtrl extends BimBaseCtrl{

	private static final Logger LOGGER = Logger.getLogger(CoDiCtrl.class);
	
	private UsuarioServicio usuarioServicio;
			
	public CoDiCtrl() {
		super();
		
		this.usuarioServicio = new UsuarioServicio();
		
		
	}
	
	@Path("/autoriza")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response autorizaCoDi(JsonObject datosAutoriza, @HeaderParam("Authorization") String token) {
		LOGGER.info("CTRL: Comenzando autorizaCoDi metodo");
		JsonObject usuarioPrincipal = Utilerias.obtenerPrincipal(token);
		JsonObject usuarioRequest = null;
		JsonObject resultadoObjeto = null;
		JsonObject autorizacion = null;
		String usuPasCif  = null;
		String usuClave = Utilerias.obtenerStringPropiedad(usuarioPrincipal, "usuClave");
		String fechaSis = Utilerias.obtenerFechaSis();
		String usuPasswo = Utilerias.obtenerStringPropiedad(datosAutoriza, "password");
		
		if(usuPasswo == null || usuPasswo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.5");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		usuarioRequest = new JsonObject();
		usuarioRequest.addProperty("Usu_Clave", usuClave);
		usuarioRequest.addProperty("Usu_Passwo", usuPasswo);
		usuarioRequest.addProperty("FechaSis",fechaSis);
		
		JsonObject usuario = this.usuarioServicio.usuarioConsultar(usuarioRequest);
		usuario = Utilerias.obtenerJsonObjectPropiedad(usuario, "usuario");
		
		if(usuario == null || usuario.isJsonPrimitive()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		String usuNumero = Utilerias.obtenerStringPropiedad(usuario, "Usu_Numero");
		String usuStatus = Utilerias.obtenerStringPropiedad(usuario, "Usu_Status");
		
		if(usuNumero == null || (usuStatus == null || usuStatus.isEmpty())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.20");
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
		
		usuPasCif = Racal.cifraPassword_HSM(datosAutoriza.get("password").getAsString());
		
		if(usuPasCif.length() == 7 && usuPasCif.equals("autoriz")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(usuPasCif.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(!usuPasCif.equals(usuario.get("Usu_Passwo").getAsString().trim())) {			
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.20");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}
		
		resultadoObjeto = new JsonObject();
		autorizacion = new JsonObject();
		autorizacion.addProperty("claveCifrado", usuPasCif);
		resultadoObjeto.add("autorizacion", autorizacion);
		
		LOGGER.info("CTRL: Terminando autorizaCoDi metodo");
		return Response.ok(resultadoObjeto.toString(), MediaType.APPLICATION_JSON).build();
		
	}
	
}
