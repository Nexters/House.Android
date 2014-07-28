package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nexters.house.*;
import com.nexters.house.entity.*;
import com.nexters.house.utils.*;

public class ListAdapter extends BaseAdapter {

	final String TAG = "MainListAdapter";

	public Context mContext;
	private ArrayList<InteriorEntity> mExamItemArrayList;
	private LayoutInflater mLayoutInflater;
	int resource;

	public ListAdapter(Context context,
			ArrayList<InteriorEntity> mExamItemArrayList, int resource) {
		mContext = context;
		this.mExamItemArrayList = mExamItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}

	CommonUtils mUtil = new CommonUtils();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mExamItemArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		int minHeight = mUtil.pxToDp(mContext, 1000);
		Log.d(TAG, "pxToDp"+ minHeight);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.tv_image = (ImageView) convertView
					.findViewById(R.id.tv_image);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			convertView.setTag(holder);
			
			SliderLayout slider = (SliderLayout) convertView
					.findViewById(R.id.slider);
			List<String> image_urls = mExamItemArrayList.get(position).image_urls;
			
			for (String url : image_urls) {
				TextSliderView textSliderView = new TextSliderView(
						convertView.getContext());
				// initialize a SliderLayout
				textSliderView.image(url);
				// .setScaleType(BaseSliderView.ScaleType.Fit);
				slider.addSlider(textSliderView);
			}
			
			// 리스트뷰안의 아이템 높이 설정하는 메소드
			convertView.setMinimumHeight(minHeight);
			Log.d(TAG, "minHeight"+minHeight);

		} else {
			holder = (Holder) convertView.getTag();
		}

		// set title, content, etc...
		String title = mExamItemArrayList.get(position).id;
		String content = mExamItemArrayList.get(position).content;

		holder.tv_title.setText(title);
		holder.tv_content.setText(content);

		// set click listener
		holder.tv_title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext,
						mExamItemArrayList.get(position).id,
						Toast.LENGTH_SHORT).show();

			}
		});

		holder.tv_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext,
						mExamItemArrayList.get(position).content,
						Toast.LENGTH_SHORT).show();

			}
		});

		return convertView;
	}

	private class Holder {
		ImageView tv_image;
		TextView tv_title, tv_content;
	}

}