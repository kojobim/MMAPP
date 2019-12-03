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

public class TransferenciasNacionalesDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(TransferenciasNacionalesDAO.class);
	
	public JsonObject transferenciasNacionalesConsultar(JsonObject datosTransferenciasNacionalesConsultar) {
		logger.info("DAO: Comenzando transferenciasNacionalesConsultar...");
		
		String SQL = "exec NBTRANACCON ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";		
		JsonObject resultado = null;
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cstmt = conn.prepareCall(SQL);) {
				
				cstmt.setString(1, datosTransferenciasNacionalesConsultar.get("Trn_UsuAdm").getAsString());
				cstmt.setString(2, datosTransferenciasNacionalesConsultar.get("Trn_Usuari").getAsString());
				cstmt.setString(3, datosTransferenciasNacionalesConsultar.get("Trn_Client").getAsString());
				cstmt.setString(4, datosTransferenciasNacionalesConsultar.get("Trn_Consec").getAsString());
				cstmt.setString(5, datosTransferenciasNacionalesConsultar.get("Trn_Status").getAsString());
				cstmt.setString(6, datosTransferenciasNacionalesConsultar.get("Trn_Transf").getAsString());
				cstmt.setString(7, datosTransferenciasNacionalesConsultar.get("Tip_Consul").getAsString());			
				cstmt.setString(8, datosTransferenciasNacionalesConsultar.get("NumTransac").getAsString());
				cstmt.setString(9, datosTransferenciasNacionalesConsultar.get("Transaccio").getAsString());
				cstmt.setString(10, datosTransferenciasNacionalesConsultar.get("Usuario").getAsString());
				cstmt.setString(11, datosTransferenciasNacionalesConsultar.get("FechaSis").getAsString());
				cstmt.setString(12, datosTransferenciasNacionalesConsultar.get("SucOrigen").getAsString());
				cstmt.setString(13, datosTransferenciasNacionalesConsultar.get("SucDestino").getAsString());
				cstmt.setString(14, datosTransferenciasNacionalesConsultar.get("Modulo").getAsString());

			    boolean resultadoCon = cstmt.execute();
			    JsonArray transferenciasNacionalesArreglo = new JsonArray();
			    
			    while (true) {
			    	int contadorAct = cstmt.getUpdateCount();
			    	logger.debug("resultadoCon >>>>> " + resultadoCon);
			    	logger.debug("contadorAct >>>>> " + contadorAct);
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
								logger.debug("rsObjeto >>>>>" + rsObjeto);
								rsArreglo.add(rsObjeto);
							}
						}
						transferenciasNacionalesArreglo.add(rsArreglo);
					}		    		
		    		
			    	if(!resultadoCon && contadorAct == -1)
			    		break;
			    	
			    	resultadoCon = cstmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
			    }			    
			    resultado = new JsonObject();
	    		JsonObject transferenciasNacionalesObjeto = new JsonObject();
	    		transferenciasNacionalesObjeto.add("transferenciaNacional", transferenciasNacionalesArreglo);
	    		resultado.add("transferenciasNacionales", transferenciasNacionalesObjeto);
			    
			    logger.debug("Resultado >>>>> " + resultado);
				cstmt.getMoreResults(Statement.CLOSE_ALL_RESULTS);
				cstmt.close();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logger.info("DAO: Terminando transferenciasNacionalesConsultar...");
		return resultado;
	}

}
