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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nexters.house.R;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.adapter.InteriorAdapter;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.entity.reqcode.AP0007;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class InteriorFragment extends Fragment {
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private ListView mLvMain;
	private InteriorAdapter mListAdapter;
	private ImageView btnDown;
	private TextView tvContent;
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
		View v = inflater.inflate(R.layout.fragment_interior, container, false);
		initResource(v);
		initEvent();
		return v;
	}

	@SuppressLint("InflateParams")
	private void initResource(View v){
		mLvMain = (ListView) v.findViewById(R.id.lv_interior_view);
		mInteriorItemArrayList = new ArrayList<InteriorEntity>();
		
		View footerView = ((LayoutInflater)getActivity().
				getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);

		mListAdapter = new InteriorAdapter(getActivity().
				getApplicationContext(), mInteriorItemArrayList, mMainActivity);
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0007 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0007.class);
				if(mLvMain.getSelectedItemPosition() > 0)
					mLvMain.setSelection(mLvMain.getSelectedItemPosition() - 1);
				mListAdapter.clear();
				addInteriorList(0);
			}
		};
		TransHandler<AP0007> articleHandler = new TransHandler<AP0007>("AP0007", handler);
		mListAdapter.setHandler(articleHandler);
		
		//footerview를  listview 제일 하단에 붙임 
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
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//what is the bottom item that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;				

				//is the bottom item visible & not loading more already ? Load more !
				if(isState && (lastInScreen == totalItemCount) && (mArticleTask.getStatus() == Status.FINISHED)){
					if(mInteriorItemArrayList.size() > 0)
						addInteriorList(mInteriorItemArrayList.get(mInteriorItemArrayList.size() - 1).no);
					else 
						addInteriorList(0);
					isState = false;
				}
			}
		};
	}

	@Override
	public void onResume() {
		// init List
		mListAdapter.clear();
		addInteriorList(0);
		super.onResume();
	}

	private void initEvent(){
		mLvMain.setOnScrollListener(mScrollListener);
	}

	public void addInteriorList(long interiorNo){
		if(mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		
//		Log.d("interiorNo", "interiorNo = " + interiorNo);
		AP0001 ap = new AP0001();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setOrderType("new");
		ap.setReqPo(0);
		ap.setReqPoCnt(3);
		ap.setReqPoType(AP0001.NORMAL);
		ap.setReqPoNo(interiorNo);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				InteriorAdapter listAdapter = mListAdapter;
				List<AP0001> apList = resCode.getTranData();
				AP0001 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0001.class);
				
				for(int i=0; i<ap.getResCnt(); i++){
					AP0001Res res = ap.getRes().get(i);
					
					ArrayList<String> imgUrls = new ArrayList<String>();
					for(int j=0; j<res.brdImg.size(); j++)
						imgUrls.add(mMainActivity.getString(R.string.base_uri) + res.brdImg.get(j).brdOriginImg);
					listAdapter.add(res.brdNo, res.brdId, res.brdNm, res.brdProfileImg, res.brdCreated, new String(res.brdContents), imgUrls, res.brdLikeCnt, res.brdCommentCnt);
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