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

import java.rmi.Remote;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.core.INetworkServiceRegistry;
import org.opengoss.core.IRegistry;
import org.opengoss.core.IApplicationServiceRegistry;
import org.opengoss.core.Registry;
import org.opengoss.core.UID;
import org.opengoss.core.internal.ServiceHolder;
import org.opengoss.core.internal.descriptor.Constants;
import org.opengoss.core.internal.descriptor.ServiceDescriptor;
import org.opengoss.core.internal.descriptor.ServiceScope;
import org.opengoss.core.util.AssertException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Application level service registry that wraps OSGi service registry.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-19
 */
public class ApplicationServiceRegistry extends Registry 
	implements IApplicationServiceRegistry {
	
	static final String APPLICATION_NAME = "org.opengoss.core.application";
	
	static final String DEFAULT_APPLICATION = "Default";

	static final Log log = LogFactory.getLog(ApplicationServiceRegistry.class);
	
	private String application;

	private Map<UID, ServiceRegistration> cache = new HashMap<UID, ServiceRegistration>();

	public ApplicationServiceRegistry() {
		this(null);
	}
	
	public ApplicationServiceRegistry(IRegistry parent) {
		super(parent);
		this.application = System.getProperty(APPLICATION_NAME);
		if(this.application == null) {
			this.application = DEFAULT_APPLICATION;
		}
	}

	public Object getService(BundleContext context, String id) {
		return getService(context, id, null);
	}

	public <T> T getService(BundleContext context, Class<T> intf) {
		return getService(context, null, intf);
	}

	public <T> T getService(BundleContext context, String id, Class<T> intf) {
		T service = getFromOSGi(context, id, intf);
		if(service == null) {
			if(getParent() != null) {
				service = (T)((INetworkServiceRegistry)getParent()).getService(id, intf);
			}
		}
		return service;
	}

	public void registerService(ServiceHolder holder) throws Exception {
		//register service in osgi.
		ServiceDescriptor descriptor = holder.getDescriptor();
		UID serviceUid = descriptor.getGlobalUid();
		Dictionary<String, String> props = serviceUid.toDictionary();
		props.put(Constants.SCOPE, descriptor.getScope().toString());
		ServiceRegistration reg = ((PluginServiceRegistry)holder.getRegistry())
			.getBundleContext().registerService(descriptor.getInterfaces(), holder.getService(), props);
		cache.put(serviceUid, reg);
		//register service to network registry.
		if (descriptor.getScope() == (ServiceScope.NETWORK)) {
			((INetworkServiceRegistry) getParent()).register(application + ":"
					+ serviceUid.getId(), (Remote) holder.getService());
		}
	}

	public void unregisterService(ServiceHolder holder) throws Exception {
		UID uid = holder.getDescriptor().getGlobalUid();
		//unregister from network registry.
		if(holder.getDescriptor().getScope() == (ServiceScope.NETWORK)) {
			((INetworkServiceRegistry)getParent()).unregister(application + ":" + uid.getId());
		}
		//unregister from osgi.
		ServiceRegistration reg = cache.remove(uid);
		if (reg != null) {
			reg.unregister();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T getFromOSGi(BundleContext context, String id, Class<T> intf) {
		T service = null;
		String filter = null;
		if(id != null) {
			filter = new UID(id).toFilter();
		}
		String intfName = null;
		if(intf != null) {
			intfName = intf.getName();
		}
		try {
			ServiceReference[] refs = context.getServiceReferences(intfName, filter);
			if(refs == null || refs.length == 0) {
				log.warn("No service found in OSGi! id= " 
						+ id + ", filter=" + filter);
			} else if(refs.length > 1) {
				log.warn("More than one services found in OSGi! id= " 
						+ id + ", filter=" + filter);
			} else { //refs.length == 1
				service = (T)context.getService(refs[0]);
			}
			return service; 
		} catch (InvalidSyntaxException e) {
			throw new AssertException("!Invalid filter syntax: " + filter, e);
		}
		
	}

}
