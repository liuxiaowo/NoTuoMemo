package com.example.memo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.example.adapter.CollectGridItem;

public class ShowCardActivity extends Activity {
	ImageView card_iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_card);
		
		card_iv=(ImageView)findViewById(R.id.show_card_iv);
		int position;
		
		Intent intent = getIntent();   
		Bundle bundle = intent.getExtras();
		position=bundle.getInt("position");
		
		ImageView show_card_iv = (ImageView) findViewById(R.id.show_card_iv);
		CollectGridItem temp;
		temp = CollectShelfActivity.list.get(position);
		show_card_iv.setBackgroundResource(temp.imgID);
		
		card_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_card, menu);
		return true;
	}

}
