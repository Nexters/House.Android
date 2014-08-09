package com.nexters.house.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nexters.house.R;
import com.nexters.house.fragment.BoardFragment;
import com.nexters.house.fragment.InteriorFragment;
import com.nexters.house.fragment.MyPageFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_INTERIOR = 0;
	public final static int FRAGMENT_BOARD = 1;
	public final static int FRAGMENT_MYPAGE = 2;

	private Button mBtnInterior;
	private Button mBtnBoard;
	private Button mBtnMypage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		boolean logout = getIntent().getBooleanExtra("logout", false);
		if (logout) {
			Log.d("logout", "logout : ");
			startActivity(new Intent(this, StartActivity.class));
			finish();
			return;
		}

		initResources();
		initEvent();

		mCurrentFragmentIndex = FRAGMENT_INTERIOR;
		fragmentReplace(mCurrentFragmentIndex);
	}

	private void initResources() {
		mBtnInterior = (Button) findViewById(R.id.btn_interior);
		mBtnBoard = (Button) findViewById(R.id.btn_board);
		mBtnMypage = (Button) findViewById(R.id.btn_mypage);
	}

	private void initEvent() {
		mBtnInterior.setOnClickListener(this);
		mBtnBoard.setOnClickListener(this);
		mBtnMypage.setOnClickListener(this);
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

	// http://tools.android.com/tips/non-constant-fields 참조
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_interior) {
			mCurrentFragmentIndex = FRAGMENT_INTERIOR;
			fragmentReplace(mCurrentFragmentIndex);
		} else if (v.getId() == R.id.btn_board) {
			mCurrentFragmentIndex = FRAGMENT_BOARD;
			fragmentReplace(mCurrentFragmentIndex);
		} else if (v.getId() == R.id.btn_mypage) {
			mCurrentFragmentIndex = FRAGMENT_MYPAGE;
			fragmentReplace(mCurrentFragmentIndex);
		}
	}

}
