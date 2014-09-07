package com.nexters.house.thread;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.MediaType;

import android.R.interpolator;
import android.os.AsyncTask;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.handler.TransHandler;


// ***************************************
// Private classes
// ***************************************
public class CommonTask extends AsyncTask<Void, Void, Integer> {
	public static final int IGNORE = 2;
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	
	private PreHandler mPreHandler;
    private Handler mHandler;
    private PostHandler mPostHandler;
    
    private static AtomicBoolean isLoading;
    
    public CommonTask(PreHandler preHandler, Handler handler, PostHandler postHandler) {
    	mPreHandler = preHandler;
    	mHandler = handler;
    	mPostHandler = postHandler;
    	isLoading = new AtomicBoolean(false);
    }	
    
    
	@Override
	protected void onPreExecute() {
		if(mPreHandler != null)
			mPreHandler.handle();
	}

	@Override
	protected Integer doInBackground(Void... arg0) {
		if(!isLoading.get())
			isLoading.set(true);
		else 
			return FAIL;
		mHandler.handle();
		return SUCCESS;
	}

    @Override
    protected void onPostExecute(Integer result) {
    	isLoading.set(false);
    	if(mPostHandler != null)
    		mPostHandler.handle();
    }
    
    public interface PreHandler {
    	public void handle();
    }
    public interface PostHandler{
    	public void handle();
    }
    public interface Handler {
    	public void handle();
    }
}