package com.nexters.house.fragment;

import java.util.*;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.InteriorAdapter;
import com.nexters.house.entity.*;

public class InteriorFragment extends Fragment {
	
	private final String TAG = "MainActivity";

	private ListView lv_main;
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private InteriorAdapter mListAdapter;
	private Button btn_write;
		
	@SuppressWarnings("serial")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	  
		Log.d(TAG, "onCreateView");
		
		View v = inflater.inflate(R.layout.fragment_interior, container, false);

		Log.d(TAG, "viewInit");

		lv_main = (ListView) v.findViewById(R.id.lv_interior_view);

		mInteriorItemArrayList = new ArrayList<InteriorEntity>();
		
		mListAdapter = new InteriorAdapter(getActivity().getApplicationContext(),
				mInteriorItemArrayList, R.layout.custom_view_interior);

		lv_main.setAdapter(mListAdapter);
		
		Log.d(TAG, "setContent");
	//	Toast.makeText(v.getContext(), "Hello World!", Toast.LENGTH_SHORT).show();
		
		for (int itemCount = 0; itemCount < 10; itemCount++) {
			InteriorEntity mInteriorEntity = new InteriorEntity();

			mInteriorEntity.id = "User Id : " + itemCount;
			mInteriorEntity.content = "Content에 들어가는 내용들입니다.  : " + itemCount;
			mInteriorEntity.image_urls = new ArrayList<String>(){{
				add("http://www.interiordecodir.com/image/artistic/artistic-large-simple-minimlaist-house-interior.jpg");
				add("http://www.ardvarkpainting.us/wp-content/uploads/2012/01/interior_house.jpg");
				add("http://greatinteriordesign.com/wp-content/uploads/2009/09/brazil-multi-level-single-family-home-guest-bedroom-design.jpg");
			}};
			mInteriorEntity.category = "Interior Category";
			mInteriorItemArrayList.add(mInteriorEntity);
		}

		mListAdapter.notifyDataSetChanged();
	  
		
		initResources(v);
		
		return v;
	}
	
	private void initResources(View v){
		btn_write=(Button)v.findViewById(R.id.btn_write);
		
	    btn_write.setText("쓰기");
	    btn_write.bringToFront();		
	    btn_write.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SelectWriteActivity.class);
				
			 	startActivity(intent);
				
			}
		});
	}

}

