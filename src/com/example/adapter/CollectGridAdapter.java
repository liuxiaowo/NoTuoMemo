package com.example.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.memo.R;

public class CollectGridAdapter extends BaseAdapter{
	ArrayList<CollectGridItem> list;
	LayoutInflater inflater;

	public CollectGridAdapter(ArrayList<CollectGridItem> list, Context context) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v=convertView;
		ViewHolder holder;
		if(v==null)
		{
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.collect_grid_child_item, null);
			holder.im=(ImageView) v.findViewById(R.id.collect_item);
			v.setTag(holder);
		}else{
			//������ͼ
			holder=(ViewHolder)v.getTag();
			
		}
		
//		holder.tv2.setText(String.valueOf(list.get(position).getNum()));
		holder.im.setBackgroundResource(list.get(position).getImgID());
		return v;
	}
	public class ViewHolder{
//		TextView tv1;
//		TextView tv2;
		ImageView im;
		
	}
}
