package com.example.memo;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FinishAssigmentActivity extends Activity {
	int counterbefore;
	int counternow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_finish_assigment);
		Button reward;
		reward=(Button)findViewById(R.id.finish_assigment_reward);
		Button reward_later;
		reward_later=(Button)findViewById(R.id.finish_assigment_later);
		final TextView num;
		num=(TextView)findViewById(R.id.finish_assigment_num);
		
		
		
		final SharedPreferences mySharedPreferences= getSharedPreferences("test", 
				Context.MODE_PRIVATE); 
		SharedPreferences.Editor mEditor = mySharedPreferences.edit();  
		counternow=mySharedPreferences.getInt("counter", 0);
		String text="抽奖次数剩余:"+counternow;
		num.setText(text);
		
		reward.setOnClickListener(new OnClickListener() {
			
			@Override
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				setContentView(R.layout.activity_reward);
				
//				//实例化SharedPreferences对象（第一步） 
//				SharedPreferences mySharedPreferences= getSharedPreferences("test", 
//								Context.MODE_PRIVATE); 
//				SharedPreferences.Editor mEditor = mySharedPreferences.edit();  
//				counterbefore=mySharedPreferences.getInt("counter", 0);
//				mEditor.putInt("counter", counterbefore+counterafter);  
//				mEditor.commit();  
//				counternow=mySharedPreferences.getInt("counter", 0);
				
				if (counternow>=1) {
					counternow--;
					String text="抽奖次数剩余:"+counternow;
					num.setText(text);
				}
				
				Intent in = new Intent(FinishAssigmentActivity.this,RewardActivity.class);
				startActivity(in);
				 }
			
		});
reward_later.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		
		//使用toast信息提示框提示成功写入数据 
//		Toast.makeText(this, "数据成功写入SharedPreferences！" , 
//		Toast.LENGTH_LONG).show(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finish_assigment, menu);
		return true;
	}

}
