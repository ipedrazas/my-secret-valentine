package com.bsm.mysecretvalentine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GridActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		GridView gridView = (GridView) findViewById(R.id.grid_view);
		 
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), ImageGalleryActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });
        gridView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), ImageGalleryActivity.class);
                // passing array index
                i.putExtra("id", (Integer) v.getTag());
                startActivity(i);
                return true;
            }
        });
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_grid, menu);
		return true;
	}

}
