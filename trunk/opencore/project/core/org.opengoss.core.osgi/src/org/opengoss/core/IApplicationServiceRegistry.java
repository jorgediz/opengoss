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

import org.osgi.framework.BundleContext;

/**
 * Application level service registry. Clients could get services from the
 * registry by id or interface.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-19
 */
public interface IApplicationServiceRegistry extends IRegistry {

	public Object getService(BundleContext context, String id);

	public <T> T getService(BundleContext context, Class<T> intf);

	public <T> T getService(BundleContext context, String id, Class<T> intf);

}
