package com.example.memo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.adapter.History;
import com.example.adapter.ViewPagerAdapter;
import com.example.service.LockService;
import com.example.service.SharedService;
import com.example.sqlLite.SqlLiteOpenHelper;
import com.slidingmenu.lib.SlidingMenu;
@SuppressLint({ "NewApi", "ResourceAsColor" })
@SuppressWarnings("deprecation")
public class AddMemoActivity extends Activity implements OnPageChangeListener{
	ArrayList<View> viewlist;
	LayoutInflater inflater;
	ViewPager viewpager;
	ViewPagerAdapter adapter;
	RadioGroup rg;
	RadioButton edit,accessory,alarm;
	RelativeLayout list,save,cancel;
	View line;
	LocalActivityManager manager;
	private Drawable drawable1,drawable2,drawable3;
	ImageButton text;
	ImageButton index,down;
	public static String tit,con;
	public static 	File record;
	public static LockService sservice;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private View parentView;
    Button exit,look,notuo,shelf,use;
    public static boolean Edit=false; 
    public static boolean Memo=false;
    public static String memo_title=null,memo_content=null,text_title=null,text_content=null;
	public static String get_title,get_content,get_time,get_ring,get_start,get_end,get_voice,get_total,get_name,get_start_time,get_end_time;
	public static int get_remind,get_notuo,get_shark,get_cilent;
	public static String get_pic;
	public static boolean show_pic;
	ArrayList<History> hlist;
	public static boolean clear_flag=false;
	 // 定义一个变量，来标识是否退出 
    public static boolean isExit = false;  
    public static boolean ring_name_flag=false;
    public static Handler mHandler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            isExit = false; 
        } 
    }; 
    

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		manager=new LocalActivityManager(this,true);
		manager.dispatchCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.activity_add_memo, null);
		setContentView(parentView);
		
		viewpager=(ViewPager)findViewById(R.id.home_page);
		edit=(RadioButton)findViewById(R.id.add_memo_edit);
		accessory=(RadioButton)findViewById(R.id.add_memo_accessory);
		alarm=(RadioButton)findViewById(R.id.add_memo_alarm);
		rg=(RadioGroup)findViewById(R.id.add_memo_nav_window);
		text=(ImageButton)findViewById(R.id.add_memo_text);
		index=(ImageButton)findViewById(R.id.add_memo_tuo);
		down=(ImageButton)findViewById(R.id.add_memo_down);
		initialize(); 
		sligingInit();
		if(Edit) showEdit();

		adapter=new ViewPagerAdapter(viewlist); 
		viewpager.setAdapter(adapter);//		tit=sservice.memo_title;
		viewpager.setOnPageChangeListener(this);

		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
				pop.showAsDropDown(v);
				pop.showAtLocation(parentView, Gravity.TOP,down.getRight(), down.getBottom()*5/3);
			}
		});
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					// 当RadioGroup中某一项被点击后执行
					// getChildCount()返回group中子选项的个数
					for (int i = 0; i < group.getChildCount(); i++) {
						if (group.getChildAt(i).getId() == checkedId) {
							// 选择当前viewpager显示的页面
							viewpager.setCurrentItem(i, true);
							setCristal(i);
						}
					}
	 
				}
			});
		
		text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tit=MemoAddOneActivity.title.getText().toString();
				con=MemoAddOneActivity.content.getText().toString();
				if(!tit.equals(""))  memo_title=tit;
				if(!con.equals(""))  memo_content=con;
				if(Edit) Edit=false;
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), AddTextActivity.class);
				startActivity(intent);
				finish();
			}
		});

    }

	 @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 Activity curActivity = manager.getActivity("accessory");  
	     if(curActivity != null && curActivity instanceof MemoAddTwoActivity){  
	     ((MemoAddTwoActivity)curActivity).handleActivityResult(requestCode, resultCode, data);   
	       }
	  }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_memo, menu);
        return true;
    }
    
    private void initialize(){
        viewlist=new ArrayList<View>();
        inflater=LayoutInflater.from(getApplicationContext());
		Intent in1 = new Intent(getApplicationContext(), MemoAddOneActivity.class);
		View v1 = getView("edit", in1);
		Intent in2 = new Intent(getApplicationContext(), MemoAddTwoActivity.class);
		View v2 = getView("accessory", in2);
		Intent in3 = new Intent(getApplicationContext(), MemoAddThreeActivity.class);
		View v3 = getView("alarm", in3);

		viewlist.add(v1);
		viewlist.add(v2);
		viewlist.add(v3);
		
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
			
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Edit){
						pop.dismiss();
						Edit=false;
						Bundle data=new Bundle();
						data.putString("creat_time",get_time);
						data.putString("title",get_title);
						data.putString("content",get_content);
						data.putString("voice", get_voice);
						data.putString("start_time", get_start_time);
						data.putString("end_time", get_end_time);
						data.putInt("shark", get_shark);
						data.putInt("client", get_cilent);
						data.putString("ring", get_ring);
						data.putInt("notuo", get_notuo);
						data.putInt("remind", get_remind);
						data.putString("ring_name",get_name);
						data.putSerializable("hlist",(Serializable)hlist);
						hlist=(ArrayList<History>)data.getSerializable("show_hlist");
						Intent intent=new Intent(getApplicationContext(), ShowMemoActivity.class);
						intent.putExtras(data);
						startActivity(intent);
						finish();
					}
				}
			});
			
			list.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop.dismiss();
					if(Edit) Edit=false;
					Intent intent=new Intent();
					intent.setClass(getApplicationContext(), ListMemoActivity.class);
					startActivity(intent);
					finish();
				}
			});
			save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop.dismiss();
					String creat_time = SharedService.format.format(System.currentTimeMillis());
					String title=MemoAddOneActivity.title.getText().toString();
					String content=MemoAddOneActivity.content.getText().toString();
					String record=MemoAddTwoActivity.record_ring_path;
					String picId=null;
					if(!MemoAddTwoActivity.mSelectPath.isEmpty() && MemoAddTwoActivity.mSelectPath.size()>0){
						picId=creat_time;
					}
					if(content.equals("")&&title.equals("")&&record==null&&picId==null){
						Toast.makeText(getApplicationContext(), "请输入内容",Toast.LENGTH_SHORT).show(); 
					}else{
						if(MemoAddThreeActivity.notuo==1){
							if(content.equals("")){
								Toast.makeText(getApplicationContext(), "no拖任务内容不许为空",Toast.LENGTH_SHORT).show(); 
							}else {
								SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
								SQLiteDatabase db=fdb.getReadableDatabase();
								String sql;
								sql="select * from memo_data";
								Cursor c=db.rawQuery(sql, null);
								ArrayList<History> recovery=new ArrayList<History>();
								if(c.getCount()!=0){
									for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
											String start=c.getString(c.getColumnIndex("start_time"));
											String end=c.getString(c.getColumnIndex("end_time"));
											History h=new History(start,end);
											recovery.add(h);
										};
								}
								db.close();
								String start_time=MemoAddThreeActivity.begin_time;
								String end_time=MemoAddThreeActivity.end_time;
								int i;
								boolean flag=false;
								if(!recovery.isEmpty()&&recovery.size()>0){
									for(i=0;i<recovery.size();i++){
										String start2=recovery.get(i).getStart_time();
										String end2=recovery.get(i).getEnd_time();
										if(compareTime(1,start_time,end2)){
											//save开始时间比rec结束时间大
											flag=false;
										}else if(compareTime(2,end_time,start2)){
											//save结束时间比rec开始时间小
											flag=false;
										}else {
											flag=true;
											break;
										}
									}
								}
								if(flag){
									Toast.makeText(getApplicationContext(), "no拖任务时间不能重叠",Toast.LENGTH_SHORT).show(); 
								}else saveMemo();
							}
							
						}else saveMemo();
						
					}
				}
			});
	 }
 
    
    public View getView(String id,Intent in){
		return manager.startActivity(id, in).getDecorView();		
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCristal(arg0);
		//adapter.notifyDataSetChanged();
	}
	
    public void setCristal(int i){
    	int j;
		for(j=0;j<3;j++){
			if(j==0){
				if(i==j){ 
					drawable1=this.getResources().getDrawable(R.drawable.edit_check); 
				}else{
					drawable1= this.getResources().getDrawable(R.drawable.edit);
				}
				edit.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable1,null,null);
			}else if(j==1){
				if(i==j){ 
					drawable2=this.getResources().getDrawable(R.drawable.accessory_check);
				}else{
					drawable2=this.getResources().getDrawable(R.drawable.accessory);
				}
				accessory.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable2,null,null);

			}else if(j==2){
				if(i==j){ 
					drawable3=this.getResources().getDrawable(R.drawable.alarm_clock_check);
				}else{
					drawable3=this.getResources().getDrawable(R.drawable.alarm_clock);
				}
				alarm.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable3,null,null);

			}
		}
    }
    
    void sligingInit() {
		// TODO Auto-generated method stub
        // configure the SlidingMenu  
        SlidingMenu menu = new SlidingMenu(this);  
        menu.setMode(SlidingMenu.LEFT);  
        // 设置触摸屏幕的模式  
        menu.setTouchModeAbove(SlidingMenu.LEFT);  
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
    
	   public static void exit(Context context,Activity a) { 
	        if (!isExit) { 
	            isExit = true; 
	            Toast.makeText(context, "再按一次退出程序", 
	                    Toast.LENGTH_SHORT).show(); 
//	            if(MemoAddThreeActivity.pop.isFocusable()) MemoAddThreeActivity.pop.dismiss();
//	            if(MemoAddThreeActivity.pop2.isFocusable()) MemoAddThreeActivity.pop2.dismiss();
	            // 利用handler延迟发送更改状态信息 
	            mHandler.sendEmptyMessageDelayed(0, 2000); 
	        } else {          
	        	Toast.makeText(context, "退出程序", 
	                    Toast.LENGTH_SHORT).show(); 
	            a.finish(); 
	        } 
	    } 

//
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		MemoAddThreeActivity.pop.dismiss();
    		MemoAddThreeActivity.pop2.dismiss();
    		MemoAddThreeActivity.pop3.dismiss();
    	}
    	return true;
    }

    void showEdit(){
		Intent in1=getIntent();
		Bundle data=in1.getExtras();
		get_time=data.getString("creat_time");
		hlist=(ArrayList<History>)data.getSerializable("show_hlist");
		get_title=data.getString("title");
		get_content=data.getString("content");
		get_time=data.getString("creat_time");
		get_ring=data.getString("ring_path");
		get_voice=data.getString("voice");
		get_start=data.getString("start_time");
		get_end=data.getString("end_time");
		get_notuo=data.getInt("notuo");
		get_shark=data.getInt("shark");
		get_remind=data.getInt("remind");
		get_cilent=data.getInt("cilent");
		get_name=data.getString("ring_name");
		get_pic=data.getString("pic");
		get_start_time=data.getString("start_time");
		get_end_time=data.getString("end_time");
		get_total=data.getString("timetotal");
		if(get_title!=null)
		MemoAddOneActivity.title.setText(get_title);
		if(get_content!=null)
		MemoAddOneActivity.content.setText(get_content);
		if(get_voice!=null){
			MemoAddTwoActivity.soundFile=new File(get_voice);
//			MemoAddTwoActivity.record_ring_path=get_voice;
			MemoAddTwoActivity.player_total.setText(get_total);
		}
		//图片显示
		if(get_pic!=null){
			MemoAddTwoActivity.listView.setAdapter(ShowMemoActivity.imgAdapter);
		}
		
		Drawable ope= this.getResources().getDrawable(R.drawable.alarm_open); 
		if(get_remind==1){
			MemoAddThreeActivity.sw1.setBackgroundDrawable(ope);
			MemoAddThreeActivity.btime.setText(get_start_time);
		if(get_notuo==1)
				MemoAddThreeActivity.sw2.setBackgroundDrawable(ope);
				MemoAddThreeActivity.etime.setText(get_end_time);
		if(get_shark==1)
				MemoAddThreeActivity.sw3.setBackgroundDrawable(ope);
		if(get_cilent==1)
				MemoAddThreeActivity.sw4.setBackgroundDrawable(ope);
		if(get_ring!=null)
//				MemoAddTwoActivity.record_ring_path=get_ring;
//				File ring=new File(MemoAddTwoActivity.record_ring_path);
				ring_name_flag=true;
				MemoAddThreeActivity.ring_name.setText(get_name);
		}
    }
    int getDurationByPosition(Cursor c, int position) {
	    c.moveToPosition(position);
	    int durationIndex = c.getColumnIndex(MediaStore.Audio.Media.DURATION);
	    int info = c.getInt(durationIndex) / 1000;
	    return info;
	}
    
    void saveMemo(){
    	String creat_time = SharedService.format.format(System.currentTimeMillis());
		String title=MemoAddOneActivity.title.getText().toString();
		String content=MemoAddOneActivity.content.getText().toString();
		String record=MemoAddTwoActivity.record_ring_path;
		String picId=null;
		if(!MemoAddTwoActivity.mSelectPath.isEmpty() && MemoAddTwoActivity.mSelectPath.size()>0){
			picId=creat_time;
		}
		SqlLiteOpenHelper fdb=new SqlLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db=fdb.getWritableDatabase();
		ContentValues cv=new ContentValues();
		if(title.equals("") )title="无标题";
		String ring_path=MemoAddThreeActivity.ring_path;
		String start_time=MemoAddThreeActivity.begin_time;
		String end_time=MemoAddThreeActivity.end_time;
		cv.put("creat_time",creat_time);
		cv.put("title",title);
		cv.put("content",content);
		cv.put("start_time",start_time);
		cv.put("end_time",end_time);
		cv.put("voice",MemoAddTwoActivity.record_ring_path);
		cv.put("picture",picId);
		cv.put("remind",MemoAddThreeActivity.remind);
		cv.put("notuo",MemoAddThreeActivity.notuo);
		cv.put("shark",MemoAddThreeActivity.shark);
		cv.put("cilent",MemoAddThreeActivity.cilent);
		cv.put("ring",ring_path);
		cv.put("star",0);
		Calendar c=Calendar.getInstance();
		int hour,min;
		hour=c.get(Calendar.HOUR_OF_DAY);
		min=c.get(Calendar.MINUTE);
		if(MemoAddThreeActivity.bhour>hour){
			Memo=true;
		}else{
			if(MemoAddThreeActivity.bhour==hour){
				if(MemoAddThreeActivity.bmin>min) Memo=true;
				else Memo=false;
			}else{
				Memo=false;
			}
		}
		//String creat_time=c.get(Calendar.YEAR)+"-"+c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		String[] pict={"pic1","pic2","pic3","pic4","pic5","pic6","pic7","pic8","pic9"};
		ContentValues pic=new ContentValues();
		for(int i=0;i<MemoAddTwoActivity.mSelectPath.size();i++){
			pic.put(pict[i],MemoAddTwoActivity.mSelectPath.get(i));
		};
		pic.put("p_Id",creat_time);
		db.insert("memo_data", null,cv);
		db.insert("pictures", null,pic);
		if(!Edit){//新建
			Toast.makeText(getApplicationContext(), "保存成功",Toast.LENGTH_SHORT).show();
			db.close();
			finish();  
		    Intent intent = new Intent(getApplicationContext(), AddMemoActivity.class);  
		    startActivity(intent); 	
		}else{//修改
			db.delete("memo_data","creat_time=?",new String[]{get_time});
			db.delete("pictures","p_Id=?",new String[]{get_time});
			Toast.makeText(getApplicationContext(), "修改成功",Toast.LENGTH_SHORT).show();
			db.close();
			Edit=false;
			Bundle data=new Bundle();
			data.putString("creat_time",creat_time);
			data.putString("title",MemoAddOneActivity.title.getText().toString());
			data.putString("content",MemoAddOneActivity.content.getText().toString());
	        Intent intent = new Intent(getApplicationContext(), ListMemoActivity.class);  
	        intent.putExtras(data);
	        startActivity(intent); 
	        finish();
		}
    }
    
   boolean compareTime(int type,String now,String rec){//-1小于，1大于
    	//now开始时间大于rec结束时间 或 now结束时间小于rec开始时间
    	//分割日期和时间
		String[] t1 = now.split(" ");
		String[] t2 = rec.split(" ");
		//分割日期
		String[] t1_y = t1[0].split("-");
		String[] t2_y = t2[0].split("-");
		//分割时间
		String[] t1_h = t1[1].split(":");
		String[] t2_h = t2[1].split(":");
		
		int now_y=Integer.parseInt(t1_y[0]);
		int now_mo=Integer.parseInt(t1_y[1]);
		int now_d=Integer.parseInt(t1_y[2]);
		int now_h=Integer.parseInt(t1_h[0]);
		int now_mi=Integer.parseInt(t1_h[1]);
		int rec_y=Integer.parseInt(t2_y[0]);
		int rec_mo=Integer.parseInt(t2_y[1]);
		int rec_d=Integer.parseInt(t2_y[2]);
		int rec_h=Integer.parseInt(t2_h[0]);
		int rec_mi=Integer.parseInt(t2_h[1]);
		
		if(type==1){//now开始时间大于rec结束时间
			if(now_y>rec_y) return true;
			else if(now_y==rec_y){
				if(now_mo>rec_mo) return true;
				else if(now_mo==rec_mo){
					if(now_d>rec_d) return true;
					else if(now_d==rec_d){
						if(now_h>rec_h) return true;
						else if(now_h==rec_h){
							if(now_mi>rec_mi) return true;
							else return false;
						}
						else return false;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}else{//now结束时间小于rec开始时间
			if(now_y<rec_y) return true;
			else if(now_y==rec_y) {
				if(now_mo<rec_mo) return true;
				else if(now_mo==rec_mo){
					if(now_d<rec_d) return true;
					else if(now_d==rec_d){
						if(now_h<rec_h) return true;
						else if(now_h==rec_h){
							if(now_mi<rec_mi) return true;
							else return false;
						}
						else return false;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		
    }
}
