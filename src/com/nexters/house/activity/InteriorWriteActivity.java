package com.nexters.house.activity;

import java.util.ArrayList;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nexters.house.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class InteriorWriteActivity extends Activity {

	GridView gridGallery;
	
	Handler handler;
	GalleryAdapter adapter;

	ImageView imgSinglePick;
	Button btnGalleryPick;
	Button btnGalleryPickMul;

	String action;
	ViewSwitcher viewSwitcher;
	ImageLoader imageLoader;

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
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	private void init() {

		handler = new Handler();
	//	gridGallery = (GridView) findViewById(R.id.gridGallery);
	//	gridGallery.setFastScrollEnabled(true);
	//	gridGallery.setOnItemClickListener(mItemDeleteListener);
	
		
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setMultiplePick(false);
	//	gridGallery.setAdapter(adapter);

	
		

	
		GalleryAdapter.dataChecked.clear(); //버튼 누를때마다 리스트 초기화 시켜줭
	
		GalleryAdapter.data.clear();
		GalleryAdapter.selectCnt=0; //숫자도 초기화
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		viewSwitcher.setDisplayedChild(1);

		imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

		btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
		btnGalleryPick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_PICK);
				startActivityForResult(i, 100);
			}
		});

		btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
		btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 200);
			}
		});

	}

	AdapterView.OnItemClickListener mItemDeleteListener=new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> I, View v, int position,
				long id) {
/*			adapter.deleteItem(v, position); //클릭한 아이템 지우고
			
			ArrayList<CustomGallery> selected = adapter.getSelected();

			String[] allPath = new String[selected.size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).sdcardPath;
			}

			Intent data = new Intent().putExtra("all_path", allPath);
			onActivityResult(200,Activity.RESULT_OK,data);
		//	setResult(RESULT_OK, data);
		//	finish();
		 * 
		 */
		}

	}; 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			adapter.clear();

			viewSwitcher.setDisplayedChild(1);
			String single_path = data.getStringExtra("single_path");
			imageLoader.displayImage("file://" + single_path, imgSinglePick);

		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
	//		String[] all_path = data.getStringArrayExtra("all_path");


			SliderLayout previewSlider=(SliderLayout)findViewById(R.id.previewSlider);
			String[] allPath = new String[adapter.getSelected().size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = adapter.getSelected().get(i).sdcardPath;
			
			}
			
		for (String url : allPath) {
				TextSliderView textSliderView = new TextSliderView(getApplicationContext());
				// initialize a SliderLayout
				textSliderView.image(url);
				// .setScaleType(BaseSliderView.ScaleType.Fit);
				previewSlider.addSlider(textSliderView);
				
			}
/*<<<<<<< HEAD
			ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

			for (String string : all_path) {
				CustomGallery item = new CustomGallery();
				
				item.sdcardPath = string;

				dataT.add(item);
			}

			viewSwitcher.setDisplayedChild(0);
			adapter.addAll(dataT);
			

*/


			
			
			viewSwitcher.setDisplayedChild(0);
			adapter.isShow = false;
			adapter.notifyDataSetChanged();
//			adapter.addAll(GalleryAdapter.dataChecked);

		}
	}
}
