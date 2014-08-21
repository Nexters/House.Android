package com.nexters.house.thread;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.util.Log;

import com.nexters.house.R;
import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.handler.AbstractHandler;
import com.nexters.house.utils.BeanUtils;

// ***************************************
// Private classes
// ***************************************
public class PostMessageTask extends AsyncTask<MediaType, Void, Integer> {
	public static int POST_SUCCESS = 1;
	public static int POST_FAIL = 0;
	
	private AbstractAsyncActivity mAbstractAsyncActivity;
    private AbstractHandler mAbstractHandler;
    private int mHandlerType;
    
    public PostMessageTask(AbstractAsyncActivity abstractAsyncActivity, AbstractHandler abstractHandler, int handlerType) {
    	mAbstractAsyncActivity = abstractAsyncActivity;
    	mAbstractHandler = abstractHandler;
    	mHandlerType = handlerType;
    }
    
    @Override
    protected void onPreExecute() {
    	mAbstractAsyncActivity.showLoadingProgressDialog();
    }

    @Override
    protected Integer doInBackground(MediaType... params) {
    	 
        try {
            if (params.length <= 0) {
                return null;
            }
            MediaType mediaType = params[0];

            // The URL for making the POST request
            final String url = mAbstractAsyncActivity.getString(R.string.base_uri) + "/house/{code}.app?token={token}";

            HttpHeaders requestHeaders = new HttpHeaders();
            	
            // Set Token	
            String token = SessionManager.getInstance(mAbstractAsyncActivity.getApplicationContext()).getUserDetails().get(SessionManager.KEY_TOKEN);
            
            // Sending a JSON or XML object i.e. "application/json" or "application/xml"
            requestHeaders.setContentType(mediaType);

            // Populate the Message object to serialize and headers in an
            // HttpEntity object to use for the request mAbstractHandler
            HttpEntity<APICode> requestEntity = new HttpEntity<APICode>(mAbstractHandler.getReqCode(), requestHeaders);
            
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            if (mediaType == MediaType.APPLICATION_JSON) {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } else if (mediaType == MediaType.APPLICATION_XML) {
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
            }

            // Make the network request, posting the message, expecting a String in response from the server
            ResponseEntity<APICode> response = null;
//            Log.d("request : ", "request : " + JacksonUtils.objectToJson(mAbstractHandler.getReqCode()));
        	response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
            		APICode.class, mAbstractHandler.getReqCode().getTranCd(), token);
//            Log.d("response : ", "response : " + JacksonUtils.objectToJson(response.getBody()));
            mAbstractHandler.setResCode(response.getBody());
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
    	mAbstractAsyncActivity.dismissProgressDialog();
        
    	if(POST_SUCCESS == result)
    		mAbstractHandler.handle(mHandlerType);
    	else if(POST_FAIL == result)
    		mAbstractHandler.showError();
    }
}