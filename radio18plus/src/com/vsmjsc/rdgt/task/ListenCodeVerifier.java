package com.vsmjsc.rdgt.task;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.vsmjsc.rdgt.cloudservice.HttpGet;
import com.vsmjsc.rdgt.cloudservice.HttpResponse;

public class ListenCodeVerifier extends AsyncTask<Void, Integer, Boolean> {

	private String RadioCode;

	public ListenCodeVerifier(String radioCode) {
		RadioCode = radioCode;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		HttpGet httpget = new HttpGet();
		try {
			HttpResponse response = httpget.getDataInputStreamFromUrl(String.format("http://api.radio18plus.radito.com/1.0/license/check/%s", RadioCode));
			if (response != null && response.getHttpCode() == 200) {
				int status = new JSONObject(response.getContent()).getInt("status");
				if (status == 200) {
					return Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

}
