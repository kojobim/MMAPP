package com.bim.msf4j.ctrl;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.service.AvisoPrivacidadServicio;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.PasswordServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.TransferenciasBIMServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/usuario")
public class UsuarioCtrl extends BimBaseCtrl {

	private static Logger logger = Logger.getLogger(UsuarioCtrl.class);
	
	private static String CuentaDestinoBIMConsultarTipConsulL2;
	private CuentaDestinoServicio cuentaDestinoServicio;
	private TransaccionServicio transaccionServicio;
	private AvisoPrivacidadServicio avisoPrivacidadServicio;
	private TransferenciasBIMServicio transferenciasBIMServicio;
	private TokenServicio tokenServicio;
	private PasswordServicio passwordServicio;
	private UsuarioServicio usuarioServicio;
	private BitacoraServicio bitacoraServicio;
	
	static {
		CuentaDestinoBIMConsultarTipConsulL2 = properties.getProperty("op.cuenta_destino_bim_consultar.tip_consul.l2");	
	}
	
	public UsuarioCtrl() {
		super();
		
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.avisoPrivacidadServicio = new AvisoPrivacidadServicio();
		this.transferenciasBIMServicio = new TransferenciasBIMServicio();
		this.tokenServicio = new TokenServicio();
		this.passwordServicio = new PasswordServicio();
		this.usuarioServicio = new UsuarioServicio();
		this.bitacoraServicio = new BitacoraServicio();

	}

