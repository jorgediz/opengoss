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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Web servicelet is a bridge between servlet and web service object.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IWServicelet {
	
	String VIEW = "view";
	/**
	 * URI of the web servicelet.
	 * 
	 * @return URI of the web servicelet.
	 */
	String getUri();
	/**
	 * Set the web service container that this web servicelet belongs to.
	 * 
	 * @param container web service container.
	 */
	void setContainer(IWSContainer container);
	/**
	 * Handle the HTTP servlet request.
	 * @param req HTTP servlet request.
	 * @param resp HTTP servlet response.
	 * @throws IOException
	 */
	void service(HttpServletRequest req, HttpServletResponse resp) throws IOException;

}
