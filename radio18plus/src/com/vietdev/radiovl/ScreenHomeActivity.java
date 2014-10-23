package com.vietdev.radiovl;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.vsmjsc.rdgt.model.RadioTrack;

public class ScreenHomeActivity extends SherlockFragmentActivity {

	public static final String BROADCAST_FOR_FRAGMENT_CATEGORY = "com.vsmjsc.vnradio.FragmentCategory";
	private AdView adView;

	private InterstitialAd mInterstitial;
	private Fragment currentFragmentTabRadioShop;
	private Fragment currentFragmentTabDangNghe;
	private RadioTrack currentPlaying;

	public void setRadioPlaying(RadioTrack track) {
		currentPlaying = track;
	}

	public RadioTrack getCurrentPlaying() {
		return currentPlaying;
	}

	public void setCurrentFragmentTabRadioShop(Fragment fragment) {
		currentFragmentTabRadioShop = fragment;
	}

	public Fragment getCurrentFragmentTabRadioShop() {
		return currentFragmentTabRadioShop;
	}

	@Override
	public void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480, 800).discCacheExtraOptions(240, 400, CompressFormat.JPEG, 75).threadPoolSize(3) // default
				.threadPriority(Thread.NORM_PRIORITY - 1) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(1 * 1024 * 1024)) // default
				.discCache(new UnlimitedDiscCache(cacheDir)) // default

				.imageDownloader(new BaseImageDownloader(this)) // default
				.defaultDisplayImageOptions(defaultOptions) // default
				.build();
		ImageLoader.getInstance().init(config);

		super.onCreate(arg0);
		setContentView(R.layout.screen_home);
		displayAdmob();
		currentFragmentTabRadioShop = new FragmentChuyenMuc();
		getSupportFragmentManager().beginTransaction().replace(R.id.llContainer, currentFragmentTabRadioShop).addToBackStack(null).commit();
	}

	private void displayAdmob(){ 
		AdView adViewCurrent = (AdView) this.findViewById(R.id.adView);   
		AdRequest adRequestCurrent = new AdRequest.Builder().build(); 
		adViewCurrent.loadAd(adRequestCurrent);
	}
	
	public void startInterstitial() {
		mInterstitial = new InterstitialAd(ScreenHomeActivity.this);
		mInterstitial.setAdUnitId("ca-app-pub-1214276490829950/1631437420");
 
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("")
				.build();
 
		mInterstitial.loadAd(adRequest);
		mInterstitial.setAdListener(new AdListener() {
			public void onAdLoaded() { 
				displayInterstitial();
			}
		});
	}

	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (mInterstitial.isLoaded()) {
			mInterstitial.show();
		}
	}
	public void onClick_Back(View v) {
		if (getSupportFragmentManager().findFragmentById(R.id.llContainer) instanceof FragmentRadioTrack) {
			currentFragmentTabRadioShop = new FragmentChuyenMuc();
			getSupportFragmentManager().beginTransaction().replace(R.id.llContainer, currentFragmentTabRadioShop).addToBackStack(null).commit();
		}
	}

	public void onClick_Play(View v) {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
			((ImageView) findViewById(R.id.imgButtonPlay)).setImageResource(R.drawable.play);
		} else if (mPlayer != null && !mPlayer.isPlaying()) {
			mPlayer.start();
			((ImageView) findViewById(R.id.imgButtonPlay)).setImageResource(R.drawable.pause);
		} else {
			//Play demo radio track
		}
	}

	public void onClick_RadioShop(View v) {
		if (currentFragmentTabRadioShop instanceof FragmentRadioTrack) {
			((TextView) findViewById(R.id.txtTitle)).setText(((FragmentRadioTrack) currentFragmentTabRadioShop).getCategoryName());
		} else {
			((TextView) findViewById(R.id.txtTitle)).setText(getResources().getString(R.string.txt_category_top));
		}

		findViewById(R.id.tabDaMua).setBackgroundColor(Color.TRANSPARENT);
		findViewById(R.id.tabDangNghe).setBackgroundColor(Color.TRANSPARENT);
		findViewById(R.id.tabRadioShop).setBackgroundResource(R.drawable.radio_shop_bg);
		//getSupportFragmentManager().beginTransaction().replace(R.id.llContainer, currentFragmentTabRadioShop).addToBackStack(null).commit();
	}

	public void onClick_DangNghe(View v) {
		((TextView) findViewById(R.id.txtTitle)).setText(getResources().getString(R.string.txt_listening_menu));
		findViewById(R.id.tabDaMua).setBackgroundColor(Color.TRANSPARENT);
		findViewById(R.id.tabDangNghe).setBackgroundResource(R.drawable.dangnghe_bg);
		findViewById(R.id.tabRadioShop).setBackgroundColor(Color.TRANSPARENT);
		getSupportFragmentManager().beginTransaction().replace(R.id.llContainer, currentFragmentTabDangNghe = new FragmentDangNghe()).addToBackStack(null).commit();
	}

	public void onClick_DaMua(View v) {
		((TextView) findViewById(R.id.txtTitle)).setText(getResources().getString(R.string.txt_downloaded));
		findViewById(R.id.tabDaMua).setBackgroundResource(R.drawable.radio_damua_bg);
		findViewById(R.id.tabDangNghe).setBackgroundColor(Color.TRANSPARENT);
		findViewById(R.id.tabRadioShop).setBackgroundColor(Color.TRANSPARENT);
		((TextView) findViewById(R.id.txtTitle)).setText("Radio đã mua");
		getSupportFragmentManager().beginTransaction().replace(R.id.llContainer, new FragmentDaMua()).addToBackStack(null).commit();
	}

	public Fragment getCurrentFragmentTabDangNghe() {
		return currentFragmentTabDangNghe;
	}

	public void setCurrentFragmentTabDangNghe(Fragment currentFragmentTabDangNghe) {
		this.currentFragmentTabDangNghe = currentFragmentTabDangNghe;
	}

	
	
 
	
	public void playRadioTrack(final RadioTrack radio) {
		new AsyncTask<Void, Integer, Void>() {
			private String maxDurationText;
			
			protected void onPreExecute() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() { 
						findViewById(R.id.imgButtonPlay).setVisibility(View.GONE);
						findViewById(R.id.progressLoadingMedia).setVisibility(View.VISIBLE);
						((TextView) findViewById(R.id.txtRadioName)).setText(radio.getName());
					}
				});
			};

			@Override
			protected Void doInBackground(Void... params) {  
//				System.out.println(">>>> BUG >>>>" + radio.getAudioUrl());
//				Log.wtf(">>>>>>>>>>>>", radio.getAudioUrl());
				if (mPlayer == null) {
					Uri uri = Uri.parse(radio.getAudioUrl()); 
					MediaPlayer mPlayer = MediaPlayer.create(ScreenHomeActivity.this, uri);
					mPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
						@Override
						public void onBufferingUpdate(MediaPlayer mp, int percent) {
							publishProgress(mp.getDuration(), mp.getCurrentPosition(), percent);
						}
					});
					mPlayer.start();
				} else {
					mPlayer.stop();
					mPlayer.reset();
					mPlayer.release();
				}
				 
//				Uri uri = Uri.parse(radio.getAudioUrl());
//				MediaPlayer mPlayer = MediaPlayer.create(ScreenHomeActivity.this, uri);
				
				  
	            return null;
	             
			}

			protected void onProgressUpdate(Integer... values) {
				((SeekBar) findViewById(R.id.seekBar)).setMax(values[0]);
				((SeekBar) findViewById(R.id.seekBar)).setProgress(values[1]);
				((SeekBar) findViewById(R.id.seekBar)).setSecondaryProgress(values[2] * values[0] / 100);
				int seconds = (int) (values[1] / 1000) % 60;
				int minutes = (int) ((values[1] / (1000 * 60)) % 60);
				((TextView) findViewById(R.id.txtProgress)).setText(minutes + ":" + seconds);
				((TextView) findViewById(R.id.txtProgress)).setText("");
			};

			protected void onPostExecute(Void result) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						findViewById(R.id.imgButtonPlay).setVisibility(View.VISIBLE);
						findViewById(R.id.progressLoadingMedia).setVisibility(View.GONE);
					}
				});
			};
		}.execute();

	}
 
	private MediaPlayer mPlayer; 
}
