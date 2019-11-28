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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/transferencias-nacionales")
public class TransferenciasNacionalesCtrl extends BimBaseCtrl {

	private static final Logger logger = Logger.getLogger(TransferenciasNacionalesCtrl.class);
	
	//servicio
	private TokenServicio tokenServicio;
	private TransaccionServicio transaccionServicio;
	private SPEIServicio speiServicio;
	private BitacoraServicio bitacoraServicio;
	private CuentaDestinoServicio cuentaDestinoServicio;
	private CorreoServicio correoServicio;
	
	// propiedades
	private  static String TransferenciaNacionalBitacoraCreacionOpBitTipOpe;
	
	static {		
		TransferenciaNacionalBitacoraCreacionOpBitTipOpe = properties.getProperty("op.tranferencia_nacional.bitacora_creacion.bit_tip_ope");
	}
	
	public TransferenciasNacionalesCtrl() {
		super();
		logger.info("CTRL: Comenzando metodo init...");
		this.tokenServicio = new TokenServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.speiServicio = new SPEIServicio();
		this.bitacoraServicio = new BitacoraServicio();
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		this.correoServicio = new CorreoServicio();
		logger.info("CTRL: Terminando metodo init...");
	}

	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaNacionalCreacion(JsonObject datosTransferenciaSolicitud, @Context Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaNacionalCreacion metodo");
		
		StringBuilder scriptName = null;
		
		String bearerToken = null;
		String bitDireIP = null;
		String fechaSis = null;
		String usuUsuAdm = null;
		String usuClient = null;
		String usuNumero = null;
		String usuNombre = null;
		String usuEmail = null;
		String numTransac = null;
		String cpRSAToken = null;
		String trsCueOri = null;
		String trsCueDes = null;
		String trsDescri = null;
		String trsRFC = null;
		String trsDaBeAd = null;
		String validarToken = null;
		String usuFolTok = null;
		String trsConsec = null;
		String bitPriRef = null;
		String bitSegRef = null;		
		String trnBanDes = null;
		String trnMonTot = null;
		String trnUsCaNo = null;
		String cdeConsec = null;
		String trnBanco = null;
		String trnFePrEn = null;
		String trnDesAdi = null;
		String trnFecCap = null;
		String cdeEmaBen = null;
		String trnTipTra = null;
		String errCodigo = null;
		String trsClaRas = null;
		String trsFecAut = null;
		String trsFecCar = null;
		String trsFecApl = null;
		String trsFirma = null;
		String errMensaj = null;
		String asuntoCliente = null;
		String asuntoBeneficiario = null;
		String plantillaTransferenciaInmediataCliente = null;
		String plantillaTransferenciaBeneficiario = null;		
		String strVerifi = null;
		String digitoVerificador = null;
		String strVerifi1 = null;
		String strVerifi2 = null;
		String strVerifi3 = null;
		String strVerifi4 = null;
		String strVerifi5 = null;		
		String trnCueOriOcu = null;
		String trnCueDesOcu = null;		
		String fechaFormato = null;
		String fechaFormatoHoras = null;
		String monto = null;
		String cuerpoEmailCliente = null;
		String cuerpoEmailBeneficiario = null;
		Double trsIVA = null;
		Double trsMonto = null;
		
		BimMessageDTO bimMessageDTO = null;
		BimEmailTemplateDTO emailTemplateCliente = null;
		BimEmailTemplateDTO emailTemplateBeneficiario = null;
		
		JsonObject datosTransferencia = null;
		JsonObject principalResultadoObjecto = null;
		JsonObject datosTransferenciaExitosa = null;
		JsonObject folioTransaccion = null;
		JsonObject folioTransaccionResultado = null;
		JsonObject datosHorariosSPEI = null;
		JsonObject datosTransferenciaNacional = null;
		JsonObject transferenciaSPEICreacionResultado = null;
		JsonObject transaccionSPEICreacion = null;
		JsonObject datosBitacora = null;
		JsonObject bitacoraCreacionResultado = null;
		JsonObject datosCuentaDestinoSPEI = null;
		JsonObject cuentaDestinoSPEIActivacionResultado = null;
		JsonObject datosTransferenciaSPEIConsultar = null;
		JsonObject  datosTransferenciaSPEIConsultarResultado = null;
		JsonObject transaccionesSPEI = null;
		JsonObject transaccionSPEIObjeto = null;
		JsonObject datosTransferenciaSPEI = null;
		JsonObject transferenciaSPEIProcesarResultado = null;
		JsonObject transferenciaSPEIProcesarResultadoObjeto = null;
		JsonObject transferenciaExitosa = null;
		
		JsonArray transaccionSPEI = null;
		
		bearerToken = solicitud.getHeader("Authorization");
		bitDireIP = solicitud.getHeader("X-Forwarded-For");
		fechaSis = Utilerias.obtenerFechaSis();
		
		principalResultadoObjecto = Utilerias.obtenerPrincipal(bearerToken);
		
		// propiedades extraidas del principal
		usuUsuAdm = principalResultadoObjecto.get("usuUsuAdm").getAsString();
		usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		usuNombre = principalResultadoObjecto.get("usuNombre").getAsString();
		usuEmail = principalResultadoObjecto.get("usuEmail").getAsString();	
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		// String usuFolTok = Utilerias.obtenerStringPropiedad(principalResultado, "usuFolTok");
		usuFolTok = "0416218854";
		
		if(datosTransferenciaSolicitud.isJsonNull()) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.56");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		datosTransferencia = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSolicitud, "transferencia");
		
		if(datosTransferencia.has("trsMonto")) {
			trsMonto = Utilerias.obtenerDoublePropiedad(datosTransferencia, "trsMonto");
		} else {
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "trsMonto");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(datosTransferencia.has("trsIVA")) {
			trsIVA = Utilerias.obtenerDoublePropiedad(datosTransferencia, "trsIVA"); 
		} else {
			trsIVA = 0.0;
		}

		// propiedades extraidas de la solicitud
		cpRSAToken = Utilerias.obtenerStringPropiedad(datosTransferencia, "cpRSAToken");
		trsCueOri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueOri");
		trsCueDes = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueDes");
		trsDescri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDescri");
		trsRFC = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsRFC");
		trsDaBeAd = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDaBeAd");
		
		folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		Utilerias.verificarError(folioTransaccionResultado);
		
		folioTransaccion = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion"); 
		numTransac = Utilerias.obtenerStringPropiedad(folioTransaccion, "Fol_Transa");
		
		scriptName = new StringBuilder()
				.append(TransferenciasNacionalesCtrl.class.getName())
				.append(".transferenciaNacionalCreacion");
		
		validarToken = this.tokenServicio.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, numTransac, scriptName.toString());
		
