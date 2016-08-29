package com.cdjdgm.dip.apigate.shiro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetClientPost {

	// http://localhost:8080/RESTfulExample/json/product/post
	public static String post(String sUrl, String input) {
		try {
			URL url = new URL(sUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			StringBuilder output = new StringBuilder();

			String line = null;
			while ((line = br.readLine()) != null) {
				output.append(line);
			}

			conn.disconnect();

			return output.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
