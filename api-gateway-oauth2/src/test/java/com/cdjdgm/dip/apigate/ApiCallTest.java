package com.cdjdgm.dip.apigate;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class ApiCallTest {
	//private static final Logger logger = LoggerFactory.getLogger(HmacTest.class);
    
	protected HttpClient client = HttpClientBuilder.create().build();

//    private String baseUrl = "http://127.0.0.1:9191/";
    private String baseUrl = "http://192.168.230.209:9191/";
    
    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    
    @Test
    public void hello() throws Exception {
        URL url = new URL(baseUrl + "hello");
        HttpGet method = new HttpGet(url.toURI());
       
        HttpResponse response = client.execute(method);
        StatusLine statusLine = response.getStatusLine();

//        HttpRequest.printResponse(response);
        HttpRequest.printHeader(response);

        Assert.assertTrue(statusLine.getStatusCode() == 200);
        Assert.assertEquals("Hello User!", HttpRequest.getEntity(response.getEntity().getContent()));
    }
    
    @Test
    public void call_api_no_auth_fail() throws Exception {
        URL url = new URL(baseUrl + "/api");
        HttpPost method = new HttpPost(url.toURI());
       
        HttpResponse response = client.execute(method);
        StatusLine statusLine = response.getStatusLine();

        HttpRequest.printResponse(response);
        HttpRequest.printHeader(response);

        Assert.assertEquals(401, statusLine.getStatusCode());
    }
    
    @Test
	public void login_ok() throws Exception {
		login("admin", "admin");
	}
    
    @Test
//    @Repeat(10000)
    public void login_logout_ok() throws Exception {
    	login("admin", "admin");
    	logout();
    }
	
	private Map<String,Object> login(String account, String password) throws Exception {
		URL url = new URL(baseUrl + "oauth/token");
		HttpPost httpPost = new HttpPost(url.toURI());

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "password"));
		params.add(new BasicNameValuePair("username", account));
		params.add(new BasicNameValuePair("password", password));

		String encoding = Base64.getEncoder().encodeToString("rajithapp:secret".getBytes("UTF-8"));
		httpPost.setHeader("Authorization", "Basic " + encoding);
		
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = client.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
		
		Assert.assertEquals(200, statusLine.getStatusCode());

		//HttpRequest.printResponse(response);
		HttpRequest.printHeader(response);
        
        String json = HttpRequest.getEntity(response.getEntity().getContent());
        @SuppressWarnings("unchecked")
		Map<String,Object> userAppKey = JSON.parseObject(json, Map.class);
//        System.out.println();
//        System.out.println(userAppKey);
//        System.out.println();
        return userAppKey;
	}
	
    public void logout() throws Exception {
    	
		Map<String,Object> userAppKey = login("admin", "admin");		
        String accessToken = (String)userAppKey.get("access_token");

		String entity = "";
		String result = HttpRequest.call(baseUrl + "oauth/logout", entity, accessToken);
		System.out.println(result);
		
//        URL url = new URL(baseUrl + "oauth/logout");
//        HttpGet method = new HttpGet(url.toURI());
//       
//        HttpResponse response = client.execute(method);
//        StatusLine statusLine = response.getStatusLine();
//
////        HttpRequest.printResponse(response);
//        HttpRequest.printHeader(response);
//
//        Assert.assertEquals(200, statusLine.getStatusCode());
    }
	
	@Test
	public void login_ok_echo_ok() throws Exception {
		Map<String,Object> userAppKey = login("admin", "admin");		
        String accessToken = (String)userAppKey.get("access_token");

		String entity = "Secure Hello";
		String result = HttpRequest.call(baseUrl + "api/echo", entity, accessToken);
		Assert.assertEquals("ECHO:Secure Hello", result);
	}
	
	
	@Test
	public void login_ok_api_ok() throws Exception {
		Map<String,Object> userAppKey = login("admin", "admin");
		String accessToken = (String)userAppKey.get("access_token");
		
		String result = HttpRequest.callApi("com.cdjdgm.dip.portal.remote.IDicDataService", "findAll", null, 
				accessToken, baseUrl);
		Assert.assertNotNull(result);
		
		String typekey = "BIZTYPE_CODE";
		String name = "1";
		result = HttpRequest.callApi("com.cdjdgm.dip.portal.remote.IDicDataService", "findByCode", new String[]{typekey, name},
				accessToken, baseUrl);
		
		Assert.assertNotNull(result);
	}
    
}
