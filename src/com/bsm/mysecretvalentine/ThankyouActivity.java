package com.bsm.mysecretvalentine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ThankyouActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_thankyou);
	}
	
	public void doAgain(View v){
		String msg = getString(R.string.cheeky);
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		SharedPreferences preferences = getSharedPreferences("MYSECRETVALENTINE", android.content.Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        
		Intent i = new Intent(getApplicationContext(), SplashActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
	}

}
