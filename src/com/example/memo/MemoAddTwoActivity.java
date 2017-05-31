package com.example.memo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.adapter.HorizontalListView;
import com.example.adapter.PubSelectedImgsAdapter;
import com.example.service.SharedService;
import com.example.service.LockService.newBinder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "NewApi", "ResourceAsColor" })
public class MemoAddTwoActivity extends Activity implements OnClickListener{
		public static ImageButton play;
		ImageButton record,finish,delete;
		public static SeekBar skb;
	    String tit;
	    public static TextView recording_time,player_currnet,player_total;
	    public static String record_ring_path=null;
		private View parentView;
		private PopupWindow pop = null;
		private RelativeLayout ll_popup;
		public  static File soundFile=null;
	    public static File voice;
	    public static MediaRecorder recorder=null;
		public static MediaPlayer player=new MediaPlayer();
		public static int progress;
		public static int timecount=0,rsec,rmin;
		public static String timetotal;
		public static final int REQUEST_IMAGE = 2;
		public static ArrayList<String> mSelectPath =new ArrayList<String>();
		public static Timer timer;
		public static PubSelectedImgsAdapter pubSelectedImgsAdapter;
		public static HorizontalListView listView;
		DecimalFormat df = new DecimalFormat("#00");
		
		@SuppressLint("HandlerLeak")
		public  Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:		
					Bundle b = msg.getData();
					recording_time.setText(b.getString("time"));
					break;
				case 2:	
					Bundle b2 = msg.getData();
					skb.setProgress(b2.getInt("progress"));
					player_currnet.setText( b2.getString("current"));
					player_total.setText( b2.getString("total"));
					play.setBackgroundResource(R.drawable.pause);
					break;
				case 3:
					play.setBackgroundResource(R.drawable.play);
					break;
				case 4:
					player_currnet.setText("00:00");
					player_total.setText("00:00");
					progress=0;
					skb.setProgress(progress);
					play.setBackgroundResource(R.drawable.play_gray);
					break;
				}
			}
		};
		Runnable r = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (recorder==null) { 
						if (!player.isPlaying()) {
							if(soundFile!=null&&soundFile.exists()){
								Message m3 = new Message();
								m3.what = 3;
								handler.sendMessage(m3);
							}else{
								Message m4 = new Message();
								m4.what = 4;
								handler.sendMessage(m4);
							}
						}else{
							if (player.getDuration() != 0) {
								int position =player.getCurrentPosition();
								int total = player.getDuration();
								progress = position * 100 / total;
								int min = position / 1000 / 60;
								int sec = position / 1000 % 60;
								String sc = df.format(min) + ":"
										+ df.format(sec);
								String sd = df.format(total / 1000 / 60) + ":"
										+ df.format(total / 1000 % 60);
								Bundle b = new Bundle();
								b.putInt("progress", progress);
								b.putString("current", sc);
								b.putString("total", sd);

								Message m2 = new Message();
								m2.what = 2;
								m2.setData(b);
								handler.sendMessage(m2);
							}
						}
					}
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}// while
			}
		};
		
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        parentView = getLayoutInflater().inflate(R.layout.activity_memo_add_two, null);
			setContentView(parentView);
			listView=(HorizontalListView) findViewById(R.id.accessory_listView);
	        new Thread(r).start();
	        if(AddMemoActivity.get_total!=null){
				player_total.setText(AddMemoActivity.get_total);
			}
	        //进度条
	        skb= (SeekBar) this.findViewById(R.id.seekbar);
	        //录音
	        record=(ImageButton)findViewById(R.id.voice);
	        //播放
	        play=(ImageButton)findViewById(R.id.play);
	        delete=(ImageButton)findViewById(R.id.delete);
	        player_currnet=(TextView)findViewById(R.id.time_current);
	        player_total=(TextView)findViewById(R.id.time_total);
			Init();
			play.setOnClickListener(this);
	        record.setOnClickListener(this); 
	        delete.setOnClickListener(this);
	        skb.setProgress(0);
	        skb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					if (fromUser) {
						int mesc = progress * player.getDuration() / 100;
						player.seekTo(mesc);
					}
				}
			});
	        findViewById(R.id.accessory_button).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                Intent intent = new Intent(MemoAddTwoActivity.this, MultiImageSelectorActivity.class);
	                // 是否显示拍摄图片
	                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
	                // 最大可选择图片数量
	                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
	                // 选择模式
	                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
	                // 默认选择
	                if(!mSelectPath.isEmpty() && mSelectPath.size()>0){
	                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
	                }
	                getParent().startActivityForResult(intent,REQUEST_IMAGE);
	            }
	        });

	    }

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("InlinedApi")
	public void Init() {
	    try {
	   		voice = new File(Environment.getExternalStorageDirectory().getCanonicalFile()+"/no拖noDie/record/");
	   	} catch (IOException e) {
	   		// TODO Auto-generated catch block
	   		e.printStackTrace();
	   	}
	    if (!voice.exists()) {
	       	voice.mkdirs();
	    }
		View view = getLayoutInflater().inflate(R.layout.pop_recording, null);
		pop = new PopupWindow(view,150,150);
		ll_popup = (RelativeLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(false);
		pop.setOutsideTouchable(false);
		pop.setContentView(view);
		finish = (ImageButton) view
				.findViewById(R.id.recording_finish);
        recording_time=(TextView)view.findViewById(R.id.recording_time);

		finish.setOnClickListener(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo_add_two, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.voice:
				ll_popup.startAnimation(AnimationUtils.loadAnimation(MemoAddTwoActivity.this,R.anim.activity_translate_in));
				pop.showAtLocation(parentView, Gravity.BOTTOM, 100, 100);
				record.setBackgroundResource(R.drawable.voice_stop);
				if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
					Toast.makeText(getApplicationContext(),"SD卡不存在",Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					soundFile=new File(voice, System.currentTimeMillis() + ".amr");
					// new出MediaRecorder对象  
				    recorder = new MediaRecorder();// new出MediaRecorder对象  
		            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
		            // 设置MediaRecorder的音频源为麦克风  
		            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  
		            // 设置MediaRecorder录制的音频格式  
		            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
					recorder.setOutputFile(soundFile.getAbsolutePath());
		            recorder.prepare();
		            recorder.start();
				}catch (IllegalStateException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            } 
				rsec=0;
				rmin=0;
				TimerTask task = new TimerTask(){  
				      public void run() { 
				    	    ++rsec;
				    	    if(rsec==60){
								++rmin;
								rsec=0;
							}
				    	    String time=df.format(rmin)+":"+df.format(rsec);
							Bundle b = new Bundle();
							b.putString("time", time);
							Message m1 = new Message();
							m1.what = 1;
							m1.setData(b);
							handler.sendMessage(m1); 
				    }  
				 };  
				timer = new Timer(true);
				timer.schedule(task,1000, 1000); 
				break;			
			case R.id.play:
				if(soundFile!=null&&soundFile.exists()){
					if(player.isPlaying()){
						player.pause();
						play.setBackgroundResource(R.drawable.pause);
					}else{
						if(progress!=0) player.start();
						else playSongs();
					}	
				}
				break;
			case R.id.recording_finish:
				if(soundFile!=null&&soundFile.exists()){
					record_ring_path=soundFile.getAbsolutePath();
					recorder.stop();
					recorder.release();
					timer.cancel();
					recorder=null;
					timetotal= df.format(rmin) + ":"
							+ df.format(rsec);
					player_total.setText(timetotal);
				}
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
				record.setBackgroundResource(R.drawable.voice_start);
				break;
			case R.id.delete:
				if(soundFile!=null&&soundFile.exists()){
					soundFile.delete();
					soundFile=null;
				}
				break;
		}
	}

	void playSongs() {
		// TODO Auto-generated method stub
		if(soundFile!=null&&soundFile.exists()){
			player.reset();
			try {
				player.setDataSource(soundFile.getAbsolutePath());
				try {
					player.setOnPreparedListener(preparedListener);
					player.prepareAsync();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				player.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
							progress=0;
							skb.setProgress(progress);
							player_currnet.setText("00:00");
					}
				});
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
	}
OnPreparedListener preparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			player.start();
		}
	};
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
//    	if (keyCode == KeyEvent.KEYCODE_BACK) {
//    		AddMemoActivity.exit(getApplicationContext(),getParent());	
//    	}
    	return true;
    	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

	public void handleActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_IMAGE){	
			switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
			   case RESULT_OK:
//            if(resultCode == RESULT_OK){
            	mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                pubSelectedImgsAdapter=new PubSelectedImgsAdapter(getApplicationContext(), mSelectPath);
                break;
			   case RESULT_CANCELED:
				   mSelectPath.clear();
				 break;
            }
			if(data!=null){
            listView.setAdapter(pubSelectedImgsAdapter); 
			}
        }
	}
}


