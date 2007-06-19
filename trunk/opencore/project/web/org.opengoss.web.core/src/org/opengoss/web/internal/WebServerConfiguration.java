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
package org.opengoss.web.internal;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.opengoss.core.IStartable;
import org.opengoss.web.core.IWebServerConfiguration;
/**
 * Implementation of {@link org.opengoss.web.core.IWebServerConfiguration}
 * 
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class WebServerConfiguration implements IWebServerConfiguration, IStartable {
	
	Properties props = null;
	
	public WebServerConfiguration() {
	}

	public void start() throws Exception {
		URL configURL = null;
		String configFile = System.getProperty(IWebServerConfiguration.WEB_SERVER_CONFIG);
		if(configFile == null) {
			configFile = "./etc/server.properties";
		}
		if(configFile != null) {
			configURL = new File(configFile).toURI().toURL();
		}
		if(configURL == null) {
			configURL = getClass().getClassLoader().getResource("/etc/server.properties");
		}
		if(configURL == null) {
			throw new Exception("No web server configuration found!");
		}
		props = new Properties();
		props.load(configURL.openStream());
	}

	public void stop() throws Exception {
		props.clear();
		props = null;
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}

	public int getInteger(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

}
