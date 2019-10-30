package com.bim.msf4j.ctrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.dto.MessageProxyDTO;
import com.bim.commons.dto.RequestDTO;
import com.bim.commons.enums.InversionesCategoriasEnum;
import com.bim.commons.exceptions.BadRequestException;
import com.bim.commons.exceptions.ConflictException;
import com.bim.commons.exceptions.ForbiddenException;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.utils.Filtrado;
import com.bim.commons.utils.HttpClientUtils;
import com.bim.commons.service.TokenService;
import com.bim.commons.utils.Utilerias;
import com.bim.msf4j.exceptions.BimExceptionMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.log4j.Logger;
import org.wso2.msf4j.Request;
import org.wso2.msf4j.internal.MicroservicesRegistryImpl;

@Path("/inversiones")
public class InversionesCtrl extends BimBaseCtrl {
	
	private static final Logger logger = Logger.getLogger(InversionesCtrl.class);
	
	private static String DataServiceHost;
	
	private static String TransaccionServicio;
	private static String BitacoraServicio;
	private static String InversionesServicio;
	private static String ClienteServicio;
	private static String UsuarioServicio;
	private static String ReinversionServicio;
	private static String TasaServicio;
	private static String ConfiguracionServicio;

	private static String FolioTransaccionGenerarOp;
	private static String BitacoraCreacionOp;
	private static String InversionesObtenerOp;
	private static String InversionesPagareNumeroUsuarioObtenerOp;
	private static String ClienteConsultarOp;
	private static String UsuarioPerfilRiesgoConsultarOp;
	private static String fechaHabilConsultarOp;
	private static String tasaClienteConsultarOp;
	private static String tasaMonedaConsultarOp;
	private static String tasaGATConsultaCalcularOp;
	private static String tasaGATRealConsultaCalcularOp;
	private static String inversionesStatusActualizarOp;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOp;
	private static String HorarioInversionOp;
	private static String InformacionSucursalObtenerOp;
	private static String inversionesProcesoLiquidacionGenerarOp;
	private static String inversionesContraEstadoCuentaActualizarOp;

	private static String FolioTransaccionGenerarOpSucOrigen;
	private static String BitacoraCreacionOpBitTipOpe;
	private static String BitacoraCreacionOpTransaccio;
	private static String BitacoraCreacionOpUsuario;
	private static String BitacoraCreacionOpSucOrigen;
	private static String BitacoraCreacionOpSucDestino;
	private static String BitacoraCreacionOpModulo;
	private static String BitacoraCreacionOpBitMonto;
	private static String InversionesObtenerOpInvMoneda;
	private static String InversionesObtenerOpTransaccio;
	private static String InversionesObtenerOpUsuario;
	private static String InversionesObtenerOpSucOrigen;
	private static String InversionesObtenerOpSucDestino;
	private static String InversionesObtenerOpModulo;
	private static String InversionesPagareNumeroUsuarioObtenerOpTipConsul;
	private static String InversionesPagareNumeroUsuarioObtenerOpTransaccio;
	private static String InversionesPagareNumeroUsuarioObtenerOpUsuario;
	private static String InversionesPagareNumeroUsuarioObtenerOpSucOrigen;
	private static String InversionesPagareNumeroUsuarioObtenerOpSucDestino;
	private static String InversionesPagareNumeroUsuarioObtenerOpModulo;
	private static String ClienteConsultarOpTipConsul;
	private static String ClienteConsultarOpTransaccio;
	private static String ClienteConsultarOpUsuari;
	private static String ClienteConsultarOpSucOrigen;
	private static String ClienteConsultarOpSucDestino;
	private static String ClienteConsultarOpModulo;	
	private static String UsuarioPerfilRiesgoConsultarOpTipConsul;
	private static String UsuarioPerfilRiesgoConsultarOpTransaccio;
	private static String UsuarioPerfilRiesgoConsultarOpUsuari;
	private static String UsuarioPerfilRiesgoConsultarOpSucOrigen;
	private static String UsuarioPerfilRiesgoConsultarOpSucDestino;
	private static String UsuarioPerfilRiesgoConsultarOpModulo;	
	private static String UsuarioPerfilRiesgoConsultarOpAplClient;	
	private static String fechaHabilConsultarOpFinSem;
	private static String fechaHabilConsultarOpTransaccio;
	private static String fechaHabilConsultarOpUsuari;
	private static String fechaHabilConsultarOpSucOrigen;
	private static String fechaHabilConsultarOpSucDestino;
	private static String fechaHabilConsultarOpModulo;
	private static String tasaClienteConsultarOpTasa;
	private static String tasaClienteConsultarOpTransaccio;
	private static String tasaClienteConsultarOpUsuari;
	private static String tasaClienteConsultarOpSucOrigen;
	private static String tasaClienteConsultarOpSucDestino;
	private static String tasaClienteConsultarOpModulo;	
	private static String tasaClienteConsultarOpInvMoneda;
	private static String tasaMonedaConsultarOpTransaccio;
	private static String tasaMonedaConsultarOpUsuari;
	private static String tasaMonedaConsultarOpSucOrigen;
	private static String tasaMonedaConsultarOpSucDestino;
	private static String tasaMonedaConsultarOpModulo;
	private static String tasaMonedaConsultarOpMonNumero;
	private static String tasaGATConsultaCalcularOpMonComisi;
	private static String tasaGATConsultaCalcularOpTransaccio;
	private static String tasaGATConsultaCalcularOpUsuari;
	private static String tasaGATConsultaCalcularOpSucOrigen;
	private static String tasaGATConsultaCalcularOpSucDestino;
	private static String tasaGATConsultaCalcularOpModulo;	
	private static String tasaGATConsultaCalcualrOpInvGAT;
	private static String tasaGATReaConsultaCalcularOpTransaccio;
	private static String tasaGATReaConsultaCalcularOpUsuari;
	private static String tasaGATReaConsultaCalcularOpSucOrigen;
	private static String tasaGATReaConsultaCalcularOpSucDestino;
	private static String tasaGATReaConsultaCalcularOpModulo;	
	private static String tasaGATReaConsultaCalcualrOpInvGATRea;
	private static String inversionesStatusActualizarOpAdiMoReGr;
	private static String inversionesStatusActualizarOpTransaccio;
	private static String inversionesStatusActualizarOpUsuari;
	private static String inversionesStatusActualizarOpSucOrigen;
	private static String inversionesStatusActualizarOpSucDestino;
	private static String inversionesStatusActualizarOpModulo;
	private static String inversionesStatusActualizarOpAdiInsLiq;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpTransaccio;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpUsuari;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpSucOrigen;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpSucDestino;
	private static String inversionesImportesDeInvercionFinalizadaActualizarOpModulo;
	private static String HorarioInversionOpTipConsul;
	private static String HorarioInversionOpTipTransf;
	private static String HorarioInversionOpTransaccio;
	private static String HorarioInversionOpUsuario;
	private static String HorarioInversionOpSucOrigen;
	private static String HorarioInversionOpSucDestino;
	private static String HorarioInversionOpModulo;
	private static String informacionSucursalObtenerOpTransaccio;
	private static String informacionSucursalObtenerOpUsuari;
	private static String informacionSucursalObtenerOpSucOrigen;
	private static String informacionSucursalObtenerOpSucDestino;
	private static String informacionSucursalObtenerOpModulo;
	private static String inversionesProcesoLiquidacionGenerarOpInvMonRef;
	private static String inversionesProcesoLiquidacionGenerarOpTransaccio;
	private static String inversionesProcesoLiquidacionGenerarOpUsuari;
	private static String inversionesProcesoLiquidacionGenerarOpSucOrigen;
	private static String inversionesProcesoLiquidacionGenerarOpSucDestino;
	private static String inversionesProcesoLiquidacionGenerarOpModulo;
	private static String inversionesContraEstadoCuentaActualizarOpCorMoLiDi;
	private static String inversionesContraEstadoCuentaActualizarOpTipActual;
	private static String inversionesContraEstadoCuentaActualizarOpTransaccio;
	private static String inversionesContraEstadoCuentaActualizarOpUsuari;
	private static String inversionesContraEstadoCuentaActualizarOpSucOrigen;
	private static String inversionesContraEstadoCuentaActualizarOpSucDestino;
	private static String inversionesContraEstadoCuentaActualizarOpModulo;

	private static String InversionesFilterBy;
	private static Integer InversionesMaximoPagina;
	
