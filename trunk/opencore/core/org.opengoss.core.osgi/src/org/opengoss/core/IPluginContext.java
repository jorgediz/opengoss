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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
/**
 * A plugin's execution context that is used by the plugin to interact with 
 * the OSGi and OpenCore framework.
 * <p>
 * Get the wrapped OSGi <code>BundleContext</code> by method <code>getBundleContext()
 * </code>
 * <p>
 * Get the OpenCore {@link org.opengoss.core.IPluginServiceRegistry} by 
 * method <code>getServiceRegistry</code>
 * <p>
 * Get the OpenCore {@link org.opengoss.core.IExtensionRegistry} by
 * method <code>getExtensionRegistry</code>
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0, 2006-11-18 
 */
public interface IPluginContext {

	/**
	 * Get the plugin UID.
	 * 
	 * @return UID of the plugin.
	 */
	UID getPluginUID();
	
	/**
	 * Get the OSGi bundle context.
	 * 
	 * @return the OSGi bundle context.
	 */
	BundleContext getBundleContext();

	/**
	 * Get the plugin service registry.
	 * 
	 * @return the plugin service registry.
	 */
	IPluginServiceRegistry getServiceRegistry();

	/**
	 * Get the plugin extension registry.
	 * 
	 * @return the plugin extension registry.
	 */
	IExtensionRegistry getExtensionRegistry();

	/**
	 * Get the bundle by its symbol id.
	 * 
	 * @param symboleId plugin's symbol id
	 * @return bundle
	 */
	Bundle getBundleBySymbolId(String symbolId);

	/** 
	 * Get the web bundle's URI.
	 * 
	 * @return web bundle's URI
	 */
	String getPluginURI();

}