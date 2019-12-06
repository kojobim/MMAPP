package com.bim.commons.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.utils.DBConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TransferenciasBIMDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(TransferenciasBIMDAO.class);
	
	private JsonArray ejecutarConsulta(CallableStatement cstmt) {
		JsonArray resultadoArreglo = null;
	    try {
	    	boolean resultadoCon = cstmt.execute();
	    	resultadoArreglo = new JsonArray();
		    
	    	while (true) {
		    	int contadorAct = cstmt.getUpdateCount();
		    	logger.info("@@ resultadoCon >>>>> " + resultadoCon);
		    	logger.info("@@ contadorAct >>>>> " + contadorAct);
		    	ResultSet rs = cstmt.getResultSet();
	    		if(resultadoCon) {
					ResultSetMetaData rsmd = rs.getMetaData();
					JsonArray rsArreglo = new JsonArray();
					if(rs != null) {
						while(rs.next()) {
							int numColumnas = rsmd.getColumnCount();
							JsonObject rsObjeto = new JsonObject();
							for(int i = 1; i <= numColumnas; i++) {
								try {
									String nombreColumna = rsmd.getColumnLabel(i);
									rsObjeto.addProperty(nombreColumna, rs.getObject(nombreColumna).toString());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							logger.info("@@@ rsObjeto >>>>>" + rsObjeto);
							rsArreglo.add(rsObjeto);
						}
					}
					resultadoArreglo.add(rsArreglo);
				}		    		
		    	
		    	if(!resultadoCon && contadorAct == -1)
		    		break;
		    	
		    	resultadoCon = cstmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		    }	    	
	    	cstmt.getMoreResults(Statement.CLOSE_ALL_RESULTS);
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return resultadoArreglo;
	}
	
	public JsonObject transferenciasBIMConsultar(JsonObject datosTransferenciasBIMConsultar) {
		logger.info("DAO: Comenzando transferenciasBIMConsultar...");
		
		String SQL = "exec NBTRABANCON ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";		
		JsonObject resultado = null;
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cstmt = conn.prepareCall(SQL);) {
				
				cstmt.setString(1, datosTransferenciasBIMConsultar.get("Trb_UsuAdm").getAsString());
				cstmt.setString(2, datosTransferenciasBIMConsultar.get("Trb_Usuari").getAsString());
				cstmt.setString(3, datosTransferenciasBIMConsultar.get("Trb_Client").getAsString());
				cstmt.setString(4, datosTransferenciasBIMConsultar.get("Trb_Consec").getAsString());
				cstmt.setString(5, datosTransferenciasBIMConsultar.get("Trb_Status").getAsString());
				cstmt.setString(6, datosTransferenciasBIMConsultar.get("Trb_TipTra").getAsString());
				cstmt.setString(7, datosTransferenciasBIMConsultar.get("Tip_Consul").getAsString());			
				cstmt.setString(8, datosTransferenciasBIMConsultar.get("NumTransac").getAsString());
				cstmt.setString(9, datosTransferenciasBIMConsultar.get("Transaccio").getAsString());
				cstmt.setString(10, datosTransferenciasBIMConsultar.get("Usuario").getAsString());
				cstmt.setString(11, datosTransferenciasBIMConsultar.get("FechaSis").getAsString());
				cstmt.setString(12, datosTransferenciasBIMConsultar.get("SucOrigen").getAsString());
				cstmt.setString(13, datosTransferenciasBIMConsultar.get("SucDestino").getAsString());
				cstmt.setString(14, datosTransferenciasBIMConsultar.get("Modulo").getAsString());
				
			    JsonArray transferenciasBIMArreglo = ejecutarConsulta(cstmt);
			    
			    if(transferenciasBIMArreglo != null) {
				    resultado = new JsonObject();
		    		JsonObject transferenciasBIMObjeto = new JsonObject();
		    		transferenciasBIMObjeto.add("transferenciaBIM", transferenciasBIMArreglo);
		    		resultado.add("transferenciasBIM", transferenciasBIMObjeto);
			    }
			    logger.info("@@@@ Resultado >>>>> " + resultado);				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logger.info("DAO: Terminando transferenciasBIMConsultar...");
		return resultado;
	}
	
	public JsonObject transferenciaBIMFirmasConsultar(JsonObject datosTransferenciaBIMFirmasConsultar) {
		logger.info("DAO: Comenzando transferenciaBIMFirmasConsultar...");
		
		String SQL = "exec NBFITRBRCON ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";	
		JsonObject resultado = null;
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cstmt = conn.prepareCall(SQL);) {
				
				cstmt.setString(1, datosTransferenciaBIMFirmasConsultar.get("Ftb_Valida").getAsString());
				cstmt.setString(2, datosTransferenciaBIMFirmasConsultar.get("Ftb_Usuari").getAsString());
				cstmt.setString(3, datosTransferenciaBIMFirmasConsultar.get("Ftb_Client").getAsString());
				cstmt.setString(4, datosTransferenciaBIMFirmasConsultar.get("Ftb_Consec").getAsString());
				cstmt.setString(5, datosTransferenciaBIMFirmasConsultar.get("Tip_Consul").getAsString());			
				cstmt.setString(6, datosTransferenciaBIMFirmasConsultar.get("NumTransac").getAsString());
				cstmt.setString(7, datosTransferenciaBIMFirmasConsultar.get("Transaccio").getAsString());
				cstmt.setString(8, datosTransferenciaBIMFirmasConsultar.get("Usuario").getAsString());
				cstmt.setString(9, datosTransferenciaBIMFirmasConsultar.get("FechaSis").getAsString());
				cstmt.setString(10, datosTransferenciaBIMFirmasConsultar.get("SucOrigen").getAsString());
				cstmt.setString(11, datosTransferenciaBIMFirmasConsultar.get("SucDestino").getAsString());
				cstmt.setString(12, datosTransferenciaBIMFirmasConsultar.get("Modulo").getAsString());
				
				JsonArray transferenciaBIMFirmasArreglo = ejecutarConsulta(cstmt);
				
				if(transferenciaBIMFirmasArreglo != null) {
					resultado = new JsonObject();
		    		JsonObject transferenciasBIMObjeto = new JsonObject();
		    		transferenciasBIMObjeto.add("transferenciaBIMFirmas", transferenciaBIMFirmasArreglo);
		    		resultado.add("transferenciasBIMFirmas", transferenciasBIMObjeto);
				}			    
			    logger.info("@@@@ Resultado >>>>> " + resultado);				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logger.info("DAO: Terminando transferenciaBIMFirmasConsultar...");
		return resultado;
	}
	
	public JsonObject transferenciaBIMProcesar(JsonObject datosTransferenciaBIMProcesar) {
		logger.info("DAO: Comenzando transferenciaBIMProcesar...");
		
		String SQL = "exec NBTRABANPRO ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";	
		JsonObject resultado = null;
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cstmt = conn.prepareCall(SQL);) {
				
				cstmt.setString(1, datosTransferenciaBIMProcesar.get("Trb_UsuAdm").getAsString());
				cstmt.setString(2, datosTransferenciaBIMProcesar.get("Trb_Usuari").getAsString());
				cstmt.setString(3, datosTransferenciaBIMProcesar.get("Trb_Client").getAsString());
				cstmt.setString(4, datosTransferenciaBIMProcesar.get("Trb_Consec").getAsString());
				cstmt.setString(5, datosTransferenciaBIMProcesar.get("Trb_CueOri").getAsString());				
				cstmt.setString(6, datosTransferenciaBIMProcesar.get("Trb_CueDes").getAsString());
				cstmt.setDouble(7, datosTransferenciaBIMProcesar.get("Trb_Monto").getAsDouble());
				cstmt.setString(8, datosTransferenciaBIMProcesar.get("Trb_RFC").getAsString());
				cstmt.setDouble(9, datosTransferenciaBIMProcesar.get("Trb_IVA").getAsDouble());
				cstmt.setString(10, datosTransferenciaBIMProcesar.get("Trb_SegRef").getAsString());
				cstmt.setString(11, datosTransferenciaBIMProcesar.get("Trb_DireIP").getAsString());
				cstmt.setString(12, datosTransferenciaBIMProcesar.get("Trb_TipTra").getAsString());
				cstmt.setString(13, datosTransferenciaBIMProcesar.get("Trb_ValFir").getAsString());
				cstmt.setString(14, datosTransferenciaBIMProcesar.get("NumTransac").getAsString());
				cstmt.setString(15, datosTransferenciaBIMProcesar.get("Transaccio").getAsString());
				cstmt.setString(16, datosTransferenciaBIMProcesar.get("Usuario").getAsString());
				cstmt.setString(17, datosTransferenciaBIMProcesar.get("FechaSis").getAsString());
				cstmt.setString(18, datosTransferenciaBIMProcesar.get("SucOrigen").getAsString());
				cstmt.setString(19, datosTransferenciaBIMProcesar.get("SucDestino").getAsString());
				cstmt.setString(20, datosTransferenciaBIMProcesar.get("Modulo").getAsString());
				
				JsonArray transferenciaBIMProcesarArreglo = ejecutarConsulta(cstmt);
				
				if(transferenciaBIMProcesarArreglo != null) {
					resultado = new JsonObject();
		    		JsonObject transferenciaBIMProcesarObjeto = new JsonObject();
		    		transferenciaBIMProcesarObjeto.add("transferenciaBIM", transferenciaBIMProcesarArreglo);
		    		resultado.add("transferenciasBIM", transferenciaBIMProcesarObjeto);
				}			    
			    logger.info("@@@@ Resultado >>>>> " + resultado);				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logger.info("DAO: Terminando transferenciaBIMProcesar...");
		return resultado;
	}

}
