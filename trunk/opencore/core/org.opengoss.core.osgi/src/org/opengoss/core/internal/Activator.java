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
package org.opengoss.core.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.opengoss.core.IApplicationExtensionRegistry;
import org.opengoss.core.IApplicationServiceRegistry;
import org.opengoss.core.internal.descriptor.Constants;
import org.opengoss.core.internal.descriptor.ServiceScope;
import org.opengoss.core.internal.registry.ApplicationExtensionRegistry;
import org.opengoss.core.internal.registry.ApplicationServiceRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The OSGi activator of this core bundle.
 * 
 * @author Ery Lee(licc@alleninfo.com.cn)
 */
public class Activator implements BundleActivator {

	ServiceRegistration asrRegistration = null;

	ServiceRegistration aerRegistration = null;

	public void start(BundleContext context) throws Exception {
		Dictionary<String, String> props = new Hashtable<String, String>(1);
		props.put(Constants.SCOPE, ServiceScope.APPLICATION.toString());
		props.put("uid", "Core:ApplicationServiceRegistry");
		asrRegistration = context.registerService(IApplicationServiceRegistry.class
				.getName(), new ApplicationServiceRegistry(), props);
		props.put("uid", "Core:ApplicationExtensionRegistry");
		aerRegistration = context.registerService(IApplicationExtensionRegistry.class
				.getName(), new ApplicationExtensionRegistry(), props);
	}

	public void stop(BundleContext context) throws Exception {
		aerRegistration.unregister();
		asrRegistration.unregister();
	}

}
