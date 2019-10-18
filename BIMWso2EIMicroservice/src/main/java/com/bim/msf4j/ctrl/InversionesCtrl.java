package com.bim.msf4j.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.log4j.Logger;
// import org.joda.time.DateTime;
// import org.joda.time.DateTime;
// import org.joda.time.format.DateTimeFormat;
// import org.joda.time.format.DateTimeFormatter;
import org.wso2.msf4j.Microservice;

@Path("/inversiones")
public class InversionesCtrl implements Microservice {

	private static final Logger logger = Logger.getLogger(InversionesCtrl.class);
	private static Properties properties;
	private static String FolioTransaccionGenerarSucOrigen;
	private static String BitacoraCreacionBitTipOpe;
	private static String BitacoraCreacionBitMonto;
	private static String BitacoraCreacionUsuario;
	private static String BitacoraCreacionSucOrigen;
	private static String BitacoraCreacionSucDestino;
	private static String BitacoraCreacionModulo;
	private static String InversionesPagareConsultaTipConsul;
	private static String InversionesPagareConsultaUsuario;
	private static String InversionesPagareConsultaSucOrigen;
	private static String InversionesPagareConsultaSucDestino;
	private static String InversionesPagareConsultaModulo;
	private static String InversionesObtenerInvMoneda;
	private static String InversionesObtenerUsuario;
	private static String InversionesObtenerSucOrigen;
	private static String InversionesObtenerSucDestino;
	private static String InversionesObtenerModulo;
	private static String DataServiceHost;
	private static String BitacoraServicio;
	private static String TransaccionServicio;
	private static String BitacoraCreacionOp;
	private static String FolioTransaccionGenerarOp;
	private static String InversionesServicio;
	private static String InversionesPagareNumeroUsuarioObtenerOp;
	private static String InversionesObtenerOp;

