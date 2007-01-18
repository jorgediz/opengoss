package org.opengoss.core.console;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private Console console = null;
	
	public void start(BundleContext context) throws Exception {
		console = new Console();
		context.addFrameworkListener(console);
		context.addBundleListener(console);
		context.addServiceListener(console);
	}
	
	public void stop(BundleContext context) throws Exception {
		if(console != null) {
			context.removeServiceListener(console);
			context.removeBundleListener(console);
			context.removeFrameworkListener(console);
		}
	}

}
