package com.example.infinitemessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
			String id = params[0];
			String deviceId = params[1];
			String userId = params[2];
			String updateType  = params[3];
			
		
 			HttpClient httpclient = new DefaultHttpClient();
 			HttpPost httpost = new HttpPost(
					"http://www.watershedcorporation.com/push/channelSubscription.php");
			try {
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
						3);
				nameValuePair.add(new BasicNameValuePair("id",id));
				nameValuePair.add(new BasicNameValuePair("deviceId", deviceId));
				nameValuePair.add(new BasicNameValuePair("userId", userId));
				nameValuePair.add(new BasicNameValuePair("updateType", updateType));

				httpost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				httpclient.execute(httpost);
			} catch (ClientProtocolException e) {

			} catch (IOException e) {
				// TODO: handle exception
			}
			return id  + " " + userId + " " + deviceId;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(),  result, Toast.LENGTH_LONG).show();
		}

	}
}
