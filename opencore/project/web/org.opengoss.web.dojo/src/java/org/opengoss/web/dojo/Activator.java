package org.opengoss.web.dojo;

import org.opengoss.web.core.WebPluginActivator;

public class Activator extends WebPluginActivator {

	protected ClassLoader getClassLoader() {
		return Activator.class.getClassLoader();
	}

}
