package com.example.memo;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.AllListAdapter;
import com.example.adapter.History;
import com.example.sqlLite.SqlLiteOpenHelper;

public class ListTextActivity extends Activity {
	ImageButton back,down,memo;
	AllListAdapter adapter;
	ListView lv;
	RelativeLayout add,starsearch,search,delete;
	private PopupWindow pop = null,pop2=null,pop3=null;
	private LinearLayout ll_popup,ll_pop3;
	private RelativeLayout ll_popup2;
	private View parentView;
	ImageButton del;
	CheckBox checkall;
	String title,content,creat_time;
	TextView title_nav;
	LinearLayout cut;
	EditText search_key;
	Button search_yes,search_no;
	String keyofSearch;
	public static ArrayList<History> hlist=new ArrayList<History>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_list_text, null);
		setContentView(parentView);
		
		back=(ImageButton)findViewById(R.id.list_text_back);
		down=(ImageButton)findViewById(R.id.list_text_down);
		memo=(ImageButton)findViewById(R.id.list_text_memo);
		title_nav=(TextView)findViewById(R.id.list_text_title);
		cut=(LinearLayout)findViewById(R.id.list_text_cut);
		
		init();

		adapter = new AllListAdapter(hlist, getApplicationContext());
		lv = (ListView) findViewById(R.id.list_text_lv);
		lv.setAdapter(adapter);
		this.adapter.list_kind=2;

		DownPop();
		DelPop();
		SearchPop();
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
					title=hlist.get(arg2).getTitle();
					content=hlist.get(arg2).getContent();
					Bundle data=new Bundle();
					data.putString("title",title);
					data.putString("content",content);
					data.putString("creat_time",creat_time);
					data.putSerializable("hlist",(Serializable)hlist);
					Intent intent=new Intent(getApplicationContext(), ShowTextActivity.class);
					intent.putExtras(data);
					startActivity(intent);
					finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cut.getVisibility()==cut.VISIBLE){
					if(!adapter.check_all){
						Intent intent=new Intent();
						intent.setClass(getApplicationContext(), AddTextActivity.class);
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
				}else{
					if(!adapter.check_all){
					title_nav.setVisibility(title_nav.INVISIBLE);
					cut.setVisibility(cut.VISIBLE);
					init();
					adapter.notifyDataSetChanged();
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
				
			}
		});
		
		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!pop2.isShowing()&&!pop3.isShowing()){
					pop.showAsDropDown(v);
					pop.showAtLocation(parentView, Gravity.TOP,down.getRight(), down.getBottom()*5/3);
				}
				}
				
		});
		memo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), ListMemoActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	void SearchPop() {
		// TODO Auto-generated method stub
		 View view = getLayoutInflater().inflate(R.layout.pop_search, null);
			pop3 = new PopupWindow(view,200,200);
			ll_pop3 = (LinearLayout) view.findViewById(R.id.pop_search_win);
			pop3.setWidth(LayoutParams.WRAP_CONTENT);
			pop3.setHeight(LayoutParams.WRAP_CONTENT);
			pop3.setBackgroundDrawable(new BitmapDrawable());
			pop3.setFocusable(true);
			pop3.setOutsideTouchable(false);
			pop3.setContentView(view);
			search_key= (EditText) view
					.findViewById(R.id.pop_search_key);
			search_yes= (Button) view
					.findViewById(R.id.pop_search_yes);
			search_no= (Button) view
					.findViewById(R.id.pop_search_no);
			search_key.setFocusable(true);
			search_yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!hlist.isEmpty()){
						keyofSearch=search_key.getText().toString();
						search_key.setText("");
						cut.setVisibility(cut.INVISIBLE);
						title_nav.setVisibility(title_nav.VISIBLE);
						searchList();
						pop3.dismiss();
					}else
						Toast.makeText(getApplicationContext(), "列表空",Toast.LENGTH_SHORT).show(); 
				}
			});
			search_no.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop3.dismiss();
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
					cut.setVisibility(cut.INVISIBLE);
					title_nav.setVisibility(title_nav.VISIBLE);
					scanStarList();
					adapter.notifyDataSetChanged();
					}else
						Toast.makeText(getApplicationContext(), "列表空",Toast.LENGTH_SHORT).show(); 

				}
			});
			search.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop.dismiss();
					pop3.showAsDropDown(v);
					pop3.showAtLocation(parentView, Gravity.CENTER_VERTICAL,Gravity.RIGHT/2,Gravity.TOP/2);
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

	public void init(){
		SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db=fdb.getReadableDatabase();
		String sql;
		sql="select * from text_data";
		Cursor c=db.rawQuery(sql, null);
		hlist.clear();
		if(c.getCount()!=0){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				String tit=c.getString(c.getColumnIndex("title"));
				String con=c.getString(c.getColumnIndex("content"));
				String time=c.getString(c.getColumnIndex("creat_time"));
				int isstar=c.getInt(c.getColumnIndex("star"));
				History h=new History(tit, con,time);
				if(isstar==1)	h.setStar(true);
				else h.setStar(false);
				hlist.add(h);
			}
		}
		db.close();
	}
	
	public static void delHistory(Context context,String time){
		SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(context);
		SQLiteDatabase db=fdb.getReadableDatabase();
		db.delete("text_data","creat_time=?",new String[]{time});
		db.close();
	}
	
	void scanStarList(){
		SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db=fdb.getReadableDatabase();
		String sql;
		String tit,con,time;
		sql="select * from text_data";
		Cursor c=db.rawQuery(sql, null);
		hlist.clear();
		if(c.getCount()!=0){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				int isstar=c.getInt(c.getColumnIndex("star"));
				if(isstar==1){
					tit=c.getString(c.getColumnIndex("title"));
					con=c.getString(c.getColumnIndex("content"));
					time=c.getString(c.getColumnIndex("creat_time"));
					History h=new History(tit,con,time);
					h.setStar(true);
					hlist.add(h);
				};
			}
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_text, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		adapter.check_all=false;
		for(int i=0;i<hlist.size();i++){
			hlist.get(i).setChecked(false);
		}
		checkall.setChecked(false);
		pop2.dismiss();
		adapter.notifyDataSetChanged();

	}
	 @SuppressWarnings("unused")
		private void showDel(){
		        //AlertDialog.Builder normalDialog=new AlertDialog.Builder(getApplicationContext());  
		        AlertDialog.Builder normalDia=new AlertDialog.Builder(ListTextActivity.this);  
		        normalDia.setIcon(R.drawable.ic_launcher);  
		        normalDia.setTitle("删除极简");  
		        normalDia.setMessage("确定删除极简文本记录吗？");  
		          
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
					db.delete("text_data","creat_time=?",new String[]{dtime});
				}
			}
			db.close();
			if(hlist.isEmpty()&&cut.getVisibility()==cut.INVISIBLE){
				title_nav.setVisibility(title_nav.INVISIBLE);
				cut.setVisibility(cut.VISIBLE);
			}
			init();
			adapter.notifyDataSetChanged();
		}
	}  
	
	void searchList(){
			SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
			SQLiteDatabase db=fdb.getReadableDatabase();	
			String sql1,sql2;
			sql1="select * from text_data where content like ? ";
			Cursor c=db.rawQuery(sql1, new String[]{keyofSearch});
			hlist.clear();
			if(c.getCount()!=0){
				for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
					String tit=c.getString(c.getColumnIndex("title"));
					String con=c.getString(c.getColumnIndex("content"));
					String time=c.getString(c.getColumnIndex("creat_time"));
					int isstar=c.getInt(c.getColumnIndex("star"));
					History h=new History(tit, con,time);
					if(isstar==1)	h.setStar(true);
					else h.setStar(false);
					hlist.add(h);
				}
			}
			sql2="select * from text_data where title like ? ";
			Cursor d=db.rawQuery(sql2, new String[]{keyofSearch});
			int j;
			if(d.getCount()!=0){
				for(d.moveToFirst();!d.isAfterLast();d.moveToNext()){
					String time=d.getString(d.getColumnIndex("creat_time"));
					for( j=0;j<hlist.size();j++){
						if(hlist.get(j).getTime().equals(time)) break;
					}
					if(j==hlist.size()){
						String tit=d.getString(d.getColumnIndex("title"));
						String con=d.getString(d.getColumnIndex("content"));
						int isstar=d.getInt(d.getColumnIndex("star"));
						History h=new History(tit, con,time);
						if(isstar==1)	h.setStar(true);
						else h.setStar(false);
						hlist.add(h);
					}
				}
			}
			db.close();
			title_nav.setText("查询结果");
			adapter.notifyDataSetChanged();
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent intent1=new Intent();
				intent1.setClass(getApplicationContext(), AddTextActivity.class);
				startActivity(intent1);
				finish();
    	}
    	return true;
    }
}