	@PostConstruct
	public void init() {
		try (InputStream inputStream = new FileInputStream(
				System.getenv("BIM_HOME") + "/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();

			if (inputStream != null) {
				properties.load(inputStream);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		FolioTransaccionGenerarSucOrigen = properties.getProperty("op.folio_transaccion_generar.sucOrigen");
		BitacoraCreacionBitTipOpe = properties.getProperty("op.bitacora_creacion.bit_tip_ope");
		BitacoraCreacionBitMonto = properties.getProperty("op.bitacora_creacion.bit_monto");
		BitacoraCreacionUsuario = properties.getProperty("op.bitacora_creacion.usuario");
		BitacoraCreacionSucOrigen = properties.getProperty("op.bitacora_creacion.suc_origen");
		BitacoraCreacionSucDestino = properties.getProperty("op.bitacora_creacion.suc_destino");
		BitacoraCreacionModulo = properties.getProperty("op.bitacora_creacion.modulo");

		InversionesPagareConsultaTipConsul = properties.getProperty("op.inversiones_pagare_consulta.tip_consul");
		InversionesPagareConsultaUsuario = properties.getProperty("op.inversiones_pagare_consulta.usuario");
		InversionesPagareConsultaSucOrigen = properties.getProperty("op.inversiones_pagare_consulta.suc_origen");
		InversionesPagareConsultaSucDestino = properties.getProperty("op.inversiones_pagare_consulta.suc_destino");
		InversionesPagareConsultaModulo = properties.getProperty("op.inversiones_pagare_consulta.modulo");

		InversionesObtenerInvMoneda = properties.getProperty("op.inversiones_obtener.inv_moneda");
		InversionesObtenerUsuario = properties.getProperty("op.inversiones_obtener.usuario");
		InversionesObtenerSucOrigen = properties.getProperty("op.inversiones_obtener.suc_origen");
		InversionesObtenerSucDestino = properties.getProperty("op.inversiones_obtener.suc_destino");
		InversionesObtenerModulo = properties.getProperty("op.inversiones_obtener.modulo");

		DataServiceHost = properties.getProperty("data_service.host");

		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");

		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("bitacora_servicio.op.inversiones_pagare_numero_usuario_obtener");
		InversionesObtenerOp = properties.getProperty("bitacora_servicio.op.inversiones_obtener");
	}

	@Path("{invNumero}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response detalleInversion(@PathParam("invNumero") String invNumero,
			@QueryParam("categoria") String categoria) {
		logger.info("CTRL: Empezando detalleInversion Method...");

		// DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd");

		SimpleDateFormat simpleDateFormatSis = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormatSis.format(fecha);

		JsonObject datosTransaccion = new JsonObject();
		datosTransaccion.addProperty("Num_Transa", "");
		datosTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarSucOrigen);

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

		String BitacoraCreacionFolTransa = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();

		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", "001844");
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", Integer.parseInt(BitacoraCreacionBitMonto));
		datosBitacora.addProperty("Bit_PriRef", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", "172.30.20.130");
		datosBitacora.addProperty("NumTransac", BitacoraCreacionFolTransa);
		datosBitacora.addProperty("Transaccio", "HOR");
		datosBitacora.addProperty("Usuario", BitacoraCreacionUsuario);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionModulo);

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
		StringBuilder inversionConsultarUrl = new StringBuilder().append(DataServiceHost);

		if (categoria == "PAGARE") {
			datosInversion.addProperty("Inv_Numero", "");
			datosInversion.addProperty("Inv_Usuari", "000024");
			datosInversion.addProperty("Tip_Consul", InversionesPagareConsultaTipConsul);
			datosInversion.addProperty("NumTransac", BitacoraCreacionFolTransa);
			datosInversion.addProperty("Transaccio", "HWE");
			datosInversion.addProperty("Usuario", InversionesPagareConsultaUsuario);
			datosInversion.addProperty("FechaSis", fechaSis);
			datosInversion.addProperty("SucOrigen", InversionesPagareConsultaSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesPagareConsultaSucDestino);
			datosInversion.addProperty("Modulo", InversionesPagareConsultaModulo);

			inversionConsultarUrl
					.append("/")
					.append(InversionesServicio)
					.append("/")
					.append(InversionesPagareNumeroUsuarioObtenerOp);
		} else {
			datosInversion.addProperty("Inv_Client", "00193500");
			datosInversion.addProperty("Inv_Moneda", InversionesObtenerInvMoneda);
			datosInversion.addProperty("NumTransac", BitacoraCreacionFolTransa);
			datosInversion.addProperty("Transaccio", "HWE");
			datosInversion.addProperty("Usuario", InversionesObtenerUsuario);
			datosInversion.addProperty("FechaSis", fechaSis);
			datosInversion.addProperty("SucOrigen", InversionesObtenerSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesObtenerSucDestino);
			datosInversion.addProperty("Modulo", InversionesObtenerModulo);

			inversionConsultarUrl
					.append("/")
					.append(InversionesServicio)
					.append("/")
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
		JsonObject inversionConsultarOpResultadoObjeto = new Gson().fromJson(inversionConsultarOpResultado,
				JsonObject.class);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);

		JsonArray inversionesArreglo = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray();

		JsonObject resultado = null;
		for (JsonElement invElemento : inversionesArreglo) {
			JsonObject inversionObj = invElemento.getAsJsonObject();
			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero)
				&& inversionObj.get("Fot_Descri").getAsString().equals(categoria)
				&& (inversionObj.has("Inv_Tipo") && inversionObj.get("Inv_Tipo").getAsString().equals("V"))) {
					
				String invFecIni = inversionObj.get("Inv_FecIni").getAsString();
				String invFecVen = inversionObj.get("Inv_FecVen").getAsString();
				int plazo = 0;
				double intBru = 0;
				double invIntNet = 0;
				double invISRTot = 0;

				// if (inversionObj.has("Inv_Tipo") && inversionObj.get("Inv_Tipo").getAsString() != "V")

				if (categoria == "PAGARE") {
					plazo = inversionObj.get("Inv_Plazo").getAsInt();
					intBru = inversionObj.get("Inv_TBruta").getAsDouble();
					invIntNet = inversionObj.get("Imp_Intere").getAsDouble();
					invISRTot = inversionObj.get("Inv_ISR").getAsDouble();
				} else {
					plazo = inversionObj.get("Plazo").getAsInt();
					double amoTasa = inversionObj.get("Amo_Tasa").getAsDouble();
					double amoISR = inversionObj.get("Amo_ISR").getAsDouble();
					intBru = amoTasa + amoISR / 10;
					invIntNet = inversionObj.get("Inv_IntNet").getAsDouble();
					invISRTot = inversionObj.get("Inv_ISRTot").getAsDouble();
				}

				Date fechaIni = null;
				Date fechaVen = null;

				try {
					SimpleDateFormat simpleDateFormatFechaBase = new SimpleDateFormat("dd/MM/yyyy");
					fechaIni = simpleDateFormatFechaBase.parse(invFecIni);
					
				} catch (ParseException e) {
					logger.info("formato de fecha no valido.");
					try {
						SimpleDateFormat simpleDateFormatFechaBase = new SimpleDateFormat("dd-MM-yyyy");
						fechaIni = simpleDateFormatFechaBase.parse(invFecIni);
					} catch (ParseException ei) {
						logger.info("formato de fecha no valido.");
					}
				}
				
				try {
					SimpleDateFormat simpleDateFormatFechaBase = new SimpleDateFormat("dd/MM/yyyy");
					fechaVen = simpleDateFormatFechaBase.parse(invFecVen);
				} catch (ParseException e) {
					logger.info("formato de fecha no valido.");
					try {
						SimpleDateFormat simpleDateFormatFechaBase = new SimpleDateFormat("dd-MM-yyyy");
						fechaVen = simpleDateFormatFechaBase.parse(invFecVen);
					} catch (ParseException ei) {
						logger.info("formato de fecha no valido.");
					}
				}

				logger.info(">>>>>>> invFecIni: " + invFecIni);
				logger.info(">>>>>>> fechaIni: " + fechaIni);
				logger.info(">>>>>>> fechaVen: " + fechaVen);
				logger.info(">>>>>>> invFecVen: " + invFecVen);

				// DateTime dtFecIni = null;
				// DateTime dtFecVen = null;
				
				// if(!invFecIni.contains("Proximo Vencimiento") && !invFecIni.isEmpty()) {
				// 	invFecIni = Utilerias.convertirFechaAFormatoSimple(invFecIni);
				// 	dtFecIni = DateTime.parse(invFecIni);
				// }
					
				// if(fechaVen != null) {
				// 	invFecVen = Utilerias.convertirFechaAFormatoSimple(invFecVen);
				// 	dtFecVen = DateTime.parse(invFecVen);
				// }

				intBru = Utilerias.redondear(intBru, 2);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");

				logger.info(">>>>>>> inversionObj: " + inversionObj);

				resultado = new JsonObject();
				JsonObject inversion = new JsonObject();
				inversion.addProperty("invFecIni", fechaIni != null ? simpleDateFormat.format(fechaIni) : invFecIni.trim());
				inversion.addProperty("invFecVen", fechaVen != null ? simpleDateFormat.format(fechaVen) : invFecVen.trim());
				inversion.addProperty("invCuenta", inversionObj.has("Inv_Cuenta") ? inversionObj.get("Inv_Cuenta").getAsString() : "");
				inversion.addProperty("invGat", inversionObj.has("Inv_Gat") ? inversionObj.get("Inv_Gat").getAsDouble() : null);
				inversion.addProperty("invGatRea", inversionObj.has("Inv_GatRea") ? inversionObj.get("Inv_GatRea").getAsDouble() : null);
				inversion.addProperty("plazo", plazo);
				inversion.addProperty("intBru", intBru);
				inversion.addProperty("invIntNet", invIntNet);
				inversion.addProperty("invISRTot", invISRTot);
				inversion.addProperty("invTotal", inversionObj.has("Inv_Total") ? inversionObj.get("Inv_Total").getAsDouble() : null);
				inversion.addProperty("cpRenInv", Utilerias.calcularVencimiento(fechaVen));
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

		return Response
			.status(Response.Status.OK)
			.header("Content-Type", MediaType.APPLICATION_JSON)
			.entity(resultado)
			.build();

    	// logger.info("CTRL: Empezando detalleInversion method...");
    	// String body = HttpClientUtils.getStringContent(request);
    	// logger.info("vo: " + body);
    	
    	// JsonObject jsonObj = new Gson().fromJson(body, JsonObject.class);
    	// JsonObject inversion = jsonObj.get("inversion").getAsJsonObject();
    	// String invFecIni = inversion.get("invFecIni").getAsString();
    	// String invFecVen = inversion.get("invFecVen").getAsString();
    	// double intBru = inversion.get("intBru").getAsDouble();
    	
    	// intBru = Utilerias.redondear(intBru, 2);
    	// invFecIni = Utilerias.convertirFechaAFormatoSimple(invFecIni);
    	// invFecVen = Utilerias.convertirFechaAFormatoSimple(invFecVen);
    	// DateTime dtFecIni = DateTime.parse(invFecIni);
    	// DateTime dtFecVen = DateTime.parse(invFecVen);
		// inversion.addProperty("cpRenInv", Utilerias.calcularVencimiento(invFecVen));
    	
    	// inversion.addProperty("intBru", intBru);
    	// inversion.addProperty("invFecIni", dtfOut.print(dtFecIni));
    	// inversion.addProperty("invFecVen", dtfOut.print(dtFecVen));
    	
    	// logger.info("CTRL: Terminando detalleInversion method...");
    	// return Response
    	// 		.status(Response.Status.OK)
    	// 		.header("Content-Type", MediaType.APPLICATION_JSON)
    	// 		.entity(jsonObj)
    	// 		.build();
    }

}
