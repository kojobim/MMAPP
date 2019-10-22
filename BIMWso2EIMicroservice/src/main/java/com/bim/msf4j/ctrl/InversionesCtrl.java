package com.bim.msf4j.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.utils.Filtrado;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

@Path("/inversiones")
public class InversionesCtrl implements Microservice {
	
	private static final Logger logger = Logger.getLogger(InversionesCtrl.class);
	
	private static Properties properties;
	private static String DataServiceHost;
	
	private static String TransaccionServicio;
	private static String BitacoraServicio;
	private static String InversionesServicio;
	private static String ClienteServicio;
	private static String CuentaServicio;
	private static String UsuarioServicio;
	private static String ReinversionServicio;
	private static String TasaServicio;
	private static String TokenServicio;

	private static String FolioTransaccionGenerarOp;
	private static String BitacoraCreacionOp;
	private static String InversionesObtenerOp;
	private static String InversionesPagareNumeroUsuarioObtenerOp;
	private static String ClienteConsultarOp;
	private static String CuentaOrigenConsultarOp;
	private static String UsuarioPerfilRiesgoConsultarOp;
	private static String fechaHabilConsultarOp;
	private static String tasaClienteConsultarOp;
	private static String tasaMonedaConsultarOp;
	private static String tasaGATConsultaCalcularOp;
	private static String intentosActualizacionOp;
	private static String inversionesStatusActualizarOp;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOp;

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
	private static String ClienteConsultarOpTipConsul;
	private static String ClienteConsultarOpTransaccio;
	private static String ClienteConsultarOpUsuari;
	private static String ClienteConsultarOpSucOrigen;
	private static String ClienteConsultarOpSucDestino;
	private static String ClienteConsultarOpModulo;	
	private static String CuentaOrigenConsultarOpTipConsul;
	private static String CuentaOrigenConsultarOpTransaccio;
	private static String CuentaOrigenConsultarOpUsuari;
	private static String CuentaOrigenConsultarOpSucOrigen;
	private static String CuentaOrigenConsultarOpSucDestino;
	private static String CuentaOrigenConsultarOpModulo;
	private static String UsuarioPerfilRiesgoConsultarOpTipConsul;
	private static String UsuarioPerfilRiesgoConsultarOpTransaccio;
	private static String UsuarioPerfilRiesgoConsultarOpUsuari;
	private static String UsuarioPerfilRiesgoConsultarOpSucOrigen;
	private static String UsuarioPerfilRiesgoConsultarOpSucDestino;
	private static String UsuarioPerfilRiesgoConsultarOpModulo;	
	private static String fechaHabilConsultarOpFinSem;
	private static String fechaHabilConsultarOpTransaccio;
	private static String fechaHabilConsultarOpUsuari;
	private static String fechaHabilConsultarOpSucOrigen;
	private static String fechaHabilConsultarOpSucDestino;
	private static String fechaHabilConsultarOpModulo;
	private static String tasaClienteConsultarOpTransaccio;
	private static String tasaClienteConsultarOpUsuari;
	private static String tasaClienteConsultarOpSucOrigen;
	private static String tasaClienteConsultarOpSucDestino;
	private static String tasaClienteConsultarOpModulo;	
	private static String tasaClienteConsultarOpInvMoneda;
	private static String tasaMonedaConsultarOpTransaccio;
	private static String tasaMonedaConsultarOpUsuari;
	private static String tasaMonedaConsultarOpSucOrigen;
	private static String tasaMonedaConsultarOpSucDestino;
	private static String tasaMonedaConsultarOpModulo;
	private static String tasaMonedaConsultarOpMonNumero;
	private static String tasaMonedaConsultarOpMonFecha;
	private static String tasaGATConsultaCalcularOpTransaccio;
	private static String tasaGATConsultaCalcularOpUsuari;
	private static String tasaGATConsultaCalcularOpSucOrigen;
	private static String tasaGATConsultaCalcularOpSucDestino;
	private static String tasaGATConsultaCalcularOpModulo;	
	private static String intentosActualizacionOpTransaccio;
	private static String intentosActualizacionOpUsuari;
	private static String intentosActualizacionOpSucOrigen;
	private static String intentosActualizacionOpSucDestino;
	private static String intentosActualizacionOpModulo;	
	private static String inversionesStatusActualizarOpTransaccio;
	private static String inversionesStatusActualizarOpUsuari;
	private static String inversionesStatusActualizarOpSucOrigen;
	private static String inversionesStatusActualizarOpSucDestino;
	private static String inversionesStatusActualizarOpModulo;	
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpTransaccio;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpUsuari;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpSucOrigen;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpSucDestino;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpModulo;

