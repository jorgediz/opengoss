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

import java.net.URL;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
/**
 * XML parser that parse "META-MF/plugin.xml" file.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0.
 * @since 2006-11-20
 */
public class XmlParser {
	/**
	 * Parse XML file and build the <code>PluginDescriptor</code>.
	 * 
	 * @see org.opengoss.core.internal.descriptor.PluginDescriptor
	 * @param xml "META-MF/plugin.xml" file
	 * @return pluginDescriptor
	 * @throws Exception
	 */
	public static PluginDescriptor parse(URL xml) throws Exception {
		Document document = new Builder().build(xml.openStream());
		Element element = document.getRootElement();
		return new PluginDescriptor(element);
	}

}
