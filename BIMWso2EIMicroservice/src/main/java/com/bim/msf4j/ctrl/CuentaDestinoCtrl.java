package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.wso2.msf4j.Request;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/cuentas-destino")
public class CuentaDestinoCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CuentaDestinoCtrl.class);

	private CuentaDestinoServicio cuentaDestinoServicio;
	private TransaccionServicio transaccionServicio;
	private TokenServicio tokenServicio;
	private BitacoraServicio bitacoraServicio;
	
	public CuentaDestinoCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.tokenServicio = new TokenServicio();
		this.bitacoraServicio = new BitacoraServicio();
		
		logger.info("CTRL: Finalizando metodo init...");		
	}
	
	@Path("/beneficiarios")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentaDestinoVerificar(@QueryParam("cpCuenta") String cuenta, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando cuentaDestinoVerificar metodo...");
		
		logger.info("CTRL: Finalizando cuentaDestinoVerificar metodo...");
		return Response.ok(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/alta-bim")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response altaCuentaDestinoBIM(@Context final Request solicitud, JsonObject cuentaDestinoObjeto) {
		logger.info("CTRL: Comenzando altaCuentaDestinoBIM metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuNumero");
		// String usuFolTok = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuFolTok");
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		String usuFolTok = "416218850";
		
		JsonObject altaCuentaDestinoBim = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoObjeto, "altaCuentaDestinoBim");
		String cpRSAToken = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cpRSAToken");
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");
		
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac);

		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		String uuid = java.util.UUID.randomUUID().toString();
		uuid = uuid.substring(uuid.length()-6).toUpperCase();
		String uuidCif = Racal.cifraPassword_HSM(uuid);
		logger.info("UUID: " + uuid);
		logger.info("UUID Cifrada: " + uuidCif);
		
		if(uuidCif.length() == 7 && uuidCif.equals("autoriz")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(uuidCif.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		String cdbCuenta = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdbCuenta");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosCuentasEspeciales = new JsonObject();
		datosCuentasEspeciales.addProperty("Ces_Cuenta", cdbCuenta);
		datosCuentasEspeciales.addProperty("NumTransac", numTransac);
		datosCuentasEspeciales.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentasEspecialesConsultarResultado = this.cuentaDestinoServicio.cuentasEspecialesConsultar(datosCuentasEspeciales);
		JsonObject cuentasEspeciales = Utilerias.obtenerJsonObjectPropiedad(cuentasEspecialesConsultarResultado, "cuentasEspeciales");
		
		if(cuentasEspeciales.entrySet().isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.46");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuUsuAdm");
		String usuClient = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuClient");		
		String cdbAlias = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdbAlias");
		String cdbRFCBen = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdbRFCBen");
		String cdbEmaBen = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdbEmaBen");
		
		JsonObject datosCuentaDestinoBIM = new JsonObject();
		datosCuentaDestinoBIM.addProperty("Cdb_UsuAdm", usuUsuAdm);
		datosCuentaDestinoBIM.addProperty("Cdb_Cuenta", cdbCuenta);
		datosCuentaDestinoBIM.addProperty("Cdb_CliUsu", usuClient);
		datosCuentaDestinoBIM.addProperty("Cdb_Alias", cdbAlias);
		datosCuentaDestinoBIM.addProperty("Cdb_RFCBen", cdbRFCBen);
		datosCuentaDestinoBIM.addProperty("Cdb_EmaBen", cdbEmaBen);
		datosCuentaDestinoBIM.addProperty("Cdb_Random", uuid); // Validar
		datosCuentaDestinoBIM.addProperty("NumTransac", numTransac);
		datosCuentaDestinoBIM.addProperty("fechaSis", fechaSis);
		
		JsonObject cuentaDestinoBIMCreacionResultado = this.cuentaDestinoServicio.cuentaDestinoBIMCreacion(datosCuentaDestinoBIM);
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIMCreacionResultado, "cuentaDestinoBIM");
		String errCodigo = Utilerias.obtenerStringPropiedad(cuentaDestinoBIM, "Err_Codigo");
		
		if(!"000000".equals(errCodigo)) {
			String errMensaj = Utilerias.obtenerStringPropiedad(cuentaDestinoBIM, "errMensaj");
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		String bitDireIP = solicitud.getHeader("User-Agent") != null ? solicitud.getHeader("User-Agent") : "";
		String bitPriRef = solicitud.getHeader("X-Forwarded-For") != null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);

		JsonObject creacionBitacoraResultado = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info(creacionBitacoraResultado);
		
		logger.info("CTRL: Finalizando altaCuentaDestinoBIM metodo...");
		return Response.ok(cuentaDestinoBIMCreacionResultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
}
