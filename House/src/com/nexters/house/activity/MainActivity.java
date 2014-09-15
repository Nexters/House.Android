package com.nexters.house.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.animation.*;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nexters.house.*;
import com.nexters.house.fragment.*;
import com.nexters.house.utils.*;

public class MainActivity extends AbstractAsyncFragmentActivity implements OnClickListener {
	final String TAG = "MainActivity";

	public int mCurrentFragmentIndex;
	public int mPrevFragmentIndex;
	public final static int FRAGMENT_INTERIOR = 0;
	public final static int FRAGMENT_BOARD = 1;
	public final static int FRAGMENT_MYPAGE = 2;

	public static final int ADD_TAG_DIALOG=3378;
	private Boolean isVisible = true;
	private Button mBtnInterior;
	private Button mBtnBoard;
	private Button mBtnMypage;
	private ImageView mBtnWrite;
	private View mLineInterior;
	private View mLineBoard;
	private View mLineMypage;

	public final static int RESULT_NONE = 0;
	public final static int RESULT_WRITE = 1;
	public final static int RESULT_VIEW = 2;
	public int RESULT_STATUS = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		boolean logout = getIntent().getBooleanExtra("logout", false);
		if (logout) {
			startActivity(new Intent(this, StartActivity.class));
			finish();
			return;
		}
		initActionBar();
		initResource();
		initEvent();

		changeFragment(FRAGMENT_INTERIOR);
	}

	private void initResource(){
		mBtnInterior = (Button) findViewById(R.id.btn_interior);
		mBtnBoard = (Button) findViewById(R.id.btn_board);
		mBtnMypage = (Button) findViewById(R.id.btn_mypage);
		mLineInterior = (View) findViewById(R.id.header_line_interior);
		mLineBoard = (View) findViewById(R.id.header_line_board);
		mLineMypage = (View) findViewById(R.id.header_line_mypage);

		mBtnWrite = (ImageView) findViewById(R.id.btn_write);
		mBtnWrite.bringToFront();

	}

	private void initEvent(){
		mBtnInterior.setOnClickListener(this);
		mBtnBoard.setOnClickListener(this);
		mBtnMypage.setOnClickListener(this);
		mBtnWrite.setOnClickListener(this);
	}

	private void initActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		getSupportActionBar().setCustomView(R.layout.action_main);
	}

	public void fragmentReplace() {
		Fragment newFragment = null;
		newFragment = getFragment(mCurrentFragmentIndex);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();

		int diffLocation = mPrevFragmentIndex - mCurrentFragmentIndex;
		if(diffLocation > 0)
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
		else if(diffLocation < 0)
			fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

		//		if(mCurrentFragmentIndex == FRAGMENT_DETAIL_INTERIOR)
		//			fragmentTransaction.addToBackStack(null);

		fragmentTransaction.replace(R.id.ll_fragment, newFragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("RESULT ", "RESULT_OK 1 " + RESULT_OK + " - " + resultCode);
		
		if(resultCode == RESULT_OK)
			RESULT_STATUS = RESULT_WRITE;
		super.onActivityResult(requestCode, resultCode, data);
	}
		
	private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_INTERIOR:
			newFragment = new InteriorFragment();
			break;
		case FRAGMENT_BOARD:
			newFragment = new BoardFragment();
			break;
		case FRAGMENT_MYPAGE:
			newFragment = new MyPageFragment();
			break;
		default:
			Log.d(TAG, "Unhandle case");
			break;
		}
		return newFragment;
	}

	public void setWriteButtonVisibility(int idx){
		Animation hide = AnimationUtils.loadAnimation(this, R.anim.hide);
		Animation show = AnimationUtils.loadAnimation(this, R.anim.show);
		if (idx == FRAGMENT_MYPAGE) {
			if(isVisible){
				isVisible = false;
				mBtnWrite.setAnimation(hide);
				mBtnWrite.setClickable(false);
			}
		}else{
			if(!isVisible){
				isVisible = true;
				mBtnWrite.setAnimation(show);
				mBtnWrite.setClickable(true);
			}
		}
	}
	
	public void setCurrentFragmentIndex(int mCurrentFragmentIndex) {
		this.mPrevFragmentIndex = this.mCurrentFragmentIndex;
		this.mCurrentFragmentIndex = mCurrentFragmentIndex;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_interior:
			changeFragment(FRAGMENT_INTERIOR);
			break;
		case R.id.btn_board:
			changeFragment(FRAGMENT_BOARD);
			break;
		case R.id.btn_mypage:
			changeFragment(FRAGMENT_MYPAGE);
			break;
		case R.id.btn_write:
			if(this.mCurrentFragmentIndex == FRAGMENT_INTERIOR){
				Intent intent=new Intent(this,InteriorWriteActivity.class);
				startActivityForResult(intent, 0);
			} else if(this.mCurrentFragmentIndex == FRAGMENT_BOARD){
				Intent intent=new Intent(this,TalkWriteActivity.class);
				startActivityForResult(intent, 0);
			}
			break;
		}
	}
	
	public void changeFragment(int currentFragmentIndex){
		setCurrentFragmentIndex(currentFragmentIndex);
		setButtonResource(mCurrentFragmentIndex);
		setWriteButtonVisibility(currentFragmentIndex);
		fragmentReplace();
	}

	public void setButtonResource(int currentFragmentIndex){
		mBtnBoard.setTextColor(Color.parseColor("#959595"));
		mBtnMypage.setTextColor(Color.parseColor("#959595"));
		mBtnInterior.setTextColor(Color.parseColor("#959595"));
		mLineInterior.setBackgroundColor(Color.parseColor("#FFFFFF"));
		mLineBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));
		mLineMypage.setBackgroundColor(Color.parseColor("#FFFFFF"));

		if(mCurrentFragmentIndex == FRAGMENT_INTERIOR ) {
			mBtnInterior.setTextColor(Color.parseColor("#008996"));
			mLineInterior.setBackgroundColor(Color.parseColor("#008996"));	
		}else if(mCurrentFragmentIndex == FRAGMENT_BOARD) {
			mBtnBoard.setTextColor(Color.parseColor("#008996"));
			mLineBoard.setBackgroundColor(Color.parseColor("#008996"));
		}else if(mCurrentFragmentIndex == FRAGMENT_MYPAGE){
			mBtnMypage.setTextColor(Color.parseColor("#008996"));
			mLineMypage.setBackgroundColor(Color.parseColor("#008996"));
		}
	}
	public Dialog onCreateDialog(int id) {
	    AlertDialog dialog = null;
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    switch (id) {
	    case ADD_TAG_DIALOG:
	        builder.setMessage("삭제하시겠습니까?").setCancelable(
	                false).setPositiveButton("예",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {

	                    }
	                }).setNegativeButton("아니요",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {
	                        dialog.cancel();
	                    }
	                });

	        dialog = builder.create();
	        break;
	    default:
	        dialog = null;
	    }
	    dialog.setOwnerActivity(this);
	    return dialog;
	}

}
