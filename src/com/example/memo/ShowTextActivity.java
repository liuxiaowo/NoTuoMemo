package com.example.memo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.adapter.History;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ShowTextActivity extends Activity {
	TextView title,time,content;
	ImageButton back,add;
	ImageButton last,next,edit,delete;
	String title_show,time_show,content_show;
	ArrayList<History> hlist;
	static int position=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_text);
		
		title=(TextView)findViewById(R.id.show_text_title);
		time=(TextView)findViewById(R.id.show_text_time);
		content=(TextView)findViewById(R.id.show_text_content);
		back=(ImageButton)findViewById(R.id.show_text_back);
		add=(ImageButton)findViewById(R.id.show_text_add);
		last=(ImageButton)findViewById(R.id.text_above);
		next=(ImageButton)findViewById(R.id.text_next);
		edit=(ImageButton)findViewById(R.id.text_change);
		delete=(ImageButton)findViewById(R.id.text_dele);
		
		Intent intent=getIntent();
		Bundle show=intent.getExtras();
		time_show=show.getString("creat_time");
		title_show=show.getString("title");
		content_show=show.getString("content");
//		hlist=ListTextActivity.hlist;
		hlist=(ArrayList<History>)show.getSerializable("hlist");
		showView();
		
		position=searchPosition();
		btnChange(position);
		
		last.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!hlist.isEmpty()){
				if(position!=0){
					btnChange(--position);
					time_show=hlist.get(position).getTime();
					title_show=hlist.get(position).getTitle();
					content_show=hlist.get(position).getContent();
					time.setText(time_show);
					title.setText(title_show);
					content.setText(content_show);
				}	
			}
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
					AddTextActivity.Edit=true;
					Bundle data=new Bundle();
					data.putString("creat_time",time_show);
					data.putString("title",title_show);
					data.putString("content",content_show);
					data.putSerializable("show_hlist",hlist);
					Intent in=new Intent(getApplicationContext(),AddTextActivity.class);
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
				intent.setClass(getApplicationContext(), ListTextActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), AddTextActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	
	protected void showView() {
		// TODO Auto-generated method stub
		time.setText(time_show);
		title.setText(title_show);
		content.setText(content_show);
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
		showView();
	}

	void del(){
		ListTextActivity.delHistory(getApplicationContext(),time_show);
		if(hlist.size()==1){//只剩一个
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), ListTextActivity.class);
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
	        AlertDialog.Builder normalDia=new AlertDialog.Builder(ShowTextActivity.this);  
	        normalDia.setIcon(R.drawable.ic_launcher);  
	        normalDia.setTitle("删除极简");  
	        normalDia.setMessage("你确定删除这条极简文本记录吗？");  
	          
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_text, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent in=new Intent(getApplicationContext(),ListTextActivity.class);
		startActivity(in);
		finish();
	}

}
