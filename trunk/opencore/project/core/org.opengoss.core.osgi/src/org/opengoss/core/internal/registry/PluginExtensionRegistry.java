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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.opengoss.core.IExtensionRegistry;
import org.opengoss.core.IPluginExtensionRegistry;
import org.opengoss.core.IPluginServiceRegistry;
import org.opengoss.core.Registry;
import org.opengoss.core.UID;
import org.opengoss.core.internal.Extension;
import org.opengoss.core.internal.ExtensionPoint;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
/**
 * Plugin level extension registry.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public class PluginExtensionRegistry extends Registry 
	implements IPluginExtensionRegistry {
	
	private BundleContext context;
	
	private IPluginServiceRegistry serviceRegistry;
	
	private Map<UID, Extension> extensionMap 
		= new HashMap<UID, Extension>(5);
	
	private Map<UID, ExtensionPoint> extensionPointMap 
		= new HashMap<UID, ExtensionPoint>(5);

	public PluginExtensionRegistry(BundleContext context, 
			IExtensionRegistry parent) {
		super(parent);
		this.context = context;
	}

	public BundleContext getBundleContext() {
		return context;
	}

	public Bundle getBundle() {
		return this.context.getBundle();
	}

	public void setServiceRegistry(IPluginServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getServiceRegistry()
	 */
	public IPluginServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#addExtensionPoint(org.opengoss.core.internal.ExtensionPoint)
	 */
	public void addExtensionPoint(ExtensionPoint extensionPoint) {
		extensionPointMap.put(extensionPoint.getDescriptor().getUid(), extensionPoint);
		((IExtensionRegistry)getParent()).addExtensionPoint(extensionPoint);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#removeExtensionPoint(org.opengoss.core.internal.ExtensionPoint)
	 */
	public void removeExtensionPoint(ExtensionPoint extensionPoint) {
		((IExtensionRegistry)getParent()).removeExtensionPoint(extensionPoint);
		extensionPointMap.remove(extensionPoint);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#addExtension(org.opengoss.core.internal.Extension)
	 */
	public void addExtension(Extension extension) {
		extensionMap.put(extension.getDescriptor().getUid(), extension);
		((IExtensionRegistry)getParent()).addExtension(extension);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#removeExtension(org.opengoss.core.internal.Extension)
	 */
	public void removeExtension(Extension extension) {
		((IExtensionRegistry)getParent()).removeExtension(extension);
		extensionMap.remove(extension);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getExtension(org.opengoss.core.UID)
	 */
	public Extension getExtension(UID uid) {
		Extension extension = extensionMap.get(uid);
		if(extension == null) {
			extension = ((IExtensionRegistry)getParent()).getExtension(uid);
		}
		return extension;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getExtensionPoint(org.opengoss.core.UID)
	 */
	public ExtensionPoint getExtensionPoint(UID uid) {
		ExtensionPoint extensionPoint = extensionPointMap.get(uid);
		if(extensionPoint == null) {
			extensionPoint = ((IExtensionRegistry)getParent()).getExtensionPoint(uid);
		}
		return extensionPoint;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.core.IExtensionRegistry#getExtensions(org.opengoss.core.UID)
	 */
	public Extension[] getExtensions(UID pointUid) {
		return ((IExtensionRegistry)getParent()).getExtensions(pointUid);
	}
	/**
	 * Build the extention registry.
	 * 
	 * @param builder extension registry builder.
	 * @throws Exception
	 */
	public void build(ExtensionRegistryBuilder builder) throws Exception {
		builder.build(this);
	}
	/**
	 * Destroy the extension registry.
	 * @throws Exception
	 */
	public void destroy() throws Exception {
		destroyExtensions();
		destroyExtensionPoints();
	}

	private void destroyExtensions() throws Exception {
		//destory extensions
		Set<UID> uids = extensionMap.keySet();
		for (UID uid : uids) {
			//unbind extension
			Extension extension = extensionMap.get(uid);
			UID pointUid = extension.getDescriptor().getPointUid();
			ExtensionPoint extensionPoint = getExtensionPoint(pointUid);
			if(extensionPoint != null) {
				extensionPoint.unbindExtension(extension);
			}
			//remove extension
			removeExtension(extension);
		}
		//ensure to clear extension map
		extensionMap.clear();
	}

	private void destroyExtensionPoints() throws Exception {
		//destroy extensionPoints
		Set<UID> uids = extensionPointMap.keySet();
		for (UID uid : uids) {
			//unbind extensionPoint
			ExtensionPoint extensionPoint = extensionPointMap.get(uid);
			Extension[] extensions = getExtensions(extensionPoint.getDescriptor().getGlobalUid());
			for (Extension extension : extensions) {
				extensionPoint.unbindExtension(extension);
			}
			//remove extensionPoint
			removeExtensionPoint(extensionPoint);
		}
		//ensure to clear
		extensionPointMap.clear();
	}

}
