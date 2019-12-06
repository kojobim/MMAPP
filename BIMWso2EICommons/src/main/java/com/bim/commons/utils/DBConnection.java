package com.bim.commons.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {
	
	private static HikariConfig config;
	private static HikariDataSource dataSource;
	
	static {
		config = new HikariConfig(System.getenv("BIM_HOME") + "/BIMWso2EIConfig/persistence.properties");
		dataSource = new HikariDataSource(config);
	}
	
	private DBConnection() {}
	
	public static final Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
	
}