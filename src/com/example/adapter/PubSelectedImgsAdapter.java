package com.example.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import com.example.memo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PubSelectedImgsAdapter extends BaseAdapter {

	Context context;
	List<String> list;
	
	public PubSelectedImgsAdapter(Context context,List<String> data) {
		this.context=context;
		this.list=data;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Holder holder;
		if (view==null) {
			view=LayoutInflater.from(context).inflate(R.layout.pub_selected_imgs_item, null);
			holder=new Holder();
			holder.imageView=(ImageView) view.findViewById(R.id.imageView);
			view.setTag(holder);
		}else {
			holder= (Holder) view.getTag();
		}
		ImageLoader.getInstance().displayImage("file://"+list.get(position),holder.imageView);
		return view;
	}
	
	class Holder{
		ImageView imageView;
	}
	
}
