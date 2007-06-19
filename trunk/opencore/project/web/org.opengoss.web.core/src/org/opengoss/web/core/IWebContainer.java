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
 * Web application container.
 * <p>
 * A web container belongs to a {@link org.opengoss.web.core.IWebServer}.
 * </p>
 * <p>
 * A web container may contain one or more {@link org.opengoss.web.core.IWebPlugin}.
 * </p> 
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IWebContainer {
	
	String NAME = "webContainer";
	/**
	 * Add web plugin to this container.
	 * 
	 * @param webPlugin web plugin.
	 * @throws Exception
	 */
	void add(IWebPlugin webPlugin) throws Exception;
	/**
	 * Remove web plugin from this container.
	 * @param webPlugin web plugin.
	 * @throws Exception
	 */
	void remove(IWebPlugin webPlugin) throws Exception;

}
