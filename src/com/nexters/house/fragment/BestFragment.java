package com.nexters.house.fragment;

import java.util.*;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.entity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.adapter.ListAdapter;

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
				mBestItemArrayList, R.layout.item_row);

		lv_main.setAdapter(mListAdapter);
		
		Log.d(TAG, "setContent");

		for (int itemCount = 0; itemCount < 20; itemCount++) {
			BestEntity mExamEntity = new BestEntity();

			mExamEntity.title = "Title : " + itemCount;
			mExamEntity.content = "Content : " + itemCount;

			mBestItemArrayList.add(mExamEntity);

		}

		mListAdapter.notifyDataSetChanged();
		
		return v;
	}

}

