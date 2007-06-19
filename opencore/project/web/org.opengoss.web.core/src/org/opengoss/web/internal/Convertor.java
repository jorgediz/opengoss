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
/**
 * Utility class to convert string to object of specified type.
 *
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-20
 * @since 1.0
 */
public class Convertor {

	public static Object convertStr2Object(Class<?> type, String str) {
		if (type == String.class) {
			return str;
		}
		if (type == int.class || type == Integer.class) {
			return Integer.parseInt(str);
		}
		if (type == long.class || type == Long.class) {
			return Long.parseLong(str);
		}
		if (type == short.class || type == Short.class) {
			return Short.parseShort(str);
		}
		if (type == float.class || type == Float.class) {
			return Float.parseFloat(str);
		}
		if (type == double.class || type == Double.class) {
			return Double.parseDouble(str);
		}
		if (type == byte.class || type == Byte.class) {
			return Byte.parseByte(str);
		}
		if (type == boolean.class || type == Boolean.class) {
			return Boolean.parseBoolean(str);
		}
		throw new RuntimeException(
				"!AssertFailure! Unsupported parameter type: " + type);
	}

}
