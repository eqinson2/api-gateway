package com.cdjdgm.dip.apigate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class LoginOauthTest {

	public static void main(String[] args) throws Exception{
	    doHttpPost("http://192.168.0.207:9191/oauth/token", "admin", "admin");
	}

	public static void doHttpPost(String urlStr, String account, String password) throws Exception{
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		String encoding = Base64.getEncoder().encodeToString("rajithapp:secret".getBytes("UTF-8"));
		conn.setRequestProperty("Authorization", "Basic " + encoding);
		
		OutputStream out = conn.getOutputStream();
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		writer.write("grant_type=password");
		writer.write("&username=");
		writer.write(account);
		writer.write("&password=");
		writer.write(password);

		writer.close();
		out.close();

		if (conn.getResponseCode() != 200) {
			System.out.println(conn.getResponseMessage());
		}

		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		System.out.println(sb.toString());
	}

}