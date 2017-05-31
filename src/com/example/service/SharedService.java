package com.example.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.example.memo.LockActivity;
import com.example.sqlLite.SqlLiteOpenHelper;


public class SharedService extends Service{
	private String TAG = "SharedService";
	SqlLiteOpenHelper timeOpenHelper;
	public  static Date start_date = null;
	public  static Date end_date=null;
	public  static long dist;
	public  static String create_time;
	public  static String title;
	public  static String content;
	public  static String voice;
	public  static int picture;
	public  static int shark,remind;
	public  static int client,notuo;
	public  static String ring;
	public  boolean running =false;
	public static CharSequence contentText;
	public static CustomDigitalClock timeClock;
	Vibrator  vibrator;
	public static int flag=0;
	public static SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static MediaPlayer player = new MediaPlayer();
	@Override
	public IBinder onBind(Intent arg0){
		return null;
	}

	public void onCreate(){
			 running = true;
			 new Thread(){
				public void run(){
					while(running){					
						try {
							   flag++;
							   AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE); 
							   int statusFlag = (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) ? 1: 0; 
							   Calendar c =Calendar.getInstance();
							   Calendar sc = Calendar.getInstance();
							   timeOpenHelper=new SqlLiteOpenHelper(getApplicationContext());
							   SQLiteDatabase db = timeOpenHelper.getReadableDatabase();
								//查询获得游标  
								 Cursor c1 = db.query   ("memo_data",null,null,null,null,null,null);  
								 while(c1.moveToNext()){
									 create_time=c1.getString(c1.getColumnIndex("creat_time"));
									 title = c1.getString(c1.getColumnIndex("title"));
									 content = c1.getString(c1.getColumnIndex("content")); 
									 String start_time=c1.getString(c1.getColumnIndex("start_time"));
									 String end_time=c1.getString(c1.getColumnIndex("end_time"));
									 voice = c1.getString(c1.getColumnIndex("voice"));
									 remind=c1.getInt(c1.getColumnIndex("remind"));
									 picture=c1.getInt(c1.getColumnIndex("picture"));
									 shark = c1.getInt(c1.getColumnIndex("shark"));
									 client = c1.getInt(c1.getColumnIndex("cilent"));
									 notuo= c1.getInt(c1.getColumnIndex("notuo"));
									 ring = c1.getString(c1.getColumnIndex("ring"));
							  	try {
							  	    if(start_time!=null){
							  		start_date = (Date) format.parse(start_time);
							  		sc.setTime(start_date);
							  	    }
							  	    if(end_time!=null){
							  		end_date = (Date) format.parse(end_time);
							  		dist=end_date.getTime()-start_date.getTime();
							  		}
							  	if(sc.get(Calendar.YEAR)==c.get(Calendar.YEAR)
										   &&(sc.get(Calendar.MONTH)+1)==(c.get(Calendar.MONTH)+1)
										   &&sc.get(Calendar.DAY_OF_MONTH)==c.get(Calendar.DAY_OF_MONTH)
										   &&sc.get(Calendar.HOUR_OF_DAY)==c.get(Calendar.HOUR_OF_DAY)
										   &&sc.get(Calendar.MINUTE)==c.get(Calendar.MINUTE)
										   &&sc.get(Calendar.SECOND)==c.get(Calendar.SECOND)){
							  		if(shark==1){
							  			vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
							  	        long [] pattern = {100,400,100,400};  
							  	        vibrator.vibrate(pattern,-1); 
							  		}
							  		//client
							  		if(statusFlag == 1){
							  			if(client==1){
							  				try {
							  					player.reset();
							  					if(ring!=null){
												player.setDataSource(ring);
												try {
													player.setOnPreparedListener(preparedListener);
													player.prepareAsync();
												} catch (IllegalStateException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
//												player.prepare();
//								  				player.start();
								  				Timer timer=new Timer();
								        		TimerTask task=new TimerTask() {
								        			@Override
								        			public void run() {
								        				player.stop();
								        				player.release();
								        			}
								        		};
								        		timer.schedule(task,10000);	
							  					}
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (SecurityException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IllegalStateException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} 	
							  			}
							  		}
						  				try {
						  					player.reset();
						  					if(ring!=null){
											player.setDataSource(ring);
											try {
												player.setOnPreparedListener(preparedListener);
												player.prepareAsync();
											} catch (IllegalStateException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											Timer timer=new Timer();
							        		TimerTask task=new TimerTask() {
							        			@Override
							        			public void run() {
							        				player.stop();
							        				player.release();
							        			}
							        		};
							        		timer.schedule(task,10000);
						  					}
										} catch (IllegalArgumentException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (SecurityException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IllegalStateException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 	
							  		if(notuo==1){
										   Intent intent = new Intent(); 
										   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										   intent.setClass(SharedService.this, LockActivity.class);                                   
										   startActivity(intent);
							  		}
  								   }
							  	} catch (ParseException e) {
							  		e.printStackTrace();
							  	}
							  	catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalStateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								 }
								c1.close();
								db.close();
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			 }.start();
	}
	OnPreparedListener preparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			player.start();
		}
	};
	public void onDestroy() {  
        Log.i(TAG, "----------------- onDestroy------");  
        super.onDestroy();  
        // 在此重新启动,使服务常驻内存  
        startService(new Intent(this, LockService.class));  
        running= false;  
        vibrator.cancel();
    }  

}
