package com.bim.commons.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.exceptions.UnauthorizedException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Utilerias {

	private static final Logger logger = LoggerFactory.getLogger(Utilerias.class);
	private static Properties properties;
	private static String IdentityServer;
	private static String DataServer;
	
	static {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		
		IdentityServer = properties.getProperty("identity_server.host");
		DataServer = properties.getProperty("data_service.host");
	}
	
	@SuppressWarnings("unchecked")
	public static JsonArray paginado(JsonArray datos, int page, int per_page) {
		logger.info("COMMONS: Comenzando paginado metodo");
		ArrayList<JsonElement> listaElementos = (ArrayList<JsonElement>)new Gson().fromJson(datos.toString(), ArrayList.class);
		if(per_page > listaElementos.size())
			return datos; 
		int numeroDatos = (page - 1) * per_page;
		List<JsonElement> listaElementosPaginados = listaElementos.subList(numeroDatos, numeroDatos + per_page);
		logger.info("COMMONS: Finalizando paginado metodo");
		return new Gson().fromJson(new Gson().toJson(listaElementosPaginados), JsonArray.class);
	}
	   
	public static Boolean calcularVencimiento(Date fechaVencimiento, Date horIni, Date horFin) {
		logger.info("COMMONS: Comenzando calcularVencimiento metodo");

		if(fechaVencimiento == null)
			return false;
		
		Date fechaActual = null;

		Calendar calendario = Calendar.getInstance();
		calendario.set(Calendar.YEAR, 1900);
		calendario.set(Calendar.MONTH, Calendar.JANUARY);
		calendario.set(Calendar.DAY_OF_MONTH, 1);
		Date horaActual = calendario.getTime();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			fechaActual = sdf.parse(sdf.format(new Date()));
		} catch (Exception e) {
			logger.info("formato de fecha no válido.");
		}

		Date calHorIni = null;
		Date calHorFin = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			calendario.setTime(horIni);
			calendario.add(Calendar.MINUTE, -1);
			calHorIni = sdf.parse(sdf.format(calendario.getTime()));
		} catch (Exception e) {
			logger.info("error al formatear calHorIni.");
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			calendario.setTime(horFin);
			calendario.add(Calendar.MINUTE, 1);
			calHorFin = sdf.parse(sdf.format(calendario.getTime()));
		} catch (Exception e) {
			logger.info("error al formatear calHorFin.");
		}

		logger.info("- fechaActual: " + fechaActual);
		logger.info("- HoraActual: " + horaActual);
		logger.info("- fechaVencimiento: " + fechaVencimiento);
		logger.info("- calHorIni: " + calHorIni);
		logger.info("- calHorFin: " + calHorFin);
		
		if(fechaVencimiento.compareTo(fechaActual) == 0 &&
			(horaActual.after(calHorIni) && horaActual.before(calHorFin)))
				return true;
		logger.info("COMMONS: Finalizando calcularVencimiento metodo");
		return false;
	}
	
	public static double redondear(double cantidad, int decimales) {
		logger.info("COMMONS: Comenzando redondear metodo...");
	    double escala = Math.pow(10, decimales);
	    logger.info("COMMONS: Finalizando redondear metodo...");
    	return Math.round(cantidad * escala) / escala;
	}
	
	public static String convertirFechaAFormatoSimple(String fecha) {
		logger.info("COMMONS: Comenzando convertirFecha metodo...");
		String fechaConvertir = fecha.trim();
		if(fechaConvertir.length() == 10 && fechaConvertir.contains("/")) {
			fechaConvertir = fechaConvertir.substring(6)
				+ "-" + fechaConvertir.substring(3, 5)
				+ "-" + fechaConvertir.substring(0, 2);
		}		
		logger.info("COMMONS: Finalizando convertirFecha metodo...");
		return fechaConvertir;
	}
	
	public static Boolean validaNumero(String valor) {
		logger.info("COMMONS: Comenzando validaNumero metodo...");
		String regex = "\\d+";
		logger.info("COMMONS: Finalizando validaNumero metodo...");
		return valor.matches(regex);
	}
	
	public static String concatenar(String ...args) {
		logger.info("COMMONS: Comenzando concatenar metodo...");
		StringBuilder resultado = new StringBuilder();
		for(int j = 0; j < args.length; j++) {
			String arg = args[j];
			if(arg == null || arg.isEmpty())
				continue;
			
			String []argElementos = arg.split(" ");
			
			for(int i = 0; i < argElementos.length; i++) {
				if(argElementos[i].isEmpty())
					continue;
				
				resultado.append(argElementos[i]);
				
				if(i < argElementos.length - 1) 
					resultado.append(" ");
			}	

			if(j < args.length - 1) 
				resultado.append(" ");
		}
		logger.info("COMMONS: Finalizando concatenar metodo...");
		return resultado.toString();	
	}
	
	public static JsonObject obtenerPrincipal(String bearerToken) {
		logger.info("COMMONS: Comenzando getPrincipal metodo...");
		RequestDTO principalSolicitud = new RequestDTO();
		principalSolicitud.setUrl(IdentityServer);
		principalSolicitud.setIsHttps(true);
		principalSolicitud.addHeader("Authorization", bearerToken);
		principalSolicitud.addHeader("Content-Type", "application/json");
		principalSolicitud.addHeader("Accept", "application/json");
		
		String principalResultado = HttpClientUtils.getPerform(principalSolicitud);
		JsonObject principalResultadoObjecto = new Gson().fromJson(principalResultado, JsonObject.class);
		
		if(principalResultadoObjecto.has("error")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.29");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}
		logger.info("COMMONS: Finalizando getPrincipal metodo...");
		return principalResultadoObjecto;
	}
	
	public static String obtenerFechaSis() {
		logger.info("COMMONS: Comenzando getFechaSis metodo");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		logger.info("COMMONS: Finalizando getFechaSis metodo");
		return simpleDateFormat.format(fecha);	
	}
	
	public static String getOperacionUrl(String servicio, String operacion) {
		return new StringBuilder()
				.append(DataServer)
				.append("/")
				.append(servicio)
				.append("/")
				.append(operacion)
				.toString();
	}
	
	public static JsonObject performOperacion(String servicioNombre, String operacionNombre, JsonObject datosOperacion) {
		String url = getOperacionUrl(servicioNombre, operacionNombre);
		RequestDTO solicitudOperacion = new RequestDTO();
		solicitudOperacion.setUrl(url);
		JsonObject operacion = new JsonObject();
		operacion.add(operacionNombre, datosOperacion);
		solicitudOperacion.setBody(operacion);
		String resultado = HttpClientUtils.postPerform(solicitudOperacion);
		JsonObject resultadoObjeto = resultado != null ? new Gson().fromJson(resultado, JsonObject.class) : null;
		return resultadoObjeto;
	}

	private static Boolean validarDatosPropiedad(JsonObject datos, String nombrePropiedad) {
		if(datos == null)
			return false;
		if(nombrePropiedad == null || nombrePropiedad.isEmpty())
			return false;
		return true;
	}
	
	public static String obtenerStringPropiedad(JsonObject datos, String nombrePropiedad) {
		if(!validarDatosPropiedad(datos, nombrePropiedad))
			return null;
		return datos.has(nombrePropiedad) && datos.get(nombrePropiedad).isJsonPrimitive() ? datos.get(nombrePropiedad).getAsString() : null;
	}
	
	public static Integer obtenerIntPropiedad(JsonObject datos, String nombrePropiedad) {
		if(!validarDatosPropiedad(datos, nombrePropiedad))
			return null;
		return datos.has(nombrePropiedad) && datos.get(nombrePropiedad).isJsonPrimitive() ? datos.get(nombrePropiedad).getAsInt() : null;
	}
	
	public static Double obtenerDoublePropiedad(JsonObject datos, String nombrePropiedad) {
		if(!validarDatosPropiedad(datos, nombrePropiedad))
			return null;
		return datos.has(nombrePropiedad) && datos.get(nombrePropiedad).isJsonPrimitive() ? datos.get(nombrePropiedad).getAsDouble() : null;
	}
	
	public static Float obtenerFloatPropiedad(JsonObject datos, String nombrePropiedad) {
		if(!validarDatosPropiedad(datos, nombrePropiedad))
			return null;
		return datos.has(nombrePropiedad) && datos.get(nombrePropiedad).isJsonPrimitive() ? datos.get(nombrePropiedad).getAsFloat() : null;
	}
	
	public static JsonObject obtenerJsonObjectPropiedad(JsonObject datos, String nombrePropiedad) {
		if(!validarDatosPropiedad(datos, nombrePropiedad))
			return null;
		return datos.has(nombrePropiedad)  && datos.get(nombrePropiedad).isJsonObject() ? datos.get(nombrePropiedad).getAsJsonObject() : null;
	}
	
	public static JsonArray obtenerJsonArrayPropiedad(JsonObject datos, String nombrePropiedad) {
		if(!validarDatosPropiedad(datos, nombrePropiedad))
			return null;
		return datos.has(nombrePropiedad)  && datos.get(nombrePropiedad).isJsonArray() ? datos.get(nombrePropiedad).getAsJsonArray() : null;
	}
}
