package com.bim.commons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bim.commons.dto.BimMessageDTO;
import com.bim.commons.exceptions.InternalServerException;
import com.bim.commons.utils.DBConnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ResultSetDAO {

	private static final Logger logger = LoggerFactory.getLogger(ResultSetDAO.class);

	public JsonObject resultSet(JsonObject datos, String procedureName, String nombreObjeto, String nombreArreglo) {
		logger.info("DAO: Comenzando resultSet metodo...");
		Map<String, Object> datosMap = null;
		ArrayList<Object> datosArray = null;
		int index = 0;
		JsonObject resultado = null;
		
		if(datos != null && !datos.isJsonNull()){
			datosMap = new Gson().fromJson(datos, Map.class);    		
			datosArray = new ArrayList<>();
    	}

		StringBuilder query = new StringBuilder()
				.append("exec ")
				.append(procedureName).append(" ");
		
		for (Entry<String, Object> dato : datosMap.entrySet()){
			String clave = dato.getKey();
			
			if(!clave.contains("@")) {
				clave = "@"+clave;
			}
			query.append(clave).append(" = ").append("?");
			++index;
			
			if(index < datosMap.size()) {
				query.append(", ");
			}
			datosArray.add(dato.getValue());
		}
		
		Connection conn = null;
		PreparedStatement statement = null;
		
		try {
			conn = DBConnection.getConnection();
			statement = conn.prepareStatement(query.toString());

			for(int i = 0; i < datosArray.size(); i++) {
				statement.setObject(i+1, datosArray.get(i));
			}
			
			boolean resultadoCon = statement.execute();
			JsonArray jsonArray = new JsonArray();

			while (true) {
				int contadorAct = statement.getUpdateCount();
				logger.debug("resultadoCon >>>>> " + resultadoCon);
				logger.debug("contadorAct >>>>> " + contadorAct);
				ResultSet rs = statement.getResultSet();
				if (resultadoCon) {
					ResultSetMetaData rsmd = rs.getMetaData();
					JsonArray rsArreglo = new JsonArray();
					if (rs != null) {
						while (rs.next()) {
							int numColumnas = rsmd.getColumnCount();
							JsonObject rsObjeto = new JsonObject();
							for (int i = 1; i <= numColumnas; i++) {
								String nombreColumna = rsmd.getColumnLabel(i);
								String valorColumna = null;
							
								try {
									valorColumna = rs.getObject(nombreColumna).toString();
								} catch (Exception e) {
									e.printStackTrace();
								}
								rsObjeto.addProperty(nombreColumna, valorColumna != null ? valorColumna : "");
							}
							logger.debug("rsObjeto >>>>>" + rsObjeto);
							rsArreglo.add(rsObjeto);
						}
					}
					jsonArray.add(rsArreglo);
				}

				if (!resultadoCon && contadorAct == -1)
					break;

				resultadoCon = statement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
			}
			resultado = new JsonObject();
			JsonObject jsonObjeto = new JsonObject();
			jsonObjeto.add(nombreArreglo, jsonArray);
			resultado.add(nombreObjeto, jsonObjeto);

			logger.debug("Resultado >>>>> " + resultado);
			statement.getMoreResults(Statement.CLOSE_ALL_RESULTS);
			statement.close();

		} catch (SQLException e) {
			BimMessageDTO bimMessageDTO = new BimMessageDTO("COMMONS.500");
			throw new InternalServerException(bimMessageDTO.toString());
		} finally {
			if(conn != null) {
				try {
					if(!conn.isClosed())
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		logger.info("DAO: Terminando resultSet metodo...");
		return resultado;
	}
}
