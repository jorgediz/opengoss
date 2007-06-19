package org.opengoss.dao.internal.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.opengoss.core.IPluginContext;
import org.opengoss.core.IPluginContextAware;
import org.opengoss.core.IStartable;
import org.opengoss.orm.core.IConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;

public class SessionFactoryBuilder implements IStartable, IPluginContextAware {

	private IExtensionRegistry registry;

	private IPluginContext pluginContext;

	private SessionFactory sessionFactory;

	private ServiceRegistration registration;

	public SessionFactoryBuilder(IExtensionRegistry registry) {
		this.registry = registry;
	}

	@SuppressWarnings("unchecked")
	public void start() throws Exception {
		Configuration configuration = new Configuration().configure(new File(
				"./etc/hibernate.cfg.xml"));
		Class[] domainClasses = getDomainClasses();
		for (Class domainClass : domainClasses) {
			configuration.addClass(domainClass);
		}
		sessionFactory = configuration.buildSessionFactory();
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("scope", "APPLICATION");
		props.put("uid", "hibernate:SessionFactory");

		registration = pluginContext.getBundleContext().registerService(
				SessionFactory.class.getName(), sessionFactory, props);
	}

	public void stop() throws Exception {
		registration.unregister();
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	public void setPluginContext(IPluginContext context) {
		this.pluginContext = context;
	}

	@SuppressWarnings("unchecked")
	private Class[] getDomainClasses() throws Exception {
		List<Class> domainClasses = new ArrayList<Class>();

		IExtensionPoint point = registry
				.getExtensionPoint(IConstants.DOMAIN_OBJECT_EXTENSION_POINT);
		IExtension[] extensions = point.getExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] elements = extension
					.getConfigurationElements();
			for (IConfigurationElement configurationElement : elements) {
				Bundle bundle = pluginContext.getBundleBySymbolId(extension
						.getNamespaceIdentifier());
				Class domainClass = bundle.loadClass(configurationElement
						.getAttribute("class"));
				domainClasses.add(domainClass);
			}
		}

		return domainClasses.toArray(new Class[domainClasses.size()]);
	}

}
