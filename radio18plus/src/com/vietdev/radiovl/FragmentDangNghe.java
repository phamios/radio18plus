package com.vietdev.radiovl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vsmjsc.rdgt.model.RadioTrack;

public class FragmentDangNghe extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_track_detail, null);
		getActivity().findViewById(R.id.imgHeaderIndicator).setVisibility(View.GONE);
		getActivity().findViewById(R.id.llHeader).setOnClickListener(null);
		getActivity().findViewById(R.id.llHeader).setClickable(false);
		RadioTrack track = ((ScreenHomeActivity)getActivity()).getCurrentPlaying();
		if (track != null) {
			ImageLoader.getInstance().displayImage(track.getImageUrl(), (ImageView) view.findViewById(R.id.imgTrackAvatar));
			((TextView) view.findViewById(R.id.txtDescription)).setText(track.getDescription());
		} else {
			((TextView) view.findViewById(R.id.txtDescription)).setText("Không có radio nào đang nghe.");
		}
		
		return view;
	}
}
