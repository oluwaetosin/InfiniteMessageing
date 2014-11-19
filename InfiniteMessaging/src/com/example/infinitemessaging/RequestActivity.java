package com.example.infinitemessaging;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

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
import android.widget.Toast;

public class RequestActivity extends Activity{
	
	protected String url;
	protected String request = null;
    private static String TAG_CONTENT_VALUE = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		url = getString(R.string.domainName) + "/push/engine/enquiry/index.php";
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
			String response = "";
			// TODO Auto-generated method stub
			try {
				URL urlPost =  new URL(url);
				HttpURLConnection conn;
				String param = "request="+URLEncoder.encode(request,"UTF-8");
						      // + "&mobileNumb=" + URLEncoder.encode(null,"UTF-8");
				 
				conn=(HttpURLConnection)urlPost.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				
				conn.setFixedLengthStreamingMode(param.getBytes().length);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				PrintWriter out = new PrintWriter(conn.getOutputStream());
				out.print(param);
				out.close();
				
				//start listening to the stream
				Scanner inStream = new Scanner(conn.getInputStream());

				//process the stream and store it in StringBuilder
				while(inStream.hasNextLine()){
				response +=(inStream.nextLine());
				}
				inStream.close();		 
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
	        
			jsonString  = response;
		 
			 // jsonString = new ServiceCall().getWebData(url,params[0],null);
			
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
			    	 
			    	try {
						JSONObject jsonObject = new JSONObject(jsonString);
					    TAG_CONTENT_VALUE = 	jsonObject.getString("content");
						 Log.d("d",TAG_CONTENT_VALUE);
					} catch (JSONException e) {
						 
						e.printStackTrace();
					}
			    
			    	webview.loadData(TAG_CONTENT_VALUE,"text/html", "utf-8");
			     
			    	
			    }else{
			    	Toast.makeText(getApplicationContext(),"Error Completing Request", Toast.LENGTH_LONG).show();
			    	 
			    	Log.d("status","null");
			    }
		}
      }
}
