package com.bim.msf4j.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.bim.commons.enums.CuentaDestinoEstadosEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.ConfiguracionServicio;
import com.bim.commons.service.CorreoServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	
	private static Integer CuentaDestinoBIMNumeroDigitos;
	private static String CuentaDestinoBIMStatusActivo;
	private static String CuentaDestinoBIMStatusPendiente;
	private static String CuentaDestinoBIMTipConsul;
	
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
		
		
		CuentaDestinoBIMNumeroDigitos = Integer.parseInt(properties.getProperty("cuenta_destino_servicio.numero_digitos"));
		CuentaDestinoBIMStatusActivo = properties.getProperty("cuenta_destino_servicio.status_activo");
		CuentaDestinoBIMStatusPendiente = properties.getProperty("cuenta_destino_servicio.status_pendiente");
		CuentaDestinoBIMTipConsul = properties.getProperty("op.cuenta_destino_bim_consultar.tip_consul.l1");
		
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
		
		if(cpCuenta.length() > CuentaDestinoBIMNumeroDigitos || cpCuenta.length() < CuentaDestinoBIMNumeroDigitos) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.50");
			bimMessageDTO.addMergeVariable("digitos", CuentaDestinoBIMNumeroDigitos.toString());
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
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response listadoCuentasDestinoBIM(@QueryParam("status") String status, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando listadoCuentasDestinoBIM metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principalResultadoObjecto: " + principalResultadoObjecto);
		
		String usuAdm = principalResultadoObjecto.get("usuUsuAdm").getAsString();

		if(status == null || status.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.49");
            throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(CuentaDestinoEstadosEnum.validarEstado(status) == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.52");
			bimMessageDTO.addMergeVariable("status", status);
            throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
        
		JsonObject datosCuentaDestinoBIMConsultar = new JsonObject();
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", usuAdm);
		datosCuentaDestinoBIMConsultar.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", CuentaDestinoBIMTipConsul);
		
		if(status.equals(CuentaDestinoBIMStatusActivo))
			datosCuentaDestinoBIMConsultar.addProperty("Cdb_Status", "A");
		
		if(status.equals(CuentaDestinoBIMStatusPendiente))
			datosCuentaDestinoBIMConsultar.addProperty("Cdb_Status", "I");

		JsonObject cuentaDestinoBIMConsultarRestultado = this.cuentaDestinoServicio.cuentaDestinoBIMConsultar(datosCuentaDestinoBIMConsultar);
		
		Utilerias.verificarError(cuentaDestinoBIMConsultarRestultado);
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoBIMConsultarRestultado, "cuentaDestinoBIM");
		logger.info("- cuentaDestinoBIM: " + cuentaDestinoBIM);

		JsonArray cuentasDestinoBIM = Utilerias.obtenerJsonArrayPropiedad(cuentaDestinoBIM, "cuentasDestinoBIM");

		JsonArray cuentasDestinoBIMArray = new JsonArray();
		JsonObject cuentasDestinoBIMObjeto = new JsonObject();
		
		if(cuentaDestinoBIM.has("cuentasDestinoBIM")) {
			for(JsonElement cuenta : cuentasDestinoBIM) {
				JsonObject cuentaObjeto = (JsonObject)cuenta;
				cuentasDestinoBIMObjeto.add("cdbCuenta", cuentaObjeto.get("Cdb_Cuenta"));
				cuentasDestinoBIMObjeto.add("cdbAlias", cuentaObjeto.get("Cdb_Alias"));
				cuentasDestinoBIMObjeto.add("cdbFecAlt", cuentaObjeto.get("Cdb_FecAlt"));
				cuentasDestinoBIMObjeto.add("cdbRFCBen", cuentaObjeto.get("Cdb_RFCBen"));
				cuentasDestinoBIMObjeto.add("cdbEmaBen", cuentaObjeto.get("Cdb_EmaBen"));
				cuentasDestinoBIMArray.add(cuentasDestinoBIMObjeto);
			}
		}
		
		JsonObject resultado = new JsonObject();
		resultado.add("cuentasDestinoBIM", cuentasDestinoBIMArray);
		
		logger.info("CTRL: Terminando listadoCuentasDestinoBIM metodo...");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
                .build();
	}

	@Path("/alta-bim")
	@POST()
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

		StringBuilder scriptName = new StringBuilder()
				.append(CuentaDestinoCtrl.class.getName())
				.append(".altaCuentaDestinoBIM");
		
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName.toString());

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
		String usuEmail = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuEmail");
		String cliComOrd = Utilerias.obtenerStringPropiedad(cuentasEspeciales, "Cli_ComOrd");
		String usuNombre = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuNombre");

		StringBuilder destinatario = new StringBuilder()
				.append(usuEmail);

		if(!usuUsuAdm.equals(usuNumero)) 
			destinatario
				.append(",")
				.append(cdbEmaBen);		
		
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
		
		correoServicio.enviarCorreo(destinatario.toString(), asunto, cuerpo);
		
		logger.info("CTRL: Finalizando altaCuentaDestinoBIM metodo...");
		return Response.created(null)
				.build();
	}

	@Path("/nacionales")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerCuentasDestinoNacional(@QueryParam("status") String status, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando obtenerCuentasDestinoNacional metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("principalResultadoObjeto: " + principalResultadoObjeto);
		
		if(status == null || status.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.49");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(CuentaDestinoEstadosEnum.validarEstado(status) == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.52");
			bimMessageDTO.addMergeVariable("status", status);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuUsuAdm");
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNumero");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("Cds_Usuari", usuNumero);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		JsonObject cuentaDestinoSPEIConsultarResultado = cuentaDestinoServicio.cuentaDestinoSPEIConsultar(datosCuentaDestinoSPEI);
		
		Utilerias.verificarError(cuentaDestinoSPEIConsultarResultado);
		
		JsonObject cuentaDestinoSPEIConsultarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoSPEIConsultarResultado, "cuentasDestino");
		JsonArray cuentasDestinoArreglo = Utilerias.obtenerJsonArrayPropiedad(cuentaDestinoSPEIConsultarResultadoObjeto, "cuentaDestino");
		
		JsonObject resultado = new JsonObject();
		JsonArray cuentasDestinoNacional = new JsonArray();
		String cuentaDestinoEstado = CuentaDestinoEstadosEnum.validarEstado(status).toString();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");	
		
		for(JsonElement cuentaDestinoElemento : cuentasDestinoArreglo) {
			JsonObject cuentaDestinoObjeto = cuentaDestinoElemento.getAsJsonObject();
			String cdsStatus = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_Status");
			String cdsFecAlt = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_FecAlt");
			
			Date fecAlt = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");		
				fecAlt = sdf.parse(cdsFecAlt);
			} catch (Exception e) {
	            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.43");
				throw new InternalServerException(bimMessageDTO.toString());
			}
			
			if(cdsStatus != null && cdsStatus.equals(cuentaDestinoEstado)) {
				JsonObject cuentaDestino = new JsonObject();
				cuentaDestino.addProperty("cpCLABE", Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_CLABE"));
				cuentaDestino.addProperty("cpAlias", Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_Alias"));
				cuentaDestino.addProperty("cpBanco", Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_Banco").trim());
				cuentaDestino.addProperty("cpFecAlt", fecAlt != null ? simpleDateFormat.format(fecAlt) : "");
				cuentaDestino.addProperty("cpRFCBen", Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_RFCBen"));
				cuentaDestino.addProperty("cpEmaBen", Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_EmaBen"));
				cuentasDestinoNacional.add(cuentaDestino);
			}
		}		
		resultado.add("cuentasDestinoNacional", cuentasDestinoNacional);
		
		logger.info("CTRL: Terminando obtenerCuentasDestinoNacional metodo...");
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("/alta-nacionales")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response altaCuentaDestinoNacional(@Context final Request solicitud, JsonObject cuentaDestinoObjeto) {
		logger.info("CTRL: Comenzando altaCuentaDestinoNacional metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuNumero");
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuUsuAdm");
		String usuClient = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuClient");
		String usuNombre = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuNombre");
		String usuEmail = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuEmail");
		// String usuFolTok = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuFolTok");
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		String usuFolTok = "0416218850";
		
		JsonObject altaCuentaDestinoBim = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoObjeto, "altaCuentaDestinoNacional");
		String cpRSAToken = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cpRSAToken");
		String cdsCLABE = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdsCLABE");
		String cdsBanco = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdsBanco");
		String cdsAlias = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdsAlias");
		String cdsRFCBen = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdsRFCBen");
		String cdsEmaBen = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdsEmaBen");
		String cdsDesAdi = Utilerias.obtenerStringPropiedad(altaCuentaDestinoBim, "cdsDesAdi");		

		String fechaSis = Utilerias.obtenerFechaSis();
		
		String bitPriRef = solicitud.getHeader("User-Agent");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");

		StringBuilder scriptName = new StringBuilder()
				.append(CuentaDestinoCtrl.class.getName())
				.append(".altaCuentaDestinoNacional");
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");
		
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName.toString());

		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
				
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_PriRef", bitPriRef != null ? bitPriRef : "");
		datosBitacora.addProperty("Bit_DireIP", bitDireIP != null ? bitDireIP : "");
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);

		JsonObject bitacoraCreacionOpResultadoObjeto = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		
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

		String tipProces = "F";		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("Cds_CLABE", cdsCLABE);
		datosCuentaDestinoSPEI.addProperty("Cds_Banco", cdsBanco);
		datosCuentaDestinoSPEI.addProperty("Cds_CliUsu",usuClient);
		datosCuentaDestinoSPEI.addProperty("Cds_Alias", cdsAlias);
		datosCuentaDestinoSPEI.addProperty("Cds_RFCBen", cdsRFCBen);
		datosCuentaDestinoSPEI.addProperty("Cds_EmaBen", cdsEmaBen);
		datosCuentaDestinoSPEI.addProperty("Cds_DesAdi", cdsDesAdi);
		datosCuentaDestinoSPEI.addProperty("Cds_Random", cdsEncript);
		datosCuentaDestinoSPEI.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoSPEICreacionResultado = this.cuentaDestinoServicio.cuentaDestinoSPEICreacion(datosCuentaDestinoSPEI);
		logger.info("cuentaDestinoSPEICreacionResultado  " + cuentaDestinoSPEICreacionResultado);
		
		//tipProces = "f" dato en duro, pendiende de validar
		JsonObject datosCuentaDestinoProcesar = new JsonObject();
		datosCuentaDestinoProcesar.addProperty("Cud_UsuAdm", usuUsuAdm);
		datosCuentaDestinoProcesar.addProperty("Cud_CLABE", cdsCLABE);
		datosCuentaDestinoProcesar.addProperty("Cud_Banco", cdsBanco);
		datosCuentaDestinoProcesar.addProperty("Tip_Proces",tipProces);//pendiente validar
		datosCuentaDestinoProcesar.addProperty("NumTransac", numTransac);
		datosCuentaDestinoProcesar.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoProcesarResultado = this.cuentaDestinoServicio.cuentaDestinoProcesar(datosCuentaDestinoProcesar);
		logger.info("cuentaDestinoProcesarResultado  " + cuentaDestinoProcesarResultado);
		
		JsonObject datosCuentaDestinoSPEIAct = new JsonObject();
		datosCuentaDestinoSPEIAct.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEIAct.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEIAct.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoSPEIActivacionResultado = this.cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEIAct);
		logger.info("cuentaDestinoSPEIActivacionResultado  " + cuentaDestinoSPEIActivacionResultado);

		JsonObject datosConfiguracion = new JsonObject();
		datosConfiguracion.addProperty("FechaSis", fechaSis);

		JsonObject configuracionBancoConsultarDetalleResultado = this.configuracionServicio.configuracionBancoConsultarDetalle(datosConfiguracion);
		JsonObject configuracionesBanco = Utilerias.obtenerJsonObjectPropiedad(configuracionBancoConsultarDetalleResultado, "configuracionesBanco");
		JsonArray configuracionBanco = Utilerias.obtenerJsonArrayPropiedad(configuracionesBanco, "configuracionBanco");
		String parMiCuDe = Utilerias.obtenerStringPropiedad(configuracionBanco.get(0).getAsJsonObject(), "Par_MiCuDe");

		String asunto = Utilerias.obtenerPropiedadPlantilla("mail.alta_cuenta_destino_nacional.asunto");
		String plantilla = Utilerias.obtenerPlantilla("alta-cuenta-destino-nacional");
		
		StringBuilder destinatario = new StringBuilder()
				.append(usuEmail);

		if(!usuUsuAdm.equals(usuNumero)) 
			destinatario
				.append(",")
				.append(cdsEmaBen);
		
		StringBuilder cdbCuentaOcu = new StringBuilder()
				.append("**************")
				.append(bitPriRef.substring(bitPriRef.length()-4));
		
		BimEmailTemplateDTO emailTemplateDTO = new BimEmailTemplateDTO(plantilla);
		emailTemplateDTO.addMergeVariable("Str_Client", usuClient);
		emailTemplateDTO.addMergeVariable("Usu_Nombre", usuNombre);
		emailTemplateDTO.addMergeVariable("Cud_Cuenta", cdbCuentaOcu.toString());
		emailTemplateDTO.addMergeVariable("Cud_Alias", cdsAlias);
		emailTemplateDTO.addMergeVariable("Nom_Banco", cdsBanco);
		emailTemplateDTO.addMergeVariable("Cds_Random", cdsEncript);
		emailTemplateDTO.addMergeVariable("TiempoEspera", parMiCuDe);
		String cuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateDTO);
		
		correoServicio.enviarCorreo(destinatario.toString(), asunto, cuerpo);

		
		logger.info("CTRL: Finalizando altaCuentaDestinoNacional método...");
		return Response.created(null)
				.build();
	}
}
