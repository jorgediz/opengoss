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
package org.opengoss.web.service;
/**
 * URI pattern to match URI path. Parse the path to a <code>URIMatcher</code>.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class URIPattern {
	
	String[] parts;
	
	public URIPattern(String uri) {
		parts = uri.split("/");
	}
	
	public URIMatcher match(String uri) {
		URIMatcher matcher = new URIMatcher();
		if(uri != null) {
			String[] strs = uri.split("/");
			if(strs.length != parts.length) {
				matcher.setMatched(false);
				return matcher;
			}
			int i = 0;
			for (; i < parts.length; i++) {
				if(parts[i].startsWith("$")) {
					matcher.put(getKey(parts[i]), strs[i]);
				} else if( !parts[i].equalsIgnoreCase(strs[i]) ) {
					break;
				} else {
					//nothing
				}
			}
			matcher.setMatched(i == parts.length);
		}
		return matcher;
	}

	private String getKey(String part) {
		return part.substring(1);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.length; i++) {
			sb.append("/").append(parts[i]);
		}
		return sb.substring(1);
	}

}
