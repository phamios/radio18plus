package com.vsmjsc.rdgt.task;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.vsmjsc.rdgt.cloudservice.HttpGet;
import com.vsmjsc.rdgt.cloudservice.HttpResponse;
import com.vsmjsc.rdgt.model.RadioTrack;
import com.vsmjsc.rdgt.utils.UtilFuntions;

public class RadioTrackLoader extends AsyncTask<Void, Void, RadioTrack> {
	String radioTrackId;

	public RadioTrackLoader(String trackId) {
		radioTrackId = trackId;
	}

	@Override
	protected RadioTrack doInBackground(Void... params) {
		HttpGet httpget = new HttpGet();
		//HttpResponse response = httpget.getDataInputStreamFromUrl(String.format("http://api.radio18plus.radito.com/1.0/track/%s", radioTrackId));
		HttpResponse response = httpget.getDataInputStreamFromUrl(String.format("http://admega.vn/api/radio_track/%s", radioTrackId));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> Radio Track load" + String.format("http://admega.vn/api/radio_track/%s", radioTrackId));
		if (response != null && response.getHttpCode() == 200) {
			try {
				//JSONObject jsonObj = new JSONObject(response.getContent()).getJSONObject("data");
				JSONObject jsonObj = new JSONObject(response.getContent());
				JSONArray jsonArray = jsonObj.getJSONArray("data");
				
				RadioTrack track = new RadioTrack();
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj1 = jsonArray.getJSONObject(i);
					track.setId(jsonObj1.getInt("id"));
					track.setName(jsonObj1.getString("name"));
					track.setDate(jsonObj1.getString("date"));
					track.setLength(jsonObj1.getString("length"));
					if(jsonObj1.getString("likes").equalsIgnoreCase("null")){
						track.setLikes(0);
					}
					if(jsonObj1.getString("views").equalsIgnoreCase("null")){
						track.setViews(0);
					}
					
					track.setImageUrl(UtilFuntions.getRealUrl(jsonObj1.getString("image")));
					track.setAudioUrl(UtilFuntions.getRealUrl(jsonObj1.getString("link")));
					 
					track.setDescription(jsonObj1.getString("description"));
				}
				
				return track;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
