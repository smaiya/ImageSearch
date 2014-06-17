package com.smaiya.example.imagesearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class ImageSearchActivity extends Activity {

	protected final int FILTER_SETTINGS_REQUEST_CODE = 10;
	protected final String fileName = "filter_settings.txt";

	private String searchString;
	private FilterSettings filterSettings;
	private EditText etSearch;
	private GridView gvImages;
	private ArrayList<ImageDetail> imageList;
	private ImageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);

		filterSettings = new FilterSettings();

		// Read filter settings
		readFilterSettings();

		etSearch = (EditText) findViewById(R.id.etSearch);
		gvImages = (GridView) findViewById(R.id.gvImages);

		imageList = new ArrayList<ImageDetail>();
		adapter = new ImageAdapter(this, imageList);
		gvImages.setAdapter(adapter);

		// Set infinite scroll support
		gvImages.setOnScrollListener(new InfiniteScroller() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				handleImageSearch(page);
			}
		});

		// Display image in full view
		gvImages.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageDetail target = imageList.get(position);
				Intent i = new Intent(getApplicationContext(),
						ImageFullviewActivity.class);
				i.putExtra("selImage", target);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search, menu);
		return true;
	}

	/*
	 * On Settings handler
	 */
	public void onSettings(MenuItem mi) {
		Intent i = new Intent(this, SearchFilterActivity.class);
		i.putExtra("settings", filterSettings);
		startActivityForResult(i, FILTER_SETTINGS_REQUEST_CODE);
	}

	/*
	 * Handle the return data after editing.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == FILTER_SETTINGS_REQUEST_CODE) {
			// Get the filter settings
			filterSettings = (FilterSettings) data
					.getSerializableExtra("settings");
			Toast.makeText(this, "Applying new filter settings !!! ",
					Toast.LENGTH_SHORT).show();
			// Save the filter settings
			saveFilterSettings();

			// clear the current result
			adapter.clear();

			// Handle the search
			handleImageSearch(0);

		}
	}

	/*
	 * Search button on click handler
	 */
	public void onSearchImages(View v) {
		// If it's a new string other than the previous search
		if (!etSearch.getText().toString().trim().equals(searchString)) {
			// clear the current result
			adapter.clear();
			// Set the search string
			searchString = etSearch.getText().toString().trim();
			// Handle the search
			handleImageSearch(0);
		}
	}

	/*
	 * Utility used to read the filter settings from the file.
	 */
	private void readFilterSettings() {
		ObjectInputStream input;
		try {
			input = new ObjectInputStream(new FileInputStream(new File(
					new File(getFilesDir(), "") + File.separator + fileName)));
			filterSettings = (FilterSettings) input.readObject();
			if (filterSettings == null)
				filterSettings.SetDefaultValues();
			input.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Utility used to write the Filter settings to a file.
	 */
	private void saveFilterSettings() {
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(new File(
					getFilesDir(), "") + File.separator + fileName));
			out.writeObject(filterSettings);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Checking for Network Connectivity
	 */
	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	/*
	 * Handle Image Search
	 */
	private void handleImageSearch(int pageCount) {
		// Verify the network connection and the search string
		if (isNetworkAvailable() && (searchString != null)) {
			String url = "https://ajax.googleapis.com/ajax/services/search/images";
			AsyncHttpClient client = new AsyncHttpClient();

			// Construct the Query JSON with the filter options
			RequestParams params = new RequestParams();
			params.put("q", searchString.toString());
			params.put("rsz", "8");
			params.put("v", "1.0");
			params.put("imgsz", filterSettings.getImageSize());
			params.put("imgc", filterSettings.getColorOptions());
			params.put("imgtype", filterSettings.getTypeOptions());

			// Enable Safe search
			if (filterSettings.isSafeSearch())
				params.put("safe", "active");
			params.put("start", pageCount);

			client.get(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray result = null;
					try {
						result = response.getJSONObject("responseData")
								.getJSONArray("results");
						adapter.addAll(ImageDetail.convertJSON(result));
					} catch (JSONException e) {
						Toast.makeText(
								ImageSearchActivity.this,
								"Couldn't fetch a proper result..Try again !!!",
								Toast.LENGTH_SHORT).show();
						searchString = null;
					} catch (OutOfMemoryError e) {
						Toast.makeText(ImageSearchActivity.this,
								"Internal error occured. Try again !!!",
								Toast.LENGTH_SHORT).show();
						searchString = null;
					}
				}

				@Override
				public void onFailure(Throwable e, String s) {
					Toast.makeText(ImageSearchActivity.this,
							"Image Search Failed!.", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
