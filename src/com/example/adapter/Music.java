package com.example.adapter;

public class Music {
	private String name;
	private String path;
	private boolean checked;
	
	public  Music(String name,String path){
		setName(name);
		setPath(path);
		setChecked(false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
