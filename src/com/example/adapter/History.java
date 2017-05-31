package com.example.adapter;

import java.io.Serializable;
import java.util.ArrayList;


public class History implements Serializable{
	private boolean star;
	private String title;
	private String content;
	private String time;
	private boolean checked;
	private String picture;
	private String voice;
	private String start_time;
	private String end_time;
	private int shark;
	private int client;
	private String ring;
	private int notuo;
	private int remind;
	
	public History(String start_time,String end_time){
		setStart_time(start_time);
		setEnd_time(end_time);
	}
	
	public History(String title,String content,String time){
		setContent(content);
		setStar(false);
		setTime(time);
		setTitle(title);
		setChecked(false);
	}
	public History(String title,String content,String time,String picture,String voice,String start_time,String end_time,int shark,int client,String ring,int notuo,int remind){
		setContent(content);
		setStar(false);
		setTime(time);
		setTitle(title);
		setPicture(picture);
		setVoice(voice);
		setStart_time(start_time);
		setEnd_time(end_time);
		setShark(shark);
		setClient(client);
		setRing(ring);
		setNotuo(notuo);
		setRemind(remind);
		setChecked(false);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isStar() {
		return star;
	}

	public void setStar(boolean star) {
		this.star = star;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public int getShark() {
		return shark;
	}
	public void setShark(int shark) {
		this.shark = shark;
	}
	public int getClient() {
		return client;
	}
	public void setClient(int client) {
		this.client = client;
	}
	public String getRing() {
		return ring;
	}
	public void setRing(String ring) {
		this.ring = ring;
	}
	public int getNotuo() {
		return notuo;
	}
	public void setNotuo(int notuo) {
		this.notuo = notuo;
	}
	public int getRemind() {
		return remind;
	}
	public void setRemind(int remind) {
		this.remind = remind;
	}
}
