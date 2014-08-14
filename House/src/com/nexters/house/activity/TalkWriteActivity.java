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
	private HorzGridViewAdapter horzGridViewAdapter;
	private Context mContext;
	public static TwoWayGridView horzGridView;
	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	
	
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
		setContentView(R.layout.activity_talk_write);

		mContext=getApplicationContext();
		horzGridView 
		= (TwoWayGridView) findViewById(R.id.horz_gridview);


	

	}

	public void addImage_talk(View view){
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
	//	gridGallery = (GridView) findViewById(R.id.gridGallery_talk);
	//	gridGallery.setFastScrollEnabled(true);
	//	gridGallery.setOnItemClickListener(mItemDeleteListener);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setMultiplePick(false);
	//	gridGallery.setAdapter(adapter);

		GalleryAdapter.clear(); //버튼 누를때마다 리스트 초기화 시켜줭
		
		GalleryAdapter.selectCnt=0; //숫자도 초기화
		horzGridView.clearDisappearingChildren();
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_talk);
		viewSwitcher.setDisplayedChild(1);

		imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick_talk);
/*
		btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
		btnGalleryPick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Action.ACTION_PICK);
				startActivityForResult(i, 100);

			}
		});*/

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

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			adapter.clear();
			adapter.notifyDataSetChanged();
			
			viewSwitcher.setDisplayedChild(1);
			String single_path = data.getStringExtra("single_path");
			imageLoader.displayImage("file://" + single_path, imgSinglePick);

		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
//			String[] all_path = data.getStringArrayExtra("all_path");

	
			List<DataObject> horzData = generateGridViewObjects();


			if(horzGridViewAdapter==null){
			
				horzGridViewAdapter = new HorzGridViewAdapter(mContext,horzData,1);
				horzGridView.setAdapter(horzGridViewAdapter);

			}else
				horzGridViewAdapter.data=horzData;
			horzGridViewAdapter.notifyDataSetChanged();
				
			viewSwitcher.setDisplayedChild(0);
//			adapter.isShow = false;
//			adapter.notifyDataSetChanged();		
			}
	}


	private List<DataObject> generateGridViewObjects(){
		List<DataObject> allData=new ArrayList<DataObject>();

		String path;
		
		for(int i=0;i<GalleryAdapter.customGalleriesChecked.size();i++){
			path=GalleryAdapter.customGalleriesChecked.get(i).sdcardPath;
			
			DataObject singleObject= new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}

}
