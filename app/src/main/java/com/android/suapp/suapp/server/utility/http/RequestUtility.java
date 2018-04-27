package com.android.suapp.suapp.server.utility.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;

public abstract class RequestUtility {

	public static String post(String server, String methodName, String params){
		byte[] data;
		BufferedReader is = null;
		StringBuilder answer = new StringBuilder();

		try {
			URL url = new URL(server + methodName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			int a = 1;

			//params = new Gson().toJson(params);
			String encodedData = URLEncoder.encode( params, "UTF-8" );

			conn.setRequestProperty("Content-Length", "" + Integer.toString(encodedData.getBytes().length));
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			OutputStream os = conn.getOutputStream();
			data = encodedData.getBytes("UTF-8");
			data = params.getBytes();
			os.write(data);

			conn.connect();

			is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String temp;
			while ((temp = is.readLine()) != null)
				answer.append(temp);

			return answer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static String get(String server, String methodName, String params) throws IOException {
		URL url = new URL(server + methodName + '?' + params);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
		StringBuilder receivedString = new StringBuilder();
		String temp;
		while ((temp = br.readLine()) != null)
			receivedString.append(temp);
		return receivedString.toString();
	}

	public static Response apachePOST(HttpPost httpPost) throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		return new Response(httpClient.execute(httpPost));
	}

	public static Response apacheGET(HttpGet httpGet, boolean redirect) throws IOException {
		HttpClient httpClient;
		if (redirect){
			httpClient = HttpClients.createDefault();
		}else {
			httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
		}
		return new Response(httpClient.execute(httpGet));
	}
}