	public InversionesCtrl() {
		logger.info("Ctrl: Empezando metodo init...");
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		
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

		ClienteConsultarOpTipConsul = properties.getProperty("op.tasa_cliente_consultar.tip_consul");
		ClienteConsultarOpTransaccio  = properties.getProperty("op.tasa_cliente_consultar.transaccio");
		ClienteConsultarOpUsuari = properties.getProperty("op.tasa_cliente_consultar.usuario");
		ClienteConsultarOpSucOrigen = properties.getProperty("op.tasa_cliente_consultar.suc_origen");
		ClienteConsultarOpSucDestino = properties.getProperty("op.tasa_cliente_consultar.suc_destino");
		ClienteConsultarOpModulo = properties.getProperty("op.tasa_cliente_consultar.modulo");

		CuentaOrigenConsultarOpTipConsul = properties.getProperty("op.cuenta_origen_consultar.tip_consul");
		CuentaOrigenConsultarOpTransaccio  = properties.getProperty("op.cuenta_origen_consultar.transaccio");
		CuentaOrigenConsultarOpUsuari = properties.getProperty("op.cuenta_origen_consultar.usuario");
		CuentaOrigenConsultarOpSucOrigen = properties.getProperty("op.cuenta_origen_consultar.suc_origen");
		CuentaOrigenConsultarOpSucDestino = properties.getProperty("op.cuenta_origen_consultar.suc_destino");
		CuentaOrigenConsultarOpModulo = properties.getProperty("op.cuenta_origen_consultar.modulo");

		UsuarioPerfilRiesgoConsultarOpTipConsul = properties.getProperty("op.usuario_perfil_riesgo_consultar.tip_consul");
		UsuarioPerfilRiesgoConsultarOpTransaccio  = properties.getProperty("op.usuario_perfil_riesgo_consultar.transaccio");
		UsuarioPerfilRiesgoConsultarOpUsuari = properties.getProperty("op.usuario_perfil_riesgo_consultar.usuario");
		UsuarioPerfilRiesgoConsultarOpSucOrigen = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_origen");
		UsuarioPerfilRiesgoConsultarOpSucDestino = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_destino");
		UsuarioPerfilRiesgoConsultarOpModulo = properties.getProperty("op.usuario_perfil_riesgo_consultar.modulo");

		fechaHabilConsultarOpFinSem  = properties.getProperty("op.fecha_habil_consultar.fin_sem");
		fechaHabilConsultarOpTransaccio  = properties.getProperty("op.fecha_habil_consultar.transaccio");
		fechaHabilConsultarOpUsuari = properties.getProperty("op.fecha_habil_consultar.usuario");
		fechaHabilConsultarOpSucOrigen = properties.getProperty("op.fecha_habil_consultar.suc_origen");
		fechaHabilConsultarOpSucDestino = properties.getProperty("op.fecha_habil_consultar.suc_destino");
		fechaHabilConsultarOpModulo = properties.getProperty("op.fecha_habil_consultar.modulo");

		tasaClienteConsultarOpTransaccio  = properties.getProperty("op.tasa_cliente_consultar.transaccio");
		tasaClienteConsultarOpUsuari = properties.getProperty("op.tasa_cliente_consultar.usuario");
		tasaClienteConsultarOpSucOrigen = properties.getProperty("op.tasa_cliente_consultar.suc_origen");
		tasaClienteConsultarOpSucDestino = properties.getProperty("op.tasa_cliente_consultar.suc_destino");
		tasaClienteConsultarOpModulo = properties.getProperty("op.tasa_cliente_consultar.modulo");		
		tasaClienteConsultarOpInvMoneda = properties.getProperty("op.tasa_cliente_consultar.inv_moneda");
		
		tasaMonedaConsultarOpTransaccio  = properties.getProperty("op.tasa_moneda_consultar.transaccio");
		tasaMonedaConsultarOpUsuari = properties.getProperty("op.tasa_moneda_consultar.usuario");
		tasaMonedaConsultarOpSucOrigen = properties.getProperty("op.tasa_moneda_consultar.suc_origen");
		tasaMonedaConsultarOpSucDestino = properties.getProperty("op.tasa_moneda_consultar.suc_destino");
		tasaMonedaConsultarOpModulo = properties.getProperty("op.tasa_moneda_consultar.modulo");		
		tasaMonedaConsultarOpMonNumero = properties.getProperty("op.tasa_moneda_consultar.mon_numero");
		tasaMonedaConsultarOpMonFecha = properties.getProperty("op.tasa_moneda_consultar.mon_fecha");
		
		tasaGATConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_consulta_calcular.transaccio");
		tasaGATConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_consulta_calcular.usuario");
		tasaGATConsultaCalcularOpSucOrigen = properties.getProperty("op.gat_consulta_calcular.suc_origen");
		tasaGATConsultaCalcularOpSucDestino = properties.getProperty("op.gat_consulta_calcular.suc_destino");
		tasaGATConsultaCalcularOpModulo = properties.getProperty("op.gat_consulta_calcular.modulo");	
		
		tasaGATConsultaCalcularOpTransaccio  = properties.getProperty("op.intentos_actualizacion.transaccio");
		tasaGATConsultaCalcularOpUsuari = properties.getProperty("op.intentos_actualizacion.usuario");
		tasaGATConsultaCalcularOpSucOrigen = properties.getProperty("op.intentos_actualizacion.suc_origen");
		tasaGATConsultaCalcularOpSucDestino = properties.getProperty("op.intentos_actualizacion.suc_destino");
		tasaGATConsultaCalcularOpModulo = properties.getProperty("op.intentos_actualizacion.modulo");
		
		inversionesStatusActualizarOpTransaccio  = properties.getProperty("op.inversiones-status_actualizar.transaccio");
		inversionesStatusActualizarOpUsuari = properties.getProperty("op.inversiones-status_actualizar.usuario");
		inversionesStatusActualizarOpSucOrigen = properties.getProperty("op.inversiones-status_actualizar.suc_origen");
		inversionesStatusActualizarOpSucDestino = properties.getProperty("op.inversiones-status_actualizar.suc_destino");
		inversionesStatusActualizarOpModulo = properties.getProperty("op.inversiones-status_actualizar.modulo");

		inversionesImportesDeInvercionFinalizadaActualizarOpTransaccio  = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.transaccio");
		inversionesImportesDeInvercionFinalizadaActualizarOpUsuari = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.usuario");
		inversionesImportesDeInvercionFinalizadaActualizarOpSucOrigen = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.suc_origen");
		inversionesImportesDeInvercionFinalizadaActualizarOpSucDestino = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.suc_destino");
		inversionesImportesDeInvercionFinalizadaActualizarOpModulo = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.modulo");
		
		DataServiceHost = properties.getProperty("data_service.host");
		
		logger.info("DataServiceHost" + DataServiceHost);
		
		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");
		ClienteServicio= properties.getProperty("data_service.cliente_servicio");
		CuentaServicio= properties.getProperty("data_service.cuenta_servicio");
		UsuarioServicio= properties.getProperty("data_service.usuario_servicio");
		ReinversionServicio= properties.getProperty("data_service.reinversion_servicio");
		TasaServicio= properties.getProperty("data_service.tasa_servicio");
		TokenServicio= properties.getProperty("data_service.token_servicio");
		
		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
		InversionesObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_obtener");
		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_numero_usuario_obtener");
		ClienteConsultarOp = properties.getProperty("cliente_servicio.op.cliente_consultar");
		CuentaOrigenConsultarOp = properties.getProperty("cuenta_servicio.op.cuenta_origen_consultar");
		UsuarioPerfilRiesgoConsultarOp = properties.getProperty("usuario_servicio.op.usuario_perfil_riesgo_consultar");
		fechaHabilConsultarOp = properties.getProperty("reinversion_servicio.op.fecha_habil_consultar");
		tasaClienteConsultarOp = properties.getProperty("tasa_servicio.op.tasa_cliente_consultar");
		tasaMonedaConsultarOp = properties.getProperty("tasa_servicio.op.tasa_moneda_consultar");
		tasaGATConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_consulta_calcular");
		intentosActualizacionOp = properties.getProperty("token_servicio.op.intentos_actualizacion");
		inversionesStatusActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_status_actualizar");
		inversionesImportesDeInvercionFinalizadaActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_importes_de_invercion_finalizada_actualizar");

		logger.info("Ctrl: Terminando metodo init...");
	}
	
