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

import org.opengoss.web.service.IAliasChangeListener;
import org.opengoss.web.service.IMarshaller;
import org.opengoss.web.service.IMarshallerRegistry;
/**
 * Implementation of {@link org.opengoss.web.service.IMarshallerRegistry}
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class MarshallerRegistry implements IMarshallerRegistry {

	Map<String, IMarshaller> marshallerMap = new HashMap<String, IMarshaller>(5);
	
	Map<String, String> mediaTypeMap = new HashMap<String, String>(5);
	
	Map<String, Class> aliasMap = new HashMap<String, Class>();

	private MarshallerClassLoader marshallerClassLoader;
	
	public MarshallerRegistry() {
		marshallerClassLoader = new MarshallerClassLoader(MarshallerRegistry.class.getClassLoader());
		addMarshaller(new SerializableMarshaller());
		putMime(SerializableMarshaller.VIEW, "application/x-java-serialized-object");
	}
	
	public void addClass(Class clazz) {
		addAliasClass(clazz.getName(), clazz);
	}
	
	public void addAliasClass(String alias, Class clazz) {
		aliasMap.put(alias, clazz);
		fireAliasAddEvent(alias, clazz);
		marshallerClassLoader.contributeClass(clazz);
	}

	public void removeClass(Class clazz) {
		removeAliasClass(clazz.getName(), clazz);
	}

	public void removeAliasClass(String alias, Class clazz) {
		aliasMap.remove(alias);
		fireAliasRemoveEvent(alias);
		marshallerClassLoader.removeClass(clazz);
	}

	public IMarshaller getMarshaller(String mediaType) {
		return marshallerMap.get(mediaType);
	}

	public void addMarshaller(IMarshaller marshaller) {
		marshallerMap.put(marshaller.getRepresentation(), marshaller);
		marshaller.setClassLoader(marshallerClassLoader);
	}
	
	public void removeMashaller(IMarshaller marshaller) {
		marshallerMap.remove(marshaller.getRepresentation());
	}
	
	private void fireAliasAddEvent(String alias, Class clazz) {
		IMarshaller[] marshallers = marshallerMap.values().toArray(
				new IMarshaller[marshallerMap.size()]);
		for (IMarshaller marshaller : marshallers) {
			if(marshaller instanceof IAliasChangeListener) {
				((IAliasChangeListener)marshaller).onAliasAdd(alias, clazz);
			}
		}
	}
	
	private void fireAliasRemoveEvent(String alias) {
		IMarshaller[] marshallers = marshallerMap.values().toArray(
				new IMarshaller[marshallerMap.size()]);
		for (IMarshaller marshaller : marshallers) {
			if(marshaller instanceof IAliasChangeListener) {
				((IAliasChangeListener)marshaller).onAliasRemove(alias);
			}
		}
	}

	public String getMime(String view) {
		return mediaTypeMap.get(view);
	}

	public void putMime(String view, String mediaType) {
		mediaTypeMap.put(view, mediaType);
	}


	public ClassLoader getMarshallerClassLoader() {
		return marshallerClassLoader;
	}
	
}
