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
 * Marshaller registry.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IMarshallerRegistry {
	/**
	 * Add a domain object class.
	 * 
	 * @param clazz
	 */
	void addClass(Class clazz);
	/**
	 * Remove a domain object class.
	 * 
	 * @param clazz domain object class
	 */
	void removeClass(Class clazz);
	/**
	 * Add a domain object class with alias.
	 * 
	 * @param alias alias of the domain object class.
	 * @param clazz domain object class
	 */
	void addAliasClass(String alias, Class clazz);
	/**
	 * Remove a domain object class with alias
	 * 
	 * @param alias alias of the domain object class.
	 * @param clazz domain object class
	 */
	void removeAliasClass(String alias, Class clazz);
	/**
	 * Get the MIME type of the representation.
	 * 
	 * @param representation
	 * @return MIME type 
	 */
	String getMime(String representation);
	/**
	 * Set the MIME type of the representation.
	 * @param representation 
	 * @param mediaType
	 */
	void putMime(String representation, String mediaType);
	/**
	 * Get marshaller of the representation.
	 * 
	 * @param representation
	 * @return marshaller of the representation.
	 */
	IMarshaller getMarshaller(String representation);
	/**
	 * Add a representation marshaller.
	 * 
	 * @param marshaller
	 */
	void addMarshaller(IMarshaller marshaller);
	/**
	 * Remove a representation marshaller.
	 * 
	 * @param marshaller
	 */
	void removeMashaller(IMarshaller marshaller);
	
}
