package com.nexters.house.fragment;

import java.util.*;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;

public class InteriorFragment extends Fragment{
	
	public final static int SCROLL_STATE_IDLE = 0;
	
	private final String TAG = "MainActivity";

	private ListView lv_main;
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private InteriorAdapter mListAdapter;
	private Button btn_write;
	//test
	private Button btn_add;
	private Boolean loading = true;
	
	private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;

		
	@SuppressWarnings("serial")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		Log.d(TAG, "onCreateView");
		
		View v = inflater.inflate(R.layout.fragment_interior, container, false);

		Log.d(TAG, "viewInit");

		lv_main = (ListView) v.findViewById(R.id.lv_interior_view);
		//add the footer before adding the adapter, else the footer will nod load!
		View footerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
		
		//footerview를  listview 제일 하단에 붙임 
		lv_main.addFooterView(footerView);
		
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
			
			mInteriorEntity.badge = itemCount;
			mInteriorEntity.reply = itemCount*100;
			mInteriorEntity.share = itemCount*10;
			mInteriorEntity.scrap = 50;
			
			mInteriorItemArrayList.add(mInteriorEntity);
		}

		mListAdapter.notifyDataSetChanged();
	  
		
		initResources(v);
		
		return v;
	}
	
	private void initResources(View v){
		btn_write=(Button)v.findViewById(R.id.btn_write);
		btn_add = (Button)v.findViewById(R.id.btn_add);
	    btn_write.setText("쓰기");
	    btn_add.setText("add");
	    btn_write.bringToFront();
	    btn_add.bringToFront();
	    
	    btn_write.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),InteriorWriteActivity.class);
				
			 	startActivity(intent);
				
			}
		});
	    
	    btn_add.setOnClickListener(new Button.OnClickListener(){
	    	@Override
			public void onClick(View v) {
	    		mListAdapter.add();
	    		mListAdapter.notifyDataSetChanged();
				
			}
	    	
	    });
	    
	    lv_main.setOnScrollListener(new OnScrollListener(){
			
			//useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			//dumdumdum			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				
				//what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;				
				
				//is the bottom item visible & not loading more already ? Load more !
				if((lastInScreen == totalItemCount) && !(loading)){					
					Thread thread =  new Thread(null, loadMoreListItems);
			        thread.start();
				}
			}
		});
	    
		//Load the first 20 items
		Thread thread =  new Thread(null, loadMoreListItems);
        thread.start();
	    
	}

	   //Runnable to load the items 
    private Runnable loadMoreListItems = new Runnable() {			
		@SuppressWarnings("serial")
		@Override
		public void run() {
			//Set flag so we cant load new items 2 at the same time
			loading = true;
			
			//Reset the array that holds the new items
			mInteriorItemArrayList = new ArrayList<InteriorEntity>();
	    	
			//Simulate a delay, delete this on a production environment!
	    	try { Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
			//Get 15 new listitems
	    	for (int i = 0; i < 20; i++) {		
				mListAdapter.add();
				mHandler.sendEmptyMessage(0);
			}
	    	
	    	
			//Done! now continue on the UI thread
	        getActivity().runOnUiThread(returnRes);
	        
		}
	};	
	
    
	//Since we cant update our UI from a thread this Runnable takes care of that! 
	//스레드는 내부적인 연산만 해야한다. 다른 스레드 소속의 UI를 건드릴 수 없다. 그래서 Handler를 사용하는거?!
	//스레드간의 통신할 수 있는 장치가 필요한데 그게 바로 핸들러이다.
	
	
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
        	
			//Loop thru the new items and add them to the adapter
			if(mInteriorItemArrayList != null && mInteriorItemArrayList.size() > 0){
                for(int i=0;i<mInteriorItemArrayList.size();i++)
                	mListAdapter.add();
            }
        	
			//Update the Application title
        	getActivity().setTitle("House" + String.valueOf(mListAdapter.getCount()) + " items");
			
			//Tell to the adapter that changes have been made, this will cause the list to refresh
        	mListAdapter.notifyDataSetChanged();
			
			//Done loading more.
            loading = false;
        }
    };

    
    Handler mHandler = new Handler(){
    	public void handleMessage(Message msg){
    		if(msg.what == 0){
    			mListAdapter.notifyDataSetChanged();
    		}
    	}
    };

}

