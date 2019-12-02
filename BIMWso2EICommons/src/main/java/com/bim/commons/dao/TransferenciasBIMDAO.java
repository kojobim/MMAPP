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
				
			    boolean resultadoCon = cstmt.execute();
			    JsonArray transferenciasBIMArreglo = new JsonArray();
			    
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
									String nombreColumna = rsmd.getColumnName(i);
									rsObjeto.addProperty(nombreColumna, rs.getObject(nombreColumna).toString());
								}
								logger.info("@@@ rsObjeto >>>>>" + rsObjeto);
								rsArreglo.add(rsObjeto);
							}
						}
						transferenciasBIMArreglo.add(rsArreglo);
					}		    		
			    	
			    	if(!resultadoCon && contadorAct == -1)
			    		break;
			    	
			    	resultadoCon = cstmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
			    }			    
			    resultado = new JsonObject();
	    		JsonObject transferenciasBIMObjeto = new JsonObject();
	    		transferenciasBIMObjeto.add("transferenciaBIM", transferenciasBIMArreglo);
	    		resultado.add("transferenciasBIM", transferenciasBIMObjeto);
			    
			    logger.info("@@@@ Resultado >>>>> " + resultado);
				cstmt.getMoreResults(Statement.CLOSE_ALL_RESULTS);
				cstmt.close();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logger.info("DAO: Terminando transferenciasBIMConsultar...");
		return resultado;
	}

}
