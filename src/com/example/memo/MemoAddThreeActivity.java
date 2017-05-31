package com.example.memo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.example.adapter.Music;
import com.example.adapter.MusicListAdapter;
import com.example.service.SharedService;

@SuppressLint({ "NewApi", "ResourceAsColor" })
public class MemoAddThreeActivity extends Activity {
	public static ImageButton sw1,sw2,sw3,sw4,music_add,music_back;
	Drawable ope,clo;
	public static PopupWindow pop = null,pop2=null,pop3=null;
	private LinearLayout ll_popup,ll_popup2;
	RelativeLayout ll_pop_sys;
	public static String begin_time,end_time,ring_path=null;
	public static int byear=0,bmonth,bdate,bhour,bmin;
	int eyear=0,emonth,edate,ehour,emin;
	public static int remind=0,notuo=0,shark=0,cilent=0;
	Button yes_pop1,yes_pop2,music_yes,music_no;
	RelativeLayout chose_ring;
	public static TextView btime,etime,ring_name,music_title;
	DatePicker dpicker1,dpicker2;
	TimePicker tpicker1,tpicker2;
	public static int position=-1;
	private View parentView;
	Calendar c;
	public  static MediaPlayer player=new MediaPlayer();
	public static ArrayList<Music> mlist=new ArrayList<Music>();
	ListView lv_music;
	MusicListAdapter adapter;
	public static int play_count=0;
	boolean flag=false;
	String nameOfRing="无";
	Runnable r = new Runnable() {
		@Override
			public void run() {
			// TODO Auto-generated method stub
				while (true) {
					if (player.isPlaying()) {
						if(play_count<10)
							play_count++;
						else player.stop();
					}else
						play_count=0;
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}// while
			}
		};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        parentView = getLayoutInflater().inflate(R.layout.activity_memo_add_three, null);
		setContentView(parentView);
		new Thread(r).start();
		sw1=(ImageButton)findViewById(R.id.alarm_remind_switch);
		sw2=(ImageButton)findViewById(R.id.alarm_notuo_switch);
		sw3=(ImageButton)findViewById(R.id.alarm_shark_switch);
		sw4=(ImageButton)findViewById(R.id.alarm_cilent_switch);
		chose_ring=(RelativeLayout)findViewById(R.id.alarm_ring);
		btime=(TextView)findViewById(R.id.alarm_remind_time);
		etime=(TextView)findViewById(R.id.alarm_notuo_time);
		ring_name=(TextView)findViewById(R.id.alarm_ring_chose);
		if(AddMemoActivity.ring_name_flag){
		ring_name.setText(AddMemoActivity.get_name);
		}
		ope= this.getResources().getDrawable(R.drawable.alarm_open); 
		clo=this.getResources().getDrawable(R.drawable.alarm_close); 
		
		beginPop();
		endPop();
		SystemMusic();
		musicPop();
		
