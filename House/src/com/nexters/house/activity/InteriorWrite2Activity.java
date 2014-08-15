package com.nexters.house.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.jess.ui.TwoWayGridView;
import com.nexters.house.R;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.adapter.HorzGridViewAdapter;
import com.nexters.house.adapter.PagerAdapterClass;
import com.nexters.house.entity.Action;
import com.nexters.house.entity.DataObject;
import com.nostra13.universalimageloader.core.ImageLoader;

public class InteriorWrite2Activity extends Activity {
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	private static String savedContent = "";
	private static String savedInfo = "";
	public static TwoWayGridView mInteriorGridView2;

	private Context mContext;

	private EditText mInteriorContent;
	private EditText mInteriorInfo;

	private GalleryAdapter mGalleryAdapter;
	private HorzGridViewAdapter mHorzGridViewAdapter;
	private PagerAdapterClass mPagerAdapterClass;
	private ViewPager mViewPager;

	private ViewSwitcher mViewSwitcher;
	private ImageLoader mImageLoader;

	private ImageView mImgSinglePick;
	private Button btnGalleryPick;
	private Button btnGalleryPickMul;
	private Button previewOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interior_write2);

		initResource();
	}

	private void initResource() {
		// 입력한 정보 끌어오기
		mInteriorInfo = (EditText) findViewById(R.id.interior_info); // 인테리어정보
		mInteriorContent = (EditText) findViewById(R.id.interior_content); // 이야기
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_interior2);
		mInteriorGridView2 = (TwoWayGridView) findViewById(R.id.interior_gridview_2);
		mContext = getApplicationContext();

		List<DataObject> horzData = generateGridViewObjects();
		mHorzGridViewAdapter = new HorzGridViewAdapter(mContext, horzData, 0);
		mInteriorGridView2.setAdapter(mHorzGridViewAdapter);

		mInteriorContent.setText(savedContent);
		mInteriorInfo.setText(savedInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.interior_write2, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.complete:
	            completeWrite();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	private void completeWrite() {
		finish();
		Toast.makeText(this, "작성한 내용이 업로드됩니다.", Toast.LENGTH_SHORT).show();
	
		
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
		    alert.setTitle("Title");
		    // Icon for AlertDialog
		    alert.setIcon(R.drawable.icon);
		    alert.show();

	}

	public void addImage_interior2(View view) {
		savedContent = mInteriorContent.getText().toString(); // 저장해놓고
		savedInfo = mInteriorInfo.getText().toString();

		// onBackPressed();
		Intent multiplePickIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(multiplePickIntent, 200);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		refreshHorzGrid();
	}

	public void refreshHorzGrid(){
		// 이미지 하나도 선택 안할 경우 null 아닐 경우 그 밖
		List<DataObject> horzData = generateGridViewObjects();
		if(horzData == null){
			mViewSwitcher.setDisplayedChild(1);
		} else {
			mHorzGridViewAdapter.setHorzData(horzData);
			mHorzGridViewAdapter.notifyDataSetChanged();
			mViewSwitcher.setDisplayedChild(0);
		}
	}
	
	private List<DataObject> generateGridViewObjects() {
		List<DataObject> allData = new ArrayList<DataObject>();
		String path;

		if(GalleryAdapter.customGalleriesChecked.size() <= 0)
			return null;
		for (int i = 0; i < GalleryAdapter.customGalleriesChecked.size(); i++) {
			path = GalleryAdapter.customGalleriesChecked.get(i).sdcardPath;
			DataObject singleObject = new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}
}
