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

	private TextView tvLikeCnt;
	private TextView tvReplyCnt;
	private EditText etReply;
	private ToggleButton btnLike;
	private ToggleButton btnScrap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_detail_page);

		initActionBar();
		initResources();
		
		setListViewHeightBasedOnChildren(lvContent);

		setListViewHeightBasedOnChildren(lvReply);
		overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
	}

	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
	}

	@SuppressLint("InflateParams")
	private void initResources() {

		lvContent = (ListView) findViewById(R.id.lv_interior_image);
		lvReply = (ListView) findViewById(R.id.lv_reply);

		mImageArrayList = new ArrayList<ContentEntity>();
		mReplyArrayList = new ArrayList<ReplyEntity>();

		ivImage = (ImageView) findViewById(R.id.image_row);
		
		tvLikeCnt=(TextView)findViewById(R.id.tv_cnt_likes);
		tvReplyCnt=(TextView)findViewById(R.id.tv_cnt_reply);

		btnLike=(ToggleButton)findViewById(R.id.btn_like);
		btnScrap=(ToggleButton)findViewById(R.id.btn_scrap);
		
		etReply=(EditText)findViewById(R.id.edittxt_reply);
		
		mMainListAdapter = new ContentDetailAdapter(this, mImageArrayList, R.layout.image_row);
		mReplyAdapter = new ReplyAdapter(this,mReplyArrayList, R.layout.reply);

		lvContent.setAdapter(mMainListAdapter);
		//15를 사진 갯수대로 수정하여야.
		for (int itemCount = 0; itemCount < 15; itemCount++) {
			ContentEntity mExamEntity = new ContentEntity();

			mImageArrayList.add(mExamEntity);

		}

		mMainListAdapter.notifyDataSetChanged();

		lvReply.setAdapter(mReplyAdapter);
		//20을 댓글 갯수로 바꾸어야
		for (int itemCount = 0; itemCount < 3; itemCount++) {
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
	public void likeClicked(View v){ //like버튼 눌렀을때 cnt증가
		
		int before=Integer.parseInt((String) tvLikeCnt.getText());
		if(btnLike.isChecked())
			tvLikeCnt.setText(Integer.toString(before+1));
		else
			tvLikeCnt.setText(Integer.toString(before-1));
	}
	public void sendClicked(View v){ //댓글 전송버튼 눌렀을때 cnt 증가 및 댓글리스트 하나더 생성
	
		if(etReply.length()>0){ 
			int before=Integer.parseInt((String) tvReplyCnt.getText());
			tvReplyCnt.setText(Integer.toString(before+1));
		
			ReplyEntity mReplyEntity = new ReplyEntity();
			mReplyArrayList.add(mReplyEntity);
			mReplyAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(lvReply);
		}
		else
			Toast.makeText(this, "1자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();

	}
	public void scrapClicked(View v){
		//스크랩 버튼 눌렀을때
	}
}