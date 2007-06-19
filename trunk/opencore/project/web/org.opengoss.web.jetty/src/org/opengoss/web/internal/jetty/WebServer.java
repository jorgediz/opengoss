/*
 * Copyright 2005-2006 the original authors and www.opengoss.org community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengoss.web.internal.jetty;

import java.util.ArrayList;
import java.util.List;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.HashSessionIdManager;
import org.mortbay.thread.BoundedThreadPool;
import org.mortbay.thread.ThreadPool;
import org.opengoss.web.core.IWebContainer;
import org.opengoss.web.core.IWebServer;
import org.opengoss.web.core.IWebServerConfiguration;
/**
 * Web Server implementation based on Jetty.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2007-11-23
 * @since 1.0
 */
public class WebServer implements IWebServer {

	private Server server;
	
	private IWebServerConfiguration configuration;
	
	private IWebContainer webContainer;
	
	public WebServer() {
		server = new Server();
	}
	/**
	 * Inject web server configuration.
	 * 
	 * @param configuration web server configuration.
	 */
	public void setConfiguration(IWebServerConfiguration configuration) {
		this.configuration = configuration;
	}
	/**
	 * Inject web container.
	 * 
	 * @param container web container
	 */
	public void setWebContainer(IWebContainer container) throws Exception {
		((WebContainer)container).setServer(server);
		server.addHandler((WebContainer)container);	
		this.webContainer = container;
	}
	/**
	 * Start web server.
	 */
	public void start() throws Exception {
		if(configuration == null) {
			throw new Exception("Illegal State Exception, no config available!");
		}
		server.setThreadPool(configThreadPool());
		server.setConnectors(configConnectors());
		server.setSessionIdManager(new HashSessionIdManager());
		server.start();
	}
	/**
	 * Stop web server.
	 */
	public void stop() throws Exception {
		server.stop();
	}

	private ThreadPool configThreadPool() {
		BoundedThreadPool pool = new BoundedThreadPool();
		pool.setMinThreads(configuration.getInteger(IWebServerConfiguration.SERVER_THREAD_MIN));
		pool.setLowThreads(configuration.getInteger(IWebServerConfiguration.SERVER_THREAD_LOW));
		pool.setMaxThreads(configuration.getInteger(IWebServerConfiguration.SERVER_THREAD_MAX));
		pool.setDaemon(true);
		return pool;
	}

	private Connector[] configConnectors() {
		SelectChannelConnector connector = null;
		List<Connector> connectors = new ArrayList<Connector>(2);
		boolean isStart = configuration.getBoolean(IWebServerConfiguration.SERVER_WEB_START);
		if(isStart) {
			connector = new SelectChannelConnector();
			connector.setName(IWebContainer.NAME);
			connector.setPort(configuration.getInteger(IWebServerConfiguration.SERVER_WEB_PORT));
			connectors.add(connector);
		}
		return connectors.toArray(new Connector[connectors.size()]);
	}

}
