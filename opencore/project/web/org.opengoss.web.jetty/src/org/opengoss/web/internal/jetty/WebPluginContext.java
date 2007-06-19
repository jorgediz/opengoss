package org.opengoss.web.internal.jetty;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.resource.Resource;

public interface WebPluginContext {

	ServletContext getContext(String uriPath);

	String getMimeType(String file);

	URL getResource(String path) throws MalformedURLException;

	String getInitParameter(String name);

	Enumeration getInitParameterNames();

	Object getAttribute(String name);

	Enumeration getAttributeNames();

	List getContextAttributeListeners();

	String getDisplayName();

	String getContextPath();

	URL getBaseResource();

	boolean isStarted();

	ServletHandler getServletHandler();

	void setEventListeners(EventListener[] eventListeners);

	void setWelcomeFiles(String[] files);

	SecurityHandler getSecurityHandler();

	ErrorHandler getErrorHandler();

	Object[] getWelcomeFiles();

	Object[] getEventListeners();

	void setDisplayName(String string);

	Map getInitParams();

	Class loadClass(String className) throws ClassNotFoundException;

	SessionHandler getSessionHandler();

	MimeTypes getMimeTypes();

	void addLocaleEncoding(String locale, String encoding);

	void setResourceAlias(String uri, String location);

	String getDefaultsDescriptor();

	File getTempDirectory();

	boolean isDistributable();

	void setDistributable(boolean b);

	void addEventListener(EventListener l);

	Resource getWebInf();

}
