package com.vsmjsc.rdgt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RadioTrack implements Parcelable {

	private int trackId, views, likes, catId;
	private String date, name, description, audioUrl, imageUrl, cat_alias, track_alias, length;

	public int getId() {
		return trackId;
	}

	public void setId(int id) {
		trackId = id;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getViews() {
		return views;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getLikes() {
		return likes;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String url) {
		this.audioUrl = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String image_url) {
		this.imageUrl = image_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(description);
		dest.writeString(date);
		dest.writeString(name);
		dest.writeString(imageUrl);
		dest.writeString(audioUrl);
		dest.writeInt(trackId);
		dest.writeInt(views);
		dest.writeInt(likes);
	}

	public void readFromParcel(Parcel source) {
		description = source.readString();
		date = source.readString();
		name = source.readString();
		imageUrl = source.readString();
		audioUrl = source.readString();
		trackId = source.readInt();
		views = source.readInt();
		likes = source.readInt();
	}

	public RadioTrack(Parcel in) {
		readFromParcel(in);
	}

	public RadioTrack() {
	}

	public String getCat_alias() {
		return cat_alias;
	}

	public void setCat_alias(String cat_alias) {
		this.cat_alias = cat_alias;
	}

	public String getTrack_alias() {
		return track_alias;
	}

	public void setTrack_alias(String track_alias) {
		this.track_alias = track_alias;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public RadioTrack createFromParcel(Parcel in) {
			return new RadioTrack(in);
		}

		public RadioTrack[] newArray(int size) {
			return new RadioTrack[size];
		}
	};
}
