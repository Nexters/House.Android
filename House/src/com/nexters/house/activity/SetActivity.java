package com.nexters.house.activity;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nexters.house.R;
import com.nexters.house.adapter.SetFragmentPagerAdapter;

public class SetActivity extends AbstractAsyncFragmentActivity {
	private SetFragmentPagerAdapter mSetFragmentPagerAdapter = null;
	private ViewPager mViewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initResource();
		// Log.d("action", "action : " + getActionBar());
		initActionBar();
	}

	private void initResource() {
		// Create
		this.mViewPager = (ViewPager) findViewById(R.id.set_view_pager);
		this.mSetFragmentPagerAdapter = new SetFragmentPagerAdapter(
				getSupportFragmentManager(), mViewPager, getActionBar());
		// Set
		this.mViewPager.setAdapter(mSetFragmentPagerAdapter);
		// view pager 클릭시 이벤트 설정을 위해서
		this.mViewPager.setTag(mSetFragmentPagerAdapter);
	}

	private void initActionBar() {
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.action_set);
	}

	@Override
	public void onBackPressed() {
		if (mViewPager.getCurrentItem() == 1) {
			mSetFragmentPagerAdapter.changeFragmentPager(0);
			return ;
		}
		super.onBackPressed();
	}
}