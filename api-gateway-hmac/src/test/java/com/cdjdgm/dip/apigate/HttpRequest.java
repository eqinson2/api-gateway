package com.cdjdgm.dip.apigate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cdjdgm.dip.apigate.shiro.util.HmacSha1;

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
			String accessKey, String secretKey, String baseUrl) throws Exception{

		StringBuilder sb = new StringBuilder(baseUrl);
		sb.append("api/").append(interfaceName).append("/").append(methodName);

		String entity = JSON.toJSONString(paraValues);
		
		return HttpRequest.call(sb.toString(), entity, accessKey, secretKey);
	}
	
	public static String call(String sUrl, String entity, String accessKey, String secretKey) throws Exception{
		URL url = new URL(sUrl);
		HttpPost httpPost = new HttpPost(url.toURI());
		
		String contentType = "application/json; charset=utf-8";
		Header[] headers = buildHeader(accessKey, secretKey, httpPost, contentType, entity);
		
		httpPost.setHeaders(headers);
		httpPost.setEntity(new StringEntity(entity, ContentType.create(contentType)));
		
		HttpResponse response = client.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		//printResponse(execute);
		//printHeader(execute);
		if (401==statusCode){
			throw new RuntimeException("Status Code:401, 没有访问权限");
		} else if (500==statusCode){
			throw new RuntimeException("Status Code:500, 远程服务器内部错误");			
		} else if (200!=statusCode){
			throw new RuntimeException("Status Code:"+statusCode+", 未知错误");
		}
		
		String result = getEntity(response.getEntity().getContent());
		return result;
	}
	
    private static Header[] buildHeader(String accessKey, String secretKey, HttpRequestBase httpRequest,
    		String contentType, String entity) throws Exception {
    	String contentMd5 = null;
    	if (entity == null){
    		contentMd5 = "";
    	} else {
    		byte[] bytes = entity.getBytes("ISO-8859-1");
    		contentMd5 = DigestUtils.md5Hex(bytes);
    	}
    	
        //String dateString = dateFormatter.get().format(new Date());
        String dateString = String.valueOf(new Date().getTime());

        // Signature = Base64( HMAC-SHA1( SecretKey, UTF-8-Encoding-Of( StringToSign ) ) )
        		
        String stringToSign = String.format(Locale.US, "%s\n%s\n%s\n%s\n%s",
                httpRequest.getMethod(),
                contentMd5,
                contentType.toLowerCase(),
                dateString,
                httpRequest.getURI().getPath());
        logger.debug("客户端加密前stringToSign：\n"+stringToSign);
        logger.debug("客户端加密前secretKey："+ByteSource.Util.bytes(secretKey));

        byte[] hexHmac = HmacSha1.hash(ByteSource.Util.bytes(secretKey).getBytes(), stringToSign);
        stringToSign = Base64.encodeToString(hexHmac);
        logger.debug("客户端stringToSign："+stringToSign);

        String authorization = String.format(Locale.US, "AWS %s:%s", accessKey, stringToSign);
        Header[] headers = new Header[] {
                new BasicHeader("Content-Type", contentType),
                new BasicHeader("Content-MD5", contentMd5),
                new BasicHeader("Date", dateString),
                new BasicHeader("Authorization", authorization),
        };
        logger.debug("客户端authorization："+authorization);

        return headers;
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
