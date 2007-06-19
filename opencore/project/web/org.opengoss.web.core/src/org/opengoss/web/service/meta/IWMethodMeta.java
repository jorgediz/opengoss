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
package org.opengoss.web.service.meta;

import java.lang.reflect.Method;

import org.opengoss.web.service.URIPattern;
/**
 * Metadata for web method.
 * 
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IWMethodMeta {
	/**
	 * Get HTTP method.
	 * 
	 * @return http method.
	 */
	String getMethodType();
	/**
	 * Get the URI pattern that matchs this web method.
	 * @return uri pattern
	 */
	URIPattern getUriPattern();
	/**
	 * Get the object method that will be invoked.
	 * 
	 * @return method that will be invoked.
	 */
	Method getMethod();
	/**
	 * Get the parameter matadatas.
	 * 
	 * @return parameter matadatas.
	 */
	IWParamMeta[] getParamMetas();

}
