package com.cdjdgm.dip.apigate;

import java.lang.reflect.Proxy;

import com.cdjdgm.dip.util.StringUtil;

/**
 * 接口的代理类的创建工厂
 * 使用Java的动态代理实现
 * @author chris.guan
 */
public class CallerProxyFactory {
	private static String baseUrl;
	
	private static String accessKey;
	
	@SuppressWarnings("unchecked")
	public static <T> T getCallerBean(Class<T> callerInterface) {
		if (StringUtil.isBlank(accessKey) || StringUtil.isBlank(baseUrl)){
			throw new IllegalStateException("accessKey/secretKey/baseUrl必须被赋值");
		}
		
		T caller = (T) Proxy.newProxyInstance(CallerInvocationHandlerImpl.class.getClassLoader(), 
				new Class[] { callerInterface },
				new CallerInvocationHandlerImpl());
		return caller;
	}

	public static void config(String baseUrl, String accessKey){
		CallerProxyFactory.baseUrl = baseUrl;
		CallerProxyFactory.accessKey = accessKey;
	}
	
	public static String getAccessKey() {
		return accessKey;
	}

	public static void setAccessKey(String accessKey) {
		CallerProxyFactory.accessKey = accessKey;
	}

	public static String getBaseUrl() {
		return baseUrl;
	}

	public static void setBaseUrl(String baseUrl) {
		CallerProxyFactory.baseUrl = baseUrl;
	}
	
	
}