	public InversionesCtrl() {
		super();
		logger.info("Ctrl: Empezando metodo init...");
		
		FolioTransaccionGenerarOpSucOrigen = properties.getProperty("op.folio_transaccion_generar.suc_origen");
		
		BitacoraCreacionOpBitTipOpe = properties.getProperty("op.bitacora_creacion.bit_tipope.inversiones_listado");
		BitacoraCreacionOpTransaccio = properties.getProperty("op.bitacora_creacion.transaccio");
		BitacoraCreacionOpUsuario = properties.getProperty("op.bitacora_creacion.usuario");
		BitacoraCreacionOpSucOrigen = properties.getProperty("op.bitacora_creacion.suc_origen");
		BitacoraCreacionOpSucDestino= properties.getProperty("op.bitacora_creacion.suc_destino");
		BitacoraCreacionOpModulo = properties.getProperty("op.bitacora_creacion.modulo");
		BitacoraCreacionOpBitMonto = properties.getProperty("op.bitacora_creacion.bit_monto");

		InversionesObtenerOpInvMoneda = properties.getProperty("op.inversiones_obtener.inv_moneda");
		InversionesObtenerOpTransaccio = properties.getProperty("op.inversiones_obtener.transaccio");
		InversionesObtenerOpUsuario = properties.getProperty("op.inversiones_obtener.usuario");
		InversionesObtenerOpSucOrigen = properties.getProperty("op.inversiones_obtener.suc_origen");
		InversionesObtenerOpSucDestino = properties.getProperty("op.inversiones_obtener.suc_destino");
		InversionesObtenerOpModulo = properties.getProperty("op.inversiones_obtener.modulo");

		InversionesPagareNumeroUsuarioObtenerOpTipConsul = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.tip_consul");
		InversionesPagareNumeroUsuarioObtenerOpTransaccio = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.transaccio");
		InversionesPagareNumeroUsuarioObtenerOpUsuario = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.usuario");
		InversionesPagareNumeroUsuarioObtenerOpSucOrigen = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_origen");
		InversionesPagareNumeroUsuarioObtenerOpSucDestino = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.suc_destino");
		InversionesPagareNumeroUsuarioObtenerOpModulo = properties.getProperty("op.inversiones_pagare_numero_usuario_obtener.modulo");

		ClienteConsultarOpTipConsul = properties.getProperty("op.tasa_cliente_consultar.tip_consul");
		ClienteConsultarOpTransaccio  = properties.getProperty("op.tasa_cliente_consultar.transaccio");
		ClienteConsultarOpUsuari = properties.getProperty("op.tasa_cliente_consultar.usuario");
		ClienteConsultarOpSucOrigen = properties.getProperty("op.tasa_cliente_consultar.suc_origen");
		ClienteConsultarOpSucDestino = properties.getProperty("op.tasa_cliente_consultar.suc_destino");
		ClienteConsultarOpModulo = properties.getProperty("op.tasa_cliente_consultar.modulo");

		UsuarioPerfilRiesgoConsultarOpAplClient = properties.getProperty("op.usuario_perfil_riesgo_consultar.apl_cuesti");
		UsuarioPerfilRiesgoConsultarOpTipConsul = properties.getProperty("op.usuario_perfil_riesgo_consultar.tip_consul");
		UsuarioPerfilRiesgoConsultarOpTransaccio  = properties.getProperty("op.usuario_perfil_riesgo_consultar.transaccio");
		UsuarioPerfilRiesgoConsultarOpUsuari = properties.getProperty("op.usuario_perfil_riesgo_consultar.usuario");
		UsuarioPerfilRiesgoConsultarOpSucOrigen = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_origen");
		UsuarioPerfilRiesgoConsultarOpSucDestino = properties.getProperty("op.usuario_perfil_riesgo_consultar.suc_destino");
		UsuarioPerfilRiesgoConsultarOpModulo = properties.getProperty("op.usuario_perfil_riesgo_consultar.modulo");

		fechaHabilConsultarOpFinSem  = properties.getProperty("op.fecha_habil_consultar.fin_sem");
		fechaHabilConsultarOpTransaccio  = properties.getProperty("op.fecha_habil_consultar.transaccio");
		fechaHabilConsultarOpUsuari = properties.getProperty("op.fecha_habil_consultar.usuario");
		fechaHabilConsultarOpSucOrigen = properties.getProperty("op.fecha_habil_consultar.suc_origen");
		fechaHabilConsultarOpSucDestino = properties.getProperty("op.fecha_habil_consultar.suc_destino");
		fechaHabilConsultarOpModulo = properties.getProperty("op.fecha_habil_consultar.modulo");
		
		tasaClienteConsultarOpTasa  = properties.getProperty("op.tasa_cliente_consultar.tasa");
		tasaClienteConsultarOpTransaccio  = properties.getProperty("op.tasa_cliente_consultar.transaccio");
		tasaClienteConsultarOpUsuari = properties.getProperty("op.tasa_cliente_consultar.usuario");
		tasaClienteConsultarOpSucOrigen = properties.getProperty("op.tasa_cliente_consultar.suc_origen");
		tasaClienteConsultarOpSucDestino = properties.getProperty("op.tasa_cliente_consultar.suc_destino");
		tasaClienteConsultarOpModulo = properties.getProperty("op.tasa_cliente_consultar.modulo");		
		tasaClienteConsultarOpInvMoneda = properties.getProperty("op.tasa_cliente_consultar.inv_moneda");
		
		tasaMonedaConsultarOpTransaccio  = properties.getProperty("op.tasa_moneda_consultar.transaccio");
		tasaMonedaConsultarOpUsuari = properties.getProperty("op.tasa_moneda_consultar.usuario");
		tasaMonedaConsultarOpSucOrigen = properties.getProperty("op.tasa_moneda_consultar.suc_origen");
		tasaMonedaConsultarOpSucDestino = properties.getProperty("op.tasa_moneda_consultar.suc_destino");
		tasaMonedaConsultarOpModulo = properties.getProperty("op.tasa_moneda_consultar.modulo");		
		tasaMonedaConsultarOpMonNumero = properties.getProperty("op.tasa_moneda_consultar.mon_numero");
		
		tasaGATConsultaCalcularOpMonComisi  = properties.getProperty("op.tasa_gat_consulta_calcular.mon_comisi");
		tasaGATConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_consulta_calcular.transaccio");
		tasaGATConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_consulta_calcular.usuario");
		tasaGATConsultaCalcularOpSucOrigen = properties.getProperty("op.tasa_gat_consulta_calcular.suc_origen");
		tasaGATConsultaCalcularOpSucDestino = properties.getProperty("op.tasa_gat_consulta_calcular.suc_destino");
		tasaGATConsultaCalcularOpModulo = properties.getProperty("op.tasa_gat_consulta_calcular.modulo");	
		tasaGATConsultaCalcualrOpInvGAT = properties.getProperty("op.tasa_gat_consulta_calcular.inv_gat");
		
		tasaGATReaConsultaCalcularOpTransaccio  = properties.getProperty("op.tasa_gat_rea_consulta_calcular.transaccio");
		tasaGATReaConsultaCalcularOpUsuari = properties.getProperty("op.tasa_gat_rea_consulta_calcular.usuario");
		tasaGATReaConsultaCalcularOpSucOrigen = properties.getProperty("op.tasa_gat_rea_consulta_calcular.suc_origen");
		tasaGATReaConsultaCalcularOpSucDestino = properties.getProperty("op.tasa_gat_rea_consulta_calcular.suc_destino");
		tasaGATReaConsultaCalcularOpModulo = properties.getProperty("op.tasa_gat_rea_consulta_calcular.modulo");	
		tasaGATReaConsultaCalcualrOpInvGATRea = properties.getProperty("op.tasa_gat_rea_consulta_calcular.inv_gat_rea");
		
		inversionesStatusActualizarOpAdiMoReGr = properties.getProperty("op.inversiones_status_actualizar.adi_moregr");
		inversionesStatusActualizarOpTransaccio = properties.getProperty("op.inversiones_status_actualizar.transaccio");
		inversionesStatusActualizarOpUsuari = properties.getProperty("op.tasa_gat_rea_consulta_calcular.usuario");
		inversionesStatusActualizarOpSucOrigen = properties.getProperty("op.inversiones_status_actualizar.suc_origen");
		inversionesStatusActualizarOpSucDestino = properties.getProperty("op.inversiones_status_actualizar.suc_destino");
		inversionesStatusActualizarOpModulo = properties.getProperty("op.inversiones_status_actualizar.modulo");
		inversionesStatusActualizarOpAdiInsLiq = properties.getProperty("op.inversiones_status_actualizar.adi_insLiq");

		inversionesImportesDeInvercionFinalizadaActualizarOpTransaccio  = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.transaccio");
		inversionesImportesDeInvercionFinalizadaActualizarOpUsuari = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.usuario");
		inversionesImportesDeInvercionFinalizadaActualizarOpSucOrigen = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.suc_origen");
		inversionesImportesDeInvercionFinalizadaActualizarOpSucDestino = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.suc_destino");
		inversionesImportesDeInvercionFinalizadaActualizarOpModulo = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.modulo");

		
		informacionSucursalObtenerOpTransaccio  = properties.getProperty("op.informacion_sucursal_obtener.transaccio");
		informacionSucursalObtenerOpUsuari = properties.getProperty("op.informacion_sucursal_obtener.usuario");
		informacionSucursalObtenerOpSucOrigen = properties.getProperty("op.informacion_sucursal_obtener.suc_origen");
		informacionSucursalObtenerOpSucDestino = properties.getProperty("op.informacion_sucursal_obtener.suc_destino");
		informacionSucursalObtenerOpModulo = properties.getProperty("op.informacion_sucursal_obtener.modulo");

		HorarioInversionOpTipConsul = properties.getProperty("op.horario_inversion.tip_consul");
		HorarioInversionOpTipTransf = properties.getProperty("op.horario_inversion.tip_transf");
		HorarioInversionOpTransaccio = properties.getProperty("op.horario_inversion.transaccio");
		HorarioInversionOpUsuario = properties.getProperty("op.horario_inversion.usuario");
		HorarioInversionOpSucOrigen = properties.getProperty("op.horario_inversion.suc_origen");
		HorarioInversionOpSucDestino = properties.getProperty("op.horario_inversion.suc_destino");
		HorarioInversionOpModulo = properties.getProperty("op.horario_inversion.modulo");
		
		inversionesProcesoLiquidacionGenerarOpInvMonRef  = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.inv_monref");
		inversionesProcesoLiquidacionGenerarOpTransaccio  = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.transaccio");
		inversionesProcesoLiquidacionGenerarOpUsuari = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.usuario");
		inversionesProcesoLiquidacionGenerarOpSucOrigen = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.suc_origen");
		inversionesProcesoLiquidacionGenerarOpSucDestino = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.suc_destino");
		inversionesProcesoLiquidacionGenerarOpModulo = properties.getProperty("op.inversiones_importes_de_invercion_finalizada_actualizar.modulo");
		
		inversionesContraEstadoCuentaActualizarOpCorMoLiDi  = properties.getProperty("op.inversiones_proceso_liquidacion_generar.cor_molidi");
		inversionesContraEstadoCuentaActualizarOpTipActual  = properties.getProperty("op.inversiones_proceso_liquidacion_generar.tip_actual");
		inversionesContraEstadoCuentaActualizarOpTransaccio  = properties.getProperty("op.inversiones_proceso_liquidacion_generar.transaccio");
		inversionesContraEstadoCuentaActualizarOpUsuari = properties.getProperty("op.inversiones_proceso_liquidacion_generar.usuario");
		inversionesContraEstadoCuentaActualizarOpSucOrigen = properties.getProperty("op.inversiones_proceso_liquidacion_generar.suc_origen");
		inversionesContraEstadoCuentaActualizarOpSucDestino = properties.getProperty("op.inversiones_proceso_liquidacion_generar.suc_destino");
		inversionesContraEstadoCuentaActualizarOpModulo = properties.getProperty("op.inversiones_proceso_liquidacion_generar.modulo");
		
		DataServiceHost = properties.getProperty("data_service.host");
		
		logger.info("DataServiceHost" + DataServiceHost);
		
		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		InversionesServicio = properties.getProperty("data_service.inversiones_servicio");
		ClienteServicio= properties.getProperty("data_service.cliente_servicio");
		UsuarioServicio= properties.getProperty("data_service.usuario_servicio");
		ReinversionServicio= properties.getProperty("data_service.reinversion_servicio");
		TasaServicio= properties.getProperty("data_service.tasa_servicio");
		TokenServicio= properties.getProperty("data_service.token_servicio");
		ConfiguracionServicio = properties.getProperty("data_service.configuracion_servicio");
		
		FolioTransaccionGenerarOp = properties.getProperty("transaccion_servicio.op.folio_transaccion_generar");
		BitacoraCreacionOp = properties.getProperty("bitacora_servicio.op.bitacora_creacion");
		InversionesObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_obtener");
		InversionesPagareNumeroUsuarioObtenerOp = properties.getProperty("inversiones_servicio.op.inversiones_pagare_numero_usuario_obtener");
		ClienteConsultarOp = properties.getProperty("cliente_servicio.op.cliente_consultar");
		UsuarioPerfilRiesgoConsultarOp = properties.getProperty("usuario_servicio.op.usuario_perfil_riesgo_consultar");
		fechaHabilConsultarOp = properties.getProperty("reinversion_servicio.op.fecha_habil_consultar");
		tasaClienteConsultarOp = properties.getProperty("tasa_servicio.op.tasa_cliente_consultar");
		tasaMonedaConsultarOp = properties.getProperty("tasa_servicio.op.tasa_moneda_consultar");
		tasaGATConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_consulta_calcular");
		tasaGATRealConsultaCalcularOp = properties.getProperty("tasa_servicio.op.tasa_gat_rea_consulta_calcular");
		inversionesStatusActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_status_actualizar");
		inversionesImportesDeInvercionFinalizadaActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_importes_de_invercion_finalizada_actualizar");
		HorarioInversionOp = properties.getProperty("configuracion_servicio.op.horario_inversion");
		InformacionSucursalObtenerOp = properties.getProperty("configuracion_servicio.op.informacion_sucursal_obtener");
		inversionesProcesoLiquidacionGenerarOp = properties.getProperty("inversiones_servicio.op.inversiones_proceso_liquidacion_generar");
		inversionesContraEstadoCuentaActualizarOp = properties.getProperty("inversiones_servicio.op.inversiones_contra_estado_cuenta_actualizar");

		
		InversionesFilterBy = properties.getProperty("inversiones_servicio.filter_by");
		InversionesMaximoPagina = Integer.parseInt(properties.getProperty("inversiones_servicio.maximo_pagina"));
		
		logger.info("Ctrl: Terminando metodo init...");
	}
	
