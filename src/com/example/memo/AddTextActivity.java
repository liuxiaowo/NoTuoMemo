package com.example.memo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.adapter.History;
import com.example.service.LockService;
import com.example.service.SharedService;
import com.example.sqlLite.SqlLiteOpenHelper;
import com.slidingmenu.lib.SlidingMenu;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("InlinedApi")
public class AddTextActivity extends Activity {
	ImageButton memo;
	EditText title,content;
	String tit,con;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	ImageButton index,down;
	private View parentView;
	RelativeLayout list,save,cancel;
	public static LockService sservice;
	Button exit,look,notuo,shelf,use;
	public static boolean Edit=false;
	String edit_title,edit_content,get_time;
	View line;
	ArrayList<History> hlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_add_text,null);
		setContentView(parentView);
		memo=(ImageButton)findViewById(R.id.add_text_memo);
		title=(EditText)findViewById(R.id.add_text_title);
		content=(EditText)findViewById(R.id.add_text_content);
		index=(ImageButton)findViewById(R.id.add_text_tuo);
		down=(ImageButton)findViewById(R.id.add_text_down);
		
		init();
		sligingInit();
		
		if(Edit){
			Intent in1=getIntent();
			Bundle data=in1.getExtras();
			edit_title=data.getString("title");
			edit_content=data.getString("content");
			get_time=data.getString("creat_time");
			hlist=(ArrayList<History>)data.getSerializable("show_hlist");
			title.setText(edit_title);
			content.setText(edit_content);
		}else{
			tit=AddMemoActivity.text_title;
			con=AddMemoActivity.text_content;
			if(tit!=null) {
				title.setText(tit);
			}
			if(con!=null){
				content.setText(con);
			}
		}

		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
				pop.showAsDropDown(v);
				pop.showAtLocation(parentView, Gravity.TOP,down.getRight(), down.getBottom()*5/3);
			}
		});
		
		memo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tit=title.getText().toString();
				con=content.getText().toString();
				if(tit!=null)  AddMemoActivity.text_title=tit;
				if(con!=null)  AddMemoActivity.text_content=con;
				if(Edit) Edit=false;
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), AddMemoActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	void init(){
		View view = getLayoutInflater().inflate(R.layout.pop_add_downmenu, null);
		pop = new PopupWindow(view,200,200);
		ll_popup = (LinearLayout) view.findViewById(R.id.pop_add_menu);
		pop.setWidth(LayoutParams.WRAP_CONTENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		list = (RelativeLayout) view
				.findViewById(R.id.pop_add_list);
		save = (RelativeLayout) view
				.findViewById(R.id.pop_add_save);
		cancel=(RelativeLayout) view
				.findViewById(R.id.pop_add_back);

		
		if(!Edit){
			cancel.setVisibility(cancel.INVISIBLE);
		} 
		list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Edit) Edit=false;
				pop.dismiss();
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), ListTextActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Edit){
					pop.dismiss();
					Edit=false;
					Bundle data=new Bundle();
					data.putString("title",edit_title);
					data.putString("content",edit_content);
					data.putString("creat_time",get_time);
					data.putSerializable("hlist",(Serializable)hlist);
					Intent intent=new Intent(getApplicationContext(), ShowTextActivity.class);
					intent.putExtras(data);
					startActivity(intent);
					finish();
				}
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				
				if(title.getText().toString().equals("")&&content.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "标题和内容不能同时为空",Toast.LENGTH_SHORT).show(); 
				}else{
					SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
					SQLiteDatabase db=fdb.getWritableDatabase();
					Calendar c=Calendar.getInstance();
					//String creat_time=c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
					String creat_time = SharedService.format.format(System.currentTimeMillis());
					ContentValues cv=new ContentValues();
					cv.put("creat_time",creat_time);
					if(!title.getText().toString().equals(""))
					{
						cv.put("title",title.getText().toString());
					}else{
						cv.put("title","无标题");
					}
//					cv.put("title",title.getText().toString());
					cv.put("content",content.getText().toString());
					if(!Edit){
						cv.put("star",0);
						db.insert("text_data", null,cv);
						Toast.makeText(getApplicationContext(), "已保存",Toast.LENGTH_SHORT).show(); 
						db.close();
						finish();  
				        Intent intent = new Intent(getApplicationContext(), AddTextActivity.class);  
				        startActivity(intent); 
					}else{
						Edit=false;
						db.update("text_data",cv,"creat_time=?",new String[]{get_time});
						Toast.makeText(getApplicationContext(), "修改成功",Toast.LENGTH_SHORT).show();
						db.close();
						Bundle data=new Bundle();
						data.putString("creat_time",creat_time);
						data.putString("title",title.getText().toString());
						data.putString("content",content.getText().toString());
				        Intent intent = new Intent(getApplicationContext(), ListTextActivity.class);  
				        intent.putExtras(data);
				        startActivity(intent); 
				        finish();
					}
				}
			}
		});
	}
	
	void sligingInit() {
		// TODO Auto-generated method stub
        // configure the SlidingMenu  
        SlidingMenu menu = new SlidingMenu(this);  
        menu.setMode(SlidingMenu.LEFT);  
        // 设置触摸屏幕的模式  
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  
        menu.setShadowWidthRes(R.dimen.shadow_width);  
        menu.setShadowDrawable(R.drawable.empty);  
        // 设置滑动菜单视图的宽度  
        menu.setBehindOffsetRes(R.dimen.rest);  
        // 设置渐入渐出效果的值  
        menu.setFadeDegree(0.35f);  
        /** 
         * SLIDING_WINDOW will include the Title/ActionBar in the content 
         * section of the SlidingMenu, while SLIDING_CONTENT does not. 
         */  
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
        //为侧滑菜单设置布局  
        menu.setMenu(R.layout.sliding);  
    	
    	
    	  exit= (Button) menu.findViewById(R.id.index_exit);
    	  look=(Button) menu.findViewById(R.id.index_look);
    	  notuo=(Button) menu.findViewById(R.id.index_tasklist);
    	  shelf=(Button) menu.findViewById(R.id.index_collectshelf);
    	  use=(Button) menu.findViewById(R.id.index_instruction);
    	  exit.setOnClickListener(new DrawerListener());
    	  look.setOnClickListener(new DrawerListener());
    	  use.setOnClickListener(new DrawerListener());
    	  notuo.setOnClickListener(new DrawerListener());
    	  shelf.setOnClickListener(new DrawerListener());
	}

    public class DrawerListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	            //根据item点击行号判断启用指定Fragment 
	            switch (v.getId()){ 
	            	case R.id.index_exit:
	            		finish();
	            		break;
	                case R.id.index_look: 
	                	Edit=false;
	                	Intent intent1=new Intent();
	    				intent1.setClass(getApplicationContext(), ListMemoActivity.class);
	    				startActivity(intent1);
	                    break; 
	                case R.id.index_tasklist: 
	                	Edit=false;
	                	Intent intent2=new Intent();
	    				intent2.setClass(getApplicationContext(), TaskActivity.class);
	    				startActivity(intent2);
	                    break; 
	                case R.id.index_collectshelf:
	                	Edit=false;
	                	Intent intent3=new Intent();
	    				intent3.setClass(getApplicationContext(), CollectShelfActivity.class);
	    				startActivity(intent3);
	                    break; 
	                case R.id.index_instruction:
	                	Edit=false;
	                	Intent intent4=new Intent();
	    				intent4.setClass(getApplicationContext(), InstructionActivity.class);
	    				startActivity(intent4);
	            } 
		}
    	
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		
    	}
    	return true;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_text, menu);
		return true;
	}

}