	@Path("/")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject inversionesListado(@QueryParam("page") int page, @QueryParam("per_page") int per_page, @QueryParam("filter_by") String filter_by, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando inversionesListado metodo");
		
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
		String bit_PriRef = solicitud.getHeader("User-Agent");
		String bit_DireIP = solicitud.getHeader("X-Forwarded-For");
		
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
		
		inversionesResultado.forEach(inversionResultado -> {
			JsonObject inversionResultadoObjeto = (JsonObject) inversionResultado;
			String invCantid = inversionResultadoObjeto.get("Inv_Cantid").getAsString();
			Double invCantidRedondeado = Utilerias.redondear(Double.parseDouble(invCantid), 3);
			inversionResultadoObjeto.addProperty("Inv_Cantid", invCantidRedondeado.toString());
		});
		logger.info("inversionesResultado " +  inversionesResultado);
		JsonObject inversionesResultadoFinal = Filtrado.filtroInversiones(inversionesResultado, page, per_page, filter_by);
		
		logger.info("CTRL: Terminando login metodo");	
		return inversionesResultadoFinal;
	}

	@Path("{invNumero}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject detalleInversion(@PathParam("invNumero") String invNumero,
			@QueryParam("categoria") String categoria, @Context final Request solicitud) {
		logger.info("CTRL: Empezando detalleInversion Method...");
		logger.info("@PathVariable >>>>> invNumero" + invNumero);
		SimpleDateFormat simpleDateFormatSis = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormatSis.format(fecha);

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

		String BitacoraCreacionFolTransa = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();

		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_PriRef = solicitud.getHeader("User-Agent");
		String bit_DireIP = solicitud.getHeader("X-Forwarded-For");

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
		datosBitacora.addProperty("Bit_Monto", Integer.parseInt(BitacoraCreacionOpBitMonto));
		datosBitacora.addProperty("Bit_PriRef", bit_PriRef);
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", bit_DireIP);
		datosBitacora.addProperty("NumTransac", BitacoraCreacionFolTransa);
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
			datosInversion.addProperty("NumTransac", BitacoraCreacionFolTransa);
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
			datosInversion.addProperty("NumTransac", BitacoraCreacionFolTransa);
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
		JsonObject inversionConsultarOpResultadoObjeto = new Gson().fromJson(inversionConsultarOpResultado,
				JsonObject.class);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);

		JsonObject inversionesObjecto = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject();
		JsonArray inversionesArreglo = inversionesObjecto.has("inversion") ? inversionesObjecto.get("inversion").getAsJsonArray() : new JsonArray();

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

