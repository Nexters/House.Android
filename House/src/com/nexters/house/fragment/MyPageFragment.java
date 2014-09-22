package com.nexters.house.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nexters.house.R;
import com.nexters.house.activity.ContentDetailActivity;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.activity.SetActivity;
import com.nexters.house.adapter.MyPageAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.MyPageEntity;
import com.nexters.house.entity.reqcode.AP0001;
import com.nexters.house.entity.reqcode.AP0001.AP0001Res;
import com.nexters.house.entity.reqcode.CM0005;
import com.nexters.house.entity.reqcode.CM0006;
import com.nexters.house.entity.reqcode.CM0006.CM0006Img;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.DownloadImageTask;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.ImageManagingHelper;
import com.nexters.house.utils.JacksonUtils;
import com.nexters.house.utils.TempFileManager;
import com.nexters.house.view.ExpandableHeightGridView;


public class MyPageFragment extends Fragment implements View.OnClickListener {
	public static final int REQUEST_TAKE_PHOTO = 1;
	public static final int REQUEST_PICK_FROM_GALLERY = 2;
	public static final int REQUEST_CODE_CROP_IMAGE = 3;

	private ArrayList<MyPageEntity> mMyPageItemArrayList;
	private ExpandableHeightGridView mGridview;
	private MyPageAdapter mMyPageAdapter;
	private MainActivity mMainActivity;

	private ImageView mHouseProfile;
	private ImageView mBtnSetting; 
	
	private TextView mBtnScrap;
	private TextView mBtnUpload;

	private PostMessageTask mMyPageTask;
	private PostMessageTask mUserTask;
	private Bitmap mImageBitmap;
	
	private int mCodeType;
	private int mPoType;
	
	@Override
	public void onAttach(Activity activity) {
		mMainActivity = (MainActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mypage, container, false);

		initResource(v);
		initEvent();		 
		return v;
	}

	public void initResource(View v){
		mMyPageItemArrayList = new ArrayList<MyPageEntity>();
		mCodeType = CodeType.INTERIOR_TYPE;
		mPoType = AP0001.NORMAL;
		
		mGridview = (ExpandableHeightGridView) v.findViewById(R.id.gv_mypage);
		mGridview.setExpanded(true);
		mMyPageAdapter = new MyPageAdapter(mMainActivity, mMyPageItemArrayList);
		mGridview.setAdapter(mMyPageAdapter);
		
		mHouseProfile = (ImageView) v.findViewById(R.id.house_profile);
		mBtnSetting = (ImageView) v.findViewById(R.id.btn_setting);
		mBtnScrap = (TextView) v.findViewById(R.id.btn_scrap);
		mBtnUpload = (TextView) v.findViewById(R.id.btn_upload);
		
		//	Settings
		SessionManager sessionManager = SessionManager.getInstance(mMainActivity);

		HashMap<String, String> userDetails = sessionManager.getUserDetails();
		String imgSrc = userDetails.get(SessionManager.KEY_PROFILE_PATH);
		if(imgSrc != null){
			new DownloadImageTask(mHouseProfile).setCrop(true).execute(imgSrc);
//			ImageManagingHelper.getCroppedBitmap(mImageBitmap, mIvPhotoWidth);
			
			mHouseProfile.setVisibility(View.VISIBLE);
			Log.d("mypageurl", "mypageurl" + imgSrc);
		}
		//원래 기본사진크기 가져오려고했는데..안됨 우선은 숫자로 넣어둠.
//		mIvPhotoWidth = 200;
	}

	public void initEvent(){
		mGridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent=new Intent(mMainActivity,ContentDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mMainActivity.startActivity(intent);
			}
		});
		mHouseProfile.setOnClickListener(this);
		mBtnSetting.setOnClickListener(this);
		mBtnScrap.setOnClickListener(this);
		mBtnUpload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_setting:
			Intent intent = new Intent(mMainActivity, SetActivity.class);
			startActivity(intent);
			break;
		case R.id.house_profile:
			dispatchPickFromGalleryIntent();
			break;
		case R.id.btn_scrap:
			mCodeType = CodeType.INTERIOR_TYPE;
			mPoType = AP0001.SCRAP;
			initMyPage();
			break;
		case R.id.btn_upload:
			mCodeType = CodeType.INTERIOR_TYPE;
			mPoType = AP0001.NORMAL;
			initMyPage();
			break;
		}
	}
	
	@Override
	public void onResume() {
		initMyPage();
		super.onResume();
	}

	public void initMyPage(){
		if(mMyPageTask != null && mMyPageTask.getStatus() != mMyPageTask.getStatus().FINISHED)
			return ;
		mMyPageAdapter.clear();
		mGridview.setSelection(0);
		addMyPageList(0);
	}
	
	public void addMyPageList(long no){
		AP0001 ap = new AP0001();
		ap.setType(mCodeType);
		ap.setOrderType("new");
		ap.setReqPo(0);
		ap.setReqPoCnt(3);
		ap.setReqPoNo(no);
		ap.setReqPoType(mPoType);
		ap.setUsrId(SessionManager.getInstance(mMainActivity).getUserDetails().get(SessionManager.KEY_EMAIL));
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				List<AP0001> apList = resCode.getTranData();
				AP0001 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0001.class);
				long lastNo = 0;
				
				for(int i=0; i<ap.getResCnt(); i++){
					AP0001Res res = ap.getRes().get(i);
					String imgUrl = null;
					if(res.brdImg.size() > 0)
						imgUrl = mMainActivity.getString(R.string.base_uri) + res.brdImg.get(0).brdOriginImg;
					lastNo = res.brdNo;
					mMyPageAdapter.add(res.brdNo, mCodeType, res.brdId, res.brdNm, res.brdCateNm, imgUrl);
				}
