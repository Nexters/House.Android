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

public class PagerAdapterClass extends PagerAdapter {
	private LayoutInflater mInflater;
	private ArrayList<View> views;
	private int pageNum = GalleryAdapter.customGalleriesChecked.size();
//	private ArrayList<EditText> singleInfo;
//	public ArrayList<String> InfoList;
	private Context c;
	private ViewPager mPager;

	private InteriorWriteActivity mInteriorWriteActivity;
	private int currentPosition;
	
	public PagerAdapterClass(Context c, ViewPager pager, InteriorWriteActivity interiorWriteActivity) {
		super();
		mInteriorWriteActivity = interiorWriteActivity;
		this.c = c;
		this.mPager = pager;
		mInflater = LayoutInflater.from(c);
		views = new ArrayList<View>();

//		singleInfo=new ArrayList<EditText>();
//		InfoList=new ArrayList<String>();
		
		for (int i = 0; i <pageNum; i++) {
			View v = mInflater.inflate(R.layout.pageritem, null);
			Button btn = (Button) v.findViewById(R.id.btn_delete);
			ImageView singleImg=(ImageView)v.findViewById(R.id.singleImage);
		
//			singleInfo.add((EditText)v.findViewById(R.id.singleInfomation));
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=2;
		
//			InfoList.add(singleInfo.get(i).getText().toString());
			Bitmap bmp=BitmapFactory.decodeFile(GalleryAdapter.customGalleriesChecked.get(i).sdcardPath,options);
			singleImg.setImageBitmap(bmp);
			
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
		Log.d("create", "create : " + views.get(position) + " - " + position
				+ " - size " + getCount());

		
		((ViewPager) pager).addView(views.get(position));
		
	
/*		if( singleInfo.get(position).getText().toString()!="")
			InfoList.set(position, singleInfo.get(position).getText().toString());
*/		return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View pager, Object obj) {
		return pager == obj;
	}
}
