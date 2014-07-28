package com.nexters.house.activity;

import android.*;
import android.app.*;
import android.os.*;
import android.view.*;
import com.nexters.house.*;
import com.nexters.house.R;

public class ContentDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_detail_page);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
