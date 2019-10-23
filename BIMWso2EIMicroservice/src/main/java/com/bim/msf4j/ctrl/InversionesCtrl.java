package com.bim.msf4j.ctrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;
import org.wso2.msf4j.internal.MicroservicesRegistryImpl;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.utils.Filtrado;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Utilerias;
import com.bim.msf4j.exceptions.BimExceptionMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/inversiones")
public class InversionesCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(InversionesCtrl.class);
	
	private static String DataServiceHost;
	
	private static String TransaccionServicio;
	private static String BitacoraServicio;
	private static String InversionesServicio;
	private static String ConfiguracionServicio;

	private static String FolioTransaccionGenerarOp;
	private static String BitacoraCreacionOp;
	private static String InversionesObtenerOp;
	private static String InversionesPagareNumeroUsuarioObtenerOp;
	private static String HorarioInversionOp;

	private static String FolioTransaccionGenerarOpSucOrigen;
	private static String BitacoraCreacionOpBitTipOpe;
	private static String BitacoraCreacionOpTransaccio;
	private static String BitacoraCreacionOpUsuario;
	private static String BitacoraCreacionOpSucOrigen;
	private static String BitacoraCreacionOpSucDestino;
	private static String BitacoraCreacionOpModulo;
	private static String BitacoraCreacionOpBitMonto;
	private static String InversionesObtenerOpInvMoneda;
	private static String InversionesObtenerOpTransaccio;
	private static String InversionesObtenerOpUsuario;
	private static String InversionesObtenerOpSucOrigen;
	private static String InversionesObtenerOpSucDestino;
	private static String InversionesObtenerOpModulo;
	private static String InversionesPagareNumeroUsuarioObtenerOpTipConsul;
	private static String InversionesPagareNumeroUsuarioObtenerOpTransaccio;
	private static String InversionesPagareNumeroUsuarioObtenerOpUsuario;
	private static String InversionesPagareNumeroUsuarioObtenerOpSucOrigen;
	private static String InversionesPagareNumeroUsuarioObtenerOpSucDestino;
	private static String InversionesPagareNumeroUsuarioObtenerOpModulo;
	private static String HorarioInversionOpTipConsul;
	private static String HorarioInversionOpTipTransf;
	private static String HorarioInversionOpTransaccio;
	private static String HorarioInversionOpUsuario;
	private static String HorarioInversionOpSucOrigen;
	private static String HorarioInversionOpSucDestino;
	private static String HorarioInversionOpModulo;

	private static String InversionesFilterBy;
	private static Integer InversionesMaximoPagina;
	
	public InversionesCtrl() {
		super();
		logger.info("Ctrl: Empezando metodo init...");
		
		FolioTransaccionGenerarOpSucOrigen = properties.getProperty("op.folio_transaccion_generar.suc_origen");
		
		BitacoraCreacionOpBitTipOpe = properties.getProperty("op.bitacora_creacion.bit_tipope.inversiones_listado");
		BitacoraCreacionOpTransaccio = properties.getProperty("op.bitacora_creacion.transaccio");
		BitacoraCreacionOpUsuario = properties.getProperty("op.bitacora_creacion.usuario");
		BitacoraCreacionOpSucOrigen = properties.getProperty("op.bitacora_creacion.suc_origen");
		BitacoraCreacionOpSucDestino= properties.getProperty("op.bitacora_creacion.suc_destino");
		BitacoraCreacionOpModulo = properties.getProperty("op.bitacora_creacion.modulo");
		BitacoraCreacionOpBitMonto = properties.getProperty("op.bitacora_creacion.bit_monto");

		InversionesObtenerOpInvMoneda = properties.getProperty("op.inversiones_obtener.inv_moneda");
		InversionesObtenerOpTransaccio = properties.getProperty("op.inversiones_obtener.transaccio");
		InversionesObtenerOpUsuario = properties.getProperty("op.inversiones_obtener.usuario");
		InversionesObtenerOpSucOrigen = properties.getProperty("op.inversiones_obtener.suc_origen");
		InversionesObtenerOpSucDestino = properties.getProperty("op.inversiones_obtener.suc_destino");
		InversionesObtenerOpModulo = properties.getProperty("op.inversiones_obtener.modulo");

		InversionesPagareNumeroUsuarioObtenerOpTipConsul = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.tip_consul");
		InversionesPagareNumeroUsuarioObtenerOpTransaccio = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.transaccio");
		InversionesPagareNumeroUsuarioObtenerOpUsuario = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.usuario");
		InversionesPagareNumeroUsuarioObtenerOpSucOrigen = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_origen");
		InversionesPagareNumeroUsuarioObtenerOpSucDestino = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_destino");
		InversionesPagareNumeroUsuarioObtenerOpModulo = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.modulo");

		HorarioInversionOpTipConsul = properties.getProperty("op.horario_inversion.tip_consul");
		HorarioInversionOpTipTransf = properties.getProperty("op.horario_inversion.tip_transf");
		HorarioInversionOpTransaccio = properties.getProperty("op.horario_inversion.transaccio");
		HorarioInversionOpUsuario = properties.getProperty("op.horario_inversion.usuario");
		HorarioInversionOpSucOrigen = properties.getProperty("op.horario_inversion.suc_origen");
		HorarioInversionOpSucDestino = properties.getProperty("op.horario_inversion.suc_destino");
		HorarioInversionOpModulo = properties.getProperty("op.horario_inversion.modulo");
		
		DataServiceHost = properties.getProperty("data_service.host");
		
		logger.info("DataServiceHost" + DataServiceHost);
		
		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");
		ConfiguracionServicio = properties.getProperty("data_service.configuracion_servicio");
		
		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
		InversionesObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_obtener");
		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_numero_usuario_obtener");
		HorarioInversionOp = properties.getProperty("configuracion_servicio.op.horario_inversion");
		
		InversionesFilterBy = properties.getProperty("inversiones_servicio.filter_by");
		InversionesMaximoPagina = Integer.parseInt(properties.getProperty("inversiones_servicio.maximo_pagina"));
		
		logger.info("Ctrl: Terminando metodo init...");
	}
	
	@Path("/")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject inversionesListado(@QueryParam("page") String page, @QueryParam("per_page") String perPage, @QueryParam("filter_by") String filterBy, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando inversionesListado metodo");
		
		if(page == null || perPage == null) 
			throw new BadRequestException("BIM.MENSAJ.2");
		
		if(!Utilerias.isNumber(page)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.isNumber(perPage)) {
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

		if(filterBy != null && !filterBy.equals(InversionesFilterBy))
			throw new BadRequestException("BIM.MENSAJ.6");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		JsonObject datosTransaccion = new JsonObject();
		datosTransaccion.addProperty("Num_Transa", "");
		datosTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		logger.info("datosTransaccion" + datosTransaccion);
		StringBuilder folioTransaccionGenerarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TransaccionServicio)
				.append("/")
				.append(FolioTransaccionGenerarOp);
		JsonObject folioTransaccionGenerarOp = new JsonObject();
		folioTransaccionGenerarOp.add("folioTransaccionGenerarOp", datosTransaccion);
		logger.info("folioTransaccionGenerarOp" + folioTransaccionGenerarOp);

		RequestDTO folioTransaccionGenerarOpSolicitud = new RequestDTO();
		folioTransaccionGenerarOpSolicitud.setUrl(folioTransaccionGenerarUrl.toString());
		MessageProxyDTO folioTransaccionGenerarOpMensaje= new MessageProxyDTO();
		folioTransaccionGenerarOpMensaje.setBody(folioTransaccionGenerarOp.toString());
		folioTransaccionGenerarOpSolicitud.setMessage(folioTransaccionGenerarOpMensaje);

		String folioTransaccionGenerarOpResultado = HttpClientUtils.postPerform(folioTransaccionGenerarOpSolicitud);
		JsonObject folioTransaccionGenerarOpResultadoObjeto = new Gson().fromJson(folioTransaccionGenerarOpResultado, JsonObject.class);
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String numTransac = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_DireIP = solicitud.getHeader("User-Agent") == null ? solicitud.getHeader("User-Agent") : "";
		String bit_PriRef = solicitud.getHeader("X-Forwarded-For") == null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		/* 
			Parametros obtenidos por medio del principal 
				Bit_Usuari = usuNumero
				Inv_Client = usuClient
				Inv_Usuari = usuNumero
		*/
		
		String usuNumero = "001844";
		String usuClient = "00193500";
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", 0);
		datosBitacora.addProperty("Bit_PriRef", bit_PriRef);
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", bit_DireIP);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);

		StringBuilder bitacoraCreacionOpUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(BitacoraServicio)
				.append("/")
				.append(BitacoraCreacionOp);
		JsonObject bitacoraCreacionOp = new JsonObject();
		bitacoraCreacionOp.add("bitacoraCreacionOp", datosBitacora);
		logger.info("bitacoraCreacionOp" + bitacoraCreacionOp);

		RequestDTO bitacoraCreacionSolicitud = new RequestDTO();
		bitacoraCreacionSolicitud.setUrl(bitacoraCreacionOpUrl.toString());
		MessageProxyDTO bitacoraCreacionOpMensaje= new MessageProxyDTO();
		bitacoraCreacionOpMensaje.setBody(bitacoraCreacionOp.toString());
		bitacoraCreacionSolicitud.setMessage(bitacoraCreacionOpMensaje);
		HttpClientUtils.postPerform(bitacoraCreacionSolicitud);

		JsonObject inversionesObtener = new JsonObject();
		inversionesObtener.addProperty("Inv_Client", usuClient);
		inversionesObtener.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
		inversionesObtener.addProperty("NumTransac", "");
		inversionesObtener.addProperty("Transaccio", InversionesObtenerOpTransaccio);
		inversionesObtener.addProperty("Usuario", InversionesObtenerOpUsuario);
		inversionesObtener.addProperty("FechaSis", fechaSis);
		inversionesObtener.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
		inversionesObtener.addProperty("SucDestino", InversionesObtenerOpSucDestino);
		inversionesObtener.addProperty("Modulo", InversionesObtenerOpModulo);
				
		StringBuilder inversionesObtenerUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/")
				.append(InversionesObtenerOp);
		JsonObject inversionesObtenerOp = new JsonObject();
		inversionesObtenerOp.add("inversionesObtenerOp", inversionesObtener);
		logger.info("inversionesObtenerOp" + inversionesObtenerOp);

		RequestDTO inversionesObtenerSolicitud = new RequestDTO();
		inversionesObtenerSolicitud.setUrl(inversionesObtenerUrl.toString());
		MessageProxyDTO inversionesObtenerMensaje = new MessageProxyDTO();
		inversionesObtenerMensaje.setBody(inversionesObtenerOp.toString());
		inversionesObtenerSolicitud.setMessage(inversionesObtenerMensaje);
		logger.info("inversionesObtenerSolicitud " + inversionesObtenerSolicitud.toString());
		String inversionesObtenerOpResultado = HttpClientUtils.postPerform(inversionesObtenerSolicitud);
		JsonObject inversionesObtenerOpResultadoObjeto = new Gson().fromJson(inversionesObtenerOpResultado, JsonObject.class);
		logger.info("inversionesObtenerOpResultadoObjeto" + inversionesObtenerOpResultadoObjeto);
		
		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
			inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Numero", "");
			inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
			inversionesPagareNumeroUsuarioObtener.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
			inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
			inversionesPagareNumeroUsuarioObtener.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
			inversionesPagareNumeroUsuarioObtener.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);
			inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
			inversionesPagareNumeroUsuarioObtener.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
			inversionesPagareNumeroUsuarioObtener.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
			inversionesPagareNumeroUsuarioObtener.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
	
		StringBuilder inversionesPagareNumeroUsuarioObtenerUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/")
				.append(InversionesPagareNumeroUsuarioObtenerOp);
		JsonObject inversionesPagareNumeroUsuarioObtenerOp = new JsonObject();
		inversionesPagareNumeroUsuarioObtenerOp.add("inversionesPagareNumeroUsuarioObtenerOp", inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOp" + inversionesPagareNumeroUsuarioObtenerOp);

		RequestDTO inversionesPagareNumeroUsuarioObtenerSolicitud = new RequestDTO();
		inversionesPagareNumeroUsuarioObtenerSolicitud.setUrl(inversionesPagareNumeroUsuarioObtenerUrl.toString());
		MessageProxyDTO inversionesPagareNumeroUsuarioObtenerMensaje = new MessageProxyDTO();
		inversionesPagareNumeroUsuarioObtenerMensaje.setBody(inversionesPagareNumeroUsuarioObtenerOp.toString());
		inversionesPagareNumeroUsuarioObtenerSolicitud.setMessage(inversionesPagareNumeroUsuarioObtenerMensaje);
		
		String inversionesPagareNumeroUsuarioObtenerOpResultado = HttpClientUtils.postPerform(inversionesPagareNumeroUsuarioObtenerSolicitud);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = new Gson().fromJson(inversionesPagareNumeroUsuarioObtenerOpResultado, JsonObject.class);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);
		
		JsonArray inversionesResultado = new JsonArray();
		if(inversionesPagareNumeroUsuarioObtenerOp.has("inversiones") && inversionesPagareNumeroUsuarioObtenerOp.get("inversiones").getAsJsonObject().has("inversion")) {
			inversionesResultado.addAll(inversionesPagareNumeroUsuarioObtenerOp.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());
		}

		inversionesResultado.addAll(inversionesObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());

		JsonObject datosHorario = new JsonObject();
		datosHorario.addProperty("Tip_Consul", HorarioInversionOpTipConsul);
		datosHorario.addProperty("Tip_Transf", HorarioInversionOpTipTransf);
		datosHorario.addProperty("Err_Codigo", "");
		datosHorario.addProperty("Msj_Error", "");
		datosHorario.addProperty("NumTransac", numTransac);
		datosHorario.addProperty("Transaccio", HorarioInversionOpTransaccio);
		datosHorario.addProperty("Usuario", HorarioInversionOpUsuario);
		datosHorario.addProperty("FechaSis", fechaSis);
		datosHorario.addProperty("SucOrigen", HorarioInversionOpSucOrigen);
		datosHorario.addProperty("SucDestino", HorarioInversionOpSucDestino);
		datosHorario.addProperty("Modulo", HorarioInversionOpModulo);

		logger.info("datosHorario" + datosHorario);
		StringBuilder horarioInversionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(ConfiguracionServicio)
				.append("/")
				.append(HorarioInversionOp);

		JsonObject horarioInversionOp = new JsonObject();
		horarioInversionOp.add("horarioInversionOp", datosHorario);
		logger.info("horarioInversionOp" + horarioInversionOp);

		RequestDTO horarioInversionOpSolicitud = new RequestDTO();
		horarioInversionOpSolicitud.setUrl(horarioInversionUrl.toString());
		MessageProxyDTO horarioInversionOpMensaje = new MessageProxyDTO();
		horarioInversionOpMensaje.setBody(horarioInversionOp.toString());
		horarioInversionOpSolicitud.setMessage(horarioInversionOpMensaje);

		String horarioInversionOpResultado = HttpClientUtils.postPerform(horarioInversionOpSolicitud);
		JsonObject horarioInversionOpResultadoObjecto = new Gson().fromJson(horarioInversionOpResultado, JsonObject.class);
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
		return inversionesResultadoFinal;
	}

	@Path("{invNumero}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject detalleInversion(@PathParam("invNumero") String invNumero,
			@QueryParam("categoria") String categoria, @Context final Request solicitud) {
		logger.info("CTRL: Empezando detalleInversion Method...");
		SimpleDateFormat simpleDateFormatSis = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormatSis.format(fecha);

		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_PriRef = solicitud.getHeader("User-Agent");
		String bit_DireIP = solicitud.getHeader("X-Forwarded-For");

		JsonObject datosTransaccion = new JsonObject();
		datosTransaccion.addProperty("Num_Transa", "");
		datosTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);

		logger.info("datosTransaccion" + datosTransaccion);
		StringBuilder folioTransaccionGenerarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TransaccionServicio)
				.append("/")
				.append(FolioTransaccionGenerarOp);

		JsonObject folioTransaccionGenerarOp = new JsonObject();
		folioTransaccionGenerarOp.add("folioTransaccionGenerarOp", datosTransaccion);
		logger.info("folioTransaccionGenerarOp" + folioTransaccionGenerarOp);

		RequestDTO folioTransaccionGenerarOpSolicitud = new RequestDTO();
		folioTransaccionGenerarOpSolicitud.setUrl(folioTransaccionGenerarUrl.toString());
		MessageProxyDTO folioTransaccionGenerarOpMensaje = new MessageProxyDTO();
		folioTransaccionGenerarOpMensaje.setBody(folioTransaccionGenerarOp.toString());
		folioTransaccionGenerarOpSolicitud.setMessage(folioTransaccionGenerarOpMensaje);

		String folioTransaccionGenerarOpResultado = HttpClientUtils.postPerform(folioTransaccionGenerarOpSolicitud);
		JsonObject folioTransaccionGenerarOpResultadoObjeto = new Gson().fromJson(folioTransaccionGenerarOpResultado, JsonObject.class);
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String FolioTransaccionGenerarOpFolTransa = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();

		/* 
			Parametros obtenidos por medio del principal 
				Bit_Usuari = usuNumero
				Inv_Client = usuClient
				Inv_Usuari = usuNumero
		*/
		
		String usuNumero = "000024";
		String usuClient = "00193500";

		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", Integer.parseInt(BitacoraCreacionOpBitMonto));
		datosBitacora.addProperty("Bit_PriRef", bit_PriRef != null ? bit_PriRef : "");
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", bit_DireIP != null ? bit_DireIP : "");
		datosBitacora.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);

		logger.info("datosBitacora" + datosBitacora);
		StringBuilder bitacoraCreacionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(BitacoraServicio)
				.append("/")
				.append(BitacoraCreacionOp);

		JsonObject bitacoraCreacionOp = new JsonObject();
		bitacoraCreacionOp.add("bitacoraCreacionOp", datosBitacora);
		logger.info("bitacoraCreacionOp" + bitacoraCreacionOp);

		RequestDTO bitacoraCreacionOpSolicitud = new RequestDTO();
		bitacoraCreacionOpSolicitud.setUrl(bitacoraCreacionUrl.toString());
		MessageProxyDTO bitacoraCreacionOpMensaje = new MessageProxyDTO();
		bitacoraCreacionOpMensaje.setBody(bitacoraCreacionOp.toString());
		bitacoraCreacionOpSolicitud.setMessage(bitacoraCreacionOpMensaje);

		String bitacoraCreacionOpResultado = HttpClientUtils.postPerform(bitacoraCreacionOpSolicitud);
		JsonObject bitacoraCreacionOpResultadoObjeto = new Gson().fromJson(bitacoraCreacionOpResultado, JsonObject.class);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		
		JsonObject datosInversion = new JsonObject();
		datosInversion.addProperty("FechaSis", fechaSis);
		StringBuilder inversionConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/");

		if ("PAGARE".equals(categoria)) {
			datosInversion.addProperty("Inv_Numero", "");
			datosInversion.addProperty("Inv_Usuari", usuNumero);
			datosInversion.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
			datosInversion.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
			datosInversion.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
			datosInversion.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);			
			datosInversion.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
			datosInversion.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);

			inversionConsultarUrl
					.append(InversionesPagareNumeroUsuarioObtenerOp);
		} else {
			datosInversion.addProperty("Inv_Client", usuClient);
			datosInversion.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
			datosInversion.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
			datosInversion.addProperty("Transaccio", InversionesObtenerOpTransaccio);
			datosInversion.addProperty("Usuario", InversionesObtenerOpUsuario);
			datosInversion.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesObtenerOpSucDestino);
			datosInversion.addProperty("Modulo", InversionesObtenerOpModulo);

			inversionConsultarUrl
					.append(InversionesObtenerOp);
		}

		logger.info("datosInversion" + datosInversion);

		JsonObject inversionConsultarOp = new JsonObject();
		inversionConsultarOp.add("inversionConsultarOp", datosInversion);
		logger.info("inversionConsultarOp" + inversionConsultarOp);

		RequestDTO inversionConsultarOpSolicitud = new RequestDTO();
		inversionConsultarOpSolicitud.setUrl(inversionConsultarUrl.toString());
		MessageProxyDTO inversionConsultarOpMensaje = new MessageProxyDTO();
		inversionConsultarOpMensaje.setBody(inversionConsultarOp.toString());
		inversionConsultarOpSolicitud.setMessage(inversionConsultarOpMensaje);

		String inversionConsultarOpResultado = HttpClientUtils.postPerform(inversionConsultarOpSolicitud);
		JsonObject inversionConsultarOpResultadoObjeto = new Gson().fromJson(inversionConsultarOpResultado, JsonObject.class);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);

		JsonObject inversionesObjecto = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject();
		JsonArray inversionesArreglo = inversionesObjecto.has("inversion") ? inversionesObjecto.get("inversion").getAsJsonArray() : new JsonArray();

		JsonObject datosHorario = new JsonObject();
		datosHorario.addProperty("Tip_Consul", HorarioInversionOpTipConsul);
		datosHorario.addProperty("Tip_Transf", HorarioInversionOpTipTransf);
		datosHorario.addProperty("Err_Codigo", "");
		datosHorario.addProperty("Msj_Error", "");
		datosHorario.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
		datosHorario.addProperty("Transaccio", HorarioInversionOpTransaccio);
		datosHorario.addProperty("Usuario", HorarioInversionOpUsuario);
		datosHorario.addProperty("FechaSis", fechaSis);
		datosHorario.addProperty("SucOrigen", HorarioInversionOpSucOrigen);
		datosHorario.addProperty("SucDestino", HorarioInversionOpSucDestino);
		datosHorario.addProperty("Modulo", HorarioInversionOpModulo);

		logger.info("datosHorario" + datosHorario);
		StringBuilder horarioInversionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(ConfiguracionServicio)
				.append("/")
				.append(HorarioInversionOp);

		JsonObject horarioInversionOp = new JsonObject();
		horarioInversionOp.add("horarioInversionOp", datosHorario);
		logger.info("horarioInversionOp" + horarioInversionOp);

		RequestDTO horarioInversionOpSolicitud = new RequestDTO();
		horarioInversionOpSolicitud.setUrl(horarioInversionUrl.toString());
		MessageProxyDTO horarioInversionOpMensaje = new MessageProxyDTO();
		horarioInversionOpMensaje.setBody(horarioInversionOp.toString());
		horarioInversionOpSolicitud.setMessage(horarioInversionOpMensaje);

		String horarioInversionOpResultado = HttpClientUtils.postPerform(horarioInversionOpSolicitud);
		JsonObject horarioInversionOpResultadoObjecto = new Gson().fromJson(horarioInversionOpResultado, JsonObject.class);
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

				if ("PAGARE".equalsIgnoreCase(categoria)) {
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
			resultado = new JsonObject();
			JsonObject Error = new JsonObject();
			Error.addProperty("Err_Codigo", 409);
			Error.addProperty("Err_Mensaj", "Numero de inversion invalido");
			resultado.add("Error", Error);
		}

		return resultado;
    }
	
	private void addResourceToRegistry(MicroservicesRegistryImpl microservicesRegistryImpl) {
		microservicesRegistryImpl.addExceptionMapper(new BimExceptionMapper());
	}
}
