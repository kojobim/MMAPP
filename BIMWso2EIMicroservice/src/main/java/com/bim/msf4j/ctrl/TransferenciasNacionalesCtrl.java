package com.bim.msf4j.ctrl;

import java.sql.SQLException;
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
import com.bim.commons.enums.TransferenciasProgramadasFrecuenciaEnum;
import com.bim.commons.enums.TransferenciasProgramadasLimiteEnum;
import com.bim.commons.enums.TransferenciasProgramadasRecordatorioEnum;
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
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	
	private static String InversionesFilterBy;
	private static Integer InversionesMaximoPagina;
	private static String TransferenciaNacionalDuracion;
	
	// propiedades
	private static String TransferenciaNacionalBitacoraCreacionOpBitTipOpe;
	private static String CuentaDestinoSPEIConsultarOpTipConsulL1; 
	private static String TransferenciaNacionalProgramada;
	private static String TransferenciaNacionalInmediata;
	
	
	static {		
		TransferenciaNacionalBitacoraCreacionOpBitTipOpe = properties.getProperty("op.tranferencia_nacional.bitacora_creacion.bit_tip_ope");
		TransferenciaNacionalBitacoraCreacionOpBitTipOpe = properties.getProperty("op.tranferencia_nacional.bitacora_creacion.bit_tip_ope");
		InversionesFilterBy = properties.getProperty("inversiones_servicio.filter_by");
		InversionesMaximoPagina = Integer.parseInt(properties.getProperty("inversiones_servicio.maximo_pagina"));
		
		CuentaDestinoSPEIConsultarOpTipConsulL1 = properties.getProperty("op.cuenta_destino_spei_consultar.tip_consul.l1");
		TransferenciaNacionalDuracion = properties.getProperty("transferencias_spei.duracion");
		TransferenciaNacionalProgramada = properties.getProperty("transferencias_spei.programada");
		TransferenciaNacionalInmediata = properties.getProperty("transferencias_spei.inmediata");
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
	public Response transferenciaNacionalCreacion(JsonObject datosTransferenciaSolicitud, @Context Request solicitud) throws SQLException {
		logger.info("CTRL: Comenzando transferenciaNacionalCreacion metodo");
		
        //inicia declaracion de variables
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
        String usuFolTok = "0416218854"; //valor hard-code hasta integracion de servicio login
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
		String trnSecuen = null;
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
		String plantillaTransferenciaProgramadaCliente = null;
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
		String trsFrecue = null;
		String trsFePrEn = null;
		String trsTipDur = null;
		String trsDurFec = null;
		String strFrecuencia = null;
		String strRecordatorio = null;
		String strDuracion = null;
		String firUsuNom = null;
		String trsCueDesConsulta = null;
		String cdsEmaBen = null;
		String trnConsecConsulta = null;
		Date fecha = null;
		Date fechaActual = null;
	    Integer trsDurTra = null;
	    Integer trsDiAnEm = null;
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
		JsonObject transferenciaSPEICreacion = null;
		JsonObject datosBitacora = null;
		JsonObject bitacoraCreacionResultado = null;
		JsonObject datosCuentaDestinoSPEI = null;
		JsonObject cuentaDestinoSPEIActivacionResultado = null;
		JsonObject datosTransferenciaSPEIConsultar = null;
		JsonObject datosTransferenciaSPEIConsultarResultado = null;
		JsonObject transferenciasSPEI = null;
		JsonObject transferenciaSPEIObjeto = null;
		JsonObject cuentaDestinoObjeto = null;
		JsonObject datosTransferenciaSPEI = null;
		JsonObject transferenciaSPEIProcesarResultado = null;
		JsonObject transferenciaSPEIProcesarResultadoObjeto = null;
		JsonObject transferenciaExitosa = null;
		JsonObject cpTransferenciaProgramada = null;
		JsonObject transferenciaObjeto = null;
		JsonObject horariosSPEIResultado = null;
		JsonObject cuentaDestinoObjetoResultado = null;
		JsonObject datosCuentaDestinoConsultaSPEI = null;
		JsonObject cuentaDestinoConsultaResultado = null;
		JsonObject cuentaDestinoSPEIConsultarResultadoObjeto = null;
		JsonObject transferenciaSPEIFirmasConsultarResultadoObjeto = null;
		JsonObject datosTransferenciaSPEIFirmas = null;
		
		JsonArray transferenciaSPEI = null;
		JsonArray cuentasDestinoArreglo = null;
		JsonArray transferenciaSPEIFirmasConsultarResultadoArray = null;
        //termina declaracion de variables
		
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
		
		if(datosTransferenciaSolicitud.isJsonNull()) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.56");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		datosTransferencia = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSolicitud, "transferencia");
		
		// validacion de propiedad extraida de la solicitud
		if(datosTransferencia.has("trsMonto")) {
			trsMonto = Utilerias.obtenerDoublePropiedad(datosTransferencia, "trsMonto");
		} else {
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "trsMonto");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		// propiedades extraidas de la solicitud
		cpRSAToken = Utilerias.obtenerStringPropiedad(datosTransferencia, "cpRSAToken");
		trsCueOri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueOri");
		trsCueDes = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueDes");
		trsDescri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDescri");
		trsRFC = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsRFC");
		trsIVA = Utilerias.obtenerDoublePropiedad(datosTransferencia, "trsIVA");
		trsDaBeAd = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDaBeAd");
		
        if(Strings.isNullOrEmpty(cpRSAToken)){
            bimMessageDTO = new BimMessageDTO("COMMONS.400");
            bimMessageDTO.addMergeVariable("propiedad", "cpRSAToken");
            throw new BadRequestException(bimMessageDTO.toString());
            
        } else if(Strings.isNullOrEmpty(trsCueOri)) {
            bimMessageDTO = new BimMessageDTO("COMMONS.400");
            bimMessageDTO.addMergeVariable("propiedad", "trsCueOri");
            throw new BadRequestException(bimMessageDTO.toString());
            
        } else if(Strings.isNullOrEmpty(trsCueDes)) {
            bimMessageDTO = new BimMessageDTO("COMMONS.400");
            bimMessageDTO.addMergeVariable("propiedad", "trsCueDes");
            throw new BadRequestException(bimMessageDTO.toString());
 
        } else if(Strings.isNullOrEmpty(trsDaBeAd)) {
            bimMessageDTO = new BimMessageDTO("COMMONS.400");
            bimMessageDTO.addMergeVariable("propiedad", "trsDaBeAd");
            throw new BadRequestException(bimMessageDTO.toString());
        }
        
		//validacion para tranferencia programada
		if(datosTransferencia.has("cpTransferenciaProgramada") && 
				!datosTransferencia.get("cpTransferenciaProgramada").isJsonNull()) {
			cpTransferenciaProgramada = (JsonObject)datosTransferencia.get("cpTransferenciaProgramada");
			trsFrecue = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trsFrecue");
		    trsFePrEn = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trsFePrEn");
		    trsTipDur = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trsTipDur");
		    trsDurFec = Utilerias.obtenerStringPropiedad(cpTransferenciaProgramada, "trsDurFec");
		    trsDurTra = Utilerias.obtenerIntPropiedad(cpTransferenciaProgramada, "trsDurTra");
		    trsDiAnEm = Utilerias.obtenerIntPropiedad(cpTransferenciaProgramada, "trsDiAnEm");
		    trnTipTra = TransferenciaNacionalProgramada;
		} else {
			trnTipTra = TransferenciaNacionalInmediata;
		}
		
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
		
		//consulta de horarios
		datosHorariosSPEI = new JsonObject();
		datosHorariosSPEI.addProperty("FechaSis", fechaSis);
		
		horariosSPEIResultado = speiServicio.horariosSPEIConsultar(datosHorariosSPEI);
		
		if(horariosSPEIResultado.has("Msj_Error")) {
			errMensaj = Utilerias.obtenerStringPropiedad(horariosSPEIResultado, "Msj_Error");
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		datosCuentaDestinoConsultaSPEI = new JsonObject();
		datosCuentaDestinoConsultaSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoConsultaSPEI.addProperty("Cds_Usuari", usuNumero);
		datosCuentaDestinoConsultaSPEI.addProperty("FechaSis", fechaSis);
		datosCuentaDestinoConsultaSPEI.addProperty("Tip_Consul", CuentaDestinoSPEIConsultarOpTipConsulL1);
		cuentaDestinoConsultaResultado = this.cuentaDestinoServicio.cuentaDestinoSPEIConsultar(datosCuentaDestinoConsultaSPEI);
		Utilerias.verificarError(cuentaDestinoConsultaResultado);
		
		cuentaDestinoSPEIConsultarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(cuentaDestinoConsultaResultado, "cuentasDestino");
		cuentasDestinoArreglo = Optional
				.fromNullable(Utilerias.obtenerJsonArrayPropiedad(cuentaDestinoSPEIConsultarResultadoObjeto, "cuentaDestino"))
				.or(new JsonArray());
		
		for(JsonElement cuentaDestinoElemento : cuentasDestinoArreglo) {
			cuentaDestinoObjetoResultado = cuentaDestinoElemento.getAsJsonObject();
			trsCueDesConsulta = Utilerias.obtenerStringPropiedad(cuentaDestinoObjetoResultado, "Cds_CLABE");
			if(trsCueDes.equals(trsCueDesConsulta))
				cuentaDestinoObjeto = (JsonObject)cuentaDestinoElemento;
		}
		
		cdsEmaBen = Utilerias.obtenerStringPropiedad(cuentaDestinoObjeto, "Cds_EmaBen");
		logger.info("EMAIL BENEFICIARIO " + cdsEmaBen);
		logger.info("@@@@@@@@@@@@@@@@@@@@ TIP TRA " + trnTipTra);
		
		//creacion de transferencia 
		datosTransferenciaNacional = new JsonObject();
		datosTransferenciaNacional.addProperty("Trs_Usuari", usuUsuAdm);
		datosTransferenciaNacional.addProperty("Trs_Client", usuClient);
		datosTransferenciaNacional.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaNacional.addProperty("Trs_CueDes", trsCueDes);
		datosTransferenciaNacional.addProperty("Trs_Monto", trsMonto);
		datosTransferenciaNacional.addProperty("Trs_Descri", trsDescri);
		datosTransferenciaNacional.addProperty("Trs_PriRef", usuNombre);
		datosTransferenciaNacional.addProperty("Trs_RFC", trsRFC);
		datosTransferenciaNacional.addProperty("Trs_IVA", trsIVA != null ? trsIVA : 0.0);
		datosTransferenciaNacional.addProperty("Trs_Email", cdsEmaBen);
		datosTransferenciaNacional.addProperty("Trs_Tipo", "I");
		datosTransferenciaNacional.addProperty("Trs_UsuCap", usuNumero);
		datosTransferenciaNacional.addProperty("Trs_TipTra", trnTipTra);
		datosTransferenciaNacional.addProperty("Trs_Frecue", trsFrecue != null ? trsFrecue : ""); // si no "U"
		datosTransferenciaNacional.addProperty("Trs_FePrEn", trsFePrEn != null ? trsFePrEn : "");
		datosTransferenciaNacional.addProperty("Trs_TipDur", trsTipDur != null ? trsTipDur : "");
		datosTransferenciaNacional.addProperty("Trs_DurFec", trsDurFec != null ? trsDurFec : "");
		datosTransferenciaNacional.addProperty("Trs_DurTra", trsDurTra != null ? trsDurTra : 0);
		datosTransferenciaNacional.addProperty("Trs_DiAnEm", trsDiAnEm != null ? trsDiAnEm : 0);
		datosTransferenciaNacional.addProperty("NumTransac", numTransac);	
		datosTransferenciaNacional.addProperty("FechaSis", fechaSis);
		
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ datosTransferenciaNacional " + datosTransferenciaNacional);
		transferenciaSPEICreacionResultado = this.speiServicio.transferenciaSPEICreacion(datosTransferenciaNacional);
		Utilerias.verificarError(transferenciaSPEICreacionResultado);
		JsonObject transferenciaSPEICreacionResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEICreacionResultado, "transferenciaSPEI");
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ transferenciaSPEICreacionResultadoObjeto " + transferenciaSPEICreacionResultadoObjeto);

		errCodigo = Utilerias.obtenerStringPropiedad(transferenciaSPEICreacionResultadoObjeto, "Err_Codigo");
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ errCodigo " + errCodigo);
		
		if(!"000000".equals(errCodigo)) {
			errMensaj = Utilerias.obtenerStringPropiedad(transferenciaSPEICreacionResultadoObjeto, "Err_Mensaj");			
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		transferenciaSPEICreacion = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEICreacionResultado, "transferenciaSPEI");
		trsConsec = Utilerias.obtenerStringPropiedad(transferenciaSPEICreacion, "Trs_Consec");
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ CONSEC " + trsConsec);
		
		
		// creacion de registro en bitacora
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
		
		// activaci�n de cuenta para transferencia
		datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("NumTransac", numTransac);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		cuentaDestinoSPEIActivacionResultado = this.cuentaDestinoServicio.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEI );
		Utilerias.verificarError(cuentaDestinoSPEIActivacionResultado);
		
		// consulta de transferencia
		datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		
		datosTransferenciaSPEIConsultarResultado = this.speiServicio.transferenciaSPEIConsultarResultSets(datosTransferenciaSPEIConsultar);
		Utilerias.verificarError(datosTransferenciaSPEIConsultarResultado);
		
		transferenciasSPEI = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSPEIConsultarResultado, "transferenciasSPEI");
		transferenciaSPEI = Utilerias.obtenerJsonArrayResultante(transferenciasSPEI, "transferenciaSPEI");
		
		// Validar que objeto extraer del arreglo 
		JsonArray transferenciaNacionalConsultarResultadoArreglo1 = transferenciaSPEI.get(0).getAsJsonArray();
		logger.info(">>>>>>>>>>>>>>>>>>> transferenciaNacionalConsultarResultadoArreglo1 " + transferenciaNacionalConsultarResultadoArreglo1);
		for(JsonElement transferencia : transferenciaNacionalConsultarResultadoArreglo1) {
			transferenciaObjeto = transferencia.getAsJsonObject();
			trnConsecConsulta = Utilerias.obtenerStringPropiedad(transferenciaObjeto, "Trn_Consec");
			if(trsConsec.equals(trnConsecConsulta)) 
				transferenciaSPEIObjeto = (JsonObject)transferencia;
		}
		
		logger.info("@@@@@@ transferenciaSPEIObjeto " + transferenciaSPEIObjeto);
		// extraccion de propiedades de la consulta
		trnBanDes = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_BanDes");
		trnMonTot = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_MonTot");
		trnUsCaNo = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_UsCaNo");
		cdeConsec = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Cde_Consec");
		trnBanco = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_Banco");
		trnFePrEn = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_FePrEn");
		trnDesAdi = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_DesAdi");
		trnFecCap = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_FecCap");
		cdeEmaBen = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Cde_EmaBen");
		trnSecuen = Utilerias.obtenerStringPropiedad(transferenciaSPEIObjeto, "Trn_Secuen");
		
		// procesamiento de transferencia
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
		datosTransferenciaSPEI.addProperty("Trs_TipTra", trnTipTra);
		datosTransferenciaSPEI.addProperty("Trs_ValFir", "S");
		datosTransferenciaSPEI.addProperty("Trs_DaBeAd", trsDaBeAd);
		datosTransferenciaSPEI.addProperty("Ban_Descri", trnBanDes);
