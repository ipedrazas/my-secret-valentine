package com.bsm.mysecretvalentine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsm.mysecretvalentine.om.MessageService;
import com.bsm.mysecretvalentine.util.L;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class WizardActivity extends Activity {
	
	TextView messageText;
	TextView imageText;
	TextView send;
	TextView swoooosh;
	ImageView image_ok;
	ImageView msg_ok;
	ImageView letter;
	ProgressDialog mProgressDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wizard);
        send = (TextView) findViewById(R.id.send_message);
        imageText = (TextView) findViewById(R.id.textImage);
        messageText = (TextView) findViewById(R.id.textMessage);
        swoooosh = (TextView) findViewById(R.id.swoooosh);
        image_ok = (ImageView) findViewById(R.id.confirmation_image);
        msg_ok = (ImageView) findViewById(R.id.confirmation_message);
        letter = (ImageView) findViewById(R.id.letter);
		
		this.initialiseControls();

	}
	
	private void initialiseControls(){
		SharedPreferences preferences = getSharedPreferences("MYSECRETVALENTINE", android.content.Context.MODE_PRIVATE);
		int imageId = preferences.getInt("IMAGE_ID", 0);
		int msgId = preferences.getInt("MSG_ID", 0);
		if(imageId==1){
			send.setText(R.string.almost_there);
			image_ok.setVisibility(ImageView.VISIBLE);
			imageText.setText(R.string.image_selected);
		}
		if(msgId==1){
			send.setText(R.string.almost_there);
			msg_ok.setVisibility(ImageView.VISIBLE);
			messageText.setText(R.string.message_set);
		}
		
		if(msgId==1 && imageId==1){
			image_ok.setVisibility(ImageView.VISIBLE);
			msg_ok.setVisibility(ImageView.VISIBLE);
			messageText.setText(R.string.message_set);
			imageText.setText(R.string.image_selected);
			letter.setImageResource(R.drawable.ic_letter_heart);
			send.setText(R.string.send_button_happy);
			msg_ok.setVisibility(ImageView.VISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		L.d("OnResume!");
		this.initialiseControls();
	}
	
	public void doSelectImage(View v){
		Intent i = new Intent(getApplicationContext(), ImageGalleryActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
	}
	
	public void doWriteMessage(View v){
		Intent i = new Intent(getApplicationContext(), MessageActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
	}
	
	public void doSend(View v){
		SharedPreferences preferences = getSharedPreferences("MYSECRETVALENTINE", android.content.Context.MODE_PRIVATE);
		int imageId = preferences.getInt("IMAGE_ID", 0);
		int msgId = preferences.getInt("MSG_ID", 0);
		if(msgId==1 && imageId==1){
			moveLetterOut();
			send.setVisibility(TextView.GONE);
			swoooosh.setVisibility(TextView.VISIBLE);
			mProgressDialog = new ProgressDialog(WizardActivity.this);
			mProgressDialog.setMessage(getString(R.string.sending));
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			SendMessage sm = new SendMessage();
			sm.execute();
		}
	}
	
	private int[] oldPos = new int[2];
	private void moveLetterOut(){
	    int[] newPos = new int[2];
	    letter.getLocationInWindow(newPos);
	    newPos[0] += letter.getRight() + (letter.getWidth() * 2);
	    TranslateAnimation animation = new TranslateAnimation(
	            Animation.ABSOLUTE, oldPos[0], 
	            Animation.ABSOLUTE, newPos[0], 
	            Animation.ABSOLUTE, oldPos[1],
	            Animation.ABSOLUTE, oldPos[1]);

	    oldPos = newPos;

	    animation.setFillAfter(true);
	    animation.setDuration(800);
	    letter.startAnimation(animation);
	}
	
	private void doThankYou(){
		Thread t = new Thread(){   
            @Override
            public void run() {
                try {
                    synchronized(this){ 
                        wait(500);
                    }
                    Intent i = new Intent(getApplicationContext(), ThankyouActivity.class);
                	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }catch(InterruptedException ex){ 
                    ex.printStackTrace();
                }
            }
    	};
    	t.start();
		
	}
	
	private class SendMessage extends AsyncTask<Void, Void, Boolean> {
		@Override
	    protected Boolean doInBackground(Void...params) {
			return MessageService.send(WizardActivity.this);
				
		}
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        mProgressDialog.show();
		    }

		    @Override
		    protected void onPostExecute(Boolean result){
		    	super.onPostExecute(result);
		    	mProgressDialog.dismiss();
		    	if(result.booleanValue()){
		    		doThankYou();
		    	}else{
		    		initialiseControls();
		    		String msg = getString(R.string.error);
		    		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		    		
		    	}		    	
		    }		
	}


}
