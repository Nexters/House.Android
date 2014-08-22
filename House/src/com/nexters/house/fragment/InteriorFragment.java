package com.nexters.house.fragment;

import java.util.*;

import uk.co.senab.actionbarpulltorefresh.library.*;
import uk.co.senab.actionbarpulltorefresh.library.listeners.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;

public class InteriorFragment extends Fragment implements OnRefreshListener {

	private final String TAG = "InteriorFragment";

	private PullToRefreshLayout mPullToRefreshLayout;

	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private ListView lv_main;
	private InteriorAdapter mListAdapter;
	private ImageView btn_write;
	private Boolean loading = true;
	private TextView tvContent;
	private FragmentActivity mFragmentActivity;

	public InteriorFragment(FragmentActivity fragmentActivity) {
		this.mFragmentActivity = fragmentActivity;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_interior, container, false);
		initResources(v);
		initEvents();

		return v;
	}



	@SuppressLint("InflateParams")
	private void initResources(View v){

		mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity())
		.allChildrenArePullable()
		.listener(this)
		.setup(mPullToRefreshLayout);

		btn_write=(ImageView)v.findViewById(R.id.btn_write);
		btn_write.bringToFront();


		mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.ptr_layout);
		lv_main = (ListView) v.findViewById(R.id.lv_interior_view);
		mInteriorItemArrayList = new ArrayList<InteriorEntity>();

		View footerView = ((LayoutInflater)getActivity().
				getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);

		mListAdapter = new InteriorAdapter(getActivity().
				getApplicationContext(), mInteriorItemArrayList, R.layout.custom_view_interior, mFragmentActivity);

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

			mHandler.sendEmptyMessage(0);

		}
	};

	private Runnable loadMoreListItems = new Runnable() {			
		@Override
		public void run() {
			loading = true;

			mHandler.sendEmptyMessage(1);

		}
	};	

	private Runnable refreshListItem = new Runnable(){
		@Override
		public void run(){
			mHandler.sendEmptyMessage(2);
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

			Log.d("Click View Id","Soyoon = " + Integer.toString(v.getId()));
			switch(v.getId()){
			case R.id.btn_write:
				Intent intent=new Intent(getActivity(),InteriorWriteActivity.class);
				startActivity(intent);
				break;

			}
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

	@Override
	public void onRefreshStarted(View view) {
		/**
		 * Simulate Refresh with 4 seconds sleep
		 */
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@SuppressWarnings("serial")
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				InteriorEntity e = new InteriorEntity();
				e.id = "refreshId";
				e.category ="refresh";
				e.content = "새로고쳐서 만들어지는 컨텐츠 리스트 ㅠㅠ 라이브러리는 힘드네요  content";
				e.image_urls = new ArrayList<String>(){{
					add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
				}};
				e.badge = 0;
				e.reply = 0;
				//e.scrap = 0;
				//e.share = 0;
				mInteriorItemArrayList.add(0,e);
				mListAdapter.notifyDataSetChanged();
				// Notify PullToRefreshLayout that the refresh has finished
				mPullToRefreshLayout.setRefreshComplete();
			}
		}.execute();
	}

}