package com.smaiya.example.imagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageAdapter extends ArrayAdapter<ImageDetail> {

	public ImageAdapter(Context context, List<ImageDetail> images) {
		super(context, R.layout.activity_image_search, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageDetail image = this.getItem(position);
		
		/*
		 * http://loopj.com/android-smart-image-view/
		 */
		SmartImageView smartImage;

		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			smartImage = (SmartImageView) inflator.inflate(R.layout.image_result, parent, false);
		} else {
			smartImage = (SmartImageView) convertView;
			smartImage.setImageResource(android.R.color.transparent);
		}

		smartImage.setImageUrl(image.getThumbnail());

		return smartImage;
	}
}