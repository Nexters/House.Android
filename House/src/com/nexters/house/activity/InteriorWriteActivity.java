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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.nexters.house.R;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.adapter.PagerAdapterClass;
import com.nexters.house.entity.Action;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class InteriorWriteActivity extends Activity {
	private int mPrevPosition;
	private String action;

	private GalleryAdapter mGalleryAdapter;
	private PagerAdapterClass mPagerAdapterClass;

	private ImageView imgSinglePick;
	private ImageLoader mImageLoader;

	private LinearLayout mPageMark;

	private ViewSwitcher mViewSwitcher;
	private ViewPager mPager;

	private Button btnGalleryPick;
	private Button btnGalleryPickMul;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_preview_write);

		initActionBar();
		initImageLoader();
		initResource();

		Intent multiplePickIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(multiplePickIntent, 200);
	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
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

	private void initResource() {
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(),
				mImageLoader);
		mGalleryAdapter.setMultiplePick(false);

		GalleryAdapter.clear(); // 버튼 누를때마다 리스트 초기화 시켜줭 + 숫자도 초기화
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		mViewSwitcher.setDisplayedChild(1);

		imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

		// pagination
		mPageMark = (LinearLayout) findViewById(R.id.page_mark);
		mPager = (ViewPager) findViewById(R.id.previewPager);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshPager();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void refreshPager() {
		mPagerAdapterClass = new PagerAdapterClass(getApplicationContext(),
				mPager, this);
		mPager.setAdapter(mPagerAdapterClass);
		
		mPageMark.removeAllViews(); // 다시 다 지워
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (mPageMark.getChildAt(mPrevPosition) != null) {
					mPageMark.getChildAt(mPrevPosition).setBackgroundResource(
							R.drawable.icon_doldol);
				}
				mPageMark.getChildAt(position).setBackgroundResource(
						R.drawable.icon_doldol_click);
				mPrevPosition = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		if (GalleryAdapter.customGalleriesChecked.size() > 0) {
			mPrevPosition = 0;
			for (int i = 0; i < GalleryAdapter.customGalleriesChecked.size(); i++)
				addPageMark();
			mPageMark.getChildAt(mPrevPosition).setBackgroundResource(
					R.drawable.page_select);
			
			mViewSwitcher.setDisplayedChild(0);
		} else {
			mViewSwitcher.setDisplayedChild(1);
		}
		mGalleryAdapter.isShow = false;
		mGalleryAdapter.notifyDataSetChanged();
	}

	private void addPageMark() {
		ImageView iv = new ImageView(getApplicationContext()); // 페이지 표시 이미지 뷰
																// 생성
		iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		iv.setBackgroundResource(R.drawable.page_not);
		mPageMark.addView(iv);// LinearLayout에 추가
	}

	public void removePageMark() {
		// iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));
		int checkedSize = GalleryAdapter.customGalleriesChecked.size();

		Log.d("removePage", "removePage : " + mPager.getCurrentItem() + " , "
				+ checkedSize + ", " + mPageMark.getChildCount());
		mPageMark.removeView(mPageMark.getChildAt(checkedSize - 1));
	}

	private void openNext() {
		finish();
		Intent intent = new Intent(this, InteriorWrite2Activity.class);
		/*
		 * pageradapter.notifyDataSetChanged(); String allInfo = ""; for(int
		 * j=0;j<pageradapter.InfoList.size();j++){ //스트링 합치기
		 * allInfo=allInfo.concat(pageradapter.InfoList.get(j));
		 * allInfo=allInfo.concat(", ");
		 * }
		 */
		startActivity(intent);
	}

	private void moreSelect() {
		Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("입력을 취소하시겠습니까 ?")
				.setCancelable(false)
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				})
				.setNegativeButton("아니오",
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
}
