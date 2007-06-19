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
package org.opengoss.core;

import org.opengoss.core.internal.Extension;
import org.opengoss.core.internal.ExtensionPoint;
/**
 * ExtensionPoint and extension registry.
 * 
 * <p>
 * Responsibilities of this registry:
 * <ul>
 * 	<li>Register or unregister extension points and extensions.</li>
 * 	<li>Get extension points and extesions.</li>
 * </ul>
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public interface IExtensionRegistry extends IRegistry {
	/**
	 * Get service registry this extention registry depends on.
	 * 
	 * @return service registry.
	 */
	IRegistry getServiceRegistry();
	/**
	 * Get an extension by UID.
	 * 
	 * @param uid uid of the extension.
	 * @return extension 
	 */
	Extension getExtension(UID uid);
	/**
	 * Get an exteionpoint by UID.
	 * 
	 * @param uid uid of the extension.
	 * @return extensionPoint
	 */
	ExtensionPoint getExtensionPoint(UID uid);
	/**
	 * Add an extension point.
	 * 
	 * @param extensionPoint
	 */
	void addExtensionPoint(ExtensionPoint extensionPoint);
	/**
	 * Add an extension.
	 * 
	 * @param extension
	 */
	void addExtension(Extension extension);
	/**
	 * Get the extensions that belong to the extention point that the 
	 * <code>pointUid</code> specified.
	 * 
	 * @param pointUid point uid
	 * @return extensions
	 */
	Extension[] getExtensions(UID pointUid);
	/**
	 * Remove an extension.
	 * 
	 * @param extension
	 */
	void removeExtension(Extension extension);
	/**
	 * Remove an extension point.
	 * 
	 * @param extensionPoint
	 */
	void removeExtensionPoint(ExtensionPoint extensionPoint);
	
}
