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

import android.os.AsyncTask;
 
	 
 public class sendUpdates extends AsyncTask<String, Void, String> {
	 @Override
	protected String doInBackground(String... params) {
		
		String userId = params[0];
		String deviceId = params[1];
		String messageId = params[2];
		String status = params[3];
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(
				"http://www.watershedcorporation.com/push/receiveUpdates.php");
		try {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
					3);
			nameValuePair.add(new BasicNameValuePair("userID", userId));
			nameValuePair.add(new BasicNameValuePair("deviceID", deviceId));
			nameValuePair.add(new BasicNameValuePair("messageID", messageId));
			nameValuePair.add(new BasicNameValuePair("status", status));
			 
			httpost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			httpclient.execute(httpost);
		} catch (ClientProtocolException e) {

		} catch (IOException e) {
			// TODO: handle exception
		}
		return null;
	}
	
 }
 
