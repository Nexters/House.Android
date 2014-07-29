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
import com.nexters.house.activity.*;
import com.nexters.house.entity.*;
import com.nexters.house.utils.*;

public class InteriorAdapter extends BaseAdapter implements OnClickListener {
	
	public static final int REQUEST_CONTENT_DETAIL_VIEW = 0;

	final String TAG = "MainListAdapter";

	public Context mContext;
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private LayoutInflater mLayoutInflater;
	int resource;

	public InteriorAdapter(Context context, ArrayList<InteriorEntity> mExamItemArrayList, int resource) {
		mContext = context;
		this.mInteriorItemArrayList = mExamItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}

	CommonUtils mUtil = new CommonUtils();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInteriorItemArrayList.size();
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
		//Log.d(TAG, "pxToDp"+ minHeight);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
			convertView.setTag(holder);
			
			SliderLayout slider = (SliderLayout) convertView.findViewById(R.id.slider);
			List<String> image_urls = mInteriorItemArrayList.get(position).image_urls;
			
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
			//Log.d(TAG, "minHeight"+minHeight);

		} else {
			holder = (Holder) convertView.getTag();
		}

		// 여기에서 게시물의 사용자 아이디/ 카테고리/ 내용/ 이미지를 넣어줄거임.
		String id = mInteriorItemArrayList.get(position).id;
		
		String content = mInteriorItemArrayList.get(position).content;
		String category = mInteriorItemArrayList.get(position).category;
		//List<String> image = mInteriorItemArrayList.get(position).image_urls;
		

		holder.tv_id.setText(id);
		holder.tv_content.setText(content);
		holder.tv_category.setText(category);
		
		// set click listener
		
		holder.tv_id.setOnClickListener(this);
		holder.tv_content.setOnClickListener(this);
		//holder.iv_image.setOnClickListener(this);

		return convertView;
	}

	private class Holder {
		ImageView iv_image;
		TextView tv_id, tv_content, tv_category;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.tv_content|| v.getId()==R.id.tv_id /*|| v.getId()==R.id.iv_image */){
			Intent intent = new Intent(v.getContext(), ContentDetailActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
		
	}

	

}