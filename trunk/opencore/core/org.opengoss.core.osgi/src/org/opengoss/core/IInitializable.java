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

/**
 * A service that need to be initialized and destroyed should implement 
 * this interface.
 * 
 * <p>
 * When a plugin(bundle) is activated by {@link org.opengoss.core.PluginActivator},
 * the initializable service could be initialized. When the plugin deactivated, 
 * the service could be destroyed.
 * </p> 
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0, 2006-11-18 
 */
public interface IInitializable {
	
	/**
	 * Initialize the service.
	 * 
	 * @throws Exception error occurs when intializing.
	 */
	void init() throws Exception;
	
	/**
	 * Destroy the service.
	 * 
	 * @throws Exception error occurs when destroying.
	 */
	void destory() throws Exception;

}
