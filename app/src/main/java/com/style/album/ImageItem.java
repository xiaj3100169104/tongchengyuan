package com.style.album;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	private String thumbnPath;
	private String imagePath;
	private int size;
	private String displayName;
	private String title;
	private double latitude;
	private double longitude;
	private String bucketName;
	private boolean isSelected = false;
	private int section;
	private long addedTime;
	private String time;

	private ImageItem() {
		super();
	}

	public ImageItem(String imagePath, long addedTime, int size, String displayName, String title, double latitude, double longitude, String bucketName,
			boolean isSelected) {
		super();
		this.imagePath = imagePath;
		this.addedTime = addedTime;
		this.size = size;
		this.displayName = displayName;
		this.title = title;
		this.latitude = latitude;
		this.longitude = longitude;
		this.bucketName = bucketName;
		this.isSelected = isSelected;
	}

	public String getThumbnPath() {
		return thumbnPath;
	}

	public void setThumbnPath(String thumbnPath) {
		this.thumbnPath = thumbnPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public long getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(long addedTime) {
		this.addedTime = addedTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
