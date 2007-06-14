package org.opengoss.cnm.security.internal;

import org.opengoss.core.IPluginContext;
import org.opengoss.web.core.WebPluginActivator;

public class Activator extends WebPluginActivator {

	@Override
	protected ClassLoader getClassLoader() {
		return Activator.class.getClassLoader();
	}

	public IPluginContext getPluginContext(){
		return this.pluginContext; //程序上的实现调用其他的pluginContext
	}
	
}
