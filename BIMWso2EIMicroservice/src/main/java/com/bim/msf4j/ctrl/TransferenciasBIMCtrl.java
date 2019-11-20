package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;

import com.bim.commons.dto.BimEmailTemplateDTO;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.CorreoServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.TransferenciasBIMServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/transferencias-bim")
public class TransferenciasBIMCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(TransferenciasBIMCtrl.class);

	private TransaccionServicio transaccionServicio;
	private TransferenciasBIMServicio transferenciasBIMServicio;
	private BitacoraServicio bitacoraServicio;
	private TokenServicio tokenServicio;
	private CorreoServicio correoServicio;
	
	public TransferenciasBIMCtrl() {
		super();
		
		logger.info("CTRL: Comenzando metodo init...");
		this.transaccionServicio = new TransaccionServicio();
		this.transferenciasBIMServicio = new TransferenciasBIMServicio();
		this.bitacoraServicio = new BitacoraServicio();
		this.tokenServicio = new TokenServicio();
	}


	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaBIMCreacion(JsonObject datosTransferenciaBIM,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaBIMCreacion metodo");
		logger.info("- datosTransferenciaBIM " + datosTransferenciaBIM);
		String bearerToken = solicitud.getHeader("Authorization");
		logger.info("- bearerToken " + bearerToken);
		JsonObject principal = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principal " + principal);
		
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(principal, "usuUsuAdm");
		String usuClient = Utilerias.obtenerStringPropiedad(principal, "usuClient");
		String usuNumero = Utilerias.obtenerStringPropiedad(principal, "usuNumero");
		String usuEmail = Utilerias.obtenerStringPropiedad(principal, "usuEmail");
		
		JsonObject transferenciaBIM = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIM, "transferencia");
		
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTrasaccionResultado " +  folioTransaccionResultado);
		
		JsonObject folioTransaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion");
		String folTransa = Utilerias.obtenerStringPropiedad(folioTransaccion, "Fol_Transa");
		

		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		// String usuFolTok = Utilerias.obtenerStringPropiedad(principalResultado, "usuFolTok");
		String usuFolTok = "0416218854";
		String cpRSAToken = Utilerias.obtenerStringPropiedad(transferenciaBIM, "cpRSAToken");
		String scriptName = new StringBuilder()
				.append(TransferenciasBIMCtrl.class.getName())
				.append(".transferenciaBIMCreacion")
				.toString();
		logger.info("- scriptName  " + scriptName);
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, folTransa, scriptName);
		logger.info("- validarToken " + validarToken);
		
		String fechaSis = Utilerias.obtenerFechaSis();
		JsonObject datosCuentaDestinoTransferenciaBIMActivacion = new JsonObject();
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("Cdb_UsuAdm", usuClient);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("NumTransac", folTransa);
		datosCuentaDestinoTransferenciaBIMActivacion.addProperty("FechaSis", fechaSis);
		JsonObject datosCuentaDestinoTransferenciaBIMActivacionResultado = this.transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion );
		logger.info("- datosCuentaDestinoTransferenciaBIMActivacionResultado " + datosCuentaDestinoTransferenciaBIMActivacionResultado);
		
		String trbCueOri = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbCueOri");
		String trbCueDes = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbCueDes");
		Double trbMonto = Utilerias.obtenerDoublePropiedad(transferenciaBIM, "trbMonto");
		String trbDescri = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbDescri");
		
		JsonObject datosTransferenciaBIMCreacion = new JsonObject();
		datosTransferenciaBIMCreacion.addProperty("Trb_Client", usuClient);
		datosTransferenciaBIMCreacion.addProperty("Trb_CueOri", trbCueOri);
		datosTransferenciaBIMCreacion.addProperty("Trb_CueDes", trbCueDes);
		datosTransferenciaBIMCreacion.addProperty("Trb_Monto", trbMonto);
		datosTransferenciaBIMCreacion.addProperty("Trb_MonOri", trbMonto.toString());
		datosTransferenciaBIMCreacion.addProperty("Trb_MonDes", trbMonto.toString());
		datosTransferenciaBIMCreacion.addProperty("Trb_Descri", trbDescri);
		datosTransferenciaBIMCreacion.addProperty("Trb_UsuCap", usuNumero);
		// El Trb_FecAut se deja en duro debido a que no se ha contemplado el flujo de transferencias programadas
		datosTransferenciaBIMCreacion.addProperty("Trb_FecAut", "1900-01-01 00:00:00");
		// El Trb_FePrEn se deja en duro debido a que no se ha contemplado el flujo de transferencias programadas
		datosTransferenciaBIMCreacion.addProperty("Trb_FePrEn", "1900-01-01 00:00:00");
		// El Trb_DurFec se deja en duro debido a que no se ha contemplado el flujo de transferencias programadas
		datosTransferenciaBIMCreacion.addProperty("Trb_DurFec", "1900-01-01 00:00:00");
		datosTransferenciaBIMCreacion.addProperty("Trb_EmaBen", usuEmail);
		datosTransferenciaBIMCreacion.addProperty("NumTransac", folTransa);
		datosTransferenciaBIMCreacion.addProperty("Trb_TipTra", "I");
		datosTransferenciaBIMCreacion.addProperty("Trb_Frecue", "U");
		datosTransferenciaBIMCreacion.addProperty("FechaSis", fechaSis);
		JsonObject datosTransferenciaBIMCreacionResultado = this.transferenciasBIMServicio.transferenciaBIMCreacion(datosTransferenciaBIMCreacion );
		logger.info("- datosTransferenciaBIMCreacionResultado " + datosTransferenciaBIMCreacionResultado);
		
		JsonObject transferenciaBIMCreacion = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIMCreacionResultado, "transferenciaBIM");
		String trbConsec = Utilerias.obtenerStringPropiedad(transferenciaBIMCreacion, "Trb_Consec");
		
		String bitPriRef = new StringBuilder()
				.append("Cuenta: ")
				.append(trbCueDes)
				.append(" BIM")
				.toString();
		String bitSegRef = new StringBuilder()
				.append("$ ")
				.append(trbMonto)
				.append(" Pesos")
				.toString();
		String bitDireIP = solicitud.getHeader("X_Forwarded_For");
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_CueOri", trbCueOri);
		datosBitacora.addProperty("Bit_CueDes", trbCueDes);
		datosBitacora.addProperty("Bit_Monto", trbMonto.toString()                          );
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		datosBitacora.addProperty("Bit_SegRef", bitSegRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("FechaSis", fechaSis);
		JsonObject datosBitacoraResultado = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info("- datosBitacoraResultado " + datosBitacoraResultado);
		
		JsonObject datosTransferenciaBIMProcesar = new JsonObject();
		datosTransferenciaBIMProcesar.addProperty("Trb_UsuAdm", usuUsuAdm);
		datosTransferenciaBIMProcesar.addProperty("Trb_Usuari", usuNumero);
		datosTransferenciaBIMProcesar.addProperty("Trb_Client", usuClient);
		datosTransferenciaBIMProcesar.addProperty("Trb_Consec", trbConsec);
		datosTransferenciaBIMProcesar.addProperty("Trb_CueOri", trbCueOri);
		datosTransferenciaBIMProcesar.addProperty("Trb_CueDes", trbCueDes);
		datosTransferenciaBIMProcesar.addProperty("Trb_Monto", trbMonto);
		datosTransferenciaBIMProcesar.addProperty("Trb_SegRef", trbDescri);
		datosTransferenciaBIMProcesar.addProperty("NumTransac", folTransa);
		datosTransferenciaBIMProcesar.addProperty("FechaSis", fechaSis);
		JsonObject datosTransferenciaBIMProcesarResultado = this.transferenciasBIMServicio.transferenciaBIMProcesar(datosTransferenciaBIMProcesar );
		logger.info("- datosTransferenciaBIMProcesarResultado " +  datosTransferenciaBIMProcesarResultado);
		
		JsonObject datosTransferenciaBIMConsultar = new JsonObject();
		datosTransferenciaBIMConsultar.addProperty("Trb_UsuAdm", usuUsuAdm);
		datosTransferenciaBIMConsultar.addProperty("Trb_Usuari", usuNumero);
		datosTransferenciaBIMConsultar.addProperty("FechaSis", fechaSis);
		datosTransferenciaBIMConsultar.addProperty("Trb_Consec", "");
		JsonObject datosTransferenciaBIMConsultarResultado = this.transferenciasBIMServicio.transferenciasBIMConsultar(datosTransferenciaBIMConsultar );
		logger.info("- datosTransferenciaBIMConsultarResultado " + datosTransferenciaBIMConsultarResultado);
		
		JsonObject transferenciasBIMConsultar = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIMConsultarResultado, "transferenciasBIM");
		JsonObject transferenciaBIMConsultar = Utilerias.obtenerJsonObjectPropiedad(transferenciasBIMConsultar, "transferenciaBIM");
		
		String trbDeCuOr = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_DeCuOr");
		String corAlias = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Cor_Alias");
		String trbDeCuDe = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_DeCuDe");
		String cdbAlias = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Cdb_Alias");
		String trbMonPes = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_MonPes");
		String trbFecAut = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_FecAut");
		String trbUsCaNo = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_UsCaNo");
		
		JsonObject datosTransferenciaBIMExitosa = new JsonObject();
		datosTransferenciaBIMExitosa.addProperty("trbDeCuOr", trbDeCuOr);
		datosTransferenciaBIMExitosa.addProperty("corAlias", corAlias);
		datosTransferenciaBIMExitosa.addProperty("trbDeCuDe", trbDeCuDe);
		datosTransferenciaBIMExitosa.addProperty("cdbAlias", cdbAlias);
		datosTransferenciaBIMExitosa.addProperty("trbMonPes", trbMonPes);
		datosTransferenciaBIMExitosa.addProperty("trbDescri", trbDescri);
		datosTransferenciaBIMExitosa.addProperty("numTransac", folTransa);
		datosTransferenciaBIMExitosa.addProperty("trbFecAut", trbFecAut);
		datosTransferenciaBIMExitosa.addProperty("trbUsCaNo", trbUsCaNo);
		
		JsonObject transferenciaBIMExitosa = new JsonObject();
		transferenciaBIMExitosa.add("transferenciaExitosa", datosTransferenciaBIMExitosa);
		
		String sVarVerif = new StringBuilder()
				.append("Origen: ")
				.append(trbCueOri)
				.append(" ")
				.append("Destino: ")
				.append(trbCueDes)
				.append(" ")
				.append("Cantidad: ")
				.append(trbMonto.toString())
				.append(" ")
				.append("Folio: ")
				.append(folTransa)
				.toString();
		String digitoVerificador = Utilerias.generarDigitoVerificador(sVarVerif);
		logger.info("- digitoVerificador " + digitoVerificador);
		
		String transferenciaBIMClientAsunto = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.cliente.asunto");
		String transferenciaBIMClientPlantilla = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.cliente.plantilla");
		BimEmailTemplateDTO emailTemplateTransferenciaBIMClienteDTO = new BimEmailTemplateDTO(transferenciaBIMClientPlantilla);
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("str_Firmas", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_ProDes", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DeCuOr", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DeCuDe", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DesMon", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_TipCam", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_StrEqu", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_Descri", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_RFC", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_IVA", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("NumTransac", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_UsCaNo", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecCapDDMMYYYY", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecCapHHMM", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("str_Autori", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecAutDDMMYYYY", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecAutHHMM", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FePrEnDDMMYYYY", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("strFrecuencia", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("strDuracion", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("strRecordatorio", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador170", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador7170", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador14170", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador21170", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("verificador28170", "");
		String transferenciaBIMClienteCuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateTransferenciaBIMClienteDTO);
		logger.info("- usuEmail " + usuEmail);
		logger.info("- transferenciaBIMClientAsunto " + transferenciaBIMClientAsunto);
		logger.info("- transferenciaBIMClienteCuerpo " + transferenciaBIMClienteCuerpo);
		this.correoServicio.enviarCorreo(usuEmail, transferenciaBIMClientAsunto, transferenciaBIMClienteCuerpo);
		
		String transferenciaBIMBeneficiarioAsunto = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.beneficiario.asunto");
		String transferenciaBIMBeneficiarioPlantilla = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.beneficiario.plantilla");
		BimEmailTemplateDTO emailTemplateTransferenciaBIMBeneficiarioDTO = new BimEmailTemplateDTO(transferenciaBIMBeneficiarioPlantilla);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DeCuOr", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DeCuDe", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_Monto", ""); 
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DesMon", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_TipCam", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_MonEqu", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_StrEqu", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_Descri", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("NumTransac", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCarDDMMYYYY", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCarHHMM", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCarHHMM", "");
		String transferenciaBIMBeneficiarioCuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateTransferenciaBIMBeneficiarioDTO);
		logger.info("- usuEmail " + usuEmail);
		logger.info("- transferenciaBIMBeneficiarioAsunto " + transferenciaBIMBeneficiarioAsunto);
		logger.info("- emailTemplateTransferenciaBIMBeneficiarioDTO " + emailTemplateTransferenciaBIMBeneficiarioDTO);
		this.correoServicio.enviarCorreo(usuEmail, transferenciaBIMBeneficiarioAsunto, transferenciaBIMBeneficiarioCuerpo);
		logger.info("CTRL: Terminando transferenciaBIMCreacion metodo");
		return Response
				.ok(transferenciaBIMExitosa, MediaType.APPLICATION_JSON)
				.build();
	}
	
}
