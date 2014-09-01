package com.nexters.house.activity;

import java.io.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.graphics.drawable.StateListDrawable;
import android.os.*;
import android.provider.*;
import android.support.v4.app.NavUtils;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;
import com.nexters.house.utils.*;
import com.nostra13.universalimageloader.cache.disc.impl.*;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;
import com.nostra13.universalimageloader.utils.*;

public class CustomGalleryActivity extends Activity {
	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;

	ViewSwitcher viewSwitcher;
	ImageView imgNoMedia;
	Button btnGalleryOk;

	TextView interiorTxt;
	Button btnCancel;
	String action;
	private ImageLoader imageLoader;
	public static int noCancel=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "/NotoSansKR-Black.otf"); // font from assets:
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
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
	}

	@SuppressWarnings("static-access")
	private void init() {
		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader,
				true, true);
		gridGallery.setOnScrollListener(listener);

//		gridGallery.setSelector(new StateListDrawable());
		if (action.equalsIgnoreCase(Action.ACTION_MULTIPLE_PICK)) {
		//	findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
			gridGallery.setOnItemClickListener(mItemMulClickListener);
			adapter.setMultiplePick(true);
		} else if (action.equalsIgnoreCase(Action.ACTION_PICK)) {
		//	findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
			gridGallery.setOnItemClickListener(mItemSingleClickListener);
			adapter.setMultiplePick(false);
		}

		adapter.isShow = true;
		gridGallery.setAdapter(adapter);
		imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

		viewSwitcher=(ViewSwitcher)findViewById(R.id.switchCancel);
		
		btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
		btnGalleryOk.setOnClickListener(mOkClickListener);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		interiorTxt=(TextView)findViewById(R.id.txt_cameraRoll);
		if(noCancel==1) //write2에서 왔으면
		{
			btnCancel.setVisibility(View.GONE);
			viewSwitcher.showNext();
		}
		btnCancel.setOnClickListener(mCancelClickListener);
		
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();
	}

	private void checkImageStatus() {
		if (adapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}

	// 사진 선택 완료 버튼 누를 시
	View.OnClickListener mOkClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};
	View.OnClickListener mCancelClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			adapter.changeSelection(v, position);
		}
	};

	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			CustomGallery item = adapter.getItem(position);
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

			@SuppressWarnings("deprecation")
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
	@Override
	public void onBackPressed(){ //interiorWrite2Activity도 finish해주어야..
		if(noCancel==1){
			//아무것도 안해.
		}
		else{
		 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage("입력을 취소하시겠습니까?").setCancelable(
		        false).setPositiveButton("예",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		        /*	Intent intent=new Intent(CustomGalleryActivity.this,MainActivity.class);
		    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    		if(InteriorWrite2Activity.fa!=null)
		    			InteriorWrite2Activity.fa.finish();
		    		setResult(RESULT_OK);
		    		finish();
		    		startActivity(intent);*/
		        	finish();
		        	if(InteriorWriteActivity.faIn!=null)
		        	InteriorWriteActivity.faIn.finish(); //뒤 액티비티도 지워.
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

}
