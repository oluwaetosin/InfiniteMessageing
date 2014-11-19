package com.example.infinitemessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.infobip.push.AbstractPushReceiver;
import com.infobip.push.PushNotification;
import com.infobip.push.PushNotificationBuilder;
    public class MyPushReceiver extends AbstractPushReceiver  {
	 // get Instance  of Database Adapter
    	
	@Override
	public void onRegistered(Context context) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "Successfully registered.", Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	protected void onRegistrationRefreshed(Context context) {
	Toast.makeText(context, "Registration is refreshed.", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onNotificationReceived(PushNotification notification,
			Context context) {
		PushNotificationBuilder builder = new PushNotificationBuilder(context);
		builder.setLayoutId(R.layout.notification_drawer);
		builder.setImageId(R.id.image);
		builder.setImageDrawableId(R.drawable.app_icon);
	    builder.setTitleId(R.id.title);
	    builder.setDateId(R.id.date);
	    builder.setTextId(R.id.text);

		    // TODO Auto-generated method stub
		    Calendar c = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
		    String strDate = sdf.format(c.getTime());
		 
		    SharedPreferences pref = context.getSharedPreferences("com.example.infinitemessaging.myPrefFile",0);
			final String userId = pref.getString("userID", "");
			final String deviceId = pref.getString("deviceID", "");
			
			
		    DBConstants myDB = new DBConstants();
		    sendUpdates sU    = new sendUpdates();
		  
			String id 		          = notification.getId();
			String msg 		          = notification.getMessage();
			String title              = notification.getTitle();
			String media              = notification.getMediaData();
			String mimetype           = notification.getMimeType();
			String url                = notification.getUrl();
			
			String read               = "0";
			myDB.insert(context, msg, media, mimetype,read, title, url, strDate,id);
		    sU.execute(new String[]{userId,deviceId,id,"15"});
			 		
	}
	@Override
	protected void onNotificationOpened(PushNotification notification, Context context) {
	redirect(context);
	
	}
	 

	@Override
	public void onUnregistered(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int reason, Context context) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "Error occurred: " + reason, Toast.LENGTH_SHORT).show();
	}
   public void redirect(Context context){
	    Intent intent = new Intent(context, Inbox.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(intent);
   }  
}
