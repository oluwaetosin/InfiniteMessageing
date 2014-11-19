package com.example.infinitemessaging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PushRequestActivity extends Activity{
	protected String url;
	protected String request = null;
	protected String mobileNumb = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.send_request);
		url = getString(R.string.domainName) + "/push/engine/enquiryPush/";
		Button sendRequest = (Button) findViewById(R.id.button1);
		final EditText requestText  = (EditText) findViewById(R.id.editText1);
		 SharedPreferences pref = PushRequestActivity.this.getSharedPreferences("com.example.infinitemessaging.myPrefFile",0);
		 mobileNumb = pref.getString("userID", "");
		
		sendRequest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				request = requestText.getText().toString();
				if(request != null){
				String[] parameter = {request,mobileNumb};
				new WebData().execute(parameter);
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.push_request_xml, menu);
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

		case R.id.inbox:
			startActivity(new Intent(getBaseContext(), Inbox.class));
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	 public class WebData extends AsyncTask<String, Void, Void>{
	        
	    	private ProgressDialog pDialog;
			 
		    WebView webview = (WebView) findViewById(R.id.webRequestView);

			@Override
	    	protected void onPreExecute() {
	    		// TODO Auto-generated method stub
	    		super.onPreExecute();
	    		pDialog = new ProgressDialog(PushRequestActivity.this);
				pDialog.setMessage("...Please Wait");
				pDialog.setCancelable(false);
				pDialog.show();
	    	}
	    	  
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				  new ServiceCall().getWebData(url,params[0],params[1]);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				 if (pDialog.isShowing())
		             pDialog.dismiss();
				 Toast.makeText(getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_LONG).show();
			}
	      }
}