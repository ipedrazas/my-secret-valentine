package com.bsm.mysecretvalentine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.bsm.mysecretvalentine.util.MCrypt;
import com.bsm.mysecretvalentine.util.SimpleCrypto;

public class MessageActivity extends Activity {

	EditText email;
	EditText msg;
	EditText from;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		email = (EditText) findViewById(R.id.email);
		msg = (EditText) findViewById(R.id.message);
//		from = (EditText) findViewById(R.id.myemail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_message, menu);
		return true;
	}
	
	public void doSaveMessage(View v){
		SharedPreferences sp = getSharedPreferences("MYSECRETVALENTINE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MESSAGE_ID", 1);
        String sEmail = email.getText().toString();
        editor.putString("MSG_TO", sEmail);
//        String sFrom = from.getText().toString();
        String sFrom = getDeviceName(sEmail);
        if(!sFrom.isEmpty())
        	editor.putString("MSG_FROM", sFrom);
        String text = msg.getText().toString();
        if (!text.isEmpty())
        	editor.putString("MSG_TEXT", text);
        
        editor.commit();        
		Intent i = new Intent(getApplicationContext(), WizardActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}

	public String getTelephone(){
		TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();
	    String androidId;
	    sb.append(tm.getDeviceId()).append("#").append(tm.getSimSerialNumber());	   
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    sb.append("#").append(androidId);
	    return sb.toString();
	    
	}
	public String getDeviceName(String email) {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  int sdk = Build.VERSION.SDK_INT;
		  String release = Build.VERSION.RELEASE;	
		  String tel = getTelephone();
		  String res;
		  if (model.startsWith(manufacturer)) {
		    res = capitalize(model) + '#' + sdk + "#" + tel;
		  } else {
		    res = capitalize(manufacturer) + " " + model + '#' + release + '#' + sdk + "#" + tel;
		  }
		  
		  try {
			  MCrypt m = new MCrypt(email,email);
			  String c =  MCrypt.bytesToHex( m.encrypt(res));
			  return c;
		  } catch (Exception e) {
			return res;
		  }
		}
	
	private String capitalize(String s) {
		  if (s == null || s.length() == 0) {
		    return "";
		  }
		  char first = s.charAt(0);
		  if (Character.isUpperCase(first)) {
		    return s;
		  } else {
		    return Character.toUpperCase(first) + s.substring(1);
		  }
		} 
}
