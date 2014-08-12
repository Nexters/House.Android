package com.nexters.house.fragment;

import java.util.*;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;

public class ContentDetailFragment extends Fragment {

	private final String TAG = "ContentDetailFragment";

	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private InteriorAdapter mListAdapter;
	
	private InteriorEntity mInteriorEntity = new InteriorEntity();
	
	private ImageView userProfileImg;
	private TextView userProfileId;
	private TextView cntLike;
	private TextView cntReply;
	
	private ListView interiorListView;
	private ListView replyListView;
	
	private Boolean loading = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.content_detail_page, container, false);
//		initResources(v);
//		initEvents();

		return v;
	}


	@SuppressLint("InflateParams")
	private void initResources(View v){

//		interiorListView = (ListView) v.findViewById(R.id.lv_interior_view);
//		mInteriorItemArrayList = new ArrayList<InteriorEntity>();
//
//		View footerView = ((LayoutInflater)getActivity().
//				getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
//
//		mListAdapter = new InteriorAdapter(getActivity().
//				getApplicationContext(), mInteriorItemArrayList, R.layout.custom_view_interior);
//
//		//footerview를  listview 제일 하단에 붙임 
//		interiorListView.addFooterView(footerView);
//		interiorListView.setAdapter(mListAdapter);
//
//		//Load the first 5 items
//		Thread thread =  new Thread(null, loadListItems);
//		thread.start();

	}

	private void initEvents(){
		
	
	}

//	private Runnable loadListItems = new Runnable(){
//		@Override
////		public void run(){
////			loading = true;
////
////			mHandler.sendEmptyMessage(0);
//
//		}
//	};
//	



//	Handler mHandler = new Handler(){
//		public void handleMessage(Message msg){
//			switch(msg.what){
//			case 0:
//				//처음 로딩할때 몇개로딩할지 여기서 결정하고 보여주는거.
//				for(int i=0;i<5 ;i++){
//					mListAdapter.add();
//				}
//				mListAdapter.notifyDataSetChanged();
//				//Done loading more.
//				loading = false;
//				break;
//			case 1:
//				
//				break;
//			case 2:
//				break;
//			case 3:
//				break;
//			}
//		}
//	};


}