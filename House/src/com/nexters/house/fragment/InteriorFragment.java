package com.nexters.house.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nexters.house.R;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.adapter.InteriorAdapter;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class InteriorFragment extends Fragment {
	private final static boolean UP = true;
	private final static boolean DOWN = false;
	private final static boolean ASYNC = true;
	private final static boolean SYNC = false;
	private final static int DEFAULT_SIZE = 3;
	
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private PullToRefreshListView mPullRefreshListView;
	private InteriorAdapter mListAdapter;
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
		View v = inflater.inflate(R.layout.fragment_interior, container, false);
		initResource(v);
		initEvent();
		return v;
	}

	@SuppressLint("InflateParams")
	private void initResource(View v){
		mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.lv_interior_view);
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				long no = 0;
				if(mInteriorItemArrayList.size() > 0)
					no = mInteriorItemArrayList.get(0).no;
				addInteriorList(no, DOWN, SYNC);
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				long no = 0;
				if(mInteriorItemArrayList.size() > 0)
					no = mInteriorItemArrayList.get(mInteriorItemArrayList.size() - 1).no;
				addInteriorList(no, UP, SYNC);
			}
		});
		mInteriorItemArrayList = new ArrayList<InteriorEntity>();
		mListAdapter = new InteriorAdapter(getActivity().
				getApplicationContext(), mInteriorItemArrayList, mMainActivity, mPullRefreshListView.getRefreshableView());
		mPullRefreshListView.setAdapter(mListAdapter);
	}

	@Override
	public void onResume() {
		Log.d("RESULT ", "RESULT_OK " + mMainActivity.RESULT_STATUS + " - " + mMainActivity.RESULT_WRITE);
		
		if(mMainActivity.RESULT_STATUS == mMainActivity.RESULT_WRITE){
			initInteriorList(0);
			mMainActivity.RESULT_STATUS = mMainActivity.RESULT_NONE;
		} else {
			if(mInteriorItemArrayList.size() > 0)
				initInteriorList(mInteriorItemArrayList.get(mListAdapter.getSelectedPosition()).no + 1);
			else
				initInteriorList(0);
		}
		super.onResume();
	}

	
	public void initInteriorList(long interiorNo){
		mListAdapter.clear();
		addInteriorList(interiorNo, UP, ASYNC);
	}
	
	private void initEvent(){}

	public void addInteriorList(long interiorNo, final boolean upAndDown, boolean status){
		if(!status && mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		AP0001 ap = new AP0001();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setOrderType("new");
		ap.setReqPo(0);
		if(upAndDown)
			ap.setReqPoCnt(DEFAULT_SIZE);
		else
			ap.setReqPoCnt(-DEFAULT_SIZE);
		ap.setReqPoType(AP0001.NORMAL);
		ap.setReqPoNo(interiorNo);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				List<AP0001> apList = resCode.getTranData();
				AP0001 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0001.class);
				
				for(int i=0; i<ap.getResCnt(); i++){
					AP0001Res res = ap.getRes().get(i);
					
					ArrayList<String> imgUrls = new ArrayList<String>();
					for(int j=0; j<res.brdImg.size(); j++)
						imgUrls.add(mMainActivity.getString(R.string.base_uri) + res.brdImg.get(j).brdOriginImg);
					
					InteriorEntity e = new InteriorEntity();
					e.no = res.brdNo;
					e.id = res.brdId;
					e.name = res.brdNm;
					e.profileImg = res.brdProfileImg;
					e.created = res.brdCreated;
					e.content = new String(res.brdContents);
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