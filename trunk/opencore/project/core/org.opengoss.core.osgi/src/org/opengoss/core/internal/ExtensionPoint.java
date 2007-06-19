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

import org.opengoss.core.UID;
import org.opengoss.core.internal.descriptor.ExtensionPointDescriptor;
import org.opengoss.core.internal.registry.PluginExtensionRegistry;
import org.opengoss.core.internal.registry.RegistryObject;
import org.opengoss.core.util.AssertException;

/**
 * Extension Point.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
public class ExtensionPoint extends RegistryObject {

	private Object service = null;
	
	private Method bindMethod = null;
	
	private Method unbindMethod = null;
	
	private final ExtensionPointDescriptor descriptor;

	public ExtensionPoint(PluginExtensionRegistry registry, 
			ExtensionPointDescriptor descriptor) {
		super(registry);
		this.descriptor = descriptor;
	}

	public ExtensionPointDescriptor getDescriptor() {
		return descriptor;
	}
	
	public Method getBindMethod() {
		if(bindMethod == null) {
			init();
		}
		return bindMethod;
	}

	void setBindMethod(Method addMethod) {
		this.bindMethod = addMethod;
	}

	public Method getUnbindMethod() {
		if(unbindMethod == null) {
			init();
		}
		return unbindMethod;
	}

	void setUnbindMethod(Method removeMethod) {
		this.unbindMethod = removeMethod;
	}

	public Object getService() {
		if(service == null) {
			init();
		}
		return service;
	}

	void setService(Object service) {
		this.service = service;
	}

	public void bindExtention(Extension extension) throws Exception {
		getBindMethod().invoke(service, extension.getParams());
	}
	
	public void unbindExtension(Extension extension) throws Exception {
		getUnbindMethod().invoke(service, extension.getParams());
	}

	private void init() {
		UID target = descriptor.getTarget();
		Object service = ((PluginExtensionRegistry)getRegistry()).getServiceRegistry().getService(target.getId());
		if (service == null) {
			throw new AssertException(
					"No target service found for extension point: " + descriptor.getGlobalUid());
		}
		setService(service);
		Method[] methods = service.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(descriptor.getAddMethod())) {
				setBindMethod(method);
			} else if (method.getName().equals(descriptor.getRemoveMethod())) {
				setUnbindMethod(method);
			}
		}
	}
	
}
