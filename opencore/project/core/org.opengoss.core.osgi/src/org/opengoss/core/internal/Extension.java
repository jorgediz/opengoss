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
package org.opengoss.core.internal;

import java.lang.reflect.Method;

import org.opengoss.core.internal.descriptor.ExtensionDescriptor;
import org.opengoss.core.internal.descriptor.ParamDescriptor;
import org.opengoss.core.internal.registry.PluginExtensionRegistry;
import org.opengoss.core.internal.registry.RegistryObject;
import org.opengoss.core.util.Convertor;

/**
 * Extension.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public class Extension extends RegistryObject {

	private Object[] params = null;

	private ExtensionDescriptor descriptor;

	public Extension(PluginExtensionRegistry registry,
			ExtensionDescriptor descriptor) {
		super(registry);
		this.descriptor = descriptor;
	}

	public ExtensionDescriptor getDescriptor() {
		return descriptor;
	}

	public Object[] getParams() throws Exception {
		if(params == null) {
			params = initParams();
		}
		return params;
	}

	@SuppressWarnings("unchecked")
	private Object[] initParams() throws Exception {
		ParamDescriptor[] paramDescriptors = descriptor.getParamDescriptors();
		ExtensionPoint extensionPoint = getExtensionRegistry()
			.getExtensionPoint(descriptor.getPointUid());
		Method addMethod = extensionPoint.getBindMethod();
		Class<?>[] types = addMethod.getParameterTypes();
		Object[] params = new Object[types.length];
		for (int i = 0; i < paramDescriptors.length; i++) {
			if(paramDescriptors[i].isUidRef()) {
				params[i] = getExtensionRegistry().getServiceRegistry()
					.getService(paramDescriptors[i].getRefUID().getId());
			} else if(paramDescriptors[i].isIntfRef()){
				Class intf = getIntf(paramDescriptors[i].getRefIntf());
				params[i] = getExtensionRegistry().getServiceRegistry().getService(intf);
			} else if(paramDescriptors[i].isClazz()) {
				params[i] = getExtensionRegistry().getBundle()
				.loadClass(paramDescriptors[i].getClazz()).newInstance();
			} else { //value tag
				if(types[i] == Class.class) {
					params[i] = getExtensionRegistry().getBundle().loadClass(paramDescriptors[i].getValue());
				} else {
					params[i] = Convertor.convert(types[i], paramDescriptors[i].getValue());
				}
			}
		}
		return params;
	}
	
	@SuppressWarnings("unchecked")
	private Class getIntf(String intf) throws ClassNotFoundException {
		return getExtensionRegistry().getBundle().loadClass(intf);
	}
	
	private PluginExtensionRegistry getExtensionRegistry() {
		return (PluginExtensionRegistry)getRegistry();
	}

}
