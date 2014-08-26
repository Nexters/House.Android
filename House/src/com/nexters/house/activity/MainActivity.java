package com.nexters.house.activity;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nexters.house.*;
import com.nexters.house.fragment.*;

public class MainActivity extends SherlockFragmentActivity  implements OnClickListener {
	final String TAG = "MainActivity";

	public int mCurrentFragmentIndex;
	public int mPrevFragmentIndex;
	public final static int FRAGMENT_INTERIOR = 0;
	public final static int FRAGMENT_BOARD = 1;
	public final static int FRAGMENT_MYPAGE = 2;
	public final static int FRAGMENT_DETAIL_INTERIOR = 3;
	
	private Button mBtnInterior;
	private Button mBtnBoard;
	private Button mBtnMypage;
	private View mLineInterior;
	private View mLineBoard;
	private View mLineMypage;

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
		initResources();
		initEvent();

		changeFragment(FRAGMENT_INTERIOR);
	}

	private void initResources(){
		mBtnInterior = (Button) findViewById(R.id.btn_interior);
		mBtnBoard = (Button) findViewById(R.id.btn_board);
		mBtnMypage = (Button) findViewById(R.id.btn_mypage);
		mLineInterior = (View) findViewById(R.id.header_line_interior);
		mLineBoard = (View) findViewById(R.id.header_line_board);
		mLineMypage = (View) findViewById(R.id.header_line_mypage);

	}

	private void initEvent(){
		mBtnInterior.setOnClickListener(this);
		mBtnBoard.setOnClickListener(this);
		mBtnMypage.setOnClickListener(this);
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
		
		if(mCurrentFragmentIndex == FRAGMENT_DETAIL_INTERIOR)
			fragmentTransaction.addToBackStack(null);
		
		fragmentTransaction.replace(R.id.ll_fragment, newFragment);
		fragmentTransaction.commit();
	}

	private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_INTERIOR:
			newFragment = new InteriorFragment(this);
			break;
		case FRAGMENT_BOARD:
			newFragment = new BoardFragment();
			break;
		case FRAGMENT_MYPAGE:
			newFragment = new MyPageFragment();
			break;
		case FRAGMENT_DETAIL_INTERIOR :
			newFragment = new ContentDetailFragment();
			break;
		default:
			Log.d(TAG, "Unhandle case");
			break;
		}
		return newFragment;
	}
	
	public void setCurrentFragmentIndex(int mCurrentFragmentIndex) {
		this.mPrevFragmentIndex = this.mCurrentFragmentIndex;
		this.mCurrentFragmentIndex = mCurrentFragmentIndex;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_interior) {
			changeFragment(FRAGMENT_INTERIOR);
		} else if(v.getId() == R.id.btn_board) {
			changeFragment(FRAGMENT_BOARD);
		} else if(v.getId() == R.id.btn_mypage) {
			changeFragment(FRAGMENT_MYPAGE);
		} 
	}

	public void changeFragment(int currentFragmentIndex){
		setCurrentFragmentIndex(currentFragmentIndex);
		setButtonResource(mCurrentFragmentIndex);
		fragmentReplace();
	}
	
	public void setButtonResource(int currentFragmentIndex){
		mBtnBoard.setTextColor(Color.parseColor("#959595"));
		mBtnMypage.setTextColor(Color.parseColor("#959595"));
		mBtnInterior.setTextColor(Color.parseColor("#959595"));
		mLineInterior.setBackgroundColor(Color.parseColor("#FFFFFF"));
		mLineBoard.setBackgroundColor(Color.parseColor("#FFFFFF"));
		mLineMypage.setBackgroundColor(Color.parseColor("#FFFFFF"));
		
		if(mCurrentFragmentIndex == FRAGMENT_INTERIOR || mCurrentFragmentIndex == FRAGMENT_DETAIL_INTERIOR) {
			mBtnInterior.setTextColor(Color.parseColor("#8DCEC0"));
			mLineInterior.setBackgroundColor(Color.parseColor("#8DCEC0"));	
		}else if(mCurrentFragmentIndex == FRAGMENT_BOARD) {
			mBtnBoard.setTextColor(Color.parseColor("#8DCEC0"));
			mLineBoard.setBackgroundColor(Color.parseColor("#8DCEC0"));
		}else if(mCurrentFragmentIndex == FRAGMENT_MYPAGE){
			mBtnMypage.setTextColor(Color.parseColor("#8DCEC0"));
			mLineMypage.setBackgroundColor(Color.parseColor("#8DCEC0"));
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		if(mCurrentFragmentIndex == FRAGMENT_DETAIL_INTERIOR){
			changeFragment(FRAGMENT_INTERIOR);
		} 
	}
}
