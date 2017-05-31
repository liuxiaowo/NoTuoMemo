package com.example.adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.memo.R;
import com.example.sqlLite.SqlLiteOpenHelper;

public class MusicListAdapter extends BaseAdapter{
	ArrayList<Music> mlist;
	LayoutInflater inflater;
	int id;
	Context context;
	
	public MusicListAdapter(ArrayList<Music> mlist, Context context){
		this.context=context;
		this.mlist=mlist;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		ViewHolder holder;

		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.music_list_item, null);
			holder.item = (RadioButton) v.findViewById(R.id.music_item_check);
			holder.name=(TextView) v.findViewById(R.id.music_item_name);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		if(!mlist.isEmpty()){
			if(mlist.get(position).isChecked()) (holder.item).setChecked(true);
			else (holder.item).setChecked(false);
			holder.name.setText(mlist.get(position).getName());
		}
		
		return v;
	}

	public class ViewHolder {
		RadioButton item;
		TextView name;
	}
	
}
