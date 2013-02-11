package com.bsm.mysecretvalentine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.res.Resources.NotFoundException;
import android.os.StrictMode;
import android.util.Log;

public class CustomHttpClient {

    public static final int CONNECTION_TIMEOUT = 3 * 1000; // milliseconds
    public static final int SOCKET_TIMEOUT = 5 * 1000; // milliseconds
	private static final String DEBUG_TAG = "CustomHttpClient";
    
    public static HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	         // Set the timeout in milliseconds until a connection is established.
	         // The default value is zero, that means the timeout is not used. 
	         int timeoutConnection = CONNECTION_TIMEOUT;
	         HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
	         // Set the default socket timeout (SO_TIMEOUT) 
	         // in milliseconds which is the timeout for waiting for data.
	         int timeoutSocket = SOCKET_TIMEOUT;
	         HttpConnectionParams.setSoTimeout(params, timeoutSocket);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
    
  
    private static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws NotAuthorisedException, ConnectionException {
    	Log.d(DEBUG_TAG, "executeHttpPost");
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();            
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
           if(status==401){
            	throw new NotAuthorisedException();
            }
           in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

           StringBuffer sb = new StringBuffer("");
           String line = "";
           String NL = System.getProperty("line.separator");
           while ((line = in.readLine()) != null) {
               sb.append(line + NL);
           }

			in.close();
			String result = sb.toString();
	        return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionException();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String executeHttpGet(String url) throws NotFoundException, ConnectionException {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            Integer i =response.getStatusLine().getStatusCode();
            if (i==404){
            	throw new NotFoundException(url + " not found");
            }else{
            	in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                String result = sb.toString();
                return result;
            }
        } catch (URISyntaxException e) {
			throw new ConnectionException();
		} catch (IOException e) {
			throw new ConnectionException();
		} finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                	throw new ConnectionException();
                }
            }
        }
    }
    
    public static String doGet(String url) throws NotFoundException, ConnectionException {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		return CustomHttpClient.executeHttpGet(url);
	}
	
	public static String doPost(Map<String, String> params, String url) throws NotAuthorisedException, ConnectionException{
		Log.d(DEBUG_TAG, "doPost");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for(String key : params.keySet()){
			postParameters.add(new BasicNameValuePair(key , params.get(key)));
		}
		return CustomHttpClient.executeHttpPost(url, postParameters);

	}
}
