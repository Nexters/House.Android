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
	public int getCount() {
		// TODO Auto-generated method stub
		return mReplyItemList.size();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			
			holder.userProfileImage = (ImageView) convertView.findViewById(R.id.iv_user_profile_image);
			holder.userName = (TextView) convertView.findViewById(R.id.tv_user_profile_name);
			//holder.replyContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
			

		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.userProfileImage.setImageResource(R.drawable.user_profile_image);


		return convertView;
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }
 
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
 
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
}

	private class Holder {
		
		ImageView userProfileImage;
		TextView userName;
		TextView replyContent;
	}

}