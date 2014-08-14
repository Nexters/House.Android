package com.nexters.house.activity;

import java.util.ArrayList;
import java.util.List;

import com.jess.ui.TwoWayGridView;
import com.nexters.house.R;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.adapter.HorzGridViewAdapter;
import com.nexters.house.adapter.PagerAdapterClass;
import com.nexters.house.entity.Action;
import com.nexters.house.entity.DataObject;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class InteriorWrite2Activity extends Activity {

	EditText interiorInfo;
	private HorzGridViewAdapter mHorzGridViewAdapter;
	private Context mContext;
	public static TwoWayGridView interiorGridView2;
	GalleryAdapter adapter;
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	private static String savedContent = "";
	private static String savedInfo = "";
	ViewSwitcher viewSwitcher;
	ImageLoader imageLoader;

	EditText interiorContent;

	ImageView imgSinglePick;
	Button btnGalleryPick;
	Button btnGalleryPickMul;
	Button previewOk;
	PagerAdapterClass pageradapter;
	private ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interior_write2);
	}
	
	public void initResource(){
		// 입력한 정보 끌어오기
		interiorInfo = (EditText) findViewById(R.id.interior_info); // 인테리어정보
		interiorContent = (EditText) findViewById(R.id.interior_content); // 이야기
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_interior2);
		interiorGridView2 = (TwoWayGridView) findViewById(R.id.interior_gridview_2);
		mContext = getApplicationContext();

		List<DataObject> horzData = generateGridViewObjects();
		mHorzGridViewAdapter = new HorzGridViewAdapter(mContext, horzData, 0);
		interiorGridView2.setAdapter(mHorzGridViewAdapter);
		
		interiorContent.setText(savedContent);
		interiorInfo.setText(savedInfo);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.interior_write2, menu);
		return true;
	}

	public void addImage_interior2(View view) {
		savedContent = interiorContent.getText().toString(); // 저장해놓고
		savedInfo = interiorInfo.getText().toString();
		
		// onBackPressed();
		Intent multiplePickIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(multiplePickIntent, 200);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			adapter.clear();
			adapter.notifyDataSetChanged();
			viewSwitcher.setDisplayedChild(1);
			String single_path = data.getStringExtra("single_path");
			imageLoader.displayImage("file://" + single_path, imgSinglePick);
		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			List<DataObject> horzData = generateGridViewObjects();
			mHorzGridViewAdapter.data = horzData;
			mHorzGridViewAdapter.notifyDataSetChanged();

			viewSwitcher.setDisplayedChild(0);
		}
	}

	private List<DataObject> generateGridViewObjects() {
		List<DataObject> allData = new ArrayList<DataObject>();
		String path;

		for (int i = 0; i < GalleryAdapter.customGalleriesChecked.size(); i++) {
			path = GalleryAdapter.customGalleriesChecked.get(i).sdcardPath;
			DataObject singleObject = new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}
}
