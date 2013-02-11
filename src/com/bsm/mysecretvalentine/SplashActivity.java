package com.bsm.mysecretvalentine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences preferences = getSharedPreferences("MYSECRETVALENTINE", android.content.Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        
		setContentView(R.layout.activity_splash);
		TextView title = (TextView) findViewById(R.id.fullscreen_content);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/CrayonCrumble.ttf");
		title.setTypeface(font);
		

		// Set up the user interaction to manually show or hide the system UI.
		title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), WizardActivity.class);
		    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(i);
		        finish();
			}
		});


	}

	
}
