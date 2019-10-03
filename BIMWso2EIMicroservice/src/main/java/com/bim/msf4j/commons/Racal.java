package com.bim.msf4j.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Racal {

	private static final Logger logger = Logger.getLogger(Racal.class);
	private static Properties properties;
	
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
	}
	
	public static String validaToken(String clave) {
		String server = properties.getProperty("token.server");
		int port = Integer.parseInt(properties.getProperty("token.port"));
		logger.info("server: " + server);
		logger.info("port: " + port);
		TcpSocket socket = new TcpSocket();
		String claveEncriptada = "";
		String validaToken = "";
		String mensaje = "";
		int respuesta = 0;

		clave = clave + repiteCaracterString(16 - clave.length(), " ");
		claveEncriptada = encriptar(clave, 16);
		respuesta = socket.creaConexionSocket(server, port);

		logger.info("respuesta: " + respuesta);
		if (respuesta == 0) {
			mensaje = "VP" + claveEncriptada + repiteCaracterString(18, " ");
			socket.enviaMensaje(mensaje);
			validaToken = socket.recibeMensaje(2);
			socket.cierraConexionSocket();
		}
		logger.info("validaToken: " + validaToken);
		return validaToken;
	}

	public static String repiteCaracterString(int repeticionEnt, String caracterStr) {
		StringBuffer caracterRepetidoStr = new StringBuffer();

		for (int i = 0; i < repeticionEnt; i++) 
			caracterRepetidoStr.append(caracterStr);
		
		return caracterRepetidoStr.toString();
	}

	public static String encriptar(String password, int longitud) {
		StringBuffer passwordEncriptado = new StringBuffer(repiteCaracterString(16, " "));
		StringBuffer cadena = new StringBuffer("85C2DCA4CBCED3DF");
		char caracter = ' ';

		for (int i = longitud; i > 0; i--) {
			caracter = cadena.charAt(11);
			caracter = (char) (caracter + '\001');
			cadena.replace(11, 12, String.valueOf(caracter));
			caracter = xor(password.charAt(i - 1), cadena.toString());
			passwordEncriptado.replace(i - 1, i, String.valueOf(caracter));
		}

		return passwordEncriptado.toString();
	}
	

	private static char xor(char caracter, String cadena) {
		int i = 0;

		while (i < cadena.length()) 
			caracter = (char) (caracter ^ cadena.charAt(i++));
		
		return caracter;
	}
	
}