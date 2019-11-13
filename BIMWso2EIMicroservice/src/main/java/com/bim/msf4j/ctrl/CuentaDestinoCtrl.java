package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimEmailTemplateDTO;
import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.ConfiguracionServicio;
import com.bim.commons.service.CorreoServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/cuentas-destino")
public class CuentaDestinoCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(CuentaDestinoCtrl.class);

	private CuentaDestinoServicio cuentaDestinoServicio;
	private TransaccionServicio transaccionServicio;
	private TokenServicio tokenServicio;
	private BitacoraServicio bitacoraServicio;
	private ConfiguracionServicio configuracionServicio;
	private CorreoServicio correoServicio;
	private static Integer CuentaDestinoNumeroDigitos;
	
	public CuentaDestinoCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.tokenServicio = new TokenServicio();
		this.bitacoraServicio = new BitacoraServicio();
		this.configuracionServicio = new ConfiguracionServicio();
		this.correoServicio = new CorreoServicio();
		
		
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
		String usuFolTok = "0416218850";
		
		JsonObject altaCuentaDestinoBim = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoObjeto, "altaCuentaDestinoBim");
		String cpRSAToken = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cpRSAToken");
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");
		String scriptName = CuentaDestinoCtrl.class.getName() + ".altaCuentaDestinoBIM";
		
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName);

		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		String uuid = java.util.UUID.randomUUID().toString();
		String cdsEncript = Racal.cifraPassword_HSM(uuid.substring(uuid.length()-6).toUpperCase());
		logger.info("Cds_Encript: " + cdsEncript);
		
		if(cdsEncript.length() == 7 && cdsEncript.equals("autoriz")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(cdsEncript.isEmpty()) {
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
		datosCuentaDestinoBIM.addProperty("Cdb_Random", cdsEncript);
		datosCuentaDestinoBIM.addProperty("NumTransac", numTransac);
		datosCuentaDestinoBIM.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoBIMCreacionResultado = this.cuentaDestinoServicio.cuentaDestinoBIMCreacion(datosCuentaDestinoBIM);
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIMCreacionResultado, "cuentaDestinoBIM");
		String errCodigo = Utilerias.obtenerStringPropiedad(cuentaDestinoBIM, "Err_Codigo");
		
		if(!"000000".equals(errCodigo)) {
			String errMensaj = Utilerias.obtenerStringPropiedad(cuentaDestinoBIM, "Err_Mensaj");
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
		
		JsonObject datosConfiguracion = new JsonObject();
		datosConfiguracion.addProperty("FechaSis", fechaSis);
		
		JsonObject configuracionBancoConsultarDetalleResultado = this.configuracionServicio.configuracionBancoConsultarDetalle(datosConfiguracion);
		JsonObject configuracionesBanco = Utilerias.obtenerJsonObjectPropiedad(configuracionBancoConsultarDetalleResultado, "configuracionesBanco");
		JsonArray configuracionBanco = Utilerias.obtenerJsonArrayPropiedad(configuracionesBanco, "configuracionBanco");
		String parMiCuDe = Utilerias.obtenerStringPropiedad(configuracionBanco.get(0).getAsJsonObject(), "Par_MiCuDe");
		
		String asunto = Utilerias.obtenerPropiedadPlantilla("mail.alta_cuenta_destino_bim.asunto");
		String plantilla = Utilerias.obtenerPlantilla("alta-cuenta-destino-bim");
		String destinatario = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuEmail");
		String cliComOrd = Utilerias.obtenerStringPropiedad(cuentasEspeciales, "Cli_ComOrd");
		String usuNombre = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuNombre");

		if(usuUsuAdm.equals(usuNumero)) destinatario += "," + cdbEmaBen;
		
		StringBuilder cdbCuentaOcu = new StringBuilder()
				.append("**************")
				.append(cdbCuenta.substring(cdbCuenta.length()-4));
		
		BimEmailTemplateDTO emailTemplateDTO = new BimEmailTemplateDTO(plantilla);
		emailTemplateDTO.addMergeVariable("Str_Client", cliComOrd);
		emailTemplateDTO.addMergeVariable("Usu_Nombre", usuNombre);
		emailTemplateDTO.addMergeVariable("Cdb_Cuenta", cdbCuentaOcu.toString());
		emailTemplateDTO.addMergeVariable("Cdb_Alias", cdbAlias);
		emailTemplateDTO.addMergeVariable("Str_Random", cdsEncript);
		emailTemplateDTO.addMergeVariable("TiempoEspera", parMiCuDe);
		String cuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateDTO);
		
		try {
			logger.info("Iniciando envio de mensaje...");
			correoServicio.enviarCorreo(destinatario, asunto, cuerpo);
			logger.info("Terminando envio de mensaje...");
		} catch (Exception e) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.48");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		logger.info("CTRL: Finalizando altaCuentaDestinoBIM metodo...");
		return Response.created(null)
				.build();
	}
	
}
