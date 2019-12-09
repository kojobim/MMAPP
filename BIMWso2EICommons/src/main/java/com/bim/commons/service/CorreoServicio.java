package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.InternalServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Esta clase define las operaciones sobre envio de correo
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class CorreoServicio {
	
	private static final Logger logger = LoggerFactory.getLogger(CorreoServicio.class);

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
	
	/**
	 * Método para envio de correo
	 * @param destinatario
	 * @param asunto
	 * @param cuerpo
	 */
	public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
		logger.info("SERVICE: Comenzando enviarCorreo metodo...");
    	
		Session session = Session.getDefaultInstance(properties);
		String Remitente = properties.getProperty("mail.data.from");
		String Host = properties.getProperty("mail.smtp.host");
		String Charset = properties.getProperty("mail.message.charset");
		String ContentType = properties.getProperty("mail.message.content_type");
		
		Transport transporte = null;
	    try {			 
			MimeMessage mensaje = new MimeMessage(session);
			mensaje.setFrom(new InternetAddress(Remitente));
			InternetAddress[] address = InternetAddress.parse(destinatario);
			mensaje.setRecipients(Message.RecipientType.TO, address);
			mensaje.setSubject(asunto, Charset);
			mensaje.setContent(cuerpo, ContentType);
        
        	transporte = session.getTransport();   
			logger.info("Enviando mensaje...");
			transporte.connect(Host, "", "");
			transporte.sendMessage(mensaje, mensaje.getAllRecipients());
			logger.info("Correo enviado!");
		} catch (Exception ex) {
			logger.info("El correo no se envio.");
			logger.info("Error: " + ex.getMessage());
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.51");
			throw new InternalServerException(bimMessageDTO.toString());
		} finally {
			if(transporte != null) {
				try {
					transporte.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		
		logger.info("SERVICE: Finalizando enviarCorreo metodo...");
	}
}
