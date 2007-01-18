package org.opengoss.core.util;

public final class Convertor {

	@SuppressWarnings("unchecked")
	public static Object convert(Class clazz, String value) 
		throws ConvertException {
		return convert(clazz, value, Thread.currentThread().getContextClassLoader());
	}
	
	@SuppressWarnings("unchecked")
	public static Object convert(Class clazz, String value, ClassLoader cl) 
		throws ConvertException {
		try {
			if(clazz == String.class) {
				return value;
			}
			if(clazz == Byte.class || clazz == byte.class) {
				return Byte.valueOf(value);
			}
			if(clazz == Double.class || clazz == double.class) {
				return Double.valueOf(value);
			}
			if(clazz == Float.class || clazz == float.class) {
				return Float.valueOf(value);
			}
			if(clazz == Integer.class || clazz == int.class) {
				return Integer.valueOf(value);
			}
			if(clazz == Long.class || clazz == long.class) {
				return Long.valueOf(value);
			}
			if(clazz == Short.class || clazz == short.class) {
				return Short.valueOf(value);
			}
			if(clazz == Boolean.class || clazz == boolean.class) {
				return Boolean.valueOf(value);
			}
			if(clazz == Class.class) {
				return cl.loadClass(value);
			}
		} catch (Exception e) {
			throw new ConvertException(e);
		}
		throw new ConvertException(clazz, value);
	}
	
	@SuppressWarnings("serial")
	static class ConvertException extends Exception {

		@SuppressWarnings("unchecked")
		private Class clazz = null;
		
		private String value = null;

		@SuppressWarnings("unchecked")
		public ConvertException(Class clazz, String value) {
			this.clazz = clazz;
			this.value = value;
		}

		public ConvertException(Exception e) {
			super(e);
		}

		@Override
		public String toString() {
			if(clazz == null) {
				return super.toString();
			}
			return "Cannot convert " + value + " to type of " + clazz.getName();
		}
		
	}
	
}
