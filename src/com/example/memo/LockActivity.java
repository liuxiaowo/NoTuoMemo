package com.example.memo;



import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.service.CustomDigitalClock;
import com.example.service.LockService;
import com.example.service.SharedService;




public class LockActivity extends Activity{
public static int flag=1;
LockService mmservice;
View mLockView;
private int intLevel;
private int intScale;
public static  CustomDigitalClock timeClock; 
public TextView task_name;
public TextView task_content;
private String TAG = "LockActivity";
public static int text=1;
public static long dist_real;
public static NotificationManager mNotificationManager;
public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; 
@SuppressLint("SimpleDateFormat")
public static SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
ServiceConnection svc =new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			mmservice=((LockService.newBinder) service).getService();
			Runnable r = new Runnable() {
				public void run() {
					 while(!Thread.currentThread().isInterrupted()){           
						   try {                
							   doWork();
							   Thread.sleep(1000);    
							   } catch (InterruptedException e) {    
								   Thread.currentThread().interrupt(); 
							   }catch(Exception e){            
									   
								}       
						   }   
				}
			};
			new Thread(r).start();
		}
};
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        /*all screen show*/
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//					   WindowManager.LayoutParams.FLAG_FULLSCREEN);
			 this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
			setContentView(R.layout.activity_lock);
			
	        ImageButton tel=(ImageButton)findViewById(R.id.phone);
	        ImageButton quit=(ImageButton)findViewById(R.id.quit);
	        timeClock=(CustomDigitalClock) findViewById(R.id.time);
	        task_name = (TextView) findViewById(R.id.task_name);
	        task_content= (TextView) findViewById(R.id.task_content);
	        //bindService
	        Intent intent = new Intent(getApplicationContext(), LockService.class);
			bindService(intent, svc, Context.BIND_AUTO_CREATE);
			//battery
			registerReceiver(mBatInfoReceiver, new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED));
			//shut down
			registerReceiver(mShutdownInfoReceiver, new IntentFilter(
					Intent.ACTION_SHUTDOWN));
	        tel.setOnClickListener(new TelonClickListener());
	        quit.setOnClickListener(new QuitonClickListener());
	        if(SharedService.notuo==1){
	        dist_real = SharedService.end_date.getTime()-System.currentTimeMillis();
	        }
	        if(text==1){
	        timeClock.setEndTime(System.currentTimeMillis()+SharedService.dist);
	        text++;
	        }else{
	        timeClock.setEndTime(System.currentTimeMillis()+dist_real);
	        text++;
	        }
	        task_name.setText(SharedService.title);
	        task_content.setText(SharedService.content);
	    }
	    /*
	     Tel
	      */
	    public final class TelonClickListener implements View.OnClickListener{
	    	public void onClick(View v){
	    		 Intent intent = new Intent();
	    		    intent.setAction("android.intent.action.DIAL");
	    		    startActivity(intent);
	    		    
	    	}
	    }
	    /* 捕捉到ACTION_BATTERY_CHANGED时要运行的method */
		public void onBatteryInfoReceiver(int intLevel, int intScale) {
			int battery=intLevel * 100 / intScale;
			if((battery<15&&battery>=10)||(battery<10&&battery>=5)||(battery<5)){
				// 创建退出对话框  
				AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            // 设置对话框标题  
	            isExit.setTitle("电量提示");  
	            // 设置对话框消息  
	            isExit.setMessage("电量过低，要退出吗？(退出即任务失败)");  
	            // 添加选择按钮并注册监听  
	            isExit.setButton("确定", listener);  
	            isExit.setButton2("取消", listener);  
	            // 显示对话框  
	            isExit.show(); 
			}
		}
		/**监听对话框里面的button点击事件*/  
	    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	                finish();  
	                Toast.makeText(getApplicationContext(), "任务失败",
							Toast.LENGTH_SHORT).show();
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	    /*  battery*/
		private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
					intLevel = intent.getIntExtra("level", 0);
					intScale = intent.getIntExtra("scale", 100);
					onBatteryInfoReceiver(intLevel, intScale);
				}
			}
		};
		 /* shut down */
		private BroadcastReceiver mShutdownInfoReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (Intent.ACTION_SHUTDOWN.equals(action)) {
					finish();
				}
			}
		};
		 /*
		    * clock
		    */
		   public void doWork() {   
			   runOnUiThread(new Runnable() {       
				   @SuppressWarnings("deprecation")
				public void run() {            
					   try{ 
						   Calendar c =Calendar.getInstance();
						   Calendar cal = Calendar.getInstance();
						   cal.setTime(SharedService.end_date);
						   if((cal.get(Calendar.YEAR)==c.get(Calendar.YEAR)
								   &&(cal.get(Calendar.MONTH)+1)==(c.get(Calendar.MONTH)+1)
								   &&cal.get(Calendar.DAY_OF_MONTH)==c.get(Calendar.DAY_OF_MONTH)
								   &&cal.get(Calendar.HOUR_OF_DAY)==c.get(Calendar.HOUR_OF_DAY)
								   &&cal.get(Calendar.MINUTE)==c.get(Calendar.MINUTE)
								   &&cal.get(Calendar.SECOND)==c.get(Calendar.SECOND))||(dist_real==0)){
								SharedPreferences mySharedPreferences= getSharedPreferences("test",Context.MODE_PRIVATE); 
								SharedPreferences.Editor mEditor = mySharedPreferences.edit();  
								int counter=mySharedPreferences.getInt("counter", 0);
								counter++;
								mEditor.putInt("counter", counter);  
								mEditor.commit();  
							   Intent in = new Intent(LockActivity.this,FinishAssigmentActivity.class);
							   startActivity(in);
							   finish();
							   mNotificationManager.cancel(1);
						   }
						   }catch (Exception e) {}         
					   }     
				   });
			   }     
	   /*
	    * quit
	    */
	   public final class QuitonClickListener implements View.OnClickListener{
	    	public void onClick(View v){
	    		if(flag==1){
	    			// 创建退出对话框  
	    			AlertDialog isExit2 = new AlertDialog.Builder(LockActivity.this).create();  
		            // 设置对话框标题  
		            isExit2.setTitle("退出提示");  
		            // 设置对话框消息  
		            isExit2.setMessage("你只有一次退出机会，确定要退出吗？超过(退出时间/任务时间)的1/3即任务失败");  
		            // 添加选择按钮并注册监听  
		            isExit2.setButton("确定", Quitlistener);  
		            isExit2.setButton2("取消", Quitlistener);  
		            // 显示对话框  
		            isExit2.show();
	    		}else{
	    			//v.setVisibility(View.INVISIBLE);
	    			Toast.makeText(getApplicationContext(), "已锁死",
							Toast.LENGTH_SHORT).show();
	    		}
	    		flag++;
	    	}
	    }
	   /**监听对话框里面的button点击事件*/  
	    DialogInterface.OnClickListener Quitlistener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序 
	            	finish();
	            	Timer timer=new Timer();
	        		TimerTask task=new TimerTask() {
	        			@Override
	        			public void run() {
	        				CountDownInform(getApplicationContext());	
	        			}
	        		};
	        		timer.schedule(task, SharedService.dist/3);	 
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };
	    public void CountDownInform(Context c){
	        String ns = Context.NOTIFICATION_SERVICE;
	        mNotificationManager = (NotificationManager) getSystemService(ns);
	        int icon = R.drawable.tuo;
	        CharSequence tickerText = "no拖任务倒计时";
	        long when = System.currentTimeMillis();
	        Notification notification = new Notification(icon, tickerText,when); 
	        Context context = c;
	        CharSequence contentTitle = "任务失败";
	        CharSequence contentText = "原因:退出时间超过任务时间的1/3";
	        Intent notificationIntent = new Intent(this, LockActivity.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                notificationIntent, 0);
	        notification.setLatestEventInfo(context, contentTitle, contentText,
	                contentIntent);
	        mNotificationManager.notify(1, notification);
	        }
//	   //shield Home
//		public void onAttachedToWindow() {
//			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//		    super.onAttachedToWindow();
//	    }
//		
		//shield Back
		public boolean onKeyDown(int keyCode ,KeyEvent event){			
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				return true ;
	             // TODO Auto-generated method stub
			else if (keyCode == event. KEYCODE_HOME) {
	             return true;
	         }else{
	             return super.onKeyDown(keyCode, event);

	      }
			
		}
		//shield Drop down notification bar
		public void onWindowFocusChanged(boolean hasFocus) {
	        super.onWindowFocusChanged(hasFocus);
	        try {
	            Object service = getSystemService("statusbar");
	            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
	            Method test = statusbarManager.getMethod("collapse");
	            test.invoke(service);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
		public void onDestroy() {  
	        Log.i(TAG, "----------------- onDestroy------");
	        unbindService(svc);
	        super.onDestroy();  
	        this.unregisterReceiver(mBatInfoReceiver);  
	        this.unregisterReceiver(mShutdownInfoReceiver);
	        //mNotificationManager.cancel(1);    
	    }  
}

		
	    

