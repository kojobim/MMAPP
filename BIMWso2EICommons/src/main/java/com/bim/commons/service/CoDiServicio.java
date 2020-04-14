package com.bim.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.Utilerias;
import com.google.gson.JsonObject;

/**
 * Esta clase define las operaciones CoDi
 * @author Backend Team MedioMelon
 * @version BackendMM2020
 *
 */
public class CoDiServicio extends BaseService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CoDiServicio.class);
	
	
	private static String CoDiServicio;
	
	//operaciones
	private static String AceptarPagoCoDiOp;
	private static String AceptarPagoCoDiOpTransaccio;
	private static String AceptarPagoCoDiOpUsuario;
	private static String AceptarPagoCoDiOpSucOrigen;
	private static String AceptarPagoCoDiOpSucDestino;
	private static String AceptarPagoCoDiOpModulo;
	
	static {
		CoDiServicio = properties.getProperty("data_service.codi_servicio");
		
		AceptarPagoCoDiOp = properties.getProperty("codi_servicio.op.aceptar_pago_codi");
		AceptarPagoCoDiOpTransaccio = properties.getProperty("op.aceptar_pago_codi.transaccio");
		AceptarPagoCoDiOpUsuario = properties.getProperty("op.aceptar_pago_codi.usuario");
		AceptarPagoCoDiOpSucOrigen = properties.getProperty("op.aceptar_pago_codi.suc_origen");
		AceptarPagoCoDiOpSucDestino = properties.getProperty("op.aceptar_pago_codi.suc_destino");
		AceptarPagoCoDiOpModulo = properties.getProperty("op.aceptar_pago_codi.modulo");		
	}
	
	public CoDiServicio() {
		super();
	}
	
	
	/**
	 * Método para aceptar/rechazar el pago de un codigo QR en CoDi
	 * ProcedureName: SPCODIABALT
	 * @param aceptarPagoCoDiPeticion
	 * <pre> 
	 * { 
			Cod_NumTra?: String,
            Cod_TipAvi: String,
            Cod_FoEsCD: String,
            Cod_Monto: Float,
            Cod_Concep: String,
            Cod_RefNum: String,
            Cod_NumCeC: String,
            Cod_DigVeC: String,
            Cod_BancoC: String,
            Cod_TipCuC: String,
            Cod_CuentC: String,
            Cod_NumCeV: String,
            Cod_DigVeV: String,
            Cod_BancoV: String,
            Cod_TipCuV: String,
            Cod_CuentV: String,
            Cod_NombrV: String,
            NumTransac?: String
	 * }
	 * </pre>
	 * @return
	 * <pre>
	 * {
	 *	operacion: {
			Err_Codigo: String,
			Err_Mensaj: String,
			Cod_Numero: String
	 *	}
	 * }
	 * </pre>
	 */
	
	public JsonObject confirmaPagoCoDi(JsonObject aceptarPagoCoDiPeticion) {
		LOGGER.info("COMMONS: Comenzando aceptarPagoCoDi metodo...");
		if(!aceptarPagoCoDiPeticion.has("NumTransac")){
			aceptarPagoCoDiPeticion.addProperty("NumTransac", "");			
		}

		if(!aceptarPagoCoDiPeticion.has("Cod_NumTra")){
			aceptarPagoCoDiPeticion.addProperty("Cod_NumTra", "");			
		}
		
		aceptarPagoCoDiPeticion.addProperty("Transaccio", AceptarPagoCoDiOpTransaccio);
		aceptarPagoCoDiPeticion.addProperty("Usuario", AceptarPagoCoDiOpUsuario);
		aceptarPagoCoDiPeticion.addProperty("SucOrigen", AceptarPagoCoDiOpSucOrigen);
		aceptarPagoCoDiPeticion.addProperty("SucDestino", AceptarPagoCoDiOpSucDestino);
		aceptarPagoCoDiPeticion.addProperty("Modulo", AceptarPagoCoDiOpModulo);
		JsonObject resultadoObjecto = Utilerias
				.performOperacion(CoDiServicio, AceptarPagoCoDiOp, aceptarPagoCoDiPeticion);
		LOGGER.info("COMMONS: Finalizando aceptarPagoCoDi metodo...");
		return resultadoObjecto;

	}
	
}
