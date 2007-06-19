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
package org.opengoss.web.service.meta;

import java.lang.annotation.Annotation;

import org.opengoss.web.service.annotation.WBody;
import org.opengoss.web.service.annotation.WForm;
import org.opengoss.web.service.annotation.WParam;
/**
 * Annotation style web parameter matadata.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class AnnoWParamMeta implements IWParamMeta {

	private Annotation annotation;

	public AnnoWParamMeta(Annotation annotation) {
		this.annotation = annotation;
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWParamMeta#getName()
	 */
	public String getName() {
		if(annotation instanceof WParam) {
			return ((WParam)annotation).name();
		}
		if(annotation instanceof WForm) {
			return ((WForm)annotation).name();
		}
		return "";
	}
	/*
	 * (non-Javadoc)
	 * @see org.opengoss.web.service.meta.IWParamMeta#getType()
	 */
	public Type getType() {
		if(annotation instanceof WParam) {
			return Type.WPARAM;
		}
		if(annotation instanceof WForm) {
			return Type.WFORM;
		}
		if(annotation instanceof WBody) {
			return Type.WBODY;
		}
		throw new RuntimeException("Unknown annotation type:" + annotation);
	}

}
