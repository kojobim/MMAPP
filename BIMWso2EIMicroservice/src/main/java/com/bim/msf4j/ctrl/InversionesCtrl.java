package com.bim.msf4j.ctrl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.BimEmailTemplateDTO;
import com.bim.commons.enums.InversionesCategoriasEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.ClienteServicio;
import com.bim.commons.service.ConfiguracionServicio;
import com.bim.commons.service.InversionesServicio;
import com.bim.commons.service.ReinversionServicio;
import com.bim.commons.service.TasaServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.service.CorreoServicio;
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

	private static String InversionesFilterBy;
	private static Integer InversionesMaximoPagina;
	
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
		
		
		InversionesFilterBy = properties.getProperty("inversiones_servicio.filter_by");
		InversionesMaximoPagina = Integer.parseInt(properties.getProperty("inversiones_servicio.maximo_pagina"));
		
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

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String numTransac = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_DireIP = solicitud.getHeader("User-Agent") == null ? solicitud.getHeader("User-Agent") : "";
		String bit_PriRef = solicitud.getHeader("X-Forwarded-For") == null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		String usuNumero = "001844";
		String usuClient = "00193500";
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bit_PriRef);
		datosBitacora.addProperty("Bit_DireIP", bit_DireIP);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);

		this.bitacoraServicio.creacionBitacora(datosBitacora);

		JsonObject inversionesObtener = new JsonObject();
		inversionesObtener.addProperty("Inv_Client", usuClient);
		inversionesObtener.addProperty("FechaSis", fechaSis);
				
		JsonObject inversionesObtenerOpResultadoObjeto = this.inversionesServicio.inversionesObtener(inversionesObtener);
		logger.info("inversionesObtenerOpResultadoObjeto" + inversionesObtenerOpResultadoObjeto);
		
		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
		inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
		inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
	
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);
		
		JsonArray inversionesResultado = new JsonArray();

		inversionesResultado.addAll(inversionesObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());

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

			Date fechaVen = null;
			Date horIni = null;
			Date horFin = null;

			try {
				SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
				fechaVen = sdfFecha.parse(invFecVen);
			} catch (Exception e) {
				try {
					SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
					fechaVen = sdfFecha.parse(invFecVen);
				} catch (ParseException ex) {
					logger.info("formato sdfFecha no valido.");
				}
			}

			try {
				SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				horIni = sdfHora.parse(horHorIni);
			} catch (Exception e) {
				try {
					SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					horIni = sdfHora.parse(horHorIni);
				} catch (Exception ex) {
					logger.info("error al formatear HorIni.");
				}
			}

			try {
				SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				horFin = sdfHora.parse(horHorFin);
			} catch (Exception e) {
				try {
					SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					horFin = sdfHora.parse(horHorFin);
				} catch (Exception ex) {
					logger.info("error al formatear HorFin.");
				}
			}

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

				Date fechaIni = null;
				Date fechaVen = null;

				try {
					SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
					fechaIni = sdfFecha.parse(invFecIni);
				} catch (Exception e) {
					try {
						SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
						fechaIni = sdfFecha.parse(invFecIni);
					} catch (ParseException ex) {
						logger.info("formato sdfFecha no valido.");
					}
				}

				try {
					SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
					fechaVen = sdfFecha.parse(invFecVen);
				} catch (Exception e) {
					try {
						SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
						fechaVen = sdfFecha.parse(invFecVen);
					} catch (ParseException ex) {
						logger.info("formato sdfFecha no valido.");
					}
				}

				Date horIni = null;
				Date horFin = null;

				SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try {
					horIni = sdfHora.parse(horHorIni);
				} catch (Exception e) {
					logger.info("error al formatear HorIni.");
				}

				try {
					horFin = sdfHora.parse(horHorFin);
				} catch (Exception e) {
					logger.info("error al formatear HorFin.");
				}

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
		// String usuFolTok = principalResultadoObjecto.get("usuFolTok").getAsString();
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		String usuFolTok = "0416218850";
		
		String bitPriRef = solicitud.getHeader("User-Agent");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));

		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");
		
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

		JsonObject inversionesObjecto = Utilerias.obtenerJsonObjectPropiedad(inversionConsultarOpResultadoObjeto, "inversiones");
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
		datosCliente.addProperty("NumTransac", numTransac);
		datosCliente.addProperty("FechaSis", fechaSis);
		
		logger.info("datosCliente" + datosCliente);
		JsonObject clienteConsultarOpResultadoObjeto = this.clienteServicio.clienteConsultar(datosCliente);
		logger.info("clienteConsultarOpResultadoObjeto" + clienteConsultarOpResultadoObjeto);

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

		String cliTipo = Utilerias.obtenerStringPropiedad(cliente, "Cli_Tipo");
		Double invCantid = Utilerias.obtenerDoublePropiedad(inversion, "Inv_Cantid");

		JsonObject datosTasaCliente = new JsonObject();
		datosTasaCliente.addProperty("Cli_Numero", usuClient);
		datosTasaCliente.addProperty("Inv_Cantid", invCantid);
		datosTasaCliente.addProperty("Cli_Tipo", cliTipo);
		datosTasaCliente.addProperty("Plazo", plazo);
		datosTasaCliente.addProperty("NumTransac", numTransac);
		datosTasaCliente.addProperty("FechaSis", fechaSis);

		logger.info("datosTasaCliente" + datosTasaCliente);
		JsonObject tasaClienteConsultarOpResultadoObjeto = this.tasaServicio.tasaClienteConsultar(datosTasaCliente);
		logger.info("tasaClienteConsultarOpResultadoObjeto" + tasaClienteConsultarOpResultadoObjeto);

		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("NumTransac", numTransac);
		datosMoneda.addProperty("FechaSis", fechaSis);

		logger.info("datosMoneda" + datosMoneda);
		JsonObject tasaMonedaConsultarOpResultadoObjeto = this.tasaServicio.tasaMonedaConsultar(datosMoneda);
		logger.info("tasaMonedaConsultarOpResultadoObjeto" + tasaMonedaConsultarOpResultadoObjeto);

		/**
		 * REGLA DE NEGOCIO: verifica que la cantidad de inversión en UDIS sea menor a 400,000.00 para calcular GAT y GATReal
		 */

		JsonObject monedaConsultar = Utilerias.obtenerJsonObjectPropiedad(tasaMonedaConsultarOpResultadoObjeto, "monedaConsultar");
		Double monFixCom = Utilerias.obtenerDoublePropiedad(monedaConsultar, "Mon_FixCom");
		Double MonTotUDI = 400000.00;
		Double invGAT = 0.00;
		Double invGATRea = 0.00;

		JsonObject clienteConsultar = Utilerias.obtenerJsonObjectPropiedad(tasaClienteConsultarOpResultadoObjeto, "clienteConsultar");
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

			JsonObject GATConsultaCalcular = Utilerias.obtenerJsonObjectPropiedad(tasaGATConsultaCalcularOpResultadoObjeto, "GATConsultaCalcular");
			invGAT = Utilerias.obtenerDoublePropiedad(GATConsultaCalcular, "Inv_GAT");	

			JsonObject datosGATRea = new JsonObject();
			datosGATRea.addProperty("Inv_GAT", invGAT);
			datosGATRea.addProperty("NumTransac", numTransac);
			datosGATRea.addProperty("FechaSis", fechaSis);

			logger.info("datosGATRea" + datosGATRea);
			JsonObject tasaGATRealConsultaCalcularOpResultadoObjeto = this.tasaServicio.tasaGATRealConsultaCalcular(datosGATRea);
			logger.info("tasaGATRealConsultaCalcularOpResultadoObjeto" + tasaGATRealConsultaCalcularOpResultadoObjeto);

			JsonObject GATRealConsultaCalcular = Utilerias.obtenerJsonObjectPropiedad(tasaGATRealConsultaCalcularOpResultadoObjeto, "GATRealConsultaCalcular");
			invGATRea = Utilerias.obtenerDoublePropiedad(GATRealConsultaCalcular, "Inv_GATRea");
		}	

		int invPlazo = Utilerias.obtenerIntPropiedad(inversion, "Inv_Plazo");
		JsonObject resultadoCalculaTasa = null;
		JsonObject informacionSucursal = Utilerias.obtenerJsonObjectPropiedad(informacionSucursalObtenerOpResultadoObjeto, "informacionSucursal");

		int parDiBaIn = Utilerias.obtenerIntPropiedad(informacionSucursal, "Par_DiBaIn");
		Double cliTasISR = Utilerias.obtenerDoublePropiedad(cliente, "Cli_TasISR");
		String cliCobISR = Utilerias.obtenerStringPropiedad(cliente, "Cli_CobISR");

		/**
		 * REGLA DE NEGOCIO: verifica que la cantidad de inversión sea mayor a 5000 y el plazo sea mayor a cero 
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

		/**
		 * REGLA DE NEGOCIO: valida token de transacción y bloquea al usuario en caso de 5 intentos fallidos
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
		String inverNumero = Utilerias.obtenerStringPropiedad(inversion, "Inv_Numero");

		JsonObject datosStatusActualizar = new JsonObject();
		datosStatusActualizar.addProperty("Adi_Invers", inverNumero);
		datosStatusActualizar.addProperty("NumTransac", numTransac);
		datosStatusActualizar.addProperty("FechaSis", fechaSis);

		logger.info("datosStatusActualizar" + datosStatusActualizar);
		JsonObject inversionesStatusActualizarOpResultadoObjeto = this.inversionesServicio.inversionesStatusActualizar(datosStatusActualizar);
		logger.info("inversionesStatusActualizarOpResultadoObjeto" + inversionesStatusActualizarOpResultadoObjeto);

		JsonObject fechaHabil = Utilerias.obtenerJsonObjectPropiedad(fechaHabilConsultarOpResultadoObjeto, "fechaHabil");
		
		String sigFecha = Utilerias.obtenerStringPropiedad(fechaHabil, "Fecha");
		Double invCanTot = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanTot");
		Double invTasa = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Tasa");
		Double invISR = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_ISR");
		Double invCapita = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_Capita");
		Double invCanNet = Utilerias.obtenerDoublePropiedad(resultadoCalculaTasa, "Inv_CanNet");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		TimeZone tz = TimeZone.getTimeZone("UTC");
		simpleDateFormat.setTimeZone(tz);

		Date rfecIn = null;
		Date rfecVe = null;
		try {
			rfecIn = simpleDateFormat.parse(fechaSis);	
		} catch (Exception e) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");		
			rfecVe = sdf.parse(sigFecha);
		} catch (Exception e) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		String invCuenta = Utilerias.obtenerStringPropiedad(inversion, "Inv_Cuenta");

		JsonObject datosInversionFinalizada = new JsonObject();
		datosInversionFinalizada.addProperty("Inv_Numero", inverNumero);
		datosInversionFinalizada.addProperty("Inv_Deposi", invCapita);
		datosInversionFinalizada.addProperty("Inv_rFecIn", rfecIn != null ? simpleDateFormat.format(rfecIn) : "");
		datosInversionFinalizada.addProperty("Inv_rFecVe", rfecVe != null ? simpleDateFormat.format(rfecVe) : "");
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

		JsonObject datosProcesoLiquidacion = new JsonObject();
		datosProcesoLiquidacion.addProperty("Inv_Numero", invNumero);
		datosProcesoLiquidacion.addProperty("Inv_rFecIn", rfecIn != null ? simpleDateFormat.format(rfecIn) : "");
		datosProcesoLiquidacion.addProperty("Inv_rFecVe", rfecVe != null ? simpleDateFormat.format(rfecVe) : "");
		datosProcesoLiquidacion.addProperty("Inv_rCanti", invCantid);
		datosProcesoLiquidacion.addProperty("Inv_rTasa", invTasa);
		datosProcesoLiquidacion.addProperty("Inv_rISR", invISR);
		datosProcesoLiquidacion.addProperty("Inv_rCuent", invCuenta);
		datosProcesoLiquidacion.addProperty("Dias_Base", parDiBaIn);		
		datosProcesoLiquidacion.addProperty("Inv_Fecha", rfecIn != null ? simpleDateFormat.format(rfecIn) : "");
		datosProcesoLiquidacion.addProperty("Inv_rTBrut", invTasInt);
		datosProcesoLiquidacion.addProperty("NumTransac", numTransac);		
		datosProcesoLiquidacion.addProperty("FechaSis", fechaSis);

		logger.info("datosProcesoLiquidacion" + datosProcesoLiquidacion);
		JsonObject inversionesProcesoLiquidacionGenerarOpResultadoObjeto = this.inversionesServicio.inversionesProcesoLiquidacionGenerar(datosProcesoLiquidacion);
		logger.info("inversionesProcesoLiquidacionGenerarOpResultadoObjeto" + inversionesProcesoLiquidacionGenerarOpResultadoObjeto);
		
		JsonObject ProcesoLiquidacionGenerar = Utilerias.obtenerJsonObjectPropiedad(inversionesProcesoLiquidacionGenerarOpResultadoObjeto, "ProcesoLiquidacionGenerar");
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
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);

		logger.info("datosBitacora" + datosBitacora);
		JsonObject bitacoraCreacionOpResultadoObjeto = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);

		JsonObject datosIversionVsEstadoCuenta = new JsonObject();
		datosIversionVsEstadoCuenta.addProperty("Cor_Usuari", usuNumero);
		datosIversionVsEstadoCuenta.addProperty("Cor_Cuenta", invCuenta);
		datosIversionVsEstadoCuenta.addProperty("Cor_MonDia", invCanTot);
		datosIversionVsEstadoCuenta.addProperty("NumTransac", numTransac);
		datosIversionVsEstadoCuenta.addProperty("FechaSis", fechaSis);

		logger.info("datosIversionVsEstadoCuenta" + datosIversionVsEstadoCuenta);
		JsonObject inversionesContraEstadoCuentaActualizarOpResultadoObjeto = this.inversionesServicio.inversionesContraEstadoCuentaActualizar(datosIversionVsEstadoCuenta);
		logger.info("inversionesContraEstadoCuentaActualizarOpResultadoObjeto" + inversionesContraEstadoCuentaActualizarOpResultadoObjeto);
		
		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
		inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
		inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
		
		logger.info("inversionesPagareNumeroUsuarioObtener" + inversionesPagareNumeroUsuarioObtener);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = this.inversionesServicio.inversionesPagareNumeroUsuarioObtener(inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);

		JsonObject inversiones = Utilerias.obtenerJsonObjectPropiedad(inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto, "inversiones");
		JsonArray inversionesArreglos = Utilerias.obtenerJsonArrayPropiedad(inversiones, "inversion");

		Double objInvGAT = null;
		Date fecIni = null;
		Date fecVen = null;
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

				 try {
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
					 fecIni = sdf.parse(invFecIni);
				 } catch (Exception e) {
					BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
					throw new InternalServerException(bimMessageDTO.toString());
				 }

				 try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
					fecVen = sdf.parse(invFecVen);
				} catch (Exception e) {
					BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
					throw new InternalServerException(bimMessageDTO.toString());
				}

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
				inversionRenovada.addProperty("invFecIni", fecIni != null ? simpleDateFormat.format(fecIni) : "");
				inversionRenovada.addProperty("invISR", invISR);
				inversionRenovada.addProperty("invCanISR", rInvCanISR);
				inversionRenovada.addProperty("invFecVen", fecVen != null ? simpleDateFormat.format(fecVen) : "");
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
		emailTemplateDTO.addMergeVariable("Inv_FecIni", fecIni != null ? simpleDateFormat.format(fecIni) : "");
		emailTemplateDTO.addMergeVariable("Inv_FecVen", fecVen != null ? simpleDateFormat.format(fecVen) : "");
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
	
}
