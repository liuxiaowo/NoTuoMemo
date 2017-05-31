package com.example.adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memo.ListTextActivity;
import com.example.memo.R;
import com.example.sqlLite.SqlLiteOpenHelper;

public class AllListAdapter extends BaseAdapter{
	ArrayList<History> hlist;
	LayoutInflater inflater;
	int id;
	Context context;
	public int list_kind=0; //1-备忘录list ;2-极简list
	public boolean check_all=false;

	public AllListAdapter(ArrayList<History> hlist, Context context){
		this.hlist=hlist;
		this.context=context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hlist.size();
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
		final ViewHolder holder;

		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item, null);
			holder.star = (ImageView) v.findViewById(R.id.list_item_star);
			holder.time = (TextView) v.findViewById(R.id.list_item_time);
			holder.tit = (TextView) v.findViewById(R.id.list_item_title);
			holder.con = (TextView) v.findViewById(R.id.list_item_content);
			holder.checked=(CheckBox) v.findViewById(R.id.list_item_check);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		if(!hlist.isEmpty()){
			(holder.tit).setText(hlist.get(position).getTitle());
			(holder.con).setText(hlist.get(position).getContent());
			(holder.time).setText(hlist.get(position).getTime());

			if(hlist.get(position).isStar()){
				(holder.star).setBackgroundResource(R.drawable.star_checked);
			}else{
				(holder.star).setBackgroundResource(R.drawable.star_uncheck);
			}
			if(check_all){
				holder.checked.setVisibility(holder.checked.VISIBLE);
				if(hlist.get(position).isChecked()) holder.checked.setChecked(true);
				else holder.checked.setChecked(false);
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Boolean ischeck=hlist.get(position).isChecked();
						if(!ischeck){
							hlist.get(position).setChecked(true);
							holder.checked.setChecked(true);
						} 
						else {
							hlist.get(position).setChecked(false);
							holder.checked.setChecked(false);
						}
					}
				});
			}else{
				v.setClickable(false);
				holder.checked.setVisibility(holder.checked.INVISIBLE);
			}
			holder.star.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					id = position;
					v.setBackgroundResource(clickStar());
				}
			});
//			holder.checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					// TODO Auto-generated method stub
//					if(isChecked) hlist.get(position).setChecked(true);
//					else hlist.get(position).setChecked(false);
//				}
//			});
		}
		
		return v;
	}

	public class ViewHolder {
		TextView tit;
		TextView con;
		TextView time;
		ImageView star;
		CheckBox checked;
	}
	
	public int clickStar() {
		if(hlist.get(id).isStar()){//点亮情况下再次点击
					hlist.get(id).setStar(false);
					starChange(hlist.get(id), 0);
					return R.drawable.star_uncheck;
		}else{
			hlist.get(id).setStar(true);
			starChange(hlist.get(id), 1);
			return R.drawable.star_checked;
		}
	}
	
	public void starChange(History m,int i){
		SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(context);
		SQLiteDatabase db=fdb.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("star",i);
		String sql;
		if(list_kind==1) sql="memo_data";
		else sql="text_data";
		db.update(sql, cv, "creat_time=?",new String[]{m.getTime()}); 
		db.close();
	}
	
}
