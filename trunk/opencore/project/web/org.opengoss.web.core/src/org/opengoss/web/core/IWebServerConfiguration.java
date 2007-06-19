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
package org.opengoss.web.core;

/**
 * Web server configurations including HTTP server port, thread pool etc.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IWebServerConfiguration {
	
	String WEB_SERVER_CONFIG = "org.opengoss.web.server.config";
	
	String SERVER_WEB_START = "server.web.start";
	
	String SERVER_WEB_PORT = "server.web.port";
	
	String SERVER_THREAD_MIN = "server.thread.min";

	String SERVER_THREAD_LOW = "server.thread.low";
	
	String SERVER_THREAD_MAX = "server.thread.max";
	
	/**
	 * Get string property.
	 * @param key property key
	 * @return string property value
	 */
	public String getProperty(String key);
	/**
	 * Get boolean property.
	 * @param key property key.
	 * @return boolean property value.
	 */
	public boolean getBoolean(String key);
	/**
	 * Get int property.
	 * @param key property key.
	 * @return int property value.
	 */
	public int getInteger(String key);
}
