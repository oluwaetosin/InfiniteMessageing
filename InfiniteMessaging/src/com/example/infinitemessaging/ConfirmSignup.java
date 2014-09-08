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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.infobip.push.PushNotificationManager;
import com.infobip.push.RegistrationData;

public class ConfirmSignup extends Activity {
	String latitude = null;
	String longitude = null;
	LocationHelper location;
	Editor editor;
	EditText hiddenShortcode;
	ImageButton register;
	EditText inputShortcode;
	EditText username;
	EditText inputPhoneNumber;
	public String phoneNumber;
	public String shortcode = null;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		final PushNotificationManager manager = new PushNotificationManager(
				this);
		manager.initialize("1079560850569", "c415a5b35c4c", "0b109ff2008c");
		hiddenShortcode = (EditText) findViewById(R.id.editTexthiddenShortcode);
		inputShortcode = (EditText) findViewById(R.id.editTextShortCode);
		username = (EditText) findViewById(R.id.editTextUsername);
		inputPhoneNumber = (EditText) findViewById(R.id.editTextHiddenPhoneNumber);
		register = (ImageButton) findViewById(R.id.imageButtonRegister);

		Intent intent = getIntent();

		if (intent != null) {
			shortcode = intent.getStringExtra("randStringValue");
			phoneNumber = intent.getStringExtra("phoneNumber");
			inputPhoneNumber.setText(phoneNumber);
			hiddenShortcode.setText(shortcode);
		}

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (valiadteInput()) {
					checkMethod(manager);
					List<String> channels = new ArrayList<String>();
					channels.add("sport");
					channels.add("news");
					channels.add("weather");
					RegistrationData registrationData = new RegistrationData();
					registrationData.setUserId(phoneNumber);
					registrationData.setChannels(channels);
					registrationData.setPhoneNumber(phoneNumber);
					manager.register(registrationData);
					String deviceId = manager.getDeviceId();
					// send registeration data to Url
					sendRegisterationData(phoneNumber, phoneNumber, deviceId);
					username.setText("");
					inputShortcode.setText("");
					
					 
					sharedPreferences = getSharedPreferences(
							getString(R.string.PREF_NAME), Context.MODE_PRIVATE);
					 
						sharedPreferences.edit().putBoolean(getString(R.string.FIRST_TIME), false).commit();
						sharedPreferences.edit().putString("deviceID",
								deviceId).commit();
						sharedPreferences.edit().putString("userID", phoneNumber).commit();
					Intent intent = new Intent(getApplicationContext(),
							Inbox.class);
					finish();
					startActivity(intent);

				}
			}
		});

	}

	private void sendRegisterationData(String phoneNumber, String userId,
			String deviceId) {
		getLocation();
		RegisterData sendValues = new RegisterData();
		sendValues.execute(new String[] { phoneNumber, userId, deviceId,
				latitude, longitude });
	}

	private void checkMethod(final PushNotificationManager manager) {
		if (manager.isRegistered()) {
			Intent intent = new Intent(getApplicationContext(), Inbox.class);
			inputShortcode.setText("");
			username.setText("");
			Toast.makeText(getApplicationContext(),
					"You are Already Registered", Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	}

	public boolean valiadteInput() {
		String userNameValue = username.getText().toString();
		String inputShortcodeValue = inputShortcode.getText().toString();
		if (userNameValue.equals("")) {
			Toast.makeText(getApplicationContext(), "Fill Username",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (inputShortcode.equals("")) {
			Toast.makeText(getApplicationContext(), " Enter Short Code ",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!inputShortcodeValue.trim().equals(
				hiddenShortcode.getText().toString().trim())) {
			Toast.makeText(
					getApplicationContext(),
					" Short Code Invalid "
							+ hiddenShortcode.getText().toString(),
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}

	}

	protected class RegisterData extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			String phoneNumber = params[0];
			String userId = params[1];
			String deviceId = params[2];
			String latitude = params[3];
			String longitude = params[4];
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(
					"http://www.watershedcorporation.com/push/registeruser.php");
			try {
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
						3);
				nameValuePair.add(new BasicNameValuePair("phone", phoneNumber));
				nameValuePair.add(new BasicNameValuePair("userId", userId));
				nameValuePair.add(new BasicNameValuePair("deviceId", deviceId));
				nameValuePair.add(new BasicNameValuePair("lat", latitude));
				nameValuePair.add(new BasicNameValuePair("lng", longitude));
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

	public void getLocation() {
		location = new LocationHelper(ConfirmSignup.this);
		if (location.canGetLocation()) {
			longitude = String.valueOf(location.longitude);
			latitude = String.valueOf(location.latitude);
		}
	}
}
