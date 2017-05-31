package com.example.service;



import java.io.IOException;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.memo.LockActivity;



public class LockService extends Service {  
    private String TAG = "LockService";
    private Intent zdLockIntent = null; 
    public static MediaPlayer mplayer =new MediaPlayer();

    IBinder mBinder = new newBinder();

	public class newBinder extends Binder {
		// 绑定时获取Service
		public LockService getService() {
			return LockService.this;
		}
	}
	// 绑定Service
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
    public void onCreate() {  
        super.onCreate(); 
        zdLockIntent = new Intent(LockService.this, LockActivity.class);
        //close before Activity
        zdLockIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //create new Activity
        zdLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /* Registered broadcasting*/  
        IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");  
        LockService.this.registerReceiver(mScreenOnReceiver, mScreenOnFilter);  
  
        /* Registered broadcasting*/  
        IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");  
        LockService.this.registerReceiver(mScreenOffReceiver, mScreenOffFilter); 

    }  
  
    public void onDestroy() {  
        Log.i(TAG, "----------------- onDestroy------");  
        super.onDestroy();  
        this.unregisterReceiver(mScreenOnReceiver);  
        this.unregisterReceiver(mScreenOffReceiver);
    }  
  

	private KeyguardManager mKeyguardManager = null;  
    private KeyguardManager.KeyguardLock mKeyguardLock = null;  
    // The screen becomes bright, we have to hide the default lock screen interface.
    private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {  
                Log.i(TAG, "----------------- android.intent.action.SCREEN_ON------");  
            }  
        }  
    };  
  
    // The screen becomes dark / bright. We need to call the KeyguardManager class to unlock the screen.
    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals("android.intent.action.SCREEN_OFF") || action.equals("android.intent.action.SCREEN_ON")) {  
                mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);  
                mKeyguardLock = mKeyguardManager.newKeyguardLock("");  
                mKeyguardLock.disableKeyguard(); 
                //startActivity(zdLockIntent);
            }  
        }  
    }; 
    public void playsongs(String path) {
		try {
			mplayer.reset();
			mplayer.setDataSource(path);  //写这个方法，可以自动加载try catch
			mplayer.prepare();
			mplayer.start();
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

	public boolean isPlaying() {
		return mplayer.isPlaying();
	}

	public int getCurrentPosition() {
		return mplayer.getCurrentPosition();
	}

	public int getDuration() {
		return mplayer.getDuration();
	}

	public void seekTo(int msec) {
		mplayer.seekTo(msec);
	}

	public void start() {
		mplayer.start();
	}

	public void pause() {
		mplayer.pause();
	}
  
}  
