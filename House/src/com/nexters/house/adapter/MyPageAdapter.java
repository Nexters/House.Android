package com.nexters.house.adapter;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.utils.*;

public class MyPageAdapter extends BaseAdapter{
	private Context mContext;
	private CommonUtils mUtils = new CommonUtils();

	public MyPageAdapter(Context context) {
		mContext = context;
	}

	@Override
    public int getCount() {
        return mThumbIds.length;
    }

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        
       @SuppressWarnings("static-access")
       int size = mUtils.dpToPx(mContext, 145);
       Log.d("size is=", ""+size);
       
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
	}
	
    // references to our images
    private Integer[] mThumbIds = {
    		R.drawable.sample01, R.drawable.sample02,
            R.drawable.sample03, R.drawable.sample04,
            R.drawable.sample05, R.drawable.sample06,
            R.drawable.sample01, R.drawable.sample02,
            R.drawable.sample03, R.drawable.sample04,
            R.drawable.sample05, R.drawable.sample06,
            R.drawable.sample01, R.drawable.sample02,
            R.drawable.sample03, R.drawable.sample04,
            R.drawable.sample05, R.drawable.sample06
    };
}