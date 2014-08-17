package com.nexters.house.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.jess.ui.TwoWayGridView;
import com.nexters.house.R;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.adapter.HorzGridViewAdapter;
import com.nexters.house.entity.Action;
import com.nexters.house.entity.DataObject;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class TalkWriteActivity extends Activity {
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	public static TwoWayGridView mTalkHorzGridView;

	private Context mContext;
	
	private String action;

	private GalleryAdapter mGalleryAdapter;
	private HorzGridViewAdapter mHorzGridViewAdapter;
	
	private GridView mGridGallery;
	private ImageView mImgSinglePick;

	private ViewSwitcher mViewSwitcher;
	private ImageLoader mImageLoader;

	private Button btnGalleryPick;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_talk_write);
		
		initImageLoader();
		initResource();
		initEvent();
	}

	private void initResource() {
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(),
				mImageLoader);
		mGalleryAdapter.setMultiplePick(false);

		GalleryAdapter.clear(); // 버튼 누를때마다 리스트 초기화 시켜줭 + 숫자도 초기화
		
		mTalkHorzGridView = (TwoWayGridView) findViewById(R.id.horz_gridview);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_talk);
//		mViewSwitcher.setDisplayedChild(1);
		mImgSinglePick = (ImageView) findViewById(R.id.imgSinglePick_talk);
		btnGalleryPick = (Button) findViewById(R.id.btn_gallery);
		
		mContext = getApplicationContext();
		List<DataObject> horzData = new ArrayList<DataObject>();
		mHorzGridViewAdapter = new HorzGridViewAdapter(mContext,
				horzData, 1);
		mTalkHorzGridView.setAdapter(mHorzGridViewAdapter);
//		mTalkHorzGridView.clearDisappearingChildren();
	}

	private void initEvent(){
		btnGalleryPick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent multiplePickIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(multiplePickIntent, 200);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.talk_write, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.completeTalk:
	            completeTalkWrite();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void completeTalkWrite() {
		finish();
		Toast.makeText(this, "작성한 내용이 업로드됩니다.", Toast.LENGTH_SHORT).show();
	}

	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 refreshHorzGrid();
	}
	
	public void refreshHorzGrid(){
		// 이미지 하나도 선택 안할 경우 null 아닐 경우 그 밖
		List<DataObject> horzData = generateGridViewObjects();
		Log.d("dataObject: ", "dataObject:" + horzData);
		
		// mViewSwitcher.setDisplayedChild(1); 이미지 뷰를 부를 때 문제가 생김 뭔진 몰라도..
//		if(horzData == null){
////			mViewSwitcher.setDisplayedChild(1);
//		} else {
			mHorzGridViewAdapter.setHorzData(horzData);
			mHorzGridViewAdapter.notifyDataSetChanged();
			mViewSwitcher.setDisplayedChild(0);
//		}
	}
	
	@Override
	public void onBackPressed(){
		 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage("입력을 취소하시겠습니까 ?").setCancelable(
		        false).setPositiveButton("예",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            finish();
		        }
		        }).setNegativeButton("아니오",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            // Action for 'NO' Button
		            dialog.cancel();
		        }
		        });
		    AlertDialog alert = alt_bld.create();
		    // Title for AlertDialog
		   // alert.setTitle("Title");
		    // Icon for AlertDialog
		   // alert.setIcon(R.drawable.icon);
		    alert.show();
	}

	private List<DataObject> generateGridViewObjects() {
		List<DataObject> allData = new ArrayList<DataObject>();
		String path;

//		Log.d("GalleryAdapter.customGalleriesChecked.size()", "GalleryAdapter.customGalleriesChecked.size() : " + GalleryAdapter.customGalleriesChecked.size());
//		if(GalleryAdapter.customGalleriesChecked.size() <= 0)
//			return null;
		for (int i = 0; i < GalleryAdapter.customGalleriesChecked.size(); i++) {
			path = GalleryAdapter.customGalleriesChecked.get(i).sdcardPath;
			DataObject singleObject = new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}
}
