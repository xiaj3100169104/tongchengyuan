package com.style.album;

import java.io.Serializable;
import java.util.List;

/**
 * 一个图片文件夹对象
 * 
 * @author Administrator
 * 
 */
public class PicBucket implements Serializable {
	private String bucketName;
	private List<ImageItem> images;

	public PicBucket() {
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public List<ImageItem> getImages() {
		return images;
	}

	public void setImages(List<ImageItem> images) {
		this.images = images;
	}

}
