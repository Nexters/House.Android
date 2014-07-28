package com.nexters.house.fragment;

import java.util.*;

import android.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.activity.*;
import com.nexters.house.adapter.ListAdapter;
import com.nexters.house.entity.*;
import com.nexters.house.*;
import com.nexters.house.R;

public class BestFragment extends Fragment {
	
	private final String TAG = "MainActivity";

	// list
	private ListView lv_main;
	private ArrayList<BestEntity> mBestItemArrayList;
	private ListAdapter mListAdapter;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	  
		Log.d(TAG, "onCreateView");
		
		View v = inflater.inflate(R.layout.fragment_best, container, false);

		Log.d(TAG, "viewInit");

		lv_main = (ListView) v.findViewById(R.id.lv_best_view);

		mBestItemArrayList = new ArrayList<BestEntity>();
		mListAdapter = new ListAdapter(getActivity().getApplicationContext(),
				mBestItemArrayList, R.layout.custom_view_interior);

		lv_main.setAdapter(mListAdapter);
		
		Log.d(TAG, "setContent");
	//	Toast.makeText(v.getContext(), "Hello World!", Toast.LENGTH_SHORT).show();
		
		for (int itemCount = 0; itemCount < 20; itemCount++) {
			BestEntity mExamEntity = new BestEntity();

			mExamEntity.title = "Title : " + itemCount;
			mExamEntity.content = "Content : " + itemCount;
			mExamEntity.image_urls = new ArrayList<String>(){{
				add("http://tvfiles.alphacoders.com/100/hdclearart-10.png");
				add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
				add("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
			}};
			mBestItemArrayList.add(mExamEntity);
		}

		mListAdapter.notifyDataSetChanged();
	  
		Button b=(Button)v.findViewById(R.id.btn_write);
	    b.setText("쓰기");
	    b.bringToFront();
		b.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SelectWriteActivity.class);
				
			 	startActivity(intent);
				
			}
		});
		return v;
	}

}

