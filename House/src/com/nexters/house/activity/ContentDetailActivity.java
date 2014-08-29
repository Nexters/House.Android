package com.nexters.house.activity;

import java.util.*;

import android.annotation.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.nexters.house.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;

public class ContentDetailActivity extends SherlockFragmentActivity {

	private final String TAG = "ContentDetailActivity";

	private ListView lvContent;
	private ListView lvReply;
	private ArrayList<ContentEntity> mImageArrayList;
	private ArrayList<ReplyEntity> mReplyArrayList;
	private ContentDetailAdapter mMainListAdapter;
	private ReplyAdapter mReplyAdapter;
	private ImageView ivImage;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setWindowAnimations(R.anim.hide);
		setContentView(R.layout.content_detail_page);
		
		initActionBar();
		initResources();
		setListViewHeightBasedOnChildren(lvContent);

		overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
	}
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);}
		
	@SuppressLint("InflateParams")
	private void initResources() {

		lvContent = (ListView) findViewById(R.id.lv_interior_image);
		lvReply = (ListView) findViewById(R.id.lv_reply);

		mImageArrayList = new ArrayList<ContentEntity>();
		mReplyArrayList = new ArrayList<ReplyEntity>();

		ivImage = (ImageView) findViewById(R.id.image_row);

		mMainListAdapter = new ContentDetailAdapter(this, mImageArrayList, R.layout.image_row);
		mReplyAdapter = new ReplyAdapter(this,mReplyArrayList, R.layout.reply);

		lvContent.setAdapter(mMainListAdapter);
		for (int itemCount = 0; itemCount < 15; itemCount++) {
			ContentEntity mExamEntity = new ContentEntity();

			mImageArrayList.add(mExamEntity);

		}

		mMainListAdapter.notifyDataSetChanged();

		lvReply.setAdapter(mReplyAdapter);
		for (int itemCount = 0; itemCount < 20; itemCount++) {
			ReplyEntity mReplyEntity = new ReplyEntity();

			mReplyArrayList.add(mReplyEntity);

		}

	}
	
	private void initActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		getSupportActionBar().setCustomView(R.layout.action_main);
	}

	// 스크롤뷰안에 리스트뷰 스크롤제대로 되도록
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}