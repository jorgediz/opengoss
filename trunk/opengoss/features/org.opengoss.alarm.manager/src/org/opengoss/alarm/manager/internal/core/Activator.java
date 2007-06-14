package org.opengoss.alarm.manager.internal.core;

import org.opengoss.web.core.WebPluginActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator extends WebPluginActivator {
	
	@Override
	protected ClassLoader getClassLoader() {
		return Activator.class.getClassLoader();
	}

}
