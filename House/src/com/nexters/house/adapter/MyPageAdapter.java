package com.nexters.house.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nexters.house.R;
import com.nexters.house.activity.ContentDetailActivity;
import com.nexters.house.entity.MyPageEntity;
import com.nexters.house.thread.DownloadImageTask;
import com.nexters.house.utils.CommonUtils;

public class MyPageAdapter extends BaseAdapter{
	private Context mContext;
	private CommonUtils mUtils = new CommonUtils();

	private ArrayList<MyPageEntity> mMyPageItemArrayList;
	
	private int refreshCnt;
	
	public MyPageAdapter(Context context, ArrayList<MyPageEntity> mMyPageItemArrayList) {
		mContext = context;
		this.mMyPageItemArrayList = mMyPageItemArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		
		if(convertView != null)
			holder = (Holder) convertView.getTag();
        if(convertView == null || holder.position != position || holder.refresh != refreshCnt) {
			holder = new Holder();
			
			final long no = mMyPageItemArrayList.get(position).no;
			final int type = mMyPageItemArrayList.get(position).type;
			String category = mMyPageItemArrayList.get(position).category;
			String imgUrl = mMyPageItemArrayList.get(position).imageUrl;
					
			holder.position = position;
			holder.refresh = refreshCnt;
			holder.no = no;
			holder.type = type;
			holder.category = category;
			holder.imageUrl = imgUrl;
			holder.myPageImage = new ImageView(mContext);
			
        	// if it's not recycled, initialize some attributes
			int size = mUtils.dpToPx(mContext, 145);
			holder.myPageImage.setLayoutParams(new GridView.LayoutParams(size, size));
			holder.myPageImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.myPageImage.setPadding(1, 1, 1, 1);
			Log.d("MyPageAdapter", "MyPageAdapter : " + holder.imageUrl);
			new DownloadImageTask(holder.myPageImage).execute(holder.imageUrl);
			
			// set listener
			holder.myPageImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,ContentDetailActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("brdNo", no);
					intent.putExtra("brdType", type);
					mContext.startActivity(intent);
				}
			});
			
			holder.myPageImage.setTag(holder);
        }
        return holder.myPageImage;
	}
	
	private class Holder {
		int refresh;
		int position;
		long no;
		int type;
		String category;
		String imageUrl;
		ImageView myPageImage;
	}
	
	public void clear() {
		mMyPageItemArrayList.clear();
	}
	
	public void add(long brdNo, int type, String brdId, String brdName, String category, String imgUrl) {
		MyPageEntity e = new MyPageEntity();
		e.no = brdNo;
		e.id = brdId;
		e.name = brdName;
		e.type = type;
		e.category = category;
		e.imageUrl = imgUrl;
		
		mMyPageItemArrayList.add(e);
	}
    
	@Override
	public void notifyDataSetChanged() {
		refreshCnt += 1;
		super.notifyDataSetChanged();
	}
	
	@Override
    public int getCount() {
        return mMyPageItemArrayList.size();
    }

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
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