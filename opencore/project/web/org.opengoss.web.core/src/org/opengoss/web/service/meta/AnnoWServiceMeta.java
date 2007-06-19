package org.opengoss.web.service.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.opengoss.web.service.annotation.WMethod;
import org.opengoss.web.service.annotation.WService;

public class AnnoWServiceMeta extends AbstractWServiceMeta {

	private WService wsAnnotation;

	private List<AnnoWMethodMeta> wMethodMetaCache = new ArrayList<AnnoWMethodMeta>();

	public AnnoWServiceMeta(Class<?> clazz) {
		super(clazz);
		wsAnnotation = clazz.getAnnotation(WService.class);
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(WMethod.class)) {
				WMethod wmAnnotation = method.getAnnotation(WMethod.class);
				wMethodMetaCache.add(new AnnoWMethodMeta(wmAnnotation, method));
			}
		}
	}

	public String getServiceUri() {
		return wsAnnotation.uri();
	}

	public String getServiceName() {
		return wsAnnotation.name();
	}

	public IWMethodMeta[] getWMMetas() {
		return wMethodMetaCache.toArray(new IWMethodMeta[wMethodMetaCache.size()]);
	}

	public IWMethodMeta findWMMeta(String type, String path) {
		for (IWMethodMeta wmMeta : getWMMetas()) {
			if(wmMeta.getMethodType().toString().equals(type) && 
					wmMeta.getUriPattern().match(path).isMatched()) {
				return wmMeta;
			}
		}
		return null;
	}

}
