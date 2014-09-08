package com.example.infinitemessaging;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class ServiceCall {
	String response = null;
	
	public String getWebData(String url, String request,String mobileNumb){
		   List<NameValuePair> list = new ArrayList<NameValuePair>(2);
		   list.add(new BasicNameValuePair("request",request));
		   list.add(new BasicNameValuePair("mobileNumb",mobileNumb));
		try {
			
			HttpParams params = new BasicHttpParams();
			int timeout       = 10000;
			int socketTimeout = 30000;
			HttpConnectionParams.setConnectionTimeout(params, timeout);
	        HttpConnectionParams.setSoTimeout(params, socketTimeout);		
			
	        DefaultHttpClient httpClient = new DefaultHttpClient(params);
	        HttpEntity httpEntity        = null;
	        HttpResponse httpResponse    = null;
	        
	        HttpPost httppost   = new HttpPost(url);
	        httppost.setEntity(new UrlEncodedFormEntity(list));
			httpResponse   =  httpClient.execute(httppost);
			httpEntity     = httpResponse.getEntity();
			response       = EntityUtils.toString(httpEntity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
        
		return response;
	}
	

}
