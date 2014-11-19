package com.example.infinitemessaging;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.infobip.push.ChannelRegistrationListener;
import com.infobip.push.PushNotificationManager;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Channels extends ListActivity {
	DBHelper oData;
	String domainUrl = getString(R.string.domainName);
	MyAdapter myadapter;
	List<String> channels = new ArrayList<String>();
	String foundChannel = null;
	String updateType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channels);
		
		oData = new DBHelper(this);
		SQLiteDatabase db = oData.getReadableDatabase();
		String sql = "SELECT _id,name,register FROM channels";
		Cursor oLoop = db.rawQuery(sql, null);
		startManagingCursor(oLoop);
		setListAdapter(new MyAdapter(this,R.layout.channel_list, oLoop));

	}

	private class MyAdapter extends ResourceCursorAdapter {
		final String register = "register";
		public MyAdapter(Context context,int layout, Cursor c) {
			super(context, layout, c);
			// TODO Auto-generated constructor stub
			c.moveToFirst();
			while (!c.isAfterLast()) 
			{
				if(c.getString(2).equals("true")){
				   channels.add(c.getString(1));
				}
				 c.moveToNext();
			}
			
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			
			 
			CheckBox chkBox = (CheckBox) view.findViewById(R.id.radioBtn);
			TextView text = (TextView) view.findViewById(R.id.txt);

			int channelId = cursor.getInt(cursor.getColumnIndex("_id"));
			 chkBox.setTag(channelId);
			 String channelName = cursor.getString(cursor.getColumnIndex("name"));
			String registered = cursor.getString(cursor
					.getColumnIndex(register)); 
			
			text.setText(channelName);
			if (registered.equals("true")) {
				chkBox.setChecked(true);
			}else{
				chkBox.setChecked(false);	
			}
			
			chkBox.setOnClickListener(new OnClickListener() {	
       		 
				public void onClick(View v) {
					// TODO Auto-generated method stub
					oData = new DBHelper(getApplicationContext());
					SQLiteDatabase db = oData.getReadableDatabase();
					String id = String.valueOf( (v.getTag()));
					if(((CompoundButton) v).isChecked()){
						updateType = "checked";
						ContentValues values = new ContentValues();
						values.put("register","true");
						db.update(DBConstants.TABLE_CHANNEL, values,"_id=?", new String[] {String.valueOf(id)});
						makeNewChannellIST(db,id,updateType);
				
						 
					}else{
						 
						ContentValues values = new ContentValues();
						values.put("register","false");
						updateType = "unchecked";
						db.update(DBConstants.TABLE_CHANNEL, values,"_id=?", new String[] {String.valueOf(id)});
						makeNewChannellIST(db,id,updateType);
						
					}
				   
				}

				private void makeNewChannellIST(SQLiteDatabase db, final String id, String checkStatus) {
					List<String> channels = new ArrayList<String>();
					String sql2 = "SELECT _id,name,register FROM channels";
					Cursor oLoop = db.rawQuery(sql2, null);
					oLoop.moveToFirst();
					while (!oLoop.isAfterLast()) 
					{
						if(oLoop.getString(2).equals("true")){
						   channels.add(oLoop.getString(1));
						}
						 oLoop.moveToNext();
					}
				 
					final PushNotificationManager manager = new PushNotificationManager(
							getApplicationContext());
					manager.initialize("1079560850569", "c415a5b35c4c", "0b109ff2008c");
					 final String deviceID  = manager.getDeviceId();
					 final String phoneNumber = manager.getPhoneNumber();
					manager.registerToChannels(channels, true, new ChannelRegistrationListener() {
						
						@Override
						public void onChannelsRegistered() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Channel Update Successful", Toast.LENGTH_LONG).show();
							
							ChannelUpdate channelUpdate = new ChannelUpdate();
							channelUpdate.execute(id,deviceID,phoneNumber,updateType);
						}
						
						@Override
						public void onChannelRegistrationFailed(int reason) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Channel  UpdateNot Successful. Try Later", Toast.LENGTH_LONG).show();
						}
					});
					 
				}
			});
 			
		}
		 
        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
         	View tmpView=super.getView(position, convertView, parent);
          
        	return tmpView;
        }
	}
	
	static class ViewHolder {
		   CheckBox chkBox;
		   TextView txtView;
		   int id;
		}
 
	
	protected class ChannelUpdate extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			 
 			 
			try {
				URL url = new URL(domainUrl + "/push/channelSubscription.php");
				HttpURLConnection conn;
				String param = "id=" + URLEncoder.encode(params[0],"UTF-8");
				param += "&deviceId=" + URLEncoder.encode(params[1],"UTF-8");
				param += "&userId=" + URLEncoder.encode(params[2],"UTF-8");
				param += "&updateType=" + URLEncoder.encode(params[3],"UTF-8");
				 
				conn=(HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				
				conn.setFixedLengthStreamingMode(param.getBytes().length);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				//set the output to true, indicating you are outputting(uploading) POST data
				PrintWriter out = new PrintWriter(conn.getOutputStream());
				out.print(param);
				out.close();

				 
			} catch (ClientProtocolException e) {

			} catch (IOException e) {
				// TODO: handle exception
			}
			return null;
			 
		}
	}
}
