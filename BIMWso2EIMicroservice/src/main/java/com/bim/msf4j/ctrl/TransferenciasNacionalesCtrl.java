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

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.service.BitacoraServicio;
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
	
	public TransferenciasNacionalesCtrl() {
		super();
		
		this.tokenServicio = new TokenServicio();
		this.transaccionServicio = new TransaccionServicio();
		this.speiServicio = new SPEIServicio();
		this.bitacoraServicio = new BitacoraServicio();
		this.cuentaDestinoServicio = new CuentaDestinoServicio();
		
	}

	@Path("/")
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transferenciaSPEICreacion(JsonObject datosTransferenciaSolicitud, 
			@Context Request solicitud) {
		logger.info("CTRL: Comenzando transferenciaSPEICreacion metodo");
		String bearerToken = solicitud.getHeader("Authorization");
		
		JsonObject principalResultado = Utilerias.obtenerPrincipal(bearerToken);
		logger.info("- principalResultado " + principalResultado);
		
		
		String usuUsuAdm = Utilerias.obtenerStringPropiedad(principalResultado, "usuUsuAdm");
		String usuClient = Utilerias.obtenerStringPropiedad(principalResultado, "usuClient");
		String usuNumero = Utilerias.obtenerStringPropiedad(principalResultado, "usuNumero");
		String usuNombre = Utilerias.obtenerStringPropiedad(principalResultado, "usuNombre");
		
		JsonObject datosTransferencia = Utilerias
				.obtenerJsonObjectPropiedad(datosTransferenciaSolicitud, "transferencia");
		logger.info("- datosTransferencia " + datosTransferencia);
		
		String trsCueOri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueOri");
		String trsCueDes = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsCueDes");
		String trsMonto = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsMonto");
		String trsDescri = Utilerias.obtenerStringPropiedad(datosTransferencia, "trsDescri");
		
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		logger.info("- folioTransaccionResultado " + folioTransaccionResultado);
		
		JsonObject folioTransaccion = Utilerias
				.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion"); 
		String folTransa = Utilerias.obtenerStringPropiedad(folioTransaccion, "Fol_Transa");
		
		String cpRSAToken = Utilerias.obtenerStringPropiedad(datosTransferencia, "cpRSAToken");
		/**
		 * Se utiliza usuFolTok en duro debido a que todavia no se puede obtener del principal
		 */
		String usuFolTok = "416218850";
		String validarTokenResultado = this.tokenServicio
				.validarTokenOperacion(usuFolTok, cpRSAToken, usuNumero, folTransa);
		
		if ("B".equals(validarTokenResultado)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new ForbiddenException(bimMessageDTO.toString());
		}

		if ("C".equals(validarTokenResultado)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		String fechaSis = Utilerias.obtenerFechaSis();
		
		JsonObject datosHorariosSPEI = new JsonObject();
		datosHorariosSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject horariosSPEIResultado = speiServicio.horariosSPEIConsultar(datosHorariosSPEI);
		logger.info("- horariosSPEIResultado " + horariosSPEIResultado );
		
		/**
		 * Validar horario de disponibilidad de SPEI
		 * 
		 */
		
		JsonObject datosTransfarenciaSPEI = new JsonObject();
		datosTransfarenciaSPEI.addProperty("Trs_Usuari", usuUsuAdm);
		datosTransfarenciaSPEI.addProperty("Trs_Client", usuClient);
		datosTransfarenciaSPEI.addProperty("Trs_CueOri", trsCueOri);
		datosTransfarenciaSPEI.addProperty("Trs_CueDes", trsCueDes);
		datosTransfarenciaSPEI.addProperty("Trs_Monto", Double.parseDouble(trsMonto));
		datosTransfarenciaSPEI.addProperty("Trs_Descri", trsDescri);
		datosTransfarenciaSPEI.addProperty("Trs_PriRef", usuNombre);
		datosTransfarenciaSPEI.addProperty("Trs_UsuCap", usuNumero);
		//Trs_FePrEn es un dato de prueba
		datosTransfarenciaSPEI.addProperty("Trs_FePrEn", "1900-01-01 00:00:00");
		//Trs_DurFec es un dato de prueba
		datosTransfarenciaSPEI.addProperty("Trs_DurFec", "1900-01-01 00:00:00");
		datosTransfarenciaSPEI.addProperty("Trs_Tipo","I");
		datosTransfarenciaSPEI.addProperty("Trs_TipTra","I");
		datosTransfarenciaSPEI.addProperty("Trs_Frecue","U");
		datosTransfarenciaSPEI.addProperty("FechaSis", fechaSis);
		datosTransfarenciaSPEI.addProperty("NumTransac", folTransa);
		
		JsonObject transferenciaSPEICreacionResultado = this.speiServicio
				.transaferenciaSPEICreacion(datosTransfarenciaSPEI );
		logger.info("- transferenciaSPEICreacionResultado " + transferenciaSPEICreacionResultado);
		
		String bitDireIP = solicitud.getHeader("X_Forwarded_For");
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_CueOri", trsCueOri);
		datosBitacora.addProperty("Bit_CueDes", trsCueDes);
		datosBitacora.addProperty("Bit_Monto",  trsMonto);
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
		datosBitacora.addProperty("NumTransac", folTransa);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("FechaSis", fechaSis);
		
		JsonObject bitacoraCreacionResultado = this.bitacoraServicio.creacionBitacora(datosBitacora );
		logger.info("- bitacoraCreacionResultado " + bitacoraCreacionResultado);
		
		JsonObject datosCuentaDestinoSPEI = new JsonObject();
		datosCuentaDestinoSPEI.addProperty("Cds_UsuAdm", usuUsuAdm);
		datosCuentaDestinoSPEI.addProperty("NumTransac", folTransa);
		datosCuentaDestinoSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject cuentaDestinoSPEIActivacionResultado = this.cuentaDestinoServicio
				.cuentaDestinoSPEIActivacion(datosCuentaDestinoSPEI );
		logger.info("- cuentaDestinoSPEIActivacionResultado " + cuentaDestinoSPEIActivacionResultado);
		
		JsonObject datosTransferenciaSPEIConsultar = new JsonObject();
		datosTransferenciaSPEIConsultar.addProperty("Trn_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEIConsultar.addProperty("Trn_Usuari", usuNumero);
		datosTransferenciaSPEIConsultar.addProperty("FechaSis", fechaSis);
		
		JsonObject  datosTransferenciaSPEIConsultarResultado = this.speiServicio
				.transaferenciaSPEIConsultar(datosTransferenciaSPEIConsultar);
		logger.info("- datosTransferenciaSPEIConsultarResultado " + datosTransferenciaSPEIConsultarResultado);
		
		JsonObject datosTransferenciaSPEI = new JsonObject();
		datosTransferenciaSPEI.addProperty("Trs_UsuAdm", usuUsuAdm);
		datosTransferenciaSPEI.addProperty("Trs_Usuari", usuNumero);
		datosTransferenciaSPEI.addProperty("Trs_UsuCli", usuClient);
		//Trs_Consec es un dato de prueba
		datosTransferenciaSPEI.addProperty("Trs_Consec", "0000105506");
		datosTransferenciaSPEI.addProperty("Trs_CueOri", trsCueOri);
		datosTransferenciaSPEI.addProperty("Trs_CueBen", trsCueDes);
		datosTransferenciaSPEI.addProperty("Trs_Monto", Double.parseDouble(trsMonto));
		//Trs_ConPag es un dato de prueba
		datosTransferenciaSPEI.addProperty("Trs_ConPag", "PRUEBA SPEI");
		//Trs_Banco es un dato de prueba
		datosTransferenciaSPEI.addProperty("Trs_Banco", "40012");
		//Trs_SegRef es un dato de prueba
		datosTransferenciaSPEI.addProperty("Trs_SegRef", "Nombre Usuario BE");
		//Trs_CoCuDe es un dato de prueba
		datosTransferenciaSPEI.addProperty("Trs_CoCuDe", "0000001815");
		datosTransferenciaSPEI.addProperty("Trs_TCPDir", bitDireIP);
		//Ban_Descri es un dato de prueba
		datosTransferenciaSPEI.addProperty("Ban_Descri", "BBVA BANCOMER");
		datosTransferenciaSPEI.addProperty("NumTransac", folTransa);
		datosTransferenciaSPEI.addProperty("FechaSis", fechaSis);
		
		JsonObject transferenciaSPEIProcesarResultado = this.speiServicio
				.transaferenciaSPEIProcesar(datosTransferenciaSPEI);
		logger.info("- transferenciaSPEIProcearResultado " + transferenciaSPEIProcesarResultado);
		
		JsonObject datosTransferenciaExitosa = new JsonObject();
		datosTransferenciaExitosa.addProperty("trnCueOri", trsCueOri);
		datosTransferenciaExitosa.addProperty("trnCueDes", trsMonto);
		datosTransferenciaExitosa.addProperty("trnDescri", trsDescri);
		datosTransferenciaExitosa.addProperty("numTransac", folTransa);
		datosTransferenciaExitosa.addProperty("trnBanDes", "");
		datosTransferenciaExitosa.addProperty("trnMonTot", "");
		datosTransferenciaExitosa.addProperty("trsClaRas", "");
		datosTransferenciaExitosa.addProperty("trnUsCaNo", "");
		datosTransferenciaExitosa.addProperty("trsFecAut", "");
		datosTransferenciaExitosa.addProperty("trsFecCar", "");
		datosTransferenciaExitosa.addProperty("trsFecApl", "");
		
		JsonObject transferenciaExitosa = new JsonObject();
		transferenciaExitosa.add("transferenciaExitosa", datosTransferenciaExitosa);
		logger.info("- transferenciaExitosa " + transferenciaExitosa);
		
		logger.info("CTRL: Finalizando transferenciaSPEICreacion metodo");
		return Response.ok(transferenciaExitosa.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
}
