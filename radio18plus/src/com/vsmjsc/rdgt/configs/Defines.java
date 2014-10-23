package com.vsmjsc.rdgt.configs;


public interface Defines {
//	public static final String API_ROOT_URL = "http://vietnetmedia.vn/phuongboo/api";
	public static final String API_ROOT_URL = "http://admega.vn/api";
	public static final String TRACK_URL = API_ROOT_URL+"?cmd=track&id=";
	
	public static final String INTENT_ACTION_MEDIA_OK = "com.vsmjsc.vnradio.action.ACTION_MEDIA_PLAYING";
	public static final String INTENT_ACTION_MEDIA_ERROR = "com.vsmjsc.vnradio.action.ACTION_MEDIA_ERROR";
	public static final String INTENT_ACTION_MEDIA_PREPARING = "com.vsmjsc.vnradio.action.ACTION_MEDIA_PREPARING";
	
	public static int IGNORE_CATEGORY[] = {};
	public static int FREE_TRACK[] = {
		18,19,
		25,26,
		30,31,
		37,38,
		84,85,
		53,54,
		61,62,
		82,83,
		75,76,
		78,79};
}
