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
package org.opengoss.web.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.opengoss.web.service.IMarshaller;
import org.opengoss.web.service.IMarshallerRegistry;
import org.opengoss.web.service.IWSContainer;
import org.opengoss.web.service.IWServicelet;
/**
 * Implementation of {@link org.opengoss.web.service.IWSContainer}
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class WSContainer implements IWSContainer {
	
	private IMarshallerRegistry marshallerRegistry;
	
	private Map<String, IWServicelet> services = new HashMap<String, IWServicelet>();
	
	public WSContainer() {
	}

	public IMarshallerRegistry getMarshallerRegistry() {
		return marshallerRegistry;
	}

	public void setMarshallerRegistry(IMarshallerRegistry marshallerRegistry) {
		this.marshallerRegistry = marshallerRegistry;
	}

	public void add(IWServicelet servicelet) throws Exception {
		services.put(servicelet.getUri(), servicelet);
	}

	public void remove(IWServicelet servicelet) throws Exception {
		services.remove(servicelet);
	}

	public IMarshaller getMarshaller(String view) {
		return marshallerRegistry.getMarshaller(view);
	}

	public IWServicelet getWServicelet(String pathInfo) {
		Set<String> keys = services.keySet();;
		for (String key : keys) {
			if( pathInfo.startsWith(key) ) {
				return services.get(key);
			}
		}
		return null;
	}
	
}
