package com.nexters.house.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import com.nexters.house.R;
import com.nexters.house.R.layout;
import com.nexters.house.R.menu;
import com.nexters.house.adapter.ContentImageAdapter;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.DataObject;
import com.nexters.house.entity.ReplyEntity;
import com.nexters.house.entity.reqcode.AP0003;
import com.nexters.house.entity.reqcode.AP0006;
import com.nexters.house.entity.reqcode.AP0003.AP0003Comment;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.DownloadImageTask;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.ImageManagingHelper;
import com.nexters.house.utils.JacksonUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AbstractAsyncActivity implements View.OnClickListener {
	// Article
	private TextView mContent;
	private TextView mHouseInfo;
	
	private Button mBtnInterior;
	
	private PostMessageTask mPostTask;
	private PostMessageTask mArticleTask;
	
	private long brdNo;
	private int brdType;
	private String usrId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interior_write2);
		
		initResource();
		initEvent();
	}

	private void initResource() {
		brdNo = getIntent().getLongExtra("brdNo", 0);
		brdType = CodeType.INTERIOR_TYPE;
		usrId = SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL);
		
		Button btnGallery=(Button)findViewById(R.id.btn_gallery);
		TextView txtNoPhoto=(TextView)findViewById(R.id.txt_editPhoto);
		
		mBtnInterior = (Button) findViewById(R.id.btn_interior);
		mContent = (TextView) findViewById(R.id.interior_content);
		mHouseInfo = (TextView) findViewById(R.id.interior_info);
		
		// init
		btnGallery.setVisibility(View.GONE);
		txtNoPhoto.setVisibility(View.VISIBLE);
	}
	
	private void initEvent(){
		mBtnInterior.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		setContents(brdNo);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		completeWrite();
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
	
	public void completeWrite() {
		if(mArticleTask != null && mArticleTask.getStatus() != mArticleTask.getStatus().FINISHED)
			return ;
		
		AP0006 ap = new AP0006();
		ap.setBrdNo(brdNo);
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setBrdId(SessionManager.getInstance(this).getUserDetails().get(SessionManager.KEY_EMAIL));
		ap.setBrdSubject("");
		ap.setBrdContents(mContent.getText().toString().getBytes());
		ap.setBrdTag(mHouseInfo.getText().toString());
		ap.setBrdCateNo(0);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				CustomGalleryActivity.noCancel = 0;
				setResult(RESULT_OK);
				finish();
				showResult("작성한 내용이 업로드됩니다.");
			}
		};
		// Post Aricle 
    	TransHandler<AP0006> articleHandler = new TransHandler<AP0006>("AP0006", handler, ap);
		mArticleTask = new PostMessageTask(this, articleHandler);
		mArticleTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public void setContents(long brdNo) {
		if(mPostTask != null && mPostTask.getStatus() != mPostTask.getStatus().FINISHED)
			return ;
		showLoadingProgressDialog();
		
		AP0003 ap = new AP0003();
		ap.setType(brdType);
		ap.setReqPoNo(brdNo);
		ap.setUsrId(usrId);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0003 ap = JacksonUtils.hashMapToObject((HashMap) resCode
						.getTranData().get(0), AP0003.class);
				mContent.setText(new String(ap.getBrdContents()));
				mHouseInfo.setText(ap.getBrdTag());
				dismissProgressDialog();
			}
		};
		TransHandler<AP0003> articleHandler = new TransHandler<AP0003>("AP0003", handler, ap);
		mPostTask = new PostMessageTask(this, articleHandler);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
}
