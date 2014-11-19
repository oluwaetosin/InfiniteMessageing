package com.example.infinitemessaging;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class ServiceCall {
	String response = null;
	
	public String getWebData(String Posturl, String request,String mobileNumb){
		  
		try {
			URL url =  new URL(Posturl);
			HttpURLConnection conn;
			String param = "request="+URLEncoder.encode(request,"UTF-8") +
					       "&mobileNumb=" + URLEncoder.encode(mobileNumb,"UTF-8");
			 
			conn=(HttpURLConnection)url.openConnection();
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
		
        
		return response;
	}
	

}