		chose_ring.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(remind==1){
					mlist.clear();
					SystemMusic();
					ll_pop_sys.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
					pop3.showAtLocation(parentView, Gravity.BOTTOM,Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
				}
			
			}
		});
		
		sw1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setBackgroundDrawable(changeSwitch(v.getBackground()));
				if(v.getBackground()==clo){
					chose_ring.setClickable(false);
					remind=0;
					notuo=0;
					shark=0;
					cilent=0;
					btime.setTextColor(R.color.gray);
					etime.setTextColor(R.color.gray);
					sw2.setBackgroundDrawable(clo);
					sw3.setBackgroundDrawable(clo);
					sw4.setBackgroundDrawable(clo);
				}else{
					remind=1;
					chose_ring.setClickable(true);
					ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM,Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
				}
			}
		});
		
		sw2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sw1.getBackground()==ope){
					v.setBackgroundDrawable(changeSwitch(v.getBackground()));
					if(v.getBackground()==ope){
						notuo=1;
						ll_popup2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_translate_in));
						pop2.showAtLocation(parentView, Gravity.BOTTOM,Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
					}else{
						notuo=0;
						etime.setTextColor(R.color.gray);
					}
				}
			}
		});
		
		sw3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sw1.getBackground()==ope){
					v.setBackgroundDrawable(changeSwitch(v.getBackground()));
					if(v.getBackground()==ope){
						shark=1;
					}else shark=0;
				}			
			}
		});
		
		sw4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sw1.getBackground()==ope){
					v.setBackgroundDrawable(changeSwitch(v.getBackground()));
					if(v.getBackground()==ope){
						cilent=1;
					}else cilent=0;
				}			
			}
		});
	}

	public Drawable changeSwitch(Drawable d){
		if(d==ope) 	return clo;
		return ope;
		
	}
	
	private void beginPop() {
	// TODO Auto-generated method stub
		 View view = getLayoutInflater().inflate(R.layout.pop_begin_time, null);
			pop = new PopupWindow(view,view.getWidth(),view.getHeight());
			ll_popup = (LinearLayout) view.findViewById(R.id.pop_begintime);
			pop.setWidth(LayoutParams.WRAP_CONTENT);
			pop.setHeight(LayoutParams.WRAP_CONTENT);
			pop.setBackgroundDrawable(new BitmapDrawable());
			pop.setFocusable(false);
			pop.setOutsideTouchable(false);
			pop.setContentView(view);
			yes_pop1= (Button) view
					.findViewById(R.id.pop_begintime_yes);
			dpicker1=(DatePicker) view.findViewById(R.id.pop_begintime_date);
			tpicker1=(TimePicker) view.findViewById(R.id.pop_begintime_time);
			
			if(byear==0){
				c=Calendar.getInstance();
				byear=c.get(Calendar.YEAR);
				bmonth=c.get(Calendar.MONTH);
				bdate=c.get(Calendar.DAY_OF_MONTH);
				bhour=c.get(Calendar.HOUR_OF_DAY);
				bmin=c.get(Calendar.MINUTE);
				//start_time_min= SharedService.format.format(c.getTime());
			}
			
			
			dpicker1.init(byear, bmonth, bdate, new OnDateChangedListener() {
				
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					byear=year;
					bmonth=monthOfYear;
					bdate=dayOfMonth;
				}
			});
			tpicker1.setOnTimeChangedListener(new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					bhour=hourOfDay;
					bmin=minute;
				}
			});
			yes_pop1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(getYes1()){
						pop.dismiss();
						Calendar dateAndTime1 = Calendar.getInstance(Locale.CHINA);
						 dateAndTime1.set(Calendar.YEAR, byear);
			             dateAndTime1.set(Calendar.MONTH, bmonth);
			             dateAndTime1.set(Calendar.DAY_OF_MONTH, bdate);
			             dateAndTime1.set(Calendar.HOUR_OF_DAY, bhour);
			             dateAndTime1.set(Calendar.MINUTE, bmin);
						begin_time=SharedService.format.format(dateAndTime1.getTime());
						//begin_time=byear+"-"+bmonth+"-"+bdate+" "+bhour+":"+bmin+":"+"00";
						btime.setText(begin_time);
						btime.setTextColor(R.color.black);
					}else
						Toast.makeText(getApplicationContext(), "提醒时间必须不小于当前时间",Toast.LENGTH_SHORT).show();
						
				}
			});
	}
		
	private void endPop() {
	// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(R.layout.pop_end_time, null);
		pop2 = new PopupWindow(view,view.getWidth(),view.getHeight());
		ll_popup2 = (LinearLayout) view.findViewById(R.id.pop_endtime);
		pop2.setWidth(LayoutParams.WRAP_CONTENT);
		pop2.setHeight(LayoutParams.WRAP_CONTENT);
		pop2.setBackgroundDrawable(new BitmapDrawable());
		pop2.setFocusable(false);
		pop2.setOutsideTouchable(false);
		pop2.setContentView(view);
		yes_pop2= (Button) view
				.findViewById(R.id.pop_endtime_yes);
		dpicker2=(DatePicker) view.findViewById(R.id.pop_endtime_date);
		tpicker2=(TimePicker) view.findViewById(R.id.pop_endtime_time);
		
		if(eyear==0){
			c=Calendar.getInstance();
			eyear=c.get(Calendar.YEAR);
			emonth=c.get(Calendar.MONTH);
			edate=c.get(Calendar.DAY_OF_MONTH);
			ehour=c.get(Calendar.HOUR_OF_DAY);
			emin=c.get(Calendar.MINUTE);
//			end_time_min= SharedService.format.format(c.getTime());
		}
		
		dpicker2.init(eyear, emonth, edate, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				eyear=year;
				emonth=monthOfYear;
				edate=dayOfMonth;
			}
		});
		tpicker2.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				ehour=hourOfDay;
				emin=minute;
			}
		});
		yes_pop2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getYes2()){
					pop2.dismiss();
					//c=Calendar.getInstance();
					Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
					 dateAndTime.set(Calendar.YEAR, eyear);
		             dateAndTime.set(Calendar.MONTH, emonth);
		             dateAndTime.set(Calendar.DAY_OF_MONTH, edate);
		             dateAndTime.set(Calendar.HOUR_OF_DAY, ehour);
		             dateAndTime.set(Calendar.MINUTE, emin);
					end_time=SharedService.format.format(dateAndTime.getTime());
					//end_time=eyear+"-"+emonth+"-"+edate+" "+ehour+":"+emin+":"+"00";
					etime.setText(end_time);
					etime.setTextColor(R.color.black);
				}else
					Toast.makeText(getApplicationContext(), "结束时间必须大于开始时间",Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	return true;
    	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo_add_three, menu);
		return true;
	} 
	

	public void SystemMusic() {
		// TODO Auto-generated method stub
		String name,path;
		flag=false;
		//取得需要遍历的文件目录
		//File home = new File("sdcard/media/audio/ringtones");
		File home = new File("/system/media/audio/alarms");
		//Uri uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		//遍历文件目录
		Music m1=new Music("无",null);
		mlist.add(m1);
		if (home.listFiles(new MusicFilter()).length > 0) {
			for (File file : home.listFiles(new MusicFilter())) {
				name=file.getName();
				path=file.getPath();
				Music m=new Music(name, path);
				if(path.equals(ring_path)){
					m.setChecked(true);
					flag=true;
				} 
				mlist.add(m);
			}
		}
		if(!flag&&ring_path!=null){
			File ring=new File(ring_path);
			nameOfRing=ring.getName();
			Music m2 = new Music(nameOfRing,ring_path);
			m2.setChecked(true);
			mlist.add(m2);
		}else  if(!flag||ring_path==null)	mlist.get(0).setChecked(true);
		
	}
	
	public static void playSongs(String path) {
		// TODO Auto-generated method stubs
		player.reset();
		try {
			player.setDataSource(path);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	 void musicPop() {
		// TODO Auto-generated method stub
		 View view = getLayoutInflater().inflate(R.layout.pop_system_music, null);
			pop3 = new PopupWindow(view,view.getWidth(),view.getHeight());
			ll_pop_sys = (RelativeLayout) view.findViewById(R.id.pop_sys_music);
			pop3.setWidth(LayoutParams.MATCH_PARENT);
			pop3.setHeight(LayoutParams.MATCH_PARENT);
			pop3.setBackgroundDrawable(new BitmapDrawable());
			pop3.setFocusable(true);
			pop3.setOutsideTouchable(false);
			pop3.setContentView(view);
			adapter = new MusicListAdapter(mlist,MemoAddThreeActivity.this);
			lv_music = (ListView) view.findViewById(R.id.system_music_lv);
			lv_music.setItemsCanFocus(false);
			music_yes=(Button)view.findViewById(R.id.music_yes);
			music_no=(Button)view.findViewById(R.id.music_no);
			music_add=(ImageButton)view.findViewById(R.id.system_music_add);
			music_back=(ImageButton)view.findViewById(R.id.system_music_back);
			music_title=(TextView)view.findViewById(R.id.system_music_title);
			lv_music.setAdapter(adapter);
			lv_music.setFocusable(true);
			lv_music.clearChoices();
			music_no.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(player.isPlaying()) player.stop();
					position=-1;
					pop3.dismiss();
				}
			});
			
			music_yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(position!=-1){
						if(!mlist.get(position).getPath().equals(ring_path)){
							nameOfRing=mlist.get(position).getName();
							ring_name.setText(nameOfRing);
							ring_path=mlist.get(position).getPath();
						}
					}
					if(player.isPlaying()) player.stop();
					pop3.dismiss();
				}
			});
			
			music_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(player.isPlaying()) player.stop();
					music_title.setText("本地音乐");
					music_back.setVisibility(View.VISIBLE);
					mlist.clear();
					LocalMusic();
					adapter.notifyDataSetChanged();
				}
			});
			
			music_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					music_title.setText("系统音乐");
					music_back.setVisibility(View.INVISIBLE);
					mlist.clear();
					if(player.isPlaying()) player.stop();
					SystemMusic();
					adapter.notifyDataSetChanged();
				}
			});
			
			lv_music.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					position=arg2;
					for(int i=0;i<mlist.size();i++){
						if(i==arg2) mlist.get(i).setChecked(true);
						else mlist.get(i).setChecked(false);
					}
					adapter.notifyDataSetChanged();
					if(mlist.get(arg2).getPath()!=null){
						playSongs(mlist.get(arg2).getPath());
					}
					
				}
			});
	}

	void LocalMusic() {
		// TODO Auto-generated method stub
		String name,path;
		ContentResolver cr = getContentResolver();
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] s = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA };
		Cursor c = cr.query(uri, s, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			path = c.getString(c
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			name = c.getString(c
						.getColumnIndex(MediaStore.Audio.Media.TITLE));
			Music m = new Music(name,path);
			if(path.equals(ring_path)){
				m.setChecked(true);
			}
			mlist.add(m);
		}
	}
	
	//过滤所有不是以.mp3结尾的文件
	class MusicFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3"));
		}
	}
	
	boolean getYes1(){
		if(byear>c.get(Calendar.YEAR)) return true;
		else if(byear==c.get(Calendar.YEAR)){
			if(bmonth>c.get(Calendar.MONTH)) return true;
			else if(bmonth==c.get(Calendar.MONTH)){
				if(bdate>c.get(Calendar.DAY_OF_MONTH)) return true;
				else if(bdate==c.get(Calendar.DAY_OF_MONTH)){
					if(bhour>c.get(Calendar.HOUR_OF_DAY)) return true;
					else if(bhour==c.get(Calendar.HOUR_OF_DAY)) {
						if(bmin>c.get(Calendar.MINUTE))  return true;
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
	
	boolean getYes2(){
		if(eyear>byear) return true;
		else if(eyear==byear){
			if(emonth>bmonth) return true;
			else if(emonth==bmonth){
				if(edate>bdate) return true;
				else if(edate==bdate){
					if(ehour>bhour) return true;
					else if(ehour==bhour) {
						if(emin>bmin)  return true;
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
