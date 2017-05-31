package com.example.memo;

import java.util.Random;

import com.example.sqlLite.SqlLiteOpenHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

public class RewardActivity extends Activity {
	private ImageView iv_frame;
	private AnimationDrawable frameAnim;
	private Animation animation;

	private SQLiteDatabase db;
	private static String numchange;

	int cardback = R.drawable.cardback;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reward);

		SharedPreferences mySharedPreferences = getSharedPreferences("test",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mySharedPreferences.edit();
		int counter = mySharedPreferences.getInt("counter", 0);

		// Toast.makeText(this, "读取数据如下："+"\n"+"name：" + counter + "\n" +
		// "habit：",
		// Toast.LENGTH_LONG).show();

		iv_frame = (ImageView) findViewById(R.id.iv_frame);
		
		iv_frame.setBackgroundResource(R.anim.frame);
		         // 把AnimationDrawable设置为ImageView的背景
		frameAnim = (AnimationDrawable) iv_frame.getBackground(); 
		
		frameAnim.start();
	
		
		
		final ImageView image1 = (ImageView) findViewById(R.id.reward_card);
		if (counter < 1) {
			Toast.makeText(getApplicationContext(), "抽奖次数不足", Toast.LENGTH_LONG)
					.show();
			image1.setImageDrawable(getResources().getDrawable(R.drawable.nocount));
		} else {
			Random rand = new Random();
			int i;
			i = rand.nextInt(14);
			image1.setImageDrawable(getResources().getDrawable(
					CollectShelfActivity.Card[i]));

			SqlLiteOpenHelper sdb = new SqlLiteOpenHelper(RewardActivity.this);

			db = sdb.getWritableDatabase();
			String a;
			a = String.valueOf(i);
			numchange = a;

			ContentValues values = new ContentValues();
			values.put("cardName", numchange);
			db.update("card", values, "cardID=?", new String[] { numchange });

			System.out.print(numchange);
			db.close();

			counter--;
			 
			mEditor.putInt("counter", counter);
			mEditor.commit();
//			Toast.makeText(this,
//					"读取数据如下：" + "\n" + "name：" + counter + "\n" + "habit：",
//					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reward, menu);
		return true;
	}
//	 public void stopFrame(View view) {  
//	        AnimationDrawable anim = (AnimationDrawable) iv_frame.getBackground();  
//	        if (anim.isRunning()) { //如果正在运行,就停止  
//	            anim.stop();  	            
//	            iv_frame.setVisibility(View.INVISIBLE);
//	        }  
//	    }  

}
