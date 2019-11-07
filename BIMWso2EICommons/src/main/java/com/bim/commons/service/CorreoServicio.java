package com.bim.commons.service;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CorreoServicio {
	
	private static final Logger logger = Logger.getLogger(CorreoServicio.class);

	private static Properties properties;
	
	public CorreoServicio() {
		super();
		
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/mail.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	public void enviarCorreo(String destinatario, String asunto, String cuerpo) throws Exception {
		logger.info("SERVICE: Comenzando enviarCorreo metodo...");
    	
		Session session = Session.getDefaultInstance(properties);
		String Remitente = properties.getProperty("mail.data.from");
		String Host = properties.getProperty("mail.smtp.host");
		String Charset = properties.getProperty("mail.message.charset");
		String ContentType = properties.getProperty("mail.message.content_type");
		
		MimeMessage mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(Remitente));
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setSubject(asunto, Charset);
        mensaje.setContent(cuerpo, ContentType);
        
        Transport transport = session.getTransport();        
	     try
	     {
	         logger.info("Enviando mensaje...");
	         transport.connect(Host, "", "");
	         transport.sendMessage(mensaje, mensaje.getAllRecipients());
	         logger.info("Correo enviado!");
	     }
	     catch (Exception ex) {
	         logger.info("El correo no se envio.");
	         logger.info("Error: " + ex.getMessage());
	     }
	     finally
	     {
	         transport.close();
	     }
		
		logger.info("SERVICE: Finalizando enviarCorreo metodo...");
	}
}
