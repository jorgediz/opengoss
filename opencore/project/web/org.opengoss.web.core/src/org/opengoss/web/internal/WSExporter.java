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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;

import org.opengoss.web.service.IWSContainer;
import org.opengoss.web.service.IWSExporter;
import org.opengoss.web.service.WServiceException;
import org.opengoss.web.service.annotation.WService;
import org.opengoss.web.service.meta.AnnoWServiceMeta;
import org.opengoss.web.service.meta.IWServiceMeta;
import org.opengoss.web.service.meta.XmlWServiceMeta;
/**
 * Implementation of {@link org.opengoss.web.service.IWSExporter}
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class WSExporter implements IWSExporter {
	
	IWSContainer wsContainer;
	
	Map<Object, Exported> exportedCache = new HashMap<Object, Exported>();

	public WSExporter(IWSContainer container) {
		this.wsContainer = container;
	}
	
	public void export(Object serviceObject) throws Exception {
		IWServiceMeta meta = getWServiceMeta(serviceObject);
		if(meta == null) {
			throw new WServiceException("No web service metadata found!");
		}
		WServiceMetalet metalet = new WServiceMetalet(meta);
		WServicelet servicelet = new WServicelet(meta, serviceObject);
		
//		wsContainer.addWebService(meta.getServiceUri() + META_DATA, metalet);
		servicelet.setContainer(wsContainer);
		wsContainer.add(servicelet);
		
		exportedCache.put(serviceObject, new Exported(metalet, servicelet));
	}

	public void unexport(Object serviceObject) throws Exception {
		Exported exported = exportedCache.get(serviceObject);
		if(exported != null) {
			wsContainer.remove(exported.servicelet);
//			wsContainer.removeWebService(exported.metalet);
		}
		exportedCache.remove(serviceObject);
	}

	private IWServiceMeta getWServiceMeta(Object serviceObject) throws WServiceException {
		Class<?>[] intfs = serviceObject.getClass().getInterfaces();
		for (Class<?> intf : intfs) {
			if(intf.isAnnotationPresent(WService.class)) {
				return new AnnoWServiceMeta(intf);
			}
			String className = intf.getName().substring(
					intf.getName().lastIndexOf(".") + 1);
			URL url = intf.getResource("/META-INF/" + className + ".map.xml");
			if(url != null) {
				try {
					Document xmlDoc = new Builder().build(url.openStream());
					return new XmlWServiceMeta(intf, xmlDoc);
				} catch (Exception e) {
					throw new WServiceException(e);
				}
			}
		}
		return null;
	}

	static class Exported {
		WServicelet servicelet;
		WServiceMetalet metalet;
		Exported(WServiceMetalet metalet, WServicelet servicelet) {
			this.metalet = metalet;
			this.servicelet = servicelet;
		}
	}

}
