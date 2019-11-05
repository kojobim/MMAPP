package com.bim.commons.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import org.stringtemplate.v4.ST;
import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.BimEmailTemplateDTO;
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

	/**
	 * Método que calcula tasa
	 * @param datos
	 * <pre>
	 * {
	 * 	Inv_Plazo: int,
	 * 	Inv_Cantid: double,
	 * 	TasInv: double,
	 * 	Par_DiBaIn: int,
	 * 	Par_ISR: double,
	 * 	Cli_CobISR: String
	 * }
	 * </pre>
	 * @return
	 * { }
	 */	
	public static JsonObject calculaTasa(JsonObject datos) {
		/**
		 * PARÁMETROS OBTENIDOS DE SP Y VARIABLE CORRESPONDIENTE
		 * 	TasInv = Se obtiene al consultar el SP CLTAGRCACON.
		 * 	Par_DiBaIn = Se obtiene al consultar el SP SOPARAMSCON.
		 * 	Par_ISR = Cli_TasISR Se obtiene al consultar el SP CLCLIENTCON.
		 * 	Cli_CobISR = Se obtiene de consultar al SP CLCLIENTCON.
		 */
		int invPlazo = Utilerias.getIntProperty(datos, "Inv_Plazo");
		double invCantid = Utilerias.getDoubleProperty(datos, "Inv_Cantid");
		double invTBruta = Utilerias.getDoubleProperty(datos, "TasInv");
		int parDiBaIn = Utilerias.getIntProperty(datos, "Par_DiBaIn"); 
		double parISR = Utilerias.getDoubleProperty(datos, "Par_ISR");
		String cliCobISR = Utilerias.getStringProperty(datos, "Cli_CobISR")

		double invCanBru = invCantid * invTBruta * invPlazo / (parDiBaIn * 100);

		double tasISR = 0;
		double tasNet = 0;
		double canNet = 0;

		if("S".equals(cliCobISR))
			tasISR = parISR / 10;

		tasNet = invTBruta - tasISR;
		canNet = invCantid * tasNet * invPlazo / (parDiBaIn * 100);

		double invCanISR = invCantid * tasISR * invPlazo / (parDiBaIn * 100);
		double invCanTot = invCantid + canNet;

		JsonObject resultado = new JsonObject();
		resultado.addProperty("Inv_Capita", invCantid);
		resultado.addProperty("Inv_CanBru", invCanBru);
		resultado.addProperty("Inv_ISR", redondear(tasISR, 2));
		resultado.addProperty("Inv_CanISR", invCanISR);
		resultado.addProperty("Inv_Tasa", redondear(tasNet, 2));
		resultado.addProperty("Inv_CanNet", canNet);
		resultado.addProperty("Inv_CanTot", invCanTot);

		return resultado;
	}
	//Cierre del método

	
	public static Boolean isNumber(String value) {
		logger.info("COMMONS: Iniciando isNumber metodo...");
		String regex = "\\d+";
		logger.info("COMMONS: Finalizando isNumber metodo...");
		return value.matches(regex);
	}

	/**
	 * Método que genera una clave numérica a partir de una cadena de texto
	 * @param sVarEncode String
	 * @return
	 * String
	 */
	public static String generarDigitoVerificador(String sVarEncode) {
		int matrizd1[][] = new int[2][2];
		int matrizd2[][] = new int[2][2];
		int matrizenc1[][] = new int[2][2];
		int matrizenc2[][] = new int[2][2];
		String sVarEncodeTmp = sVarEncode;

		if(sVarEncodeTmp.length() < 8) {
			for(int i = sVarEncodeTmp.length(); i < 8; i++) {
				sVarEncodeTmp += " ";
			}
		} else if(sVarEncodeTmp.length() > 8) {
			sVarEncodeTmp = "";
			for(int i = 0; i < 8; i++) {
				sVarEncodeTmp += sVarEncode.charAt(i);
			}
		}

		// Carga de la primera matriz de datos
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(i == 0)
					matrizd1[j][0] = (int) sVarEncodeTmp.charAt(j);
				else
					matrizd1[j][1] = (int) sVarEncodeTmp.charAt(j + 2);
			}
		}

		// Carga de la segunda matriz de datos
		for(int i = 4; i < 6; i++) {
			for(int j = 4; j < 6; j++) {
				if(i == 4)
					matrizd2[j-4][0] = (int) sVarEncodeTmp.charAt(j);
				else
					matrizd2[j-4][1] = (int) sVarEncodeTmp.charAt(j + 2);
			}
		}

		// Multiplica las matrices por la matriz codificadora
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				int suma1 = 0;
				int suma2 = 0;

				for(int x = 0; x < 2; x++) {
					suma1 += matrizd1[x][i] * matrizd2[j][x];
					suma2 += matrizd2[x][i] * matrizd2[j][x];
				}

				matrizenc1[j][i] = suma1;
				matrizenc2[j][i] = suma2;
			}
		}

		String claveEnc = "";
		String claveEnc1 = "";

		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(String.valueOf(matrizenc1[j][i]).length() < 3) {
					if(String.valueOf(matrizenc1[j][i]).length() > 1) {
						claveEnc += "0";
					} else {
						claveEnc += "00";
					}
				}
				claveEnc += String.valueOf(matrizenc1[j][i]);
			}
		}

		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(String.valueOf(matrizenc2[j][i]).length() < 3) {
					if(String.valueOf(matrizenc2[j][i]).length() > 1) {
						claveEnc1 += "0";
					} else {
						claveEnc1 += "00";
					}
				}
				claveEnc1 += String.valueOf(matrizenc2[j][i]);
			}
		}

		claveEnc += claveEnc1;
		return claveEnc;
	}
	//Cierre del método
	
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
	
	public static String obtenerPropiedadPlantilla(String property) {
		Properties templateProps = null;
		
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/template.properties")) {
			templateProps = new Properties();
			
			if(inputStream != null) {
				templateProps.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}

    	return templateProps.getProperty(property);
	}
	
	public static String obtenerPlantilla(String archivo) {
		StringBuilder contentBuilder = new StringBuilder();
		String templateBaseLocation = "/BIMWso2EICommons/src/main/resource/com/bim/commons/templates/";
		try {
		    BufferedReader in = new BufferedReader(
		    		new InputStreamReader(new FileInputStream(System.getenv("BIM_HOME") + templateBaseLocation + archivo + ".html"),
		    				StandardCharsets.ISO_8859_1));
		    
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    	return contentBuilder.toString();
	}

	public static String obtenerMensajePlantilla(BimEmailTemplateDTO emailTemplateDTO) {		
		ST template = new ST(emailTemplateDTO.getTemplate(), '{', '}');
		
		if(emailTemplateDTO.getMergeVariables() != null) 
			for(Entry<String, String> entry : emailTemplateDTO.getMergeVariables().entrySet())
				template.add(entry.getKey(), entry.getValue());
			
		return template.render().toString();
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
