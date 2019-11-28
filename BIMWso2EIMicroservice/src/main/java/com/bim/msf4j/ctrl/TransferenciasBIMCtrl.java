package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.service.TransferenciasBIMServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/transferencias-bim")
public class TransferenciasBIMCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(TransferenciasBIMCtrl.class);

	private TransferenciasBIMServicio tranferenciasBIMServicio;

	private static Integer transferenciasBIMMaximoPagina;

	static {
		transferenciasBIMMaximoPagina = Integer
				.parseInt(properties.getProperty("transferencias_bim_servicio.maximo_pagina"));
	}

	public TransferenciasBIMCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		tranferenciasBIMServicio = new TransferenciasBIMServicio();
		logger.info("CTRL: Terminando metodo init...");
	}

	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response transferenciasProgramadasBIMListado(@DefaultValue("1") @QueryParam("page") Integer page,
			@DefaultValue("10") @QueryParam("per_page") Integer perPage, @HeaderParam("Authorization") String token) {
		logger.info("CTRL: Comenzando transferenciasProgramadasBIMListado metodo");
		// inicia declaracion de variables
		String trbDeCuOr = null;
		String trbDeCuDe = null;
		Double trbTotPes = null;
		String trbFeSiEj = null;
		String cpTrnSec = null;
		String trbEmaBen = null;
		String trbFrecue = null;
		String cpTrbSecue = null;
		String trbFePrEn = null;
		String trbTipDur = null;
		String trbSigSec = null;
		String trbSecuen = null;
		Integer trbDiAnEm = null;
		Integer totalElementos = null;
		
		JsonObject datosTransferenciaBIMConsultar = null;
		JsonArray datosTransferenciaListado = null;
		JsonObject transferenciasBIM = null;
		JsonObject usuarioPrincipal = null;
		JsonObject transferenciasProgramadas = null;
		JsonObject transferenciaProgramadaActiva = null;
		BimMessageDTO bimMessageDTO = null;
		// termina declaraci�n de variables

		if (page <= 0) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}

		if (perPage <= 0 || perPage > transferenciasBIMMaximoPagina) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.10");
			bimMessageDTO.addMergeVariable("perPage", perPage.toString());
			bimMessageDTO.addMergeVariable("maximo", transferenciasBIMMaximoPagina.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}

		usuarioPrincipal = Utilerias.obtenerPrincipal(token);

		datosTransferenciaBIMConsultar = new JsonObject();
		datosTransferenciaBIMConsultar.addProperty("Trb_UsuAdm", usuarioPrincipal.get("usuUsuAdm").getAsString());
		datosTransferenciaBIMConsultar.addProperty("Trb_Usuari", usuarioPrincipal.get("usuNumero").getAsString());
		datosTransferenciaBIMConsultar.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		transferenciasBIM = this.tranferenciasBIMServicio.transferenciasBIMConsultar(datosTransferenciaBIMConsultar);

		if (transferenciasBIM.has("transferenciasBIM")) { // respuesta exitosa por DSS
			transferenciasProgramadas = new JsonObject(); // inicializamos respuesta
			transferenciasProgramadas.add("transferenciasProgramadas", new JsonArray()); // inicializamos respuesta vacia por security check
			transferenciasBIM = transferenciasBIM.get("transferenciasBIM").getAsJsonObject(); // obtenemos objeto transferenciasBIM para manipularlo
			
			if (transferenciasBIM.has("transferenciaBIM")) { // existe un innerObject con las transferencias?

				// solo transferencias programadas activas
				
				if(transferenciasBIM.get("transferenciaBIM").isJsonObject()) {
					
					if(logger.isDebugEnabled()) {
						logger.debug("resultset is a JsonObject: " + transferenciasBIM.get("transferenciaBIM").getAsJsonObject());
					}
					
					datosTransferenciaListado = new JsonArray();
					datosTransferenciaListado.add(transferenciasBIM.get("transferenciaBIM").getAsJsonObject());
					
				}else{
					
					datosTransferenciaListado = Utilerias.obtenerJsonArrayPropiedad(transferenciasBIM, "transferenciaBIM");
					
				}
				
				JsonArray transferenciasProgramadasActivas = Utilerias.filtrarPropiedadesArray(
						datosTransferenciaListado,
						jsonObject -> jsonObject.get("Trb_TipTra").getAsString().equals("P")
								&& jsonObject.get("Trb_Status").getAsString().equals("A"));
				
				totalElementos = transferenciasProgramadasActivas.size();
				transferenciasProgramadasActivas = Utilerias.paginado(transferenciasProgramadasActivas, page, perPage);
				
				
				// parseo de entidad
				for(JsonElement elemento : transferenciasProgramadasActivas){
					//asignaci�n de variables
					trbDeCuOr = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_DeCuOr");
					trbDeCuDe = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_DeCuDe");
					trbTotPes = Utilerias.obtenerDoublePropiedad(elemento.getAsJsonObject(), "Trb_TotPes");
					trbFeSiEj = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_FeSiEj");
					trbEmaBen = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_EmaBen");
					trbFrecue = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(),"Trb_Frecue");
					trbFePrEn = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_FePrEn");
					trbDiAnEm = Utilerias.obtenerIntPropiedad(elemento.getAsJsonObject(), "Trb_DiAnEm");
					trbFeSiEj = Utilerias.formatearFecha(trbFeSiEj, "yyyy-MM-dd");
					
					// generando String de secuencias
					trbTipDur = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_TipDur");
					trbSigSec = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(),"Trb_SigSec");
					
					
					if(trbTipDur.trim().equals("I")){ // duraci�n ilimitada
						trbSecuen = "N";
						cpTrbSecue = "Sin limite";
					}else {
						trbSecuen = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(), "Trb_Secuen");
						cpTrbSecue = trbSecuen;
					}
					
					cpTrnSec = new StringBuilder(trbSigSec).append("/").append(trbSecuen).toString();
					// String de secuencias generado exitosamente
					
					
					
					// formateo de objeto
					transferenciaProgramadaActiva = new JsonObject();
					transferenciaProgramadaActiva.addProperty("trbDeCuOr", trbDeCuOr);
					transferenciaProgramadaActiva.addProperty("trbDeCuDe",trbDeCuDe);
					transferenciaProgramadaActiva.addProperty("trbTotPes",trbTotPes);
					transferenciaProgramadaActiva.addProperty("trbFeSiEj",trbFeSiEj);
					transferenciaProgramadaActiva.addProperty("cpTrnSec",cpTrnSec);
					transferenciaProgramadaActiva.addProperty("trbEmaBen",trbEmaBen);
					transferenciaProgramadaActiva.addProperty("trbFrecue",trbFrecue);
					transferenciaProgramadaActiva.addProperty("cpTrbSecue",cpTrbSecue);
					transferenciaProgramadaActiva.addProperty("trbFePrEn",trbFePrEn);
					transferenciaProgramadaActiva.addProperty("trbDiAnEm",trbDiAnEm);
					
					transferenciasProgramadas.get("transferenciasProgramadas")
						.getAsJsonArray().add(transferenciaProgramadaActiva);
				}

			}
		}
		
		logger.info("CTRL: Terminando transferenciasProgramadasBIMListado metodo");
		return Response
				.ok(transferenciasProgramadas)
				.header("X-Total-Count", totalElementos)
				.build();
	}
}
