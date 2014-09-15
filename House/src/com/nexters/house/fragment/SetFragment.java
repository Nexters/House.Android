package com.nexters.house.fragment;

import org.springframework.http.MediaType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nexters.house.R;
import com.nexters.house.activity.SetActivity;
import com.nexters.house.adapter.SetFragmentPagerAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.reqcode.CM0004;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;

public class SetFragment extends Fragment implements View.OnClickListener {
    private LinearLayout mBtnLogout;
    private LinearLayout mBtnSend;
    private LinearLayout mBtnNickName;
    private LinearLayout mBtnVersion;
    private LinearLayout mBtnWithdraw;
    private LinearLayout mBtnModifypw;
    private SetActivity mSetActivity;
    private View mView;

    private SetFragmentPagerAdapter.MyFragmentPagerListener setFragmentPagerListener;

    private PostMessageTask mAuthTask;
    
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
        mSetActivity = (SetActivity) activity;
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
        
        initResource();
        initEvent();
//        initActionBar();
        return mView;
    }

    private void initResource() {
        mBtnLogout = (LinearLayout) mView.findViewById(R.id.btn_logout);
        mBtnSend = (LinearLayout) mView.findViewById(R.id.btn_send);
        mBtnNickName = (LinearLayout) mView.findViewById(R.id.btn_nickname);
        mBtnVersion = (LinearLayout) mView.findViewById(R.id.btn_version);
        mBtnWithdraw = (LinearLayout) mView.findViewById(R.id.btn_withdraw);
        mBtnModifypw = (LinearLayout) mView.findViewById(R.id.btn_modifypw);
        
        if(SessionManager.getInstance(mSetActivity).getUserDetails().get(SessionManager.HOUSE) == null)
        	mBtnNickName.setVisibility(View.GONE);
        ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.set_view_pager);
        SetFragmentPagerAdapter myFragmentPagerAdapter = (SetFragmentPagerAdapter) viewPager.getTag();
        setFragmentPagerListener = new SetFragmentPagerAdapter.MyFragmentPagerListener(myFragmentPagerAdapter, viewPager);
    }

    private void initEvent() {
        // 현재창에서 해결
        mBtnLogout.setOnClickListener(this);
        mBtnWithdraw.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        
        //
        mBtnNickName.setOnClickListener(setFragmentPagerListener);
        mBtnVersion.setOnClickListener(setFragmentPagerListener);
        mBtnModifypw.setOnClickListener(setFragmentPagerListener);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                dispatchLogout();
                break;
            case R.id.btn_send:
                dispatchSend();
                break;
            case R.id.btn_withdraw:
                dispatchWithdraw();
                break;
        }
    }

    private void dispatchLogout() {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mSetActivity);
        confirmDialog.setTitle("로그아웃 하시겠습니까?");
        confirmDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            	SessionManager.getInstance(mSetActivity).logoutUser();
            }
        });
        confirmDialog.setNegativeButton("아니오", null);
        confirmDialog.show();
    }

    private void dispatchSend() {
    	Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","cultisttp@gmail.com", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
		startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void dispatchWithdraw() {
    	if(mAuthTask != null && mAuthTask.getStatus() != mAuthTask.getStatus().FINISHED)
			return ;
		
		String usrId = SessionManager.getInstance(mSetActivity).getUserDetails().get(SessionManager.KEY_EMAIL);
		
		CM0004 cm = new CM0004();
		cm.setUsrId(usrId);
//		cm.setCustName(name);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				SessionManager.getInstance(mSetActivity).logoutUser();
				mSetActivity.showResult("회원탈퇴 되었습니다.");
			}
		};
		// Post Aricle 
    	TransHandler<CM0004> authHandler = new TransHandler<CM0004>("CM0004", handler, cm);
    	mAuthTask = new PostMessageTask(mSetActivity, authHandler);
    	mAuthTask.execute(MediaType.APPLICATION_JSON);
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
