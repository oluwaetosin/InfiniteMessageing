package com.example.infinitemessaging;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBConstants {
	public static final String KEY_id = "_id";
	public static final String KEY_title = "title";
	public static final String KEY_body = "body";
	public static final String KEY_time = "time";
	public static final String KEY_media = "media";
	public static final String KEY_mimetype = "mimetype";
	public static final String KEY_read = "read";
	public static final String KEY_url = "url";
	public static final String KEY_messageID = "messageID";
	
	 
	
	public static final String TABLE_NAME = "push";
	public static final String TABLE_CHANNEL = "channels";
	
	
	public void insert(Context context, String body, String media, String mimetype, String read,
			String title, String url, String time, String messageID){
		
		DBHelper oData =  new DBHelper(context);
		 SQLiteDatabase  db  = oData.getReadableDatabase();
		 
		 ContentValues tableContent = new ContentValues();
		 tableContent.put(DBConstants.KEY_body, body);
		 tableContent.put(DBConstants.KEY_media,media);
		 tableContent.put(DBConstants.KEY_mimetype, mimetype);
		 tableContent.put(DBConstants.KEY_read, "0");
		 tableContent.put(DBConstants.KEY_title,title);
		 tableContent.put(DBConstants.KEY_time, time);
		 tableContent.put(DBConstants.KEY_url, url);
		 tableContent.put(DBConstants.KEY_messageID, messageID);
	
 db.insert("push", null, tableContent);
	}

}
