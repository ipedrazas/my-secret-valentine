package com.bsm.mysecretvalentine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bsm.mysecretvalentine.util.L;

public class GalleryActivity extends Activity {

	private static final int PICK_IMAGE = 1;

	private ImageView imgView;
	private Bitmap bitmap;
	private Button upload;
	private ProgressDialog dialog;
	private String filePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		imgView = (ImageView) findViewById(R.id.ImageView);
		
		if(imgView.getDrawable()==null){
			try {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		upload = (Button) findViewById(R.id.Upload);
        upload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please select image", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(GalleryActivity.this, "Uploading","Please wait...", true);
                    new ImageUploadTask().execute();
                }
            }
        });
		
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
	        case PICK_IMAGE:
	            if (resultCode == Activity.RESULT_OK) {
	                Uri selectedImageUri = data.getData();
	                String filePath = null;

	                try {
	                    // OI FILE Manager
	                    String filemanagerstring = selectedImageUri.getPath();

	                    // MEDIA GALLERY
	                    String selectedImagePath = getPath(selectedImageUri);

	                    if (selectedImagePath != null) {
	                        filePath = selectedImagePath;
	                    } else if (filemanagerstring != null) {
	                        filePath = filemanagerstring;
	                    } else {
	                        Toast.makeText(getApplicationContext(), "Unknown path", Toast.LENGTH_LONG).show();
	                        Log.e("Bitmap", "Unknown path");
	                    }

	                    if (filePath != null) {
	                    	L.d(filePath);
	                    	this.filePath = filePath;
	                        decodeFile(filePath);
	                    } else {
	                        bitmap = null;
	                    }
	                } catch (Exception e) {
	                    Toast.makeText(getApplicationContext(), "Internal error",
	                            Toast.LENGTH_LONG).show();
	                    Log.e(e.getClass().getName(), e.getMessage(), e);
	                }
	            }
	            break;
	        default:
	        }
	    }
	 
	 public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        @SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, projection, null, null, null);
	        if (cursor != null) {
	            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	            int column_index = cursor
	                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            cursor.moveToFirst();
	            return cursor.getString(column_index);
	        } else
	            return null;
	    }

	 public void decodeFile(String filePath) {
	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, o);

	        // The new size we want to scale to
	        final int REQUIRED_SIZE = 1024;

	        // Find the correct scale value. It should be the power of 2.
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
	                break;
	            width_tmp /= 2;
	            height_tmp /= 2;
	            scale *= 2;
	        }

	        // Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        bitmap = BitmapFactory.decodeFile(filePath, o2);

	        imgView.setImageBitmap(bitmap);

	    }
	 
	
	class ImageUploadTask extends AsyncTask <Void, Void, String>{
        @Override
        protected String doInBackground(Void... unsued) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                String uploadURL = "http://api.oioi.me/image/upload";
                HttpPost httpPost = new HttpPost(uploadURL);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, bos);
                entity.addPart("u", new StringBody("bsm"));
                entity.addPart("app-token", new StringBody("12024ead2c267bfe46fab73e571b4325e1dc1a8a"));
                entity.addPart("file", new FileBody(new File(GalleryActivity.this.filePath)));
                entity.addPart("blibb_id", new StringBody("51182565a564de3d8c6036d4"));
                
                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost, localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader( response.getEntity().getContent(), "UTF-8"));
                
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine())!=null){
	                  sb.append(line);
	            }                
                return sb.toString();
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                e.printStackTrace();
                return null;
            }

            // (null);
        }

        @Override
        protected void onProgressUpdate(Void... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                if (sResponse != null) {
                	L.d(sResponse);
                    JSONObject JResponse = new JSONObject(sResponse);
                    String message = JResponse.getString("upload");
                    SharedPreferences sp = getSharedPreferences("MYSECRETVALENTINE", MODE_PRIVATE);
			        SharedPreferences.Editor editor = sp.edit();
                    editor.putString("IMG_URL", message);
                    editor.putInt("IMAGE_ID", 1);
                    editor.putInt("STATUS", 1);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), WizardActivity.class);
			    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
