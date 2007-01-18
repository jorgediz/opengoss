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

import org.osgi.framework.BundleContext;

/**
 * The plugin services registry. 
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 2006-11-19
 */
public interface IPluginServiceRegistry extends IRegistry {
	
	/**
	 * Get bundle context.
	 * 
	 * @return bundle context.
	 */
	public BundleContext getBundleContext();
	
	/**
	 * Get a service object by id.
	 * 
	 * @param id service id.
	 * @return service object.
	 */
	Object getService(String id);

	/**
	 * Get a service object by interface.
	 * 
	 * @param inft service interface.
	 * @return service object.
	 */
	<T> T getService(Class<T> inft);
	
	/**
	 * Get a service object by id and interface.
	 * 
	 * @param id service id.
	 * @param intf service interface.
	 * @return service object.
	 */
	<T> T getService(String id, Class<T> intf);
	
	/**
	 * Get the services that registered by this plugin.
	 * 
	 * @return services registered by this plugin.
	 */
	Object[] getPluginServices();
	
}
