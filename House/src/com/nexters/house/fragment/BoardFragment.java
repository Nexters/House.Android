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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nexters.house.R;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.adapter.BoardAdapter;
import com.nexters.house.adapter.InteriorAdapter;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.BoardEntity;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class BoardFragment extends Fragment {
	private final static boolean UP = true;
	private final static boolean DOWN = false;
	private final static boolean ASYNC = true;
	private final static boolean SYNC = false;
	private final static int DEFAULT_SIZE = 3;
	
	private ArrayList<BoardEntity> mBoardItemArrayList;
	private PullToRefreshListView mPullRefreshListView;
	private BoardAdapter mListAdapter;

	private MainActivity mMainActivity;
	private PostMessageTask mArticleTask;

	@Override
	public void onAttach(Activity activity) {
		this.mMainActivity = (MainActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_board, container, false);
		initResource(v);
		initEvent();
		return v;
	}

	@SuppressLint("InflateParams")
	private void initResource(View v) {
		mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.lv_board_view);
		mBoardItemArrayList = new ArrayList<BoardEntity>();
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				long no = 0;
				if(mBoardItemArrayList.size() > 0)
					no = mBoardItemArrayList.get(0).no;
				addSudatalkList(no, DOWN, SYNC);
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				long no = 0;
				if(mBoardItemArrayList.size() > 0)
					no = mBoardItemArrayList.get(mBoardItemArrayList.size() - 1).no;
				addSudatalkList(no, UP, SYNC);
			}
		});
		mListAdapter = new BoardAdapter(mMainActivity,
				mBoardItemArrayList, mMainActivity, mPullRefreshListView.getRefreshableView());
		mPullRefreshListView.setAdapter(mListAdapter);
	}

	@Override
	public void onResume() {
		Log.d("RESULT ", "RESULT_OK " + mMainActivity.RESULT_STATUS + " - " + mMainActivity.RESULT_WRITE);
		
		if(mMainActivity.RESULT_STATUS == mMainActivity.RESULT_WRITE){
			initSudatalkList(0);
			mMainActivity.RESULT_STATUS = mMainActivity.RESULT_NONE;
		} else {
			if(mBoardItemArrayList.size() > 0)
				initSudatalkList(mBoardItemArrayList.get(mListAdapter.getSelectedPosition()).no + 1);
			else
				initSudatalkList(0);
		}
		super.onResume();
	}
	
	public void initSudatalkList(long talkNo){
		Log.d("initSudatalkList", "initSudatalkList = ");
		
		mListAdapter.clear();
		addSudatalkList(talkNo, UP, ASYNC);
	}
	
	private void initEvent() { }

	public void addSudatalkList(long talkNo, final boolean upAndDown, boolean status){
		if(!status && mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		AP0001 ap = new AP0001();
		ap.setType(CodeType.SUDATALK_TYPE);
		ap.setOrderType("new");
		ap.setReqPo(0);
		if(upAndDown)
			ap.setReqPoCnt(DEFAULT_SIZE);
		else
			ap.setReqPoCnt(-DEFAULT_SIZE);
		ap.setReqPoType(AP0001.NORMAL);
		ap.setReqPoNo(talkNo);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				List<AP0001> apList = resCode.getTranData();
				AP0001 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0001.class);
				
				for(int i=0; i<ap.getResCnt(); i++){
					AP0001Res res = ap.getRes().get(i);
					
					ArrayList<String> imgUrls = new ArrayList<String>();
					for(int j=0; j<res.brdImg.size(); j++)
						imgUrls.add(mMainActivity.getString(R.string.base_uri) + res.brdImg.get(j).brdOriginImg);
					BoardEntity e = new BoardEntity();
					e.no = res.brdNo;
					e.id = res.brdId;
					e.name = res.brdNm;
					e.profileImg = res.brdProfileImg;
					e.created = res.brdCreated;
					e.subject = res.brdSubject;
					e.content = new String(res.brdContents);
					e.category = res.brdCateNm;
					e.imageUrls = imgUrls;
					e.comment = res.brdCommentCnt;
					e.like = res.brdLikeCnt;
					
					if(upAndDown)
						mListAdapter.add(e);
					else
						mListAdapter.add(0, e);
				}
				if(!upAndDown)
					mPullRefreshListView.getRefreshableView().setSelection(DEFAULT_SIZE);
				mListAdapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();
			}
		};
		TransHandler<AP0001> articleHandler = new TransHandler<AP0001>("AP0001", handler, ap);
		mArticleTask = new PostMessageTask(mMainActivity, articleHandler);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}
}