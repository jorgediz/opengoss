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
package org.opengoss.core;

import org.opengoss.core.util.AssertException;

/**
 * A Hierachical registry.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 
 * @since 2006-11-20
 */
public class Registry implements IRegistry {
	
	private IRegistry parent = null;
	
	public Registry(IRegistry parent) {
		this.parent = parent;
	}
	/**
	 * Get parent registry.
	 * 
	 * @return parent registry.
	 */
	public IRegistry getParent() {
		return parent;
	}
	/**
	 * Set parent registry.
	 * 
	 * @param parent registry.
	 */
	public void setParent(IRegistry parent) {
		if(parent == null) {
			throw new AssertException("parent cannot be null!");
		}
		this.parent = parent;
	}
	
}
