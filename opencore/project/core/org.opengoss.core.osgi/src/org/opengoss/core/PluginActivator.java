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

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.core.internal.descriptor.Constants;
import org.opengoss.core.internal.descriptor.PluginDescriptor;
import org.opengoss.core.internal.descriptor.ServiceScope;
import org.opengoss.core.internal.descriptor.XmlParser;
import org.opengoss.core.internal.registry.ExtensionRegistryBuilder;
import org.opengoss.core.internal.registry.PluginExtensionRegistry;
import org.opengoss.core.internal.registry.PluginServiceRegistry;
import org.opengoss.core.internal.registry.ServiceRegistryBuilder;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
/**
 * Plugin activator.
 * <p>
 * A implementation of OSGi <code>BundleActivator</code> is responsible to:
 * <ul>
 * 	<li>Parse "plugin.xml" </li>
 * 	<li>Construct a <code>PluginServiceRegistry</code>
 * 	<li>Construct a <code>PluginExtensionRegistry</code>
 * 	<li>Construct a <code>PluginContext</code>
 * 	<li>Start or stop the stable services.
 * </ul> 
 * </p>
 * <p>
 * A plugin built on OpenCore could provide an activator that extends this 
 * class. Sample code as below:
 * <code>
 * public class AnActivator extends PluginActivator {
 *     protected void startPlugin(IPluginContext pluginContext) throws Exception {
 *     }
 *     protected void stopPlugin(IPluginContext pluginContext) throws Exception {
 *     }
 * }
 * </code>
 * </p>
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public abstract class PluginActivator implements BundleActivator {

	static final Log log = LogFactory.getLog(PluginActivator.class);
	
	protected IPluginContext pluginContext = null;

	public void start(BundleContext context) throws Exception {
		//Parse the xml configuration file
		URL xml = context.getBundle().getEntry("plugin.xml");
		if(xml == null) {
			log.error("No plugin.xml found!");
			return;
		}
		PluginDescriptor pluginDescriptor = XmlParser.parse(xml);
		
		//Build plugin service registry
		IApplicationServiceRegistry appServiceRegistry = getService(context, 
				IApplicationServiceRegistry.class);
		PluginServiceRegistry serviceRegistry = new PluginServiceRegistry(context, 
				appServiceRegistry);
		serviceRegistry.build(new ServiceRegistryBuilder(context.getBundle(),
				pluginDescriptor));
		
		//Build plugin extension registry
		IExtensionRegistry appExtensionRegistry = getService(context, 
				IApplicationExtensionRegistry.class);
		PluginExtensionRegistry extensionRegistry = new PluginExtensionRegistry(context, 
				appExtensionRegistry);
		extensionRegistry.setServiceRegistry(serviceRegistry);
		extensionRegistry.build(new ExtensionRegistryBuilder(pluginDescriptor));
		
		//Create plugin context
		pluginContext = new PluginContext(pluginDescriptor, context);
		((PluginContext)pluginContext).setExtensionRegistry(extensionRegistry);
		((PluginContext)pluginContext).setServiceRegistry(serviceRegistry);
		
		//Start services
		startServices(pluginContext);
		
		//Subclass to customize the startup process
		startPlugin(pluginContext);
	}

	private void startServices(IPluginContext pluginContext) 
		throws Exception {
		Object[] services = pluginContext.getServiceRegistry()
			.getPluginServices();
		for (Object service : services) {
			if(service instanceof IPluginContextAware) {
				((IPluginContextAware)service).setPluginContext(pluginContext);
			}
			if(service instanceof IInitializable) {
				((IInitializable)service).init();
			}
			if(service instanceof IStartable) {
				((IStartable)service).start();
			}
		}
	}

	/**
	 * Clients should override this method to customize the startup process.
	 * 
	 * @param pluginContext plugin context.
	 * @throws Exception
	 */
	protected abstract void startPlugin(IPluginContext pluginContext) throws Exception;

	public void stop(BundleContext context) throws Exception {
		if(pluginContext == null) {
			log.warn("Plugin is still not started.");
			return;
		}
		
		//Subclass to customize the stop process
		stopPlugin(pluginContext);
		
		//Stop services
		stopServices(pluginContext);
		
		//Destory registries
		((PluginServiceRegistry)pluginContext.getServiceRegistry()).destroy();
		((PluginExtensionRegistry)pluginContext.getExtensionRegistry()).destroy();
		pluginContext = null;
	}

	/**
	 * Clients should override this method to customize the stopping process.
	 * 
	 * @param pluginContext plugin context
	 * @throws Exception
	 */
	protected abstract void stopPlugin(IPluginContext pluginContext) throws Exception;

	private void stopServices(IPluginContext pluginContext) 
		throws Exception {
		Object[] services = pluginContext.getServiceRegistry().getPluginServices();
		for (Object service : services) {
			if(service instanceof IStartable) {
				((IStartable)service).stop();
			}
			if(service instanceof IInitializable) {
				((IInitializable)service).destory();
			}
			if(service instanceof IPluginContextAware) {
				((IPluginContextAware)service).setPluginContext(null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T getService(BundleContext context, Class<T> clazz) throws Exception {
		String props = "(" + Constants.SCOPE + "=" + ServiceScope.APPLICATION.toString() + ")";
		ServiceReference[] refs = context.getServiceReferences(clazz.getName(), props);
		if(refs == null || refs.length == 0) {
			throw new Exception("No Service Found: " + clazz);
		}
		if(refs.length > 1) {
			log.warn("More than one service found: " + clazz);
		}
		return (T)context.getService(refs[0]);
	}

}
