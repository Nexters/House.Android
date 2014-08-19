package com.nexters.house.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;

public class MyPageAdapter extends BaseAdapter{
	private Context mContext;

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
        
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
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
            R.drawable.preview_image_01, R.drawable.preview_image_02,
            R.drawable.preview_image_03, R.drawable.preview_image_02,
            R.drawable.preview_image_02, R.drawable.preview_image_02,
            R.drawable.preview_image_03, R.drawable.preview_image_03,
            R.drawable.preview_image_03, R.drawable.preview_image_03,
            R.drawable.preview_image_01, R.drawable.preview_image_01,
            R.drawable.preview_image_01, R.drawable.preview_image_01,
    };
}