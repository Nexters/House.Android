package com.nexters.house.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.nexters.house.R;
import com.nexters.house.adapter.PagerAdapterClass;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class InteriorWriteActivity extends Activity {

	GalleryAdapter adapter;

	PagerAdapterClass pageradapter;
	private ViewPager mPager;
	ImageView imgSinglePick;
	Button btnGalleryPick;
	Button btnGalleryPickMul;

	String action;
	ViewSwitcher viewSwitcher;
	ImageLoader imageLoader;
	private int mPrevPosition;
	private LinearLayout mPageMark;

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
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setMultiplePick(false);


		GalleryAdapter.dataChecked.clear(); //버튼 누를때마다 리스트 초기화 시켜줭
	
		GalleryAdapter.data.clear();
		GalleryAdapter.selectCnt=0; //숫자도 초기화
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		viewSwitcher.setDisplayedChild(1);

		imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

	}

	
	
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

		
			mPageMark=(LinearLayout)findViewById(R.id.page_mark);
			mPager=(ViewPager)findViewById(R.id.previewPager);
			pageradapter=new PagerAdapterClass(getApplicationContext(), mPager,this);
	//		mPager.setAdapter(new PagerAdapterClass(getApplicationContext(), mPager));
		
			mPager.setAdapter(pageradapter);
			mPageMark.removeAllViews();  //다시 다 지워
			mPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {

					if(mPageMark.getChildAt(mPrevPosition)!=null) {
						mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_not); 
						}
					mPageMark.getChildAt(position).setBackgroundResource(R.drawable.page_select);
					mPrevPosition=position;
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {				
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {	
				}
			});
			
			mPrevPosition=0;
			for(int i=0;i<GalleryAdapter.dataChecked.size();i++)
				addPageMark();
			mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_select);
					
			viewSwitcher.setDisplayedChild(0);
			adapter.isShow = false;
			adapter.notifyDataSetChanged();
//			adapter.addAll(GalleryAdapter.dataChecked);

		}
	}
	
	private void addPageMark() {
		ImageView iv = new ImageView(getApplicationContext());	//페이지 표시 이미지 뷰 생성
		iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iv.setBackgroundResource(R.drawable.page_not);
		mPageMark.addView(iv);//LinearLayout에 추가
	}
	public void removePageMark(){
		
	//	iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		int checkedSize=GalleryAdapter.dataChecked.size();
	
		
		Log.d("removePage", "removePage : " + mPager.getCurrentItem() + " , " + checkedSize + ", " + mPageMark.getChildCount());
	
	
		mPageMark.removeView(mPageMark.getChildAt(checkedSize-1));
	}
	
	private void openNext() {

		finish();
		Intent i =new Intent(this,InteriorWrite2Activity.class);
		startActivity(i);
		
		
	}

	private void moreSelect() {
		Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);
		
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

}
