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

/**
 * Extension descriptor.
 * 
 * <p>
 * <XML configuration likes:
 * </p>
 * <p>
 * &lt;extension id=&quot;extensionId&quot; point=&quot;pointId&quot;&gt;
		&lt;param/&gt;
 * &lt;/extension&gt;
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public final class ExtensionDescriptor extends IdentiableXmlDescriptor {

	private UID pointUid;

	private List<ParamDescriptor> paramList = new ArrayList<ParamDescriptor>(5);

	public ExtensionDescriptor(Element element) throws ParseException {
		super(element);
		pointUid = new UID(element.getAttributeValue("point"));
		buildParams(element);
	}

	private void buildParams(Element element) throws ParseException {
		Elements elements = element.getChildElements(TAG_PARAM);
		for (int i = 0; i < elements.size(); i++) {
			Element paramElement = (Element) elements.get(i);
			paramList.add(new ParamDescriptor(paramElement));
		}
	}

	public ParamDescriptor[] getParamDescriptors() {
		return paramList.toArray(new ParamDescriptor[paramList.size()]);
	}

	public UID getPointUid() {
		return pointUid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ExtensionDescriptor))
			return false;
		ExtensionDescriptor ed = (ExtensionDescriptor) obj;
		return getGlobalUid().equals(ed.getGlobalUid());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ExtensionDescriptor[");
		sb.append("id=").append(getUid()).append(",");
		sb.append("point=").append(getPointUid()).append("]");
		return sb.toString();
	}

}
