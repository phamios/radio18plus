package com.vsmjsc.rdgt.cloudservice;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

public class HttpGet {
	private static String HTTP_PROXY_HOST = "192.168.1.120";
	private static int HTTP_PROXY_PORT = 8888;

	public HttpResponse getDataInputStreamFromUrl(String strurl) {
		//System.out.println("Phuoc debug, url: "+ strurl);
		HttpResponse response = new HttpResponse();
		try {
			URL url = new URL(strurl);
			HttpURLConnection connection = null;
			if (isProxyEnabled()) {
				SocketAddress add = new InetSocketAddress(HTTP_PROXY_HOST, HTTP_PROXY_PORT);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, add);
				connection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				connection = (HttpURLConnection) url.openConnection();
			}

			connection.setConnectTimeout(8000);
			connection.setRequestMethod("GET");
			connection.connect();
			int httpCode = connection.getResponseCode();
			response.setHttpCode(httpCode);
			response.setInputStream(connection.getInputStream());
			response.setContentLength(connection.getContentLength());

			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private boolean isProxyEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
