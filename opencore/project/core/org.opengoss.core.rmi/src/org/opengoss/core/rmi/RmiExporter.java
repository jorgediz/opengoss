package org.opengoss.core.rmi;

import org.springframework.remoting.rmi.RmiServiceExporter;

public class RmiExporter {
	
	public RmiExporter(String serviceName,Class<?> serviceInterface,Object target,int port){
		RmiServiceExporter exporter=new RmiServiceExporter();
		exporter.setServiceName(serviceName);
		exporter.setService(target);
		exporter.setServiceInterface(serviceInterface);
		exporter.setRegistryPort(port);
		try {
			exporter.afterPropertiesSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
