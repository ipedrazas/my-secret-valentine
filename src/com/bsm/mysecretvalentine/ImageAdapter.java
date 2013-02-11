package com.bsm.mysecretvalentine;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
 
	public float px;
	
	private void initScreenSizeElements(){
		Resources r = Resources.getSystem();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());
        
	}
	
 
    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
        initScreenSizeElements();
    }
 
    @Override
    public int getCount() {
        return ImagesArray.mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        return ImagesArray.mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView;
    	if (convertView == null) {
	        imageView = new ImageView(mContext);
	        imageView.setImageResource(ImagesArray.mThumbIds[position]);
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setAdjustViewBounds(true);
	        imageView.setTag(Integer.toString(position));
    	}else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }
 
}
