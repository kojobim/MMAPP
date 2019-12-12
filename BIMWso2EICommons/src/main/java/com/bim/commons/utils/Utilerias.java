package com.bim.commons.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.stringtemplate.v4.ST;
import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.BimEmailTemplateDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.exceptions.InternalServerException;
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
	
	public static Date convertirZonaHoraria(Date fecha, TimeZone zonaHorariaActual, TimeZone zonaHorariaDestino) {
		logger.info("COMMONS: Comenzando convertirZonaHoraria metodo");
		logger.debug("FECHA ORIGINAL: "+fecha.toString());

		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		calendario.setTimeZone(zonaHorariaActual);
		calendario.add(Calendar.MILLISECOND, zonaHorariaActual.getRawOffset() * -1);
		if (zonaHorariaActual.inDaylightTime(calendario.getTime())) {
			calendario.add(Calendar.MILLISECOND, calendario.getTimeZone().getDSTSavings() * -1);
		}

		calendario.add(Calendar.MILLISECOND, zonaHorariaDestino.getRawOffset());
		if (zonaHorariaDestino.inDaylightTime(calendario.getTime())) {
		    calendario.add(Calendar.MILLISECOND, zonaHorariaDestino.getDSTSavings());
		}
		
		logger.debug("FECHA PARSEADA: "+calendario.getTime());	

		logger.info("COMMONS: Finalizando convertirZonaHoraria metodo");
		return calendario.getTime();
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
			
			calHorIni = convertirZonaHoraria(horIni, TimeZone.getTimeZone("CST6CDT"),TimeZone.getTimeZone("UTC"));
			calendario.setTime(calHorIni);
			calendario.add(Calendar.MINUTE, -1);
			calHorIni = calendario.getTime();
		} catch (Exception e) {
			logger.info("error al formatear calHorIni.");
		}

		try {
			calHorFin = convertirZonaHoraria(horFin, TimeZone.getTimeZone("CST6CDT"),TimeZone.getTimeZone("UTC"));
			calendario.setTime(calHorFin);
			calendario.add(Calendar.MINUTE, -1);
			calHorFin = calendario.getTime();
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
		int invPlazo = Utilerias.obtenerIntPropiedad(datos, "Inv_Plazo");
		double invCantid = Utilerias.obtenerDoublePropiedad(datos, "Inv_Cantid");
		double invTBruta = Utilerias.obtenerDoublePropiedad(datos, "TasInv");
		int parDiBaIn = Utilerias.obtenerIntPropiedad(datos, "Par_DiBaIn"); 
		double parISR = Utilerias.obtenerDoublePropiedad(datos, "Par_ISR");
		String cliCobISR = Utilerias.obtenerStringPropiedad(datos, "Cli_CobISR");

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

	
	public static Boolean validaNumero(String valor) {
		logger.info("COMMONS: Comenzando validaNumero metodo...");
		String regex = "\\d+";
		logger.info("COMMONS: Finalizando validaNumero metodo...");
		return valor.matches(regex);
	}

	public static String encode(String sVarEncode) {
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

	/**
	 * Método que genera una clave numérica a partir de una cadena de texto
	 * @param sVarEncode String
	 * @return
	 * String
	 */
	public static String generarDigitoVerificador(String sVarVerif) {
		String encVerif = "";
		String tmpEncextd = sVarVerif;
		int lenEncextd = tmpEncextd.length();
		sVarVerif = "";
		String codEncextd = "";
		int cntEncextd = 1;

		for(int iEncextd = 0; iEncextd < lenEncextd; iEncextd++) {
			sVarVerif += tmpEncextd.substring(iEncextd, iEncextd + 1);
			if(cntEncextd == 8 || iEncextd == lenEncextd) {
				encVerif = encode(sVarVerif);
				sVarVerif = "";
				codEncextd += encVerif;
				cntEncextd = 0;
			}
			cntEncextd++;
		}
		encVerif = codEncextd;
		return encVerif;
	}
	//Cierre del método
	
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
		principalSolicitud.addHeader("Content-Type", "application/x-www-form-urlencoded");
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
		logger.info("- HttpClientUtils - resultado "  + resultado);
		JsonObject resultadoObjeto = resultado != null ? new Gson().fromJson(resultado, JsonObject.class) : null;
		verificarError(resultadoObjeto);
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
		String templateBaseLocation = obtenerPropiedadPlantilla("template.base_location");
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
	
	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}
	
	public static JsonObject fromJsonObject(String data) {
		return new Gson().fromJson(data, JsonObject.class);
	}
	
	public static void verificarError(JsonObject resultado) {
		if(resultado == null || resultado.has("Fault") ) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("COMMONS.500");
			throw new InternalServerException(bimMessageDTO.toString());
		}
	}
	
	public static JsonArray obtenerJsonArrayResultante(JsonObject resultado, String propiedad) {
		JsonArray arrayResultante = null;
		if(!resultado.has(propiedad))
			return arrayResultante;
		if(resultado.get(propiedad).isJsonObject()) {
			JsonObject resultadoObjecto = Utilerias.obtenerJsonObjectPropiedad(resultado, propiedad);
			if(resultadoObjecto.entrySet().size() == 0)
				return null;
			arrayResultante = new JsonArray();
			arrayResultante.add(resultadoObjecto);
		}
		if(resultado.get(propiedad).isJsonArray())
			arrayResultante = Utilerias.obtenerJsonArrayPropiedad(resultado, propiedad);
		
		return arrayResultante;
	}
	
	public static Date convertirFecha(String fecha, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		TimeZone tz = TimeZone.getTimeZone("UTC");
		sdf.setTimeZone(tz);
		Date fechaResultado = null;
		
		if(fecha == null || fecha.isEmpty() || (fecha = fecha.trim()).length() == 0)
			return null;
		
		try {
			fechaResultado = sdf.parse(fecha);
		} catch (ParseException e) {
			logger.warn("Error en el formato de fecha.");
			logger.debug("[ FECHA: "+fecha+",FORMATO "+formato);
		}
		logger.debug("Formato de fecha encontrado");
		logger.debug("[ FECHA: "+fecha+" == FORMATO "+formato);
		return fechaResultado;
	}

	public static Date convertirFecha(String fecha) {
		List<String> formatosEntrada = new ArrayList<>();
		formatosEntrada.add("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		formatosEntrada.add("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		formatosEntrada.add("yyyy-MM-dd HH:mm:ss");
		formatosEntrada.add("yyyy-MM-dd");
		formatosEntrada.add("dd-MM-yyyy");
		formatosEntrada.add("dd/MM/yyyy");
		formatosEntrada.add("HH:mm");
		Date fechaResultado = null;

		if(fecha == null || fecha.isEmpty() || (fecha = fecha.trim()).length() == 0)
			return null;

		for(String formatoEntrada : formatosEntrada) {
			SimpleDateFormat sdf = new SimpleDateFormat(formatoEntrada);
			TimeZone tz = TimeZone.getTimeZone("UTC");
			sdf.setTimeZone(tz);
			try {
				fechaResultado = sdf.parse(fecha);
			} catch (ParseException e) {
				logger.warn("Error en el formato de fecha.");
				logger.debug("[ FECHA: "+fecha+" <> FORMATO "+formatoEntrada);
			}
			
			if(fechaResultado != null) {
				logger.debug("Formato de fecha encontrado");
				logger.debug("[ FECHA: "+fecha+" == FORMATO "+formatoEntrada);
				return fechaResultado;				
			}

		}
		return null;
	}
	
	public static String formatearFecha(String fecha, String formatoResultado) {
		List<String> formatosEntrada = new ArrayList<>();
		formatosEntrada.add("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		formatosEntrada.add("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		formatosEntrada.add("yyyy-MM-dd HH:mm:ss");
		formatosEntrada.add("yyyy-MM-dd");
		formatosEntrada.add("dd-MM-yyyy");
		formatosEntrada.add("dd-MM-yyyy HH:mm");
		formatosEntrada.add("dd/MM/yyyy");
		formatosEntrada.add("HH:mm");
		Date fechaResultado = null;
		
		if(fecha == null || fecha.isEmpty() || (fecha = fecha.trim()).length() == 0)
			return "";
		for(String formatoEntrada : formatosEntrada) {
			fechaResultado = convertirFecha(fecha, formatoEntrada);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatoResultado);
			if(fechaResultado != null)
				return simpleDateFormat.format(fechaResultado);
		}
		return "";
	}
	
	public static String formatearFecha(String fecha) {
		List<String> formatosEntrada = new ArrayList<>();
		formatosEntrada.add("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		formatosEntrada.add("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		formatosEntrada.add("yyyy-MM-dd HH:mm:ss");
		formatosEntrada.add("yyyy-MM-dd");
		formatosEntrada.add("dd-MM-yyyy");
		formatosEntrada.add("dd/MM/yyyy");
		formatosEntrada.add("HH:mm");
		Date fechaResultado = null;
		
		for(String formatoEntrada : formatosEntrada) {
			fechaResultado = convertirFecha(fecha, formatoEntrada);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatoEntrada);
			if(fechaResultado != null)
				return simpleDateFormat.format(fechaResultado);
		}
		return null;
	}
	
	public static String formatearCuenta(String cadenaOriginal, int numeroCaracteres, int numerosOcultos) {
		logger.info("COMMONS: Comenzando formatearCuenta metodo");
		String caracteres = null;
		for(int i = 0; i<numerosOcultos; i++) {
			caracteres += "*";
		}
		String digitos = cadenaOriginal.substring(cadenaOriginal.length() - numeroCaracteres);
		String cadenaFinal = new StringBuilder()
				.append(caracteres.substring(digitos.length()))
				.append(digitos)
				.toString();
		logger.info("COMMONS: Finalizando formatearCuenta metodo");
		return cadenaFinal;
	}
	
	public static JsonObject obtenerElementoPorPropiedadValorString(JsonArray arreglo, String propiedad, String valor) {
		if(arreglo == null)
			return null;
		if(arreglo.size() == 0)
			return null;
		if(propiedad == null || propiedad.isEmpty())
			return null;
		if(valor == null || valor.isEmpty())
			return null;
		for(JsonElement elemento : arreglo) {
			JsonObject elementoObjeto = (JsonObject) elemento;
			if(!elementoObjeto.has(propiedad))
				return null;
			if(elementoObjeto.get(propiedad).getAsString().equals(valor))
				return elementoObjeto;
		}
		return null;
	}
	
	public static JsonArray  filtrarPropiedadesArray(JsonArray datos, Predicate<JsonObject> predicado){
		logger.info("COMMONS: Comenzando filtrarPropiedadesArray metodo");
		JsonArray resultante = null;
		if(logger.isDebugEnabled()){
			logger.debug("DATOS A FILTRAR "+datos);
		}
		if(datos != null && !datos.isJsonNull() && datos.isJsonArray()) {
			resultante = new JsonArray();
			for(JsonElement elemento : datos) {

				if(predicado.test(elemento.getAsJsonObject())) {
					
					if(logger.isDebugEnabled()){
						logger.debug("elemento coincide con el predicado [ Elemento = "+elemento+", Predicado = "+predicado.toString());
					}
					
					resultante.add(elemento);

				}				
			}
		}
		logger.info("COMMONS: Finalizando filtrarPropiedadesArray metodo");
		return resultante;
	}
}
