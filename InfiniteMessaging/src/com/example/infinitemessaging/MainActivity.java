package com.example.infinitemessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
		sendSms.execute(new String[] {
				"http://www.watershedcorporation.com/push/engine/push.php", phoneNumber,
				randString });
	}

	private class sendSmsData extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			String phoneNumber = params[1];
			String shortcode = params[2];
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(url);
			try {
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
						2);
				nameValuePair.add(new BasicNameValuePair("phone", phoneNumber));
				nameValuePair
						.add(new BasicNameValuePair("shortCode", shortcode));
				httpost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				httpclient.execute(httpost);
			} catch (ClientProtocolException e) {

			} catch (IOException e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}

	}

}