		if ("B".equals(validarToken)) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		datosHorariosSPEI = new JsonObject();
		datosHorariosSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject horariosSPEIResultado = speiServicio.horariosSPEIConsultar(datosHorariosSPEI);
		logger.info("- horariosSPEIResultado " + horariosSPEIResultado );
		
		if(horariosSPEIResultado.has("Msj_Error")) {
			errMensaj = Utilerias.obtenerStringPropiedad(horariosSPEIResultado, "Msj_Error");
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		datosTransferenciaNacional = new JsonObject();
		datosTransferenciaNacional.addProperty("Trs_Usuari", usuUsuAdm);
		datosTransferenciaNacional.addProperty("Trs_Client", usuClient);
		datosTransferenciaNacional.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaNacional.addProperty("Trs_CueDes", trsCueDes);
		datosTransferenciaNacional.addProperty("Trs_Monto", trsMonto);
		datosTransferenciaNacional.addProperty("Trs_Descri", trsDescri);
		datosTransferenciaNacional.addProperty("Trs_PriRef", usuNombre);
		datosTransferenciaNacional.addProperty("Trs_RFC", trsRFC);
		datosTransferenciaNacional.addProperty("Trs_IVA", trsIVA);
		datosTransferenciaNacional.addProperty("Trs_Tipo", "I");
		datosTransferenciaNacional.addProperty("Trs_UsuCap", usuNumero);
		datosTransferenciaNacional.addProperty("Trs_TipTra", "I");
		datosTransferenciaNacional.addProperty("Trs_Frecue", "U");
		datosTransferenciaNacional.addProperty("Trs_FePrEn", "1900-01-01 00:00:00");
		datosTransferenciaNacional.addProperty("Trs_DurFec", "1900-01-01 00:00:00");
		datosTransferenciaNacional.addProperty("NumTransac", numTransac);	
		datosTransferenciaNacional.addProperty("FechaSis", fechaSis);
		
		transferenciaSPEICreacionResultado = this.speiServicio.transferenciaSPEICreacion(datosTransferenciaNacional);
		Utilerias.verificarError(transferenciaSPEICreacionResultado);
		
		transaccionSPEICreacion = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEICreacionResultado, "transaccionSPEI");
		logger.info("-- transaccionSPEICreacion " + transaccionSPEICreacion);
		trsConsec = Utilerias.obtenerStringPropiedad(transaccionSPEICreacion, "Trs_Consec");
		
		datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_TipOpe", TransferenciaNacionalBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_NumTra", numTransac);
		datosBitacora.addProperty("Bit_CueOri", trsCueOri);
		datosBitacora.addProperty("Bit_CueDes", trsCueDes);
		datosBitacora.addProperty("Bit_Monto", trsMonto);
		bitPriRef = new StringBuilder()
				.append("Cuenta: ")
				.append(trsCueDes)
				.toString();
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
		bitSegRef = new StringBuilder()
				.append("$")
				.append(trsMonto)
				.append(" pesos")
				.toString();
		datosBitacora.addProperty("Bit_SegRef", bitSegRef);
		datosBitacora.addProperty("Bit_DireIP", bitDireIP);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("FechaSis", fechaSis);
		
		bitacoraCreacionResultado = this.bitacoraServicio.creacionBitacora(datosBitacora);
		Utilerias.verificarError(bitacoraCreacionResultado);
		
		datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		cuentaDestinoSPEIActivacionResultado = this.cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEI );
		Utilerias.verificarError(cuentaDestinoSPEIActivacionResultado);
		
		datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		
		datosTransferenciaSPEIConsultarResultado = this.speiServicio.transferenciaSPEIConsultar(datosTransferenciaSPEIConsultar);
		Utilerias.verificarError(datosTransferenciaSPEIConsultarResultado);
		
		transaccionesSPEI = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSPEIConsultarResultado, "transaccionesSPEI");
		transaccionSPEI = Utilerias.obtenerJsonArrayResultante(transaccionesSPEI, "transaccionSPEI");
		transaccionSPEIObjeto = (JsonObject)transaccionSPEI.get(transaccionSPEI.size()-1);
		
		trnBanDes = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_BanDes");
		trnMonTot = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_MonTot");
		trnUsCaNo = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_UsCaNo");
		cdeConsec = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Cde_Consec");
		trnBanco = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_Banco");
		trnFePrEn = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_FePrEn");
		trnDesAdi = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_DesAdi");
		trnFecCap = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_FecCap");
		cdeEmaBen = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Cde_EmaBen");
		trnTipTra = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_TipTra");
		
		datosTransferenciaSPEI = new JsonObject();
		datosTransferenciaSPEI.addProperty("Trs_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEI.addProperty("Trs_Usuari", usuNumero);
		datosTransferenciaSPEI.addProperty("Trs_UsuCli", usuClient);
		datosTransferenciaSPEI.addProperty("Trs_Consec", trsConsec);
		datosTransferenciaSPEI.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaSPEI.addProperty("Trs_CueBen", trsCueDes);
		datosTransferenciaSPEI.addProperty("Trs_Monto", trsMonto);
		datosTransferenciaSPEI.addProperty("Trs_IVA", trsIVA);
		datosTransferenciaSPEI.addProperty("Trs_ConPag", trsDescri);
		datosTransferenciaSPEI.addProperty("Trs_Banco", trnBanco);
		datosTransferenciaSPEI.addProperty("Trs_SegRef", usuNombre);
		datosTransferenciaSPEI.addProperty("Trs_CoCuDe", cdeConsec);
		datosTransferenciaSPEI.addProperty("Trs_TCPDir", bitDireIP);
		datosTransferenciaSPEI.addProperty("Trs_DireIP", bitDireIP);
		datosTransferenciaSPEI.addProperty("Trs_DaBeAd", trsDaBeAd);
		datosTransferenciaSPEI.addProperty("Ban_Descri", trnBanDes);
		datosTransferenciaSPEI.addProperty("NumTransac", numTransac);
		datosTransferenciaSPEI.addProperty("FechaSis", fechaSis);
		
		transferenciaSPEIProcesarResultado = this.speiServicio.transferenciaSPEIProcesar(datosTransferenciaSPEI);
		Utilerias.verificarError(transferenciaSPEIProcesarResultado);

		transferenciaSPEIProcesarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEIProcesarResultado, "transaccionSPEI");
		errCodigo = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Codigo");
		trsClaRas = (Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_ClaRas").trim());
		trsFecAut = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecAut");
		trsFecCar = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecCar");
		trsFecApl = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecApl");
		trsFirma = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_Firma");
		logger.info("-- transferenciaSPEIProcesarResultadoObjeto " + transferenciaSPEIProcesarResultadoObjeto);
		
		if(!"000000".equals(errCodigo)) {
			errMensaj = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Mensaj");			
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		/**
		 * REGLA DE NEGOCIO: Envío de correo con plantilla establecida por BIM y encriptado de digito verificador
		 */
		asuntoCliente = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_nacional.asunto_cliente");
		asuntoBeneficiario = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_nacional.asunto_beneficiario");
		plantillaTransferenciaInmediataCliente = Utilerias.obtenerPlantilla("transferencia-nacional-inmediata-cliente");
		plantillaTransferenciaBeneficiario = Utilerias.obtenerPlantilla("transferencia-nacional-beneficiario");
		
		strVerifi = "Origen:" + trsCueOri + " Destino:" + trsCueDes + " Cantidad:" + trsMonto + " Folio:" + numTransac;
		digitoVerificador = Utilerias.generarDigitoVerificador(strVerifi);
		strVerifi1 = digitoVerificador.substring(0, 70);
		strVerifi2 = digitoVerificador.substring(70, 140);
		strVerifi3 = digitoVerificador.substring(140, 210);
		strVerifi4 = digitoVerificador.substring(210, 280);
		strVerifi5 = digitoVerificador.substring(280);
		
		trnCueOriOcu = Utilerias.formatearCuenta(trsCueOri, 4, 10);
		trnCueDesOcu = Utilerias.formatearCuenta(trsCueDes, 4, 14);
		
		fechaFormato = "dd-MM-yyyy";
		fechaFormatoHoras = "dd-MM-yyyy HH:mm";
		trsFecApl = Utilerias.formatearFecha(trsFecApl, fechaFormato);
		trsFecCar = Utilerias.formatearFecha(trsFecCar, fechaFormato);
		trsFecAut = Utilerias.formatearFecha(trsFecAut, fechaFormatoHoras);
		trnFecCap = Utilerias.formatearFecha(trnFecCap, fechaFormatoHoras);
		
		if(("I").equals(trnTipTra)) {
			emailTemplateCliente = new BimEmailTemplateDTO(plantillaTransferenciaInmediataCliente);
			emailTemplateCliente.addMergeVariable("Trn_ProDes", "ACEPTADA");
		}
		
		monto = new StringBuilder()
			.append("$")
			.append(trsMonto)
			.toString();
		emailTemplateCliente.addMergeVariable("Trn_Monto", monto);
		emailTemplateCliente.addMergeVariable("str_Firmas", trsFirma);
		emailTemplateCliente.addMergeVariable("Trn_DeCuOr", trnCueOriOcu.toString());
		emailTemplateCliente.addMergeVariable("Trn_CueDes", trnCueDesOcu.toString());		
		emailTemplateCliente.addMergeVariable("Trn_BanDes", trnBanDes);
		emailTemplateCliente.addMergeVariable("Trn_Descri", trsDescri);
		emailTemplateCliente.addMergeVariable("Trn_RFC", trsRFC);
		emailTemplateCliente.addMergeVariable("Trn_IVA", trsIVA.toString());
		emailTemplateCliente.addMergeVariable("NumTransac", numTransac);
		emailTemplateCliente.addMergeVariable("Trn_ClaRas", trsClaRas);
		emailTemplateCliente.addMergeVariable("Trn_DesAdi", trnDesAdi);
		emailTemplateCliente.addMergeVariable("Trn_UsCaNo", trnUsCaNo);
		emailTemplateCliente.addMergeVariable("Trn_FecCap", trnFecCap);
		// pendiente str_Autori por ResultSet de NBTRANACCON parametro Fir_UsuNom
		emailTemplateCliente.addMergeVariable("str_Autori", "");
		emailTemplateCliente.addMergeVariable("Trn_FecAut", trsFecAut);
		emailTemplateCliente.addMergeVariable("Trn_FecCar", trsFecCar);
		emailTemplateCliente.addMergeVariable("Trn_FecApl", trsFecApl); 
		emailTemplateCliente.addMergeVariable("Trn_FePrEn", trnFePrEn);
		emailTemplateCliente.addMergeVariable("Str_Verifi1", strVerifi1);
		emailTemplateCliente.addMergeVariable("Str_Verifi2", strVerifi2);
		emailTemplateCliente.addMergeVariable("Str_Verifi3", strVerifi3);
		emailTemplateCliente.addMergeVariable("Str_Verifi4", strVerifi4);
		emailTemplateCliente.addMergeVariable("Str_Verifi5", strVerifi5);
		cuerpoEmailCliente = Utilerias.obtenerMensajePlantilla(emailTemplateCliente);
		correoServicio.enviarCorreo(usuEmail, asuntoCliente, cuerpoEmailCliente);
		
		emailTemplateBeneficiario = new BimEmailTemplateDTO(plantillaTransferenciaBeneficiario);
		emailTemplateBeneficiario.addMergeVariable("Trn_DeCuOr", trnCueOriOcu.toString());
		emailTemplateBeneficiario.addMergeVariable("Trn_CueDe", trnCueDesOcu.toString());
		emailTemplateBeneficiario.addMergeVariable("Trn_Monto", monto);
		emailTemplateBeneficiario.addMergeVariable("Trn_Descri", trsDescri);
		emailTemplateBeneficiario.addMergeVariable("NumTransac", numTransac);
		emailTemplateBeneficiario.addMergeVariable("Trn_FecCar", trsFecCar);
		emailTemplateBeneficiario.addMergeVariable("Trn_FecApl", trsFecApl);
		cuerpoEmailBeneficiario = Utilerias.obtenerMensajePlantilla(emailTemplateBeneficiario);
		correoServicio.enviarCorreo(cdeEmaBen, asuntoBeneficiario, cuerpoEmailBeneficiario);

		datosTransferenciaExitosa = new JsonObject();
		datosTransferenciaExitosa.addProperty("trnCueOri", trsCueOri);
		datosTransferenciaExitosa.addProperty("trnCueDes", trsCueDes);
		datosTransferenciaExitosa.addProperty("trnDescri", trsDescri);
		datosTransferenciaExitosa.addProperty("numTransac", numTransac);
		datosTransferenciaExitosa.addProperty("trnBanDes", trnBanDes); 
		datosTransferenciaExitosa.addProperty("trnMonTot", trnMonTot);
		datosTransferenciaExitosa.addProperty("trsClaRas", trsClaRas);
		datosTransferenciaExitosa.addProperty("trnUsCaNo", trnUsCaNo); 
		datosTransferenciaExitosa.addProperty("trsFecAut", trsFecAut);
		datosTransferenciaExitosa.addProperty("trsFecCar", trsFecCar);
		datosTransferenciaExitosa.addProperty("trsFecApl", trsFecApl);

		transferenciaExitosa = new JsonObject();
		transferenciaExitosa.add("transferenciaExitosa", datosTransferenciaExitosa);
		
		logger.info("CTRL: Finalizando transferenciaNacionalCreacion metodo");
		return Response.ok(transferenciaExitosa.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
}
