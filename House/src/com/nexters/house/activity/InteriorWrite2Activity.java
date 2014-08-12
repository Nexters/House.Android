package com.nexters.house.activity;

import java.util.ArrayList;
import java.util.List;

import com.jess.ui.TwoWayGridView;
import com.nexters.house.R;
import com.nexters.house.adapter.HorzGridViewAdapter;
import com.nexters.house.adapter.PagerAdapterClass;
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
	private HorzGridViewAdapter horzGridViewAdapter;
	private Context mContext;
	public static TwoWayGridView horzGridView;
	GalleryAdapter adapter;
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	private static String savedContent="";
	
	ViewSwitcher viewSwitcher;
	ImageLoader imageLoader;

	EditText write2Content;
	
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
		//입력한 정보 끌어오기
		interiorInfo=(EditText)findViewById(R.id.interiorInfo); //인테리어정보
		Intent intent=getIntent();
		String allInfo=intent.getExtras().getString("ALLINFO");
		interiorInfo.setText(allInfo);	
				
		mContext=getApplicationContext();
		write2Content=(EditText)findViewById(R.id.editText); //이야기
		write2Content.setText(savedContent);
		
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_interior2);
	
		
		horzGridView 
		= (TwoWayGridView) findViewById(R.id.horz_gridview_2);
		
		List<DataObject> horzDataInit = generateGridViewObjects();
			
			horzGridViewAdapter = new HorzGridViewAdapter(mContext,horzDataInit,0);
			horzGridView.setAdapter(horzGridViewAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.interior_write2, menu);
		return true;
	}
	public void addImage_interior2(View view){	
	
		
		savedContent=write2Content.getText().toString(); //저장해놓고
	//	savedInfo=interiorInfo.getText().toString();
		onBackPressed();
	/*	Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);
	*/}
/*	private void initImageLoader() {
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

	//	gridGallery = (GridView) findViewById(R.id.gridGallery_talk);
	//	gridGallery.setFastScrollEnabled(true);
	//	gridGallery.setOnItemClickListener(mItemDeleteListener);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setMultiplePick(false);
	//	gridGallery.setAdapter(adapter);

//		GalleryAdapter.dataChecked.clear(); //버튼 누를때마다 리스트 초기화 시켜줭
//		GalleryAdapter.data.clear();
//		GalleryAdapter.selectCnt=0; //숫자도 초기화
		horzGridView.clearDisappearingChildren();
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_interior2);
		viewSwitcher.setDisplayedChild(1);

		imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick_interior2);

		btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
		btnGalleryPick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Action.ACTION_PICK);
				startActivityForResult(i, 100);

			}
		});

		btnGalleryPickMul = (Button) findViewById(R.id.btn_gallery);
		btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 200);
			}
		});

	}
*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			adapter.clear();

			viewSwitcher.setDisplayedChild(1);
			String single_path = data.getStringExtra("single_path");
			imageLoader.displayImage("file://" + single_path, imgSinglePick);

		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
//			String[] all_path = data.getStringArrayExtra("all_path");

	
	
			
			List<DataObject> horzData = generateGridViewObjects();


			/*if(horzGridViewAdapter==null){
			
				horzGridViewAdapter = new HorzGridViewAdapter(mContext,horzData);
				horzGridView.setAdapter(horzGridViewAdapter);

			}else*/
				horzGridViewAdapter.data=horzData;
			horzGridViewAdapter.notifyDataSetChanged();
				
			viewSwitcher.setDisplayedChild(0);
			
//			adapter.isShow = false;
//			adapter.notifyDataSetChanged();		
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

	private List<DataObject> generateGridViewObjects(){
		List<DataObject> allData=new ArrayList<DataObject>();

		String path;
		
		for(int i=0;i<GalleryAdapter.dataChecked.size();i++){
			path=GalleryAdapter.dataChecked.get(i).sdcardPath;
			
			DataObject singleObject= new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}

}
