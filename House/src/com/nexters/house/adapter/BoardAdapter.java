package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.utils.*;

public class BoardAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<String> mArrayList;
	private CommonUtils mUtil;

	public BoardAdapter() {
		super();
		mUtil = new CommonUtils();
	}

	public BoardAdapter(Context context, ArrayList<String> arrayList) {
		super();
		this.mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mArrayList = arrayList;
	}

	@Override
	public int getCount() {
		return mArrayList.size();
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
		@SuppressWarnings("static-access")
		int minHeight = mUtil.pxToDp(mContext, 700);
		Log.d("CheckCHeckCheck", "" + position);
		if (position % 2 == 0) {
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.custom_view_board_left, parent, false);
			}
		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.custom_view_board_right, parent, false);
			}
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.board_content);
		textView.setText(mArrayList.get(position));
		// 리스트뷰안의 아이템 높이 설정하는 메소드
		convertView.setMinimumHeight(minHeight);

		return convertView;
	}
}
