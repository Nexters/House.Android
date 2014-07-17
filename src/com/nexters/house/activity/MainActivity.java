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
	public final static int FRAGMENT_BEST = 0;
	public final static int FRAGMENT_INTERIOR = 1;
	public final static int FRAGMENT_BOARD = 2;
	public final static int FRAGMENT_MYPAGE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn_best = (Button) findViewById(R.id.btn_best);
		btn_best.setOnClickListener(this);
		Button btn_interior = (Button) findViewById(R.id.btn_interior);
		btn_interior.setOnClickListener(this);
		Button btn_board = (Button) findViewById(R.id.btn_board);
		btn_board.setOnClickListener(this);
		Button btn_mypage = (Button) findViewById(R.id.btn_mypage);
		btn_mypage.setOnClickListener(this);
		
		mCurrentFragmentIndex = FRAGMENT_BEST;

		fragmentReplace(mCurrentFragmentIndex);
	}

	public void fragmentReplace(int reqNewFragmentIndex) {

		Fragment newFragment = null;

		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);

		newFragment = getFragment(reqNewFragmentIndex);

		// replace fragment
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.ll_fragment, newFragment);

		// Commit the transaction
		transaction.commit();

	}

	private Fragment getFragment(int idx) {
		Fragment newFragment = null;
		
		switch (idx) {
		case FRAGMENT_BEST:
			newFragment = new BestFragment();
			break;
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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btn_best:
			mCurrentFragmentIndex = FRAGMENT_BEST;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.btn_interior:
			mCurrentFragmentIndex = FRAGMENT_INTERIOR;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.btn_board:
			mCurrentFragmentIndex = FRAGMENT_BOARD;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.btn_mypage:
			mCurrentFragmentIndex = FRAGMENT_MYPAGE;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		}

	}

}
