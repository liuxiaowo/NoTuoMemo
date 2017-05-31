package com.example.memo;

import java.util.Timer;
import java.util.TimerTask;

import com.example.service.SharedService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
	
		final Intent it = new Intent(this, AddMemoActivity.class); //你要转向的Activity   
		Timer timer = new Timer(); 
		TimerTask task = new TimerTask() {  
		    @Override  
		    public void run() { 
		    Intent in = new Intent(getApplicationContext(),SharedService.class);
			startService(in);
		    startActivity(it); //执行  
		    finish();
		 } 
	};
		timer.schedule(task, 1000 * 2); //2秒后
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
