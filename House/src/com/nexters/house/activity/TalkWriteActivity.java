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
import com.nexters.house.entity.CustomGallery;
import com.nexters.house.entity.DataObject;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class TalkWriteActivity extends Activity {
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	public static TwoWayGridView horzGridView;

	private int NUMBER_SOURCE_ITEMS = 500;

	private Context mContext;
	private HorzGridViewAdapter mHorzGridViewAdapter;

	GalleryAdapter mGalleryAdapter;
	ImageView mImgSinglePick;
	Button mBtnGalleryPickMul;

	ViewSwitcher mViewSwitcher;
	ImageLoader mImageLoader;
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_talk_write);

		mContext = getApplicationContext();
		horzGridView = (TwoWayGridView) findViewById(R.id.horz_gridview);
	}

	public void addImage_talk(View view) {
		initImageLoader();
		init();
		Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);
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
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(), mImageLoader);
		mGalleryAdapter.setMultiplePick(false);
		mGalleryAdapter.clear();
		
		horzGridView.clearDisappearingChildren();
		
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_talk);
		mViewSwitcher.setDisplayedChild(1);

		mImgSinglePick = (ImageView) findViewById(R.id.imgSinglePick_talk);

		mBtnGalleryPickMul = (Button) findViewById(R.id.btn_addPhoto);
		mBtnGalleryPickMul.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 200);
			}
		});

	}

	AdapterView.OnItemClickListener mItemDeleteListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> I, View v, int position, long id) {
			/*
			 * adapter.deleteItem(v, position); //클릭한 아이템 지우고
			 * 
			 * ArrayList<CustomGallery> selected = adapter.getSelected();
			 * 
			 * String[] allPath = new String[selected.size()]; for (int i = 0; i
			 * < allPath.length; i++) { allPath[i] = selected.get(i).sdcardPath;
			 * }
			 * 
			 * Intent data = new Intent().putExtra("all_path", allPath);
			 * onActivityResult(200,Activity.RESULT_OK,data); //
			 * setResult(RESULT_OK, data); // finish();
			 */
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			mGalleryAdapter.clear();

			mViewSwitcher.setDisplayedChild(1);
			String single_path = data.getStringExtra("single_path");
			mImageLoader.displayImage("file://" + single_path, mImgSinglePick);
		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			List<DataObject> horzData = generateGridViewObjects();

			if (mHorzGridViewAdapter == null) {
				mHorzGridViewAdapter = new HorzGridViewAdapter(mContext,
						horzData);
				horzGridView.setAdapter(mHorzGridViewAdapter);
			} else
				mHorzGridViewAdapter.data = horzData;
			mHorzGridViewAdapter.notifyDataSetChanged();

			mViewSwitcher.setDisplayedChild(0);
			// adapter.isShow = false;
			// adapter.notifyDataSetChanged();
		}
	}

	private List<DataObject> generateGridViewObjects() {
		List<DataObject> allData = new ArrayList<DataObject>();
		List<CustomGallery> customGalleries = mGalleryAdapter.customGalleriesChecked;
		String path = null;

		for (int i = 0; i < customGalleries.size(); i++) {
			path = customGalleries.get(i).sdcardPath;

			DataObject singleObject = new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}

}
