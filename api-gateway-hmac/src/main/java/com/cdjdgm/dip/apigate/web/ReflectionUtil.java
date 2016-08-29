package com.cdjdgm.dip.apigate.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

import com.cdjdgm.dip.util.TypeConverter;


final class ReflectionUtil {

	public static Object invokeMethodByName(final Object obj, final String methodName, Object[] args) {
		Method method = getDeclareMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		
		// 转换参数类型
		Class<?>[] paraTypes = method.getParameterTypes();
		try {
			for (int i=0;i<paraTypes.length;i++){
				if (args[i]!=null && !paraTypes[i].isAssignableFrom(args[i].getClass())){
						args[i] = TypeConverter.convert(paraTypes[i], args[i].toString());
				}
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("参数错误 [" + methodName + "] on target [" + obj + "], args:"+args);
		}
		
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}
	
	public static Method getDeclareMethodByName(final Object obj, final String methodName) {
		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}
	

	
	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if ((e instanceof IllegalAccessException) || (e instanceof IllegalArgumentException)
				|| (e instanceof NoSuchMethodException)) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
}
