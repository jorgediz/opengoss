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
package org.opengoss.web.internal;

import java.util.HashMap;
import java.util.Map;
/**
 * Marshaller class loader implementation.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class MarshallerClassLoader extends ClassLoader {
	
	public Map<String, Class> contributedClasses = new HashMap<String, Class>(11);
	
	public MarshallerClassLoader(ClassLoader classLoader) {
		super(classLoader);
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(contributedClasses.containsKey(name)) {
			return contributedClasses.get(name);
		}
		return super.findClass(name);
	}

	public void contributeClass(Class clazz) {
		contributedClasses.put(clazz.getName(), clazz);
	}

	public void removeClass(Class clazz) {
		contributedClasses.remove(clazz.getName());
	}

}
