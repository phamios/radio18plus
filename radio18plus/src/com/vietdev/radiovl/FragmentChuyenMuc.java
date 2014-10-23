package com.vietdev.radiovl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vsmjsc.rdgt.cloudservice.HttpGet;
import com.vsmjsc.rdgt.cloudservice.HttpResponse;
import com.vsmjsc.rdgt.configs.Defines;
import com.vsmjsc.rdgt.widget.PagingListView;
import com.vsmjsc.rdgt.widget.PagingListView.PagingListViewListener;

public class FragmentChuyenMuc extends Fragment implements PagingListViewListener, OnItemClickListener {

	private PagingListView lvCategory;
	private SimpleAdapter adapterCategory;
	private static List<HashMap<String, Object>> dataCategory = new ArrayList<HashMap<String, Object>>();

	private View llLoading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_layout_chuyenmuc, null, false);
		llLoading = v.findViewById(R.id.llLoading);
		lvCategory = (PagingListView) v.findViewById(R.id.lvChuyenMuc);
		adapterCategory = new SimpleAdapter(getActivity(), dataCategory, R.layout.item_chuyenmuc, new String[] { "name" }, new int[] { R.id.txtTenChuyenMuc });
		lvCategory.setAdapter(adapterCategory);
		lvCategory.setOnItemClickListener(this);
		lvCategory.setPullRefreshEnable(true);
		lvCategory.setPullLoadEnable(false);
		lvCategory.setPagingListViewListener(this);
		getActivity().findViewById(R.id.imgHeaderIndicator).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.llHeader).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ScreenHomeActivity)getActivity()).onClick_Back(v);
			}
		});
		getActivity().findViewById(R.id.llHeader).setClickable(true);
		//load chuyen muc
		new CategoryDataLoader().execute();
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
		String catName = dataCategory.get(pos-1).get("name").toString();
		((TextView)getActivity().findViewById(R.id.txtTitle)).setText(catName);
		FragmentRadioTrack fragment = new FragmentRadioTrack(catName);
		((ScreenHomeActivity)getActivity()).setCurrentFragmentTabRadioShop(fragment);
		fragment.setCatId(Integer.parseInt(dataCategory.get(pos-1).get("cat_id").toString()));
		getFragmentManager().beginTransaction().replace(R.id.llContainer, fragment).addToBackStack(null).commit();
	}

	class CategoryDataLoader extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			HttpGet httpget = new HttpGet();
			try {
				String jsonString = null;
				File cacheCategory = new File("/data/data/com.vsmjsc.rdgt/cat.xml");
				if (cacheCategory.exists()) {
					jsonString = (String) new XStream(new DomDriver()).fromXML(cacheCategory);
				} else {
					HttpResponse response = httpget.getDataInputStreamFromUrl("http://admega.vn/api/list_cate/116");
					if (response != null && response.getHttpCode() == 200) {
						jsonString = response.getContent();
					}
				}
				if (isRefresh) {
					isRefresh = false;
					dataCategory.clear();
				}
				if (jsonString != null) {
					JSONObject jsonObj = new JSONObject(jsonString);
					JSONArray jsonArray = jsonObj.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj1 = jsonArray.getJSONObject(i);
						int cat_id = jsonObj1.getInt("id");
						boolean needSkip = false;
						for (int skipCatId : Defines.IGNORE_CATEGORY) {
							if (cat_id == skipCatId) {
								needSkip = true;
								break;
							}
						}
						if (!needSkip) {
							String name = jsonObj1.getString("name");
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("name", name);
							item.put("cat_id", cat_id);

							dataCategory.add(item);
						}
					}
//					new XStream().toXML(jsonString, new FileOutputStream("/data/data/com.vsmjsc.rdgt/cat.xml"));
					return Boolean.TRUE;
				} else {
					return Boolean.FALSE;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result == null) {
				new AlertDialog.Builder(getActivity()).setTitle("Lỗi").setMessage("Lỗi trong lúc tải radio. Vui lòng thử lại").setPositiveButton("Ok", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						getActivity().finish();
					}
				}).show();
			} else {
				if (result) {
					if (getActivity() != null) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								llLoading.setVisibility(View.GONE);
								lvCategory.setVisibility(View.VISIBLE);
								lvCategory.setPullLoadEnable(false);
								lvCategory.stopLoadMore();
								lvCategory.stopRefresh();
								adapterCategory.notifyDataSetChanged();
							}
						});
					} else {
						new AlertDialog.Builder(getActivity()).setTitle("Lỗi ứng dụng").setMessage("Activity Null").setPositiveButton("Ok", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								getActivity().finish();
							}
						}).show();
					}
				} else {
					new AlertDialog.Builder(getActivity()).setTitle("Lỗi server").setMessage("request không được đáp ứng").setPositiveButton("Ok", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							getActivity().finish();
						}
					}).show();
				}
			}

			super.onPostExecute(result);
		}
	}

	private boolean isRefresh;

	@Override
	public void onRefresh() {
		isRefresh = true;
		lvCategory.setRefreshTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));
		new CategoryDataLoader().execute();
	}

	@Override
	public void onLoadMore() {
	}
}
