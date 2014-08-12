package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.entity.*;
import com.nexters.house.utils.*;

public class ContentDetailAdapter extends BaseAdapter implements OnClickListener {
	
	public static final int REQUEST_CONTENT_DETAIL_VIEW = 0;

	final String TAG = "ContentDetailAdapter";

	public Context mContext;
	private ArrayList<InteriorEntity> mContentArrayList;
	private LayoutInflater mLayoutInflater;
	private int resource;
	private CommonUtils mUtil = new CommonUtils();

	public ContentDetailAdapter(Context context, ArrayList<InteriorEntity> mContentArrayList, int resource) {
		mContext = context;
		this.mContentArrayList = mContentArrayList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContentArrayList.size();
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
		int minHeight = mUtil.pxToDp(mContext, 100);
		//Log.d(TAG, "pxToDp"+ minHeight);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.tvUserId = (TextView) convertView.findViewById(R.id.tv_user_id);
			holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
			holder.tvCntLike = (TextView) convertView.findViewById(R.id.tv_cnt_likes);
			holder.tvCntReply = (TextView) convertView.findViewById(R.id.tv_cnt_reply);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			
			convertView.setTag(holder);
			
			
			// 리스트뷰안의 아이템 높이 설정하는 메소드
			convertView.setMinimumHeight(minHeight);
			//Log.d(TAG, "minHeight"+minHeight);

		} else {
			holder = (Holder) convertView.getTag();
		}

		// 여기에서 게시물의 사용자 아이디/ 카테고리/ 내용/ 이미지를 넣어줄거임.
		String id = mContentArrayList.get(position).id;
		String content = mContentArrayList.get(position).content;
		String category = mContentArrayList.get(position).category;
		List<String> image = mContentArrayList.get(position).image_urls;
		int nLike = mContentArrayList.get(position).badge;
		int nReply = mContentArrayList.get(position).reply;
		int nShare = mContentArrayList.get(position).share;
		int nScrap = mContentArrayList.get(position).scrap;
		
		
		holder.tvUserId.setText(id);
		holder.tvContent.setText(content);
		holder.tvCategory.setText(category);
		holder.tvCntLike.setText(Integer.toString(nLike));
		holder.tvCntReply.setText(Integer.toString(nReply));
		holder.tvCntShare.setText(Integer.toString(nShare));
		holder.tvCntScrap.setText(Integer.toString(nScrap));
		
		// set click listener
		
		
		holder.tvCntReply.setOnClickListener(this);
		holder.tvCntShare.setOnClickListener(this);
		holder.tvCntScrap.setOnClickListener(this);

		return convertView;
	}

	private class Holder {
		ImageView ivUserProfile;
		TextView tvUserId, tvCategory, tvCntLike, tvCntReply, tvCntShare, tvCntScrap, tvContent;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	

}