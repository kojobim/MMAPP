package com.bim.msf4j.ctrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sybase.jdbc4.jdbc.SybDriver;
import com.sybase.jdbc4.jdbc.SybResultSet;

@Path("/script")
public class MockCtrl extends BimBaseCtrl {

	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ejecutaScript(JsonObject script) {
		Connection c = null;
		JsonArray responseArray = new JsonArray();
		JsonObject response = null;
		System.out.println("EJECUTAR SCRIPTS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	try {
		
			SybDriver sybDriver = (SybDriver) Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance();
			sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_7);
			DriverManager.registerDriver(sybDriver);
			final String SQL = script.get("script").getAsString();
			boolean hasMoreResults = true;
			int getTotalCount = 0;
			boolean isResulsetQuery = 
					SQL.startsWith("exec")
					|| SQL.startsWith("EXEC")
					|| SQL.startsWith("exec ")
					|| SQL.startsWith("EXEC ")
					|| SQL.startsWith("select")
					|| SQL.startsWith("select ")
					|| SQL.startsWith("SELECT")
					|| SQL.startsWith("SELECT ")
					|| SQL.startsWith("sp_helptext ");
			try {
				System.out.println("QUERY: "+SQL.toString());
				System.out.println("IS RESULSET QUERY: "+isResulsetQuery);
				c = DriverManager.getConnection("jdbc:sybase:Tds:172.30.28.69:5000/dbBIM_Consulta", "DESA", "666666");
				PreparedStatement statement = c.prepareStatement(SQL);
				ResultSet sta = null;
				if(isResulsetQuery) {
					sta= statement.executeQuery();					
				}else {
					statement.execute();
				}

				
				if(sta != null) {
					
					if(isResulsetQuery) {
						do {
							SybResultSet rs = (SybResultSet) statement.getResultSet();
							

			                if(hasMoreResults && rs != null) {
			                	String queryText = "";
			                	while(rs.next()) {
				                	response = new JsonObject();
									ResultSetMetaData rsmd = rs.getMetaData();
					                System.out.println("TOTAL ROWS IN RS "+rsmd.getColumnCount());
					                
				                	for(int i = 1; i <= rsmd.getColumnCount(); i++) {
				                		
				                		if(SQL.startsWith("sp_helptext ") && rsmd.getColumnLabel(i).equals("text")) {
				                			queryText = queryText+rs.getObject(i).toString();
				                		}else {
					                		response.addProperty(rsmd.getColumnLabel(i), rs.getObject(i).toString());
						                	System.out.println(rsmd.getColumnLabel(i)+ " = "+rs.getObject(i).toString());				                			
				                		}
					                }
				                	
			                		if(SQL.startsWith("sp_helptext ")) {
			                			response.addProperty("text", queryText);
			                		}
			                		responseArray.add(response);
				                	
			                	}
			                }

		                	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@q@@");
		                	hasMoreResults = statement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		                	getTotalCount =statement.getUpdateCount();
		                	
		                	System.out.println("MORE RESULTS? "+hasMoreResults);
		                	System.out.println("COUNT: "+getTotalCount);
						}while(hasMoreResults || getTotalCount != -1);						
					}else {
						System.out.println("ALL FINISHED");
					}

				}

				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	System.out.println("LISTO! EJECUTADO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return Response.ok(responseArray.toString()).build();
	}
	
}
