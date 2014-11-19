package com.example.infinitemessaging;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.infobip.push.PushNotificationManager;
import com.infobip.push.RegistrationData;

public class ConfirmSignup extends Activity {
	String domainUrl;
	String latitude = null;
	String longitude = null;
	LocationHelper location;
	Editor editor;
	EditText hiddenShortcode;
	ImageButton register;
	EditText inputShortcode;
	EditText firstName;
	String firstNameValue;
	String lastNameValue;
	EditText lastName;
	EditText inputPhoneNumber;
	public String phoneNumber;
	public String shortcode = null;
	SharedPreferences sharedPreferences;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		domainUrl = getString(R.string.domainName); 
		final PushNotificationManager manager = new PushNotificationManager(
				this);
		manager.initialize("1079560850569", "c415a5b35c4c", "0b109ff2008c");
		manager.setUserLocation(locationObject());
		hiddenShortcode = (EditText) findViewById(R.id.editTexthiddenShortcode);
		inputShortcode = (EditText) findViewById(R.id.editTextShortCode);
		firstName = (EditText) findViewById(R.id.editTextFirstName);
		lastName = (EditText) findViewById(R.id.editTextLastName);
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
					firstName.setText("");
					lastName.setText("");
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
				latitude, longitude,lastNameValue,firstNameValue });
		
	}

	private void checkMethod(final PushNotificationManager manager) {
		if (manager.isRegistered()) {
			Intent intent = new Intent(getApplicationContext(), Inbox.class);
			inputShortcode.setText("");
			lastName.setText("");
			firstName.setText("");
			Toast.makeText(getApplicationContext(),
					"You are Already Registered", Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	}

	public boolean valiadteInput() {
		firstNameValue = firstName.getText().toString();
	    lastNameValue = lastName.getText().toString();
		String inputShortcodeValue = inputShortcode.getText().toString();
		if (firstNameValue.equals("")) {
			Toast.makeText(getApplicationContext(), "First Name Required",
					Toast.LENGTH_LONG).show();
			return false;
		} 
		else if (lastNameValue.equals("")) {
			Toast.makeText(getApplicationContext(), "Last Name Required",
					Toast.LENGTH_LONG).show();
			return false;
		} 
		else if (inputShortcode.equals("")) {
			Toast.makeText(getApplicationContext(), " Enter Short Code ",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!inputShortcodeValue.trim().equals(
				hiddenShortcode.getText().toString().trim())) {
			Toast.makeText(
					getApplicationContext(),
					" Short Code Invalid ",
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}

	}

	protected class RegisterData extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			   String response = null;
			try {
				 
 
				URL url = new URL(domainUrl+"/push/registeruser.php");
				HttpURLConnection conn;
				String param = "lastName=" + URLEncoder.encode(params[5],"UTF-8") 
						 + "&firstName=" + URLEncoder.encode(params[6], "UTF-8")
				         + "&phone=" + URLEncoder.encode( params[0], "UTF-8")
				         + "&userId=" + URLEncoder.encode(params[1], "UTF-8")
				         + "&deviceId=" + URLEncoder.encode(params[2], "UTF-8")
				         + "&lat=" + URLEncoder.encode(params[3], "UTF-8")
				         + "&lng=" + URLEncoder.encode(params[4], "UTF-8");
				conn = (HttpURLConnection)url.openConnection();
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

	public void getLocation() {
		location = new LocationHelper(ConfirmSignup.this);
		if (location.canGetLocation()) {
			longitude = String.valueOf(location.longitude);
			latitude = String.valueOf(location.latitude);
		}
	}
	
	private Location locationObject(){
		location = new LocationHelper(ConfirmSignup.this);
		if(location.canGetLocation()){
			return location.getLocation();
		}
		else return null;
	}
}
