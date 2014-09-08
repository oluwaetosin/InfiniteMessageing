package com.example.infinitemessaging;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	 
	private static final String DATABASE_NAME = "notification";
	private static final int DATABASE_VERSION = 5;
	private static final String TABLE_NAME = "push";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	 create(db);
 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion < newVersion) {db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
		  db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_CHANNEL + ";" );
		  create(db);
		}
		

	}
	
	private void create(SQLiteDatabase db) {
		String sql = "CREATE TABLE "
		 		     + TABLE_NAME +"("
		 		     + DBConstants.KEY_id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
		 		     + DBConstants.KEY_body + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_media + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_mimetype + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_read     + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_title    + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_time     + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_url      + " TEXT NOT NULL,"
		 		     + DBConstants.KEY_messageID     + " TEXT NOT NULL"
		 		     + ");";
		 db.execSQL(sql);
		 ContentValues tableContent = new ContentValues();
		 tableContent.put(DBConstants.KEY_body, "Hello world");
		 tableContent.put(DBConstants.KEY_media, "Heaad kamkmv oaoao oaoa");
		 tableContent.put(DBConstants.KEY_mimetype, "Hello world");
		 tableContent.put(DBConstants.KEY_read, "0");
		 tableContent.put(DBConstants.KEY_title, "Hello world");
		 tableContent.put(DBConstants.KEY_time, "04:24:02");
		 tableContent.put(DBConstants.KEY_url, "wdfwfwf");
		 tableContent.put(DBConstants.KEY_messageID, "wdfwfwrtree");
		 
		 String sqlChannels = "CREATE TABLE "
				 		     + 		"channels ("
				 		     + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				 		     + "name TEXT NOT NULL,"
				 		     + "register TEXT NOT NULL"
				 		     + ");";
 db.execSQL(sqlChannels);
// db.insert("push", null, tableContent);
// db.insert("push", null, tableContent);
// db.insert("push", null, tableContent);

 ContentValues content1 = new ContentValues();
               content1.put("name", "news");
               content1.put("register","true");
 

 ContentValues content2 = new ContentValues();
 content2.put("name", "weather");
 content2.put("register","true");
  

 
 ContentValues content3 = new ContentValues();
 				content3.put("name", "sport");
                content3.put("register","true");
 
   db.insert("channels", null, content1);
   db.insert("channels", null, content2);
   db.insert("channels", null, content3);
	}	 
}
