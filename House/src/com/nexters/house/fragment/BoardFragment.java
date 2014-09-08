package com.nexters.house.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.nexters.house.R;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.adapter.BoardAdapter;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.BoardEntity;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0003;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.entity.reqcode.AP0007;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class BoardFragment extends Fragment {
	private ArrayList<BoardEntity> mBoardItemArrayList;
	private ListView mLvMain;
	private BoardAdapter mListAdapter;

	private MainActivity mMainActivity;

	private PostMessageTask mArticleTask;

	private OnScrollListener mScrollListener;


	@Override
	public void onAttach(Activity activity) {
		this.mMainActivity = (MainActivity) activity;
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
		
		View footerView = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter,
				null, false);

		mListAdapter = new BoardAdapter(mMainActivity,
				mBoardItemArrayList, mMainActivity);
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0007 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0007.class);
				
				mListAdapter.clear();
				addSudatalkList(0);
			}
		};
		TransHandler<AP0007> articleHandler = new TransHandler<AP0007>("AP0007", handler);
		mListAdapter.setHandler(articleHandler);
		
		// footerview를 listview 제일 하단에 붙임
		mLvMain.addFooterView(footerView);
		mLvMain.setAdapter(mListAdapter);

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

	public void addSudatalkList(long talkNo){
		if(mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		
//		Log.d("talkNo", "talkNo = " + talkNo);
		AP0001 ap = new AP0001();
		ap.setType(CodeType.SUDATALK_TYPE);
		ap.setOrderType("new");
		ap.setReqPo(0);
		ap.setReqPoCnt(3);
		ap.setReqPoType(AP0001.NORMAL);
		ap.setReqPoNo(talkNo);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				BoardAdapter listAdapter = mListAdapter;
				List<AP0001> apList = resCode.getTranData();
				AP0001 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0001.class);
				
				for(int i=0; i<ap.getResCnt(); i++){
					AP0001Res res = ap.getRes().get(i);
					
					ArrayList<String> imgUrls = new ArrayList<String>();
					for(int j=0; j<res.brdImg.size(); j++)
						imgUrls.add(mMainActivity.getString(R.string.base_uri) + res.brdImg.get(j).brdOriginImg);
					listAdapter.add(res.brdNo, res.brdId, res.brdNm, res.brdProfileImg, res.brdCreated, new String(res.brdContents), res.brdSubject, res.brdCateNm, imgUrls, res.brdLikeCnt, res.brdCommentCnt);
				}
//				Log.d("resCnt", "resCnt : " + ap.getResCnt());
				listAdapter.notifyDataSetChanged();
			}
		};
		
		TransHandler<AP0001> articleHandler = new TransHandler<AP0001>("AP0001", handler, ap);
		mArticleTask = new PostMessageTask(mMainActivity, articleHandler);
		mArticleTask.setShowLoadingProgressDialog(false);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}
}