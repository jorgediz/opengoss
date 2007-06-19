package org.opengoss.web.internal.jetty;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;

import org.mortbay.jetty.MimeTypes;
import org.mortbay.log.Log;
import org.mortbay.util.Attributes;
import org.mortbay.util.AttributesMap;
import org.mortbay.util.LazyList;
import org.mortbay.util.URIUtil;

public class WebPluginServletContext implements ServletContext {

	static final Logger logger = Logger.getLogger("WebPluginServletContext");

	private WebPluginHandler contextHandler;

	private Attributes contextAttributes;

	WebPluginServletContext(WebPluginHandler handler) {
		this.contextHandler = handler;
		contextAttributes = new AttributesMap();
	}

	public WebPluginHandler getContextHandler() {
		return contextHandler;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getContext(java.lang.String)
	 */
	public ServletContext getContext(String uriPath) {
		return contextHandler.getContext(uriPath);
		// // TODO this is a very poor implementation!
		// // TODO move this to Server
		// ContextHandler context=null;
		// Handler[] handlers =
		// getServer().getChildHandlersByClass(ContextHandler.class);
		// for (int i=0;i<handlers.length;i++)
		// {
		// if (handlers[i]==null || !handlers[i].isStarted())
		// continue;
		// ContextHandler ch = (ContextHandler)handlers[i];
		// String context_path=ch.getContextPath();
		// if (uripath.equals(context_path) ||
		// (uripath.startsWith(context_path)&&uripath.charAt(context_path.length())=='/'))
		// {
		// if (context==null ||
		// context_path.length()>context.getContextPath().length())
		// context=ch;
		// }
		// }
		//        
		// if (context!=null)
		// return context._context;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getMajorVersion()
	 */
	public int getMajorVersion() {
		return 2;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
	 */
	public String getMimeType(String file) {
		return contextHandler.getMimeType(file);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	public int getMinorVersion() {
		return 5;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
	 */
	public RequestDispatcher getNamedDispatcher(String name) {
		// TODO:
		// return new Dispatcher(ContextHandler.this, name);
		return null;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
	 */
	public String getRealPath(String path) {
		return null;
//		try {
			//TODO:Test
			//return contextHandler.getResource(path).toExternalForm();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String uriInContext) {
		if (uriInContext == null)
			return null;

		if (!uriInContext.startsWith("/"))
			return null;

		try {
			String query = null;
			int q = 0;
			if ((q = uriInContext.indexOf('?')) > 0) {
				query = uriInContext.substring(q + 1);
				uriInContext = uriInContext.substring(0, q);
			}
			if ((q = uriInContext.indexOf(';')) > 0)
				uriInContext = uriInContext.substring(0, q);

			String pathInContext = URIUtil.canonicalPath(URIUtil
					.decodePath(uriInContext));
			String uri = URIUtil.addPaths(getContextPath(), uriInContext);
			return new DefaultDispatcher((WebPluginHandler) contextHandler, uri,
					pathInContext, query);
		} catch (Exception e) {
			Log.ignore(e);
		}
		return null;
	}

	/* ------------------------------------------------------------ */
	/* 
	 */
	public URL getResource(String path) throws MalformedURLException {
		return contextHandler.getResource(path);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	public InputStream getResourceAsStream(String path) {
		try {
			URL url = getResource(path);
			if (url == null)
				return null;
			return url.openStream();
		} catch (Exception e) {
			Log.ignore(e);
			return null;
		}
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	public Set getResourcePaths(String path) {
		// TODO:
		return new HashSet();
		// return delegate.getResourcePaths(path);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getServerInfo()
	 */
	public String getServerInfo() {
		return "Jetty-6.0@OSGi";
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 */
	public Servlet getServlet(String name) throws ServletException {
		return null;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getServletNames()
	 */
	public Enumeration getServletNames() {
		return Collections.enumeration(Collections.EMPTY_LIST);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getServlets()
	 */
	public Enumeration getServlets() {
		return Collections.enumeration(Collections.EMPTY_LIST);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#log(java.lang.Exception,
	 *      java.lang.String)
	 */
	public void log(Exception exception, String msg) {
		logger.log(Level.WARNING, msg, exception);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#log(java.lang.String)
	 */
	public void log(String msg) {
		logger.log(Level.INFO, msg);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#log(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void log(String message, Throwable throwable) {
		logger.log(Level.WARNING, message, throwable);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String name) {
		return contextHandler.getInitParameter(name);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames() {
		return contextHandler.getInitParameterNames();
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	public synchronized Object getAttribute(String name) {
		Object o = contextHandler.getAttribute(name);
		if (o == null) {
			o = contextAttributes.getAttribute(name);
		}
		return o;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 */
	public synchronized Enumeration getAttributeNames() {
		HashSet set = new HashSet();
		Enumeration e = contextAttributes.getAttributeNames();
		while (e.hasMoreElements())
			set.add(e.nextElement());
		e = contextHandler.getAttributeNames();
		while (e.hasMoreElements())
			set.add(e.nextElement());

		return Collections.enumeration(set);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	public synchronized void setAttribute(String name, Object value) {

		Object old_value = contextAttributes == null ? null : contextAttributes
				.getAttribute(name);

		if (value == null) {
			contextAttributes.removeAttribute(name);
		} else {
			contextAttributes.setAttribute(name, value);
		}
		List contextAttributeListeners = contextHandler
				.getContextAttributeListeners();
		if (contextAttributeListeners != null) {
			ServletContextAttributeEvent event = new ServletContextAttributeEvent(
					this, name, old_value == null ? value : old_value);

			for (int i = 0; i < LazyList.size(contextAttributeListeners); i++) {
				ServletContextAttributeListener l = (ServletContextAttributeListener) LazyList
						.get(contextAttributeListeners, i);

				if (old_value == null)
					l.attributeAdded(event);
				else if (value == null)
					l.attributeRemoved(event);
				else
					l.attributeReplaced(event);
			}
		}
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	public synchronized void removeAttribute(String name) {
		Object old_value = contextAttributes.getAttribute(name);
		contextAttributes.removeAttribute(name);
		if (old_value != null) {
			List contextAttributeListeners = contextHandler
					.getContextAttributeListeners();
			if (contextAttributeListeners != null) {
				ServletContextAttributeEvent event = new ServletContextAttributeEvent(
						this, name, old_value);

				for (int i = 0; i < LazyList.size(contextAttributeListeners); i++)
					((ServletContextAttributeListener) LazyList.get(
							contextAttributeListeners, i))
							.attributeRemoved(event);
			}
		}
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	public String getServletContextName() {
		String name = contextHandler.getDisplayName();
		if (name == null)
			name = contextHandler.getContextPath();
		return name;
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return Returns the _contextPath.
	 */
	public String getContextPath() {
		return contextHandler.getContextPath();
	}

	/* ------------------------------------------------------------ */
	public String toString() {
		return "OSGiServletContext@" + Integer.toHexString(hashCode()) + "{"
				+ getContextPath() + "," + contextHandler.getBaseResource()
				+ "}";
	}

	public MimeTypes getMimeTypes() {
		return contextHandler.getMimeTypes();
	}

	public String[] getWelcomeFiles() {
		return contextHandler.getWelcomeFiles();
	}

	public void destory() {
		contextAttributes.clearAttributes();
	}

}
