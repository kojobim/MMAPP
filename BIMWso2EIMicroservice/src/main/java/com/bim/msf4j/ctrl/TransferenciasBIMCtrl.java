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
import com.google.gson.JsonArray;
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
		this.correoServicio = new CorreoServicio();
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
		String trbRFC = Utilerias.obtenerStringPropiedad(transferenciaBIM, "trbRFC");
		Double trbIVA = Utilerias.obtenerDoublePropiedad(transferenciaBIM, "trbIVA"); 
		
		JsonObject datosTransferenciaBIMCreacion = new JsonObject();
		datosTransferenciaBIMCreacion.addProperty("Trb_RFC", trbRFC);
		datosTransferenciaBIMCreacion.addProperty("Trb_IVA", trbIVA);
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
		datosBitacora.addProperty("Bit_TipOpe", "085");
		JsonObject datosBitacoraResultado = this.bitacoraServicio.creacionBitacora(datosBitacora);
		logger.info("- datosBitacoraResultado " + datosBitacoraResultado);
		
		JsonObject datosCuentaDestinoTransferenciaBIMActivacionResultado2 = this.transferenciasBIMServicio.cuentaDestinoTransferenciaBIMActivacion(datosCuentaDestinoTransferenciaBIMActivacion );
		logger.info("- datosCuentaDestinoTransferenciaBIMActivacionResultado2 " + datosCuentaDestinoTransferenciaBIMActivacionResultado2);
		
		JsonObject datosTransferenciaBIMConsultar = new JsonObject();
		datosTransferenciaBIMConsultar.addProperty("Trb_UsuAdm", usuUsuAdm);
		datosTransferenciaBIMConsultar.addProperty("Trb_Usuari", usuNumero);
		datosTransferenciaBIMConsultar.addProperty("FechaSis", fechaSis);
		datosTransferenciaBIMConsultar.addProperty("Tip_Consul", "L2");
		JsonObject datosTransferenciaBIMConsultarResultado = this.transferenciasBIMServicio.transferenciasBIMConsultar(datosTransferenciaBIMConsultar );
		logger.info("- datosTransferenciaBIMConsultarResultado " + datosTransferenciaBIMConsultarResultado);

		JsonObject transferenciasBIMConsultar = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaBIMConsultarResultado, "transferenciasBIM");
		logger.info("- trbConsec " + trbConsec);
		JsonObject transferenciaBIMConsultar = Utilerias.obtenerJsonObjectPropiedad(transferenciasBIMConsultar, "transferenciaBIM");
		logger.info("- transferenciaBIMConsultar " + transferenciaBIMConsultar);
		
		JsonObject datosTransferenciaBIMFirmasConsultar = new JsonObject();
		datosTransferenciaBIMFirmasConsultar.addProperty("Ftb_Consec", trbConsec);
		datosTransferenciaBIMFirmasConsultar.addProperty("FechaSis", fechaSis);
		JsonObject transferenciaBIMFirmasConsultarResultado = this.transferenciasBIMServicio.transferenciaBIMFirmasConsultar(datosTransferenciaBIMFirmasConsultar );
		
		JsonObject tranferenciaBIMFirmas = Utilerias.obtenerJsonObjectPropiedad(transferenciaBIMFirmasConsultarResultado, "transferenciaBIMFirmas");
		
		String ftbConse = Utilerias.obtenerStringPropiedad(tranferenciaBIMFirmas, "Ftb_Consec");
		String ftbNivFir = Utilerias.obtenerStringPropiedad(tranferenciaBIMFirmas, "Ftb_NivFir");
		String ftbCantid = Utilerias.obtenerStringPropiedad(tranferenciaBIMFirmas, "Ftb_Cantid");
		String trbFecCap = Utilerias.obtenerStringPropiedad(transferenciaBIM, "Trb_FecCap");
		
		JsonObject datosTransferenciaBIMProcesar = new JsonObject();
		datosTransferenciaBIMProcesar.addProperty("Trb_RFC", trbRFC);
		datosTransferenciaBIMProcesar.addProperty("Trb_IVA", trbIVA);
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

		String trbDeCuOr = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_DeCuOr");
		String corAlias = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Cor_Alias");
		String trbDeCuDe = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_DeCuDe");
		String cdbAlias = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Cdb_Alias");
		String trbMonPes = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_MonPes");
		String trbFecAut = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_FecAut");
		String trbUsCaNo = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_UsCaNo");
		String trbEmaBen = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_EmaBen");
		String trbDesMon = Utilerias.obtenerStringPropiedad(transferenciaBIMConsultar, "Trb_DesMon");
		
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
		
		String transferenciaBIMClienteAsunto = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.cliente.asunto");
		String transferenciaBIMClientePlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.cliente.plantilla");
		String transferenciaBIMClientePlantilla = Utilerias.obtenerPlantilla(transferenciaBIMClientePlantillaName);
		BimEmailTemplateDTO emailTemplateTransferenciaBIMClienteDTO = new BimEmailTemplateDTO(transferenciaBIMClientePlantilla);
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DeCuOr", Utilerias.formatearCuenta(trbCueOri, 2));
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DeCuDe", Utilerias.formatearCuenta(trbCueDes, 2));
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_Monto", trbMonto.toString()); 
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_DesMon", trbDesMon);
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_TipCam", trbDesMon);
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_MonEqu", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_StrEqu", "");
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_Descri", trbDescri);
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("NumTransac", folTransa);
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecCarDDMMYYYY", Utilerias.formatearFecha(trbFecCap, "dd-MM-yyyy"));
		emailTemplateTransferenciaBIMClienteDTO.addMergeVariable("Trb_FecCarHHMM", Utilerias.formatearFecha(trbFecCap, "HH:mm"));
		
		String transferenciaBIMClienteCuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateTransferenciaBIMClienteDTO);
		logger.info("- usuEmail " + usuEmail);
		logger.info("- transferenciaBIMClientAsunto " + transferenciaBIMClienteAsunto);
		this.correoServicio.enviarCorreo("penriquez@mediomelon.mx", transferenciaBIMClienteAsunto, transferenciaBIMClienteCuerpo);
		
		String transferenciaBIMBeneficiarioAsunto = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.beneficiario.asunto");
		String transferenciaBIMBeneficiarioPlantillaName = Utilerias.obtenerPropiedadPlantilla("mail.transferencias_bim.beneficiario.plantilla");
		String transferenciaBIMBeneficiarioPlantilla = Utilerias.obtenerPlantilla(transferenciaBIMBeneficiarioPlantillaName);
		BimEmailTemplateDTO emailTemplateTransferenciaBIMBeneficiarioDTO = new BimEmailTemplateDTO(transferenciaBIMBeneficiarioPlantilla);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("str_Firmas", ftbNivFir);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_ProDes", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DeCuOr", Utilerias.formatearCuenta(trbCueOri, 2));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DeCuDe", Utilerias.formatearCuenta(trbCueDes, 2));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_DesMon", trbMonto.toString());
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_TipCam", trbDesMon);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_StrEqu", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_Descri", trbDescri);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_RFC", trbRFC);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_IVA", trbIVA.toString());
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("NumTransac", folTransa);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_UsCaNo", trbUsCaNo);
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCapDDMMYYYY", Utilerias.formatearFecha(trbFecCap, "dd-MM-yyyy"));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecCapHHMM", Utilerias.formatearFecha(trbFecCap, "HH:mm"));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("str_Autori", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecAutDDMMYYYY", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FecAutHHMM", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("Trb_FePrEnDDMMYYYY", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("strFrecuencia", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("strDuracion", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("strRecordatorio", "");
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("verificador170", digitoVerificador.substring(1,70));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("verificador7170", digitoVerificador.substring(70,140));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("verificador14170", digitoVerificador.substring(140,211));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("verificador21170", digitoVerificador.substring(211,281));
		emailTemplateTransferenciaBIMBeneficiarioDTO.addMergeVariable("verificador28170", digitoVerificador.substring(281));
		String transferenciaBIMBeneficiarioCuerpo = Utilerias.obtenerMensajePlantilla(emailTemplateTransferenciaBIMBeneficiarioDTO);
		logger.info("- trbEmaBen " + trbEmaBen);
		logger.info("- transferenciaBIMBeneficiarioAsunto " + transferenciaBIMBeneficiarioAsunto);
		this.correoServicio.enviarCorreo("penriquez@mediomelon.mx", transferenciaBIMBeneficiarioAsunto, transferenciaBIMBeneficiarioCuerpo);
		logger.info("CTRL: Terminando transferenciaBIMCreacion metodo");
		return Response
				.ok(transferenciaBIMExitosa, MediaType.APPLICATION_JSON)
				.build();
	}
	
}
