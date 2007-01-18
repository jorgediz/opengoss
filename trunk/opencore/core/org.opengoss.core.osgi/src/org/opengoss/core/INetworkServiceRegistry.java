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

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Network level registry.
 * <p>
 * Clients could register remote services. The default implementation is RMI 
 * registry.
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-19
 */
public interface INetworkServiceRegistry extends IRegistry {

	/**
	 * Register a remote service.
	 * 
	 * @param id service id
	 * @param service remote service object
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	void register(String id, Remote service) throws RemoteException,
			AlreadyBoundException;

	/**
	 * Unregister a remote service by id.
	 * 
	 * @param id service id
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	void unregister(String id) throws RemoteException, NotBoundException;
	
	<T> T getService(String id, Class<T> intf);

}
