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
 * Property descriptor.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public final class PropertyDescriptor extends XmlDescriptor {

	private String name;

	private UID refUID;

	private String value;

	private String refIntf;

	public PropertyDescriptor(Element element) throws ParseException {
		name = element.getAttributeValue(TAG_NAME);
		if (StringUtils.isEmpty(name)) {
			throw new ParseException("!No 'name' tag!");
		}
		value = element.getAttributeValue(TAG_VALUE);
		String refStr = element.getAttributeValue(TAG_REF);
		if (StringUtils.isEmpty(value)
				&& StringUtils.isEmpty(refStr)) {
			throw new ParseException("!No 'value' and 'ref' tag!");
		}
		if (!StringUtils.isEmpty(refStr)) {
			if(refStr.startsWith(INTF_PREFIX)) {
				this.refIntf = refStr.substring(INTF_PREFIX.length());
			} else if(refStr.startsWith(UID_PREFIX)){
				this.refUID = new UID(refStr.substring(UID_PREFIX.length()));
			} else {
				this.refUID = new UID(refStr);
			}
		}
	}

	public String getName() {
		return name;
	}

	public UID getRefUID() {
		return refUID;
	}
	
	public String getRefIntf() {
		return refIntf;
	}

	public String getValue() {
		return value;
	}

	public boolean isUidRef() {
		return refUID != null;
	}
	
	public boolean isIntfRef() {
		return refIntf != null;
	}

	public UID getGlobalUid() {
		throw new UnsupportedOperationException();
	}
	
	public UID getUid() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(this instanceof PropertyDescriptor)) {
			return false;
		}
		PropertyDescriptor descriptor = (PropertyDescriptor) obj;
		return this.name.equals(descriptor.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PropertyDescriptor[");
		sb.append("name=").append(name).append(",");
		sb.append("value=").append(value == null ? "null" : value).append(",");
		sb.append("ref=").append(refUID == null ? "null" : refUID).append(",");
		return sb.toString();
	}

}
