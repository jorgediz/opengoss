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
import java.io.InputStream;
import java.io.OutputStream;
/**
 * Representation marshaller.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public interface IMarshaller {
	/**
	 * Get the representation this marshaller handles..
	 *  
	 * @return representation name.
	 */
	String getRepresentation();
	/**
	 * Read the representation from inputStream.
	 * 
	 * @param inputStream representation inputStream.
	 * @param clazz class of the instance unmarshalled from the inputStream.
	 * @return instance unmarshalled from the inputStream.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	<T> T read(InputStream inputStream, Class<T> clazz) throws IOException,
			ClassNotFoundException;
	/**
	 * Write a domain data object to outputStream.
	 * 
	 * @param dataObject a domain data object.
	 * @param outputStream representation outputStream.
	 * @throws IOException
	 */
	void write(Object dataObject, OutputStream outputStream) throws IOException;
	/**
	 * Set the class loader this marshaller used when unmarshalling a 
	 * representation inputStream.
	 * 
	 * @param classLoader
	 */
	void setClassLoader(ClassLoader classLoader);

}
