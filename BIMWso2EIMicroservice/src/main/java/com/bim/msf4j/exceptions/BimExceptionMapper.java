package com.bim.msf4j.exceptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.BimException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.google.gson.JsonObject;

public class BimExceptionMapper implements ExceptionMapper<BimException> {

	private Properties properties;
	

	@Override
	public Response toResponse(BimException exception) {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/message.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		Status status = Status.INTERNAL_SERVER_ERROR;
		if(exception instanceof BadRequestException) 
			status = Status.BAD_REQUEST;
		
		if(exception instanceof ConflictException)
			status = Status.CONFLICT;
		
		if(exception instanceof UnauthorizedException)
			status = Status.UNAUTHORIZED;
		
		if(exception instanceof ForbiddenException)
			status = Status.FORBIDDEN;
		
		String exceptionMessage = exception.getMessage();
		System.out.println("exceptionMessage:" + exceptionMessage);
		String mensaje = properties.getProperty(exceptionMessage);
		System.out.println("mensaje: " + mensaje);
		JsonObject datosError = new JsonObject();
		datosError.addProperty("Err_Codigo", exceptionMessage);
		datosError.addProperty("Err_Mensaj", mensaje);
		JsonObject error = new JsonObject();
		error.add("Error", datosError);
		System.out.println("error: " + error);
		return Response.status(status)
				.entity(error.toString())
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
