package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.entity.*;

public class ReplyAdapter extends BaseAdapter{
	final String TAG = "MainListAdapter";

	public Context mContext;
	private ArrayList<ReplyEntity> mReplyItemList;
	private LayoutInflater mLayoutInflater;
	int resource;

	public ReplyAdapter(Context context,
			ArrayList<ReplyEntity> mReplyItemList, int resource) {
		mContext = context;
		this.mReplyItemList = mReplyItemList;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		
		if(convertView != null)
			holder = (Holder) convertView.getTag();
		if (convertView == null || (holder != null && holder.position != position)) {
			final View createView;
			createView = convertView = mLayoutInflater.inflate(resource, null);
			holder = new Holder();
			// find resource
			
			holder.position = position;
			holder.profileImg = (ImageView) convertView.findViewById(R.id.iv_user_profile_image);
			holder.name = (TextView) convertView.findViewById(R.id.tv_user_profile_name);
			holder.content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.created = (TextView) convertView.findViewById(R.id.tv_created);
			
			holder.profileImg.setImageResource(R.drawable.user_profile_image);
			holder.name.setText(mReplyItemList.get(position).name);
			holder.content.setText(mReplyItemList.get(position).content);
			holder.created.setText(mReplyItemList.get(position).created);
			convertView.setTag(holder);
		}
		return convertView;
	}

	private class Holder {
		int position;
		ImageView profileImg;
		TextView name;
		TextView content;
		TextView created;
	}

	@Override
	public int getCount() {
		return mReplyItemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
}