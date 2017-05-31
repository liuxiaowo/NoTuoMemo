package com.example.memo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.adapter.CollectGridAdapter;
import com.example.adapter.CollectGridItem;
import com.example.sqlLite.SqlLiteOpenHelper;

public class CollectShelfActivity extends Activity {
	int i = 0;
	int p = 0;

	private SQLiteDatabase db;
	public static ArrayList<CollectGridItem> list;
	CollectGridAdapter adapter;
	GridView lv;
	Button collectreward;

	CollectGridItem temp;

	public static int[] Card = new int[] { R.drawable.card0, R.drawable.card1,
			R.drawable.card2, R.drawable.card3, R.drawable.card4,
			R.drawable.card5, R.drawable.card6, R.drawable.card7,
			R.drawable.card8, R.drawable.card9, R.drawable.card10,
			R.drawable.card11, R.drawable.card12, R.drawable.card13,
			R.drawable.card14,

			};
	int cardback = R.drawable.cardback;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collect_shelf);

		collectreward=(Button)findViewById(R.id.collectshelf_reward);
		
		Initialize();
		adapter = new CollectGridAdapter(list, getApplicationContext());
		lv = (GridView) findViewById(R.id.collectshelf_cards);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				// TODO Auto-generated method stub

				
				Intent intent = new Intent(CollectShelfActivity.this,ShowCardActivity.class); 
				intent.putExtra("position", position); 
				startActivity(intent);
			
			}

		});
		
		collectreward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(CollectShelfActivity.this,FinishAssigmentActivity.class);
				startActivity(in);
			}
		});
		
	}

	private void Initialize() {
		// TODO Auto-generated method stub
		list = new ArrayList<CollectGridItem>();
		// 数据库连接类
		 SqlLiteOpenHelper sdb =new SqlLiteOpenHelper(getApplicationContext());
		// 获得数据库操作类
		db = sdb.getReadableDatabase();

		Cursor c = db.rawQuery("select cardName from card ", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			i = c.getInt(0);
			if (i < 0) {
				CollectGridItem m = new CollectGridItem(cardback, i, cardback);
				list.add(m);
			} else {
				CollectGridItem m = new CollectGridItem(Card[i], i, Card[i]);
				list.add(m);
			}

		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collect_shelf, menu);
		return true;
	}

}
