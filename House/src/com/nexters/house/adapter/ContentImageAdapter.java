package com.nexters.house.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nexters.house.R;
import com.nexters.house.thread.DownloadImageTask;

public class ContentImageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> mImgs;
	private LayoutInflater mLayoutInflater;
	int resource;

	public ContentImageAdapter(Context context, ArrayList<String> imgs,
			int resource) {
		mContext = context;
		this.mImgs = imgs;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;

		if (convertView != null)
			holder = (Holder) convertView.getTag();
		if (convertView == null	|| holder.position != position) {
			convertView = mLayoutInflater.inflate(resource, null);
			holder = new Holder();

			// find resource
			holder.position = position;
			holder.image = (ImageView) convertView
					.findViewById(R.id.tv_content_image);

			String url = mImgs.get(position);
			Log.d("ContentImageAdapter",
					"ContentImageAdapter : " + mImgs.size());
			new DownloadImageTask(holder.image).execute(url);

			convertView.setTag(holder);
		}

		return convertView;
	}

	private class Holder {
		int position;
		ImageView image;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getCount() {
		return mImgs.size();
	}
}