package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/cuentas-destino")
public class CuentaDestinoCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CuentaDestinoCtrl.class);

	private CuentaDestinoServicio cuentaDestinoServicio;
	private static Integer CuentaDestinoNumeroDigitos;
	
	public CuentaDestinoCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		
		CuentaDestinoNumeroDigitos = Integer.parseInt(properties.getProperty("cuenta_destino_servicio.numero_digitos"));
		logger.info("CTRL: Finalizando metodo init...");		
	}
	
	@Path("/beneficiarios")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentaDestinoVerificar(@QueryParam("cpCuenta") String cpCuenta, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando cuentaDestinoVerificar metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principalResultadoObjecto: " + principalResultadoObjecto);
		
		if(cpCuenta == null || cpCuenta.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.45");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(cpCuenta.length() > CuentaDestinoNumeroDigitos || cpCuenta.length() < CuentaDestinoNumeroDigitos) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.50");
			bimMessageDTO.addMergeVariable("digitos", CuentaDestinoNumeroDigitos.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosCuentaDestinoVerificar = new JsonObject();
		datosCuentaDestinoVerificar.addProperty("Ces_Cuenta", cpCuenta);
		datosCuentaDestinoVerificar.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoVerificarResultadoObjeto = this.cuentaDestinoServicio.cuentasEspecialesConsultar(datosCuentaDestinoVerificar);
		logger.info("- cuentaDestinoVerificarResultadoObjeto: " + cuentaDestinoVerificarResultadoObjeto);
		
		JsonObject cuentaDestinoObjeto = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoVerificarResultadoObjeto, "cuentasEspeciales");
		logger.info("- cuentaDestinoObjeto: " + cuentaDestinoObjeto);
		
		if(cuentaDestinoObjeto == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.47");
			bimMessageDTO.addMergeVariable("cuenta", cpCuenta);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonObject cuentaDestinoVerificarObjeto = new JsonObject();		
		if(cuentaDestinoObjeto.has("Cue_Status")) {
			String cueStatus = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cue_Status");
			if(!cueStatus.equals("A")) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.47");
				bimMessageDTO.addMergeVariable("cuenta", cpCuenta);
				throw new ConflictException(bimMessageDTO.toString());
			}

			String cueNumero = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cue_Numero");
			String cliComOrd = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cli_ComOrd");

			cuentaDestinoVerificarObjeto.addProperty("cueNumero", cueNumero);
			cuentaDestinoVerificarObjeto.addProperty("cliComOrd", cliComOrd);
			logger.info("- cuentaDestinoVerificarObjeto " + cuentaDestinoVerificarObjeto);
		}

		JsonObject cuentaDestinoVerificarResultado = new JsonObject();
		cuentaDestinoVerificarResultado.add("beneficiario", cuentaDestinoVerificarObjeto);
		logger.info("CTRL: Terminando cuentaDestinoVerificar metodo");
		return Response.ok(cuentaDestinoVerificarResultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/BIM")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listadoCuentasDestinoBIM(@QueryParam("status") String status, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando listadoCuentasDestinoBIM metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info(principalResultadoObjecto);
		
		if(status == null || status.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.49");
            throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
        
        JsonObject datosCuentaDestinoBIMConsultar = new JsonObject();
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", "");
		datosCuentaDestinoBIMConsultar.addProperty("FechaSis", fechaSis);

		if(status.equals("ACTIVO"))
			datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", "L2");
		
		if(status.equals("PENDIENTE"))
			datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", "L3");
		
		JsonObject cuentaDestinoBIMConsultarRestultado = this.cuentaDestinoServicio.cuentaDestinoBIMConsultar(datosCuentaDestinoBIMConsultar);
		logger.info("- cuentaDestinoBIMConsultarRestultado: " + cuentaDestinoBIMConsultarRestultado);
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIMConsultarRestultado, "cuentaDestinoBIM");
		logger.info("- cuentaDestinoBIM: " + cuentaDestinoBIM);

		JsonObject cuentasDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIM, "cuentasDestinoBIM");
		logger.info("- cuentasDestinoBIM: " + cuentasDestinoBIM);

		JsonObject resultado = new JsonObject();
		resultado.add("cuentasDestinoBIM", cuentasDestinoBIM);
		
		logger.info("CTRL: Terminando listadoCuentasDestinoBIM metodo...");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
                .build();
	}
}
