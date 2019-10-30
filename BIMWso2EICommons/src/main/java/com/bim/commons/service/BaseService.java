package com.bim.commons.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Esta clase define las caracteristicas genericas de los servicios
 * @author Backend Team MedioMelon
 * @version BackendMM022019
 *
 */
public class BaseService {

	protected static Properties properties;
	
	public BaseService() {
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		
	}
}
