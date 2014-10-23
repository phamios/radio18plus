package com.vietdev.radiovl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vsmjsc.rdgt.sqlite.CacheManager;
import com.vsmjsc.rdgt.widget.PagingListView;

public class FragmentDaMua extends Fragment {

	private PagingListView lvDaMua;
	private SimpleAdapter adapter;
	private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_layout_damua, null);
		getActivity().findViewById(R.id.imgHeaderIndicator).setVisibility(View.GONE);
		getActivity().findViewById(R.id.llHeader).setOnClickListener(null);
		getActivity().findViewById(R.id.llHeader).setClickable(false);

		lvDaMua = (PagingListView) view.findViewById(R.id.lvRadioDaMua);

		String[] from = { "title", "mota" };
		int[] to = { R.id.txtTenRadio, R.id.txtDescription };
		adapter = new SimpleAdapter(getActivity(), data, R.layout.item_radiotrack, from, to);

		lvDaMua.setAdapter(adapter);
		new MyRadioLoader().execute();
		return view;
	}

	private class MyRadioLoader extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			//hardcode for 5 radio track demo, every category get one track to demo. This is the best radio track should be demo for customer
			//URL: http://api.radio18plus.radito.com/1.0/category/1/tracks
			
			//catID=1
//			HashMap<String, Object> item1 = new HashMap<String, Object>();
//			item1.put("title", "Yêu đều đặn và những lợi ích");
//			item1.put("mota", "Cùng chuyên gia khám phá những lợi ích của việc yêu thường xuyên và an toàn");
//			item1.put("track_id", "96");
//			data.add(item1);
//
//			//catID=2
//			HashMap<String, Object> item2 = new HashMap<String, Object>();
//			item2.put("title", "BJ liệu có thai");
//			item2.put("mota", "Một cô đồng nghiệp xinh xinh BJ cho tôi sau một tháng bảo là có thai...");
//			item2.put("track_id", "24");
//			data.add(item2);
//
//			//catID=3
//			HashMap<String, Object> item3 = new HashMap<String, Object>();
//			item3.put("title", "Tránh thai tự nhiên");
//			item3.put("mota", "Bạn không muốn dùng bao cao su vì nó làm giảm đi kích thích? Nhưng lại sợ có thai ? Hãy tìm hiểu về các cách tránh thai tự nhiên nhé.");
//			item3.put("track_id", "90");
//			data.add(item3);
//
//			//catID=4
//			HashMap<String, Object> item4 = new HashMap<String, Object>();
//			item4.put("title", "Gái trinh");
//			item4.put("mota", "Một cô gái hết ngậm của trai này, rồi cho trai khác làm việc qua hậu môn, nhưng cương quyết không quan hệ băng đường âm đạo có được gọi là trinh tiết không?");
//			item4.put("track_id", "38");
//			data.add(item4);
//
//			//catID=5
//			HashMap<String, Object> item5 = new HashMap<String, Object>();
//			item5.put("title", "Khi đàn ông ghen");
//			item5.put("mota", "Đàn ông ghen tuông được ví như là quả bom, một khi nổ thì tạo ra vết nứt sâu thẳm, gây mất niềm tin và cảm giác bị tổn thương trầm trọng.");
//			item5.put("track_id", "85");
//			data.add(item5);

			//separator for radio licensed
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			//Load radio track that enduser has bought. Load from cache. Noties that: when user buy one track then it must be save to cache. If user clear cache then he must loss licence for that track and he must repay if he want to listen again for that track  
			CacheManager cache = new CacheManager(getActivity());
			cache.open();
			//TODO: get radio track here and add to data.
			cache.close();
			return 0;//return number of radio that user has bought
		}

		@Override
		protected void onPostExecute(final Integer result) {
			//in some devices method onPostExcute was not run on UI thread. So that, to ensure about this. We must cover my code by method runOnUiThread
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					lvDaMua.setVisibility(View.VISIBLE);
					if (result == 0) {
						getActivity().findViewById(R.id.txtIndicator).setVisibility(View.GONE);
					} else {
						getActivity().findViewById(R.id.txtIndicator).setVisibility(View.GONE);

					}

					lvDaMua.setPullLoadEnable(false);
					lvDaMua.setPullRefreshEnable(false);
					lvDaMua.stopLoadMore();
					lvDaMua.stopRefresh();
					adapter.notifyDataSetChanged();
				}
			});
		}
	}
}
