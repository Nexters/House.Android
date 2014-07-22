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
import android.widget.ImageView;

public class InteriorWriteActivity extends Activity {

	int REQUEST_IMAGE = 002;
	ImageView imgv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interior_write);
		imgv=(ImageView)findViewById(R.id.imgv);


		Intent intent = new Intent(Intent.ACTION_PICK, 

				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				InteriorWriteActivity.this.startActivityForResult(intent, REQUEST_IMAGE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.interior_write, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		 // TODO Auto-generated method stub

		 super.onActivityResult(requestCode, resultCode, data);

		 

		 if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null)

		 {

		  final Uri selectImageUri = data.getData();

		  final String[] filePathColumn = {MediaStore.Images.Media.DATA};

		  final Cursor imageCursor = this.getContentResolver().query(selectImageUri, filePathColumn, null, null, null);

		  imageCursor.moveToFirst();

		    

		  final int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);

		  final String imagePath = imageCursor.getString(columnIndex);

		  imageCursor.close();

		  

		  final Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

	//	  imgv.setImageBitmap(bitmap);

		 }

		 

		}


}
