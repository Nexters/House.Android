package com.nexters.house.fragment;

import java.util.*;

import uk.co.senab.actionbarpulltorefresh.library.*;
import uk.co.senab.actionbarpulltorefresh.library.listeners.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;

public class BoardFragment extends Fragment implements OnRefreshListener{
	ListView mBoardList;
	View mView;
	
	private PullToRefreshLayout mPullToRefreshLayout;

	private ArrayList<BoardEntity> mBoardItemArrayList;
	private ListView lvMain;
	private BoardAdapter mListAdapter;
	
	private Boolean loading = true;
	private MainActivity mMainActivity;
	
	public BoardFragment(MainActivity mainActivity) {
		this.mMainActivity = mainActivity;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_board, container, false);
		initResources(v);
		initEvents();

		return v;
	}
	
	@SuppressLint("InflateParams")
	private void initResources(View v){

		mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);

		lvMain = (ListView) v.findViewById(R.id.board_list);
		mBoardItemArrayList = new ArrayList<BoardEntity>();

		View footerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);

		mListAdapter = new BoardAdapter(getActivity().getApplicationContext(), mBoardItemArrayList, R.layout.custom_view_board_left, mMainActivity);

		//footerview를  listview 제일 하단에 붙임 
		lvMain.addFooterView(footerView);
		lvMain.setAdapter(mListAdapter);

		//Load the first 5 items
		Thread thread =  new Thread(null, loadListItems);
		thread.start();


	}

//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//
//		ArrayList<String> arrayList = new ArrayList<String>();
//		arrayList.add("content1");
//		arrayList.add("content2");
//		arrayList.add("content3");
//		arrayList.add("content4");
//		arrayList.add("content5");
//		arrayList.add("content6");
//		arrayList.add("content7");
//		arrayList.add("content8");
//		arrayList.add("content9");
//		arrayList.add("content10");
//
//		BoardAdapter boardAdapter = new BoardAdapter(getActivity(), arrayList);
//
//		mBoardList.setAdapter(boardAdapter);
//	}
	
	
	private void initEvents(){
		lvMain.setOnScrollListener(scrollListener);


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

	
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				BoardEntity b = new BoardEntity();
				
				b.id = "Refresh!!";
				
				b.category = "새로고침";
				b.createdTime = "방금";
				b.title = "새로고쳐졌당 :) ";
				b.content = "얼른해서 완성합시다. 이거 화면 늘어나는거 어디까지되는지 테스트도해야되는데. 글자 제한을 몇으로 해야될지 그리고 화면은 얼마나 늘어나게될지 한번 봐봅시다. 인유어하우스 화이팅 마감이 얼마 남지 않았음.";
				
				b.like = 1;
				b.reply = 1;
				
				mBoardItemArrayList.add(0,b);
				mListAdapter.notifyDataSetChanged();
				// Notify PullToRefreshLayout that the refresh has finished
				mPullToRefreshLayout.setRefreshComplete();
			}
		}.execute();
	}

}