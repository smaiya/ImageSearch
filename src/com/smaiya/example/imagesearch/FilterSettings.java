package com.smaiya.example.imagesearch;

import java.io.Serializable;

public class FilterSettings implements Serializable {
	private static final long serialVersionUID = 2007391636933777017L;

	private String imageSize;
	private String colorFilter;
	private String imageType;
	private boolean isSafeSearch;

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getColorFilter() {
		return colorFilter;
	}

	public void setColorFilter(String colorFilter) {
		this.colorFilter = colorFilter;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public void SetDefaultValues() {
		imageSize = "Any size";
		colorFilter = "Any color";
		imageType = "Any type";
		setSafeSearch(false);
	}

	public String getSizeOptions() {
		String size = "";

		switch (imageSize) {

		case "Large":
			size = "large";
			break;

		case "Medium":
			size = "medium";
			break;
		case "Icon":
			size = "small";
			break;
		default:
			break;
		}

		return size;
	}

	public String getColorOptions() {
		String color = "";

		switch (colorFilter) {

		case "Full color":
			color = "color";
			break;

		case "Black and white":
			color = "gray";
			break;
		case "Transparent":
			color = "trans";
			break;
		default:
			break;
		}

		return color;
	}

	public String getTypeOptions() {
		String type = "";

		switch (imageType) {

		case "Face":
			type = "face";
			break;
		case "Photo":
			type = "photo";
			break;
		case "Clip art":
			type = "clipart";
			break;
		case "Line drawing":
			type = "lineart";
			break;
		case "Animated":
			type = "animated";
			break;

		default:
			break;
		}
		return type;
	}

	public boolean isSafeSearch() {
		return isSafeSearch;
	}

	public void setSafeSearch(boolean isSafeSearch) {
		this.isSafeSearch = isSafeSearch;
	}

}
