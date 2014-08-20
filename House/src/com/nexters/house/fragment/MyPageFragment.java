package com.nexters.house.fragment;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.nexters.house.R;
import com.nexters.house.activity.SetActivity;
import com.nexters.house.adapter.MyPageAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.thread.DownloadImageTask;


public class MyPageFragment extends Fragment {
	
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_PICK_FROM_GALLERY = 2;
    public static final int REQUEST_CODE_CROP_IMAGE = 3;
    
    //private ImageView mBtnIvPhoto;
    
    private ImageView mHouseProfile;
    private ProfilePictureView mFacebookProfile;
    
	private ImageView mBtnSetting; 
    private GridView mGridview;
    
    private Activity mActivity;
    private View mView;
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_mypage, container, false);
		
		initResource();
		initEvent();
		return mView;
	}

	public void initResource(){
		mHouseProfile = (ImageView) mView.findViewById(R.id.house_profile);
		mFacebookProfile = (ProfilePictureView) mView.findViewById(R.id.facebook_profile);
		
		mGridview = (GridView) mView.findViewById(R.id.gv_mypage);
		mBtnSetting = (ImageView) mView.findViewById(R.id.btn_setting);
		
		
//		Settings
		SessionManager sessionManager = SessionManager.getInstance(mActivity);
		
		HashMap<String, String> userDetails = sessionManager.getUserDetails();
		String imgSrc = userDetails.get(SessionManager.KEY_PROFILE_PATH);
		
		if(sessionManager.getLoginType() == SessionManager.FACEBOOK){
			mFacebookProfile.setProfileId(imgSrc);
			mFacebookProfile.setVisibility(View.VISIBLE);
		} else {
			new DownloadImageTask(mHouseProfile).execute(imgSrc);
			mHouseProfile.setVisibility(View.VISIBLE);
		}
	}
	
	public void initEvent(){
		mGridview.setAdapter(new MyPageAdapter(mActivity.getApplicationContext()));
		mGridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
		
		mBtnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, SetActivity.class);
		        startActivity(intent);
			}
		});
	}
}