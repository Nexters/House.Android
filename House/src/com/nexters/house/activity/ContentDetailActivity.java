package com.nexters.house.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nexters.house.R;
import com.nexters.house.adapter.ContentImageAdapter;
import com.nexters.house.adapter.ReplyAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.ReplyEntity;
import com.nexters.house.entity.reqcode.AP0003;
import com.nexters.house.entity.reqcode.AP0003.AP0003Comment;
import com.nexters.house.entity.reqcode.AP0004;
import com.nexters.house.entity.reqcode.AP0005;
import com.nexters.house.entity.reqcode.AP0008;
import com.nexters.house.entity.reqcode.AP0009;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class ContentDetailActivity extends AbstractAsyncFragmentActivity
		implements View.OnClickListener {
	private final String TAG = "ContentDetailActivity";

	// Article
	private ImageView mProfileImage;
	private TextView mProfileName;
	private TextView mCreated;
	private ListView mContentImage;
	private TextView mContent;
	private ContentImageAdapter mImageAdapter;

	// Like Scrap
	private ToggleButton mBtnLike;
	private ToggleButton mBtnScrap;

	private TextView mLikeCnt;
	private TextView mScrapCnt;
	private TextView mReplyCnt;

	// Replay
	private EditText mEditReply;
	private Button mBtnSendReply;
	private ReplyAdapter mReplyAdapter;
	private ListView mReplyContent;

	private ScrollView mScrollView;

	private PostMessageTask mPostTask;
	
	// List
	private ArrayList<String> mImageArrayList;
	private ArrayList<ReplyEntity> mReplyArrayList;

	// Article No
	private long brdNo;
	private int brdType;
	private String usrId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_detail_page);

		initActionBar();
		initResources();

		initEvent();
		
		setListViewHeightBasedOnChildren(mContentImage);
		setListViewHeightBasedOnChildren(mReplyContent);

		overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
	}

	@SuppressLint("InflateParams")
	private void initResources() {
		brdNo = getIntent().getLongExtra("brdNo", 0);
		brdType = getIntent().getIntExtra("brdType", CodeType.INTERIOR_TYPE);
		usrId = SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL);
		
		mImageArrayList = new ArrayList<String>();
		mReplyArrayList = new ArrayList<ReplyEntity>();

		// article
		mProfileImage = (ImageView) findViewById(R.id.iv_user_profile_image);
		mProfileName = (TextView) findViewById(R.id.tv_user_profile_name);
		mCreated = (TextView) findViewById(R.id.tv_created);
		mContentImage = (ListView) findViewById(R.id.lv_interior_image);
		mContent = (TextView) findViewById(R.id.tv_content);

		mImageAdapter = new ContentImageAdapter(this, mImageArrayList,
				R.layout.image_row);
		mContentImage.setAdapter(mImageAdapter);

		// like reply
		mLikeCnt = (TextView) findViewById(R.id.tv_cnt_likes);
		mScrapCnt = (TextView) findViewById(R.id.tv_cnt_scrap);
		mReplyCnt = (TextView) findViewById(R.id.tv_cnt_reply);

		mBtnLike = (ToggleButton) findViewById(R.id.btn_like);
		mBtnScrap = (ToggleButton) findViewById(R.id.btn_scrap);

		// reply
		mReplyContent = (ListView) findViewById(R.id.lv_reply);

		mEditReply = (EditText) findViewById(R.id.edittxt_reply);
		mBtnSendReply = (Button) findViewById(R.id.btn_send_reply);

		mReplyAdapter = new ReplyAdapter(this, mReplyArrayList, R.layout.reply, brdType);
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				listComment();
			}
		};
		TransHandler<AP0009> articleHandler = new TransHandler<AP0009>("AP0009", handler);
		mReplyAdapter.setHandler(articleHandler);
		mReplyContent.setAdapter(mReplyAdapter);

		mScrollView = (ScrollView) findViewById(R.id.sv_content);

		// init
		setContents(brdNo);
	}

	public void initEvent() {
		mBtnLike.setOnClickListener(this);
		mBtnScrap.setOnClickListener(this);
		mBtnSendReply.setOnClickListener(this);
	}
	
	public void writeComment(){
		AP0008 ap = new AP0008();
		ap.setType(brdType);
		ap.setReqPoNo(brdNo);
		ap.setCommentId(usrId);
		ap.setCommentContents(mEditReply.getText().toString().getBytes());
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				listComment();
			}
		};
		TransHandler<AP0008> articleHandler = new TransHandler<AP0008>("AP0008", handler, ap);
		
		mPostTask = new PostMessageTask(this, articleHandler);
		mPostTask.setShowLoadingProgressDialog(true);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public void toggleLikeCnt(){
		AP0004 ap = new AP0004();
		ap.setType(brdType);
		ap.setBrdNo(brdNo);
		ap.setUsrId(usrId);

		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0004 ap = JacksonUtils.hashMapToObject((HashMap) resCode
						.getTranData().get(0), AP0004.class);
				mLikeCnt.setText("" + ap.getLikeCnt());
			}
		};
		TransHandler<AP0004> articleHandler = new TransHandler<AP0004>("AP0004", handler, ap);
		
		mPostTask = new PostMessageTask(this, articleHandler);
		mPostTask.setShowLoadingProgressDialog(true);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public void toggleScrapCnt(){
		AP0005 ap = new AP0005();
		ap.setType(brdType);
		ap.setBrdNo(brdNo);
		ap.setUsrId(usrId);

		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0005 ap = JacksonUtils.hashMapToObject((HashMap) resCode
						.getTranData().get(0), AP0005.class);
				mScrapCnt.setText("" + ap.getScrapCnt());
			}
		};
		TransHandler<AP0005> articleHandler = new TransHandler<AP0005>("AP0005", handler, ap);
		
		mPostTask = new PostMessageTask(this, articleHandler);
		mPostTask.setShowLoadingProgressDialog(true);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public void setContents(long brdNo) {
		AP0003 ap = new AP0003();
		ap.setType(brdType);
		ap.setReqPoNo(brdNo);
		ap.setUsrId(usrId);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0003 ap = JacksonUtils.hashMapToObject((HashMap) resCode
						.getTranData().get(0), AP0003.class);

				// mProfileImage.setImageBitmap(bm);
				mProfileName.setText(ap.getBrdNm());
				mCreated.setText(ap.getBrdCreated());
				mContent.setText(new String(ap.getBrdContents()));
				
				List<String> imgs = ap.getBrdImg();
				for (int i = 0; i < imgs.size(); i++)
					mImageArrayList.add(getApplicationContext().getString(R.string.base_uri) + imgs.get(i));

				List<AP0003Comment> comments = ap.getBrdComment();
				for (int i = 0; i < comments.size(); i++){
					ReplyEntity reply = new ReplyEntity();
					reply.no = comments.get(i).brdCommentNo;
					reply.id = comments.get(i).brdCommentId;
					reply.profileImg = comments.get(i).brdCommentProfileImg;
					reply.name = comments.get(i).brdCommentNm;
					reply.content = new String(comments.get(i).brdCommentContents);
					reply.created = comments.get(i).brdCommentCreated;
					mReplyArrayList.add(reply);
				}
				mLikeCnt.setText("" + ap.getBrdLikeCnt());
				mScrapCnt.setText("" + ap.getBrdScrapCnt());
				mReplyCnt.setText("" + ap.getBrdCommentCnt());
				Log.d("ap.getBrdScrapState()", "ap.getBrdScrapState() : " + ap.getBrdScrapState());
				
				mBtnLike.setChecked((ap.getBrdLikeState() == 1)? true : false);
				mBtnScrap.setChecked((ap.getBrdScrapState() == 1)? true : false);
				
				mReplyAdapter.notifyDataSetChanged();
				mImageAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(mContentImage);
				setListViewHeightBasedOnChildren(mReplyContent);
			}
		};
		
		TransHandler<AP0003> articleHandler = new TransHandler<AP0003>("AP0003", handler, ap);
		mPostTask = new PostMessageTask(this, articleHandler);
		mPostTask.setShowLoadingProgressDialog(false);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public void listComment(){
		AP0003 ap = new AP0003();
		ap.setType(brdType);
		ap.setReqPoNo(brdNo);
		ap.setUsrId(usrId);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0003 ap = JacksonUtils.hashMapToObject((HashMap) resCode
						.getTranData().get(0), AP0003.class);
//				Log.d("AP0003Comment", "AP0003Comment : ");
				List<AP0003Comment> comments = ap.getBrdComment();
				mReplyArrayList.clear();
				for (int i = 0; i < comments.size(); i++){
					ReplyEntity reply = new ReplyEntity();
					reply.no = comments.get(i).brdCommentNo;
					reply.id = comments.get(i).brdCommentId;
					reply.profileImg = comments.get(i).brdCommentProfileImg;
					reply.name = comments.get(i).brdCommentNm;
					reply.content = new String(comments.get(i).brdCommentContents);
					reply.created = comments.get(i).brdCommentCreated;
					mReplyArrayList.add(reply);
				}
				mReplyCnt.setText("" + ap.getBrdCommentCnt());
				mEditReply.setText("");
				mReplyAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(mReplyContent);
			}
		};
		TransHandler<AP0003> articleHandler = new TransHandler<AP0003>("AP0003", handler, ap);

		mPostTask = new PostMessageTask(this, articleHandler);
		mPostTask.setShowLoadingProgressDialog(false);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_like:
			toggleLikeCnt();
			break;
		case R.id.btn_scrap:
			toggleScrapCnt();
			break;
		case R.id.btn_send_reply:
			writeComment();
			break;
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
	}
	
	private void initActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		getSupportActionBar().setCustomView(R.layout.action_main);
	}
	
	// 스크롤뷰안에 리스트뷰 스크롤제대로 되도록
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

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