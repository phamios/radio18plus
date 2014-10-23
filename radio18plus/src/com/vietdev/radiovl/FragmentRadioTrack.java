package com.vietdev.radiovl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.vsmjsc.rdgt.cloudservice.HttpGet;
import com.vsmjsc.rdgt.cloudservice.HttpResponse;
import com.vsmjsc.rdgt.model.RadioTrack;
import com.vsmjsc.rdgt.task.RadioTrackLoader;
import com.vsmjsc.rdgt.utils.UtilFuntions;
import com.vsmjsc.rdgt.widget.PagingListView;
import com.vsmjsc.rdgt.widget.PagingListView.PagingListViewListener;

@SuppressLint("ValidFragment")
public class FragmentRadioTrack extends Fragment implements PagingListViewListener, OnItemClickListener {

	private PagingListView lvRadioTrack;
	private SimpleAdapter adapter;
	private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

	String[] subcategory_from = { "title", "mota" };
	int[] subcategory_to = { R.id.txtTenRadio, R.id.txtDescription };

	private static int totalTrack;

	private String mCatName;

	public String getCategoryName() {
		return mCatName;
	}
	
	public FragmentRadioTrack(String catName){
		this.mCatName = catName;
	}

	private View llLoading;

	private int catId;

	public void setCatId(int catId) {
		this.catId = catId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_layout_chuyenmuc, null, false);
		llLoading = v.findViewById(R.id.llLoading);
		lvRadioTrack = (PagingListView) v.findViewById(R.id.lvChuyenMuc);
		adapter = new SimpleAdapter(getActivity(), data, R.layout.item_radiotrack, subcategory_from, subcategory_to);
		lvRadioTrack.setAdapter(adapter);
		lvRadioTrack.setOnItemClickListener(this);
		lvRadioTrack.setPagingListViewListener(this);
		getActivity().findViewById(R.id.imgHeaderIndicator).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.llHeader).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((ScreenHomeActivity) getActivity()).onClick_Back(v);
			}
		});
		getActivity().findViewById(R.id.llHeader).setClickable(true);
		//load chuyen muc
		new SubCategoryLoader(catId).execute();
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
		String trackId = data.get(pos - 1).get("track_id").toString();
		((TextView) getActivity().findViewById(R.id.txtTitle)).setText(data.get(pos - 1).get("title").toString());
		//load radio track detail
		new RadioTrackLoader(trackId) {
			protected void onPostExecute(final RadioTrack result) {
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							getActivity().findViewById(R.id.tabDaMua).setBackgroundColor(Color.TRANSPARENT);
							getActivity().findViewById(R.id.tabDangNghe).setBackgroundResource(R.drawable.dangnghe_bg);
							getActivity().findViewById(R.id.tabRadioShop).setBackgroundColor(Color.TRANSPARENT);
							((ScreenHomeActivity) getActivity()).playRadioTrack(result);
							FragmentDangNghe fragment = new FragmentDangNghe();
							((ScreenHomeActivity) getActivity()).setRadioPlaying(result);
							getFragmentManager().beginTransaction().replace(R.id.llContainer, fragment).addToBackStack(null).commit();
						}
					});
			};
		}.execute();
	}

	class SubCategoryLoader extends AsyncTask<Void, Void, Boolean> {
		private String catId;

		public SubCategoryLoader(int catId) {
			this.catId = "" + catId;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			HttpGet httpget = new HttpGet();

			try {
				//String url = String.format("http://api.radio18plus.radito.com/1.0/category/%s/tracks", catId);
				String url = String.format("http://admega.vn/api/radio_bycate/%s", catId);
				System.out.println(">>>>>> debug, url>" + String.format("http://admega.vn/api/radio_bycate/%s", catId));
				//Log.wtf(">>>>>>>>>", url);
				HttpResponse response = httpget.getDataInputStreamFromUrl(url);
				if (response != null && response.getHttpCode() == 200) {
					JSONObject jsonObj = new JSONObject(response.getContent());
					JSONArray jsonArray = jsonObj.getJSONArray("data");
					totalTrack = jsonObj.getInt("totals");

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj1 = jsonArray.getJSONObject(i);
						String name = jsonObj1.getString("name");
						String track_link = jsonObj1.getString("link");
						int id = jsonObj1.getInt("id");

						String mota = jsonObj1.getString("description");
						//String image = jsonObj1.getString("image");
						String length = jsonObj1.getString("length");
						if (length.equalsIgnoreCase("null")) {
							length = "N/A";
						} else {
							int milisec = Integer.parseInt(length);
							length = UtilFuntions.getTimeString(milisec);
						}
						HashMap<String, Object> item = new HashMap<String, Object>();
					 
						System.out.println(">>>>>>>> NAME: " + name);
						item.put("title", name);
						item.put("mota", mota);
						item.put("length", length);
						item.put("track_id", id);
						item.put("track_link", track_link);
						data.add(item);
					}
					return Boolean.TRUE;
				} else {
					return Boolean.FALSE;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (data.size() >= (totalTrack)) {
				lvRadioTrack.setPullLoadEnable(false);
				lvRadioTrack.setPullRefreshEnable(false);
			} else {
				lvRadioTrack.setPullLoadEnable(true);

			}
			if (result != null && getActivity() != null)
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						lvRadioTrack.stopLoadMore();
						lvRadioTrack.stopRefresh();
						lvRadioTrack.setVisibility(View.VISIBLE);
						llLoading.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
					}
				});
			else
				new AlertDialog.Builder(getActivity()).setTitle("Lỗi").setMessage("Lỗi mạng").show();
		}
	}

	@Override
	public void onRefresh() {
		lvRadioTrack.setRefreshTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));
		new SubCategoryLoader(catId).execute();
	}

	@Override
	public void onLoadMore() {
		//		if (mCurrentState == STATE_CATEGORY) {
		//
		//		}
		//		if (mCurrentState == STATE_SUBCATEGORY) {
		//			new SubCategoryLoader(mCurrentCategoryId).execute();
		//		}
	}
}
