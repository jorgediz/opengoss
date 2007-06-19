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

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opengoss.web.service.IMarshaller;
import org.opengoss.web.service.IWSContainer;
import org.opengoss.web.service.IWServicelet;
import org.opengoss.web.service.URIMatcher;
import org.opengoss.web.service.meta.IWMethodMeta;
import org.opengoss.web.service.meta.IWParamMeta;
import org.opengoss.web.service.meta.IWServiceMeta;
/**
 * Implementation of {@link org.opengoss.web.service.IWServicelet}
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class WServicelet implements IWServicelet {
	
	private final IWServiceMeta wsMeta;

	private final Object serviceObject;

	private IWSContainer container;
	
	public WServicelet(IWServiceMeta meta, Object serviceObject) {
		this.wsMeta = meta;
		this.serviceObject = serviceObject;
	}
	
	public String getUri() {
		return wsMeta.getServiceUri();
	}
	
	private Object callMethod(IWMethodMeta wmMeta, HttpServletRequest request, 
			HttpServletResponse response) 
		throws Exception {
		Map<String, String> paramMap = getParamMap(request, wmMeta);
		Map<String, String> formMap = getForm(request, wmMeta);
		Class<?>[] paramTypes = wmMeta.getMethod().getParameterTypes();
		IWParamMeta[] paramMetas = wmMeta.getParamMetas();
		Object[] args = new Object[paramTypes.length];
		for (int i = 0; i < args.length; i++) {
			IWParamMeta paramMeta = paramMetas[i];
			IWParamMeta.Type type = paramMeta.getType();
			if(type == IWParamMeta.Type.WPARAM) {
				args[i] = Convertor.convertStr2Object(paramTypes[i], paramMap.get(paramMeta.getName()));
			} else if(type == IWParamMeta.Type.WFORM) {
				args[i] = Convertor.convertStr2Object(paramTypes[i], formMap.get(paramMeta.getName()));
			} else if(type == IWParamMeta.Type.WBODY) {
				args[i] = fromWebBody(request, paramTypes[i]);
			} else {
				throw new RuntimeException("Unknow Annotation Type: " + paramMeta);
			}
		}
		return wmMeta.getMethod().invoke(serviceObject, args);
	}

	private <T> T fromWebBody(HttpServletRequest request, Class<T> clazz) throws Exception {
		IMarshaller marshaller = container.getMarshaller(getView(request));
		return marshaller.read(request.getInputStream(), clazz);
	}

	private Map<String, String> getParamMap(HttpServletRequest request, IWMethodMeta wmMeta) {
		URIMatcher matcher = wmMeta.getUriPattern().match(request.getPathInfo());
		Map<String, String> paramMap = matcher.getParaMap();
		return paramMap;
	}
	

	private Map<String, String> getForm(HttpServletRequest request, IWMethodMeta wmMeta) 
		throws Exception {
		IWParamMeta[] paramMetas = wmMeta.getParamMetas();
		boolean isFormExisted = false;
		for (IWParamMeta paramMeta : paramMetas) {
			if(paramMeta.getType() == IWParamMeta.Type.WFORM) {
				isFormExisted = true;
			}
		}
		return isFormExisted ? request.getParameterMap(): null;
	}

	private String getView(HttpServletRequest request) throws IOException {
		String view = request.getParameter(VIEW);
		if(view == null) {
			view = "java-object";;
		}
		return view;
	}

	public void service(HttpServletRequest request, HttpServletResponse response) 
		throws IOException {
		//Match URI and find method metadata.
		String target = request.getPathInfo().substring(getUri().length());
		IWMethodMeta wmMeta = wsMeta.findWMMeta(request.getMethod(), target);
		if(wmMeta == null) {
			response.sendError(400, "No service method found!");
			response.flushBuffer();
			return;
		}
		Object dataObject = null;
		try {
			dataObject = callMethod(wmMeta, request, response);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			response.flushBuffer();
			return;
		}
		
		//Marshall data object
		if(dataObject != null) {
			String view = getView(request);
			IMarshaller marshaller = container.getMarshaller(view);
			marshaller.write(dataObject, response.getOutputStream());
		}
		response.setStatus(200);
		response.flushBuffer();		
	}

	public void setContainer(IWSContainer container) {
		this.container = container;
	}

}
