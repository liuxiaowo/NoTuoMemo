package com.example.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter{
	ArrayList<View> viewlist;
	
	public ViewPagerAdapter(ArrayList<View> viewlist){
		this.viewlist=viewlist;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager)container).removeView(viewlist.get(position));   
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		((ViewPager)container).addView(viewlist.get(position));
		return viewlist.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewlist.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

}
