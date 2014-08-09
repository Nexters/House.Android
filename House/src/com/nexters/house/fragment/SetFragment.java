package com.nexters.house.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.Session;
import com.kakao.APIErrorResult;
import com.kakao.LogoutResponseCallback;
import com.kakao.UserManagement;
import com.nexters.house.R;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.adapter.SetFragmentPagerAdapter;
import com.nexters.house.core.SessionManager;

public class SetFragment extends Fragment implements View.OnClickListener {
    private LinearLayout mBtnLogout;
    private LinearLayout mBtnSend;
    private LinearLayout mBtnNickName;
    private LinearLayout mBtnVersion;
    private LinearLayout mBtnWithdraw;
    private Activity mActivity;
    private View mView;

    private SetFragmentPagerAdapter.MyFragmentPagerListener setFragmentPagerListener;

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
//        Bundle args = new Bundle();
////        args.put
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (View) inflater.inflate(R.layout.fragment_set, container, false);
        
        initResources();
        initEvents();
//        initActionBar();
        return mView;
    }

    private void initResources() {
        mBtnLogout = (LinearLayout) mView.findViewById(R.id.btn_logout);
        mBtnSend = (LinearLayout) mView.findViewById(R.id.btn_send);
        mBtnNickName = (LinearLayout) mView.findViewById(R.id.btn_nickname);
        mBtnVersion = (LinearLayout) mView.findViewById(R.id.btn_version);
        mBtnWithdraw = (LinearLayout) mView.findViewById(R.id.btn_withdraw);

        ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.set_view_pager);
        SetFragmentPagerAdapter myFragmentPagerAdapter = (SetFragmentPagerAdapter) viewPager.getTag();
        setFragmentPagerListener = new SetFragmentPagerAdapter.MyFragmentPagerListener(myFragmentPagerAdapter, viewPager);
    }

    private void initEvents() {
        // 현재창에서 해결
        mBtnLogout.setOnClickListener(this);
        mBtnWithdraw.setOnClickListener(this);
        //
        mBtnSend.setOnClickListener(setFragmentPagerListener);
        mBtnNickName.setOnClickListener(setFragmentPagerListener);
        mBtnVersion.setOnClickListener(setFragmentPagerListener);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                dispatchLogout();
                break;
//            case R.id.btn_send:
//                dispatchSend();
//                break;
//            case R.id.btn_nickname:
//                dispatchNickName();
//                break;
//            case R.id.btn_version:
//                dispatchVersion();
//                break;
            case R.id.btn_withdraw:
                dispatchWithdraw();
                break;
        }
    }

    private void dispatchLogout() {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mActivity);
        confirmDialog.setTitle("로그아웃 하시겠습니까?");
        confirmDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            	SessionManager.getInstance(mActivity).logoutUser();
            }
        });
        confirmDialog.setNegativeButton("아니오", null);
        confirmDialog.show();
    }

    private void dispatchSend() {

    }

    private void dispatchNickName() {

    }

    private void dispatchVersion() {

    }

    private void dispatchWithdraw() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
