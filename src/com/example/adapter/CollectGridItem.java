package com.example.adapter;

public class CollectGridItem {
	private int tag;
	private int num;
	public int imgID;
	
	public CollectGridItem(int tag,int num, int imgID) {
		setTag(tag);
		setNum(num);
		setImgID(imgID);
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public int getImgID() {
		return imgID;
	}

	public void setImgID(int ImgID) {
		this.imgID = ImgID;
	}


}
