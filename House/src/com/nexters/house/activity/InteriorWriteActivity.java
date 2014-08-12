package com.nexters.house.activity;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.*;
import com.nexters.house.adapter.PagerAdapterClass;
import com.nexters.house.R;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class InteriorWriteActivity extends Activity {

//	GridView gridGallery;
	
	Handler handler;
	GalleryAdapter adapter;

	PagerAdapterClass pageradapter;
	private ViewPager mPager;
	ImageView imgSinglePick;
	Button btnGalleryPick;
	Button btnGalleryPickMul;

	String action;
	ViewSwitcher viewSwitcher;
	ImageLoader imageLoader;
	

	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		initImageLoader();
		init();
		
		Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);

		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.interior_write, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            moreSelect();
	            return true;
	        case R.id.action_next:
	            openNext();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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

	/*	btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
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
		});*/

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

		
			mPager=(ViewPager)findViewById(R.id.previewPager);
			pageradapter=new PagerAdapterClass(getApplicationContext(), mPager);
	//		mPager.setAdapter(new PagerAdapterClass(getApplicationContext(), mPager));
		
			mPager.setAdapter(pageradapter);
			
			
			
			
					
			viewSwitcher.setDisplayedChild(0);
			adapter.isShow = false;
			adapter.notifyDataSetChanged();
//			adapter.addAll(GalleryAdapter.dataChecked);

		}
	}
	
	private void openNext() {
		
	
		pageradapter.notifyDataSetChanged();
		String allInfo = "";
		for(int j=0;j<pageradapter.InfoList.size();j++){ //스트링 합치기
			allInfo=allInfo.concat(pageradapter.InfoList.get(j));
			allInfo=allInfo.concat(", ");
			
		}
		Intent i =new Intent(this,InteriorWrite2Activity.class);
		i.putExtra("ALLINFO",allInfo);
		startActivity(i);
		
		
	}

	private void moreSelect() {
		Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);
		
	}
}
