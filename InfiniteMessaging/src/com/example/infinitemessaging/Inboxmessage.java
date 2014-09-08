package com.example.infinitemessaging;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.infobip.push.media.MediaWebView;

public class Inboxmessage extends Activity {
 
	TextView title, message, url;
	MediaWebView media;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		DBHelper oData;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inboxmessage);

		title = (TextView) findViewById(R.id.txtTitle);
		message = (TextView) findViewById(R.id.txtInboxMsg);
		media = (MediaWebView) findViewById(R.id.txtMedia);
		url = (TextView) findViewById(R.id.txtUrl);

		Bundle b = getIntent().getExtras();
		final long id = b.getLong("id");

		oData = new DBHelper(this);

		SQLiteDatabase db = oData.getReadableDatabase();
		
		String sql = "SELECT * FROM push WHERE _id=" + Long.toString(id);
		Cursor oLoop = db.rawQuery(sql, null);
		if (oLoop.moveToFirst()) {
			final String Message = oLoop.getString(1);
			String Title = oLoop.getString(5);
			String mimeType = oLoop.getString(3);
			String Url = oLoop.getString(7);
			String media = oLoop.getString(2);
			populateView(Message, Title, media, Url, mimeType);
			if(oLoop.getString(4).equals("0")){
				String messageID = oLoop.getString(8);
				ContentValues content = new ContentValues();
				content.put(DBConstants.KEY_read, "1");
				db.update("push", content, "_id =?", new String[]{Long.toString(id)});
				SharedPreferences pref = getApplicationContext().getSharedPreferences("com.example.infinitemessaging.myPrefFile",0);
				final String userId = pref.getString("userID", "");
				final String deviceId = pref.getString("deviceID", "");
				sendUpdates su = new sendUpdates();
				 su.execute(new String[]{userId,deviceId,messageID,"20"});
			};
		}

	}

	

	private void populateView(String Message, String Title, String Media,
			final String Url, String mimeType) {
		title.setText(Title);
		if (Message != null) {
			message.setText(Message);
		}
		if (Media != null) {
			media.loadData(Media, "text/html", null);
		}
		url.setText("Visit: " + Html.fromHtml("<u><a href='"+Url+"'>"+Url+"</a></u>"));
		url.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(Url));
				startActivity(myIntent);	
			}
		});
	}
	
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this,Inbox.class);
		startActivity(intent);
		finish();
	}
}
