package com.cdjdgm.dip.apigate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class HmacTest {
	//private static final Logger logger = LoggerFactory.getLogger(HmacTest.class);
    
	protected HttpClient client = HttpClientBuilder.create().build();

    private String baseUrl = "http://127.0.0.1:8080/";
    
    @Test
    public void login_no_params_fail() throws Exception {
        URL url = new URL(baseUrl + "login");
        HttpPost method = new HttpPost(url.toURI());
       
        HttpResponse response = client.execute(method);
        StatusLine statusLine = response.getStatusLine();

        HttpRequest.printResponse(response);
        HttpRequest.printHeader(response);

        Assert.assertTrue(statusLine.getStatusCode() != 200);
    }
    
	@Test
	public void login_ok() throws Exception {
		login("admin", "admin");
	}
	
	private UserAppKey login(String account, String password) throws Exception {
		URL url = new URL(baseUrl + "login");
		HttpPost httpPost = new HttpPost(url.toURI());

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("timeout", "100"));

		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = client.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
		
		Assert.assertEquals(200, statusLine.getStatusCode());

		//HttpRequest.printResponse(response);
		HttpRequest.printHeader(response);
        
        String json = HttpRequest.getEntity(response.getEntity().getContent());
        UserAppKey userAppKey = JSON.parseObject(json, UserAppKey.class);
        return userAppKey;
	}
	
	@Test
	public void login_ok_admin_ok() throws Exception {
		UserAppKey userAppKey = login("admin", "admin");
		
        String accessKey = userAppKey.getAccessKey();
        String secretKey = userAppKey.getSecretKey();

		String entity = "lady gaga";
		String result = HttpRequest.call(baseUrl + "admin", entity, accessKey, secretKey);
		System.out.println(result);
	}
	
	
	@Test
	public void login_ok_api_ok() throws Exception {
		UserAppKey userAppKey = login("admin", "admin");
		
        String accessKey = userAppKey.getAccessKey();
        String secretKey = userAppKey.getSecretKey();
	
		String result = HttpRequest.callApi("com.cdjdgm.dip.portal.remote.IDicDataService", "findAll", null, 
				accessKey, secretKey, baseUrl);
		Assert.assertNotNull(result);
		
		String typekey = "BIZTYPE_CODE";
		String name = "1";
		result = HttpRequest.callApi("com.cdjdgm.dip.portal.remote.IDicDataService", "findByCode", new String[]{typekey, name},
				accessKey, secretKey, baseUrl);
		
		Assert.assertNotNull(result);
	}
	
    
}
