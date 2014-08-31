package com.nexters.house.handler;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.activity.CustomGalleryActivity;

public class ArticleHandler<T> extends AbstractHandler<T> {
	public final static int WRITE_INTERIOR = 0;
	public final static int WRITE_SUDATALK = 1;
	public final static int LIST_INTERIOR = 2;
	public final static int LIST_SUDATALK = 3;
	public final static int READ_INTERIOR = 4;
	public final static int READ_SUDATALK = 5;
	public final static int LIKE_CNT = 6;
	public final static int SCRAP_CNT = 7;
	public final static int WRITE_REPLY = 8;

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
}
