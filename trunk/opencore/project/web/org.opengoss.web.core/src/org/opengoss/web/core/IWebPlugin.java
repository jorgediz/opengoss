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
package org.opengoss.web.core;

import org.opengoss.core.IPluginContext;
/**
 * Web plugins are parts of a web application. One web plugin is a OpenCore plugin
 * (or OSGi bundle) adding web features..
 * <p>
 * A web plugin when activating will be adding to a web container.
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IWebPlugin {
	/**
	 * Get the context path of the web plugin.
	 * 
	 * @return context path of the web plugin
	 */
	String getContextPath();
	/**
	 * OSGi class loader of this plugin.
	 * @return class loader of this plugin.
	 */
	ClassLoader getClassLoader();
	/**
	 * Get the wrapped plugin context.
	 * 
	 * @return plugin context.
	 */
	IPluginContext getContext();

}
