package com.vsmjsc.rdgt.cloudservice;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class HttpResponse {
	private int contentLength;
	private int httpCode;
	private InputStream inputStream;

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getContent() {
		if (inputStream != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[500];
			int len;
			try {
				while ((len = inputStream.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}

				inputStream.close();
				inputStream = null;
				return baos.toString("utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
