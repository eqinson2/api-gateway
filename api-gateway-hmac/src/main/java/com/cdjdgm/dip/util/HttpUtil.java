package com.cdjdgm.dip.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static String post(URL url, List<NameValuePair> paramList) {
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpPost httpPost = null; 
        try{
			httpPost = new HttpPost(url.toURI());
			if (paramList != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			}
        } catch (Exception e){
        	throw new RuntimeException(e);
        }
		
		try (CloseableHttpResponse response = httpclient.execute(httpPost);){
			return EntityUtils.toString(response.getEntity(),"UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	public static String post(String remoteUrl, Map<String, Object> map){
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
        for (Map.Entry<String, Object> kv : map.entrySet()){
        	params.add(new BasicNameValuePair(kv.getKey(), kv.getValue().toString()));  
		}
        
        return post(url, params);
	}

	public static String post(URL url) throws Exception {
		return post(url,null);
	}
	
	public static String post(String url) throws Exception {
		return post(url,null);
	}
}
