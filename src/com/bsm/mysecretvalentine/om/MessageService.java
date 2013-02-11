package com.bsm.mysecretvalentine.om;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.bsm.mysecretvalentine.util.ConnectionException;
import com.bsm.mysecretvalentine.util.CustomHttpClient;
import com.bsm.mysecretvalentine.util.L;
import com.bsm.mysecretvalentine.util.NotAuthorisedException;
import com.bsm.mysecretvalentine.util.Preferences;

public class MessageService {
	
	public static void sendMessage(Message msg){
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_token", Preferences.ACCESS_TOKEN);
		map.put("transaction", msg.getFrom());
		map.put("to", msg.getTo());
		map.put("message", msg.getText());
		map.put("url", msg.getImage());
		map.put("from", Preferences.FROM);
		
		try {
			String res = CustomHttpClient.doPost(map, Preferences.URL_SEND);
			L.d(res);
		} catch (NotAuthorisedException e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String from, String to, String text, String image){
		Message m = new Message();
		m.setFrom(from);
		m.setImage(image);
		m.setText(text);
		m.setTo(to);
		sendMessage(m);
	}
	
	public static void send(Context ctx){
		SharedPreferences p = ctx.getSharedPreferences("MYSECRETVALENTINE", android.content.Context.MODE_PRIVATE);
		String from = p.getString("MSG_FROM", Preferences.FROM);
		String to = p.getString("MSG_TO", Preferences.TO);
		String text = p.getString("MSG_TEXT", Preferences.TEXT);
		String img;
		if(p.contains("IMG_URL")){
			img = p.getString("IMG_URL", "");
		}else{			
			int image = p.getInt("MSG_IMG", 0);
			img = Integer.toString(image);
		}
		sendMessage(from, to, text, img);
		
	}

}
