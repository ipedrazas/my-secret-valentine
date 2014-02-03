package com.bsm.mysecretvalentine.om;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bsm.mysecretvalentine.ImagesArray;
import com.bsm.mysecretvalentine.util.ConnectionException;
import com.bsm.mysecretvalentine.util.CustomHttpClient;
import com.bsm.mysecretvalentine.util.L;
import com.bsm.mysecretvalentine.util.NotAuthorisedException;
import com.bsm.mysecretvalentine.util.Preferences;

public class MessageService {
	
	public static boolean sendMessage(Message msg){
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_token", Preferences.ACCESS_TOKEN);
		map.put("transaction", msg.getFrom());
		map.put("to", msg.getTo());
		map.put("message", msg.getText());
		map.put("url", msg.getImage());
		map.put("from", Preferences.FROM);
		boolean ok = false;
		try {
			String res = CustomHttpClient.doPost(map, Preferences.URL_SEND);
			L.d(res);
			JSONObject JResponse = new JSONObject(res);
			String new_id = JResponse.getString("id");
			
			map.clear();
			map.put("app_token", Preferences.ACCESS_TOKEN);
			map.put("transaction", msg.getFrom());
			map.put("id", new_id);
			res = CustomHttpClient.doPost(map, Preferences.URL_PUSH);
			ok = true;
			
		} catch (NotAuthorisedException e) {
			e.printStackTrace();
			Log.d("MessageService", map.toString());
		} catch (ConnectionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ok;
	}
	
	public static boolean sendMessage(String from, String to, String text, String image){
		Message m = new Message();
		m.setFrom(from);
		m.setImage(image);
		m.setText(text);
		m.setTo(to);
		return sendMessage(m);
	}
	
	public static boolean send(Context ctx){
		SharedPreferences p = ctx.getSharedPreferences("MYSECRETVALENTINE", android.content.Context.MODE_PRIVATE);
		String from = p.getString("MSG_FROM", Preferences.FROM);
		String to = p.getString("MSG_TO", Preferences.TO);
		String text = p.getString("MSG_TEXT", Preferences.TEXT);
		String img = ImagesArray.mURLSs[0];
		if(p.contains("IMG_URL")){
			img = p.getString("IMG_URL", "");
		}
		return sendMessage(from, to, text, img);
		
	}

}
