package com.nexters.house.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.activity.CustomGalleryActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.DataObject;
import com.nexters.house.entity.reqcode.AP0006;
import com.nexters.house.entity.reqcode.AP0006.AP0006Img;
import com.nexters.house.utils.ImageManagingHelper;

public class ArticleHandler<T> extends AbstractHandler<T> {
	public final static int WRITE_INTERIOR = 0;
	public final static int WRITE_SUDATALK = 1;
	public final static int LIST_INTERIOR = 2;
	public final static int LIST_SUDATALK = 3;
	public final static int READ_INTERIOR = 4;
	public final static int READ_SUDATALK = 5;
	public final static int DELETE_INTERIOR = 6;
	public final static int DELETE_SUDATALK = 7;
	public final static int LIKE_CNT = 8;
	public final static int SCRAP_CNT = 9;
	public final static int WRITE_REPLY = 10;
	public final static int DELETE_REPLY = 11;
	public final static int LIST_REPLY = 12;
	
	private Handler mHandler = null;

	public ArticleHandler(AbstractAsyncActivity abstractAsyncActivity,
			String tranCd) {
		super(abstractAsyncActivity, tranCd);
	}

	public ArticleHandler(
			AbstractAsyncFragmentActivity abstractAsyncFragmentActivity,
			String tranCd) {
		super(abstractAsyncFragmentActivity, tranCd);
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	@Override
	public void handle(int method) {
		switch (method) {
		case WRITE_INTERIOR:
		case WRITE_SUDATALK:
			writeArticle(method);
			break;
		default:
			processHandler();
			break;
		}
	}

	public void processHandler() {
		mHandler.handle(resCode);
	}

	public void writeArticle(int type) {
		if (WRITE_INTERIOR == type) {
			CustomGalleryActivity.noCancel = 0;
			mAbstractAsyncActivity.setResult(mAbstractAsyncActivity.RESULT_OK);
			mAbstractAsyncActivity.finish();
			mAbstractAsyncActivity.showResult("작성한 내용이 업로드됩니다.");
		} else if (WRITE_SUDATALK == type) {

		}
	}

	@Override
	public void showError() {
		if (mAbstractAsyncActivity != null)
			mAbstractAsyncActivity.showResult("Fail");
		else if (mAbstractAsyncFragmentActivity != null)
			mAbstractAsyncFragmentActivity.showResult("Fail!!");
	}
	
	public static AP0006 returnAP0006(Context context, int codeType, int cateNo, String subject, byte[] content, String tag, List<DataObject> dataObjects){
		AP0006 ap = new AP0006();
		ap.setType(codeType);
		ap.setBrdId(SessionManager.getInstance(context).getUserDetails().get(SessionManager.KEY_EMAIL));
		ap.setBrdSubject(subject);
		ap.setBrdContents(content);
		ap.setBrdTag(tag);
		ap.setBrdCateNo(cateNo);
		
		ArrayList<AP0006Img> imgs = new ArrayList<AP0006Img>();
		for(DataObject dataObject : dataObjects){
			AP0006Img img = new AP0006Img();
			String path = dataObject.getName();
			String name = path.substring(path.lastIndexOf('/') + 1);
			String type = path.substring(path.lastIndexOf('.') + 1);
			File file = new File(path);
			byte[] contents = ImageManagingHelper.getImageToBytes(file);
			long size = file.length();
			
			img.imgNm = img.imgOriginNm = name;
			img.imgSize = size; 
			img.imgType = type;
			img.imgContent = contents;
//			Log.d("dataObject", "dataObject : " + contents.length + " - " + size);
			imgs.add(img);
		}
		ap.setBrdImg(imgs);
		return ap;
	}
}
