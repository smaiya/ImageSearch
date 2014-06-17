package com.smaiya.example.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;

public class SearchFilterActivity extends Activity {

	private Spinner spSize;
	private Spinner spColor;
	private Spinner spType;
	private Switch swSafeSearch;

	private ArrayAdapter<CharSequence> sizeAdapter;
	private ArrayAdapter<CharSequence> colorAdapter;
	private ArrayAdapter<CharSequence> typeAdapter;

	private FilterSettings filterSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_filter);

		createFilterElements();

		// Get the value of filter elements from the intent and set it.
		filterSettings = (FilterSettings) getIntent().getSerializableExtra(
				"settings");

		// Set the values of spinner
		if (filterSettings != null) {
			spSize.setSelection(sizeAdapter.getPosition(filterSettings
					.getImageSize()));
			spColor.setSelection(colorAdapter.getPosition(filterSettings
					.getColorFilter()));
			spType.setSelection(typeAdapter.getPosition(filterSettings
					.getImageType()));
			swSafeSearch.setChecked(filterSettings.isSafeSearch());
		}
	}

	private void createFilterElements() {
		// Size
		spSize = (Spinner) findViewById(R.id.spSize);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		sizeAdapter = ArrayAdapter.createFromResource(this, R.array.size_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sizeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spSize.setAdapter(sizeAdapter);

		// ItemSelection listener
		spSize.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> aView, View v, int pos,
					long id) {
				filterSettings
						.setImageSize(sizeAdapter.getItem(pos).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		// Color
		spColor = (Spinner) findViewById(R.id.spCol);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		colorAdapter = ArrayAdapter.createFromResource(this,
				R.array.color_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		colorAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spColor.setAdapter(colorAdapter);

		// ItemSelection listener
		spColor.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> aView, View v, int pos,
					long id) {
				filterSettings.setColorFilter(colorAdapter.getItem(pos)
						.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		// Type
		spType = (Spinner) findViewById(R.id.spType);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		typeAdapter = ArrayAdapter.createFromResource(this, R.array.type_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spType.setAdapter(typeAdapter);

		// ItemSelection listener
		spType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> aView, View v, int pos,
					long id) {
				filterSettings
						.setImageType(typeAdapter.getItem(pos).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		
		//Safe Search
		swSafeSearch = (Switch)findViewById(R.id.swSafesearch);
		
		//On checked change listener
		swSafeSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				filterSettings.setSafeSearch(isChecked);
				
			}
		});

	}

	/*
	 * On click handler for the save button. Save the data and return to the
	 * caller through an "Intent".
	 */
	public void saveFilterSettings(View v) {
		Intent data = new Intent();
		data.putExtra("settings", filterSettings);
		// Set the result and return to the caller
		setResult(RESULT_OK, data);
		// Activity is done, close it.
		finish();
	}

}
