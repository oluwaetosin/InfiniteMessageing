package com.example.infinitemessaging;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class RequestActivity extends Activity{
	
	protected String url = "http://www.watershedcorporation.com/push/engine/enquiry/index.php";
	protected String request = null;
    private static String TAG_CONTENT_VALUE = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		Button sendRequest = (Button) findViewById(R.id.buttonSend);
		final EditText requestText  = (EditText) findViewById(R.id.EdtRequest);
		
		sendRequest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				request  = requestText.getText().toString();
				// TODO Auto-generated method stub
				String[] parameter = {request};
				new WebData().execute(parameter);
			   
				
			}
		});
		
	}
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	// TODO Auto-generated method stub
	    	 getMenuInflater().inflate(R.menu.send_request, menu);
	    		return true;
	     } 
	     
	    public boolean onOptionsItemSelected(MenuItem Item){
			switch(Item.getItemId()){
			case R.id.inbox:
				Intent intent = new Intent(getApplicationContext(), Inbox.class);
				startActivity(intent);
				finish();
			    break;
			case R.id.request_push:
				Intent pushRequestIntent = new Intent(getApplicationContext(), PushRequestActivity.class);
				startActivity(pushRequestIntent);
				finish();
			    break;
			  default:
				  return super.onOptionsItemSelected(Item);
			}	
	    	return true;
	    		 
	    	 }
	    
      public class WebData extends AsyncTask<String, Void, Void>{
        
    	private ProgressDialog pDialog;
		private String jsonString = null;
	    WebView webview = (WebView) findViewById(R.id.webRequestView);

		@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		pDialog = new ProgressDialog(RequestActivity.this);
			pDialog.setMessage("...Please Wait");
			pDialog.setCancelable(false);
			pDialog.show();
    	}
    	  
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			  jsonString = new ServiceCall().getWebData(url,params[0],null);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			 if (pDialog.isShowing())
	             pDialog.dismiss();
			 final EditText requestText  = (EditText) findViewById(R.id.EdtRequest);
			    if(jsonString != null){
			    	Log.d("status",jsonString);
			    	try {
						JSONObject jsonObject = new JSONObject(jsonString);
					    TAG_CONTENT_VALUE = 	jsonObject.getString("content");
						 
					} catch (JSONException e) {
						 
						e.printStackTrace();
					}
			    
			    	webview.loadData(TAG_CONTENT_VALUE,"text/html", "utf-8");
			    	
			    }else{
			    	requestText.setText(jsonString + " cant find data ");
			    	Log.d("status","null");
			    }
		}
      }
}
