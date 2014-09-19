package com.nexters.house.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ViewSwitcher;

import com.jess.ui.TwoWayGridView;
import com.nexters.house.R;
import com.nexters.house.adapter.GalleryAdapter;
import com.nexters.house.adapter.HorzGridViewAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.Action;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.DataObject;
import com.nexters.house.entity.reqcode.AP0006;
import com.nexters.house.entity.reqcode.AP0010;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.ImageManagingHelper;
import com.nexters.house.utils.JacksonUtils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class TalkWriteActivity extends AbstractAsyncActivity {
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	public static TwoWayGridView mTalkHorzGridView;

	private Context mContext;
	private EditText mTalkContent;
	private EditText mTalkSubject;
	private RadioGroup mTalkCategory;
	
	private GalleryAdapter mGalleryAdapter;
	private HorzGridViewAdapter mHorzGridViewAdapter;

	private ViewSwitcher mViewSwitcher;
	private ImageLoader mImageLoader;

	private Button btnGalleryPick;

	private PostMessageTask mArticleTask;
	
	List<DataObject> imgDatas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_talk_write);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initImageLoader();
		initResource();
		initEvent();
	}

	private void initResource() {
		
		mTalkContent = (EditText) findViewById(R.id.talk_content);
		mTalkSubject = (EditText) findViewById(R.id.talk_subject);
		mTalkCategory = (RadioGroup) findViewById(R.id.talk_category);
		
		mGalleryAdapter = new GalleryAdapter(getApplicationContext(),
				mImageLoader);
		mGalleryAdapter.setMultiplePick(false);
		GalleryAdapter.clear(); // 버튼 누를때마다 리스트 초기화 시켜줭 + 숫자도 초기화

		mTalkHorzGridView = (TwoWayGridView) findViewById(R.id.horz_gridview);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_talk);
		btnGalleryPick = (Button) findViewById(R.id.btn_gallery);

		mContext = getApplicationContext();
		List<DataObject> horzData = new ArrayList<DataObject>();
		mHorzGridViewAdapter = new HorzGridViewAdapter(mContext, horzData,
				mTalkHorzGridView,mImageLoader);
		mTalkHorzGridView.setAdapter(mHorzGridViewAdapter);
		
	}

	private void initEvent() {
		btnGalleryPick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomGalleryActivity.noCancel=1;
				Intent multiplePickIntent = new Intent(
						Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(multiplePickIntent, 200);
			}
		});
	}

	public void completeTalkWrite(View view) {
		if(mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		AP0006 ap = new AP0006();
		ap.setType(CodeType.SUDATALK_TYPE);
		ap.setBrdId(SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL));
		ap.setBrdSubject(mTalkSubject.getText().toString());
		ap.setBrdContents(mTalkContent.getText().toString().getBytes());
		ap.setBrdTag("");
		ap.setBrdCateNo(convertCategory(mTalkCategory.getCheckedRadioButtonId()));
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				AP0006 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0006.class);
				
				if(ap.getResultYn().equals("Y")){
					imgDatas = generateGridViewObjects();
					long brdNo = ap.getBrdNo();
					uploadImage(brdNo, 0);
				}
			}
		};
		// Post Aricle 
    	TransHandler<AP0006> articleHandler = new TransHandler<AP0006>("AP0006", handler, ap);
		mArticleTask = new PostMessageTask(this, articleHandler);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}
	
	
	public void uploadImage(final long brdNo, final int index){
		if(imgDatas == null || imgDatas.size() <= 0){
			CustomGalleryActivity.noCancel = 0;
		    setResult(RESULT_OK);
			showResult("작성한 내용이 업로드됩니다.");
			finish();
			return ;
		}
		
		AP0010 ap = new AP0010();
		ap.setType(CodeType.SUDATALK_TYPE);
		ap.setBrdId(SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL));
		ap.setBrdNo(brdNo);
		
		String path = imgDatas.get(index).getName();
		String name = path.substring(path.lastIndexOf('/') + 1);
		String type = path.substring(path.lastIndexOf('.') + 1);
		File file = new File(path);
		byte[] contents = ImageManagingHelper.getImageToBytes(file);
		long size = file.length();
		
		ap.setImgNm(name);
		ap.setImgOriginNm(name);
		ap.setImgType(type);
		ap.setImgContent(contents);
		ap.setImgSize(size);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				AP0010 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0010.class);
				
				if(imgDatas.size() == index){
					CustomGalleryActivity.noCancel = 0;
				    setResult(RESULT_OK);
					showResult("작성한 내용이 업로드됩니다.");
					finish();
				} else {
					uploadImage(brdNo, index + 1);
				}
			}
		};
		// Post Aricle 
    	TransHandler<AP0010> articleHandler = new TransHandler<AP0010>("AP0010", handler, ap);
		mArticleTask = new PostMessageTask(this, articleHandler);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public int convertCategory(int btnId){
		switch (btnId) {
		case R.id.radio_qa :
			return 1;
		case R.id.radio_chat :
			return 2;
		case R.id.radio_review :
			return 3;
		}
		return 0;
	}

	public void clickedCancel_talk(View view){
		onBackPressed();
	}
	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshHorzGrid();
	}

	public void refreshHorzGrid() {
		// 이미지 하나도 선택 안할 경우 null 아닐 경우 그 밖
		List<DataObject> horzData = generateGridViewObjects();

		if (horzData == null) {
			mViewSwitcher.setDisplayedChild(1);
		} else {
			mHorzGridViewAdapter.setHorzData(horzData);
			mHorzGridViewAdapter.notifyDataSetChanged();
			mViewSwitcher.setDisplayedChild(0);
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("입력을 취소하시겠습니까 ?")
				.setCancelable(false)
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						CustomGalleryActivity.noCancel=0;
						finish();
					}
				})
				.setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'NO' Button
								dialog.cancel();
							}
						});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		// alert.setTitle("Title");
		// Icon for AlertDialog
		// alert.setIcon(R.drawable.icon);
		alert.show();
	}

	private List<DataObject> generateGridViewObjects() {
		List<DataObject> allData = new ArrayList<DataObject>();
		String path;
		// Log.d("GalleryAdapter.customGalleriesChecked.size()",
		// "GalleryAdapter.customGalleriesChecked.size() : " +
		// GalleryAdapter.customGalleriesChecked.size());
		if (GalleryAdapter.customGalleriesChecked.size() <= 0)
			return null;
		for (int i = 0; i < GalleryAdapter.customGalleriesChecked.size(); i++) {
			path = GalleryAdapter.customGalleriesChecked.get(i).sdcardPath;
			DataObject singleObject = new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}
}
