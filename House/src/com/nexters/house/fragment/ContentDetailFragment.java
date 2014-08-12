package com.nexters.house.fragment;

import java.util.*;

import uk.co.senab.actionbarpulltorefresh.library.*;
import android.annotation.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.*;

import com.nexters.house.R;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;


public class ContentDetailFragment extends Fragment {

	private final String TAG = "ContentDetailFragment";

	private ListView lvContent;
	private ArrayList<ContentEntity> mExamItemArrayList;
	private ContentDetailAdapter mMainListAdapter;
	private ImageView ivImage;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.content_detail_page, container, false);
		initResources(v);

		setListViewHeightBasedOnChildren(lvContent);
		return v;
	}


	@SuppressLint("InflateParams")
	private void initResources(View v){
		
		lvContent = (ListView) v.findViewById(R.id.lv_interior_image);
		mExamItemArrayList = new ArrayList<ContentEntity>();
		ivImage = (ImageView) v.findViewById(R.id.image_row);

		mExamItemArrayList = new ArrayList<ContentEntity>();
		mMainListAdapter = new ContentDetailAdapter(getActivity().getApplicationContext(),
				mExamItemArrayList, R.layout.image_row);

		lvContent.setAdapter(mMainListAdapter);
		for (int itemCount = 0; itemCount < 15; itemCount++) {
			ContentEntity mExamEntity = new ContentEntity();
			
			mExamEntity.imageUrl="https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10524374_570245013084779_7454008372005256632_n.jpg?oh=4761db9f33b72709585016c2649c747e&oe=5434C617&__gda__=1413811119_55884851b246ddb301725a0a78cacc84"; 

			mExamItemArrayList.add(mExamEntity);

		}

		mMainListAdapter.notifyDataSetChanged();


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



}