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

import nu.xom.Element;

import org.opengoss.core.UID;
import org.opengoss.core.util.StringUtils;
/**
 * Extension Point descriptor.
 * 
 * <p>
 * <XML configuration likes:
 * </p>
 * <p>
 * &lt;extension-point id=&quot;pointId&quot; target=&quot;serviceId&quot; 
 *	bindMethod=&quot;addExtension&quot; unbindMethod=&quot;removeExtension&quot;/&gt;
 *	&lt;/extension-point&gt;
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */

@SuppressWarnings("serial")
public final class ExtensionPointDescriptor extends IdentiableXmlDescriptor {

	private UID target;

	private String addMethod;

	private String removeMethod;

	public ExtensionPointDescriptor(Element element)
			throws ParseException {
		super(element);
		target = new UID(element.getAttributeValue(TAG_TARGET));
		addMethod = element.getAttributeValue(TAG_BINDMETHOD);
		if (StringUtils.isEmpty(addMethod)) {
			throw new ParseException("Empty 'bindMethod' property!");
		}
		removeMethod = element.getAttributeValue(TAG_UNBINDMETHOD);
		if (StringUtils.isEmpty(removeMethod)) {
			throw new ParseException("Empty 'unbindMethod' property!");
		}
	}

	public String getAddMethod() {
		return addMethod;
	}

	public String getRemoveMethod() {
		return removeMethod;
	}

	public UID getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ExtensionPointDescriptor)) {
			return false;
		}
		ExtensionPointDescriptor epd = (ExtensionPointDescriptor) obj;
		return getUid().equals(epd.getUid());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ExtensionPointDescriptor[");
		sb.append("id=").append(getUid()).append(",");
		sb.append("target=").append(getTarget()).append(",");
		sb.append("bindMethod=").append(getAddMethod()).append(",");
		sb.append("unbindMethod=").append(getRemoveMethod()).append("]");
		return sb.toString();
	}

}
