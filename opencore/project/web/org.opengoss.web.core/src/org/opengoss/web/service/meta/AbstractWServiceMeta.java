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
/**
 * Abstract implementation of web service metadata.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public abstract class AbstractWServiceMeta implements IWServiceMeta {

	private Class<?> clazz;

	public AbstractWServiceMeta(Class<?> clazz) {
		this.clazz = clazz;
	}
	/**
	 * Get the service class.
	 * 
	 * @return service class.
	 */
	public Class<?> getClazz() {
		return clazz;
	}
	
}
