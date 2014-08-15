package com.nexters.house.activity;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.jess.ui.*;
import com.nexters.house.R;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.Action;
import com.nexters.house.entity.DataObject;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class TalkWriteActivity extends Activity {
	private Context mContext;

	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;

	public static TwoWayGridView mHorzGridView;

	private String action;

	private HorzGridViewAdapter mHorzGridViewAdapter;
	private GalleryAdapter mGalleryAdapter;

	private GridView mGridGallery;
	private Handler mHandler;

	private ImageView mImgSinglePick;
	private ViewSwitcher mViewSwitcher;
	private ImageLoader mImageLoader;

	private Button btnGalleryPick;
	private Button btnGalleryPickMul;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_talk_write);
		
		initResource();
	}

	private void initResource() {
		mContext = getApplicationContext();
		mHorzGridView = (TwoWayGridView) findViewById(R.id.horz_gridview);
		
		List<DataObject> horzData = new ArrayList<DataObject>();
		mHorzGridViewAdapter = new HorzGridViewAdapter(mContext,
				horzData, 1);
		mHorzGridView.setAdapter(mHorzGridViewAdapter);
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
	    // Handle presses on the action bar items
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

	public void addImage_talk(View view) {
		initImageLoader();
		init();
		Intent multiplePickIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(multiplePickIntent, 200);
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

	private void init() {
		mHandler = new Handler();
		// gridGallery = (GridView) findViewById(R.id.gridGallery_talk);
		// gridGallery.setFastScrollEnabled(true);
		// gridGallery.setOnItemClickListener(mItemDeleteListener);
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(),
				mImageLoader);
		mGalleryAdapter.setMultiplePick(false);
		// gridGallery.setAdapter(adapter);

		GalleryAdapter.clear(); // 버튼 누를때마다 리스트 초기화 시켜줭

		GalleryAdapter.selectCnt = 0; // 숫자도 초기화
		mHorzGridView.clearDisappearingChildren();
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_talk);
		mViewSwitcher.setDisplayedChild(1);

		mImgSinglePick = (ImageView) findViewById(R.id.imgSinglePick_talk);
		/*
		 * btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
		 * btnGalleryPick.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent i = new Intent(Action.ACTION_PICK); startActivityForResult(i,
		 * 100);
		 * 
		 * } });
		 */

		btnGalleryPickMul = (Button) findViewById(R.id.btn_addPhoto);
		btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 200);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
//			mViewSwitcher.setDisplayedChild(1);
////			String single_path = data.getStringExtra("single_path");
////			imageLoader.displayImage("file://" + single_path, imgSinglePick);
//		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			List<DataObject> horzData = generateGridViewObjects();

//			if (mHorzGridViewAdapter == null) {
//		
//			} else
				mHorzGridViewAdapter.setHorzData(horzData);
			mHorzGridViewAdapter.notifyDataSetChanged();

			mViewSwitcher.setDisplayedChild(0);
//		}
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// refreshHorzGrid();
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
