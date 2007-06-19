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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.opengoss.web.service.AbstractMarshaller;

/**
 * Serialized java object marshaller.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class SerializableMarshaller extends AbstractMarshaller {

	public static final String VIEW = "java-object";

	public String getRepresentation() {
		return VIEW;
	}

	public void write(Object dataObject, OutputStream output)
			throws IOException {
		ObjectOutputStream oos = null;
		if(output instanceof ObjectOutputStream) {
			oos = (ObjectOutputStream)output;
		} else {
			oos = new ObjectOutputStream(output);
		}
		oos.writeObject(dataObject);
		oos.flush();
	}

	@SuppressWarnings("unchecked")
	public <T> T read(InputStream inputStream, Class<T> clazz) throws IOException, ClassNotFoundException {
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(getClassLoader());
			ObjectInputStreamWithContextClassLoader ois = new ObjectInputStreamWithContextClassLoader(inputStream);
			return (T)ois.readObject();
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}
	
	static class ObjectInputStreamWithContextClassLoader extends ObjectInputStream {

		protected ObjectInputStreamWithContextClassLoader() throws IOException, SecurityException {
			super();
		}

		public ObjectInputStreamWithContextClassLoader(InputStream in) throws IOException {
			super(in);
		}

		@Override
		protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			try {
				return super.resolveClass(desc);
			} catch (ClassNotFoundException ex) {
			    return Thread.currentThread().getContextClassLoader().loadClass(desc.getName());
			}
		}

		@Override
		protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, 
			ClassNotFoundException {
			try {
			return super.resolveProxyClass(interfaces);
			} catch(ClassNotFoundException e) {
				return resolveProxyClassWithContextCL(interfaces);
			}
		}

		//remind: maybe something error.
		private Class<?> resolveProxyClassWithContextCL(String[] interfaces) throws ClassNotFoundException {
			ClassLoader nonPublicLoader = null;
			boolean hasNonPublicInterface = false;

			// define proxy in class loader of non-public interface(s), if any
			Class[] classObjs = new Class[interfaces.length];
			for (int i = 0; i < interfaces.length; i++) {
			    Class cl = Thread.currentThread().getContextClassLoader().loadClass(interfaces[i]);
			    if ((cl.getModifiers() & Modifier.PUBLIC) == 0) {
				if (hasNonPublicInterface) {
				    if (nonPublicLoader != cl.getClassLoader()) {
					throw new IllegalAccessError(
					    "conflicting non-public interface class loaders");
				    }
				} else {
				    nonPublicLoader = cl.getClassLoader();
				    hasNonPublicInterface = true;
				}
			    }
			    classObjs[i] = cl;
			}
			try {
			    return Proxy.getProxyClass(hasNonPublicInterface ? nonPublicLoader 
			    		: Thread.currentThread().getContextClassLoader(), classObjs);
			} catch (IllegalArgumentException e) {
			    throw new ClassNotFoundException(null, e);
			}
		}
	}

}
