package com.nexters.house.activity;

import com.nexters.house.R;
import com.nexters.house.R.layout;
import com.nexters.house.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interior_write2);
		Button btnGallery=(Button)findViewById(R.id.btn_gallery);
		TextView txtNoPhoto=(TextView)findViewById(R.id.txt_editPhoto);
		btnGallery.setVisibility(View.GONE);
		txtNoPhoto.setVisibility(View.VISIBLE);
	}

	public void completeWrite(View view) {
		
		
		setResult(RESULT_OK);
		finish();
		Toast.makeText(this, "작성한 내용이 업로드됩니다.", Toast.LENGTH_SHORT).show();
	}

	public void clickedCancel(View view){
		onBackPressed();
	}
	@Override
	public void onBackPressed(){
		 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage("입력을 취소하시겠습니까?").setCancelable(
		        false).setPositiveButton("예",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		        	
		            finish();
		        }
		        }).setNegativeButton("아니요",
		        new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
		            // Action for 'NO' Button
		            dialog.cancel();
		        }
		        });
		    AlertDialog alert = alt_bld.create();
		    // Title for AlertDialog

		    alert.show();

	}

}
