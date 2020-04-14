package com.bim.msf4j.ctrl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.exceptions.UnauthorizedException;
import com.bim.commons.service.CoDiServicio;
import com.bim.commons.service.CuentaServicio;
import com.bim.commons.service.TransaccionServicio;
import com.bim.commons.service.UsuarioServicio;
import com.bim.commons.utils.Racal;
import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

@Path("/codi")
public class CoDiCtrl extends BimBaseCtrl{

	private static final Logger LOGGER = Logger.getLogger(CoDiCtrl.class);
	
	private UsuarioServicio usuarioServicio;
	private CuentaServicio cuentaServicio;
	private TransaccionServicio transaccionServicio;
	private CoDiServicio codiServicio;
			
	public CoDiCtrl() {
		super();
		
		this.codiServicio = new CoDiServicio();
		this.usuarioServicio = new UsuarioServicio();
		this.cuentaServicio = new CuentaServicio();
		this.transaccionServicio = new TransaccionServicio();
				
	}
	
	@Path("/confirma-pago")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmaPagoCoDi(JsonObject datosConfirmacion) {
		LOGGER.info("CTRL: Comenzando confirmaPagoCoDi metodo");
		// solo para caso de error
		BimMessageDTO bimMessageDTO = null;
		
		//objetos a utilizar
		JsonObject confirmaPagoCoDiReq = null;
		JsonObject datosComprador = null;
		JsonObject datosVendedor = null;
		JsonObject datosResultado = null;
		
		//primitivos a utilizar
		String tipoAviso = null;
		String fechaSis = Utilerias.obtenerFechaSis();
		
		// validar que todos los datos esten presentes puesto que son obligatorios
		datosConfirmacion = Utilerias.obtenerJsonObjectPropiedad(datosConfirmacion, "datosConfirmacion");

		if(!datosConfirmacion.has("cpFolio")){ // IDC es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "cpFolio");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosConfirmacion.has("cpTipoAviso")){ // el tipo de aviso de procesamiento es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "cpTipoAviso");
			throw new BadRequestException(bimMessageDTO.toString());			
		}else{
			
			tipoAviso = Utilerias.obtenerStringPropiedad(datosConfirmacion, "cpTipoAviso");
			
			if(!tipoAviso.equals("0") && !tipoAviso.equals("2")) { //el tipo de aviso no existe o esta fuera del alcance de confirmacion por cliente/aplicacion
				bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.79");
				throw new BadRequestException(bimMessageDTO.toString());				
			}
		}		
		
		if(!datosConfirmacion.has("monto")) { // el monto debe existir
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "monto");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(Utilerias.obtenerDoublePropiedad(datosConfirmacion, "monto") < 0) { // el monto no debe ser un valor negativo
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.80");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosConfirmacion.has("concepto")) { // el concepto es obligatorio 
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "concepto");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosConfirmacion.has("cpReferencia")) { // el concepto es obligatorio 
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "cpReferencia");
			throw new BadRequestException(bimMessageDTO.toString());		
		}
		
		datosComprador = Utilerias.obtenerJsonObjectPropiedad(datosConfirmacion, "comprador");
		
		if(datosComprador.isJsonNull() || !datosComprador.isJsonObject()){ //objeto comprador inválido
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "comprador");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosComprador.has("alias")) { //alias de numero de celular obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "alias del comprador");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosComprador.has("dv")) { // digito verificador obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "digito verificador del comprador");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosComprador.has("banco")) { // banco obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "banco del comprador");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosComprador.has("cuentaTipo")) { //tipo de la cuenta es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "tipo de cuenta del comprador");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosComprador.has("clabe")) { // CLABE es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "CLABE del comprador");
			throw new BadRequestException(bimMessageDTO.toString());	
		}
		
		datosVendedor = Utilerias.obtenerJsonObjectPropiedad(datosConfirmacion, "vendedor");
		
		if(datosVendedor.isJsonNull() || !datosVendedor.isJsonObject()){ //objeto comprador inválido
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "vendedor");
			throw new BadRequestException(bimMessageDTO.toString());	
		}
		
		if(!datosVendedor.has("alias")) { //alias de numero de celular obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "alias del vendedor");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosVendedor.has("dv")) { // digito verificador obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "digito verificador del vendedor");
			throw new BadRequestException(bimMessageDTO.toString());	
		}
		
		if(!datosVendedor.has("banco")) { // banco obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "banco del vendedor");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosVendedor.has("cuentaTipo")) { //tipo de la cuenta es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "tipo de cuenta del vendedor");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosVendedor.has("clabe")) { // CLABE es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "CLABE del vendedor");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!datosVendedor.has("titular")) { // titular es obligatorio
			bimMessageDTO = new BimMessageDTO("COMMONS.400");
			bimMessageDTO.addMergeVariable("propiedad", "titular de la cuenta del vendedor");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		
		// comienza confirmacion
		JsonObject folioTransaccionResultado = this.transaccionServicio.folioTransaccionGenerar();
		
		folioTransaccionResultado = Utilerias.obtenerJsonObjectPropiedad(folioTransaccionResultado, "transaccion");
		
		confirmaPagoCoDiReq = new JsonObject();
		confirmaPagoCoDiReq.addProperty("Cod_NumTra", Utilerias.obtenerStringPropiedad(datosConfirmacion, "transaccion"));
		confirmaPagoCoDiReq.addProperty("Cod_TipAvi", Utilerias.obtenerStringPropiedad(datosConfirmacion, "cpTipoAviso"));
		confirmaPagoCoDiReq.addProperty("Cod_FoEsCD", Utilerias.obtenerStringPropiedad(datosConfirmacion, "cpFolio"));
		confirmaPagoCoDiReq.addProperty("Cod_Monto", Utilerias.obtenerDoublePropiedad(datosConfirmacion, "monto"));
		confirmaPagoCoDiReq.addProperty("Cod_Concep", Utilerias.obtenerStringPropiedad(datosConfirmacion, "concepto"));
		confirmaPagoCoDiReq.addProperty("Cod_RefNum", Utilerias.obtenerStringPropiedad(datosConfirmacion, "cpReferencia"));
		confirmaPagoCoDiReq.addProperty("Cod_NumCeC", Utilerias.obtenerStringPropiedad(datosComprador, "alias"));
		confirmaPagoCoDiReq.addProperty("Cod_DigVeC", Utilerias.obtenerStringPropiedad(datosComprador, "dv"));
		confirmaPagoCoDiReq.addProperty("Cod_BancoC", Utilerias.obtenerStringPropiedad(datosComprador, "banco"));
		confirmaPagoCoDiReq.addProperty("Cod_TipCuC", Utilerias.obtenerStringPropiedad(datosComprador, "cuentaTipo"));
		confirmaPagoCoDiReq.addProperty("Cod_CuentC", Utilerias.obtenerStringPropiedad(datosComprador, "clabe"));
		confirmaPagoCoDiReq.addProperty("Cod_NumCeV", Utilerias.obtenerStringPropiedad(datosVendedor, "alias"));
		confirmaPagoCoDiReq.addProperty("Cod_DigVeV", Utilerias.obtenerStringPropiedad(datosVendedor, "dv"));
		confirmaPagoCoDiReq.addProperty("Cod_BancoV", Utilerias.obtenerStringPropiedad(datosVendedor, "banco"));
		confirmaPagoCoDiReq.addProperty("Cod_TipCuV", Utilerias.obtenerStringPropiedad(datosVendedor, "cuentaTipo"));
		confirmaPagoCoDiReq.addProperty("Cod_CuentV", Utilerias.obtenerStringPropiedad(datosVendedor, "clabe"));
		confirmaPagoCoDiReq.addProperty("Cod_NombrV", Utilerias.obtenerStringPropiedad(datosVendedor, "titular"));
		confirmaPagoCoDiReq.addProperty("NumTransac", Utilerias.obtenerStringPropiedad(folioTransaccionResultado, "Fol_Transa"));
		confirmaPagoCoDiReq.addProperty("FechaSis", fechaSis);
		
		datosResultado = this.codiServicio.confirmaPagoCoDi(confirmaPagoCoDiReq);
		String errCodigo = Utilerias.obtenerStringPropiedad(datosResultado, "Err_Codigo");
		
		if(!"000000".equals(errCodigo)) {
			bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");
			String errMensaj = Utilerias.obtenerStringPropiedad(datosResultado, "Err_Mensaj");
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		return Response.noContent().build();
	}
	
	
	@Path("/cuenta-principal")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerCuentaPrincipal(@HeaderParam("Authorization") String token) {
		LOGGER.info("CTRL: Comenzando obtenerCuentaPrincipal metodo");
		JsonObject usuarioPrincipal = Utilerias.obtenerPrincipal(token);
		JsonObject resultado = null;
		JsonObject cuentaDatosElemento = null;
		JsonObject usuarioRequest = null;
		JsonObject cuentaDatosRequest = null;
		
		JsonObject usuario = null;
		JsonObject cuentaDatos = null;
		
		String usuClave = Utilerias.obtenerStringPropiedad(usuarioPrincipal, "usuClave");
		String fechaSis = Utilerias.obtenerFechaSis();
		
		usuarioRequest = new JsonObject();
		usuarioRequest.addProperty("Usu_Clave", usuClave);
		usuarioRequest.addProperty("FechaSis",fechaSis);		
		usuario = this.usuarioServicio.usuarioConsultar(usuarioRequest);
		usuario = Utilerias.obtenerJsonObjectPropiedad(usuario, "usuario");
		
		cuentaDatosRequest = new JsonObject();
		cuentaDatosRequest.addProperty("Cue_Numero", Utilerias.obtenerStringPropiedad(usuario, "Usu_CuCaCo"));
		cuentaDatosRequest.addProperty("FechaSis", fechaSis);
		cuentaDatosRequest.addProperty("Cue_Client", "");
		cuentaDatos = this.cuentaServicio.cuentaDatosConsultar(cuentaDatosRequest);
		cuentaDatos = Utilerias.obtenerJsonObjectPropiedad(cuentaDatos, "cuenta");
		
		cuentaDatosElemento = new JsonObject();
		cuentaDatosElemento.addProperty("cueNumero", Utilerias.obtenerStringPropiedad(cuentaDatos, "Cue_Numero"));
		cuentaDatosElemento.addProperty("cueMoneda", Utilerias.obtenerStringPropiedad(cuentaDatos, "Cue_Moneda"));
		cuentaDatosElemento.addProperty("monDescri", Utilerias.obtenerStringPropiedad(cuentaDatos, "Mon_Descri"));
		cuentaDatosElemento.addProperty("monAbrevi", Utilerias.obtenerStringPropiedad(cuentaDatos, "Mon_Abrevi"));
		cuentaDatosElemento.addProperty("tipDescri", Utilerias.obtenerStringPropiedad(cuentaDatos, "Tip_Descri"));
		cuentaDatosElemento.addProperty("cueDispon", Utilerias.obtenerDoublePropiedad(cuentaDatos, "Cue_Dispon"));
		cuentaDatosElemento.addProperty("cueClabe", Utilerias.obtenerStringPropiedad(cuentaDatos, "Cue_Clabe"));
		cuentaDatosElemento.addProperty("cueTipo", Utilerias.obtenerStringPropiedad(cuentaDatos, "Cue_Tipo"));
		cuentaDatosElemento.addProperty("cueReteni", Utilerias.obtenerDoublePropiedad(cuentaDatos, "Cue_Reteni"));
		
		
		resultado = new JsonObject();
		resultado.add("cuentaDatos", cuentaDatosElemento);
		
		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@Path("/autoriza")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response autorizaCoDi(JsonObject datosAutoriza, @HeaderParam("Authorization") String token) {
		LOGGER.info("CTRL: Comenzando autorizaCoDi metodo");
		JsonObject usuarioPrincipal = Utilerias.obtenerPrincipal(token);
		JsonObject usuarioRequest = null;
		JsonObject resultadoObjeto = null;
		JsonObject autorizacion = null;
		String usuPasCif  = null;
		String usuClave = Utilerias.obtenerStringPropiedad(usuarioPrincipal, "usuClave");
		String fechaSis = Utilerias.obtenerFechaSis();
		String usuPasswo = Utilerias.obtenerStringPropiedad(datosAutoriza, "password");
		
		if(usuPasswo == null || usuPasswo.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.5");
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		usuarioRequest = new JsonObject();
		usuarioRequest.addProperty("Usu_Clave", usuClave);
		usuarioRequest.addProperty("Usu_Passwo", usuPasswo);
		usuarioRequest.addProperty("FechaSis",fechaSis);
		
		JsonObject usuario = this.usuarioServicio.usuarioConsultar(usuarioRequest);
		usuario = Utilerias.obtenerJsonObjectPropiedad(usuario, "usuario");
		
		if(usuario == null || usuario.isJsonPrimitive()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		String usuNumero = Utilerias.obtenerStringPropiedad(usuario, "Usu_Numero");
		String usuStatus = Utilerias.obtenerStringPropiedad(usuario, "Usu_Status");
		
		if(usuNumero == null || (usuStatus == null || usuStatus.isEmpty())) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.20");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.11");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("F")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.12");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String tokStatus = Utilerias.obtenerStringPropiedad(usuario, "Tok_Status");
		
		if(tokStatus == null || tokStatus.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(tokStatus.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.13");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		Integer usuCoAcNe = Utilerias.obtenerIntPropiedad(usuario, "Usu_CoAcNe"); 
		Integer usuCoDeNe = Utilerias.obtenerIntPropiedad(usuario, "Usu_CoDeNe");
		Integer usuCoPaNe = Utilerias.obtenerIntPropiedad(usuario, "Usu_CoPaNe");
		
		if(usuCoAcNe == null || usuCoDeNe == null || usuCoPaNe == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
			
		if((usuCoAcNe.intValue() > 3 || usuStatus.equals("B")) && usuCoDeNe.intValue() < 3 && usuCoPaNe < 1) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.14");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuCoDeNe.intValue() > 3 && usuStatus.equals("B")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.15");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuCoPaNe.intValue() > 1 && usuStatus.equals("B")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.15");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		String usuStaSes = Utilerias.obtenerStringPropiedad(usuario, "Usu_StaSes");

		if(usuStaSes == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.1");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(!usuStaSes.equals("I")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.16");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("D")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.17");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(usuStatus.equals("C")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.18");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		if(!usuStatus.equals("A")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.19");
			throw new ConflictException(bimMessageDTO.toString());
		}
		
		usuPasCif = Racal.cifraPassword_HSM(datosAutoriza.get("password").getAsString());
		
		if(usuPasCif.length() == 7 && usuPasCif.equals("autoriz")) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.7");
			throw new InternalServerException(bimMessageDTO.toString());
		}

		if(usuPasCif.isEmpty()) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.8");
			throw new InternalServerException(bimMessageDTO.toString());
		}
		
		if(!usuPasCif.equals(usuario.get("Usu_Passwo").getAsString().trim())) {			
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.20");
			throw new UnauthorizedException(bimMessageDTO.toString());
		}
		
		resultadoObjeto = new JsonObject();
		autorizacion = new JsonObject();
		autorizacion.addProperty("claveCifrado", usuPasCif);
		resultadoObjeto.add("autorizacion", autorizacion);
		
		LOGGER.info("CTRL: Terminando autorizaCoDi metodo");
		return Response.ok(resultadoObjeto.toString(), MediaType.APPLICATION_JSON).build();
		
	}
	
}