	@Path("/")
	@GET()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inversionesListado(@QueryParam("page") String page, @QueryParam("per_page") String perPage, @QueryParam("filter_by") String filterBy, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando inversionesListado metodo");
		
		if(page == null || perPage == null) 
			throw new BadRequestException("BIM.MENSAJ.2");
		
		if(!Utilerias.isNumber(page)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.9");
			bimMessageDTO.addMergeVariable("page", page);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(!Utilerias.isNumber(perPage)) {
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

		if(filterBy != null && !filterBy.equals(InversionesFilterBy))
			throw new BadRequestException("BIM.MENSAJ.6");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaSis = simpleDateFormat.format(fecha);
		
		JsonObject datosTransaccion = new JsonObject();
		datosTransaccion.addProperty("Num_Transa", "");
		datosTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		logger.info("datosTransaccion" + datosTransaccion);
		StringBuilder folioTransaccionGenerarUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(TransaccionServicio)
				.append("/")
				.append(FolioTransaccionGenerarOp);
		JsonObject folioTransaccionGenerarOp = new JsonObject();
		folioTransaccionGenerarOp.add("folioTransaccionGenerarOp", datosTransaccion);
		logger.info("folioTransaccionGenerarOp" + folioTransaccionGenerarOp);

		RequestDTO folioTransaccionGenerarOpSolicitud = new RequestDTO();
		folioTransaccionGenerarOpSolicitud.setUrl(folioTransaccionGenerarUrl.toString());
		MessageProxyDTO folioTransaccionGenerarOpMensaje= new MessageProxyDTO();
		folioTransaccionGenerarOpMensaje.setBody(folioTransaccionGenerarOp.toString());
		folioTransaccionGenerarOpSolicitud.setMessage(folioTransaccionGenerarOpMensaje);

		String folioTransaccionGenerarOpResultado = HttpClientUtils.postPerform(folioTransaccionGenerarOpSolicitud);
		JsonObject folioTransaccionGenerarOpResultadoObjeto = new Gson().fromJson(folioTransaccionGenerarOpResultado, JsonObject.class);
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String numTransac = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bit_DireIP = solicitud.getHeader("User-Agent") == null ? solicitud.getHeader("User-Agent") : "";
		String bit_PriRef = solicitud.getHeader("X-Forwarded-For") == null ? solicitud.getHeader("X-Forwarded-For") : "";
		
		/* 
			Parametros obtenidos por medio del principal 
				Bit_Usuari = usuNumero
				Inv_Client = usuClient
				Inv_Usuari = usuNumero
		*/
		
		String usuNumero = "001844";
		String usuClient = "00193500";
		
		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", 0);
		datosBitacora.addProperty("Bit_PriRef", bit_PriRef);
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", bit_DireIP);
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);

		StringBuilder bitacoraCreacionOpUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(BitacoraServicio)
				.append("/")
				.append(BitacoraCreacionOp);
		JsonObject bitacoraCreacionOp = new JsonObject();
		bitacoraCreacionOp.add("bitacoraCreacionOp", datosBitacora);
		logger.info("bitacoraCreacionOp" + bitacoraCreacionOp);

		RequestDTO bitacoraCreacionSolicitud = new RequestDTO();
		bitacoraCreacionSolicitud.setUrl(bitacoraCreacionOpUrl.toString());
		MessageProxyDTO bitacoraCreacionOpMensaje= new MessageProxyDTO();
		bitacoraCreacionOpMensaje.setBody(bitacoraCreacionOp.toString());
		bitacoraCreacionSolicitud.setMessage(bitacoraCreacionOpMensaje);
		HttpClientUtils.postPerform(bitacoraCreacionSolicitud);

		JsonObject inversionesObtener = new JsonObject();
		inversionesObtener.addProperty("Inv_Client", usuClient);
		inversionesObtener.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
		inversionesObtener.addProperty("NumTransac", "");
		inversionesObtener.addProperty("Transaccio", InversionesObtenerOpTransaccio);
		inversionesObtener.addProperty("Usuario", InversionesObtenerOpUsuario);
		inversionesObtener.addProperty("FechaSis", fechaSis);
		inversionesObtener.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
		inversionesObtener.addProperty("SucDestino", InversionesObtenerOpSucDestino);
		inversionesObtener.addProperty("Modulo", InversionesObtenerOpModulo);
				
		StringBuilder inversionesObtenerUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/")
				.append(InversionesObtenerOp);
		JsonObject inversionesObtenerOp = new JsonObject();
		inversionesObtenerOp.add("inversionesObtenerOp", inversionesObtener);
		logger.info("inversionesObtenerOp" + inversionesObtenerOp);

		RequestDTO inversionesObtenerSolicitud = new RequestDTO();
		inversionesObtenerSolicitud.setUrl(inversionesObtenerUrl.toString());
		MessageProxyDTO inversionesObtenerMensaje = new MessageProxyDTO();
		inversionesObtenerMensaje.setBody(inversionesObtenerOp.toString());
		inversionesObtenerSolicitud.setMessage(inversionesObtenerMensaje);
		logger.info("inversionesObtenerSolicitud " + inversionesObtenerSolicitud.toString());
		String inversionesObtenerOpResultado = HttpClientUtils.postPerform(inversionesObtenerSolicitud);
		JsonObject inversionesObtenerOpResultadoObjeto = new Gson().fromJson(inversionesObtenerOpResultado, JsonObject.class);
		logger.info("inversionesObtenerOpResultadoObjeto" + inversionesObtenerOpResultadoObjeto);
		
		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
			inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Numero", "");
			inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
			inversionesPagareNumeroUsuarioObtener.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
			inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
			inversionesPagareNumeroUsuarioObtener.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
			inversionesPagareNumeroUsuarioObtener.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);
			inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
			inversionesPagareNumeroUsuarioObtener.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
			inversionesPagareNumeroUsuarioObtener.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
			inversionesPagareNumeroUsuarioObtener.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
	
		StringBuilder inversionesPagareNumeroUsuarioObtenerUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(InversionesServicio)
				.append("/")
				.append(InversionesPagareNumeroUsuarioObtenerOp);
		JsonObject inversionesPagareNumeroUsuarioObtenerOp = new JsonObject();
		inversionesPagareNumeroUsuarioObtenerOp.add("inversionesPagareNumeroUsuarioObtenerOp", inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOp" + inversionesPagareNumeroUsuarioObtenerOp);

		RequestDTO inversionesPagareNumeroUsuarioObtenerSolicitud = new RequestDTO();
		inversionesPagareNumeroUsuarioObtenerSolicitud.setUrl(inversionesPagareNumeroUsuarioObtenerUrl.toString());
		MessageProxyDTO inversionesPagareNumeroUsuarioObtenerMensaje = new MessageProxyDTO();
		inversionesPagareNumeroUsuarioObtenerMensaje.setBody(inversionesPagareNumeroUsuarioObtenerOp.toString());
		inversionesPagareNumeroUsuarioObtenerSolicitud.setMessage(inversionesPagareNumeroUsuarioObtenerMensaje);
		
		String inversionesPagareNumeroUsuarioObtenerOpResultado = HttpClientUtils.postPerform(inversionesPagareNumeroUsuarioObtenerSolicitud);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = new Gson().fromJson(inversionesPagareNumeroUsuarioObtenerOpResultado, JsonObject.class);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);
		
		JsonArray inversionesResultado = new JsonArray();
		if(inversionesPagareNumeroUsuarioObtenerOp.has("inversiones") && inversionesPagareNumeroUsuarioObtenerOp.get("inversiones").getAsJsonObject().has("inversion")) {
			inversionesResultado.addAll(inversionesPagareNumeroUsuarioObtenerOp.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());
		}

		inversionesResultado.addAll(inversionesObtenerOpResultadoObjeto.get("inversiones").getAsJsonObject().get("inversion").getAsJsonArray());

		JsonObject datosHorario = new JsonObject();
		datosHorario.addProperty("Tip_Consul", HorarioInversionOpTipConsul);
		datosHorario.addProperty("Tip_Transf", HorarioInversionOpTipTransf);
		datosHorario.addProperty("Err_Codigo", "");
		datosHorario.addProperty("Msj_Error", "");
		datosHorario.addProperty("NumTransac", numTransac);
		datosHorario.addProperty("Transaccio", HorarioInversionOpTransaccio);
		datosHorario.addProperty("Usuario", HorarioInversionOpUsuario);
		datosHorario.addProperty("FechaSis", fechaSis);
		datosHorario.addProperty("SucOrigen", HorarioInversionOpSucOrigen);
		datosHorario.addProperty("SucDestino", HorarioInversionOpSucDestino);
		datosHorario.addProperty("Modulo", HorarioInversionOpModulo);

		logger.info("datosHorario" + datosHorario);
		StringBuilder horarioInversionUrl = new StringBuilder()
				.append(DataServiceHost)
				.append("/")
				.append(ConfiguracionServicio)
				.append("/")
				.append(HorarioInversionOp);

		JsonObject horarioInversionOp = new JsonObject();
		horarioInversionOp.add("horarioInversionOp", datosHorario);
		logger.info("horarioInversionOp" + horarioInversionOp);

		RequestDTO horarioInversionOpSolicitud = new RequestDTO();
		horarioInversionOpSolicitud.setUrl(horarioInversionUrl.toString());
		MessageProxyDTO horarioInversionOpMensaje = new MessageProxyDTO();
		horarioInversionOpMensaje.setBody(horarioInversionOp.toString());
		horarioInversionOpSolicitud.setMessage(horarioInversionOpMensaje);

		String horarioInversionOpResultado = HttpClientUtils.postPerform(horarioInversionOpSolicitud);
		JsonObject horarioInversionOpResultadoObjecto = new Gson().fromJson(horarioInversionOpResultado, JsonObject.class);
		logger.info("horarioInversionOpResultadoObjecto" + horarioInversionOpResultadoObjecto);

		JsonObject horariosObjecto = horarioInversionOpResultadoObjecto.get("horariosInversion").getAsJsonObject();
		JsonArray horariosArreglo = horariosObjecto.has("horarioInversion") ? horariosObjecto.get("horarioInversion").getAsJsonArray() : new JsonArray();
		
		inversionesResultado.forEach(inversionResultado -> {
			JsonObject inversionResultadoObjeto = (JsonObject) inversionResultado;
			String invCantid = inversionResultadoObjeto.has("Inv_Cantid") ? inversionResultadoObjeto.get("Inv_Cantid").getAsString() : "";
			String invFecVen = inversionResultadoObjeto.has("Inv_FecVen") ? inversionResultadoObjeto.get("Inv_FecVen").getAsString() : "";

			String horHorIni = null;
			String horHorFin = null;

			for (JsonElement horElemento : horariosArreglo) {
				JsonObject horarioObj = horElemento.getAsJsonObject();
				if("IN".equals(horarioObj.get("Hor_TipMod").getAsString())) {
					horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
					horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
				} else if("CE".equals(horarioObj.get("Hor_TipMod").getAsString())) {
					horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
					horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
				}
			}

			Date fechaVen = null;
			Date horIni = null;
			Date horFin = null;

			try {
				SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
				fechaVen = sdfFecha.parse(invFecVen);
			} catch (Exception e) {
				try {
					SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
					fechaVen = sdfFecha.parse(invFecVen);
				} catch (ParseException ex) {
					logger.info("formato sdfFecha no valido.");
				}
			}

			try {
				SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				horIni = sdfHora.parse(horHorIni);
			} catch (Exception e) {
				try {
					SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					horIni = sdfHora.parse(horHorIni);
				} catch (Exception ex) {
					logger.info("error al formatear HorIni.");
				}
			}

			try {
				SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				horFin = sdfHora.parse(horHorFin);
			} catch (Exception e) {
				try {
					SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					horFin = sdfHora.parse(horHorFin);
				} catch (Exception ex) {
					logger.info("error al formatear HorFin.");
				}
			}

			Boolean cpRenInv = Utilerias.calcularVencimiento(fechaVen, horIni, horFin);		
			Double invCantidRedondeado = Utilerias.redondear(Double.parseDouble(invCantid), 3);
			inversionResultadoObjeto.addProperty("Inv_Cantid", invCantidRedondeado.toString());
			inversionResultadoObjeto.addProperty("cpRenInv", cpRenInv);
		});
		logger.info("inversionesResultado " +  inversionesResultado);
		JsonObject inversionesResultadoFinal = Filtrado.filtroInversiones(inversionesResultado, pageValue, perPageValue, filterBy);
		
		logger.info("CTRL: Terminando login metodo");	
		return Response.ok(inversionesResultadoFinal.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("{invNumero}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response detalleInversion(@PathParam("invNumero") String invNumero,
			@QueryParam("categoria") String categoria, @Context final Request solicitud) {
		logger.info("CTRL: Empezando detalleInversion Method...");

		String bearerToken = solicitud.getHeader("Authorization");
		JsonObject principalResultadoObjecto = Utilerias.getPrincipal(bearerToken);

		if(!Utilerias.isNumber(invNumero)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.23");
			bimMessageDTO.addMergeVariable("invNumero", invNumero);
			throw new BadRequestException(bimMessageDTO.toString());
		}
		
		if(InversionesCategoriasEnum.validarCategoria(categoria) == null) {
				BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.24");
				bimMessageDTO.addMergeVariable("categoria", categoria);
				throw new BadRequestException(bimMessageDTO.toString());
		}

		JsonObject datosTransaccion = new JsonObject();
		datosTransaccion.addProperty("Num_Transa", "");
		datosTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);

		logger.info("datosTransaccion" + datosTransaccion);
		JsonObject folioTransaccionGenerarOpResultadoObjeto = Utilerias.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, datosTransaccion);
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		String FolioTransaccionGenerarOpFolTransa = folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject().get("Fol_Transa").getAsString();

		String fechaSis = Utilerias.getFechaSis();

		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));
		String bitPriRef = solicitud.getHeader("User-Agent");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		
		String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		String usuClient = principalResultadoObjecto.get("usuClient").getAsString();

		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", Integer.parseInt(BitacoraCreacionOpBitMonto));
		datosBitacora.addProperty("Bit_PriRef", bitPriRef != null ? bitPriRef : "");
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", bitDireIP != null ? bitDireIP : "");
		datosBitacora.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);

		logger.info("datosBitacora" + datosBitacora);
		JsonObject bitacoraCreacionOpResultadoObjeto = Utilerias.performOperacion(BitacoraServicio, BitacoraCreacionOp, datosBitacora);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		
		JsonObject datosInversion = new JsonObject();
		datosInversion.addProperty("FechaSis", fechaSis);

		String inversionesConsultarOp;
		if (categoria.equals(InversionesCategoriasEnum.PAGARE.toString())) {
			datosInversion.addProperty("Inv_Numero", "");
			datosInversion.addProperty("Inv_Usuari", usuNumero);
			datosInversion.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
			datosInversion.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
			datosInversion.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
			datosInversion.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);			
			datosInversion.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
			datosInversion.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);

			inversionesConsultarOp = InversionesPagareNumeroUsuarioObtenerOp;
		} else {
			datosInversion.addProperty("Inv_Client", usuClient);
			datosInversion.addProperty("Inv_Moneda", InversionesObtenerOpInvMoneda);
			datosInversion.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
			datosInversion.addProperty("Transaccio", InversionesObtenerOpTransaccio);
			datosInversion.addProperty("Usuario", InversionesObtenerOpUsuario);
			datosInversion.addProperty("SucOrigen", InversionesObtenerOpSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesObtenerOpSucDestino);
			datosInversion.addProperty("Modulo", InversionesObtenerOpModulo);

			inversionesConsultarOp = InversionesObtenerOp;
		}

		logger.info("datosInversion" + datosInversion);
		JsonObject inversionConsultarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesConsultarOp, datosInversion);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);

		JsonObject inversionesObjecto = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject();
		JsonArray inversionesArreglo = inversionesObjecto.has("inversion") ? inversionesObjecto.get("inversion").getAsJsonArray() : new JsonArray();

		JsonObject datosHorario = new JsonObject();
		datosHorario.addProperty("Tip_Consul", HorarioInversionOpTipConsul);
		datosHorario.addProperty("Tip_Transf", HorarioInversionOpTipTransf);
		datosHorario.addProperty("Err_Codigo", "");
		datosHorario.addProperty("Msj_Error", "");
		datosHorario.addProperty("NumTransac", FolioTransaccionGenerarOpFolTransa);
		datosHorario.addProperty("Transaccio", HorarioInversionOpTransaccio);
		datosHorario.addProperty("Usuario", HorarioInversionOpUsuario);
		datosHorario.addProperty("FechaSis", fechaSis);
		datosHorario.addProperty("SucOrigen", HorarioInversionOpSucOrigen);
		datosHorario.addProperty("SucDestino", HorarioInversionOpSucDestino);
		datosHorario.addProperty("Modulo", HorarioInversionOpModulo);

		logger.info("datosHorario" + datosHorario);
		JsonObject horarioInversionOpResultadoObjecto = Utilerias.performOperacion(ConfiguracionServicio, HorarioInversionOp, datosHorario);
		logger.info("horarioInversionOpResultadoObjecto" + horarioInversionOpResultadoObjecto);

		JsonObject horariosObjecto = horarioInversionOpResultadoObjecto.get("horariosInversion").getAsJsonObject();
		JsonArray horariosArreglo = horariosObjecto.has("horarioInversion") ? horariosObjecto.get("horarioInversion").getAsJsonArray() : new JsonArray();

		String horHorIni = null;
		String horHorFin = null;

		for (JsonElement horElemento : horariosArreglo) {
			JsonObject horarioObj = horElemento.getAsJsonObject();
			if("PAGARE".equals(categoria) && "IN".equals(horarioObj.get("Hor_TipMod").getAsString())) {
				horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
				horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
			} else if("CE".equals(horarioObj.get("Hor_TipMod").getAsString())) {
				horHorIni = horarioObj.has("Hor_HorIni") ? horarioObj.get("Hor_HorIni").getAsString() : "";
				horHorFin = horarioObj.has("Hor_HorFin") ? horarioObj.get("Hor_HorFin").getAsString() : "";
			}
		}
		
		JsonObject resultado = null;
		for (JsonElement invElemento : inversionesArreglo) {
			JsonObject inversionObj = invElemento.getAsJsonObject();
			Boolean fotDescri = inversionObj.has("Fot_Descri") ? inversionObj.get("Fot_Descri").getAsString().equals(categoria) : true;
			Boolean invTipo = inversionObj.has("Inv_Tipo") ? inversionObj.get("Inv_Tipo").getAsString().equals("V") : true;

			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero) && fotDescri && invTipo) {
				String invFecIni = inversionObj.has("Inv_FecIni") ? inversionObj.get("Inv_FecIni").getAsString() :  "";
				String invFecVen = inversionObj.has("Inv_FecVen") ? inversionObj.get("Inv_FecVen").getAsString() : "";
				int plazo = 0;
				double intBru = 0;
				double invIntNet = 0;
				double invISRTot = 0;
				double invGat = 0;
				double invGatRea = 0;

				if (categoria.equals(InversionesCategoriasEnum.PAGARE.toString())) {
					invGat = inversionObj.has("Inv_GAT") ? inversionObj.get("Inv_GAT").getAsDouble() : 0;
					invGatRea = inversionObj.has("Inv_GATRea") ? inversionObj.get("Inv_GATRea").getAsDouble() : 0;
					plazo = inversionObj.has("Inv_Plazo") ? inversionObj.get("Inv_Plazo").getAsInt() : 0;
					intBru = inversionObj.has("Inv_TBruta") ? inversionObj.get("Inv_TBruta").getAsDouble() : 0;
					invIntNet = inversionObj.has("Imp_Intere") ? inversionObj.get("Imp_Intere").getAsDouble() : 0;
					invISRTot = inversionObj.has("Inv_ISR") ? inversionObj.get("Inv_ISR").getAsDouble() : 0;
				} else {
					invGat = inversionObj.has("Inv_Gat") ? inversionObj.get("Inv_Gat").getAsDouble() : 0;
					invGatRea = inversionObj.has("Inv_GatRea") ? inversionObj.get("Inv_GatRea").getAsDouble() : 0;
					plazo = inversionObj.has("Plazo") ? inversionObj.get("Plazo").getAsInt() : 0;

					double amoTasa = inversionObj.has("Amo_Tasa") ? inversionObj.get("Amo_Tasa").getAsDouble() : 0;
					double amoISR = inversionObj.has("Amo_ISR") ? inversionObj.get("Amo_ISR").getAsDouble() : 0;					
					intBru = amoTasa + amoISR / 10;

					invIntNet = inversionObj.has("Inv_IntNet") ? inversionObj.get("Inv_IntNet").getAsDouble() : 0;
					invISRTot = inversionObj.has("Inv_ISRTot") ? inversionObj.get("Inv_ISRTot").getAsDouble() : 0;
				}

				Date fechaIni = null;
				Date fechaVen = null;

				try {
					SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
					fechaIni = sdfFecha.parse(invFecIni);
				} catch (Exception e) {
					try {
						SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
						fechaIni = sdfFecha.parse(invFecIni);
					} catch (ParseException ex) {
						logger.info("formato sdfFecha no valido.");
					}
				}

				try {
					SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
					fechaVen = sdfFecha.parse(invFecVen);
				} catch (Exception e) {
					try {
						SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
						fechaVen = sdfFecha.parse(invFecVen);
					} catch (ParseException ex) {
						logger.info("formato sdfFecha no valido.");
					}
				}

				Date horIni = null;
				Date horFin = null;

				SimpleDateFormat sdfHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try {
					horIni = sdfHora.parse(horHorIni);
				} catch (Exception e) {
					logger.info("error al formatear HorIni.");
				}

				try {
					horFin = sdfHora.parse(horHorFin);
				} catch (Exception e) {
					logger.info("error al formatear HorFin.");
				}

				Boolean cpRenInv = Utilerias.calcularVencimiento(fechaVen, horIni, horFin);
				intBru = Utilerias.redondear(intBru, 2);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				resultado = new JsonObject();
				JsonObject inversion = new JsonObject();
				inversion.addProperty("invFecIni", fechaIni != null ? simpleDateFormat.format(fechaIni) : "");
				inversion.addProperty("invFecVen", fechaVen != null ? simpleDateFormat.format(fechaVen) : "");
				inversion.addProperty("invCuenta", inversionObj.has("Inv_Cuenta") ? inversionObj.get("Inv_Cuenta").getAsString() : "");
				inversion.addProperty("invGat", invGat);
				inversion.addProperty("invGatRea", invGatRea);
				inversion.addProperty("plazo", plazo);
				inversion.addProperty("intBru", intBru);
				inversion.addProperty("invIntNet", invIntNet);
				inversion.addProperty("invISRTot", invISRTot);
				inversion.addProperty("invTotal", inversionObj.has("Inv_Total") ? inversionObj.get("Inv_Total").getAsDouble() : 0);
				inversion.addProperty("cpRenInv", cpRenInv);
				resultado.add("inversion", inversion);
			}
		}

		if(resultado == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.25");
			bimMessageDTO.addMergeVariable("invNumero", invNumero);
			throw new ConflictException(bimMessageDTO.toString());
		}

		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path("{invNumero}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reinversion(@PathParam("invNumero") String invNumero,	
			@QueryParam("categoria") String categoria, @Context final Request solicitud) {
		logger.info("CTRL: Comenzando reinversion metodo");
		String mensaje = null;
		try {
			mensaje = HttpClientUtils.getStringContent(solicitud);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonObject inversionesObjeto = new Gson().fromJson(mensaje, JsonObject.class);
		JsonObject renovarInversion = inversionesObjeto.getAsJsonObject("renovarInversion");	
			
		// String bearerToken = solicitud.getHeader("Authorization");
		// JsonObject principalResultadoObjecto = Utilerias.getPrincipal(bearerToken);
		
		// String usuNumero = principalResultadoObjecto.get("usuNumero").getAsString();
		// String usuClient = principalResultadoObjecto.get("usuClient").getAsString();
		
		String usuClient = "00193500";
		
		String bitPriRef = solicitud.getHeader("User-Agent");
		String bitDireIP = solicitud.getHeader("X-Forwarded-For");
		String fechaSis = Utilerias.getFechaSis();
		
		logger.info("User-Agent: " + solicitud.getHeader("User-Agent"));
		logger.info("X-Forwarded-For: " + solicitud.getHeader("X-Forwarded-For"));

		JsonObject datosTransaccion = new JsonObject();
		datosTransaccion.addProperty("Num_Transa", "");
		datosTransaccion.addProperty("SucOrigen", FolioTransaccionGenerarOpSucOrigen);
		
		logger.info("datosTransaccion" + datosTransaccion);
		JsonObject folioTransaccionGenerarOpResultadoObjeto = Utilerias.performOperacion(TransaccionServicio, FolioTransaccionGenerarOp, datosTransaccion);
		logger.info("folioTransaccionGenerarOpResultadoObjeto" + folioTransaccionGenerarOpResultadoObjeto);

		JsonObject transaccion = folioTransaccionGenerarOpResultadoObjeto.has("transaccion") ? folioTransaccionGenerarOpResultadoObjeto.get("transaccion").getAsJsonObject() : new JsonObject();
		String numTransac = transaccion.has("Fol_Transa") ? transaccion.get("Fol_Transa").getAsString() : ""; 
				
		/* 
			SP 2   NBINVERSCON
			Este SP se encarga de obtener los datos de inversion de un usuario
			Se verifica que el numero de inversion proporcionado por fron se encuentre en las inversiónes de usuario
		*/
		
		String usuNumero = "001844";

		JsonObject datosInversion = new JsonObject();
		datosInversion.addProperty("FechaSis", fechaSis);
		String inversionesConsultarOp;
		if ("PAGARE".equals(categoria)) {
			datosInversion.addProperty("Inv_Numero", "");
			datosInversion.addProperty("Inv_Usuari", usuNumero);
			datosInversion.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
			datosInversion.addProperty("NumTransac", numTransac);
			datosInversion.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
			datosInversion.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);			
			datosInversion.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
			datosInversion.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
			datosInversion.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
			inversionesConsultarOp = InversionesPagareNumeroUsuarioObtenerOp;
		} else {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.27");
			throw new BadRequestException(bimMessageDTO.toString());
		}

		logger.info("datosInversion" + datosInversion);
		JsonObject inversionConsultarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesConsultarOp, datosInversion);
		logger.info("inversionConsultarOpResultadoObjeto" + inversionConsultarOpResultadoObjeto);

		JsonObject inversionesObjecto = inversionConsultarOpResultadoObjeto.get("inversiones").getAsJsonObject();
		JsonArray inversionesArreglo = inversionesObjecto.has("inversion") ? inversionesObjecto.get("inversion").getAsJsonArray() : new JsonArray();
		
		JsonObject inversion = null;
		for (JsonElement invElemento : inversionesArreglo) {
			JsonObject inversionObj = invElemento.getAsJsonObject();
			if (inversionObj.get("Inv_Numero").getAsString().equals(invNumero)) {
				inversion = inversionObj;
			}
		}

		if(inversion == null) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.25");
			bimMessageDTO.addMergeVariable("invNumero", invNumero);
			throw new ConflictException(bimMessageDTO.toString());
		}

		logger.info("INVERSION " + inversion);

		/* 
			SP3 CLCLIENTCON se consultan los datos del cliente
		*/	
		
		JsonObject datosCliente = new JsonObject();
		datosCliente.addProperty("Cli_Numero", usuClient);
		datosCliente.addProperty("Cli_Sucurs", "");
		datosCliente.addProperty("Cli_Nombre", "");
		datosCliente.addProperty("Tip_Consul", ClienteConsultarOpTipConsul);
		datosCliente.addProperty("NumTransac", numTransac);
		datosCliente.addProperty("Transaccio", ClienteConsultarOpTransaccio);
		datosCliente.addProperty("Usuario", ClienteConsultarOpUsuari);
		datosCliente.addProperty("FechaSis", fechaSis);
		datosCliente.addProperty("SucOrigen", ClienteConsultarOpSucOrigen);
		datosCliente.addProperty("SucDestino", ClienteConsultarOpSucDestino);
		datosCliente.addProperty("Modulo", ClienteConsultarOpModulo);
		
		logger.info("datosCliente" + datosCliente);
		JsonObject clienteConsultarOpResultadoObjeto = Utilerias.performOperacion(ClienteServicio, ClienteConsultarOp, datosCliente);
		logger.info("clienteConsultarOpResultadoObjeto" + clienteConsultarOpResultadoObjeto);

		JsonObject cliente = clienteConsultarOpResultadoObjeto.has("cliente") ? clienteConsultarOpResultadoObjeto.get("cliente").getAsJsonObject() : new JsonObject();
		String cliSucurs = cliente.has("Cli_Sucurs") ? cliente.get("Cli_Sucurs").getAsString() : "";
		String cliComple = cliente.has("Cli_Comple") ? cliente.get("Cli_Comple").getAsString() : "";

		/* 
			SP4 NBCUEORICON se consulta 
		*/

		JsonObject datosSucursal = new JsonObject();
		datosSucursal.addProperty("Par_Sucurs", cliSucurs);
		datosSucursal.addProperty("Tip_Consul", "");
		datosSucursal.addProperty("NumTransac", numTransac);
		datosSucursal.addProperty("Transaccio", informacionSucursalObtenerOpTransaccio);
		datosSucursal.addProperty("Usuario", informacionSucursalObtenerOpUsuari);
		datosSucursal.addProperty("FechaSis", fechaSis);
		datosSucursal.addProperty("SucOrigen", informacionSucursalObtenerOpSucOrigen);
		datosSucursal.addProperty("SucDestino", informacionSucursalObtenerOpSucDestino);
		datosSucursal.addProperty("Modulo", informacionSucursalObtenerOpModulo);

		logger.info("datosSucursal" + datosSucursal);
		JsonObject informacionSucursalObtenerOpResultadoObjeto = Utilerias.performOperacion(ConfiguracionServicio, InformacionSucursalObtenerOp, datosSucursal);
		logger.info("informacionSucursalObtenerOpResultadoObjeto" + informacionSucursalObtenerOpResultadoObjeto);		

		JsonObject datosPerfilRiesgo = new JsonObject();
		datosPerfilRiesgo.addProperty("Apl_Client", usuClient);
		datosPerfilRiesgo.addProperty("Apl_Cuesti", Integer.parseInt(UsuarioPerfilRiesgoConsultarOpAplClient));
		datosPerfilRiesgo.addProperty("Tip_Consul", UsuarioPerfilRiesgoConsultarOpTipConsul);
		datosPerfilRiesgo.addProperty("NumTransac", numTransac);
		datosPerfilRiesgo.addProperty("Transaccio", UsuarioPerfilRiesgoConsultarOpTransaccio);
		datosPerfilRiesgo.addProperty("Usuario", UsuarioPerfilRiesgoConsultarOpUsuari);
		datosPerfilRiesgo.addProperty("FechaSis", fechaSis);
		datosPerfilRiesgo.addProperty("SucOrigen", UsuarioPerfilRiesgoConsultarOpSucOrigen);
		datosPerfilRiesgo.addProperty("SucDestino", UsuarioPerfilRiesgoConsultarOpSucDestino);
		datosPerfilRiesgo.addProperty("Modulo", UsuarioPerfilRiesgoConsultarOpModulo);

		logger.info("datosPerfilRiesgo" + datosPerfilRiesgo);
		JsonObject usuarioPerfilRiesgoConsultarOpResultadoObjeto = Utilerias.performOperacion(UsuarioServicio, UsuarioPerfilRiesgoConsultarOp, datosPerfilRiesgo);
		logger.info("usuarioPerfilRiesgoConsultarOpResultadoObjeto" + usuarioPerfilRiesgoConsultarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
				Fecha =  fecha actual del sistema
				NumDia=  plazo
		 */

		JsonObject datosFechaHabil = new JsonObject();
		datosFechaHabil.addProperty("Fecha", fechaSis);
		datosFechaHabil.addProperty("NumDia", inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsInt() : 0);
		datosFechaHabil.addProperty("FinSem", fechaHabilConsultarOpFinSem);
		datosFechaHabil.addProperty("NumTransac", numTransac);
		datosFechaHabil.addProperty("Transaccio", fechaHabilConsultarOpTransaccio);
		datosFechaHabil.addProperty("Usuario", fechaHabilConsultarOpUsuari);
		datosFechaHabil.addProperty("FechaSis", fechaSis);
		datosFechaHabil.addProperty("SucOrigen", fechaHabilConsultarOpSucOrigen);
		datosFechaHabil.addProperty("SucDestino", fechaHabilConsultarOpSucDestino);
		datosFechaHabil.addProperty("Modulo", fechaHabilConsultarOpModulo);

		logger.info("datosFechaHabil" + datosFechaHabil);
		JsonObject fechaHabilConsultarOpResultadoObjeto = Utilerias.performOperacion(ReinversionServicio, fechaHabilConsultarOp, datosFechaHabil);
		logger.info("fechaHabilConsultarOpResultadoObjeto" + fechaHabilConsultarOpResultadoObjeto);

		/* 
			Parametros obtenidos por medio del principal
				Cli_Numero = usu_Client
				Inv_Moneda=  01 constante
		*/		

		String cliTipo = cliente.has("Cli_Tipo") ? cliente.get("Cli_Tipo").getAsString() : "";

		JsonObject datosTasaCliente = new JsonObject();
		datosTasaCliente.addProperty("Cli_Numero", usuClient);
		datosTasaCliente.addProperty("Inv_Cantid", inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0);
		datosTasaCliente.addProperty("Inv_Moneda", tasaClienteConsultarOpInvMoneda);
		datosTasaCliente.addProperty("Cli_Tipo", cliTipo);
		datosTasaCliente.addProperty("Plazo", inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsInt() : 0);
		datosTasaCliente.addProperty("Inv_FecVen", "");
		datosTasaCliente.addProperty("Ine_Numero", "");
		datosTasaCliente.addProperty("Tasa", Integer.parseInt(tasaClienteConsultarOpTasa));		
		datosTasaCliente.addProperty("Inv_GruTas", "");
		datosTasaCliente.addProperty("Inv_NuPoGr", "");
		datosTasaCliente.addProperty("NumTransac", numTransac);
		datosTasaCliente.addProperty("Transaccio", tasaClienteConsultarOpTransaccio);
		datosTasaCliente.addProperty("Usuario", tasaClienteConsultarOpUsuari);
		datosTasaCliente.addProperty("FechaSis", fechaSis);
		datosTasaCliente.addProperty("SucOrigen", tasaClienteConsultarOpSucOrigen);
		datosTasaCliente.addProperty("SucDestino", tasaClienteConsultarOpSucDestino);
		datosTasaCliente.addProperty("Modulo", tasaClienteConsultarOpModulo);

		logger.info("datosTasaCliente" + datosTasaCliente);
		JsonObject tasaClienteConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, tasaClienteConsultarOp, datosTasaCliente);
		logger.info("tasaClienteConsultarOpResultadoObjeto" + tasaClienteConsultarOpResultadoObjeto);
		
		/* 
			Parametros obtenidos por medio del principal
			Mon_Numero =  99 constante para obtener la udi
 		*/

		JsonObject datosMoneda = new JsonObject();
		datosMoneda.addProperty("Mon_Numero", tasaMonedaConsultarOpMonNumero);
		datosMoneda.addProperty("Mon_Descri", "");
		datosMoneda.addProperty("Mon_Fecha", "");
		datosMoneda.addProperty("Tip_Consul", "");
		datosMoneda.addProperty("NumTransac", numTransac);
		datosMoneda.addProperty("Transaccio", tasaMonedaConsultarOpTransaccio);
		datosMoneda.addProperty("Usuario", tasaMonedaConsultarOpUsuari);
		datosMoneda.addProperty("FechaSis", fechaSis);
		datosMoneda.addProperty("SucOrigen", tasaMonedaConsultarOpSucOrigen);
		datosMoneda.addProperty("SucDestino", tasaMonedaConsultarOpSucDestino);
		datosMoneda.addProperty("Modulo", tasaMonedaConsultarOpModulo);

		logger.info("datosMoneda" + datosMoneda);
		JsonObject tasaMonedaConsultarOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, tasaMonedaConsultarOp, datosMoneda);
		logger.info("tasaMonedaConsultarOpResultadoObjeto" + tasaMonedaConsultarOpResultadoObjeto);

		JsonObject monedaConsultar = tasaMonedaConsultarOpResultadoObjeto.has("monedaConsultar") ? tasaMonedaConsultarOpResultadoObjeto.get("monedaConsultar").getAsJsonObject() : new JsonObject();
		Double monFixCom = monedaConsultar.has("Mon_FixCom") ? monedaConsultar.get("Mon_FixCom").getAsDouble() : 0;
		Double invCantid = inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0;
		Double MonTotUDI = 400000.00;
		Double invGAT = 0.00;
		Double invGATRea = 0.00;

		JsonObject clienteConsultar = tasaClienteConsultarOpResultadoObjeto.has("clienteConsultar") ? tasaClienteConsultarOpResultadoObjeto.get("clienteConsultar").getAsJsonObject() : new JsonObject();
		Double invTasInt = clienteConsultar.has("TasInv") ? clienteConsultar.get("TasInv").getAsDouble() : 0;

		if((invCantid / monFixCom) < MonTotUDI) {
		
			/* 
				Parametros obtenidos por medio del principal
			*/

			JsonObject datosGAT = new JsonObject();
			datosGAT.addProperty("Inv_Dias",  inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsInt() : 0);
			datosGAT.addProperty("Inv_TasInt", invTasInt);
			datosGAT.addProperty("Inv_GAT", Integer.parseInt(tasaGATConsultaCalcualrOpInvGAT));
			datosGAT.addProperty("Cal_Opcion", "");
			datosGAT.addProperty("Cue_MonInv", invCantid);
			datosGAT.addProperty("Mon_Comisi", Integer.parseInt(tasaGATConsultaCalcularOpMonComisi));
			datosGAT.addProperty("NumTransac", numTransac);
			datosGAT.addProperty("Transaccio", tasaGATConsultaCalcularOpTransaccio);
			datosGAT.addProperty("Usuario", tasaGATConsultaCalcularOpUsuari);
			datosGAT.addProperty("FechaSis", fechaSis);
			datosGAT.addProperty("SucOrigen", tasaGATConsultaCalcularOpSucOrigen);
			datosGAT.addProperty("SucDestino", tasaGATConsultaCalcularOpSucDestino);
			datosGAT.addProperty("Modulo", tasaGATConsultaCalcularOpModulo);

			logger.info("datosGAT" + datosGAT);
			JsonObject tasaGATConsultaCalcularOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, tasaGATConsultaCalcularOp, datosGAT);
			logger.info("tasaGATConsultaCalcularOpResultadoObjeto" + tasaGATConsultaCalcularOpResultadoObjeto);

			JsonObject GATConsultaCalcular = tasaGATConsultaCalcularOpResultadoObjeto.has("GATConsultaCalcular") ? tasaGATConsultaCalcularOpResultadoObjeto.get("GATConsultaCalcular").getAsJsonObject() : new JsonObject();
			invGAT = GATConsultaCalcular.has("Inv_GAT") ? GATConsultaCalcular.get("Inv_GAT").getAsDouble() : 0;			

			/* 
				STORED PROCEDURE INCALINFPRO
			*/		

			JsonObject datosGATRea = new JsonObject();
			datosGATRea.addProperty("Inv_GAT", invGAT);
			datosGATRea.addProperty("Inv_GATRea", Integer.parseInt(tasaGATReaConsultaCalcualrOpInvGATRea));
			datosGATRea.addProperty("NumTransac", numTransac);
			datosGATRea.addProperty("Transaccio", tasaGATReaConsultaCalcularOpTransaccio);
			datosGATRea.addProperty("Usuario", tasaGATReaConsultaCalcularOpUsuari);
			datosGATRea.addProperty("FechaSis", fechaSis);
			datosGATRea.addProperty("SucOrigen", tasaGATReaConsultaCalcularOpSucOrigen);
			datosGATRea.addProperty("SucDestino", tasaGATReaConsultaCalcularOpSucDestino);
			datosGATRea.addProperty("Modulo", tasaGATReaConsultaCalcularOpModulo);

			logger.info("datosGATRea" + datosGATRea);
			JsonObject tasaGATRealConsultaCalcularOpResultadoObjeto = Utilerias.performOperacion(TasaServicio, tasaGATRealConsultaCalcularOp, datosGATRea);
			logger.info("tasaGATRealConsultaCalcularOpResultadoObjeto" + tasaGATRealConsultaCalcularOpResultadoObjeto);

			JsonObject GATRealConsultaCalcular = tasaGATRealConsultaCalcularOpResultadoObjeto.has("GATRealConsultaCalcular") ? tasaGATRealConsultaCalcularOpResultadoObjeto.get("GATRealConsultaCalcular").getAsJsonObject() : new JsonObject();
			invGATRea = GATRealConsultaCalcular.has("Inv_GATRea") ? GATRealConsultaCalcular.get("Inv_GATRea").getAsDouble() : 0;	
		}	

		int invPlazo =  inversion.has("Inv_Plazo") ? inversion.get("Inv_Plazo").getAsInt() : 0;
		JsonObject resultadoCalculaTasa = null;
		JsonObject informacionSucursal = informacionSucursalObtenerOpResultadoObjeto.has("informacionSucursal") ? informacionSucursalObtenerOpResultadoObjeto.get("informacionSucursal").getAsJsonObject() : new JsonObject();

		int Par_DiBaIn = informacionSucursal.has("Par_DiBaIn") ? informacionSucursal.get("Par_DiBaIn").getAsInt() : 0;
		Double Cli_TasISR = cliente.has("Cli_TasISR") ? cliente.get("Cli_TasISR").getAsDouble() : 0;
		String Cli_CobISR = cliente.has("Cli_CobISR") ? cliente.get("Cli_CobISR").getAsString() : "";

		if(invCantid < 5000 || invPlazo <= 0){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.26");
			bimMessageDTO.addMergeVariable("invCatid", invCantid.toString());
			throw new ConflictException(bimMessageDTO.toString());
		}

		JsonObject calculaTasa = new JsonObject();
		calculaTasa.addProperty("Inv_Plazo", invPlazo);
		calculaTasa.addProperty("Inv_Cantid", invCantid);
		calculaTasa.addProperty("TasInv", invTasInt);
		calculaTasa.addProperty("Par_DiBaIn", Par_DiBaIn);
		calculaTasa.addProperty("Par_ISR", Cli_TasISR);
		calculaTasa.addProperty("Cli_CobISR", Cli_CobISR);

		resultadoCalculaTasa = Utilerias.calculaTasa(calculaTasa);
		logger.info("resultadoCalculaTasa" + resultadoCalculaTasa);

		/* 
			*************************************************************************
			*************************************************************************
			Empiezan reglas de negocio
			*************************************************************************
			*************************************************************************
		 */

		String folToken = "416218850";
		String cpRSAToken = renovarInversion.has("cpRSAToken") ? renovarInversion.get("cpRSAToken").getAsString() : "";
		String validarToken = TokenService.validarTokenOperacion(folToken, cpRSAToken, usuNumero);

		logger.info("validarToken   " + validarToken);

		if ("B".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.30");
			throw new ForbiddenException(bimMessageDTO.toString());
		}

		if ("C".equals(validarToken)) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.28");
			throw new ForbiddenException(bimMessageDTO.toString());
		}
		
		/* 
			Parametros obtenidos por medio del principal
		*/

		JsonObject datosStatusActualizar = new JsonObject();
		datosStatusActualizar.addProperty("Adi_Invers", inversion.has("Inv_Numero") ? inversion.get("Inv_Numero").getAsString() : "");
		datosStatusActualizar.addProperty("Adi_InsLiq", inversionesStatusActualizarOpAdiInsLiq);
		datosStatusActualizar.addProperty("Adi_MoReGr", Integer.parseInt(inversionesStatusActualizarOpAdiMoReGr));
		datosStatusActualizar.addProperty("NumTransac", numTransac);
		datosStatusActualizar.addProperty("Transaccio", inversionesStatusActualizarOpTransaccio);
		datosStatusActualizar.addProperty("Usuario", inversionesStatusActualizarOpUsuari);
		datosStatusActualizar.addProperty("FechaSis", fechaSis);
		datosStatusActualizar.addProperty("SucOrigen", inversionesStatusActualizarOpSucOrigen);
		datosStatusActualizar.addProperty("SucDestino", inversionesStatusActualizarOpSucDestino);
		datosStatusActualizar.addProperty("Modulo", inversionesStatusActualizarOpModulo);

		logger.info("datosStatusActualizar" + datosStatusActualizar);
		JsonObject inversionesStatusActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesStatusActualizarOp, datosStatusActualizar);
		logger.info("inversionesStatusActualizarOpResultadoObjeto" + inversionesStatusActualizarOpResultadoObjeto);
		
		JsonObject fechaHabil = fechaHabilConsultarOpResultadoObjeto.has("fechaHabil") ? fechaHabilConsultarOpResultadoObjeto.get("fechaHabil").getAsJsonObject() : new JsonObject();
		
		String sigFecha = fechaHabil.has("Fecha") ? fechaHabil.get("Fecha").getAsString() : "";
		Double invCanTot = resultadoCalculaTasa.has("Inv_CanTot") ? resultadoCalculaTasa.get("Inv_CanTot").getAsDouble() : 0; 
		Double invTasa = resultadoCalculaTasa.has("Inv_Tasa") ? resultadoCalculaTasa.get("Inv_Tasa").getAsDouble() : 0; 
		Double invISR = resultadoCalculaTasa.has("Inv_ISR") ? resultadoCalculaTasa.get("Inv_ISR").getAsDouble() : 0;  
		Double invCapita = resultadoCalculaTasa.has("Inv_Capita") ? resultadoCalculaTasa.get("Inv_Capita").getAsDouble() : 0; 		
		Double invCanISR = resultadoCalculaTasa.has("Inv_CanISR") ? resultadoCalculaTasa.get("Inv_CanISR").getAsDouble() : 0; 
		Double invCanNet = resultadoCalculaTasa.has("Inv_CanNet") ? resultadoCalculaTasa.get("Inv_CanNet").getAsDouble() : 0;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date rfecIn = null;
		Date rfecVe = null;
		logger.info("FECHA DEL SISTEMA" + fechaSis);
		logger.info("FECHA SIGUENTE HABIL" + sigFecha);

		try {
			rfecIn = simpleDateFormat.parse(fechaSis);	
		} catch (Exception e) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				rfecIn = sdf.parse(fechaSis);
			} catch (Exception ex) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
					rfecIn = sdf.parse(fechaSis);
				} catch (Exception ei) {
					logger.info("Error en el formato de fecha.");
				}
			}
		}
		
		try {
			rfecVe = simpleDateFormat.parse(sigFecha);	
		} catch (Exception e) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				rfecVe = sdf.parse(sigFecha);
			} catch (Exception ex) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
					rfecVe = sdf.parse(sigFecha);
				} catch (Exception ei) {
					logger.info("Error en el formato de fecha.");
				}
			}
		}

		/* 
			Parametros obtenidos por medio del principal
		*/

		JsonObject datosInversionFinalizada = new JsonObject();
		datosInversionFinalizada.addProperty("Inv_Numero", inversion.has("Inv_Numero") ? inversion.get("Inv_Numero").getAsString() : "");
		datosInversionFinalizada.addProperty("Inv_Deposi", invCapita);
		datosInversionFinalizada.addProperty("Inv_rFecIn", rfecIn != null ? simpleDateFormat.format(rfecIn) : "");
		datosInversionFinalizada.addProperty("Inv_rFecVe", rfecVe != null ? simpleDateFormat.format(rfecVe) : "");
		datosInversionFinalizada.addProperty("Inv_rCanti", inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0);
		datosInversionFinalizada.addProperty("Inv_rTasa", invTasa);
		datosInversionFinalizada.addProperty("Inv_rAutor", inversionesImportesDeInvercionFinalizadaActualizarOpUsuari);
		datosInversionFinalizada.addProperty("Inv_rISR", invISR);
		datosInversionFinalizada.addProperty("Inv_rCuent", inversion.has("Inv_Cuenta") ? inversion.get("Inv_Cuenta").getAsString() : "");
		datosInversionFinalizada.addProperty("Inv_rTBrut", invTasInt);
		datosInversionFinalizada.addProperty("NumTransac", numTransac);		
		datosInversionFinalizada.addProperty("Transaccio", inversionesImportesDeInvercionFinalizadaActualizarOpTransaccio);
		datosInversionFinalizada.addProperty("Usuario", inversionesImportesDeInvercionFinalizadaActualizarOpUsuari);
		datosInversionFinalizada.addProperty("FechaSis", fechaSis);
		datosInversionFinalizada.addProperty("SucOrigen", inversionesImportesDeInvercionFinalizadaActualizarOpSucOrigen);
		datosInversionFinalizada.addProperty("SucDestino", inversionesImportesDeInvercionFinalizadaActualizarOpSucDestino);
		datosInversionFinalizada.addProperty("Modulo", inversionesImportesDeInvercionFinalizadaActualizarOpModulo);

		logger.info("datosInversionFinalizada" + datosInversionFinalizada);
		JsonObject inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesImportesDeInvercionFinalizadaActualizarOp, datosInversionFinalizada);
		logger.info("inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto" + inversionesImportesDeInvercionFinalizadaActualizarOpResultadoObjeto);

		/* 
			SP 15 LISTO MAPEO
		*/		

		JsonObject datosProcesoLiquidacion = new JsonObject();
		datosProcesoLiquidacion.addProperty("Inv_Numero", inversion.has("Inv_Numero") ? inversion.get("Inv_Numero").getAsString() : "");
		datosProcesoLiquidacion.addProperty("Inv_rFecIn", rfecIn != null ? simpleDateFormat.format(rfecIn) : "");
		datosProcesoLiquidacion.addProperty("Inv_rFecVe", rfecVe != null ? simpleDateFormat.format(rfecVe) : "");
		datosProcesoLiquidacion.addProperty("Inv_rCanti", inversion.has("Inv_Cantid") ? inversion.get("Inv_Cantid").getAsDouble() : 0);
		datosProcesoLiquidacion.addProperty("Inv_rTasa", invTasa);
		datosProcesoLiquidacion.addProperty("Inv_rAutor", inversionesImportesDeInvercionFinalizadaActualizarOpUsuari);
		datosProcesoLiquidacion.addProperty("Inv_rISR", invISR);
		datosProcesoLiquidacion.addProperty("Inv_rCuent", inversion.has("Inv_Cuenta") ? inversion.get("Inv_Cuenta").getAsString() : "");
		datosProcesoLiquidacion.addProperty("Dias_Base", Par_DiBaIn);		
		datosProcesoLiquidacion.addProperty("Inv_Fecha", rfecIn != null ? simpleDateFormat.format(rfecIn) : "");
		datosProcesoLiquidacion.addProperty("Inv_rTBrut", invTasInt);
		datosProcesoLiquidacion.addProperty("Inv_MonRef", inversionesProcesoLiquidacionGenerarOpInvMonRef);
		datosProcesoLiquidacion.addProperty("NumTransac", numTransac);		
		datosProcesoLiquidacion.addProperty("Transaccio", inversionesProcesoLiquidacionGenerarOpTransaccio);
		datosProcesoLiquidacion.addProperty("Usuario", inversionesProcesoLiquidacionGenerarOpUsuari);
		datosProcesoLiquidacion.addProperty("FechaSis", fechaSis);
		datosProcesoLiquidacion.addProperty("SucOrigen", inversionesProcesoLiquidacionGenerarOpSucOrigen);
		datosProcesoLiquidacion.addProperty("SucDestino", inversionesProcesoLiquidacionGenerarOpSucDestino);
		datosProcesoLiquidacion.addProperty("Modulo", inversionesProcesoLiquidacionGenerarOpModulo);

		logger.info("datosProcesoLiquidacion" + datosProcesoLiquidacion);
		JsonObject inversionesProcesoLiquidacionGenerarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesProcesoLiquidacionGenerarOp, datosProcesoLiquidacion);
		logger.info("inversionesProcesoLiquidacionGenerarOpResultadoObjeto" + inversionesProcesoLiquidacionGenerarOpResultadoObjeto);
		
		JsonObject ProcesoLiquidacionGenerar = inversionesProcesoLiquidacionGenerarOpResultadoObjeto.has("ProcesoLiquidacionGenerar") ? inversionesProcesoLiquidacionGenerarOpResultadoObjeto.get("ProcesoLiquidacionGenerar").getAsJsonObject() : new JsonObject();
		String errCodigo = ProcesoLiquidacionGenerar.has("Err_Codigo") ? ProcesoLiquidacionGenerar.get("Err_Codigo").getAsString() : "";
		String errMensaj = ProcesoLiquidacionGenerar.has("Err_Mensaj") ? ProcesoLiquidacionGenerar.get("Err_Mensaj").getAsString() : "";
		String invNueva = ProcesoLiquidacionGenerar.has("Inv_Nueva") ? ProcesoLiquidacionGenerar.get("Inv_Nueva").getAsString() : "";

		if(!"000000".equals(errCodigo)){
			BimMessageDTO bimMessageDTO = new BimMessageDTO("BIM.MENSAJ.31");			
			bimMessageDTO.addMergeVariable("errMensaj", errMensaj);
			throw new InternalServerException(bimMessageDTO.toString());
		}		

		/* 
			SP 16 
		*/

		JsonObject datosBitacora = new JsonObject();
		datosBitacora.addProperty("Bit_Usuari", usuNumero);
		datosBitacora.addProperty("Bit_Fecha", fechaSis);
		datosBitacora.addProperty("Bit_NumTra", "");
		datosBitacora.addProperty("Bit_TipOpe", BitacoraCreacionOpBitTipOpe);
		datosBitacora.addProperty("Bit_CueOri", "");
		datosBitacora.addProperty("Bit_CueDes", "");
		datosBitacora.addProperty("Bit_Monto", Integer.parseInt(BitacoraCreacionOpBitMonto));
		datosBitacora.addProperty("Bit_PriRef", bitPriRef != null ? bitPriRef : "");
		datosBitacora.addProperty("Bit_SegRef", "");
		datosBitacora.addProperty("Bit_DireIP", bitDireIP != null ? bitDireIP : "");
		datosBitacora.addProperty("NumTransac", numTransac);
		datosBitacora.addProperty("Transaccio", BitacoraCreacionOpTransaccio);
		datosBitacora.addProperty("Usuario", BitacoraCreacionOpUsuario);
		datosBitacora.addProperty("FechaSis", fechaSis);
		datosBitacora.addProperty("SucOrigen", BitacoraCreacionOpSucOrigen);
		datosBitacora.addProperty("SucDestino", BitacoraCreacionOpSucDestino);
		datosBitacora.addProperty("Modulo", BitacoraCreacionOpModulo);

		logger.info("datosBitacora" + datosBitacora);
		JsonObject bitacoraCreacionOpResultadoObjeto = Utilerias.performOperacion(BitacoraServicio, BitacoraCreacionOp, datosBitacora);
		logger.info("bitacoraCreacionOpResultadoObjeto" + bitacoraCreacionOpResultadoObjeto);
		
		/* 
			SP 17 EN PROGRESO MAPEO
		*/	

		JsonObject datosIversionVsEstadoCuenta = new JsonObject();
		datosIversionVsEstadoCuenta.addProperty("Cor_Usuari", usuNumero);
		datosIversionVsEstadoCuenta.addProperty("Cor_Cuenta", inversion.has("Inv_Cuenta") ? inversion.get("Inv_Cuenta").getAsString(): "");
		datosIversionVsEstadoCuenta.addProperty("Cor_Status", "");
		datosIversionVsEstadoCuenta.addProperty("Cor_MoLiDi", inversionesContraEstadoCuentaActualizarOpCorMoLiDi);
		datosIversionVsEstadoCuenta.addProperty("Cor_MonDia", invCanTot);
		datosIversionVsEstadoCuenta.addProperty("Cor_CliUsu", "");
		datosIversionVsEstadoCuenta.addProperty("Cor_Alias", "");
		datosIversionVsEstadoCuenta.addProperty("Tip_Actual", inversionesContraEstadoCuentaActualizarOpTipActual);
		datosIversionVsEstadoCuenta.addProperty("NumTransac", numTransac);		
		datosIversionVsEstadoCuenta.addProperty("Transaccio", inversionesContraEstadoCuentaActualizarOpTransaccio);
		datosIversionVsEstadoCuenta.addProperty("Usuario", inversionesContraEstadoCuentaActualizarOpUsuari);
		datosIversionVsEstadoCuenta.addProperty("FechaSis", fechaSis);
		datosIversionVsEstadoCuenta.addProperty("SucOrigen", inversionesContraEstadoCuentaActualizarOpSucOrigen);
		datosIversionVsEstadoCuenta.addProperty("SucDestino", inversionesContraEstadoCuentaActualizarOpSucDestino);
		datosIversionVsEstadoCuenta.addProperty("Modulo", inversionesContraEstadoCuentaActualizarOpModulo);

		logger.info("datosIversionVsEstadoCuenta" + datosIversionVsEstadoCuenta);
		JsonObject inversionesContraEstadoCuentaActualizarOpResultadoObjeto = Utilerias.performOperacion(InversionesServicio, inversionesContraEstadoCuentaActualizarOp, datosIversionVsEstadoCuenta);
		logger.info("inversionesContraEstadoCuentaActualizarOpResultadoObjeto" + inversionesContraEstadoCuentaActualizarOpResultadoObjeto);
		
		/* 
			SP 18 PENDIENTE MAPEO
		*/		

		JsonObject inversionesPagareNumeroUsuarioObtener = new JsonObject();
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Numero", "");
		inversionesPagareNumeroUsuarioObtener.addProperty("Inv_Usuari", usuNumero);
		inversionesPagareNumeroUsuarioObtener.addProperty("Tip_Consul", InversionesPagareNumeroUsuarioObtenerOpTipConsul);
		inversionesPagareNumeroUsuarioObtener.addProperty("NumTransac", numTransac);
		inversionesPagareNumeroUsuarioObtener.addProperty("Transaccio", InversionesPagareNumeroUsuarioObtenerOpTransaccio);
		inversionesPagareNumeroUsuarioObtener.addProperty("Usuario", InversionesPagareNumeroUsuarioObtenerOpUsuario);
		inversionesPagareNumeroUsuarioObtener.addProperty("FechaSis", fechaSis);
		inversionesPagareNumeroUsuarioObtener.addProperty("SucOrigen", InversionesPagareNumeroUsuarioObtenerOpSucOrigen);
		inversionesPagareNumeroUsuarioObtener.addProperty("SucDestino", InversionesPagareNumeroUsuarioObtenerOpSucDestino);
		inversionesPagareNumeroUsuarioObtener.addProperty("Modulo", InversionesPagareNumeroUsuarioObtenerOpModulo);
		
		logger.info("inversionesPagareNumeroUsuarioObtener" + inversionesPagareNumeroUsuarioObtener);
		JsonObject inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto = Utilerias.performOperacion(InversionesServicio, InversionesPagareNumeroUsuarioObtenerOp, inversionesPagareNumeroUsuarioObtener);
		logger.info("inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto" + inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto);

		JsonObject inversiones = inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto.has("inversiones") ? inversionesPagareNumeroUsuarioObtenerOpResultadoObjecto.get("inversiones").getAsJsonObject() : new JsonObject();
		JsonArray inversionesArreglos = inversiones.has("inversion") ? inversiones.get("inversion").getAsJsonArray() : new JsonArray();

		JsonObject resultado = null;
		for (JsonElement invElemento : inversionesArreglos) {
			JsonObject inversionObj = invElemento.getAsJsonObject();

			if (inversionObj.get("Inv_Numero").getAsString().equals(invNueva)) {
				String invFecIni = inversionObj.has("Inv_FecIni") ? inversionObj.get("Inv_FecIni").getAsString() : "";
				String invFecVen = inversionObj.has("Inv_FecVen") ? inversionObj.get("Inv_FecVen").getAsString() : "";
				Date fecIni = null;
				Date fecVen = null;

				 try {
					 fecIni = simpleDateFormat.parse(invFecIni);
				 } catch (Exception e) {
					 logger.info("Error en el formato de fecha.");
				 }

				 try {
					fecVen = simpleDateFormat.parse(invFecVen);
				} catch (Exception e) {
					logger.info("Error en el formato de fecha.");
				}

				resultado = new JsonObject();
				JsonObject inversionRenovada = new JsonObject();
				inversionRenovada.addProperty("invCuenta", inversionObj.has("Inv_Cuenta") ? inversionObj.get("Inv_Cuenta").getAsString() : "");
				inversionRenovada.addProperty("invNueva", invNueva);
				inversionRenovada.addProperty("invCantidad", inversionObj.has("Inv_Cantid") ? inversionObj.get("Inv_Cantid").getAsString() : "");
				inversionRenovada.addProperty("invDeposi", invCapita);
				inversionRenovada.addProperty("invPlazo", inversionObj.has("Inv_Plazo") ? inversionObj.get("Inv_Plazo").getAsInt() : 0);
				inversionRenovada.addProperty("invTBruta",  inversionObj.has("Inv_TBruta") ? inversionObj.get("Inv_TBruta").getAsInt() : 0);
				inversionRenovada.addProperty("invCanBru", inversionObj.has("Imp_Intere") ? inversionObj.get("Imp_Intere").getAsDouble() : 0);
				inversionRenovada.addProperty("invGat", inversionObj.has("Inv_GAT") ? inversionObj.get("Inv_GAT").getAsDouble() : 0);
				inversionRenovada.addProperty("invGatRea", inversionObj.has("Inv_GATRea") ? inversionObj.get("Inv_GATRea").getAsDouble() : 0);
				inversionRenovada.addProperty("invFecIni", fecIni != null ? simpleDateFormat.format(fecIni) : "");
				inversionRenovada.addProperty("invISR", invISR);
				inversionRenovada.addProperty("invCanISR", inversionObj.has("Imp_ISR") ? inversionObj.get("Imp_ISR").getAsDouble() : 0);
				inversionRenovada.addProperty("invFecVen", fecVen != null ? simpleDateFormat.format(fecVen) : "");
				inversionRenovada.addProperty("invTasa", inversionObj.has("Inv_Tasa") ? inversionObj.get("Inv_Tasa").getAsDouble() : 0);
				inversionRenovada.addProperty("invCanNet", invCanNet);
				inversionRenovada.addProperty("adiInsLiq", inversionObj.has("Adi_InsLiq") ? inversionObj.get("Adi_InsLiq").getAsString() : "");
				inversionRenovada.addProperty("invCanTot", inversionObj.has("Inv_Total") ? inversionObj.get("Inv_Total").getAsDouble() : 0);
				inversionRenovada.addProperty("usuNombre", cliComple);
				resultado.add("inversionRenovada", inversionRenovada);
			}
		}	

		return Response.ok(resultado.toString(), MediaType.APPLICATION_JSON)
				.build();
    	}
	
		private void addResourceToRegistry(MicroservicesRegistryImpl microservicesRegistryImpl) {
			microservicesRegistryImpl.addExceptionMapper(new BimExceptionMapper());
	}
}
