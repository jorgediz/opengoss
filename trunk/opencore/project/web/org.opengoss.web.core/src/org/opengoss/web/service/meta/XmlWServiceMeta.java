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
package org.opengoss.web.service.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
/**
 * XML style web service meta.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class XmlWServiceMeta extends AbstractWServiceMeta {

	private final Element xmlRoot;

	private List<XmlWMethodMeta> xmlWMMetas = new ArrayList<XmlWMethodMeta>();

	public XmlWServiceMeta(Class<?> clazz, Document xmlDoc) {
		super(clazz);
		this.xmlRoot = xmlDoc.getRootElement();
		Method[] methods = getClazz().getMethods();
		for (Method method : methods) {
			Element wmElement = findWMElement(method);
			if (wmElement != null) {
				xmlWMMetas.add(new XmlWMethodMeta(method, wmElement));
			}
		}
	}

	private Element findWMElement(Method method) {
		Elements elements = xmlRoot.getChildElements();
		for (int i = 0; i < elements.size(); i++) {
			Element wmElement = elements.get(i);
			Elements wpElements = wmElement.getChildElements();
			int wpSize = wpElements.size();
			if (method.getName().equals(wmElement.getAttributeValue("invoke"))
					&& wpSize == method.getParameterTypes().length) {
				return wmElement;
			}
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWServiceMeta#getServiceUri()
	 */
	public String getServiceUri() {
		return xmlRoot.getAttributeValue("uri");
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWServiceMeta#getServiceName()
	 */
	public String getServiceName() {
		return xmlRoot.getAttributeValue("name");
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWServiceMeta#getWMMetas()
	 */
	public IWMethodMeta[] getWMMetas() {
		return xmlWMMetas.toArray(new IWMethodMeta[xmlWMMetas.size()]);
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWServiceMeta#findWMMeta(java.lang.String, java.lang.String)
	 */
	public IWMethodMeta findWMMeta(String type, String uri) {
		IWMethodMeta[] wmMetas = getWMMetas();
		for (IWMethodMeta wmMeta : wmMetas) {
			if (wmMeta.getMethodType().equals(type)
					&& wmMeta.getUriPattern().match(uri).isMatched()) {
				return wmMeta;
			}
		}
		return null;
	}

}
