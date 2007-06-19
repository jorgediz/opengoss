/**
 * 
 */
package org.opengoss.core.internal;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.Date;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * @author erylee
 *
 */
public class Console implements BundleListener, FrameworkListener,
		ServiceListener {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.BundleEvent)
	 */
	public void bundleChanged(BundleEvent event) {
		String bundleSymbol = event.getBundle().getSymbolicName();
		switch(event.getType()) {
		case BundleEvent.INSTALLED:
			println("Bundle Installed: " + bundleSymbol);
			break;
		case BundleEvent.RESOLVED:
			println("Bundle Resolved: " + bundleSymbol);
			break;
		case BundleEvent.STARTING:
			println("Bundle Starting: " + bundleSymbol + " ...");
			break;
		case BundleEvent.STARTED:
			println("Bundle Started: " + bundleSymbol);
			break;
		case BundleEvent.STOPPING:
			println("Bundle Stopping: " + bundleSymbol + " ...");
			break;
		case BundleEvent.STOPPED:
			println("Bundle Stopped: " + bundleSymbol);
			break;
		case BundleEvent.UNRESOLVED:
			println("Bundle Unresolved: "+ bundleSymbol);
			break;
		case BundleEvent.UNINSTALLED:
			println("Bundle Uninstalled: " + bundleSymbol);
			break;
		case BundleEvent.UPDATED:
			println("Bundle Updated: " + bundleSymbol);
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.FrameworkListener#frameworkEvent(org.osgi.framework.FrameworkEvent)
	 */
	public void frameworkEvent(FrameworkEvent event) {
		String bundleSymbol = event.getBundle().getSymbolicName();
		Throwable t = event.getThrowable();
		switch(event.getType()) {
		case FrameworkEvent.STARTED:
			println("Framework Started.");
			break;
		case FrameworkEvent.PACKAGES_REFRESHED:
			println("Framework Packages Refreshed: Bundle=" + bundleSymbol);
			break;
		case FrameworkEvent.ERROR:
			println("Framework Error: Bundle=" + bundleSymbol);
			printStackTrace(t);
			break;
		case FrameworkEvent.WARNING:
			println("Framework Warning: Bundle=" + bundleSymbol);
			printStackTrace(t);
			break;
		case FrameworkEvent.INFO:
			println("Framework Info: Bundle=" + bundleSymbol);
			printStackTrace(t);
			break;
		case FrameworkEvent.STARTLEVEL_CHANGED:
			println("Framwork StartLevel Changed.");
		default:
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	public void serviceChanged(ServiceEvent event) {
		String serviceProps = getServiceProps(event.getServiceReference());
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			println("Service Registered: " + serviceProps);
			break;
		case ServiceEvent.UNREGISTERING:
			println("Service Unregisering: " + serviceProps);
			break;
		case ServiceEvent.MODIFIED:
			println("Service Modified: " + serviceProps);
			break;
		default:
			break;
		}
	}
	
	private String getServiceProps(ServiceReference serviceReference) {
		StringBuilder sb = new StringBuilder("{");
		String[] keys = serviceReference.getPropertyKeys();
		for (String key : keys) {
			Object value = serviceReference.getProperty(key);
			sb.append(key).append("=").append(toString(value)).append(",");
		}
		sb.append("}");
		return sb.toString();
	}

	private String toString(Object value) {
		StringBuilder sb = new StringBuilder();
		if(value.getClass().isArray()) {
			sb.append("[");
			for (int i = 0; i < Array.getLength(value); i++) {
				sb.append(toString(Array.get(value, i)));
				sb.append(",");
			}
			sb.append("]");
		} else {
			sb.append(value.toString());
		}
		return sb.toString();
	}


	private void println(String msg) {
		Date time = new Date();
		DateFormat df = DateFormat.getDateTimeInstance();
		msg = "[" + df.format(time) + "] " + msg;
		System.out.println(msg);
	}

	private void printStackTrace(Throwable t) {
		if(t != null) {
			t.printStackTrace();
		}
	}

}
