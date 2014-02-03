package com.bsm.mysecretvalentine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bsm.mysecretvalentine.util.BitmapUtils;
import com.bsm.mysecretvalentine.util.L;


public class ImageGalleryActivity extends Activity {
	int pos;
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.full_image_gallery);

	    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
	    ImagePagerAdapter adapter = new ImagePagerAdapter();
	    viewPager.setAdapter(adapter);
	  }
	  public void openGallery(View v){
		  Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(i);
	        finish();
	  }
	  
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindDrawables(findViewById(R.id.view_pager));
		System.gc();
	}
	
	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}
	    
	    
	  private class ImagePagerAdapter extends PagerAdapter {

	    @Override
	    public int getCount() {
	      return ImagesArray.mThumbIds.length;
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	      return view == ((ImageView) object);
	    }

	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {	    
	      Context context = ImageGalleryActivity.this;
	      ImageGalleryActivity.this.pos = position;
	      ImageView imageView = new ImageView(context);
	      int padding = context.getResources().getDimensionPixelSize(
	          R.dimen.padding_medium);
	      imageView.setPadding(padding, padding, padding, padding);
	      imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//	      imageView.setImageResource(ImagesArray.mThumbIds[position]);
	      imageView.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), ImagesArray.mThumbIds[position], 400, 400));
	      ((ViewPager) container).addView(imageView, 0);
	     
	      imageView.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	String msg = getString(R.string.image_selected);
	    			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	    			SharedPreferences sp = getSharedPreferences("MYSECRETVALENTINE", MODE_PRIVATE);
	    	        SharedPreferences.Editor editor = sp.edit();
	    	        editor.putInt("IMAGE_ID", 1);
	    	        L.d("Position: " + ImageGalleryActivity.this.pos);
	    	        int pos = ImageGalleryActivity.this.pos - 1;
	    	        editor.putInt("MSG_IMG", ImagesArray.mThumbIds[pos]);
	    	        editor.putString("IMG_URL", ImagesArray.mURLSs[pos]);
	    	        L.d("Image: " + ImagesArray.mURLSs[pos]);
	    	        editor.commit();  
	    			Intent i = new Intent(getApplicationContext(), WizardActivity.class);
	    	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	        startActivity(i);
	            }
	      }); 
	      
	      return imageView;
	    }

	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	      ((ViewPager) container).removeView((ImageView) object);
	    }
	    
	    
	    
	  }
	}