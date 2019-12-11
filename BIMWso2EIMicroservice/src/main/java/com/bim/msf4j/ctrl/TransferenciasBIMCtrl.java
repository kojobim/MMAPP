package com.bim.msf4j.ctrl;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimEmailTemplateDTO;
import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.enums.TransferenciasProgramadasFrecuenciaEnum;
import com.bim.commons.enums.TransferenciasProgramadasLimiteEnum;
import com.bim.commons.enums.TransferenciasProgramadasRecordatorioEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.CorreoServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.TransferenciasBIMServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/transferencias-bim")
public class TransferenciasBIMCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(TransferenciasBIMCtrl.class);

	private TransferenciasBIMServicio transferenciasBIMServicio;
	private TransaccionServicio transaccionServicio;
	private BitacoraServicio bitacoraServicio;
	private TokenServicio tokenServicio;
	private CorreoServicio correoServicio;
	private CuentaDestinoServicio cuentaDestinoServicio;

	private static Integer transferenciasBIMMaximoPagina;
	private static String CuentaDestinoBIMConsultarOpTipConsul;
	private static String TransferenciaBIMCreacionOpTrbTipTraI;
	private static String TransferenciaBIMCreacionOpTrbTipTraP;
	private static String TransferenciaBIMCreacionOpTrbFrecue;
	private static String TransferenciaBIMCreacionOpTrbMonOri;
	private static String TransferenciaBIMCreacionOpTrbMonDes;
	private static String TransferenciaBIMCreacionOpTrbFePrEn;
	private static String TransferenciaBIMCreacionOpTrbDurFec;
	private static String TransferenciaBIMBitacoraCreacionOpBitTipOpe;

	static {
		transferenciasBIMMaximoPagina = Integer
				.parseInt(properties.getProperty("transferencias_bim_servicio.maximo_pagina"));
		
		CuentaDestinoBIMConsultarOpTipConsul = properties.getProperty("op.cuenta_destino_bim_consultar.tip_consul.l2");
		TransferenciaBIMCreacionOpTrbTipTraI = properties.getProperty("op.transferencia_bim_creacion.trb_tip_tra.i");
		TransferenciaBIMCreacionOpTrbTipTraP = properties.getProperty("op.transferencia_bim_creacion.trb_tip_tra.p");
		TransferenciaBIMCreacionOpTrbFrecue = properties.getProperty("op.transferencia_bim_creacion.trb_frecue");
		TransferenciaBIMCreacionOpTrbMonOri = properties.getProperty("op.transferencia_bim_creacion.trb_mon_ori");
		TransferenciaBIMCreacionOpTrbMonDes = properties.getProperty("op.transferencia_bim_creacion.trb_mon_des");
		TransferenciaBIMCreacionOpTrbFePrEn = properties.getProperty("op.transferencia_bim_creacion.trb_fe_pr_en");
		TransferenciaBIMCreacionOpTrbDurFec = properties.getProperty("op.transferencia_bim_creacion.trb_dur_fec");
		TransferenciaBIMBitacoraCreacionOpBitTipOpe = properties.getProperty("op.transferencia_bim.bitacora_creacion.bit_tip_ope");
	}

	public TransferenciasBIMCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.transferenciasBIMServicio = new TransferenciasBIMServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.bitacoraServicio = new BitacoraServicio();
		this.tokenServicio = new TokenServicio();
		this.correoServicio = new CorreoServicio();
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
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
		String trbDescri = null;
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
		transferenciasBIM = this.transferenciasBIMServicio.transferenciasBIMConsultar(datosTransferenciaBIMConsultar);

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
				
				totalElementos = transferenciasProgramadasActivas != null ? transferenciasProgramadasActivas.size() : 0;
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
					trbDescri = Utilerias.obtenerStringPropiedad(elemento.getAsJsonObject(),"Trb_Descri");
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
					transferenciaProgramadaActiva.addProperty("trbDescri", trbDescri);
					
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

	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaBIMCreacion(JsonObject datosTransferenciaBIM,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaBIMCreacion metodo...");

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principal = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principal, "usuNumero");
		String usuFolTok = Utilerias.obtenerStringPropiedad(principal, "usuFolTok");
		
		JsonObject transferenciaBIM = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIM, "transferencia");
		
		String cpRSAToken = Utilerias.obtenerStringPropiedad(transferenciaBIM, "cpRSAToken");
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.debug("- folioTrasaccionResultado " +  folioTransaccionResultado);
		
		JsonObject folioTransaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(folioTransaccion, "Fol_Transa");

		String scriptName = new StringBuilder()
				.append(TransferenciasBIMCtrl.class.getName())
				.append(".transferenciaBIMCreacion")
				.toString();

		logger.debug("- scriptName  " + scriptName);
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, folTransa, scriptName);
		logger.debug("- validarToken " + validarToken);
		
		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(principal, "usuUsuAdm");
		String usuClient = Utilerias.obtenerStringPropiedad(principal, "usuClient");
		String usuEmail = Utilerias.obtenerStringPropiedad(principal, "usuEmail");
		
		String trbCueOri = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbCueOri");
		String trbCueDes = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbCueDes");
		String trbDescri = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbDescri");
		Double trbMonto = Utilerias.obtenerDoublePropiedad(transferenciaBIM, "trbMonto");		
		String trbRFC = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbRFC");
		Double trbIVA = Utilerias.obtenerDoublePropiedad(transferenciaBIM, "trbIVA");
		JsonObject cpTransferenciaProgramada = Utilerias.obtenerJsonObjectPropiedad(transferenciaBIM, "cpTransferenciaProgramada");
		
		String trbFrecue = null;
		String trbFePrEn = null;
		String trbTipDur = null;
		String trbDurFec = null;
		Integer trbDurTra = null;
		Integer trbDiAnEm = null;		
		Date fechaProgramada = null;
		Date fechaActual = null;
		
		String transferenciaBIMTrbTipTra = TransferenciaBIMCreacionOpTrbTipTraI;
		
		if(cpTransferenciaProgramada != null && cpTransferenciaProgramada.entrySet().size() > 0) {
			trbFrecue = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trbFrecue");
			trbFePrEn = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trbFePrEn");
			trbTipDur = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trbTipDur");
			trbDurFec = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trbDurFec");
			trbDurTra = Utilerias.obtenerIntPropiedad(cpTransferenciaProgramada, "trbDurTra");
			trbDiAnEm = Utilerias.obtenerIntPropiedad(cpTransferenciaProgramada, "trbDiAnEm");
			
			if(trbFrecue == null || "".equals(trbFrecue)) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbFrecue");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if(TransferenciasProgramadasFrecuenciaEnum.validarFrecuencia(trbFrecue) == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
				bimMessageDTO.addMergeVariable("nombreParametro", "trbFrecue");
				bimMessageDTO.addMergeVariable("valor", trbFrecue);
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if(trbFePrEn == null || "".equals(trbFePrEn)) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbFePrEn");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			fechaProgramada = Utilerias.convertirFecha(trbFePrEn, "yyyy-MM-dd");
			fechaActual = Utilerias.convertirFecha(fechaSis, "yyyy-MM-dd");
			
			if(fechaProgramada != null && fechaActual != null) {
				if(fechaProgramada.compareTo(fechaActual) < 0) {
					BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.64");
					bimMessageDTO.addMergeVariable("nombreParametro", "trbFePrEn");
					bimMessageDTO.addMergeVariable("valor", trbFePrEn);
					throw new BadRequestException(bimMessageDTO.toString());
				}
			}
			
			if(trbTipDur == null || "".equals(trbTipDur)) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbTipDur");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if("U".equals(trbFrecue) && "F".equals(trbTipDur)) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.70");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if(TransferenciasProgramadasLimiteEnum.validarLimite(trbTipDur) == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
				bimMessageDTO.addMergeVariable("nombreParametro", "trbTipDur");
				bimMessageDTO.addMergeVariable("valor", trbTipDur);
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if("F".equals(trbTipDur) && (trbDurFec == null || "".equals(trbDurFec))) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbDurFec");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if("T".equals(trbTipDur) && trbDurTra == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbDurTra");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if(trbDurTra <= 0) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.69");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbDurTra");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if(trbDiAnEm == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
				bimMessageDTO.addMergeVariable("nombrePropiedad", "trbDiAnEm");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			if(TransferenciasProgramadasRecordatorioEnum.validarRecordatorio(trbDiAnEm) == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
				bimMessageDTO.addMergeVariable("nombreParametro", "trbDiAnEm");
				bimMessageDTO.addMergeVariable("valor", trbDiAnEm.toString());
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			transferenciaBIMTrbTipTra = TransferenciaBIMCreacionOpTrbTipTraP;
		}
		
		JsonObject datosCuentaDestinoBIMConsultar = new JsonObject();
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", usuUsuAdm);
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Usuari", usuNumero);
		datosCuentaDestinoBIMConsultar.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", CuentaDestinoBIMConsultarOpTipConsul);
		JsonObject cuentaDestinoBIMConsultarResultado = this.cuentaDestinoServicio.cuentaDestinoBIMConsultar(datosCuentaDestinoBIMConsultar);
		logger.debug("- cuentaDestinoBIMConsultarResultado " + cuentaDestinoBIMConsultarResultado);
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIMConsultarResultado, "cuentaDestinoBIM");
		logger.debug("- cuentaDestinoBIM: " + cuentaDestinoBIM);

		JsonArray cuentasDestinoBIM = Utilerias.obtenerJsonArrayPropiedad(cuentaDestinoBIM, "cuentasDestinoBIM");
		
		if(cuentasDestinoBIM == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.66");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String trbEmaBen = null;
		for(JsonElement cuentaDestinoElemento : cuentasDestinoBIM) {
			JsonObject cuentaDestinoObjeto = cuentaDestinoElemento.getAsJsonObject();
			String cdbCuenta = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cdb_Cuenta");
			if(trbCueDes.equals(cdbCuenta))
				trbEmaBen = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cdb_EmaBen");
		}

		JsonObject datosCuentaDestinoTransferenciaBIMActivacion = new JsonObject();
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Cdb_UsuAdm", usuUsuAdm);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("NumTransac", folTransa);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("FechaSis", fechaSis);
		JsonObject datosCuentaDestinoTransferenciaBIMActivacionResultado1 = this.transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion);
		logger.debug("- datosCuentaDestinoTransferenciaBIMActivacionResultado1 " + datosCuentaDestinoTransferenciaBIMActivacionResultado1);
		
		JsonObject datosTransferenciaBIMCreacion = new JsonObject();
		datosTransferenciaBIMCreacion.addProperty("Trb_RFC", trbRFC != null ? trbRFC : "");
		datosTransferenciaBIMCreacion.addProperty("Trb_IVA", trbIVA != null ? trbIVA : 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_Client", usuClient);
		datosTransferenciaBIMCreacion.addProperty("Trb_CueOri", trbCueOri);
		datosTransferenciaBIMCreacion.addProperty("Trb_CueDes", trbCueDes);
		datosTransferenciaBIMCreacion.addProperty("Trb_Monto", trbMonto);
		datosTransferenciaBIMCreacion.addProperty("Trb_MonOri", TransferenciaBIMCreacionOpTrbMonOri);
		datosTransferenciaBIMCreacion.addProperty("Trb_MonDes", TransferenciaBIMCreacionOpTrbMonDes);
		datosTransferenciaBIMCreacion.addProperty("Trb_Descri", trbDescri);
		datosTransferenciaBIMCreacion.addProperty("Trb_UsuCap", usuNumero);
		datosTransferenciaBIMCreacion.addProperty("Trb_FecAut", fechaSis);
		datosTransferenciaBIMCreacion.addProperty("Trb_FePrEn", trbFePrEn != null ? trbFePrEn : TransferenciaBIMCreacionOpTrbFePrEn);
		datosTransferenciaBIMCreacion.addProperty("Trb_TipDur", trbTipDur != null ? trbTipDur : "");
		datosTransferenciaBIMCreacion.addProperty("Trb_DurFec", trbDurFec != null ? trbDurFec : TransferenciaBIMCreacionOpTrbDurFec);
		datosTransferenciaBIMCreacion.addProperty("Trb_DurTra", trbDurTra != null ? trbDurTra : 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_DiAnEm", trbDiAnEm != null ? trbDiAnEm : 0);
		datosTransferenciaBIMCreacion.addProperty("Trb_EmaBen", trbEmaBen != null ? trbEmaBen : "");
		datosTransferenciaBIMCreacion.addProperty("NumTransac", folTransa);
		datosTransferenciaBIMCreacion.addProperty("Trb_TipTra", transferenciaBIMTrbTipTra);
		datosTransferenciaBIMCreacion.addProperty("Trb_Frecue", trbFrecue != null ? trbFrecue : TransferenciaBIMCreacionOpTrbFrecue);
		datosTransferenciaBIMCreacion.addProperty("FechaSis", fechaSis);
		JsonObject datosTransferenciaBIMCreacionResultado = this.transferenciasBIMServicio.transferenciaBIMCreacion(datosTransferenciaBIMCreacion);
		logger.debug("- datosTransferenciaBIMCreacionResultado " + datosTransferenciaBIMCreacionResultado);
		
		JsonObject transferenciaBIMCreacion = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIMCreacionResultado, "transferenciaBIM");
		String errCodigo = Utilerias.obtenerStringPropiedad(transferenciaBIMCreacion, "Err_Codigo");
		String trbConsec = Utilerias.obtenerStringPropiedad(transferenciaBIMCreacion, "Trb_Consec");
		
		if(!"000000".equals(errCodigo)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			String errMensaj = Utilerias.obtenerStringPropiedad(transferenciaBIMCreacion, "Err_Mensaj");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		String bitPriRef = new StringBuilder()
				.append("Cuenta: ")
				.append(trbCueDes)
				.append(" BIM")
				.toString();
		
		String bitSegRef = new StringBuilder()
				.append("$ ")
				.append(trbMonto)
				.append(" Pesos")
				.toString();
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_CueOri", trbCueOri);
		datosBitacora.addProperty("Bit_CueDes", trbCueDes);
		datosBitacora.addProperty("Bit_Monto", trbMonto.toString());
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		datosBitacora.addProperty("Bit_SegRef", bitSegRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("Bit_TipOpe", TransferenciaBIMBitacoraCreacionOpBitTipOpe);
		JsonObject datosBitacoraResultado = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.debug("- datosBitacoraResultado " + datosBitacoraResultado);
		
		JsonObject datosCuentaDestinoTransferenciaBIMActivacionResultado2 = this.transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion);
		logger.debug("- datosCuentaDestinoTransferenciaBIMActivacionResultado2 " + datosCuentaDestinoTransferenciaBIMActivacionResultado2);
		
		JsonObject datosTransferenciaBIMConsultar = new JsonObject();
		datosTransferenciaBIMConsultar.addProperty("Trb_UsuAdm", usuUsuAdm);
		datosTransferenciaBIMConsultar.addProperty("Trb_Usuari", usuNumero);
		datosTransferenciaBIMConsultar.addProperty("FechaSis", fechaSis);
		JsonObject datosTransferenciaBIMConsultarResultado = this.transferenciasBIMServicio.transferenciasBIMConsultarResultSets(datosTransferenciaBIMConsultar);
		logger.debug("- datosTransferenciaBIMConsultarResultado " + datosTransferenciaBIMConsultarResultado);

		JsonObject transferenciasBIMConsultar = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIMConsultarResultado, "transferenciasBIM");
		JsonArray transferenciaBIMConsultar = Utilerias.obtenerJsonArrayPropiedad(transferenciasBIMConsultar, "transferenciaBIM");
		logger.debug("- transferenciaBIMConsultar " + transferenciaBIMConsultar);
		
		JsonArray transferenciaBIMConsultarResultadoArreglo = transferenciaBIMConsultar.get(0).getAsJsonArray();
		
		String tipTra = transferenciaBIMTrbTipTra;
		JsonArray transferenciasActivas = Utilerias.filtrarPropiedadesArray(
				transferenciaBIMConsultarResultadoArreglo,
				jsonObject -> jsonObject.get("Trb_TipTra").getAsString().equals(tipTra)
						&& jsonObject.get("Trb_Status").getAsString().equals("A")
						&& jsonObject.get("Trb_Consec").getAsString().equals(trbConsec));
		
		if(transferenciasActivas.size() == 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.67");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonObject transferenciaBIMConsultarResultadoObjeto = transferenciasActivas.get(0).getAsJsonObject();

		String trbDeCuOr = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_DeCuOr");
		String corAlias = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Cor_Alias");
		String trbDeCuDe = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_DeCuDe");
		String cdbAlias = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Cdb_Alias");
		String trbMonPes = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_MonPes");
		String trbFecAut = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_FecAut");
		String trbUsCaNo = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_UsCaNo");
		String trbDesMon = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_DesMon");
		String trbFecCap = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_FecCap");
		String trbTipTra = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_TipTra");
		String trbSecuen = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultarResultadoObjeto, "Trb_Secuen");
		
		JsonObject datosTransferenciaBIMProcesar = new JsonObject();
		datosTransferenciaBIMProcesar.addProperty("Trb_RFC", trbRFC != null ? trbRFC : "");
		datosTransferenciaBIMProcesar.addProperty("Trb_IVA", trbIVA != null ? trbIVA : 0);
		datosTransferenciaBIMProcesar.addProperty("Trb_UsuAdm", usuUsuAdm);
		datosTransferenciaBIMProcesar.addProperty("Trb_Usuari", usuNumero);
		datosTransferenciaBIMProcesar.addProperty("Trb_Client", usuClient);
		datosTransferenciaBIMProcesar.addProperty("Trb_Consec", trbConsec);
		datosTransferenciaBIMProcesar.addProperty("Trb_CueOri", trbCueOri);
		datosTransferenciaBIMProcesar.addProperty("Trb_CueDes", trbCueDes);
		datosTransferenciaBIMProcesar.addProperty("Trb_TipTra", transferenciaBIMTrbTipTra);
		datosTransferenciaBIMProcesar.addProperty("Trb_Monto", trbMonto);
		datosTransferenciaBIMProcesar.addProperty("Trb_SegRef", trbDescri);
		datosTransferenciaBIMProcesar.addProperty("NumTransac", folTransa);
		datosTransferenciaBIMProcesar.addProperty("FechaSis", fechaSis);
		JsonObject datosTransferenciaBIMProcesarResultado = this.transferenciasBIMServicio.transferenciaBIMProcesarResultSets(datosTransferenciaBIMProcesar);
		logger.debug("- datosTransferenciaBIMProcesarResultado " +  datosTransferenciaBIMProcesarResultado);
		
		JsonObject transferenciasBIMProcesar= Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIMProcesarResultado, "transferenciasBIM");
		JsonArray transferenciaBIMProcesar = Utilerias.obtenerJsonArrayPropiedad(transferenciasBIMProcesar, "transferenciaBIM");
		logger.debug("- transferenciaBIMProcesar " + transferenciaBIMProcesar);
		
		JsonArray transferenciaBIMProcesarResultadoArreglo1 = transferenciaBIMProcesar.get(0).getAsJsonArray();
		JsonObject transferenciaBIMProcesarResultadoObjeto1 = transferenciaBIMProcesarResultadoArreglo1.get(0).getAsJsonObject();
		errCodigo = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto1, "Err_Codigo");
		
		if(errCodigo != null && !"000000".equals(errCodigo)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			String errMensaj = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto1, "Err_Mensaj");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		JsonArray transferenciaBIMProcesarResultadoArreglo2 = transferenciaBIMProcesar.get(transferenciaBIMProcesar.size() - 1).getAsJsonArray();
		
		JsonArray transferenciasProcesadas = Utilerias.filtrarPropiedadesArray(
				transferenciaBIMProcesarResultadoArreglo2,
				jsonObject -> jsonObject.get("Numero").getAsString().equals(trbConsec));
		
		JsonObject transferenciaBIMProcesarResultadoObjeto2 = transferenciasProcesadas.get(0).getAsJsonObject();
		
		String trbMonEqu = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto2, "Trb_MonEqu");
		String trbStrEqu = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto2, "Trb_StrEqu");
		String trbTipCam = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto2, "Trb_TipCam");
		String trbProces = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto2, "Trb_Proces");
		String trbProgra = Utilerias.obtenerStringPropiedad(transferenciaBIMProcesarResultadoObjeto2, "Trb_Progra");
		
		JsonObject datosTransferenciaBIMFirmasConsultar = new JsonObject();
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Consec", trbConsec);
		datosTransferenciaBIMFirmasConsultar.addProperty("FechaSis", fechaSis);
		JsonObject transferenciaBIMFirmasConsultarResultado = this.transferenciasBIMServicio.transferenciaBIMFirmasConsultarResultSets(datosTransferenciaBIMFirmasConsultar);
		
		JsonObject transferenciasBIMFirmas = Utilerias.obtenerJsonObjectPropiedad(transferenciaBIMFirmasConsultarResultado, "transferenciasBIMFirmas");
		JsonArray transferenciaBIMFirmas = Utilerias.obtenerJsonArrayPropiedad(transferenciasBIMFirmas, "transferenciaBIMFirmas");
		logger.debug("- transferenciaBIMFirmas " + transferenciaBIMFirmas);
		
		JsonArray transferenciaBIMFirmasResultadoArreglo1 = transferenciaBIMFirmas.get(0).getAsJsonArray();
		
		String strComa = "";
		StringBuilder strFirmas = new StringBuilder();		
		if(transferenciaBIMFirmasResultadoArreglo1 != null) {
			for(JsonElement transferenciaBIMFirmaElemento : transferenciaBIMFirmasResultadoArreglo1) {
				JsonObject transferenciaBIMFirmaObjeto = transferenciaBIMFirmaElemento.getAsJsonObject();
				String ftbNivFir = Utilerias.obtenerStringPropiedad(transferenciaBIMFirmaObjeto, "Ftb_NivFir");
				String ftbCantid = Utilerias.obtenerStringPropiedad(transferenciaBIMFirmaObjeto, "Ftb_Cantid");
				
				strFirmas.append(strComa)
					.append(ftbNivFir)
					.append("=")
					.append(ftbCantid);
				
				if("".equals(strComa))
					strComa = ", ";
			}
		}
		
		JsonArray transferenciaBIMFirmasResultadoArreglo2 = transferenciaBIMFirmas.get(1).getAsJsonArray();
		
		strComa = "";
		StringBuilder strAutori = new StringBuilder();		
		if(transferenciaBIMFirmasResultadoArreglo2 != null) {
			for(JsonElement transferenciaBIMFirmaElemento : transferenciaBIMFirmasResultadoArreglo2) {
				JsonObject transferenciaBIMFirmaObjeto = transferenciaBIMFirmaElemento.getAsJsonObject();
				String ftbUsuNom = Utilerias.obtenerStringPropiedad(transferenciaBIMFirmaObjeto, "Ftb_UsuNom");
				
				strAutori.append(strComa)
					.append(ftbUsuNom);
				
				if("".equals(strComa))
					strComa = ", ";
			}
		}
		
		String fecAut = Utilerias.formatearFecha(trbFecAut, "yyyy-MM-dd");
		
		JsonObject datosTransferenciaBIMExitosa = new JsonObject();
		datosTransferenciaBIMExitosa.addProperty("trbDeCuOr", trbDeCuOr);
		datosTransferenciaBIMExitosa.addProperty("corAlias", corAlias);
		datosTransferenciaBIMExitosa.addProperty("trbDeCuDe", trbDeCuDe);
		datosTransferenciaBIMExitosa.addProperty("cdbAlias", cdbAlias);
		datosTransferenciaBIMExitosa.addProperty("trbMonPes", trbMonPes != null ? Double.parseDouble(trbMonPes) : 0.0);
		datosTransferenciaBIMExitosa.addProperty("trbDescri", trbDescri);
		datosTransferenciaBIMExitosa.addProperty("numTransac", folTransa);
		datosTransferenciaBIMExitosa.addProperty("trbFecAut", fecAut);
		datosTransferenciaBIMExitosa.addProperty("trbUsCaNo", trbUsCaNo);
		
		JsonObject transferenciaBIMExitosa = new JsonObject();
		transferenciaBIMExitosa.add("transferenciaExitosa", datosTransferenciaBIMExitosa);
		
		String trbProDes = null;
		boolean bTipoCambio	= false;
		if("S".equals(trbProces) && "I".equals(trbTipTra)) {
			if(trbStrEqu != null)
				bTipoCambio	= true;
			trbProDes = "Enviada";
		} else if("N".equals(trbProces) && "S".equals(trbProgra) && "P".equals(trbTipTra)) {
			trbProDes = "Programada Enviada";
		} else if("N".equals(trbProces) && "I".equals(trbTipTra)) {
			trbProDes = "Autorizada";
		} else if("N".equals(trbProces) && "N".equals(trbProgra) && "P".equals(trbTipTra)) {
			trbProDes = "Programada Autorizada";
		} else if("S".equals(trbProces) && "P".equals(trbTipTra)) {
			if(trbStrEqu != null)
				bTipoCambio	= true;
			trbProDes = "Programada Enviada";
		}
		
		String sVarVerif = new StringBuilder()
				.append("Origen: ")
				.append(trbCueOri)
				.append(" ")
				.append("Destino: ")
				.append(trbCueDes)
				.append(" ")
				.append("Cantidad: ")
				.append(trbMonto.toString())
				.append(" ")
				.append("Folio: ")
				.append(folTransa)
				.toString();
		
		String digitoVerificador = Utilerias.generarDigitoVerificador(sVarVerif);
		logger.debug("- digitoVerificador " + digitoVerificador);
		
		String transferenciaBIMClientePlantillaName = null;
		String transferenciaBIMBeneficiarioPlantillaName = null;
		
		if("I".equals(transferenciaBIMTrbTipTra)) {
			transferenciaBIMClientePlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim_inmediata.cliente.plantilla");
			transferenciaBIMBeneficiarioPlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim_inmediata.beneficiario.plantilla");
		} else if(fechaProgramada != null && fechaActual != null) {
			if(fechaProgramada.compareTo(fechaActual) == 0) {
				transferenciaBIMClientePlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim_programada.cliente.plantilla");
				transferenciaBIMBeneficiarioPlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim_programada.beneficiario.plantilla");
			} else if(fechaProgramada.compareTo(fechaActual) > 0) {
				transferenciaBIMClientePlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim_programada.cliente.plantilla");
			}
		}
		
		if(transferenciaBIMBeneficiarioPlantillaName != null) {
			String transferenciaBIMBeneficiarioAsunto = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim.beneficiario.asunto");
			String transferenciaBIMBeneficiarioPlantilla = Utilerias.obtenerPlantilla(transferenciaBIMBeneficiarioPlantillaName);
			BimEmailTemplateDTO emailTemplateTransferenciaBIMBeneficiarioDTO = new BimEmailTemplateDTO(transferenciaBIMBeneficiarioPlantilla);
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DeCuOr", Utilerias.formatearCuenta(trbCueOri, 4, 10));
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DeCuDe", Utilerias.formatearCuenta(trbCueDes, 4, 10));
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_Monto", trbMonto != null ? "$" + trbMonto.toString() : ""); 
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DesMon", trbDesMon != null ? trbDesMon.toUpperCase() : "");
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_TipCam", bTipoCambio ? "$" + trbTipCam : "");
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_MonEqu", bTipoCambio ? "$" + trbMonEqu : "");
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_StrEqu", bTipoCambio ? trbStrEqu.toUpperCase() : "");
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_Descri", trbDescri);
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("NumTransac", folTransa);
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCarDDMMYYYY", Utilerias.formatearFecha(trbFecCap, "dd-MM-yyyy"));
			emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCarHHMM", Utilerias.formatearFecha(trbFecCap, "HH:mm"));
			
			String transferenciaBIMClienteCuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateTransferenciaBIMBeneficiarioDTO);
			this.correoServicio.enviarCorreo(trbEmaBen, transferenciaBIMBeneficiarioAsunto, transferenciaBIMClienteCuerpo);
		}
		
		if(transferenciaBIMClientePlantillaName != null) {
			String transferenciaBIMClienteAsunto = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_bim.cliente.asunto");
			String transferenciaBIMClientePlantilla = Utilerias.obtenerPlantilla(transferenciaBIMClientePlantillaName);
			BimEmailTemplateDTO emailTemplateTransferenciaBIMClienteDTO = new BimEmailTemplateDTO(transferenciaBIMClientePlantilla);
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("str_Firmas", "I".equals(transferenciaBIMTrbTipTra) ? strFirmas.toString() : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_ProDes", trbProDes);
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DeCuOr", Utilerias.formatearCuenta(trbCueOri, 4, 10));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DeCuDe", Utilerias.formatearCuenta(trbCueDes, 4, 10));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_Monto", trbMonto != null ? "$" + trbMonto.toString() : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DesMon", trbDesMon != null ? trbDesMon.toUpperCase() : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_TipCam", bTipoCambio ? "$" + trbTipCam : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_MonEqu", bTipoCambio ? "$" + trbMonEqu : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_StrEqu", bTipoCambio ? trbStrEqu.toUpperCase() : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_Descri", trbDescri);
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_RFC", trbRFC != null && !"".equals(trbRFC) ? trbRFC : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_IVA", trbIVA != null && trbIVA != 0 ? trbIVA.toString() : "");
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("NumTransac", folTransa);
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_UsCaNo", trbUsCaNo);
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecCapDDMMYYYY", Utilerias.formatearFecha(trbFecCap, "dd-MM-yyyy"));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecCapHHMM", Utilerias.formatearFecha(trbFecCap, "HH:mm"));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("str_Autori", strAutori.toString());
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecAutDDMMYYYY", Utilerias.formatearFecha(trbFecAut, "dd-MM-yyyy"));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecAutHHMM", Utilerias.formatearFecha(trbFecAut, "HH:mm"));
			
			if("P".equals(transferenciaBIMTrbTipTra)) {
				emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FePrEnDDMMYYYY", Utilerias.formatearFecha(trbFePrEn, "dd-MM-yyyy"));
				emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("strFrecuencia", TransferenciasProgramadasFrecuenciaEnum.validarFrecuencia(trbFrecue));
				emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("strDuracion", "N".equals(trbSecuen) ? TransferenciasProgramadasLimiteEnum.validarLimite("I") : trbSecuen);
				emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("strRecordatorio", TransferenciasProgramadasRecordatorioEnum.validarRecordatorio(trbDiAnEm));
			}
			
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador170", digitoVerificador.substring(0,70));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador7170", digitoVerificador.substring(70,140));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador14170", digitoVerificador.substring(140,210));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador21170", digitoVerificador.substring(210,280));
			emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador28170", digitoVerificador.substring(280));
			String transferenciaBIMBeneficiarioCuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateTransferenciaBIMClienteDTO);
			this.correoServicio.enviarCorreo(usuEmail, transferenciaBIMClienteAsunto, transferenciaBIMBeneficiarioCuerpo);
		}
		logger.info("CTRL: Terminando transferenciaBIMCreacion metodo...");

		return Response.ok(transferenciaBIMExitosa, MediaType.APPLICATION_JSON)
				.build();
	}
}
