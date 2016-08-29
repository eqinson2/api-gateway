package com.cdjdgm.dip.apigate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 接口的代理类的实现类
 * @author chris.guan
 */
public class HttpRequest{
	static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
	
	protected static HttpClient client = HttpClientBuilder.create().build();
	

	/**
	 * 调用接口服务器的Api
	 * @param method
	 * @param anno
	 * @param params
	 * @return
	 */
	public static String callApi(String interfaceName, String methodName, Object[] paraValues, 
			String accessKey, String baseUrl) throws Exception{

		StringBuilder sb = new StringBuilder(baseUrl);
		sb.append("api/").append(interfaceName).append("/").append(methodName);

		String entity = JSON.toJSONString(paraValues);
		
		return HttpRequest.call(sb.toString(), entity, accessKey);
	}
	
	public static String call(String sUrl, String entity, String accessKey) throws Exception{
		URL url = new URL(sUrl);
		HttpPost httpPost = new HttpPost(url.toURI());
		
		String contentType = "application/json; charset=utf-8";
		
//		String encoding = Base64.getEncoder().encodeToString("rajithapp:secret".getBytes("UTF-8"));
		httpPost.setHeader("Authorization", "Bearer " + accessKey);
		
//		Header[] headers = buildHeader(accessKey, httpPost, contentType, entity);
//		httpPost.setHeaders(headers);

		httpPost.setEntity(new StringEntity(entity, ContentType.create(contentType)));
		
		HttpResponse response = client.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		//printResponse(execute);
		//printHeader(execute);
		if (statusCode>=400 && statusCode<500){
			throw new RuntimeException("Status Code:"+ statusCode +", 客户端错误");
		} else if (statusCode>=500 && statusCode<600){
			throw new RuntimeException("Status Code:"+ statusCode +", 服务器错误");			
		}
		
		String result = getEntity(response.getEntity().getContent());
		return result;
	}
	
    
    public static String getEntity(InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
    

    public static void printHeader(HttpResponse execute) {
        for (Header header : execute.getAllHeaders()) {
            logger.debug("{} = {}", header.getName(), header.getValue());
        }
    }

    public static void printResponse(HttpResponse execute) throws Exception {
        StatusLine statusLine = execute.getStatusLine();

        logger.info("Reason: {}", statusLine.getReasonPhrase());
        logger.info("Status: {}", statusLine.getStatusCode());
        logger.info("Read: {}", getEntity(execute.getEntity().getContent()));
    }
    
}
