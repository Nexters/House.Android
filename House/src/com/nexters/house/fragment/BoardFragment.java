package com.nexters.house.fragment;

import java.util.*;

import org.springframework.http.MediaType;

import uk.co.senab.actionbarpulltorefresh.library.*;
import uk.co.senab.actionbarpulltorefresh.library.listeners.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0007;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.handler.AbstractHandler;
import com.nexters.house.handler.ArticleHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class BoardFragment extends Fragment {
	private ArrayList<BoardEntity> mBoardItemArrayList;
	private ListView mLvMain;
	private BoardAdapter mListAdapter;

	private MainActivity mMainActivity;

	private PostMessageTask mArticleTask;

	private ArticleHandler<AP0001> mAP0001Handler;
	private ArticleHandler<AP0007> mAP0007Handler;

	private OnScrollListener mScrollListener;

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
	private void initResources(View v) {
		mLvMain = (ListView) v.findViewById(R.id.lv_board_view);
		mBoardItemArrayList = new ArrayList<BoardEntity>();

		mAP0001Handler = new ArticleHandler<AP0001>(mMainActivity, "AP0001");
		mAP0007Handler = new ArticleHandler<AP0007>(mMainActivity, "AP0007");
		
		View footerView = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter,
				null, false);

		mListAdapter = new BoardAdapter(mMainActivity,
				mBoardItemArrayList, mMainActivity);

		// footerview를 listview 제일 하단에 붙임
		mLvMain.addFooterView(footerView);
		mLvMain.setAdapter(mListAdapter);

		// Post List
		processAP0001();
		processAP0007();
		
		// Scroll
		mScrollListener = new OnScrollListener() {
			boolean isState;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				isState = true;
			}

			@Override
			public synchronized void onScroll(AbsListView view,
					int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				// what is the bottom item that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;

				// is the bottom item visible & not loading more already ? Load
				// more !
				if (isState && (lastInScreen == totalItemCount)
						&& (mArticleTask.getStatus() == Status.FINISHED)) {
					if (mBoardItemArrayList.size() > 0)
						addSudatalkList(mBoardItemArrayList
								.get(mBoardItemArrayList.size() - 1).no);
					else
						addSudatalkList(0);
					isState = false;
				}
			}
		};

	}

	@Override
	public void onResume() {
		super.onResume();
		// init List
		mListAdapter.clear();
		addSudatalkList(0);
	}
	
	
	private void initEvents() {
		mLvMain.setOnScrollListener(mScrollListener);
	}
	
	public void processAP0001(){
    	AbstractHandler.Handler handler = new AbstractHandler.Handler() {
			public void handle(APICode resCode) {
				BoardAdapter listAdapter = mListAdapter;
				List<AP0001> apList = resCode.getTranData();
				AP0001 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0001.class);
				
				for(int i=0; i<ap.getResCnt(); i++){
					AP0001Res res = ap.getRes().get(i);
					
					ArrayList<String> imgUrls = new ArrayList<String>();
					for(int j=0; j<res.brdImg.size(); j++)
						imgUrls.add(mMainActivity.getString(R.string.base_uri) + res.brdImg.get(j).brdOriginImg);
					listAdapter.add(res.brdNo, res.brdId, new String(res.brdContents) + " - " + res.brdNo, imgUrls, res.brdLikeCnt, res.brdCommentCnt);
				}
//				Log.d("resCnt", "resCnt : " + ap.getResCnt());
				listAdapter.notifyDataSetChanged();
			}
		};
		mAP0001Handler.setHandler(handler);
	}
	
	public void processAP0007(){
    	AbstractHandler.Handler handler = new AbstractHandler.Handler() {
			public void handle(APICode resCode) {
				AP0007 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0007.class);
				
				mListAdapter.clear();
				addSudatalkList(0);
			}
		};
		mAP0007Handler.setHandler(handler);
	}
	
	public void addSudatalkList(long talkNo){
		if(mArticleTask != null && !(mArticleTask.getStatus() == Status.FINISHED))
			return ;
//		Log.d("interiorNo", "interiorNo = " + interiorNo);
		AP0001 ap = new AP0001();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setOrderType("new");
		ap.setReqPo(0);
		ap.setReqPoCnt(3);
		ap.setReqPoNo(talkNo);
		
		mAP0001Handler.setOneTranData(ap);
		mArticleTask = new PostMessageTask(mMainActivity, mAP0001Handler, ArticleHandler.LIST_INTERIOR);
		mArticleTask.setShowLoadingProgressDialog(false);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}
}