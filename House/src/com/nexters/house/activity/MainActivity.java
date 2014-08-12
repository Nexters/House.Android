package com.nexters.house.activity;

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

	private Button btn_interior;
	private Button btn_board;
	private Button btn_mypage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initResources();
		initEvent();

		mCurrentFragmentIndex = FRAGMENT_INTERIOR;
		fragmentReplace(mCurrentFragmentIndex);

	}


	private void initResources(){
		btn_interior = (Button) findViewById(R.id.btn_interior);
		btn_board = (Button) findViewById(R.id.btn_board);
		btn_mypage = (Button) findViewById(R.id.btn_mypage);

	}

	private void initEvent(){

		btn_interior.setOnClickListener(this);
		btn_board.setOnClickListener(this);
		btn_mypage.setOnClickListener(this);

	}


	public void fragmentReplace(int reqNewFragmentIndex) {
		Fragment newFragment = null;
		newFragment = getFragment(reqNewFragmentIndex);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		ft.replace(R.id.ll_fragment, newFragment);
		ft.commit();
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
			mCurrentFragmentIndex = FRAGMENT_INTERIOR;
			fragmentReplace(mCurrentFragmentIndex);
		} else if(v.getId() == R.id.btn_board) {
			mCurrentFragmentIndex = FRAGMENT_BOARD;
			fragmentReplace(mCurrentFragmentIndex);
		} else if(v.getId() == R.id.btn_mypage) {
			mCurrentFragmentIndex = FRAGMENT_MYPAGE;
			fragmentReplace(mCurrentFragmentIndex);
		} 
	}

}
