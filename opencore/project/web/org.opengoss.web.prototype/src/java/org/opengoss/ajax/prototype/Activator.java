package org.opengoss.ajax.prototype;

import org.opengoss.web.core.WebPluginActivator;

public class Activator extends WebPluginActivator {

	@Override
	protected ClassLoader getClassLoader() {
		return Activator.class.getClassLoader();
	}

}
