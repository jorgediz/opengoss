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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.opengoss.core.IPluginServiceRegistry;
import org.opengoss.core.IRegistry;
import org.opengoss.core.Registry;
import org.opengoss.core.UID;
import org.opengoss.core.internal.ServiceHolder;
import org.opengoss.core.internal.descriptor.ServiceScope;
import org.osgi.framework.BundleContext;
/**
 * Plugin level service registry.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-19
 * 
 */
public final class PluginServiceRegistry extends Registry
	implements IPluginServiceRegistry {
	
	private BundleContext context;

	/**
	 * Cache the local services, then we could get them directly in the same plugin.
	 */
	private final Map<UID, ServiceHolder> serviceCache = new HashMap<UID, ServiceHolder>(5);
	
	public PluginServiceRegistry(BundleContext context, IRegistry parent) {
		super(parent);
		this.context = context;
	}

	public BundleContext getBundleContext() {
		return context;
	}

	/**
	 * Get service by id.
	 * 
	 * @return service object.
	 */
	public Object getService(String id) {
		Object service = getPluginService(id);
		if(service == null) {
			service = ((ApplicationServiceRegistry)getParent()).getService(context, id);
		}
		return service;
	}
	/**
	 * Get service by interface.
	 * 
	 * @param intf service interface.
	 * @return service object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> intf) {
		T service = null;
		
		Collection<ServiceHolder> hodlers = serviceCache.values();
		for (ServiceHolder holder : hodlers) {
			Object obj = holder.getService();
			if(intf.isAssignableFrom(obj.getClass())) {
				service = (T)obj;
				break;
			}
		}
		
		if(service == null) {
			service = ((ApplicationServiceRegistry)getParent()).getService(context, intf); 
		}
		
		return service; 
	}

	/**
	 * Get service by id and interface.
	 * 
	 * @param id service id.
	 * @param intf service interface.
	 * @return service object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(String id, Class<T> intf) {
		UID uid = new UID(id);
		ServiceHolder holder = serviceCache.get(uid);
		if(holder != null) {
			return (T)holder.getService();
		}
		return ((ApplicationServiceRegistry)getParent()).getService(context, id, intf);
	}
	/**
	 * Get the services registered by the plugin.
	 * 
	 * @return the service objects.
	 */
	public Object[] getPluginServices() {
		ServiceHolder[] holders = serviceCache.values()
			.toArray(new ServiceHolder[serviceCache.size()]);
		Object[] services = new Object[holders.length];
		for (int i = 0; i < holders.length; i++) {
			services[i] = holders[i].getService();
		}
		return services;
	}
	/**
	 * Get plugin service by id.
	 * 
	 * @param id service id.
	 * @return service object.
	 */
	private Object getPluginService(String id) {
		UID uid = new UID(id);
		ServiceHolder holder = serviceCache.get(uid);
		return holder == null ? null : holder.getService();
	}
	/**
	 * Register a service.
	 * <p>
	 * This method is called by service builder, and not intended to be called 
	 * by other clients.
	 * </p>
	 * 
	 * @see org.opengoss.core.internal.ServiceHolder 
	 * @param holder service holder
	 * @throws Exception
	 */
	public void registerService(ServiceHolder holder) throws Exception {
		serviceCache.put(holder.getDescriptor().getUid(), holder);
		if(holder.getDescriptor().getScope().isHigher(ServiceScope.PLUGIN)) {
			((ApplicationServiceRegistry)getParent()).registerService(holder);
		}
	}
	/**
	 * Unregister a service.
	 * <p>
	 * This method is called when the plugin registry is destroyed.
	 * </p>
	 * 
	 * @see org.opengoss.core.internal.ServiceHolder
	 * @param holder service holder.
	 */
	public void unregisterService(ServiceHolder holder) throws Exception {
		if(holder.getDescriptor().getScope().isHigher(ServiceScope.PLUGIN)) {
			((ApplicationServiceRegistry)getParent()).unregisterService(holder);
		}
		serviceCache.remove(holder.getDescriptor().getUid());
	}
	/**
	 * Build the plugin registry.
	 * 
	 * <p>
	 * This method is called by {@link org.opengoss.core.PluginActivator} 
	 * when a plugin starting.
	 * 
	 * @param builder service registry builder.
	 * @throws Exception 
	 */
	public void build(ServiceRegistryBuilder builder) throws Exception {
		builder.build(this);
	}
	/**
	 * Unregister all the services registered by the plugin and destory the registry.
	 * 
	 * @throws Exception
	 */
	public void destroy() throws Exception {
		ServiceHolder[] holders = serviceCache.values().toArray(
				new ServiceHolder[serviceCache.size()]);
		for (ServiceHolder holder : holders) {
			unregisterService(holder);
		}
		//ensure to clear
		serviceCache.clear();
	}

}
