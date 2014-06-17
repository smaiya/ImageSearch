package com.smaiya.example.imagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ImageFullviewActivity extends Activity {
	private SmartImageView smartImage;
	private TextView tvDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_fullview);

		smartImage = (SmartImageView) findViewById(R.id.simgDisplay);
		ImageDetail imgDetail = (ImageDetail) getIntent().getSerializableExtra("selImage");
		final String url = imgDetail.getCompleteUrl();
		smartImage.setImageUrl(url);
		
		tvDescription = (TextView)findViewById(R.id.tvDescription);
		tvDescription.setText(imgDetail.getImageDesc());
		
	}
}