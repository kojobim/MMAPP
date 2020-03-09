package com.bim.msf4j.ctrl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimEmailTemplateDTO;
import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.enums.InversionesCategoriasEnum;
import com.bim.commons.enums.InversionesCedeCantidadInversionEnum;
import com.bim.commons.enums.InversionesCedeFormulasEnum;
import com.bim.commons.enums.InversionesCedeTiposEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.AmortizacionServicio;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.ClienteServicio;
import com.bim.commons.service.ConfiguracionServicio;
import com.bim.commons.service.CorreoServicio;
import com.bim.commons.service.InversionesServicio;
import com.bim.commons.service.ReinversionServicio;
import com.bim.commons.service.TasaServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.service.CuentaServicio;
import com.bim.commons.utils.Filtrado;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/inversiones")
public class InversionesCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(InversionesCtrl.class);

	private BitacoraServicio bitacoraServicio;
	private ConfiguracionServicio configuracionServicio;
	private TransaccionServicio transaccionServicio;
	private InversionesServicio inversionesServicio;
	private TokenServicio tokenServicio;
	private ClienteServicio clienteServicio;
	private UsuarioServicio usuarioServicio;
	private ReinversionServicio reinversionServicio;
	private TasaServicio tasaServicio;
	private CorreoServicio correoServicio;
	private CuentaServicio cuentaServicio;
	private AmortizacionServicio amortizacionServicio;

	private static String InversionesFilterBy;
	private static Integer InversionesMaximoPagina;
	private static String ClienteConsultarOpTipConsul;
	private static String ConsultaInversionBitacoraCreacionOpBitTipOpe;
	private static String ReinversionBitacoraCreacionOpBitTipOpe;
	private static String InversionBitacoraCreacionOpBitTipOpe;
	private static String InversionesCedePlazosConsultarOpPlaMoneda;
	private static String InversionesCedePlazosConsultarOpTipConsulC4;
	private static Double InversionesPagareMonTotUDI;
	private static String InversionesCedeMonTotUDI;
	private static String ConsultaInversionPagareCuentaTipoC1;
	private static Integer InversionesCedeDiaPId;
	private static String InversionesCedeInvTasBas;
	private static String InversionCedeAltaBitacoraCreacionOpBitTipOpe;
		
	public InversionesCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.bitacoraServicio = new BitacoraServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.inversionesServicio = new InversionesServicio();
		this.tokenServicio = new TokenServicio();
		this.clienteServicio = new ClienteServicio();
		this.usuarioServicio = new UsuarioServicio();
		this.reinversionServicio = new ReinversionServicio();
		this.tasaServicio = new TasaServicio();
		this.correoServicio = new CorreoServicio();
		this.configuracionServicio = new ConfiguracionServicio();
		this.cuentaServicio = new CuentaServicio();
		this.amortizacionServicio = new AmortizacionServicio();
		
		
		InversionesFilterBy = properties.getProperty("inversiones_servicio.filter_by");
		InversionesMaximoPagina = Integer.parseInt(properties.getProperty("inversiones_servicio.maximo_pagina"));
		ConsultaInversionPagareCuentaTipoC1 = properties.getProperty("op.cuenta_origen_consultar.tip_consul_c1");
		ClienteConsultarOpTipConsul = properties.getProperty("op.cliente_consultar.tip_consul");
		ConsultaInversionBitacoraCreacionOpBitTipOpe = properties.getProperty("op.consulta_inversion.bitacora_creacion.bit_tip_ope");
		ReinversionBitacoraCreacionOpBitTipOpe = properties.getProperty("op.reinversion.bitacora_creacion.bit_tip_ope");
		InversionBitacoraCreacionOpBitTipOpe = properties.getProperty("op.inversion.bitacora_creacion.bit_tip_ope");
		InversionesCedePlazosConsultarOpPlaMoneda = properties.getProperty("op.inversiones_cede_plazos_consultar.pla_moneda");
		InversionesCedePlazosConsultarOpTipConsulC4 = properties.getProperty("op.inversiones_cede_plazos_consultar.tip_consul_c4");
		InversionesPagareMonTotUDI = Double.parseDouble(properties.getProperty("op.inversiones_pagare.mon_tot_udi"));
		InversionesCedeMonTotUDI = properties.getProperty("op.inversiones_cede_alta.mon_tot_udi");
		InversionesCedeDiaPId = Integer.parseInt(properties.getProperty("op.inversiones_cede_alta.diap_id"));
		InversionesCedeInvTasBas = properties.getProperty("op.inversiones_cede_alta.inv_tas_bas");
		InversionCedeAltaBitacoraCreacionOpBitTipOpe = properties.getProperty("op.inversiones_cede_alta.bitacora_creacion.bit_tip_ope");
		
		logger.info("CTRL: Terminando metodo init...");
	}
	
	@Path("/")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inversionesListado(@QueryParam("page") String page,
			@QueryParam("per_page") String perPage, 
			@QueryParam("filter_by") String filterBy,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando inversionesListado metodo");
		
		if(page == null || perPage == null) 
			throw new BadRequestException("BIM.MENSAJ.2");
		
		if(!Utilerias.validaNumero(page)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.validaNumero(perPage)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.22");
			bimMessageDTO.addMergeVariable("perPage", perPage);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		int pageValue = Integer.parseInt(page);
		int perPageValue = Integer.parseInt(perPage);

		if(pageValue <= 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(perPageValue <= 0 || perPageValue > InversionesMaximoPagina) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.10");
			bimMessageDTO.addMergeVariable("perPage", perPage);
			bimMessageDTO.addMergeVariable("maximo", InversionesMaximoPagina.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}

        if(filterBy != null && !filterBy.equals(InversionesFilterBy)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.6");
            throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);

		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String numTransac = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_DireIP = solicitud.getHeader("X-Forwarded-For") != null ? solicitud.getHeader("X-forwarded-For") : "";
		String bit_PriRef = solicitud.getHeader("User-Agent") != null ? solicitud.getHeader("User-Agent") : "";
		
		String usuNumero = principalResultadoObjeto.get("usuNumero").getAsString();
		String usuClient = principalResultadoObjeto.get("usuClient").getAsString();
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bit_PriRef);
		datosBitacora.addProperty("Bit_DireIP", bit_DireIP);
		datosBitacora.addProperty("Bit_TipOpe", ConsultaInversionBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);

		this.bitacoraServicio.creacionBitacora(datosBitacora);

		JsonObject inversionesObtener = new JsonObject();
		inversionesObtener.addProperty("Inv_Client", usuClient);
		inversionesObtener.addProperty("FechaSis", fechaSis);
				
		JsonObject inversionesObtenerOpResultadoObjeto = this.inversionesServicio.inversionesObtener(inversionesObtener);
		logger.info("inversionesObtenerOpResultadoObjeto" + inversionesObtenerOpResultadoObjeto);
		
		Utilerias.verificarError(inversionesObtenerOpResultadoObjeto);
		
		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
		inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
		inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
	
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjeto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjeto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjeto);
		
		Utilerias.verificarError(inversionesPagareNumeroUsuarioObtenerOpResultadoObjeto);
		
		JsonArray inversionesResultado = new JsonArray();

		if(inversionesObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().has("inversion"))
			inversionesResultado.addAll(inversionesObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());
		
		if(inversionesPagareNumeroUsuarioObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().has("inversion"))
			inversionesResultado.addAll(inversionesPagareNumeroUsuarioObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());

		JsonObject datosHorario = new JsonObject();
		datosHorario.addProperty("NumTransac", numTransac);
		datosHorario.addProperty("FechaSis", fechaSis);

		logger.info("datosHorario" + datosHorario);
		JsonObject horarioInversionOpResultadoObjecto = this.configuracionServicio.horariosConsultar(datosHorario);
		logger.info("horarioInversionOpResultadoObjecto" + horarioInversionOpResultadoObjecto);

		JsonObject horariosObjecto = horarioInversionOpResultadoObjecto.get("horariosInversion").getAsJsonObject();
		JsonArray horariosArreglo = horariosObjecto.has("horarioInversion") ? horariosObjecto.get("horarioInversion").getAsJsonArray() : new JsonArray();
		
		inversionesResultado.forEach(inversionResultado -> {
			JsonObject inversionResultadoObjeto = (JsonObject) inversionResultado;
			String invCantid = inversionResultadoObjeto.has("Inv_Cantid") ? inversionResultadoObjeto.get("Inv_Cantid").getAsString() : "";
			String invFecVen = inversionResultadoObjeto.has("Inv_FecVen") ? inversionResultadoObjeto.get("Inv_FecVen").getAsString() : "";

			String horHorIni = null;
			String horHorFin = null;

			for (JsonElement horElemento : horariosArreglo) {
				JsonObject horarioObj = horElemento.getAsJsonObject();
				if("IN".equals(horarioObj.get("Hor_TipMod").getAsString())) {
					horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
					horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
				} else if("CE".equals(horarioObj.get("Hor_TipMod").getAsString())) {
					horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
					horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
				}
			}

			Date fechaVen = Utilerias.convertirFecha(invFecVen);
			Date horIni = Utilerias.convertirFecha(horHorIni);
			Date horFin = Utilerias.convertirFecha(horHorFin);

			Boolean cpRenInv = Utilerias.calcularVencimiento(fechaVen, horIni, horFin);		
			Double invCantidRedondeado = Utilerias.redondear(Double.parseDouble(invCantid), 3);
			inversionResultadoObjeto.addProperty("Inv_Cantid", invCantidRedondeado.toString());
			inversionResultadoObjeto.addProperty("cpRenInv", cpRenInv);
		});
		logger.info("inversionesResultado " +  inversionesResultado);
		JsonObject inversionesResultadoFinal = Filtrado.filtroInversiones(inversionesResultado, pageValue, perPageValue, filterBy);
		
		logger.info("CTRL: Terminando login metodo");	
		return Response.ok(inversionesResultadoFinal.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("{invNumero}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response detalleInversion(@PathParam("invNumero") String invNumero,
			@QueryParam("categoria") String categoria, @Context final Request solicitud) {
		logger.info("CTRL: Empezando detalleInversion Method...");

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);

		if(!Utilerias.validaNumero(invNumero)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.23");
			bimMessageDTO.addMergeVariable("invNumero", invNumero);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(InversionesCategoriasEnum.validarCategoria(categoria) == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.24");
				bimMessageDTO.addMergeVariable("categoria", categoria);
				throw new BadRequestException(bimMessageDTO.toString());
		}

		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String FolioTransaccionGenerarOpFolTransa = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();

		String fechaSis = Utilerias.obtenerFechaSis();

		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bitPriRef = solicitud.getHeader("User-Agent");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();

		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef != null ? bitPriRef : "");
		datosBitacora.addProperty("Bit_DireIP", bitDireIP != null ? bitDireIP : "");
		datosBitacora.addProperty("Bit_TipOpe", ConsultaInversionBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);

		logger.info("datosBitacora" + datosBitacora);
		JsonObject bitacoraCreacionOpResultadoObjeto = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		
		JsonObject datosInversion = new JsonObject();
		datosInversion.addProperty("FechaSis", fechaSis);


		JsonObject inversionConsultarOpResultadoObjeto = null;
		if (categoria.equals(InversionesCategoriasEnum.PAGARE.toString())) {
			datosInversion.addProperty("Inv_Usuari", usuNumero);
			datosInversion.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);

			inversionConsultarOpResultadoObjeto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(datosInversion);
		} else {
			datosInversion.addProperty("Inv_Client", usuClient);
			datosInversion.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
			inversionConsultarOpResultadoObjeto = this.inversionesServicio.inversionesObtener(datosInversion);
		}

		logger.info("datosInversion" + datosInversion);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);

		JsonObject inversionesObjecto = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject();
		JsonArray inversionesArreglo = inversionesObjecto.has("inversion") ? inversionesObjecto.get("inversion").getAsJsonArray() : new JsonArray();

		JsonObject datosHorario = new JsonObject();
		datosHorario.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
		datosHorario.addProperty("FechaSis", fechaSis);

		logger.info("datosHorario" + datosHorario);
		JsonObject horarioInversionOpResultadoObjecto  = this.configuracionServicio.horariosConsultar(datosHorario);
		logger.info("horarioInversionOpResultadoObjecto" + horarioInversionOpResultadoObjecto);

		JsonObject horariosObjecto = horarioInversionOpResultadoObjecto.get("horariosInversion").getAsJsonObject();
		JsonArray horariosArreglo = horariosObjecto.has("horarioInversion") ? horariosObjecto.get("horarioInversion").getAsJsonArray() : new JsonArray();

		String horHorIni = null;
		String horHorFin = null;

		for (JsonElement horElemento : horariosArreglo) {
			JsonObject horarioObj = horElemento.getAsJsonObject();
			if("PAGARE".equals(categoria) && "IN".equals(horarioObj.get("Hor_TipMod").getAsString())) {
				horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
				horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
			} else if("CE".equals(horarioObj.get("Hor_TipMod").getAsString())) {
				horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
				horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
			}
		}
		
		JsonObject resultado = null;
		for (JsonElement invElemento : inversionesArreglo) {
			JsonObject inversionObj = invElemento.getAsJsonObject();
			Boolean fotDescri = inversionObj.has("Fot_Descri") ? inversionObj.get("Fot_Descri").getAsString().equals(categoria) : true;
			Boolean invTipo = inversionObj.has("Inv_Tipo") ? inversionObj.get("Inv_Tipo").getAsString().equals("V") : true;

			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero) && fotDescri && invTipo) {
				String invFecIni = inversionObj.has("Inv_FecIni") ? inversionObj.get("Inv_FecIni").getAsString() :  "";
				String invFecVen = inversionObj.has("Inv_FecVen") ? inversionObj.get("Inv_FecVen").getAsString() : "";
				int plazo = 0;
				double intBru = 0;
				double invIntNet = 0;
				double invISRTot = 0;
				double invGat = 0;
				double invGatRea = 0;

				if (categoria.equals(InversionesCategoriasEnum.PAGARE.toString())) {
					invGat = inversionObj.has("Inv_GAT") ? inversionObj.get("Inv_GAT").getAsDouble() : 0;
					invGatRea = inversionObj.has("Inv_GATRea") ? inversionObj.get("Inv_GATRea").getAsDouble() : 0;
					plazo = inversionObj.has("Inv_Plazo") ? inversionObj.get("Inv_Plazo").getAsInt() : 0;
					intBru = inversionObj.has("Inv_TBruta") ? inversionObj.get("Inv_TBruta").getAsDouble() : 0;
					invIntNet = inversionObj.has("Imp_Intere") ? inversionObj.get("Imp_Intere").getAsDouble() : 0;
					invISRTot = inversionObj.has("Inv_ISR") ? inversionObj.get("Inv_ISR").getAsDouble() : 0;
				} else {
					invGat = inversionObj.has("Inv_Gat") ? inversionObj.get("Inv_Gat").getAsDouble() : 0;
					invGatRea = inversionObj.has("Inv_GatRea") ? inversionObj.get("Inv_GatRea").getAsDouble() : 0;
					plazo = inversionObj.has("Plazo") ? inversionObj.get("Plazo").getAsInt() : 0;

					double amoTasa = inversionObj.has("Amo_Tasa") ? inversionObj.get("Amo_Tasa").getAsDouble() : 0;
					double amoISR = inversionObj.has("Amo_ISR") ? inversionObj.get("Amo_ISR").getAsDouble() : 0;					
					intBru = amoTasa + amoISR / 10;

					invIntNet = inversionObj.has("Inv_IntNet") ? inversionObj.get("Inv_IntNet").getAsDouble() : 0;
					invISRTot = inversionObj.has("Inv_ISRTot") ? inversionObj.get("Inv_ISRTot").getAsDouble() : 0;
				}

				Date fechaIni = Utilerias.convertirFecha(invFecIni);
				Date fechaVen = Utilerias.convertirFecha(invFecVen);


				Date horIni = Utilerias.convertirFecha(horHorIni);
				Date horFin = Utilerias.convertirFecha(horHorFin);

				Boolean cpRenInv = Utilerias.calcularVencimiento(fechaVen, horIni, horFin);
				intBru = Utilerias.redondear(intBru, 2);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				resultado = new JsonObject();
				JsonObject inversion = new JsonObject();
				inversion.addProperty("invFecIni", fechaIni != null ? simpleDateFormat.format(fechaIni) : "");
				inversion.addProperty("invFecVen", fechaVen != null ? simpleDateFormat.format(fechaVen) : "");
				inversion.addProperty("invCuenta", inversionObj.has("Inv_Cuenta") ? inversionObj.get("Inv_Cuenta").getAsString() : "");
				inversion.addProperty("invGat", invGat);
				inversion.addProperty("invGatRea", invGatRea);
				inversion.addProperty("plazo", plazo);
				inversion.addProperty("intBru", intBru);
				inversion.addProperty("invIntNet", invIntNet);
				inversion.addProperty("invISRTot", invISRTot);
				inversion.addProperty("invTotal", inversionObj.has("Inv_Total") ? inversionObj.get("Inv_Total").getAsDouble() : 0);
				inversion.addProperty("cpRenInv", cpRenInv);
				resultado.add("inversion", inversion);
			}
		}

		if(resultado == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.25");
			bimMessageDTO.addMergeVariable("invNumero", invNumero);
			throw new ConflictException(bimMessageDTO.toString());
		}

		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("{invNumero}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reinversion(@PathParam("invNumero") String invNumero,	
			@QueryParam("categoria") String categoria, JsonObject inversionesObjeto, 
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando reinversion metodo");
		JsonObject renovarInversion = inversionesObjeto.getAsJsonObject("renovarInversion");	
			
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		String usuEmail = principalResultadoObjecto.get("usuEmail").getAsString();		
		 String usuFolTok = principalResultadoObjecto.get("usuFolTok").getAsString();
		
		String bitPriRef = solicitud.getHeader("User-Agent");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));

		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);
		
		Utilerias.verificarError(folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");

		/**
		 * REGLA DE NEGOCIO: valida token de transacci�n y bloquea al usuario en caso de 5 intentos fallidos
		 */

		String cpRSAToken = Utilerias.obtenerStringPropiedad(renovarInversion, "cpRSAToken");

		StringBuilder scriptName = new StringBuilder()
				.append(InversionesCtrl.class.getName())
				.append(".reinversion");

		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName.toString());

		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new ForbiddenException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		JsonObject datosInversion = new JsonObject();
		datosInversion.addProperty("FechaSis", fechaSis);
		JsonObject inversionConsultarOpResultadoObjeto = null;
		if ("PAGARE".equals(categoria)) {
			datosInversion.addProperty("Inv_Usuari", usuNumero);
			datosInversion.addProperty("NumTransac", numTransac);
			inversionConsultarOpResultadoObjeto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(datosInversion);
		} else {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.27");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		logger.info("datosInversion" + datosInversion);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);
		
		Utilerias.verificarError(inversionConsultarOpResultadoObjeto);

		JsonObject inversionesObjecto = Utilerias.obtenerJsonObjectPropiedad(inversionConsultarOpResultadoObjeto, "inversiones");
		
		if(inversionesObjecto.entrySet().isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.57");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonArray inversionesArreglo = Utilerias.obtenerJsonArrayPropiedad(inversionesObjecto, "inversion");
		
		JsonObject inversion = null;
		for (JsonElement invElemento : inversionesArreglo) {
			JsonObject inversionObj = invElemento.getAsJsonObject();
			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero)) {
				inversion = inversionObj;
			}
		}

		if(inversion == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.25");
			bimMessageDTO.addMergeVariable("invNumero", invNumero);
			throw new ConflictException(bimMessageDTO.toString());
		}

		logger.info("INVERSION " + inversion);
		
		JsonObject datosCliente = new JsonObject();
		datosCliente.addProperty("Cli_Numero", usuClient);
		datosCliente.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosCliente.addProperty("NumTransac", numTransac);
		datosCliente.addProperty("FechaSis", fechaSis);
		
		logger.info("datosCliente" + datosCliente);
		JsonObject clienteConsultarOpResultadoObjeto = this.clienteServicio.clienteConsultar(datosCliente);
		logger.info("clienteConsultarOpResultadoObjeto" + clienteConsultarOpResultadoObjeto);
		
		Utilerias.verificarError(clienteConsultarOpResultadoObjeto);
		
		JsonObject cliente = Utilerias.obtenerJsonObjectPropiedad(clienteConsultarOpResultadoObjeto, "cliente");
		String cliSucurs = Utilerias.obtenerStringPropiedad(cliente, "Cli_Sucurs");
		String cliComple = Utilerias.obtenerStringPropiedad(cliente, "Cli_Comple");

		JsonObject datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", cliSucurs);
		datosSucursal.addProperty("NumTransac", numTransac);
		datosSucursal.addProperty("FechaSis", fechaSis);

		logger.info("datosSucursal" + datosSucursal);
		JsonObject informacionSucursalObtenerOpResultadoObjeto = this.configuracionServicio.informacionSucursalObtener(datosSucursal);
		logger.info("informacionSucursalObtenerOpResultadoObjeto" + informacionSucursalObtenerOpResultadoObjeto);
		
		Utilerias.verificarError(informacionSucursalObtenerOpResultadoObjeto);

		JsonObject datosPerfilRiesgo = new JsonObject();
		datosPerfilRiesgo.addProperty("Apl_Client", usuClient);
		datosPerfilRiesgo.addProperty("NumTransac", numTransac);
		datosPerfilRiesgo.addProperty("FechaSis", fechaSis);

		logger.info("datosPerfilRiesgo" + datosPerfilRiesgo);
		JsonObject usuarioPerfilRiesgoConsultarOpResultadoObjeto = this.usuarioServicio.usuarioPerfilRiesgoConsultar(datosPerfilRiesgo);
		logger.info("usuarioPerfilRiesgoConsultarOpResultadoObjeto" + usuarioPerfilRiesgoConsultarOpResultadoObjeto);
		int plazo = Utilerias.obtenerIntPropiedad(inversion, "Inv_Plazo");

		JsonObject datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", fechaSis);
		datosFechaHabil.addProperty("NumDia", plazo);
		datosFechaHabil.addProperty("NumTransac", numTransac);
		datosFechaHabil.addProperty("FechaSis", fechaSis);

		logger.info("datosFechaHabil" + datosFechaHabil);
		JsonObject fechaHabilConsultarOpResultadoObjeto = this.reinversionServicio.fechaHabilConsultar(datosFechaHabil);
		logger.info("fechaHabilConsultarOpResultadoObjeto" + fechaHabilConsultarOpResultadoObjeto);
		
		Utilerias.verificarError(fechaHabilConsultarOpResultadoObjeto);

		String cliTipo = Utilerias.obtenerStringPropiedad(cliente, "Cli_Tipo");
		Double invCantid = Utilerias.obtenerDoublePropiedad(inversion, "Inv_Cantid");
		String fecVenInv = Utilerias.obtenerStringPropiedad(inversion, "Inv_FecVen");

		String fecVenI = Utilerias.formatearFecha(fecVenInv, "yyyy-MM-dd HH:mm:ss");

		JsonObject datosTasaCliente = new JsonObject();
		datosTasaCliente.addProperty("Cli_Numero", usuClient);
		datosTasaCliente.addProperty("Inv_Cantid", invCantid);
		datosTasaCliente.addProperty("Cli_Tipo", cliTipo);
		datosTasaCliente.addProperty("Plazo", plazo);
		datosTasaCliente.addProperty("Inv_FecVen", fecVenI != null ? fecVenI : "");
		datosTasaCliente.addProperty("NumTransac", numTransac);
		datosTasaCliente.addProperty("FechaSis", fechaSis);

		logger.info("datosTasaCliente" + datosTasaCliente);
		JsonObject tasaClienteConsultarOpResultadoObjeto = this.tasaServicio.tasaClienteConsultar(datosTasaCliente);
		logger.info("tasaClienteConsultarOpResultadoObjeto" + tasaClienteConsultarOpResultadoObjeto);
		
		Utilerias.verificarError(tasaClienteConsultarOpResultadoObjeto);

		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("NumTransac", numTransac);
		datosMoneda.addProperty("FechaSis", fechaSis);

		logger.info("datosMoneda" + datosMoneda);
		JsonObject tasaMonedaConsultarOpResultadoObjeto = this.tasaServicio.tasaMonedaConsultar(datosMoneda);
		logger.info("tasaMonedaConsultarOpResultadoObjeto" + tasaMonedaConsultarOpResultadoObjeto);
		
		Utilerias.verificarError(tasaMonedaConsultarOpResultadoObjeto);

		/**
		 * REGLA DE NEGOCIO: verifica que la cantidad de inversión en UDIS sea menor a 400,000.00 para calcular GAT y GATReal
		 */

		JsonObject monedaConsultar = Utilerias.obtenerJsonObjectPropiedad(tasaMonedaConsultarOpResultadoObjeto, "tasaMoneda");
		Double monFixCom = Utilerias.obtenerDoublePropiedad(monedaConsultar, "Mon_FixCom");
		Double MonTotUDI = 400000.00;
		Double invGAT = 0.00;
		Double invGATRea = 0.00;

		JsonObject clienteConsultar = Utilerias.obtenerJsonObjectPropiedad(tasaClienteConsultarOpResultadoObjeto, "tasaCliente");
		Double invTasInt = Utilerias.obtenerDoublePropiedad(clienteConsultar, "TasInv");

		if((invCantid / monFixCom) < MonTotUDI) {

			JsonObject datosGAT = new JsonObject();
			datosGAT.addProperty("Inv_Dias",  inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsInt() : 0);
			datosGAT.addProperty("Inv_TasInt", invTasInt);
			datosGAT.addProperty("Cue_MonInv", invCantid);
			datosGAT.addProperty("NumTransac", numTransac);
			datosGAT.addProperty("FechaSis", fechaSis);

			logger.info("datosGAT" + datosGAT);
			JsonObject tasaGATConsultaCalcularOpResultadoObjeto = this.tasaServicio.tasaGATConsultaCalcular(datosGAT);
			logger.info("tasaGATConsultaCalcularOpResultadoObjeto" + tasaGATConsultaCalcularOpResultadoObjeto);

			JsonObject GATConsultaCalcular = Utilerias.obtenerJsonObjectPropiedad(tasaGATConsultaCalcularOpResultadoObjeto, "tasaGAT");
			invGAT = Utilerias.obtenerDoublePropiedad(GATConsultaCalcular, "Inv_GAT");	

			JsonObject datosGATRea = new JsonObject();
			datosGATRea.addProperty("Inv_GAT", invGAT);
			datosGATRea.addProperty("NumTransac", numTransac);
			datosGATRea.addProperty("FechaSis", fechaSis);

			logger.info("datosGATRea" + datosGATRea);
			JsonObject tasaGATRealConsultaCalcularOpResultadoObjeto = this.tasaServicio.tasaGATRealConsultaCalcular(datosGATRea);
			logger.info("tasaGATRealConsultaCalcularOpResultadoObjeto" + tasaGATRealConsultaCalcularOpResultadoObjeto);

			JsonObject GATRealConsultaCalcular = Utilerias.obtenerJsonObjectPropiedad(tasaGATRealConsultaCalcularOpResultadoObjeto, "tasaGATReal");
			invGATRea = Utilerias.obtenerDoublePropiedad(GATRealConsultaCalcular, "Inv_GATRea");
		}	

		int invPlazo = Utilerias.obtenerIntPropiedad(inversion, "Inv_Plazo");
		JsonObject resultadoCalculaTasa = null;
		JsonObject informacionSucursal = Utilerias.obtenerJsonObjectPropiedad(informacionSucursalObtenerOpResultadoObjeto, "informacionSucursal");

		int parDiBaIn = Utilerias.obtenerIntPropiedad(informacionSucursal, "Par_DiBaIn");
		Double cliTasISR = Utilerias.obtenerDoublePropiedad(cliente, "Cli_TasISR");
		String cliCobISR = Utilerias.obtenerStringPropiedad(cliente, "Cli_CobISR");

		/**
		 * REGLA DE NEGOCIO: verifica que la cantidad de inversi�n sea mayor a 5000 y el plazo sea mayor a cero 
		 */

		if(invCantid < 5000 || invPlazo <= 0){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.26");
			bimMessageDTO.addMergeVariable("invCatid", invCantid.toString());
			throw new ConflictException(bimMessageDTO.toString());
		}

		JsonObject calculaTasa = new JsonObject();
		calculaTasa.addProperty("Inv_Plazo", invPlazo);
		calculaTasa.addProperty("Inv_Cantid", invCantid);
		calculaTasa.addProperty("TasInv", invTasInt);
		calculaTasa.addProperty("Par_DiBaIn", parDiBaIn);
		calculaTasa.addProperty("Par_ISR", cliTasISR);
		calculaTasa.addProperty("Cli_CobISR", cliCobISR);

		resultadoCalculaTasa = Utilerias.calculaTasa(calculaTasa);
		logger.info("resultadoCalculaTasa" + resultadoCalculaTasa);

		String inverNumero = Utilerias.obtenerStringPropiedad(inversion, "Inv_Numero");

		JsonObject datosStatusActualizar = new JsonObject();
		datosStatusActualizar.addProperty("Adi_Invers", inverNumero);
		datosStatusActualizar.addProperty("NumTransac", numTransac);
		datosStatusActualizar.addProperty("FechaSis", fechaSis);

		logger.info("datosStatusActualizar" + datosStatusActualizar);
		JsonObject inversionesStatusActualizarOpResultadoObjeto = this.inversionesServicio.inversionesStatusActualizar(datosStatusActualizar);
		logger.info("inversionesStatusActualizarOpResultadoObjeto" + inversionesStatusActualizarOpResultadoObjeto);
		
		Utilerias.verificarError(inversionesStatusActualizarOpResultadoObjeto);

		JsonObject fechaHabil = Utilerias.obtenerJsonObjectPropiedad(fechaHabilConsultarOpResultadoObjeto, "fechaHabil");
		
		String sigFecha = Utilerias.obtenerStringPropiedad(fechaHabil, "Fecha");
		Double invCanTot = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanTot");
		Double invTasa = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Tasa");
		Double invISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_ISR");
		Double invCapita = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Capita");
		Double invCanNet = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanNet");

		String formato = "yyyy-MM-dd";
		String rfecIn = Utilerias.formatearFecha(fechaSis, formato);
		String rfecVe = Utilerias.formatearFecha(sigFecha, formato);
		
		String invCuenta = Utilerias.obtenerStringPropiedad(inversion, "Inv_Cuenta");

		JsonObject datosInversionFinalizada = new JsonObject();
		datosInversionFinalizada.addProperty("Inv_Numero", inverNumero);
		datosInversionFinalizada.addProperty("Inv_Deposi", invCapita);
		datosInversionFinalizada.addProperty("Inv_rFecIn", rfecIn != null ? rfecIn : "");
		datosInversionFinalizada.addProperty("Inv_rFecVe", rfecVe != null ? rfecVe : "");
		datosInversionFinalizada.addProperty("Inv_rCanti", invCantid);
		datosInversionFinalizada.addProperty("Inv_rTasa", invTasa);
		datosInversionFinalizada.addProperty("Inv_rISR", invISR);
		datosInversionFinalizada.addProperty("Inv_rCuent", invCuenta);
		datosInversionFinalizada.addProperty("Inv_rTBrut", invTasInt);
		datosInversionFinalizada.addProperty("NumTransac", numTransac);
		datosInversionFinalizada.addProperty("FechaSis", fechaSis);

		logger.info("datosInversionFinalizada" + datosInversionFinalizada);
		JsonObject inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto = this.inversionesServicio.inversionesImportesDeInversionFinalizadaActualizar(datosInversionFinalizada);
		logger.info("inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto" + inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto);
		
		Utilerias.verificarError(inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto);

		JsonObject datosProcesoLiquidacion = new JsonObject();
		datosProcesoLiquidacion.addProperty("Inv_Numero", invNumero);
		datosProcesoLiquidacion.addProperty("Inv_rFecIn", rfecIn != null ? rfecIn : "");
		datosProcesoLiquidacion.addProperty("Inv_rFecVe", rfecVe != null ? rfecVe : "");
		datosProcesoLiquidacion.addProperty("Inv_rCanti", invCantid);
		datosProcesoLiquidacion.addProperty("Inv_rTasa", invTasa);
		datosProcesoLiquidacion.addProperty("Inv_rISR", invISR);
		datosProcesoLiquidacion.addProperty("Inv_rCuent", invCuenta);
		datosProcesoLiquidacion.addProperty("Dias_Base", parDiBaIn);		
		datosProcesoLiquidacion.addProperty("Inv_Fecha", rfecIn != null ? rfecIn : "");
		datosProcesoLiquidacion.addProperty("Inv_rTBrut", invTasInt);
		datosProcesoLiquidacion.addProperty("NumTransac", numTransac);		
		datosProcesoLiquidacion.addProperty("FechaSis", fechaSis);

		logger.info("datosProcesoLiquidacion" + datosProcesoLiquidacion);
		JsonObject inversionesProcesoLiquidacionGenerarOpResultadoObjeto = this.inversionesServicio.inversionesProcesoLiquidacionGenerar(datosProcesoLiquidacion);
		logger.info("inversionesProcesoLiquidacionGenerarOpResultadoObjeto" + inversionesProcesoLiquidacionGenerarOpResultadoObjeto);
		
		Utilerias.verificarError(inversionesProcesoLiquidacionGenerarOpResultadoObjeto);
		
		JsonObject ProcesoLiquidacionGenerar = Utilerias.obtenerJsonObjectPropiedad(inversionesProcesoLiquidacionGenerarOpResultadoObjeto, "procesoLiquidacionGenerar");
		String errCodigo = Utilerias.obtenerStringPropiedad(ProcesoLiquidacionGenerar, "Err_Codigo");
		String errMensaj = Utilerias.obtenerStringPropiedad(ProcesoLiquidacionGenerar, "Err_Mensaj");
		String invNueva = Utilerias.obtenerStringPropiedad(ProcesoLiquidacionGenerar, "Inv_Nueva");

		if(errCodigo == null || !"000000".equals(errCodigo)){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");			
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}

		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef != null ? bitPriRef : "");
		datosBitacora.addProperty("Bit_DireIP", bitDireIP != null ? bitDireIP : "");
		datosBitacora.addProperty("Bit_TipOpe", ReinversionBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);

		logger.info("datosBitacora" + datosBitacora);
		JsonObject bitacoraCreacionOpResultadoObjeto = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		
		Utilerias.verificarError(bitacoraCreacionOpResultadoObjeto);

		JsonObject datosIversionVsEstadoCuenta = new JsonObject();
		datosIversionVsEstadoCuenta.addProperty("Cor_Usuari", usuNumero);
		datosIversionVsEstadoCuenta.addProperty("Cor_Cuenta", invCuenta);
		datosIversionVsEstadoCuenta.addProperty("Cor_MonDia", invCanTot);
		datosIversionVsEstadoCuenta.addProperty("NumTransac", numTransac);
		datosIversionVsEstadoCuenta.addProperty("FechaSis", fechaSis);

		logger.info("datosIversionVsEstadoCuenta" + datosIversionVsEstadoCuenta);
		JsonObject inversionesContraEstadoCuentaActualizarOpResultadoObjeto = this.inversionesServicio.inversionesContraEstadoCuentaActualizar(datosIversionVsEstadoCuenta);
		logger.info("inversionesContraEstadoCuentaActualizarOpResultadoObjeto" + inversionesContraEstadoCuentaActualizarOpResultadoObjeto);
		
		Utilerias.verificarError(inversionesContraEstadoCuentaActualizarOpResultadoObjeto);
		
		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
		inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
		inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
		
		logger.info("inversionesPagareNumeroUsuarioObtener" + inversionesPagareNumeroUsuarioObtener);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);
		
		Utilerias.verificarError(inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);

		JsonObject inversiones = Utilerias.obtenerJsonObjectPropiedad(inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto, "inversiones");
		JsonArray inversionesArreglos = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");

		Double objInvGAT = null;
		String fecIni = null;
		String fecVen = null;
		String rInvCuenta = "";
		Double rInvCantid = null;
		int rPlazo = 0;	
		Double rIntTBruta = null;
		Double rImpIntere = null;
		Double rInvCanISR = null;
		Double rInvTasa = null;
		String rAdiInstLiq = "";
		Double rInvCantTot = null;

		JsonObject resultado = null;
		for (JsonElement invElemento : inversionesArreglos) {
			JsonObject inversionObj = invElemento.getAsJsonObject();

			if (inversionObj.get("Inv_Numero").getAsString().equals(invNueva)) {
				objInvGAT = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_GAT");
				Double objInvGATRea = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_GATRea");

				logger.info("invGAT calculado: " + invGAT + ", invGAT inversion: " + objInvGAT);
				logger.info("invGATRea calculado: " + invGATRea + ", invGATRea inversion: " + objInvGATRea);

				String invFecIni = Utilerias.obtenerStringPropiedad(inversionObj, "Inv_FecIni");
				String invFecVen = Utilerias.obtenerStringPropiedad(inversionObj, "Inv_FecVen");
				
				fecIni = Utilerias.formatearFecha(invFecIni, formato);
				fecVen = Utilerias.formatearFecha(invFecVen, formato);

				rInvCuenta = Utilerias.obtenerStringPropiedad(inversionObj, "Inv_Cuenta");
				rInvCantid = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_Cantid");
				rPlazo = Utilerias.obtenerIntPropiedad(inversionObj, "Inv_Plazo");
				rIntTBruta = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_TBruta");
				rImpIntere = Utilerias.obtenerDoublePropiedad(inversionObj, "Imp_Intere");
				rInvCanISR = Utilerias.obtenerDoublePropiedad(inversionObj, "Imp_ISR");
				rInvTasa = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_Tasa");
				rAdiInstLiq = Utilerias.obtenerStringPropiedad(inversionObj, "Adi_InsLiq");
				rInvCantTot = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_Total");


				resultado = new JsonObject();
				JsonObject inversionRenovada = new JsonObject();
				inversionRenovada.addProperty("invCuenta", rInvCuenta);
				inversionRenovada.addProperty("invNueva", invNueva);
				inversionRenovada.addProperty("invCantidad", rInvCantid);
				inversionRenovada.addProperty("invDeposi", invCapita);
				inversionRenovada.addProperty("invPlazo", rPlazo);
				inversionRenovada.addProperty("invTBruta",  rIntTBruta);
				inversionRenovada.addProperty("invCanBru", rImpIntere);
				inversionRenovada.addProperty("invGat", objInvGAT);
				inversionRenovada.addProperty("invGatRea", objInvGATRea);
				inversionRenovada.addProperty("invFecIni", fecIni != null ? fecIni : "");
				inversionRenovada.addProperty("invISR", invISR);
				inversionRenovada.addProperty("invCanISR", rInvCanISR);
				inversionRenovada.addProperty("invFecVen", fecVen != null ? fecVen : "");
				inversionRenovada.addProperty("invTasa", rInvTasa);
				inversionRenovada.addProperty("invCanNet", invCanNet);
				inversionRenovada.addProperty("adiInsLiq", rAdiInstLiq);
				inversionRenovada.addProperty("invCanTot", rInvCantTot);
				inversionRenovada.addProperty("usuNombre", cliComple);
				resultado.add("inversionRenovada", inversionRenovada);
			}
		}
		
		/**
		 * REGLA DE NEGOCIO: Envío de correo con plantilla establecida por BIM y encriptado de digito verificador
		 */

		String asunto = Utilerias.obtenerPropiedadPlantilla("mail.reinversion.asunto");
		String plantilla = Utilerias.obtenerPlantilla("reinversion");
		String invNuevamen = invNueva.substring(invNueva.length()-7);		
		String rInvCuentamen = rInvCuenta.substring(rInvCuenta.length()-4);

		StringBuilder invNuevaOcu = new StringBuilder()
				.append("**************")
				.append(invNuevamen);		
		
		StringBuilder invCuentaOcu = new StringBuilder()
				.append("**************")
				.append(rInvCuentamen);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fechaSisRedu = null;
		try {
			fechaSisRedu = sdf.parse(fechaSis);
		} catch (Exception e) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		String strVerifi = "Origen:" + rInvCuenta + " Destino:" + invNueva + " Cantidad:" + rInvCantid + " Folio:" + numTransac;
		String digitoVerificador = Utilerias.generarDigitoVerificador(strVerifi);
		String strVerifi1 = digitoVerificador.substring(0, 70);
		String strVerifi2 = digitoVerificador.substring(70, 140);
		String strVerifi3 = digitoVerificador.substring(140, 210);
		String strVerifi4 = digitoVerificador.substring(210, 280);
		String strVerifi5 = digitoVerificador.substring(280);

		DecimalFormat formatter = new DecimalFormat("#,###.00");

		BimEmailTemplateDTO emailTemplateDTO = new BimEmailTemplateDTO(plantilla);
		emailTemplateDTO.addMergeVariable("Inv_Nueva", invNuevaOcu.toString());
		emailTemplateDTO.addMergeVariable("Inv_Cuenta", invCuentaOcu.toString());
		emailTemplateDTO.addMergeVariable("Inv_Cantid", String.valueOf(formatter.format(rInvCantid)));
		emailTemplateDTO.addMergeVariable("Inv_Deposi", String.valueOf(formatter.format(invCapita)));
		emailTemplateDTO.addMergeVariable("Inv_Plazo", String.valueOf(rPlazo));
		emailTemplateDTO.addMergeVariable("Inv_InfGAT", String.valueOf(formatter.format(objInvGAT)));
		emailTemplateDTO.addMergeVariable("Inv_FecIni", fecIni != null ? fecIni : "");
		emailTemplateDTO.addMergeVariable("Inv_FecVen", fecVen != null ? fecVen : "");
		emailTemplateDTO.addMergeVariable("Str_InsLiq", rAdiInstLiq);
		emailTemplateDTO.addMergeVariable("Inv_TBruta", rIntTBruta.toString());
		emailTemplateDTO.addMergeVariable("Inv_CanBru", String.valueOf(formatter.format(rImpIntere)));
		emailTemplateDTO.addMergeVariable("Inv_ISR", invISR.toString());
		emailTemplateDTO.addMergeVariable("Inv_CanISR", String.valueOf(formatter.format(rInvCanISR)));
		emailTemplateDTO.addMergeVariable("Inv_Tasa", String.valueOf(formatter.format(rInvTasa)));
		emailTemplateDTO.addMergeVariable("Inv_CanNet", String.valueOf(formatter.format(invCanNet)));
		emailTemplateDTO.addMergeVariable("Inv_CanTot", String.valueOf(formatter.format(rInvCantTot)));
		emailTemplateDTO.addMergeVariable("NumTransac", numTransac);
		emailTemplateDTO.addMergeVariable("Usu_Nombre", cliComple);
		emailTemplateDTO.addMergeVariable("fechaSis", fechaSisRedu != null ? sdf.format(fechaSisRedu) : "");
		emailTemplateDTO.addMergeVariable("Str_Verifi1", strVerifi1);
		emailTemplateDTO.addMergeVariable("Str_Verifi2", strVerifi2);
		emailTemplateDTO.addMergeVariable("Str_Verifi3", strVerifi3);
		emailTemplateDTO.addMergeVariable("Str_Verifi4", strVerifi4);
		emailTemplateDTO.addMergeVariable("Str_Verifi5", strVerifi5);
		String cuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateDTO);

		correoServicio.enviarCorreo(usuEmail, asunto, cuerpo);

		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("plazos")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerPlazosInversion(@QueryParam("producto") String producto,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando inversionesPlazosListado metodo");
		
		String plaProduc= InversionesCedeTiposEnum.validarProducto(producto);
		
		if (plaProduc == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.71");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		Utilerias.verificarError(principalResultadoObjeto);
		
		String fechaSis = Utilerias.obtenerFechaSis();

		JsonObject datosInversionesCedePlazos = new JsonObject();
		datosInversionesCedePlazos.addProperty("Pla_Moneda", InversionesCedePlazosConsultarOpPlaMoneda);
		datosInversionesCedePlazos.addProperty("Pla_Produc", plaProduc);
		datosInversionesCedePlazos.addProperty("FechaSis", fechaSis);
				
		JsonObject inversionesCedePlazosConsultarOpResultado = this.inversionesServicio.inversionesCedePlazosConsultar(datosInversionesCedePlazos);
		logger.info("inversionesCedePlazosConsultarOpResultado" + inversionesCedePlazosConsultarOpResultado);
		
		Utilerias.verificarError(inversionesCedePlazosConsultarOpResultado);
		
		JsonObject inversionesCedePlazosObjeto = Utilerias.obtenerJsonObjectPropiedad(inversionesCedePlazosConsultarOpResultado, "plazos");		
		
		JsonArray inversionesCedePlazosArreglo;
		
		if (!inversionesCedePlazosObjeto.isJsonNull()) {
			inversionesCedePlazosArreglo = Utilerias.obtenerJsonArrayPropiedad(inversionesCedePlazosObjeto, "plazo");
		} else {
			inversionesCedePlazosArreglo = new JsonArray();
		}
		
		JsonObject resultado = new JsonObject();
		JsonArray plazosDeInversion = new JsonArray();
		
		for (JsonElement invPlazoElemento : inversionesCedePlazosArreglo) {
			JsonObject invPlazoObjeto = invPlazoElemento.getAsJsonObject();
			
			if (!invPlazoObjeto.isJsonNull()) {
				JsonObject invPlazo = new JsonObject();
				invPlazo.addProperty("plaDescri", Utilerias.obtenerStringPropiedad(invPlazoObjeto, "Pla_Descri"));
				invPlazo.addProperty("plaNumero", Utilerias.obtenerStringPropiedad(invPlazoObjeto, "Pla_Numero"));
				plazosDeInversion.add(invPlazo);
			}
		}
		
		resultado.add("plazosDeInversion", plazosDeInversion);
		
		logger.info("CTRL: Terminando inversionesPlazosListado metodo");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/pagare/calculadora")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculadora(@QueryParam("monto") String monto, 
			@QueryParam("plazo") String plazo,
			@QueryParam("fec_ven") String invFecVen,
			@HeaderParam("Authorization") String token,
			@Context final Request solicitud) {
		logger.info("CTRL: Empezando calculadora Method...");

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		String fechaSis = Utilerias.obtenerFechaSis();
		
		// Inicia declaración de variables
		String plazoTex = null;
		String montoTex = null;
		String cliTipo = null;
		String cliSucurs = null;
		String cliCobISR = null;
		String numTransac = null;
		String fecVenI = null;
		String invGATStr = "N.A.";
		String invGATReaStr = "N.A.";
		Double invCanBru = null;
		Double invCanTot = null;
		Double invTasa = null;
		Double invISR = null;
		Double invCanISR = null;
		Double invCapita = null;
		Double invCanNet = null;
		Double cliTasISR = null;	
		Double monFixCom = null;
		Double invGAT = null;
		Double invGATRea = null;
		Double invTasInt = null;
		Integer parDiBaIn = null;
		Integer plazoInt = null;
		Integer montoInt = null;
		
		JsonObject resultadoCalculaTasa = null;
		JsonObject cliente = null;
		JsonObject datosCliente = null;
		JsonObject transaccion = null;
		JsonObject folioTransaccionGenerarOpResultadoObjeto = null;
		JsonObject clienteConsultarOpResultadoObjeto = null;
		JsonObject datosTasaCliente = null;
		JsonObject tasaClienteConsultarOpResultadoObjeto = null;
		JsonObject datosMoneda = null;
		JsonObject tasaMonedaConsultarOpResultadoObjeto = null;
		JsonObject monedaConsultar = null;
		JsonObject clienteConsultar = null;
		JsonObject datosGAT = null;
		JsonObject tasaGATConsultaCalcularOpResultadoObjeto = null;
		JsonObject GATConsultaCalcular = null;
		JsonObject datosGATRea = null;
		JsonObject tasaGATRealConsultaCalcularOpResultadoObjeto = null;
		JsonObject GATRealConsultaCalcular = null;
		JsonObject datosSucursal = null;
		JsonObject informacionSucursalObtenerOpResultadoObjeto = null;
		JsonObject informacionSucursal = null;
		JsonObject calculaTasa = null;
		JsonObject resultado = null;
		JsonObject calcularora = null;
		
		if(plazo == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "plazo");
			throw new BadRequestException(bimMessageDTO.toString());
		}		
		if(invFecVen == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "fec_Ven");
			throw new BadRequestException(bimMessageDTO.toString());
		}		
		if(monto == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "monto");
			throw new BadRequestException(bimMessageDTO.toString());
		}		
		if(!Utilerias.validaNumero(plazo)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "plazo");
			bimMessageDTO.addMergeVariable("valor", plazoTex);
			throw new BadRequestException(bimMessageDTO.toString());
		}		
		if(!Utilerias.validaNumero(monto)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "monto");
			bimMessageDTO.addMergeVariable("valor", montoTex);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		/**
		 * REGLA DE NEGOCIO: verifica que la cantidad de inversi�n sea mayor a 5000 y el plazo sea mayor a cero 
		 */
		plazoInt = Integer.parseInt(plazo);
		montoInt = Integer.parseInt(monto);
		if(plazoInt <= 0){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.73");
			bimMessageDTO.addMergeVariable("nombreParametro", "plazo");
			throw new ConflictException(bimMessageDTO.toString());
		}		
		if(montoInt < 5000){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.26");
			bimMessageDTO.addMergeVariable("invCatid", monto.toString());
			throw new ConflictException(bimMessageDTO.toString());
		}	
		
		folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		if(logger.isDebugEnabled()) {
			logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);
		}
		Utilerias.verificarError(folioTransaccionGenerarOpResultadoObjeto);
		transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");
		
		datosCliente = new JsonObject();
		datosCliente.addProperty("Cli_Numero", usuClient);
		datosCliente.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosCliente.addProperty("NumTransac", numTransac);
		datosCliente.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
			logger.info("datosCliente" + datosCliente);
		}
		clienteConsultarOpResultadoObjeto = this.clienteServicio.clienteConsultar(datosCliente);
		if(logger.isDebugEnabled()) {
			logger.info("clienteConsultarOpResultadoObjeto" + clienteConsultarOpResultadoObjeto);
		}
		Utilerias.verificarError(clienteConsultarOpResultadoObjeto);		
		cliente = Utilerias.obtenerJsonObjectPropiedad(clienteConsultarOpResultadoObjeto, "cliente");		
		cliTipo = Utilerias.obtenerStringPropiedad(cliente, "Cli_Tipo");
		cliSucurs = Utilerias.obtenerStringPropiedad(cliente, "Cli_Sucurs");
		cliTasISR = Utilerias.obtenerDoublePropiedad(cliente, "Cli_TasISR");
		cliCobISR = Utilerias.obtenerStringPropiedad(cliente, "Cli_CobISR");

		fecVenI = Utilerias.formatearFecha(invFecVen, "yyyy-MM-dd HH:mm:ss");

		datosTasaCliente = new JsonObject();
		datosTasaCliente.addProperty("Cli_Numero", usuClient);
		datosTasaCliente.addProperty("Inv_Cantid", montoInt);
		datosTasaCliente.addProperty("Cli_Tipo", cliTipo);
		datosTasaCliente.addProperty("Plazo", plazoInt);
		datosTasaCliente.addProperty("Inv_FecVen", fecVenI);
		datosTasaCliente.addProperty("NumTransac", numTransac);
		datosTasaCliente.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
			logger.info("datosTasaCliente" + datosTasaCliente);
		}
		tasaClienteConsultarOpResultadoObjeto = this.tasaServicio.tasaClienteConsultar(datosTasaCliente);
		if(logger.isDebugEnabled()) {
			logger.info("tasaClienteConsultarOpResultadoObjeto" + tasaClienteConsultarOpResultadoObjeto);
		}
		Utilerias.verificarError(tasaClienteConsultarOpResultadoObjeto);

		datosMoneda = new JsonObject();
		datosMoneda.addProperty("NumTransac", numTransac);
		datosMoneda.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
			logger.info("datosMoneda" + datosMoneda);
		}
		tasaMonedaConsultarOpResultadoObjeto = this.tasaServicio.tasaMonedaConsultar(datosMoneda);
		if(logger.isDebugEnabled()) {
			logger.info("tasaMonedaConsultarOpResultadoObjeto" + tasaMonedaConsultarOpResultadoObjeto);
		}
		Utilerias.verificarError(tasaMonedaConsultarOpResultadoObjeto);

		/**
		 * REGLA DE NEGOCIO: verifica que la cantidad de inversi�n en UDIS sea menor a 400,000.00 para calcular GAT y GATReal
		 */

		monedaConsultar = Utilerias.obtenerJsonObjectPropiedad(tasaMonedaConsultarOpResultadoObjeto, "tasaMoneda");
		monFixCom = Utilerias.obtenerDoublePropiedad(monedaConsultar, "Mon_FixCom");
		invGAT = 0.00;
		invGATRea = 0.00;

		clienteConsultar = Utilerias.obtenerJsonObjectPropiedad(tasaClienteConsultarOpResultadoObjeto, "tasaCliente");
		invTasInt = Utilerias.obtenerDoublePropiedad(clienteConsultar, "TasInv");

		if((montoInt / monFixCom) < InversionesPagareMonTotUDI) {

			datosGAT = new JsonObject();
			datosGAT.addProperty("Inv_Dias",  plazoInt);
			datosGAT.addProperty("Inv_TasInt", invTasInt);
			datosGAT.addProperty("Cue_MonInv", montoInt);
			datosGAT.addProperty("NumTransac", numTransac);
			datosGAT.addProperty("FechaSis", fechaSis);
			
			if(logger.isDebugEnabled()) {
				logger.info("datosGAT" + datosGAT);
			}
			tasaGATConsultaCalcularOpResultadoObjeto = this.tasaServicio.tasaGATConsultaCalcular(datosGAT);
			if(logger.isDebugEnabled()) {
				logger.info("tasaGATConsultaCalcularOpResultadoObjeto" + tasaGATConsultaCalcularOpResultadoObjeto);
			}
			GATConsultaCalcular = Utilerias.obtenerJsonObjectPropiedad(tasaGATConsultaCalcularOpResultadoObjeto, "tasaGAT");
			invGAT = Utilerias.obtenerDoublePropiedad(GATConsultaCalcular, "Inv_GAT");	

			datosGATRea = new JsonObject();
			datosGATRea.addProperty("Inv_GAT", invGAT);
			datosGATRea.addProperty("NumTransac", numTransac);
			datosGATRea.addProperty("FechaSis", fechaSis);
			if(logger.isDebugEnabled()) {
				logger.info("datosGATRea" + datosGATRea);
			}
			tasaGATRealConsultaCalcularOpResultadoObjeto = this.tasaServicio.tasaGATRealConsultaCalcular(datosGATRea);
			if(logger.isDebugEnabled()) {
				logger.info("tasaGATRealConsultaCalcularOpResultadoObjeto" + tasaGATRealConsultaCalcularOpResultadoObjeto);
			}
			GATRealConsultaCalcular = Utilerias.obtenerJsonObjectPropiedad(tasaGATRealConsultaCalcularOpResultadoObjeto, "tasaGATReal");
			invGATRea = Utilerias.obtenerDoublePropiedad(GATRealConsultaCalcular, "Inv_GATRea");
		}	

		datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", cliSucurs);
		datosSucursal.addProperty("NumTransac", numTransac);
		datosSucursal.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
			logger.info("datosSucursal" + datosSucursal);
		}
		informacionSucursalObtenerOpResultadoObjeto = this.configuracionServicio.informacionSucursalObtener(datosSucursal);
			if(logger.isDebugEnabled()) {
		logger.info("informacionSucursalObtenerOpResultadoObjeto" + informacionSucursalObtenerOpResultadoObjeto);
		}
		Utilerias.verificarError(informacionSucursalObtenerOpResultadoObjeto);
		informacionSucursal = Utilerias.obtenerJsonObjectPropiedad(informacionSucursalObtenerOpResultadoObjeto, "informacionSucursal");
		parDiBaIn = Utilerias.obtenerIntPropiedad(informacionSucursal, "Par_DiBaIn");

		calculaTasa = new JsonObject();
		calculaTasa.addProperty("Inv_Plazo", plazoInt);
		calculaTasa.addProperty("Inv_Cantid", montoInt);
		calculaTasa.addProperty("TasInv", invTasInt);
		calculaTasa.addProperty("Par_DiBaIn", parDiBaIn);
		calculaTasa.addProperty("Par_ISR", cliTasISR);
		calculaTasa.addProperty("Cli_CobISR", cliCobISR);

		resultadoCalculaTasa = Utilerias.calculaTasa(calculaTasa);
		if(logger.isDebugEnabled()) {
			logger.info("resultadoCalculaTasa" + resultadoCalculaTasa);
		}
		invCanBru = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanBru");
		invCanTot = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanTot");
		invTasa = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Tasa");
		invISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_ISR");
		invCanISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanISR");
		invCapita = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Capita");
		invCanNet = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanNet");
				
		resultado = new JsonObject();
		calcularora = new JsonObject();
		calcularora.addProperty("invCantidad", invCapita);	
		calcularora.addProperty("invTBruta",  invTasInt);
		calcularora.addProperty("invCanBru", Utilerias.redondear(invCanBru, 2));
		calcularora.addProperty("invGat", invGAT == 0 ? invGATStr : invGAT.toString());
		calcularora.addProperty("invGatRea", invGATRea == 0 ? invGATReaStr : invGATRea.toString());
		calcularora.addProperty("invISR", invISR);
		calcularora.addProperty("invCanISR", Utilerias.redondear(invCanISR, 2));
		calcularora.addProperty("invTasa", invTasa);
		calcularora.addProperty("invCanNet", Utilerias.redondear(invCanNet, 2));
		calcularora.addProperty("invCanTot", Utilerias.redondear(invCanTot, 2));
		resultado.add("Calculadora", calcularora);
		
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("duracion")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerPeriodoDuracion(@QueryParam("pla_numero") String plaNumero,
			@QueryParam("producto") String producto, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerPeriodoDuracion metodo");
		
		if(plaNumero == null || plaNumero.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
			bimMessageDTO.addMergeVariable("nombrePropiedad", "pla_numero");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String plaProduc= InversionesCedeTiposEnum.validarProducto(producto);
		
		if (plaProduc == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.71");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		Utilerias.verificarError(principalResultadoObjeto);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosInversionesCedePlazos = new JsonObject();
		datosInversionesCedePlazos.addProperty("Pla_Numero", plaNumero);
		datosInversionesCedePlazos.addProperty("Pla_Produc", plaProduc);
		datosInversionesCedePlazos.addProperty("Tip_Consul", InversionesCedePlazosConsultarOpTipConsulC4);
		datosInversionesCedePlazos.addProperty("FechaSis", fechaSis);
		
		JsonObject inversionesCedePlazosConsultarOpResultado = this.inversionesServicio.inversionesCedePlazosConsultar(datosInversionesCedePlazos);
		logger.debug("inversionesCedePlazosConsultarOpResultado" + inversionesCedePlazosConsultarOpResultado);
		
		Utilerias.verificarError(inversionesCedePlazosConsultarOpResultado);
		
		JsonObject inversionesCedePlazoObjeto = Utilerias.obtenerJsonObjectPropiedad(inversionesCedePlazosConsultarOpResultado, "plazo");
		
		if(inversionesCedePlazoObjeto.isJsonNull() || inversionesCedePlazoObjeto.entrySet().size() == 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.74");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		
		int plaDias = Utilerias.obtenerIntPropiedad(inversionesCedePlazoObjeto, "Pla_Dias");
		Date fechaIni = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(fechaSis), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
		String cpFechaIni = sdf.format(fechaIni);
		Date fechaFin = Utilerias.agregarDiasAFecha(fechaIni, plaDias);
		
		JsonObject datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", sdf.format(fechaFin));
		datosFechaHabil.addProperty("FechaSis", fechaSis);
		
		JsonObject fechaHabilConsultarOpResultado = this.reinversionServicio.fechaHabilConsultar(datosFechaHabil);
		logger.debug("fechaHabilConsultarOpResultado" + fechaHabilConsultarOpResultado);
		
		Utilerias.verificarError(fechaHabilConsultarOpResultado);
		
		JsonObject fechaHabilConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(fechaHabilConsultarOpResultado, "fechaHabil");
		
		JsonObject resultado = new JsonObject();
		JsonObject duracionInversion = new JsonObject();
		
		String fecha = Utilerias.obtenerStringPropiedad(fechaHabilConsultarObjeto, "Fecha");
		fechaFin = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(fecha), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));	
		String cpFechaFin = sdf.format(fechaFin);
		
		duracionInversion.addProperty("cpFechaIni", cpFechaIni);
		duracionInversion.addProperty("cpFechaFin", cpFechaFin);
		
		resultado.add("duracionInversion", duracionInversion);
		
		logger.info("CTRL: Terminando obtenerPeriodoDuracion metodo");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("fecha-habil")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response siguienteFechaHabil(@QueryParam("plazo") String plazo,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando siguenteFechaHabil metodo");

		String formato = "yyyy-MM-dd";
		String bearerToken = null;
		String fechaSis = null;
		String Fecha = null;
		String invFecVen = null;
		String fechaFinStr = null;
		Integer dias = null;
		Integer plazoFin = null;
		Integer plazoInt = null;
		JsonObject datosFechaHabil = null;
		JsonObject principalResultadoObjeto = null;
		JsonObject FechaHabil = null;
		JsonObject resultado = null;
		JsonObject fechaHabilConsultarOpResultado = null;
		JsonObject fechaHabilConsultarObjeto = null;
		Date fechaIni = null;
		Date fechaFin = null;
		
		bearerToken = solicitud.getHeader("Authorization");
		principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		Utilerias.verificarError(principalResultadoObjeto);
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");	
		
		if(plazo == null || plazo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
			bimMessageDTO.addMergeVariable("nombrePropiedad", "plazo");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		if(!Utilerias.validaNumero(plazo)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "plazo");
			bimMessageDTO.addMergeVariable("valor", plazo);
			throw new BadRequestException(bimMessageDTO.toString());
		}

		plazoInt = Integer.parseInt(plazo);
		if(plazoInt <= 0){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.73");
			bimMessageDTO.addMergeVariable("nombreParametro", "plazo");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		fechaSis = Utilerias.obtenerFechaSis();	
		fechaIni = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(fechaSis), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
		fechaFin = Utilerias.agregarDiasAFecha(fechaIni, plazoInt);
		fechaFinStr = sdf.format(fechaFin);
		datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", fechaFinStr);
		datosFechaHabil.addProperty("FechaSis", fechaSis);
		
		fechaHabilConsultarOpResultado = this.reinversionServicio.fechaHabilConsultar(datosFechaHabil);
		if(logger.isDebugEnabled()) {
		logger.debug("fechaHabilConsultarOpResultado" + fechaHabilConsultarOpResultado);
		}
		Utilerias.verificarError(fechaHabilConsultarOpResultado);		
		fechaHabilConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(fechaHabilConsultarOpResultado, "fechaHabil");

		Fecha = Utilerias.obtenerStringPropiedad(fechaHabilConsultarObjeto, "Fecha");
		dias = Utilerias.obtenerIntPropiedad(fechaHabilConsultarObjeto, "Dias");
		invFecVen = Utilerias.formatearFecha(Fecha, formato);		
		plazoFin = plazoInt + dias;
		
		resultado = new JsonObject();				
		FechaHabil = new JsonObject();
		FechaHabil.addProperty("plazo", plazoFin);
		FechaHabil.addProperty("invFecVen", invFecVen);
		
		resultado.add("siguienteFechaHabil", FechaHabil);
		
		logger.info("CTRL: Terminando siguenteFechaHabil metodo");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("cede/calculadora")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerCalculosInversionCede(@QueryParam("producto") String producto,
			@QueryParam("pla_numero") String plaNumero, @QueryParam("fec_ini") String fecIni,
			@QueryParam("monto") Double monto, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerCalculosInversionCede metodo");
		
		String plaProduc = InversionesCedeTiposEnum.validarProducto(producto);
		
		if (plaProduc == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.71");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(plaNumero == null || plaNumero.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
			bimMessageDTO.addMergeVariable("nombrePropiedad", "pla_numero");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(fecIni == null || fecIni.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.68");
			bimMessageDTO.addMergeVariable("nombrePropiedad", "fecIni");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		if(Utilerias.formatearFecha(fecIni) == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		Double cantidadInversion = InversionesCedeCantidadInversionEnum.validarCantidadInversion(producto);
		
		if(monto == null || monto < cantidadInversion) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.75");
			bimMessageDTO.addMergeVariable("invCantid", cantidadInversion.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		Utilerias.verificarError(principalResultadoObjeto);
		
		String usuClient = principalResultadoObjeto.get("usuClient").getAsString();
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosClienteConsultar = new JsonObject();
		datosClienteConsultar.addProperty("Cli_Numero", usuClient);
		datosClienteConsultar.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosClienteConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject clienteConsultarOpResultado = this.clienteServicio.clienteConsultar(datosClienteConsultar);
		logger.debug("clienteConsultarOpResultado" + clienteConsultarOpResultado);
		
		Utilerias.verificarError(clienteConsultarOpResultado);
		
		JsonObject clienteConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(clienteConsultarOpResultado, "cliente");
		String cliSucurs = Utilerias.obtenerStringPropiedad(clienteConsultarObjeto, "Cli_Sucurs");
		Double cliTasISR = Utilerias.obtenerDoublePropiedad(clienteConsultarObjeto, "Cli_TasISR");
		String cliCobISR = Utilerias.obtenerStringPropiedad(clienteConsultarObjeto, "Cli_CobISR");

		JsonObject datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", cliSucurs);
		datosSucursal.addProperty("FechaSis", fechaSis);

		logger.debug("datosSucursal" + datosSucursal);
		JsonObject informacionSucursalObtenerOpResultado = this.configuracionServicio.informacionSucursalObtener(datosSucursal);
		logger.debug("informacionSucursalObtenerOpResultado" + informacionSucursalObtenerOpResultado);
		
		Utilerias.verificarError(informacionSucursalObtenerOpResultado);
		
		JsonObject informacionSucursalObtenerObjeto = Utilerias.obtenerJsonObjectPropiedad(informacionSucursalObtenerOpResultado, "informacionSucursal");
		Integer parDiBaIn = Utilerias.obtenerIntPropiedad(informacionSucursalObtenerObjeto, "Par_DiBaIn");
		
		JsonObject datosInversionesCedePlazos = new JsonObject();
		datosInversionesCedePlazos.addProperty("Pla_Numero", plaNumero);
		datosInversionesCedePlazos.addProperty("Pla_Produc", plaProduc);
		datosInversionesCedePlazos.addProperty("Tip_Consul", InversionesCedePlazosConsultarOpTipConsulC4);
		datosInversionesCedePlazos.addProperty("FechaSis", fechaSis);
		
		JsonObject inversionesCedePlazosConsultarOpResultado = this.inversionesServicio.inversionesCedePlazosConsultar(datosInversionesCedePlazos);
		logger.debug("inversionesCedePlazosConsultarOpResultado" + inversionesCedePlazosConsultarOpResultado);
		
		Utilerias.verificarError(inversionesCedePlazosConsultarOpResultado);
		
		JsonObject inversionesCedePlazoObjeto = Utilerias.obtenerJsonObjectPropiedad(inversionesCedePlazosConsultarOpResultado, "plazo");
		
		if(inversionesCedePlazoObjeto.isJsonNull() || inversionesCedePlazoObjeto.entrySet().size() == 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.74");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		Integer plaDias = Utilerias.obtenerIntPropiedad(inversionesCedePlazoObjeto, "Pla_Dias");
		String fotNumero = InversionesCedeFormulasEnum.validarProducto(producto);
		
		JsonObject datosTasaInversionesCedeConsultar = new JsonObject();
		datosTasaInversionesCedeConsultar.addProperty("Tas_Plazo", plaNumero);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Cantid", monto);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Formul", fotNumero);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Fecha", fecIni);
		datosTasaInversionesCedeConsultar.addProperty("Cli_Numero", usuClient);
		datosTasaInversionesCedeConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject tasaInversionesCedeConsultarOpResultado = this.tasaServicio.tasaInversionesCedeConsultar(datosTasaInversionesCedeConsultar);
		logger.debug("tasaInversionesCedeConsultarOpResultado" + tasaInversionesCedeConsultarOpResultado);
		
		Utilerias.verificarError(tasaInversionesCedeConsultarOpResultado);
		
		JsonObject tasaInversionesCedeConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaInversionesCedeConsultarOpResultado, "tasaCede");
		Double tasTasa = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_Tasa");
		Double tasPorBas = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_PorBas");
		Double tasPuntos = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_Puntos");
		Double tasVarRea = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_VarRea");
		
		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("FechaSis", fechaSis);
		
		JsonObject tasaMonedaConsultarOpResultado = this.tasaServicio.tasaMonedaConsultar(datosMoneda);
		logger.debug("tasaMonedaConsultarOpResultado" + tasaMonedaConsultarOpResultado);
		
		Utilerias.verificarError(tasaMonedaConsultarOpResultado);
		
		JsonObject tasaMonedaConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaMonedaConsultarOpResultado, "tasaMoneda");
		Double monFixCom = Utilerias.obtenerDoublePropiedad(tasaMonedaConsultarObjeto, "Mon_FixCom");
		
		/**
		 * REGLA DE NEGOCIO: validar que el monto de la inversion en UDIS sea menor a 40,000,000.00 para calcular GAT y GATReal
		 */
		
		Double invGAT = null;
		Double invGATRea = null;

		if((monto / monFixCom) < Double.parseDouble(InversionesCedeMonTotUDI)) {
			JsonObject datosGATCede = new JsonObject();
			datosGATCede.addProperty("Inv_Dias", plaDias);
			datosGATCede.addProperty("Inv_TasInt", tasTasa);
			datosGATCede.addProperty("Cue_MonInv", monto);
			datosGATCede.addProperty("FechaSis", fechaSis);
			
			JsonObject tasaGATCedeConsultaCalcularOpResultado = this.tasaServicio.tasaGATCedeConsultaCalcular(datosGATCede);
			logger.debug("tasaGATCedeConsultaCalcularOpResultado" + tasaGATCedeConsultaCalcularOpResultado);
			
			Utilerias.verificarError(tasaGATCedeConsultaCalcularOpResultado);
			
			JsonObject tasaGATCedeConsultaCalcularObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaGATCedeConsultaCalcularOpResultado, "tasaGATCede");
			invGAT = Utilerias.obtenerDoublePropiedad(tasaGATCedeConsultaCalcularObjeto, "Inv_GAT");
			
			JsonObject datosGATReal = new JsonObject();
			datosGATReal.addProperty("Inv_GAT", invGAT);
			datosGATReal.addProperty("FechaSis", fechaSis);
			
			JsonObject tasaGATRealConsultaCalcularOpResultado = this.tasaServicio.tasaGATRealConsultaCalcular(datosGATReal);
			logger.debug("tasaGATRealConsultaCalcularOpResultado" + tasaGATRealConsultaCalcularOpResultado);
			
			Utilerias.verificarError(tasaGATRealConsultaCalcularOpResultado);
			
			JsonObject tasaGATRealConsultaCalcularObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaGATRealConsultaCalcularOpResultado, "tasaGATReal");
			invGATRea = Utilerias.obtenerDoublePropiedad(tasaGATRealConsultaCalcularObjeto, "Inv_GATRea");
		}
		
		JsonObject calculaTasa = new JsonObject();
		calculaTasa.addProperty("Inv_Plazo", plaDias);
		calculaTasa.addProperty("Inv_Cantid", monto);
		calculaTasa.addProperty("TasInv", tasTasa);
		calculaTasa.addProperty("Par_DiBaIn", parDiBaIn);
		calculaTasa.addProperty("Par_ISR", cliTasISR);
		calculaTasa.addProperty("Cli_CobISR", cliCobISR);

		JsonObject resultadoCalculaTasa = Utilerias.calculaTasa(calculaTasa);
		logger.debug("resultadoCalculaTasa" + resultadoCalculaTasa);
		
		Double invCanBru = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanBru");
		Double invISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_ISR");
		Double invCanISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanISR");
		Double invTasa = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Tasa");
		Double invCanNet = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanNet");
		Double invCanTot = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanTot");
		
		StringBuilder cpTasReferencia = new StringBuilder()
				.append("Cetes 28 D\u00edas ")
				.append(Utilerias.redondear(tasVarRea, 2));
		
		JsonObject resultado = new JsonObject();
		JsonObject calculadora = new JsonObject();
		
		calculadora.addProperty("invPts", Utilerias.redondear(tasPorBas/100, 2));
		calculadora.addProperty("invPuntos", Utilerias.redondear(tasPuntos, 2));
		calculadora.addProperty("invTBruta", Utilerias.redondear(tasTasa, 2));
		calculadora.addProperty("invCanBru", Utilerias.redondear(invCanBru, 2));
		calculadora.addProperty("cpInvISR", invISR);
		calculadora.addProperty("invCanISR", Utilerias.redondear(invCanISR, 2));
		calculadora.addProperty("cpInvTasa", invTasa);
		calculadora.addProperty("invCanNet", Utilerias.redondear(invCanNet, 2));
		calculadora.addProperty("invCanTot", Utilerias.redondear(invCanTot, 2));
		calculadora.addProperty("invGat", invGAT != null ? invGAT.toString() : "N.A.");
		calculadora.addProperty("invGatRea", invGATRea != null ? invGATRea.toString() : "N.A.");
		calculadora.addProperty("cpTasReferencia", cpTasReferencia.toString());
		
		resultado.add("calculadora", calculadora);
		
		logger.info("CTRL: Terminando obtenerCalculosInversionCede metodo");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
					.build();
	}
	
	@Path("pagare")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response nuevaInversionPagare(JsonObject inversionesObjeto,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando nueva inversion pagare metodo");
		
		String cpRSAToken = null;
		String invCuenta = null;
		String invFecVen = null;
		String bearerToken = null;
		String usuNumero = null;
		String usuEmail = null;		
		String usuFolTok = null;
		String bitDireIP = null;
		String fechaSis = null;
		String usuNombre = null;
		String numTransac = null;
		String invNumero = null;
		String adiInsliq = null;
		String strInsLiq = null;
		String plazo = null;
		String validarToken = null;
		String errCodigo = null;
		String errMensaj = null;
		String strVerifi = null;
		String digitoVerificador = null;
		String strVerifi1 = null;
		String strVerifi2 = null;
		String strVerifi3 = null;
		String strVerifi4 = null;
		String strVerifi5 = null;
		String invFecIni =  null;
		String fecIni = null;
		String fecVen = null;		
		String formato = "yyyy-MM-dd";
		Double invTasa = null;
		Double invTBruta = null;
		Double invISR = null;
		Double invDeposi = null;
		Double invCanNet = null;
		Double corMonDia = null;
		Double corMoLiDi = null;	
		Double montoAcumulado = null;
		Double objInvGAT = null;
		Double objInvGATRea = null;
		Double InvCantid = null;
		Double ImpIntere = null;
		Double InvCanISR = null;
		Double InvTasa = null;
		Double InvCantTot = null;
		Integer Plazo = null;
		
		JsonArray inversionesArreglos = null;
		
		JsonObject resultado = null;
		JsonObject requestBodyPagare = null;
		JsonObject principalResultadoObjecto = null;
		JsonObject folioTransaccionGenerarOpResultadoObjeto = null;
		JsonObject transaccion = null;
		JsonObject datosCuentaOrigenConsultar = null;
		JsonObject consultaCuentaOrigenOpResultado = null;
		JsonObject cuenta = null;
		JsonObject datosInversionesAlta = null;
		JsonObject inversionesPagareAltaOpResultado = null;
		JsonObject alta = null;
		JsonObject cobroPagareAltaOpResultado = null;
		JsonObject datosInversionesPagareInformacionGuardar = null;
		JsonObject guardaInformacionAltaPagareOpResultado =  null;
		JsonObject datosIversionVsEstadoCuenta = null;
		JsonObject inversionesContraEstadoCuentaActualizarOpResultadoObjeto = null;
		JsonObject datosBitacora = null;
		JsonObject bitacoraCreacionOpResultadoObjeto = null;
		JsonObject inversionesPagareNumeroUsuarioObtener = null;
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = null;
		JsonObject inversiones = null;
		JsonObject datosInversionesAltaPagar = null;
		
		requestBodyPagare = inversionesObjeto.getAsJsonObject("nuevaInversion");
		Utilerias.verificarError(requestBodyPagare);		
		
		bearerToken = solicitud.getHeader("Authorization");
		principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		cpRSAToken = Utilerias.obtenerStringPropiedad(requestBodyPagare, "cpRSAToken");
		invCuenta = Utilerias.obtenerStringPropiedad(requestBodyPagare, "invCuenta");
		invFecVen = Utilerias.obtenerStringPropiedad(requestBodyPagare, "invFecVen");
		adiInsliq = Utilerias.obtenerStringPropiedad(requestBodyPagare, "adiInsliq");
		plazo = Utilerias.obtenerStringPropiedad(requestBodyPagare, "plazo");
		invTasa = Utilerias.obtenerDoublePropiedad(requestBodyPagare, "invTasa");
		invTBruta = Utilerias.obtenerDoublePropiedad(requestBodyPagare, "invTBruta");
		invISR = Utilerias.obtenerDoublePropiedad(requestBodyPagare, "invISR");
		invDeposi = Utilerias.obtenerDoublePropiedad(requestBodyPagare, "invDeposi");
		invCanNet = Utilerias.obtenerDoublePropiedad(requestBodyPagare, "invCanNet");
		
		usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		usuEmail = principalResultadoObjecto.get("usuEmail").getAsString();
		usuFolTok = principalResultadoObjecto.get("usuFolTok").getAsString();
		usuNombre = principalResultadoObjecto.get("usuNombre").getAsString();
		
		bitDireIP = solicitud.getHeader("X-Forwarded-For");
		fechaSis = Utilerias.obtenerFechaSis();
		
		if(!Utilerias.validaNumero(invTasa.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "invTasa");
			bimMessageDTO.addMergeVariable("valor", invTasa.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		if(!Utilerias.validaNumero(invTBruta.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "invTBruta");
			bimMessageDTO.addMergeVariable("valor", invTBruta.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		if(!Utilerias.validaNumero(invISR.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "invISR");
			bimMessageDTO.addMergeVariable("valor", invISR.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		if(!Utilerias.validaNumero(invDeposi.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "invDeposi");
			bimMessageDTO.addMergeVariable("valor", invDeposi.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		if(!Utilerias.validaNumero(invCanNet.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "invCanNet");
			bimMessageDTO.addMergeVariable("valor", invCanNet.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(cpRSAToken == null || cpRSAToken.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "cpRSAToken");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		if(plazo == null || plazo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "plazo");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		if(invCuenta == null || invCuenta.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "invCuenta");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		if(invFecVen == null || invFecVen.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "invFecVen");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		if(adiInsliq == null || adiInsliq.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "adiInsliq");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		if(logger.isDebugEnabled()) {
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);
		}
		Utilerias.verificarError(folioTransaccionGenerarOpResultadoObjeto);

		transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");
		
		StringBuilder scriptName = new StringBuilder()
				.append(InversionesCtrl.class.getName())
				.append(".nuevaInversionPagare");

		validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName.toString());
				
		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		datosCuentaOrigenConsultar = new JsonObject();
		datosCuentaOrigenConsultar.addProperty("Cor_Usuari", usuNumero);
		datosCuentaOrigenConsultar.addProperty("Cor_Cuenta", invCuenta);
		datosCuentaOrigenConsultar.addProperty("Tip_Consul", ConsultaInversionPagareCuentaTipoC1);
		datosCuentaOrigenConsultar.addProperty("FechaSis", fechaSis);
		
		consultaCuentaOrigenOpResultado = this.cuentaServicio.cuentaOrigenConsultar(datosCuentaOrigenConsultar);
		if(logger.isDebugEnabled()) {
			logger.info("consultaCuentaOrigenOpResultado" + consultaCuentaOrigenOpResultado);
			}
		
		cuenta = Utilerias.obtenerJsonObjectPropiedad(consultaCuentaOrigenOpResultado, "cuenta");
		corMonDia = Utilerias.obtenerDoublePropiedad(cuenta, "Cor_MonDia");
		corMoLiDi = Utilerias.obtenerDoublePropiedad(cuenta, "Cor_MoLiDi");	
		montoAcumulado = corMonDia + invDeposi;		
		
		/**
		 * REGLA DE NEGOCIO: Validar el monto maximo Diario permitido para inversiones, depende de cada usuario.
		 */
		if(corMoLiDi < montoAcumulado) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.77");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		
		datosInversionesAlta = new JsonObject();
		datosInversionesAlta.addProperty("Inv_FecIni", fechaSis);
		datosInversionesAlta.addProperty("Inv_FecVen", invFecVen);
		datosInversionesAlta.addProperty("Inv_Cantid", invDeposi);
		datosInversionesAlta.addProperty("Inv_Tasa", invTasa);
		datosInversionesAlta.addProperty("Inv_ISR", invISR);
		datosInversionesAlta.addProperty("Inv_Cuenta", invCuenta);
		datosInversionesAlta.addProperty("Inv_TBruta", invTBruta);
		datosInversionesAlta.addProperty("NumTransac", numTransac);
		datosInversionesAlta.addProperty("FechaSis", fechaSis);
		datosInversionesAlta.addProperty("Inv_OpcTas", "");
		
		inversionesPagareAltaOpResultado = this.inversionesServicio.inversionesAlta(datosInversionesAlta);
		if(logger.isDebugEnabled()) {
		logger.debug("inversionesPagareAltaOpResultado" + inversionesPagareAltaOpResultado);
		}
		Utilerias.verificarError(inversionesPagareAltaOpResultado);
		
		alta = Utilerias.obtenerJsonObjectPropiedad(inversionesPagareAltaOpResultado, "alta");
		invNumero = Utilerias.obtenerStringPropiedad(alta, "Inv_Numero");
		errCodigo = Utilerias.obtenerStringPropiedad(alta, "Err_Codigo");
		errMensaj = Utilerias.obtenerStringPropiedad(alta, "Err_Mensaj");

		if(errCodigo == null || !"000000".equals(errCodigo)){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");			
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		datosInversionesAltaPagar = new JsonObject();
		datosInversionesAltaPagar.addProperty("Inv_Numero", invNumero);
		datosInversionesAltaPagar.addProperty("Inv_FecIni", fechaSis);
		datosInversionesAltaPagar.addProperty("Inv_Cantid", invDeposi);
		datosInversionesAltaPagar.addProperty("Inv_Cuenta", invCuenta);
		datosInversionesAltaPagar.addProperty("Fecha", fechaSis);
		datosInversionesAltaPagar.addProperty("NumTransac", numTransac);
		datosInversionesAltaPagar.addProperty("FechaSis", fechaSis);
		
		cobroPagareAltaOpResultado = this.inversionesServicio.inversionesAltaPagar(datosInversionesAltaPagar);
		if(logger.isDebugEnabled()) {
			logger.debug("cobroPagareAltaOpResultado" + cobroPagareAltaOpResultado);
			}
		Utilerias.verificarError(cobroPagareAltaOpResultado);

		JsonObject cargo = Utilerias.obtenerJsonObjectPropiedad(cobroPagareAltaOpResultado, "cargo");
		errCodigo = Utilerias.obtenerStringPropiedad(cargo, "Err_Codigo");
		errMensaj = Utilerias.obtenerStringPropiedad(cargo, "Err_Mensaj");

		if(errCodigo == null || !"000000".equals(errCodigo)){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");			
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		datosInversionesPagareInformacionGuardar = new JsonObject();
		datosInversionesPagareInformacionGuardar.addProperty("Adi_Invers", invNumero);
		datosInversionesPagareInformacionGuardar.addProperty("Adi_InsLiq", adiInsliq);
		datosInversionesPagareInformacionGuardar.addProperty("Adi_MoReGr", 0);
		datosInversionesPagareInformacionGuardar.addProperty("NumTransac", numTransac);
		datosInversionesPagareInformacionGuardar.addProperty("FechaSis", fechaSis);
		
		guardaInformacionAltaPagareOpResultado = this.inversionesServicio.inversionesPagareInformacionGuardar(datosInversionesPagareInformacionGuardar);
		if(logger.isDebugEnabled()) {
			logger.debug("guardaInformacionAltaPagareOpResultado" + guardaInformacionAltaPagareOpResultado);
			}
		Utilerias.verificarError(guardaInformacionAltaPagareOpResultado);
		
		JsonObject informacionGuardar = Utilerias.obtenerJsonObjectPropiedad(guardaInformacionAltaPagareOpResultado, "informacionGuardar");
		errCodigo = Utilerias.obtenerStringPropiedad(informacionGuardar, "Err_Codigo");
		errMensaj = Utilerias.obtenerStringPropiedad(informacionGuardar, "Err_Mensaj");

		if(errCodigo == null || !"000000".equals(errCodigo)){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");			
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}		

		datosIversionVsEstadoCuenta = new JsonObject();
		datosIversionVsEstadoCuenta.addProperty("Cor_Usuari", usuNumero);
		datosIversionVsEstadoCuenta.addProperty("Cor_Cuenta", invCuenta);
		datosIversionVsEstadoCuenta.addProperty("Cor_MonDia", invDeposi);
		datosIversionVsEstadoCuenta.addProperty("NumTransac", numTransac);
		datosIversionVsEstadoCuenta.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
		logger.info("datosIversionVsEstadoCuenta" + datosIversionVsEstadoCuenta);
		}
		inversionesContraEstadoCuentaActualizarOpResultadoObjeto = this.inversionesServicio.inversionesContraEstadoCuentaActualizar(datosIversionVsEstadoCuenta);
		if(logger.isDebugEnabled()) {
		logger.info("inversionesContraEstadoCuentaActualizarOpResultadoObjeto" + inversionesContraEstadoCuentaActualizarOpResultadoObjeto);
		}
		Utilerias.verificarError(inversionesContraEstadoCuentaActualizarOpResultadoObjeto);
		
		if("01".equals(adiInsliq)){
			strInsLiq = "Renovar Capital con interes";
		}else if("02".equals(adiInsliq)) {
			strInsLiq = "Renova Capital";
		}else {
			strInsLiq = "No renovar";
		}

		StringBuilder bitPriRef = new StringBuilder()
				.append(invCuenta)
				.append("-")
				.append(invDeposi);
		
		StringBuilder bitSegRef = new StringBuilder()
				.append(plazo)
				.append("-")
				.append(strInsLiq);		
		
		datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef.toString());
		datosBitacora.addProperty("Bit_DireIP", bitDireIP != null ? bitDireIP : "");
		datosBitacora.addProperty("Bit_SegRef", bitSegRef.toString());
		datosBitacora.addProperty("Bit_TipOpe", InversionBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
		logger.info("datosBitacora" + datosBitacora);
		}
		bitacoraCreacionOpResultadoObjeto = this.bitacoraServicio.creacionBitacora(datosBitacora);
		if(logger.isDebugEnabled()) {
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		}
		Utilerias.verificarError(bitacoraCreacionOpResultadoObjeto);
		
		inversionesPagareNumeroUsuarioObtener = new JsonObject();
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
		inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
		inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
		if(logger.isDebugEnabled()) {
		logger.info("inversionesPagareNumeroUsuarioObtener" + inversionesPagareNumeroUsuarioObtener);
		}
		inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(inversionesPagareNumeroUsuarioObtener);
		if(logger.isDebugEnabled()) {
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);
		}
		Utilerias.verificarError(inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);

		inversiones = Utilerias.obtenerJsonObjectPropiedad(inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto, "inversiones");
		inversionesArreglos = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");
		
		strVerifi = "Origen:" + invCuenta + " Destino:" + invNumero + " Cantidad:" + InvCantid + " Folio:" + numTransac;
		digitoVerificador = Utilerias.generarDigitoVerificador(strVerifi);
		strVerifi1 = digitoVerificador.substring(0, 70);
		strVerifi2 = digitoVerificador.substring(70, 140);
		strVerifi3 = digitoVerificador.substring(140, 210);
		strVerifi4 = digitoVerificador.substring(210, 280);
		strVerifi5 = digitoVerificador.substring(280);

		resultado = null;
		for (JsonElement invElemento : inversionesArreglos) {
			JsonObject inversionObj = invElemento.getAsJsonObject();

			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero)) {
				objInvGAT = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_GAT");
				InvCantid = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_Cantid");
				objInvGATRea = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_GATRea");
				invFecIni = Utilerias.obtenerStringPropiedad(inversionObj, "Inv_FecIni");
				InvCanISR = Utilerias.obtenerDoublePropiedad(inversionObj, "Imp_ISR");
				fecIni = Utilerias.formatearFecha(invFecIni, formato);
				fecVen = Utilerias.formatearFecha(invFecVen, formato);
				Plazo = Utilerias.obtenerIntPropiedad(inversionObj, "Inv_Plazo");
				ImpIntere = Utilerias.obtenerDoublePropiedad(inversionObj, "Imp_Intere");
				InvTasa = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_Tasa");
				InvCantTot = Utilerias.obtenerDoublePropiedad(inversionObj, "Inv_Total");
				
				resultado = new JsonObject();
				JsonObject nuevaInversion = new JsonObject();
				nuevaInversion.addProperty("invCuenta", invCuenta);
				nuevaInversion.addProperty("invNueva", invNumero);
				nuevaInversion.addProperty("invCantidad", InvCantid);
				nuevaInversion.addProperty("invDeposi", invDeposi);
				nuevaInversion.addProperty("invPlazo", Plazo);
				nuevaInversion.addProperty("invTBruta",  invTBruta);
				nuevaInversion.addProperty("invCanBru", ImpIntere);
				nuevaInversion.addProperty("invGat", objInvGAT);
				nuevaInversion.addProperty("invGatRea", objInvGATRea);
				nuevaInversion.addProperty("invFecIni", fecIni != null ? fecIni : "");
				nuevaInversion.addProperty("invISR", invISR);
				nuevaInversion.addProperty("invCanISR", InvCanISR);
				nuevaInversion.addProperty("invFecVen", fecVen != null ? fecVen : "");
				nuevaInversion.addProperty("invTasa", InvTasa);
				nuevaInversion.addProperty("invCanNet", invCanNet);
				nuevaInversion.addProperty("adiInsLiq", strInsLiq);
				nuevaInversion.addProperty("invCanTot", InvCantTot);
				nuevaInversion.addProperty("usuNombre", usuNombre);
				nuevaInversion.addProperty("digitoVerificador", digitoVerificador);
				resultado.add("inversionCreada", nuevaInversion);
			}
		}
		
		/**
		 * REGLA DE NEGOCIO: Envío de correo con plantilla establecida por BIM y encriptado de digito verificador
		 */

		String asunto = Utilerias.obtenerPropiedadPlantilla("mail.inversion.asunto");
		String plantilla = Utilerias.obtenerPlantilla("inversion");
		String invNuevamen = invNumero.substring(invNumero.length()-7);		
		String rInvCuentamen = invCuenta.substring(invCuenta.length()-4);

		StringBuilder invNuevaOcu = new StringBuilder()
				.append("**************")
				.append(invNuevamen);		
		
		StringBuilder invCuentaOcu = new StringBuilder()
				.append("**************")
				.append(rInvCuentamen);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fechaSisRedu = null;
		try {
			fechaSisRedu = sdf.parse(fechaSis);
		} catch (Exception e) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		DecimalFormat formatter = new DecimalFormat("#,###.00");

		BimEmailTemplateDTO emailTemplateDTO = new BimEmailTemplateDTO(plantilla);
		emailTemplateDTO.addMergeVariable("Inv_Nueva", invNuevaOcu.toString());
		emailTemplateDTO.addMergeVariable("Inv_Cuenta", invCuentaOcu.toString());
		emailTemplateDTO.addMergeVariable("Inv_Cantid", String.valueOf(formatter.format(InvCantid)));
		emailTemplateDTO.addMergeVariable("Inv_Plazo", String.valueOf(Plazo));
		emailTemplateDTO.addMergeVariable("Inv_InfGAT", String.valueOf(formatter.format(objInvGAT)));
		emailTemplateDTO.addMergeVariable("Inv_InfGATRea", String.valueOf(formatter.format(objInvGATRea)));
		emailTemplateDTO.addMergeVariable("Inv_FecIni", fecIni != null ? fecIni : "");
		emailTemplateDTO.addMergeVariable("Inv_FecVen", fecVen != null ? fecVen : "");
		emailTemplateDTO.addMergeVariable("Str_InsLiq", strInsLiq);
		emailTemplateDTO.addMergeVariable("Inv_TBruta", invTBruta.toString());
		emailTemplateDTO.addMergeVariable("Inv_CanBru", String.valueOf(formatter.format(ImpIntere)));
		emailTemplateDTO.addMergeVariable("Inv_ISR", invISR.toString());
		emailTemplateDTO.addMergeVariable("Inv_CanISR", String.valueOf(formatter.format(InvCanISR)));
		emailTemplateDTO.addMergeVariable("Inv_Tasa", String.valueOf(formatter.format(InvTasa)));
		emailTemplateDTO.addMergeVariable("Inv_CanNet", String.valueOf(formatter.format(invCanNet)));
		emailTemplateDTO.addMergeVariable("Inv_CanTot", String.valueOf(formatter.format(InvCantTot)));
		emailTemplateDTO.addMergeVariable("NumTransac", numTransac);
		emailTemplateDTO.addMergeVariable("Usu_Nombre", usuNombre);
		emailTemplateDTO.addMergeVariable("fechaSis", fechaSisRedu != null ? sdf.format(fechaSisRedu) : "");
		emailTemplateDTO.addMergeVariable("Str_Verifi1", strVerifi1);
		emailTemplateDTO.addMergeVariable("Str_Verifi2", strVerifi2);
		emailTemplateDTO.addMergeVariable("Str_Verifi3", strVerifi3);
		emailTemplateDTO.addMergeVariable("Str_Verifi4", strVerifi4);
		emailTemplateDTO.addMergeVariable("Str_Verifi5", strVerifi5);
		String cuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateDTO);

		correoServicio.enviarCorreo(usuEmail, asunto, cuerpo);

		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("cede/tabla-amortizacion")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerTablaAmortizacionInversionCede(@QueryParam("monto") Double monto, 
			@QueryParam("pla_numero") String plaNumero, @QueryParam("producto") String producto,
			@QueryParam("diap_id") Integer diaPId, @QueryParam("dia_amo") Integer diaAmo,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerTablaAmortizacionInversionCede metodo");
		
		String plaProduc = InversionesCedeTiposEnum.validarProducto(producto);
		
		if (plaProduc == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.71");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		Double cantidadInversion = InversionesCedeCantidadInversionEnum.validarCantidadInversion(producto);
		
		if(monto == null || monto < cantidadInversion) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.75");
			bimMessageDTO.addMergeVariable("invCantid", cantidadInversion.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(plaNumero == null || plaNumero.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "pla_numero");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(diaPId == null || !Utilerias.validaNumero(diaPId.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "diap_id");
			bimMessageDTO.addMergeVariable("valor", diaPId.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(diaPId == InversionesCedeDiaPId && (diaAmo == null || diaAmo.toString().isEmpty())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "dia_amo");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(diaAmo != null && !Utilerias.validaNumero(diaAmo.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "dia_amo");
			bimMessageDTO.addMergeVariable("valor", diaAmo.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		Utilerias.verificarError(principalResultadoObjeto);
		
		String usuClient = principalResultadoObjeto.get("usuClient").getAsString();
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosInversionesCedePlazos = new JsonObject();
		datosInversionesCedePlazos.addProperty("Pla_Numero", plaNumero);
		datosInversionesCedePlazos.addProperty("Pla_Produc", plaProduc);
		datosInversionesCedePlazos.addProperty("Tip_Consul", InversionesCedePlazosConsultarOpTipConsulC4);
		datosInversionesCedePlazos.addProperty("FechaSis", fechaSis);
		
		JsonObject inversionesCedePlazosConsultarOpResultado = this.inversionesServicio.inversionesCedePlazosConsultar(datosInversionesCedePlazos);
		logger.debug("inversionesCedePlazosConsultarOpResultado" + inversionesCedePlazosConsultarOpResultado);
		
		Utilerias.verificarError(inversionesCedePlazosConsultarOpResultado);
		
		JsonObject inversionesCedePlazoObjeto = Utilerias.obtenerJsonObjectPropiedad(inversionesCedePlazosConsultarOpResultado, "plazo");
		
		if(inversionesCedePlazoObjeto.isJsonNull() || inversionesCedePlazoObjeto.entrySet().size() == 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.74");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		
		Integer plaDias = Utilerias.obtenerIntPropiedad(inversionesCedePlazoObjeto, "Pla_Dias");
		String fotNumero = InversionesCedeFormulasEnum.validarProducto(producto);
		Date fechaIni = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(fechaSis), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
		String cpFechaIni = sdf.format(fechaIni);
		Date fechaFin = Utilerias.agregarDiasAFecha(fechaIni, plaDias);
		
		JsonObject datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", sdf.format(fechaFin));
		datosFechaHabil.addProperty("FechaSis", fechaSis);
		
		JsonObject fechaHabilConsultarOpResultado = this.reinversionServicio.fechaHabilConsultar(datosFechaHabil);
		logger.debug("fechaHabilConsultarOpResultado" + fechaHabilConsultarOpResultado);
		
		Utilerias.verificarError(fechaHabilConsultarOpResultado);
		
		JsonObject fechaHabilConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(fechaHabilConsultarOpResultado, "fechaHabil");
		
		String fecha = Utilerias.obtenerStringPropiedad(fechaHabilConsultarObjeto, "Fecha");
		fechaFin = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(fecha), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));	
		String cpFechaFin = sdf.format(fechaFin);
		
		JsonObject datosTasaInversionesCedeConsultar = new JsonObject();
		datosTasaInversionesCedeConsultar.addProperty("Tas_Plazo", plaNumero);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Cantid", monto);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Formul", fotNumero);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Fecha", cpFechaIni);
		datosTasaInversionesCedeConsultar.addProperty("Cli_Numero", usuClient);
		datosTasaInversionesCedeConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject tasaInversionesCedeConsultarOpResultado = this.tasaServicio.tasaInversionesCedeConsultar(datosTasaInversionesCedeConsultar);
		logger.debug("tasaInversionesCedeConsultarOpResultado" + tasaInversionesCedeConsultarOpResultado);
		
		Utilerias.verificarError(tasaInversionesCedeConsultarOpResultado);
		
		JsonObject tasaInversionesCedeConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaInversionesCedeConsultarOpResultado, "tasaCede");
		Double tasTasa = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_Tasa");
		
		JsonObject datosClienteConsultar = new JsonObject();
		datosClienteConsultar.addProperty("Cli_Numero", usuClient);
		datosClienteConsultar.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosClienteConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject clienteConsultarOpResultado = this.clienteServicio.clienteConsultar(datosClienteConsultar);
		logger.debug("clienteConsultarOpResultado" + clienteConsultarOpResultado);
		
		Utilerias.verificarError(clienteConsultarOpResultado);
		
		JsonObject clienteConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(clienteConsultarOpResultado, "cliente");
		String cliSucurs = Utilerias.obtenerStringPropiedad(clienteConsultarObjeto, "Cli_Sucurs");
		Double cliTasISR = Utilerias.obtenerDoublePropiedad(clienteConsultarObjeto, "Cli_TasISR");
		String cliCobISR = Utilerias.obtenerStringPropiedad(clienteConsultarObjeto, "Cli_CobISR");
		
		JsonObject datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", cliSucurs);
		datosSucursal.addProperty("FechaSis", fechaSis);
		
		JsonObject informacionSucursalObtenerOpResultado = this.configuracionServicio.informacionSucursalObtener(datosSucursal);
		logger.debug("informacionSucursalObtenerOpResultado" + informacionSucursalObtenerOpResultado);
		
		Utilerias.verificarError(informacionSucursalObtenerOpResultado);
		
		JsonObject informacionSucursalObtenerObjeto = Utilerias.obtenerJsonObjectPropiedad(informacionSucursalObtenerOpResultado, "informacionSucursal");
		Integer parDiBaIn = Utilerias.obtenerIntPropiedad(informacionSucursalObtenerObjeto, "Par_DiBaIn");
		
		JsonObject calculaTasa = new JsonObject();
		calculaTasa.addProperty("Inv_Plazo", plaDias);
		calculaTasa.addProperty("Inv_Cantid", monto);
		calculaTasa.addProperty("TasInv", tasTasa);
		calculaTasa.addProperty("Par_DiBaIn", parDiBaIn);
		calculaTasa.addProperty("Par_ISR", cliTasISR);
		calculaTasa.addProperty("Cli_CobISR", cliCobISR);

		JsonObject resultadoCalculaTasa = Utilerias.calculaTasa(calculaTasa);
		logger.debug("resultadoCalculaTasa" + resultadoCalculaTasa);
		
		Double invTasa = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Tasa");
		
		Date amortiDiaPago = Utilerias.calcularAmortizacionDiaDePago(diaPId, fechaIni, diaAmo);
		
		JsonObject datosAmortizacionGenerar = new JsonObject();
		datosAmortizacionGenerar.addProperty("Ced_Cantid", monto);
		datosAmortizacionGenerar.addProperty("Ced_Plazo", plaDias);
		datosAmortizacionGenerar.addProperty("Ced_FecIni", cpFechaIni);
		datosAmortizacionGenerar.addProperty("Ced_FecVen", cpFechaFin);
		datosAmortizacionGenerar.addProperty("Ced_DiaPag", diaPId);
		datosAmortizacionGenerar.addProperty("Ced_DiPaFe", sdf.format(amortiDiaPago));
		datosAmortizacionGenerar.addProperty("Ced_TasBru", tasTasa);
		datosAmortizacionGenerar.addProperty("Ced_TasNet", invTasa);
		datosAmortizacionGenerar.addProperty("Ced_Produc", plaProduc);
		datosAmortizacionGenerar.addProperty("FechaSis", fechaSis);
		
		JsonObject amortizacionGenerarOpResultado = this.amortizacionServicio.amortizacionGenerar(datosAmortizacionGenerar);
		logger.debug("amortizacionGenerarOpResultado" + amortizacionGenerarOpResultado);
		
		Utilerias.verificarError(amortizacionGenerarOpResultado);
		
		JsonObject amortizacionGenerarObjeto = Utilerias.obtenerJsonObjectPropiedad(amortizacionGenerarOpResultado, "amortizaciones");
		JsonArray amortizacionGenerarArreglo = Utilerias.obtenerJsonArrayPropiedad(amortizacionGenerarObjeto, "amortizacion");
		
		JsonArray amortizaciones = new JsonArray();
		
		if(amortizacionGenerarArreglo != null) {
			for(JsonElement amoElemento : amortizacionGenerarArreglo) {
				JsonObject amoObjeto = amoElemento.getAsJsonObject();
				if(!amoObjeto.isJsonNull()) {
					String amoFecIni = Utilerias.obtenerStringPropiedad(amoObjeto, "Amo_FecIni");
					Date cpAmoFecIni = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(amoFecIni), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
					String amoFecVen = Utilerias.obtenerStringPropiedad(amoObjeto, "Amo_FecVen");
					Date cpAmoFecVen = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(amoFecVen), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
					
					JsonObject amortizacion = new JsonObject();
					amortizacion.addProperty("amoFecIni", sdf.format(cpAmoFecIni));
					amortizacion.addProperty("amoFecVen", sdf.format(cpAmoFecVen));
					amortizacion.addProperty("amoCantid", Utilerias.obtenerDoublePropiedad(amoObjeto, "Amo_Cantid"));
					amortizacion.addProperty("amoIntNet", Utilerias.obtenerDoublePropiedad(amoObjeto, "Amo_IntNet"));
					amortizacion.addProperty("amoISR", Utilerias.obtenerDoublePropiedad(amoObjeto, "Amo_ISR"));
					amortizaciones.add(amortizacion);
				}
			}
		}
		
		JsonObject resultado = new JsonObject();
		resultado.add("amortizaciones", amortizaciones);
		
		logger.info("CTRL: Terminando obtenerTablaAmortizacionInversionCede metodo");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
					.build();
	}
	
	@Path("cede")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	public Response inversionCedeAlta(JsonObject datosInversionCede,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando inversionCedeAlta metodo");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNumero");
		String usuFolTok = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuFolTok");
		String usuClient = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuClient");
		String usuNombre = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNombre");
		String usuEmail = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuEmail");
		
		JsonObject inversionCede = Utilerias.obtenerJsonObjectPropiedad(datosInversionCede, "inversion");
		
		String cpRSAToken = Utilerias.obtenerStringPropiedad(inversionCede, "cpRSAToken");
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.debug("- folioTrasaccionResultado " +  folioTransaccionResultado);
		
		JsonObject folioTransaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(folioTransaccion, "Fol_Transa");
		
		String bitDireIP = solicitud.getHeader("X-Forwarded-For") != null ? solicitud.getHeader("X-forwarded-For") : "";

		String scriptName = new StringBuilder()
				.append(InversionesCtrl.class.getName())
				.append(".inversionCedeCreacion")
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
		
		String plaNumero = Utilerias.obtenerStringPropiedad(inversionCede, "plaNumero");
		String producto = Utilerias.obtenerStringPropiedad(inversionCede, "producto");
		Double monto = Utilerias.obtenerDoublePropiedad(inversionCede, "monto");
		String invCuenta = Utilerias.obtenerStringPropiedad(inversionCede, "invCuenta");
		String invFecIni = Utilerias.obtenerStringPropiedad(inversionCede, "invFecIni");
		String invFecVen = Utilerias.obtenerStringPropiedad(inversionCede, "invFecVen");
		Double invTBruta = Utilerias.obtenerDoublePropiedad(inversionCede, "invTBruta");
		Integer diaPId = Utilerias.obtenerIntPropiedad(inversionCede, "diaPId");
		Integer diaAmo = Utilerias.obtenerIntPropiedad(inversionCede, "diaAmo");
		
		if(plaNumero == null || plaNumero.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "plaNumero");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String plaProduc = InversionesCedeTiposEnum.validarProducto(producto);
		
		if (plaProduc == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.71");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		Double cantidadInversion = InversionesCedeCantidadInversionEnum.validarCantidadInversion(producto);
		
		if(monto == null || monto < cantidadInversion) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.75");
			bimMessageDTO.addMergeVariable("invCantid", cantidadInversion.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.validaNumero(monto.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "monto");
			bimMessageDTO.addMergeVariable("valor", monto.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(invCuenta == null || invCuenta.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "invCuenta");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(invFecIni == null || invFecIni.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "invFecIni");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(invFecVen == null || invFecVen.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "invFecVen");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(invTBruta == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "invTBruta");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.validaNumero(invTBruta.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "invTBruta");
			bimMessageDTO.addMergeVariable("valor", invTBruta.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(diaPId == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "diaPId");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.validaNumero(diaPId.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "diaPId");
			bimMessageDTO.addMergeVariable("valor", diaPId.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(diaPId == InversionesCedeDiaPId && (diaAmo == null || diaAmo.toString().isEmpty())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.72");
			bimMessageDTO.addMergeVariable("nombreParametro", "diaAmo");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(diaAmo != null && !Utilerias.validaNumero(diaAmo.toString())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
			bimMessageDTO.addMergeVariable("nombreParametro", "diaAmo");
			bimMessageDTO.addMergeVariable("valor", diaAmo.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fotNumero = InversionesCedeFormulasEnum.validarProducto(producto);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosTasaInversionesCedeConsultar = new JsonObject();
		datosTasaInversionesCedeConsultar.addProperty("Tas_Plazo", plaNumero);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Cantid", monto);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Formul", fotNumero);
		datosTasaInversionesCedeConsultar.addProperty("Tas_Fecha", invFecIni);
		datosTasaInversionesCedeConsultar.addProperty("Cli_Numero", usuClient);
		datosTasaInversionesCedeConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject tasaInversionesCedeConsultarOpResultado = this.tasaServicio.tasaInversionesCedeConsultar(datosTasaInversionesCedeConsultar);
		logger.debug("tasaInversionesCedeConsultarOpResultado" + tasaInversionesCedeConsultarOpResultado);
		
		JsonObject tasaInversionesCedeConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaInversionesCedeConsultarOpResultado, "tasaCede");
		Double tasTasa = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_Tasa");
		Double invPorBas = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_PorBas");
		Double invPuntos = Utilerias.obtenerDoublePropiedad(tasaInversionesCedeConsultarObjeto, "Tas_Puntos");
		String tasEsquema = Utilerias.obtenerStringPropiedad(tasaInversionesCedeConsultarObjeto, "Tas_Esquema");
		
		JsonObject datosInversionesCedePlazos = new JsonObject();
		datosInversionesCedePlazos.addProperty("Pla_Numero", plaNumero);
		datosInversionesCedePlazos.addProperty("Pla_Produc", plaProduc);
		datosInversionesCedePlazos.addProperty("Tip_Consul", InversionesCedePlazosConsultarOpTipConsulC4);
		datosInversionesCedePlazos.addProperty("FechaSis", fechaSis);
		
		JsonObject inversionesCedePlazosConsultarOpResultado = this.inversionesServicio.inversionesCedePlazosConsultar(datosInversionesCedePlazos);
		logger.debug("inversionesCedePlazosConsultarOpResultado" + inversionesCedePlazosConsultarOpResultado);
		
		JsonObject inversionesCedePlazoObjeto = Utilerias.obtenerJsonObjectPropiedad(inversionesCedePlazosConsultarOpResultado, "plazo");
		
		if(inversionesCedePlazoObjeto.isJsonNull() || inversionesCedePlazoObjeto.entrySet().size() == 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.74");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		Integer plaDias = Utilerias.obtenerIntPropiedad(inversionesCedePlazoObjeto, "Pla_Dias");
		String plaDescri = Utilerias.obtenerStringPropiedad(inversionesCedePlazoObjeto, "Pla_Descri");
		
		JsonObject datosClienteConsultar = new JsonObject();
		datosClienteConsultar.addProperty("Cli_Numero", usuClient);
		datosClienteConsultar.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosClienteConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject clienteConsultarOpResultado = this.clienteServicio.clienteConsultar(datosClienteConsultar);
		logger.debug("clienteConsultarOpResultado" + clienteConsultarOpResultado);
		
		JsonObject clienteConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(clienteConsultarOpResultado, "cliente");
		String cliSucurs = Utilerias.obtenerStringPropiedad(clienteConsultarObjeto, "Cli_Sucurs");
		Double cliTasISR = Utilerias.obtenerDoublePropiedad(clienteConsultarObjeto, "Cli_TasISR");
		String cliCobISR = Utilerias.obtenerStringPropiedad(clienteConsultarObjeto, "Cli_CobISR");
		
		JsonObject datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", cliSucurs);
		datosSucursal.addProperty("FechaSis", fechaSis);
		
		JsonObject informacionSucursalObtenerOpResultado = this.configuracionServicio.informacionSucursalObtener(datosSucursal);
		logger.debug("informacionSucursalObtenerOpResultado" + informacionSucursalObtenerOpResultado);
		
		JsonObject informacionSucursalObtenerObjeto = Utilerias.obtenerJsonObjectPropiedad(informacionSucursalObtenerOpResultado, "informacionSucursal");
		Integer parDiBaIn = Utilerias.obtenerIntPropiedad(informacionSucursalObtenerObjeto, "Par_DiBaIn");
		
		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("FechaSis", fechaSis);
		
		JsonObject tasaMonedaConsultarOpResultado = this.tasaServicio.tasaMonedaConsultar(datosMoneda);
		logger.debug("tasaMonedaConsultarOpResultado" + tasaMonedaConsultarOpResultado);
		
		JsonObject tasaMonedaConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaMonedaConsultarOpResultado, "tasaMoneda");
		Double monFixCom = Utilerias.obtenerDoublePropiedad(tasaMonedaConsultarObjeto, "Mon_FixCom");
		
		/**
		 * REGLA DE NEGOCIO: validar que el monto de la inversion en UDIS sea menor a 40,000,000.00 para calcular GAT y GATReal
		 */
		
		Double invGAT = null;
		Double invGATRea = null;

		if((monto / monFixCom) < Double.parseDouble(InversionesCedeMonTotUDI)) {
			JsonObject datosGATCede = new JsonObject();
			datosGATCede.addProperty("Inv_Dias", plaDias);
			datosGATCede.addProperty("Inv_TasInt", tasTasa);
			datosGATCede.addProperty("Cue_MonInv", monto);
			datosGATCede.addProperty("FechaSis", fechaSis);
			
			JsonObject tasaGATCedeConsultaCalcularOpResultado = this.tasaServicio.tasaGATCedeConsultaCalcular(datosGATCede);
			logger.debug("tasaGATCedeConsultaCalcularOpResultado" + tasaGATCedeConsultaCalcularOpResultado);
			
			JsonObject tasaGATCedeConsultaCalcularObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaGATCedeConsultaCalcularOpResultado, "tasaGATCede");
			invGAT = Utilerias.obtenerDoublePropiedad(tasaGATCedeConsultaCalcularObjeto, "Inv_GAT");
			
			JsonObject datosGATReal = new JsonObject();
			datosGATReal.addProperty("Inv_GAT", invGAT);
			datosGATReal.addProperty("FechaSis", fechaSis);
			
			JsonObject tasaGATRealConsultaCalcularOpResultado = this.tasaServicio.tasaGATRealConsultaCalcular(datosGATReal);
			logger.debug("tasaGATRealConsultaCalcularOpResultado" + tasaGATRealConsultaCalcularOpResultado);
			
			JsonObject tasaGATRealConsultaCalcularObjeto = Utilerias.obtenerJsonObjectPropiedad(tasaGATRealConsultaCalcularOpResultado, "tasaGATReal");
			invGATRea = Utilerias.obtenerDoublePropiedad(tasaGATRealConsultaCalcularObjeto, "Inv_GATRea");
		}
		
		JsonObject calculaTasa = new JsonObject();
		calculaTasa.addProperty("Inv_Plazo", plaDias);
		calculaTasa.addProperty("Inv_Cantid", monto);
		calculaTasa.addProperty("TasInv", tasTasa);
		calculaTasa.addProperty("Par_DiBaIn", parDiBaIn);
		calculaTasa.addProperty("Par_ISR", cliTasISR);
		calculaTasa.addProperty("Cli_CobISR", cliCobISR);

		JsonObject resultadoCalculaTasa = Utilerias.calculaTasa(calculaTasa);
		logger.debug("resultadoCalculaTasa" + resultadoCalculaTasa);
		
		Double invTasa = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Tasa");
		Double invCanBru = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanBru");
		Double invISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_ISR");
		Double invCanISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanISR");
		Double invCanNet = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanNet");
		Double invCanTot = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanTot");
		
		JsonObject datosInversionesCedeAlta = new JsonObject();
		datosInversionesCedeAlta.addProperty("Inv_Cuenta", invCuenta);
		datosInversionesCedeAlta.addProperty("Inv_Plazo", plaNumero);
		datosInversionesCedeAlta.addProperty("Inv_FecVen", invFecVen);
		datosInversionesCedeAlta.addProperty("Inv_Cantid", monto);
		datosInversionesCedeAlta.addProperty("Inv_Tasa", invTasa);
		datosInversionesCedeAlta.addProperty("Inv_ForTas", fotNumero);
		datosInversionesCedeAlta.addProperty("Inv_Produc", plaProduc);
		datosInversionesCedeAlta.addProperty("Inv_PorBas", invPorBas);
		datosInversionesCedeAlta.addProperty("Inv_Puntos", invPuntos);
		datosInversionesCedeAlta.addProperty("Inv_TBruta", invTBruta);
		datosInversionesCedeAlta.addProperty("NumTransac", folTransa);
		datosInversionesCedeAlta.addProperty("FechaSis", fechaSis);
		
		if("002".equals(fotNumero)) {
			datosInversionesCedeAlta.addProperty("Inv_TasBas", InversionesCedeInvTasBas);
		}
		
		JsonObject inversionesCedeAltaOpResultado = this.inversionesServicio.inversionesCedeAlta(datosInversionesCedeAlta);
		logger.debug("inversionesCedeAltaOpResultado" + inversionesCedeAltaOpResultado);
		
		JsonObject inversionesCedeAltaObjeto = Utilerias.obtenerJsonObjectPropiedad(inversionesCedeAltaOpResultado, "inversion");
		String invNumero = Utilerias.obtenerStringPropiedad(inversionesCedeAltaObjeto, "Inv_Numero");
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		
		Date fechaIni = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(fechaSis), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
		
		Date amortiDiaPago = Utilerias.calcularAmortizacionDiaDePago(diaPId, fechaIni, diaAmo);
		
		JsonObject datosAmortizacionGenerar = new JsonObject();
		datosAmortizacionGenerar.addProperty("Ced_Cantid", monto);
		datosAmortizacionGenerar.addProperty("Ced_Plazo", plaDias);
		datosAmortizacionGenerar.addProperty("Ced_FecIni", invFecIni);
		datosAmortizacionGenerar.addProperty("Ced_FecVen", invFecVen);
		datosAmortizacionGenerar.addProperty("Ced_DiaPag", diaPId);
		datosAmortizacionGenerar.addProperty("Ced_DiPaFe", sdf.format(amortiDiaPago));
		datosAmortizacionGenerar.addProperty("Ced_TasBru", tasTasa);
		datosAmortizacionGenerar.addProperty("Ced_TasNet", invTasa);
		datosAmortizacionGenerar.addProperty("Ced_Produc", plaProduc);
		datosAmortizacionGenerar.addProperty("FechaSis", fechaSis);
		
		JsonObject amortizacionGenerarOpResultado = this.amortizacionServicio.amortizacionGenerar(datosAmortizacionGenerar);
		logger.debug("amortizacionGenerarOpResultado" + amortizacionGenerarOpResultado);
		
		JsonObject amortizacionGenerarObjeto = Utilerias.obtenerJsonObjectPropiedad(amortizacionGenerarOpResultado, "amortizaciones");
		JsonArray amortizacionGenerarArreglo = Utilerias.obtenerJsonArrayPropiedad(amortizacionGenerarObjeto, "amortizacion");
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		String tablaAmortiBody = "";
		
		if(amortizacionGenerarArreglo != null) {
			for(JsonElement amoElemento : amortizacionGenerarArreglo) {
				JsonObject amoObjeto = amoElemento.getAsJsonObject();
				if(!amoObjeto.isJsonNull()) {
					String amoFecIni = Utilerias.obtenerStringPropiedad(amoObjeto, "Amo_FecIni");
					Date cpAmoFecIni = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(amoFecIni), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
					String amoFecVen = Utilerias.obtenerStringPropiedad(amoObjeto, "Amo_FecVen");
					Date cpAmoFecVen = Utilerias.convertirZonaHoraria(Utilerias.convertirFecha(amoFecVen), TimeZone.getTimeZone("CST6CDT"), TimeZone.getTimeZone("UTC"));
					
					String amoNumero = Utilerias.obtenerStringPropiedad(amoObjeto, "Amo_Numero");
					Double amoCantid = Utilerias.obtenerDoublePropiedad(amoObjeto, "Amo_Cantid");
					Double amoIntNet = Utilerias.obtenerDoublePropiedad(amoObjeto, "Amo_IntNet");
					Double amoISR = Utilerias.obtenerDoublePropiedad(amoObjeto, "Amo_ISR");
										
					JsonObject datosAmortizacionAlta = new JsonObject();
					datosAmortizacionAlta.addProperty("Amo_Invers", invNumero);
					datosAmortizacionAlta.addProperty("Amo_Numero", amoNumero);
					datosAmortizacionAlta.addProperty("Amo_FecIni", sdf.format(cpAmoFecIni));
					datosAmortizacionAlta.addProperty("Amo_FecVen", sdf.format(cpAmoFecVen));
					datosAmortizacionAlta.addProperty("Amo_Cantid", amoCantid);
					datosAmortizacionAlta.addProperty("Amo_Tasa", invTasa);
					datosAmortizacionAlta.addProperty("Amo_TasBru", tasTasa);
					datosAmortizacionAlta.addProperty("Amo_ISR", cliTasISR);
					datosAmortizacionAlta.addProperty("NumTransac", folTransa);
					datosAmortizacionAlta.addProperty("FechaSis", fechaSis);
					
					this.amortizacionServicio.amortizacionAlta(datosAmortizacionAlta);
					
					tablaAmortiBody += new StringBuilder()
							.append("<tr><td align='center'>")
							.append(sdf.format(cpAmoFecVen))
							.append("</td><td align='right'>$")
							.append(formatter.format(amoCantid))
							.append("</td><td align='right'>$")
							.append(formatter.format(amoIntNet))
							.append("</td><td align='right'>$")
							.append(formatter.format(amoISR))
							.append("</td><td align='right'>$")
							.append(formatter.format(amoCantid + amoIntNet))
							.append("</td></tr>")
							.toString();
				}
			}
		}
		
		JsonObject datosInversionesCedePagar = new JsonObject();
		datosInversionesCedePagar.addProperty("Inv_Numero", invNumero);
		datosInversionesCedePagar.addProperty("NumTransac", folTransa);
		datosInversionesCedePagar.addProperty("FechaSis", fechaSis);
		
		this.inversionesServicio.inversionesCedePagar(datosInversionesCedePagar);
		
		JsonObject datosInversionVsEstadoCuenta = new JsonObject();
		datosInversionVsEstadoCuenta.addProperty("Cor_Usuari", usuNumero);
		datosInversionVsEstadoCuenta.addProperty("Cor_Cuenta", invCuenta);
		datosInversionVsEstadoCuenta.addProperty("Cor_MonDia", monto);
		datosInversionVsEstadoCuenta.addProperty("NumTransac", folTransa);
		datosInversionVsEstadoCuenta.addProperty("FechaSis", fechaSis);
		
		this.inversionesServicio.inversionesContraEstadoCuentaActualizar(datosInversionVsEstadoCuenta);
		
		String bitPriRef = new StringBuilder()
				.append(invCuenta)
				.append("-$")
				.append(monto)
				.toString();
		
		String bitSegRef = new StringBuilder()
				.append(plaDias)
				.append(" Dias-CEDE")
				.toString();
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_TipOpe", InversionCedeAltaBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		datosBitacora.addProperty("Bit_SegRef", bitSegRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);

		this.bitacoraServicio.creacionBitacora(datosBitacora);
		
		/**
		 * REGLA DE NEGOCIO: Envio de correo con plantilla establecida por BIM y encriptado de digito verificador
		 */

		String asunto = Utilerias.obtenerPropiedadPlantilla("mail.alta_inversion_cede_valor.asunto");
		String plantilla = Utilerias.obtenerPlantilla("alta-inversion-cede-valor");

		SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fechaSisRedu = null;
		
		try {
			fechaSisRedu = sdfTemp.parse(fechaSis);
		} catch (Exception e) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		String strVerifi = "Origen:" + invCuenta + " Destino:" + invNumero + " Cantidad:" + monto + " Folio:" + folTransa;
		String digitoVerificador = Utilerias.generarDigitoVerificador(strVerifi);
		String strVerifi1 = digitoVerificador.substring(0, 70);
		String strVerifi2 = digitoVerificador.substring(70, 140);
		String strVerifi3 = digitoVerificador.substring(140, 210);
		String strVerifi4 = digitoVerificador.substring(210, 280);
		String strVerifi5 = digitoVerificador.substring(280);

		BimEmailTemplateDTO emailTemplateDTO = new BimEmailTemplateDTO(plantilla);
		emailTemplateDTO.addMergeVariable("Inv_Numero", Utilerias.formatearCuenta(invCuenta, 7, 14));
		emailTemplateDTO.addMergeVariable("Inv_Cuenta", Utilerias.formatearCuenta(invCuenta, 4, 14));
		emailTemplateDTO.addMergeVariable("Inv_Cantid", String.valueOf(formatter.format(monto)));
		emailTemplateDTO.addMergeVariable("Inv_PlazoDescri", plaDescri);
		emailTemplateDTO.addMergeVariable("Inv_GATCede", invGAT != null ? invGAT.toString() : "N.A.");
		emailTemplateDTO.addMergeVariable("Inv_GATReaCede", invGATRea != null ? invGATRea.toString() : "N.A.");
		emailTemplateDTO.addMergeVariable("Inv_FecIni", invFecIni);
		emailTemplateDTO.addMergeVariable("Inv_FecVen", invFecVen);
		emailTemplateDTO.addMergeVariable("Tas_Referencia", tasEsquema);
		emailTemplateDTO.addMergeVariable("Inv_ISR", invISR.toString());
		emailTemplateDTO.addMergeVariable("NumTransac", folTransa);
		emailTemplateDTO.addMergeVariable("Usu_Nombre", usuNombre);
		emailTemplateDTO.addMergeVariable("FechaSis", fechaSisRedu != null ? sdf.format(fechaSisRedu) : "");
		emailTemplateDTO.addMergeVariable("TablaAmortiBody", tablaAmortiBody);
		emailTemplateDTO.addMergeVariable("Str_Verifi1", strVerifi1);
		emailTemplateDTO.addMergeVariable("Str_Verifi2", strVerifi2);
		emailTemplateDTO.addMergeVariable("Str_Verifi3", strVerifi3);
		emailTemplateDTO.addMergeVariable("Str_Verifi4", strVerifi4);
		emailTemplateDTO.addMergeVariable("Str_Verifi5", strVerifi5);
		String cuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateDTO);

		this.correoServicio.enviarCorreo(usuEmail, asunto, cuerpo);
		
		JsonObject inversion = new JsonObject();
		inversion.addProperty("invCuenta", invCuenta);
		inversion.addProperty("invCantid", monto);
		inversion.addProperty("invNueva", invNumero);
		inversion.addProperty("invPlazoDescri", plaDescri);
		inversion.addProperty("invGAT", invGAT != null ? invGAT.toString() : "N.A.");
		inversion.addProperty("invGATRea", invGATRea != null ? invGATRea.toString() : "N.A.");
		inversion.addProperty("invFecIni", invFecIni);
		inversion.addProperty("invFecVen", invFecVen);
		inversion.addProperty("tasReferComp", tasEsquema);
		inversion.addProperty("invTBruta", invTBruta);
		inversion.addProperty("invCanBru", invCanBru);
		inversion.addProperty("invISR", invISR);
		inversion.addProperty("invCanISR", invCanISR);
		inversion.addProperty("invTasa", invTasa);
		inversion.addProperty("invCanNet", invCanNet);
		inversion.addProperty("invCanTot", invCanTot);
		inversion.addProperty("usuNombre", usuNombre);
		inversion.addProperty("strVerifi", digitoVerificador);
		
		JsonObject resultado = new JsonObject();
		resultado.add("inversion", inversion);
		
		logger.info("CTRL: Terminando inversionCedeAlta metodo");
		return Response.status(Response.Status.CREATED)
				.type(MediaType.APPLICATION_JSON).entity(resultado.toString())
				.build();
	}
}
