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
package org.opengoss.core.internal.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengoss.core.IApplicationExtensionRegistry;
import org.opengoss.core.IApplicationServiceRegistry;
import org.opengoss.core.Registry;
import org.opengoss.core.UID;
import org.opengoss.core.internal.Extension;
import org.opengoss.core.internal.ExtensionPoint;
/**
 * Application level extension registry.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public class ApplicationExtensionRegistry extends Registry
	implements IApplicationExtensionRegistry {
	
	private Map<UID, Extension> extensionMap 
		= new HashMap<UID, Extension>();
	
	private Map<UID, ExtensionPoint> extensionPointMap 
		= new HashMap<UID, ExtensionPoint>(); 
	
	public ApplicationExtensionRegistry() {
		super(null);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#addExtension(org.opengoss.core.internal.Extension)
	 */
	public void addExtension(Extension extension) {
		extensionMap.put(extension.getDescriptor().getGlobalUid(), extension);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#removeExtension(org.opengoss.core.internal.Extension)
	 */
	public void removeExtension(Extension extension) {
		extensionMap.remove(extension.getDescriptor().getGlobalUid());
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#addExtensionPoint(org.opengoss.core.internal.ExtensionPoint)
	 */
	public void addExtensionPoint(ExtensionPoint extensionPoint) {
		extensionPointMap.put(extensionPoint.getDescriptor().getGlobalUid(), extensionPoint);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#removeExtensionPoint(org.opengoss.core.internal.ExtensionPoint)
	 */
	public void removeExtensionPoint(ExtensionPoint extensionPoint) {
		extensionPointMap.remove(extensionPoint.getDescriptor().getGlobalUid());
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getExtension(org.opengoss.core.UID)
	 */
	public Extension getExtension(UID uid) {
		return extensionMap.get(uid);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getExtensionPoint(org.opengoss.core.UID)
	 */
	public ExtensionPoint getExtensionPoint(UID uid) {
		return extensionPointMap.get(uid);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getExtensions(org.opengoss.core.UID)
	 */
	public Extension[] getExtensions(UID pointUid) {
		Collection<Extension> allExtensions = extensionMap.values();
		List<Extension> selectedExtensions = new ArrayList<Extension>();
		for (Extension extension : allExtensions) {
			if(pointUid.equals(extension.getDescriptor().getGlobalUid())) {
				selectedExtensions.add(extension);
			}
		}
		return selectedExtensions.toArray(new Extension[selectedExtensions.size()]);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getServiceRegistry()
	 */
	public IApplicationServiceRegistry getServiceRegistry() {
		throw new UnsupportedOperationException();
	}
	
}
