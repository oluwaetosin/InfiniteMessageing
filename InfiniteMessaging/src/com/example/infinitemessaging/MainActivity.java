package com.example.infinitemessaging;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	static Random random = new Random();
	String domainUrl;
	private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STR_LENGTH = 6;
	EditText phoneNumber;
	ImageButton register;
	private static final String randString = getRandomString();
	SharedPreferences sharedpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		domainUrl = getString(R.string.domainName); 
		sharedpreferences = getSharedPreferences(getString(R.string.PREF_NAME),
				Context.MODE_PRIVATE);

		if (sharedpreferences.contains(getString(R.string.FIRST_TIME))) {
			Intent intent = new Intent(MainActivity.this, Inbox.class);
			startActivity(intent);
			finish();
		}

		phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumb);
		register = (ImageButton) findViewById(R.id.imageButton1);
		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phoneNumberValue = phoneNumber.getText().toString();
				if (!validatePhoneNumber(phoneNumberValue)) {
					return;
				};
		
				sendSmsData(phoneNumberValue);
				Intent intent = new Intent(MainActivity.this,
						ConfirmSignup.class);
				intent.putExtra("randStringValue", randString);
				intent.putExtra("phoneNumber", phoneNumberValue);
				startActivityForResult(intent, 20);
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

	public Boolean validatePhoneNumber(String phoneNumber) {
		if (phoneNumber.length() != 13) {
			Toast.makeText(getApplicationContext(),
					"Phone Number Should be Of this format: \n 234**********",
					Toast.LENGTH_LONG).show();
			;
			return false;
		}

		if (!phoneNumber.trim().substring(0, 3).equals("234")) {
			Toast.makeText(getApplicationContext(),
					"Phone Number Should Start With 234 ", Toast.LENGTH_LONG)
					.show();
			return false;
		} else
			return true;
	}

	private static int getRandomNumber() {
		int randomInt = 0;
		randomInt = random.nextInt(_CHAR.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public static String getRandomString() {

		StringBuffer randStr = new StringBuffer();

		for (int i = 0; i < RANDOM_STR_LENGTH; i++) {

			int number = getRandomNumber();
			char ch = _CHAR.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	public void sendSmsData(String phoneNumber) {

		sendSmsData sendSms = new sendSmsData();
		Toast.makeText(getApplicationContext(), domainUrl+"/push/engine/push.php", Toast.LENGTH_LONG).show();
		sendSms.execute(new String[] {
				domainUrl+"/push/engine/push.php", phoneNumber,
				randString });
	}

	private class sendSmsData extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String  response = null;
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn;
				String param = "phone=" + URLEncoder.encode(params[1],"UTF-8")+"&shortCode="+ URLEncoder.encode(params[2],"UTF-8");
				conn=(HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				
				conn.setFixedLengthStreamingMode(param.getBytes().length);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				//set the output to true, indicating you are outputting(uploading) POST data
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
			} catch (ClientProtocolException e) {

			} catch (IOException e) {
				// TODO: handle exception
			}
			return response;
		}

	}

}
