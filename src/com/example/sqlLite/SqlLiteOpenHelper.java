package com.example.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteOpenHelper extends SQLiteOpenHelper{

	public SqlLiteOpenHelper(Context context) {
		super(context, "db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//备忘录
		db.execSQL("create table memo_data(m_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"creat_time varchar(100)," +
				"title varchar(100)," +
				"content varchar(100)," +
				"start_time varchar(100)," +
				"end_time varchar(100)," +
				"voice varchar(200)," +
				"remind int default 0," +
				"notuo int default 0," +
				"shark int default 0," +
				"cilent int default 0," +
				"ring varhcar(200)," +
				"picture varchar(100),"+
				"star int  default 0)");
		
		//极简
		db.execSQL("create table text_data(t_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"creat_time varchar(100)," +
				"title varchar(100)," +
				"content varchar(100)," +
				"star int default 0)");
		
		//备忘录添加图片
		db.execSQL("create table pictures( " +
				"p_Id varchar(100) PRIMARY KEY," +
				"pic1 varchar(200)," +
				"pic2 varchar(200)," +
				"pic3 varchar(200)," +
				"pic4 varchar(200)," +
				"pic5 varchar(200)," +
				"pic6 varchar(200)," +
				"pic7 varchar(200)," +
				"pic8 varchar(200)," +
				"pic9 varchar(200))");
		
		//抽奖
		db.execSQL("create table card(cardID Integer primary key, cardName Integer)");
		
	    db.execSQL("insert into card(cardID,cardName) values(0,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(1,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(2,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(3,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(4,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(5,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(6,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(7,-1)");
	    db.execSQL("insert into card(cardID,cardName) values(8,-1)");
	    
	    int values1,values2,i;
	    values1=9;
	    values2=-1;
	    for(i=9;i<15;i++){
	    	values1=i;
	    	String sql2="insert into card(cardID,cardName) values("+values1+","+values2+")";
	    	db.execSQL(sql2);
	    }
//	    int shark  = 1;
//	    //int cilent = 1;
//	    String title = "吃饭";
//	    String content = "意大利面";
//	    String start_time = "2015-9-24 0:29:00"; 
//	    String end_time = "2015-9-24 0:30:00";	
//        ContentValues cv = new ContentValues(); 
//        cv.put("shark", shark);
//        cv.put("title", title); 
//        cv.put("content", content);
//        cv.put("start_time", start_time);  
//        cv.put("end_time", end_time);  
//        db.insert("memo_data", null, cv);  
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists db");
		onCreate(db);
	}
}

