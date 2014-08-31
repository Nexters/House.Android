package com.nexters.house.activity;

import java.util.*;

import org.springframework.http.*;

import android.annotation.*;
import android.content.*;
import android.os.AsyncTask.Status;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;
import com.nexters.house.entity.reqcode.*;
import com.nexters.house.handler.*;
import com.nexters.house.thread.*;
import com.nexters.house.utils.*;

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
	private TextView mReplyCnt;
	// Replay
	private EditText mEditReply;
	private Button mBtnSendReply;
	private ReplyAdapter mReplyAdapter;
	private ListView mReplyContent;

	private ScrollView mScrollView;

	private PostMessageTask mArticleTask;
	private ArticleHandler<AP0003> mAP0003Handler;
	private ArticleHandler<AP0004> mAP0004Handler;
	private ArticleHandler<AP0005> mAP0005Handler;
	private ArticleHandler<AP0008> mAP0008Handler;

	// List
	private ArrayList<String> mImageArrayList;
	private ArrayList<ReplyEntity> mReplyArrayList;

	// Article No
	private long brdNo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_detail_page);

		initActionBar();
		initResources();

		setListViewHeightBasedOnChildren(mContentImage);
		setListViewHeightBasedOnChildren(mReplyContent);
		overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
	}

	@SuppressLint("InflateParams")
	private void initResources() {
		brdNo = getIntent().getLongExtra("brdNo", 0);

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
		mReplyCnt = (TextView) findViewById(R.id.tv_cnt_reply);

		mBtnLike = (ToggleButton) findViewById(R.id.btn_like);
		mBtnScrap = (ToggleButton) findViewById(R.id.btn_scrap);

		// reply
		mReplyContent = (ListView) findViewById(R.id.lv_reply);

		mEditReply = (EditText) findViewById(R.id.edittxt_reply);
		mBtnSendReply = (Button) findViewById(R.id.btn_send_reply);

		mReplyAdapter = new ReplyAdapter(this, mReplyArrayList, R.layout.reply);
		mReplyContent.setAdapter(mReplyAdapter);

		mScrollView = (ScrollView) findViewById(R.id.sv_content);

		processAP0003();
		processAP0004();
		processAP0005();
		processAP0008();

		// 20을 댓글 갯수로 바꾸어야
		for (int itemCount = 0; itemCount < 3; itemCount++) {
			ReplyEntity mReplyEntity = new ReplyEntity();
			mReplyArrayList.add(mReplyEntity);
		}
		// init
		setContent(brdNo);
	}

	public void initEvent() {
		mBtnLike.setOnClickListener(this);
		mBtnScrap.setOnClickListener(this);
		mBtnSendReply.setOnClickListener(this);
	}

	public void processAP0003() {
		mAP0003Handler = new ArticleHandler<AP0003>(this, "AP0003");
		AbstractHandler.Handler handler = new AbstractHandler.Handler() {
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
				mLikeCnt.setText("" + ap.getBrdLikeCnt());
				mReplyCnt.setText("" + ap.getBrdCommentCnt());

				mImageAdapter.notifyDataSetChanged();
				ContentImageAdapter.setListViewHeightBasedOnChildren(mContentImage);
			}
		};
		mAP0003Handler.setHandler(handler);
	}

	public void processAP0004() {
		mAP0004Handler = new ArticleHandler<AP0004>(this, "AP0004");
		AbstractHandler.Handler handler = new AbstractHandler.Handler() {
			public void handle(APICode resCode) {
				AP0004 ap = JacksonUtils.hashMapToObject((HashMap) resCode
						.getTranData().get(0), AP0004.class);

				mLikeCnt.setText(ap.getLikeCnt());

			}
		};
		mAP0004Handler.setHandler(handler);

		int before = Integer.parseInt((String) mLikeCnt.getText());
		if (mBtnLike.isChecked())
			mLikeCnt.setText(Integer.toString(before + 1));
		else
			mLikeCnt.setText(Integer.toString(before - 1));
	}

	public void processAP0005() {

	}

	public void processAP0008() {

	}

	public void setContent(long brdNo) {
		if (mArticleTask != null
				&& !(mArticleTask.getStatus() == Status.FINISHED))
			return;
		Log.d("brdNo", "brdNo = " + brdNo);
		AP0003 ap = new AP0003();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setReqPoNo(brdNo);

		mAP0003Handler.setOneTranData(ap);
		mArticleTask = new PostMessageTask(this, mAP0003Handler,
				ArticleHandler.LIST_INTERIOR);
		mArticleTask.setShowLoadingProgressDialog(false);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
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

	public void sendClicked(View v) { // 댓글 전송버튼 눌렀을때 cnt 증가 및 댓글리스트 하나더 생성
		if (mEditReply.length() > 0) {
			int before = Integer.parseInt((String) mReplyCnt.getText());
			mReplyCnt.setText(Integer.toString(before + 1));

			ReplyEntity mReplyEntity = new ReplyEntity();
			mReplyArrayList.add(0, mReplyEntity);
			mReplyAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(mReplyContent);

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.hideSoftInputFromWindow(mEditReply.getWindowToken(), 0);
		} else
			Toast.makeText(this, "1자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_like:

			break;
		case R.id.btn_scrap:

			break;
		case R.id.btn_send_reply:

			break;
		}
	}




}