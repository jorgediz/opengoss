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
package org.opengoss.web.internal.jetty;

import org.opengoss.core.IPluginContext;
import org.opengoss.core.PluginActivator;
import org.osgi.framework.BundleContext;
/**
 * Plugin Activator.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class Activator extends PluginActivator {
	
	//TODO: DEBUG
	public static BundleContext bundleContext;

	@Override
	protected void startPlugin(IPluginContext pluginContext)
			throws Exception {
		bundleContext = pluginContext.getBundleContext();
	}

	@Override
	protected void stopPlugin(IPluginContext pluginContext)
			throws Exception {
		bundleContext = null;
	}

}
