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

import com.nexters.house.R;
import com.nexters.house.adapter.SetFragmentPagerAdapter;

public class SetActivity extends FragmentActivity {
    private SetFragmentPagerAdapter mSetViewPagerAdapter = null;
    private ViewPager mViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initResources();
//        Log.d("action", "action : " + getActionBar());
        initActionBar();
    }

	private void initResources() {
        // Create
        this.mViewPager = (ViewPager) findViewById(R.id.set_view_pager);
        this.mSetViewPagerAdapter = new SetFragmentPagerAdapter(getSupportFragmentManager(), mViewPager, getActionBar());

        // Set
        this.mViewPager.setAdapter(mSetViewPagerAdapter);
        // view pager 클릭시 이벤트 설정을 위해서
        this.mViewPager.setTag(mSetViewPagerAdapter);
    }

    private void initActionBar() {
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        getActionBar().setCustomView(R.layout.action_set);
    }
}