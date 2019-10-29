package com.bim.msf4j.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.internal.DataHolder;
import org.wso2.msf4j.internal.MSF4JConstants;
import org.wso2.msf4j.internal.MicroservicesRegistryImpl;

import com.bim.msf4j.exceptions.BimExceptionMapper;

public class BimBaseCtrl implements Microservice {

	protected static Properties properties;
	protected static String SaldoServicio;
	protected static String ClienteServicio;
	protected static String TransaccionServicio;
	protected static String BitacoraServicio;
	protected static String MovimientosServicio;
	protected static String UsuarioServicio;
	protected static String ConfiguracionServicio;
	protected static String TokenServicio;

	public BimBaseCtrl() {
		Map<String, MicroservicesRegistryImpl> microserviceRegistryMap = DataHolder.getInstance().getMicroservicesRegistries();
		MicroservicesRegistryImpl microservicesRegistryImpl = microserviceRegistryMap.get(MSF4JConstants.CHANNEL_ID);
		
		if (microservicesRegistryImpl != null) {
			addResourceToRegistry(microservicesRegistryImpl);
        } else {
        	microserviceRegistryMap.values().forEach(this::addResourceToRegistry);
        }
		
		try (InputStream inputStream = new FileInputStream(System.getenv("BIM_HOME")+"/BIMWso2EIConfig/services.properties")) {
			properties = new Properties();
			
			if(inputStream != null) {
				properties.load(inputStream);
			}			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}

		SaldoServicio = properties.getProperty("data_service.saldo_servicio");
		ClienteServicio = properties.getProperty("data_service.cliente_servicio");
		TransaccionServicio = properties.getProperty("data_service.transaccion_servicio");
		BitacoraServicio = properties.getProperty("data_service.bitacora_servicio");
		MovimientosServicio = properties.getProperty("data_service.movimientos_servicio");
		UsuarioServicio = properties.getProperty("data_service.usuario_servicio");
		ConfiguracionServicio = properties.getProperty("data_service.configuracion_servicio");
		TokenServicio = properties.getProperty("data_service.token_servicio");
		
	}
	
	private void addResourceToRegistry(MicroservicesRegistryImpl microservicesRegistryImpl) {
		microservicesRegistryImpl.addExceptionMapper(new BimExceptionMapper());
	}
}
