package com.nexters.house.fragment;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.Fragment;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.widget.*;
import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.core.*;
import com.nexters.house.thread.*;
import com.nexters.house.utils.*;
import com.nexters.house.view.*;


public class MyPageFragment extends Fragment{

	public static final int REQUEST_TAKE_PHOTO = 1;
	public static final int REQUEST_PICK_FROM_GALLERY = 2;
	public static final int REQUEST_CODE_CROP_IMAGE = 3;

	private ImageView mHouseProfile;
	private ProfilePictureView mFacebookProfile;

	private ImageView mBtnSetting; 
	private ExpandableHeightGridView mGridview;

	private Activity mActivity;
	private View mView;

	private Bitmap mImageBitmap;
	private int mIvPhotoWidth;

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
		//원래 기본사진크기 가져오려고했는데..안됨 우선은 숫자로 넣어둠.
		mIvPhotoWidth = 200;
		initEvent();		 


		return mView;
	}


	public void initResource(){
		mHouseProfile = (ImageView) mView.findViewById(R.id.house_profile);
		mFacebookProfile = (ProfilePictureView) mView.findViewById(R.id.facebook_profile);

		mGridview = (ExpandableHeightGridView) mView.findViewById(R.id.gv_mypage);
		mGridview.setExpanded(true);
		mBtnSetting = (ImageView) mView.findViewById(R.id.btn_setting);


		//		Settings
		SessionManager sessionManager = SessionManager.getInstance(mActivity);

		HashMap<String, String> userDetails = sessionManager.getUserDetails();
		String imgSrc = userDetails.get(SessionManager.KEY_PROFILE_PATH);

		if(sessionManager.getLoginType() == SessionManager.FACEBOOK){
			mFacebookProfile.setProfileId(imgSrc);
			mFacebookProfile.setVisibility(View.VISIBLE);
		} else if(imgSrc != null){
			new DownloadImageTask(mHouseProfile).execute(imgSrc);
			mHouseProfile.setVisibility(View.VISIBLE);
		}
	}

	public void initEvent(){
		mGridview.setAdapter(new MyPageAdapter(mActivity.getApplicationContext()));
		mGridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(mActivity,ContentDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);
			}
		});

		mHouseProfile.setOnClickListener(btnClickListener);
		mBtnSetting.setOnClickListener(btnClickListener);
	}


	private View.OnClickListener btnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_setting:
				Intent intent = new Intent(mActivity, SetActivity.class);
				startActivity(intent);
				break;
			case R.id.house_profile:
				dispatchPickFromGalleryIntent();
				break;
			}
		}
	};

	//    private void showDialogChoosingPhoto() {
	//        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
	//        builder.setItems(R.array.menu_choosing_photo_dialog_items, new DialogInterface.OnClickListener() {
	//                   public void onClick(DialogInterface dialog, int which) {
	//                       switch (which) {
	//                           case 0:
	//                               dispatchTakePictureIntent();
	//                               break;
	//                           case 1:
	//                               dispatchPickFromGalleryIntent();
	//                               break;
	//                       }
	//                   }
	//               });
	//        builder.show();
	//    }

	private void dispatchPickFromGalleryIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, REQUEST_PICK_FROM_GALLERY);
	}

	//    private void dispatchTakePictureIntent() {
	//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	//        intent.putExtra(MediaStore.EXTRA_OUTPUT, TempFileManager.getImageFileUri());
	//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
	//    }

	private Intent makeCropIntent(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// [주의] outputX, outputY가 커지면 메모리 오류 때문에 프로세스가 중지됨.
		// 비트맵을 반환하지 말고 파일 형태로 저장하도록 변경해야할 듯.
		intent.putExtra("outputX", 256);
		intent.putExtra("outputY", 256);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		return intent;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_TAKE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = makeCropIntent(TempFileManager.getImageFileUri());
				startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
			}
			break;
		case REQUEST_PICK_FROM_GALLERY:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				Intent intent = makeCropIntent(uri);
				startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
			}
			break;
		case REQUEST_CODE_CROP_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					mImageBitmap = extras.getParcelable("data");
					if (mImageBitmap != null) {
						mHouseProfile.setImageBitmap(ImageManagingHelper.getCroppedBitmap(mImageBitmap, mIvPhotoWidth));
						TempFileManager.saveBitmapToImageFile(mImageBitmap);
					} else {
						Toast.makeText(mActivity, "잘린 이미지가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mActivity, "잘린 이미지가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		TempFileManager.deleteImageFile();
	}

}