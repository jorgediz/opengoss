package org.opengoss.rmi.test.client;

import org.opengoss.rmi.RmiService;
import org.opengoss.rmi.test.ITest;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		RmiService<ITest> service=new RmiService<ITest>("rmi://192.168.1.120:1299/test",ITest.class);
		ITest test=service.getService();
		System.err.println(test.echo("hello,world"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
