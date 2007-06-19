package org.opengoss.rmi.test.server;

import org.opengoss.rmi.RmiExporter;
import org.opengoss.rmi.test.ITest;
import org.opengoss.rmi.test.impl.Test;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private RmiExporter exporter;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
			ITest test=new Test();
			exporter = new RmiExporter("test",ITest.class,test,1299);
			
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {

	}

}
