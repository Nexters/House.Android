package com.nexters.house.activity;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.*;
import com.nexters.house.R;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.entity.Action;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class InteriorWriteActivity extends Activity {
	GalleryAdapter mGalleryAdapter;

	ImageView mImgSinglePick;
	Button mBtnGalleryPick;
	Button mBtnGalleryPickMul;

	ViewSwitcher mViewSwitcher;
	ImageLoader mImageLoader;

	String action;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

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
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(),
				mImageLoader);
		mGalleryAdapter.setMultiplePick(false);
		mGalleryAdapter.clear();
		
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		mViewSwitcher.setDisplayedChild(1);

		mImgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

		mBtnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
		mBtnGalleryPick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_PICK);
				startActivityForResult(i, 100);
			}
		});

		mBtnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
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
			 * mGalleryAdapter.deleteItem(v, position); //클릭한 아이템 지우고
			 * 
			 * ArrayList<CustomGallery> selected =
			 * mGalleryAdapter.getSelected();
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
			// String[] all_path = data.getStringArrayExtra("all_path");
			SliderLayout previewSlider = (SliderLayout) findViewById(R.id.previewSlider);
			String[] allPath = new String[mGalleryAdapter.getSelected().size()];

			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = mGalleryAdapter.getSelected().get(i).sdcardPath;
			}
			for (String url : allPath) {
				TextSliderView textSliderView = new TextSliderView(
						getApplicationContext());
				// initialize a SliderLayout
				textSliderView.image(url);
				// .setScaleType(BaseSliderView.ScaleType.Fit);
				previewSlider.addSlider(textSliderView);

			}
			mViewSwitcher.setDisplayedChild(0);
			mGalleryAdapter.toggleGallery(false);
			mGalleryAdapter.notifyDataSetChanged();
			// mGalleryAdapter.addAll(GalleryAdapter.dataChecked);

		}
	}
}
