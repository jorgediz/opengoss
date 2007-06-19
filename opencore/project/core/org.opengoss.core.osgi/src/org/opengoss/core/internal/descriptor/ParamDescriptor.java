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
 * Parameter descriptor.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public class ParamDescriptor extends XmlDescriptor {

	private UID refUID;

	private String refIntf;

	private String value;

	private String clazz;

	public ParamDescriptor(Element element) throws ParseException {
		String refStr = element.getAttributeValue(TAG_REF);
		clazz = element.getAttributeValue(TAG_CLASS);
		value = element.getAttributeValue(TAG_VALUE);
		if (StringUtils.isEmpty(value)
				&& StringUtils.isEmpty(refStr)
				&& StringUtils.isEmpty(clazz)) {
			throw new ParseException("!No 'value' or 'ref' tag!");
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
	
	public boolean isClazz() {
		return !StringUtils.isEmpty(clazz);
	}

	public String getClazz() {
		return clazz;
	}

}
