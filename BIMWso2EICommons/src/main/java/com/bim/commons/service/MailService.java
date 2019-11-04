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

public class MailService {
	
	private static final Logger logger = Logger.getLogger(MailService.class);

	private static Properties properties;
	
	public MailService() {
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
	
	public void enviarCorreo(String destinario, String asunto, String mensaje) throws Exception {
		logger.info("SERVICE: Comenzando enviarCorreo metodo...");
    	
		Session session = Session.getDefaultInstance(properties);
		String MailFrom = properties.getProperty("mail.data.from");
		String MailHost = properties.getProperty("mail.smtp.host");
		
		MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(MailFrom));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinario));
        msg.setSubject(asunto, "UTF-8");
        msg.setContent(mensaje,"text/html");
        
        Transport transport = session.getTransport();        
	     try
	     {
	         logger.info("Enviando mensaje...");
	         transport.connect(MailHost, "", "");
	         transport.sendMessage(msg, msg.getAllRecipients());
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
