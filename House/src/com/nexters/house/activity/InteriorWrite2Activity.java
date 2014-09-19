package com.nexters.house.activity;

import java.io.File;
import java.io.IOException;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.nexters.house.entity.reqcode.*;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.CommonTask;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.ImageManagingHelper;
import com.nexters.house.utils.JacksonUtils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

public class InteriorWrite2Activity extends AbstractAsyncActivity implements View.OnClickListener {
	public final static int COLUMN_PORT = 0;
	public final static int COLUMN_LAND = 1;
	public static int column_selected;
	public static int[] displayWidth;
	public static int[] displayHeight;
	
	public static TwoWayGridView mInteriorGridView2;

	private static String savedContent = "";
	private static String savedInfo = "";
	
	private Context mContext;
	private EditText mInteriorContent;
	private EditText mInteriorInfo;

	private HorzGridViewAdapter mHorzGridViewAdapter;

	private ViewSwitcher mViewSwitcher;
	private ImageLoader mImageLoader;

	private Button btnGalleryPick;
	private Button mBtnComplete;
	
	private PostMessageTask mArticleTask;
	
	List<DataObject> imgDatas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interior_write2);
		savedContent="";
		savedInfo="";
		// 처음에 포커스 없애기
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initImageLoader();
		initResource();
		initEvent();
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
	private void initResource() {
		// 입력한 정보 끌어오기
		mInteriorInfo = (EditText) findViewById(R.id.interior_info); // 인테리어정보
		mInteriorContent = (EditText) findViewById(R.id.interior_content); // 이야기
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher_interior2);
		mInteriorGridView2 = (TwoWayGridView) findViewById(R.id.interior_gridview_2);
		btnGalleryPick = (Button) findViewById(R.id.btn_gallery);
		mBtnComplete = (Button) findViewById(R.id.btn_interior);
		
		mContext = getApplicationContext();
		List<DataObject> horzData = new ArrayList<DataObject>();
		mHorzGridViewAdapter = new HorzGridViewAdapter(mContext, horzData, mInteriorGridView2,mImageLoader);
		mInteriorGridView2.setAdapter(mHorzGridViewAdapter);
		
		mInteriorContent.setText(savedContent);
		mInteriorInfo.setText(savedInfo);
	}

	private void initEvent(){
		mBtnComplete.setOnClickListener(this);
		btnGalleryPick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				savedContent = mInteriorContent.getText().toString(); // 저장해놓고
				savedInfo = mInteriorInfo.getText().toString();
				// onBackPressed();
				CustomGalleryActivity.noCancel=1;
				Intent multiplePickIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(multiplePickIntent, 200);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_interior:	
			completeWrite();
			break;
		}
	}

	public void completeWrite() {
		if(mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		AP0006 ap = new AP0006();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setBrdId(SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL));
		ap.setBrdSubject("");
		ap.setBrdContents(mInteriorContent.getText().toString().getBytes());
		ap.setBrdTag(mInteriorInfo.getText().toString());
		ap.setBrdCateNo(0);
		
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
		
		final AP0010 ap = new AP0010();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setBrdId(SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL));
		ap.setBrdNo(brdNo);
		
		String path = imgDatas.get(index).getName();
		String name = path.substring(path.lastIndexOf('/') + 1);
		final String type = path.substring(path.lastIndexOf('.') + 1);
		final File file = new File(path);
		byte[] contents = null;
		long size = file.length();
		
		ap.setImgNm(name);
		ap.setImgOriginNm(name);
		ap.setImgType(type);
		ap.setImgContent(contents);
		ap.setImgSize(size);
		
		
		CommonTask.Handler commonHandler = new CommonTask.Handler(){
			@Override
			public void handle() {
				try {
					ap.setImgContent(ImageManagingHelper.getBitmapToBytes(Picasso.with(getApplicationContext()).load(file).resize(300, 300).get(), type));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		CommonTask.PostHandler postHandler = new CommonTask.PostHandler() {
			@Override
			public void handle() {
				TransHandler.Handler handler = new TransHandler.Handler() {
					@Override
					public void handle(APICode resCode) {
						AP0010 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0010.class);
						Log.d("imgDatas", "imageDatas : " + imgDatas.size());
						if(imgDatas.size() == index + 1){
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
				mArticleTask = new PostMessageTask(InteriorWrite2Activity.this, articleHandler);
				mArticleTask.execute(MediaType.APPLICATION_JSON);
			}
		};
		CommonTask commonTask = new CommonTask(null, commonHandler, postHandler);
		commonTask.execute();
	}
	
	public void clickedCancel(View view){
		onBackPressed();
	}

	@Override
	public void onBackPressed(){
		 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage("입력을 취소하시겠습니까?").setCancelable(
		        false).setPositiveButton("예",
		        new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		        	CustomGalleryActivity.noCancel=0;
		            finish();
		        }
		        }).setNegativeButton("아니요",
		        new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
		            // Action for 'NO' Button
		            dialog.cancel();
		        }
		        });
		    AlertDialog alert = alt_bld.create();
		    // Title for AlertDialog
		    alert.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshHorzGrid();
	}

	public void refreshHorzGrid(){
		// 이미지 하나도 선택 안할 경우 null 아닐 경우 그 밖
		List<DataObject> horzData = generateGridViewObjects();
		if(horzData == null){
			mViewSwitcher.setDisplayedChild(1);
		} else {
			mHorzGridViewAdapter.setHorzData(horzData);
			mHorzGridViewAdapter.notifyDataSetChanged();
			mViewSwitcher.setDisplayedChild(0);
		}
	}
	
	private List<DataObject> generateGridViewObjects() {
		List<DataObject> allData = new ArrayList<DataObject>();
		String path;

		if(GalleryAdapter.customGalleriesChecked.size() <= 0)
			return null;
		for (int i = 0; i < GalleryAdapter.customGalleriesChecked.size(); i++) {
			path = GalleryAdapter.customGalleriesChecked.get(i).sdcardPath;
			DataObject singleObject = new DataObject(path);
			allData.add(singleObject);
		}
		return allData;
	}
}
