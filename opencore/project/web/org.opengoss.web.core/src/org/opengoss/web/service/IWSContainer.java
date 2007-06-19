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
package org.opengoss.web.service;
/**
 * Web servicelet container. 
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IWSContainer {
	/**
	 * Add a web servicelet.
	 * 
	 * @param servicelet a web servicelet
	 * @throws Exception
	 */
	void add(IWServicelet servicelet) throws Exception;
	/**
	 * Remove a web servicelet.
	 * 
	 * @param servicelet a web servicelet.
	 * @throws Exception
	 */
	void remove(IWServicelet servicelet) throws Exception;
	/**
	 * Get a marshaller by representation type.
	 * 
	 * @param representation representation type.
	 * @return marshaller of the representation.
	 */
	IMarshaller getMarshaller(String representation);
	/**
	 * Get a web servicelet by URL pathInfo.
	 * 
	 * @param pathInfo URL pathInfo.
	 * @return a web servicelet. 
	 */
	IWServicelet getWServicelet(String pathInfo);
	
}
