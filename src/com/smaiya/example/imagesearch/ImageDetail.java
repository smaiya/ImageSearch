package com.smaiya.example.imagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private String completeUrl;
	private String thumbnail;
	private String imageDesc;

	public ImageDetail(JSONObject o) {
		try {
			setCompleteUrl(o.getString("url"));
			setThumbnail(o.getString("tbUrl"));
			setImageDesc(o.getString("height") + "X" + o.getString("width") + " - " + o.getString("visibleUrl"));
		
		} catch (JSONException e) {
			setCompleteUrl(null);
			setThumbnail(null);
			setImageDesc(null);
			
		}
	}

	public static ArrayList<ImageDetail> convertJSON(JSONArray json) {
		ArrayList<ImageDetail> imgList = new ArrayList<ImageDetail>();
		for (int i = 0; i < json.length(); i++) {
			try {
				JSONObject obj = json.getJSONObject(i);
				imgList.add(new ImageDetail(obj));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return imgList;
	}

	public String getCompleteUrl() {
		return completeUrl;
	}

	public void setCompleteUrl(String completeUrl) {
		this.completeUrl = completeUrl;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}
}