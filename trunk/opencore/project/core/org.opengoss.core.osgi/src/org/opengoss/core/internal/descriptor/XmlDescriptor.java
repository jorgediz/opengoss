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

/**
 * XML descriptor that defines tag names.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0
 * @since 2006-11-20
 */
@SuppressWarnings("serial")
public class XmlDescriptor implements IDescriptor {

	static final String TAG_URI = "uri";

	static final String TAG_SERVICE = "service";

	static final String TAG_EXTENSION = "dynamic-extension";

	static final String TAG_EXTENSION_POINT = "dynamic-extension-point";

	static final String TAG_PARAM = "param";

	static final String TAG_TARGET = "target";
	
	static final String TAG_BINDMETHOD = "bindMethod";

	static final String TAG_UNBINDMETHOD = "unbindMethod";

	static final String TAG_REF = "ref";

	static final String TAG_VALUE = "value";

	static final String TAG_NAME = "name";

	static final String TAG_SCOPE = "scope";

	static final String TAG_CLASS = "class";

	static final String TAG_PROPERTY = "property";

	static final String TAG_INTERFACE = "interface";
	
	static final String INTF_PREFIX = "intf:";
	
	static final String UID_PREFIX = "id:";

	private IDescriptor parent = null;

	public void setParent(IDescriptor parent) {
		this.parent = parent;
	}

	public IDescriptor getParent() {
		return parent;
	}
	
}
