package org.opengoss.petstore.gwt;

import org.opengoss.web.core.WebPluginActivator;

public class Activator extends WebPluginActivator {

	@Override
	protected ClassLoader getClassLoader() {
		return Activator.class.getClassLoader();
	}

}
