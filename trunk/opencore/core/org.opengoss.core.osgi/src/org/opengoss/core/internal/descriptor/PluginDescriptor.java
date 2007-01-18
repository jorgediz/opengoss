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
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;

import org.opengoss.core.UID;
import org.opengoss.core.UID.MalformedUIDException;

/**
 * Plugin descriptor.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @uri 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public class PluginDescriptor extends IdentiableXmlDescriptor {

	private String uri;

	private List<ServiceDescriptor> sdList = new ArrayList<ServiceDescriptor>(5);

	private List<ExtensionPointDescriptor> epdList = new ArrayList<ExtensionPointDescriptor>(
			5);

	private List<ExtensionDescriptor> edList = new ArrayList<ExtensionDescriptor>(
			5);

	public PluginDescriptor(Element element) throws ParseException {
		super(element);
		uri = element.getAttributeValue(TAG_URI);
		addServiceDescriptors(element);
		addExtensionDescriptor(element);
		addExtensionPointDescriptor(element);
	}

	public String getUri() {
		return uri;
	}

	private void addServiceDescriptors(Element pluginElement)
			throws ParseException {
		Elements elements = pluginElement.getChildElements(TAG_SERVICE);
		for (int i = 0; i < elements.size(); i++) {
			Element e = (Element) elements.get(i);
			ServiceDescriptor serviceDescriptor = new ServiceDescriptor(e);
			serviceDescriptor.setParent(this);
			sdList.add(serviceDescriptor);
		}
	}

	private void addExtensionDescriptor(Element element) throws ParseException,
			MalformedUIDException {
		Elements elements = element.getChildElements(TAG_EXTENSION);
		for (int i = 0; i < elements.size(); i++) {
			Element e = (Element) elements.get(i);
			ExtensionDescriptor descriptor = new ExtensionDescriptor(e);
			descriptor.setParent(this);
			edList.add(descriptor);
		}
	}

	private void addExtensionPointDescriptor(Element element)
			throws MalformedUIDException, ParseException {
		Elements elements = element.getChildElements(TAG_EXTENSION_POINT);
		for (int i = 0; i < elements.size(); i++) {
			Element e = (Element) elements.get(i);
			ExtensionPointDescriptor descriptor = new ExtensionPointDescriptor(
					e);
			descriptor.setParent(this);
			epdList.add(descriptor);
		}
	}

	public ServiceDescriptor[] getServiceDescriptors() {
		return sdList.toArray(new ServiceDescriptor[sdList.size()]);
	}

	public ExtensionDescriptor[] getExtensionDescriptors() {
		return edList.toArray(new ExtensionDescriptor[edList.size()]);
	}

	public ExtensionPointDescriptor[] getExtensionPointDescriptors() {
		return epdList.toArray(new ExtensionPointDescriptor[epdList.size()]);
	}

	public ExtensionDescriptor getExtensionDescriptor(UID uid) {
		for (ExtensionDescriptor ed : edList) {
			if (uid.equals(ed.getUid())) {
				return ed;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PluginDescriptor)) {
			return false;
		}
		PluginDescriptor descriptor = (PluginDescriptor) obj;
		return getGlobalUid().equals(descriptor.getGlobalUid());
	}

	@Override
	public String toString() {
		return new StringBuilder("PluginDescriptor[id=").append(getUid())
				.append(", uri=").append(uri).append("]").toString();
	}

}
