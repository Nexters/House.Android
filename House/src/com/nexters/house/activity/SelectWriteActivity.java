package com.nexters.house.activity;


import com.nexters.house.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;

public class SelectWriteActivity extends Activity {
	//private static final int SELECT_PICTURE = 15;
    //private String selectedImagePath;
    //ADDED
    //private String filemanagerstring;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_write);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_write, menu);
		return true;
	}
	
	public void talkWrite(View view){
		Intent intent=new Intent(this,TalkWriteActivity.class);
		startActivity(intent);
	}
	public void interiorWrite(View view){
		Intent intent=new Intent(this,InteriorWriteActivity.class);
		startActivity(intent);

	}



}
