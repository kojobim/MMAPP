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

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.exceptions.UnauthorizedException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Utilerias {

	private static final Logger logger = Logger.getLogger(Utilerias.class);
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
		logger.info("Inicio paginado");
		ArrayList<JsonElement> list = (ArrayList<JsonElement>)new Gson().fromJson(datos.toString(), ArrayList.class);
		if(per_page > list.size())
			return datos; 
		int numDatos = (page - 1) * per_page;
		List<JsonElement> listResult = list.subList(numDatos, numDatos + per_page);
		logger.info("Termino paginado");
		return new Gson().fromJson(new Gson().toJson(listResult), JsonArray.class);
	}
	   
	public static Boolean calcularVencimiento(Date fechaVen, Date horIni, Date horFin) {
		logger.info("COMMONS: Iniciando calcularVencimiento...");

		if(fechaVen == null)
			return false;
		
		Date fechaActual = null;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1900);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date horaActual = calendar.getTime();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			fechaActual = sdf.parse(sdf.format(new Date()));
		} catch (Exception e) {
			logger.info("formato de fecha no valido.");
		}

		Date calHorIni = null;
		Date calHorFin = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			calendar.setTime(horIni);
			calendar.add(Calendar.MINUTE, -1);
			calHorIni = sdf.parse(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			logger.info("error al formatear calHorIni.");
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			calendar.setTime(horFin);
			calendar.add(Calendar.MINUTE, 1);
			calHorFin = sdf.parse(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			logger.info("error al formatear calHorFin.");
		}

		logger.info(">>>>> fechaActual: " + fechaActual);
		logger.info(">>>>> HoraActual: " + horaActual);
		logger.info(">>>>> fechaVen: " + fechaVen);
		logger.info(">>>>> calHorIni: " + calHorIni);
		logger.info(">>>>> calHorFin: " + calHorFin);
		
		logger.info("COMMONS: Finalizando calcularVencimiento...");
		if(fechaVen.compareTo(fechaActual) == 0 &&
			(horaActual.after(calHorIni) && horaActual.before(calHorFin)))
				return true;

		return false;
	}
	
	public static double redondear(double cantidad, int decimales) {
		logger.info("COMMONS: Iniciando redondear...");
	    double escala = Math.pow(10, decimales);
	    logger.info("COMMONS: Finalizando redondear...");
    	return Math.round(cantidad * escala) / escala;
	}
	
	public static String convertirFechaAFormatoSimple(String fecha) {
		logger.info("COMMONS: Iniciando convertirFecha...");
		String fechaConv = fecha.trim();
		if(fechaConv.length() == 10 && fechaConv.contains("/")) {
			fechaConv = fechaConv.substring(6)
				+ "-" + fechaConv.substring(3, 5)
				+ "-" + fechaConv.substring(0, 2);
		}
		
		logger.info("COMMONS: Finalizando convertirFecha...");
		return fechaConv;
	}
	
	public static Boolean isNumber(String value) {
		logger.info("COMMONS: Iniciando isNumber metodo...");
		String regex = "\\d+";
		logger.info("COMMONS: Finalizando isNumber metodo...");
		return value.matches(regex);
	}
	
	public static String concat(String ...args) {
		logger.info("COMMONS: Iniciando concat metodo...");
		StringBuilder resultado = new StringBuilder();
		for(int j = 0; j < args.length; j++) {
			String arg = args[j];
			if(arg == null || arg.isEmpty())
				continue;
			
			String []argItems = arg.split(" ");
			
			for(int i = 0; i < argItems.length; i++) {
				if(argItems[i].isEmpty())
					continue;
				
				resultado.append(argItems[i]);
				
				if(i < argItems.length - 1) 
					resultado.append(" ");
			}	

			if(j < args.length - 1) 
				resultado.append(" ");
		}
		logger.info("COMMONS: Finalizando concat metodo...");
		return resultado.toString();	
	}
	
	public static JsonObject getPrincipal(String bearerToken) {
		RequestDTO principalSolicitud = new RequestDTO();
		principalSolicitud.setUrl(IdentityServer);
		principalSolicitud.setIsHttps(true);
		principalSolicitud.addHeader("Authorization", bearerToken);
		principalSolicitud.addHeader("Content-Type", "application/x-www-form-urlencoded");
		principalSolicitud.addHeader("Accept", "application/json");
		
		String principalResultado = HttpClientUtils.getPerform(principalSolicitud);
		JsonObject principalResultadoObjecto = new Gson().fromJson(principalResultado, JsonObject.class);
		
		if(principalResultadoObjecto.has("error")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.29");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}
		
		return principalResultadoObjecto;
	}
	
	public static String getFechaSis() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
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

	public static String getStringProperty(JsonObject datos, String propertyName) {
		if(datos == null)
			return null;
		return datos.has(propertyName) && datos.get(propertyName).isJsonPrimitive() ? datos.get(propertyName).getAsString() : null;
	}
	
	public static Integer getIntProperty(JsonObject datos, String propertyName) {
		if(datos == null)
			return null;
		return datos.has(propertyName) && datos.get(propertyName).isJsonPrimitive() ? datos.get(propertyName).getAsInt() : null;
	}
	
	public static Double getDoubleProperty(JsonObject datos, String propertyName) {
		if(datos == null)
			return null;
		return datos.has(propertyName) && datos.get(propertyName).isJsonPrimitive() ? datos.get(propertyName).getAsDouble() : null;
	}
	
	public static Float getFloatProperty(JsonObject datos, String propertyName) {
		if(datos == null)
			return null;
		return datos.has(propertyName) && datos.get(propertyName).isJsonPrimitive() ? datos.get(propertyName).getAsFloat() : null;
	}
	
	public static JsonObject getJsonObjectProperty(JsonObject datos, String propertyName) {
		if(datos == null)
			return null;
		return datos.has(propertyName)  && datos.get(propertyName).isJsonObject() ? datos.get(propertyName).getAsJsonObject() : null;
	}
	
	public static JsonArray getJsonArrayProperty(JsonObject datos, String propertyName) {
		if(datos == null)
			return null;
		return datos.has(propertyName)  && datos.get(propertyName).isJsonArray() ? datos.get(propertyName).getAsJsonArray() : null;
	}
}
