package com.nexters.house.adapter;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nexters.house.R;
import com.nexters.house.core.SessionManager;
import com.nexters.house.fragment.SetFragment;
import com.nexters.house.fragment.SetNickNameFragment;
import com.nexters.house.fragment.SetPwFragment;
import com.nexters.house.fragment.SetVersionFragment;
/**
 * Created by Leucosian on 4/17/14.
 */
public class SetFragmentPagerAdapter extends FragmentStatePagerAdapter {
    public static final int PAGE_COUNT = 2;

    public Fragment mSetFragment = null;
    public Fragment mNickNameFragment = null;
    public Fragment mVersionFragment = null;
    public Fragment mPwFragment = null;
    public Fragment[] mTargetFragments = null;
    private ViewPager mViewPager = null;
    private ActionBar mActionBar = null;

    public SetFragmentPagerAdapter(FragmentManager fm, ViewPager viewPager, ActionBar actionBar) {
        super(fm);
        // init
        this.mViewPager = viewPager;
        this.mActionBar = actionBar;
        initResource();
    }

    private void initResource() {
        mSetFragment = SetFragment.newInstance();
        mTargetFragments = new Fragment[2];
        mTargetFragments[0] = SetFragment.newInstance();
        mTargetFragments[1] = SetFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return mTargetFragments[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // 사용자 정의

    public void initActionBar() {
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        if (mViewPager.getCurrentItem() == 0) {
            mActionBar.setCustomView(R.layout.action_set);
        } else if (mViewPager.getCurrentItem() == 1) {
            mActionBar.setCustomView(R.layout.action_set2);

            View view = mActionBar.getCustomView();
            Button button = (Button) view.findViewById(R.id.btn_back);
            button.setOnClickListener(new MyFragmentPagerListener(this, mViewPager));
        }
    }

    public void setFragment(int id){
        switch (id){
            case R.layout.fragment_set_nickname :
                mTargetFragments[1] = SetNickNameFragment.newInstance();
                break;
            case R.layout.fragment_set_version :
                mTargetFragments[1] = SetVersionFragment.newInstance();
                break;
            case R.layout.fragment_set_pw :
            	mTargetFragments[1] = SetPwFragment.newInstance();
            	break;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public void changeFragmentPager(int index){
    	 notifyDataSetChanged();
         mViewPager.setCurrentItem(index);
         initActionBar();
    }
    
    public static class MyFragmentPagerListener implements View.OnClickListener {
        private SetFragmentPagerAdapter mSetFragmentPagerAdapter;
        private ViewPager mViewPager;

        public MyFragmentPagerListener(SetFragmentPagerAdapter setFragmentPagerAdapter, ViewPager viewPager) {
            this.mSetFragmentPagerAdapter = setFragmentPagerAdapter;
            this.mViewPager = viewPager;
        }

        @Override
        public void onClick(View v) {
            int index = 0;
            switch (v.getId()) {
                case R.id.btn_nickname:
                	index = 1;
                    mSetFragmentPagerAdapter.setFragment(R.layout.fragment_set_nickname);
                    break;
                case R.id.btn_version:
                    index = 1;
                    mSetFragmentPagerAdapter.setFragment(R.layout.fragment_set_version);
                    break;
                case R.id.btn_modifypw:
                	index = 1;
                	mSetFragmentPagerAdapter.setFragment(R.layout.fragment_set_pw);
                	break;
                case R.id.btn_back:
                    index = 0;
                    break;
            }
            mSetFragmentPagerAdapter.changeFragmentPager(index);
        }
    }
}