	@Path("/cuentas-destino-nacionales")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasDestinoNacionalesListado(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando cuentasDestinoNacionalesListado metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuAdm = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuUsuAdm");
		logger.info("- usuAdm " + usuAdm);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjeto, "usuNumero");
		logger.info("- usuNumero " + usuNumero);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		logger.info("- fechaSis " + fechaSis);

		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTransaccionGenerarOpResultadoObjeto: " + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");

		JsonObject datosCuentaDestinoSPEIActivacion = new JsonObject();
		datosCuentaDestinoSPEIActivacion.addProperty("Cds_UsuAdm", usuAdm);
		datosCuentaDestinoSPEIActivacion.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEIActivacion.addProperty("FechaSis", fechaSis);
		JsonObject cuentaDestinoSPEIActivacion = this.cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEIActivacion);
		logger.info("- cuentaDestinoSPEIActivacion: " + cuentaDestinoSPEIActivacion);

		Utilerias.verificarError(cuentaDestinoSPEIActivacion);
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuAdm);
		datosCuentaDestinoSPEI.addProperty("Cds_Usuari", usuNumero);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		JsonObject cuentasDestinoSPEIRespuesta = this.cuentaDestinoServicio.cuentaDestinoSPEIConsultar(datosCuentaDestinoSPEI );
		logger.info("- cuentasDestinoSPEIRespuesta " + cuentasDestinoSPEIRespuesta);
		
		JsonObject cuentasDestinoObjeto = Utilerias.obtenerJsonObjectPropiedad(cuentasDestinoSPEIRespuesta, "cuentasDestino");
		JsonArray cuentaDestinoArray = Utilerias.obtenerJsonArrayPropiedad(cuentasDestinoObjeto, "cuentaDestino");
		
		JsonArray cuentasDestinoResultado = new JsonArray();
		
		if(cuentaDestinoArray != null) {
			for(JsonElement cuentaDestinoElemento : cuentaDestinoArray) {
				JsonObject cuentaDestinoElementoObjeto = (JsonObject) cuentaDestinoElemento;
				JsonObject cuentaDestinoResultado = new JsonObject();
				cuentaDestinoResultado.addProperty("cdsCLABE", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_CLABE"));
				cuentaDestinoResultado.addProperty("cdsConsec", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_Consec"));
				cuentaDestinoResultado.addProperty("cdsDesCue", Utilerias.obtenerStringPropiedad(cuentaDestinoElementoObjeto, "Cds_DesCue"));
				cuentasDestinoResultado.add(cuentaDestinoResultado);
			}
		}
		
		JsonObject cuentasDestino = new JsonObject();
		cuentasDestino.add("cuentasDestino", cuentasDestinoResultado);
		logger.info("- cuentasDestino " + cuentasDestino);
		logger.info("CTRL: Finalizando cuentasDestinoNacionalesListado metodo");
		
		return Response.ok(cuentasDestino.toString(),MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("/aviso-privacidad")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response verificarAvisoPrivacidad(@Context Request solicitud) {
		logger.info("CTRL: Comenzando verificarAvisoPrivacidad metodo...");
		
		logger.info("Authorization " + solicitud.getHeader("Authorization"));

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principal " + principalResultadoObjeto);

		JsonObject folioTransaccionGenerarResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		
		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarResultadoObjeto,"transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(transaccion,"Fol_Transa");
		logger.info("- folTransa " + folTransa);

		String fechaSis = Utilerias.obtenerFechaSis();
		
		String usuNumero = principalResultadoObjeto.get("usuNumero").getAsString();
		
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Usu_Numero", usuNumero);
		datosAvisoPrivacidad.addProperty("NumTransac", folTransa);
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		
		JsonObject avisoPrivacidadVerificarObjeto = this.avisoPrivacidadServicio.avisoPrivacidadVerificar(datosAvisoPrivacidad);
		logger.info("- avisoPrivacidadVerificarObjeto " + avisoPrivacidadVerificarObjeto);
		
		Utilerias.verificarError(avisoPrivacidadVerificarObjeto);
		
		JsonObject avisoPrivacidad = Utilerias.obtenerJsonObjectPropiedad(avisoPrivacidadVerificarObjeto, "avisoPrivacidad");
		
		Boolean usuAceAvi = Boolean.parseBoolean(Utilerias.obtenerStringPropiedad(avisoPrivacidad,"Usu_AceAvi"));
		logger.info("- usuAceAvi " + usuAceAvi);

		JsonObject avisoPrivacidadResultadoObjeto = new JsonObject();
		avisoPrivacidadResultadoObjeto.addProperty("cpAvisoAceptado", usuAceAvi);
		
		JsonObject avisoPrivacidadAceptado = new JsonObject();
		avisoPrivacidadAceptado.add("avisoPrivacidadAceptado", avisoPrivacidadResultadoObjeto);
		logger.info("CTRL: Finalizando verificarAvisoPrivacidad metodo");
		return Response.ok(avisoPrivacidadAceptado.toString(), MediaType.APPLICATION_JSON)
				.build();	
	}
	
	@Path("/aviso-privacidad")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	public Response aceptarAvisoPrivacidad(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando aceptarAvisoPrivacidad metodo...");
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		
		JsonObject folioTransaccionGenerarResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarResultadoObjeto" + folioTransaccionGenerarResultadoObjeto);

		String FolioTransaccionGenerarFolTransa = folioTransaccionGenerarResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosAvisoPrivacidad = new JsonObject();
		datosAvisoPrivacidad.addProperty("Usu_Numero", usuNumero);
		datosAvisoPrivacidad.addProperty("Usu_Client", usuClient);
		datosAvisoPrivacidad.addProperty("Usu_FecAce", fechaSis);
		datosAvisoPrivacidad.addProperty("Usu_FecAct", fechaSis);
		datosAvisoPrivacidad.addProperty("NumTransac", FolioTransaccionGenerarFolTransa);
		datosAvisoPrivacidad.addProperty("FechaSis", fechaSis);
		
		JsonObject avisoPrivacidadActualizacionResultado = avisoPrivacidadServicio.avisoPrivacidadActualizacion(datosAvisoPrivacidad);
		JsonObject avisoPrivacidadActualizacionResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(avisoPrivacidadActualizacionResultado, "avisoPrivacidad");
		String errCodigo = Utilerias.obtenerStringPropiedad(avisoPrivacidadActualizacionResultadoObjeto, "Err_Codigo");
		
		if(!"000000".equals(errCodigo)) {
			String errMensaj = Utilerias.obtenerStringPropiedad(avisoPrivacidadActualizacionResultadoObjeto, "Err_Mensaj");			
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		logger.info("CTRL: Terminando aceptarAvisoPrivacidad metodo...");
		return Response.noContent()
				.build();
	}

	@Path("/cuentas-origen")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasOrigenListado(@Context final Request solicitud) {
		logger.info("CTRL: Empezando cuentasOrigenListado metodo...");

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultadoObjecto, "usuNumero");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosCuentasOrigenConsultar = new JsonObject();
		datosCuentasOrigenConsultar.addProperty("Cor_Usuari", usuNumero);
		datosCuentasOrigenConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentasOrigenRespuesta = this.transferenciasBIMServicio
				.cuentasOrigenConsultar(datosCuentasOrigenConsultar);
		logger.info("- cuentasOrigenRespuesta " + cuentasOrigenRespuesta);
		
		JsonObject cuentas = Utilerias.obtenerJsonObjectPropiedad(cuentasOrigenRespuesta, "cuentasOrigen");
		
		JsonArray cuentasOrigen = Utilerias.obtenerJsonArrayResultante(cuentas, "cuentaOrigen"); 
			
		JsonArray cuentasOrigenArray = new JsonArray();
		
		for(JsonElement cuentaOrigenElemento : cuentasOrigen) {
			JsonObject cuentaOrigenItem = (JsonObject) cuentaOrigenElemento;
			JsonObject cuentaOrigen = new JsonObject();
			cuentaOrigen.addProperty("corCuenta", Utilerias.obtenerStringPropiedad(cuentaOrigenItem, "Cor_Cuenta"));
			cuentaOrigen.addProperty("corCueFor", Utilerias.obtenerStringPropiedad(cuentaOrigenItem, "Cor_CueFor"));
			cuentaOrigen.addProperty("cueDispon", Utilerias.obtenerDoublePropiedad(cuentaOrigenItem, "Cue_Dispon"));
			cuentasOrigenArray.add(cuentaOrigen);
		}
		
		JsonObject cuentasOrigenResultado = new JsonObject();
		cuentasOrigenResultado.add("cuentasOrigen", cuentasOrigenArray);

		logger.info("CTRL: Terminando cuentasOrigenListado metodo...");
		return Response.ok(cuentasOrigenResultado, MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/cuentas-destino-bim")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cuentasDestinoBimListado(@Context final Request solicitud) {
		logger.info("CTRL: Comenzando cuentasDestinoBimListado metodo...");

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuUseAdm = principalResultadoObjecto.get("usuUsuAdm").getAsString();
		String fechaSis = Utilerias.obtenerFechaSis();

		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTransaccionGenerarOpResultadoObjeto: " + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		String numTransac = Utilerias.obtenerStringPropiedad(transaccion, "Fol_Transa");

		JsonObject datosCuentaDestinoTransferenciaBIMActivacion = new JsonObject();
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Cdb_UsuAdm", usuUseAdm);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("NumTransac", numTransac);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("FechaSis", fechaSis);

		JsonObject cuentaDestinoTransferenciaBIMActivacionResultado = this.transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion);
		logger.info("- cuentaDestinoTransferenciaBIMActivacionResultado: " + cuentaDestinoTransferenciaBIMActivacionResultado);
		Utilerias.verificarError(cuentaDestinoTransferenciaBIMActivacionResultado);
		
		JsonObject datosCuentaDestinoBIMConsultar = new JsonObject();
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_UsuAdm", usuUseAdm);
		datosCuentaDestinoBIMConsultar.addProperty("Cdb_Usuari", usuNumero);
		datosCuentaDestinoBIMConsultar.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoBIMConsultar.addProperty("Tip_Consul", CuentaDestinoBIMConsultarTipConsulL2);
		JsonObject  datosCuentaDestinoBIMConsultarResultado = this.cuentaDestinoServicio.cuentaDestinoBIMConsultar(datosCuentaDestinoBIMConsultar );
		logger.info("- datosCuentaDestinoBIMConsultarResultado  "  + datosCuentaDestinoBIMConsultarResultado);
		
		JsonObject cuentaDestinoBIM = Utilerias.obtenerJsonObjectPropiedad(datosCuentaDestinoBIMConsultarResultado, "cuentaDestinoBIM");		
		JsonArray cuentasDestinoBIM = Utilerias.obtenerJsonArrayResultante(cuentaDestinoBIM, "cuentasDestinoBIM");
		
		JsonArray cuentasDestinoBIMResultado = new JsonArray();		
		if(cuentasDestinoBIM != null) {
			for(JsonElement cuentaDestinoBIMElemento: cuentasDestinoBIM) {
				JsonObject cuentaDestinoBIMItem = (JsonObject) cuentaDestinoBIMElemento;
				JsonObject cuentaDestinoBIMResultado = new JsonObject();
				cuentaDestinoBIMResultado.addProperty("cdbCuenta", Utilerias.obtenerStringPropiedad(cuentaDestinoBIMItem, "Cdb_Cuenta"));
				cuentaDestinoBIMResultado.addProperty("cdbCueFor", Utilerias.obtenerStringPropiedad(cuentaDestinoBIMItem, "Cdb_CueFor"));
				cuentasDestinoBIMResultado.add(cuentaDestinoBIMResultado);
			}
		}
		
		JsonObject cuentasDestinoRespuesta = new JsonObject();
		cuentasDestinoRespuesta.add("cuentasDestino", cuentasDestinoBIMResultado);
		logger.info("CTRL: Terminando cuentasDestinoBimListado metodo...");
		return Response.ok(cuentasDestinoRespuesta, MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/password")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response actualizaPassword(
					JsonObject datosCambiarPassword,
					@HeaderParam("Authorization")final String token,
					@HeaderParam("X-Forwarded-For")final String bitDireIP) {
		
		logger.info("CTRL: Comenzando cambiarPassword metodo.....");

		datosCambiarPassword = Utilerias
				.obtenerJsonObjectPropiedad(datosCambiarPassword, "actualizaPassword"); //obtener inner object
		
		//inicia declaracion de variables
		String cpRSAToken = null;
		String actPasswo = null;
		String usuPasswo = null;
		String conPasswo = null;
		String usuFolTok = "0416218854"; //valor hard-code hasta integracion de servicio login
		String validarToken = null;
		String numTransac = null;
		String usuClave = null;
		String usuNumero = null;
		String scriptName = null;
		String usuUsuAdm = null;
	
		JsonObject bitacoraResultado = null;
		JsonObject transac = null;
		JsonObject usuario = null;
		JsonObject usuarioAct = null;
		JsonObject servicioRequest = null;
		JsonObject usuarioParamAct = null;
		JsonObject cuentasEspeciales = null;
		BimMessageDTO bimMessageDTO = null;
		//termina declaracion de variables
		
		// asignacion de variables para su manipulacion
		cpRSAToken = Utilerias.obtenerStringPropiedad(datosCambiarPassword, "cpRSAToken");
		actPasswo = Utilerias.obtenerStringPropiedad(datosCambiarPassword, "actPasswo");
		usuPasswo = Utilerias.obtenerStringPropiedad(datosCambiarPassword, "usuPasswo");
		conPasswo = Utilerias.obtenerStringPropiedad(datosCambiarPassword, "conPasswo");		
		//termina la asignacion de variables para su manipulacion
		
		//se valida que todos los parametros obligatorios se encuentren presentes		
		if(Strings.isNullOrEmpty(cpRSAToken)){
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "cpRSAToken");
			throw new BadRequestException(bimMessageDTO.toString());
			
		} else if(Strings.isNullOrEmpty(actPasswo)) {
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "actPasswo");
			throw new BadRequestException(bimMessageDTO.toString());
			
		} else if(Strings.isNullOrEmpty(usuPasswo)) {
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "usuPasswo");
			throw new BadRequestException(bimMessageDTO.toString());
			
		} else if(Strings.isNullOrEmpty(conPasswo)) {
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "conPasswo");
			throw new BadRequestException(bimMessageDTO.toString());
			
		}
		
		//obtener principal y propiedades para su manipulacion
		usuario = Utilerias.obtenerPrincipal(token); 
		usuClave = Utilerias.obtenerStringPropiedad(usuario, "usuClave");
		usuNumero = Utilerias.obtenerStringPropiedad(usuario, "usuNumero");
		usuUsuAdm = Utilerias.obtenerStringPropiedad(usuario, "usuUsuAdm");
		
		// se validan casos de error especificados en DSS
		if(!StringUtils.equals(usuPasswo, conPasswo)) { // nuevo password y confirmacion son distintos
		
		}
		
		if(StringUtils.equals(usuPasswo, usuClave)){ //password igual a clave de usuario 
			
		}
		
		scriptName = new StringBuilder()
				.append(UsuarioCtrl.class.getName())
				.append(".actualizaPassword").toString();
		
		transac =this.transaccionServicio.folioTransaccionGenerar(); //obtener transaccion para operaciones siguientes
		transac = Utilerias.obtenerJsonObjectPropiedad(transac, "transaccion"); //obteniendo inner object
		numTransac = Utilerias.obtenerStringPropiedad(transac, "Fol_Transa"); //obteniendo numero de transaccion
		
		validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName);
		
		if ("B".equals(validarToken)) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new ForbiddenException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		// consultado usuario
		servicioRequest = new JsonObject();
		servicioRequest.addProperty("Ces_Usuari", usuNumero);
		servicioRequest.addProperty("NumTransac", numTransac);
		servicioRequest.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		
		cuentasEspeciales = this.passwordServicio.cuentasEspecialesConsulta(servicioRequest);
		cuentasEspeciales = Utilerias.obtenerJsonObjectPropiedad(cuentasEspeciales, "cuentasEspeciales");

		actPasswo = Racal.cifraPassword_HSM(actPasswo); // cifrar password actual
		
		if(actPasswo.length() == 7 && actPasswo.equals("autoriz")) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(actPasswo.isEmpty()) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		//el password proporcionado como actual no coincide con el almacenado en Sybase
		if(StringUtils.equals(actPasswo, Utilerias.obtenerStringPropiedad(cuentasEspeciales, "Usu_Passwo"))) { 
			
		}
		
		usuPasswo = Racal.cifraPassword_HSM(usuPasswo); //encriptando el nuevo password
		
		if(usuPasswo.length() == 7 && usuPasswo.equals("autoriz")) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(usuPasswo.isEmpty()) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		// guardando nuevo password de usuario....
		servicioRequest = new JsonObject();
		servicioRequest.addProperty("Usu_Numero", usuNumero);
		servicioRequest.addProperty("Usu_Passwo", usuPasswo);
		servicioRequest.addProperty("Tip_Actual", "E");
		servicioRequest.addProperty("NumTransac", numTransac);
		servicioRequest.addProperty("Usu_UsuAdm", usuUsuAdm);
		servicioRequest.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		
		usuarioAct = this.usuarioServicio.usuarioActualizar(servicioRequest);
		usuarioAct = Utilerias.obtenerJsonObjectPropiedad(usuarioAct, "usuario"); //obteniendo inner property usuario
		
		if(usuarioAct.has("Err_Codigo")) { //posible error proveniente de SP
			
			// el password que intenta utilizarse ya fue usado con anterioridad
			if(StringUtils.equals(Utilerias.obtenerStringPropiedad(usuarioAct, "Err_Codigo"), "000001")) { 
				
			}
			
		}
		
		// actualizando parametros
		servicioRequest = new JsonObject();
		servicioRequest.addProperty("Pau_Usuari", usuNumero);
		servicioRequest.addProperty("Tip_Actual", "D");
		servicioRequest.addProperty("NumTransac", numTransac);		
		servicioRequest.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		
		usuarioParamAct = this.usuarioServicio.usuarioParametrosActualizacion(servicioRequest);
		
		if(usuarioParamAct.has("REQUEST_STATUS")){ //obtenida respuesta de servidor
			
			// No se obtuvo una respuesta exitosa por parte del servidor...
			if(! StringUtils.equals(Utilerias.obtenerStringPropiedad(usuarioParamAct, "REQUEST_STATUS"), "SUCCESSFUL")){
				
			}
			
		}
		
		
		// guardado en bitacora
		servicioRequest = new JsonObject();
		servicioRequest.addProperty("Bit_Usuari", usuNumero);
		servicioRequest.addProperty("Bit_Fecha", Utilerias.obtenerFechaSis());
		servicioRequest.addProperty("Bit_TipOpe", "120");
		servicioRequest.addProperty("NumTransac", numTransac);
		servicioRequest.addProperty("FechaSis", Utilerias.obtenerFechaSis());
		servicioRequest.addProperty("Bit_DireIP", bitDireIP);
		
		bitacoraResultado = this.bitacoraServicio.creacionBitacora(servicioRequest);

		if(bitacoraResultado.has("REQUEST_STATUS")){ //obtenida respuesta de servidor
			
			// No se obtuvo una respuesta exitosa por parte del servidor al guardar en la bitacora...
			if(! StringUtils.equals(Utilerias.obtenerStringPropiedad(bitacoraResultado, "REQUEST_STATUS"), "SUCCESSFUL")){
				
			}
			
		}
		
		return Response.noContent().build();
	}
	
}
