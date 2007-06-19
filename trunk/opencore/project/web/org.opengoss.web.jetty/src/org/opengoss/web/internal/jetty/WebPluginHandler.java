package org.opengoss.web.internal.jetty;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PermissionCollection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpException;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.handler.HandlerWrapper;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.resource.Resource;
import org.mortbay.util.Attributes;
import org.mortbay.util.AttributesMap;
import org.mortbay.util.LazyList;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;
import org.opengoss.web.core.IWebPlugin;

public class WebPluginHandler extends HandlerWrapper implements
		WebPluginContext {
	
	public final static String WEB_DEFAULTS_XML="org/mortbay/jetty/webapp/webdefault.xml";

	private static ThreadLocal contexts = new ThreadLocal();

	public static ServletContext getCurrentContext() {
		return (ServletContext) contexts.get();
	}

	static final Log log = LogFactory.getLog("OWebContextHandler");

	private Attributes attributes;
	private ClassLoader classLoader;
	private WebPluginServletContext context;
	private String contextPath;
	private Map initParams;
	private String displayName;
	private URL baseResource;
	private MimeTypes mimeTypes;
	private Map localeEncodingMap;
	private String[] welcomeFiles;
	private ErrorHandler errorHandler;
	private String[] vhosts;
	private EventListener[] eventListeners;
	private boolean shutdown;

	private PermissionCollection permissions;

	private Object contextListeners;
	private Object contextAttributeListeners;
	private Object requestListeners;
	private Object requestAttributeListeners;

	private final IWebPlugin webPlugin;

	private SecurityHandler securityHandler;

	private ServletHandler servletHandler;

	private SessionHandler sessionHandler;

	public WebPluginHandler(WebContainer webContainer,
			IWebPlugin wePlugin) {
		super();

		context = new WebPluginServletContext(this);
		context.setAttribute("WebPlugin", wePlugin);
		attributes = new AttributesMap();
		initParams = new HashMap();
		localeEncodingMap = new HashMap();
		this.baseResource = wePlugin.getContext().getBundleContext()
				.getBundle().getResource("/");

		setServer(webContainer.getServer());
		this.webPlugin = wePlugin;
		this.classLoader = wePlugin.getClassLoader();
		this.contextPath = wePlugin.getContextPath();

		securityHandler = new SecurityHandler();
		servletHandler = new ServletHandler();
		sessionHandler = new SessionHandler();

		sessionHandler.setHandler(securityHandler);
		securityHandler.setHandler(servletHandler);
		this.setHandler(sessionHandler);
	}

	public IWebPlugin getWebComponent() {
		return webPlugin;
	}

	public void setVirtualHosts(String[] vhosts) {
		this.vhosts = vhosts;
	}

	public String[] getVirtualHosts() {
		return vhosts;
	}

	public Object getAttribute(String name) {
		return attributes.getAttribute(name);
	}

	public Enumeration getAttributeNames() {
		return attributes.getAttributeNames();
	}

	public Attributes getAttributes() {
		return attributes;
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return Returns the classLoader.
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return Returns the contextPath.
	 */
	public String getContextPath() {
		return contextPath;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String name) {
		return (String) initParams.get(name);
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(initParams.keySet());
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return Returns the initParams.
	 */
	public Map getInitParams() {
		return initParams;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	public String getDisplayName() {
		return displayName;
	}

	/* ------------------------------------------------------------ */
	public EventListener[] getEventListeners() {
		return eventListeners;
	}

	/* ------------------------------------------------------------ */
	public void setEventListeners(EventListener[] eventListeners) {
		contextListeners = null;
		contextAttributeListeners = null;
		requestListeners = null;
		requestAttributeListeners = null;

		this.eventListeners = eventListeners;

		for (int i = 0; eventListeners != null && i < eventListeners.length; i++) {
			EventListener listener = eventListeners[i];

			if (listener instanceof ServletContextListener)
				contextListeners = LazyList.add(contextListeners, listener);

			if (listener instanceof ServletContextAttributeListener)
				contextAttributeListeners = LazyList.add(
						contextAttributeListeners, listener);

			if (listener instanceof ServletRequestListener)
				requestListeners = LazyList.add(requestListeners, listener);

			if (listener instanceof ServletRequestAttributeListener)
				requestAttributeListeners = LazyList.add(
						requestAttributeListeners, listener);
		}

		if (sessionHandler != null)
			sessionHandler.clearEventListeners();

		for (int i = 0; eventListeners != null && i < eventListeners.length; i++) {
			EventListener listener = eventListeners[i];

			if ((listener instanceof HttpSessionActivationListener)
					|| (listener instanceof HttpSessionAttributeListener)
					|| (listener instanceof HttpSessionBindingListener)
					|| (listener instanceof HttpSessionListener)) {
				if (sessionHandler != null)
					sessionHandler.addEventListener(listener);
			}

		}
	}

	/* ------------------------------------------------------------ */
	public void addEventListener(EventListener listener) {
		setEventListeners((EventListener[]) LazyList.addToArray(
				getEventListeners(), listener, EventListener.class));
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return true if this context is accepting new requests
	 */
	public boolean isShutdown() {
		return !shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see org.mortbay.thread.AbstractLifeCycle#doStart()
	 */
	protected void doStart() throws Exception {
		ClassLoader old_classloader = null;
		Thread current_thread = null;
		Object old_context = null;
		try {
			// Set the classloader
			if (classLoader != null) {
				current_thread = Thread.currentThread();
				old_classloader = current_thread.getContextClassLoader();
				current_thread.setContextClassLoader(classLoader);
			}

			if (mimeTypes == null)
				mimeTypes = new MimeTypes();

			old_context = contexts.get();
			contexts.set(context);

			if (errorHandler == null)
				setErrorHandler(new ErrorHandler());

			startContext();

		} finally {
			contexts.set(old_context);

			// reset the classloader
			if (classLoader != null) {
				current_thread.setContextClassLoader(old_classloader);
			}
		}
	}
	
	WebXmlConfiguration configuration;

	/* ------------------------------------------------------------ */
	protected void startContext() throws Exception {
		configuration = new WebXmlConfiguration();
		configuration.setWebPluginContext(this);
		configuration.configureWebPlugin();

		if (errorHandler != null)
			errorHandler.start();

		// Context listeners
		if (contextListeners != null) {
			ServletContextEvent event = new ServletContextEvent(context);
			for (int i = 0; i < LazyList.size(contextListeners); i++) {
				((ServletContextListener) LazyList.get(contextListeners, i))
						.contextInitialized(event);
			}
		}

		super.doStart();
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see org.mortbay.thread.AbstractLifeCycle#doStop()
	 */
	protected void doStop() throws Exception {
		// set context
		Object oldContext = contexts.get();
		contexts.set(context);

		ClassLoader oldClassLoader = null;
		try {
			// Set the classloader
			if (classLoader != null) {
				oldClassLoader = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(classLoader);
			}
			super.doStop();
			// Context listeners
			if (contextListeners != null) {
				ServletContextEvent event = new ServletContextEvent(context);
				for (int i = LazyList.size(contextListeners); i-- > 0;) {
					((ServletContextListener) LazyList.get(contextListeners, i))
							.contextDestroyed(event);
				}
			}
			if (errorHandler != null)
				errorHandler.stop();
		} finally {
			contexts.set(oldContext);
			// reset the classloader
			if (classLoader != null)
				Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

		context.destory();

		configuration.deconfigureWebPlugin();
		// restore security handler
		if (securityHandler.getHandler() != null) {
			sessionHandler.setHandler(securityHandler);
			securityHandler.setHandler(servletHandler);
		}
	}

	/* ------------------------------------------------------------ */
	/*
	 * @see org.mortbay.jetty.Handler#handle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, int dispatch) throws IOException,
			ServletException {
		boolean isNewContext = false;
		ServletContext oldContext = null;
		String oldContextPath = null;
		String oldServletPath = null;
		String oldPathInfo = null;
		ClassLoader oldClassLoader = null;

		Request baseRequest = (request instanceof Request) ? (Request) request
				: HttpConnection.getCurrentConnection().getRequest();
		if (!isStarted() || shutdown
				|| (dispatch == REQUEST && baseRequest.isHandled()))
			return;

		oldContext = baseRequest.getContext();

		// Are we already in this context?
		if (oldContext != context) {
			isNewContext = true;

			// Check the vhosts
			if (vhosts != null && vhosts.length > 0) {
				String vhost = request.getServerName();
				boolean match = false;

				// TODO non-linear lookup
				for (int i = 0; !match && i < vhosts.length; i++)
					match = vhosts[i] != null
							&& vhosts[i].equalsIgnoreCase(vhost);
				if (!match)
					return;
			}

			// Nope - so check the target.
			if (dispatch == REQUEST) {
				if (target.equals(contextPath)) {
					target = contextPath;
					if (!target.endsWith("/")) {
						baseRequest.setHandled(true);
						if (request.getQueryString() != null)
							response.sendRedirect(target + "/?"
									+ request.getQueryString());
						else
							response.sendRedirect(target + "/");
						return;
					}
				} else if (target.startsWith(contextPath)
						&& (contextPath.length() == 1 || target
								.charAt(contextPath.length()) == '/')) {
					if (contextPath.length() > 1)
						target = target.substring(contextPath.length());
				} else {
					// Not for this context!
					return;
				}
			}
		}

		try {
			oldContextPath = baseRequest.getContextPath();
			oldServletPath = baseRequest.getServletPath();
			oldPathInfo = baseRequest.getPathInfo();

			// Update the paths
			baseRequest.setContext(context);
			if (dispatch != INCLUDE && target.startsWith("/")) {
				if (contextPath.length() == 1)
					baseRequest.setContextPath("");
				else
					baseRequest.setContextPath(contextPath);
				baseRequest.setServletPath(null);
				baseRequest.setPathInfo(target);
			}

			ServletRequestEvent event = null;
			if (isNewContext) {
				// Set the classloader
				if (classLoader != null) {
					oldClassLoader = Thread.currentThread()
							.getContextClassLoader();
					Thread.currentThread().setContextClassLoader(classLoader);
				}

				// Handle the REALLY SILLY request events!
				if (requestListeners != null) {
					event = new ServletRequestEvent(context, request);
					for (int i = 0; i < LazyList.size(requestListeners); i++)
						((ServletRequestListener) LazyList.get(
								requestListeners, i)).requestInitialized(event);
				}
				for (int i = 0; i < LazyList.size(requestAttributeListeners); i++)
					baseRequest.addEventListener(((EventListener) LazyList.get(
							requestAttributeListeners, i)));
			}

			// Handle the request
			try {
				if (dispatch == REQUEST && isProtectedTarget(target))
					throw new HttpException(HttpServletResponse.SC_NOT_FOUND);

				Handler handler = getHandler();
				if (handler != null)
					handler.handle(target, request, response, dispatch);
			} catch (HttpException e) {
				log.debug("handle error!", e);
				response.sendError(e.getStatus(), e.getReason());
			} finally {
				// Handle more REALLY SILLY request events!
				if (isNewContext) {
					for (int i = LazyList.size(requestListeners); i-- > 0;)
						((ServletRequestListener) LazyList.get(
								requestListeners, i)).requestDestroyed(event);

					for (int i = 0; i < LazyList
							.size(requestAttributeListeners); i++)
						baseRequest
								.removeEventListener(((EventListener) LazyList
										.get(requestAttributeListeners, i)));
				}
			}
		} finally {
			if (oldContext != context) {
				// reset the classloader
				if (classLoader != null) {
					Thread.currentThread()
							.setContextClassLoader(oldClassLoader);
				}

				// reset the context and servlet path.
				baseRequest.setContext(oldContext);
				baseRequest.setContextPath(oldContextPath);
				baseRequest.setServletPath(oldServletPath);
				baseRequest.setPathInfo(oldPathInfo);
			}
		}
	}

	public void removeAttribute(String name) {
		attributes.removeAttribute(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.setAttribute(name, value);
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public void clearAttributes() {
		attributes.clearAttributes();
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setContextPath(String contextPath) {
		if (contextPath != null && contextPath.length() > 1
				&& contextPath.endsWith("/")) {
			throw new IllegalArgumentException("ends with /");
		}
		this.contextPath = contextPath;
		if (getServer() != null && getServer().isStarted()) {
			Handler[] contextCollections = getServer().getChildHandlersByClass(
					ContextHandlerCollection.class);
			for (int h = 0; contextCollections != null
					&& h < contextCollections.length; h++)
				((ContextHandlerCollection) contextCollections[h])
						.mapContexts();
		}
	}

	public void setInitParams(Map initParams) {
		if (initParams == null) {
			return;
		}
		initParams = new HashMap(initParams);
	}

	public void setDisplayName(String servletContextName) {
		displayName = servletContextName;
	}

	public URL getBaseResource() {
		return baseResource;
	}

	public void setBaseResource(URL base) {
		baseResource = base;
	}

	public MimeTypes getMimeTypes() {
		return mimeTypes;
	}

	public void setMimeTypes(MimeTypes mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public void setWelcomeFiles(String[] files) {
		welcomeFiles = files;
	}

	public String[] getWelcomeFiles() {
		return welcomeFiles;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		if (errorHandler != null) {
			errorHandler.setServer(getServer());
		}
		if (getServer() != null)
			getServer().getContainer().update(this, errorHandler, errorHandler,
					"errorHandler");
		this.errorHandler = errorHandler;
	}

	public String toString() {
		return "OSGiContextHandler@" + Integer.toHexString(hashCode()) + "{"
				+ getContextPath() + "," + getBaseResource() + "}";
	}

	/* ------------------------------------------------------------ */
	public synchronized Class loadClass(String className)
			throws ClassNotFoundException {
		if (className == null) {
			return null;
		}
		return classLoader.loadClass(className);
	}

	public void addLocaleEncoding(String locale, String encoding) {
		localeEncodingMap.put(locale, encoding);
	}

	public String getLocaleEncoding(Locale locale) {
		String encoding = (String) localeEncodingMap.get(locale.toString());
		if (encoding == null) {
			encoding = (String) localeEncodingMap.get(locale.getLanguage());
		}
		return encoding;
	}

	public URL getResource(String path) throws MalformedURLException {
		path = URIUtil.canonicalPath(path);
		return new URL(URIUtil.addPaths(baseResource.toExternalForm(), path));
	}

	public ServletContext getContext(String uriPath) {
		// TODO:
		return context;
	}

	public List getContextAttributeListeners() {
		return (List) contextAttributeListeners;
	}

	public String getMimeType(String file) {
		return null;
	}

	public SecurityHandler getSecurityHandler() {
		return securityHandler;
	}

	public void setSecurityHandler(SecurityHandler securityHandler) {
		this.securityHandler = securityHandler;
	}

	public ServletHandler getServletHandler() {
		return servletHandler;
	}

	public void setServletHanlder(ServletHandler handler) {
		this.servletHandler = handler;
	}

	public SessionHandler getSessionHandler() {
		return sessionHandler;
	}

	public void setSessionHandler(SessionHandler handler) {
		this.sessionHandler = handler;
	}

	private transient Map resourceAliases = new HashMap(5);

	public void setResourceAlias(String alias, String uri) {
		resourceAliases.put(alias, uri);
	}

	/* ------------------------------------------------------------ */
	public Map getResourceAliases() {
		return resourceAliases;
	}

	/* ------------------------------------------------------------ */
	public void setResourceAliases(Map map) {
		resourceAliases = map;
	}

	public String removeResourceAlias(String alias) {
		return (String) resourceAliases.remove(alias);
	}

	/* ------------------------------------------------------------ */
	protected boolean isProtectedTarget(String target) {
		return StringUtil.startsWithIgnoreCase(target, "/web-inf")
				|| StringUtil.startsWithIgnoreCase(target, "/meta-inf");
	}

	public PermissionCollection getPermissions() {

		return permissions;
	}

	public void setPermissions(PermissionCollection permissions) {
		this.permissions = permissions;
	}

	public String getDefaultsDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getTempDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDistributable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDistributable(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public Resource getWebInf() {
		// TODO Auto-generated method stub
		return null;
	}

}
