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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.core.internal.Extension;
import org.opengoss.core.internal.ExtensionPoint;
import org.opengoss.core.internal.descriptor.ExtensionDescriptor;
import org.opengoss.core.internal.descriptor.ExtensionPointDescriptor;
import org.opengoss.core.internal.descriptor.PluginDescriptor;
/**
 * Builder of the {@link org.opengoss.core.internal.registry.PluginExtensionRegistry}
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public class ExtensionRegistryBuilder {
	
	static final Log log = LogFactory.getLog(ExtensionRegistryBuilder.class);

	private PluginDescriptor pluginDescriptor;

	public ExtensionRegistryBuilder(PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}
	/**
	 * Build the extension registry.
	 * 
	 * @param extensionRegistry
	 * @throws Exception
	 */
	public void build(PluginExtensionRegistry extensionRegistry) 
		throws Exception {
		buildExtentionPoints(extensionRegistry);
		buildExtentions(extensionRegistry);
	}

	private void buildExtentionPoints(PluginExtensionRegistry extensionRegistry) throws Exception {
		ExtensionPointDescriptor[] pointDescriptors = pluginDescriptor.getExtensionPointDescriptors();
		for (ExtensionPointDescriptor pointDescriptor : pointDescriptors) {
			ExtensionPoint extensionPoint = new ExtensionPoint(extensionRegistry, pointDescriptor);
			
			Extension[] extensions = extensionRegistry.getExtensions(pointDescriptor.getGlobalUid());
			bind(extensionPoint, extensions);
			
			extensionRegistry.addExtensionPoint(extensionPoint);
		}
	}

	private void buildExtentions(PluginExtensionRegistry extensionRegistry) throws Exception {
		ExtensionDescriptor[] extensionDescriptors = pluginDescriptor.getExtensionDescriptors();
		for (ExtensionDescriptor extensionDescriptor : extensionDescriptors) {
			Extension extension = new Extension(extensionRegistry, extensionDescriptor);
			ExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(extensionDescriptor.getPointUid());
			if(extensionPoint == null) {
				log.warn("Cannot find the extensionPoint:" 
						+ extension.getDescriptor().getPointUid());
			} else {
				bind(extensionPoint, new Extension[]{ extension });
			}
			extensionRegistry.addExtension(extension);
		}
	}

	private void bind(ExtensionPoint extensionPoint, Extension[] extensions) 
		throws Exception {
		for (Extension extension : extensions) {
			extensionPoint.bindExtention(extension);
		}
	}

}
