package com.bsm.mysecretvalentine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


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
	  
	  @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_image_gallery, menu);
			return true;
		}
	  
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	 
	        switch (item.getItemId())
	        {
	        case R.id.menu_gallery:
	        	Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
		    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(i);
		        finish();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
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
	      imageView.setImageResource(ImagesArray.mThumbIds[position]);
	      ((ViewPager) container).addView(imageView, 0);
	      imageView.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	String msg = getString(R.string.image_selected);
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					SharedPreferences sp = getSharedPreferences("MYSECRETVALENTINE", MODE_PRIVATE);
			        SharedPreferences.Editor editor = sp.edit();
			        editor.putInt("IMAGE_ID", ImageGalleryActivity.this.pos);
			        editor.putInt("MSG_IMG", ImagesArray.mThumbIds[ImageGalleryActivity.this.pos]);
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