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
package org.opengoss.core.internal.descriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import nu.xom.Elements;

import org.opengoss.core.util.StringUtils;

/**
 * Service descriptor.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public final class ServiceDescriptor extends IdentiableXmlDescriptor {

	private String clazz;

	private ServiceScope scope;

	List<String> intfList = new ArrayList<String>(3);

	ConstructorDescriptor constructorDescriptor = null;

	Map<String, PropertyDescriptor> propertyDescriptorMap 
		= new HashMap<String, PropertyDescriptor>(5);

	public ServiceDescriptor(Element element) throws 
			ParseException {
		super(element);
		String scopeStr = element.getAttributeValue(TAG_SCOPE);
		if (StringUtils.isEmpty(scopeStr)) {
			this.scope = ServiceScope.PLUGIN;
		} else {
			this.scope = ServiceScope.valueOf(scopeStr.toUpperCase());
		}
		this.clazz = element.getAttributeValue(TAG_CLASS);
		if (StringUtils.isEmpty(clazz)) {
			throw new ParseException("No 'class' property!");
		}
		addInterfaceDescriptors(element);
		parseConstructorDescriptor(element);
		addPropertyDescriptors(element);
	}

	private void parseConstructorDescriptor(Element element)
			throws ParseException {
		Elements elements= element.getChildElements("constructor");
		if (elements != null && elements.size() == 1) {
			constructorDescriptor = new ConstructorDescriptor((Element)elements.get(0));
		}
	}

	public String getClazz() {
		return clazz;
	}

	public ServiceScope getScope() {
		return scope;
	}

	public String[] getInterfaces() {
		return intfList.toArray(new String[intfList.size()]);
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptorMap.values().toArray(
				new PropertyDescriptor[propertyDescriptorMap.size()]);
	}
	
	public boolean isConstructorInjection()  {
		return constructorDescriptor != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(this instanceof ServiceDescriptor)) {
			return false;
		}
		ServiceDescriptor descriptor = (ServiceDescriptor) obj;
		return getGlobalUid().equals(descriptor.getGlobalUid());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ServiceDescriptor[");
		sb.append("id=").append(getUid()).append(",");
		sb.append("class=").append(clazz).append("]");
		return sb.toString();
	}

	private void addInterfaceDescriptors(Element element) {
		Elements elements = element.getChildElements(TAG_INTERFACE);
		for (int i = 0; i < elements.size(); i++) {
			Element intfElement = (Element) elements.get(i);
			intfList.add(intfElement.getAttributeValue("name"));
		}
	}

	private void addPropertyDescriptors(Element serviceElement)
			throws ParseException {
		Elements elements = serviceElement.getChildElements(TAG_PROPERTY);
		for (int i = 0; i < elements.size(); i++) {
			Element element = (Element) elements.get(i);
			PropertyDescriptor propDescriptor = new PropertyDescriptor(element);
			propertyDescriptorMap.put(propDescriptor.getName(), propDescriptor);
		}
	}

	public ConstructorDescriptor getConstructorDescriptor() {
		return constructorDescriptor;
	}

}
