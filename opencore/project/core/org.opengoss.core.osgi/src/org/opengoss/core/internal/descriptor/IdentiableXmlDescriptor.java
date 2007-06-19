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
/**
 * Xml Descriptor.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public class IdentiableXmlDescriptor extends XmlDescriptor 
	implements IdentiableDescriptor {

	static final String TAG_ID = "id";

	private UID uid;
	
	public IdentiableXmlDescriptor(Element element) {
		uid = new UID(element.getAttributeValue(TAG_ID));
	}

	public UID getUid() {
		return uid;
	}

	public UID getGlobalUid() {
		UID globalUid = uid;
		if(getParent() != null) {
			globalUid = ((IdentiableDescriptor)getParent()).getGlobalUid().append(uid);
		}
		return globalUid;
	}


	public int hashCode() {
		return getGlobalUid().hashCode();
	}

}
