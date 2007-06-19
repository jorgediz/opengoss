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
package org.opengoss.core;

import org.opengoss.core.internal.descriptor.PluginDescriptor;
import org.opengoss.core.internal.registry.PluginExtensionRegistry;
import org.opengoss.core.internal.registry.PluginServiceRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * Plugin context.
 * <p>
 * The context wraps OSGi bundle context, service registry and 
 * extension registry.
 * <p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public final class PluginContext implements IPluginContext {

	private final BundleContext bundleContext;

	private final PluginDescriptor pluginDescriptor;

	private PluginServiceRegistry serviceRegistry;

	private PluginExtensionRegistry extensionRegistry;
	/**
	 * Create a plugin context with plugin descriptor and OSGi bundle context.
	 * 
	 * @param descriptor plugin descriptor
	 * @param context OSGi bundle context
	 */
	public PluginContext(PluginDescriptor descriptor, BundleContext context) {
		this.pluginDescriptor = descriptor;
		this.bundleContext = context;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IPluginContext#getPluginUID()
	 */
	public UID getPluginUID() {
		return pluginDescriptor.getUid();
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IPluginContext#getBundleContext()
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IPluginContext#getServiceRegistry()
	 */
	public IPluginServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IPluginContext#getExtensionRegistry()
	 */
	public IExtensionRegistry getExtensionRegistry() {
		return extensionRegistry;
	}
	/**
	 * Inject the plugin level service registry.
	 * 
	 * @param serviceRegistry plugin service registry.
	 */
	public void setServiceRegistry(PluginServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	/**
	 * Inject the plugin level extension registry.
	 * 
	 * @param extensionRegistry plugin extension registry.
	 */
	public void setExtensionRegistry(PluginExtensionRegistry extensionRegistry) {
		this.extensionRegistry = extensionRegistry;
	}
	
	public Bundle getBundleBySymbolId(String symbolId) {
		Bundle[] installedBundles = bundleContext.getBundles();
		for (Bundle bundle : installedBundles) {
			if(bundle.getSymbolicName().equals(symbolId)) {
				return bundle;
			}
		}
		return null;
	}
	
	public String getPluginURI() {
		String uri = pluginDescriptor.getUri();
		if(uri == null) {
			uri = pluginDescriptor.getUid().toURI();
		}
		return uri;
	}

}
