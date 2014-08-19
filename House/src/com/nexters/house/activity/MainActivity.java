package com.nexters.house.activity;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.fragment.*;

public class MainActivity extends FragmentActivity implements OnClickListener {

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_INTERIOR = 0;
	public final static int FRAGMENT_BOARD = 1;
	public final static int FRAGMENT_MYPAGE = 2;

	private ImageView mBtnInterior;
	private ImageView mBtnBoard;
	private ImageView mBtnMypage;

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
		

		mCurrentFragmentIndex = FRAGMENT_INTERIOR;
		fragmentReplace(mCurrentFragmentIndex);
	}

	private void initResources(){
		mBtnInterior = (ImageView) findViewById(R.id.btn_interior);
		mBtnBoard = (ImageView) findViewById(R.id.btn_board);
		mBtnMypage = (ImageView) findViewById(R.id.btn_mypage);

	}

	private void initEvent(){
		mBtnInterior.setOnClickListener(this);
		mBtnBoard.setOnClickListener(this);
		mBtnMypage.setOnClickListener(this);
	}
	
    private void initActionBar() {
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        getActionBar().setCustomView(R.layout.action_main);
    }

	public void fragmentReplace(int reqNewFragmentIndex) {
		Fragment newFragment = null;
		newFragment = getFragment(reqNewFragmentIndex);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();

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
		default:
			Log.d(TAG, "Unhandle case");
			break;
		}
		return newFragment;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_interior) {
			mBtnBoard.setImageResource(R.drawable.header_icon_chat);
			mBtnMypage.setImageResource(R.drawable.header_icon_mypage);
			mBtnInterior.setImageResource(R.drawable.header_icon_house_click);
			
			mCurrentFragmentIndex = FRAGMENT_INTERIOR;
			fragmentReplace(mCurrentFragmentIndex);
		} else if(v.getId() == R.id.btn_board) {
			
			mBtnMypage.setImageResource(R.drawable.header_icon_mypage);
			mBtnInterior.setImageResource(R.drawable.header_icon_house);
			mBtnBoard.setImageResource(R.drawable.header_icon_chat_click);
			mCurrentFragmentIndex = FRAGMENT_BOARD;
			fragmentReplace(mCurrentFragmentIndex);
		} else if(v.getId() == R.id.btn_mypage) {
			mBtnBoard.setImageResource(R.drawable.header_icon_chat);
			mBtnInterior.setImageResource(R.drawable.header_icon_house);
			mBtnMypage.setImageResource(R.drawable.header_icon_mypage_click);
			mCurrentFragmentIndex = FRAGMENT_MYPAGE;
			fragmentReplace(mCurrentFragmentIndex);
		} 
	}

}
