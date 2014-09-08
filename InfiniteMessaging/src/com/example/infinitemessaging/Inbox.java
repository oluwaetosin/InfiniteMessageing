package com.example.infinitemessaging;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Inbox extends ListActivity {

	DBHelper oData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// setContentView(R.layout.listview_activity_main);
		   
		displayMessage();
	}

	private void displayMessage() {
		oData = new DBHelper(this);
		SQLiteDatabase db = oData.getReadableDatabase();
		if(numberOfRows(db) > 0){
		String[] columnNames = {DBConstants.KEY_id, DBConstants.KEY_time, DBConstants.KEY_title,
					DBConstants.KEY_body,DBConstants.KEY_read, BaseColumns._ID };
		Cursor oLoop = db.query("push", columnNames, null, null, null, null,
					DBConstants.KEY_id + " DESC");
		setListAdapter(new MyMessageadapter(getApplicationContext(), R.layout.listview_each_item, oLoop));
 
		}else{
			String[] notification = {"You have no Message"};
			this.setListAdapter(new ArrayAdapter<String>(this, R.layout.listview_each_item, R.id.txtMsg, notification));		
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 
	
	}

 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.chaennels:
			startActivity(new Intent(getBaseContext(), Channels.class));
			return true;

		case R.id.sendRequest:
			startActivity(new Intent(getBaseContext(), RequestActivity.class));
			return true;

		case R.id.request_push:
			Intent pushRequestIntent = new Intent(getApplicationContext(), PushRequestActivity.class);
			startActivity(pushRequestIntent);
			finish();
		   return true;		
			
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	public int numberOfRows(SQLiteDatabase db){
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, DBConstants.TABLE_NAME);
	      return numRows;
	   }
	
	private class MyMessageadapter extends ResourceCursorAdapter{

		public MyMessageadapter(Context context, int layout, Cursor c) {
			super(context, layout, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View v, Context context, Cursor c) {
			// TODO Auto-generated method stub
			TextView title = (TextView)   v.findViewById(R.id.txtTitle);
			TextView time  = (TextView)   v.findViewById(R.id.txtTime);
			TextView msg   =  (TextView)  v.findViewById(R.id.txtMsg);
			ImageView icon = (ImageView)  v.findViewById(R.id.imageView1);
			RelativeLayout messageContainer = (RelativeLayout) v.findViewById(R.id.txtContent);
			
			int id = c.getInt(c.getColumnIndex("_id"));
			String titleValue = c.getString(c.getColumnIndex(DBConstants.KEY_title));
			String timeValue  = c.getString(c.getColumnIndex(DBConstants.KEY_time));
			String msgValue   = c.getString(c.getColumnIndex(DBConstants.KEY_body)); 
			String readValue   = c.getString(c.getColumnIndex(DBConstants.KEY_read)); 
			messageContainer.setTag(id);
			 title.setText(titleValue);
			 time.setText(timeValue);
			 msg.setText(msgValue);
			 if(readValue.equals("1")){
				 icon.setImageResource(R.drawable.ic_action_read);
				 messageContainer.setBackgroundColor(0x99CCFF);                       
			      }
			 if(readValue.equals("0")){
				 icon.setImageResource(R.drawable.ic_action_unread);
                 
			 }
			messageContainer.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					Long id =  Long.valueOf(String.valueOf(v.getTag()));
					  removeItemFromList(id);   
					return true;
				}
			});
			messageContainer.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Long id =  Long.valueOf(String.valueOf(v.getTag()));
					Intent myIntent = new Intent(v.getContext(), Inboxmessage.class);
					Bundle b = new Bundle();
					b.putLong("id", id);
					myIntent.putExtras(b);
					startActivity(myIntent);
					finish();
				}
			});
			 
			
			
			
		}
		 
	}
	  protected void removeItemFromList( Long position) {
		    oData = new DBHelper(this);
			final SQLiteDatabase db = oData.getReadableDatabase();
	        final String[] deletePosition = new String[]{String.valueOf(position)};
	        AlertDialog.Builder alert = new AlertDialog.Builder(
	                Inbox.this);
	   
	        alert.setTitle("Delete");
	        alert.setMessage("Do you want delete this item?");
	        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
		              db.delete(DBConstants.TABLE_NAME, "_id=?", deletePosition);
		              displayMessage();
					  return;
				}
			});
	        
	        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					
				}
			});
	     alert.show();
	    }
}