package com.bim.msf4j.ctrl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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
import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.BitacoraServicio;
import com.bim.commons.service.CorreoServicio;
import com.bim.commons.service.CuentaDestinoServicio;
import com.bim.commons.service.SPEIServicio;
import com.bim.commons.service.TokenServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/transferencias-nacionales")
public class TransferenciasNacionalesCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(TransferenciasNacionalesCtrl.class);
	
	private TokenServicio tokenServicio;
	private TransaccionServicio transaccionServicio;
	private SPEIServicio speiServicio;
	private BitacoraServicio bitacoraServicio;
	private CuentaDestinoServicio cuentaDestinoServicio;
	private CorreoServicio correoServicio;
	
	private  static String TransferenciaNacionalBitacoraCreacionOpBitTipOpe;
	
	public TransferenciasNacionalesCtrl() {
		super();
		
		this.tokenServicio = new TokenServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.speiServicio = new SPEIServicio();
		this.bitacoraServicio = new BitacoraServicio();
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.correoServicio = new CorreoServicio();
		
		TransferenciaNacionalBitacoraCreacionOpBitTipOpe = properties.getProperty("op.tranferencia_nacional.bitacora_creacion.bit_tip_ope");
	}

	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaSPEICreacion(JsonObject datosTransferenciaSolicitud, @Context Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaSPEICreacion metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principalResultadoObjecto " + principalResultadoObjecto);
		
		String usuUsuAdm = principalResultadoObjecto.get("usuUsuAdm").getAsString();
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuNombre = principalResultadoObjecto.get("usuNombre").getAsString();
		String usuEmail = principalResultadoObjecto.get("usuEmail").getAsString();	

		if(datosTransferenciaSolicitud.isJsonNull()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.56");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		JsonObject datosTransferencia = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSolicitud, "transferencia");
		logger.info("- datosTransferencia " + datosTransferencia);
		
		String cpRSAToken = Utilerias.obtenerStringPropiedad(datosTransferencia, "cpRSAToken");
		String trsCueOri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueOri");
		String trsCueDes = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueDes");
		String trsDescri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDescri");
		String trsMonto = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsMonto");		
		String trsRFC = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsRFC");
		String trsIVA = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsIVA"); 
		String trsDaBeAd = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDaBeAd");
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTransaccionResultado " + folioTransaccionResultado);
		Utilerias.verificarError(folioTransaccionResultado);
		
		JsonObject folioTransaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion"); 
		String numTransac = Utilerias.obtenerStringPropiedad(folioTransaccion, "Fol_Transa");
		
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		// String usuFolTok = Utilerias.obtenerStringPropiedad(principalResultado, "usuFolTok");
		String usuFolTok = "0416218854";
		
		StringBuilder scriptName = new StringBuilder()
				.append(TransferenciasNacionalesCtrl.class.getName())
				.append(".transferenciaSPEICreacion");
		
		String validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName.toString());
		
		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		JsonObject datosHorariosSPEI = new JsonObject();
		datosHorariosSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject horariosSPEIResultado = speiServicio.horariosSPEIConsultar(datosHorariosSPEI);
		logger.info("- horariosSPEIResultado " + horariosSPEIResultado );
		
		if(horariosSPEIResultado.has("Msj_Error")) {
			String errMensaj = Utilerias.obtenerStringPropiedad(horariosSPEIResultado, "Msj_Error");
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonObject datosTransfarenciaSPEI = new JsonObject();
		datosTransfarenciaSPEI.addProperty("Trs_Usuari", usuUsuAdm);
		datosTransfarenciaSPEI.addProperty("Trs_Client", usuClient);
		datosTransfarenciaSPEI.addProperty("Trs_CueOri", trsCueOri);
		datosTransfarenciaSPEI.addProperty("Trs_CueDes", trsCueDes);
		datosTransfarenciaSPEI.addProperty("Trs_Monto", Double.parseDouble(trsMonto));
		datosTransfarenciaSPEI.addProperty("Trs_Descri", trsDescri);
		datosTransfarenciaSPEI.addProperty("Trs_PriRef", usuNombre);
		datosTransfarenciaSPEI.addProperty("Trs_RFC", trsRFC);
		datosTransfarenciaSPEI.addProperty("Trs_IVA", Double.parseDouble(trsIVA));
		datosTransfarenciaSPEI.addProperty("Trs_Tipo","I");
		datosTransfarenciaSPEI.addProperty("Trs_UsuCap", usuNumero);
		datosTransfarenciaSPEI.addProperty("Trs_TipTra","I");
		datosTransfarenciaSPEI.addProperty("Trs_Frecue","U");
		//Trs_FePrEn es un dato de prueba
		datosTransfarenciaSPEI.addProperty("Trs_FePrEn", "1900-01-01 00:00:00");
		//Trs_DurFec es un dato de prueba
		datosTransfarenciaSPEI.addProperty("Trs_DurFec", "1900-01-01 00:00:00");
		datosTransfarenciaSPEI.addProperty("NumTransac", numTransac);	
		datosTransfarenciaSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject transferenciaSPEICreacionResultado = this.speiServicio.transferenciaSPEICreacion(datosTransfarenciaSPEI );
		Utilerias.verificarError(transferenciaSPEICreacionResultado);
		logger.info("- transferenciaSPEICreacionResultado " + transferenciaSPEICreacionResultado);
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_TipOpe", TransferenciaNacionalBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_NumTra", numTransac);
		datosBitacora.addProperty("Bit_CueOri", trsCueOri);
		datosBitacora.addProperty("Bit_CueDes", trsCueDes);
		datosBitacora.addProperty("Bit_Monto", 0);
		//Bit_PriRef es un dato propuesto
		String bitPriRef = new StringBuilder()
				.append("Cuenta: ")
				.append(trsCueDes)
				.toString();
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		//Bit_SegRef es un dato propuesto
		String bitSegRef = new StringBuilder()
				.append("$")
				.append(trsMonto)
				.append(" pesos")
				.toString();
		datosBitacora.addProperty("Bit_SegRef", bitSegRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);
		
		JsonObject bitacoraCreacionResultado = this.bitacoraServicio.creacionBitacora(datosBitacora);
		Utilerias.verificarError(bitacoraCreacionResultado);
		logger.info("- bitacoraCreacionResultado " + bitacoraCreacionResultado);
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoSPEIActivacionResultado = this.cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEI );
		Utilerias.verificarError(cuentaDestinoSPEIActivacionResultado);
		logger.info("- cuentaDestinoSPEIActivacionResultado " + cuentaDestinoSPEIActivacionResultado);
		
		JsonObject datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject  datosTransferenciaSPEIConsultarResultado = this.speiServicio.transferenciaSPEIConsultar(datosTransferenciaSPEIConsultar);
		Utilerias.verificarError(datosTransferenciaSPEIConsultarResultado);
		logger.info("- datosTransferenciaSPEIConsultarResultado " + datosTransferenciaSPEIConsultarResultado);
		
		JsonObject transferenciaSPEIConsultarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSPEIConsultarResultado, "transaccionSPEI");
		String trnBanDes = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_BanDes");
		String trnMonTot = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_MonTot");
		String trnUsCaNo = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_UsCaNo");
		String trnConsec = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_Consec");
		String trnBanco = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_Banco");
		String trnFePrEn = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_FePrEn");
		String trnDesAdi = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_DesAdi");
		String trnFecCap = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_FecCap");
		String trnDeCuOr = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_DeCuOr");
		String trnEmaBen = Utilerias.obtenerStringPropiedad(transferenciaSPEIConsultarResultadoObjeto, "Trn_EmaBen");
		
		JsonObject datosTransferenciaSPEI = new JsonObject();
		datosTransferenciaSPEI.addProperty("Trs_Usuari", usuNumero);
		datosTransferenciaSPEI.addProperty("Trs_UsuCli", usuClient);
		datosTransferenciaSPEI.addProperty("Trs_Consec", trnConsec);
		datosTransferenciaSPEI.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaSPEI.addProperty("Trs_CueBen", trsCueDes);
		datosTransferenciaSPEI.addProperty("Trs_Monto", Double.parseDouble(trsMonto));
		datosTransferenciaSPEI.addProperty("Trs_IVA", Double.parseDouble(trsIVA));
		datosTransferenciaSPEI.addProperty("Trs_ConPag", trsDescri);
		datosTransferenciaSPEI.addProperty("Trs_Banco", trnBanco);
		datosTransferenciaSPEI.addProperty("Trs_SegRef", usuNombre);
		datosTransferenciaSPEI.addProperty("Trs_CoCuDe", trnConsec);
		datosTransferenciaSPEI.addProperty("Trs_TCPDir", bitDireIP);
		datosTransferenciaSPEI.addProperty("Trs_DireIP", bitDireIP);
		datosTransferenciaSPEI.addProperty("Trs_DaBeAd", trsDaBeAd);
		datosTransferenciaSPEI.addProperty("Ban_Descri", trnBanDes);
		datosTransferenciaSPEI.addProperty("NumTransac", numTransac);
		datosTransferenciaSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject transferenciaSPEIProcesarResultado = this.speiServicio.transferenciaSPEIProcesar(datosTransferenciaSPEI);
		Utilerias.verificarError(transferenciaSPEIProcesarResultado);
		logger.info("- transferenciaSPEIProcesarResultado " + transferenciaSPEIProcesarResultado);

		JsonObject transferenciaSPEIProcesarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEIProcesarResultado, "transaccionSPEI");
		String errCodigo = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Codigo");
		String trsClaRas = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_ClaRas");
		String trsFecAut = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecAut");
		String trsFecCar = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecCar");
		String trsFecApl = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecApl");
		String trsFirma = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_Firma");
		
		if(!"000000".equals(errCodigo)) {
			String errMensaj = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Mensaj");			
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		/**
		 * REGLA DE NEGOCIO: Envío de correo con plantilla establecida por BIM y encriptado de digito verificador
		 */
		
		String asuntoCliente = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_nacional.asunto_cliente");
		String plantillaCliente = Utilerias.obtenerPlantilla("transferencia-nacional-cliente");
		
		String strVerifi = "Origen:" + trsCueOri + " Destino:" + trsCueDes + " Cantidad:" + trsMonto + " Folio:" + numTransac;
		String digitoVerificador = Utilerias.generarDigitoVerificador(strVerifi);
		String strVerifi1 = digitoVerificador.substring(1, 70);
		String strVerifi2 = digitoVerificador.substring(71, 70);
		String strVerifi3 = digitoVerificador.substring(141, 70);
		String strVerifi4 = digitoVerificador.substring(211, 70);
		String strVerifi5 = digitoVerificador.substring(281, 70);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm");
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		StringBuilder trnDeCuOrOcu = new StringBuilder()
				.append("**********")
				.append(trnDeCuOr.substring(trnDeCuOr.length()-4));
		
		StringBuilder trnCueDesOcu = new StringBuilder()
				.append("***************")
				.append(trsCueDes.substring(trsCueDes.length()-3));
		
		BimEmailTemplateDTO emailTemplateCliente = new BimEmailTemplateDTO(plantillaCliente);
		emailTemplateCliente.addMergeVariable("str_Firmas", trsFirma);
//		emailTemplateCliente.addMergeVariable("Trn_ProDes", "");
		emailTemplateCliente.addMergeVariable("Trn_DeCuOr", trnDeCuOrOcu.toString());
		emailTemplateCliente.addMergeVariable("Trn_CueDes", trnCueDesOcu.toString());		
		emailTemplateCliente.addMergeVariable("Trn_BanDes", trnBanDes);
		emailTemplateCliente.addMergeVariable("Trn_Monto", String.valueOf(formatter.format(trsMonto)));
		emailTemplateCliente.addMergeVariable("Trn_Descri", trsDescri);
		emailTemplateCliente.addMergeVariable("Trn_RFC", trsRFC);
		emailTemplateCliente.addMergeVariable("Trn_IVA", String.valueOf(formatter.format(trsIVA)));
		emailTemplateCliente.addMergeVariable("NumTransac", numTransac);
		emailTemplateCliente.addMergeVariable("Trn_ClaRas", trsClaRas);
		emailTemplateCliente.addMergeVariable("Trn_DesAdi", trnDesAdi);
		emailTemplateCliente.addMergeVariable("Trn_UsCaNo", trnUsCaNo);
		emailTemplateCliente.addMergeVariable("Trn_FecCap", sdf.format(trnFecCap));
//		emailTemplateCliente.addMergeVariable("str_Autori", "");
		emailTemplateCliente.addMergeVariable("Trn_FecAut", sdf.format(trsFecAut));
		emailTemplateCliente.addMergeVariable("Trn_FecCar", trsFecCar);
		emailTemplateCliente.addMergeVariable("Trn_FecApl", trsFecApl); 
		emailTemplateCliente.addMergeVariable("Trn_FePrEn", simpleDateFormat.format(trnFePrEn));
//		emailTemplateCliente.addMergeVariable("strFrecuencia", ""); 
//		emailTemplateCliente.addMergeVariable("strDuracion", ""); 
//		emailTemplateCliente.addMergeVariable("strRecordatorio", ""); 
		emailTemplateCliente.addMergeVariable("Str_Verifi1", strVerifi1);
		emailTemplateCliente.addMergeVariable("Str_Verifi2", strVerifi2);
		emailTemplateCliente.addMergeVariable("Str_Verifi3", strVerifi3);
		emailTemplateCliente.addMergeVariable("Str_Verifi4", strVerifi4);
		emailTemplateCliente.addMergeVariable("Str_Verifi5", strVerifi5);
//		emailTemplateCliente.addMergeVariable("Trn_FecOpe", fechaSis);
//		emailTemplateCliente.addMergeVariable("She_HorIni", "");
		String cuerpoEmailCliente = Utilerias.obtenerMensajePlantilla(emailTemplateCliente);
		
		correoServicio.enviarCorreo(usuEmail, asuntoCliente, cuerpoEmailCliente);
		
		String asuntoBeneficiario = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_nacional.asunto_beneficiario");
		String plantillaBeneficiario = Utilerias.obtenerPlantilla("transferencia-nacional-beneficiario");
		
		BimEmailTemplateDTO emailTemplateBeneficiario = new BimEmailTemplateDTO(plantillaBeneficiario);
		emailTemplateBeneficiario.addMergeVariable("Trn_DeCuOr", trnDeCuOrOcu.toString());
		emailTemplateBeneficiario.addMergeVariable("Trn_CueDe", trnCueDesOcu.toString());
		emailTemplateBeneficiario.addMergeVariable("Trn_Monto", String.valueOf(formatter.format(trsMonto)));
		emailTemplateBeneficiario.addMergeVariable("Trn_Descri", trsDescri);
		emailTemplateBeneficiario.addMergeVariable("NumTransac", numTransac);
		emailTemplateBeneficiario.addMergeVariable("Trn_FecCar", trsFecCar);
		emailTemplateBeneficiario.addMergeVariable("Trn_FecApl", trsFecApl);
		String cuerpoEmailBeneficiario = Utilerias.obtenerMensajePlantilla(emailTemplateCliente);
		
		correoServicio.enviarCorreo(trnEmaBen, asuntoBeneficiario, cuerpoEmailBeneficiario);
		
		JsonObject datosTransferenciaExitosa = new JsonObject();
		datosTransferenciaExitosa.addProperty("trnCueOri", trsCueOri);
		datosTransferenciaExitosa.addProperty("trnCueDes", trsCueDes);
		datosTransferenciaExitosa.addProperty("trnDescri", trsDescri);
		datosTransferenciaExitosa.addProperty("numTransac", numTransac);
		datosTransferenciaExitosa.addProperty("trnBanDes", trnBanDes); 
		datosTransferenciaExitosa.addProperty("trnMonTot", String.valueOf(formatter.format(trnMonTot)));
		datosTransferenciaExitosa.addProperty("trsClaRas", trsClaRas);
		datosTransferenciaExitosa.addProperty("trnUsCaNo", trnUsCaNo); 
		datosTransferenciaExitosa.addProperty("trsFecAut", trsFecAut);
		datosTransferenciaExitosa.addProperty("trsFecCar", trsFecCar);
		datosTransferenciaExitosa.addProperty("trsFecApl", trsFecApl);

		JsonObject transferenciaExitosa = new JsonObject();
		transferenciaExitosa.add("transferenciaExitosa", datosTransferenciaExitosa);
		logger.info("- transferenciaExitosa " + transferenciaExitosa);
		
		logger.info("CTRL: Finalizando transferenciaSPEICreacion metodo");
		return Response.ok(transferenciaExitosa.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
}
