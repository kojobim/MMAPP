package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.service.SoapService;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/utils")
public class UtilsCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(UtilsCtrl.class);
	private SoapService soapService;
	
	public UtilsCtrl() {
		super();
		this.soapService = new SoapService();
	}
	
	@Path("/cuentas/enviar-correo-movimientos")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void movimientosEnvioCorreo(JsonObject datosMovimientos) throws SOAPException {
		logger.info("CTRL: Comenzando movimientosEnvioCorreo metodo");
		
		String anio = Utilerias.getStringProperty(datosMovimientos, "anio");
		String mes = Utilerias.getStringProperty(datosMovimientos, "mes");
		String cliente = Utilerias.getStringProperty(datosMovimientos, "cliente");
		 
        this.soapService.movimientosEnvioCorreo(anio, mes, cliente);
		
		logger.info("CTRL: Finalizando movimientosEnvioCorreo metodo");
	}
	 
}
