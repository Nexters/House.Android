package com.nexters.house.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.entity.CustomGallery;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class PagerAdapterClass extends PagerAdapter {
	private LayoutInflater mInflater;
	private ArrayList<View> views;
	private int pageNum;
//	private ArrayList<EditText> singleInfo;
//	public ArrayList<String> InfoList;
	private Context mContext;
	private ViewPager mViewPagers;
	private InteriorWriteActivity mInteriorWriteActivity;
	private int currentPosition;
	private ImageLoader mImageLoader;
	
	public PagerAdapterClass(Context context, ViewPager pager, InteriorWriteActivity interiorWriteActivity,ImageLoader imageloader) {
		super();
		mInteriorWriteActivity = interiorWriteActivity;
		this.mContext = context;
		this.mViewPagers = pager;
		this.mImageLoader=imageloader;
		mInflater = LayoutInflater.from(context);
		views = new ArrayList<View>();
		pageNum = GalleryAdapter.customGalleriesChecked.size();
		
//		singleInfo=new ArrayList<EditText>();
//		InfoList=new ArrayList<String>();
		
		for (int i = 0; i <pageNum; i++) {
			View v = mInflater.inflate(R.layout.pager_item, null);
			Button btn = (Button) v.findViewById(R.id.btn_delete);
			ImageView singleImg=(ImageView)v.findViewById(R.id.singleImage);
		
			mImageLoader.displayImage("file://" + GalleryAdapter.customGalleriesChecked.get(i).sdcardPath,

					singleImg, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							
							super.onLoadingStarted(imageUri, view);
						}
					});
			
			
			final int index = i;
			btn.setOnClickListener(new View.OnClickListener() {
				CustomGallery customGallery = GalleryAdapter.customGalleriesChecked.get(index);  
				
				@Override
				public void onClick(View v) {
					if (views.size() == 1)
						return;
					// 버튼의 뷰를 찾아서 ArrayList에서 빼야함. index로 할 경우, 사이즈가 밀려서 제대로 된
					// 인덱스를 가리키지 않음.
					views.remove(v.getParent());
					
					mInteriorWriteActivity.removePageMark();
					GalleryAdapter.compareChecked(customGallery);
					GalleryAdapter.customGalleriesChecked.remove(customGallery);
					GalleryAdapter.selectCnt--;
					notifyDataSetChanged();
				}
			});
			views.add(v);
		}
	}

	private int getRealWidth(String fileName){
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(fileName, options); 
        return options.outWidth; 
	}
	private int getRealHeight(String fileName){
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(fileName, options); 
        return options.outHeight; 
	}
	@Override
	public int getCount() {
		return views.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(final View pager, final int position) {
		
		
//		Log.d("create", "create : " + views.get(position) + " - " + position
//				+ " - size " + getCount());
		((ViewPager) pager).addView(views.get(position));
		return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View pager, Object obj) {
		return pager == obj;
	}
	public void clearCache() {
		mImageLoader.clearDiscCache();
		mImageLoader.clearMemoryCache();
	}

}
