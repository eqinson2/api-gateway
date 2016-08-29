package com.cdjdgm.dip.util;

import java.util.ResourceBundle;

public class ResourceUtil {
	
	public static String getValue(ResourceBundle bundle, String key) {
		String value;
		// 未取到值，从properites文件中查找
		try {
			value = bundle.getString(key);
			if (value==null){
				throw new RuntimeException("没有该属性的值："+key);
			}
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			return value;
		} catch (Throwable e) {
			return null;
		}
	}
	
	public static String getValue(String resFileName, String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(resFileName);
		return getValue(bundle, key);
	}
}
