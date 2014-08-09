package com.nexters.house.activity;

import java.io.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.entity.Action;
import com.nexters.house.entity.CustomGallery;
import com.nostra13.universalimageloader.cache.disc.impl.*;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;
import com.nostra13.universalimageloader.utils.*;

public class CustomGalleryActivity extends Activity {
	GridView mGridGallery;
	ImageView mImgNoMedia;
	ImageLoader mImageLoader;
	Button mBtnGalleryOk;
	
	GalleryAdapter mGalleryAdapter;
	
	Handler mHandler;
	
	String action;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery);

		action = getIntent().getAction();
		if (action == null) {
			finish();
		}
		initImageLoader();
		init();
	}

	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getBaseContext())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());

			ImageLoaderConfiguration config = builder.build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(config);

		} catch (Exception e) {

		}
	}

	private void init() {
		mHandler = new Handler();
		mGridGallery = (GridView) findViewById(R.id.gridGallery);
		mGridGallery.setFastScrollEnabled(true);
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(), mImageLoader);
		PauseOnScrollListener listener = new PauseOnScrollListener(mImageLoader,
				true, true);
		mGridGallery.setOnScrollListener(listener);

		if (action.equalsIgnoreCase(Action.ACTION_MULTIPLE_PICK)) {
			findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
			mGridGallery.setOnItemClickListener(mItemMulClickListener);
			mGalleryAdapter.setMultiplePick(true);
		} else if (action.equalsIgnoreCase(Action.ACTION_PICK)) {
			findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
			mGridGallery.setOnItemClickListener(mItemSingleClickListener);
			mGalleryAdapter.setMultiplePick(false);
		}
		
		mGalleryAdapter.toggleGallery(true);
		mGridGallery.setAdapter(mGalleryAdapter);
		mImgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

		mBtnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
		mBtnGalleryOk.setOnClickListener(mOkClickListener);

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mGalleryAdapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();

	}

	private void checkImageStatus() {
		if (mGalleryAdapter.isEmpty()) {
			mImgNoMedia.setVisibility(View.VISIBLE);
		} else {
			mImgNoMedia.setVisibility(View.GONE);
		}
	}

	View.OnClickListener mOkClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ArrayList<CustomGallery> selected = mGalleryAdapter.getSelected();
			Log.d("size", "sadfasdlkndsaf : " + selected.size());
			String[] allPath = new String[selected.size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).sdcardPath;
			}

			Intent data = new Intent().putExtra("all_path", allPath);
			setResult(RESULT_OK, data);
			finish();
		}
	};
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {		
			mGalleryAdapter.changeSelection(v, position);
		}
	};

	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			CustomGallery item = mGalleryAdapter.getItem(position);
			Intent data = new Intent().putExtra("single_path", item.sdcardPath);
			setResult(RESULT_OK, data);
			finish();
		}
	};

	private ArrayList<CustomGallery> getGalleryPhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;

			Cursor imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					CustomGallery item = new CustomGallery();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);

					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}

}
