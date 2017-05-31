package com.example.memo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
@SuppressLint({ "NewApi", "ResourceAsColor" })
public class MemoAddOneActivity extends Activity {
	public static EditText title,content;
	String tit,con;
	RelativeLayout list,save;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_memo_add_one);
		title=(EditText)findViewById(R.id.title_inputbox);
		content=(EditText)findViewById(R.id.content_inputbox);
		if(AddMemoActivity.Edit){
			tit=AddMemoActivity.memo_title;
			con=AddMemoActivity.memo_content;
		}
			title.setText(tit);
			content.setText(con);
	}

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo_add_one, menu);
		return true;
	}
	

}
