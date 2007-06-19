/*
 * Copyright 2005-2006 the original authors and www.opengoss.org community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengoss.core.internal.registry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.opengoss.core.IServiceProxy;
import org.opengoss.core.UID;
import org.opengoss.core.internal.ServiceHolder;
import org.opengoss.core.internal.descriptor.ConstructorDescriptor;
import org.opengoss.core.internal.descriptor.ParamDescriptor;
import org.opengoss.core.internal.descriptor.PluginDescriptor;
import org.opengoss.core.internal.descriptor.PropertyDescriptor;
import org.opengoss.core.internal.descriptor.ServiceDescriptor;
import org.opengoss.core.util.AssertException;
import org.opengoss.core.util.Convertor;
import org.osgi.framework.Bundle;

/**
 * Buildler of the {@link org.opengoss.core.internal.registry.PluginServiceRegistry}.
 * <p>
 * This builder is responsible to initiate, assamble and register services.
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-19
 */
public class ServiceRegistryBuilder {
	
	private Bundle bundle;
	
	private PluginDescriptor pluginDescriptor;
	
	private Map<UID, Object> serviceMap = new HashMap<UID, Object>();
	
	public ServiceRegistryBuilder(Bundle bundle, PluginDescriptor pluginDescriptor) {
		this.bundle = bundle;
		this.pluginDescriptor = pluginDescriptor;
	}
	/**
	 * Build the registry.
	 * 
	 * @param registry plugin service registry.
	 * @throws Exception
	 */
	public void build(PluginServiceRegistry registry) throws Exception {
		initiateServices();
		assembleServices(registry);
		registerServices(registry);
		serviceMap.clear();
	}
	/**
	 * Initiate services.
	 * 
	 * @throws Exception
	 */
	private void initiateServices() throws Exception {
		for (ServiceDescriptor descriptor : pluginDescriptor.getServiceDescriptors()) {
			if( !descriptor.isConstructorInjection() ) {
				UID id = descriptor.getUid();
				String name = descriptor.getClazz();
				Object service = bundle.loadClass(name).newInstance();
				serviceMap.put(id, service);
			}
		}
	}
	/**
	 * Assemble services by constructor injection and property injection.
	 * 
	 * @param registry plugin service registry.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void assembleServices(PluginServiceRegistry registry) throws Exception {
		//assemble all services
		for (ServiceDescriptor descriptor : pluginDescriptor.getServiceDescriptors()) {
			UID id = descriptor.getUid();
			Object service = serviceMap.get(id);
			if(descriptor.isConstructorInjection()) {
				ConstructorDescriptor cd = descriptor.getConstructorDescriptor();
				ParamDescriptor[] argDescriptors = cd.getParamDescriptors();
				Class clazz = bundle.loadClass(descriptor.getClazz());
				Constructor[] constructors = clazz.getConstructors();
				Constructor constructor = null;
				for (int i = 0; i < constructors.length; i++) {
					if(constructors[i].getParameterTypes().length == argDescriptors.length) {
						constructor = constructors[i];
						break;
					}
				}
				if(constructor == null) {
					throw new AssertException("No Constructor Found!");
				}
				Class[] types = constructor.getParameterTypes();
				Object[] args = new Object[argDescriptors.length];
				for (int i = 0; i < argDescriptors.length; i++) {
					if (argDescriptors[i].isUidRef()) {
						args[i] = getRefService(registry, argDescriptors[i].getRefUID());
					} else if(argDescriptors[i].isIntfRef()) {
						args[i] = getRefService(registry, argDescriptors[i].getRefIntf());
					} else {
						String str = argDescriptors[i].getValue();
						if(str.startsWith("$")) {//system property
							str = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
							str = System.getProperty(str);
						}
						if(str != null) {
							args[i] = Convertor.convert(types[i], argDescriptors[i].getValue());
						}
					}
					if(args[i] instanceof IServiceProxy) {
						args[i] = ((IServiceProxy)args[i]).getService();
					}
				}
				service = constructor.newInstance(args);
				serviceMap.put(id, service);
			}
			for (PropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
				String name = propertyDescriptor.getName();
				Object value = null;
				if (propertyDescriptor.isUidRef()) {
					UID uid = propertyDescriptor.getRefUID();
					value = getRefService(registry, uid);
				} else if(propertyDescriptor.isIntfRef()) {
					String intf = propertyDescriptor.getRefIntf();
					value = getRefService(registry, intf);
				}else {
					String str = propertyDescriptor.getValue();
					if(str.startsWith("$")) {//system property
						str = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
						str = System.getProperty(str);
					}
					if(str != null) {
						value = convert(service, name, str);
					}
				}
				if(value instanceof IServiceProxy) {
					value = ((IServiceProxy)value).getService();
				}
				if(value != null) {
					injectProperty(service, name, value);
				}
			}
		}
	}
	/**
	 * Registry services.
	 * @param registry plugin service registry.
	 */
	private void registerServices(PluginServiceRegistry registry) throws Exception {
		//register all services
		for (ServiceDescriptor descriptor : pluginDescriptor.getServiceDescriptors()) {
			ServiceHolder holder = new ServiceHolder(registry, descriptor, 
					serviceMap.get(descriptor.getUid()));
			registry.registerService(holder); 
		}
	}
	//TODO: whether to lookup "serviceMap"?
	@SuppressWarnings("unchecked")
	private Object getRefService(PluginServiceRegistry registry, String intf) 
		throws Exception {
		Class intfClass = bundle.loadClass(intf);
		return registry.getService(intfClass);
	}

	private Object getRefService(PluginServiceRegistry serviceRegistry, UID ref) {
		Object value = serviceMap.get(ref); 
		if(value == null) {
			value = serviceRegistry.getService(ref.getId());
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private Object convert(Object service, String name, String strValue) 
		throws Exception {
		Field field = service.getClass().getDeclaredField(name);
		Class clazz = field.getType();
		return Convertor.convert(clazz, strValue);
	}

	private void injectProperty(Object service, String name, Object value) 
		throws Exception {
		Field field = service.getClass().getDeclaredField(name);
		String setMethodName = getSetterMethod(name);
		Method setterMethod = service.getClass().getMethod(setMethodName, 
				new Class[]{field.getType()});
		setterMethod.invoke(service, value);
	}

	private String getSetterMethod(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
