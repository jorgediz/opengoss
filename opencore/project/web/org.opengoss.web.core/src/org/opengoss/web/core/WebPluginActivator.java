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
import org.opengoss.core.PluginActivator;
/**
 * Web plugin activator.
 * <p>
 * Each web plugin should provide an activator that extends this class.
 * </p>
 * <p>
 * This activator will create an instance of <code>WebPlugin</code>, and
 * add it to <code>WebContainer</code>.
 * </p>
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public abstract class WebPluginActivator extends PluginActivator {

	private IWebPlugin webPlugin = null;

	@Override
	protected void startPlugin(IPluginContext pluginContext)
			throws Exception {
		webPlugin = new WebPlugin(getClassLoader(), pluginContext);
		getWebContainer(pluginContext).add(webPlugin);
		System.out.println("WebPlugin started: " + pluginContext.getPluginURI());
	}

	protected abstract ClassLoader getClassLoader();

	@Override
	protected void stopPlugin(IPluginContext pluginContext)
			throws Exception {
		if (webPlugin != null) {
			getWebContainer(pluginContext).remove(webPlugin);
			webPlugin = null;
			System.out.println("WebPlugin stopped: " + pluginContext.getPluginURI());
		}
	}

	private IWebContainer getWebContainer(IPluginContext pluginContext) {
		return pluginContext.getServiceRegistry().getService(
				IWebContainer.class);
	}

}
