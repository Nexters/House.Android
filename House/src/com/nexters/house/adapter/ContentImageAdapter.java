package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.entity.*;
import com.nexters.house.utils.*;

public class ContentImageAdapter extends BaseAdapter{
	final String TAG = "MainListAdapter";

	private Context mContext;
	private MainActivity mMainActivity;
	private ArrayList<ContentEntity> mContentItemArrayList;
	private LayoutInflater mLayoutInflater;
	int resource;
	private CommonUtils mUtil;

	
	public ContentImageAdapter(Context context, ArrayList<ContentEntity> mContentItemArrayList, int resource, MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mContext = context;
		mUtil = new CommonUtils();
		this.mContentItemArrayList = mContentItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContentItemArrayList.size();
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
			
			convertView.setTag(holder);

			
			List<String> image_urls = mContentItemArrayList.get(position).image_urls;
			

		} else {
			holder = (Holder) convertView.getTag();
		}
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
		ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10;	
		ImageView image11,image12,image13,image14,image15;
	}
}