//				Log.d("resCnt", "resCnt : " + ap.getResCnt());
				if(ap.getResCnt() == 0)
					mMyPageAdapter.notifyDataSetChanged();
				else 
					addMyPageList(lastNo);
			}
		};
		TransHandler<AP0001> articleHandler = new TransHandler<AP0001>("AP0001", handler, ap);
		mMyPageTask = new PostMessageTask(mMainActivity, articleHandler);
		mMyPageTask.execute(MediaType.APPLICATION_JSON);
	}
	
	private void dispatchPickFromGalleryIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, REQUEST_PICK_FROM_GALLERY);
	}

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
//				Log.d("REQUEST_PICK_FROM_GALLERY", "REQUEST_PICK_FROM_GALLERY : " + TempFileManager.getImageFileUri());
				startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
			}
			break;
		case REQUEST_CODE_CROP_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					mImageBitmap = extras.getParcelable("data");
					if (mImageBitmap != null) {
//						mHouseProfile.setImageBitmap(ImageManagingHelper.getCroppedBitmap(mImageBitmap, mIvPhotoWidth));
						File file = TempFileManager.saveBitmapToImageFile(mImageBitmap);
//						Log.d("File : ", "File : " + file.getAbsolutePath());
						saveProfile(file, mImageBitmap);
					} else {
						Toast.makeText(mMainActivity, "잘린 이미지가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mMainActivity, "잘린 이미지가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	public void refreshUser(){
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				CM0005 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0005.class);
				String profileImg = cm.getProfileImg();
				SessionManager.getInstance(mMainActivity).putString(SessionManager.KEY_PROFILE_PATH, mMainActivity.getString(R.string.base_uri) + profileImg);
				String imgSrc = SessionManager.getInstance(mMainActivity).getUserDetails().get(SessionManager.KEY_PROFILE_PATH);
				if(imgSrc != null){
					new DownloadImageTask(mHouseProfile).setCrop(true).execute(imgSrc);
					mHouseProfile.setVisibility(View.VISIBLE);
				}
				Log.d("SessionManager ", "SessionManager : " + profileImg);
			}
		}; 
	  	CM0005 cm = new CM0005();
		cm.setUsrId(SessionManager.getInstance(mMainActivity).getUserDetails().get(SessionManager.KEY_EMAIL));
		
    	TransHandler<CM0005> authHandler = new TransHandler<CM0005>("CM0005", handler, cm);
    	mUserTask = new PostMessageTask(mMainActivity, authHandler);
    	mUserTask.execute(MediaType.APPLICATION_JSON); 
	}
	
	public void saveProfile(File file, Bitmap bitmap){
		if(mUserTask != null && mUserTask.getStatus() != mUserTask.getStatus().FINISHED)
			return ;
		// 크롭을 하기 위해 file 이미지 불러오기 !!
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				CM0006 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0006.class);
				
				if(cm.getResultYn().equals("Y")){
					refreshUser();
				}
			}
		}; 
	  	CM0006 cm = new CM0006();
		CM0006Img profileImg = new CM0006Img();
//		Log.e("img", "imgs : " + imgUrl);
		String path = file.getAbsolutePath();
		String name = path.substring(path.lastIndexOf('/') + 1);
		final String type = path.substring(path.lastIndexOf('.') + 1);
		byte[] contents = ImageManagingHelper.getBitmapToBytes(bitmap, type);
		long size = file.length();
		
		profileImg.imgContent = contents;
		profileImg.imgNm = profileImg.imgOriginNm = name;
		profileImg.imgSize = size;
		profileImg.imgType = type;
		cm.setUsrImg(profileImg);
		cm.setUsrId(SessionManager.getInstance(mMainActivity).getUserDetails().get(SessionManager.KEY_EMAIL));
		
    	TransHandler<CM0006> authHandler = new TransHandler<CM0006>("CM0006", handler, cm);
    	mUserTask = new PostMessageTask(mMainActivity, authHandler);
    	mUserTask.execute(MediaType.APPLICATION_JSON); 
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		TempFileManager.deleteImageFile();
	}

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
	
	//    private void dispatchTakePictureIntent() {
	//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	//        intent.putExtra(MediaStore.EXTRA_OUTPUT, TempFileManager.getImageFileUri());
	//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
	//    }
}