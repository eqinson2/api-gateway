package com.cdjdgm.dip.apigate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cdjdgm.dip.util.StringUtil;

/**
 * 接口的代理类的实现类
 * @author chris.guan
 */
public class CallerInvocationHandlerImpl implements InvocationHandler{
	Logger logger = LoggerFactory.getLogger(CallerInvocationHandlerImpl.class);
	
	protected HttpClient client = HttpClientBuilder.create().build();
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> returnType = method.getReturnType();
		String name = method.getName();
		String ifname = method.getDeclaringClass().getName();
		
		System.out.println(ifname);
		
		String accessKey = CallerProxyFactory.getAccessKey();
		String secretKey = CallerProxyFactory.getSecretKey();
		String baseUrl = CallerProxyFactory.getBaseUrl();
		if (StringUtil.isBlank(accessKey) || StringUtil.isBlank(secretKey) || StringUtil.isBlank(baseUrl)){
			throw new IllegalStateException("accessKey/secretKey/baseUrl必须被赋值");
		}
		
		Object object = callApi(ifname, name, args, returnType, accessKey, secretKey, baseUrl);
		return object;
	}

	/**
	 * 调用接口服务器的Api
	 * @param method
	 * @param anno
	 * @param params
	 * @return
	 */
	private <T> T callApi(String interfaceName, String methodName, Object[] paraValues, Class<T> returnType, 
			String accessKey, String secretKey, String baseUrl) throws Exception{
		String result = HttpRequest.callApi(interfaceName, methodName, paraValues, accessKey, secretKey, baseUrl);
		if (StringUtil.isBlank(result)){
			return null;
		}
		return JSON.parseObject(result, returnType);
	}
	
    
}
