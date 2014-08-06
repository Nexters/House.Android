package com.nexters.house.fragment;

import java.util.*;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;
import com.nexters.vobble.activity.*;
import com.nexters.vobble.entity.*;

public class InteriorFragment extends Fragment {

	private final String TAG = "InteriorFragment";


	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private ListView lv_main;
	private InteriorAdapter mListAdapter;
	private Button btn_write;
	private Boolean loading = true;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_interior, container, false);
		initResources(v);
		initEvents();

		return v;
	}


	private void initResources(View v){

		btn_write=(Button)v.findViewById(R.id.btn_write);
		btn_write.setText("쓰기");
		btn_write.bringToFront();

		lv_main = (ListView) v.findViewById(R.id.lv_interior_view);
		mInteriorItemArrayList = new ArrayList<InteriorEntity>();

		View footerView = ((LayoutInflater)getActivity().
				getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);

		mListAdapter = new InteriorAdapter(getActivity().
				getApplicationContext(), mInteriorItemArrayList, R.layout.custom_view_interior);

		//footerview를  listview 제일 하단에 붙임 
		lv_main.addFooterView(footerView);
		lv_main.setAdapter(mListAdapter);

		//Load the first 5 items
		Thread thread =  new Thread(null, loadListItems);
		thread.start();


	}

	private void initEvents(){
		btn_write.setOnClickListener(clickListener);
		lv_main.setOnScrollListener(scrollListener);
	}

	private Runnable loadListItems = new Runnable(){
		@Override
		public void run(){
			loading = true;

			//Reset the array that holds the new items
			//mInteriorItemArrayList = new ArrayList<InteriorEntity>();

			//Simulate a delay, delete this on a production environment!
			try { Thread.sleep(500);
			} catch (InterruptedException e) {}

			mHandler.sendEmptyMessage(0);

		}
	};

	private Runnable loadMoreListItems = new Runnable() {			
		@Override
		public void run() {
			loading = true;

			//Reset the array that holds the new items
			mInteriorItemArrayList = new ArrayList<InteriorEntity>();

			//Simulate a delay, delete this on a production environment!
			try { Thread.sleep(500);
			} catch (InterruptedException e) {}

			mHandler.sendEmptyMessage(1);

		}
	};	


	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				//처음 로딩할때 몇개로딩할지 여기서 결정하고 보여주는거.
				for(int i=0;i<5 ;i++){
					mListAdapter.add();
				}
				mListAdapter.notifyDataSetChanged();
				//Done loading more.
				loading = false;
				break;
			case 1:
				//스크롤해서 리스트3개씩 추가해주는부분 
				for (int i = 0; i < 3; i++) {		
					mListAdapter.add();	
				}
				mListAdapter.notifyDataSetChanged();
				loading = false;
				break;
			case 2:
				break;
			case 3:
				break;
			}
		}
	};

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(getActivity(),SelectWriteActivity.class);
			startActivity(intent);
		}
	};

	private OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			//what is the bottom iten that is visible
			int lastInScreen = firstVisibleItem + visibleItemCount;				

			//is the bottom item visible & not loading more already ? Load more !
			if((lastInScreen == totalItemCount) && !(loading)){

				Thread thread =  new Thread(null, loadMoreListItems);
				thread.start();
			}

		}
	};

}

