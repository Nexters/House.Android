package com.nexters.house.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nexters.house.R;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.adapter.InteriorAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.entity.reqcode.AP0007;
import com.nexters.house.handler.AbstractHandler;
import com.nexters.house.handler.ArticleHandler;
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
	
	private ArticleHandler<AP0001> mAP0001Handler;
	private ArticleHandler<AP0007> mAP0007Handler;
	
	private OnScrollListener mScrollListener;
	
	public InteriorFragment(MainActivity mainActivity) {
		this.mMainActivity = mainActivity;
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
		mLvMain = (ListView) v.findViewById(R.id.lv_interior_view);
		mInteriorItemArrayList = new ArrayList<InteriorEntity>();
		
		mAP0001Handler = new ArticleHandler<AP0001>(mMainActivity, "AP0001");
		mAP0007Handler = new ArticleHandler<AP0007>(mMainActivity, "AP0007");
		
		View footerView = ((LayoutInflater)getActivity().
				getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);

		mListAdapter = new InteriorAdapter(getActivity().
				getApplicationContext(), mInteriorItemArrayList, R.layout.custom_view_interior, mMainActivity);
		mListAdapter.setHandler(mAP0007Handler);
		
		//footerview를  listview 제일 하단에 붙임 
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
			public synchronized void onScroll(AbsListView view, int firstVisibleItem,
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
		super.onResume();
		// init List
		mListAdapter.clear();
		addInteriorList(0);
	}

	private void initEvents(){
		mLvMain.setOnScrollListener(mScrollListener);
	}
	
	public void processAP0001(){
    	AbstractHandler.Handler handler = new AbstractHandler.Handler() {
			public void handle(APICode resCode) {
				InteriorAdapter listAdapter = mListAdapter;
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
				addInteriorList(0);
			}
		};
		mAP0007Handler.setHandler(handler);
	}
	
	public void addInteriorList(long interiorNo){
		if(mArticleTask != null && !(mArticleTask.getStatus() == Status.FINISHED))
			return ;
//		Log.d("interiorNo", "interiorNo = " + interiorNo);
		AP0001 ap = new AP0001();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setOrderType("new");
		ap.setReqPo(0);
		ap.setReqPoCnt(3);
		ap.setReqPoNo(interiorNo);
		
		mAP0001Handler.setOneTranData(ap);
		mArticleTask = new PostMessageTask(mMainActivity, mAP0001Handler, ArticleHandler.LIST_INTERIOR);
		mArticleTask.setShowLoadingProgressDialog(false);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}

	public InteriorEntity mockEntity(){
		InteriorEntity e = new InteriorEntity();
		e.badge = 1;
		e.content = "인테리어 입니다 ~~~~";
		e.id = "newId";
		e.imageUrls = new ArrayList<String>(){{
			add("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-xfp1/v/t1.0-9/10402950_570244953084785_2207844659246242948_n.jpg?oh=b19d78a504af3a54501e629f0383da87&oe=5448A4C6&__gda__=1413348687_974d19e8b5ddcf217cb99b77b0186685");
			add("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/t1.0-9/10487348_570244969751450_1480175892860352468_n.jpg");
			add("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xpa1/t1.0-9/1546376_570244983084782_3616217572638065925_n.jpg");
			add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
			add("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10524374_570245013084779_7454008372005256632_n.jpg?oh=4761db9f33b72709585016c2649c747e&oe=5434C617&__gda__=1413811119_55884851b246ddb301725a0a78cacc84");
			}};; 
		e.comment = 1;
		e.like = 2;
		return e;
	}
}