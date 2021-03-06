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

import nu.xom.Element;
import nu.xom.Elements;

import org.opengoss.web.service.URIPattern;
/**
 * XML style web method metadata.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class XmlWMethodMeta extends AbstractWMethodMeta {

	private Element element;

	public XmlWMethodMeta(Method method, Element element) {
		super(method);
		this.element = element;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWMethodMeta#getUriPattern()
	 */
	public URIPattern getUriPattern() {
		return new URIPattern(element.getAttributeValue("uri"));
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWMethodMeta#getMethodType()
	 */
	public String getMethodType() {
		String type = element.getAttributeValue("type");
		return type == null ? "GET" : type;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWMethodMeta#getParamMetas()
	 */
	public IWParamMeta[] getParamMetas() {
		Elements elements = element.getChildElements();
		IWParamMeta[] paramMetas = new XmlWParamMeta[elements.size()];
		for(int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			paramMetas[i] = new XmlWParamMeta(element);
		}
		return paramMetas;
	}

}
