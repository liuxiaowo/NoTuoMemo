package com.example.adapter;

import java.util.ArrayList;

import com.example.adapter.AllListAdapter.ViewHolder;
import com.example.memo.R;
import com.example.sqlLite.SqlLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter{
	ArrayList<History> hlist;
	LayoutInflater inflater;
	int id;
	Context context;
	
	public TaskListAdapter(ArrayList<History> hlist, Context context){
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
		ViewHolder holder;

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
			
			holder.star.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					id = position;
					v.setBackgroundResource(clickStar());
				}
			});
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
		sql="memo_data";
		db.update(sql, cv, "time=?",new String[]{m.getTime()}); 
		db.close();
	}
}
