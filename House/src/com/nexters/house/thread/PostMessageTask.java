package com.nexters.house.thread;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nexters.house.R;
import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.activity.AsyncActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.utils.JacksonUtils;

// ***************************************
// Private classes
// ***************************************
public class PostMessageTask extends AsyncTask<MediaType, Void, Integer> {
	public static final int POST_IGNORE = 2;
	public static final int POST_SUCCESS = 1;
	public static final int POST_FAIL = 0;
	
	private AsyncActivity mAbstractAsyncActivity;
    private TransHandler mTransHandler;
    private Context mContext;
    private static AtomicBoolean isLoading;
    
    public PostMessageTask(AbstractAsyncFragmentActivity abstractAsyncFragmentActivity, TransHandler transHandler) {
    	mAbstractAsyncActivity = abstractAsyncFragmentActivity;
    	mContext = abstractAsyncFragmentActivity.getApplicationContext();
    	mTransHandler = transHandler;
    	isLoading = new AtomicBoolean(false);
    }
    
    public PostMessageTask(AbstractAsyncActivity abstractAsyncActivity, TransHandler transHandler) {
    	mAbstractAsyncActivity = abstractAsyncActivity;
    	mContext = abstractAsyncActivity.getApplicationContext();
    	mTransHandler = transHandler;
    	isLoading = new AtomicBoolean(false);
    }
    
    @Override
    protected void onPreExecute() {
//    	Log.d("PostMessageTask", "PostMessageTask : " + JacksonUtils.objectToJson(mAbstractHandler.getReqCode()));
    }

    @Override
    protected Integer doInBackground(MediaType... params) {
        try {
            if (params.length <= 0) {
                return null;
            }
            MediaType mediaType = params[0];

            // The URL for making the POST request
             final String url = mContext.getString(R.string.base_uri) + "/house/{code}.app?token={token}";
//            final String url = mAbstractAsyncActivity.getString(R.string.base_uri) + "/house/CM0002.app";
            
            HttpHeaders requestHeaders = new HttpHeaders();
            // Set Token	
            String token = SessionManager.getInstance(mContext).getUserDetails().get(SessionManager.KEY_TOKEN);
            
            // Sending a JSON or XML object i.e. "application/json" or "application/xml"
            requestHeaders.setContentType(mediaType);

            // Populate the Message object to serialize and headers in an
            // HttpEntity object to use for the request mAbstractHandler
            HttpEntity<APICode> requestEntity = new HttpEntity<APICode>(mTransHandler.getReqCode(), requestHeaders);
            
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new PostRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());  
            
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            if (mediaType == MediaType.APPLICATION_JSON) {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } else if (mediaType == MediaType.APPLICATION_XML) {
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
            }
            // Make the network request, posting the message, expecting a String in response from the server
            ResponseEntity<APICode> response = null;
            Log.d("request : ", "request : " + JacksonUtils.objectToJson(mTransHandler.getReqCode()) + " token : " + token);
        	response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
            		APICode.class, mTransHandler.getReqCode().getTranCd(), token);
            Log.d("response : ", "response : " + JacksonUtils.objectToJson(response.getBody()));
//        	Log.d("response : ", "response : " + response.getStatusCode().toString());
        	mTransHandler.setResCode(response.getBody());
        	if(response.getBody().getErrorCd() != null)
        		return POST_FAIL;
            // Return the response body to display to the user
            return POST_SUCCESS;
        } catch (Exception e) {
        	e.printStackTrace();
            Log.e("POST_ERROR : ", "POST_ERROR : " + e.getMessage());
        } 
        return POST_FAIL;
    }

    @Override
    protected void onPostExecute(Integer result) {
    	if(POST_SUCCESS == result)
    		mTransHandler.handle();
    	else if(POST_FAIL == result){
    		if(mTransHandler.getResCode() == null){
    			SessionManager.getInstance(mContext).logoutUser();
    		} else if(mTransHandler.getResCode().getErrorCd() == null)
    			mAbstractAsyncActivity.showResult("Exception Error");
    		else
    			mAbstractAsyncActivity.showResult(mTransHandler.getResCode().getErrorMsg());
    	} else if(POST_IGNORE == result){}
    }
    
    public class PostRestTemplate extends RestTemplate {
        public PostRestTemplate() {
            if (getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
//                Log.d("HTTP", "HttpUrlConnection is used");
                ((SimpleClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(10 * 1000);
                ((SimpleClientHttpRequestFactory) getRequestFactory()).setReadTimeout(10 * 1000);
            } else if (getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
//                Log.d("HTTP", "HttpClient is used");
                ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setReadTimeout(10 * 1000);
                ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(10 * 1000);
            }
        }
    }
}