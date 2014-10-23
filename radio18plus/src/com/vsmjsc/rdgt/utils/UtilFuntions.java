package com.vsmjsc.rdgt.utils;

import com.vsmjsc.rdgt.configs.Defines;

public class UtilFuntions {
	public static String getRealUrl(String rawUrl) {
		rawUrl = rawUrl.trim();
		if (!rawUrl.startsWith("http://")) {
			if (!rawUrl.startsWith("/")) {
				rawUrl = "/" + rawUrl;
			}
			 rawUrl = "http://admega.vn/" + rawUrl;
		 
		}
		return rawUrl.replaceAll(" ", "%20"); 
	}

	public static String getTimeString(int milisec) {
		int total_ss = milisec / (1000);
		int mm = total_ss / (60);
		int ss = total_ss % (60);

		return (mm < 10 ? ("0" + mm) : mm) + ":" + (ss < 10 ? "0" + ss : ss);
	}

	public static boolean checkFree(int trackId) {
		for (int i = 0; i < Defines.FREE_TRACK.length; i++) {
			if (trackId == Defines.FREE_TRACK[i]) {
				return true;
			}
		}

		return false;
	}
}
