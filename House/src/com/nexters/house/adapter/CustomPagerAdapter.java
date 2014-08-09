package com.nexters.house.adapter;

import android.support.v4.view.*;
import android.view.*;

public class CustomPagerAdapter extends PagerAdapter {
	private int mPageCount;
    private View[] mPageViews;
    
    public CustomPagerAdapter(int pageCount, View[] pageViews) {
        this.mPageCount = pageCount;
        this.mPageViews = pageViews;
    }
    
    @Override
    public int getCount() {
        return mPageCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mPageViews[position];
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View container, Object obj) {
        return container == obj;
    }
}