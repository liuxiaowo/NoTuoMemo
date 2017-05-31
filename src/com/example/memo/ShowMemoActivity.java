package com.example.memo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.datatype.Duration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.example.adapter.History;
import com.example.adapter.HorizontalListView;
import com.example.adapter.PubSelectedImgsAdapter;
import com.example.service.SharedService;
import com.example.sqlLite.SqlLiteOpenHelper;

public class ShowMemoActivity extends Activity {
	SqlLiteOpenHelper sqlOpenHelper;
	public static String show_voice;
	public static int show_picture;
	public static HorizontalListView show_listView;
	TextView title,time,content;
	ImageButton back,add;
	ImageButton last,next,edit,delete;
	String title_show,time_show,content_show,picture_show,voice_show,voic_path=null,ring_path=null,start_time,end_time;
	ArrayList <String> photo_show  = new ArrayList<String>();;
	int get_remind;
	int get_notuo;
	int get_shark;
	int get_cilent;
	ArrayList<History> hlist;
	public static int timecount=0,rsec,rmin;
	public static TextView player_currnet,player_total,show_memo_starttime,show_memo_endtime,show_memo_remind,show_memo_ring;
	public static ImageButton play;
	static int position=-1;
	public static ArrayList<String> pic_path_he = new ArrayList<String>();
	static SeekBar skb;
	static int skb_p=0;
	MediaPlayer player=new MediaPlayer();
	public static String create_time_real;
	public static String start_time_memo,end_time_memo;
	DecimalFormat df = new DecimalFormat("#00");
	String[] pict={"pic1","pic2","pic3","pic4","pic5","pic6","pic7","pic8","pic9"};
	RelativeLayout show_player;
	public static String create_time_photo,create_time_photo_show,create_time_voice_show,ring,ring_name;
	public static PubSelectedImgsAdapter imgAdapter,imgAdapter_show;
	File record_song;
	public static Thread th;
	public static boolean isTrue=true;
	public static int shark,client,remind,notuo;
	public static String url,name;
	
	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 2:	
				Bundle b2 = msg.getData();
				skb.setProgress(skb_p);
				player_currnet.setText( b2.getString("current"));
				player_total.setText( b2.getString("total"));
				play.setBackgroundResource(R.drawable.show_pause);
				break;
			}
		}
	};
	Runnable r = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {  
				if(player.isPlaying()){
						if (player.getDuration() != 0) {
							int position =player.getCurrentPosition();
							int total = player.getDuration();
							skb_p = position * 100 / total;
							int min = position / 1000 / 60;
							int sec = position / 1000 % 60;
							String sc = df.format(min) + ":"
									+ df.format(sec);
							String sd = df.format(total / 1000 / 60) + ":"
									+ df.format(total / 1000 % 60);
							Bundle b = new Bundle();
							b.putString("current", sc);
							b.putString("total", sd);
							Message m2 = new Message();
							m2.what = 2;
							m2.setData(b);
							handler.sendMessage(m2);
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_memo);
		skb_p=0;
		show_listView = (HorizontalListView)findViewById(R.id.show_listView);
		new Thread(r).start();
		title=(TextView)findViewById(R.id.show_memo_title);
		time=(TextView)findViewById(R.id.show_memo_time);
		content=(TextView)findViewById(R.id.show_memo_content);
		back=(ImageButton)findViewById(R.id.show_memo_back);
		add=(ImageButton)findViewById(R.id.show_memo_add);
		last=(ImageButton)findViewById(R.id.above);
		next=(ImageButton)findViewById(R.id.next);
		edit=(ImageButton)findViewById(R.id.change);
		delete=(ImageButton)findViewById(R.id.dele);
		skb=(SeekBar)findViewById(R.id.show_memo_seekbar);
		show_player=(RelativeLayout)findViewById(R.id.show_memo_player);
		skb.setProgress(0);
		player_currnet=(TextView)findViewById(R.id.show_memo_time_current);
		player_total=(TextView)findViewById(R.id.show_memo_time_total);
		play=(ImageButton)findViewById(R.id.show_memo_play);
		show_listView = (HorizontalListView)findViewById(R.id.show_listView);
		show_memo_starttime=(TextView)findViewById(R.id.show_memo_starttime);
		show_memo_endtime=(TextView)findViewById(R.id.show_memo_endtime);
		show_memo_remind=(TextView)findViewById(R.id.show_memo_remind);
		show_memo_ring=(TextView)findViewById(R.id.show_memo_ring);
		Intent in2 = getIntent();
		create_time_real = in2.getStringExtra("creat_time");
		pic_path_he.clear();
		MemoAddTwoActivity.mSelectPath.clear();
		sqlOpenHelper=new SqlLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = sqlOpenHelper.getReadableDatabase();
		Cursor c2 = db.query   ("pictures",null,null,null,null,null,null); 
		while(c2.moveToNext()){
			create_time_photo=c2.getString(c2.getColumnIndex("p_Id"));
			if(create_time_photo!=null){
			if(create_time_real.equals(create_time_photo)){
				for(int i=0;i<9;i++){
					String pic_path=c2.getString(c2.getColumnIndex(pict[i]));
					if(pic_path!=null){
					pic_path_he.add(pic_path);
					}
				}
			}
		  }
		}	
		db.close();
		 imgAdapter=new PubSelectedImgsAdapter(getApplicationContext(),pic_path_he);
		if(create_time_photo!=null){
			show_listView.setAdapter(imgAdapter);	
		}
		Intent intent=getIntent();
		Bundle show=intent.getExtras();
		time_show=show.getString("creat_time");
		title_show=show.getString("title");
		content_show=show.getString("content");
		picture_show=show.getString("picture");
		voic_path=show.getString("voice");
		start_time =show.getString("start_time");
		end_time = show.getString("end_time");
		shark=show.getInt("shark");
		client=show.getInt("client");
		ring = show.getString("ring");
		notuo=show.getInt("notuo");
		remind=show.getInt("remind");
		ring_name=show.getString("ring_name");
		if(voic_path==null)
			show_player.setVisibility(show_player.INVISIBLE);
		hlist=(ArrayList<History>)show.getSerializable("hlist");
		showView();
		position=searchPosition();
		btnChange(position);
		play.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(voic_path!=null){
					if(skb_p==0){
						playSongs();
					}
					else{ 
						if(player.isPlaying()){
							play.setBackgroundResource(R.drawable.show_play);
							player.pause();
						}
						else{
							player.start();
						}
					}
				}
			}
		});
		last.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skb_p=0;
				ring=null;
				if(!hlist.isEmpty()){
				if(position!=0){
					btnChange(--position);
					time_show=hlist.get(position).getTime();
					title_show=hlist.get(position).getTitle();
					content_show=hlist.get(position).getContent();
					picture_show = hlist.get(position).getPicture();
					voic_path=hlist.get(position).getVoice();
					start_time=hlist.get(position).getStart_time();
					end_time=hlist.get(position).getEnd_time();
					shark=hlist.get(position).getShark();
					client=hlist.get(position).getClient();
					ring=hlist.get(position).getRing();
					notuo=hlist.get(position).getNotuo();
					remind=hlist.get(position).getRemind();
					if(ring==null){
						show_memo_ring.setText("");
					}
					time.setText("创建时间:"+time_show);
					title.setText(title_show);
					if(!content_show.equals("")){
					content.setText("内容:"+content_show);
					}else{
					content.setText("");
					}
//					if(start_time!=null){
					if(remind==1){
					show_memo_starttime.setText("开始时间:"+start_time);
					if(shark==1&&client==0){
					show_memo_remind.setText("提醒方式:"+"震动");
					}else if(shark==0&&client==1){
					show_memo_remind.setText("提醒方式:"+"静音下提醒");	
					}else if(shark==1&&client==1){
					show_memo_remind.setText("提醒方式:"+"震动+静音下提醒");
					}else{
					show_memo_remind.setText("");
					}
					}else{
					show_memo_starttime.setText("");
					}
//						if(end_time!=null){
					if(notuo==1){
					show_memo_endtime.setText("结束时间:"+end_time);
					}else{
					show_memo_endtime.setText("");
					}
					photo_show.clear();
					sqlOpenHelper=new SqlLiteOpenHelper(getApplicationContext());
					SQLiteDatabase db = sqlOpenHelper.getReadableDatabase();
					Cursor c2 = db.query   ("pictures",null,null,null,null,null,null); 
					while(c2.moveToNext()){
						create_time_photo_show=c2.getString(c2.getColumnIndex("p_Id"));
						if(create_time_photo_show!=null){
						if(time_show.equals(create_time_photo_show)){
							for(int i=0;i<9;i++){
								String pic_path=c2.getString(c2.getColumnIndex(pict[i]));
								if(pic_path!=null){
								photo_show.add(pic_path);
								}
							}
						}
					  }
					}	
					db.close();
					PubSelectedImgsAdapter imgAdapter_show=new PubSelectedImgsAdapter(getApplicationContext(),photo_show);
					if(create_time_photo_show!=null){
						show_listView.setAdapter(imgAdapter_show);	
					}
					if(voic_path==null)
						show_player.setVisibility(show_player.INVISIBLE);
					else{
						show_player.setVisibility(show_player.VISIBLE);
					}
					ContentResolver contentResolver =  getApplicationContext().getContentResolver(); 
					Cursor c = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
					for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
						String url = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
						if(url.equals(voic_path)){
						int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
						String timetotal = df.format(duration / 1000 / 60) + ":"
								+ df.format(duration / 1000 % 60);
						player_total.setText(timetotal);
						}
						if(url.equals(ring)){
							name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
							show_memo_ring.setText("铃声:"+name);
						}
					}
					c.close();
				}
						
			}
			}
		});

