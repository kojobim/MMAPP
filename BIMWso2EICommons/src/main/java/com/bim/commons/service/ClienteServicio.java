package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones sobre el cliente
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class ClienteServicio extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(ClienteServicio.class);
	
	private static String ClienteServicio;
	private static String ClienteConsultarOp;
	private static String ClienteConsultarOpTransaccio;
	private static String ClienteConsultarOpUsuario;
	private static String ClienteConsultarOpSucOrigen;
	private static String ClienteConsultarOpSucDestino;
	private static String ClienteConsultarOpModulo;
	
	public ClienteServicio() {
		super();

		ClienteServicio = properties.getProperty("data_service.cliente_servicio");
		
		ClienteConsultarOp = properties.getProperty("cliente_servicio.op.cliente_consultar");
		ClienteConsultarOpTransaccio = properties.getProperty("op.cliente_consultar.transaccio");
		ClienteConsultarOpUsuario = properties.getProperty("op.cliente_consultar.usuario");
		ClienteConsultarOpSucOrigen = properties.getProperty("op.cliente_consultar.suc_origen");
		ClienteConsultarOpSucDestino = properties.getProperty("op.cliente_consultar.suc_destino");
		ClienteConsultarOpModulo = properties.getProperty("op.cliente_consultar.modulo");
	}
	
	
	/**
	 * Método para la consulta de cliente
	 * ProcedureName: CLCLIENTCON
	 * @param datosClienteConsultar
	 * <pre> 
	 * { 
	 *	Cli_Numero: String,
	 *	Tip_Consul: String,
	 *	Cli_Sucurs?: String,
	 *	Cli_Nombre?: String,
	 *	NumTransac?: String,
	 *	FechaSis: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *	cliente: {
	 *		Cli_Numero: String,
	 *		Cli_Sucurs: String,
	 *		Cli_Tipo: String,
	 *		Cli_Sector: String,
	 *		Cli_Activi: String,
	 *		Cli_ActINE: String,
	 *		Tit_Numero: String,
	 *		Cli_Titulo: String,
	 *		Cli_Nombre: String,
	 *		Cli_ApePat: String,
	 *		Cli_ApeMat: String,
	 *		Cli_RazSoc: String,
	 *		Cli_Comple: String,
	 *		Cli_ComOrd: String,
	 *		Cli_RFC: String,
	 *		Cli_CURP: String,
	 *		Cli_Calle: String,
	 *		Cli_CalNum: String,
	 *		Cli_NumExt: String,
	 *		Cli_Coloni: String,
	 *		Cli_Entida: String,
	 *		Cli_Locali: String,
	 *		Cli_Pais: String,
	 *		Cli_CodPos: String,
	 *		Cli_ApaPos: String,
	 *		Cli_Consec: Integer,
	 *		Cli_CobISR: String,
	 *		Cli_Promot: String,
	 *		Cli_ProAsi: String,
	 *		Cli_ProOri: String,
	 *		Cli_Telefo: String,
	 *		Cli_Fax: String,
	 *		Cli_Status: String,
	 *		Cli_ClaCar: String,
	 *		Cli_Clasif: String,
	 *		Cli_Contac: String,
	 *		Cli_ConCon: Integer,
	 *		Cli_CobIVA: String,
	 *		Cli_DesAct: String,
	 *		Cli_Nacion: String,
	 *		Cli_GruCor: String,
	 *		Cli_NomCor: String,
	 *		Cli_PaiRes: String,
	 *		Cli_TasISR: Decimal,
	 *		Cli_ActEmp: String,
	 *		Cli_Grupo: String,
	 *		Cli_Corpor: String,
	 *		Cli_InsChe: String,
	 *		Cli_ProOri: String,
	 *		Cli_ClaEco: String,
	 *		Cli_Fecha: Date,
	 *		FechaSis: Date,
	 *		Cda_Tamano: String,
	 *		Cda_TiDeRe: Integer,
	 *		Cda_Usuari: String,
	 *		Cda_DesEsp: String,
	 *		Cda_NumEmp: Integer,
	 *		Tit_Numero: String,
	 *		Cli_SucAti: String,
	 *		Adi_Email: String,
	 *		Loc_Nombre: String,
	 *		Ent_Nombre: String,
	 *		Pai_Nombre: String
	 *	}
	 * }
	 * </pre>
	 */
	public JsonObject clienteConsultar(JsonObject datosClienteConsultar) {
		logger.info("COMMONS: Comenzando clienteConsultar metodo...");
		if(!datosClienteConsultar.has("Cli_Sucurs"))
			datosClienteConsultar.addProperty("Cli_Sucurs", "");
		if(!datosClienteConsultar.has("Cli_Nombre"))
			datosClienteConsultar.addProperty("Cli_Nombre", "");
		if(!datosClienteConsultar.has("NumTransac"))
			datosClienteConsultar.addProperty("NumTransac", "");
		datosClienteConsultar.addProperty("Transaccio", ClienteConsultarOpTransaccio);
		datosClienteConsultar.addProperty("Usuario", ClienteConsultarOpUsuario);
		datosClienteConsultar.addProperty("SucOrigen", ClienteConsultarOpSucOrigen);
		datosClienteConsultar.addProperty("SucDestino", ClienteConsultarOpSucDestino);
		datosClienteConsultar.addProperty("Modulo", ClienteConsultarOpModulo);
		JsonObject clienteConsultarResultadoObjecto = Utilerias
				.performOperacion(ClienteServicio, ClienteConsultarOp, 
						datosClienteConsultar);
		logger.info("COMMONS: Finalizando clienteConsultar metodo...");
		return clienteConsultarResultadoObjecto;
	}//Cierre del método

}