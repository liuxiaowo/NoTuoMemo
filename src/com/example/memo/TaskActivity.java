package com.example.memo;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.adapter.AllListAdapter;
import com.example.adapter.History;
import com.example.adapter.TaskListAdapter;
import com.example.sqlLite.SqlLiteOpenHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskActivity extends Activity {
	ImageButton back,down;
	AllListAdapter adapter;
	ListView lv;
	RelativeLayout add,starsearch,search,delete;
	private PopupWindow pop = null,pop2=null;
	private LinearLayout ll_popup;
	private RelativeLayout ll_popup2;
	private View parentView;
	ImageButton del;
	CheckBox checkall;
	String title,content,creat_time;
	TextView title_nav;
	public static ArrayList<History> hlist=new ArrayList<History>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_task, null);
		setContentView(parentView);
		
		back=(ImageButton)findViewById(R.id.task_back);
		down=(ImageButton)findViewById(R.id.task_down);
		
		init();
		
		adapter = new AllListAdapter(hlist, getApplicationContext());
		this.adapter.list_kind=2;
	
		lv = (ListView) findViewById(R.id.task_lv);
		lv.setAdapter(adapter);
		
		DownPop();
		DelPop();
		lv.setFocusable(true);
		lv.setFocusableInTouchMode(true);
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				adapter.check_all=true;
				for(int i=0;i<hlist.size();i++){
					if(i==arg2) hlist.get(i).setChecked(true);
					else hlist.get(i).setChecked(false);
				}
				
				adapter.notifyDataSetChanged();
				ll_popup2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
				pop2.showAtLocation(parentView, Gravity.BOTTOM,Gravity.CENTER_HORIZONTAL,pop2.getHeight()/2);
				return false;
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				creat_time=hlist.get(arg2).getTime();
				String title=hlist.get(arg2).getTitle();
				String content=hlist.get(arg2).getContent();
				String voice_path=hlist.get(arg2).getVoice();
				String start_time=hlist.get(arg2).getStart_time();
				String end_time=hlist.get(arg2).getEnd_time();
				int shark=hlist.get(arg2).getShark();
				int client=hlist.get(arg2).getClient();
				int notuo=hlist.get(arg2).getNotuo();
				int remind=hlist.get(arg2).getRemind();
				String  ring =hlist.get(arg2).getRing();
				Bundle data=new Bundle();
				data.putString("creat_time",creat_time);
				data.putString("title",title);
				data.putString("content",content);
				data.putString("voice", voice_path);
				data.putString("start_time", start_time);
				data.putString("end_time", end_time);
				data.putInt("shark", shark);
				data.putInt("client", client);
				data.putString("ring", ring);
				data.putInt("notuo", notuo);
				data.putInt("remind", remind);
				data.putSerializable("hlist",(Serializable)hlist);
				Intent intent=new Intent(getApplicationContext(), ShowMemoActivity.class);
				intent.putExtras(data);
				startActivity(intent);
				finish();
			}
		});
		

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
//				if(title_nav.getText().toString().equals("no拖noDie任务单")){
//					if(!adapter.check_all){
//						Intent intent=new Intent();
//						intent.setClass(getApplicationContext(), AddMemoActivity.class);
//						startActivity(intent);
//						finish();
//					}else{
//						adapter.check_all=false;
//						for(int i=0;i<hlist.size();i++){
//							hlist.get(i).setChecked(false);
//						}
//						pop2.dismiss();
//						adapter.notifyDataSetChanged();
//						checkall.setChecked(false);
//					}
//				}else{
//					if(!adapter.check_all){
//					title_nav.setText("no拖noDie任务单");
//					init();
//					adapter.notifyDataSetChanged();
//					}else{
//						adapter.check_all=false;
//						for(int i=0;i<hlist.size();i++){
//							hlist.get(i).setChecked(false);
//						}
//						pop2.dismiss();
//						adapter.notifyDataSetChanged();
//						checkall.setChecked(false);
//					}
//				}	
				if(!adapter.check_all){
					Intent intent=new Intent();
					intent.setClass(getApplicationContext(), AddMemoActivity.class);
					startActivity(intent);
					finish();
				}else{
					adapter.check_all=false;
					for(int i=0;i<hlist.size();i++){
						hlist.get(i).setChecked(false);
					}
					pop2.dismiss();
					adapter.notifyDataSetChanged();
					checkall.setChecked(false);
				}
			}
		});
		
		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.showAsDropDown(v);
				pop.showAtLocation(parentView, Gravity.TOP,down.getRight(), down.getBottom()*5/3);
			}
		});
	}
	
	void DownPop() {
		// TODO Auto-generated method stub
		 View view = getLayoutInflater().inflate(R.layout.pop_list_downmenu, null);
			pop = new PopupWindow(view,200,200);
			ll_popup = (LinearLayout) view.findViewById(R.id.pop_list_menu);
			pop.setWidth(LayoutParams.WRAP_CONTENT);
			pop.setHeight(LayoutParams.WRAP_CONTENT);
			pop.setBackgroundDrawable(new BitmapDrawable());
			pop.setFocusable(true);
			pop.setOutsideTouchable(false);
			pop.setContentView(view);
			add= (RelativeLayout) view
					.findViewById(R.id.pop_list_add);
			starsearch= (RelativeLayout) view
					.findViewById(R.id.pop_list_star);
			search=(RelativeLayout) view
					.findViewById(R.id.pop_list_search);
			delete=(RelativeLayout) view
					.findViewById(R.id.pop_list_delete);
			
			add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(getApplicationContext(), AddTextActivity.class);
					startActivity(intent);
					finish();
				}
			});
			starsearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!hlist.isEmpty()){
						pop.dismiss();
						title_nav.setText("标星任务");
						scanStarList();
						adapter.notifyDataSetChanged();
					}
				}
			});
			search.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop.dismiss();
				}
			});
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop.dismiss();
					adapter.check_all=true;
					adapter.notifyDataSetChanged();
					ll_popup2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
					pop2.showAtLocation(parentView, Gravity.BOTTOM,Gravity.CENTER_HORIZONTAL,pop2.getHeight()/2);
				}
			});
	}
		 
	 void DelPop(){
		 View view = getLayoutInflater().inflate(R.layout.pop_delete, null);
			pop2 = new PopupWindow(view,200,200);
			ll_popup2= (RelativeLayout) view.findViewById(R.id.pop_delete_win);
			pop2.setWidth(LayoutParams.MATCH_PARENT);
			pop2.setHeight(LayoutParams.WRAP_CONTENT);
			pop2.setBackgroundDrawable(new BitmapDrawable());
			pop2.setFocusable(false);
			pop2.setOutsideTouchable(false);
			pop2.setContentView(view);
			lv.setFocusable(true);
			del= (ImageButton) view
					.findViewById(R.id.pop_delete_img);
			checkall= (CheckBox) view
					.findViewById(R.id.pop_delete_check);
			
			del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop2.dismiss();
					adapter.check_all=false;
					showDel();
				}
			});
			checkall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(checkall.isChecked()){
						for(int i=0;i<hlist.size();i++){
							hlist.get(i).setChecked(true);
						}
					}else{
						for(int i=0;i<hlist.size();i++){
							hlist.get(i).setChecked(false);
						}
					}
					adapter.notifyDataSetChanged();
				}
			});
	 }
		 
		 

		void init(){
			SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
			SQLiteDatabase db=fdb.getReadableDatabase();
			String sql;
			sql="select * from memo_data";
			Cursor c=db.rawQuery(sql, null);
			hlist.clear();
			if(c.getCount()!=0){
				for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
					int isnotuo=c.getInt(c.getColumnIndex("notuo"));
					if(isnotuo==1){
						String tit=c.getString(c.getColumnIndex("title"));
						String con=c.getString(c.getColumnIndex("content"));
						String time=c.getString(c.getColumnIndex("creat_time"));
						String picture=c.getString(c.getColumnIndex("picture"));
						String voice_path=c.getString(c.getColumnIndex("voice"));
						String start_time=c.getString(c.getColumnIndex("start_time"));
						String end_time=c.getString(c.getColumnIndex("end_time"));
						int shark = c.getInt(c.getColumnIndex("shark"));
						int client = c.getInt(c.getColumnIndex("cilent"));
						int notuo= c.getInt(c.getColumnIndex("notuo"));
						int remind= c.getInt(c.getColumnIndex("remind"));
						String ring = c.getString(c.getColumnIndex("ring"));
						int isstar=c.getInt(c.getColumnIndex("star"));
						History h=new History(tit, con,time,picture,voice_path,start_time,end_time,shark,client,ring,notuo,remind);
						if(isstar==1)	h.setStar(true);
						else h.setStar(false);
						hlist.add(h);
					};
				}
			}
			db.close();
		}

		
		
		void scanStarList(){
			SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
			SQLiteDatabase db=fdb.getReadableDatabase();
			String sql;
			String tit,con,time;
			sql="select * from memo_data";
			Cursor c=db.rawQuery(sql, null);
			hlist.clear();
			if(c.getCount()!=0){
				for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
					int isstar=c.getInt(c.getColumnIndex("star"));
					int isnotuo=c.getInt(c.getColumnIndex("notuo"));
					if(isstar==1&&isnotuo==1){
						tit=c.getString(c.getColumnIndex("title"));
						con=c.getString(c.getColumnIndex("content"));
						time=c.getString(c.getColumnIndex("creat_time"));
						String picture=c.getString(c.getColumnIndex("picture"));
						String voice_path=c.getString(c.getColumnIndex("voice"));
						String start_time=c.getString(c.getColumnIndex("start_time"));
						String end_time=c.getString(c.getColumnIndex("end_time"));
						int shark = c.getInt(c.getColumnIndex("shark"));
						int client = c.getInt(c.getColumnIndex("cilent"));
						int notuo= c.getInt(c.getColumnIndex("notuo"));
						int remind= c.getInt(c.getColumnIndex("remind"));
						String ring = c.getString(c.getColumnIndex("ring"));
						History h=new History(tit, con,time,picture,voice_path,start_time,end_time,shark,client,ring,notuo,remind);
						h.setStar(true);
						hlist.add(h);
					};
				}
			}
			db.close();
		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			if(pop2.isShowing()){
				adapter.check_all=false;
				for(int i=0;i<hlist.size();i++){
					hlist.get(i).setChecked(false);
				}
				checkall.setChecked(false);
				pop2.dismiss();
				adapter.notifyDataSetChanged();
			}else{
				Intent in=new Intent(getApplicationContext(),AddMemoActivity.class);
				startActivity(in);
				finish();
			}
		}
		
		@SuppressWarnings("unused")
		private void showDel(){
		        //AlertDialog.Builder normalDialog=new AlertDialog.Builder(getApplicationContext());  
		        AlertDialog.Builder normalDia=new AlertDialog.Builder(TaskActivity.this);  
		        normalDia.setIcon(R.drawable.ic_launcher);  
		        normalDia.setTitle("删除极简");  
		        normalDia.setMessage("确定删除no拖任务吗？");  
		          
		        normalDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                // TODO Auto-generated method stub  
		                checkedDel();
		            }  
		        });  
		        normalDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                // TODO Auto-generated method stub  
		                 
		            }  
		        });  
		        normalDia.create().show();  
		    }
		
		
		protected void checkedDel() {
			// TODO Auto-generated method stub
			if(!hlist.isEmpty()){
				String dtime;
				SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
				SQLiteDatabase db=fdb.getReadableDatabase();
				for(int i=0;i<hlist.size();i++){
					if(hlist.get(i).isChecked()){
						dtime=hlist.get(i).getTime();
						db.delete("memo_data","creat_time=?",new String[]{dtime});
					}
				}
				db.close();
				init();
				adapter.notifyDataSetChanged();
			}
		}  
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}

}
