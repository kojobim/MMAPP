package com.bim.msf4j.ctrl;

import java.text.DecimalFormat;
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
import com.bim.commons.utils.Filtrado;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	
	private static String InversionesFilterBy;
	private static Integer InversionesMaximoPagina;
	
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
		InversionesFilterBy = properties.getProperty("inversiones_servicio.filter_by");
		InversionesMaximoPagina = Integer.parseInt(properties.getProperty("inversiones_servicio.maximo_pagina"));
	}

	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaNacionalCreacion(JsonObject datosTransferenciaSolicitud, @Context Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaNacionalCreacion metodo");
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
				.append(".transferenciaNacionalCreacion");
		
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
		
		JsonObject datosTransferenciaNacional = new JsonObject();
		datosTransferenciaNacional.addProperty("Trs_Usuari", usuUsuAdm);
		datosTransferenciaNacional.addProperty("Trs_Client", usuClient);
		datosTransferenciaNacional.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaNacional.addProperty("Trs_CueDes", trsCueDes);
		datosTransferenciaNacional.addProperty("Trs_Monto", Double.parseDouble(trsMonto));
		datosTransferenciaNacional.addProperty("Trs_Descri", trsDescri);
		datosTransferenciaNacional.addProperty("Trs_PriRef", usuNombre);
		datosTransferenciaNacional.addProperty("Trs_RFC", trsRFC);
		datosTransferenciaNacional.addProperty("Trs_IVA", Double.parseDouble(trsIVA));
		datosTransferenciaNacional.addProperty("Trs_Tipo", "I");
		datosTransferenciaNacional.addProperty("Trs_UsuCap", usuNumero);
		datosTransferenciaNacional.addProperty("Trs_TipTra", "I");
		datosTransferenciaNacional.addProperty("Trs_Frecue", "U");
		datosTransferenciaNacional.addProperty("Trs_FePrEn", "1900-01-01 00:00:00");
		datosTransferenciaNacional.addProperty("Trs_DurFec", "1900-01-01 00:00:00");
		datosTransferenciaNacional.addProperty("NumTransac", numTransac);	
		datosTransferenciaNacional.addProperty("FechaSis", fechaSis);
		
		JsonObject transferenciaSPEICreacionResultado = this.speiServicio.transferenciaSPEICreacion(datosTransferenciaNacional);
		Utilerias.verificarError(transferenciaSPEICreacionResultado);
		
		JsonObject transaccionSPEICreacion = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEICreacionResultado, "transaccionSPEI");
		logger.info("-- transaccionSPEICreacion " + transaccionSPEICreacion);
		String trsConsec = Utilerias.obtenerStringPropiedad(transaccionSPEICreacion, "Trs_Consec");
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_TipOpe", TransferenciaNacionalBitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_NumTra", numTransac);
		datosBitacora.addProperty("Bit_CueOri", trsCueOri);
		datosBitacora.addProperty("Bit_CueDes", trsCueDes);
		datosBitacora.addProperty("Bit_Monto", 0);
		String bitPriRef = new StringBuilder()
				.append("Cuenta: ")
				.append(trsCueDes)
				.toString();
		datosBitacora.addProperty("Bit_PriRef", bitPriRef);
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
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoSPEIActivacionResultado = this.cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEI );
		Utilerias.verificarError(cuentaDestinoSPEIActivacionResultado);
		
		JsonObject datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject  datosTransferenciaSPEIConsultarResultado = this.speiServicio.transferenciaSPEIConsultar(datosTransferenciaSPEIConsultar);
		Utilerias.verificarError(datosTransferenciaSPEIConsultarResultado);
		
		JsonObject transaccionesSPEI = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSPEIConsultarResultado, "transaccionesSPEI");
		JsonArray transaccionSPEI = Utilerias.obtenerJsonArrayResultante(transaccionesSPEI, "transaccionSPEI");
		JsonObject transaccionSPEIObjeto = (JsonObject)transaccionSPEI.get(transaccionSPEI.size()-1);
		
		String trnBanDes = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_BanDes");
		String trnMonTot = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_MonTot");
		String trnUsCaNo = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_UsCaNo");
		String cdeConsec = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Cde_Consec");
		String trnBanco = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_Banco");
		String trnFePrEn = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_FePrEn");
		String trnDesAdi = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_DesAdi");
		String trnFecCap = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_FecCap");
		String trnDeCuOr = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_DeCuOr");
		String trnEmaBen = Utilerias.obtenerStringPropiedad(transaccionSPEIObjeto, "Trn_EmaBen");
		
		JsonObject datosTransferenciaSPEI = new JsonObject();
		datosTransferenciaSPEI.addProperty("Trs_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEI.addProperty("Trs_Usuari", usuNumero);
		datosTransferenciaSPEI.addProperty("Trs_UsuCli", usuClient);
		datosTransferenciaSPEI.addProperty("Trs_Consec", trsConsec);
		datosTransferenciaSPEI.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaSPEI.addProperty("Trs_CueBen", trsCueDes);
		datosTransferenciaSPEI.addProperty("Trs_Monto", Double.parseDouble(trsMonto));
		datosTransferenciaSPEI.addProperty("Trs_IVA", Double.parseDouble(trsIVA));
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
		
		JsonObject transferenciaSPEIProcesarResultado = this.speiServicio.transferenciaSPEIProcesar(datosTransferenciaSPEI);
		Utilerias.verificarError(transferenciaSPEIProcesarResultado);

		JsonObject transferenciaSPEIProcesarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEIProcesarResultado, "transaccionSPEI");
		String errCodigo = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Codigo");
		String trsClaRas = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_ClaRas");
		String trsFecAut = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecAut");
		String trsFecCar = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecCar");
		String trsFecApl = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecApl");
		String trsFirma = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_Firma");
		logger.info("-- transferenciaSPEIProcesarResultadoObjeto " + transferenciaSPEIProcesarResultadoObjeto);
		
		if(!"000000".equals(errCodigo)) {
			String errMensaj = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Mensaj");			
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		/**
		 * REGLA DE NEGOCIO: Env�o de correo con plantilla establecida por BIM y encriptado de digito verificador
		 */
		String asuntoCliente = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_nacional.asunto_cliente");
		String plantillaCliente = Utilerias.obtenerPlantilla("transferencia-nacional-cliente");

		String strVerifi = "Origen:" + trsCueOri + " Destino:" + trsCueDes + " Cantidad:" + trsMonto + " Folio:" + numTransac;
		String digitoVerificador = Utilerias.generarDigitoVerificador(strVerifi);
		String strVerifi1 = digitoVerificador.substring(0, 70);
		String strVerifi2 = digitoVerificador.substring(70, 140);
		String strVerifi3 = digitoVerificador.substring(140, 210);
		String strVerifi4 = digitoVerificador.substring(210, 280);
		String strVerifi5 = digitoVerificador.substring(280);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm");
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		String trnCueOriOcu = Utilerias.formatearCuenta(trsCueOri, 4, 10);
		logger.info(">>>>>>>>>>>>>> trnCueOriOcu " + trnCueOriOcu);
		
		String trnCueDesOcu = Utilerias.formatearCuenta(trsCueDes, 4, 14);
		logger.info(">>>>>>>>>>>>>> trnCueDesOcu " + trnCueDesOcu);
		
		BimEmailTemplateDTO emailTemplateCliente = new BimEmailTemplateDTO(plantillaCliente);
		emailTemplateCliente.addMergeVariable("str_Firmas", trsFirma);
//		emailTemplateCliente.addMergeVariable("Trn_ProDes", "");
		emailTemplateCliente.addMergeVariable("Trn_DeCuOr", trnCueOriOcu.toString());
		emailTemplateCliente.addMergeVariable("Trn_CueDes", trnCueDesOcu.toString());		
		emailTemplateCliente.addMergeVariable("Trn_BanDes", trnBanDes);
		emailTemplateCliente.addMergeVariable("Trn_Monto", trsMonto);
		emailTemplateCliente.addMergeVariable("Trn_Descri", trsDescri);
		emailTemplateCliente.addMergeVariable("Trn_RFC", trsRFC);
		emailTemplateCliente.addMergeVariable("Trn_IVA", trsIVA);
		emailTemplateCliente.addMergeVariable("NumTransac", numTransac);
		emailTemplateCliente.addMergeVariable("Trn_ClaRas", trsClaRas);
		emailTemplateCliente.addMergeVariable("Trn_DesAdi", trnDesAdi);
		emailTemplateCliente.addMergeVariable("Trn_UsCaNo", trnUsCaNo);
		emailTemplateCliente.addMergeVariable("Trn_FecCap", trnFecCap);
//		emailTemplateCliente.addMergeVariable("str_Autori", "");
		emailTemplateCliente.addMergeVariable("Trn_FecAut", trsFecAut);
		emailTemplateCliente.addMergeVariable("Trn_FecCar", trsFecCar);
		emailTemplateCliente.addMergeVariable("Trn_FecApl", trsFecApl); 
		emailTemplateCliente.addMergeVariable("Trn_FePrEn", trnFePrEn);
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
		logger.info(">>>>>>>>>>>>>>>>> emailTemplateCliente " + emailTemplateCliente);
		correoServicio.enviarCorreo(usuEmail, asuntoCliente, cuerpoEmailCliente);
		
		String asuntoBeneficiario = Utilerias.obtenerPropiedadPlantilla("mail.transferencia_nacional.asunto_beneficiario");
		String plantillaBeneficiario = Utilerias.obtenerPlantilla("transferencia-nacional-beneficiario");
		
		BimEmailTemplateDTO emailTemplateBeneficiario = new BimEmailTemplateDTO(plantillaBeneficiario);
		emailTemplateBeneficiario.addMergeVariable("Trn_DeCuOr", trnCueOriOcu.toString());
		emailTemplateBeneficiario.addMergeVariable("Trn_CueDe", trnCueDesOcu.toString());
		emailTemplateBeneficiario.addMergeVariable("Trn_Monto", trsMonto);
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
		datosTransferenciaExitosa.addProperty("trnMonTot", trnMonTot);
		datosTransferenciaExitosa.addProperty("trsClaRas", trsClaRas);
		datosTransferenciaExitosa.addProperty("trnUsCaNo", trnUsCaNo); 
		datosTransferenciaExitosa.addProperty("trsFecAut", trsFecAut);
		datosTransferenciaExitosa.addProperty("trsFecCar", trsFecCar);
		datosTransferenciaExitosa.addProperty("trsFecApl", trsFecApl);

		JsonObject transferenciaExitosa = new JsonObject();
		transferenciaExitosa.add("transferenciaExitosa", datosTransferenciaExitosa);
		logger.info("- transferenciaExitosa " + transferenciaExitosa);
		
		logger.info("CTRL: Finalizando transferenciaNacionalCreacion metodo");
		return Response.ok(transferenciaExitosa.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("/")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listadoTransferenciasNacionalesProgramadas(@QueryParam("page") String page,
			@QueryParam("per_page") String perPage, 
			@QueryParam("filter_by") String filterBy,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando listadoTransferenciasNacionalesProgramadas m�todo");
		
		if(page == null || perPage == null) 
			throw new BadRequestException("BIM.MENSAJ.2");
		
		if(!Utilerias.validaNumero(page)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.validaNumero(perPage)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.22");
			bimMessageDTO.addMergeVariable("perPage", perPage);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		int pageValue = Integer.parseInt(page);
		int perPageValue = Integer.parseInt(perPage);

		if(pageValue <= 0) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(perPageValue <= 0 || perPageValue > InversionesMaximoPagina) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.10");
			bimMessageDTO.addMergeVariable("perPage", perPage);
			bimMessageDTO.addMergeVariable("maximo", InversionesMaximoPagina.toString());
			throw new BadRequestException(bimMessageDTO.toString());
		}

        if(filterBy != null && !filterBy.equals(InversionesFilterBy)) {
            BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.6");
            throw new BadRequestException(bimMessageDTO.toString());
		}
		
		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);

		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String numTransac = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_DireIP = solicitud.getHeader("X-Forwarded-For") == null ? solicitud.getHeader("User-Agent") : "";
		String bit_PriRef = solicitud.getHeader("User-Agent") == null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		String usuUsuAdm = principalResultadoObjeto.get("usuUsuAdm").getAsString();
		String usuClient = principalResultadoObjeto.get("usuClient").getAsString();
		String usuNumero = principalResultadoObjeto.get("usuNumero").getAsString();
		
		
		JsonObject datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Client", usuClient);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject  datosTransferenciaSPEIConsultarResultado = this.speiServicio.transferenciaSPEIConsultar(datosTransferenciaSPEIConsultar);
		Utilerias.verificarError(datosTransferenciaSPEIConsultarResultado);
		
		JsonObject transaccionesSPEI = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSPEIConsultarResultado, "transaccionesSPEI");
		JsonArray transaccionElementoArray = Utilerias.obtenerJsonArrayPropiedad(transaccionesSPEI, "transaccionSPEI");
		
		if(transaccionesSPEI.entrySet().isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.58");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		
		JsonArray inversionesResultadoFinal = new JsonArray();
		for(JsonElement tramsaccionElemento : transaccionElementoArray) {
			JsonObject transaccionObjeto = (JsonObject) tramsaccionElemento;
			inversionesResultadoFinal.add(inversionesResultadoFinal);
		}

		
		
		logger.info("CTRL: Terminando listadoTransferenciasNacionalesProgramadas m�todo");	
		return Response.ok(inversionesResultadoFinal.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	
}
