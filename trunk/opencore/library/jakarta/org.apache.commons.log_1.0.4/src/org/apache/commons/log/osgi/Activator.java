package org.apache.commons.log.osgi;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		File file = new File("./etc/log4j.properties");
		PropertyConfigurator.configure(file.toURI().toURL());
	}

	public void stop(BundleContext context) throws Exception {
	}

}
