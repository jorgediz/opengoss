package com.tangramoss.cnm.customer;

import org.opengoss.web.core.WebPluginActivator;

public class Activator   extends WebPluginActivator {

	@Override
	protected ClassLoader getClassLoader() {
		return Activator.class.getClassLoader();
	}

}
