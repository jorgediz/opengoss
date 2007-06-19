package org.opengoss.core.rmi;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;

public class RmiService<T> {
		
	private T target;
	
	@SuppressWarnings("unchecked")
	public RmiService(String serviceUrl, Class<T> serviceInterface) {
		RmiProxyFactoryBean proxyFactoryBean=new RmiProxyFactoryBean();
		proxyFactoryBean.setServiceUrl(serviceUrl);
		proxyFactoryBean.setServiceInterface(serviceInterface);
		try {
			proxyFactoryBean.afterPropertiesSet();
			target=(T) proxyFactoryBean.getObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public T getService(){
		return target;
	}

}