next.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		skb_p=0;
		ring =null;
		// TODO Auto-generated method stub
		if(!hlist.isEmpty()){
			if(position!=hlist.size()-1){
				showNext();
			}
		}
	}
});

edit.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(position!=-1){
			AddMemoActivity.Edit=true;
			Bundle data=new Bundle();
			data.putString("creat_time",time_show);
			data.putString("pic",create_time_photo);
			data.putSerializable("show_hlist",hlist);
			data.putString("title",title_show);
			data.putString("content",content_show);
			data.putSerializable("show_hlist",(Serializable)hlist);
			data.putString("ring_path",ring_path);
			data.putString("voice",voic_path);
			data.putInt("notuo",notuo);
			data.putInt("shark",shark);
			data.putInt("remind",remind);
			data.putInt("cilent",client);
			data.putString("ring", ring);
			data.putString("ring_name", name);
			data.putString("start_time", start_time);
			data.putString("end_time", end_time);
			data.putString("timetotal", player_total.getText().toString());
			Intent in=new Intent(getApplicationContext(),AddMemoActivity.class);
			in.putExtras(data);
			startActivity(in);
			finish();
		}
	}
});

delete.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
	if(!hlist.isEmpty()){
		showDel();
	}
}
});

back.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), ListMemoActivity.class);
		startActivity(intent);
		finish();
	}
});
add.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), AddMemoActivity.class);
		startActivity(intent);
		finish();
	}
});

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

}
	protected void showView() {
		// TODO Auto-generated method stub
		skb_p=0;
		time.setText("创建时间:"+time_show);
		title.setText(title_show);
		if(!content_show.equals("")){
			content.setText("内容:"+content_show);
			}else{
			content.setText("");
			}
		if(remind==1){
			show_memo_starttime.setText("开始时间:"+start_time);
			if(shark==1&&client==0){
			show_memo_remind.setText("提醒方式:"+"震动");
			}else if(shark==0&&client==1){
			show_memo_remind.setText("提醒方式:"+"静音下提醒");	
			}else if(shark==1&&client==1){
			show_memo_remind.setText("提醒方式:"+"震动+静音下提醒");
			}else{
			show_memo_remind.setText("");
			}
			}else{
			show_memo_starttime.setText("");
			}
//				if(end_time!=null){
			if(notuo==1){
			show_memo_endtime.setText("结束时间:"+end_time);
			}else{
			show_memo_endtime.setText("");
			}
			if(ring_name!=null){
				show_memo_ring.setText("铃声:"+ring_name);
			}
		photo_show.clear();
		sqlOpenHelper=new SqlLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = sqlOpenHelper.getReadableDatabase();
		Cursor c2 = db.query   ("pictures",null,null,null,null,null,null); 
		while(c2.moveToNext()){
			create_time_photo_show=c2.getString(c2.getColumnIndex("p_Id"));
			if(create_time_photo_show!=null){
			if(time_show.equals(create_time_photo_show)){
				for(int i=0;i<9;i++){
					String pic_path=c2.getString(c2.getColumnIndex(pict[i]));
					if(pic_path!=null){
					photo_show.add(pic_path);
					}
				}
			}
		  }
		}	
		db.close();
		PubSelectedImgsAdapter imgAdapter_show=new PubSelectedImgsAdapter(getApplicationContext(),photo_show);
		if(create_time_photo_show!=null){
			show_listView.setAdapter(imgAdapter_show);	
		}
		if(voic_path==null)
			show_player.setVisibility(show_player.INVISIBLE);
		else{
			show_player.setVisibility(show_player.VISIBLE);
		}
		ContentResolver contentResolver =  getApplicationContext().getContentResolver(); 
		Cursor c = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			String url = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			if(url.equals(voic_path)){
			int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
			String timetotal = df.format(duration / 1000 / 60) + ":"
					+ df.format(duration / 1000 % 60);
			player_total.setText(timetotal);
			}
			if(url.equals(ring)){
				name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				show_memo_ring.setText("铃声:"+name);
			}
		}
		c.close();
		if(ring!=null){
			ring=null;
		}
	}

	void btnChange(int i){
			if(position==hlist.size()-1) next.setBackgroundResource(R.drawable.next_latest);
			else next.setBackgroundResource(R.drawable.next);
			if(position==0) last.setBackgroundResource(R.drawable.above_latest);
			else last.setBackgroundResource(R.drawable.above);
	}

	int searchPosition() {
		// TODO Auto-generated method stub
		int i;
		if(!hlist.isEmpty()){
			for(i=0;i<hlist.size();i++){
				String time=hlist.get(i).getTime();
				if(time_show.equals(time)) return i;
			}
			return -1;
		} 
		return -1;

	}
	void showNext(){
		btnChange(++position);
		time_show=hlist.get(position).getTime();
		title_show=hlist.get(position).getTitle();
		content_show=hlist.get(position).getContent();
		picture_show = hlist.get(position).getPicture();
		voic_path=hlist.get(position).getVoice();
		start_time=hlist.get(position).getStart_time();
		end_time=hlist.get(position).getEnd_time();
		shark=hlist.get(position).getShark();
		client=hlist.get(position).getClient();
		ring=hlist.get(position).getRing();
		notuo=hlist.get(position).getNotuo();
		remind=hlist.get(position).getRemind();
		if(ring==null){
			show_memo_ring.setText("");
		}
		time.setText("创建时间:"+time_show);
		title.setText(title_show);
		if(!content_show.equals("")){
			content.setText("内容:"+content_show);
			}else{
			content.setText("");
			}
		if(remind==1){
			show_memo_starttime.setText("开始时间:"+start_time);
			if(shark==1&&client==0){
			show_memo_remind.setText("提醒方式:"+"震动");
			}else if(shark==0&&client==1){
			show_memo_remind.setText("提醒方式:"+"静音下提醒");	
			}else if(shark==1&&client==1){
			show_memo_remind.setText("提醒方式:"+"震动+静音下提醒");
			}else{
			show_memo_remind.setText("");
			}
			}else{
			show_memo_starttime.setText("");
			}
//				if(end_time!=null){
			if(notuo==1){
			show_memo_endtime.setText("结束时间:"+end_time);
			}else{
			show_memo_endtime.setText("");
			}
		photo_show.clear();
		sqlOpenHelper=new SqlLiteOpenHelper(getApplicationContext());
		SQLiteDatabase db = sqlOpenHelper.getReadableDatabase();
		Cursor c2 = db.query   ("pictures",null,null,null,null,null,null); 
		while(c2.moveToNext()){
			create_time_photo_show=c2.getString(c2.getColumnIndex("p_Id"));
			if(create_time_photo_show!=null){
			if(time_show.equals(create_time_photo_show)){
				for(int i=0;i<9;i++){
					String pic_path=c2.getString(c2.getColumnIndex(pict[i]));
					if(pic_path!=null){
					photo_show.add(pic_path);
					}
				}
			}
		  }
		}	
		db.close();
		PubSelectedImgsAdapter imgAdapter_show=new PubSelectedImgsAdapter(getApplicationContext(),photo_show);
		if(create_time_photo_show!=null){
			show_listView.setAdapter(imgAdapter_show);	
		}
		if(voic_path==null)
			show_player.setVisibility(show_player.INVISIBLE);
		else{
			show_player.setVisibility(show_player.VISIBLE);
		}
		ContentResolver contentResolver =  getApplicationContext().getContentResolver(); 
		Cursor c = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			String url = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			if(url.equals(voic_path)){
			int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
			String timetotal = df.format(duration / 1000 / 60) + ":"
					+ df.format(duration / 1000 % 60);
			player_total.setText(timetotal);
			}
			if(url.equals(ring)){
				name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
					show_memo_ring.setText("铃声:"+name);
			}
		}
		c.close();
		if(ring!=null){
			ring = null;
		}
	}
	void del(){
		ListMemoActivity.delHistory(getApplicationContext(),time_show);
		if(hlist.size()==1){//只剩一个
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), ListMemoActivity.class);
			startActivity(intent);
			finish();
		}else{
			if(position==hlist.size()-1){
				position=-1;
				showNext();
				hlist.remove(hlist.size()-1);
			}else{
				showNext();
				hlist.remove(position-1);
			} 
			
		}
		position=searchPosition();
		btnChange(position);
	}
	
	 @SuppressWarnings("unused")
	private void showDel(){
	        //AlertDialog.Builder normalDialog=new AlertDialog.Builder(getApplicationContext());  
	        AlertDialog.Builder normalDia=new AlertDialog.Builder(ShowMemoActivity.this);  
	        normalDia.setIcon(R.drawable.ic_launcher);  
	        normalDia.setTitle("删除极简");  
	        normalDia.setMessage("你确定删除这条备忘录文本记录吗？");  
	          
	        normalDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                // TODO Auto-generated method stub  
	                del();
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

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent in=new Intent(getApplicationContext(),ListMemoActivity.class);
		startActivity(in);
		finish();
	}

	void playSongs() {
		// TODO Auto-generated method stub
		if(voic_path!=null){
			player.reset();
			try {
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setDataSource(voic_path);
				try {
					player.setOnPreparedListener(preparedListener);
					player.prepareAsync();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				player.prepare();
//				player.start();
				player.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
							skb_p=0;
							skb.setProgress(skb_p);
							player_currnet.setText("00:00");
							play.setBackgroundResource(R.drawable.show_play);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_memo, menu);
		return true;
	}
}