		return resultado;
	}

	@Path("{invNumero}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject reinversion(@PathParam("invNumero") String invNumero,	
			@QueryParam("categoria") String categoria, @Context final Request solicitud) {
			logger.info("CTRL: Comenzando reinversion metodo");
			String mensaje = HttpClientUtils.getStringContent(solicitud);
			JsonObject inversionesObjeto = new Gson().fromJson(mensaje, JsonObject.class);
			JsonObject renovarInversion = inversionesObjeto.getAsJsonObject("renovarInversion");		
	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		JsonObject resultado = null;

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
		
		/* 
			Parametros obtenidos por medio del principal 
				Inv_Usuari = usuNumero
		*/
		
		String usuNumero = "000024";

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
			datosInversion.addProperty("NumTransac", numTransac);
			datosInversion.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
			datosInversion.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);			
			datosInversion.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
			datosInversion.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);

			inversionConsultarUrl
					.append(InversionesPagareNumeroUsuarioObtenerOp);
		} else {
			resultado = new JsonObject();
			JsonObject Error = new JsonObject();
			Error.addProperty("Err_Codigo", 400);
			Error.addProperty("Err_Mensaj", "Actualmente solo se pueden hacer renovaciones de tipo PAGARE");
			resultado.add("Error", Error);

			return resultado;
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

		JsonObject inversionesObjecto = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject();
		JsonArray inversionesArreglo = inversionesObjecto.has("inversion") ? inversionesObjecto.get("inversion").getAsJsonArray() : new JsonArray();
		
		JsonObject inversion = null;

		for (JsonElement invElemento : inversionesArreglo) {
			JsonObject inversionObj = invElemento.getAsJsonObject();
			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero)) {
				inversion = inversionObj;
			}
		}

		if(inversion == null) {
			resultado = new JsonObject();
			JsonObject Error = new JsonObject();
			Error.addProperty("Err_Codigo", 409);
			Error.addProperty("Err_Mensaj", "Inversion no encontrada");
			resultado.add("Error", Error);

			return resultado;
		}

		logger.info("INVERSION" + inversion);

		/* 
			Parametros obtenidos por medio del principal
				Cli_Numero = Usu_Client validar viene se sp1
		*/
	
		String usuClient = "00193500";
		
		JsonObject datosCliente = new JsonObject();
		datosCliente.addProperty("Cli_Numero", usuClient);
		datosCliente.addProperty("Cli_Sucurs", "");
		datosCliente.addProperty("Cli_Nombre", "");
		datosCliente.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosCliente.addProperty("NumTransac", numTransac);
		datosCliente.addProperty("Transaccio", ClienteConsultarOpTransaccio);
		datosCliente.addProperty("Usuario", ClienteConsultarOpUsuari);
		datosCliente.addProperty("FechaSis", fechaSis);
		datosCliente.addProperty("SucOrigen", ClienteConsultarOpSucOrigen);
		datosCliente.addProperty("SucDestino", ClienteConsultarOpSucDestino);
		datosCliente.addProperty("Modulo", ClienteConsultarOpModulo);
		
		logger.info("datosCliente" + datosCliente);
		StringBuilder clienteConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(ClienteServicio)
				.append("/")
				.append(ClienteConsultarOp);

		JsonObject clienteConsultarOp = new JsonObject();
		clienteConsultarOp.add("clienteConsultarOp", datosCliente);
		logger.info("clienteConsultarOp" + clienteConsultarOp);

		RequestDTO clienteConsultarOpSolicitud = new RequestDTO();
		clienteConsultarOpSolicitud.setUrl(clienteConsultarUrl.toString());
		MessageProxyDTO clienteConsultarOpMensaje= new MessageProxyDTO();
		clienteConsultarOpMensaje.setBody(clienteConsultarOp.toString());
		clienteConsultarOpSolicitud.setMessage(clienteConsultarOpMensaje);

		String clienteConsultarOpResultado = HttpClientUtils.postPerform(clienteConsultarOpSolicitud);
		JsonObject clienteConsultarOpResultadoObjeto = new Gson().fromJson(clienteConsultarOpResultado, JsonObject.class);
		logger.info("clienteConsultarOpResultadoObjeto" + clienteConsultarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
				Cor_Usuari = ver de donde se obtiene
		*/
	
		String corUsuari = "001844";

		JsonObject datosCuenta = new JsonObject();
		datosCuenta.addProperty("Cor_Usuari", corUsuari);
		datosCuenta.addProperty("Cor_Cuenta", "");
		datosCuenta.addProperty("Cor_Moneda", "");
		datosCuenta.addProperty("Cor_CliUsu", "");
		datosCuenta.addProperty("Usu_SucMod", "");
		datosCuenta.addProperty("Tip_Consul", CuentaOrigenConsultarOpTipConsul);
		datosCuenta.addProperty("NumTransac", numTransac);
		datosCuenta.addProperty("Transaccio", CuentaOrigenConsultarOpTransaccio);
		datosCuenta.addProperty("Usuario", CuentaOrigenConsultarOpUsuari);
		datosCuenta.addProperty("FechaSis", fechaSis);
		datosCuenta.addProperty("SucOrigen", CuentaOrigenConsultarOpSucOrigen);
		datosCuenta.addProperty("SucDestino", CuentaOrigenConsultarOpSucDestino);
		datosCuenta.addProperty("Modulo", CuentaOrigenConsultarOpModulo);

		logger.info("datosCuenta" + datosCuenta);
		StringBuilder cuentaOrigenConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(CuentaServicio)
				.append("/")
				.append(CuentaOrigenConsultarOp);

		JsonObject cuentaOrigenConsultarOp = new JsonObject();
		cuentaOrigenConsultarOp.add("cuentaOrigenConsultarOp", datosCuenta);
		logger.info("cuentaOrigenConsultarOp" + cuentaOrigenConsultarOp);

		RequestDTO cuentaOrigenConsultarOpSolicitud = new RequestDTO();
		cuentaOrigenConsultarOpSolicitud.setUrl(cuentaOrigenConsultarUrl.toString());
		MessageProxyDTO cuentaOrigenConsultarOpMensaje= new MessageProxyDTO();
		cuentaOrigenConsultarOpMensaje.setBody(cuentaOrigenConsultarOp.toString());
		cuentaOrigenConsultarOpSolicitud.setMessage(cuentaOrigenConsultarOpMensaje);

		String cuentaOrigenConsultarOpResultado = HttpClientUtils.postPerform(cuentaOrigenConsultarOpSolicitud);
		JsonObject cuentaOrigenConsultarOpResultadoObjeto = new Gson().fromJson(cuentaOrigenConsultarOpResultado, JsonObject.class);
		logger.info("cuentaOrigenConsultarOpResultadoObjeto" + cuentaOrigenConsultarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
				Apl_Client =  Cli_Numero viene de sp3 que se obtiene de sp1 Usu_Client
				Apl_Cuesti =  ver de donde se optiene
 		*/
	
		int aplCuesti = 0;

		JsonObject datosPerfilRiesgo = new JsonObject();
		datosPerfilRiesgo.addProperty("Apl_Client", usuClient);
		datosPerfilRiesgo.addProperty("Apl_Cuesti", aplCuesti);
		datosPerfilRiesgo.addProperty("Tip_Consul", UsuarioPerfilRiesgoConsultarOpTipConsul);
		datosPerfilRiesgo.addProperty("NumTransac", numTransac);
		datosPerfilRiesgo.addProperty("Transaccio", UsuarioPerfilRiesgoConsultarOpTransaccio);
		datosPerfilRiesgo.addProperty("Usuario", UsuarioPerfilRiesgoConsultarOpUsuari);
		datosPerfilRiesgo.addProperty("FechaSis", fechaSis);
		datosPerfilRiesgo.addProperty("SucOrigen", UsuarioPerfilRiesgoConsultarOpSucOrigen);
		datosPerfilRiesgo.addProperty("SucDestino", UsuarioPerfilRiesgoConsultarOpSucDestino);
		datosPerfilRiesgo.addProperty("Modulo", UsuarioPerfilRiesgoConsultarOpModulo);

		logger.info("datosPerfilRiesgo" + datosPerfilRiesgo);
		StringBuilder usuarioPerfilRiesgoConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(UsuarioServicio)
				.append("/")
				.append(UsuarioPerfilRiesgoConsultarOp);

		JsonObject usuarioPerfilRiesgoConsultarOp = new JsonObject();
		usuarioPerfilRiesgoConsultarOp.add("usuarioPerfilRiesgoConsultarOp", datosPerfilRiesgo);
		logger.info("usuarioPerfilRiesgoConsultarOp" + usuarioPerfilRiesgoConsultarOp);

		RequestDTO usuarioPerfilRiesgoConsultarOpSolicitud = new RequestDTO();
		usuarioPerfilRiesgoConsultarOpSolicitud.setUrl(usuarioPerfilRiesgoConsultarUrl.toString());
		MessageProxyDTO usuarioPerfilRiesgoConsultarOpMensaje= new MessageProxyDTO();
		usuarioPerfilRiesgoConsultarOpMensaje.setBody(usuarioPerfilRiesgoConsultarOp.toString());
		usuarioPerfilRiesgoConsultarOpSolicitud.setMessage(usuarioPerfilRiesgoConsultarOpMensaje);

		String usuarioPerfilRiesgoConsultarOpResultado = HttpClientUtils.postPerform(usuarioPerfilRiesgoConsultarOpSolicitud);
		JsonObject usuarioPerfilRiesgoConsultarOpResultadoObjeto = new Gson().fromJson(usuarioPerfilRiesgoConsultarOpResultado, JsonObject.class);
		logger.info("usuarioPerfilRiesgoConsultarOpResultadoObjeto" + usuarioPerfilRiesgoConsultarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
				Fecha =  ver de donde se optiene
				NumDia=  ver de donde se optiene confirmar que sea plazo
				FinSem=  "N" fines de semana no se consideran dias habiles
		 */

		JsonObject datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", fechaSis);
		datosFechaHabil.addProperty("NumDia", inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsNumber() : 0);
		datosFechaHabil.addProperty("FinSem", fechaHabilConsultarOpFinSem);
		datosFechaHabil.addProperty("NumTransac", numTransac);
		datosFechaHabil.addProperty("Transaccio", fechaHabilConsultarOpTransaccio);
		datosFechaHabil.addProperty("Usuario", fechaHabilConsultarOpUsuari);
		datosFechaHabil.addProperty("FechaSis", fechaSis);
		datosFechaHabil.addProperty("SucOrigen", fechaHabilConsultarOpSucOrigen);
		datosFechaHabil.addProperty("SucDestino", fechaHabilConsultarOpSucDestino);
		datosFechaHabil.addProperty("Modulo", fechaHabilConsultarOpModulo);

		logger.info("datosFechaHabil" + datosFechaHabil);
		StringBuilder fechaHabilConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(ReinversionServicio)
				.append("/")
				.append(fechaHabilConsultarOp);

		JsonObject fechaHabilConsultarOp = new JsonObject();
		fechaHabilConsultarOp.add("fechaHabilConsultarOp", datosFechaHabil);
		logger.info("fechaHabilConsultarOp" + fechaHabilConsultarOp);

		RequestDTO fechaHabilConsultarOpSolicitud = new RequestDTO();
		fechaHabilConsultarOpSolicitud.setUrl(fechaHabilConsultarUrl.toString());
		MessageProxyDTO fechaHabilConsultarOpMensaje= new MessageProxyDTO();
		fechaHabilConsultarOpMensaje.setBody(fechaHabilConsultarOp.toString());
		fechaHabilConsultarOpSolicitud.setMessage(fechaHabilConsultarOpMensaje);

		String fechaHabilConsultarOpResultado = HttpClientUtils.postPerform(fechaHabilConsultarOpSolicitud);
		JsonObject fechaHabilConsultarOpResultadoObjeto = new Gson().fromJson(fechaHabilConsultarOpResultado, JsonObject.class);
		logger.info("fechaHabilConsultarOpResultadoObjeto" + fechaHabilConsultarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
				Cli_Numero viene de sp3 que se obtiene de sp1 Usu_Client
				Inv_Moneda=  ver de donde se optiene
				Cli_Tipo = 2 ver donde se obitene
				Ine_Numero= preguntar si es igual al Cli_ActINE del sp3
 		*/
		
		String cliTipo = "2";

		String invFecVen = inversion.has("Inv_FecVen") ? inversion.get("Inv_FecVen").getAsString() : "";
		Date fecVen = null;
		
		try {
			fecVen = simpleDateFormat.parse(invFecVen);	
		} catch (Exception e) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				fecVen = sdf.parse(invFecVen);
			} catch (Exception ex) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					fecVen = sdf.parse(invFecVen);
				} catch (Exception ei) {
					logger.info("Error en el formato de fecha.");
				}
			}
		}

		JsonObject datosTasaCliente = new JsonObject();
		datosTasaCliente.addProperty("Cli_Numero", usuClient);
		datosTasaCliente.addProperty("Inv_Cantid", inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0);
		datosTasaCliente.addProperty("Inv_Moneda", tasaClienteConsultarOpInvMoneda);
		datosTasaCliente.addProperty("Cli_Tipo", cliTipo);
		datosTasaCliente.addProperty("Plazo", inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsNumber() : 0);
		datosTasaCliente.addProperty("Inv_FecVen", fecVen != null ? simpleDateFormat.format(fecVen) : "");
		datosTasaCliente.addProperty("Ine_Numero", "");
		datosTasaCliente.addProperty("Tasa", inversion.has("Inv_tasa") ? inversion.get("Inv_Tasa").getAsDouble() : 0);
		datosTasaCliente.addProperty("Inv_GruTas", "");
		datosTasaCliente.addProperty("Inv_NuPoGr", "");
		datosTasaCliente.addProperty("NumTransac", numTransac);
		datosTasaCliente.addProperty("Transaccio", tasaClienteConsultarOpTransaccio);
		datosTasaCliente.addProperty("Usuario", tasaClienteConsultarOpUsuari);
		datosTasaCliente.addProperty("FechaSis", fechaSis);
		datosTasaCliente.addProperty("SucOrigen", tasaClienteConsultarOpSucOrigen);
		datosTasaCliente.addProperty("SucDestino", tasaClienteConsultarOpSucDestino);
		datosTasaCliente.addProperty("Modulo", tasaClienteConsultarOpModulo);

		logger.info("datosTasaCliente" + datosTasaCliente);
		StringBuilder tasaClienteConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TasaServicio)
				.append("/")
				.append(tasaClienteConsultarOp);

		JsonObject tasaClienteConsultarOp = new JsonObject();
		tasaClienteConsultarOp.add("tasaClienteConsultarOp", datosTasaCliente);
		logger.info("tasaClienteConsultarOp" + tasaClienteConsultarOp);

		RequestDTO tasaClienteConsultarOpSolicitud = new RequestDTO();
		tasaClienteConsultarOpSolicitud.setUrl(tasaClienteConsultarUrl.toString());
		MessageProxyDTO tasaClienteConsultarOpMensaje= new MessageProxyDTO();
		tasaClienteConsultarOpMensaje.setBody(tasaClienteConsultarOp.toString());
		tasaClienteConsultarOpSolicitud.setMessage(tasaClienteConsultarOpMensaje);

		String tasaClienteConsultarOpResultado = HttpClientUtils.postPerform(tasaClienteConsultarOpSolicitud);
		JsonObject tasaClienteConsultarOpResultadoObjeto = new Gson().fromJson(tasaClienteConsultarOpResultado, JsonObject.class);
		logger.info("tasaClienteConsultarOpResultadoObjeto" + tasaClienteConsultarOpResultadoObjeto);
		
		/* 
			Parametros obtenidos por medio del principal
			Mon_Numero = Inv_Moneda? o ver de donde se obiene si es constante
			Mon_fecha = que fecha se utiliza
 		*/

		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("Mon_Numero", tasaMonedaConsultarOpMonNumero);
		datosMoneda.addProperty("Mon_Descri", "");
		datosMoneda.addProperty("Mon_Fecha", tasaMonedaConsultarOpMonFecha);
		datosMoneda.addProperty("Tip_Consul", "");
		datosMoneda.addProperty("NumTransac", numTransac);
		datosMoneda.addProperty("Transaccio", tasaMonedaConsultarOpTransaccio);
		datosMoneda.addProperty("Usuario", tasaMonedaConsultarOpUsuari);
		datosMoneda.addProperty("FechaSis", fechaSis);
		datosMoneda.addProperty("SucOrigen", tasaMonedaConsultarOpSucOrigen);
		datosMoneda.addProperty("SucDestino", tasaMonedaConsultarOpSucDestino);
		datosMoneda.addProperty("Modulo", tasaMonedaConsultarOpModulo);

		logger.info("datosMoneda" + datosMoneda);
		StringBuilder tasaMonedaConsultarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TasaServicio)
				.append("/")
				.append(tasaMonedaConsultarOp);
		JsonObject tasaMonedaConsultarOp = new JsonObject();
		tasaMonedaConsultarOp.add("tasaMonedaConsultarOp", datosMoneda);
		logger.info("tasaMonedaConsultarOp" + tasaMonedaConsultarOp);

		RequestDTO tasaMonedaConsultarOpSolicitud = new RequestDTO();
		tasaMonedaConsultarOpSolicitud.setUrl(tasaMonedaConsultarUrl.toString());
		MessageProxyDTO tasaMonedaConsultarOpMensaje= new MessageProxyDTO();
		tasaMonedaConsultarOpMensaje.setBody(tasaMonedaConsultarOp.toString());
		tasaMonedaConsultarOpSolicitud.setMessage(tasaMonedaConsultarOpMensaje);

		String tasaMonedaConsultarOpResultado = HttpClientUtils.postPerform(tasaMonedaConsultarOpSolicitud);
		JsonObject tasaMonedaConsultarOpResultadoObjeto = new Gson().fromJson(tasaMonedaConsultarOpResultado, JsonObject.class);
		logger.info("tasaMonedaConsultarOpResultadoObjeto" + tasaMonedaConsultarOpResultadoObjeto);
		
		/* 
			Parametros obtenidos por medio del principal
			Inv_Dias = inv_plazo? o ver de donde se obiene si es constante
			Inv_TasInt = 4.65 de donde se obtiene si es el Inv_Tasa de detalle de inversion
			Mon_Comisi = 0 de donde se obtiene
			cueMonInv = 509000 a Inv_cantidad de inversion
			monComisi = 0? donde se obtiene
		 */
		
		int cueMonInv = 509000;
		int monComisi = 0;

		JsonObject datosGAT = new JsonObject();
		datosGAT.addProperty("Inv_Dias",  inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsNumber() : 0);
		datosGAT.addProperty("Inv_TasInt", inversion.has("Inv_Tasa") ? inversion.get("Inv_Tasa").getAsDouble() : 0);
		datosGAT.addProperty("Inv_GAT", inversion.has("Inv_GAT") ? inversion.get("Inv_GAT").getAsDouble() : 0);
		datosGAT.addProperty("Cal_Opcion", "");
		datosGAT.addProperty("Cue_MonInv", cueMonInv);
		datosGAT.addProperty("Mon_Comisi", monComisi);
		datosGAT.addProperty("NumTransac", numTransac);
		datosGAT.addProperty("Transaccio", tasaGATConsultaCalcularOpTransaccio);
		datosGAT.addProperty("Usuario", tasaGATConsultaCalcularOpUsuari);
		datosGAT.addProperty("FechaSis", fechaSis);
		datosGAT.addProperty("SucOrigen", tasaGATConsultaCalcularOpSucOrigen);
		datosGAT.addProperty("SucDestino", tasaGATConsultaCalcularOpSucDestino);
		datosGAT.addProperty("Modulo", tasaGATConsultaCalcularOpModulo);

		logger.info("datosMoneda" + datosMoneda);
		StringBuilder tasaGATConsultaCalcularUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TasaServicio)
				.append("/")
				.append(tasaGATConsultaCalcularOp);
		JsonObject tasaGATConsultaCalcularOp = new JsonObject();
		tasaGATConsultaCalcularOp.add("tasaGATConsultaCalcularOp", datosGAT);
		logger.info("tasaGATConsultaCalcularOp" + tasaGATConsultaCalcularOp);

		RequestDTO tasaGATConsultaCalcularOpSolicitud = new RequestDTO();
		tasaGATConsultaCalcularOpSolicitud.setUrl(tasaGATConsultaCalcularUrl.toString());
		MessageProxyDTO tasaGATConsultaCalcularOpMensaje= new MessageProxyDTO();
		tasaGATConsultaCalcularOpMensaje.setBody(tasaGATConsultaCalcularOp.toString());
		tasaGATConsultaCalcularOpSolicitud.setMessage(tasaGATConsultaCalcularOpMensaje);

		String tasaGATConsultaCalcularOpResultado = HttpClientUtils.postPerform(tasaGATConsultaCalcularOpSolicitud);
		JsonObject tasaGATConsultaCalcularOpResultadoObjeto = new Gson().fromJson(tasaGATConsultaCalcularOpResultado, JsonObject.class);
		logger.info("tasaGATConsultaCalcularOpResultadoObjeto" + tasaGATConsultaCalcularOpResultadoObjeto);

		

		/*
			+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			verificar si aplica el sp 10 debido a que en el documento de reglas de engocio de paertura pagare
			en las secciones de reinvertir no se utiliza el GATRea
			+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		*/

		/* 
			*************************************************************************
			*************************************************************************
			Empiezan reglas de negocio
			*************************************************************************
			*************************************************************************
		 */




		
		/* 
			Parametros obtenidos por medio del principal
			Tok_Usuari = 001844 este numero es igual al de usuNumero de donde se obtiene
			Tip_Actual = 1 ver de donde se obtiene
		 */
		
		String tokUsuari = "001844";
		String tipActual = "1";

		JsonObject datosIntentosToken = new JsonObject();
		datosIntentosToken.addProperty("Tok_Folio",  renovarInversion.has("cpRSAToken") ? renovarInversion.get("cpRSAToken").getAsString() : "");
		datosIntentosToken.addProperty("Tok_UsuAdm", "");
		datosIntentosToken.addProperty("Tok_Usuari", tokUsuari);
		datosIntentosToken.addProperty("Tok_ComCan", "");
		datosIntentosToken.addProperty("Tip_Actual", tipActual);
		datosIntentosToken.addProperty("NumTransac", numTransac);
		datosIntentosToken.addProperty("Transaccio", intentosActualizacionOpTransaccio);
		datosIntentosToken.addProperty("Usuario", intentosActualizacionOpUsuari);
		datosIntentosToken.addProperty("FechaSis", fechaSis);
		datosIntentosToken.addProperty("SucOrigen", intentosActualizacionOpSucOrigen);
		datosIntentosToken.addProperty("SucDestino", intentosActualizacionOpSucDestino);
		datosIntentosToken.addProperty("Modulo", intentosActualizacionOpModulo);

		logger.info("datosIntentosToken" + datosIntentosToken);
		StringBuilder intentosActualizacionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TokenServicio)
				.append("/")
				.append(intentosActualizacionOp);
		JsonObject intentosActualizacionOp = new JsonObject();
		intentosActualizacionOp.add("intentosActualizacionOp", datosIntentosToken);
		logger.info("intentosActualizacionOp" + intentosActualizacionOp);

		RequestDTO intentosActualizacionOpSolicitud = new RequestDTO();
		intentosActualizacionOpSolicitud.setUrl(intentosActualizacionUrl.toString());
		MessageProxyDTO intentosActualizacionOpMensaje= new MessageProxyDTO();
		intentosActualizacionOpMensaje.setBody(intentosActualizacionOp.toString());
		intentosActualizacionOpSolicitud.setMessage(intentosActualizacionOpMensaje);

		String intentosActualizacionOpResultado = HttpClientUtils.postPerform(intentosActualizacionOpSolicitud);
		JsonObject intentosActualizacionOpResultadoObjeto = new Gson().fromJson(intentosActualizacionOpResultado, JsonObject.class);
		logger.info("intentosActualizacionOpResultadoObjeto" + intentosActualizacionOpResultadoObjeto);

		

		 

















		
		/* 
			Parametros obtenidos por medio del principal
			Adi_Invers = 001935000030001 si es el token que mandan el el requestbody o ver de donde se obiene si es constante
			Adi_InsLiq = 01 lo reguresa el SP 11,12 
			Adi_MoReGr = 0 este numero es igual al de usuNumero de donde se obtiene
			numtransac = 49646239
		 */
		
		String adiInvers = "001935000030001";
		String adiinsLiq = "01";
		int adimoReGr = 0;

		JsonObject datosStatusActualizar = new JsonObject();
		datosStatusActualizar.addProperty("Adi_Invers", adiInvers);
		datosStatusActualizar.addProperty("Adi_InsLiq", adiinsLiq);
		datosStatusActualizar.addProperty("Adi_MoReGr", adimoReGr);
		datosStatusActualizar.addProperty("NumTransac", numTransac);
		datosStatusActualizar.addProperty("Transaccio", inversionesStatusActualizarOpTransaccio);
		datosStatusActualizar.addProperty("Usuario", inversionesStatusActualizarOpUsuari);
		datosStatusActualizar.addProperty("FechaSis", fechaSis);
		datosStatusActualizar.addProperty("SucOrigen", inversionesStatusActualizarOpSucOrigen);
		datosStatusActualizar.addProperty("SucDestino", inversionesStatusActualizarOpSucDestino);
		datosStatusActualizar.addProperty("Modulo", inversionesStatusActualizarOpModulo);

		logger.info("datosStatusActualizar" + datosStatusActualizar);
		StringBuilder inversionesStatusActualizarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/")
				.append(inversionesStatusActualizarOp);
		JsonObject inversionesStatusActualizarOp = new JsonObject();
		inversionesStatusActualizarOp.add("inversionesStatusActualizarOp", datosStatusActualizar);
		logger.info("inversionesStatusActualizarOp" + inversionesStatusActualizarOp);

		RequestDTO inversionesStatusActualizarOpSolicitud = new RequestDTO();
		inversionesStatusActualizarOpSolicitud.setUrl(inversionesStatusActualizarUrl.toString());
		MessageProxyDTO inversionesStatusActualizarOpMensaje= new MessageProxyDTO();
		inversionesStatusActualizarOpMensaje.setBody(inversionesStatusActualizarOp.toString());
		inversionesStatusActualizarOpSolicitud.setMessage(inversionesStatusActualizarOpMensaje);

		String inversionesStatusActualizarOpResultado = HttpClientUtils.postPerform(inversionesStatusActualizarOpSolicitud);
		JsonObject inversionesStatusActualizarOpResultadoObjeto = new Gson().fromJson(inversionesStatusActualizarOpResultado, JsonObject.class);
		logger.info("inversionesStatusActualizarOpResultadoObjeto" + inversionesStatusActualizarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
			Inv_Deposi = si es Inv_Cantid
			Inv_rFecIn = fecha fin de vencimiento de invercion ant
			Inv_rFecVe = fecha de sp de siguente fecha habil
			Inv_rCanti = inv_cantidad o se calcula
			Inv_rAutor = si es igual al Usuario 

			Inv_rISR = ISR de inversion
			Inv_rCuent

			Adi_MoReGr = 0 este numero es igual al de usuNumero de donde se obtiene
			numtransac = 49646239
		 */
		
		// String adiInvers = "001935000030001";
		// String adiinsLiq = "01";
		// int adimoReGr = 0;

	


		JsonObject datosInversionFinalizada = new JsonObject();
		datosInversionFinalizada.addProperty("Inv_Numero", inversion.has("Inv_Numero") ? inversion.get("Inv_Numero").getAsString() : "");
		datosInversionFinalizada.addProperty("Inv_Deposi", inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0);
		datosInversionFinalizada.addProperty("Inv_rFecIn", inversion.has("Inv_FecVen") ? inversion.get("Inv_FecVen").getAsString() : "");
		datosInversionFinalizada.addProperty("Inv_rFecVe", fechaHabilConsultarOpResultadoObjeto.has("Fecha") ? fechaHabilConsultarOpResultadoObjeto.get("Fecha").getAsString() : "");
		datosInversionFinalizada.addProperty("Inv_rCanti", inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0);
		datosInversionFinalizada.addProperty("Inv_rTasa", inversion.has("Inv_Tasa") ? inversion.get("Inv_Tasa").getAsDouble() : 0);
		datosInversionFinalizada.addProperty("Inv_rAutor", inversionesImportesDeInvercionFinalizadaActualizarOpUsuari);
		datosInversionFinalizada.addProperty("Inv_rISR", inversion.has("Inv_ISR") ? inversion.get("Inv_ISR").getAsDouble() : 0);
		datosInversionFinalizada.addProperty("Inv_rCuent", inversion.has("Inv_Cuenta") ? inversion.get("Inv_Cuenta").getAsString() : "");
		datosInversionFinalizada.addProperty("Inv_rTBrut", inversion.has("Inv_TBruta") ? inversion.get("Inv_TBruta").getAsString() : "");
		datosInversionFinalizada.addProperty("Transaccio", inversionesImportesDeInvercionFinalizadaActualizarOpTransaccio);
		datosInversionFinalizada.addProperty("Usuario", inversionesImportesDeInvercionFinalizadaActualizarOpUsuari);
		datosInversionFinalizada.addProperty("FechaSis", fechaSis);
		datosInversionFinalizada.addProperty("SucOrigen", inversionesImportesDeInvercionFinalizadaActualizarOpSucOrigen);
		datosInversionFinalizada.addProperty("SucDestino", inversionesImportesDeInvercionFinalizadaActualizarOpSucDestino);
		datosInversionFinalizada.addProperty("Modulo", inversionesImportesDeInvercionFinalizadaActualizarOpModulo);

		logger.info("datosInversionFinalizada" + datosInversionFinalizada);
		StringBuilder inversionesImportesDeInvercionFinalizadaActualizarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/")
				.append(inversionesImportesDeInvercionFinalizadaActualizarOp);
		JsonObject inversionesImportesDeInvercionFinalizadaActualizarOp = new JsonObject();
		inversionesImportesDeInvercionFinalizadaActualizarOp.add("inversionesImportesDeInvercionFinalizadaActualizarOp", datosInversionFinalizada);
		logger.info("inversionesImportesDeInvercionFinalizadaActualizarOp" + inversionesImportesDeInvercionFinalizadaActualizarOp);

		RequestDTO inversionesImportesDeInvercionFinalizadaActualizarOpSolicitud = new RequestDTO();
		inversionesImportesDeInvercionFinalizadaActualizarOpSolicitud.setUrl(inversionesImportesDeInvercionFinalizadaActualizarUrl.toString());
		MessageProxyDTO inversionesImportesDeInvercionFinalizadaActualizarOpMensaje= new MessageProxyDTO();
		inversionesImportesDeInvercionFinalizadaActualizarOpMensaje.setBody(inversionesImportesDeInvercionFinalizadaActualizarOp.toString());
		inversionesImportesDeInvercionFinalizadaActualizarOpSolicitud.setMessage(inversionesImportesDeInvercionFinalizadaActualizarOpMensaje);

		String inversionesImportesDeInvercionFinalizadaActualizarOpResultado = HttpClientUtils.postPerform(inversionesImportesDeInvercionFinalizadaActualizarOpSolicitud);
		JsonObject inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto = new Gson().fromJson(inversionesImportesDeInvercionFinalizadaActualizarOpResultado, JsonObject.class);
		logger.info("inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto" + inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto);


		resultado = new JsonObject();
		resultado.add("inversionRenovada", inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto);

		return resultado;
	}
}