//		datosTransferenciaSPEI.addProperty("NumTransac", numTransac);
		datosTransferenciaSPEI.addProperty("FechaSis", fechaSis);
		logger.info(">>>>>>>>>>>>>>> PROCESAR " + datosTransferenciaSPEI);
		transferenciaSPEIProcesarResultado = this.speiServicio.transferenciaSPEIProcesar(datosTransferenciaSPEI);
		Utilerias.verificarError(transferenciaSPEIProcesarResultado);
		transferenciaSPEIProcesarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEIProcesarResultado, "transferenciaSPEI");
		
		datosTransferenciaSPEIFirmas = new JsonObject();
		datosTransferenciaSPEIFirmas.addProperty("Fts_Consec", trsConsec);
		datosTransferenciaSPEIFirmas.addProperty("FechaSis", fechaSis);
		transferenciaSPEIFirmasConsultarResultadoObjeto = this.speiServicio.transferenciaSPEIFirmasConsultarResultSet(datosTransferenciaSPEIFirmas);
		
		/**
		 * REGLA DE NEGOCIO: Env�o de correo con plantilla establecida por BIM y encriptado de digito verificador
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
		
		// validacion para transferencia inmediata
		if(("I").equals(trnTipTra)) {
			emailTemplateCliente = new BimEmailTemplateDTO(plantillaTransferenciaInmediataCliente);
			emailTemplateCliente.addMergeVariable("Trn_ProDes", "ACEPTADA");
		}
		
		// validacion para transferencia programada
		if("P".equals(trnTipTra)) {
			
			if(TransferenciasProgramadasFrecuenciaEnum.validarFrecuencia(trsFrecue) == null){
				bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
				bimMessageDTO.addMergeVariable("nombreParametro", "trsFrecue");
				bimMessageDTO.addMergeVariable("valor", trsFrecue);
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			// valida la fecha de inicio de la transferencia
			if(trsFePrEn == null) {
				bimMessageDTO = new BimMessageDTO("COMMONS.400");
				bimMessageDTO.addMergeVariable("propiedad", "trsFePrEn");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			// valida el tipo de limite para la transferencia
			if(TransferenciasProgramadasLimiteEnum.validarLimite(trsTipDur) == null ) {
				bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
				bimMessageDTO.addMergeVariable("nombreParametro", "trsTipDur");
				bimMessageDTO.addMergeVariable("valor", trsTipDur);
				throw new BadRequestException(bimMessageDTO.toString());
			} 

			// validacion si se Limita por fecha
			if(trsTipDur.equals(TransferenciasProgramadasLimiteEnum.F.toString())) {
				if(trsDurFec == null) {
					bimMessageDTO = new BimMessageDTO("COMMONS.400");
					bimMessageDTO.addMergeVariable("propiedad", "trsDurFec");
					throw new BadRequestException(bimMessageDTO.toString());
				}
			}
			
			// validacion si se Limita por cantidad
			if(trsTipDur.equals(TransferenciasProgramadasLimiteEnum.T.toString())) {
				if(trsDurTra == null || trsDurTra < 0) {
					bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.59");
					bimMessageDTO.addMergeVariable("nombreParametro", "trsDurTra");
					bimMessageDTO.addMergeVariable("valor", trsDurTra.toString());
					throw new BadRequestException(bimMessageDTO.toString());
				}
			}
			
			// validacion si el numero de dias para recordatorio es nulo
			if(trsDiAnEm == null) {
				bimMessageDTO = new BimMessageDTO("COMMONS.400");
				bimMessageDTO.addMergeVariable("propiedad", "trsDiAnEm");
				throw new BadRequestException(bimMessageDTO.toString());
			}
			
			// validacion si el numero de dias es mayor a 3 dias
			if(trsDiAnEm > 3) {
				bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.65");
				bimMessageDTO.addMergeVariable("nombreParametro", "trsDiAnEm");
				bimMessageDTO.addMergeVariable("dias", "3");
				throw new ConflictException(bimMessageDTO.toString());
			}
			
			strFrecuencia = TransferenciasProgramadasFrecuenciaEnum.validarFrecuencia(trsFrecue);
			strRecordatorio = TransferenciasProgramadasRecordatorioEnum.validarRecordatorio(trsDiAnEm);
			
			if("N".equals(trnSecuen) || "0".equals(trnSecuen))
				strDuracion = TransferenciaNacionalDuracion;
			
			fecha = Utilerias.convertirFecha(trsFePrEn, "yyyy-MM-dd");
			fechaActual = Utilerias.convertirFecha(Utilerias.obtenerFechaSis(), "yyyy-MM-dd");
			
			// valida si la fecha programada es menor a la fecha actual
			if(fecha.compareTo(fechaActual) < 0) {
				bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.64");
				bimMessageDTO.addMergeVariable("nombreParametro", "trsFePrEn");
				bimMessageDTO.addMergeVariable("valor", trsFePrEn);
				throw new BadRequestException(bimMessageDTO.toString());
			}
			// valida si la fecha programada es igual a la fecha actual
			if(fecha.compareTo(fechaActual) == 0) {
				emailTemplateCliente = new BimEmailTemplateDTO(plantillaTransferenciaInmediataCliente);
				
				//descripcion de Trn_ProDes temporal
				emailTemplateCliente.addMergeVariable("Trn_ProDes", "Programada Enviada");
				
			 	datosTransferenciaSPEI.addProperty("Trs_ValFir", "N");
			 	transferenciaSPEIProcesarResultado = this.speiServicio.transferenciaSPEIProcesar(datosTransferenciaSPEI);
				Utilerias.verificarError(transferenciaSPEIProcesarResultado);
				transferenciaSPEIProcesarResultadoObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEIProcesarResultado, "transferenciaSPEI");
				logger.info("@@@@@@@ transferenciaSPEIProcesarResultadoObjeto " + transferenciaSPEIProcesarResultadoObjeto);
				
				transferenciaSPEIFirmasConsultarResultadoObjeto = this.speiServicio.transferenciaSPEIFirmasConsultarResultSet(datosTransferenciaSPEIFirmas);
				logger.info(">>>>>> transferenciaSPEIFirmasConsultarResultadoObjeto " + transferenciaSPEIFirmasConsultarResultadoObjeto);
				Utilerias.verificarError(transferenciaSPEIFirmasConsultarResultadoObjeto);
				
			// valida si la fecha programada es mayor a la fecha actual
			} else if(fecha.compareTo(fechaActual) > 0) {
				emailTemplateCliente = new BimEmailTemplateDTO(plantillaTransferenciaProgramadaCliente);
				//descripcion de Trn_ProDes temporal
				emailTemplateCliente.addMergeVariable("Trn_ProDes", "Autorizada");
				emailTemplateCliente.addMergeVariable("strFrecuencia", strFrecuencia);
				emailTemplateCliente.addMergeVariable("strDuracion", strDuracion);
				emailTemplateCliente.addMergeVariable("strRecordatorio", strRecordatorio);				
			}
		}
		
		//propiedades extraidas de SP procesar
		errCodigo = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Codigo");
		logger.info(">>>>>>>>>>>>> UTILERIAS " + Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_ClaRas"));
		trsClaRas = (Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_ClaRas").trim());
		trsFecAut = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecAut");
		trsFecCar = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecCar");
		trsFecApl = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Trs_FecApl");
		
		if(!"000000".equals(errCodigo)) {
			errMensaj = Utilerias.obtenerStringPropiedad(transferenciaSPEIProcesarResultadoObjeto, "Err_Mensaj");			
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		JsonObject transferenciasFirmasConsultarObjeto = Utilerias.obtenerJsonObjectPropiedad(transferenciaSPEIFirmasConsultarResultadoObjeto, "transferenciasSPEI");
		logger.info(">>>>>>>>>>>>>>>>>>> transferenciasFirmasConsultarObjeto " + transferenciasFirmasConsultarObjeto);

		JsonArray transferenciaFirmasConsultarArray = Utilerias.obtenerJsonArrayResultante(transferenciasFirmasConsultarObjeto, "transferenciaSPEI");
		logger.info(">>>>>>>>>>>>>>>>>>> transferenciaFirmasConsultarArray " + transferenciaFirmasConsultarArray);

		transferenciaSPEIFirmasConsultarResultadoArray = transferenciaFirmasConsultarArray.get(1).getAsJsonArray();
		logger.info(">>>>>>>>>>>>>>>>>>> transferenciaSPEIFirmasConsultarResultadoArray " + transferenciaSPEIFirmasConsultarResultadoArray);

		JsonObject transferenciaFirmasConsultarResultadoObjeto = transferenciaSPEIFirmasConsultarResultadoArray.get(0).getAsJsonObject();
		if(!transferenciaFirmasConsultarResultadoObjeto.isJsonNull()) {
			firUsuNom = Utilerias.obtenerStringPropiedad(transferenciaFirmasConsultarResultadoObjeto, "Fts_UsuNom");
			logger.info(">>>>>>>>>>> Fts_UsuNom >>>>>>>>>>>" +firUsuNom);
		}
		
		monto = new StringBuilder()
			.append("$")
			.append(trsMonto)
			.toString();
		
		// variables de template para envio de email al cliente
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
		emailTemplateCliente.addMergeVariable("str_Autori", firUsuNom != null ? firUsuNom : "");
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
		logger.info("EMAIL CLI " + usuEmail);
		
		// variables de template para envio de email al beneficiario
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
		logger.info("EMAIL BEN " + cdeEmaBen);

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
	
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response listadoTransferenciasNacionalesProgramadas(@QueryParam("page") String page,
			@QueryParam("per_page") String perPage, 
			@QueryParam("filter_by") String filterBy,
			@Context final Request solicitud) {
		logger.info("CTRL: Comenzando listadoTransferenciasNacionalesProgramadas metodo");
		
		// Inicia declaración de variables
		String cpTrnTransf = null;
		String trnDeCuOr =  null;
		String trnDeCuDe =  null;
		String trnBanDes =  null;
		String trnDescri =  null;
		String trnFePrEn =  null;
		String trnEmaBen =  null;
		String trnFrecue =  null;
		String cpTrnSec =  null;
		String trnSigSec =  null;
		String trnSecuen =  null;
		String trnTipDur =  null;
		String trnTransf = null;
		String trnfecha = null;
		String cpTrnSecue = null;
		String bearerToken = null;
		String fechaSis = null;
		String numTransac = null;
		String usuUsuAdm = null;
		String usuClient = null;
		String usuNumero = null;
		Integer totalElementos = null;
		Integer trnDiAnEm =  null;
		
		JsonArray datosTransferenciaListado = null;
		JsonArray transaccionElementoArray = null;
		JsonArray transferenciasProgramadasActivas = null;
		JsonArray transferenciasProgramadas = null;
		JsonObject principalResultadoObjeto = null;
		JsonObject folioTransaccionGenerarOpResultadoObjeto = null;
		JsonObject datosTransferenciaSPEIConsultar = null;
		JsonObject  datosTransferenciaSPEIConsultarResultado = null;
		JsonObject transaccionesSPEI = null;
		JsonObject datosTransaccionNacionalObjeto = null;	
		JsonObject numTransacObjeto = null;	
		//verificando propiedades page y per_page
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
		
		bearerToken = solicitud.getHeader("Authorization");
		principalResultadoObjeto = Utilerias.obtenerPrincipal(bearerToken);

		fechaSis = Utilerias.obtenerFechaSis();
		
		folioTransaccionGenerarOpResultadoObjeto = this.transaccionServicio.folioTransaccionGenerar();
		if(logger.isDebugEnabled()){
			logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);
		}

		numTransacObjeto = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionGenerarOpResultadoObjeto, "transaccion");
		numTransac = Utilerias.obtenerStringPropiedad(numTransacObjeto, "Fol_Transa");
		usuUsuAdm = principalResultadoObjeto.get("usuUsuAdm").getAsString();
		usuClient = principalResultadoObjeto.get("usuClient").getAsString();
		usuNumero = principalResultadoObjeto.get("usuNumero").getAsString();	
		
		datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Client", usuClient);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		datosTransferenciaSPEIConsultar.addProperty("NumTransac", numTransac);
		
		datosTransferenciaSPEIConsultarResultado = this.speiServicio.transferenciaSPEIConsultar(datosTransferenciaSPEIConsultar);
		Utilerias.verificarError(datosTransferenciaSPEIConsultarResultado);
		
		transaccionesSPEI = Utilerias.obtenerJsonObjectPropiedad(datosTransferenciaSPEIConsultarResultado, "transferenciasSPEI");
		transaccionElementoArray = Utilerias.obtenerJsonArrayPropiedad(transaccionesSPEI, "transferenciaSPEI");
		
		if(transaccionesSPEI.entrySet().isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.58");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if (transaccionesSPEI.has("transferenciaSPEI")) { // existe un innerObject con las transferencias?

			// solo transferencias programadas activas
			
			if(transaccionesSPEI.get("transferenciaSPEI").isJsonObject()) {
				
				if(logger.isDebugEnabled()) {
					logger.debug("resultset is a JsonObject: " + transaccionesSPEI.get("transferenciaSPEI").getAsJsonObject());
				}
				
				datosTransferenciaListado = new JsonArray();
				datosTransferenciaListado.add(transaccionesSPEI.get("transferenciaSPEI").getAsJsonObject());
				logger.info("datos trasnferencias listado del if   " + datosTransferenciaListado);
			}else{
				
				datosTransferenciaListado = transaccionElementoArray;
				logger.info("datos trasnferencias listado del else   " + datosTransferenciaListado);
			}
			
		}
		
		transferenciasProgramadasActivas = Utilerias.filtrarPropiedadesArray(
				datosTransferenciaListado,
				jsonObject -> jsonObject.get("Trn_TipTra").getAsString().equals("P")
						&& jsonObject.get("Trn_Status").getAsString().equals("A"));


		totalElementos = transferenciasProgramadasActivas.size();
		transferenciasProgramadasActivas = Utilerias.paginado(transferenciasProgramadasActivas, pageValue, perPageValue);
		
		transferenciasProgramadas = new JsonArray();
		for(JsonElement transaccionElemento : transferenciasProgramadasActivas) {
			JsonObject transaccionObjeto = (JsonObject) transaccionElemento;
			//asignación de variables
			trnDeCuOr = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_DeCuOr");
			trnDeCuDe = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_CueOri");
			trnBanDes = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_BanDes");
			trnDescri = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_Descri");
			trnfecha = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_FePrEn");
			trnEmaBen = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_EmaBen");
			trnFrecue = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_Frecue");
			trnDiAnEm = Utilerias.obtenerIntPropiedad(transaccionObjeto, "Trn_DiAnEm");
			trnSigSec = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_SigSec");
			trnSecuen = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_Secuen");
			trnTipDur = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_TipDur");
			trnTransf = Utilerias.obtenerStringPropiedad(transaccionObjeto, "Trn_Transf");
			trnFePrEn = Utilerias.formatearFecha(trnfecha, "yyyy-MM-dd");
			
			if(trnTipDur.trim().equals("I")){ // duraci�n ilimitada
				trnSecuen = "N";
				cpTrnSecue = "Sin limite";
			}else {
				cpTrnSecue = trnSecuen;
			}
			
			cpTrnSec = new StringBuilder(trnSigSec)
					.append("/")
					.append(trnSecuen)
					.toString();
			if(trnTransf.trim().equals("S")) {
				cpTrnTransf = "SPEI";
			}else {
				cpTrnTransf = "TEF";
			}

			//formateo de objeto
			datosTransaccionNacionalObjeto = new JsonObject();
			datosTransaccionNacionalObjeto.addProperty("cpTrnTransf", cpTrnTransf);
			datosTransaccionNacionalObjeto.addProperty("trnDeCuOr", trnDeCuOr);
			datosTransaccionNacionalObjeto.addProperty("trnDeCuDe", trnDeCuDe);
			datosTransaccionNacionalObjeto.addProperty("trnBanDes", trnBanDes);
			datosTransaccionNacionalObjeto.addProperty("trnDescri", trnDescri);
			datosTransaccionNacionalObjeto.addProperty("trnFePrEn", trnFePrEn);
			datosTransaccionNacionalObjeto.addProperty("cpTrnSec", cpTrnSec);
			datosTransaccionNacionalObjeto.addProperty("trnEmaBen", trnEmaBen);
			datosTransaccionNacionalObjeto.addProperty("trnFrecue", trnFrecue);
			datosTransaccionNacionalObjeto.addProperty("cpTrnSecue", cpTrnSecue);
			datosTransaccionNacionalObjeto.addProperty("trnDiAnEm", trnDiAnEm);
			transferenciasProgramadas.add(datosTransaccionNacionalObjeto);
			
		}		
		
		logger.info("CTRL: Terminando listadoTransferenciasNacionalesProgramadas m�todo");	
		return Response
				.ok(transferenciasProgramadas)
				.header("X-Total-Count", totalElementos)
				.build();
	}	

	
}
